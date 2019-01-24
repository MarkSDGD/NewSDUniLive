package com.xike.xkliveplay.framework.entity;



/**
 * @author LiWei CreateTime: 2014-5-23下午4:08:45 info:
 * 
 */
public class ActivateTerminal {
	String userId = "";
	/**mac地址 格式为XX:XX:XX:XX:XX:XX**/
	String mac = "";

	public ActivateTerminal() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) 
	{
		this.mac = mac;
	}



	@Override
	public String toString() {
		return "ActivateTerminalReq [userId=" + userId + ", mac=" + mac + "]";
	}

}
