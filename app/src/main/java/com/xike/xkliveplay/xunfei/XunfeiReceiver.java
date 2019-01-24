/**
 * @Title: XunfeiReceiver.java
 * @Package com.xike.xkliveplay.xunfei
 * @Description: TODO
 * Copyright: Copyright (c) 2015
 * Company:青岛博瑞立方网络科技有限公司
 * 
 * @author Mernake
 * @date 2017年3月14日 上午9:43:18
 * @version V1.0
 */
package com.xike.xkliveplay.xunfei;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

/**
  * @ClassName: XunfeiReceiver
  * @Description: TODO
  * @author Mernake
  * @date 2017年3月14日 上午9:43:18
  *
  */
public class XunfeiReceiver extends BroadcastReceiver
{

	
	@Override
	public void onReceive(Context context, Intent intent) 
	{
		if (intent.getAction().equals("com.iflytek.xiri.action.LIVE")) 
		{
			if (intent.getType().equals("xiri/com.iflytek.xiri.sdmgd.livechannels")) 
			{
				showLog("received xunfei broadcast!!!", "onReceive"); //收到讯飞的广播
				Bundle bundle = intent.getExtras();
				String action = intent.getStringExtra("action");
				String channelNum = bundle.getString("channelNum");
//				Toast.makeText(context, "action="+action+ "  channelNum="+channelNum, Toast.LENGTH_SHORT).show();
				XunfeiTools.getInstance().parseBundle(bundle,context);//解析讯飞的广播
			}
		}
		
	}
	

	
	private void showLog(String content,String method)
	{
		System.out.println(XunfeiProvider.tag + "::"+method+"::"+content);
	}
}
