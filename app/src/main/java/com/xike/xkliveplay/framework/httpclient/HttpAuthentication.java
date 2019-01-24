package com.xike.xkliveplay.framework.httpclient;

import com.xike.xkliveplay.framework.entity.Authentication;
import com.xike.xkliveplay.framework.entity.AuthenticationRes;
import com.xike.xkliveplay.framework.format.XmlPara;
import com.xike.xkliveplay.framework.http.AsyncHttpRequest;
import com.xike.xkliveplay.framework.http.IRequestCallback;
import com.xike.xkliveplay.framework.http.IUpdateData;
import com.xike.xkliveplay.framework.tools.LogUtil;

import java.util.HashMap;
import java.util.List;

/**
 * @author zw
 * 
 * 请求串：
    http://219.146.10.223/Authentication?action=Login&userId=testdx11
 * 响应串：
	  <?xml version="1.0" encoding="UTF-8" ?>
	<AuthenticationResult>
		<encryToken>2C7C750DA0B448A66EC499A4</encryToken>
	</AuthenticationResult>
 *
 */
public class HttpAuthentication implements IRequestCallback{

	private static final String tag = "HttpAuthentication";
	public static final String Method = "Authentication";
	private static final String ROOTELEMENT = "AuthenticationResult";
	private Authentication req = null;
	private AuthenticationRes res = null;
	private IUpdateData iUpdateData = null;
	
	
	public HttpAuthentication(IUpdateData updateData)
	{
		this.iUpdateData = updateData;
	}
	
	
	
	public HttpAuthentication(Authentication req, IUpdateData iUpdateData) 
	{
		super();
		this.req = req;
		this.iUpdateData = iUpdateData;
	}

	public void post()
	{
		if(req == null)
		{
			LogUtil.e(tag, "post","用户认证A接口请求串为空");
			iUpdateData.updateData(Method, null, null, false);
			return;
		}
		try {
			AsyncHttpRequest request = new AsyncHttpRequest();
			request.post(VarParam.url, Method, req, this);
		} catch (Exception e) {
			e.printStackTrace();
			iUpdateData.updateData(Method, null, null, false);
			return;
		}
	}
	
	@Override
	public void requestDidFinished(String flag,String body) {
		
		LogUtil.e(tag,"requestDidFinished" ,"flag = " + flag + "用户认证A接口返回的主体是:" + body);
		
		if(!Method.equals(flag))
		{
			iUpdateData.updateData(Method, null, null, false);
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
//		LogUtil.e(tag, msg)
		if(list == null)
		{
			LogUtil.e(tag, "requestDidFinished","用户认证A接口解析列表为空");
			iUpdateData.updateData(Method, null, null, false);
			return;
		}
		
		res = new AuthenticationRes();
		
		HashMap<String, String> param;
		for(int i = 0;i<list.size();i++)
	    {
	    	param = list.get(i);
	    	if(param.containsKey("encryToken"))
	    	{
	    		res.setEncryToken(param.get("encryToken"));
	    		LogUtil.e(tag, "requestDidFinished","res = " + param.get("encryToken"));
	    	}
	    }
		
		iUpdateData.updateData(Method, null, res, true);
		
		LogUtil.e(tag, "requestDidFinished","用户认证A接口解析结果为:" + res.toSting());
	}

	@Override
	public void requestDidFailed(String method,String body) {
		// TODO Auto-generated method stub
		iUpdateData.updateData(Method, null, null, false);
	}
	
	public Authentication getReq() {
		return req;
	}

	public void setReq(Authentication req) {
		this.req = req;
	}

	public AuthenticationRes getRes() {
		return res;
	}

	public void setRes(AuthenticationRes res) {
		this.res = res;
	}
}
