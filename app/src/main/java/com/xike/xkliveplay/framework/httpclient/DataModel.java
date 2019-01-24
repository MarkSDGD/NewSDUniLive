/**
 * @Title: DataModel.java
 * @Package com.xike.xkliveplay.framework.httpclient
 * @Description: TODO
 * Copyright: Copyright (c) 2015
 * Company:青岛博瑞立方网络科技有限公司
 * 
 * @author Mernake
 * @date 2017年5月29日 上午10:36:53
 * @version V1.0
 */
package com.xike.xkliveplay.framework.httpclient;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;

import com.xike.xkliveplay.framework.entity.ContentChannel;
import com.xike.xkliveplay.framework.entity.Schedule;
import com.xike.xkliveplay.framework.http.IUpdateData;
import com.xike.xkliveplay.framework.tools.AbDateUtil;
import com.xike.xkliveplay.framework.tools.AbStrUtil;
import com.xike.xkliveplay.framework.varparams.Var;
import com.xike.xkliveplay.gd.GDHttpTools;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
  * @ClassName: DataModel
  * @Description: TODO
  * @author Mernake
  * @date 2017年5月29日 上午10:36:53
  *
  */
public class DataModel 
{
	private static DataModel model = null;
	
	private static Map<String, List<Schedule>> all6Map = new HashMap<String, List<Schedule>>();
	private static boolean isThreadStop = false;
	
	private static class MyRequestThread extends Thread
	{
		@Override
		public void run() 
		{
			while (!isThreadStop) 
			{
				try {
					sleep(1000*60);//1分钟
					handler.sendEmptyMessage(0);
				} catch (InterruptedException e) 
				{
					e.printStackTrace();
				}
			}
		}
	}
	
	private static int i = 0;
	
	@SuppressLint("HandlerLeak")
	private static Handler handler = new Handler()
	{

		@Override
		public void handleMessage(Message msg) 
		{
			if (msg.what == 0) 
			{
				i++;
				if (i>=120) //120分钟
				{
					requestSchedules();
					i=0;
				}
			}else if (msg.what == 1) 
			{
				requestSchedules();
			}
		}
	};
	
	private static void requestSchedules()
	{
		System.out.println("##########:请求6小时节目单");
		if (GDHttpTools.getInstance().isGDI6Enable()){
			GDHttpTools.getInstance().getLiveScheduleListXML(GDHttpTools.getInstance().getTag(), new IUpdateData()
			{
				@Override
				public void updateData(String method, String uniId, Object object, boolean isSuccess) {

				}
			});
		}else{
			HttpAll6Schedules httpAll6Schedules = new HttpAll6Schedules(new IUpdateData()
			{
				@Override
				public void updateData(String method, String uniId, Object object, boolean isSuccess)
				{

				}
			});

			httpAll6Schedules.post();
		}
	}
	
	public static DataModel getInstance()
	{
		if (model == null) 
		{
			model = new DataModel();
//			handler.sendEmptyMessage(1);
			MyRequestThread thread = new MyRequestThread();
			thread.start();
		}
		
		return model;
	}
	
	public static void deInstance()
	{
		System.out.println("####6小时节目单清空");
		all6Map.clear();
		isThreadStop = true;
		model = null;
		i=0;
	}
	
	public void initAll6Schedules(List<Schedule> oList)
	{
		if (oList==null || oList.size()==0)return;
		all6Map.clear();
		if (Var.allChannels.size()<=0)
		{
			return;
		}
		
		for (ContentChannel channel : Var.allChannels)
		{
			List<Schedule> tempList = new ArrayList<Schedule>();
			for (Schedule schedule : oList)
			{
				if (channel.getContentId().equals(schedule.getChannelId())) 
				{
					tempList.add(schedule);
				}else continue;
			}
			all6Map.put(channel.getContentId(), tempList);
		}
	}

	public Schedule[] getSchedule(String channelId, String time)
	{
		Schedule[] res = new Schedule[2];
		res[0] = new Schedule();
		res[1] = new Schedule();
		res[0].setProgramName("none");
		res[1].setProgramName("none");
		if (all6Map.containsKey(channelId))
		{
			List<Schedule> tempSchedules = all6Map.get(channelId);
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

		}else
		{
			System.out.println("########:null");
			return null; //说明不存在记录，需要重新请求
		}
		System.out.println("########:res");
		return res;//记录可能不完整
	}




	
	public Schedule getCurrentProgram(long nowTime, String channelId)
	{
		String time = AbDateUtil.getStringByFormat(nowTime, AbDateUtil.dateFormatymdhms);
		long timeNum = 0;
		if (AbStrUtil.isNumber(time))
		{
			timeNum = Long.valueOf(time);
		}
		
		List<Schedule> tempList = all6Map.get(channelId);
		if (tempList==null||tempList.size()<=0) 
		{
			return null;
		}
		
		for (int i = 0; i < tempList.size(); i++) 
		{
			String tempTimeStr = tempList.get(i).getStartDate()+tempList.get(i).getStartTime();
			long tempTimeNum = 0;
			if (AbStrUtil.isNumber(tempTimeStr))
			{
				tempTimeNum = Long.valueOf(tempTimeStr);
			}
			
			if (tempTimeNum == timeNum) 
			{
				return tempList.get(i);
			}else if (tempTimeNum > timeNum) 
			{
				continue;
			}else if (tempTimeNum < timeNum) 
			{
				if (i-1 >=0) 
				{
					return tempList.get(i-1);
				}
			}
		}
		return null;
	}
	
	public void computeNowProgram(List<ContentChannel> mlist)
	{
		try {
			if (mlist == null || mlist.size() == 0) 
			{
				return;
			}
			long nowTime = System.currentTimeMillis();
			int count = mlist.size();
			for (ContentChannel channel : mlist)
			{
				if (count != mlist.size()) 
				{
					System.out.println("频道列表集合发生了变化");
					return;
				}
				List<Schedule> tempList = all6Map.get(channel.getContentId());
				if (tempList == null||tempList.size() ==0) 
				{
					continue;
				}
				int index = 0;
				for (int i = 0; i < tempList.size(); i++) 
				{
					String startTime = tempList.get(i).getStartDate()+tempList.get(i).getStartTime();
					long startTimeLong = 0L;
					if (AbStrUtil.isNumber(startTime))
					{
						startTimeLong = AbDateUtil.getMillisOfStringDate(startTime, AbDateUtil.dateFormatymdhms);
					}
					
					if (nowTime ==  startTimeLong) 
					{
						//这就是要选择的节目
						index = i;
						
					}else if (nowTime > startTimeLong) 
					{
						//说明可能是这个节目，也可能是后面的节目
						continue;
					}else if (nowTime < startTimeLong) 
					{
						//说明当前的播放节目是这个之前的那个节目
						if (i-1 < 0) 
						{
							index = 0;
						}else
						index = i-1;
						break;
					}
				}
				Schedule schedule = tempList.get(index);
//				System.out.println("###play:5555555555555555555555555555555555");
				if (schedule!=null) 
				{
//					15:00-07:00 二炮手(34集大结局)
					
					String startDateTime = schedule.getStartDate()+schedule.getStartTime();
					String startTime = AbDateUtil.getStringByFormat(startDateTime, AbDateUtil.dateFormatymdhms, AbDateUtil.dateFormatHM);
					String endTime = "";
					Date startDate = AbDateUtil.getDateByFormat(startDateTime, AbDateUtil.dateFormatymdhms);
					int duration = 0;
					if (AbStrUtil.isNumber(schedule.getDuration()))
					{
						duration = Integer.valueOf(schedule.getDuration());
						Date afterDate = new Date(startDate.getTime() + duration*1000);
						endTime = AbDateUtil.getStringByFormat(afterDate, AbDateUtil.dateFormatHM);
					}
					
//					System.out.println("###play:"+startTime+"-"+endTime+ " " + schedule.getProgramName());
					channel.setLogoURL(startTime+"-"+endTime+ " " + schedule.getProgramName());
				}else channel.setLogoURL("");
			}
		} catch (ConcurrentModificationException e) 
		{
			e.printStackTrace();
		}
		
	}

	/**
	  * isAll6SchedulesExist(这里用一句话描述这个方法的作用)
	  
	  * @return void    返回类型
	  * @modifyHistory  createBy Mernake
	  */
	public boolean isAll6SchedulesExist() 
	{
		if (all6Map==null||all6Map.size()==0) 
		{
			return false;
		}
		return true;
	}
}
