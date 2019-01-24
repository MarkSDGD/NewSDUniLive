package com.xike.xkliveplay.activity.dialogerror;


import com.xike.xkliveplay.R;

import android.content.Context;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;


/**
 * @author LiWei <br>
 * CreateTime: 2015年2月26日 上午11:01:56<br>
 * <b>Info:</b><br>
 *<br>
 *   <b>Method:</b> <br>
 *   <b>Public:</b> <br>
 *   <b>Private:</b> <br>
 */
public class DialogTools 
{
	private MyDialog myDialog = null;
	public static DialogTools dialogTools = null;
	
	public static DialogTools creater()
	{
		if (dialogTools == null) 
		{
			dialogTools = new DialogTools();
		}
		return dialogTools;
	}
	
	
	public void showDialog(Context context,String errorCode,String errorMsg,String extraCode,String extraMsg)
	{
		cancelDialog();
		myDialog = new MyDialog(context, errorCode, errorMsg, extraCode, extraMsg,R.style.MyDialog);
		Window win = myDialog.getWindow();
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
