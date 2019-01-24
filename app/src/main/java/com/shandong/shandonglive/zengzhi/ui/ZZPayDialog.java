/**
 * @Title: ZZPayDialog.java
 * @Package com.shandong.shandonglive.zengzhi.ui
 * @Description: TODO
 * Copyright: Copyright (c) 2015
 * Company:青岛博瑞立方网络科技有限公司
 * 
 * @author Mernake
 * @date 2018年7月11日 上午10:54:32
 * @version V1.0
 */
package com.shandong.shandonglive.zengzhi.ui;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xike.xkliveplay.R;
import com.xike.xkliveplay.framework.entity.gd.GDOrdercmhCheckOrderRes;
import com.xike.xkliveplay.framework.entity.gd.GDOrdercmhOrderRes;
import com.xike.xkliveplay.framework.http.IUpdateData;
import com.xike.xkliveplay.framework.varparams.Var;
import com.xike.xkliveplay.gd.GDHttpTools;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


/**
  * @ClassName: ZZPayDialog
  * @Description: TODO
  * @author Mernake
  * @date 2018年7月11日 上午10:54:32
  *
  */
public class ZZPayDialog extends Dialog
{
	private Button btnPhonePay,btnZhiFuBao;
	private LinearLayout llInfoRoot;
	private RelativeLayout rlCode;

	/**0--话费，1--支付宝**/
	private int orderMode = 0;

	private String TEXT_HEAD_PHONEPAY = "话费支付，请确认订单信息：";
	private String TEXT_HEAD_ZHIFUBAO = "请使用支付宝扫码支付：";
	private INeedDismiss iNeedDismiss = null;
	
	private TextView tvHead,tvChannel,tvPackageName,tvType,tvDate,tvProductID,tvContentID,tvCodeText;
	
	private ImageView ivCode;

	public ZZPayDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
	}

	public ZZPayDialog(Context context, int themeResId) {
		super(context, themeResId);
	}


	public ZZPayDialog(Context context) {
		super(context);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.zz_pay_dialog_main);
		initView();
	}

    public INeedDismiss getiNeedDismiss() {
        return iNeedDismiss;
    }

    public void setiNeedDismiss(INeedDismiss iNeedDismiss) {
        this.iNeedDismiss = iNeedDismiss;
    }

    private void initView()
	{
		btnPhonePay = (Button) findViewById(R.id.zz_pay_dialog_btn_phonepay);
		btnZhiFuBao = (Button) findViewById(R.id.zz_pay_dialog_btn_zhifubao);
		
		llInfoRoot = (LinearLayout) findViewById(R.id.zz_ll_paydialog_info_root);
		
		tvHead = (TextView) findViewById(R.id.zz_tv_pay_hint);
		tvChannel = (TextView) findViewById(R.id.zz_tv_pay_channelname);
		tvPackageName = (TextView) findViewById(R.id.zz_tv_pay_packagename);
		tvType = (TextView) findViewById(R.id.zz_tv_pay_producttype);
		tvDate = (TextView) findViewById(R.id.zz_tv_pay_productdate);
		tvProductID = (TextView) findViewById(R.id.zz_tv_pay_productid);
		tvContentID = (TextView) findViewById(R.id.zz_tv_pay_contentid);
		
		rlCode = (RelativeLayout) findViewById(R.id.zz_rl_paydialog_code);
		tvCodeText = (TextView) findViewById(R.id.zz_tv_paydialog_codetext);
		ivCode = (ImageView) findViewById(R.id.zz_iv_paydialog_code);
		
		
		btnPhonePay.setOnFocusChangeListener(onFocusChangeListener);
		btnZhiFuBao.setOnFocusChangeListener(onFocusChangeListener);
		
		btnPhonePay.setOnClickListener(onClickListener);
		btnZhiFuBao.setOnClickListener(onClickListener);

		setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				isThreadStop = true;
			}
		});

		setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
				isThreadStop = true;
			}
		});
	}



	private ZZProduct product;
	public void refreshData(ZZProduct product)
	{
		this.product = product;
		tvChannel.setText(Var.getChannelNameByChannelId(product.getChannelid()));
		tvPackageName.setText(product.getName());
//		tvType.setText(product.getProductType()==0?"包月产品":"按次产品");

		if (product.getProductType() < GDHttpTools.getInstance().getProductListInfoRes().getData().getProductTypeDesc().length){
			String str = GDHttpTools.getInstance().getProductListInfoRes().getData().getProductTypeDesc()[product.getProductType()];
			Toast.makeText(getContext(),"取出的type是："+str + " index是："+product.getProductType(),Toast.LENGTH_LONG).show();
			tvType.setText(str);
		}

		tvDate.setText(product.getStartTime());
		tvProductID.setText(product.getProductId());
		tvContentID.setText(product.getServiceId());
	}

	private OnFocusChangeListener onFocusChangeListener = new OnFocusChangeListener()
	{
		@Override
		public void onFocusChange(View v, boolean hasFocus) 
		{
			if (hasFocus) {
				if (v.getId() == btnPhonePay.getId()) 
				{
					rlCode.setVisibility(View.INVISIBLE);
					llInfoRoot.setVisibility(View.VISIBLE);
					tvHead.setText(TEXT_HEAD_PHONEPAY);
					orderMode = 0;
				}else if (v.getId() == btnZhiFuBao.getId()) 
				{
					tvHead.setText(TEXT_HEAD_ZHIFUBAO);
					llInfoRoot.setVisibility(View.INVISIBLE);
					tvCodeText.setVisibility(View.VISIBLE);
					ivCode.setVisibility(View.INVISIBLE);
					rlCode.setVisibility(View.VISIBLE);
					orderMode = 56;
				}
			}
		}
	};
	
	private View.OnClickListener onClickListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) 
		{
			if (v.getId() == btnPhonePay.getId()) 
			{
				showToast("点击了话费付款");
//				ZZFileTools.getInstance().printlnDIR(getContext());

			}else if (v.getId() == btnZhiFuBao.getId()) 
			{
				showToast("点击了支付宝");
//				ivCode.setImageDrawable(getContext().getResources().getDrawable(R.drawable.testcode));
				ivCode.setVisibility(View.VISIBLE);
				tvCodeText.setVisibility(View.INVISIBLE);
				if (thread == null){
					isThreadStop = false;
					thread = new CheckThread();
					thread.start();
				}
			}

			if (Var.isZZEnabled)
			{
				if (orderMode == 56){
					String payurl = GDHttpTools.getInstance().getPayUrl(getContext(),product.getProductId());
					if (payurl!=null){
						getHttpBitmap(payurl);
					}else{
                        GDHttpTools.getInstance().cmhOrder(getContext(),"0", product.getContinueAble(), orderMode + "", Var.userId, GDHttpTools.getInstance().getUsertokenAIDL(), GDHttpTools.getInstance().getProductListBeanByProductId(product.getChannelid(), product.getProductId()),iUpdateData);
                    }
					return;
				}else{
					GDHttpTools.getInstance().cmhOrder(getContext(),"0", product.getContinueAble(), orderMode + "", Var.userId, GDHttpTools.getInstance().getUsertokenAIDL(), GDHttpTools.getInstance().getProductListBeanByProductId(product.getChannelid(), product.getProductId()),iUpdateData);
				}
			}
		}
	};


	private IUpdateData iUpdateData = new IUpdateData() {
		@Override
		public void updateData(String method, String uniId, Object object, boolean isSuccess)
		{
			if (method.equals(GDHttpTools.METHOD_ORDER_CMHORDER))
			{
				GDOrdercmhOrderRes res = (GDOrdercmhOrderRes) object;
				if (isSuccess){
					if (uniId.equals("0")){
						GDHttpTools.getInstance().cmhOrderSync(getContext(),"0",res.getEndTime(), Var.userId, Var.userToken,res.getData().getOrderID(),product.getProductId(),product.getChannelid(),iUpdateData);
					}else if (uniId.equals("56"))
					{
						//1.存下网络图片，2.显示网络图片，3.启动时间线程。4.不断进行checkorder请求
						getHttpBitmap(res.getData().getPayURL());
						if (thread == null){
							isThreadStop = false;
							thread = new CheckThread();
							thread.start();
						}

					}

				}else{

				}
			}else if (method.equals(GDHttpTools.METHOD_ORDER_CMHORDERSYNC))
			{
				if (isSuccess){
					Toast.makeText(getContext(),"给百途同步完成",Toast.LENGTH_LONG).show();
					ZZFileTools.getInstance().clearZZCache(getContext(),uniId);
					GDHttpTools.getInstance().setNeedReplay(true);
					if (iNeedDismiss!=null)iNeedDismiss.needDismiss(true);
					dismiss();
				}else{

				}
			}else if (method.equals(GDHttpTools.METHOD_ORDER_CMHCHECKORDER))
			{
				GDOrdercmhCheckOrderRes res = (GDOrdercmhCheckOrderRes) object;
				if (isSuccess){
					if (res.getCode().equals("11000-1"))
					{
					    Toast.makeText(getContext(),"订购成功",Toast.LENGTH_LONG).show();
						isThreadStop=true;
						GDHttpTools.getInstance().cmhOrderSync(getContext(),"0",res.getEndTime(), Var.userId, Var.userToken,res.getData().getOrderID(),product.getProductId(),product.getChannelid(),iUpdateData);
					}
				}else {

				}
			}
		}
	};

	private boolean isThreadStop = false;
	private int counter = 0;
	private CheckThread thread;
	private class CheckThread extends Thread
	{
		@Override
		public void run()
		{
			while (!isThreadStop){
				try {
					sleep(200);
					counter+=200;
					if (counter > 260 * 1000)
					{
						//TODO 发出请求刷新二维码
						handler.sendEmptyMessage(0);
					}else if (counter % 2000 == 0)
					{
						//TODO 发出checkOrder请求
						handler.sendEmptyMessage(1);
					}
				} catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
		}
	}

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg)
		{
			if (msg.what == 0){ //请求刷新二维码
				GDHttpTools.getInstance().cmhOrder(getContext(),"0", product.getContinueAble(), orderMode + "", Var.userId, GDHttpTools.getInstance().getEpgurlAIDL(), GDHttpTools.getInstance().getProductListBeanByProductId(product.getChannelid(), product.getProductId()),iUpdateData);
				counter = 0;
			}else if (msg.what == 1){ //请求checkOrder
				GDHttpTools.getInstance().cmhCheckOrder("0", Var.userId,product.getChannelid(),product.getProductId(), GDHttpTools.getInstance().getEpgurlAIDL(),iUpdateData);
			}else if (msg.what == 3){
				ivCode.setImageBitmap((Bitmap) msg.obj);
			}
		}
	};



	private void showToast(String content){
		Toast.makeText(getContext(), content, Toast.LENGTH_SHORT).show();
	}

	/**
	 * 获取网落图片资源
	 * @param url
	 * @return
	 */
	public  void getHttpBitmap(final String url)
	{
		new Thread(new Runnable() {
			@Override
			public void run() {
				URL myFileURL;
				Bitmap bitmap=null;
				try{
					myFileURL = new URL(url);
					//获得连接
					HttpURLConnection conn=(HttpURLConnection)myFileURL.openConnection();
					//设置超时时间为6000毫秒，conn.setConnectionTiem(0);表示没有时间限制
					conn.setConnectTimeout(6000);
					//连接设置获得数据流
					conn.setDoInput(true);
					//不使用缓存
					conn.setUseCaches(false);
					//这句可有可无，没有影响
					//conn.connect();
					//得到数据流
					InputStream is = conn.getInputStream();
					//解析得到图片
					bitmap = BitmapFactory.decodeStream(is);
					//关闭数据流
					is.close();
				}catch(Exception e){
					e.printStackTrace();
				}
				Message msg = Message.obtain();
				msg.what = 3;
				msg.obj = bitmap;
				handler.sendMessage(msg);
			}
		}).start();


	}

	public interface INeedDismiss{
	    void needDismiss(boolean isNeedDismiss);
    }

}
