package com.xike.xkliveplay.framework.entity;


/**
 * @author zw
 *  用户认证
 */
public class Authentication {

	/** 用户ID **/
	private String userId = "";
	/** 当前操作，开机认证时为”Login” **/
	private String action = "";

	public Authentication()
	{
		
	}
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}


	
}
