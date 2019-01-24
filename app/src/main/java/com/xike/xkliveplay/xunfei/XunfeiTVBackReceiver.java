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
public class XunfeiTVBackReceiver extends BroadcastReceiver
{

	
	@Override
	public void onReceive(Context context, Intent intent) 
	{
		if (intent.getAction().equals("com.iflytek.xiri.action.TVBACK")) 
		{
			if (intent.getType().equals("xiri/com.iflytek.xiri.sdmgd.tvback")) 
			{
				showLog("received xunfei broadcast!!!", "onReceive"); //收到讯飞的广播
				Bundle bundle = intent.getExtras();
//				String action = intent.getStringExtra("action");
//				String channelNum = bundle.getString("channelNum");
				
				String str = "";
				for (String strs : bundle.keySet()) 
				{
					str = str+"key="+strs+" value="+bundle.getString(strs)+"\n";
				}
				System.out.println("###:"+str);
//				Toast.makeText(context, "bundle="+str, Toast.LENGTH_SHORT).show();
				XunfeiTools.getInstance().parseTVBackBundle(bundle, context);
//				XunfeiTools.getInstance().parseBundle(bundle,context);//解析讯飞的广播
			}
		}
		
	}
	

	
	private void showLog(String content,String method)
	{
		System.out.println(XunfeiProvider.tag + "::"+method+"::"+content);
	}
}
