
package com.xike.xkliveplay.framework.arcplay;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.arcsoft.media.ArcPlayer;
import com.arcsoft.media.ArcPlayer.TYPE_DRM;
import com.xike.xkliveplay.framework.tools.LogUtil;

/**
 * 
 * @author LiWei <br>
 * CreateTime: 2014年10月29日 上午9:59:57<br>
 * <b>Info:</b><br>
 *<br>
 *   <b>Method:</b> <br>
 */
public class ArcPlayControl implements IPlayerStateListener{

	private static final String TAG = "ArcPlayerSampleActivity";
	private Context mContext = null;

	private ArcSurfaceView videoView = null;
	
	private IPlayControl iPlayControl = null;
	
	public boolean bPrepared = false;
	private static final int UPDATE_POSITION = 0x0001;
	private static final int SHOW_BUFFER_MESSAGE = 0x0002;
	private static final int SHOW_P2P_MESSAGE = 0x0003;
	
	private int seekStep = 0;//NoDRM_seg_000_1000.ts_3,
    
	private int lastPosition = 0;
	
	private String url = null;
	
	/* DRM params */
	private  TYPE_DRM drmType = TYPE_DRM.NO_DRM; //����������
	private  String userID = null;               //�û�id
	private  String tokenID = null;              //��֤token
	private  String dmrURL = null;               //DRM ����������������ʽ�����ڡ�http://xxx.yyy.zzz:port/��
	private  String serviceID = null;            //�㲥ʱ��ʾ contentID��ֱ��ʱ��ʾ serviceID
	private  String productid = null;            //��Ʒ ID
	private  String deviceID = null;             //�豸ID
	private int type = 0;                        //�������ͣ�0 ��ʾֱ����1 ��ʾ�㲥
	
	private IPlayFinished iPlayFinished = null;
	
	
	public IPlayFinished getiPlayFinished() 
	{
		return iPlayFinished;
	}

	public void setiPlayFinished(IPlayFinished iPlayFinished) {
		this.iPlayFinished = iPlayFinished;
	}

	public ArcPlayControl(Context context,ArcSurfaceView surfaceView,IPlayControl playControl)
	{
		this.mContext = context;
		
		this.iPlayControl = playControl;
		
		this.videoView = surfaceView;
	}
	
	public void arcPlayer_play() 
	{
		// stop music player
		Intent intent = new Intent();
		intent.setAction("com.android.music.musicservicecommand.pause");
		intent.putExtra("command", "stop");
		this.mContext.sendBroadcast(intent);
		
		try {
			if (null != url) {
				lastPosition = 0;

				System.out.println("drmType:" + drmType);
				System.out.println("userID:" + userID);
				System.out.println("tokenID:" + tokenID);
				System.out.println("dmrURL:" + dmrURL);
				System.out.println("serviceID:" + serviceID);
				System.out.println("productid:" + productid);
				System.out.println("type:" + type);
				System.out.println("deviceID:" + deviceID);
				
				videoView.setDRMConfig(drmType,userID, tokenID, dmrURL,serviceID,productid,type,deviceID); 
//				videoView.setDRMConfig(TYPE_DRM.P2P_DRM,userID, tokenID, dmrURL,serviceID,productid,type,deviceID); 
				videoView.setPlayerStateListener(this);
				videoView.setVideoPath(url);
				videoView.setOnCompletionListener(mOnCompletionListener);
				videoView.setOnPreparedListener(mOnPreparedListener);
				videoView.setOnInfoListener(mOnInfoListener);
				videoView.setOnErrorListener(mOnErrorListener);
				videoView.setOnSeekCompleteListener(mOnSeekCompleteListener);
				videoView.setOnBufferingUpdateListener(mOnBufferingUpdateListener);
				videoView.setOnDRMErrorMsgListener(mOnDRMErrorListener);
				videoView.setOnP2PBitrateChangedListener(mOnP2PBitrateChangedListener);
				//mPlayer.setDisplay(holder);
				//mPlayer.setDataSource(this,Uri.parse(strUri));
				//mPlayer.prepareAsync();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setOnCompleteListener()
	{
		if (videoView == null) 
		{
			LogUtil.e(TAG,"setOnCompleteListener", "videoView = null");
			return;
		}
		
		videoView.setOnCompletionListener(mOnCompletionListener);
	}
	
	@Override
	public void onPlayerStateChanged(int state, int what, int extra) {
		Log.d(TAG, "player onPlayerStateChanged what =" + what + ", state = " + state);
		if(state == IPlayerStateListener.PLAYER_FAIL){
			iPlayControl.isBufferData(false);
			@SuppressWarnings("unused")
			String InfoStr = null;
			InfoStr = Errorno.getErrorInfo(what,extra);
			Log.d(TAG, "Player error, msg.arg2 = " + Integer.toString(state));
		}
		if(state == IPlayerStateListener.PLAYER_PREPARING){
			Log.d(TAG, "player is in PLAYER_PREPARING");
			iPlayControl.isBufferData(true);
		}
		if(state == IPlayerStateListener.PLAYER_PREPARED){
			Log.d(TAG, "player is in PLAYER_PREPARED");
			iPlayControl.isBufferData(false);
		}
	}
	
	/**
	 * Register a callback, when play finished ,invoke this
	 */
	private ArcPlayer.OnCompletionListener mOnCompletionListener = new ArcPlayer.OnCompletionListener() {

		@Override
		public void onCompletion(ArcPlayer arcplayer) 
		{
			// TODO Auto-generated method stub
			LogUtil.e(TAG,"onCompletion", "arc:****************************play finished***************");
			if (iPlayFinished != null) 
			{
				iPlayFinished.playfinished();
			}
			bPrepared = false;
			try {
//				BufferMessagePool.instance.unRegistBufferTimeListener(bufferTimeListener);
				if(videoView != null)
				{
					videoView.stopPlayback();
//					LogUtil.e("arc", "arc:****************************play finished***************");
				}
			} catch (Exception e) 
			{
				e.printStackTrace();
			}

		}
	};
	
	private ArcPlayer.OnPreparedListener mOnPreparedListener = new ArcPlayer.OnPreparedListener() {
		@Override
		public void onPrepared(ArcPlayer arcplayer) 
		{
			if (videoView == null || iPlayControl == null) 
			{
				return;
			}
			bPrepared = true;
			onPlayerStateChanged(IPlayerStateListener.PLAYER_PREPARED, 0,0);
			if (bPrepared) 
			{
				LogUtil.e(TAG,"onPrepared", "Start to play!");
				videoView.start();// prepared
				iPlayControl.isBufferData(false);
			}
		}
	};
	
	/**
	 *  Register a callback,when  video stream  cache status  change, invoke here.֪
	 */
	private ArcPlayer.OnBufferingUpdateListener mOnBufferingUpdateListener = new ArcPlayer.OnBufferingUpdateListener() {
		
		@Override
		public void onBufferingUpdate(ArcPlayer arg0, int arg1) {
			// TODO Auto-generated method stub
			Log.d("OnBufferingUpdateListener", "arg1 = " + arg1);
		}
	};
	
	/**
	 *  Register a callback, get notification when info or warning appears.
	 *  Notificate cache start or end.
	 */
	private ArcPlayer.OnInfoListener mOnInfoListener = new ArcPlayer.OnInfoListener() {

		@Override
		public boolean onInfo(ArcPlayer arcplayer, int what, int extra) {
			// TODO Auto-generated method stub
			switch (what) {
			case  ArcPlayer.MEDIA_INFO_BUFFERING_START:
				System.out.println("abcdefg --start: ");
				Log.d("OnInfoListener", "MEDIA_INFO_BUFFERING_START,extra = " + extra);
				iPlayControl.isBufferData(true);
				break;
			case  ArcPlayer.MEDIA_INFO_BUFFERING_END:
				System.out.println("abcdefg --end: ");
				Log.d("OnInfoListener", "MEDIA_INFO_BUFFERING_END,extra = " + extra);
				iPlayControl.isBufferData(false);
				break;
			default:
				break;
			}
			return true;
		}
	};
	
	/**
	 *  Register a  callback , notificate when seek done. ֪
	 */
	ArcPlayer.OnSeekCompleteListener mOnSeekCompleteListener = new ArcPlayer.OnSeekCompleteListener() {
		
		@Override
		public void onSeekComplete(ArcPlayer ap) {
			// TODO Auto-generated method stub
			if(isPlaying() && seekStep != 0)
			{
				int curPosition = lastPosition;
				Log.d("OnSeekCompleteListener", "seekTo,OnSeekCompleteListener:" + curPosition);
				videoView.seekTo(seekStep + curPosition);
			}
		}
	};
	
	/**
	 *   Register a callback , notificate when asynchronous failed.
	 */
	ArcPlayer.OnErrorListener mOnErrorListener = new ArcPlayer.OnErrorListener() {

		@Override
		public boolean onError(ArcPlayer ap, int what, int extra) {
			// TODO Auto-generated method stub
			
			onPlayerStateChanged(IPlayerStateListener.PLAYER_FAIL,what, extra);
			if (iCode !=null)
			{
				iCode.onPlayerErrorCode(what);
			}

			return false;
		}
	};
	
	private ArcPlayer.OnP2PBitrateChangedListener mOnP2PBitrateChangedListener = new ArcPlayer.OnP2PBitrateChangedListener() {
		
		@Override
		public void onChanged(ArcPlayer ap,int p1, int p2) {
			// TODO Auto-generated method stub
			Message bufferMessage = handler.obtainMessage(SHOW_P2P_MESSAGE, p1, p2);
			handler.sendMessage(bufferMessage);
		}
	};

	/** 
	 *     Register a  callback,notificate when DRM has an error֪
	 */
	ArcPlayer.OnDRMErrorListener mOnDRMErrorListener = new ArcPlayer.OnDRMErrorListener() {
		
		@Override
		public void onError(ArcPlayer arg0, int method, int errorCode) 
		{
			// TODO Auto-generated method stub
			@SuppressWarnings("unused")
			Class <? extends ArcPlayer.OnDRMErrorListener> klass  = getClass();
			String InfoStr = "OnDRMErrorListener,method = " + method + ",errorCode = " + errorCode;
			Log.d(TAG, InfoStr);
		}
	};
	
	/**
	 *    Get now play time and total time.
	 */
	@SuppressLint("HandlerLeak")
	final Handler handler = new Handler() 
	{
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case UPDATE_POSITION:
				if(bPrepared && videoView != null)
				{
//					int position = videoView.getCurrentPosition();
//					int duration = videoView.getDuration();
				}
				break;
			case SHOW_BUFFER_MESSAGE:
//				Toast tp = null;
//				String InfoStr = "IBufferTimeListener,inTime = " + msg.arg1 + ",played = " + msg.arg2;
//				if (tp == null) {
//					tp = Toast.makeText(mContext,InfoStr, Toast.LENGTH_SHORT);
//				}
//				tp.show();
				break;
			case SHOW_P2P_MESSAGE:
				String InfoStr1 = "OnP2PBitrateChangedListener,OldBitRate = " + msg.arg1 + ",NewBitRate = " + msg.arg2;
				LogUtil.i(TAG, "handler", InfoStr1);
				break;
			}
			super.handleMessage(msg);
		}
	};
	

	public void stop()
	{
		bPrepared = false;
		if(videoView != null)
		{
			videoView.stopPlayback();
			videoView  = null;
		}
	}
	
	public boolean isPlaying()
	{
		if(videoView != null)
		{
			return videoView.isPlaying();
		}
		return false;
	}
	
	
	public void setUri(String uri) {
		this.url = uri;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public void setTokenID(String tokenID) {
		this.tokenID = tokenID;
	}
	public void setServiceID(String serviceID) {
		this.serviceID = serviceID;
	}
	public void setProductid(String productid) {
		this.productid = productid;
	}
	public void setType(int type) {
		this.type = type;
	}
	public void setDeviceID(String deviceID) {
		this.deviceID = deviceID;
	}
	public void setDrmType(TYPE_DRM drmType) {
		this.drmType = drmType;
	}
	public void setDmrURL(String dmrURL) {
		this.dmrURL = dmrURL;
	}

   //Rongzai add
	public interface IReturnPlayerErrorCode
	{
		void onPlayerErrorCode(int code);
	}

	private IReturnPlayerErrorCode iCode = null;

	public void setiCode(IReturnPlayerErrorCode iCode)
	{
		this.iCode = iCode;
	}
}
