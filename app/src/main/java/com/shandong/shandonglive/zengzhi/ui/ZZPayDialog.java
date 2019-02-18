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
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shandong.shandonglive.zengzhi.ui.gd.GDDialogTools;
import com.xike.xkliveplay.R;
import com.xike.xkliveplay.framework.adapter.SmallKeyBoardAdapter;
import com.xike.xkliveplay.framework.entity.gd.GDCMSMSCode;
import com.xike.xkliveplay.framework.entity.gd.GDCancelCMHOrderRes;
import com.xike.xkliveplay.framework.entity.gd.GDOrdercmhCheckOrderRes;
import com.xike.xkliveplay.framework.entity.gd.GDOrdercmhOrderRes;
import com.xike.xkliveplay.framework.http.IUpdateData;
import com.xike.xkliveplay.framework.tools.AppContext;
import com.xike.xkliveplay.framework.tools.GetStbinfo;
import com.xike.xkliveplay.framework.varparams.Var;
import com.xike.xkliveplay.gd.GDHttpTools;
import com.xike.xkliveplay.gd.LogUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Timer;
import java.util.TimerTask;


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

	private String TEXT_HEAD_PHONEPAY = "话费支付";
	private String TEXT_HEAD_ZHIFUBAO = "支付宝支付";
	private INeedDismiss iNeedDismiss = null;
	
	private TextView tvHead,tvChannel,tvPackageName,tvType,tvDate,tvProductID,tvContentID,tvCodeText;
	private TextView etSmsCode;
	private Button btSmsCodeConform;
	private TextView tvTimeCountDown;
	private TextView tvSendSmsTo;

	private String smsCode;
	private Long saveSmsCodeTime;
	private int smsTimeout = 60;//短信验证码超时时间1min
	private Timer smsCountDownTimer;
	private final int ACTION_SMSCOUNTDOWN = 6;
	private String keyInput = "";
	
	private ImageView ivCode;
	private Context context;
	private SmallKeyBoardAdapter smallKeyBoardAdapter;
	private GridView gdSmallKeyboard;

	private Timer zhifubaoRefreshTimer;
	private Long appTimeout = 300000L;//5min
    private Long hwTimeout = 480000L;//8min
	private AppContext appContext;
	private Long saveZhifubaoTime;
	private GDOrdercmhOrderRes saveZhifubaoRes;
	private String errorCode = "";

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}


	public ZZPayDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		this.context = context;
		appContext = AppContext.getSharedAppContext(context);
	}

	public ZZPayDialog(Context context, int themeResId) {
		super(context, themeResId);
		this.context = context;
		appContext = AppContext.getSharedAppContext(context);
	}


	public ZZPayDialog(Context context) {
		super(context);this.context = context;
		appContext = AppContext.getSharedAppContext(context);
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
		btnPhonePay.setTypeface(TypefaceTools.getYuanTiFont(context));
		btnZhiFuBao.setTypeface(TypefaceTools.getYuanTiFont(context));

//		btnPhonePay.setVisibility(View.GONE);
//		btnZhiFuBao.setVisibility(View.GONE);
		
		llInfoRoot = (LinearLayout) findViewById(R.id.zz_ll_paydialog_info_root);
		
		tvHead = (TextView) findViewById(R.id.zz_tv_pay_hint);
		tvHead.setTypeface(TypefaceTools.getYuanTiFont(context));
		tvChannel = (TextView) findViewById(R.id.zz_tv_pay_channelname);
		tvChannel.setTypeface(TypefaceTools.getYuanTiFont(context));
		tvPackageName = (TextView) findViewById(R.id.zz_tv_pay_packagename);
		tvPackageName.setTypeface(TypefaceTools.getYuanTiFont(context));
		tvType = (TextView) findViewById(R.id.zz_tv_pay_producttype);
		tvType.setTypeface(TypefaceTools.getYuanTiFont(context));
		tvDate = (TextView) findViewById(R.id.zz_tv_pay_productdate);
		tvDate.setTypeface(TypefaceTools.getYuanTiFont(context));
		tvProductID = (TextView) findViewById(R.id.zz_tv_pay_productid);
		tvProductID.setTypeface(TypefaceTools.getYuanTiFont(context));
//		tvContentID = (TextView) findViewById(R.id.zz_tv_pay_contentid);
//		tvContentID.setTypeface(TypefaceTools.getYuanTiFont(context));
		
		rlCode = (RelativeLayout) findViewById(R.id.zz_rl_paydialog_code);
		tvCodeText = (TextView) findViewById(R.id.zz_tv_paydialog_codetext);
		tvCodeText.setTypeface(TypefaceTools.getYuanTiFont(context));
		ivCode = (ImageView) findViewById(R.id.zz_iv_paydialog_code);

		etSmsCode = (TextView) findViewById(R.id.zz_et_smscode_input);
		etSmsCode.setTypeface(TypefaceTools.getYuanTiFont(context));
		tvTimeCountDown = (TextView) findViewById(R.id.zz_tv_time_count_down);
		tvTimeCountDown.setTypeface(TypefaceTools.getYuanTiFont(context));
		btSmsCodeConform = (Button) findViewById(R.id.zz_smspay_dialog_btn_conform);
        gdSmallKeyboard = (GridView) findViewById(R.id.zz_gd_small_keyboard);
        initSmallKeyBoard(context);
		tvSendSmsTo = (TextView)findViewById(R.id.zz_send_sms_to);
		tvSendSmsTo.setTypeface(TypefaceTools.getYuanTiFont(context));

        btnPhonePay.setOnFocusChangeListener(onFocusChangeListener);
		btnZhiFuBao.setOnFocusChangeListener(onFocusChangeListener);
		
		btnPhonePay.setOnClickListener(onClickListener);
		btnZhiFuBao.setOnClickListener(onClickListener);

		setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				if (zhifubaoRefreshTimer != null) {
				    zhifubaoRefreshTimer.cancel();
				    zhifubaoRefreshTimer = null;
                }
                if (smsCountDownTimer != null) {
					smsCountDownTimer.cancel();
					smsCountDownTimer = null;
				}
			}
		});

		setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
                if (zhifubaoRefreshTimer != null) {
                    zhifubaoRefreshTimer.cancel();
                    zhifubaoRefreshTimer = null;
                }
                if (smsCountDownTimer != null) {
                	smsCountDownTimer.cancel();
                	smsCountDownTimer = null;
				}
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
//			Toast.makeText(getContext(),"取出的type是："+str + " index是："+product.getProductType(),Toast.LENGTH_LONG).show();
			tvType.setText(str);
		}

		String startTime = product.getStartTime();//去掉毫秒
		LogUtils.i("xumin", "startTime" + startTime);
		int index = startTime.indexOf(".");
		tvDate.setText(startTime.substring(0, index));
		tvProductID.setText(product.getProductId());
//		tvContentID.setText(product.getServiceId());
	}

	public void refreshData(ZZProduct product, String errorCode)
	{
		this.product = product;
		tvChannel.setText(Var.getChannelNameByChannelId(product.getChannelid()));
		tvPackageName.setText(product.getName());
//		tvType.setText(product.getProductType()==0?"包月产品":"按次产品");

		if (product.getProductType() < GDHttpTools.getInstance().getProductListInfoRes().getData().getProductTypeDesc().length){
			String str = GDHttpTools.getInstance().getProductListInfoRes().getData().getProductTypeDesc()[product.getProductType()];
//			Toast.makeText(getContext(),"取出的type是："+str + " index是："+product.getProductType(),Toast.LENGTH_LONG).show();
			tvType.setText(str);
		}

		String startTime = product.getStartTime();//去掉毫秒
		LogUtils.i("xumin", "startTime" + startTime);
		int index = startTime.indexOf(".");
		tvDate.setText(startTime.substring(0, index));
		tvProductID.setText(product.getProductId());
//		tvContentID.setText(product.getServiceId());
		this.errorCode = errorCode;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		LogUtils.i("xumin", "keyCode is" + keyCode);
		if (keyCode >= KeyEvent.KEYCODE_0 && keyCode <= KeyEvent.KEYCODE_9) {
			if (keyInput.length() < 6) {
				keyInput = keyInput + (keyCode - 7);//KEYCODE_0 - KEYCODE_9: 7-16, 所以这里-7
				etSmsCode.setText(keyInput);
			}else {
				//Do Nothing
			}
		}else if (keyCode == KeyEvent.KEYCODE_DEL){
			if (keyInput.length() > 0) {
				keyInput = keyInput.substring(0, keyInput.length() - 1);
				etSmsCode.setText(keyInput);
			}
		}else {
			//Do Nothing
		}
//		else if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {//拿不到焦点，没有用，所以OK键的判断挪到了onClick里面
//			smsOrderOk(keyInput);
//		}
		return super.onKeyDown(keyCode, event);
	}

	private OnFocusChangeListener onFocusChangeListener = new OnFocusChangeListener()
	{
		@Override
		public void onFocusChange(View v, boolean hasFocus) 
		{
			if (hasFocus) {
				/*//todo  delete
				errorCode="";  //测试用*/
				if (v.getId() == btnPhonePay.getId()) 
				{
					tvHead.setText(TEXT_HEAD_PHONEPAY);

					if ("".equals(errorCode)){//这说明没有传过上次失败的产品包ID过来
						if (getHawkAvaiableSmdCode()){//已经有有效的短信验证码，那么就显示出来
							//初始化UI布局
							initSmsOrderUI();
							//以下是显示时间倒计时用的
							tvTimeCountDown.setVisibility(View.VISIBLE);
							Long locTime = (System.currentTimeMillis() - saveSmsCodeTime)/1000;
							smsOrderTimeCountDown(60 - locTime);
						}else {//没有有效的短信验证码，提示重新获取
							tvCodeText.setText("按【确定】获取短信验证码");
							ivCode.setVisibility(View.GONE);
							tvCodeText.setVisibility(View.VISIBLE);
							gdSmallKeyboard.setVisibility(View.GONE);
							etSmsCode.setVisibility(View.GONE);
						}
						//焦点在短信上，这时候判断一下，如果是支付宝的Timer不为空，这时候就取消这个timer
						if (zhifubaoRefreshTimer != null) {
							zhifubaoRefreshTimer.cancel();
							zhifubaoRefreshTimer = null;
						}
					}else {
						if ("51041-507".equals(errorCode)) {
							tvCodeText.setText("按【确定】同步订单至百途");
							ivCode.setVisibility(View.GONE);
							tvCodeText.setVisibility(View.VISIBLE);
							gdSmallKeyboard.setVisibility(View.GONE);
							etSmsCode.setVisibility(View.GONE);
						}else if ("51041-506".equals(errorCode)){
							//Need change
						}else {
							//Do Nothing 理论上不会走到这里
						}
					}

				}else if (v.getId() == btnZhiFuBao.getId()) 
				{
					tvHead.setText(TEXT_HEAD_ZHIFUBAO);

					if ("".equals(errorCode)) {
						//焦点在支付宝上，如果短信的Timer不为空，这时候就取消短信这个timer
						if (smsCountDownTimer != null) {
							smsCountDownTimer.cancel();
							smsCountDownTimer = null;
						}
						if (getHawkAvaiableZhiFuBaoQrCode()) {
							initZhifubaoOrderUI();
							zhiFuBaoOrderQrTimer();
						}else {
							tvCodeText.setText("按【确定】获取二维码");
							tvCodeText.setVisibility(View.VISIBLE);
							ivCode.setVisibility(View.INVISIBLE);
							//将话费相关的显示设置为不可见
							gdSmallKeyboard.setVisibility(View.GONE);
							etSmsCode.setVisibility(View.GONE);
							tvTimeCountDown.setVisibility(View.GONE);
							tvSendSmsTo.setVisibility(View.GONE);
						}
//					rlCode.setVisibility(View.VISIBLE);
					}else {
						if ("51041-507".equals(errorCode)) {
							tvCodeText.setText("按【确定】同步订单至百途");
							tvCodeText.setVisibility(View.VISIBLE);
							ivCode.setVisibility(View.INVISIBLE);
							//将话费相关的显示设置为不可见
							gdSmallKeyboard.setVisibility(View.GONE);
							etSmsCode.setVisibility(View.GONE);
							tvTimeCountDown.setVisibility(View.GONE);
							tvSendSmsTo.setVisibility(View.GONE);
						}else if ("51041-506".equals(errorCode)){
							//Need change 广电成功，但是CM失败的情况，怎么处理
						}else {
							//Do Nothing 理论上不会走到这里
						}
					}
				}
			}
		}
	};

	/**
	 * 这是短信验证码是否存在的判断方法，如果存在就赋值给smsCode和saveSmsCodeTime
	 * @return
	 */
	private boolean getHawkAvaiableSmdCode(){
		String saveSmsCodes = appContext.getHawkString(GDHttpTools.getInstance().GD_SMSCODE + product.getProductId());
		if ("".equals(saveSmsCodes)) {
			return false;//hawk中不存在，所以返回false
		}else {
			String[] smsCodes = saveSmsCodes.split("###");
			if (smsCodes.length == 2) {//读到了格式正确的验证码，但是还不确定是否过期
				saveSmsCodeTime = Long.parseLong(smsCodes[1]);
				smsCode = smsCodes[0];
				Long nowTime = System.currentTimeMillis();
				if (nowTime - saveSmsCodeTime < 60000){
					return true;//这是一个有效的短信验证码
				}else {
					return false;//虽然有短信验证码但是已经过期了
				}

			}else {
				return false;//无效的短信验证码
			}
		}
	}

	/**
	 * 这里获取一下，当前是否有有效的二维码信息，存在hawk中
	 * @return
	 */
	private boolean getHawkAvaiableZhiFuBaoQrCode(){
		saveZhifubaoRes = (GDOrdercmhOrderRes) appContext.getSaveObject(GDHttpTools.GD_CMHORDERRES + product.getProductId());
		saveZhifubaoTime = appContext.getLong(GDHttpTools.GD_CMHORDERSUCCES_STARTTIME + product.getProductId());
		Long nowTime = System.currentTimeMillis();
		Long midTime = nowTime - saveZhifubaoTime;
		if (midTime < appTimeout && saveZhifubaoRes != null) {
			return true;//在支付宝二维码信息存在，并且没有超过五分钟的情况下，就返回true,认为这是一个有效的二维码信息
		}else {
			return false;
		}

	}

	private void zhiFuBaoOrderQrTimer(){
		String payurl = saveZhifubaoRes.getData().getPayURL();
		if (payurl!=null){
//			showToast("准备生成二维码图片");
			boolean flag = QRCodeUtil.createQRImage(payurl,220,220,null,ZZFileTools.getInstance().getQRCodePath(getContext()));
			System.out.println("二维码图片生成结果：" + flag);
			getLocalBitmap(ZZFileTools.getInstance().getQRCodePath(getContext()));
			//启动一个计时器
			if (zhifubaoRefreshTimer == null) {
				zhifubaoRefreshTimer = new Timer();
				zhifubaoRefreshTimer.schedule(new TimerTask() {
					@Override
					public void run() {
						Long nt = System.currentTimeMillis();
						if (nt - saveZhifubaoTime >appTimeout) {
							String orderID = saveZhifubaoRes.getData().getOrderID();
							GDHttpTools.getInstance().cancelCMHOrder(context, "0", orderID, Var.userId, GDHttpTools.getInstance().getUsertokenAIDL(), iUpdateData);//5min超时了，取消一下订单
							if (!appContext.deleteObject(GDHttpTools.GD_CMHORDERRES + product.getProductId())){
								Log.e("ZZPayDialog", "删除res在超过5min的时候失败了");
							}
							handler.sendEmptyMessage(5);//取消二维码显示
							this.cancel();//取消定时器
						}else {//在时间少于5min的情况下，就查询订单
							GDHttpTools.getInstance().cmhCheckOrder("0",Var.userId,product.getChannelid(),product.getProductId(),GDHttpTools.getInstance().getUsertokenAIDL(),iUpdateData);
						}
					}
				}, 0, 2000);
			}
		}
	}

	private void smsOrderOk(String keyInput){
		String saveSmsCodes = appContext.getHawkString(GDHttpTools.getInstance().GD_SMSCODE + product.getProductId());
		if ("".equals(saveSmsCodes)) {
			//一个容错，不会出现这种情况
		}else {
			String[] smsCodes = saveSmsCodes.split("###");
			if (smsCodes.length == 2) {
				saveSmsCodeTime = Long.parseLong(smsCodes[1]);
			}else {
				Toast.makeText(context, "验证码失效，点击话费重新获取", Toast.LENGTH_SHORT).show();
			}

		}
		if (System.currentTimeMillis() - saveSmsCodeTime < 900000) {//验证码一分钟可以发一次，十五分钟内有效
			if (null != keyInput && !"".equals(keyInput) && keyInput.equals(smsCode)) {
				huaFeiSmsOrder();
			}else {
				Toast.makeText(context, "验证码有误，请重新输入", Toast.LENGTH_SHORT).show();
			}
		}else {
			Toast.makeText(context, "验证码失效，点击话费重新获取", Toast.LENGTH_SHORT).show();
		}

	}
	/**
	 * 话费支付
	 */
	private void huaFeiSmsOrder(){
		Toast.makeText(context, "验证码正确，为您订购", Toast.LENGTH_SHORT).show();
		GDHttpTools.getInstance().cmhOrder(getContext(),"0", product.getContinueAble(), orderMode + "", Var.userId, GDHttpTools.getInstance().getUsertokenAIDL(), GDHttpTools.getInstance().getProductListBeanByProductId(product.getChannelid(), product.getProductId()),iUpdateData);
		//To-Do需要确认是否要取消掉弹出框
	}

	private void initSmallKeyBoard(Context context){
	    smallKeyBoardAdapter = new SmallKeyBoardAdapter(context);
		gdSmallKeyboard.setAdapter(smallKeyBoardAdapter);
		gdSmallKeyboard.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String data =  view.getTag().toString();
				Log.e("ZZPayDialog", "Small KeyBoard value: " + data);
				if (data.equals("确定")) {
					smsOrderOk(keyInput);
					keyInput = "";
				} else if (data.equals("删除")) {
					if (keyInput.length() > 0) {
						keyInput = keyInput.substring(0, keyInput.length() - 1);
					}
				} else {
					if (keyInput.length() < 6) {
						keyInput = keyInput + data;
					}else {
						//Do Nothing
					}
				}
				etSmsCode.setText(keyInput);
			}
		});
		gdSmallKeyboard.setSelector(R.drawable.selector_gridview_item);//这里是给小键盘加上一个蓝色的框
    }

    /**
     * 生层6位短信验证码
     * @return
     */
    private String createSMSRandomCode() {
        smsCode = "";
        for (int i = 0; i < 6; i++) {
            smsCode = smsCode + ((int) ((Math.random()) * 9));
        }
	    return smsCode;
    }

	private View.OnClickListener onClickListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) 
		{

			if (v.getId() == btnPhonePay.getId()) 
			{
//				showToast("点击了话费付款");
				orderMode = 0;
				if (etSmsCode.getVisibility() == View.VISIBLE) {//当验证码输入框可见的时候，再按下遥控器上的OK键，就将这个动作路由到smsOrderOk
					smsOrderOk(keyInput);
					return;
				}
				if (!"".equals(errorCode) ) {//当errorCode不为空的时候，就说明传过来了，就说明playAuth发生51041-506或者51041-507了
					if ("51041-507".equals(errorCode)) {
						tvCodeText.setText("正在同步订单至百途");
						Log.i("ZZPayDialog","playAuth 51041-507 cmhCheckOrder");
						GDHttpTools.getInstance().cmhCheckOrder("0",Var.userId,product.getChannelid(),product.getProductId(),GDHttpTools.getInstance().getUsertokenAIDL(),iUpdateData);
//						GDHttpTools.getInstance().cmhCheckOrder("0",Var.userId,"acdb78b1-af03-4b3f-b9e4-a78ef6b2c86b",product.getProductId(),GDHttpTools.getInstance().getUsertokenAIDL(),iUpdateData);
//						GDHttpTools.getInstance().cmhOrder(getContext(),"0", product.getContinueAble(), orderMode + "", Var.userId, GDHttpTools.getInstance().getUsertokenAIDL(), GDHttpTools.getInstance().getProductListBeanByProductId(product.getChannelid(), product.getProductId()),iUpdateData);
					}else if ("51041-506".equals(errorCode)) {
						//CM不通过，GD通过需要处理
					}else {
						//Do Nothing 理论上不会发生的
					}
					return;
				}
//				ZZFileTools.getInstance().printlnDIR(getContext());

			}else if (v.getId() == btnZhiFuBao.getId()) 
			{
//				showToast("点击了支付宝");
				orderMode = 56;
				if (!"".equals(errorCode)) {//当errorCode不为空的时候，就说明传过来了，就说明playAuth发生51041-506或者51041-507了
					if ("51041-507".equals(errorCode)) {
						tvCodeText.setText("正在同步订单至百途");
						Log.i("ZZPayDialog", "playAuth 51041-507 " + "支付宝按钮调用cmhOrder方法" + " product.getChannelid(): " + product.getChannelid() + " product.getProductId(): " + product.getProductId());
						GDHttpTools.getInstance().cmhCheckOrder("0",Var.userId,product.getChannelid(),product.getProductId(),GDHttpTools.getInstance().getUsertokenAIDL(),iUpdateData);
//						GDHttpTools.getInstance().cmhOrder(getContext(),"0", product.getContinueAble(), orderMode + "", Var.userId, GDHttpTools.getInstance().getUsertokenAIDL(), GDHttpTools.getInstance().getProductListBeanByProductId(product.getChannelid(), product.getProductId()),iUpdateData);
					}else if ("51041-506".equals(errorCode)) {
						//CM不通过，GD通过需要处理
					}else {
						//Do Nothing 理论上不会发生的
					}
					return;
				}
//				ivCode.setImageDrawable(getContext().getResources().getDrawable(R.drawable.testcode));
				ivCode.setVisibility(View.VISIBLE);
				tvCodeText.setVisibility(View.INVISIBLE);
				tvSendSmsTo.setVisibility(View.GONE);
//				System.out.println("xumin query Order");
//				GDHttpTools.getInstance().queryOrderList(getContext(),"1","0", Var.userId, Var.userToken, iUpdateData);
			}

			if (Var.isZZEnabled)
			{
				if (orderMode == 56){
                    final GDOrdercmhOrderRes res = (GDOrdercmhOrderRes) appContext.getSaveObject(GDHttpTools.GD_CMHORDERRES + product.getProductId());
					Log.i("ZZPayDialog", "onClick GDOrdercmhOrderRes res==: " + res);
                    final Long saveTime = appContext.getLong(GDHttpTools.GD_CMHORDERSUCCES_STARTTIME + product.getProductId());
					Log.i("ZZPayDialog", "onClick saveTime==: " + saveTime);
					Long nowTime = System.currentTimeMillis();
					Long midTime = nowTime - saveTime;
					Log.i("ZZPayDialog", "onClick midTime==: " + midTime);
					if (midTime > appTimeout) {//超过5min,又没有超过8min的话，就先取消一下上一个订单，然后重新获取。超过5min是app的超时，8min是华为平台的超时时间
                        if (midTime < hwTimeout) {//没超过8min
                            if (res != null) {//只有有这个订购的记录数据的情况下，才会去删除订购关系,并且在订购关系删除成功之后，再发起订购
                                String orderID = res.getData().getOrderID();
                                GDHttpTools.getInstance().cancelCMHOrder(context, "0", orderID, Var.userId, GDHttpTools.getInstance().getUsertokenAIDL(), iUpdateData);//这里取消下上个订单,这个是我们这里看超过了5min，但是华为平台超时是8min
//                                if (appContext.deleteObject(GDHttpTools.GD_CMHORDERSUCCES_STARTTIME + product.getProductId())) {
//                                    System.out.println("xumin " + "在点击Onclick的时候，删除Object");
//                                }
                            }else {
                                GDHttpTools.getInstance().cmhOrder(getContext(),"0", product.getContinueAble(), orderMode + "", Var.userId, GDHttpTools.getInstance().getUsertokenAIDL(), GDHttpTools.getInstance().getProductListBeanByProductId(product.getChannelid(), product.getProductId()),iUpdateData);
                            }
                        }else {//华为已经清除了这个订单，这里不管了直接去重新申请
                            if (appContext.deleteObject(GDHttpTools.GD_CMHORDERRES + product.getProductId())) {
                                Log.i("ZZPayDialog", "在点击Onclick的时候，删除Object");
                            }
                            GDHttpTools.getInstance().cmhOrder(getContext(),"0", product.getContinueAble(), orderMode + "", Var.userId, GDHttpTools.getInstance().getUsertokenAIDL(), GDHttpTools.getInstance().getProductListBeanByProductId(product.getChannelid(), product.getProductId()),iUpdateData);
                        }

					}else {
					    if (res != null) {
                            String payurl = res.getData().getPayURL();
							Log.i("ZZPayDialog", "onClick payurl==: " + payurl);
                            if (payurl!=null){
								//因为这里已经在焦点移动到话费上的时候显示出来了，所以不需要再onclick的时候又显示一遍。所以下面屏蔽起来了，逻辑正确性需要进一步确认
//                                showToast("准备生成二维码图片");
//                                boolean flag = QRCodeUtil.createQRImage(payurl,220,220,null,ZZFileTools.getInstance().getQRCodePath(getContext()));
//                                System.out.println("二维码图片生成结果：" + flag);
//                                getLocalBitmap(ZZFileTools.getInstance().getQRCodePath(getContext()));
//                                //启动一个计时器
//                                zhifubaoRefreshTimer = new Timer();
//                                zhifubaoRefreshTimer.schedule(new TimerTask() {
//                                    @Override
//                                    public void run() {
//                                        Long nt = System.currentTimeMillis();
//                                        if (nt - saveTime >appTimeout) {
//                                            String orderID = res.getData().getOrderID();
//                                            GDHttpTools.getInstance().cancelCMHOrder(context, "0", orderID, Var.userId, GDHttpTools.getInstance().getUsertokenAIDL(), iUpdateData);//5min超时了，取消一下订单
//                                            if (!appContext.deleteObject(GDHttpTools.GD_CMHORDERRES + product.getProductId())){
//                                                System.out.println("xumin " +  "删除res在超过5min的时候失败了");
//                                            }
//                                            handler.sendEmptyMessage(5);//取消二维码显示
//                                            this.cancel();//取消定时器
//                                        }else {//在时间少于5min的情况下，就查询订单
//                                            GDHttpTools.getInstance().cmhCheckOrder("0",Var.userId,product.getChannelid(),product.getProductId(),GDHttpTools.getInstance().getUsertokenAIDL(),iUpdateData);
//                                        }
//                                    }
//                                }, 0, 2000);
								//Do Nothing
                            }else{
                                GDHttpTools.getInstance().cmhOrder(getContext(),"0", product.getContinueAble(), orderMode + "", Var.userId, GDHttpTools.getInstance().getUsertokenAIDL(), GDHttpTools.getInstance().getProductListBeanByProductId(product.getChannelid(), product.getProductId()),iUpdateData);
                            }
                        }else {
                            GDHttpTools.getInstance().cmhOrder(getContext(),"0", product.getContinueAble(), orderMode + "", Var.userId, GDHttpTools.getInstance().getUsertokenAIDL(), GDHttpTools.getInstance().getProductListBeanByProductId(product.getChannelid(), product.getProductId()),iUpdateData);
                        }
					}
					return;
				}else if (orderMode == 0) {
					//短信验证码逻辑
					String smsCodeUniID = GDHttpTools.GD_SMSCODE + product.getProductId();
					String smsCodeAndTime = appContext.getHawkString(smsCodeUniID);
					if (!smsCodeAndTime.equals("")) {//本地有存储
						String[] smsCodes = smsCodeAndTime.split("###");
						if (smsCodes.length == 2) {
							smsCode = smsCodes[0];
							saveSmsCodeTime = Long.parseLong(smsCodes[1]);
							Long locTime = (System.currentTimeMillis() - saveSmsCodeTime)/1000;
							if (locTime < 60) {
								//在焦点变化的时候就已经做了这个操作了，所以这里就先屏蔽了，但是需要验证逻辑的正确性
//								Toast.makeText(context,"一分钟内只发送一次验证码", Toast.LENGTH_SHORT).show();
//								initSmsOrderUI();
//								smsOrderTimeCountDown(60-locTime);//将剩余时间作为参数传递下去
							}else {//时间大于1min，重新调用生成短信的方法
								smsCode = createSMSRandomCode();
								GDHttpTools.getInstance().sendcmSMS("0", GDHttpTools.getInstance().getTag(), Var.userId, GDHttpTools.getInstance().getUsertokenAIDL(), GDHttpTools.getInstance().getEpgurlAIDL(), smsCode, iUpdateData);
							}
						}else {//存储的验证码和时间是不对的，就重新生成
							smsCode = createSMSRandomCode();
							GDHttpTools.getInstance().sendcmSMS("0", GDHttpTools.getInstance().getTag(), Var.userId, GDHttpTools.getInstance().getUsertokenAIDL(), GDHttpTools.getInstance().getEpgurlAIDL(), smsCode, iUpdateData);
						}
					}else {//如果本地没有存储这个短信验证码，重新生成并且存储
						smsCode = createSMSRandomCode();
						GDHttpTools.getInstance().sendcmSMS("0", GDHttpTools.getInstance().getTag(), Var.userId, GDHttpTools.getInstance().getUsertokenAIDL(), GDHttpTools.getInstance().getEpgurlAIDL(), smsCode, iUpdateData);
					}
                } else{
					//Do Nothing
				}
			}
		}
	};


	private IUpdateData iUpdateData = new IUpdateData() {
		@Override
		public void updateData(String method, String uniId, Object object, boolean isSuccess)
		{
			Log.i("ZZPayDialog", "iUpdateData updateData uniId==" + uniId+"  isSuccess=="+isSuccess);
			if (method.equals(GDHttpTools.METHOD_ORDER_CMHORDER))
			{
				final GDOrdercmhOrderRes res = (GDOrdercmhOrderRes) object;
				Log.i("ZZPayDialog", "iUpdateData GDOrdercmhOrderRes res==" + res);

				if (isSuccess){
					if (uniId.equals("0")){
						GDHttpTools.getInstance().cmhOrderSync(getContext(),"0", res.getStartTime(), Var.userId, GDHttpTools.getInstance().getUsertokenAIDL(), res.getData().getOrderID(), product.getProductId(), product.getChannelid(), iUpdateData);
					}else if ("56".equals(uniId) || "701".equals(uniId))
					{
						//1.存下网络图片，2.显示网络图片，3.启动时间线程。4.不断进行checkorder请求
//						showToast("准备生成二维码图片");
						boolean flag = QRCodeUtil.createQRImage(res.getData().getPayURL(),220,220,null,ZZFileTools.getInstance().getQRCodePath(getContext()));
						getLocalBitmap(ZZFileTools.getInstance().getQRCodePath(getContext()));
						System.out.println("二维码图片生成结果：" + flag);
					    if(flag){  //生成成功进行保存
							appContext.saveLong(GDHttpTools.GD_CMHORDERSUCCES_STARTTIME + product.getProductId(),System.currentTimeMillis());
						}

						//启动一个计时器
                        final Long saveTime = appContext.getLong(GDHttpTools.GD_CMHORDERSUCCES_STARTTIME + product.getProductId());
						Log.i("ZZPayDialog", "iUpdateData saveTime==" + saveTime);
						if(zhifubaoRefreshTimer!=null){
							zhifubaoRefreshTimer.cancel();
							zhifubaoRefreshTimer=null;
						}
                        zhifubaoRefreshTimer = new Timer();
                        zhifubaoRefreshTimer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                Long nt = System.currentTimeMillis();
                                if (nt - saveTime >appTimeout) {
								//if (nt - saveTime >10*1000) {
									Log.i("ZZPayDialog", "run() 大于5min的情况下    nt - saveTime==" + (nt - saveTime));
                                    String orderID = res.getData().getOrderID();
									Log.i("ZZPayDialog", "run() cancelCMHOrder !!");
                                    GDHttpTools.getInstance().cancelCMHOrder(context, "0", orderID, Var.userId, GDHttpTools.getInstance().getUsertokenAIDL(), iUpdateData);//5min超时了，取消一下订单
                                    if (!appContext.deleteObject(GDHttpTools.GD_CMHORDERRES + product.getProductId())){
                                        Log.i("ZZPayDialog", "run() 删除res在超过5min的时候");
                                    }
                                    handler.sendEmptyMessage(5);//取消二维码显示,并且删除上一次订购的信息GDOrdercmhOrderRes
									zhifubaoRefreshTimer.cancel();//取消定时器
									zhifubaoRefreshTimer = null;
                                }else {//在时间少于5min的情况下，就查询订单
									Log.i("ZZPayDialog", "run() < 5min  在时间少于5min的情况下，就查询订单 " );

									GDHttpTools.getInstance().cmhCheckOrder("0",Var.userId,product.getChannelid(),product.getProductId(),GDHttpTools.getInstance().getUsertokenAIDL(),iUpdateData);
                                }
                            }
                        }, 0, 2000);
					}

				}else{
					if ("51041-507".equals(errorCode) && "51048".equals(res.getCode()) && "4300".equals(res.getData().getCode())){//这是对GD不通过，CM通过的特殊处理，再次在CM平台订购，会出现重复订单的错误码，就是利用这个错误码作为判断依据，然后再往百图同步一遍
						if (res.getData() != null && !"".equals(res.getData().getOrderID()) && res.getData().getOrderID() != null){
						    Log.i("ZZPayDialog","playAuth 51041-507 " + "cmhOrder成功返回重复订单，并正确带有orderId : " + res.getData().getOrderID());
							GDHttpTools.getInstance().cmhOrderSync(getContext(),"0", res.getStartTime(), Var.userId, GDHttpTools.getInstance().getUsertokenAIDL(), res.getData().getOrderID(), product.getProductId(), product.getChannelid(), iUpdateData);
						}else {//如果OrderID没有从SDK拿到的情况下，报个错吧
                            Log.i("ZZPayDialog","playAuth 51041-507 " + "cmhOrder成功返回重复订单，但是没有orderId");
							GDDialogTools.creater().showDialog(context,res.getCode(),res.getDescription(),GDHttpTools.getInstance().getUserId(), GetStbinfo.getLocalMacAddress());
						}
					}else if ("51041-506".equals(errorCode)) {

					}else {
						GDDialogTools.creater().showDialog(context,res.getCode(),res.getDescription(),GDHttpTools.getInstance().getUserId(), GetStbinfo.getLocalMacAddress());
					}
				}
			}else if (method.equals(GDHttpTools.METHOD_ORDER_CMHORDERSYNC))
			{
				if (isSuccess){
					Toast.makeText(getContext(),"给百途同步完成",Toast.LENGTH_LONG).show();
					ZZFileTools.getInstance().clearZZCache(getContext(),uniId);//uuiId对应这里来说就是channelId
					GDHttpTools.getInstance().setNeedReplay(true);
					if (iNeedDismiss!=null)iNeedDismiss.needDismiss(true);
					dismiss();
				}else{//只要订购成功，哪怕是给百图同步失败了，也要能够继续播放,所以订购的弹出框也要消失
					Toast.makeText(getContext(),"给百途同步失败",Toast.LENGTH_LONG).show();
					ZZFileTools.getInstance().clearZZCache(getContext(),uniId);//uuiId对应这里来说就是channelId
					GDHttpTools.getInstance().setNeedReplay(true);
					if (iNeedDismiss!=null)iNeedDismiss.needDismiss(true);
					dismiss();
				}
			}else if (method.equals(GDHttpTools.METHOD_ORDER_CMHCHECKORDER))
			{
				GDOrdercmhCheckOrderRes res = (GDOrdercmhCheckOrderRes) object;
				if (isSuccess){
					if (res.getCode().equals("51000-1"))
					{
					    Toast.makeText(getContext(),"订购成功",Toast.LENGTH_LONG).show();
                        if (appContext.deleteObject(GDHttpTools.GD_CMHORDERRES + product.getProductId())){
                            Log.i("ZZPayDialog", "订购成功之后，删除这个qrOrder信息");
                        }
                        if (zhifubaoRefreshTimer != null) {
                            zhifubaoRefreshTimer.cancel();
                            zhifubaoRefreshTimer = null;
                        }
						GDHttpTools.getInstance().cmhOrderSync(getContext(),"0",res.getStartTime(),Var.userId,Var.userToken,res.getData().getOrderID(),product.getProductId(),product.getChannelid(),iUpdateData);
					}
				}else {
                    Log.i("ZZPayDialog", "查询订单失败了，现在还什么也不干，等待华为结果");
				}
			}else if (method.equals(GDHttpTools.METHOD_ORDER_CANCELORDER))
			{
				GDCancelCMHOrderRes res = (GDCancelCMHOrderRes) object;
				if (isSuccess) {
					if (res.getCode().equals("51000")) {
						handler.sendEmptyMessage(0); //刷新 生成新的订单二维码
					}
				}else {
                    Log.i("ZZPayDialog", "GDCancelCMHOrder Fail");
					GDDialogTools.creater().showDialog(context,res.getCode(),res.getDescription(),GDHttpTools.getInstance().getUserId(), GetStbinfo.getLocalMacAddress());
				}
			}else if (method.equals(GDHttpTools.METHOD_ORDER_CMSENDSMS)) {
				GDCMSMSCode res = (GDCMSMSCode) object;
				if (isSuccess) {
					//TO-DO
					//显示小键盘、输入框、倒计时，等待用户输入。 并将smdCode和时间的存储移动到这里。
					Toast.makeText(getContext(),"验证码发送成功",Toast.LENGTH_LONG).show();
					initSmsOrderUI();
					smsOrderTimeCountDown(60L);
					String smsCodeAndTime = smsCode + "###" + System.currentTimeMillis();//拼接上时间，存储hawk用的
					if (!appContext.saveHawkString(GDHttpTools.GD_SMSCODE + product.getProductId(), smsCodeAndTime)){
                        Log.i("ZZPayDialog", "存储短信验证码和时间失败");
					}

				}else {
                    Log.i("ZZPayDialog", "GDCMSMSSend Fail");
					GDDialogTools.creater().showDialog(context,res.getCode(),res.getDescription(),GDHttpTools.getInstance().getUserId(), GetStbinfo.getLocalMacAddress());
				}
			}
		}
	};

	/**
	 * 初始化点击话费按钮后的UI界面
	 */
	private void initSmsOrderUI(){
		gdSmallKeyboard.setVisibility(View.VISIBLE);
		tvTimeCountDown.setVisibility(View.VISIBLE);
		etSmsCode.setVisibility(View.VISIBLE);
		tvCodeText.setVisibility(View.GONE);
		ivCode.setVisibility(View.GONE);
		tvSendSmsTo.setVisibility(View.VISIBLE);
		String sendTo = "已发送短信至：";
		tvSendSmsTo.setText(sendTo + Var.userId);
	}

	/**
	 * 初始化点击支付宝按钮后的UI界面
	 */
	private void initZhifubaoOrderUI(){
	    LogUtils.i("xumin", "initZhifubaoOrderUI");
		gdSmallKeyboard.setVisibility(View.GONE);
		tvTimeCountDown.setVisibility(View.GONE);
		etSmsCode.setVisibility(View.GONE);
		tvCodeText.setVisibility(View.GONE);
		ivCode.setVisibility(View.VISIBLE);
		tvSendSmsTo.setVisibility(View.GONE);
	}
	/**
	 * 用Timer做一个倒计时，用来在成功发送短信之后，显示一分钟倒计时用的
	 * @param countDownTimes
	 */
	private void smsOrderTimeCountDown(Long countDownTimes){
		smsTimeout = Integer.parseInt(countDownTimes+"");
		if (smsCountDownTimer == null) {//只有当smsCountDownTimer为空的时候才再开启一个计时器
			smsCountDownTimer = new Timer();
			smsCountDownTimer.schedule(new TimerTask() {
				@Override
				public void run() {
					Message msg = Message.obtain();
					msg.what = ACTION_SMSCOUNTDOWN;
					msg.arg1 = smsTimeout;
					if (smsTimeout >= 0){
						smsTimeout--;
						handler.sendMessage(msg);
					}else {
						this.cancel();
						smsCountDownTimer = null;
					}
				}
			}, 0, 1000);//1s刷新一次UI
		}
	}

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
					if (counter % 2000 == 0)
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
			}else if (msg.what == 1){ //请求checkOrder
				GDHttpTools.getInstance().cmhCheckOrder("0",Var.userId,product.getChannelid(),product.getProductId(),GDHttpTools.getInstance().getUsertokenAIDL(),iUpdateData);
			}else if (msg.what == 3){
//                showToast("图片生成完毕，准备填充控件");
			    ivCode.setImageBitmap((Bitmap) msg.obj);
			    ivCode.setVisibility(View.VISIBLE);//设置二维码可见
			}else if (msg.what == 4) {
//				GDHttpTools.getInstance().cancelCMHOrder(getContext(), "0",);
			}else if (msg.what == 5) {//这是app监测到支付宝的订单请求超过5min的情况下
                ivCode.setVisibility(View.INVISIBLE);//设置二维码不可见
                tvCodeText.setVisibility(View.VISIBLE);
            }else if (msg.what == ACTION_SMSCOUNTDOWN) {
				if (msg.arg1 == 0) {
					tvTimeCountDown.setText("");
				}else {
					tvTimeCountDown.setText(msg.arg1 + "");
				}
			}
		}
	};



	private void showToast(String content){
	    System.out.println(content);
		Toast.makeText(getContext(), content, Toast.LENGTH_SHORT).show();
	}

	/**
	 * 获取网落图片资源
	 * @param url
	 * @return
	 */
//	public  void getHttpBitmap(final String url)
//	{
//		new Thread(new Runnable() {
//			@Override
//			public void run() {
//				URL myFileURL;
//				Bitmap bitmap=null;
//				try{
//					myFileURL = new URL(url);
//					//获得连接
//					HttpURLConnection conn=(HttpURLConnection)myFileURL.openConnection();
//					//设置超时时间为6000毫秒，conn.setConnectionTiem(0);表示没有时间限制
//					conn.setConnectTimeout(6000);
//					//连接设置获得数据流
//					conn.setDoInput(true);
//					//不使用缓存
//					conn.setUseCaches(false);
//					//这句可有可无，没有影响
//					//conn.connect();
//					//得到数据流
//					InputStream is = conn.getInputStream();
//					//解析得到图片
//					bitmap = BitmapFactory.decodeStream(is);
//					//关闭数据流
//					is.close();
//				}catch(Exception e){
//					e.printStackTrace();
//				}
//				Message msg = Message.obtain();
//				msg.what = 3;
//				msg.obj = bitmap;
//				handler.sendMessage(msg);
//			}
//		}).start();
//	}

    /**
     * 获取网落图片资源
     * @return
     */
    public  void getLocalBitmap(final String path)
    {
        new Thread(new Runnable() {
            @Override
            public void run()
            {
				Bitmap bitmap = null;
				try {
					FileInputStream is = new FileInputStream(path);
					bitmap = BitmapFactory.decodeStream(is);
				} catch (FileNotFoundException e)
				{
					e.printStackTrace();
				}
//				Bitmap bitmap = BitmapFactory.decodeFile(path);
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
