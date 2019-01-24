package com.xike.xkliveplay.framework.tools;

import com.umeng.analytics.c;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * @author LiWei <br>
 * CreateTime: 2015年3月5日 下午2:14:43<br>
 * <b>Info:</b><br>
 *<br>
 *   <b>Method:</b> <br>
 *   <b>Public:</b> <br>
 *   <b>Private:</b> <br>
 */
public class TimeRefreshTools 
{
	
	/**
	 * 
	 * function: 计算两次进入直播的时间间隔是否大于8小时 
	 * @param
	 * @return
	 */
	public static boolean isAbove8Hours(Context context)
	{
		SharedPreferences sp = context.getSharedPreferences("time", 0);
		long savedTime = sp.getLong("savetime", 0);
		if (savedTime > 0 && (System.currentTimeMillis() - savedTime) > (8 * 3600 * 1000)) 
		{
			Editor editor = sp.edit();
			editor.putLong("savetime",System.currentTimeMillis());
			editor.commit();
			return true;
		}else if(savedTime == 0)
		{
			Editor editor = sp.edit();
			editor.putLong("savetime",System.currentTimeMillis());
			editor.commit();
		}
		return false;
	}
	
	public static void setTimeToZero(Context context)
	{
		SharedPreferences sp = context.getSharedPreferences("time", 0);
		Editor editor = sp.edit();
		editor.putLong("savetime",0);
		editor.commit();
	}
}
