package com.xike.xkliveplay.activity.dialogerror;

import com.xike.xkliveplay.framework.error.ErrorBroadcastAction;
import com.xike.xkliveplay.framework.tools.LogUtil;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * @author LiWei <br>
 * CreateTime: 2015年2月26日 下午2:59:38<br>
 * <b>Info:</b><br>
 *<br>
 *   <b>Method:</b> <br>
 *   <b>Public:</b> <br>
 *   <b>Private:</b> <br>
 */
public class DialogBroadcastReceiver extends BroadcastReceiver
{
	private Activity activity = null;

	
	
	public DialogBroadcastReceiver(Activity activity) 
	{
		super();
		this.activity = activity;
	}



	/* (non-Javadoc)
	 * @see android.content.BroadcastReceiver#onReceive(android.content.Context, android.content.Intent)
	 */
	@Override
	public void onReceive(Context context, Intent intent) 
	{
		System.out.println("ccccccccccc: 收到广播： " + intent.getAction() + " \nddddddddddddddd: " + intent.getStringExtra("action"));
		if (intent.getAction().equals(ErrorBroadcastAction.ERROR_LIVEPLAY_ACTION)) 
		{
			String extra = intent.getStringExtra("action");
			String ext1 = intent.getStringExtra("userid"); // 51018或51019时，这个字段存放userId. 
			String ext2 = intent.getStringExtra("mac");// 51003,51004和51012也有可能，mac地址格式非法；51018或51019时，这个字段存放mac. 51002时这个字段存放鉴权插件返回的错误码
			System.out.println("aaaaaaaaaaaaaaaaaaa: " + ext2);
			
			if (extra.equals(ErrorBroadcastAction.ERROR_LIVEPLAY_CANCEL_DIALOG)) 
			{
				DialogTools.creater().cancelDialog();
				return;
			}
			
			String[] result = ErrorCode.getErrorCodeMsg(extra);
			
			if (result == null || result.length < 2) 
			{
				LogUtil.e("DialogBroadcastReceiver", "onReceive", "取回的错误码出错，不能弹出提示框");
				return;
			}
			
			if (!ext2.equals("")) 
			{
				if (result[0].equals(ErrorCode.ERROR_AUTH_PLUG_IN_EXCEPTION)
						|| result[0]
								.equals(ErrorCode.ERROR_READ_MAC_EXCEPTION)
						|| result[0]
								.equals("51004")
						|| result[0]
								.equals(ErrorCode.ERROR_STB_ILLEGAL_EXCEPTION)) 
				{
					result[0] = ext2;
				}
			}
			System.out.println("bbbbbbbbbbbbbbbb: " + result[0]);
			DialogTools.creater().showDialog(activity, result[0], result[1], ext1, ext2);
		}
	}
}
