package com.xike.xkliveplay.activity.fragment;

import com.xike.xkliveplay.R;
import com.xike.xkliveplay.framework.tools.VolTool;

import android.content.Context;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * @author Mernake <br>
 * CreateTime: 2015年5月19日 上午10:00:39<br>
 * <b>Info:</b><br>
 *<br>
 *   <b>Method:</b> <br>
 *   <b>Public:</b> <br>
 *   <b>Private:</b> <br>
 */
public class SoundViewControl 
{
	private View view = null;
	
	private Context context = null;
	
	private ImageView ivIcon = null;
	private SeekBar sb = null;
	private TextView tvVoice = null;

	private boolean isMute = false;
	
	public SoundViewControl(View view, Context context) 
	{
		super();
		this.view = view;
		this.context = context;
		initView();
	}

	private void initView() 
	{
		ivIcon = (ImageView) view.findViewById(R.id.iv_vol_mute);
		sb = (SeekBar) view.findViewById(R.id.sb_vol);
		tvVoice = (TextView) view.findViewById(R.id.vol_tv);
		
		int max = VolTool.create().getMaxVol(context);
		sb.setMax(max);
		int cur = VolTool.create().getCurVol(context);
		sb.setProgress(cur);
		tvVoice.setText(String.valueOf(cur));
		setMuteIcon();
	}
	
	public void onVoiceAboutKeyDown(int keyCode)
	{
		int after = 0;
		if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) 
		{
			after = VolTool.create().plusVol(context);
		}else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) 
		{
			after = VolTool.create().minusVol(context);
		}else if (keyCode == KeyEvent.KEYCODE_VOLUME_MUTE || keyCode == KeyEvent.KEYCODE_MUTE) 
		{
			setMute();
			after = VolTool.create().getCurVol(context);
			tvVoice.setText(String.valueOf(after));
			sb.setProgress(after);
			return;
		}
		
		tvVoice.setText(String.valueOf(after));
		sb.setProgress(after);
		setIcon(after);
		
	}
	
	private void setIcon(int after)
	{
		if (after > 0) 
		{
			ivIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.vol_icon));
		}else {
			ivIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.mute));
		}
	}
	
	private void setMuteIcon()
	{
		if (!isMute) 
		{
			ivIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.vol_icon));
		}else {
			ivIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.mute));
		}
	}
	
	private void setMute()
	{
//		System.out.println("isMute  = " + isMute);
//		VolTool.create().setMute(!isMute, context);
		setMuteIcon();
		isMute = !isMute;
		sb.setProgress(VolTool.create().getCurVol(context));
	}
	
	public void showVol(boolean isShow)
	{
		System.out.println("被调用 + " + isShow);
		if (isShow) 
		{
			view.setVisibility(View.VISIBLE);
			view.bringToFront();
		}else 
		{
			view.setVisibility(View.INVISIBLE);
		}
	}
	
	public boolean isViewVisible()
	{
		if (view != null) 
		{
			if (view.getVisibility() == View.VISIBLE) 
			{
				return true;
			}
		}
		return false;
	}
}
