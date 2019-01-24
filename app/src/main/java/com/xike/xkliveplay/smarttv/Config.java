package com.xike.xkliveplay.smarttv;

import java.util.HashMap;
import java.util.Map;

/**
 * @author LiWei <br>
 * CreateTime: 2014-7-16����5:13:28<br>
 * info: 
 * 
 */
public class Config 

{
	public static String smartTvProgramName = "";
	public static String smartTvChannelName = "";
	public static String smartProgramStartTime = "";
    public static String BROADCAST_REMOTE_TV_PROGRAM = "com.xike.remote.program";
	
	public static void clear()
	{
		smartTvChannelName = "";
		smartTvProgramName = "";
		smartProgramStartTime = "";
	}
	

	public static Map<String, String> map = null;
	static void initMap()
	{
		map = new HashMap<String, String>();
		map.put("CCTV-1", "CCTV-1");
		map.put("CCTV-2", "CCTV-2");
		map.put("CCTV-3", "CCTV-3");
		map.put("CCTV-4", "CCTV-4");
		map.put("CCTV-5", "CCTV-5");
		map.put("CCTV-6", "CCTV-6");
		map.put("CCTV-7", "CCTV-7");
		map.put("CCTV-8", "CCTV-8");
		map.put("CCTV-NEWS", "CCTV-NEWS");
		map.put("CCTV-10", "CCTV-10");
		map.put("CCTV-11", "CCTV-11");
		map.put("CCTV-12", "CCTV-12");
	}
	
	public static String getChannelName(String key)
	{
		if (map == null) 
		{
			initMap();
		}
		String res = map.get(key);
		if (res == null) 
		{
			return key;
		}
		return res;
	}
}
