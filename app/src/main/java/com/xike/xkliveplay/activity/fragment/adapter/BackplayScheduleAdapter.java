package com.xike.xkliveplay.activity.fragment.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xike.xkliveplay.R;
import com.xike.xkliveplay.activity.fragment.view.AlwaysMarqueeTextView;
import com.xike.xkliveplay.framework.entity.Schedule;
import com.xike.xkliveplay.framework.tools.LogUtil;

/**
 * @author liwei
 * @time 2014/2/21
 * 
 * */

public class BackplayScheduleAdapter extends BaseAdapter {
	private Context context;
	private List<Schedule> list_ch;
	private LayoutInflater mInflater;
	private static final String tag = "BackplayScheduleAdapter";

	public BackplayScheduleAdapter(Context context) {
		super();
		list_ch = new ArrayList<Schedule>();
		this.context = context;
		this.mInflater = LayoutInflater.from(context);
	}

	public void addItem(Schedule schedule) 
	{
		list_ch.add(schedule);
		
		LogUtil.e(tag,"addItem", schedule.toString());
		notifyDataSetChanged();
	}
	
	public void addItem(Schedule schedule,boolean isFocus) {
		list_ch.add(schedule);
		list_ch.get(list_ch.size() - 1).setFocus(isFocus);
		
//		LogUtil.e("addItem", schedule.toString());
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list_ch.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list_ch.get(position);
	}

	public void removeFirstAndAddInEnd(Schedule schedule) {
		if (list_ch.size() == 0)
			return;
		list_ch.remove(0);
		list_ch.add(schedule);
		notifyDataSetChanged();
	}

	public void addFirstAndRemoveEnd(Schedule schedule) {
		if (list_ch.size() == 0)
			return;
		list_ch.remove(list_ch.size() - 1);
		list_ch.add(0, schedule);
		notifyDataSetChanged();
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public void removeAll() {
		list_ch.clear();
	}
	
	public void removeAllAndNotify() {
		list_ch.clear();
		notifyDataSetChanged();
	}
	
	public void setIsFocus(String uniId,boolean isFocus)
	{
		for(int i = 0; i < list_ch.size();i++)
		{
			if(list_ch.get(i).getContentId().equals(uniId))
			{
				list_ch.get(i).setFocus(isFocus);
				notifyDataSetChanged();
				return;
			}
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		if(convertView == null)
		{
			convertView = mInflater.inflate(R.layout.backplay_item_schedule, null);
		}
		
		Schedule schedule = list_ch.get(position);
		
		TextView tv_backplay_time = (TextView) convertView
				.findViewById(R.id.tv_backplay_time);
		AlwaysMarqueeTextView tv_backplay_name = (AlwaysMarqueeTextView) convertView
				.findViewById(R.id.tv_backplay_name);
		LinearLayout ll_backplay_main = (LinearLayout) convertView
				.findViewById(R.id.ll_backplay_item);
		ImageView iv_backplay_current = (ImageView) convertView
				.findViewById(R.id.iv_backplay_current);

		if("1".equals(schedule.getStatus())) 
		{
			tv_backplay_name.setTextColor(context.getResources()
					.getColor(R.color.backplay_future_text));
			tv_backplay_time.setTextColor(context.getResources()
					.getColor(R.color.backplay_future_text));
		}else {
			tv_backplay_name.setTextColor(context.getResources()
					.getColor(R.color.white));
			tv_backplay_time.setTextColor(context.getResources()
					.getColor(R.color.white));
		}

		if (schedule.isCurrent()) 
		{
			iv_backplay_current.setVisibility(View.VISIBLE);
		}else {
			iv_backplay_current.setVisibility(View.INVISIBLE);
		}

		if (schedule.isFocus()) 
		{
			ll_backplay_main.setBackgroundResource(R.drawable.backplay_schedule_focus);
			tv_backplay_name.isScroll = true;
		}else {
			ll_backplay_main.setBackgroundResource(context
					.getResources().getColor(R.color.nocolor));
			tv_backplay_name.isScroll = false;
		}

		String str_time = makeTime(schedule.getStartTime());
		tv_backplay_time.setText(str_time);
		tv_backplay_name.setText(schedule.getProgramName().trim());

		return convertView;
	}

	private String makeTime(String startTime) {
		String hh = startTime.substring(0, 2);
		String mm = startTime.substring(2, 4);
		if (hh.indexOf("0") == 0) {
			hh = hh.substring(1);
		}
		return hh + ":" + mm;
	}
	
}
