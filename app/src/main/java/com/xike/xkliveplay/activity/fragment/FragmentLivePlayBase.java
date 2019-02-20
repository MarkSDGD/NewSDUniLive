
package com.xike.xkliveplay.activity.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemProperties;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.arcsoft.media.ArcPlayer.TYPE_DRM;
import com.mernake.framework.tools.MernakeCountDownTools;
import com.mernake.framework.tools.MernakeNetTools;
import com.mernake.framework.tools.MernakeSharedTools;
import com.shandong.shandonglive.zengzhi.ui.ZZDialogTools;
import com.shandong.shandonglive.zengzhi.ui.ZZFileTools;
import com.shandong.shandonglive.zengzhi.ui.ZZOrderDialog;
import com.shandong.shandonglive.zengzhi.ui.ZZOrderHistoryActivity;
import com.umeng.analytics.MobclickAgent;
import com.xike.xkliveplay.R;
import com.xike.xkliveplay.activity.ActivityLaunchBase;
import com.xike.xkliveplay.activity.LaunchType;
import com.xike.xkliveplay.activity.fragment.DialogSave.IFragmentDialog;
import com.xike.xkliveplay.activity.fragment.view.ViewLiveChannelList;
import com.xike.xkliveplay.framework.arcplay.ArcPlayControl;
import com.xike.xkliveplay.framework.arcplay.ArcSurfaceView;
import com.xike.xkliveplay.framework.arcplay.IPlayControl;
import com.xike.xkliveplay.framework.arcplay.IPlayFinished;
import com.xike.xkliveplay.framework.data.DataAnalyzer;
import com.xike.xkliveplay.framework.db.DBManager;
import com.xike.xkliveplay.framework.entity.Category;
import com.xike.xkliveplay.framework.entity.ContentChannel;
import com.xike.xkliveplay.framework.entity.FavoriteManage;
import com.xike.xkliveplay.framework.entity.Schedule;
import com.xike.xkliveplay.framework.entity.gd.GDAuthAidlRes;
import com.xike.xkliveplay.framework.entity.gd.GDOrderPlayAuthCMHWRes;
import com.xike.xkliveplay.framework.error.ErrorBroadcastAction;
import com.xike.xkliveplay.framework.error.ErrorCode;
import com.xike.xkliveplay.framework.error.SendBroadcastTools;
import com.xike.xkliveplay.framework.http.HttpUtil;
import com.xike.xkliveplay.framework.http.IUpdateData;
import com.xike.xkliveplay.framework.httpclient.DataModel;
import com.xike.xkliveplay.framework.httpclient.HttpFavoriteManage;
import com.xike.xkliveplay.framework.httpclient.HttpGetContentList;
import com.xike.xkliveplay.framework.httpclient.HttpGetFavoriteList;
import com.xike.xkliveplay.framework.httpclient.HttpGetScheduleList;
import com.xike.xkliveplay.framework.httpclient.VarParam;
import com.xike.xkliveplay.framework.tools.AbDateUtil;
import com.xike.xkliveplay.framework.tools.AbStrUtil;
import com.xike.xkliveplay.framework.tools.DateUtil;
import com.xike.xkliveplay.framework.tools.LogUtil;
import com.xike.xkliveplay.framework.tools.MernakeLogTools;
import com.xike.xkliveplay.framework.tools.Method;
import com.xike.xkliveplay.framework.tools.NetStatusChange;
import com.xike.xkliveplay.framework.tools.ScreenTools;
import com.xike.xkliveplay.framework.varparams.Var;
import com.xike.xkliveplay.gd.GDHttpTools;
import com.xike.xkliveplay.smarttv.Config;
import com.xike.xkliveplay.xunfei.XunfeiTools;
import com.xike.xkliveplay.xunfei.XunfeiTools.XunfeiInterface;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.xike.xkliveplay.framework.tools.UIUtils.showToast;

/**
 * 
 * @author LiWei <br>
 * CreateTime: 2014年10月29日 下午2:32:40<br>
 * <b>Info:</b><br>
 *<br>
 *   <b>Method:</b> <br>
 */
@SuppressLint("ValidFragment")
public class FragmentLivePlayBase extends FragmentBase implements IUpdateData,IFragmentDialog,IPlayFinished,XunfeiInterface
{

	public static final String Category_Recent = "recent";
	public static final String Category_Save = "save";
	@SuppressLint("SdCardPath")
	private static String filePath =  ""; // "/mnt/sdcard/tt";
	private static final String tag = "FragmentLivePlay";
	public boolean isChangeChangeInNumber = false;
	
	private boolean isFirstin = true;
	
	private RelativeLayout rl_bg = null;

	private Context mContext;
	
	/*Animation*/
	private ImageView faaImage = null;
	private AnimationDrawable mLoadingView = null;

	public List<Category> categorys = null;
	private List<ContentChannel> allContentChannel = null;
	private List<ContentChannel> curShowChannelList = null;

	/** curCategory **/
	private TextView tv_curCategory = null;
	public int curCategoryIndex = 0;
	
	/** index of curFocus in channel list,default is 3 **/
	private int curChannel = 3;
	
	private int allContentChannelIndex = 0;

	/** Recent **/
	private List<ContentChannel> recentList = new ArrayList<ContentChannel>();
	/**Save**/
	private List<ContentChannel> saveList = new ArrayList<ContentChannel>();

	/** all channels in cur category**/
	private ViewLiveChannelList lv_channelList = null;

	/** 'all' category index**/
	private String allChannelCategoryId = "";

	public LinearLayout ll_channelList = null;
	private RelativeLayout ll_channelInfo = null;
	public LinearLayout ll_liveMenu = null;
	public LinearLayout ll_channelNum = null;
	private ArcSurfaceView surfaceView = null;
	private ArcPlayControl arcPlayControl = null;

	private TextView tv_channel_name = null;
	private TextView tv_channel_num = null;
	private TextView tv_chanale_playing = null;
	private TextView tv_channel_will_play = null;

	private String curPlayingChannelId = "";

	/* About vol */
//	public RelativeLayout rl_backplay_vol_main = null;
//	public SeekBar vol_seekBar;
	private int maxVol, curVol;
	public ImageView iv_vol_mute;
	public boolean isMute = false;
	
	public long timeKeydownView = 0;
	private long timekeyDownDigit = 0;
	private long timeKeyDownRecent = 0;
	private long timeNoChannel = 0;
	private long timeKeyDownWay = 0;
	private long timeInvisiable = 0;
	
	private ImageView ivNoChannel = null;
	private String inputNumber = "";
	public View curView = null;
	private DialogSave dialog = null;
	private boolean isSaveFragment = false;
	boolean first = false;
	public IFragmentJump iFragmentJump = null;
	
	private static final int MSG_INVISIABLE_BG = 1001;
    private int lastLaunchType = 1000;//这是个假定的type，一个原则是不与正在使用的launchType重复

    public SoundViewControl soundViewControl = null;
	
	private boolean isCanPlay = true; //TODO 确定是否是可以播放
	
	private boolean isFirstPlay = true;
	private String jumpChannelName = "";
	private String jumpChannelNum = "";
	//倒计时工具
	private MernakeCountDownTools mernakeCountDownTools;

	private int rongzaitime = 0;
	private long remainTimeSec = 0;
	private MernakeCountDownTools mernakeCountDownToolsRongZai;
	private ArcPlayControl.IReturnPlayerErrorCode iCode=null;

	public FragmentLivePlayBase(IFragmentJump _fJump)
	{
		iFragmentJump = _fJump;
	}
	
	@Override
	public void onAttach(Activity activity) 
	{
		super.onAttach(activity);
		DataModel.getInstance();
		isSaveFragment = getArguments().getBoolean("type",false);
		curCategoryIndex = getArguments().getInt("index");
		jumpChannelName = getArguments().getString("channelName");
		jumpChannelNum = getArguments().getString("channelNum");
        lastLaunchType = getArguments().getInt("lastLaunchType", 1000);

        if (jumpChannelNum!=null&&jumpChannelNum.length()>0)
		{
			String channel2 = "";
			String channel3 = "";
			for(ContentChannel channel:Var.allChannels)
			{
				if (channel.getChannelNumber().length() == 1) 
				{
					channel2 = "00"+channel.getChannelNumber();
				}else if (channel.getChannelNumber().length() == 2) 
				{
					channel3 = "0"+channel.getChannelNumber();
				}
				
				if (jumpChannelNum.equals(channel.getChannelNumber()) || jumpChannelNum.equals(channel2)||jumpChannelNum.equals(channel3)) 
				{
					jumpChannelName = channel.getName();
					break;
				}
				
			}
		}
		
		
		
		if (isSaveFragment) 
		{
			findSaveCategoryIndex();
		}
		first = getArguments().getBoolean("first",false);
		LogUtil.i(tag, "onAttach","isSaveFragment:" + isSaveFragment + ",curCategoryIndex:" + curCategoryIndex);
	}
	 /**
	  * findCurCategoryIndex(这里用一句话描述这个方法的作用)
	  *
	  * @Title: findCurCategoryIndex
	  * @Description: 找到收藏类别的id    
	  * @return void    
	  */
	private void findSaveCategoryIndex() 
	{
		
		for (int i = 0; i < Var.allCategorys.size(); i++) 
		{
			if (Var.allCategorys.get(i).getName().equals("收藏")) 
			{
				curCategoryIndex = i;
				System.out.println("收藏的id是:  " + i );
				return;
			}
		}
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		LogUtil.e(tag, "onCreate","STARTING");
		super.onCreate(savedInstanceState);
		MernakeSharedTools.set(getActivity(),"isLiveStarted",true);
		mContext = getActivity().getApplicationContext();
		initParam();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) 
	{
		XunfeiTools.getInstance().setInterface(this);
		curView = inflater.inflate(R.layout.activity_live_play, container,false);
		initVersion(curView);
		initView(curView);
		initTimeShiftSeekBar(curView);

		updateKeyChangeViewTime();
		soundViewControl = new SoundViewControl(curView.findViewById(R.id.rl_vol_main), mContext);
		soundViewControl.showVol(false);
		return curView;
	}

	@Override
	public void onStart() {
		super.onStart();
		getLastPlayRecord();
	}

	public void initVersion(View view)
	{
//		String str = "V %s";
//		String str = "v%s(%s)";
		TextView tv = (TextView)view.findViewById(R.id.tv_version);
		
		PackageManager manager = getActivity().getPackageManager();
		try 
		{
		       PackageInfo info = manager.getPackageInfo(getActivity().getPackageName(), 0);
		       String appVersion = info.versionName; // 版本名，versionCode同理
//		       String appVersionCode = info.versionCode + "";
//		       String path = Environment.getExternalStorageDirectory().getAbsolutePath();
		       tv.setText(appVersion);
//		       tv.setText("CT-debug_1.39");
		        
		} catch (NameNotFoundException e) 
		{
		        e.printStackTrace();
		}
	}
	
	@SuppressLint("SdCardPath")
	private void initParam()
	{
//		regRemoteReceiver();
		DataAnalyzer.creator().init(mContext,Var.userGroupId);
		filePath = "/mnt/sdcard/tt";
		LogUtil.e(tag, "initParam","tt path is " + filePath);
		
		categorys = Var.allCategorys;
		allContentChannel = Var.allChannels;
		allChannelCategoryId = Var.allCategoryId;

//		for (int i=0;i<allContentChannel.size();i++){
//		    if (i%3==0){
//		        allContentChannel.get(i).setDescription("1");
//            }
//        }

		
		setAllContentChannelIndex();
//		curCategoryIndex = allContentChannelIndex;
		recentGetListInlocal();
		getSaveInAllContent();
		Thread thread = new Thread(myRunnable);
		thread.start();
		timeKeyDownRecent = System.currentTimeMillis();

		//请求一下6小时节目单,但还是应该加个前提，先看下数据中心是不是已经有数据了。
		Log.i("MARK","isAll6SchedulesExist=="+DataModel.getInstance().isAll6SchedulesExist());
		if (!DataModel.getInstance().isAll6SchedulesExist())
		{
			GDHttpTools.getInstance().getLiveScheduleListXML(GDHttpTools.getInstance().getTag(),gdIupdata);
		}
	}
	
	private void setAllContentChannelIndex() 
	{	
		for (int i = 0; i < categorys.size(); i++) 
		{
			if (categorys.get(i).getName().equals("全部")) 
			{
				allContentChannelIndex = i;
				break;
			}
		}
	}

	public void initView(View view) 
	{
		ll_channelList = (LinearLayout) view.findViewById(R.id.channel_list);
		ll_channelList.setVisibility(View.GONE);
		ll_channelInfo = (RelativeLayout) view.findViewById(R.id.channel_info);
		ll_channelInfo.setVisibility(View.GONE);
		ll_liveMenu = (LinearLayout) view.findViewById(R.id.live_men);
		ll_liveMenu.setVisibility(View.GONE);
		ll_channelNum = (LinearLayout) view.findViewById(R.id.ll_channelNum);

		lv_channelList = (ViewLiveChannelList) view.findViewById(R.id.lv_channel);
		lv_channelList.setFocusable(false);
		tv_curCategory = (TextView) view.findViewById(R.id.tv_channel_type);

		tv_chanale_playing = (TextView) view.findViewById(R.id.tv_channel_playing_info);
		tv_channel_name = (TextView) view.findViewById(R.id.tv_channel_name);
		tv_channel_num = (TextView) view.findViewById(R.id.tv_channel_num);
		tv_channel_will_play = (TextView) view.findViewById(R.id.tv_channel_isplaying);

		faaImage = (ImageView) view.findViewById(R.id.faa);

		ivNoChannel = (ImageView) view.findViewById(R.id.noChannel);

		surfaceView = (ArcSurfaceView) view.findViewById(R.id.VideoView);
		
		iv_vol_mute = (ImageView)view.findViewById(R.id.iv_vol_mute);
		
		tv_liveplay_timeshift = (TextView)view.findViewById(R.id.tv_liveplay_timeshift);
		
		tv_curCategory.setText(categorys.get(curCategoryIndex).getName());
		
		rl_bg = (RelativeLayout)curView.findViewById(R.id.liveplay_bg);
		if (!first||lastLaunchType == LaunchType.TYPE_RECOMMEND)
		{
			rl_bg.setVisibility(View.GONE);
		}else {
			rl_bg.setVisibility(View.VISIBLE);
			timeInvisiable = System.currentTimeMillis();
		}


		initRongzai();
		
	}

	private void initRongzai() {
		if (Var.isRongZaiEnabled){
			long a = (Long)MernakeSharedTools.get(mContext,"isSwitchedPlayUrlTime",0L);
			rongzaitime = (int) (a / 1000);
			if (rongzaitime !=0){
				mernakeCountDownToolsRongZai = new MernakeCountDownTools();
				mernakeCountDownToolsRongZai.setSumSec(rongzaitime);
				mernakeCountDownToolsRongZai.addListener(new MernakeCountDownTools.ICountDownMessage() {
					@Override
					public void onCountDownFinished() {
						com.mernake.framework.tools.MernakeLogTools.showLog("容灾倒计时结束");
						MernakeSharedTools.set(mContext,"isSwitchedPlayUrl",false);
						MernakeSharedTools.set(mContext,"isSwitchedPlayUrlTime",0L);
						remainTimeSec = 0;
						mernakeCountDownToolsRongZai = null;
					}

					@Override
					public void onCountDownExcuting(int i, int i1) {
						com.mernake.framework.tools.MernakeLogTools.showLog("=======================");
					}

					@Override
					public void onCountDownStart() {

					}

					@Override
					public void onCountDownStop() {

					}
				});
				mernakeCountDownToolsRongZai.startCountDown();
			}
		}

		iCode = new ArcPlayControl.IReturnPlayerErrorCode()
		{
			@Override
			public void onPlayerErrorCode(int code)
			{

				if (!Var.isRongZaiEnabled)
				{
					SendBroadcastTools.sendErrorBroadcast(getActivity(),
							ErrorBroadcastAction.ERROR_PLAYER_ACTION, code+"", Var.userId);
					return;
				}

				/*1.看code是不是100404,根据广电徐明的反馈，当播放器返回100404的时候，说明平台内容灾已经失败，
				 *进入跨平台容灾流程。
				 * 2.进入跨平台容灾流程后，先ping容灾控制服务器，2秒超时，即无法ping通时，直接报错。如果成功ping通表示进入跨平台容灾
				 *3.检查本地是否有另一个平台的频道列表，有直接使用，没有去请求，将请求回来的频道列表缓存数据库
				 * 4.找到当前的对应频道，进行播放，这次如果仍然失败，直接报错
				 * 5.从播放开始倒计时30分钟，30分钟后当存在新的换台行为时，返回原来的平台进行访问，如果404再按照1234的流程执行
				 *
				 * */
				boolean isSwitchedPlayUrl = (Boolean)MernakeSharedTools.get(mContext,"isSwitchedPlayUrl",false);
				if (code == 100404 && isSwitchedPlayUrl)
				{ //说明当前是已经处于跨平台容灾的情况下，结果又报错了，那只好报错了
					com.mernake.framework.tools.MernakeLogTools.showLog("在已经处于跨平台容灾的情况下，又报错了");
					showToast("在已经处于跨平台容灾的情况下，又报错了");
					SendBroadcastTools.sendErrorBroadcast(getActivity(),
							ErrorBroadcastAction.ERROR_PLAYER_ACTION, code+"", Var.userId);
					return;
				}

				if (code == 100404) //如果是404并且是移动版本
				{
					com.mernake.framework.tools.MernakeLogTools.showLog("移动版本下播放器报了404错误");
					showToast("移动版本下播放器报了404错误");
					//					String url = "111.13.100.92";
					String url = "211.137.203.86";
					//					String url = "150.138.11.188";
					com.mernake.framework.tools.MernakeLogTools.showToast(mContext,"要ping的地址是："+url);
					boolean flag = MernakeNetTools.ping(url);
					if (flag)
					{
						com.mernake.framework.tools.MernakeLogTools.showLog("ping通了容灾服务器");
						Toast.makeText(mContext, "ping通了", Toast.LENGTH_SHORT).show();
						MernakeSharedTools.set(mContext,"isSwitchedPlayUrl",true);
						playVideo(curPlayingChannel);
						countDown30Minutes();
						//						if (Var.isOtherPlayUrlExist()){ //如果存在第二套列表，则换用第二套列表
						//							replay();
						//						}else{//请求
						//							if ("1".equals(GDHttpTools.getInstance().getTag())){ //移动华为
						//								GDHttpTools.getInstance().getStaticLiveCategoryList("2",iChannelUpdata);
						//
						//							}else if ("2".equals(GDHttpTools.getInstance().getTag())){//移动中兴
						//
						//							}
						//						}
					}else{//其他的所有情况都报错，这中间包括，1.没有Ping通容灾服务器的情况。2.在另外一个平台也报404的情况
						com.mernake.framework.tools.MernakeLogTools.showLog("没有ping通容灾服务器");
						Toast.makeText(mContext,"没ping通",Toast.LENGTH_SHORT).show();
						SendBroadcastTools.sendErrorBroadcast(getActivity(),
								ErrorBroadcastAction.ERROR_PLAYER_ACTION, code+"", Var.userId);
					}
				}else{
					SendBroadcastTools.sendErrorBroadcast(getActivity(),
							ErrorBroadcastAction.ERROR_PLAYER_ACTION, code+"", Var.userId);
				}

			}
		};

		if (iCode !=null)
		{
			setIReturnPlayerErrorCode(iCode);
		}
	}

	public void setIReturnPlayerErrorCode(ArcPlayControl.IReturnPlayerErrorCode iCode)
	{
		if (arcPlayControl!=null)
		{
			arcPlayControl.setiCode(iCode);
		}
	}

	private void countDown30Minutes()
	{
		if (mernakeCountDownToolsRongZai!=null)
		{
			if (rongzaitime!=0)
			{//说明上一次容灾到现在还没到半小时
				showToast("上一次启用容灾到现在还没有到达规定时间");
				return;
			}
			mernakeCountDownToolsRongZai.stopCountDown();
			mernakeCountDownToolsRongZai=null;
		}
		mernakeCountDownToolsRongZai = new MernakeCountDownTools();
		mernakeCountDownToolsRongZai.addListener(new MernakeCountDownTools.ICountDownMessage() {
			@Override
			public void onCountDownFinished()
			{
				com.mernake.framework.tools.MernakeLogTools.showLog("容灾倒计时结束");
				//				showToast("容灾倒计时结束");
				MernakeSharedTools.set(mContext,"isSwitchedPlayUrl",false);
				MernakeSharedTools.set(mContext,"isSwitchedPlayUrlTime",0L);
				remainTimeSec = 0;
				//				replay();
			}

			@Override
			public void onCountDownExcuting(int i, int i1)
			{
				com.mernake.framework.tools.MernakeLogTools.showLog("容灾倒数：" +i+ " " +i1);
				remainTimeSec = i1;

			}

			@Override
			public void onCountDownStart()
			{
				com.mernake.framework.tools.MernakeLogTools.showLog("开始容灾倒计时");
				//				showToast("开始容灾倒计时");
			}

			@Override
			public void onCountDownStop()
			{
				com.mernake.framework.tools.MernakeLogTools.showLog("容灾倒计时关闭");
				//				showToast("容灾倒计时关闭");
			}
		});
		mernakeCountDownToolsRongZai.setSumSec(30 * 60);
		mernakeCountDownToolsRongZai.startCountDown();
	}



	public int getCurVol() {
		return curVol;
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) 
	{
		LogUtil.i(tag, "onKeyDown","pressed keyCode:" + keyCode);
		
		if (rl_bg == null) 
		{
			return true;
		}
		if (rl_bg != null && rl_bg.getVisibility() == View.VISIBLE ) 
		{
			return true;
		}
		
		if (keyCode == KeyEvent.KEYCODE_HOME) 
		{
			onStop();
			return true;
		}
		
		 if (keyCode == KeyEvent.KEYCODE_MUTE || keyCode == KeyEvent.KEYCODE_VOLUME_MUTE) //静音
		{
			 soundViewControl.showVol(false);
			 soundViewControl.onVoiceAboutKeyDown(keyCode);
		     timeKeydownView = Calendar.getInstance().getTimeInMillis();
			 return true;
		}
		 
		if (ll_channelList != null && ll_channelList.getVisibility() == View.VISIBLE) 
		{	//control left channel list
			{
				onKeyDownOperatorChannelList(keyCode);  //操作频道列表
			}
			return true;
		}
		
		/*Time shift*/
		if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT || keyCode == KeyEvent.KEYCODE_DPAD_RIGHT)  //时移
		{
//			//TODO 根据变量来判断是否可以时移
//			if (!isCanPlay) 
//			{
//				return true;
//			}
			if (ll_liveMenu.getVisibility() == View.VISIBLE) 
			{
				return true;
			}
			
			if (tv_liveplay_timeshift.getVisibility() != View.VISIBLE && keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) 
			{
				return true;
			}
			if (rl_timeshift_process.getVisibility() != View.VISIBLE) 
			{
				rl_timeshift_process.setVisibility(View.VISIBLE);
				ll_channelInfo.setVisibility(View.INVISIBLE);
				soundViewControl.showVol(false);
			}
			updateTimeShift();
			setTimeshiftTextVision(true);
			updateKeyChangeViewTime();
			updateMiddleTime(sb_timeshift_process.getProgress(), keyCode);
		}
		
		//After press number, press OK to change channel
		if (!inputNumber.equals("") && keyCode == KeyEvent.KEYCODE_DPAD_CENTER)  //按数字键再按OK
		{
			if (ll_channelNum.getVisibility() == View.VISIBLE) 
			{
				Message msg = new Message();
				Bundle b = new Bundle();
				b.putString("msg", "1");
				msg.setData(b);
				myHandler.sendMessage(msg);
			}
			timekeyDownDigit = -1;
			return true;
		}
		
		/*Delete save channel*/
		if (keyCode == KeyEvent.KEYCODE_DEL
				&& ll_channelNum.getVisibility() != View.VISIBLE) //删除收藏频道
		{
			if (curPlayingChannel.getZipCode().equals("save")) 
			{
				setAllInvisible();
				createDialoge(false);
			}
			return true;
		}
		
		/*Press number to change channel*/
		if (keyCode >= KeyEvent.KEYCODE_0 && keyCode <= KeyEvent.KEYCODE_9)  //数字键换台
		{
			onkeyDownDigit(keyCode);
			return true;
		}
		
		/*as playing,press center to call left channel list*/
		if (ll_liveMenu.getVisibility() != View.VISIBLE     //按ok呼出频道列表
				&& ll_channelList.getVisibility() != View.VISIBLE
				&& ll_channelNum.getVisibility() != View.VISIBLE
				&& keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
			setAllInvisible();
			ll_channelList.setVisibility(View.VISIBLE);
			jumpToAllChannel();
			updateKeyChangeViewTime();
			
			return true;
		}

		/*Press menu key to call menu*/
		if (keyCode == KeyEvent.KEYCODE_MENU  //按菜单键
				&& ll_channelNum.getVisibility() != View.VISIBLE) 
		{
			setAllInvisible();
			dealkeyCode_Menu();
			updateKeyChangeViewTime();
			return true;
		}
		
		/*Menu action*/
		if (ll_liveMenu.getVisibility() == View.VISIBLE)    //操作右侧菜单列表
		{
			onKeyDownOperateMenu(keyCode);
			return true;
		}

		/*Press up or down to change channel or other action*/
		if (isInLivePlayNoInputNumber())   //上下键换台
		{
			if (keyCode == KeyEvent.KEYCODE_VOLUME_UP
					|| keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) 
			{
				setAllInvisible();
				soundViewControl.showVol(true);
				soundViewControl.onVoiceAboutKeyDown(keyCode);
				updateKeyChangeViewTime();
			} else 
			{
				onKeyDownChangeChannel(keyCode);  
			}
			return true;
		}
		
		return true;
	}
	
	

	@Override
	public boolean onkeyDownReturn() 
	{
		onkeyDownBack();
		return super.onkeyDownReturn();
	}

	/**
	 * Back key
	 */
	private void onkeyDownBack() 
	{
		if (ll_channelList == null) 
		{
			return;
		}
		if (ll_channelList.getVisibility() == View.VISIBLE
				|| ll_liveMenu.getVisibility() == View.VISIBLE
				|| ivNoChannel.getVisibility() == View.VISIBLE || ll_channelInfo.getVisibility() == View.VISIBLE)
		{
			ll_channelList.setVisibility(View.GONE);
			ll_liveMenu.setVisibility(View.GONE);
			ivNoChannel.setVisibility(View.GONE);
			ll_channelInfo.setVisibility(View.INVISIBLE);
		} else if (dialog != null) 
		{
			dialog.dismiss();
			dialog = null;
		} else {
			//exitBy2Click();
			//			exitBy2Click(1);//#18041 fragmentTypw 1:live 2:backplay
			Timer tExit = null;
			if (isExit == false)
			{
				isExit = true; // 准备退出
				initToast(1);//#18041 fragmentTypw 1:live 2:backplay
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
				if (arcPlayControl != null) {
					arcPlayControl.stop();

				}
				//判断是否回到首页
                Log.i("MARK","FragmentRecommendBase.instanse=="+FragmentRecommendBase.instanse);
				if (FragmentRecommendBase.instanse != null) {//来自首页
					iFragmentJump.jumpToFramgment(LaunchType.TYPE_RECOMMEND, 0);
				} else {
                    if (arcPlayControl != null) {  //需要停止播放，並且清空一些数据，特别是要unReg网络状态广播
						arcPlayControl.stop();
					}
					clearData();
					getActivity().finish();
				}
			}
		}
	}//wangfangxu,原方法改为pubic
	public void clearData()
	{
		DataModel.deInstance();
		Var.clear();
		ScheduleCache.clearData();
		XunfeiTools.deInstance();
		NetStatusChange.getInstance().unReg(getActivity());
		NetStatusChange.getInstance().deInit();
	}

	private void onkeyDownDigit(int keyCode) 
	{
		ll_channelNum.setVisibility(View.VISIBLE);

		if (inputNumber.length() >= 3) {
			ll_channelNum.removeAllViews();
			inputNumber = "";
		}

		if (ivNoChannel.getVisibility()==View.VISIBLE) 
		{
			ivNoChannel.setVisibility(View.INVISIBLE);
		}
		int value = keyCode - KeyEvent.KEYCODE_0;
		ImageView iv = new ImageView(mContext);
		iv.setBackgroundResource(getDigitResId(value));
		ll_channelNum.addView(iv);
		updateKeyChangeChannel();
		isChangeChangeInNumber = true;
	}

	private void onKeyDownChangeChannel(int keyCode) 
	{
		if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN || keyCode == KeyEvent.KEYCODE_CHANNEL_DOWN) //按遥控器下键
		{
			recentAddItem();  //判断是否要将频道加入最近播放频道列表
			resetTimeShiftTextView();  //重置时移相关参数，如果当前处于时移状态，上下键换台会转为直播
			setAllInvisible(); //消除掉所有界面上的弹框
			timeKeyDownWay = Calendar.getInstance().getTimeInMillis(); 
			curShowChannelList = allContentChannel;//当前播放频道列表恢复到全部频道
			if (curShowChannelList == null || curShowChannelList.size() == 0) {
				return;
			}
			int index = getSeqInAllChannelByChannelNumber(curShowChannelList,curPlayingChannel.getChannelNumber()); //通过当前播放频道的频道号取出当前频道在全部频道列表中的序号
			int up = index - 1;  //要换到的频道序号为当前频道序号-1，如果小于0，则切换到全部频道到的最后1个
			if (up < 0) {
				up = curShowChannelList.size() - 1;
			}
			uploadObject.setEnterChannelType(1);
			curCategoryIndex = Var.allCategoryIndex;  //设置当前类别为全部频道类别
			curPlayingChannel = curShowChannelList.get(up); //取出将要切换的频道并赋值
			curChannel = up;  //设置当前频道序号为将要切换的频道序号
			updateChannelViewInfo(false);
		}

		if (keyCode == KeyEvent.KEYCODE_DPAD_UP || keyCode == KeyEvent.KEYCODE_CHANNEL_UP) 
		{
			recentAddItem();
			resetTimeShiftTextView();
			setAllInvisible();
			timeKeyDownWay = Calendar.getInstance().getTimeInMillis();
			curShowChannelList = allContentChannel;
			if (curShowChannelList == null || curShowChannelList.size() == 0) 
			{
				return;
			}
			int index = getSeqInAllChannelByChannelNumber(curShowChannelList,
					curPlayingChannel.getChannelNumber());
			int up = index + 1;
			if (up > curShowChannelList.size() - 1) 
			{
				up = 0;
			}
			uploadObject.setEnterChannelType(0);
			curPlayingChannel = curShowChannelList.get(up);
			curChannel = up;
			curCategoryIndex = allContentChannelIndex;
			updateChannelViewInfo(false);
		}
	}



	private void onKeyDownOperateMenu(int keyCode) 
	{
		if ((keyCode == KeyEvent.KEYCODE_DPAD_DOWN || keyCode == KeyEvent.KEYCODE_DPAD_UP)) 
		{//Menu mode,press up or down.
			dealMenuUpAndDowEvent(curView,keyCode);
			updateKeyChangeViewTime();
			return;
		}

		if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) 
		{//Menu mode,press center.
			if (curMenuIndex == 0)   //回看
			{
				setAllInvisible();

				int index = getFirstPlayInfoByChannelName(curPlayingChannel.getName());

				iFragmentJump.jumpToFramgment(LaunchType.TYPE_REPLAY, index);
				isJumpTo = true;


			} else if (curMenuIndex == 1)  //订购
			{
				setAllInvisible();
				//TODO
				getActivity().startActivity(new Intent(getActivity(), ZZOrderHistoryActivity.class));
			}else if(curMenuIndex == 2)  //收藏
			{

				if(!curPlayingChannel.getZipCode().equals("save"))
				{
					createDialoge(true);
					ll_liveMenu.setVisibility(View.GONE);
				}else {
					createDialoge(false);
					ll_liveMenu.setVisibility(View.GONE);
				}

            }
		}
	}
	
	
	
	@Override
	public void onResume() {
		isJumpTo = false;
		super.onResume();
		MobclickAgent.onPageStart("FragmentLivePlay"); 
	}
	
	@Override
	public void onPause() 
	{
		super.onPause();
		MobclickAgent.onPageEnd("FragmentLivePlay");
        stopCountDownOrdismissOrderDialog();
	}

	private void onKeyDownOperatorChannelList(int keyCode) 
	{
		if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) 
		{
			moveToCurCategory("left");
			return;
		}
		if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) 
		{
			moveToCurCategory("right");
			return;
		}

		if (keyCode == KeyEvent.KEYCODE_DPAD_UP) 
		{
			LogUtil.i(tag,"onKeyDownOperatorChannelList", "KEYCODE_DPAD_UP");
			moveToLastChannel();
			updateKeyChangeViewTime();
			return;
		}
		if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) 
		{
			LogUtil.i(tag,"onKeyDownOperatorChannelList", "KEYCODE_DPAD_DOWN");
			moveToNextChannel();
			updateKeyChangeViewTime();
			return;
		}
		if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) 
		{
			if (null == curShowChannelList || curShowChannelList.size() == 0) 
			{
				return;
			}
			ll_channelList.setVisibility(View.GONE);
			ll_channelInfo.setVisibility(View.GONE);
			if (curChannel > curShowChannelList.size() - 1
					|| curShowChannelList.get(curChannel).getContentId()
							.equals(curPlayingChannelId)) 
			{
				return;
			}
			recentAddItem();
			curPlayingChannel = curShowChannelList.get(curChannel);
			updateChannelViewInfo(false);
			resetTimeShiftTextView();
			uploadObject.setChannelId(curPlayingChannel.getContentId());
			uploadObject.setChannelName(curPlayingChannel.getName());
			uploadObject.setEnterChannelType(3);
			uploadObject.setChannelStatus(0);
			DataAnalyzer.creator().uploadLivePlay(uploadObject);
			playVideo(curShowChannelList.get(curChannel));
			return;
		}
	}

	private void showNoChannel() {
		if (ivNoChannel.getVisibility() != View.VISIBLE) 
		{
			ivNoChannel.setVisibility(View.VISIBLE);
			timeNoChannel = Calendar.getInstance().getTimeInMillis();
			inputNumber = "";
		}
	}

	private void playVideo(ContentChannel playChannel) 
	{

		LogUtil.i(tag,"playVideo","********************playVideo is start**********************curPlayingChannelId:");
		if (playChannel == null) 
		{
			LogUtil.e(tag, "playVideo","playVideo is null");
			return;
		}
		
		if (arcPlayControl != null) 
		{
			arcPlayControl.stop();
			arcPlayControl = null;
			if (lastPlayingChannel!=null) 
			{
				if (lastPlayingChannel.getPlayURL().contains("offset")) //说明上一个状态是时移状态 
				{
					if (!playChannel.getPlayURL().contains("offset")) //说明是在某个频道时移播放中，切换成了直播，那么退出时移状态,在这个方法里边退出时移状态，意味着进入直播，反之亦然
					{
						uploadObject.setChannelId(lastPlayingChannel.getContentId());
						uploadObject.setChannelName(lastPlayingChannel.getName());
						uploadObject.setProgramStatus(1);
						DataAnalyzer.creator().uploadTimeShift(uploadObject);
						//如果是向右追平了时移当前时间点，那么Playchannel和lastplayingchannel的标识应该是一样的
						//如果是在时移播放中换台了，那么lastplayingchannel和playchannel的标识应该是不一样的
						if (playChannel.getContentId().equals(lastPlayingChannel.getContentId())) 
						{
							uploadObject.setChannelStatus(0);
							DataAnalyzer.creator().uploadLivePlay(uploadObject);
						}
					}
				}else 
				{
					if (playChannel.getPlayURL().contains("offset"))//说明是在某个频道直播中，切换成了时移 ，那么退出直播状态,进入时移状态
					{
						uploadObject.setChannelId(lastPlayingChannel.getContentId());
						uploadObject.setChannelName(lastPlayingChannel.getName());
						uploadObject.setChannelStatus(1);
						DataAnalyzer.creator().uploadLivePlay(uploadObject);
//						uploadObject.setProgramStatus(0);
//						DataAnalyzer.creator().uploadLivePlay(uploadObject);
					}else if(!lastPlayingChannel.getContentId().equals(playChannel.getContentId()))//说明产生了直播换台行为
					{
						uploadObject.setChannelId(lastPlayingChannel.getContentId());
						uploadObject.setChannelName(lastPlayingChannel.getName());
						uploadObject.setChannelStatus(1);
						DataAnalyzer.creator().uploadLivePlay(uploadObject);
					}
				}
			}
		}
		//TODO 检测orderFlag判断是否是未订购频道
//		if (playChannel.getCountry().equals("0")) 
//		{
//			LogUtil.e(tag, "playVideo", "This channel is not buy!");
//			isCanPlay = false;
//			SendBroadcastTools.sendErrorBroadcast(getActivity(), ErrorBroadcastAction.ERROR_NOT_BUY_ACTION,"","");
//			return;
//		}
		isCanPlay = true;
		
		saveChannelInfo();
		lastPlayingChannel = playChannel;

		/// 试看

		stopCountDownOrdismissOrderDialog();
		Log.i("MARK","playChannel.getCountry()=="+playChannel.getCountry());

		if (Var.isZZEnabled&&playChannel.getCountry().equals("true"))  //当开启增值模块，并且要播放的这个频道是一个付费频道的时候，启动一个5分钟的定时器
 		{
			mernakeCountDownTools = new MernakeCountDownTools();
			mernakeCountDownTools.setSumSec(Var.vipTryPlayLength);//试看一分半钟，这是计时器
			mernakeCountDownTools.addListener(iCountDownMessage);
			mernakeCountDownTools.startCountDown();
			//VIP频道就要重新获取一下AIDL数据,这是防止userToken过期用的
			//int opTag = Var.isYD ? 1:0;//首先要赋值tag，也就是需要进行getAIDL访问
			int opTag = 1; //移动
			GDHttpTools.getInstance().getAIDLData(getActivity().getApplicationContext(), opTag+"", gdIupdata);
		}

        ///
		if (arcPlayControl != null) 
		{
			arcPlayControl.stop();
			arcPlayControl = null;
		}
		arcPlayControl = new ArcPlayControl(mContext, surfaceView, 
				new IPlayControl() {
			@SuppressLint("NewApi")
			@Override
			public void isBufferData(boolean isBuffer) 
			{
				System.out.println("视频缓冲状态："+isBuffer);
				showFrameAnimation(isBuffer);
				if (!isBuffer) 
				{
					if (isFirstin) 
					{
						timeInvisiable = -1;
						rl_bg.setAlpha(0);
						rl_bg.setVisibility(View.GONE);
						postVisible();
						isFirstin = false;
					}
				}
			}
		});
		arcPlayControl.setiPlayFinished(this);
		LogUtil.e(tag, "playVideo",Var.mac);
		arcPlayControl.setDeviceID(Var.mac);
		arcPlayControl.setDrmType(TYPE_DRM.P2P_NODRM);
//		arcPlayControl.setDrmType(TYPE_DRM.P2P_DRM);
		arcPlayControl.setProductid(Var.productId);
		arcPlayControl.setServiceID(playChannel.getContentId());
		LogUtil.e(tag, "playVideo","playing program content ID is: " + playChannel.getContentId());
		arcPlayControl.setTokenID(Var.userToken);
		arcPlayControl.setType(0);
		arcPlayControl.setUri(dealWithUrl(playChannel.getPlayURL()));
		timeshift_playing_url = playChannel.getTimeShiftURL();
		arcPlayControl.setUserID(Var.userId);
//		arcPlayControl.setDmrURL(Var.drm_domain_url);
		arcPlayControl.arcPlayer_play();
		timeKeyDownRecent = Calendar.getInstance().getTimeInMillis();
	}

	public String dealWithUrl(String url)
	{
		if (!Var.isRongZaiEnabled) return url;
		if (!url.contains("http://") && !url.contains("https://")) return url;

			//1--华为，2--中兴
			System.out.println("######替换前播放地址是："+url);
			showToast("替换前播放地址是："+url);
			String platformstr =  GDHttpTools.getInstance().getTag();
			String head = "http://";
			url = url.replace("http://","");
			String target = "";
			if (platformstr.equals("1"))
			{
				target = "ucdn-test.sd.chinamobile.com:8089";
			}else if (platformstr.equals("2"))
			{
				target = "ucdn-zte.sd.chinamobile.com:8089";
			}
			//            ucdn-zte.sd.chinamobile.com:8089/shandong_cabletv.live.zte.com/223.99.253.7:8081

			boolean isSwitchedPlayUrl = (Boolean)MernakeSharedTools.get(getActivity(),"isSwitchedPlayUrl",false);
			com.mernake.framework.tools.MernakeLogTools.showLog("=======> isSwitchedPlayUrl:" + isSwitchedPlayUrl);
			if (isSwitchedPlayUrl){
				com.mernake.framework.tools.MernakeLogTools.showLog("=======> platformstr:" + platformstr);
				String tail = url.substring(url.indexOf("00"));
				com.mernake.framework.tools.MernakeLogTools.showToast(mContext,"tail="+tail);
				if (platformstr.equals("1"))//当前是华为平台，要切成中兴平台
				{
					head = "http://ucdn-zte.sd.chinamobile.com:8089/shandong_cabletv.live.zte.com/223.99.253.7:8081/";
				}else if (platformstr.equals("2"))//当前是中兴平台，要切成华为平台
				{
					head = "http://ucdn-test.sd.chinamobile.com:8089/";
				}
				System.out.println("######最终替换结果是："+head+tail);
				showToast("最终替换结果是："+head+tail);
				return head+tail;
			}

			String temp[] = url.split("/");
			temp[0] = target;
			String result = head;
			for (String str:temp)
			{
				if (result.equals(head)){
					result = result + str;
				}else
					result = result+"/"+str;
			}
			System.out.println("######最终替换结果是："+result);
			showToast("最终替换结果是："+result);
			return result;


	}

	@Override
	public void stopCountDownOrdismissOrderDialog() {
		if (mernakeCountDownTools != null) {
			mernakeCountDownTools.stopCountDown();
			mernakeCountDownTools = null;
		}
		//有的时候，增值的界面的显示是不正确的，这时候要取消掉，防止产生不可预知的错误，比如断网了，比如去设置界面又回来了
		ZZOrderDialog zzOrderDialog = ZZDialogTools.getInstance().getZzOrderDialog();
		if (zzOrderDialog !=null && zzOrderDialog.isShowing()){
			zzOrderDialog.dismiss();
		}
	}
	protected void postVisible() 
	{
		new Thread(new Runnable() 
		{
			@Override
			public void run() 
			{
				try 
				{
					Thread.sleep(1000);
				} catch (InterruptedException e) 
				{
					e.printStackTrace();
				}
				if (ll_liveMenu.getVisibility() != View.VISIBLE) 
				{
					hd.sendEmptyMessage(0);
				}
			}
		}).start();
	}
	

	/**
	 * 
	  * recentAddItem(增加最近频道，如果当前播放频道已经播放了3分钟以上，则加入最近播放列表，最近播放频道列表保持10个，超过10个时，自动删除列表最早一个，加入最新一个)
	  * @return void    返回类型
	  * @modifyHistory  createBy Mernake
	 */
	private void recentAddItem() 
	{
		if(curPlayingChannel == null || recentList == null)
		{
			return;
		}
		if (Calendar.getInstance().getTimeInMillis() - timeKeyDownRecent > 5000 * 60) 
		{
			LogUtil.i("recentAddItem", "recentAddItem","************enter****************");
			int index = recentGetByChanneNum(curPlayingChannel.getChannelNumber());
			LogUtil.i("recentAddItem", "recentAddItem","is in recentList:" + index);
			if (index < 0) 
			{
				if (recentList.size() >= 10) 
				{
					recentList.remove(0);
				}
				recentList.add(curPlayingChannel);
			} else 
			{
				return;
			}
		}

	}

	/**
	 * 
	 * function: find channel index with channel number 
	 * 			and check if it is a recent channel. 
	 * 			if -1 it is not an recent channel, 
	 * 			else yes 
	 * @param
	 * @return
	 */
	private int recentGetByChanneNum(String num) 
	{
		if (recentList == null || recentList.size() == 0) 
		{
			return -1;
		}

		ContentChannel channel = null;
		for (int i = 0; i < recentList.size(); i++) 
		{
			channel = recentList.get(i);
			if (channel.getChannelNumber().equals(num)) 
			{
				return i;
			}
		}

		return -1;
	}

	/**
	 * Get recent channels from file tt
	 */
	private void recentGetListInlocal() 
	{
		try {
			FileReader fr = new FileReader(filePath);
			BufferedReader br = new BufferedReader(fr);
			String line = "";
			ContentChannel channel = null;
			recentList.clear();
			while ((line = br.readLine()) != null) {
				channel = setChannelIsRecentChannel(line);
				if (channel != null) {
					recentList.add(channel);
				}
			}
			br.close();
			fr.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * before application exit,write the recent channels to file tt.
	 */
	private void recentWriteFile() 
	{
		if(recentList == null)
		{
			return;
		}
		try {
			File file = new File(filePath);
			if (file.exists()) {
				file.delete();
			}
			FileWriter fw = new FileWriter(new File(filePath));
			BufferedWriter bw = new BufferedWriter(fw);
//			int index = 0;
//			if (recentList.size() >= 10) {
//				index = recentList.size() - 10;
//			}else {
//				index = recentList.size();
//			}

			for (int i = 0; i < recentList.size();i++) 
			{
				bw.write(recentList.get(i).getChannelNumber() + "\n");
			}
			bw.close();
			fw.close();
		} catch (Exception e) 
		{
		}
	}

	private void moveToCurCategory(String way) {
		LogUtil.i(tag, "moveToCurCategory","moveToCurCategory****************start");
		if (categorys == null || categorys.size() == 0) {
			return;
		}
		updateKeyChangeViewTime();
		if (way.equals("left")) 
		{
			curCategoryIndex -= 1;
			if (curCategoryIndex < 0) 
			{
				curCategoryIndex = categorys.size() - 1;
			}
		} else 
		{
			curCategoryIndex += 1;
			if (curCategoryIndex > categorys.size() - 1) 
			{
				curCategoryIndex = allContentChannelIndex;
			}
		}

		Category category = categorys.get(curCategoryIndex);
		tv_curCategory.setText(category.getName());
		lv_channelList.setChannlesInCategary(null,true);
		LogUtil.e(tag, "moveToCurCategory","allContentChannel.size = " + allContentChannel.size());
		LogUtil.e(tag, "moveToCurCategory","category id = " + category.getId());
		LogUtil.e(tag, "moveToCurCategory","allChannelCategoryId = " + allChannelCategoryId);
		List<ContentChannel> channels = null;
		if (category.getId().equals(Category_Recent)) 
		{
			recentAddItem();
			channels = recentList;
		} else if (category.getId().equals(allChannelCategoryId)) {
			channels = allContentChannel;
		} else if (category.getId().equals(Category_Save)) {
			channels = saveList;
		} else {
			channels = getCurGategoryChannels(category.getId());
		}

		LogUtil.i(tag, "moveToCurCategory","moveToCurCategory-->category name:" + category.getName()+ ",id:" + category.getId());
		if(channels == null)
		{
			LogUtil.i(tag, "moveToCurCategory","moveToCurCategory -->curchannel is null");
		}else{
			if (category.getId().equals(Category_Save)) {
				for (ContentChannel contentChannel : channels) 
				{
					Toast.makeText(mContext, "频道是："+contentChannel.getName(), Toast.LENGTH_SHORT).show();
					LogUtil.i(tag, "moveToCurCategory","channel="+contentChannel.getName());
				}
			}
			
		}

		if (channels == null && !category.getId().equals(Category_Recent)
				&& !category.getId().equals(Category_Save)) {// ����
			postGetContentList(category.getId());
		}else if ((channels == null||channels.size() == 0) && category.getId().equals(Category_Save)) 
		{
			HttpUtil.create().postGetFavoriteList(this);
		} else {
			curShowChannelList = channels;
			reloadChannelList(true);
		}

		LogUtil.i(tag, "moveToCurCategory","moveToCurCategory: cateName:" + category.getName());
//		printContentChannel(channels);
		LogUtil.i(tag, "moveToCurCategory","moveToCurCategory****************end");
	}

	private void moveToNextChannel() 
	{
		lv_channelList.setCurFocusIndex(curChannel);
		
		lv_channelList.keyDown_Down();

		curChannel = lv_channelList.getCurFocusIndex();
	}

	private void moveToLastChannel() 
	{
		lv_channelList.setCurFocusIndex(curChannel);
		
		lv_channelList.keyDown_up();
		
		curChannel = lv_channelList.getCurFocusIndex();
	}

	private void changeChannelByChannelNumber(boolean showNoChannel) 
	{
//		for(int i = 0; i < inputNumber.length();i++)
//		{
//			if(inputNumber.charAt(0) == '0')
//			{
//				inputNumber = inputNumber.substring(1, inputNumber.length());
//				i--;
//			}
//		}
		
		if (inputNumber.equals("0") || inputNumber.equals("00")) //000海看热播特殊处理
		{
			inputNumber = "000";
		}
		
		if (!inputNumber.equals("000")) 
		{
			for(int i = 0; i < inputNumber.length();i++)
			{
				if(inputNumber.charAt(0) == '0')
				{
					inputNumber = inputNumber.substring(1, inputNumber.length());
					i--;
				}
			}
		}
		
		LogUtil.i(tag,"changeChannelByChannelNumber:", "inputNumber-->" + inputNumber);
		int index = getSeqInAllChannelByChannelNumber(allContentChannel,
				inputNumber);
		if (index >= 0) 
		{
			curShowChannelList = allContentChannel;
			curChannel = index;
			curPlayingChannel = curShowChannelList.get(curChannel);
			
			uploadObject.setChannelId(curPlayingChannel.getContentId());
			uploadObject.setChannelName(curPlayingChannel.getName());
			uploadObject.setEnterChannelType(2);
			uploadObject.setChannelStatus(0);
			DataAnalyzer.creator().uploadLivePlay(uploadObject);
			resetTimeShiftTextView();
			setAllInvisible();
			updateChannelViewInfo(false);
//			Toast.makeText(mContext, "要切换的频道是："+curPlayingChannel.getName(), Toast.LENGTH_SHORT).show();
			playVideo(curPlayingChannel);
			isChangeChangeInNumber = true;
			inputNumber = "";
			tv_curCategory.setText("全部");
			curCategoryIndex = allContentChannelIndex;
		} else {
			if (showNoChannel) showNoChannel();
		}
	}

	private int curMenuIndex = 0;

	private void dealkeyCode_Menu() 
	{
		curMenuIndex = 1;

		if (ll_liveMenu.getVisibility() == View.VISIBLE) {
			ll_liveMenu.setVisibility(View.GONE);
		} else if (ll_liveMenu.getVisibility() == View.GONE) 
		{
			ll_liveMenu.setVisibility(View.VISIBLE);
		}
        Log.i("MARK","KEYCODE_MENU  dealMenuUpAndDowEvent");
		dealMenuUpAndDowEvent(curView,KeyEvent.KEYCODE_MENU);
	}

	@SuppressLint("ResourceType")
	private void dealMenuUpAndDowEvent(View view, int keycode)
	{
		FrameLayout fl = (FrameLayout) view.findViewById(R.id.fl_save);
		TextView tvSave1 = (TextView) view.findViewById(R.id.tv_save);
		TextView tvSave2 = (TextView) view.findViewById(R.id.tv_save1);
		TextView tView2 = (TextView) view.findViewById(R.id.tv_back);
		TextView tvOrderHistory = (TextView) view.findViewById(R.id.tv_order_history);
		if (Var.isZZEnabled) 
		{
			tvOrderHistory.setVisibility(View.VISIBLE);
		}

		/*LinearLayout.LayoutParams rl = new LinearLayout.LayoutParams(
				getResources().getDimensionPixelSize(R.dimen.px_214), getResources().getDimensionPixelSize(R.dimen.px_72));*/

		if (curPlayingChannel.getZipCode().equals("save")) {
			tvSave1.setVisibility(View.INVISIBLE);
			tvSave2.setVisibility(View.VISIBLE);
		} else {
			tvSave2.setVisibility(View.INVISIBLE);
			tvSave1.setVisibility(View.VISIBLE);
		}
		
		if (keycode == KeyEvent.KEYCODE_MENU) 
		{
			if (curMenuIndex == 1) { //第0个单位获得焦点
				curMenuIndex = 0;
				fl.setBackgroundResource(getResources().getColor(
						R.color.transparent_background));
				tView2.setBackgroundResource(R.drawable.channel_focus);
				tvOrderHistory.setBackgroundResource(getResources().getColor(
						R.color.transparent_background));

				/*rl.setMargins(0, Method.getScaleY(340), 0, 0);
				rl.gravity = Gravity.RIGHT;
				fl.setLayoutParams(rl);*/
				return;
			}
		}
		
		if (Var.isZZEnabled) 
		{
			if (keycode == KeyEvent.KEYCODE_DPAD_UP) 
			{
				if (curMenuIndex == 0) 
				{
					curMenuIndex = 2;
					fl.setBackgroundResource(R.drawable.channel_focus);
					tView2.setBackgroundResource(getResources().getColor(
							R.color.transparent_background));


					/*rl.setMargins(0, Method.getScaleY(136), 0, 0);
					rl.gravity = Gravity.RIGHT;
					fl.setLayoutParams(rl);*/
					return;
				}else if (curMenuIndex == 1) 
				{
					curMenuIndex = 0;
					tvOrderHistory.setBackgroundResource(getResources().getColor(
							R.color.transparent_background));
					tView2.setBackgroundResource(R.drawable.channel_focus);

					/*rl.setMargins(0, Method.getScaleY(340), 0, 0);
					rl.gravity = Gravity.RIGHT;
					fl.setLayoutParams(rl);*/
					return;
				}else if (curMenuIndex == 2) 
				{
					curMenuIndex = 1;

					fl.setBackgroundResource(getResources().getColor(
							R.color.transparent_background));
					tvOrderHistory.setBackgroundResource(R.drawable.channel_focus);


					/*rl.setMargins(0, Method.getScaleY(238), 0, 0);
					rl.gravity = Gravity.RIGHT;
					fl.setLayoutParams(rl);*/
					return;
				}
			}else if (keycode == KeyEvent.KEYCODE_DPAD_DOWN) 
			{
				if (curMenuIndex == 0) 
				{
					curMenuIndex = 1;

					tView2.setBackgroundResource(getResources().getColor(
							R.color.transparent_background));
					tvOrderHistory.setBackgroundResource(R.drawable.channel_focus);


					/*rl.setMargins(0, Method.getScaleY(238), 0, 0);
					rl.gravity = Gravity.RIGHT;
					fl.setLayoutParams(rl);*/
					return;
				}else if (curMenuIndex == 1) 
				{
					curMenuIndex = 2;
					fl.setBackgroundResource(R.drawable.channel_focus);
					tvOrderHistory.setBackgroundResource(getResources().getColor(
							R.color.transparent_background));

				/*	rl.setMargins(0, Method.getScaleY(136), 0, 0);
					rl.gravity = Gravity.RIGHT;
					fl.setLayoutParams(rl);*/
					return;
				}else if (curMenuIndex == 2) 
				{
					curMenuIndex = 0;
					fl.setBackgroundResource(getResources().getColor(
							R.color.transparent_background));
					tView2.setBackgroundResource(R.drawable.channel_focus);

				/*	rl.setMargins(0, Method.getScaleY(340), 0, 0);
					rl.gravity = Gravity.RIGHT;
					fl.setLayoutParams(rl);*/
					return;
				}
			}
			return;
		}


		Log.i("MARK","curMenuIndex=="+curMenuIndex);

		if (curMenuIndex == 0) {
			curMenuIndex = 2;

			tView2.setBackgroundResource(getResources().getColor(
					R.color.transparent_background));
			fl.setBackgroundResource(R.drawable.channel_focus);

			/*rl.setMargins(0, Method.getScaleY(238), 0, 0);
			rl.gravity = Gravity.RIGHT;
			fl.setLayoutParams(rl);*/
			return;
		}

		if (curMenuIndex == 2) {
			curMenuIndex = 0;

			fl.setBackgroundResource(getResources().getColor(
					R.color.transparent_background));
			tView2.setBackgroundResource(R.drawable.channel_focus);

		/*	rl.setMargins(0, Method.getScaleY(340), 0, 0);
			rl.gravity = Gravity.RIGHT;
			fl.setLayoutParams(rl);*/
			return;
		}
	}

	@Override
	public void updateData(String method, String uniId, Object object,
			boolean isSuccess) {

		LogUtil.i(tag, "updateData","method:" + method + ",uniId:" + uniId + "isSuccess:" + isSuccess);
		if (HttpGetContentList.Method.equals(method)) 
		{
			if(!isSuccess || object == null)
			{
//				SendBroadcastTools.sendErrorBroadcast(getActivity(), ErrorBroadcastAction.ERROR_GET_CHANNEL_LIST_ACTION,"","");
				return;
			}
			dealResWithContentChannels(object, uniId);
			return;
		}

		if (HttpGetScheduleList.Method.equals(method)) 
		{
			if(!isSuccess)
			{
				ErrorCode.makeErrorToast(ErrorCode.ERROR_GET_SCHEDULE_LIST_EXCEPTION, getActivity());
				return;
			}
			if(object == null)
			{
				return;
			}
			dealResWithSchedules(object, uniId);

			return;
		}

		if (HttpGetFavoriteList.Method.equals(method)) 
		{
			if(!isSuccess)
			{
//				SendBroadcastTools.sendErrorBroadcast(getActivity(), ErrorBroadcastAction.ERROR_GET_SAVE_LIST_ACTION,"","");
				return;
			}
			if(object == null)
			{
				return;
			}
			dealResFavorites(object);
			return;
		}
		
		if (HttpFavoriteManage.Method.equals(method)) 
		{
			if (isSuccess) 
			{
				Toast.makeText(mContext, "收藏成功", Toast.LENGTH_LONG).show();
			}else{
				Toast.makeText(mContext, "收藏失败", Toast.LENGTH_LONG).show();
			}
		}
		
	}

	@SuppressWarnings("unchecked")
	private void dealResWithContentChannels(Object object, String uniId) 
	{
		if (uniId.equals(categorys.get(curCategoryIndex).getId())) 
		{
			curShowChannelList = (List<ContentChannel>) object;
			resetAllContentType(uniId);
			reloadChannelList(true);
		}
		
	}
	
	private void resetAllContentType(String uniId)
	{
		
		DBManager dbManager = new DBManager(getActivity());
		ContentChannel contentChannel = new ContentChannel();
		
		
		
		for(int i = 0; i < curShowChannelList.size();i++)
		{
			for(int j = 0; j < allContentChannel.size();j++)
			{
				if(curShowChannelList.get(i).getContentId().equals(allContentChannel.get(j).getContentId()))
				{	//这里还可能一个频道属于不同的类别
					String cid = allContentChannel.get(j).getLanguage(); //全部频道中取出该频道的类别id
					String[] cids = null;
					if (cid!=null&&cid.contains(","))
					{
						 cids = cid.split(","); //获得类别id的数组
					}
					if (cid==null||cid.equals("") || (cids != null && cids.length == 0))
					{
						allContentChannel.get(j).setLanguage(uniId);
						curShowChannelList.get(i).setZipCode(allContentChannel.get(j).getZipCode());
					}else {
						if (!cid.equals("") && !cid.equals(uniId))  //之前属于另一个类别，现在又要增加一个类别
						{
							cid = cid + "," + uniId;
							allContentChannel.get(j).setLanguage(cid);
							curShowChannelList.get(i).setZipCode(allContentChannel.get(j).getZipCode());
							contentChannel.setChannelNumber(curShowChannelList.get(i).getChannelNumber()); //结果同步到数据库里边去
							dbManager.update("channel", curShowChannelList.get(i), contentChannel);
							continue;
						}
						
						for (String string : cids)  //之前属于多个类别，现在再增加一个类别
						{
							if (string.equals(uniId))  //不要重复添加类别
							{
								break;
							}
						}
						cid = cid + "," + uniId;
						allContentChannel.get(j).setLanguage(cid);
						curShowChannelList.get(i).setZipCode(allContentChannel.get(j).getZipCode());
						contentChannel.setChannelNumber(curShowChannelList.get(i).getChannelNumber()); //结果同步到数据库里边去
						dbManager.update("channel", curShowChannelList.get(i), contentChannel);
					}
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void dealResWithSchedules(Object object, String uniId) 
	{
		if (object == null || ((List<Schedule>) object).size() < 2
				|| !uniId.equals(tv_channel_name.getTag().toString())) 
		{
			return;
		}
		
		List<Schedule> list = (List<Schedule>) object;
		String date = list.get(0).getStartDate();
		String channelId = list.get(0).getChannelId();
		ScheduleCache.putCurSchedule(date, channelId, list);
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		Calendar calendar = Calendar.getInstance();
		Date nowdate = calendar.getTime();
		String startDateTime = formatter.format(nowdate);
		System.out.println("############:Date:"+startDateTime);
		Schedule[] res = ScheduleCache.getSchedule(curPlayingChannel.getContentId(), startDateTime);
		try{
			Log.i("MARK"," dealResWithSchedules  res[0]=="+res[0].getProgramName());
			Log.i("MARK"," dealResWithSchedules  res[1]=="+res[1].getProgramName());
		}catch (Exception e){
			e.printStackTrace();
		}finally {

		}
		if (res != null)
		{
			setScheduleInfo(res);
		}
	}

	@SuppressWarnings("unchecked")
	private void dealResFavorites(Object object) {
		if (object == null) 
		{
			return;
		}

		saveList = (List<ContentChannel>) object;
		alterFavoritesStateInAllConntent();
		curShowChannelList = saveList;
		reloadChannelList(true);
	}

	private void alterFavoritesStateInAllConntent()
	{
		for(int i = 0; i < saveList.size();i++)
		{
			for(int j = 0; j < allContentChannel.size();j++)
			{
				if(saveList.get(i).getChannelNumber().equals(allContentChannel.get(j).getChannelNumber()))
				{
					allContentChannel.get(j).setZipCode(Category_Save);
				}
			}
		}
	}
	
	private void postGetContentList(String categoryId) 
	{
		GDHttpTools.getInstance().getStaticLiveContentList(GDHttpTools.getInstance().getTag(),categoryId,Var.userGroupId,gdIupdata);
	}

//	private void postGetScheduleList(String channelId) 
//	{
//		HttpGetScheduleList scheduleList = new HttpGetScheduleList(this);
//		GetScheduleList req = new GetScheduleList();
//		req.setCount("2");
//		req.setOffset("0");
//		req.setChannelIds(channelId);
//		req.setStartDateTime(DateUtil.getDateStr4(Calendar.getInstance()));
//		Calendar calendar = Calendar.getInstance();
//		calendar.add(Calendar.HOUR_OF_DAY, 12);
//		req.setEndDateTime(DateUtil.getDateStr4(calendar));
//		req.setUserId(Var.userId);
//		req.setUserToken(Var.userToken);
//		scheduleList.setReq(req);
//
//		scheduleList.post();
//
//	}
	


	private void postGetFavoriteManage(String opt) 
	{
		if (curPlayingChannel.getZipCode().equals("save") && opt.equals("ADD")) 
		{
			return;
		}

		if (!curPlayingChannel.getZipCode().equals("save")
				&& opt.equals("DELETE")) 
		{
			return;
		}

		HttpFavoriteManage manage = new HttpFavoriteManage(this);

		FavoriteManage req = new FavoriteManage();
		req.setAction(opt);
		req.setContentId(curPlayingChannel.getContentId());
		req.setUserId(Var.userId);
		req.setUserToken(Var.userToken);
		req.setContentType("2");
		req.setSeriesContentIndex("");
		req.setSeriesContentID("");
		req.setCategoryID(curPlayingChannel.getLanguage());
		req.setContentProvider("");

		manage.setReq(req);
		manage.post();
		
		DBManager dbManager = new DBManager(getActivity());
		ContentChannel contentChannel = new ContentChannel();
		contentChannel.setChannelNumber(curPlayingChannel.getChannelNumber());
		
		if ("ADD".equals(opt)) {
			curPlayingChannel.setZipCode(Category_Save);
			if(curShowChannelList != null && curChannel < curShowChannelList.size()
					&& curShowChannelList.get(curChannel).getChannelNumber() == curPlayingChannel.getChannelNumber())
			{
				curShowChannelList.get(curChannel).setZipCode(Category_Save);
			}
			saveList.add(curPlayingChannel);
			alterSaveStateInAllContent(curPlayingChannel.getChannelNumber(), true);
		} else {
			curPlayingChannel.setZipCode("");
//			if(curShowChannelList != null && curChannel < curShowChannelList.size())
//			{
//				curShowChannelList.get(curChannel).setZipCode("");
//			}
			alterSaveStateInAllContent(curPlayingChannel.getChannelNumber(), false);
			getSaveInAllContent();
		}
		dbManager.update("channel", curPlayingChannel, contentChannel);
	}
	
	
	private void getSaveInAllContent()
	{
		saveList.clear();
		for(int i = 0; i < allContentChannel.size();i++)
		{
			if(allContentChannel.get(i).getZipCode().equals(Category_Save))
			{
				saveList.add(allContentChannel.get(i));
			}
		}
	}
	
	private void alterSaveStateInAllContent(String channelNum,boolean isSave)
	{
		for(int i = 0; i < allContentChannel.size();i++)
		{
			if(allContentChannel.get(i).getChannelNumber().equals(channelNum))
			{
				if(isSave)
				{
					allContentChannel.get(i).setZipCode(Category_Save);
				}else {
					allContentChannel.get(i).setZipCode("");
				}
				return;
			}
		}
	}

	private void reloadChannelList(boolean isJumpToFocus) 
	{
		if(isJumpToFocus)
		{
			lv_channelList.setChannlesInCategary(curShowChannelList,true);
		}else {
			lv_channelList.setChannlesInCategary(curShowChannelList,false);
		}
		
		curChannel = lv_channelList.getCurFocusIndex();
	}

	@Override
	public void onStop() 
	{
//		SystemProperties.set("libplayer.livets.softdemux", "1");
		if (VarParam.url.contains("tsnm")) 
		{
			SystemProperties.set("media.amplayer.displast_frame", "0");
		}
		super.onStop();
		LogUtil.i("onStop", "onStop","*************onStop*************");
		
		if (mReceiver != null) 
		{
			getActivity().unregisterReceiver(mReceiver);
			mReceiver = null;
		}
		
		recentAddItem();

		recentWriteFile();

		showFrameAnimation(false);

		if (arcPlayControl != null) 
		{
			arcPlayControl.stop();
			arcPlayControl = null;
		}


		 /*if (!isJumpTo)
		{
			getActivity().finish();
		}*/
	}

	private void saveChannelInfo() 
	{
		if(curPlayingChannel == null)
		{
			return;
		}
		SharedPreferences sp = mContext.getSharedPreferences(tag, 0);
		Editor editor = sp.edit();
		editor.putString("name", curPlayingChannel.getName());
		editor.commit();
		LogUtil.i(tag,"saveChannelInfo","name is " + curPlayingChannel.getName());
	}

	public void getLastPlayRecord() 
	{
		String name = "";
		if(!isSaveFragment)
		{
			System.out.println("######:jumpChannelName = " + Config.smartTvChannelName);
			if (Config.smartTvChannelName != null && !Config.smartTvChannelName.equals("")) 
			{
				name = Config.smartTvChannelName;
			}else {
				SharedPreferences sp = mContext.getSharedPreferences(tag, 0);
				int isSelectedUsed = sp.getInt("isselectedused", 0);
				name = sp.getString("name", "empty");
				if (isSelectedUsed==0){
					System.out.println("####:上一次的频道记录没有取到，获取带有seleted标志的频道");
					if (!Var.isJumpNumExist)
					{
						name = getSelectedChannel(name);
						MernakeLogTools.showLog("","获取到的带有selected标志的频道是："+name);
					}
				}

				System.out.println("######:saved channel is:"+name);
			}
			if (jumpChannelName !=null && jumpChannelName.length()>0) 
			{
				name = jumpChannelName;
				MernakeLogTools.showLog("","通过jumpChannelName获取到的频道是："+name);
			}
			curChannel = getFirstPlayInfoByChannelName(name);
			System.out.println("#######:curchannel is:"+curChannel);
			curShowChannelList = allContentChannel;
			curPlayingChannel = allContentChannel.get(curChannel);
			lv_channelList.setCurFocusIndex(curChannel);
			reloadChannelList(false);
		}else {
			curShowChannelList = saveList;
			reloadChannelList(true);
			if(saveList.size() == 0)
			{
				int index = getFirstPlayInfoByChannelName("CCTV-1");
				curPlayingChannel = allContentChannel.get(index);
			}else {
				if (curChannel>=curShowChannelList.size()) return;
				curPlayingChannel = curShowChannelList.get(curChannel);
			}
		}
		
		System.out.println("###直播APK进入### 上报频道是： " + curPlayingChannel.toString());
		uploadObject.setChannelId(curPlayingChannel.getContentId());
		uploadObject.setChannelName(curPlayingChannel.getName());
		uploadObject.setProgramStatus(0);
		DataAnalyzer.creator().uploadLivePlay(uploadObject);
		
		playVideo(curPlayingChannel);
		LogUtil.i(tag, "getLastPlayRecord","curPlayingChannelName:" + name);
	}


	/**
	 * getSelectedChannel(这里用一句话描述这个方法的作用)

	 * @return
	 * @return String    返回类型
	 * @modifyHistory  createBy Mernake
	 */
	private String getSelectedChannel(String oriName)
	{
		if (Var.allChannels !=null && Var.allChannels.size()>0)
		{
			for (ContentChannel contentChannel : Var.allChannels)
			{
				if (contentChannel.getCallSign().equals("1")) //说明此频道被置为默认播放，生效一次
				{
					//存个标志位
					SharedPreferences sp2 = getActivity().getSharedPreferences(tag, 0);
					Editor editor = sp2.edit();
					editor.putInt("isselectedused", 1);
					editor.commit();
					//存个频道
					SharedPreferences sp = getActivity().getSharedPreferences(tag, 0);
					Editor editor2 = sp.edit();
					editor2.putString("name", contentChannel.getName());
					editor2.commit();
					return contentChannel.getName();
				}
			}

			System.out.println("####:频道列表的第一个频道是："+Var.allChannels.get(0).getName());
			if (oriName.equals(""))
			{
				return Var.allChannels.get(0).getName();
			}else return oriName;
		}
		return "";
	}

	/**
	  * getSelectedChannel(这里用一句话描述这个方法的作用)
	
	  * @return  
	  * @return String    返回类型
	  * @modifyHistory  createBy Mernake
	  */
	private String getSelectedChannel() 
	{
		if (allContentChannel !=null && allContentChannel.size()>0) 
		{
			for (ContentChannel contentChannel : allContentChannel) 
			{
				if (contentChannel.getCallSign().equals("1")) //说明此频道被置为默认播放，生效一次
				{
					return contentChannel.getName();
				}
			}
			
			System.out.println("####:频道列表的第一个频道是："+allContentChannel.get(0).getName());
			return allContentChannel.get(0).getName();
		}
		return "";
	}

	public int getFirstPlayInfoByChannelName(String name)
	{
		for(int i = 0; i < allContentChannel.size();i++)
		{
			if(allContentChannel.get(i).getName().equals(name))
			{
				return i;
			}
		}
		
		return 0;
	}

	private ContentChannel setChannelIsRecentChannel(String channelNum) {
		if (channelNum == "" || allContentChannel == null) {
			return null;
		}

		for (int i = 0; i < allContentChannel.size(); i++) {
			if (allContentChannel.get(i).getChannelNumber().equals(channelNum)) {
				allContentChannel.get(i).setCity(Category_Recent);
				return allContentChannel.get(i);
			}
		}

		return null;
	}

	private boolean isSaveDialog = false;
	private void createDialoge(final boolean isSave) 
	{
		isSaveDialog = isSave;
		String info = "";
		if (!isSave) 
		{
			info = "确定删除收藏频道";
		} else {
			info = "确定收藏频道";
		}
		
		if(dialog != null)
		{
			dialog.dismiss();
			dialog = null;
		}
		
		dialog = new DialogSave(getActivity(), R.style.MyDialog, info, this);
		dialog.show();
		
	}

	@Override
	public void onKeyDownConfirm() {
		if (!isSaveDialog) {
			postGetFavoriteManage("DELETE");
		} else {
			postGetFavoriteManage("ADD");
		}
	}
	
	
	public void updateKeyChangeViewTime() {
		timeKeydownView = Calendar.getInstance().getTimeInMillis();
	}

	private void updateKeyChangeChannel() {
		timekeyDownDigit = Calendar.getInstance().getTimeInMillis();
	}
	
	private void updateTimeShift() {
		timekeyDownShift = Calendar.getInstance().getTimeInMillis();
	}
	
	

	private Runnable myRunnable = new Runnable() 
	{
		@Override
		public void run() {
			while (true) {
				LogUtil.i(tag,"myRunnable", "is always runing !!!!");

				if (timekeyDownShift > 0 && Calendar.getInstance().getTimeInMillis() - timekeyDownShift > 500) 
				{//TimeShift
					LogUtil.i(tag,"myRunnable", "TimeShift");
					Message msg = new Message();
					Bundle b = new Bundle();
					b.putString("msg", "4");
					timekeyDownShift = -1;
					msg.setData(b);
					myHandler.sendMessage(msg);
				}
				
				if (timeKeydownView > 0
						&& Calendar.getInstance().getTimeInMillis() - timeKeydownView > 4000) {
					LogUtil.i(tag,"myRunnable", "cancel all view in screen");
					Message msg = new Message(); // invisiable all views.
					Bundle b = new Bundle();
					b.putString("msg", "0");
					timeKeydownView = -1;
					msg.setData(b);
					myHandler.sendMessage(msg);
				}

				if (timekeyDownDigit > 0
						&& Calendar.getInstance().getTimeInMillis() - timekeyDownDigit > 3000) {
					LogUtil.i(tag,"myRunnable", "change channel by number key");
					if (ll_channelNum.getVisibility() == View.VISIBLE) 
					{
						Message msg = new Message();
						Bundle b = new Bundle();
						b.putString("msg", "1");
						msg.setData(b);
						myHandler.sendMessage(msg);
					}
					timekeyDownDigit = -1;
				}

				if (timeKeyDownWay > 0
						&& Calendar.getInstance().getTimeInMillis() - timeKeyDownWay > 500) {
					LogUtil.i(tag,"myRunnable", "Up or down to change channel");
					Message msg = new Message();
					Bundle b = new Bundle();
					b.putString("msg", "2");
					msg.setData(b);
					myHandler.sendMessage(msg);
					timeKeyDownWay = -1;
				}

				if (timeNoChannel > 0
						&& Calendar.getInstance().getTimeInMillis() - timeNoChannel > 3000) {
					LogUtil.i(tag,"myRunnable", "notification there is no channel");
					Message msg = new Message();
					Bundle b = new Bundle();
					b.putString("msg", "3");
					msg.setData(b);
					myHandler.sendMessage(msg);
					timeNoChannel = -1;
				}
				
				if (timeInvisiable > 0 && System.currentTimeMillis() - timeInvisiable > 2000) 
				{
					LogUtil.i(tag,"myRunnable", "MSG_INVISIABLE_BG");
					hd.sendEmptyMessage(MSG_INVISIABLE_BG);
				}
				
				
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) 
				{
					e.printStackTrace();
				}
			}
		}
	};

	@SuppressLint("HandlerLeak")
	private Handler myHandler = new Handler() {
		public void handleMessage(Message msg) 
		{
			
			Bundle b = msg.getData();
			String info = b.getString("msg");
			if (info == null) 
			{
				return;
			}
			LogUtil.i(tag, "myHandler","myHandler -->msg:" + info);
			
			if (info.equals("0")) {
				if (isLLisNull()) {
					return;
				}
				ll_channelInfo.setVisibility(View.GONE);
				ll_channelList.setVisibility(View.GONE);
				ll_liveMenu.setVisibility(View.GONE);
				soundViewControl.showVol(false);
				rl_timeshift_process.setVisibility(View.GONE);
			} else if (info.equals("1")) {
				ll_channelNum.removeAllViews();
				ll_channelNum.setVisibility(View.GONE);
				changeChannelByChannelNumber(true);
			} else if (info.equals("2")) { //上下键换台
				if (curChannel >= curShowChannelList.size()) 
				{
					return;
				}
				uploadObject.setChannelId(curShowChannelList.get(curChannel).getContentId());
				uploadObject.setChannelName(curShowChannelList.get(curChannel).getName());
				uploadObject.setChannelStatus(0);
				DataAnalyzer.creator().uploadLivePlay(uploadObject);
				playVideo(curShowChannelList.get(curChannel));
				isChangeChangeInNumber = true;
			} else if (info.equals("3")) 
			{
				if (ivNoChannel.getVisibility()==View.VISIBLE) 
				{
					ivNoChannel.setVisibility(View.GONE);
					inputNumber = "";
				}
			}else if (info.equals("4")) {
				jumpToNewTime();
			}
		};
	};
	
	private boolean isLLisNull()
	{
		if (ll_channelInfo == null
				|| ll_channelList == null
				|| ll_channelNum == null
				|| ll_liveMenu == null) 
		{
			return true;
		}
		return false;
	}

	private boolean isInLivePlayNoInputNumber() {
		if (ll_channelList.getVisibility() != View.VISIBLE
				&& ll_liveMenu.getVisibility() != View.VISIBLE
				&& ll_channelNum.getVisibility() != View.VISIBLE) {
			return true;
		}

		return false;

	}


	public void setAllInvisible() {
		ll_channelInfo.setVisibility(View.GONE);
		ll_channelList.setVisibility(View.GONE);
		ll_liveMenu.setVisibility(View.GONE);
		rl_timeshift_process.setVisibility(View.INVISIBLE);
		if (ivNoChannel.getVisibility() == View.VISIBLE) {
			ivNoChannel.setVisibility(View.GONE);
		}
		ivNoChannel.setVisibility(View.GONE);
		soundViewControl.showVol(false);
		timekeyDownDigit = -1;
		timeKeydownView = -1;
		timeKeyDownWay = -1;
		timeNoChannel = -1;
	}

	private int getSeqInAllChannelByChannelNumber(List<ContentChannel> lists,
			String channeNum) {
		if (lists == null || lists.size() == 0) 
		{
			return -1;
		}
		
		String[] channelNums = getChannelNumbers(channeNum);
		
		ContentChannel channel = null;
		for (int i = 0; i < lists.size(); i++) 
		{
			channel = lists.get(i);
			System.out.println("###@@@###:listchannelnum:"+channel.getChannelNumber());
			//以下一段内容是为了兼容本来频道号就是001这种的
			
			String[] epgChannelNums = getChannelNumbers(channel.getChannelNumber());
			
			//开始准备把epg的频道号也做成三种的
			
			for (int j = 0; j < epgChannelNums.length; j++) 
			{
				for (int j2 = 0; j2 < channelNums.length; j2++) 
				{
					if (epgChannelNums[j].equals(channelNums[j2]) && !epgChannelNums[j].equals("") && !channelNums[j2].equals("")) 
					{
						return  i;
					}
				}
			}
		}
		return -1;
	}

	
	private String[] getChannelNumbers(String channelNum)
	{
		String[] result = {"","",""};
		String str_1 = "";
		String str_2 = "";
		String str_3 = "";
		
		//STEP.1 当频道号是1位数字的时候
		if (channelNum.length() == 1) 
		{
			str_1 = channelNum;
			str_2 = "0"+str_1;		//1.1 添加0，补齐它的2位形式
			str_3 = "00" + str_1;	//1.2 添加00，补齐它的3为形式
		}else if (channelNum.length() == 2) //STEP.2 当频道号是2位数字的时候,要检查它是否是0开头的
		{
			str_2 = channelNum;
			if (str_2.startsWith("0")) // 2.1如果是0开头的
			{
				str_3 = "0" + str_2;//2.1.1 添加0补齐它的3位形式
				str_1 = str_2.substring(1);//2.1.2 去掉0补齐它的1位形式
			}else{
				str_3 = "0" + str_2;//2.2如果不是0开头，则添加0，只补齐它的3位形式
			}
		}else if (channelNum.length() == 3) //STEP.3当频道号是3位数字的时候，要检查它是否是0开头，或者00开头的。
		{
			str_3 = channelNum;
			if (channelNum.startsWith("00")) //3.2如果是00开头，去掉0补齐它的2位形式，去掉00补齐它的1位形式。
			{
				str_1 = channelNum.substring(2);
				str_2 = channelNum.substring(1);
			}else if (channelNum.startsWith("0")) //3.1如果是0开头，去掉0补齐它的2位形式
			{
				str_2 = channelNum.substring(1);
			}
		}
		
		
		result[0] = str_1;
		result[1] = str_2;
		result[2] = str_3;
		
		return result;
	}
	
	
	private int jumpBeforeChannel = 0;
	public void jumpToAllChannel() 
	{
		LogUtil.i(tag, "jumpToAllChannel","jumpToAllChannel:cateName:" + categorys.get(curCategoryIndex).getName());
		printContentChannel(curShowChannelList);

		lv_channelList.setCurFocusIndex(curChannel);
		jumpBeforeChannel = curChannel;
		
		if(isChangeChangeInNumber)
		{
//			lv_channelList.setChannlesInCategary(allContentChannel,false);
//			tv_curCategory.setText("全部");
			curCategoryIndex = allContentChannelIndex;
			curCategoryIndex --;
			moveToCurCategory("right");
			lv_channelList.setCurFocusIndex(jumpBeforeChannel);
			lv_channelList.refreshList();
			curChannel = jumpBeforeChannel;
			isChangeChangeInNumber = false;
			return;
		}
		
		if(categorys.get(curCategoryIndex).getName().equals("收藏"))
		{
			tv_curCategory.setText("收藏");
			curShowChannelList = saveList;
			printContentChannel(saveList);
			lv_channelList.setChannlesInCategary(curShowChannelList,true);
			curChannel = lv_channelList.getCurFocusIndex();
			LogUtil.i(tag, "jumpToAllChannel","curShowChannelList:size:" + curShowChannelList.size() + ",curchannel:" + curChannel);
		}else {
			LogUtil.e(tag,"jumpToAllChannel", curShowChannelList+"");
			lv_channelList.setChannlesInCategary(curShowChannelList,false);
		}
	}

	private int getDigitResId(int digit) {
		if (digit == 0) {
			inputNumber += "0";
			return R.drawable.d_0;
		}
		if (digit == 1) {
			inputNumber += "1";
			return R.drawable.d_1;
		}
		if (digit == 2) {
			inputNumber += "2";
			return R.drawable.d_2;
		}
		if (digit == 3) {
			inputNumber += "3";
			return R.drawable.d_3;
		}
		if (digit == 4) {
			inputNumber += "4";
			return R.drawable.d_4;
		}
		if (digit == 5) {
			inputNumber += "5";
			return R.drawable.d_5;
		}
		if (digit == 6) {
			inputNumber += "6";
			return R.drawable.d_6;
		}
		if (digit == 7) {
			inputNumber += "7";
			return R.drawable.d_7;
		}
		if (digit == 8) {
			inputNumber += "8";
			return R.drawable.d_8;
		}
		if (digit == 9) {
			inputNumber += "9";
			return R.drawable.d_9;
		}
		return R.drawable.d_9;
	}

	private List<ContentChannel> getCurGategoryChannels(String categoryId) {
		LogUtil.e(tag, "getCurGategoryChannels","categoryId = " + categoryId + " channel.size = " + allContentChannel.size());
		
		if (allContentChannel == null) {
			return null;
		}
		LogUtil.e(tag, "getCurGategoryChannels","category id = " + categoryId);
		List<ContentChannel> channels = new ArrayList<ContentChannel>();
		for (int i = 0; i < allContentChannel.size(); i++) 
		{
			if (Var.allChannels.get(i).getLanguage()!=null&&allContentChannel.get(i).getLanguage().contains(",")) //说明这个频道从属于多个类别
			{
				String cids[] = allContentChannel.get(i).getLanguage().split(",");
				for (String string : cids) 
				{
					if (string.equals(categoryId)) 
					{
						channels.add(allContentChannel.get(i));
						break;
					}
				}
 			}else {
				if (Var.allChannels.get(i).getLanguage()!=null&&allContentChannel.get(i).getLanguage().equals(categoryId))
				{
					channels.add(allContentChannel.get(i));
				}
			}
		}

		if (channels.size() > 0) {
			return channels;
		} else {
			return null;
		}

	}

	private void printContentChannel(List<ContentChannel> list) {
		if (list == null) {
			LogUtil.i(tag,"printContentChannel", "list isChangeChangeInNumber null");
			return;
		}

		for (int i = 0; i < list.size(); i++) {
			LogUtil.i(tag,list.get(i).getName(), list.get(i).toString());
		}
	}

	
	
	public void setAudio(int vol)
	{
		
	}
	
  
	private void updateChannelViewInfo(boolean isFirst) {
		updateKeyChangeViewTime();

		if (curPlayingChannel == null) {
			return;
		}

		curPlayingChannelId = curPlayingChannel.getContentId(); //获取当前播放频道的id
		ll_channelInfo.setVisibility(View.VISIBLE);
		
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, getResources().getDimensionPixelSize(R.dimen.px_146));
		lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		int top = ScreenTools.getScreenHeight(mContext)-getResources().getDimensionPixelSize(R.dimen.px_146);
		
		if (isFirst) 
		{
			lp.setMargins(getResources().getDimensionPixelSize(R.dimen.px_389), 0, 0, 0);
			
		}else {
			lp.setMargins(0, 0, 0, 0);
		}
		ll_channelInfo.setLayoutParams(lp);
//		ll_channelInfo.setGravity(Gravity.BOTTOM);
		tv_channel_name.setText(curPlayingChannel.getName());
		tv_channel_num.setText(curPlayingChannel.getChannelNumber());
		tv_channel_name.setTag(curPlayingChannel.getContentId());
		tv_chanale_playing.setText("");
		tv_channel_will_play.setText("");
		Log.i("MARK","updateChannelViewInfo dealSchedule");
		dealSchedule();
	}

	/**
	  * dealSchedule(这里用一句话描述这个方法的作用)
	  
	  * @return void    返回类型
	  * @modifyHistory  createBy Mernake
	  */
	private void dealSchedule() 
	{

		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		Calendar calendar = Calendar.getInstance();
		Date nowdate = calendar.getTime();
		String startDateTime = formatter.format(nowdate);
		Log.i("MARK"," dealSchedule startDateTime=="+startDateTime);
		System.out.println("############:Date:"+startDateTime);
//		Schedule[] res = ScheduleCache.getSchedule(curPlayingChannel.getContentId(), startDateTime);

		Schedule[] res = DataModel.getInstance().getSchedule(curPlayingChannel.getContentId(),startDateTime);
		Log.i("MARK"," dealSchedule res=="+res);
		if (res == null)
		{
			Log.i("MARK"," dealSchedule res == null");
//			postGetCurScheduleList(curPlayingChannel.getContentId());//向服务器请求该频道当天的全部节目单
//			postGetScheduleList(curPlayingChannel.getContentId()); //向服务器请求该频道12小时内的节目单
		}else {
			Log.i("MARK"," dealSchedule setScheduleInfo");
			setScheduleInfo(res);
		}
	}

	/**
	  * setScheduleInfo(这里用一句话描述这个方法的作用)
	  * @param res  
	  * @return void    返回类型
	  * @modifyHistory  createBy Mernake
	  */
	private void setScheduleInfo(Schedule[] res) 
	{
		try{
			Log.i("MARK"," setScheduleInfo  res[0]=="+res[0].getProgramName());
			Log.i("MARK"," setScheduleInfo  res[1]=="+res[1].getProgramName());
		}catch (Exception e){
			e.printStackTrace();
		}finally {

		}
		StringBuffer sBuffer = new StringBuffer();
		if (!res[0].getProgramName().equals("none")) 
		{
			String info1 = res[0].getStartTime();
			sBuffer.append("正在播放  ");
			sBuffer.append(info1.substring(0, 2) + " : " + info1.subSequence(2, 4));
			sBuffer.append("  " + res[0].getProgramName());
			tv_chanale_playing.setText(sBuffer.toString());
		}

		if (!res[1].getProgramName().equals("none")) 
		{
			String info2 = res[1].getStartTime();
			sBuffer = null;
			sBuffer = new StringBuffer();
			sBuffer.append("即将播放  ");
			sBuffer.append(info2.substring(0, 2) + " : " + info2.subSequence(2, 4));
			sBuffer.append("  " + res[1].getProgramName());
			tv_channel_will_play.setText(sBuffer.toString());
		}
	}

	private void showFrameAnimation(boolean isShow) {
		LogUtil.e(tag,"SHOWFRAME:", isShow+"");
		if (isShow) 
		{
			if (faaImage != null) 
			{
				faaImage.setVisibility(View.GONE); // this row code can not
				faaImage.setVisibility(View.VISIBLE);
				faaImage.bringToFront();
				mLoadingView = (AnimationDrawable) faaImage.getBackground();
			}
			if (mLoadingView != null)
			{
				mLoadingView.start();
			}
		} else {
			if (mLoadingView != null) 
			{
				mLoadingView.stop();
			}
			if (faaImage != null) 
			{
				faaImage.setVisibility(View.INVISIBLE);
			}
		}
	}
	
	
	
	/**TimeShift total time**/
	public static int TIME_SHIFT_TOTAL_TIME = 60 * 3;
	
	/**Current liveplay program Url**/
	private String timeshift_playing_url = "";
	/**Time shift url**/
	private String timeshift_shift_url = "";
	/**Pause icon**/
	private ImageView iv_timeshift_pause_play = null;
	/**Layout except pause icon**/
	private RelativeLayout rl_timeshift_process = null;
	/**Seekbar**/
	private SeekBar sb_timeshift_process = null;
	/**Right text -- current time**/
	private TextView tv_timeshift_right = null;
	/**Middle text -- current timeshift time**/
	private TextView tv_timeshift_middle = null;
	/**Left text -- limits of timeshift**/
	private TextView tv_timeshift_left = null;
	/**length(minute) of every press key**/
	private int step = 1;
	
	/**current timeshift position**/
	private int curMiddleTime = TIME_SHIFT_TOTAL_TIME;
	private int curMiddleTimeSeconds = TIME_SHIFT_TOTAL_TIME * 60;
	
	
	/**The beginning milisecond**/
	private long rightTime_miliSecond = 0;
	private int consumeTime = 0;
	private long timekeyDownShift = 0;
	
	ContentChannel lastPlayingChannel = null;
	
	private void resetTimeShiftVar()
	{
		timeshift_shift_url = "";
		curMiddleTime = TIME_SHIFT_TOTAL_TIME;
		curMiddleTimeSeconds = curMiddleTime * 60;
		rightTime_miliSecond = 0;
		consumeTime = 0;
		tv_liveplay_timeshift.setVisibility(View.INVISIBLE);
	}
	
	public void initTimeShiftSeekBar(View view)
	{
		iv_timeshift_pause_play = (ImageView)view.findViewById(R.id.iv_timeshift_pause_play);
		rl_timeshift_process = (RelativeLayout)view.findViewById(R.id.rl_timeshift_process);
		sb_timeshift_process = (SeekBar)view.findViewById(R.id.sb_timeshift_process);
		sb_timeshift_process.setMax(TIME_SHIFT_TOTAL_TIME);
		tv_timeshift_right = (TextView)view.findViewById(R.id.tv_timeshift_right);
		tv_timeshift_middle = (TextView)view.findViewById(R.id.tv_timeshift_middle);
		tv_timeshift_left = (TextView)view.findViewById(R.id.tv_timeshift_left);
		
		iv_timeshift_pause_play.setVisibility(View.INVISIBLE);
		rl_timeshift_process.setVisibility(View.INVISIBLE);
		resetTimeShiftTextView();
	}
	
	
	private void resetTimeShiftTextView()
	{
		resetTimeShiftVar();
		/**Time -- hh:mm**/
		String rightTime = DateUtil.getTimeStr(Calendar.getInstance());
		rightTime_miliSecond = System.currentTimeMillis();
		LogUtil.e(tag, "resetTimeShiftTextView","Right time is " + rightTime);
		String leftTime = getLeftTime(rightTime);
		LogUtil.e(tag, "resetTimeShiftTextView","Left time is " + leftTime);
		String middleTime = rightTime;
		LogUtil.e(tag, "resetTimeShiftTextView","Middle time is " + middleTime);
//		tv_timeshift_right.setText(rightTime);
		tv_timeshift_left.setText(leftTime);
		tv_timeshift_middle.setText(middleTime);
//		curMiddleTime = TIME_SHIFT_TOTAL_TIME;
		sb_timeshift_process.setProgress(curMiddleTime);
		int marginLeft = curMiddleTime * getResources().getDimensionPixelSize(R.dimen.px_843) / TIME_SHIFT_TOTAL_TIME;
		if(marginLeft < 0)
		{
			LogUtil.e(tag, "resetTimeShiftTextView","updateProcessSeekBar -->" + marginLeft);
		}
		replaceMiddleTime(marginLeft);
		
	}
	
	private void resetLeftAndRightTime()
	{
		String rightTime = DateUtil.getTimeStr(Calendar.getInstance());
		rightTime_miliSecond = System.currentTimeMillis();
		LogUtil.e(tag, "resetLeftAndRightTime","Right time is " + rightTime);
		String leftTime = getLeftTime(rightTime);
		LogUtil.e(tag, "resetLeftAndRightTime","Left time is " + leftTime);
//		tv_timeshift_right.setText(rightTime);
		tv_timeshift_left.setText(leftTime);
	}

	private String getLeftTime(String rightTime) 
	{
		String tempTime = DateUtil.getNowDatetimeSS();
		LogUtil.e(tag, "getLeftTime","Now tempTime is " + tempTime);
		int rightTime_hh = DateUtil.getHour(tempTime);
		int rightTime_mm = DateUtil.getMinute(tempTime);
		int leftTime_hh = 0;
		
		int a  = TIME_SHIFT_TOTAL_TIME  / 60;
		if (rightTime_hh < a) 
		{
			leftTime_hh = 24 + rightTime_hh - a;
		}else if (rightTime_hh == a) 
		{
			leftTime_hh = 0;
		}else 
		{
			leftTime_hh = rightTime_hh - a;
		} 
		
		if (rightTime_mm < 10) 
		{
			return leftTime_hh + ":0" + rightTime_mm;
		}
			
		return leftTime_hh + ":" + rightTime_mm;
	}
	
	private void updateMiddleTime(int cur,int keyCode)
	{
		resetLeftAndRightTime();
		consumeTime = (int)((rightTime_miliSecond - timekeyDownShift) / 1000 / 60);
		LogUtil.e(tag,"updateMiddleTime"," consumeTime=="+consumeTime);
		if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT)
		{
			curMiddleTime = curMiddleTime - step - consumeTime;
		}else{
			curMiddleTime = curMiddleTime + step - consumeTime;
		}
		if (curMiddleTime > TIME_SHIFT_TOTAL_TIME) 
		{
			curMiddleTime = TIME_SHIFT_TOTAL_TIME;
		}else if (curMiddleTime < 0) 
		{
			curMiddleTime = 0;
		}
		curMiddleTimeSeconds = curMiddleTime * 60;
		sb_timeshift_process.setProgress(curMiddleTime);
		tv_timeshift_middle.setText(calMiddleTimeText());
		LogUtil.e(tag, "updateMiddleTime","current middle time is " + curMiddleTime);
		int marginLeft = curMiddleTime *  getResources().getDimensionPixelSize(R.dimen.px_843) / TIME_SHIFT_TOTAL_TIME;
		if(marginLeft < 0)
		{
			LogUtil.e(tag, "updateMiddleTime","updateProcessSeekBar -->" + marginLeft);
		}
		replaceMiddleTime(marginLeft);
		
	}
	
	private String calMiddleTimeText() 
	{
		System.out.println("timeshifttimeshift : curmiddletime = " + curMiddleTime);
		if (curMiddleTime == TIME_SHIFT_TOTAL_TIME) 
		{
			return DateUtil.getTimeStr(Calendar.getInstance());
		}
		
		if (curMiddleTime == 0) 
		{
			return tv_timeshift_left.getText().toString();
		}
		
		int left_hh = Integer.parseInt(tv_timeshift_left.getText().toString().split(":")[0]);
		int left_mm = Integer.parseInt(tv_timeshift_left.getText().toString().split(":")[1]);
		
		System.out.println("timeshifttimeshift : left_hh = " + left_hh + " left_mm = " + left_mm);
		
		if (left_mm + curMiddleTime >= 180) 
		{
			left_hh += 3;
			left_mm = left_mm + curMiddleTime - 180;
			
		}else if (left_mm + curMiddleTime >= 120) 
		{
			left_hh += 2;
			left_mm = left_mm + curMiddleTime - 120;
		}else if (left_mm + curMiddleTime >= 60) 
		{
			left_hh += 1;
			left_mm = left_mm + curMiddleTime - 60;
		}else 
		{
			left_mm = left_mm + curMiddleTime;
		}
		if (left_hh >= 24) 
		{
			left_hh = left_hh - 24;
		}
		
		String left_hh_str = String.valueOf(left_hh);
		String left_mm_str = String.valueOf(left_mm);
		System.out.println("timeshifttimeshift : left_hh_str = " + left_hh_str + " left_mm_str = " + left_mm_str);
		
		if (left_hh_str.length() == 1) 
		{
			left_hh_str = "0" + left_hh_str;
		}
		if (left_mm_str.length() == 1) 
		{
			left_mm_str = "0" + left_mm_str;
		}
		
		return left_hh_str+":"+left_mm_str;
	}

	private void replaceMiddleTime(int marginLeft)
	{
		LogUtil.e(tag, "replaceMiddleTime","marginLeft== " + marginLeft);

		RelativeLayout.LayoutParams rl = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		
		if (Method.getScreenWidth(getActivity()) == 1920) 
		{
			rl.setMargins(marginLeft+getResources().getDimensionPixelSize(R.dimen.px_46) + 10, getResources().getDimensionPixelSize(R.dimen.px_12)-10, 0, 0);
		}
		else {
			rl.setMargins(marginLeft+getResources().getDimensionPixelSize(R.dimen.px_46) - 15, getResources().getDimensionPixelSize(R.dimen.px_12), 0, 0);
		}

		
		
		//		if (VarParam.url.contains("msnm")) //移动的 
//		{
//			rl.setMargins(marginLeft+getResources().getDimensionPixelSize(R.dimen.px_34), getResources().getDimensionPixelSize(R.dimen.px_12), 0, 0);
//		}else if (VarParam.url.contains("tsnm")) //电信的
//		{
//			rl.setMargins(marginLeft+getResources().getDimensionPixelSize(R.dimen.px_46), getResources().getDimensionPixelSize(R.dimen.px_12), 0, 0);
//		}
		
		tv_timeshift_middle.setLayoutParams(rl);
	}
	
	private void jumpToNewTime()
	{
		LogUtil.i(tag,"jumpToNewTime", "jumpToNewTime() timeshift_shift_url=="+timeshift_shift_url);
		timeshift_shift_url = getShiftUrl();
		LogUtil.i(tag,"jumpToNewTime", "timeshift_shift_url=="+timeshift_shift_url);
		convertToPlayNewUrl();
	}
	
	private void convertToPlayNewUrl() 
	{
		LogUtil.i(tag,"convertToPlayNewUrl()","");

		surfaceView.stop();
		ContentChannel newChannel = new ContentChannel();
		if (curShowChannelList == null || curShowChannelList.size() == 0
				|| curShowChannelList.get(curChannel) == null) 
		{
			newChannel.setContentId(lastPlayingChannel.getContentId());
			newChannel.setName(lastPlayingChannel.getName());
		}else
		{
			newChannel.setContentId(curShowChannelList.get(curChannel).getContentId());
			newChannel.setName(curShowChannelList.get(curChannel).getName());
		}
		newChannel.setPlayURL(timeshift_shift_url);
		newChannel.setTimeShiftURL(timeshift_playing_url);
		LogUtil.e(tag, "convertToPlayNewUrl","converted timeshift url is " + timeshift_shift_url);
		
		
		String second = tv_timeshift_middle.getText().toString();
		second = second.replace(":", "");
//		int intSecond = Integer.valueOf(second);
		String yearmonth = AbDateUtil.getCurrentDate("yyyyMMdd");
		String time = yearmonth+second+"00";
		System.out.println("####:日志上传时移时间："+time);
		if (sb_timeshift_process.getProgress() != sb_timeshift_process.getMax()) 
		{
			uploadObject.setChannelId(newChannel.getContentId());
			uploadObject.setChannelName(newChannel.getName());
			uploadObject.setProgramStatus(0);
			uploadObject.setTimeshiftStartTime(time);
			DataAnalyzer.creator().uploadTimeShift(uploadObject);
		}
		playVideo(newChannel);
	}

	private String getShiftUrl() 
	{
		
		long shiftTime = (TIME_SHIFT_TOTAL_TIME - sb_timeshift_process.getProgress()) * 60;
//		long urlTime = rightTime_miliSecond - shiftTime;
		LogUtil.i(tag,"getShiftUrl", "shiftTime=="+shiftTime);
		if (shiftTime == 0) 
		{
			setTimeshiftTextVision(false);
			return curPlayingChannel.getPlayURL();
		}
		
		if (timeshift_playing_url.contains("tstv")) 
		{
			return timeshift_playing_url + "&offsetTime=" + shiftTime;
		}
		return timeshift_playing_url + "?offsetTime=" + shiftTime;
	}
	
	
	/**2014.05.05 modify play UI**/
	
	TextView tv_liveplay_timeshift = null;

	@SuppressLint({"NewApi", "Range"})
	private void setTimeshiftTextVision(boolean b) 
	{
		if (b) 
		{
			tv_liveplay_timeshift.setVisibility(View.VISIBLE);
			tv_liveplay_timeshift.setAlpha(128);
		}else 
		{
			tv_liveplay_timeshift.setVisibility(View.INVISIBLE);
		}
		
	}

	@Override
	public void playfinished() 
	{
		LogUtil.e(tag, "playfinished", "In the LivePlay exist finished, is that right?");
	}
	
	@SuppressLint("HandlerLeak")
	private Handler hd = new Handler()
	{
		@Override
		public void handleMessage(Message msg) 
		{
			if (msg.what == 0)  //第一次进入时
			{
				if (curCategoryIndex!=0){
					moveToOneCategory(curCategoryIndex);
				}else jumpToAllChannel();
				ll_channelList.setVisibility(View.VISIBLE);
				updateChannelViewInfo(true);
				updateKeyChangeViewTime();
			}else if (msg.what == MSG_INVISIABLE_BG) 
			{
				timeInvisiable = -1;
				rl_bg.setVisibility(View.GONE);
				postVisible();
				isFirstin = false;
			}
		}
		
	};

	@SuppressLint("NewApi")
	private void moveToOneCategory(int categoryIndex)
	{
		LogUtil.i(tag, "moveToCurCategory","moveToCurCategory****************start");
		if (categorys == null || categorys.size() == 0 || categoryIndex >= categorys.size())
		{
			return;
		}
		updateKeyChangeViewTime();


		Category category = categorys.get(curCategoryIndex);
		String name = getCategroyName(category);
		tv_curCategory.setText(name);
//		if (category.getName().equals("全部"))
//		{
//			tv_curCategory.setText("全部频道");
//		}else tv_curCategory.setText(category.getName());
		LogUtil.e(tag, "moveToCurCategory","allContentChannel.size = " + Var.allChannels.size());
		LogUtil.e(tag, "moveToCurCategory","category id = " + category.getId());
		LogUtil.e(tag, "moveToCurCategory","allChannelCategoryId = " + allChannelCategoryId);
		List<ContentChannel> channels = null;
		if (category.getId().equals(Category_Recent))
		{
			recentAddItem();
			channels = recentList;
		} else if (category.getId().equals(allChannelCategoryId)) {
			channels = Var.allChannels;
		} else if (category.getId().equals(Category_Save)) {
			channels = saveList;
		} else {
			channels = getCurGategoryChannels(category.getId());
		}

		LogUtil.i(tag, "moveToCurCategory","moveToCurCategory-->category name:" + category.getName()+ ",id:" + category.getId());
		if(channels == null)
		{
			LogUtil.i(tag, "moveToCurCategory","moveToCurCategory -->curchannel is null");
		}

		if (channels == null && !category.getId().equals(Category_Recent)
				&& !category.getId().equals(Category_Save))
		{// ����
			postGetContentList(category.getId());
		} else
		{
			curShowChannelList = channels;
			reloadChannelList(true);
		}
		ll_channelList.setVisibility(View.VISIBLE);
		LogUtil.i(tag, "moveToCurCategory","moveToCurCategory: cateName:" + category.getName());
//		printContentChannel(channels);
		LogUtil.i(tag, "moveToCurCategory","moveToCurCategory****************end");

	}

	/**
	 * getCategroyName(这里用一句话描述这个方法的作用)

	 * @param category
	 * @return
	 * @return String    返回类型
	 * @modifyHistory  createBy Mernake
	 */
	private String getCategroyName(Category category)
	{
		String name = category.getName();
		if (name.equals("全部"))
		{
			name = "全部频道";
			return name;
		}

		if (name.length() == 2)
		{
			name = "　"+name+"　";
		}else if(name.length() == 3)
		{
			name = name.charAt(0) + " "+name.charAt(1)+" "+name.charAt(2);
		}

		return name;
	}

/***************远程调用部分********************/
	
	
	
	private MyRemoteReveiver mReceiver = null; 
	
	public class MyRemoteReveiver extends BroadcastReceiver
	{
		@Override
		public void onReceive(Context context, Intent intent) 
		{
			/*
			 * 1.停止播放
			 * 2.换台
			 * 
			 * */
			Log.e("stbserverli", System.currentTimeMillis()+"");
			int type = intent.getIntExtra("type", -1);
			String name = intent.getStringExtra("name");
			name = Config.getChannelName(name);
			if (name == null) 
			{
				return;
			}
			String time = intent.getStringExtra("time");
			
			System.out.println("###: type = " + type + " name = " + name + " time = " + time);
			if (type == 2) 
			{
				//跳转回看
				Config.clear();
				Config.smartTvChannelName = intent.getStringExtra("name");
				Config.smartProgramStartTime = intent.getStringExtra("time");
				iFragmentJump.jumpToFramgment(2, 4);
				isJumpTo = true;
				return;
			}
			
			//直播跳台
			resetCur(name);
		}

		private void resetCur(String name) 
		{
			for (ContentChannel contentChannel : allContentChannel) 
			{
				if (contentChannel.getName().equals(name)) 
				{
					resetTimeShiftTextView();
					inputNumber = contentChannel.getChannelNumber();
					changeChannelByChannelNumber(true);
					return;
				}
			}
		}
	}
	
	private void regRemoteReceiver() 
	{
		mReceiver = new MyRemoteReveiver();
		IntentFilter filter = new IntentFilter(Config.BROADCAST_REMOTE_TV_PROGRAM);
		getActivity().registerReceiver(mReceiver, filter);
	}

	@Override
	public void goToNextChannel() 
	{
		recentAddItem();
		resetTimeShiftTextView();
		setAllInvisible();
		timeKeyDownWay = Calendar.getInstance().getTimeInMillis();
		curShowChannelList = allContentChannel;
		if (curShowChannelList == null || curShowChannelList.size() == 0) 
		{
			return;
		}
		int index = getSeqInAllChannelByChannelNumber(curShowChannelList,
				curPlayingChannel.getChannelNumber());
		int up = index + 1;
		if (up > curShowChannelList.size() - 1) 
		{
			up = 0;
		}
		uploadObject.setEnterChannelType(0);
		curPlayingChannel = curShowChannelList.get(up);
		curChannel = up;
		curCategoryIndex = allContentChannelIndex;
		updateChannelViewInfo(false);
	}

	@Override
	public void goToLastChannel() 
	{
		recentAddItem();  //判断是否要将频道加入最近播放频道列表
		resetTimeShiftTextView();  //重置时移相关参数，如果当前处于时移状态，上下键换台会转为直播
		setAllInvisible(); //消除掉所有界面上的弹框
		timeKeyDownWay = Calendar.getInstance().getTimeInMillis(); 
		curShowChannelList = allContentChannel;//当前播放频道列表恢复到全部频道
		if (curShowChannelList == null || curShowChannelList.size() == 0) {
			return;
		}
		int index = getSeqInAllChannelByChannelNumber(curShowChannelList,curPlayingChannel.getChannelNumber()); //通过当前播放频道的频道号取出当前频道在全部频道列表中的序号
		int up = index - 1;  //要换到的频道序号为当前频道序号-1，如果小于0，则切换到全部频道到的最后1个
		if (up < 0) {
			up = curShowChannelList.size() - 1;
		}
		uploadObject.setEnterChannelType(1);
		curCategoryIndex = Var.allCategoryIndex;  //设置当前类别为全部频道类别
		curPlayingChannel = curShowChannelList.get(up); //取出将要切换的频道并赋值
		curChannel = up;  //设置当前频道序号为将要切换的频道序号
		updateChannelViewInfo(false);
	}

	@Override
	public void goToLive() 
	{
		
	}

	@Override
	public void goToChannel(String num, String channelName) 
	{
		if (AbStrUtil.isNumber(num)) 
		{
			inputNumber = num;
			changeChannelByChannelNumber(false);
		}
	}

	@Override
	public void goToTvBack() 
	{
		XunfeiTools.getInstance().sendLiveStatusBroadcast(mContext, false);
		XunfeiTools.getInstance().sendBackPlayBroadcast(mContext, true);
		setAllInvisible();
		int index = getFirstPlayInfoByChannelName(curPlayingChannel.getName());
		iFragmentJump.jumpToFramgment(LaunchType.TYPE_REPLAY, index);
		isJumpTo = true;
	}

	@Override
	public void goToSchedule(String num, String name, String starttime, String startdate, String endtime,
			String enddate) 
	{
		//TODO 点击跳转回看界面
				System.out.println("#########:这里收到了接口调用！！！");
				ll_channelInfo.setVisibility(View.GONE);
				ll_channelList.setVisibility(View.INVISIBLE);
				ll_liveMenu.setVisibility(View.GONE);
				rl_timeshift_process.setVisibility(View.INVISIBLE);
				ivNoChannel.setVisibility(View.GONE);
				ivNoChannel.setVisibility(View.GONE);
				soundViewControl.showVol(false);
				timekeyDownDigit = -1;
				timeKeydownView = -1;
				timeKeyDownWay = -1;
				timeNoChannel = -1;
				XunfeiTools.getInstance().sendLiveStatusBroadcast(mContext, false);
				XunfeiTools.getInstance().sendBackPlayBroadcast(mContext, true);
				int index = getFirstPlayInfoByChannelName(curPlayingChannel.getName());
				isJumpTo = true;
				Bundle bundle = new Bundle();
				bundle.putString("xunfeinum", num);
				bundle.putString("name", name);
				bundle.putString("starttime", starttime);
				bundle.putString("startdate", startdate);
				bundle.putBoolean("isjumpto", isJumpTo);
				iFragmentJump.jumpToFragmentWithBundles(LaunchType.TYPE_REPLAY, index, bundle);
	}
	
	@Override
	public void voicePlay() 
	{
		super.voicePlay();
		//老单播直播没有设计暂停，所以也没有播放的概念
	}
	
	
	@Override
	public void voiceJumpBackwardBySeconds(int seconds) 
	{
		super.voiceJumpBackwardBySeconds(seconds);
		setTimeshiftTextVision(true);
		updateMiddleTimeByVoice(seconds, -1);
//		 sb_timeshift_process.setProgress(sb_timeshift_process.getMax()-seconds);
         jumpToNewTime();
	}
	
	@Override
	public void voiceJumpForwordBySeconds(int seconds) 
	{
		super.voiceJumpForwordBySeconds(seconds);
		int progress = sb_timeshift_process.getProgress()+seconds / 60;
		if (progress>=TIME_SHIFT_TOTAL_TIME) 
		{
			progress = TIME_SHIFT_TOTAL_TIME;
			setTimeshiftTextVision(false);
		}
		updateMiddleTimeByVoice(seconds, 1);
//		sb_timeshift_process.setProgress(progress);
		jumpToNewTime();
	}
	
	private void updateMiddleTimeByVoice(int seconds,int way)
	{
		resetLeftAndRightTime();
		consumeTime = (int)((rightTime_miliSecond - timekeyDownShift) / 1000 / 60);
		if (way<0) 
		{
			curMiddleTimeSeconds = curMiddleTimeSeconds - seconds;
			curMiddleTime = curMiddleTimeSeconds / 60;
		}else{
			curMiddleTimeSeconds = curMiddleTimeSeconds + seconds;
			curMiddleTime = curMiddleTimeSeconds / 60;
		}
		if (curMiddleTime > TIME_SHIFT_TOTAL_TIME) 
		{
			curMiddleTime = TIME_SHIFT_TOTAL_TIME;
			curMiddleTimeSeconds = curMiddleTime * 60;
		}else if (curMiddleTime < 0) 
		{
			curMiddleTime = 0;
			curMiddleTimeSeconds = 0;
		}
		sb_timeshift_process.setProgress(curMiddleTime);
		tv_timeshift_middle.setText(calMiddleTimeText());
		LogUtil.e(tag, "updateMiddleTime","current middle time is " + curMiddleTime);
		int marginLeft = curMiddleTime *  getResources().getDimensionPixelSize(R.dimen.px_843) / TIME_SHIFT_TOTAL_TIME;
		if(marginLeft < 0)
		{
			LogUtil.e(tag, "updateMiddleTime","updateProcessSeekBar -->" + marginLeft);
		}
		replaceMiddleTime(marginLeft);
		
	}
	
	
	@Override
	public void jumpToResee() 
	{
		super.jumpToResee();
		setAllInvisible();
		int index = getFirstPlayInfoByChannelName(curPlayingChannel.getName());
		iFragmentJump.jumpToFramgment(LaunchType.TYPE_REPLAY, index);
		isJumpTo = true;
	}

	/**用来存放准备播放，但是还没进行播放鉴权的频道**/
	private ContentChannel zzTempChannel;
	private IUpdateData gdIupdata = new IUpdateData()
	{
		@Override
		public void updateData(String method, String uniId, Object object, boolean isSuccess)
		{
			if (method.equals(GDHttpTools.METHOD_GETLIVESCHEDULELIST) && isSuccess)
			{
				System.out.println("GD live schedule list request success!!!");
				dealResWithSchedules(object, uniId);
			}else if (method.equals(GDHttpTools.METHOD_GETLIVESCHEDULELIST) && !isSuccess){
				System.out.println("GD live schedule list request failed!!!");
			}

			if (method.equals(GDHttpTools.METHOD_ORDER_PLAYAUTH) && isSuccess)
			{
				System.out.println("GD live order playauth request success!!!");
				GDOrderPlayAuthCMHWRes res = (GDOrderPlayAuthCMHWRes) object;
				if (res == null) {
					System.out.println("GD live order playauth reques res = null");
					return;
				}
				if ( res.getCode().equals("51000-1"))//51A00-1 --> 51000-1
				{ //the channel can play
					GDHttpTools.getInstance().setNeedReplay(true);//这里查询成功了，虽然没有暂停，但是也设置一下这个可以重新播放的参数

					resetTimeShiftTextView();
					timeshift_playing_url = zzTempChannel.getTimeShiftURL();
					timeKeyDownRecent = Calendar.getInstance().getTimeInMillis();
				}else if (res.getCode().equals("51000-2"))//51A00-2 --> 51000-2
				{//the channel need to buy
					showToast("试看结束，请订购观看");
					GDHttpTools.getInstance().setNeedReplay(false);//不准重新播放，直到订购成功

					if (surfaceView!= null) {
						surfaceView.pause();//暂停播放
					}
					System.out.println("getActivity()=="+getActivity());

					if(getActivity()!=null){
                        ZZDialogTools.getInstance().showOrderDialog(curShowChannelList.get(curChannel).getName(),Var.userId,curShowChannelList.get(curChannel).getContentId(),Var.allCategoryId,getActivity());
                    }
				}

			}else if (method.equals(GDHttpTools.METHOD_ORDER_PLAYAUTH) && !isSuccess) {
				GDOrderPlayAuthCMHWRes res = (GDOrderPlayAuthCMHWRes) object;
				if (res == null) {
					System.out.println("res = null && isSuccess = false");
					return;
				}
				if ("51041-507".equals(res.getCode())) {//GD不通过，CM通过的情况下，需要继续播放，并且将该订单同步到百图平台
					Log.e("xumin", "GD不通过， CM通过");
					//					GDHttpTools.getInstance().cmhOrderSync(mContext, "0", res.getStartTime(), Var.userId, GDHttpTools.getInstance().getUsertokenAIDL(), "", res.getData().getProductid(), );
					GDHttpTools.getInstance().setNeedReplay(false);//不准重新播放，直到订购成功
					if (surfaceView!= null) {
						surfaceView.pause();//暂停播放
					}
					System.out.println("xumin: playauth: res.getData().size(): " + res.getData().size());
					if (res.getData() != null && res.getData().size() != 0) {//这里是判断了以下，有产品列表的,需要显示产品列表，同时需要带着这个错误码下去，用来告诉ZZChooseProductActivity这个是GD不通过，CM通过的，需要将这个OrderId传都
						ZZDialogTools.getInstance().showOrderDialog(curShowChannelList.get(curChannel).getName(),Var.userId,curShowChannelList.get(curChannel).getContentId(),Var.allCategoryId,getActivity(),"51041-507");
					}else {//产品列表为空，所以显示错误信息,也就是有可能返回的产品列表的size为0,这时候就显示出错误就好.
						System.out.println("xumin: playauth: 没有从百途拿到产品列表");
						//						ZZDialogTools.getInstance().showOrderDialog(curShowChannelList.get(curChannel).getName(),Var.userId,curShowChannelList.get(curChannel).getContentId(),Var.allCategoryId,getActivity(),"51041-507");
						((ActivityLaunchBase)getActivity()).showGDErrorDialog(res.getCode(),res.getDescription(),"","");//GD不通过，CM通过
					}

				}else if ("51041-506".equals(res.getCode())){
					Log.e("xumin", "GD通过， CM不通过");
					GDHttpTools.getInstance().setNeedReplay(false);//不准重新播放，直到订购成功
					if (surfaceView!= null) {
						surfaceView.pause();//暂停播放
					}
					//51041-506和51041-507的情况下，都是要有产品列表返回的，因为这个时候，需要再次弹出订购页面。需要拿着这个产品ID，继续去订购
					System.out.println("xumin: playauth: res.getData().size(): " + res.getData().size());
					if (res.getData() != null && res.getData().size() != 0) {//这里是判断了一下，有产品列表的
						ZZDialogTools.getInstance().showOrderDialog(curShowChannelList.get(curChannel).getName(),Var.userId,curShowChannelList.get(curChannel).getContentId(),Var.allCategoryId,getActivity());
					}else {//产品列表为空，所以显示错误信息
						System.out.println("xumin: playauth: 没有从华为拿到产品列表");
						((ActivityLaunchBase)getActivity()).showGDErrorDialog(res.getCode(),res.getDescription(),"","");
					}

				}else {//这是错误码是其他情况，就直接弹出错误码,并且暂停播放
					GDHttpTools.getInstance().setNeedReplay(false);//不准重新播放，直到订购成功
					if (surfaceView!= null) {
						surfaceView.pause();//暂停播放
					}
					((ActivityLaunchBase)getActivity()).showGDErrorDialog(res.getCode(),res.getDescription(),"","");
				}
			}

			if (method.equals(GDHttpTools.METHOD_GETLIVECONTENTLIST) && isSuccess)
			{
				dealResWithContentChannels(object, uniId);
			}

			if (method.equals(GDHttpTools.METHOD_GETAIDLDATA)) {
				//不为别的，仅仅是在VIP节目的时候，请求一下aidl接口，在这里保存下，防止过会儿使用的userToken是过期的
				if (isSuccess)
				{
					GDAuthAidlRes res = (GDAuthAidlRes) object;
					String operaTag = "";
					if (res.getData().getOperaTag().equals("HW")){
						operaTag = "1";
					}else if (res.getData().getOperaTag().equals("ZTE")){
						operaTag = "2";
					}
					GDHttpTools.getInstance().setTag(operaTag);
					GDHttpTools.getInstance().setAIDLParam(res.getData().getEPGURLAIDL(),res.getData().getUserTokenAIDL(),res.getData().getIPTVAccountAIDL());
				}
			}
		}

	};


	private MernakeCountDownTools.ICountDownMessage iCountDownMessage = new MernakeCountDownTools.ICountDownMessage()
	{
		@Override
		public void onCountDownFinished()
		{
			hd.post(new Runnable() {
				@Override
				public void run() {
					doSomethingWhenCountdownFinished();
				}
			});

		}

		@Override
		public void onCountDownExcuting(int i, int i1)
		{
			//			showToast("倒计时进行中，剩余时间：" +i + "__"+i1);
			Log.e("xumin", "倒计时: " + i + "__" + i1);
		}

		@Override
		public void onCountDownStart()
		{
			showToast("该频道为VIP频道");
		}

		@Override
		public void onCountDownStop()
		{
			//			showToast("倒计时停止");
			mernakeCountDownTools = null;//将计数器置空
		}
	};

	private void doSomethingWhenCountdownFinished()
	{
		Log.i("MARK", "doSomethingWhenCountdownFinished");//

		//TODO 进行播放鉴权工作 1.时间超过8小时，自动删除缓存。2.优先读取缓存。3.无缓存时进行playauth请求
		ZZFileTools.getInstance().isTimeToClearCache(mContext,curPlayingChannel.getContentId());

			GDOrderPlayAuthCMHWRes res = ZZFileTools.getInstance().queryChannelPlayAuth(mContext,curPlayingChannel.getContentId());
			if (res!=null){
				GDHttpTools.getInstance().addPlayAuthList(curPlayingChannel.getContentId(),res);//读到了就一定是51000-1的code
			}

			if (res != null && "51000-1".equals(res.getCode())) {
				Log.e("MARK", "本地是正确的，还在授权期限内，正常播放");//这里缓存中的信息是51000-1，准许播放的，然后缓存时间也是在允许的范围内,这时候就让他播放，啥都不管
			}else {//res 为空、或者是因其他不可知原因code不是51000-1了，这里就得重新申请授权
				zzTempChannel = curPlayingChannel;
				Log.e("MARK", "重新申请授权");//
				GDHttpTools.getInstance().playAuth(mContext,"0",GDHttpTools.getInstance().getTag(),curShowChannelList.get(curChannel).getContentId(),Var.allCategoryId,"",Var.userId,Var.userToken,GDHttpTools.getInstance().getEpgurlAIDL(),GDHttpTools.getInstance().getUsertokenAIDL(),gdIupdata);
			}

	}

}
