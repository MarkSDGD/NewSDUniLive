package com.shandong.shandonglive.zengzhi.ui.gd;


import android.content.Context;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager.LayoutParams;

import com.xike.xkliveplay.R;
import com.xike.xkliveplay.framework.error.ErrorBroadcastAction;
import com.xike.xkliveplay.framework.error.SendBroadcastTools;


/**
 * @author LiWei <br>
 * CreateTime: 2015年2月26日 上午11:01:56<br>
 * <b>Info:</b><br>
 *<br>
 *   <b>Method:</b> <br>
 *   <b>Public:</b> <br>
 *   <b>Private:</b> <br>
 */
public class GDDialogTools
{
//	private MyDialog myDialog = null;
	private GDErrorDialog myDialog = null;
	public static GDDialogTools dialogTools = null;
	
	public static GDDialogTools creater()
	{
		if (dialogTools == null) 
		{
			dialogTools = new GDDialogTools();
		}
		return dialogTools;
	}
	
	
	public void showDialog(Context context,String errorCode,String errorMsg,String extraCode,String extraMsg)
	{
		SendBroadcastTools.sendErrorBroadcast(context, ErrorBroadcastAction.ERROR_LIVEPLAY_CANCEL_DIALOG,"","");
		cancelDialog();
//		myDialog = new MyDialog(context, errorCode, errorMsg, extraCode, extraMsg,R.style.MyDialog);
		myDialog = new GDErrorDialog(context, errorCode, errorMsg, extraCode, extraMsg, R.style.MyDialog);
		Window win = myDialog.getWindow();
		win.setBackgroundDrawableResource(com.xike.xkliveplay.R.color.transparent_background);
		LayoutParams params = new LayoutParams();
//		params.type = WindowManager.LayoutParams.TYPE_TOAST;
		params.gravity = Gravity.CENTER;
		win.setAttributes(params);
		myDialog.show();
	}
	
	public void cancelDialog()
	{
		if (myDialog != null) 
		{
			myDialog.cancel();
			myDialog = null;
		}
	}
	
	
}
