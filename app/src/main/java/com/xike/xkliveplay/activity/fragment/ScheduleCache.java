/**
 * @Title: ScheduleCache.java
 * @Package com.xike.xkliveplay.activity.fragment
 * @Description: TODO
 * Copyright: Copyright (c) 2015
 * Company:青岛博瑞立方网络科技有限公司
 * 
 * @author Mernake
 * @date 2017年2月20日 上午10:09:00
 * @version V1.0
 */
package com.xike.xkliveplay.activity.fragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xike.xkliveplay.framework.entity.Schedule;

/**
 * @ClassName: ScheduleCache
 * @Description: 节目单缓存
 * @author Mernake
 * @date 2017年2月20日 上午10:09:00
 *
 */
public class ScheduleCache 
{
	public static Map<String, List<Schedule>> mCurSchedule = new HashMap<String, List<Schedule>>();
	
	public static Map<String, Map<String, List<Schedule>>> mAllSchedule = new HashMap<String, Map<String,List<Schedule>>>();
	
	
	public static void putCurSchedule(String date,String channelId,List<Schedule> list)
	{
		mCurSchedule.put(channelId, list);
		Map<String, List<Schedule>> mList = null;
		if (mAllSchedule.containsKey(date)) 
		{
			mList = mAllSchedule.get(date);
			
		}else {
			mList = new HashMap<String, List<Schedule>>();
		}
		mList.put(channelId, list);
		mAllSchedule.put(date, mList);
	}
	
	public static void clearData()
	{
		mCurSchedule.clear();
		mAllSchedule.clear();
	}
	
	/**
	 * 
	  * getSchedule(获取某频道某时间的节目及下一个节目信息)
	  * @param channelId
	  * @param time
	  * @return  
	  * @return String[]    返回类型
	  * @throws  异常处理
	  * @modifyHistory  createBy Mernake
	 */
	public static Schedule[] getSchedule(String channelId,String time)
	{
		Schedule[] res = new Schedule[2];
		res[0] = new Schedule();
		res[1] = new Schedule();
		res[0].setProgramName("none");
		res[1].setProgramName("none");
		if (mCurSchedule.containsKey(channelId)) 
		{
			List<Schedule> tempSchedules = mCurSchedule.get(channelId);
			for (int i = 0; i < tempSchedules.size(); i++) 
			{
				String timea = tempSchedules.get(i).getStartDate()+tempSchedules.get(i).getStartTime();
				long timeF = Long.valueOf(timea);
				long timeFB = Long.valueOf(time);
				if (timeFB >= timeF) 
				{
					if (i+1<tempSchedules.size()) 
					{
						String timeb = tempSchedules.get(i+1).getStartDate()+tempSchedules.get(i+1).getStartTime();
						long timeNext = Long.valueOf(timeb);
						if (timeFB <= timeNext)  //时间点在上一个节目的开始时间和下一个节目的开始时间之间，找到了两个记录 
						{
							res[0] = tempSchedules.get(i);
							res[1] = tempSchedules.get(i+1);
							return res;
						}
					}
				}
			}
			
		}else {
			return null; //说明不存在记录，需要重新请求
		}
		return res;//记录可能不完整
	}
	
	/** 获得今天完整的24小时 **/
	public static String[] getToday() 
	{
		String[] temp = new String[2];
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd000000");
		Calendar calendar = Calendar.getInstance();
		Date nowdate = calendar.getTime();
		String startDateTime = formatter.format(nowdate);
		String endDateTime = formatter.format(nowdate.getTime() + 24 * 60 * 60
				* 1000);
		temp[0] = startDateTime;
		temp[1] = endDateTime;
		return temp;
	}

	/**
	  * getReseeSchedule(这里用一句话描述这个方法的作用)
	
	  * @param contentId
	  * @param monthDay
	  * @return  
	  * @return List<Schedule>    返回类型
	  * @throws  异常处理
	  * @modifyHistory  createBy Mernake
	  */
	public static List<Schedule> getReseeSchedule(String contentId,
			String monthDay) 
	{
		System.out.println("######:monthday="+monthDay);
		if (mAllSchedule.containsKey(monthDay)) 
		{
			Map<String, List<Schedule>> maps = mAllSchedule.get(monthDay);
			if (maps.containsKey(contentId)) 
			{
				return maps.get(contentId);
			}
		}
		return null;
	}
	
	public static void setReseeSchedule(String contentId,String monthDay,List<Schedule> list)
	{
		if (mAllSchedule.containsKey(monthDay)) 
		{
			mAllSchedule.get(monthDay).put(contentId, list);
		}else {
			Map<String, List<Schedule>> mliMap = new HashMap<String, List<Schedule>>();
			mliMap.put(contentId, list);
			mAllSchedule.put(monthDay, mliMap);
		}
		
		Calendar calendar = Calendar.getInstance();
		Date nowdate = calendar.getTime();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
				"yyyyMMdd");
		Date start_date = new Date(nowdate.getTime());
		String start = simpleDateFormat.format(start_date);
		if (start.equals(monthDay)) 
		{
			mCurSchedule.put(contentId, list);
		}
	}
}
