package com.xike.xkliveplay.framework.entity;

/**
 * 
 * @author LiWei <br>
 * CreateTime: 2014年10月30日 下午3:07:29<br>
 * <b>Info:</b><br>
 *<br>
 *   <b>Method:</b> <br>
 */
public class Auth {
	/** 用户登录业务账号 **/
	private String userId = "";
	/** 加密后的参数串，包含各个输入参数 **/
//	private Authenticator authenticator = new Authenticator();
	
	private String authenticator = "";

	@Override
	public String toString() {
		return "AuthReq [userId=" + userId + ", authenticator=" + authenticator
				+ "]";
	}



	public Auth() {
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}



	public String getAuthenticator() {
		return authenticator;
	}



	public void setAuthenticator(String authenticator) {
		this.authenticator = authenticator;
	}



}
