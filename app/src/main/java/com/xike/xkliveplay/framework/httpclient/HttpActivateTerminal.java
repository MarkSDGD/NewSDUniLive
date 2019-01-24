package com.xike.xkliveplay.framework.httpclient;

import com.xike.xkliveplay.framework.entity.ActivateTerminal;
import com.xike.xkliveplay.framework.entity.ActivateTerminalRes;
import com.xike.xkliveplay.framework.format.XmlPara;
import com.xike.xkliveplay.framework.http.AsyncHttpRequest;
import com.xike.xkliveplay.framework.http.IRequestCallback;
import com.xike.xkliveplay.framework.http.IUpdateData;
import com.xike.xkliveplay.framework.tools.LogUtil;

import java.util.HashMap;
import java.util.List;

/**
 * 
 * @author LiWei <br>
 * CreateTime: 2014年10月30日 上午9:48:11<br>
 * <b>Info:</b><br>
 * 激活接口，用来完成用户换机操作，每次启动APK时可以调用，激活接口除访问失败时，总是返回成功
 *<br>
 *   <b>Method:</b> <br>
 */
public class HttpActivateTerminal implements IRequestCallback 
{

	private static final String tag = "HttpActivateTerminal";
	public static final String Method = "ActivateTerminal";
	private static final String ROOTELEMENT = "ActivateTerminal";
	private ActivateTerminal req = null;
	private IUpdateData iUpdateData;
	private ActivateTerminalRes res = null;
	
	public HttpActivateTerminal(IUpdateData updateData)
	{
		this.iUpdateData = updateData;
	}

	public HttpActivateTerminal(ActivateTerminal req, IUpdateData iUpdateData) {
		super();
		this.req = req;
		this.iUpdateData = iUpdateData;
	}

	public void post()
	{
		if(req == null)
		{
			LogUtil.e(tag, "post", "get request string is null");
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
		LogUtil.e(tag,"requestDidFinished","flag = " + flag + " return body is " + body);
		if(!Method.equals(flag))
		{
			iUpdateData.updateData(Method, null, null, false);
			return;
		}
		
		if (body.equals("unknownhost") || body.equals("sockettimeout") || body.equals("connectionrefused")) 
		{
			iUpdateData.updateData(Method, body, null, true);
			return;
		}
		
		List<HashMap<String, String>> list = XmlPara.parser(ROOTELEMENT, body);
		if(list == null || list.size() == 0)
		{
			LogUtil.e(tag, "requestDidFinished","get return string is null");
			iUpdateData.updateData(Method, null, null, true);
			return;
		}
		res = new ActivateTerminalRes();
		HashMap<String, String> param;
		if (list.size() <= 0) 
		{
			iUpdateData.updateData(Method, null, null, true);
			return;
		}
    	param = list.get(0);
    	if (param.containsKey("userId")) 
    	{
    		res.setUserId(param.get("userId"));
		}
    	if (param.containsKey("status")) 
		{
    		res.setStatus(param.get("status"));
		}
    	LogUtil.e(tag,"requestDidFinished", "after parser content is " + res.toString());
    	iUpdateData.updateData(Method, null, res, true);
	}

	@Override
	public void requestDidFailed(String method,String body)
	{
		iUpdateData.updateData(Method, null, null, false);
	}
}
