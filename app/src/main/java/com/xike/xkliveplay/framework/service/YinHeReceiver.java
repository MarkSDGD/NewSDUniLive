/**
 * @Title: YinHeReceiver.java
 * @Package com.xike.xkliveplay.framework.service
 * @Description: TODO
 * Copyright: Copyright (c) 2015
 * Company:青岛博瑞立方网络科技有限公司
 * 
 * @author Mernake
 * @date 2017年12月4日 下午3:56:30
 * @version V1.0
 */
package com.xike.xkliveplay.framework.service;

import java.util.List;

import com.xike.xkliveplay.xunfei.XunfeiTools;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
  * @ClassName: YinHeReceiver
  * @Description: TODO
  * @author Mernake
  * @date 2017年12月4日 下午3:56:30
  *
  */
public class YinHeReceiver extends BroadcastReceiver
{

	
	@Override
	public void onReceive(Context context, Intent intent) 
	{
		Bundle bundle = intent.getExtras();
		if (bundle!=null) 
		{
			for(String str:bundle.keySet())
			{
				System.out.println("key:"+str);
			}
			
			long createTime = 0;
			if (bundle.containsKey("createTime")) 
			{
				createTime = bundle.getLong("createTime");
			}
			
			String channelNum = "";
			String channelName = "";
			if (bundle.containsKey("channelCode")) 
			{
				channelNum = bundle.getString("channelCode");
			}
			
			if (bundle.containsKey("channelName")) 
			{
				channelName = bundle.getString("channelName");
			}
			
			long receiveTime = System.currentTimeMillis();
			
			System.out.println("银河小程序：创建时间："+createTime);
			System.out.println("银河小程序：接收事件："+receiveTime);
			System.out.println("银河小程序：频道号："+channelNum);
			System.out.println("银河小程序：频道名称："+channelName);
			bundle.putString("channelNum", channelNum);
			XunfeiTools.getInstance().sendYinHe(context,bundle);
		}
	}
}
