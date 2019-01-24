package com.xike.xkliveplay.framework.tools;

/**
 * @author LiWei <br>
 * CreateTime: 2014年12月12日 下午6:04:43<br>
 * <b>Info:</b><br>
 *<br>
 *   <b>Method:</b> <br>
 */
public class MacTool 
{
	public static String getTypeBMac(String mac2) 
	{
		if (mac2 == null) 
		{
			return "";
		}
		if (mac2.length() == 17) 
		{
			return mac2;
		}
		if (mac2.length() < 12) 
		{
			return "";
		}
		StringBuffer sb = new StringBuffer();
		for(int i = 0; i < 6;i++)
		{
			if(i < 5)
			{
				sb.append(mac2.subSequence(i * 2, (i+1) * 2) + ":");
			}else {
				sb.append(mac2.subSequence(i * 2, (i+1) * 2));
			}
			
		}
		LogUtil.e("ActivateTerminal","mac", "mac = " + sb.toString());
		return sb.toString();
	}
}
