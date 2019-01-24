/**
 * @author liwei
 * @d2014-2-25
 */
package com.xike.xkliveplay.framework.entity;

/**
 * @author liwei
 * 
 */
public class Authenticator {

	/** 随机数字 **/
	private String random = "";
	/** 用于Authenticator产生的临时Token **/
	private String encryToken = "";
	/** 用户业务编号 **/
	private String userId = "";
	/** 终端设备编号 **/
	private String terminalId = "";
	/** IP地址 **/
	private String ip = "";
	/** 终端设备的MAC地址，格式为XX:XX:XX:XX:XX:XX **/
	private String mac = "";
	/** 预留 **/
	private String reserved = "";
	
	

	public Authenticator() {
	}

	public String getRandom() {
		return random;
	}

	public void setRandom(String random) {
		this.random = random;
	}

	public String getEncryToken() {
		return encryToken;
	}

	public void setEncryToken(String encryToken) {
		this.encryToken = encryToken;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getTerminalId() {
		return terminalId;
	}

	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public String getReserved() {
		return reserved;
	}

	public void setReserved(String reserved) {
		this.reserved = reserved;
	}

}
