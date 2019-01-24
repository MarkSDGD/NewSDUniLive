/**
 * @Title: LivePlayXunFeiService.java
 * @Package com.xike.xkliveplay.xunfei
 * @Description: TODO
 * Copyright: Copyright (c) 2015
 * Company:青岛博瑞立方网络科技有限公司
 * 
 * @author Mernake
 * @date 2017年6月16日 下午2:28:48
 * @version V1.0
 */
package com.xike.xkliveplay.xunfei;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
  * @ClassName: LivePlayXunFeiService
  * @Description: 直播关于讯飞语音的服务，要求直播即使退出这个服务也要常驻。本服务需要具有以下功能。
  * 对外：1、可以接收讯飞的广播过来，
  * 		讯飞的广播可能包含：
  * 						a.直播频道的调起
  * 						b.回看节目的调起
  * 						c.语音控制命令的发起
  * 	2、可以针对APK主进程启动与否决定用何种方式调起直播。
  * 对内：对内发送与主APK之间的交互广播，将讯飞传递过来的功能发出
  * @author Mernake
  * @date 2017年6月16日 下午2:28:48
  *
  */
public class LivePlayXunFeiService extends Service
{
	private boolean isLogON = true;
	
	
	private void showLog(String content,String method)
	{
		if (isLogON) 
		{
			System.out.println(XunfeiProvider.tag+"::"+method+"::"+content);
		}
	}
	
	@Override
	public void onCreate() 
	{
		super.onCreate();
		showLog("LivePlayXunFeiService is creating!", "onCreate");
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) 
	{
		showLog("LivePlayXunFeiService is onStartCommand!", "onStartCommand");
		return super.onStartCommand(intent, flags, startId);
	}
	
	@Override
	public void onDestroy() 
	{
		super.onDestroy();
		showLog("LivePlayXunFeiService is destroying!", "onDestroy");
	}
	
	
	@Override
	public IBinder onBind(Intent intent) 
	{
		return null;
	}
	
	

}
