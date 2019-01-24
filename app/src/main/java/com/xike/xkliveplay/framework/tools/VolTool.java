package com.xike.xkliveplay.framework.tools;

import android.content.Context;
import android.media.AudioManager;

/**
 * @author LiWei <br>
 * CreateTime: 2014年12月3日 下午12:13:14<br>
 * <b>Info:</b><br>
 *<br>
 *   <b>Method:</b> <br>
 */
public class VolTool 
{
	private AudioManager audioManager = null;
	
	private static VolTool volTool = null;
	
	public static VolTool create()
	{
		if (volTool == null) 
		{
			volTool = new VolTool();
		}
		return volTool;
	}
	
	private void initAudioManager(Context context)
	{
		audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
	}
	
	/**
	 * 
	 * function:增大音量值 
	 * @param context
	 * @return	返回增加后的当前音量值
	 */
	public int plusVol(Context context)
	{
		if (audioManager == null) 
		{
			initAudioManager(context);
		}
		
		audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,AudioManager.ADJUST_RAISE, 0);
		return getCurVol(context);
	}
	
	/**
	 * 
	 * function:减小音量值 
	 * @param context 
	 * @return 返回减小后的当前音量值
	 */
	public int minusVol(Context context)
	{
		if (audioManager == null) 
		{
			initAudioManager(context);
		}
		
		audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,AudioManager.ADJUST_LOWER, 0);
		return getCurVol(context);
	}
	
	
	/**
	 * 
	 * function:获得当前音量 
	 * @param context
	 * @return 当前音量
	 */
	public int getCurVol(Context context)
	{
		if (audioManager == null) 
		{
			initAudioManager(context);
		}
		
		return audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
	}
	
	
	/**
	 * 
	 * function:获取音量最大值 
	 * @param context
	 * @return 最大音量
	 */
	public int getMaxVol(Context context)
	{
		if (audioManager == null) 
		{
			initAudioManager(context);
		}
		
		return audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
	}
	
	public void setMute(boolean flag,Context context)
	{
		if (audioManager == null) 
		{
			initAudioManager(context);
		}
		
		if (flag) 
		{
			audioManager.setStreamMute(AudioManager.STREAM_MUSIC, true);
		}else {
			audioManager.setStreamMute(AudioManager.STREAM_MUSIC, false);
		}
	}
}
