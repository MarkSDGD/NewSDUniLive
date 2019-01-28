/**
 * @Title: ZZOrderListViewAdapter.java
 * @Package com.shandong.shandonglive.zengzhi.ui
 * @Description: TODO
 * Copyright: Copyright (c) 2015
 * Company:青岛博瑞立方网络科技有限公司
 * 
 * @author Mernake
 * @date 2018年7月10日 下午4:06:28
 * @version V1.0
 */
package com.shandong.shandonglive.zengzhi.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xike.xkliveplay.R;

import java.util.ArrayList;
import java.util.List;

/**
  * @ClassName: ZZOrderListViewAdapter
  * @Description: TODO
  * @author Mernake
  * @date 2018年7月10日 下午4:06:28
  *
  */
public class ZZOrderListViewAdapter extends BaseAdapter
{
	private List<ZZOrderHistory> mList = new ArrayList<ZZOrderHistory>();
	
	private Context context;
	private LayoutInflater mInflater = null;
	
	

	public ZZOrderListViewAdapter(Context context) {
		super();
		this.context = context;
		mInflater = LayoutInflater.from(context);
	}
	
	public void refreshData(List<ZZOrderHistory> list)
	{
		this.mList = list;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() 
	{
		return mList.size();
	}

	@Override
	public Object getItem(int position) 
	{
		if (position>=mList.size()) 
		{
			return new ZZOrderHistory();
		}
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) 
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		if (convertView == null) 
		{
			convertView = mInflater.inflate(R.layout.zz_order_history_item_main, null);
		}
		
		ZZOrderHistory orderHistory = (ZZOrderHistory) getItem(position);
		TextView tvName = (TextView) convertView.findViewById(R.id.zz_tv_order_item_name);
		TextView tvType = (TextView) convertView.findViewById(R.id.zz_tv_order_item_type);
		TextView tvPrice = (TextView) convertView.findViewById(R.id.zz_tv_order_item_price);
		TextView tvID = (TextView) convertView.findViewById(R.id.zz_tv_order_item_id);
		TextView tvStartTime = (TextView) convertView.findViewById(R.id.zz_tv_order_item_starttime);
		TextView tvEndTime = (TextView) convertView.findViewById(R.id.zz_tv_order_item_endtime);
		
		tvName.setTypeface(TypefaceTools.getYuanTiFont(context));
		tvType.setTypeface(TypefaceTools.getYuanTiFont(context));
		
		tvName.setText(orderHistory.getName());
		tvType.setText(orderHistory.getType());
		tvPrice.setText(orderHistory.getPrice());
		tvID.setText(orderHistory.getId());
		tvStartTime.setText(orderHistory.getStartDate());
		tvEndTime.setText(orderHistory.getEndDate());
		
		return convertView;
	}

}
