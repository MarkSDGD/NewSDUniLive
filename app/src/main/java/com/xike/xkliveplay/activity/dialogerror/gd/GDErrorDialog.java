package com.xike.xkliveplay.activity.dialogerror.gd;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.xike.xkliveplay.R;


public class GDErrorDialog extends Dialog {

	private String errorCode = "";
	private String errorMsg = "";
	private String extraCode = "";
	private String extraMsg = "";

	private String userId = "";
	private String mac = "";
	private String userId_format = "\n账 号：%s";
	private String mac_format = "\nMAC：%s";


	private TextView tv_title;//tv_error_dialog_title
	private TextView tv_name; //tv_error_dialog_appname
	private TextView tv_code; //tv_error_dialog_errorcode
	private TextView tv_msg; //tv_error_dialog_errortext
	//private Button btn_ok; //btn_error_dialog_ok

	public GDErrorDialog(Context context, boolean cancelable,
                         OnCancelListener cancelListener)
	{
		super(context, cancelable, cancelListener);
	}

	public GDErrorDialog(Context context, int theme)
	{
		super(context, theme);
	}

	public GDErrorDialog(Context context)
	{
		super(context);
	}

	public GDErrorDialog(Context context, String errorCode, String errorMsg,
                         String extraCode, String extraMsg, int theme)
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
		setContentView(R.layout.error_dialog_layout);
		tv_title = (TextView) findViewById(R.id.tv_error_dialog_title);
		tv_name = (TextView) findViewById(R.id.tv_error_dialog_appname);
		tv_name.setText(this.extraCode);
		tv_code = (TextView) findViewById(R.id.tv_error_dialog_errorcode);
		tv_msg = (TextView) findViewById(R.id.tv_error_dialog_errortext);
		ImageButton btn = (ImageButton) findViewById(R.id.btn_error_dialog_ok);
		btn.setOnClickListener(new View.OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				System.out.println("dialog center key pressed!");
				dismiss();
			}
		});
		
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

		if (errorCode.equals("51018") || errorCode.equals("51019")) 
		{
			errorMsg = errorMsg + userId + mac;
		}else if (errorCode.equals("51003") || errorCode.equals("51004") || errorCode.equals("51012") || errorCode.equals("51002")) 
		{
			errorMsg = errorMsg + mac;
		}
		
		tv_code.setText("错误码：" + errorCode);
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
