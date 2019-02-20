package com.xike.xkliveplay.framework.tools;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.xike.xkliveplay.framework.error.ErrorBroadcastAction;
import com.xike.xkliveplay.framework.error.SendBroadcastTools;

/**
 * @author LiWei <br>
 * CreateTime: 2014��10��20�� ����9:39:05<br>
 * <b>Info:</b><br>
 *<br>
 *   <b>Method:</b> <br>
 */
public class NetStatusChange 
{
	private static final String tag = NetStatusChange.class.getSimpleName();
	private INetStatusChanged iChanged = null;
	public static NetStatusChange netStatusChange = null;
	
	public String PPPOE_ACTION = "PPPOE_STATE_CHANGED";
	private String PPPOE_STATE = "pppoe_state";
	private int PPPOE_STATE_DISCONNECT = 2;
	private int PPPOE_STATE_CONNECT = 1;
	
	private boolean isReg = false;
	
	public boolean isRegReceiver ()
	{
		return isReg;
	}
	
	public static NetStatusChange getInstance()
	{
		if (netStatusChange == null) 
		{
			netStatusChange = new NetStatusChange();
		}
		return netStatusChange;
	}
	
	public void reg(Context context)
	{
		if (!isReg) 
		{
		  IntentFilter intentFilter = new IntentFilter();
		  intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		  intentFilter.addAction(PPPOE_ACTION);
		  context.registerReceiver(connReceiver, intentFilter);
		  isReg = true;
		}
	}
	
	public void unReg(Context context)
	{
		if (isReg) 
		{
			context.unregisterReceiver(connReceiver);
			isReg = false;
		}
	}
	public void deInit(){
		netStatusChange = null;
		isReg=false;
	}
	private BroadcastReceiver connReceiver = new BroadcastReceiver() 
	{
		@Override
		public void onReceive(Context context, Intent intent) 
		{
			Log.i("MARK"," connReceiver onReceive intent=="+intent);
			if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) 
			{
				Log.i("MARK"," connReceiver onReceive isNetworkConnected=="+isNetworkConnected(context));
				if (!isNetworkConnected(context)) 
				 {
					 SendBroadcastTools.sendErrorBroadcast(context, ErrorBroadcastAction.ERROR_INTERNET_ACTION,"","");
				 }else {
					 SendBroadcastTools.sendErrorBroadcast(context, ErrorBroadcastAction.ERROR_LIVEPLAY_CANCEL_DIALOG,"","");
					 if (iChanged != null) 
					 {
						 iChanged.onNetStatusChange(true);
						 iChanged = null;
					 }
				}
			}else if (intent.getAction().equals(PPPOE_ACTION)) 
			{
				int state = intent.getIntExtra(PPPOE_STATE, -1); 
				if (state == -1) 
				{
					return;
				}else if (state == PPPOE_STATE_CONNECT) 
				{
					SendBroadcastTools.sendErrorBroadcast(context, ErrorBroadcastAction.ERROR_LIVEPLAY_CANCEL_DIALOG,"","");
					 if (iChanged != null) 
					 {
						 iChanged.onNetStatusChange(true);
						 iChanged = null;
					 }
				}else if (state == PPPOE_STATE_DISCONNECT) 
				{
					SendBroadcastTools.sendErrorBroadcast(context, ErrorBroadcastAction.ERROR_INTERNET_ACTION,"","");
				}
			}
			 
		}
	};
	
	
	
	
	public void setiChanged(INetStatusChanged iChanged) 
	{
		this.iChanged = iChanged;
	}

	/**
	 * function: Check network.
	 * @param
	 * @return
	 */
	public boolean checkNet(Context context) 
	{
		if (!isNetworkConnected(context)) 
		{
//			DialogError.getInstance().showDialog(context, ErrorCode.ERROR_INTERNET_EXCEPTION);
			LogUtil.e(tag, "checkNet:","the internet disconnect failed");
			return false;
		}
		return true;
	}
	
	/**Check is network ok**/
	private boolean isNetworkConnected(Context context) 
	{  
	    if (context != null) 
	    {  
	        ConnectivityManager mConnectivityManager = (ConnectivityManager) context  
	                .getSystemService(Context.CONNECTIVITY_SERVICE);  
	        NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
	        if (mNetworkInfo != null) 
	        {  
	            return mNetworkInfo.isAvailable();  
	        }  
	    }  
	    return false;  
	}
	
	public interface INetStatusChanged
	{
		public void onNetStatusChange(boolean isConnected);
	}
}
