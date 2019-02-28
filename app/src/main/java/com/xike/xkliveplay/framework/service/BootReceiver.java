package com.xike.xkliveplay.framework.service;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

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
		//开机重置标志位
		SharedPreferences sp = context.getSharedPreferences("FragmentLivePlay", 0); //isselectedused标志位是用来标志广电的那个开机第一次要启动的那个频道，是不是已经启动过了，换句话说可以近似等于是否是开机第一次启动直播apk
		SharedPreferences.Editor editor = sp.edit();
		editor.putInt("isselectedused", 0);
		editor.putBoolean("isLiveStarted",false);
		editor.commit();
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
