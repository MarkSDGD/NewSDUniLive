package com.xike.xkliveplay.activity.fragment;

import android.os.SystemProperties;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.xike.xkliveplay.R;
import com.xike.xkliveplay.framework.data.DataAnalyzer;
import com.xike.xkliveplay.framework.entity.ContentChannel;
import com.xike.xkliveplay.framework.entity.UploadObject;
import com.xike.xkliveplay.xunfei.XunfeiTools;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 
 * @author LiWei <br>
 * CreateTime: 2014年10月29日 下午2:31:20<br>
 * <b>Info:</b><br>
 *<br>
 *   <b>Method:</b> <br>
 */
public class FragmentBase extends Fragment
{
	public boolean isExit = false;
	public boolean isJumpTo = false;
	
	public UploadObject uploadObject = new UploadObject();
	/** Playing channel **/
	public ContentChannel curPlayingChannel = null;
	
	public boolean isJumpTo() {
		return isJumpTo;
	}

	public void setJumpTo(boolean isJumpTo) {
		this.isJumpTo = isJumpTo;
	}

	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		return true;
	}
	
	public boolean onkeyDownReturn()
	{
		return true;
	}
	
	public void initToast() 
	{
		View toastRoot = getActivity().getLayoutInflater().inflate(
				R.layout.custome_toast_layout, null);
		Toast toastStart = new Toast(getActivity().getApplicationContext());
		toastStart.setGravity(Gravity.BOTTOM, 0, getActivity().getResources().getDimensionPixelSize(R.dimen.px_80));
		toastStart.setDuration(Toast.LENGTH_LONG);
		toastStart.setView(toastRoot);
		toastStart.show();
	}

	public void initToast(int fragmentType) //#18041 fragmentTypw 1:live 2:backplay
	{
		//		View toastRoot = getActivity().getLayoutInflater().inflate(
		//				R.layout.custome_toast_layout, null);
		//		Toast toastStart = new Toast(getActivity().getApplicationContext());
		//		toastStart.setGravity(Gravity.BOTTOM, 0, getActivity().getResources().getDimensionPixelSize(R.dimen.px_80));
		//		toastStart.setDuration(Toast.LENGTH_LONG);
		//		toastStart.setView(toastRoot);
		//		toastStart.show();

		if (fragmentType == 1) {
			Toast.makeText(getActivity(), "再按一次退出直播", Toast.LENGTH_SHORT).show();
		}else if (fragmentType == 2) {
			Toast.makeText(getActivity(), "再按一次退出回看", Toast.LENGTH_SHORT).show();
		}else {
			Log.e("FragmentBase", "fragmentType is: " + fragmentType);
		}
	}
	
	public void exitBy2Click() 
	{
		Timer tExit = null;
		if (isExit == false) 
		{
			isExit = true; // 准备退出
			initToast();
			tExit = new Timer();
			tExit.schedule(new TimerTask() {
				@Override
				public void run() {
					isExit = false; // 取消退出
				}
			}, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务
			SystemProperties.set("libplayer.livets.softdemux", "1");
		} else {
			if (curPlayingChannel != null) 
			{
				uploadObject.setChannelId(curPlayingChannel.getContentId());
				uploadObject.setChannelName(curPlayingChannel.getName());
				uploadObject.setProgramStatus(1);
				DataAnalyzer.creator().uploadLivePlay(uploadObject);
				ScheduleCache.clearData();
				DataAnalyzer.creator().clearParam();
			}
			if (this instanceof FragmentLivePlayBase) 
			{
				XunfeiTools.getInstance().sendLiveStatusBroadcast(getActivity(), false);
			}else{
				XunfeiTools.getInstance().sendBackPlayBroadcast(getActivity(), false);
			}
			getActivity().finish();
		}
	}
	
	public void forceCloseAPK()
	{
//		isStopCountThread = true;
//		isTimeShiftMode = false;
		if (curPlayingChannel != null) 
		{
			uploadObject.setChannelId(curPlayingChannel.getContentId());
			uploadObject.setChannelName(curPlayingChannel.getName());
			uploadObject.setProgramStatus(1);
			DataAnalyzer.creator().uploadLivePlay(uploadObject);
			ScheduleCache.clearData();
			DataAnalyzer.creator().clearParam();
		}
//		closeP2PAPK();
		if (this instanceof FragmentLivePlayBase) 
		{
			XunfeiTools.getInstance().sendLiveStatusBroadcast(getActivity(), false);
		}else XunfeiTools.getInstance().sendBackPlayBroadcast(getActivity(), false);
		
		clearData();
		getActivity().finish();
	}
	
	private void clearData()
	{
		ScheduleCache.clearData();
		XunfeiTools.deInstance();
	}
	
	/**跳转到多少秒播放**/
	public void voiceJumpToSeconds(int seconds)
	{
		
	}
	
	/**快进多少秒播放**/
	public void voiceJumpForwordBySeconds(int seconds)
	{
		
	}
	
	/**快退多少秒播放**/
	public void voiceJumpBackwardBySeconds(int seconds)
	{
		
	}
//	
	/****/
	public void voicePlay()
	{
		
	}
	
	/****/
	public void voicePause()
	{
		
	}
	
	/****/
	public void voiceResume()
	{
		
	}
	
	/****/
	public void voiceRestartPlay()
	{
		
	}
	
	public void jumpToLive()
	{
		
	}
	
	public void jumpToResee()
	{
		
	}

	/**
	 * 直播专用的一个接口
	 * 在VIP频道试看的时候，如果断了网，那么就停掉试看回看时间计数器，
	 * 在OrderDialog已经出现的情况下，dismiss这个Dialog
	 * 这是为了防止发生不可描述的错误
	 */
	public void stopCountDownOrdismissOrderDialog(){

	}
}
