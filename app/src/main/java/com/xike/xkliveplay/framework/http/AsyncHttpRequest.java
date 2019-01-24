package com.xike.xkliveplay.framework.http;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Base64;

import com.xike.xkliveplay.framework.httpclient.HttpActivateTerminal;
import com.xike.xkliveplay.framework.httpclient.HttpAuth;
import com.xike.xkliveplay.framework.httpclient.HttpAuthentication;
import com.xike.xkliveplay.framework.httpclient.HttpGetAccountInfo;
import com.xike.xkliveplay.framework.httpclient.HttpGetCategoryList;
import com.xike.xkliveplay.framework.httpclient.HttpGetContentList;
import com.xike.xkliveplay.framework.httpclient.VarParam;
import com.xike.xkliveplay.framework.tools.LogUtil;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;



/**
 *        @author zw
    1.异步任务的实例必须在UI线程中创建。
	2.execute(Params... params)方法必须在UI线程中调用。
    3.不要手动调用onPreExecute()，doInBackground(Params... params)，onProgressUpdate(Progress... values)，
      onPostExecute(Result result)这几个方法。
    4.不能在doInBackground(Params... params)中更改UI组件的信息。
    5.一个任务实例只能执行一次，如果执行第二次将会抛出异常。
 */
public final class AsyncHttpRequest extends AsyncTask<Void, Void, String> 
{
	private static final String tag = "AsyncHttpRequest";
	private String url = "";
	private String method = "";
	private Object oData = null;
	private IRequestCallback iCallback = null;
	private HttpPost httpPost;
	
	
	
	
	@SuppressLint("NewApi")
	public void post(String url,String method,Object object,IRequestCallback callback)
	{
		
		this.url = url;
		this.method = method;
		this.oData = object;
		this.iCallback = callback;
		
		this.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		
	}
	/**
	 *  在onPreExecute()完成后立即执行，用于执行较为费时的操作，此方法将接收输入参数和返回计算结果。
	 *  在执行过程中可以调用publishProgress(Progress... values)来更新进度信息
	 */
	@Override
	protected String doInBackground(Void... params) 
	{
		 @SuppressWarnings("unused")
		List<BasicNameValuePair> bParams  = null;
		 try {
			bParams = convertObjectToList(oData);
			//用xml的方式请求
			String response = sendXml(oData);
			return response;
		 	} catch (Exception e) 
		 	{
			/**据说失败了要求重新连接！**/
			if(httpPost != null)
			{
				httpPost.abort();
			}
//			e.printStackTrace();
			return dealWithException(e);
		}
	}
	


	/**
	 * 在execute(Params... params)被调用后立即执行，一般用来在执行后台任务前对UI做一些标记。
	 */
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}
	
	/**
	 * 在调用publishProgress(Progress... values)时，此方法被执行，直接将进度信息更新到UI组件上。
	 */
	@Override
	protected void onProgressUpdate(Void... values) {
		super.onProgressUpdate(values);
	}
	
	/**
	 * 当后台操作结束时，此方法将会被调用，计算结果将做为参数传递到此方法中，直接将结果显示到UI组件上。
	 */
	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		if(result != null)
		{
			iCallback.requestDidFinished(method,result);
		}else {
			iCallback.requestDidFailed(method,result);
		}
	}

	 /**
     * 转数据对象到ContentValues
     */
    private List<BasicNameValuePair> convertObjectToList(Object obj)
            throws Exception {
        if (obj == null) {
            return null;
        }
        
        List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
        
        Class<?> cls = obj.getClass();
        /* 所有私有属性 */
        Field[] fields = cls.getDeclaredFields();
        for (Field field : fields) 
        {
            field.setAccessible(true);// 设置私有属性可访问
            Class<?> type = field.getType();
            String key = field.getName();
            
            if (type == Integer.class || type==int.class){
                Object temp = field.get(obj);
                if (temp != null) {
                    Integer value = (Integer)temp;
                    params.add(new BasicNameValuePair(key, value.toString()));
                }
            } else if (type == String.class) {
                Object temp = field.get(obj);
                String value = temp == null ? "" : (String)temp;
                params.add(new BasicNameValuePair(key, value));
            } else if (type == Float.class) {
                Object temp = field.get(obj);
                if (temp != null) {
                    Float value = (Float)temp;
                    params.add(new BasicNameValuePair(key, value.toString()));
                }
            } else if (type == Double.class) {
                Object temp = field.get(obj);
                if (temp != null) {
                    Double value = (Double)temp;
                    params.add(new BasicNameValuePair(key, value.toString()));
                }
            } else if (type == Short.class) {
                Object temp = field.get(obj);
                if (temp != null) {
                    Short value = (Short)temp;
                    params.add(new BasicNameValuePair(key, value.toString()));
                }
            } else if (type == Boolean.class) {
                Object temp = field.get(obj);
                if (temp != null) {
                    Boolean value = (Boolean)temp;
                    params.add(new BasicNameValuePair(key, value.toString()));
                }
            } else if (type == Byte.class) {
                Object temp = field.get(obj);
                if (temp != null) {
                    Byte value = (Byte)temp;
                    params.add(new BasicNameValuePair(key, value.toString()));
                }
            }
        }

        return params;
    }
	
	@SuppressWarnings("unused")
	private void printUrl(String url,List<BasicNameValuePair> params)
	{
		if(params == null)
		{
			LogUtil.i(tag,"printUrl","url is:" + url);
			return;
		}
		StringBuffer sBuffer = new StringBuffer();
		sBuffer.append(url + "?");
		for(int i = 0; i < params.size();i++)
		{
			BasicNameValuePair bPair = (BasicNameValuePair)params.get(i);
			if(i < params.size() - 1)
			{
				sBuffer.append(bPair.getName() + "=" + bPair.getValue() + "&");
			}else {
				sBuffer.append(bPair.getName() + "=" + bPair.getValue());
			}
		}

		LogUtil.i(tag,"printUrl","url is:" + sBuffer.toString());
		
	}
	
	/**
	 * 
	 * 直播apk升级专区
	 * 
	 * **/
	
	private OutputStream outStream;
	
	/**发送xml到服务器比较**/
	private String sendXml(Object object) 
	{
		String xmlStr = null;
		try {
			if (method.equals("LivePlayUpdate")) 
			{
				xmlStr = TR069Util.BeanToXml(object);
			}else {
				xmlStr = TR069Util.BeanToXmlOther(object);
			}
		} catch (Exception e1) 
		{
//			e1.printStackTrace();
			return dealWithException(e1);
		}
		LogUtil.e(tag,"sendXml", url+method+" *** xml is " + xmlStr);
		URL url = null;
		try {
			if (method.equals("LivePlayUpdate")) 
			{
				url = new URL(VarParam.update_url);
			}else {
				url = new URL(VarParam.url+method);
			}
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5 * 1000);
			conn.setReadTimeout(5 * 1000);
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			
//			conn.setRequestProperty("mac", GetStbinfo.getMac());
			conn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
			conn.setRequestProperty("Content-Length", String.valueOf(xmlStr.length()));
			outStream = conn.getOutputStream();
			outStream.write(xmlStr.getBytes());
			
			if (conn.getResponseCode() == 200) 
			{
				InputStream responseStream = conn.getInputStream();
				String res = stream2String(responseStream);
				LogUtil.e(tag,"sendXml", "return content is:" + res);
				return res;
			}
			System.out.println("return net code = " + conn.getResponseCode());
		} catch (MalformedURLException e) 
		{
//			e.printStackTrace();
			return dealWithException(e);
		} catch (IOException e) 
		{
			return dealWithException(e);
		} finally{
			try {
				if (outStream != null) 
				{
					outStream.flush();
					outStream.close();
				}
			} catch (IOException e) {
				return dealWithException(e);
			}
		}
		return "";
	
	}
	
	@SuppressWarnings("unused")
	private String bean2JsonStr(List<BasicNameValuePair> bParams)
	{
		JSONObject jsonObject =  new JSONObject();
		for (BasicNameValuePair basicNameValuePair : bParams) {
			try 
			{
				jsonObject.put(basicNameValuePair.getName(), basicNameValuePair.getValue());
			} catch (JSONException e) 
			{
				return dealWithException(e);
			}
		}
		LogUtil.e(tag,"json is ", jsonObject.toString());
		return jsonObject.toString();
	}
	
	public  String decodeformBase64(String message) {
		if (message == null || message.length() <= 0) {
			return "";
		}
		byte[] bytes = Base64.decode(message, Base64.DEFAULT);
		return new String(bytes);
	}
	
	/**
	 * 流转化为字符串
	 */
	public String stream2String(InputStream is) throws IOException {
		StringBuilder sb = new StringBuilder();
		String line = null;
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(is));
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
			return sb.toString();
		} catch (FileNotFoundException ex) {
			throw ex;
		} catch (IOException ex) {
			throw ex;
		} finally {
			try {
				reader.close();
			} catch (Exception e) {
			}
		}
	}

	private String dealWithException(Exception e)
	{
		System.out.println("有异常发生：异常信息：");
		e.printStackTrace();
		if (method.equals(HttpGetAccountInfo.Method)
				|| method.equals(HttpActivateTerminal.Method)
				|| method.equals(HttpAuthentication.Method)
				|| method.equals(HttpAuth.Method)
				|| method.equals(HttpGetCategoryList.Method)
				|| method.equals(HttpGetContentList.Method)) 
		{
			if (e instanceof UnknownHostException) 
			{
				return "unknownhost";
			}else if (e instanceof SocketTimeoutException) 
			{
				return "sockettimeout";
			}else if (e.getMessage().contains("Connection refused")) 
			{
				return "connectionrefused";
			}
		}
		return "error";
	}
	
}
