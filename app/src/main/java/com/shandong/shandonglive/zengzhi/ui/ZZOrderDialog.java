/**
 * @Title: ZZOrderDialog.java
 * @Package com.shandong.shandonglive.zengzhi.ui
 * @Description: TODO
 * Copyright: Copyright (c) 2015
 * Company:青岛博瑞立方网络科技有限公司
 * 
 * @author Mernake
 * @date 2018年7月11日 上午9:50:22
 * @version V1.0
 */
package com.shandong.shandonglive.zengzhi.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.xike.xkliveplay.R;


/**
  * @ClassName: ZZOrderDialog
  * @Description: TODO
  * @author Mernake
  * @date 2018年7月11日 上午9:50:22
  *
  */
public class ZZOrderDialog extends Dialog
{
	
	private TextView channelName;
	private TextView tvAccount;

	private String channelId;
	private String categoryId;
	private String errorCode = "";

	private Button zzOrderDialogBack;;
	private Button zzOrderDialogOrder;
	
	public ZZOrderDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
	}

	public ZZOrderDialog(Context context, int themeResId) {
		super(context, themeResId);
	}

	public ZZOrderDialog(Context context) {
		super(context);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.zz_order_dialog_main);
		initView();
	}

	private void initView() 
	{
		TextView tvHint = (TextView) findViewById(R.id.zz_tv_order_hint);
		tvAccount = (TextView) findViewById(R.id.zz_tv_order_account);
		channelName = (TextView) findViewById(R.id.zz_tv_order_channelname);
		TextView text1 = (TextView) findViewById(R.id.zz_tv_order_text1);
		TextView text2 = (TextView) findViewById(R.id.zz_tv_order_text2);

		tvHint.setTypeface(TypefaceTools.getYuanTiFont(getContext()));
		tvAccount.setTypeface(TypefaceTools.getYuanTiFont(getContext()));
		channelName.setTypeface(TypefaceTools.getYuanTiFont(getContext()));
		text1.setTypeface(TypefaceTools.getYuanTiFont(getContext()));
		text2.setTypeface(TypefaceTools.getYuanTiFont(getContext()));
		zzOrderDialogBack = (Button) findViewById(R.id.zz_order_dialog_btn_back);
		zzOrderDialogOrder = (Button) findViewById(R.id.zz_order_dialog_btn_order);
		zzOrderDialogOrder.requestFocus();

		zzOrderDialogBack.setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				dismiss();
			}
		});

		zzOrderDialogOrder.setOnClickListener(new View.OnClickListener()
		{
			
			@Override
			public void onClick(View v) 
			{
				Intent intent = new Intent(getContext(),ZZChooseProductActivity.class);
				intent.putExtra("channelid",channelId);
				intent.putExtra("categoryid",categoryId);
				intent.putExtra("errorcode", errorCode);
				getContext().startActivity(intent);
				dismiss();
			}
		});
	}

	public void refreshText(String name,String userId)
	{
		tvAccount.setText(userId);
		channelName.setText(name);
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

}
