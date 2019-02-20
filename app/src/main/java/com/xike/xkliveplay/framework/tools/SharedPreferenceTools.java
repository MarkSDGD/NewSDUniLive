package com.xike.xkliveplay.framework.tools;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * @author Mernake <br>
 * CreateTime: 2015年5月12日 下午2:12:45<br>
 * <b>Info:</b><br>
 *<br>
 *   <b>Method:</b> <br>
 *   <b>Public:</b> <br>
 *   <b>Private:</b> <br>
 */
public class SharedPreferenceTools 
{
	public static final String SHARED_FILE_LIVEPLAY = "liveplay";
	
	public static final String SHARED_KEY_VERSION = "key_version";

	public static final String SHARED_KEY_RECOMMEND = "key_recommend";

	/**
	 * 
	 * function:从指定的sharedpreference中读取String类型数据 
	 * @param
	 * @return
	 */
	public static String getSavedStringPreference(Context context,String key,String fileName)
	{
		SharedPreferences sharedPreferences = context.getSharedPreferences(fileName, 0);
		String res = sharedPreferences.getString(key, "");
		LogUtil.i(SharedPreferenceTools.class.getSimpleName(), "getSavedPreference:", "fileName=" + fileName + " key=" + key + " res=" + res);
		return res;
	}
	
	/**
	 * 
	 * function:向指定的sharedpreference中存储String类型数据 
	 * @param
	 * @return
	 */
	public static boolean setSavedStringPreference(Context context, String key,String value,String fileName)
	{
		SharedPreferences sharedPreferences = context.getSharedPreferences(fileName, 0);
		Editor editor = sharedPreferences.edit();
		editor.putString(key, value);
		return editor.commit();
	}
}
