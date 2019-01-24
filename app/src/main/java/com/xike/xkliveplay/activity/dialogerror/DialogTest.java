/**
 * @Title: DialogTest.java
 * @Package com.xike.xkliveplay.activity.dialogerror
 * @Description: TODO
 * Copyright: Copyright (c) 2015
 * Company:青岛博瑞立方网络科技有限公司
 * 
 * @author Mernake
 * @date 2017年5月4日 上午9:53:17
 * @version V1.0
 */
package com.xike.xkliveplay.activity.dialogerror;

import com.xike.xkliveplay.R;
import com.xike.xkliveplay.activity.launch.ActivityLaunch;
import com.xike.xkliveplay.framework.error.ErrorBroadcastAction;
import com.xike.xkliveplay.framework.error.SendBroadcastTools;

import android.app.Activity;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.KeyEvent;

/**
  * @ClassName: DialogTest
  * @Description: TODO
  * @author Mernake
  * @date 2017年5月4日 上午9:53:17
  *
  */
public class DialogTest extends Activity
{
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.errortest_main);
		
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) 
	{
		switch (keyCode) 
		{
		
		case KeyEvent.KEYCODE_0:
			SendBroadcastTools.sendErrorBroadcast(getApplicationContext(), ErrorBroadcastAction.ERROR_INTERNET_ACTION, "00:11:22:33:44:55", "13969870533");
			break;
		case KeyEvent.KEYCODE_1:
			SendBroadcastTools.sendErrorBroadcast(getApplicationContext(), ErrorBroadcastAction.ERROR_AUTH_PLUG_IN_ACTION, "00:11:22:33:44:55", "13969870533");
			break;
		case KeyEvent.KEYCODE_2:
			SendBroadcastTools.sendErrorBroadcast(getApplicationContext(), ErrorBroadcastAction.ERROR_READ_MAC_ACTION, "00:11:22:33:44:55", "13969870533");
			break;
		case KeyEvent.KEYCODE_3:
			SendBroadcastTools.sendErrorBroadcast(getApplicationContext(), ErrorBroadcastAction.ERROR_GET_ROOT_CATEGORY_ID_ACTION, "00:11:22:33:44:55", "13969870533");
			break;
		case KeyEvent.KEYCODE_4:
			SendBroadcastTools.sendErrorBroadcast(getApplicationContext(), ErrorBroadcastAction.ERROR_GET_CATEGORY_ACTION, "00:11:22:33:44:55", "13969870533");
			break;
		case KeyEvent.KEYCODE_5:
			SendBroadcastTools.sendErrorBroadcast(getApplicationContext(), ErrorBroadcastAction.ERROR_GET_CHANNEL_LIST_ACTION, "00:11:22:33:44:55", "13969870533");
			break;
		case KeyEvent.KEYCODE_6:
			SendBroadcastTools.sendErrorBroadcast(getApplicationContext(), ErrorBroadcastAction.ERROR_NOT_BUY_ACTION, "00:11:22:33:44:55", "13969870533");
			break;
		case KeyEvent.KEYCODE_7:
			SendBroadcastTools.sendErrorBroadcast(getApplicationContext(), ErrorBroadcastAction.ERROR_AUTH_EPG_ACTION, "00:11:22:33:44:55", "13969870533");
			break;
		case KeyEvent.KEYCODE_8:
			SendBroadcastTools.sendErrorBroadcast(getApplicationContext(), ErrorBroadcastAction.ERROR_STB_ILLEGAL_ACTION, "00:11:22:33:44:55", "13969870533");
			break;
		case KeyEvent.KEYCODE_9:
			SendBroadcastTools.sendErrorBroadcast(getApplicationContext(), ErrorBroadcastAction.ERROR_GET_SAVE_LIST_ACTION, "00:11:22:33:44:55", "13969870533");
			break;
		case KeyEvent.KEYCODE_DPAD_UP:
			SendBroadcastTools.sendErrorBroadcast(getApplicationContext(), ErrorBroadcastAction.ERROR_ACCOUNT_SYNCHRONOUS_ACTION, "00:11:22:33:44:55", "13969870533");
			break;
		case KeyEvent.KEYCODE_DPAD_DOWN:
			SendBroadcastTools.sendErrorBroadcast(getApplicationContext(), ErrorBroadcastAction.ERROR_ACTIVE_ACOUNT_ACTION, "00:11:22:33:44:55", "13969870533");
			break;
		case KeyEvent.KEYCODE_DPAD_LEFT:
			SendBroadcastTools.sendErrorBroadcast(getApplicationContext(), ErrorBroadcastAction.ERROR_WRITE_STBINFO_ACTION, "00:11:22:33:44:55", "13969870533");
			break;
		case KeyEvent.KEYCODE_DPAD_RIGHT:
			SendBroadcastTools.sendErrorBroadcast(getApplicationContext(), ErrorBroadcastAction.ERROR_DNS_ACTION, "00:11:22:33:44:55", "13969870533");
			break;
		case KeyEvent.KEYCODE_CHANNEL_UP:
			SendBroadcastTools.sendErrorBroadcast(getApplicationContext(), ErrorBroadcastAction.ERROR_UNKNOWN_ACTION, "00:11:22:33:44:55", "13969870533");
			break;
		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	
//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) 
//	{
//		switch (keyCode) 
//		{
//		
//		case KeyEvent.KEYCODE_0:
//			SendBroadcastTools.sendErrorBroadcast(getApplicationContext(), ErrorBroadcastAction.ERROR_UNKNOWN_ACTION, "00:11:22:33:44:55", "13969870533");
//			break;
//		case KeyEvent.KEYCODE_1:
//			SendBroadcastTools.sendErrorBroadcast(getApplicationContext(), ErrorBroadcastAction.ERROR_READ_MAC_ACTION, "00:11:22:33:44:55", "13969870533");
//			break;
//		case KeyEvent.KEYCODE_2:
//			SendBroadcastTools.sendErrorBroadcast(getApplicationContext(), ErrorBroadcastAction.ERROR_STB_ILLEGAL_ACTION, "00:11:22:33:44:55", "13969870533");
//			break;
//		case KeyEvent.KEYCODE_3:
//			SendBroadcastTools.sendErrorBroadcast(getApplicationContext(), ErrorBroadcastAction.ERROR_WRITE_STBINFO_ACTION, "00:11:22:33:44:55", "13969870533");
//			break;
//		case KeyEvent.KEYCODE_4:
//			SendBroadcastTools.sendErrorBroadcast(getApplicationContext(), ErrorBroadcastAction.ERROR_DNS_ACTION, "00:11:22:33:44:55", "13969870533");
//			break;
//		case KeyEvent.KEYCODE_5:
//			SendBroadcastTools.sendErrorBroadcast(getApplicationContext(), ErrorBroadcastAction.ERROR_UNKNOWN_ACTION, "00:11:22:33:44:55", "13969870533");
//			break;
//		case KeyEvent.KEYCODE_6:
//			SendBroadcastTools.sendErrorBroadcast(getApplicationContext(), ErrorBroadcastAction.ERROR_NOT_BUY_ACTION, "00:11:22:33:44:55", "13969870533");
//			break;
//		case KeyEvent.KEYCODE_7:
//			SendBroadcastTools.sendErrorBroadcast(getApplicationContext(), ErrorBroadcastAction.ERROR_DNS_ACTION, "00:11:22:33:44:55", "13969870533");
//			break;
//		case KeyEvent.KEYCODE_8:
//			SendBroadcastTools.sendErrorBroadcast(getApplicationContext(), ErrorBroadcastAction.ERROR_GET_CATEGORY_ACTION, "00:11:22:33:44:55", "13969870533");
//			break;
//		case KeyEvent.KEYCODE_9:
//			SendBroadcastTools.sendErrorBroadcast(getApplicationContext(), ErrorBroadcastAction.ERROR_GET_CHANNEL_LIST_ACTION, "00:11:22:33:44:55", "13969870533");
//			break;
//		case KeyEvent.KEYCODE_DPAD_UP:
//			SendBroadcastTools.sendErrorBroadcast(getApplicationContext(), ErrorBroadcastAction.ERROR_GET_PRODUCT_ID_ACTION, "00:11:22:33:44:55", "13969870533");
//			break;
//		case KeyEvent.KEYCODE_DPAD_DOWN:
//			SendBroadcastTools.sendErrorBroadcast(getApplicationContext(), ErrorBroadcastAction.ERROR_GET_ROOT_CATEGORY_ID_ACTION, "00:11:22:33:44:55", "13969870533");
//			break;
//		case KeyEvent.KEYCODE_DPAD_LEFT:
//			SendBroadcastTools.sendErrorBroadcast(getApplicationContext(), ErrorBroadcastAction.ERROR_GET_SAVE_LIST_ACTION, "00:11:22:33:44:55", "13969870533");
//			break;
//		case KeyEvent.KEYCODE_DPAD_RIGHT:
//			SendBroadcastTools.sendErrorBroadcast(getApplicationContext(), ErrorBroadcastAction.ERROR_GET_SCHEDULE_LIST_ACTION, "00:11:22:33:44:55", "13969870533");
//			break;
//		default:
//			break;
//		}
//		
//		
//		return super.onKeyDown(keyCode, event);
//	}
	
	
	private DialogBroadcastReceiver receiver = null;
	private void regReceiver()
	{
		receiver  = new DialogBroadcastReceiver(this);
		IntentFilter filter = new IntentFilter();
		filter.addAction(ErrorBroadcastAction.ERROR_LIVEPLAY_ACTION);
		this.registerReceiver(receiver, filter);
	}
	
	private void unRegReceiver()
	{
		if (receiver != null) 
		{
			this.unregisterReceiver(receiver);
			receiver = null;
		}
	}
	
	@Override
	protected void onResume() 
	{
		super.onResume();
		regReceiver();
	}
	
	@Override
	protected void onStop() 
	{
		super.onStop();
		unRegReceiver();
	}
}
