package com.xike.xkliveplay.activity.fragment;

import com.xike.xkliveplay.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/**
 * @author Mernake <br>
 * CreateTime: 2015年5月19日 下午2:54:43<br>
 * <b>Info:</b><br>
 *<br>
 *   <b>Method:</b> <br>
 *   <b>Public:</b> <br>
 *   <b>Private:</b> <br>
 */
public class DialogSave extends Dialog
{
	
private Context mContext;
	
	public Button btnOk = null;
	public Button btnCancel = null;
	private String msg = "";
	private TextView tViewInfo = null;
	
	private IFragmentDialog iDialog = null;

	public DialogSave(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		// TODO Auto-generated constructor stub
	}

	public DialogSave(Context context, int theme,String msg,IFragmentDialog _iDialog) 
	{
		super(context, theme);
		this.msg = msg;
		this.iDialog = _iDialog;
		// TODO Auto-generated constructor stub
	}

	public DialogSave(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	
		@Override
		protected void onCreate(Bundle savedInstanceState) 
		{
			super.onCreate(savedInstanceState);
			setContentView(R.layout.my_dialoge);
			btnOk = (Button)findViewById(R.id.btn_ok);
			btnCancel = (Button)findViewById(R.id.btn_cancel);
			btnOk.setFocusable(true);
			tViewInfo = (TextView)findViewById(R.id.tv_info);
			tViewInfo.setText(msg);
			
			

			btnOk.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					iDialog.onKeyDownConfirm();
					
					dismiss();
				}
			});
			
			btnCancel.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					dismiss();
				}
			});
		}
	
		interface IFragmentDialog
		{
			public void onKeyDownConfirm();
		}
}
