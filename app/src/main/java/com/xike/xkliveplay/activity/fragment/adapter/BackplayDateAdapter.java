package com.xike.xkliveplay.activity.fragment.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xike.xkliveplay.R;
import com.xike.xkliveplay.activity.fragment.FragmentBackPlayBase;
import com.xike.xkliveplay.framework.entity.BackplayDate;

/**
 * 
 * @author LiWei <br>
 * CreateTime: 2014年10月29日 上午11:56:30<br>
 * <b>Info:</b><br>
 *<br>
 *   <b>Method:</b> <br>
 */
public class BackplayDateAdapter extends BaseAdapter {

	@SuppressWarnings("unused")
	private static final String tag = "BackplayDateAdapter";
	private Context context;
	private List<BackplayDate> list_ch;
	private LayoutInflater mInflater;

//	private int nextDayIndex = 4;
//	private int nextNextDayIndex = 5;
//	private int nextNextNextDayIndex = 6;
	private int focusIndex = 3;

	public class ViewHolder {
		public TextView tv_date_monthday;
		public TextView tv_date_weekday;
	}

	public BackplayDateAdapter(Context context, List<BackplayDate> list) {
		super();
		this.context = context;
		this.list_ch = list;
		this.mInflater = LayoutInflater.from(context);
	}

	public BackplayDateAdapter(Context context) {
		super();
		this.context = context;
		list_ch = new ArrayList<BackplayDate>();
		this.mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list_ch.size();
	}

	public void removeAll() {
		list_ch.clear();
	}

	public void addItem(BackplayDate date) {
		list_ch.add(date);
		notifyDataSetChanged();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list_ch.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public void removeFirstAndAddInEnd(BackplayDate date) {
		list_ch.remove(0);
		list_ch.add(date);
		notifyDataSetChanged();
	}

	public void removeFirst(BackplayDate date) {
		list_ch.remove(0);
		notifyDataSetChanged();
	}

	public void addFirstAndRemoveEnd(BackplayDate date) {
		list_ch.remove(list_ch.size() - 1);
		list_ch.add(0, date);
		notifyDataSetChanged();
	}

	public void pressUpChangeOrder() {
		BackplayDate backplayDate = new BackplayDate();
		backplayDate = list_ch.get(0);
		list_ch.add(list_ch.size(), backplayDate);
		list_ch.remove(0);
		notifyDataSetChanged();

	}

	public void pressDownChangeOrder() {
		// ContentChannel channel = new ContentChannel();
		BackplayDate backplayDate = new BackplayDate();
		backplayDate = list_ch.get(list_ch.size() - 1);
		list_ch.add(0, backplayDate);
		list_ch.remove(list_ch.size() - 1);
		notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		final ViewHolder holder;
		convertView = mInflater.inflate(R.layout.backplay_item_date, null);
		holder = new ViewHolder();
		holder.tv_date_monthday = (TextView) convertView
				.findViewById(R.id.tv_backplay_date);
		holder.tv_date_weekday = (TextView) convertView
				.findViewById(R.id.tv_backplay_week);
		// System.out.println("position = " + position);
		if (position == focusIndex) {
			if (FragmentBackPlayBase.curList == 2) {
				convertView
						.setBackgroundResource(R.drawable.backplay_date_focus);
			} else
				convertView.setBackgroundResource(context.getResources()
						.getColor(R.color.nocolor));
		}

		holder.tv_date_monthday.setText(list_ch.get(position).getMonthDay());
		holder.tv_date_weekday.setText(list_ch.get(position).getWeekday());

		/** ����ǰ���ڵĺ��������ڱ�Ϊ��ɫ **/
		// if (list_ch.size() < 7) {
		// return convertView;
		// }

		if (list_ch.get(position).isFuture()) {
			holder.tv_date_monthday.setTextColor(context.getResources()
					.getColor(R.color.backplay_future_text));
			holder.tv_date_weekday.setTextColor(context.getResources()
					.getColor(R.color.backplay_future_text));
		}

		return convertView;
	}


	public void setFocusIndex(int focusIndex) {
		this.focusIndex = focusIndex;
	}

}
