package com.xike.xkliveplay.framework.error;

import android.content.Context;
import android.content.Intent;
import android.text.StaticLayout;

/**
 * @author LiWei <br>
 * CreateTime: 2015年2月27日 上午9:47:54<br>
 * <b>Info:</b><br>
 *<br>
 *   <b>Method:</b> <br>
 *   <b>Public:</b> <br>
 *   <b>Private:</b> <br>
 */
public class SendBroadcastTools 
{
	public static boolean isErrorUIShow = false;
	
	public static void sendErrorBroadcast(Context context,String action,String mac,String userId)
	{
		
		cancelDialog(context);
		Intent intent = new Intent();
		intent.setAction(ErrorBroadcastAction.ERROR_LIVEPLAY_ACTION);
		intent.putExtra("action", action);
		intent.putExtra("mac", mac);
		intent.putExtra("userid", userId);
		
		if (action.equals(ErrorBroadcastAction.ERROR_LIVEPLAY_CANCEL_DIALOG)) 
		{
			isErrorUIShow = false;
		}else {
			isErrorUIShow = true;
		}
		context.sendBroadcast(intent);
	}
	
	private static void cancelDialog(Context context)
	{
		Intent intent = new Intent();
		intent.setAction(ErrorBroadcastAction.ERROR_LIVEPLAY_ACTION);
		intent.putExtra("action", ErrorBroadcastAction.ERROR_LIVEPLAY_CANCEL_DIALOG);
		intent.putExtra("mac", "");
		intent.putExtra("userid", "");
		isErrorUIShow = false;
		context.sendBroadcast(intent);
	}
}
