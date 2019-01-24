package com.xike.xkliveplay.framework.entity;

/**
 * 
 * @author LiWei <br>
 * CreateTime: 2014年10月30日 下午2:22:55<br>
 * <b>Info:</b><br>
 *<br>
 *   <b>Method:</b> <br>
 */
public class GetAccountInfoRes {
	
	/**返回的账号userId**/
	private String account = "";
	/**返回的密码**/
	private String password = "";
	public GetAccountInfoRes() {
		super();
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	@Override
	public String toString() {
		return "GetAccountInfoRes [account=" + account + ", password="
				+ password + "]";
	}
	
}
