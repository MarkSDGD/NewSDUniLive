
package com.xike.xkliveplay.framework.entity;

/**
 * 
 * @author LiWei <br>
 * CreateTime: 2014年10月30日 下午2:49:24<br>
 * <b>Info:</b><br>
 *<br>
 *   <b>Method:</b> <br>
 */
public class AuthenticationRes {

	/** 业务管理平台为该用户分配的临时身份证明，只用于认证加密 **/
	private String encryToken = "";
	
	public String getEncryToken() {
		return encryToken;
	}

	public void setEncryToken(String encryToken) {
		this.encryToken = encryToken;
	}

	public AuthenticationRes()
	{
		
	}
	
	public String toSting()
	{
		StringBuffer sBuffer = new StringBuffer();
		sBuffer.append("encryToken=" + encryToken);
		return sBuffer.toString();
	}
}
