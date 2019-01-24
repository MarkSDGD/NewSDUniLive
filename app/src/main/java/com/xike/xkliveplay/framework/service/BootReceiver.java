package com.xike.xkliveplay.framework.service;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * @author Administrator
 *
 */
public class BootReceiver extends BroadcastReceiver
{
	public static boolean isBoot = true;
	@Override
	public void onReceive(Context context, Intent intent) 
	{
//		Toast.makeText(context, "开机自动服务自动启动.....~~~~~~~~~~~~~~~~~~~~~~~~~~", Toast.LENGTH_LONG).show();
	/*	Log.i("BootReceiver ","AuthenticationService onReceive intent ："+intent.getAction());

		Intent s_intent = new Intent(context,AuthenticationService.class);
		s_intent.setAction("com.xike.AuthenticationService");
		s_intent.putExtra("isStop", "false");
		s_intent.putExtra("flag", "first");
		isBoot = true;
		context.startService(s_intent);*/
	}

	
}
