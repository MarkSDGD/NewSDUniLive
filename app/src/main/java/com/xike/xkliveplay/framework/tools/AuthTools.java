/**
 * @Title: AuthTools.java
 * @Package com.xike.xkliveplay.framework.tools
 * @Description: TODO
 * Copyright: Copyright (c) 2014 
 * Company: 青岛博瑞立方网络科技有限公司
 * 
 * @author Mernake
 * @date 2015年6月9日 下午1:48:59
 * @version V1.0
 */
package com.xike.xkliveplay.framework.tools;

import java.util.regex.Pattern;

 /** 
 * @ClassName:	AuthTools 
 * @Description:TODO(这里用一句话描述这个类的作用) 
 * @author:	Mernake
 * @date:	2015年6月9日 下午1:48:59 
 *  
 */
public class AuthTools 
{
	public static boolean isMac(String mac)
	{
	   	//正则校验MAC合法性
	   	String patternMac="^[a-fA-F0-9]{2}+:[a-fA-F0-9]{2}+:[a-fA-F0-9]{2}+:[a-fA-F0-9]{2}+:[a-fA-F0-9]{2}+:[a-fA-F0-9]{2}$";
	   	if(!Pattern.compile(patternMac).matcher(mac).find())
	   	{
	   		return false;
	    }
	   	return true;
	}
	
	
	public static boolean isIPV4(String ip)
	{
		//正则校验IP合法性
		 Pattern pattern = Pattern.compile( "^((\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5]|[*])\\.){3}(\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5]|[*])$" );
		 if(!pattern.matcher(ip).find())
		 {
			 return false;
		 }
		 return true;
	}
}
