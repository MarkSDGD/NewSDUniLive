/**
 * @Title: ZZProductPriceView.java
 * @Package com.shandong.shandonglive.zengzhi.ui
 * @Description: TODO
 * Copyright: Copyright (c) 2015
 * Company:青岛博瑞立方网络科技有限公司
 * 
 * @author Mernake
 * @date 2018年7月9日 下午4:10:35
 * @version V1.0
 */
package com.shandong.shandonglive.zengzhi.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.xike.xkliveplay.R;

import java.util.HashMap;
import java.util.Map;

/**
  * @ClassName: ZZProductPriceView
  * @Description: TODO
  * @author Mernake
  * @date 2018年7月9日 下午4:10:35
  *
  */
public class ZZProductPriceView extends LinearLayout
{

	private int[] ids = {R.drawable.zz_0, R.drawable.zz_1,
			R.drawable.zz_2, R.drawable.zz_3, R.drawable.zz_4,
			R.drawable.zz_5, R.drawable.zz_6, R.drawable.zz_7,
			R.drawable.zz_8, R.drawable.zz_9, R.drawable.zz_dot};
	
	private Map<String, Integer> maps = null;
	
	
	public ZZProductPriceView(Context context, AttributeSet attrs) 
	{
		super(context, attrs);
		inflate(context, R.layout.zz_product_price_view_main, this);
		initMaps();
	}

	private void initMaps()
	{
		if (maps==null) 
		{
			maps = new HashMap<String, Integer>();
			maps.put("0", ids[0]);
			maps.put("1", ids[1]);
			maps.put("2", ids[2]);
			maps.put("3", ids[3]);
			maps.put("4", ids[4]);
			maps.put("5", ids[5]);
			maps.put("6", ids[6]);
			maps.put("7", ids[7]);
			maps.put("8", ids[8]);
			maps.put("9", ids[9]);
			maps.put(".", ids[10]);
		}
	}
	
	public ZZProductPriceView(Context context) 
	{
		super(context);
		initMaps();
	}
	
	public void setPrice(String price)
	{
		if (price==null||price.length()==0) {
			return;
		}

		for (int i = 0; i < price.length(); i++) 
		{
//			System.out.println("char = " + price.charAt(i));
			String str = String.valueOf(price.charAt(i));
//			System.out.println("ids =  " + maps.get(price.charAt(i)));
			addNumber(maps.get(str));
		}
		
		
	}
	
	private void addNumber(int id)
	{
		ImageView iv = new ImageView(getContext());
		iv.setImageDrawable(getContext().getResources().getDrawable(id));
		addView(iv);
//		ViewGroup.LayoutParams params = iv.getLayoutParams();
//		params.height = 46;
//		params.width = 24;
//		iv.setLayoutParams(params);
	}
	
}
