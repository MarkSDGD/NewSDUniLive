/**
 * @Title: LivePlayBackService.java
 * @Package com.xike.xkliveplay.framework.service
 * @Description: TODO
 * Copyright: Copyright (c) 2015
 * Company:青岛博瑞立方网络科技有限公司
 * 
 * @author Mernake
 * @date 2016年5月30日 上午10:01:08
 * @version V1.0
 */
package com.xike.xkliveplay.framework.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.xike.xkliveplay.R;
import com.xike.xkliveplay.activity.ActivityLaunchBase;
import com.xike.xkliveplay.framework.entity.LiveUpgrageResponse;
import com.xike.xkliveplay.framework.http.IUpdateData;
import com.xike.xkliveplay.framework.httpclient.HttpGetUpdate;
import com.xike.xkliveplay.framework.httpclient.VarParam;
import com.xike.xkliveplay.framework.tools.APKTools;

import android.R.integer;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.Message;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

/**
 * @ClassName: LivePlayBackService
 * @Description: 直播不需要时效性的接口请求，放入后台
 * 包括：升级接口，请求直播根栏目ID，请求全部分类栏目ID，请求全部频道，请求收藏接口
 * @author Mernake
 * @date 2016年5月30日 上午10:01:08
 * 
 * 
 *
 */
public class LivePlayBackService extends Service implements IUpdateData
{

	private ProgressDialog pBar;
	private UpdateDialog updateDialog;
	
	@Override
	public void onCreate() 
	{
		super.onCreate();
//		regInterfaceReceiver();
		/**先判断是否/data/data/com.xike.xkliveplay/files/xklive.apk存在而且版本值比当前版本高**/
//		int updateCode = APKTools.getVersionCodeFromPath(getFilesDir().getAbsolutePath() + "/xklive.apk", getApplicationContext());
//		int localCode = APKTools.getIntVersionCode(getApplicationContext());
//		
//		if (updateCode>localCode) //说明本地已经有新版本APK了，可能是上次用户并没有点击升级 
//		{
//			dealWithUpdate(null, true);
//		}
		
		
	}
	
	@Override
	public void onDestroy() 
	{
		super.onDestroy();
		System.out.println("###服务销毁");
		if (updateDialog != null) 
		{
			updateDialog.stop();
		}
//		unRegInterfaceReceiver();
	}
	
	private BroadcastReceiver receiver = new BroadcastReceiver()
	{
		@Override
		public void onReceive(Context context, Intent intent) 
		{
			if (intent.getAction().equals(LiveAction.ACTION_REQUEST_UPDATE)) 
			{
				NetAction.requestUpdate(LivePlayBackService.this,LivePlayBackService.this);
			}
		}
		
	};
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) 
	{
		NetAction.requestUpdate(LivePlayBackService.this,LivePlayBackService.this);
		return super.onStartCommand(intent, flags, startId);
	}
	
	private void regInterfaceReceiver()
	{
		IntentFilter filter = new IntentFilter();
		filter.addAction(LiveAction.ACTION_REQUEST_UPDATE);
		registerReceiver(receiver, filter);
	}
	
	private void unRegInterfaceReceiver()
	{
		unregisterReceiver(receiver);
	}
	
	
	@Override
	public void updateData(String method, String uniId, Object object,
			boolean isSuccess) 
	{
		if (method.equals(HttpGetUpdate.Method)) 
		{
			if (object == null || isSuccess == false || ((LiveUpgrageResponse)object).getUrl() == null || ((LiveUpgrageResponse)object).getUrl().length() < 5) 
			{
				return;
			}
//			/**根据must来确定选用何种升级形式**/
//		LiveUpgrageResponse object2 = new LiveUpgrageResponse();
//		object2.setUrl("abcdefghijklmn");
//		object2.setIsMust("1");
//		object2.setNewVersion("CM-V1.10");
			dealWithUpdate((LiveUpgrageResponse) object,false);
		}
	}
	
	public void dealWithUpdate(LiveUpgrageResponse object,boolean isHasApk) 
	{
		if (object.getUrl() != null && object.getUrl().length() > 5) 
		{
			if (object.getIsMust().contains("0")) 
			{
				VarParam.isForceUpdate = false;
			}else {
				VarParam.isForceUpdate = true;
			}
			updateDialog = new UpdateDialog(getApplicationContext(),R.style.MyDialog);
			Window win = updateDialog.getWindow();
			win.getDecorView().setPadding(0, 0, 0, 0);
			WindowManager.LayoutParams lp = win.getAttributes();
			lp.width = 632;
			lp.height = 241;
			lp.gravity = Gravity.CENTER;
			win.setAttributes(lp);
			updateDialog.init(object);
		}
	}
	
	@Override
	public IBinder onBind(Intent intent) 
	{
		return null;
	}
	

}
