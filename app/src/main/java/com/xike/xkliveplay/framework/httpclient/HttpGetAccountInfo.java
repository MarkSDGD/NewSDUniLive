package com.xike.xkliveplay.framework.httpclient;

import android.content.Context;
import android.util.Log;

import com.xike.xkliveplay.framework.entity.GetAccountInfo;
import com.xike.xkliveplay.framework.entity.GetAccountInfoRes;
import com.xike.xkliveplay.framework.format.XmlPara;
import com.xike.xkliveplay.framework.http.AsyncHttpRequest;
import com.xike.xkliveplay.framework.http.IRequestCallback;
import com.xike.xkliveplay.framework.http.IUpdateData;
import com.xike.xkliveplay.framework.tools.LogUtil;

import java.util.HashMap;
import java.util.List;

/**
 * @author lw
 *
 */
public class HttpGetAccountInfo implements IRequestCallback{

	private static final String tag = "HttpGetAccountInfo";
	public static final String Method = "GetAccountInfo";
	private static final String ROOTELEMENT = "AccountInfo";
	private GetAccountInfo req = null;
	private IUpdateData iUpdateData;
	private GetAccountInfoRes res = null;
	
	public HttpGetAccountInfo(IUpdateData updateData)
	{
		this.iUpdateData = updateData;
	}

	public HttpGetAccountInfo(GetAccountInfo req, IUpdateData iUpdateData) {
		super();
		this.req = req;
		this.iUpdateData = iUpdateData;
	}

	public void post(Context context)
	{
		if(req == null)
		{
			LogUtil.e(tag, "post","获取用户信息请求串为空");
			iUpdateData.updateData(Method, null, null, false);
			return;
		}
		try 
		{
			AsyncHttpRequest request = new AsyncHttpRequest();
			request.post(VarParam.url, Method, req, this);
		} catch (Exception e) 
		{
			e.printStackTrace();
			iUpdateData.updateData(Method, null, null, false);
			return;
		}
	}
	
	@Override
	public void requestDidFinished(String flag,String body) 
	{
		LogUtil.e(tag,"requestDidFinished","flag = " + flag +  "获取用户信息返回串主体是:" + body);
		if(!Method.equals(flag))
		{
			iUpdateData.updateData(Method, null, null, false);
			return;
		}
		if (body.contains("NavServerResponse") && body.contains("500")) 
		{
			iUpdateData.updateData(Method, "500", null, true);
			return;
		}
		if (body.contains("sockettimeout")
				||body.contains("unknownhost")
				||body.contains("connectionrefused")
				||body.contains("error")) 
		{
			iUpdateData.updateData(Method, body, null, true);
			return;
		}
		List<HashMap<String, String>> list = XmlPara.parser(ROOTELEMENT, body);
		if(list == null || list.size() == 0)
		{
			LogUtil.e(tag, "requestDidFinished","获取用户信息返回串为空");
			iUpdateData.updateData(Method, null, null, true);
			return;
		}
		res = new GetAccountInfoRes();
		HashMap<String, String> param;
		if (list.size() <= 0) 
		{
			iUpdateData.updateData(Method, null, null, true);
			return;
		}
		Log.e(tag, "返回的用户信息列表大小是：" + list.size() + " ，内容是：" + list.toString());
    	param = list.get(0);
    	
    	if (param.containsKey("account")) 
    	{
			res.setAccount(param.get("account"));
		}
    	
    	if (param.containsKey("password")) 
		{
			res.setPassword(param.get("password"));
		}
    	LogUtil.e(tag, "requestDidFinished","解析后的用户信息res = " + res.toString());
    	iUpdateData.updateData(Method, null, res, true);
	}

	@Override
	public void requestDidFailed(String method,String body)
	{
		iUpdateData.updateData(Method, null, null, false);
	}

}
