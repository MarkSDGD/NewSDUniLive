/**
 * @Title: UpdateDialog.java
 * @Package com.xike.xkliveplay.framework.service
 * @Description: TODO
 * Copyright: Copyright (c) 2015
 * Company:青岛博瑞立方网络科技有限公司
 * 
 * @author Mernake
 * @date 2016年6月6日 下午2:30:44
 * @version V1.0
 */
package com.xike.xkliveplay.framework.service;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.xike.xkliveplay.R;
import com.xike.xkliveplay.framework.entity.LiveUpgrageResponse;
import com.xike.xkliveplay.framework.tools.APKTools;
import com.xike.xkliveplay.framework.varparams.Var;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @ClassName: UpdateDialog
 * @Description: TODO
 * @author Mernake
 * @date 2016年6月6日 下午2:30:44
 *
 */
public class UpdateDialog extends Dialog
{

	private Button btnUpdate = null;
	
	private LiveUpgrageResponse object = null;
	private String str = "尊敬的用户，发现直播%s新版本，点击更新按钮即可升级到最新版本";
	private String hint = "";
	/**Update live apk save path**/
	public String downloadApkPath = "";
	
	private Context ctx = null;
	
	
	public UpdateDialog(Context context) 
	{
		super(context);
	}

	public UpdateDialog(Context context, int theme) 
	{
		super(context, theme);
		this.ctx = context;
		getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
	}
	
	public void init(LiveUpgrageResponse obj)
	{
		if (ctx == null) return; 
		this.object = obj;
		downloadApkPath = ctx.getFilesDir().getAbsolutePath() + "/xklive.apk";	
		hint = String.format(str, object.getNewVersion());
		downFile(object.getUrl());
		
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_update);
		btnUpdate = (Button) findViewById(R.id.btn_update);
//		tvUpdateHint = (TextView) findViewById(R.id.tv_update_hint);
		btnUpdate.setOnClickListener(listener);
	}
	
	/*
	  * <p>Title: onKeyDown</p>
	  * <p>Description: </p>
	  * @param keyCode
	  * @param event
	  * @return
	  * @see android.app.Dialog#onKeyDown(int, android.view.KeyEvent)
	  */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) 
	{
		if (keyCode == KeyEvent.KEYCODE_BACK) 
		{
			return true;
		}
		
		return super.onKeyDown(keyCode, event);
	}
	
	private  android.view.View.OnClickListener listener = new android.view.View.OnClickListener() 
	{
		@Override
		public void onClick(View v) 
		{
			update();
		}
	};

	public void downFile(final String url)
	{
		new Thread() {
			public void run()
			{
				System.out.println("###下载升级APK开始");
				HttpClient client = new DefaultHttpClient();
				HttpGet get = new HttpGet(url);
				HttpResponse response;
				try {
					response = client.execute(get);
					System.out.println("###response code:" + response.getStatusLine().getStatusCode());
					if (response.getStatusLine().getStatusCode() == 200) {
						HttpEntity entity = response.getEntity();
						long length = entity.getContentLength();
						//                Message msg = Message.obtain();
						//                msg.arg1 = (int)length;
						//                msg.what = MSG_UPDATE_INIT_PROGRESSBAR;
						//                handler.sendMessage(msg);
						InputStream is = entity.getContent();
						FileOutputStream fileOutputStream = null;
						if (is != null) {
							@SuppressWarnings("unused")
							File file = new File(downloadApkPath);
							fileOutputStream = getContext().openFileOutput("xklive.apk", Context.MODE_WORLD_READABLE);
							byte[] buf = new byte[1024];
							int ch = -1;
							int count = 0;
							while ((ch = is.read(buf)) != -1) {
								fileOutputStream.write(buf, 0, ch);
								count += ch;

								//                        msg = Message.obtain();
								//                        msg.arg1 = count;
								//                        msg.what = MSG_UPDATE_PROGRESS_UPDATE;
								//                        handler.sendMessage(msg);
								if (count > length) {
									break;
								}
							}
						}
						fileOutputStream.flush();
						if (fileOutputStream != null) {
							fileOutputStream.close();
						}
						//关闭一下输入流
						if (is != null) {
							is.close();
						}
						System.out.println("###升级APK下载完成");
						handler.sendEmptyMessage(0);
					}

				} catch (ClientProtocolException e)
				{
					e.printStackTrace();
				} catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}.start();
	}
	private int i = 0;
	
	public void stop()
	{
		ctx = null;
	}
	
	private Handler handler = new Handler()
	{

		@Override
		public void handleMessage(Message msg) 
		{
			if (msg.what == 0) 
			{
				if (ctx == null) return;
				//这里是要不弹框，等apk也校验通过，才弹出
				if (!APKTools.isAPKComplete(ctx, downloadApkPath))
				{
					System.out.println("###APK下载不完整");
					return;
				}
				if (Var.isActivityLaunched) 
				{
					show();
				}	
			}
		}
		
	};
	
	public void update() 
	{
		dismiss();
		/*if (ctx == null) return;
		if (!APKTools.isAPKComplete(ctx, downloadApkPath)) 
		{
			return;
		}
		*/
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(downloadApkPath)),
                "application/vnd.android.package-archive");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ctx.startActivity(intent);
	}
	
}
