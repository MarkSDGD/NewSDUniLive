/**
 * @Title: ZZProductItemView.java
 * @Package com.shandong.shandonglive.zengzhi.ui
 * @Description: TODO
 * Copyright: Copyright (c) 2015
 * Company:青岛博瑞立方网络科技有限公司
 * 
 * @author Mernake
 * @date 2018年7月9日 下午3:25:45
 * @version V1.0
 */
package com.shandong.shandonglive.zengzhi.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xike.xkliveplay.R;
import com.xike.xkliveplay.gd.LogUtils;


/**
  * @ClassName: ZZProductItemView
  * @Description: 产品包item
  * @author Mernake
  * @date 2018年7月9日 下午3:25:45
  *
  */
public class ZZProductItemView extends RelativeLayout
{
	private Context context;
	private ZZProduct product;
	
	private TextView tvProductName;
	private TextView tvPriceLeft;
	private ZZProductPriceView priceView;
	private TextView tvPriceRight;
	private TextView tvDeleteText;
	private TextView tvDate;

	private String errorCode = "";
	
	
	private TextView tvProductInfo;

	private Button btn;
	
	public ZZProductItemView(Context context) 
	{
		super(context);
		this.context = context;
		inflate(context, R.layout.zz_product_item_view_main, this);
		initView();
	}

	public ZZProductItemView(Context context, AttributeSet attrs) 
	{
		super(context, attrs);
		this.context = context;
		inflate(context, R.layout.zz_product_item_view_main, this);
		initView();

	}
	
	
	private void initView() 
	{
		tvProductName = (TextView) findViewById(R.id.zz_tv_productitem_name);
		tvPriceLeft = (TextView) findViewById(R.id.zz_tv_productitem_price_left);
		priceView = (ZZProductPriceView) findViewById(R.id.zzprice);
		tvPriceRight = (TextView) findViewById(R.id.zz_tv_productitem_price_right);
		tvDeleteText = (TextView) findViewById(R.id.zz_tv_deletetext);
		tvDate = (TextView) findViewById(R.id.zz_tv_produtct_date);
		tvProductInfo = (TextView) findViewById(R.id.zz_tv_product_info);

		btn = (Button) findViewById(R.id.zz_btn_productitem);

		btn.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				ZZPayDialog dialog = new ZZPayDialog(context, R.style.MyDialog_bgdim);
				dialog.setiNeedDismiss(new ZZPayDialog.INeedDismiss() {
					@Override
					public void needDismiss(boolean isNeedDismiss)
					{
						if (isNeedDismiss){
							((Activity)context).finish();
						}
					}
				});
				Window win = dialog.getWindow();
				win.setBackgroundDrawableResource(R.color.transparent_background);
				android.view.WindowManager.LayoutParams params = new android.view.WindowManager.LayoutParams();
				Bundle bundle  = new Bundle();
				params.gravity = Gravity.CENTER;
				win.setAttributes(params);
				dialog.show();

				if ("51041-507".equals(errorCode) || "51041-506".equals(errorCode)) {
					LogUtils.i("xumin", "playAuth: " + "点击了errorCode: " + errorCode);
					dialog.refreshData(product, errorCode);
				}else {//else这种情况，理论上不会发生的
					dialog.refreshData(product);
				}
			}
		});
		
		
		tvProductName.setTypeface(TypefaceTools.getYuanTiFont(getContext()));
		tvProductInfo.setTypeface(TypefaceTools.getYuanTiFont(getContext()));
		tvPriceRight.setTypeface(TypefaceTools.getYuanTiFont(getContext()));
		tvPriceLeft.setTypeface(TypefaceTools.getYuanTiFont(getContext()));
		
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}


	public void refreshData(ZZProduct product)
	{
		this.product = product;
		String temp = product.getName();//定义这个和下面的if都是容错的
		if (!"".equals(temp) && null != temp){
			tvProductName.setText(temp);
		}

		temp = product.getPriceLeft();
		if (!"".equals(temp) && null != temp) {
			tvPriceLeft.setText(temp);
		}
		temp = product.getPriceRight();
		if (!"".equals(temp) && null != temp) {
			tvPriceRight.setText(temp);
		}
		temp = product.getPrice();
		if (!"".equals(temp) && null != temp){
			priceView.setPrice(temp);
		}
		temp = product.getOriPrice();
		if (!"".equals(temp) && null != temp){
			tvDeleteText.setText(temp);
		}
		temp = product.getDate();
		if (!"".equals(temp) && null != temp){
			tvDate.setText(temp);
		}
		temp = product.getInfo();
		if (!"".equals(temp) && null != temp){
			tvProductInfo.setText(temp);
		}
		if (btn!=null) btn.setTag(product);
	}
	public void refreshData(ZZProduct product, String errorCode)
	{
		this.product = product;
		String temp = product.getName();//定义这个和下面的if都是容错的
		if (!"".equals(temp) && null != temp){
			tvProductName.setText(temp);
		}

		temp = product.getPriceLeft();
		if (!"".equals(temp) && null != temp) {
			tvPriceLeft.setText(temp);
		}
		temp = product.getPriceRight();
		if (!"".equals(temp) && null != temp) {
			tvPriceRight.setText(temp);
		}
		temp = product.getPrice();
		if (!"".equals(temp) && null != temp){
			priceView.setPrice(temp);
		}
		temp = product.getOriPrice();
		if (!"".equals(temp) && null != temp){
			tvDeleteText.setText(temp);
		}
		temp = product.getDate();
		if (!"".equals(temp) && null != temp){
			tvDate.setText(temp);
		}
		temp = product.getInfo();
		if (!"".equals(temp) && null != temp){
			tvProductInfo.setText(temp);
		}
		if (btn!=null) btn.setTag(product);

		this.errorCode = errorCode;
	}

	public void setFocusChangeListener(OnFocusChangeListener listener)
	{
		if (btn!=null) btn.setOnFocusChangeListener(listener);
	}


}
