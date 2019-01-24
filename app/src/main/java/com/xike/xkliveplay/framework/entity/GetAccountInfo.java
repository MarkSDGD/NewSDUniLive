package com.xike.xkliveplay.framework.entity;

/**
 * 
 * @author LiWei <br>
 * CreateTime: 2014年10月30日 下午1:54:53<br>
 * <b>Info:</b><br>
 *<br>
 *   <b>Method:</b> <br>
 */
public class GetAccountInfo {
	/**mac地址**/
   private String mac;

public String getMac() 
{
	return mac;
}

public void setMac(String mac) 
{
	this.mac = mac;
}

@Override
public String toString() 
{
	return "GetAccountInfoReq [mac=" + mac + "]";
}

public GetAccountInfo() 
{
	super();
}
   
   
}
