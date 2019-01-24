package com.xike.xkliveplay.framework.service;

import com.xike.xkliveplay.framework.httpclient.VarParam;

/**
 * @author LiWei <br>
 * CreateTime: 2014年12月18日 下午4:03:12<br>
 * <b>Info:</b><br>
 *<br>
 *   <b>Method:</b> <br>
 */
public class AuthenticationService extends AuthenticationServiceBase
{

	@Override
	public void onCreate() 
	{
		super.onCreate();
		if (VarParam.url == null || VarParam.url.equals("")) 
		{
			VarParam.url = VarParam.CM_URL;
		}
		if (VarParam.default_url == null || VarParam.url.equals("")) 
		{
			VarParam.default_url = VarParam.CM_DEFAULT_URL;
		}
	}
	
}
