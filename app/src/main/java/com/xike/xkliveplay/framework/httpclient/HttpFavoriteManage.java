package com.xike.xkliveplay.framework.httpclient;

import com.xike.xkliveplay.framework.entity.FavoriteManage;
import com.xike.xkliveplay.framework.entity.FavoriteManageRes;
import com.xike.xkliveplay.framework.http.AsyncHttpRequest;
import com.xike.xkliveplay.framework.http.IRequestCallback;
import com.xike.xkliveplay.framework.http.IUpdateData;
import com.xike.xkliveplay.framework.tools.LogUtil;

/**
 * 
 * @author LiWei <br>
 * CreateTime: 2014年10月30日 上午9:52:52<br>
 * <b>Info:</b><br>
 *<br>
 *   <b>Method:</b> <br>
 */
public class HttpFavoriteManage implements IRequestCallback{

	private static final String tag = "HttpFavoriteManage";
	public static final String Method = "FavoriteManage";
	@SuppressWarnings("unused")
	private static final String ROOTELEMENT = "FavoriteManageResult";
	private FavoriteManage req = null;

	private FavoriteManageRes res = null;
	
	private IUpdateData iUpdateData = null;
	
	public HttpFavoriteManage(IUpdateData updateData)
	{
		this.iUpdateData = updateData;
	}
	
	@SuppressWarnings("unused")
	private void initTestData()
	{
		req = new FavoriteManage();
		req.setAction("ADD"); //ADD或者DELETE
		req.setContentId("CHANNEL00000051");
		req.setUserId("testdx11");
		req.setUserToken("FA64182F930B3382409CD25D");
		req.setContentType("2");
		req.setSeriesContentIndex("");
		req.setSeriesContentID("");
		req.setCategoryID("");
		req.setContentProvider("");
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
		
		LogUtil.i(tag,"requestDidFinished","flag = " + flag + " ,requestDidFinished body is:" + body);
		
		if(!Method.equals(flag))
		{
			iUpdateData.updateData(Method, null, null, false);
			return;
		}
		iUpdateData.updateData(Method, null, null, true);
		
	}

	@Override
	public void requestDidFailed(String method,String body) {
		iUpdateData.updateData(Method, null, null, false);
	}
	
	public FavoriteManage getReq() {
		return req;
	}

	public void setReq(FavoriteManage req) {
		this.req = req;
	}

	public FavoriteManageRes getRes() {
		return res;
	}

	public void setRes(FavoriteManageRes res) {
		this.res = res;
	}
}
