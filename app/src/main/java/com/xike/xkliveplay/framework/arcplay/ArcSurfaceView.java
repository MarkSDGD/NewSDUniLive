package com.xike.xkliveplay.framework.arcplay;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.arcsoft.media.ArcPlayer;
import com.arcsoft.media.ArcPlayer.OnBufferingUpdateListener;
import com.arcsoft.media.ArcPlayer.OnCompletionListener;
import com.arcsoft.media.ArcPlayer.OnDRMErrorListener;
import com.arcsoft.media.ArcPlayer.OnErrorListener;
import com.arcsoft.media.ArcPlayer.OnInfoListener;
import com.arcsoft.media.ArcPlayer.OnSeekCompleteListener;
import com.arcsoft.media.ArcPlayer.TYPE_DRM;
import com.xike.xkliveplay.framework.tools.LogUtil;
import com.xike.xkliveplay.framework.varparams.Var;

import java.io.IOException;

public class ArcSurfaceView extends SurfaceView{
	private static String TAG = "VideoView";
	private static String tag = ArcSurfaceView.class.getSimpleName();

    private Context mContext;
    private String userID = null;
	private String tokenID = null;
	private String drmURL = null;
	
	private String productID = null;
	private String serviceID = null;
	private String deviceID = null;
	private int type = 0;
	private TYPE_DRM drmType = TYPE_DRM.NO_DRM;
	
    // settable by the client
    private Uri         mUri;

    // All the stuff we need for playing and showing a video
    private SurfaceHolder mSurfaceHolder = null;
    private ArcPlayer mMediaPlayer = null;
    private boolean     mIsPrepared;
    private int         mVideoWidth;
    private int         mVideoHeight;
    private int         mSurfaceWidth;
    private int         mSurfaceHeight;
    private ArcPlayer.OnPreparedListener mOnPreparedListener;
    private ArcPlayer.OnSeekCompleteListener mOnSeekCompleteListener;
    private ArcPlayer.OnErrorListener mOnErrorListener;
    private ArcPlayer.OnCompletionListener mOnCompletionListener;
    private ArcPlayer.OnInfoListener mOnInfoListener;
    private ArcPlayer.OnBufferingUpdateListener mOnBufferingUpdateListener;
    private ArcPlayer.OnDRMErrorListener mOnDRMErrorListener;
    private ArcPlayer.OnP2PBitrateChangedListener mOnP2PBitrateChangedListener;

	private IPlayerStateListener mPlayerStateListener;
    
    private boolean     mStartWhenPrepared;
    
    private IPlayFinished iPlayFinished = null;
    
    public ArcSurfaceView(Context context) 
    {
        super(context);
        mContext = context;
        initVideoView();
    }

    public ArcSurfaceView(Context context, AttributeSet attrs) 
    {
        this(context, attrs, 0);
        mContext = context;
        initVideoView();
    }
    
     

    public IPlayFinished getiPlayFinished() {
		return iPlayFinished;
	}

	public void setiPlayFinished(IPlayFinished iPlayFinished) {
		this.iPlayFinished = iPlayFinished;
	}

	public ArcSurfaceView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        initVideoView();
    }
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //Log.i("@@@@", "onMeasure");
    	LogUtil.e(tag, "onMeasure","onMeasure: mVideoWidth = " + mVideoWidth);
    	LogUtil.e(tag, "onMeasure","onMeasure: mVideoHeight = " + mVideoHeight);
    	LogUtil.e(tag, "onMeasure","onMeasure: widthMeasureSpec = " + widthMeasureSpec);
    	LogUtil.e(tag, "onMeasure","onMeasure: heightMeasureSpec = " + heightMeasureSpec);
        int width = getDefaultSize(mVideoWidth, widthMeasureSpec);
//    	int width = 
        int height = getDefaultSize(mVideoHeight, heightMeasureSpec);
        LogUtil.e(tag, "onMeasure","onMeasure: width = " + width);
        LogUtil.e(tag, "onMeasure","onMeasure: height = " + height);
        setMeasuredDimension(width+16,height+9);
        
//        setMeasuredDimension(1024,768);
    }

    public int resolveAdjustedSize(int desiredSize, int measureSpec) 
    {
        int result = desiredSize;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize =  MeasureSpec.getSize(measureSpec);
        LogUtil.e(tag, "resolveAdjustedSize","result = " + result);
        LogUtil.e(tag, "resolveAdjustedSize","resolveAdjustedSize: specMode = " + specMode);
        LogUtil.e(tag, "resolveAdjustedSize","resolveAdjustedSize: specSize = " + specSize);

        switch (specMode) {
            case MeasureSpec.UNSPECIFIED:
                /* Parent says we can be as big as we want. Just don't be larger
                 * than max size imposed on ourselves.
                 */
                result = desiredSize;
                break;

            case MeasureSpec.AT_MOST:
                /* Parent says we can be as big as we want, up to specSize.
                 * Don't be larger than specSize, and don't be larger than
                 * the max size imposed on ourselves.
                 */
                result = Math.min(desiredSize, specSize);
                break;

            case MeasureSpec.EXACTLY:
                // No choice. Do what we are told.
                result = specSize;
                break;
        }
        return result;
}

    private void initVideoView() {
        mVideoWidth = 0;
        mVideoHeight = 0;
        getHolder().addCallback(mSHCallback);
        setFocusable(true);
        setFocusableInTouchMode(true);
        requestFocus();
    }

    
    public void setVideoPath(String path) {
        setVideoURI(Uri.parse(path));
    }

    public void setVideoURI(Uri uri) {
        mUri = uri;
        mStartWhenPrepared = false;
        openVideo();
        requestLayout();
        invalidate();
    }

    public void stopPlayback() {
        if (mMediaPlayer != null) 
        {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }
    
    public void stop()
    {
    	if(mMediaPlayer != null)
    	{
    		LogUtil.e(tag,"stop", "aaaaaaaaaaaaaaaaaaaaaaaa: stop");
    		mMediaPlayer.stop();
    		mMediaPlayer.reset();
    	}
    }

    private void openVideo() {
        if (mUri == null || mSurfaceHolder == null) {
            // not ready for playback just yet, will try again later
            return;
        }
        
        Intent i = new Intent("com.android.music.musicservicecommand");
        i.putExtra("command", "pause");
        mContext.sendBroadcast(i);

        if (mMediaPlayer != null) {
            mMediaPlayer.reset();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
        try {
            mMediaPlayer = new ArcPlayer();
            mMediaPlayer.setOnPreparedListener(mPreparedListener);
            mMediaPlayer.setOnCompletionListener(mOnCompletionListener);
            mMediaPlayer.setOnErrorListener(mOnErrorListener);
            mMediaPlayer.setOnInfoListener(mOnInfoListener);
            mMediaPlayer.setOnSeekCompleteListener(mOnSeekCompleteListener);
            mMediaPlayer.setOnBufferingUpdateListener(mOnBufferingUpdateListener);
            mMediaPlayer.setOnDRMErrorListener(mOnDRMErrorListener);
            mMediaPlayer.setOnP2PBitrateChangedListener(mOnP2PBitrateChangedListener);
            mIsPrepared = false;
            Log.v(TAG, "reset duration to -1 in openVideo");
            String flashPath = getContext().getFilesDir().getAbsolutePath();
            Log.v(TAG, "flashPath = " + flashPath);
            
            mMediaPlayer.setApkDirectory(Var.getDataDir(getContext()));
            mMediaPlayer.setDeviceID(deviceID);
            mMediaPlayer.setDRMConfig(drmType, productID, userID, tokenID, drmURL, type, serviceID, flashPath);
            mMediaPlayer.setDataSource(mContext, mUri);
            //setDisPlay(mSurfaceHolder);33
            mMediaPlayer.setDisplay(mSurfaceHolder);
            //setFullScreen();
           // mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
           // mMediaPlayer.setScreenOnWhilePlaying(true);
            mMediaPlayer.prepareAsync();
            if(mPlayerStateListener != null){
            	mPlayerStateListener.onPlayerStateChanged(IPlayerStateListener.PLAYER_PREPARING, 0,0);
            }
        } catch (IOException ex) {
            Log.w(TAG, "Unable to open content: " + mUri, ex);
            if(mPlayerStateListener != null){
            	mPlayerStateListener.onPlayerStateChanged(IPlayerStateListener.PLAYER_FAIL, IPlayerStateListener.PLAYER_CONNECT_FAIL,0);
            }
            return;
        } catch (IllegalArgumentException ex) {
            Log.w(TAG, "Unable to open content: " + mUri, ex);
            if(mPlayerStateListener != null){
            	mPlayerStateListener.onPlayerStateChanged(IPlayerStateListener.PLAYER_FAIL, IPlayerStateListener.PLAYER_PREPARE_FAIL,0);
            }
            return;
        }
    }

    ArcPlayer.OnPreparedListener mPreparedListener = new ArcPlayer.OnPreparedListener() {
        public void onPrepared(ArcPlayer mp) {
            // briefly show the mediacontroller
            mIsPrepared = true;
            if (mOnPreparedListener != null) {
                mOnPreparedListener.onPrepared(mMediaPlayer);
            }

            mVideoWidth = mp.getVideoWidth();
            mVideoHeight = mp.getVideoHeight();
            
            LogUtil.e(tag, "OnPreparedListener","mVideoWidth = " + mVideoWidth);
            LogUtil.e(tag, "OnPreparedListener","mVideoHeight = " + mVideoHeight);
            if (mVideoWidth != 0 && mVideoHeight != 0) 
            {
                //Log.i("@@@@", "video size: " + mVideoWidth +"/"+ mVideoHeight);
                getHolder().setFixedSize(mVideoWidth, mVideoHeight);
                //setFullScreen();
            	//setDisPlay(mSurfaceHolder);
                if (mSurfaceWidth == mVideoWidth && mSurfaceHeight == mVideoHeight) 
                {
                    // We didn't actually change the size (it was already at the size
                    // we need), so we won't get a "surface changed" callback, so
                    // start the video here instead of in the callback.
                    if (mStartWhenPrepared) 
                    {
                        mMediaPlayer.start();
                        mStartWhenPrepared = false;
                    }
                }
            } else {
                // We don't know the video size yet, but should start anyway.
                // The video size might be reported to us later.
                if (mStartWhenPrepared) {
                    mMediaPlayer.start();
                    mStartWhenPrepared = false;
                }
            }
        }
    };

    /**
     * Register a callback to be invoked when the media file
     * is loaded and ready to go.
     *
     * @param l The callback that will be run
     */
    public void setOnPreparedListener(ArcPlayer.OnPreparedListener l)
    {
        mOnPreparedListener = l;
    }

    /**
     * Register a callback to be invoked when the end of a media file
     * has been reached during playback.
     *
     * @param l The callback that will be run
     */

    /**
     * Register a callback to be invoked when an error occurs
     * during playback or setup.  If no listener is specified,
     * or if the listener returned false, VideoView will inform
     * the user of any errors.
     *
     * @param l The callback that will be run
     */

    SurfaceHolder.Callback mSHCallback = new SurfaceHolder.Callback()
    {
        public void surfaceChanged(SurfaceHolder holder, int format,
                                    int w, int h)
        {
        	Log.d(TAG, "mSurfaceWidth = " + w + "mSurfaceHeight = " + h);
            mSurfaceWidth = w;
            mSurfaceHeight = h;
            
            if (mMediaPlayer != null && mIsPrepared && mVideoWidth == w && mVideoHeight == h) {
                mMediaPlayer.start();
            }
        }

        public void surfaceCreated(SurfaceHolder holder)
        {
            mSurfaceHolder = holder;
            Log.d(TAG, "mSurfaceHolder = " + mSurfaceHolder.getSurfaceFrame());
            openVideo();
        }

        public void surfaceDestroyed(SurfaceHolder holder)
        {
            // after we return from this we can't use the surface any more
            mSurfaceHolder = null;
            if (mMediaPlayer != null) {
            	stopPlayback();
            }
        }
    };

    public void start() {
        if (mMediaPlayer != null && mIsPrepared) {
                mMediaPlayer.start();
                mStartWhenPrepared = false;
        } else {
            mStartWhenPrepared = true;
        }
    }

    public void pause() {
        if (mMediaPlayer != null && mIsPrepared) {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.pause();
            }
        }
        mStartWhenPrepared = false;
    }
    
    public void resume(){
    	if (mMediaPlayer != null && mIsPrepared) {
			if (mMediaPlayer.isPlaying()) {
				
			}else {
				mMediaPlayer.start();
			}
		}
    }


    public boolean isPlaying() {
        if (mMediaPlayer != null && mIsPrepared) {
            return mMediaPlayer.isPlaying();
        }
        return false;
    }

	public void setOnCompletionListener(
			OnCompletionListener mOnCompletionListener) {
		this.mOnCompletionListener = mOnCompletionListener;
	}

	public void setOnInfoListener(OnInfoListener mOnInfoListener) {
		this.mOnInfoListener = mOnInfoListener;
	}

	public void setOnErrorListener(OnErrorListener mOnErrorListener) {
		this.mOnErrorListener = mOnErrorListener;
	}

	public void setOnSeekCompleteListener(
			OnSeekCompleteListener mOnSeekCompleteListener) {
		this.mOnSeekCompleteListener = mOnSeekCompleteListener;
	}

	public void setOnBufferingUpdateListener(OnBufferingUpdateListener mOnBufferingUpdateListener)
	{
		this.mOnBufferingUpdateListener = mOnBufferingUpdateListener;
	}
	
	public void setPlayerStateListener(IPlayerStateListener listener){
		mPlayerStateListener = listener;
	}
	
	public void setOnDRMErrorMsgListener(OnDRMErrorListener mOnDRMErrorListener){
		this.mOnDRMErrorListener = mOnDRMErrorListener;
	}
	
	public void setOnP2PBitrateChangedListener(ArcPlayer.OnP2PBitrateChangedListener mOnP2PBitrateChangedListener) {
		this.mOnP2PBitrateChangedListener = mOnP2PBitrateChangedListener;
	}
	
	public void seekTo(int i) {
		// TODO Auto-generated method stub
		if(mIsPrepared && mMediaPlayer != null)
			mMediaPlayer.seekTo(i);
	}

	public int getCurrentPosition() {
		// TODO Auto-generated method stub
		if (mIsPrepared && mMediaPlayer != null) {
			return mMediaPlayer.getCurrentPosition();
		}
		return 0;
	}

	public int getDuration() {
		// TODO Auto-generated method stub
		if (mIsPrepared && mMediaPlayer != null) {
			return mMediaPlayer.getDuration();
		}
		return 0;
	}

	public void setDRMConfig(TYPE_DRM drmType,String userID, String tokenID, String drmURL,
			String serviceID,String productID,int type,String deviceID) {
		this.userID = userID;
		this.tokenID = tokenID;
		this.drmURL = drmURL;
		this.productID = productID;
		this.drmType = drmType;
		this.serviceID = serviceID;
		this.type = type;
		this.deviceID = deviceID;
	}

	public void reset() 
	{
		
	}
}
