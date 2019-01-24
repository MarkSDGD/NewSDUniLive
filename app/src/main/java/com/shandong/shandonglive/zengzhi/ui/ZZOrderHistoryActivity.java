/**
 * @Title: ZZOrderHistoryActivity.java
 * @Package com.shandong.shandonglive.zengzhi.ui
 * @Description: TODO
 * Copyright: Copyright (c) 2015
 * Company:青岛博瑞立方网络科技有限公司
 * 
 * @author Mernake
 * @date 2018年7月10日 下午3:31:58
 * @version V1.0
 */
package com.shandong.shandonglive.zengzhi.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.xike.xkliveplay.R;

import java.util.ArrayList;
import java.util.List;

/**
  * @ClassName: ZZOrderHistoryActivity
  * @Description: 订购记录
  * @author Mernake
  * @date 2018年7月10日 下午3:31:58
  *
  */
public class ZZOrderHistoryActivity extends FragmentActivity
{
	private ListView lv;
	
	private ImageView ivUP;
	private ImageView ivDown;
	
	private TextView tvNoHistory;
	
	private LinearLayout llHead;
	
	@Override
	protected void onCreate(Bundle arg0) 
	{
		super.onCreate(arg0);
		setContentView(R.layout.zz_order_history_activity_main);
		
		initView();
	}
	
	private void initView() 
	{
		TextView tvHead = (TextView) findViewById(R.id.zz_tv_order_title);
		tvHead.setTypeface(TypefaceTools.getYuanTiFont(this));
		
		lv = (ListView) findViewById(R.id.zz_lv_order);
		ivUP = (ImageView) findViewById(R.id.zz_iv_order_up);
		ivDown = (ImageView) findViewById(R.id.zz_iv_order_down);
		tvNoHistory = (TextView) findViewById(R.id.zz_tv_order_nohistory);
		llHead = (LinearLayout) findViewById(R.id.zz_ll_order_tablehead);
		tvNoHistory.setTypeface(TypefaceTools.getYuanTiFont(this));
		
		List<ZZOrderHistory> list = new ArrayList<ZZOrderHistory>();
		
		ZZOrderHistory history01 = new ZZOrderHistory();
		history01.setName("高尔夫网球频道 10元/月");
		history01.setType("单频道包月");
		history01.setPrice("10元");
		history01.setId("TVDY20YF6DS2B");
		history01.setStartDate("2018-06-04 15:30");
		history01.setEndDate("2018-07-03 15:30");
		
		ZZOrderHistory history02 = new ZZOrderHistory();
		history02.setName("海看直播全频道包 50元/月");
		history02.setType("VIP包月");
		history02.setPrice("50元");
		history02.setId("TVDY31O");
		history02.setStartDate("2018-06-04 15:30");
		history02.setEndDate("2018-07-03 15:30");
		
		ZZOrderHistory history03 = new ZZOrderHistory();
		history03.setName("海看直播全频道包 360元/年");
		history03.setType("VIP包年");
		history03.setPrice("360元");
		history03.setId("TVDY9SHJ95FGS");
		history03.setStartDate("2018-06-04 15:30");
		history03.setEndDate("2019-06-04 15:30");
		
		ZZOrderHistory history04 = new ZZOrderHistory();
		history04.setName("湖南金鹰卡通 60元/月");
		history04.setType("单频道包月");
		history04.setPrice("60元");
		history04.setId("TVDY2G9");
		history04.setStartDate("2018-06-04 15:30");
		history04.setEndDate("2018-07-03 15:30");
		
		ZZOrderHistory history05 = new ZZOrderHistory();
		history05.setName("四海钓鱼 380/年");
		history05.setType("单频道包年");
		history05.setPrice("380元");
		history05.setId("TVD98SDFA9F");
		history05.setStartDate("2018-06-04 15:30");
		history05.setEndDate("2018-07-03 15:30");
		
		
		list.add(history01);
		list.add(history02);
		list.add(history03);
		list.add(history04);
		list.add(history05);
		list.add(history01);
		list.add(history02);
		list.add(history03);
		list.add(history04);
		list.add(history05);
		
		lv.setEnabled(false);
		lv.setSmoothScrollbarEnabled(true);
		lv.setOnScrollListener(listener);
		
		
		ZZOrderListViewAdapter adapter = new ZZOrderListViewAdapter(this);
		lv.setAdapter(adapter);
		adapter.refreshData(list);
		
		if (list.size() <= 5 && list.size()>0) 
		{
			ivDown.setVisibility(View.INVISIBLE);
			ivUP.setVisibility(View.INVISIBLE);
		}else if(list.size() > 5){
			ivDown.setVisibility(View.VISIBLE);
			ivUP.setVisibility(View.INVISIBLE);
		}else if (list.size() == 0) 
		{
			lv.setVisibility(View.GONE);
			llHead.setVisibility(View.GONE);
			ivDown.setVisibility(View.GONE);
			ivUP.setVisibility(View.GONE);
			tvNoHistory.setVisibility(View.VISIBLE);
			
		}
	}
	
	private OnScrollListener listener = new OnScrollListener() {
		
		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) 
		{
			
		}
		
		@Override
		public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) 
		{
			if (firstVisibleItem == 0) {
                View firstVisibleItemView = lv.getChildAt(0);
                if (firstVisibleItemView != null && firstVisibleItemView.getTop() == 0) 
                {
                    Log.d("ListView", "##### 滚动到顶部 #####");
                    ivUP.setVisibility(View.INVISIBLE);
                }else{
                	ivUP.setVisibility(View.VISIBLE);
                }
            } else if ((firstVisibleItem + visibleItemCount) == totalItemCount) {
                View lastVisibleItemView = lv.getChildAt(lv.getChildCount() - 1);
                if (lastVisibleItemView != null && lastVisibleItemView.getBottom() == lv.getHeight()) {
                    Log.d("ListView", "##### 滚动到底部 ######");  
                    ivDown.setVisibility(View.INVISIBLE);
                }else{
                	ivDown.setVisibility(View.VISIBLE);
                }
            }
		}
	};
	
	@SuppressLint("NewApi")
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) 
	{
		if (isStopKey) 
		{
			return true;
		}
		
		if (event.getAction()==KeyEvent.ACTION_DOWN) 
		{
			if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP) 
			{
//				lv.smoothScrollByOffset(-1);
				lv.smoothScrollBy(-85, 500);
				refreshKey();
				return true;
			}else if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN) 
			{
//				lv.smoothScrollByOffset(1);
				lv.smoothScrollBy(85, 500);
				refreshKey();
				return true;
			}
		}
		
		if (event.getAction() == KeyEvent.ACTION_UP) 
		{
			if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP || event.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN) 
			{
				return true;
			}
		}
		
		return super.dispatchKeyEvent(event);
	}
	
	private boolean isStopKey = false;
	private Handler handler = new Handler();
	private Runnable runnable = new Runnable() 
	{
		@Override
		public void run() 
		{
			isStopKey = false;
		}
	};
	
	private void refreshKey()
	{
		isStopKey = true;
		handler.removeCallbacks(runnable);
		handler.postDelayed(runnable, 500);
	}
	
}
