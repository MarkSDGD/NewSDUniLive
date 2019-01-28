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
import android.os.Message;
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
import com.xike.xkliveplay.framework.entity.gd.CMHOrderListData;
import com.xike.xkliveplay.framework.entity.gd.CMHOrderListResult;
import com.xike.xkliveplay.framework.http.IUpdateData;
import com.xike.xkliveplay.gd.GDHttpTools;

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
	List<ZZOrderHistory> list = new ArrayList<ZZOrderHistory>();//用来记录获取到的订购记录
	ZZOrderListViewAdapter adapter;//用于更新list
	
	@Override
	protected void onCreate(Bundle arg0) 
	{
		super.onCreate(arg0);
		setContentView(R.layout.zz_order_history_activity_main);
		
		initView();
		adapter = new ZZOrderListViewAdapter(this);
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
//		GDHttpTools.getInstance().queryOrderList(getApplicationContext(), GDHttpTools.getInstance().getTag(), "1", GDHttpTools.getInstance().getUserId(), GDHttpTools.getInstance().getUsertokenAIDL(), iUpdateData);//请求订购的包月产品列表
		lv.setEnabled(false);
		lv.setSmoothScrollbarEnabled(true);
		lv.setOnScrollListener(listener);
		hd.removeCallbacks(queryOrderHistoryThread);
		hd.post(queryOrderHistoryThread);
//		ZZOrderListViewAdapter adapter = new ZZOrderListViewAdapter(this);
//		lv.setAdapter(adapter);
//		adapter.refreshData(list);
//
//		if (list.size() <= 5 && list.size()>0)
//		{
//			ivDown.setVisibility(View.INVISIBLE);
//			ivUP.setVisibility(View.INVISIBLE);
//		}else if(list.size() > 5){
//			ivDown.setVisibility(View.VISIBLE);
//			ivUP.setVisibility(View.INVISIBLE);
//		}else if (list.size() == 0)
//		{
//			lv.setVisibility(View.GONE);
//			llHead.setVisibility(View.GONE);
//			ivDown.setVisibility(View.GONE);
//			ivUP.setVisibility(View.GONE);
//			tvNoHistory.setVisibility(View.VISIBLE);
//
//		}
	}
	private void refreshList(){
		if (list.isEmpty()) {
			Log.i("ZZOrderHistoryActivity", "list.isEmpty() in refreshList");
			return;
		}
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

	/**********************************handler用来从网络请求历史记录，然后更新UI*****************************************/
	@SuppressLint("HandlerLeak")
	private Handler hd = new Handler(){
		@Override
		public void handleMessage(Message msg)
		{
			switch (msg.what) {
			case 1:
				refreshList();//刷新页面
			break;

			default:
			break;
			}
		}
	};
	private Runnable queryOrderHistoryThread = new Runnable() {
		@Override
		public void run() {
			list.clear();
			GDHttpTools.getInstance().queryOrderList(getApplicationContext(), GDHttpTools.getInstance().getTag(), "0", GDHttpTools.getInstance().getUserId(), GDHttpTools.getInstance().getUsertokenAIDL(), new IUpdateData() {
				@Override
				public void updateData(String method, String uniId, Object object, boolean isSuccess) {
					CMHOrderListData cmhOrderListRes = (CMHOrderListData) object;
					if (isSuccess) {
						List<CMHOrderListResult.CMHOrderListBean> listBeanList = cmhOrderListRes.getData();
						for (CMHOrderListResult.CMHOrderListBean cmhOrderListBean:listBeanList) {
							ZZOrderHistory historyBean = new ZZOrderHistory();
							historyBean.setName(cmhOrderListBean.getProductName());
							historyBean.setType("包月");
							historyBean.setPrice(cmhOrderListBean.getFee());
							historyBean.setId(cmhOrderListBean.getProductID());
							historyBean.setStartDate(cmhOrderListBean.getStartTime());
							historyBean.setEndDate(cmhOrderListBean.getEndTime());
							list.add(historyBean);
						}
						hd.sendEmptyMessage(1);
					}else {
//						GDDialogTools.creater().showDialog(getApplicationContext(),cmhOrderListRes.getCode(),cmhOrderListRes.getDescription(),GDHttpTools.getInstance().getUserId(), GetStbinfo.getLocalMacAddress());
					}
				}
			});//请求订购的包月产品列表
			GDHttpTools.getInstance().queryOrderList(getApplicationContext(), GDHttpTools.getInstance().getTag(), "1", GDHttpTools.getInstance().getUserId(), GDHttpTools.getInstance().getUsertokenAIDL(), new IUpdateData() {
				@Override
				public void updateData(String method, String uniId, Object object, boolean isSuccess) {
					CMHOrderListData cmhOrderListRes = (CMHOrderListData) object;
					if (isSuccess) {
						List<CMHOrderListResult.CMHOrderListBean> listBeanList = cmhOrderListRes.getData();
						for (CMHOrderListResult.CMHOrderListBean cmhOrderListBean:listBeanList) {
							ZZOrderHistory historyBean = new ZZOrderHistory();
							historyBean.setName(cmhOrderListBean.getProductName());
							historyBean.setType("按次");
							historyBean.setPrice(cmhOrderListBean.getFee());
							historyBean.setId(cmhOrderListBean.getProductID());
							historyBean.setStartDate(cmhOrderListBean.getStartTime());
							historyBean.setEndDate(cmhOrderListBean.getEndTime());
							list.add(historyBean);
						}
						hd.sendEmptyMessage(1);
					}else {
//						GDDialogTools.creater().showDialog(getApplicationContext(),cmhOrderListRes.getCode(),cmhOrderListRes.getDescription(),GDHttpTools.getInstance().getUserId(), GetStbinfo.getLocalMacAddress());
					}
				}
			});//请求订购的单词产品列表
		}
	};
	
}
