/**
 * @Title: XunfeiTools.java
 * @Package com.xike.xkliveplay.xunfei
 * @Description: TODO
 * Copyright: Copyright (c) 2015
 * Company:青岛博瑞立方网络科技有限公司
 * 
 * @author Mernake
 * @date 2017年3月14日 上午9:35:03
 * @version V1.0
 */
package com.xike.xkliveplay.xunfei;

import java.util.List;

import com.xike.xkliveplay.framework.db.DBManager;
import com.xike.xkliveplay.framework.entity.ContentChannel;
import com.xike.xkliveplay.framework.entity.XunfeiObject;
import com.xike.xkliveplay.framework.varparams.Var;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
  * @ClassName: XunfeiTools
  * @Description: 
  * @author Mernake
  * @date 2017年3月14日 上午9:35:03
  *
  */
public class XunfeiTools 
{
	private static XunfeiTools xunfeiTools = null;
	private static final String PACKAGENAME = "com.xike.xkliveplay";
	private static final String START_CLASS = PACKAGENAME+".activity.launch.ActivityLaunch";
	
	
	private static final String KEY_ACTION = "action";
	private static final String KEY_CHANNEL_NUM = "channelNum";
	private static final String KEY_STARTTIME = "starttime";
	private static final String KEY_ENDTIME = "endtime";
	private static final String KEY_STARTDATE = "startdate";
	private static final String KEY_ENDDATE = "enddate";
	private static final String KEY_NO = "no";
	private static final String KEY_NAME = "name";
	
	
	private static final String VALUE_PREV = "prev";
	private static final String VALUE_NEXT = "next";
	private static final String VALUE_OPEN = "open";
	private static final String VALUE_SELECT ="select";
	
	private XunfeiInterface mInterface = null;
	
	
	public static XunfeiTools getInstance()
	{
		if (xunfeiTools == null) 
		{
			xunfeiTools = new XunfeiTools();
		}
		
		return xunfeiTools;
	}
	
	public void setInterface(XunfeiInterface mInterface)
	{
		this.mInterface = mInterface;
	}
	
	public void saveChannels(DBManager dbManager,Context context)
	{
			dbManager.deleteAll("channel");
			dbManager.deleteAll("xunfei");
		for (ContentChannel channel : Var.allChannels) 
		{
			dbManager.add("channel", channel);
			XunfeiObject xunfeiObject = new XunfeiObject();
			xunfeiObject.setName(channel.getName());
			xunfeiObject.setNo(channel.getChannelNumber());
			dbManager.add("xunfei", xunfeiObject);
		}
		
		sendUpdateBroadcast(context);
	}
	
	/**
	 * 
	  * sendUpdateBroadcast(发送广播更新频道列表)
	  * @param context  
	  * @return void    返回类型
	  * @throws  异常处理
	  * @modifyHistory  createBy Mernake
	 */
	public void sendUpdateBroadcast(Context context)
	{
		System.out.println(XunfeiProvider.tag + "::sendUpdateBroadcast()");
		Intent intent = new Intent();
		intent.setAction("com.iflytek.xiri.action.LIVE.updateByCP");
		context.sendBroadcast(intent);
	}

	/**
	 * 
	  * sendLiveStatusBroadcast(发送直播状态变化)
	  * @param context
	  * @param isLive  true--进入直播，false--离开直播
	  * @return void    返回类型
	  * @throws  异常处理
	  * @modifyHistory  createBy Mernake
	 */
	public void sendLiveStatusBroadcast(Context context ,boolean isLive)
	{
		System.out.println(XunfeiProvider.tag + "::sendLiveStatusBroadcast::"+isLive);
		Intent intent = new Intent();
		intent.putExtra("cp", XunfeiProvider.CODE);
		intent.putExtra("live", isLive);
		intent.setAction("com.iflytek.xiri.action.LIVE.status");
		context.sendBroadcast(intent);
	}
	
	/**
	 * 
	  * sendBackPlayBroadcast(发送回看状态变化)
	  * @param context
	  * @param isLive  true--进入回看，false--离开回看
	  * @return void    返回类型
	  * @throws  异常处理
	  * @modifyHistory  createBy Mernake
	 */
	public void sendBackPlayBroadcast(Context context ,boolean isLive)
	{
		System.out.println(XunfeiProvider.tag + "::sendBackPlayStatusBroadcast::"+isLive);
		Intent intent = new Intent();
		intent.putExtra("cp", XunfeiProvider.CODE);
		intent.putExtra("live", isLive);
		intent.setAction("com.iflytek.xiri.action.SDMBACK.status");
		context.sendBroadcast(intent);
	}
	
	
	/**
	  * parseBundle(解析讯飞广播发过来的内容，并执行对应的操作)
	
	  * @param bundle  
	  * @return void    返回类型
	  * @throws  异常处理
	  * @modifyHistory  createBy Mernake
	  */
	public void parseBundle(Bundle bundle,Context context) 
	{
		if (!bundle.containsKey(KEY_ACTION)) 
		{
			showLog("there is no key like "+ KEY_ACTION + " in bundle!!!", "parseBundle");
			return;
		}
		
		String action = bundle.getString(KEY_ACTION);
		if (action.equals(VALUE_SELECT)) //讯飞直播唤起,带频道号
		{
			jumpChannel(bundle,context);
		}else if (action.equals(VALUE_OPEN)) //讯飞直播唤起
		{
			jumpLive(context);
		}else if (action.equals(VALUE_NEXT)) //讯飞切换下一个频道
		{
			jumpNextChannel(context);
		}else if (action.equals(VALUE_PREV)) //讯飞切换上一个频道
		{
			jumpPrevChannel(context);
		}
	}
	
	/**
	  * parseBundle(解析讯飞广播发过来的内容，并执行对应的操作)
	
	  * @param bundle  
	  * @return void    返回类型
	  * @throws  异常处理
	  * @modifyHistory  createBy Mernake
	  */
	public void parseTVBackBundle(Bundle bundle,Context context) 
	{
		if (!bundle.containsKey(KEY_ACTION)) 
		{
			showLog("there is no key like "+ KEY_ACTION + " in bundle!!!", "parseBundle");
			return;
		}
		
		String action = bundle.getString(KEY_ACTION);
		if (action.equals(VALUE_SELECT)) //讯飞回看唤起,带很多信息
		{
			jumpSchedule(bundle,context);
		}else if (action.equals(VALUE_OPEN)) //讯飞回看唤起
		{
			jumpBack(context);
		}else if (action.equals(VALUE_NEXT)) //讯飞切换下一个节目
		{
//			jumpNextChannel(context);
		}else if (action.equals(VALUE_PREV)) //讯飞切换上一个节目
		{
//			jumpPrevChannel(context);
		}
	}
	
	/**
	  * jumpSchedule(跳转某个回看节目)
	  * @param bundle
	  * @param context  
	  * @return void    返回类型
	  * @throws  异常处理
	  * @modifyHistory  createBy Mernake
	  */
	private void jumpSchedule(Bundle bundle, Context context) 
	{
		String startTime = "";
		String endTime = "";
		String startDate = "";
		String endDate = "";
		String name = "";
		String channelNum = "";
		if (bundle.containsKey(KEY_CHANNEL_NUM))
		{
			channelNum = bundle.getString(KEY_CHANNEL_NUM);
		} 
		else  {
			showLog("讯飞发送过来的内容，没有频道号", "jumpSchedule");
			return;
		}
		if (bundle.containsKey(KEY_STARTTIME)) startTime = bundle.getString(KEY_STARTTIME);
		if (bundle.containsKey(KEY_STARTDATE)) startDate = bundle.getString(KEY_STARTDATE);
		if (bundle.containsKey(KEY_ENDTIME)) endTime = bundle.getString(KEY_ENDTIME);
		if (bundle.containsKey(KEY_ENDDATE)) endDate = bundle.getString(KEY_ENDDATE);
		if (bundle.containsKey(KEY_NAME)) name = bundle.getString(KEY_NAME);
//		if (startTime!=null) startTime = startTime.replace(":", "");
//		if (startDate!=null) startDate = startDate.replace("-", "");
//		if (endTime!=null) endTime = endTime.replace(":", "");
//		if (endDate!=null) endDate = endDate.replace("-", "");
		
		if (checkIsApkStarted(context)) 
		{
			//APK启动了
			if (mInterface!=null) mInterface.goToSchedule(channelNum, name, startTime, startDate, endTime, endDate);
		}else
		{
			//APK没启动
			startLiveBack(context, channelNum, name, startDate, startTime);
		}
	}
	
	private void startLiveBack(Context context,String num,String name,String startDate,String startTime)
	{
		Intent intent = new Intent(Intent.ACTION_MAIN);  
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.addCategory(Intent.CATEGORY_LAUNCHER); 
		intent.putExtra("name", name);
		intent.putExtra("xunfeinum", num);
		intent.putExtra("starttime", startTime);
		intent.putExtra("startdate", startDate);
		intent.putExtra("type", 2);
		ComponentName cn = new ComponentName(context.getPackageName(), START_CLASS);              
		intent.setComponent(cn);  
		context.startActivity(intent);
	}

	/**
	  * jumpBack(这里用一句话描述这个方法的作用)
	
	  * @param context  
	  * @return void    返回类型
	  * @throws  异常处理
	  * @modifyHistory  createBy Mernake
	  */
	private void jumpBack(Context context) 
	{
		if (checkIsApkStarted(context)) 
		{
			//APK启动了
			if (mInterface!=null) mInterface.goToTvBack();
		}else
		{
			//APK没启动
			startLiveApkWithIntent(KEY_CHANNEL_NUM, "", context,2);
		}
	}

	/**
	  * jumpPrevChannel(这里用一句话描述这个方法的作用)
	  * @param context  
	  * @return void    返回类型
	  * @throws  异常处理
	  * @modifyHistory  createBy Mernake
	  */
	private void jumpPrevChannel(Context context) 
	{
		if (checkIsApkStarted(context)) 
		{
			//APK启动了
			if (mInterface!=null) mInterface.goToLastChannel();
		}
	}

	/**
	  * jumpNextChannel(这里用一句话描述这个方法的作用)
	  * @param context  
	  * @return void    返回类型
	  * @throws  异常处理
	  * @modifyHistory  createBy Mernake
	  */
	private void jumpNextChannel(Context context) 
	{
		if (checkIsApkStarted(context)) 
		{
			//APK启动了
			if (mInterface!=null) mInterface.goToNextChannel();
		}
	}

	private void jumpLive(Context context) 
	{
		if (checkIsApkStarted(context)) 
		{
			//APK启动了
			if (mInterface!=null) mInterface.goToLive();
		}else
		{
			//APK没启动
			startLiveApkWithIntent(KEY_CHANNEL_NUM, "", context,0);
		}
	}

	private void jumpChannel(Bundle bundle,Context context)
	{
		String channelNum = bundle.getString(KEY_CHANNEL_NUM); //取出要跳转的频道号
		//判断APK是否启动了，如果没启动，则含参启动APK。如果启动了，则通过接口传递消息
		if (checkIsApkStarted(context)) 
		{
			//APK启动了
			if (mInterface!=null) mInterface.goToChannel(channelNum, "");
		}else
		{
			//APK没启动
			startLiveApkWithIntent(KEY_CHANNEL_NUM, channelNum, context,0);
		}
	}

	
	private void startLiveApkWithIntent(String key,String value,Context context, int type)
	{
		Intent intent = new Intent(Intent.ACTION_MAIN);  
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);    
		intent.putExtra(key, value); //key="channelNum",value=频道号
		intent.putExtra("type", type); //type默认填0
		ComponentName cn = new ComponentName(PACKAGENAME, START_CLASS);              
		intent.setComponent(cn);  
		context.startActivity(intent);
	}
	
	private boolean checkIsApkStarted(Context context)
	{
		 ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
		    List<RunningTaskInfo> list = am.getRunningTasks(100);
		    for (int i = 0; i < list.size(); i++) 
		    {
		    	System.out.println("topActivity:"+list.get(i).topActivity.getPackageName() + " baseActivity:"+list.get(i).baseActivity.getPackageName());
		    	
		        if (list.get(i).topActivity.getPackageName().contains(PACKAGENAME)) 
		        {
		        	if (i==0) 
		        	{
		        		return true;
					}else return false;
		        }
			}
		    
		    return false;
	}
	
	private void showLog(String content,String method)
	{
		System.out.println(XunfeiProvider.tag+"::"+method+"::"+content);
	}
	
	
	public interface XunfeiInterface
	{
		/**
		 * 
		  * goToNextChannel(跳转到下一个频道)
		  * @return void    返回类型
		  * @throws  异常处理
		  * @modifyHistory  createBy Mernake
		 */
		public void goToNextChannel();
		/**
		 * 
		  * goToLastChannel(跳转到上一个频道)
		  * @return void    返回类型
		  * @throws  异常处理
		  * @modifyHistory  createBy Mernake
		 */
		public void goToLastChannel();
		/**
		 * 
		  * goToLive(打开直播)
		  
		  * @return void    返回类型
		  * @throws  异常处理
		  * @modifyHistory  createBy Mernake
		 */
		public void goToLive();
		/**
		 * 
		  * goToChannel(跳转到某个频道)
		  * @param num
		  * @param channelName  
		  * @return void    返回类型
		  * @throws  异常处理
		  * @modifyHistory  createBy Mernake
		 */
		public void goToChannel(String num,String channelName);
		
		/**
		 * 
		  * goToTvBack(跳转回看界面)
		  * @return void    返回类型
		  * @throws  异常处理
		  * @modifyHistory  createBy Mernake
		 */
		public void goToTvBack();
		
		public void goToSchedule(String num,String name,String starttime,String startdate,String endtime,String enddate);
	}


	/**
	  * deInstance(这里用一句话描述这个方法的作用)
	  
	  * @return void    返回类型
	  * @throws  异常处理
	  * @modifyHistory  createBy Mernake
	  */
	public static void deInstance() 
	{
		xunfeiTools = null;
	}
	
	/**
	 * 
	  * sendYinHe(处理银河的直播频道跳转)
	  * @param channelNum
	  * @param name  
	  * @return void    返回类型
	  * @throws  异常处理
	  * @modifyHistory  createBy Mernake
	 */
	public void sendYinHe(Context context,Bundle bundle)
	{
		jumpChannel(bundle, context);
	}
}
