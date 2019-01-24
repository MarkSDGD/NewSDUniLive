package com.xike.xkliveplay.framework.httpclient;

import com.xike.xkliveplay.framework.entity.Auth;
import com.xike.xkliveplay.framework.entity.AuthRes;
import com.xike.xkliveplay.framework.format.XmlPara;
import com.xike.xkliveplay.framework.http.AsyncHttpRequest;
import com.xike.xkliveplay.framework.http.IRequestCallback;
import com.xike.xkliveplay.framework.http.IUpdateData;
import com.xike.xkliveplay.framework.tools.LogUtil;

import java.util.HashMap;
import java.util.List;

/**
 * @author Liwei
 *
 */
public class HttpAuth implements IRequestCallback{

	private static final String tag = "HttpAuth";
	public static final String Method = "Auth";
	private static final String ROOTELEMENT = "AuthResult";
	private Auth req = null;
	private IUpdateData iUpdateData;
	private AuthRes res = null;
	
	public HttpAuth(IUpdateData updateData)
	{
		this.iUpdateData = updateData;
//		initTestData();
	}
	
	
	
	public HttpAuth(Auth req, IUpdateData iUpdateData) {
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
			request.post(VarParam.url, Method, req, this);
		} catch (Exception e) {
			e.printStackTrace();
			iUpdateData.updateData(Method, null, null, false);
			return;
		}
	}
	
	@Override
	public void requestDidFinished(String flag,String body) {
		// TODO Auto-generated method stub
		
		LogUtil.e(tag,"requestDidFinished","flag = " + flag + "用户认证B接口返回的主体是:" + body);
		
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
		if(list == null || list.size() == 0)
		{
			LogUtil.e(tag,"requestDidFinished", "requestDidFinished XmlPara is null");
			iUpdateData.updateData(Method, null, null, true);
			return;
		}
		
		res = new AuthRes();
		
		HashMap<String, String> param;
		if (list.size() <= 0) {
			iUpdateData.updateData(Method, null, null, true);
			return;
		}
    	param = list.get(0);
    	
    	if (param.containsKey("epgDomain")) 
    	{
			res.setEpgDomain(param.get("epgDomain"));
		}
    	if (param.containsKey("epgDomainBackup")) 
    	{
			res.setEpgDomainBackup(param.get("epgDomainBackup"));
		}
    	if (param.containsKey("upgradeDomain")) 
    	{
			res.setUpgradeDomain(param.get("upgradeDomain"));
		}
    	if (param.containsKey("upgradeDomainBackup")) 
    	{
			res.setUpgradeDomainBackup(param.get("upgradeDomainBackup"));
		}
    	if (param.containsKey("managementDomain")) 
    	{
			res.setManagementDomain(param.get("managementDomain"));
		}
    	if (param.containsKey("managementDomainBackup")) 
    	{
			res.setManagementDomainBackup(param.get("managementDomainBackup"));
		}
    	if (param.containsKey("ntpDomain")) 
    	{
			res.setNtpDomain(param.get("ntpDomain"));
		}
    	if (param.containsKey("ntpDomainBackup")) 
    	{
			res.setNtpDomainBackup(param.get("ntpDomainBackup"));
		}
    	if (param.containsKey("drmDomain")) 
    	{
			res.setDrmDomain(param.get("drmDomain"));
		}
    	if (param.containsKey("drmDomainBackup")) 
    	{
			res.setDrmDomainBackup(param.get("drmDomainBackup"));
		}
    	if (param.containsKey("userToken")) 
    	{
			res.setUserToken(param.get("userToken"));
		}
    	if (param.containsKey("epgGroupNMB")) 
    	{
			res.setEpgGroupNMB(param.get("epgGroupNMB"));
		}
    	if (param.containsKey("userGroupNMB")) 
    	{
			res.setUserGroupNMB(param.get("userGroupNMB"));
		}
    	LogUtil.e(tag, "requestDidFinished","解析完成后的B接口返回结果是:" + res.toSting());
    	iUpdateData.updateData(Method, null, res, true);
	}

	@Override
	public void requestDidFailed(String method,String body) {
		iUpdateData.updateData(Method, null, null, false);
	}
	
	public Auth getReq() {
		return req;
	}

	public void setReq(Auth req) {
		this.req = req;
	}

	public AuthRes getRes() {
		return res;
	}

	public void setRes(AuthRes res) {
		this.res = res;
	}
}
