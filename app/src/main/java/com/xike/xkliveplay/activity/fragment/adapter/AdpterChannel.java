
package com.xike.xkliveplay.activity.fragment.adapter;

import java.util.ArrayList;
import java.util.List;

import com.xike.xkliveplay.R;
import com.xike.xkliveplay.framework.entity.ContentChannel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 
 * @author LiWei <br>
 * CreateTime: 2014年10月29日 下午3:50:34<br>
 * <b>Info:</b><br>
 *<br>
 *   <b>Method:</b> <br>
 */
public class AdpterChannel extends BaseAdapter{

	private Context mContext = null;
	
	private List<ContentChannel> list = null;
	
	private int focusIndex = 3;
	
	public AdpterChannel(Context context)
	{
		this.mContext = context;
		list = new ArrayList<ContentChannel>();
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}
	
	public void addItem(ContentChannel channel)
	{
		list.add(channel);
		notifyDataSetChanged();
	}

	public void removeFirstAndAddInEnd(ContentChannel endChannel)
	{
		list.remove(0);
		list.add(endChannel);
		notifyDataSetChanged();
	}
	
	public void removeFirst()
	{
		list.remove(0);
		notifyDataSetChanged();
	}
	
	public void delItemByIndex(int index)
	{
		list.remove(index);
		notifyDataSetChanged();
	}
	
	public void addFirstAndRemoveEnd(ContentChannel firstChannel)
	{
		list.remove(list.size() - 1);
		list.add(0, firstChannel);
		notifyDataSetChanged();
	}
	
	public void updateCurChooseSaveState(boolean isAdd)
	{
		if(isAdd)
		{
			list.get(focusIndex).setZipCode("save");
		}else {
			list.get(focusIndex).setZipCode("");
		}
		notifyDataSetChanged();
	}
	
	public void updateCurListSaveState(List<ContentChannel> saveList)
	{
		for(int i = 0; i < list.size();i++)
		{
			for(int j = 0; j < saveList.size();j++)
			{
				if(list.get(i).getContentId().equals(saveList.get(j).getContentId()))
				{
					list.get(i).setZipCode("save");
					notifyDataSetChanged();
				}
			}
		}
	}
	
	public void removeAll()
	{
		list.clear();
		notifyDataSetChanged();
	}
	
	@Override
	public ContentChannel getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
//		LogUtil.e("AdapterChannel", "getView ֮ǰʱ�� �� " + System.currentTimeMillis());
		if(convertView == null)
		{
			convertView = LayoutInflater.from(mContext).inflate(R.layout.list_channel_item, null);
		}
		
		ContentChannel channel = getItem(position);
		TextView tv_channel_num = (TextView)convertView.findViewById(R.id.tv_channel_num);
		TextView tv_channel_name = (TextView)convertView.findViewById(R.id.tv_channel_name);
		tv_channel_num.setText(channel.getChannelNumber());
		tv_channel_name.setText(channel.getName());
		
		ImageView iv = (ImageView)convertView.findViewById(R.id.iv_issave);
		if("save".equals(channel.getZipCode()))
		{
			iv.setBackgroundResource(R.drawable.save);
//			LogUtil.i("getView", channel.getName() + "*************************************setSave");
		}else {
			iv.setBackgroundResource(mContext.getResources().getColor(R.color.transparent_background));
		}
		
		LinearLayout lbg = (LinearLayout)convertView.findViewById(R.id.ll_bg);
		if(position == focusIndex)
		{
			lbg.setBackgroundResource(R.drawable.channel_focus);
		}else {
			lbg.setBackgroundResource(mContext.getResources().getColor(R.color.transparent_background));
		}
		
		
		return convertView;
		
	}
	
	public int getFocusIndex() {
		return focusIndex;
	}

	public void setFocusIndex(int focusIndex) {
		this.focusIndex = focusIndex;
	}

}
