/**
 * @Title: ZZChooseProductActivity.java
 * @Package com.shandong.shandonglive.zengzhi.ui
 * @Description: TODO
 * Copyright: Copyright (c) 2015
 * Company:青岛博瑞立方网络科技有限公司
 * 
 * @author Mernake
 * @date 2018年7月9日 上午10:07:00
 * @version V1.0
 */
package com.shandong.shandonglive.zengzhi.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xike.xkliveplay.R;
import com.xike.xkliveplay.framework.entity.gd.GDOrderGetProductListInfoRes;
import com.xike.xkliveplay.framework.entity.gd.GDOrderPlayAuthCMHWRes;
import com.xike.xkliveplay.framework.http.IUpdateData;
import com.xike.xkliveplay.gd.GDHttpTools;
import com.xike.xkliveplay.gd.LogUtils;


/**
  * @ClassName: ZZChooseProductActivity
  * @Description: 选择产品包界面
  * @author Mernake
  * @date 2018年7月9日 上午10:07:00
  *
  */
public class ZZChooseProductActivity extends FragmentActivity
{
	
	private LinearLayout llProductRoot;

	private String contentId,categoryId;
	private TextView  tvDesc;
	private TextView tvSubTitle;
	private TextView tvMainTitile;

	private String errorCode = "";//这个错误码，只有在51041-506和51041-507的情况下会传过来，这时候是要重新同步订单

	@Override
	protected void onCreate(Bundle arg0) 
	{
		super.onCreate(arg0);
		setContentView(R.layout.zz_choose_product_main);

		initView();
	}

	private void initView() 
	{
		tvSubTitle = (TextView) findViewById(R.id.zz_tv_chooseproduct_subtitle);
		tvSubTitle.setTypeface(TypefaceTools.getYuanTiFont(getApplicationContext()));
		tvDesc = (TextView) findViewById(R.id.zz_tv_chooseproduct_bottomline);
		tvMainTitile = (TextView) findViewById(R.id.zz_tv_chooseproduct_maintitle);
		tvMainTitile.setTypeface(TypefaceTools.getYuanTiFont(getApplicationContext()));
		
		llProductRoot = (LinearLayout) findViewById(R.id.zz_ll_product_root);
		Intent intent = getIntent();
		if (intent!=null){
			contentId = intent.getStringExtra("channelid");
			categoryId = intent.getStringExtra("categoryid");
			errorCode = intent.getStringExtra("errorcode");
			if (errorCode == null) {
				errorCode = "";
			}
		}

		GDHttpTools.getInstance().getProductListInfo(getApplicationContext(),"0", GDHttpTools.getInstance().getTag(), new IUpdateData() {
			@Override
			public void updateData(String method, String uniId, Object object, boolean isSuccess)
			{
				if (isSuccess){
					dealSuccess(method,uniId,object);
				}else{
					dealFailed(method,uniId,object);
				}
			}
		});

//		addProducts();
	}

	private void dealFailed(String method, String uniId, Object object)
	{
		if (method.equals(GDHttpTools.METHOD_ORDER_GETPRODUCTLISTINFO))
		{
			GDOrderPlayAuthCMHWRes res = GDHttpTools.getInstance().getGDOrderPlayAuthCMHWRes(contentId);
			System.out.println("contentId: " + contentId);
			int count = res.getData().size();
			for (int i = 0;i<count;i++)
			{
				ZZProduct product = new ZZProduct();
				product.setName(res.getData().get(i).getName());
				GDOrderGetProductListInfoRes.GDOrderProductList mp = GDHttpTools.getInstance().getProductInfo(res.getData().get(i).getProductid());
				if (mp != null) {
					product.setPriceLeft(mp.getPeiceHead());
					product.setPriceRight(mp.getPriceEnd());
					product.setOriPrice(mp.getOldPrice());
					product.setDate(mp.getVipTime());
					product.setInfo(mp.getVipDescription());
					product.setProductId(mp.getProductID());
				}
				product.setPrice(res.getData().get(i).getPrice());
				product.setProductDesc(res.getData().get(i).getProductDesc());
				product.setChannelid(contentId);
				product.setStartTime(res.getStartTime());
				product.setProductId(res.getData().get(i).getProductid());//显示一个ProduceID
				product.setServiceId(res.getData().get(i).getServiceid());
				product.setProductType(res.getData().get(i).getProducttype());
				product.setContinueAble(res.getData().get(i).getContinueable());
				addProduct(product);
			}
		}
	}

	private void dealSuccess(String method, String uniId, Object object)
	{
		if (method.equals(GDHttpTools.METHOD_ORDER_GETPRODUCTLISTINFO))
		{
			GDOrderPlayAuthCMHWRes res = GDHttpTools.getInstance().getGDOrderPlayAuthCMHWRes(contentId);
			int count = res.getData().size();
			for (int i = 0;i<count;i++)
			{
				ZZProduct product = new ZZProduct();
				product.setName(res.getData().get(i).getName());
				GDOrderGetProductListInfoRes.GDOrderProductList mp = GDHttpTools.getInstance().getProductInfo(res.getData().get(i).getProductid());
				if (mp != null) {
					product.setPriceLeft(mp.getPeiceHead());
					product.setPriceRight(mp.getPriceEnd());
					product.setOriPrice(mp.getOldPrice());
					product.setDate(mp.getVipTime());
					product.setInfo(mp.getVipDescription());
					product.setProductId(mp.getProductID());
				}
				product.setPrice(res.getData().get(i).getPrice());

				product.setProductDesc(res.getData().get(i).getProductDesc());
				product.setChannelid(contentId);
				product.setStartTime(res.getStartTime());
				product.setServiceId(res.getData().get(i).getServiceid());
				product.setProductType(res.getData().get(i).getProducttype());
				product.setContinueAble(res.getData().get(i).getContinueable());
				addProduct(product);
			}
			tvSubTitle.setText(GDHttpTools.getInstance().getProductListInfoRes().getData().getTitle2());
			tvMainTitile.setText(GDHttpTools.getInstance().getProductListInfoRes().getData().getTitle1());
		}
	}

	private void addProduct(final ZZProduct product){
		ZZProductItemView view = new ZZProductItemView(ZZChooseProductActivity.this);
		if ("51041-507".equals(errorCode) || "51041-506".equals(errorCode)) {
			LogUtils.i("xumin", "playAuth ZZChooseProductActivity 添加了特殊item: errorCode: " + errorCode);
			view.refreshData(product, errorCode);
		}else {//如果没有错误码，还沿用原来的方式
			view.refreshData(product);
		}
		llProductRoot.addView(view);
		view.requestFocus();
		view.setFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus){
					System.out.println("======>" + v.getClass().getSimpleName());
					ZZProduct product1 = (ZZProduct) v.getTag();
					tvDesc.setText(product1.getProductDesc());
				}
			}
		});
	}

	private void addProducts() 
	{
		ZZProduct product01 = new ZZProduct();
		product01.setName("直播单频道");
		product01.setPriceLeft("现价");
		product01.setPriceRight("元/路");
		product01.setPrice("5.9");
		product01.setOriPrice("原价7元");
		product01.setDate("30天");
		product01.setInfo("1个月内自有观看本频道");
		
		ZZProduct product02 = new ZZProduct();
		product02.setName("直播VIP月包");
		product02.setPriceLeft("现价");
		product02.setPriceRight("元/月");
		product02.setPrice("19.9");
		product02.setOriPrice("原价25元");
		product02.setDate("30天");
		product02.setInfo("尊享VIP直播特权");
		
		ZZProduct product03 = new ZZProduct();
		product03.setName("直播VIP季包");
		product03.setPriceLeft("现价");
		product03.setPriceRight("元/季");
		product03.setPrice("49.9");
		product03.setOriPrice("原价60元");
		product03.setDate("90天");
		product03.setInfo("尊享VIP直播特权");
		
		ZZProduct product04 = new ZZProduct();
		product04.setName("直播VIP年包");
		product04.setPriceLeft("现价");
		product04.setPriceRight("元/年");
		product04.setPrice("199");
		product04.setOriPrice("原价300元");
		product04.setDate("365天");
		product04.setInfo("尊享VIP直播特权");
		
		ZZProduct product05 = new ZZProduct();
		product05.setName("直播VIP世纪包");
		product05.setPriceLeft("现价");
		product05.setPriceRight("元/世纪");
		product05.setPrice("999");
		product05.setOriPrice("原价9999元");
		product05.setDate("36500天");
		product05.setInfo("尊享终身VIP直播特权");
		
		ZZProduct[] products = {product01,product02,product03,product04,product05};

		for (int i = 0; i < 5; i++)
		{
			ZZProductItemView view = new ZZProductItemView(ZZChooseProductActivity.this);
			view.refreshData(products[i]);
			llProductRoot.addView(view);
		}
		
		
	}
}
