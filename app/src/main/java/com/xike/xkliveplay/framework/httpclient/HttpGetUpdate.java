package com.xike.xkliveplay.framework.httpclient;

import com.xike.xkliveplay.framework.entity.LiveUpgrageRequest;
import com.xike.xkliveplay.framework.entity.LiveUpgrageResponse;
import com.xike.xkliveplay.framework.format.XmlPara;
import com.xike.xkliveplay.framework.http.AsyncHttpRequest;
import com.xike.xkliveplay.framework.http.IRequestCallback;
import com.xike.xkliveplay.framework.http.IUpdateData;
import com.xike.xkliveplay.framework.tools.LogUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

/**
 * 
 * @author LiWei <br>
 * CreateTime: 2014年10月30日 下午1:46:31<br>
 * <b>Info:</b><br>
 *<br>
 *   <b>Method:</b> <br>
 */
public class HttpGetUpdate implements IRequestCallback{
	
	private static final String tag = "HttpGetUpdate";
	public static final String Method = "LivePlayUpdate";
	private static final String ROOTELEMENT = "LiveUpgrageResponse";
	private LiveUpgrageRequest req = null;
	private LiveUpgrageResponse res = null;
	private IUpdateData iUpdateData;
//	private String url = "http://cmup.snctv.cn/api/liveUpgrade2.action";
	
	
	
	public HttpGetUpdate(IUpdateData iUpdateData) {
		super();
		this.iUpdateData = iUpdateData;
	}
	
	
	public HttpGetUpdate(LiveUpgrageRequest req, IUpdateData iUpdateData) {
		super();
		this.req = req;
		this.iUpdateData = iUpdateData;
	}
	
	public void post()
	{
		if(req == null)
		{
			System.err.println("req is not null");
			iUpdateData.updateData(Method, null, null, false);
			return;
		}
		try {
			AsyncHttpRequest request = new AsyncHttpRequest();
			request.post(VarParam.update_url, Method, req, this);
		} catch (Exception e) {
			e.printStackTrace();
			iUpdateData.updateData(Method, null, null, false);
			return;
		}
	}



	@Override
	public void requestDidFinished(String method, String body) {
		LogUtil.e(tag, "requestDidFinished","返回的要解析的体是： " + body);
		if(!Method.equals(method))
		{
			iUpdateData.updateData(Method, null, null, false);
			return;
		}
		List<HashMap<String, String>> list = XmlPara.parser(ROOTELEMENT, body);
		if(list == null || list.size() == 0)
		{
			LogUtil.e(tag, "requestDidFinished","requestDidFinished XmlPara is null");
			iUpdateData.updateData(Method, null, null, false);
			return;
		}
		res = new LiveUpgrageResponse();
		HashMap<String, String> param;
		if (list.size() <= 0) 
		{
			iUpdateData.updateData(Method, null, null, false);
			return;
		}
    	param = list.get(0);
    	if (param.containsKey("resultCode")) 
    	{
			res.setResultCode(param.get("resultCode"));
		}
    	if (param.containsKey("operators")) 
    	{
			res.setOperators(param.get("operators"));
		}
    	if (param.containsKey("hard")) 
    	{
			res.setHard(param.get("hard"));
		}
    	if (param.containsKey("equipment")) 
    	{
			res.setEquipment(param.get("equipment"));
		}
    	if (param.containsKey("version")) 
    	{
			res.setVersion(param.get("version"));
		}
    	if (param.containsKey("newVersion")) 
    	{
			res.setNewVersion(param.get("newVersion"));
		}
    	if (param.containsKey("url")) 
    	{
			res.setUrl(param.get("url"));
		}
    	if (param.containsKey("isMust")) 
    	{
			res.setIsMust(param.get("isMust"));
		}
    	
//		jsonToBean(body);
		iUpdateData.updateData(Method, null, res, true);
	}
	@Override
	public void requestDidFailed(String method,String body) {
		// TODO Auto-generated method stub
		iUpdateData.updateData(Method, null, null, false);
	}
	
	public LiveUpgrageResponse jsonToBean(String result) 
	{
//		System.out.println("开始解析JSON");
		res = new LiveUpgrageResponse();
		try {  
//		    JSONTokener jsonParser = new JSONTokener(result);  
		    // 此时还未读取任何json文本，直接读取就是一个JSONObject对象。  
//		    JSONObject json = (JSONObject) jsonParser.
			JSONObject json = new JSONObject(result);
//			LogUtil.e("json是否为空：", json+"");
//			LogUtil.e("operators是否为空",json.isNull("operators") + "");
		    res.setOperators(json.getString("operators"));
			res.setHard(json.getString("hard"));
			res.setEquipment(json.getString("equipment"));
			res.setVersion(json.getString("version"));
			res.setNewVersion(json.getString("newVersion"));
			res.setUrl(json.getString("url"));
			LogUtil.e(tag,"解析完成的是：", res.toString()); 
		} catch (JSONException ex) {  
		    // 异常处理代码  
			ex.printStackTrace();
		}  
		return res;
	}
}
