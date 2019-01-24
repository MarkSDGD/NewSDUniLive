package com.xike.xkliveplay.activity.dialogerror;


import com.xike.xkliveplay.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.TextView;

/**
 * @author LiWei <br>
 * CreateTime: 2015年2月26日 上午11:08:32<br>
 * <b>Info:</b><br>
 *<br>
 *   <b>Method:</b> <br>
 *   <b>Public:</b> <br>
 *   <b>Private:</b> <br>
 */
public class MyDialog extends Dialog
{

	private String errorCode = "";
	private String errorMsg = "";
	private String extraCode = "";
	private String extraMsg = "";
	
	private String userId = "";
	private String mac = "";
	private String userId_format = "\n账 号：%s";
	private String mac_format = "\nMAC：%s";
	
	
	private TextView tv_code = null;
	private TextView tv_msg = null;
	
	
	
	public MyDialog(Context context, boolean cancelable,
			OnCancelListener cancelListener) 
	{
		super(context, cancelable, cancelListener);
	}

	public MyDialog(Context context, int theme) 
	{
		super(context, theme);
	}

	public MyDialog(Context context) 
	{
		super(context);
	}

	public MyDialog(Context context, String errorCode, String errorMsg,
			String extraCode, String extraMsg,int theme) 
	{
		super(context,theme);
		this.errorCode = errorCode;
		this.errorMsg = errorMsg;
		this.extraCode = extraCode;
		this.extraMsg = extraMsg;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_error_layout);
		tv_code = (TextView) findViewById(R.id.tv_dialog_error_code);
		tv_msg = (TextView) findViewById(R.id.tv_error_msg);
		
		setUserIdAndMac();
		changeStyle();
	}
	
	
	/**
	 * function: 配置userId和MAC地址 当错误码是51018或51019时，extraCode=userID extraMsg=mac地址
	 * @param
	 * @return
	 */
	private void setUserIdAndMac() 
	{
		userId = String.format(userId_format, extraCode);
		mac = String.format(mac_format, extraMsg);
	}

	/**
	 * function: 根据错误码不同，改变文字的样式，并配置相应提示文字。51018和51019需要特殊设置
	 * @param
	 * @return
	 */
	private void changeStyle() 
	{
		if (!errorCode.equals("51018") && !errorCode.equals("51019")) //字多的提示文字小，字少的提示文字大 
		{
			tv_msg.setTextSize(24);
		}else {
			tv_msg.setTextSize(20);
		}
		
		if (errorCode.equals("51018") || errorCode.equals("51019")) 
		{
			errorMsg = errorMsg + userId + mac;
		}else if (errorCode.equals("51003") || errorCode.equals("51004") || errorCode.equals("51012") || errorCode.equals("51002")) 
		{
			errorMsg = errorMsg + mac;
		}
		
		tv_code.setText(errorCode);
		tv_msg.setText(errorMsg);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) 
	{
		if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) 
		{
			System.out.println("dialog center key pressed!");
			cancel();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
