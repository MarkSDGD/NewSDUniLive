
package com.xike.xkliveplay.activity.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.SeekBar;
import android.widget.TextView;

import com.arcsoft.media.ArcPlayer.TYPE_DRM;
import com.mernake.framework.tools.MernakeCountDownTools;
import com.shandong.shandonglive.zengzhi.ui.ZZDialogTools;
import com.shandong.shandonglive.zengzhi.ui.ZZFileTools;
import com.shandong.shandonglive.zengzhi.ui.ZZOrderDialog;
import com.umeng.analytics.MobclickAgent;
import com.xike.xkliveplay.R;
import com.xike.xkliveplay.activity.ActivityLaunchBase;
import com.xike.xkliveplay.activity.LaunchType;
import com.xike.xkliveplay.activity.fragment.adapter.BackplayDateAdapter;
import com.xike.xkliveplay.activity.fragment.view.AlwaysMarqueeTextView;
import com.xike.xkliveplay.activity.fragment.view.ViewReSeeChannelList;
import com.xike.xkliveplay.activity.fragment.view.ViewReSeeSchedulelList;
import com.xike.xkliveplay.framework.arcplay.ArcPlayControl;
import com.xike.xkliveplay.framework.arcplay.ArcSurfaceView;
import com.xike.xkliveplay.framework.arcplay.IPlayControl;
import com.xike.xkliveplay.framework.arcplay.IPlayFinished;
import com.xike.xkliveplay.framework.data.DataAnalyzer;
import com.xike.xkliveplay.framework.entity.BackplayDate;
import com.xike.xkliveplay.framework.entity.ContentChannel;
import com.xike.xkliveplay.framework.entity.Schedule;
import com.xike.xkliveplay.framework.entity.UploadObject;
import com.xike.xkliveplay.framework.entity.gd.GDAuthAidlRes;
import com.xike.xkliveplay.framework.entity.gd.GDOrderPlayAuthCMHWRes;
import com.xike.xkliveplay.framework.error.ErrorCode;
import com.xike.xkliveplay.framework.http.IUpdateData;
import com.xike.xkliveplay.framework.httpclient.HttpGetScheduleList;
import com.xike.xkliveplay.framework.httpclient.VarParam;
import com.xike.xkliveplay.framework.tools.AbDateUtil;
import com.xike.xkliveplay.framework.tools.AbStrUtil;
import com.xike.xkliveplay.framework.tools.DateUtil;
import com.xike.xkliveplay.framework.tools.LogUtil;
import com.xike.xkliveplay.framework.tools.Method;
import com.xike.xkliveplay.framework.tools.StringUtil;
import com.xike.xkliveplay.framework.tools.UIUtils;
import com.xike.xkliveplay.framework.varparams.Var;
import com.xike.xkliveplay.gd.GDHttpTools;
import com.xike.xkliveplay.smarttv.Config;
import com.xike.xkliveplay.xunfei.XunfeiTools;
import com.xike.xkliveplay.xunfei.XunfeiTools.XunfeiInterface;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * 
 * @author LiWei <br>
 * CreateTime: 2014年11月6日 上午9:54:12<br>
 * <b>Info:</b><br>
 *<br>
 *   <b>Method:</b> <br>
 */
@SuppressLint({ "ResourceAsColor", "ResourceAsColor", "ValidFragment" })
public class FragmentBackPlayBase extends FragmentBase implements IUpdateData,IPlayFinished,XunfeiInterface {

	/**
	 *  移动版：厂家特殊设置说明：
	 *  中兴:
	 *  1.在Onkeydown中截取了中兴遥控器的设置键值，176
	 *  
	 *  
	 */
	/** Activity日志标签 **/
	private final static String tag = "ActivityBackPlay";
	
	/** 列表代码 **/
	private final int CHANNEL = 1;// 频道
	private final int DATE = 2; // 日期
	private final int SCHEDULE = 3; // 节目单
	
	/** 框上下键切换频道频道号按下的时间 **/
	private long timeKeyDownWay = 0;
	private long timeKeyLeftRight = 0;

	/** 频道、日期、节目单的适配器 **/
	private BackplayDateAdapter backplayDateAdapter = null;
//	private BackplayScheduleAdapter backplayScheduleAdapter = null;
	/** 播放控制 **/
	private ArcPlayControl arcPlayControl = null;
	/** 播放进度条 **/
	private ImageView iv_pause_play = null;
	private RelativeLayout rl_backplay_process = null;
	private SeekBar process_seekBar = null;// seekBar
	private TextView tv_curPosition = null;// 当前时间
	private TextView tv_allTime = null; // 总时间
//	private int lastAllTime = 0;

	/** 右上角回看图标 **/
	private TextView tv_backplay_icon = null;

//	private boolean isJumpTo = false;
	/** 控制音量SeekBar **/
//	public RelativeLayout rl_backplay_vol_main = null; // 音量条总布局
//	private SeekBar vol_seekBar;// seekBar
//	public int maxVol, curVol;// 最大音量，当前音量
//	private AudioManager audioManager;// 系统音量管理
//	private ImageView iv_vol_mute;
//	public boolean isMute = false;

	/** 缓冲动画容器 **/
	private ImageView faaImage = null; // 右下角的缓冲圈
	/** 视频资源缓冲动画 **/
	private AnimationDrawable mLoadingView = null;
	/** 节目选择主界面 **/
	private RelativeLayout rl_backplay_main;
	
	/** 是否是从直播跳转过来 **/
	public boolean fromLivePlay = false;
	
	/** 当前焦点频道在频道数组中的位置 **/
	public int curChannel = 4;
	/** 当前焦点节目单在节目单中的位置 **/
	private int curSchedule = 4;
	/** 当前焦点日期在日期中的位置 **/
	private int curDate = 3;
	/** 当前播放的节目序号 **/
	/** 当前焦点在哪个列表中  默认为1，在频道列表中**/
	public static int curList = 1;

	/** 银幕 **/
	public ArcSurfaceView surfaceView;

	/** 频道列表，包含全部的所有信息 **/
	public List<ContentChannel> curShowChannelList = null;
	/** 节目单列表，包含某频道某天的全部节目内容 **/
	private List<Schedule> curShowScheduleList = null;
	/** 日期列表，包含当天及前后共7天 **/
	private List<BackplayDate> curShowDateList = null;
	
	private SoundViewControl soundViewControl = null;
	
	private View curVolView = null;
	
	/**日期焦点**/
	int focus = 3;

	/** 频道ListView **/
	private ViewReSeeChannelList lv_backplay_channel;
	/** 日期ListView **/
	private ListView lv_backplay_date;
	/** 节目单ListView **/
	private ViewReSeeSchedulelList lv_backplay_schedule;
	
	/**每按一次左右键的时移长度**/
	private int jumpStep = 10;
	private long jumpStepTime = -1;
	
	private UploadObject uploadObject = new UploadObject();
	
	private Schedule lastSchedule = null;

	/** 日期 **/
	public static String[] WEEK = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五",
			"星期六" };
	
	public long curTime = 0;
	
	public View curView = null;
	
	public IFragmentJump iFragmentJump = null;
	
	private Context context = null;
	
	private String xunFeiStartTime = "";
	private String xunFeiScheduleName = "";
	private String xunFeiNum = "";
	private String xunFeiStartDate = "";
	
	private boolean isFromVoice = false;
	private int lastLaunchType = 1000;//这是个假定的type，一个原则是不与正在使用的launchType重复
	private boolean fromRecommend =false;

	private MernakeCountDownTools mernakeCountDownTools;
	private Context mContext;
	private boolean isInPlayingMode=false; //记录正在播放界面状态

	public FragmentBackPlayBase() 
	{
		super();
	}


	public FragmentBackPlayBase(IFragmentJump _fJump)
	{
		iFragmentJump = _fJump;
	}
	
	
	@SuppressLint("ValidFragment")
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.context = activity;
		lastLaunchType =  getArguments().getInt("lastLaunchType", 1000);

		if(getArguments() == null)
		{
			curChannel = 4;
			fromLivePlay = false;
		}else {
			
			curChannel = getArguments().getInt("index",4);
			fromLivePlay = true;
			if (getArguments().containsKey("starttime")) //开始时间
			{
				xunFeiStartTime = getArguments().getString("starttime");
			}
			if (getArguments().containsKey("name")) //节目名称
			{
				xunFeiScheduleName = getArguments().getString("name");
			}
			if (getArguments().containsKey("startdate")) 
			{
				xunFeiStartDate = getArguments().getString("startdate");
			}
			if (getArguments().containsKey("xunfeinum")) //开始时间
			{
				xunFeiNum = getArguments().getString("xunfeinum");
				if (!getArguments().getBoolean("isjumpto")) 
				{
					fromLivePlay = false;
				}
			}
		}

		if (lastLaunchType == LaunchType.TYPE_RECOMMEND) {//回看入口：移动直播首页

			fromLivePlay = false;
			fromRecommend = true;
		}
		LogUtil.i(tag, "onAttach","onAttach -->fromLivePlay:" + fromLivePlay + ",curChannel:" + curChannel);
	}
	
	private int calIndex(String smartTvChannelName) 
	{
		for(int i = 0; i < Var.allChannels.size();i++)
		{
			if(Var.allChannels.get(i).getName().equals(smartTvChannelName))
			{
				return i;
			}
		}
		
		return 0;
	}
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogUtil.e(tag,"onCreate", "***********************回看界面create************************");
		mContext = getActivity().getApplicationContext();
		//		SystemProperties.set("media.amplayer.displast_frame", "1");
		initParam();
	}

	@Override
	public void onStart() {
		super.onStart();
		if(isInPlayingMode){
			isInPlayingMode=false;
		   playVideo();
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("FragmentBackPlay"); //统计页面
	}
	
	@Override
	public void onPause() 
	{
		super.onPause();
		MobclickAgent.onPageEnd("FragmentBackPlay");
		stopCountDownOrdismissOrderDialog();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) 
	{
		XunfeiTools.getInstance().setInterface(this);
		regRemoteReceiver();
		curView = inflater.inflate(R.layout.activity_backplay, container,false);
		
		/** 初始化各种view **/
		initViews();
		
		curVolView = curView.findViewById(R.id.rl_vol_main);
		curVolView.setVisibility(View.GONE);
		soundViewControl = new SoundViewControl(curView.findViewById(R.id.rl_vol_main), context);
		
		reloadChannelListView(curChannel);
		if (!xunFeiNum.equals("")) 
		{
			goToSchedule(xunFeiNum, xunFeiScheduleName, xunFeiStartTime, xunFeiStartDate, "", "");
			return curView;
		}
		
		if (curShowChannelList != null && curChannel < curShowChannelList.size()) 
		{
			Calendar calendar = Calendar.getInstance();
			Date nowdate = calendar.getTime();
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
					"yyyyMMdd");
			int n = curDate - 3;
			Date start_date = new Date(nowdate.getTime() + n * 24 * 60 * 60 * 1000);
			String start = simpleDateFormat.format(start_date);
			curShowScheduleList = ScheduleCache.getReseeSchedule(curShowChannelList.get(curChannel)
					.getContentId(), start);
			if (curShowScheduleList == null) 
			{
				requestNewChannelSchedule(curShowChannelList.get(curChannel).getContentId(),getThatDay());
			}else {
//				从缓存读取出节目单，进行配置流程
				dealWithScheduleList();
				if(curShowScheduleList.size() == 0 || curShowChannelList.size() == 0)
				{
					if(lv_backplay_schedule != null)
					{
						lv_backplay_schedule.clearDrawList();
					}
				}
				int index = reOrderSchedule_list();
				LogUtil.e(tag, "updateData","当前的节目单焦点是：" + index);
				reloadScheduleListView(index);
			}
		}else {
			System.err.println("reqest channel error outofarray-->curChannel:" + curChannel);
		}
		
		
		return curView;
	}


	/**
	 * 
	 * function: 获得那天的完整24小时 
	 * @param
	 * @return
	 */
	private String[] getThatDay() 
	{
		if ( Config.smartProgramStartTime == null || Config.smartProgramStartTime.equals("")) 
		{
			return null;
		}
		
		String tempTime = Config.smartProgramStartTime.substring(0, 8);
		tempTime = tempTime.substring(0, 4) + "-" + tempTime.substring(4, 6) + "-" + tempTime.substring(6);
//		tempTime = "2014-07-24";
		String[] temp = new String[2];
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd000000");
		Date thatDate = DateUtil.parseDate(tempTime, "yyyy-MM-dd");
		String startDateTime = formatter.format(thatDate);
		String endDateTime = formatter.format(thatDate.getTime() + 24 * 60 * 60
				* 1000);
		Calendar calendar = Calendar.getInstance();
		Date nowdate = calendar.getTime();
		
		int minusRes = (int) (nowdate.getTime()-thatDate.getTime());
		minusRes = minusRes / 1000 / 60 / 60 / 24;
		curDate = curDate - minusRes;
		if (curDate < 0) 
		{
			curDate = 3;
			curSchedule = 4;
			return null;
		}
		temp[0] = startDateTime;
		temp[1] = endDateTime;
		System.out.println("######:" + "remote: temp[0] = " + temp[0] + " temp[1] = " + temp[1]);
		return temp;
	}
	


	//---为什么2个线程不 合并一个线程
	private void initParam()
	{
		if (VarParam.url.contains("tsnm")) 
		{
			SystemProperties.set("media.amplayer.displast_frame", "1");
			SystemProperties.set("libplayer.livets.softdemux", "0");
		}
		curShowChannelList = Var.allChannels;
		
		/**初始化变量**/
		curList = 1;
		isInPlayingMode=false;
		/** 监控发送刷新节目单的请求**/
		Thread thread = new Thread(myRunnable);
		thread.start();
		
	}
	
	
	/** 初始化view **/
	public void initViews() 
	{
		/** 绑定日期列表 **/
		lv_backplay_date = (ListView) curView.findViewById(R.id.lv_backplay_date);
		lv_backplay_date.setDivider(null);
		/** 绑定主界面 **/
		rl_backplay_main = (RelativeLayout) curView.findViewById(R.id.rl_backplay_main);
		/** 绑定右下角缓冲圈 **/
		faaImage = (ImageView) curView.findViewById(R.id.faa);
		/** 绑定播放银幕 **/
		surfaceView = (ArcSurfaceView) curView.findViewById(R.id.VideoView);
		/** 绑定播放暂停图标 **/
		iv_pause_play = (ImageView) curView.findViewById(R.id.iv_pause_play);
		/** 绑定播放进度条 **/
		process_seekBar = (SeekBar) curView.findViewById(R.id.sb_process);
		/** 绑定进度条中的当前时间 **/
		tv_curPosition = (TextView) curView.findViewById(R.id.tv_backplay_curtime);
		/** 绑定进度条的总时间 **/
		tv_allTime = (TextView) curView.findViewById(R.id.tv_backplay_alltime);
		/**绑定播放进度条**/
		rl_backplay_process = (RelativeLayout)curView.findViewById(R.id.rl_backplay_process);
		/**绑定回看时左下角文字**/
		tv_backplaying_progame = (AlwaysMarqueeTextView)curView.findViewById(R.id.tv_backplaying_programe);
		/**隐藏暂停图标**/
		iv_pause_play.setVisibility(View.INVISIBLE);
		/**隐藏播放进度条**/
		rl_backplay_process.setVisibility(View.GONE);
		/** 右上角 **/
		tv_backplay_icon = (TextView) curView.findViewById(R.id.tv_backplay_icon);
		curShowScheduleList = new ArrayList<Schedule>();
		rl_backplay_process = (RelativeLayout)curView.findViewById(R.id.rl_backplay_process);

		lv_backplay_channel = (ViewReSeeChannelList) curView.findViewById(R.id.lv_backplay_channel);
		/** 初始化节目单 **/
		initSchedule();
	}
	
	/**初始化节目单**/
	private void initSchedule() 
	{
		lv_backplay_schedule = (ViewReSeeSchedulelList) curView.findViewById(R.id.lv_backplay_schedule);
		lv_backplay_schedule.setFocus(false);
		
	}

	/** 初始化日期列表 **/
	@SuppressLint("SimpleDateFormat")
	private void initDateList() 
	{
		curShowDateList = new ArrayList<BackplayDate>();
		SimpleDateFormat formatter = new SimpleDateFormat("M月d日");
		Calendar calendar = Calendar.getInstance();
		Date nowdate = calendar.getTime();
		String nowWeekDay = "";
		// 当前日期
		String nowMonthDay = formatter.format(nowdate);
		calendar.setTime(nowdate);
		int dayIndex = calendar.get(Calendar.DAY_OF_WEEK);
		if(dayIndex >= 1 && dayIndex <= WEEK.length)
		{
			nowWeekDay = WEEK[dayIndex - 1];
		}
		/**插入前三天**/
		insertLastThreeDays(nowdate,formatter,calendar,dayIndex);
		/** 插入今天 **/
		curShowDateList.add(new BackplayDate(nowMonthDay, nowWeekDay));
		/** 插入后三天 **/
		insertNextThreeDays(nowdate,formatter,calendar,dayIndex);
		backplayDateAdapter = new BackplayDateAdapter(context);
		lv_backplay_date.setAdapter(backplayDateAdapter);
		reloadDateListView();
		
	}
	
	/** 插入后三天 **/
	private void insertNextThreeDays(Date nowdate, SimpleDateFormat formatter,
			Calendar calendar, int dayIndex) 
	{
		for (int i = 1; i < 4; i++) 
		{
			BackplayDate tempDate = new BackplayDate();
			Date before_date = new Date(nowdate.getTime() + i * 24 * 60 * 60
					* 1000);
			String str_date = formatter.format(before_date);
			calendar.setTime(before_date);
			dayIndex = calendar.get(Calendar.DAY_OF_WEEK);
			String temp_weekday = WEEK[dayIndex - 1];
			tempDate.setMonthDay(str_date);
			tempDate.setWeekday(temp_weekday);
			tempDate.setFuture(true);
			curShowDateList.add(tempDate);
		}
		
	}

	/**插入前三天**/
	private void insertLastThreeDays(Date nowdate, SimpleDateFormat formatter,
			Calendar calendar, int dayIndex) 
	{
		for (int i = 3; i > 0; i--) 
		{
			BackplayDate tempDate = new BackplayDate();
			Date before_date = new Date(nowdate.getTime() - i * 24 * 60 * 60
					* 1000);
			String str_date = formatter.format(before_date);
			calendar.setTime(before_date);
			dayIndex = calendar.get(Calendar.DAY_OF_WEEK);
			String temp_weekday = WEEK[dayIndex - 1];
			tempDate.setMonthDay(str_date);
			tempDate.setWeekday(temp_weekday);
			curShowDateList.add(tempDate);
		}
	}

	/** 重构日期列表 **/
	private void reloadDateListView() 
	{
		backplayDateAdapter.removeAll();
		replaceDateList(0,Method.getScaleX(80),0,0,0);
		int size = Math.min(curShowDateList.size(), 7);
		for (int i = 0; i < size; i++) {
			backplayDateAdapter.addItem(curShowDateList.get(i));
		}
	}
	
	/**重新放置日期列表的位置**/
	private void replaceDateList(int left, int top, int right, int bottom,int index) 
	{
		RelativeLayout.LayoutParams rl = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		
		rl.setMargins(left, index * Method.getScaleX(56) + top, right,bottom);
		lv_backplay_date.setLayoutParams(rl);
	}

	/**
	 * 移动到下一个频道
	 */
	private void moveToNextChannel() 
	{
		curDate = 3;
		lv_backplay_channel.setCurFocusIndex(curChannel);
		lv_backplay_channel.keyDown_Down();
		curChannel = lv_backplay_channel.getCurFocusIndex();
		return;
	}

	/**
	 * 移动到上一个频道
	 */
	private void moveToLastChannel() 
	{
		curDate = 3;
		lv_backplay_channel.setCurFocusIndex(curChannel);
		lv_backplay_channel.keyDown_up();
		curChannel = lv_backplay_channel.getCurFocusIndex();
	}
	
	
	/**
	 * 移动到下一个节目
	 */
	private void moveToNextSchedule() 
	{
		if (curShowScheduleList == null || curSchedule >= curShowScheduleList.size()) 
		{
			return;
		}else {
			System.err.println("moveToNextSchedule array outofindex");
		}

		lv_backplay_schedule.setCurFocusIndex(curSchedule);
		lv_backplay_schedule.keyDown_Down();
		curSchedule = lv_backplay_schedule.getCurFocusIndex();
		return;
	}
	
	/**
	 * 移动到上一个节目
	 */
	private void moveToLastSchedule() 
	{
		lv_backplay_schedule.setCurFocusIndex(curSchedule);
		lv_backplay_schedule.keyDown_up();
		curSchedule = lv_backplay_schedule.getCurFocusIndex();
	}
	
	/**
	 * 移动到下一个日期
	 */
	private void moveToNextDate() 
	{
		if (curShowDateList == null || curShowDateList.size() == 0) 
		{
			return;
		}
		curDate += 1;
		if (curDate > curShowDateList.size() - 1) 
		{
			curDate--;
			return;
		}
		int index = 3 - curDate;	
		int start = 0;
		int end = 0;
		focus = 0;
		if (index >= 0) 
		{
			replaceDateList(0, Method.getScaleX(80), 0, 0, index);
			start = 0;
			end = Math.min(7 - index, curShowDateList.size());
			focus = curDate;
		} else {
			start = Math.abs(index);
			end = curShowDateList.size();
			focus = 3;
		}
		
		backplayDateAdapter.removeAll();
		backplayDateAdapter.setFocusIndex(focus);
		for (int i = start; i < end; i++) 
		{
			backplayDateAdapter.addItem(curShowDateList.get(i));
		}
	}

	
	
	/**重新调整日期列表的位置**/
	private void replaceScheduleList(int left, int top, int right, int bottom, int index) 
	{
				RelativeLayout.LayoutParams rl = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				rl.setMargins(left, index * 57 + top, right, bottom);
				lv_backplay_schedule.setLayoutParams(rl);
	}



	/**
	 * 移动到上一个日期
	 */
	private void moveToLastDate() 
	{
		if (curShowDateList == null || curShowDateList.size() == 0) 
		{
			return;
		}
		curDate -= 1;
		if (curDate < 0) 
		{
			curDate = 0;
			return;
		}
		int index = 3 - curDate;
		int start = 0;
		int end = 0;
		focus = 0;
		if (index >= 0) 
		{// 移动位置
			replaceDateList(0, Method.getScaleX(80), 0, 0, index);
			start = 0;
			end = 7 - index;
			focus = curDate;
		} else 
		{
			start = Math.abs(index);
			end = curShowDateList.size();
			focus = 3;
		}
		backplayDateAdapter.removeAll();
		backplayDateAdapter.setFocusIndex(focus);
		for (int i = start; i < end; i++) 
		{
			backplayDateAdapter.addItem(curShowDateList.get(i));
		}

	}

	
	
	/**重新配置节目列表的位置**/
	private void resetScheduleLayout()
	{
		replaceScheduleList(0, 25, 0, 60, 0);
	}
	
	

	
	@Override
	public void onStop() 
	{
		
		showFrameAnimation(false);
		
		if (arcPlayControl != null) 
		{
			LogUtil.e(tag, "onStop","****************");
			arcPlayControl.stop();
			arcPlayControl = null;
		}
	
		if (!fromLivePlay) 
		{
			LogUtil.e(tag,"onStop","执行了将最后一帧设置回去的操作");
			if (VarParam.url.contains("tsnm")) 
			{
				SystemProperties.set("media.amplayer.displast_frame", "0");
				SystemProperties.set("libplayer.livets.softdemux", "1");
			}
		}
		
		super.onStop();
		/*if(!isJumpTo)
		{
			getActivity().finish();
		}*/
	}
	private boolean isNeedRefresh = true;
	public boolean onKeyDown(int keyCode, KeyEvent event) 
	{
		LogUtil.e(tag,"onKeyDown", "按键按下，键值是：" + keyCode);
		
		if (keyCode == 176) //中兴机顶盒设置键 
		{
			if (arcPlayControl != null) 
			{
				arcPlayControl.stop();
				arcPlayControl = null;
			}
			
//			fromLivePlay = false;
			onStop();
			return true;
		}


//			if (rl_backplay_main.getVisibility() == View.VISIBLE) //主界面显示时
//			{
//				if(keyCode == KeyEvent.KEYCODE_DPAD_UP || keyCode == KeyEvent.KEYCODE_DPAD_DOWN) //
//				{
//
//				}
//			}

		
		if (keyCode == KeyEvent.KEYCODE_DPAD_UP
				|| keyCode == KeyEvent.KEYCODE_DPAD_DOWN) 
		{
			if (rl_backplay_main.getVisibility() == View.GONE)
			{
				return true;
			}
			onKeyDownChangeChannel(keyCode);
			return true;
		}
		
		if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT
				|| keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) 
		{
			if (rl_backplay_main.getVisibility() == View.GONE)
			{
				if (isAlreadyPlayed) 
				{
					curVolView.setVisibility(View.GONE);
//					if (isNeedRefresh) 
//					{
//						updateProcessSeekBar(surfaceView.getCurrentPosition(), surfaceView.getDuration());
//						isNeedRefresh
//					}
					/**启动快速跳转**/
					startSeekTo(keyCode);
				}
					return true;
			}
			moveToOtherList(keyCode);
			return true;
		}
		
		if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) 
		{
			/**按下了center键**/
			pressCenter();
			return true;
		}
		
		if (keyCode == KeyEvent.KEYCODE_MUTE || keyCode == KeyEvent.KEYCODE_VOLUME_MUTE) 
		{
			/**按下了静音键**/
			pressMute();
			return super.onKeyDown(keyCode, event);
		}
		
		if (keyCode == KeyEvent.KEYCODE_VOLUME_UP
				|| keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) 
		{
			/**按下了音量上键或下键**/
			pressVolUpAndDown(keyCode);
			return true;
		} 
		
		return true;
	}
	
	@Override
	public boolean onkeyDownReturn() {
		pressBack();
		return super.onkeyDownReturn();
	}
	
	/**播放跳转
	 * 步骤：
	 * 1.显示播放进度条
	 * 2.调整播放时间的位置
	 * 3.计算要跳转的位置
	 * 4.跳转
	 * 5.播放进度条消失
	 * **/
	private long lastSeekTime = 0;
	
	private void startSeekTo(int keyCode) 
	{
		System.out.println("############ 当前播放位置是：" + surfaceView.getCurrentPosition());
		
		if (process_seekBar.getProgress() == process_seekBar.getMax()) 
		{
			playfinished();
			return;
		}
		
		if (jumpStepTime < 0) 
		{
			jumpStepTime = System.currentTimeMillis();	
		}
		
		
		
		if ((System.currentTimeMillis() - lastSeekTime) > (10 * 1000)) 
		{
			if(curSchedule < curShowChannelList.size() && StringUtil.isNumber(curShowScheduleList.get(curSchedule).getDuration()))
			{
				updateProcessSeekBar(surfaceView.getCurrentPosition(), surfaceView.getDuration());
			}
		}
		
		lastSeekTime = System.currentTimeMillis();
		
//		LogUtil.e(tag, "startSeekTo","************进度条回看前的位置是：" + process_seekBar.getProgress());
		if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) 
		{
			uploadObject.setProgramOperateType(2);
			process_seekBar.setProgress(process_seekBar.getProgress() - jumpStep);
		}else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) 
		{
			uploadObject.setProgramOperateType(1);
			process_seekBar.setProgress(process_seekBar.getProgress() + jumpStep);
		}
//		surfaceView.stop();
//		LogUtil.e(tag, "startSeekTo","************进度条回看以后的位置是：" + process_seekBar.getProgress());
		LogUtil.e(tag, "startSeekTo","节目总时长为：" + surfaceView.getDuration());
		process_seekBar.setVisibility(View.VISIBLE);
		rl_backplay_process.setVisibility(View.VISIBLE);
		updateRefreshTime();
		updateRefreshProcessTime();
		
		if(curShowScheduleList != null && curSchedule < curShowScheduleList.size())
		{
			updateProcessSeekBar(process_seekBar.getProgress() * 1000, surfaceView.getDuration());
		}
		
//		continuePlay();
		return;
	}

	/**按下返回键**/
	private void pressBack() 
	{
		rl_backplay_process.setVisibility(View.GONE);
		iv_pause_play.setVisibility(View.GONE);
		curVolView.setVisibility(View.GONE);
		tv_backplay_icon.setVisibility(View.GONE);
		showFrameAnimation(false);
		if(iv_pause_play.getVisibility() == View.VISIBLE)
		{
			continuePlay();
			return;
		}
		rl_backplay_process.setVisibility(View.GONE);
		iv_pause_play.setVisibility(View.GONE);
		curVolView.setVisibility(View.GONE);
		tv_backplay_icon.setVisibility(View.GONE);
		showFrameAnimation(false);
//		RelativeLayout.LayoutParams rl = new RelativeLayout.LayoutParams(
//				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//		rl.setMargins(0, 120, 0, 0);
//		tv_curPosition.setLayoutParams(rl);

		//wangfangxu
		if (fromRecommend) {//来自移动首页

			if (rl_backplay_main.getVisibility() == View.VISIBLE)
			{
				iFragmentJump.jumpToFramgment(LaunchType.TYPE_RECOMMEND, 0);
				isJumpTo = true;
				XunfeiTools.getInstance().sendBackPlayBroadcast(context, false);
				XunfeiTools.getInstance().sendLiveStatusBroadcast(context, true);
			} else
			{
				rl_backplay_main.setVisibility(View.VISIBLE);
				rl_backplay_process.setVisibility(View.GONE);
				iv_pause_play.setVisibility(View.INVISIBLE);
				//				SystemProperties.set("media.amplayer.displast_frame", "0");
				//				SystemProperties.set("libplayer.livets.softdemux", "1");
				if (arcPlayControl != null)
				{
					uploadObject.setProgramOperateType(3);
					uploadObject.setProgramStatus(1);
					DataAnalyzer.creator().uploadResee(uploadObject);
					arcPlayControl.stop();
					arcPlayControl = null;
				}
				surfaceView.stop();
				stopCountDownOrdismissOrderDialog();
				//				moveCurSchedule();
			}
			return;
		}
		//wangfangxu


		if (!fromLivePlay)
		{
			if (rl_backplay_main.getVisibility() == View.VISIBLE) 
			{
//				SystemProperties.set("media.amplayer.displast_frame", "0");
				exitBy2Click();
			} else 
			{
				rl_backplay_main.setVisibility(View.VISIBLE);
				rl_backplay_process.setVisibility(View.GONE);
				iv_pause_play.setVisibility(View.INVISIBLE);
				
				if (arcPlayControl != null) 
				{
					uploadObject.setProgramOperateType(3);
					uploadObject.setProgramStatus(1);
					DataAnalyzer.creator().uploadResee(uploadObject);
					arcPlayControl.stop();
					arcPlayControl = null;
				}
				if (VarParam.url.contains("tsnm")) 
				{
					SystemProperties.set("media.amplayer.displast_frame", "0");
					SystemProperties.set("libplayer.livets.softdemux", "1");
				}
				surfaceView.stop();
				stopCountDownOrdismissOrderDialog();
//				moveCurSchedule();
			}
		} else 
		{
			if (rl_backplay_main.getVisibility() == View.VISIBLE) 
			{
				iFragmentJump.jumpToFramgment(LaunchType.TYPE_LIVE, 0);
				isJumpTo = true;
				XunfeiTools.getInstance().sendBackPlayBroadcast(context, false);
				XunfeiTools.getInstance().sendLiveStatusBroadcast(context, true);
			} else 
			{
				rl_backplay_main.setVisibility(View.VISIBLE);
				rl_backplay_process.setVisibility(View.GONE);
				iv_pause_play.setVisibility(View.INVISIBLE);
//				SystemProperties.set("media.amplayer.displast_frame", "0");
//				SystemProperties.set("libplayer.livets.softdemux", "1");
				if (arcPlayControl != null) 
				{
					uploadObject.setProgramOperateType(3);
					uploadObject.setProgramStatus(1);
					DataAnalyzer.creator().uploadResee(uploadObject);
					arcPlayControl.stop();
					arcPlayControl = null;
				}
				surfaceView.stop();
				stopCountDownOrdismissOrderDialog();
//				moveCurSchedule();
			}
		}
		return;
	}
	
	public void directJumpToLive()
	{
		rl_backplay_process.setVisibility(View.GONE);
		iv_pause_play.setVisibility(View.INVISIBLE);
		if (arcPlayControl != null) 
		{
			arcPlayControl.stop();
			arcPlayControl = null;
		}
		surfaceView.stop();
		iFragmentJump.jumpToFramgment(LaunchType.TYPE_LIVE, 0);
	}

	/**按下音量上键或下键**/
	private void pressVolUpAndDown(int keyCode) 
	{
//		System.out.println("fadfawefoawnovawnrvoaldfkawoelkfwolefailwrjgoawiejgoaiwfawfwae;ogiaegj");
		curVolView.setVisibility(View.VISIBLE);
		soundViewControl.onVoiceAboutKeyDown(keyCode);
		rl_backplay_process.setVisibility(View.GONE);
		iv_pause_play.setVisibility(View.INVISIBLE);
		updateRefreshTime();
	}

	
	private boolean isMuteKey = false;
	/**按下了静音键**/
	public void pressMute() 
	{
		isMuteKey = true;
		curVolView.setVisibility(View.GONE);
		soundViewControl.onVoiceAboutKeyDown(KeyEvent.KEYCODE_MUTE);
		updateRefreshTime();
	}

	/**按下了center键**/
	private void pressCenter() 
	{
		if (rl_backplay_main.getVisibility() == View.VISIBLE) 
		{
			if (curList == SCHEDULE) 
			{
				Log.e(tag, "播放开始");
				/**如果可以播放就播放**/
				playIfPossible();
				return;
			}
		} else if (surfaceView != null && !surfaceView.isPlaying()) 
		{
//			surfaceView.seekTo(10000);
			rl_backplay_process.setVisibility(View.GONE);
			uploadObject.setProgramOperateType(0);
			uploadObject.setProgramStatus(0);
			DataAnalyzer.creator().uploadResee(uploadObject);
			continuePlay();
			return;
		} else if (surfaceView != null && surfaceView.isPlaying()) 
		{
			pausePlay();
//			updateProcessSeekBar(surfaceView.getCurrentPosition(),surfaceView.getDuration());
			if(curShowScheduleList != null && curSchedule < curShowScheduleList.size())
			{
				LogUtil.e(tag, "pressCenter","节目时长为：" + surfaceView.getDuration());
				if (process_seekBar.getProgress() >= process_seekBar.getMax()) 
				{
					playfinished();
					return;
//					updateProcessSeekBar(Integer.parseInt(curShowScheduleList.get(curSchedule).getDuration()),Integer.parseInt(curShowScheduleList.get(curSchedule).getDuration()));
				}
				updateProcessSeekBar(surfaceView.getCurrentPosition(),surfaceView.getDuration());
			}
			return;
		}
		
	}

	/**如果可以播放，就播放**/
	private void playIfPossible()
	{
		if (curShowScheduleList == null || curSchedule >= curShowScheduleList.size()) 
		{
			return;
		}
		/** 计算是否能够播放 **/
		String timeStr = curShowScheduleList.get(curSchedule).getStartDate()
				+ curShowScheduleList.get(curSchedule)
						.getStartTime();
		long timeLong = Long.valueOf(timeStr);
		Calendar calendar = Calendar.getInstance();
		Date nowdate = calendar.getTime();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmm00");
		long nowTime = Long.valueOf(simpleDateFormat.format(nowdate));
		if (timeLong > nowTime || curShowScheduleList.get(curSchedule).getStatus().equals("1")) 
		{
			return;
		}
		rl_backplay_main.setVisibility(View.GONE);
		tv_backplay_icon.setVisibility(View.VISIBLE);
//		showFrameAnimation(true);
		if (VarParam.url.contains("tsnm")) 
		{
			SystemProperties.set("media.amplayer.displast_frame", "1");
		}
		isAlreadyPlayed = false;
		playVideo();
		return;
	}
	
	/**刷新当前时间**/
	private void updateRefreshTime() 
	{
		curTime = System.currentTimeMillis();
	}
	
	/**刷新左右键移动播放进度条的时间**/
	private void updateRefreshProcessTime()
	{
		timeKeyLeftRight = System.currentTimeMillis();
	}

	private Runnable myRunnable = new Runnable() 
	{

		@Override
		public void run() 
		{
			while (true) 
			{
//				LogUtil.i(tag, "System.currentTimeMillis() - timeKeyDownWay = " + t);
				if (jumpStepTime > 0 && System.currentTimeMillis() - jumpStepTime > 1000) 
				{
					Message msg = new Message();
					Bundle b = new Bundle();// 存放数据
					b.putString("msg", "9");
					msg.setData(b);
					myHandler.sendMessage(msg);
					jumpStepTime = -1;
				}
				if (timeKeyDownWay > 0
						&& System.currentTimeMillis() - timeKeyDownWay > 500)
				{
					Message msg = new Message();
					Bundle b = new Bundle();// 存放数据
					b.putString("msg", "2");
					msg.setData(b);
					myHandler.sendMessage(msg);
					timeKeyDownWay = -1;
				}
				if (timeKeyLeftRight > 0 
						&& System.currentTimeMillis() - timeKeyLeftRight > 700) {
					Message msg = new Message();
					Bundle b = new Bundle();// 存放数据
					b.putString("msg", "3");
					msg.setData(b);
					myHandler.sendMessage(msg);
					timeKeyLeftRight = -1;
				}
				
				if (curTime > 0
						&& System.currentTimeMillis() - curTime > 4000) 
				{
					Message msg = new Message();
					Bundle b = new Bundle();// 存放数据
					b.putString("msg", "4");
					msg.setData(b);
					myHandler.sendMessage(msg);
					curTime = -1;
				}
				
				try 
				{
					Thread.sleep(100);
				} catch (InterruptedException e) 
				{
					e.printStackTrace();
				}

			}
		}
	};

//	private Runnable volRunnable = new Runnable() 
//	{
//
//		@Override
//		public void run() 
//		{
//			while (true) 
//			{
//				if (curTime > 0
//						&& System.currentTimeMillis() - curTime > 4000) 
//				{
//					Message msg = new Message();
//					volHandler.sendMessage(msg);
//				}
//				try 
//				{
//					Thread.sleep(500);
//				} catch (InterruptedException e) 
//				{
//					e.printStackTrace();
//				}
//			}
//		}
//	};

//	private Handler volHandler = new Handler() 
//	{
//		public void dispatchMessage(android.os.Message msg) 
//		{
//			
//		};
//	};

	/**请求新的频道列表**/
	private void requestNewChannelSchedule(String contentId,String[] thatTime) 
	{
		String[] str_time = null;
		if (thatTime != null) 
		{
			str_time = thatTime;
		}else{
			str_time = getToday();
			Config.clear();
		}
		
		if (!isFromVoice) 
		{
			initDateList();
		}
		isFromVoice = false;

		GDHttpTools.getInstance().getLiveScheduleList(GDHttpTools.getInstance().getTag(),contentId,str_time[1],str_time[0],Var.userId,Var.userToken,gdiupdata,curChannel,curDate);
//		GetScheduleList req = new GetScheduleList();
//		req.setChannelIds(contentId);
//		req.setCount("200");
//		req.setUserId(Var.userId);
//		req.setUserToken(Var.userToken);
//		req.setStartDateTime(str_time[0]);
//		req.setEndDateTime(str_time[1]);
//		req.setOffset("0");
//		HttpGetScheduleList httpGetScheduleList = new HttpGetScheduleList(this,
//				req);
//		httpGetScheduleList.post();
	}

	/**
	 * 针对页面的操作，更新用户按键时的时间
	 */
	private void updateKeyChangeViewTime() 
	{
		timeKeyDownWay = System.currentTimeMillis();
	}

	/**
	 * 更新频道当前时间
	 */
	private void updateChannelViewInfo() 
	{
		updateKeyChangeViewTime();
	}

	/**
	 * 用户上下键切换频道
	 * 
	 * @param keyCode
	 */
	private void onKeyDownChangeChannel(int keyCode) 
	{
		if (keyCode == KeyEvent.KEYCODE_DPAD_UP) 
		{
			pressUp();
		}
		else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) 
		{
			pressDown();
		}
		System.out.println("日期："+curDate);
	}
	
	/**按下键**/
	private void pressDown() 
	{
		if (curList == CHANNEL) 
		{
			curDate = 3;
			focus = curDate;
			moveToNextChannel();
			updateKeyChangeViewTime();
		} 
		else if (curList == DATE) 
		{
			moveToNextDate();
			updateKeyChangeViewTime();
		} 
		else if (curList == SCHEDULE) 
		{
			moveToNextSchedule();
			updateKeyChangeViewTime();
		}
		updateChannelViewInfo();
	}

	/**按上键**/
	private void pressUp() 
	{
		timeKeyDownWay = System.currentTimeMillis();
		if (curShowChannelList == null || curShowChannelList.size() == 0) 
		{
			return;
		}
		if (curList == CHANNEL) 
		{
			curDate = 3;
			focus = curDate;
			moveToLastChannel();
			updateKeyChangeViewTime();
		} 
		else if (curList == DATE) 
		{
			moveToLastDate();
			updateKeyChangeViewTime();
		} 
		else if (curList == SCHEDULE) 
		{
			moveToLastSchedule();
			updateKeyChangeViewTime();
		}
		
		updateChannelViewInfo();
	}

	/** 暂停播放 **/
	private void pausePlay() 
	{
		try 
		{
			uploadObject.setProgramStatus(0);
			uploadObject.setProgramOperateType(3);
			DataAnalyzer.creator().uploadResee(uploadObject);
			curVolView.setVisibility(View.GONE);
			process_seekBar.setVisibility(View.VISIBLE);
			rl_backplay_process.setVisibility(View.VISIBLE);
			rl_backplay_process.bringToFront();
			iv_pause_play.setVisibility(View.VISIBLE);
			iv_pause_play.bringToFront();
			surfaceView.pause();
		} catch (Exception e) 
		{
			e.printStackTrace();
		}
	}

	/** 继续播放 **/
	private void continuePlay() 
	{
		if (null != arcPlayControl &&  arcPlayControl.bPrepared) 
		{
			try 
			{
				iv_pause_play.setVisibility(View.INVISIBLE);
				surfaceView.start();
			} catch (Exception e) 
			{
				e.printStackTrace();
			}
		}

	}

	/**播放**/
	private void playVideo() 
	{
//		System.out.println("abcdefgh: EPG的duration的时长：" +curShowScheduleList.get(curSchedule).getDuration());
		process_seekBar.setProgress(0);
		lastSeekTime = 0;
		setBackplayingText();

		///
		if (arcPlayControl != null) 
		{
			arcPlayControl.stop();
			arcPlayControl = null;
			if (lastSchedule != null) 
			{
				uploadObject.setProgramStatus(1);
				DataAnalyzer.creator().uploadResee(uploadObject);
			}
		}
		
		arcPlayControl = new ArcPlayControl(context, surfaceView,
				new IPlayControl() 
		{
					@Override
					public void isBufferData(boolean isBuffer) 
						{
							System.out.println("视频缓冲状态："+isBuffer);
							showFrameAnimation(isBuffer);
						}
		});
		
		arcPlayControl.setiPlayFinished(this);
		arcPlayControl.setOnCompleteListener();

//		lastAllTime = 0;
		
//		LogUtil.e(tag,"playVideo", "&&&&&&&&&&&&&&:lastAllTime:" + lastAllTime);
		
		lastSchedule = curShowScheduleList.get(curSchedule);
		
		uploadObject.setChannelId(curShowScheduleList.get(curSchedule).getChannelId());
		uploadObject.setAsssetId(curShowScheduleList.get(curSchedule).getContentId());
		uploadObject.setReseeStartTime(curShowScheduleList.get(curSchedule).getStartDate() + curShowScheduleList.get(curSchedule).getStartTime());
		String startTime = "";
		if (!AbStrUtil.isEmpty(curShowScheduleList.get(curSchedule).getStartDate()) && !AbStrUtil.isEmpty(curShowScheduleList.get(curSchedule).getStartTime())) 
		{
			startTime = curShowScheduleList.get(curSchedule).getStartDate()+curShowScheduleList.get(curSchedule).getStartTime();
		}
		String endTime = getPreTime(startTime, curShowScheduleList.get(curSchedule).getDuration());
		uploadObject.setReseeStartTime(startTime);
		uploadObject.setReseeEndTime(endTime);
		uploadObject.setProgramDuration(Integer.valueOf(curShowScheduleList.get(curSchedule).getDuration()));
		uploadObject.setProgramStatus(0);
		uploadObject.setProgramOperateType(0);
		uploadObject.setChannelName(curShowChannelList.get(curChannel).getName());
		uploadObject.setProgramName(curShowScheduleList.get(curSchedule).getProgramName());
		System.out.println("###回看时间：" + startTime + " !!!!! " + endTime);
		DataAnalyzer.creator().uploadResee(uploadObject);
//		http://119.164.58.177:80/88888888/16/20170717/269693985/1.m3u8
		initArcPlayTestData(curShowScheduleList.get(curSchedule).getPlayUrl(),
				curShowChannelList.get(curChannel).getContentId());
//		initArcPlayTestData("http://119.164.58.177:80/88888888/16/20170717/269693985/1.m3u8",
//				curShowChannelList.get(curChannel).getContentId());
		arcPlayControl.arcPlayer_play();
		isInPlayingMode=true;
		/// 试看
		curPlayingChannel=curShowChannelList.get(curChannel);
		if(curPlayingChannel!=null){
			stopCountDownOrdismissOrderDialog();
			Log.i("MARK","curPlayingChannel.getCountry()=="+curPlayingChannel.getCountry());

			if (Var.isZZEnabled&&curPlayingChannel.getCountry().equals("true"))  //当开启增值模块，并且要播放的这个频道是一个付费频道的时候，启动一个5分钟的定时器
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
		}


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
	/**
	* 时间前推或后推分钟,其中JJ表示分钟.
	*/
	public  String getPreTime(String sj1, String jj) {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		String mydate1 = "";
		try {
			Date date1 = format.parse(sj1);
			long Time = (date1.getTime() / 1000) + Integer.parseInt(jj);
			date1.setTime(Time * 1000);
			mydate1 = format.format(date1);
		} catch (Exception e) {
		}
		return mydate1;
	}
	


	/**初始化DRM设置**/
	private void initArcPlayTestData(String url, String channel) 
	{
		System.out.println("&&&url:" + url + ",channel:" + channel);
		arcPlayControl.setDeviceID(Var.mac);
		arcPlayControl.setDrmType(TYPE_DRM.P2P_NODRM);
		arcPlayControl.setProductid(Var.productId);
		arcPlayControl.setServiceID(channel);
		arcPlayControl.setTokenID(Var.userToken);
		arcPlayControl.setType(0);
		arcPlayControl.setUri(url);
		arcPlayControl.setUserID(Var.userId);
//		arcPlayControl.setDmrURL(Var.drm_domain_url);
	}

	/**请求新的日期列表**/
	private void requestNewDateSchedule(String contentId, String monthDay) 
	{ //TODO DD
		Calendar calendar = Calendar.getInstance();
		Date nowdate = calendar.getTime();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
				"yyyyMMdd000000");
		int n = curDate - 3;
		Date start_date = new Date(nowdate.getTime() + n * 24 * 60 * 60 * 1000);
		Date end_date = new Date(start_date.getTime() + 24 * 60 * 60 * 1000);
		String start = simpleDateFormat.format(start_date);
		String end = simpleDateFormat.format(end_date);
		GDHttpTools.getInstance().getLiveScheduleList(GDHttpTools.getInstance().getTag(),contentId,end,start,Var.userId,Var.userToken,gdiupdata,curChannel,curDate);
//		GetScheduleList req = new GetScheduleList();
//		req.setChannelIds(contentId);
//		req.setCount("200");
//		req.setOffset("0");
//		req.setStartDateTime(start);
//		req.setEndDateTime(end);
//		req.setUserId(Var.userId);
//		req.setUserToken(Var.userToken);
//		HttpGetScheduleList httpGetScheduleList = new HttpGetScheduleList(this,
//				req);
//		httpGetScheduleList.post();
	}
	private boolean isJumpReplay = false;


	/** 切换列表 **/
	private void moveToOtherList(int keyCode) 
	{
		if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) 
		{
			pressLeft();
		} 
		else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) 
		{
			pressRight();
		}
	}

	/**按右键**/
	private void pressRight() 
	{
		if(focus >= lv_backplay_date.getChildCount())
		{
			return;
		}
		
		switch (curList) 
		{
		case CHANNEL:
			
			lv_backplay_channel.setFocus(false);
			setDateFocusStyle(true);
			curList = DATE;
			break;
		case DATE:
			if (curShowScheduleList == null || curShowScheduleList.size() <= 0) 
			{
				return;
			}
			setDateFocusStyle(false);
			lv_backplay_schedule.setFocus(true);
			curList = SCHEDULE;
			break;
		case SCHEDULE:
			break;
		default:
			break;
		}
	}
	
	public void setDateFocusStyle(boolean flag)
	{
		if (flag) 
		{
			if (VarParam.default_url.contains("ysnm")) 
			{
				((LinearLayout) lv_backplay_date.getChildAt(focus)).setBackgroundResource(R.drawable.backplay_old_date_focus);
			}else {
				((LinearLayout) lv_backplay_date.getChildAt(focus)).setBackgroundResource(R.drawable.backplay_date_focus);
			}
		}else {
			if (VarParam.default_url.contains("ysnm")) 
			{
				((LinearLayout) lv_backplay_date.getChildAt(focus)).setBackgroundResource(R.drawable.backplay_old_date_unfocus);
			}else {
				((LinearLayout) lv_backplay_date.getChildAt(focus)).setBackgroundResource(R.drawable.backplay_date_unfocus);
			}
		}
	}
	
	/**按左键**/
	@SuppressLint("ResourceType")
	private void pressLeft()
	{
		if(focus >= lv_backplay_date.getChildCount())
		{
			return;
		}
		switch (curList) 
		{
		case CHANNEL:
			break;
		case DATE:
			lv_backplay_channel.setFocus(true);
			((LinearLayout) lv_backplay_date.getChildAt(focus))
					.setBackgroundResource(getResources().getColor(
							R.color.nocolor));
			curList = CHANNEL;
			break;
		case SCHEDULE:
			lv_backplay_schedule.setFocus(false);
			setDateFocusStyle(true);
			curList = DATE;
			break;
		default:
			break;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void updateData(String method, String uniId, Object object,
			boolean isSuccess) 
	{
		LogUtil.i(tag, "updateData","updateData start:--> method:" + method + ",uniId:" + uniId + "isSuccess:" + isSuccess);
		
		if (method.equals(HttpGetScheduleList.Method)) 
		{//获取节目单
			if (!isSuccess) 
			{
				ErrorCode.makeErrorToast(ErrorCode.ERROR_GET_SCHEDULE_LIST_EXCEPTION, context);
				return;
			}
			
			if(object == null)
			{
				return;
			} //TODO DD
			curShowScheduleList = (List<Schedule>) object;
			dealWithScheduleList();
			if(curShowScheduleList.size() == 0 || curShowChannelList.size() == 0)
			{
				if(lv_backplay_schedule != null)
				{
					lv_backplay_schedule.clearDrawList();
				}
				return;
			}
			if (curChannel < curShowChannelList.size() 
					&& !uniId.equals(curShowChannelList.get(curChannel).getContentId())) 
			{
				return;
			}
			
			Calendar calendar = Calendar.getInstance();
			Date nowdate = calendar.getTime();
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
					"yyyyMMdd");
			int n = curDate - 3;
			Date start_date = new Date(nowdate.getTime() + n * 24 * 60 * 60 * 1000);
			String start = simpleDateFormat.format(start_date);
			
			ScheduleCache.setReseeSchedule(curShowChannelList.get(curChannel).getContentId(), start, curShowScheduleList);
			
			int index = reOrderSchedule_list();
			LogUtil.e(tag, "updateData","当前的节目单焦点是：" + index);
			reloadScheduleListView(index);
//			if (isRemote) 
//			{
//				initRemotePlayParam();
//				if (curSchedule != -1) 
//				{
////					LogUtil.e(tag, "开始播放");
//					playIfPossible();
//					isRemote = false;
//				}
//				Config.clear();
//			}
		}
	}

	
	/**
	 * 
	 * function: 计算该播放哪个节目 
	 * @param
	 * @return
	 */
	private int calRemoteSchedule(String smartProgramStartTime) 
	{
//		int remoteDate = Integer.valueOf(smartProgramStartTime.substring(0, 7));
		int remoteTime = Integer.valueOf(smartProgramStartTime.substring(8,12));
		
		for (int i = 0; i < curShowScheduleList.size(); i++) 
		{
			int localTime = Integer.parseInt(curShowScheduleList.get(i).getStartTime().substring(0,curShowScheduleList.get(i).getStartTime().length() - 2));
			System.out.println("#######:@@@###:" + remoteTime + "    " + localTime);
			if (remoteTime >= localTime) 
			{
				continue;
			}else 
			{
				if (i - 1 >= 0) 
				{
					System.out.println("######:" + "要播放的节目是： " + curShowScheduleList.get(i-1).getProgramName());
					localTime = Integer.parseInt(curShowScheduleList.get(i-1).getStartTime().substring(0,curShowScheduleList.get(i-1).getStartTime().length() - 2));
					System.out.println("######:" + "remote: remoteTime = " + remoteTime + " localTime = " + localTime);
					return i-1;
				}else {
					remoteTime = 0;
					return 0;
				}
				
			}
		}
		
		return -1;
	}

	private int calSeekTime(int remoteTime, int localTime) 
	{	int rThh = remoteTime / 100;
		int rTmm = remoteTime % 1000;
		if (remoteTime < 1000) 
		{
			rTmm = remoteTime % 100;
		}
		int lThh = localTime / 100;
		
		int lTmm = localTime % 1000;
		if (localTime < 1000) {
			lTmm = localTime % 100;
		}
		
//		LogUtil.e(tag, "remote: " + rThh + " ," + rTmm + " ," + lThh + " , " + lTmm);
		
		int resMM = rTmm - lTmm;
		if (resMM < 0) 
		{
			resMM = 60 + rTmm - lTmm;
			rThh--;
		}
		int resHH = rThh - lThh;
//		LogUtil.e(tag, "remote: " + resHH + " , " + resMM);
		return resHH * 60 * 60 * 1000 + resMM * 60 * 1000;
	}

	 /**
	  * dealWithScheduleList(去除重复节目单)
	  *
	  * @Title: dealWithScheduleList
	  * @Description:     
	  * @return void    
	  */
	private void dealWithScheduleList() 
	{
//		int curDate = Integer.parseInt(getCurrentDate("yyyyMMdd"));
//		System.out.println("curdate = " + curDate);
//		if (curShowScheduleList != null && curShowScheduleList.size() > 0)
//		{
//			int date = Integer.parseInt(curShowScheduleList.get(0).getStartDate());
//			System.out.println("curdate = " + date);
//			if (curDate > date)
//			{
//				curShowScheduleList.remove(0);
//			}
//		}
				
	}
	
	/**
	 * 描述：获取表示当前日期时间的字符串.
	 * 
	 * @param format
	 *            格式化字符串，如："yyyy-MM-dd HH:mm:ss"
	 * @return String String类型的当前日期时间
	 */
	public static String getCurrentDate(String format)
	{
		String curDateTime = null;
		try
		{
			SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
			Calendar c = new GregorianCalendar();
			curDateTime = mSimpleDateFormat.format(c.getTime());
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return curDateTime;

	}


	/** 重新配置节目单列表 **/
	private int reOrderSchedule_list() 
	{
		// 当curDate=3的时候，代表日期是今天
		int index = 0;
		boolean isCurDate = false;
		
		if(curDate != 3)
		{
			if (curShowScheduleList.size() <= 10) 
			{
				index = 0;
			}else 
			{
				index = 4;
			}
			for (int i = 0; i < curShowScheduleList.size(); i++)
			{
				if(!curShowDateList.get(curDate).isFuture())
				{
					curShowScheduleList.get(i).setStatus("0");
				}else {
					curShowScheduleList.get(i).setStatus("1");
				}
			}
			return index;
		}else 
		{
			isCurDate = true;
			Date nowdate = Calendar.getInstance().getTime();
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
					"yyyyMMddHHmm00");
			long nowTime = Long.valueOf(simpleDateFormat.format(nowdate));
			
			boolean isFirst = false;
			for (int i = 0; i < curShowScheduleList.size(); i++) 
			{
				long tempTime = Long.valueOf(curShowScheduleList.get(i)
						.getStartDate()
						+ curShowScheduleList.get(i).getStartTime());
				if (nowTime < tempTime) 
				{
					curShowScheduleList.get(i).setStatus("1");
					if(!isFirst)
					{
						index = Math.max(i - 1, 0);
						isFirst = true;
					}
					
				}else 
				{
					curShowScheduleList.get(i).setStatus("0");
				}
			}
		}
		curSchedule = index;
		curShowScheduleList.get(curSchedule).setStatus("1");
		Schedule schedule = null;
		for(int i = 4;i >= 0;i--)
		{
			int tt = index - i;
			if(tt >= 0 && tt < curShowScheduleList.size())
			{
				schedule = curShowScheduleList.get(tt);
				if(i == 0 && isCurDate)
				{
					schedule.setCurrent(true);
				}else 
				{
					schedule.setCurrent(false);
				}
				
				if(i== 0 && curList == SCHEDULE)
				{
					schedule.setFocus(true);
				}else 
				{
					schedule.setFocus(false);
				}
			}
		}
		
		for(int i = 1; i <= 5;i++)
		{
			int tt = index + i;
			if(tt < curShowScheduleList.size())
			{
				schedule = curShowScheduleList.get(tt);
				schedule.setCurrent(false);
			}
		}
		return index;
	}

	/** 获得今天完整的24小时 **/
	private String[] getToday() 
	{
		String[] temp = new String[2];
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd000000");
		Calendar calendar = Calendar.getInstance();
		Date nowdate = calendar.getTime();
		String startDateTime = formatter.format(nowdate);
		String endDateTime = formatter.format(nowdate.getTime() + 24 * 60 * 60
				* 1000);
		temp[0] = startDateTime;
		temp[1] = endDateTime;
		return temp;
	}

	/** 重新加载频道列表 **/
	public void reloadChannelListView(int index) 
	{
		lv_backplay_channel.setCurFocusIndex(index);
		
		lv_backplay_channel.setChannlesInCategary(curShowChannelList);
		
		curChannel = lv_backplay_channel.getCurFocusIndex();
		/* 初始化日期列表 */
		initDateList();
	}

	/** 重新加载节目单列表 **/
	private void reloadScheduleListView(int index) 
	{
		lv_backplay_schedule.setCurFocusIndex(index);
		lv_backplay_schedule.setSchedules(curShowScheduleList);
		curSchedule = lv_backplay_schedule.getCurFocusIndex();
	}
	
	/**
	 * 是否播放视频资源加载动画
	 * 
	 * @param isShow
	 */
	private void showFrameAnimation(boolean isShow) 
	{
		if (isShow) 
		{
			if (faaImage != null) 
			{
				faaImage.setVisibility(View.GONE); 
				faaImage.setVisibility(View.VISIBLE);
				faaImage.bringToFront();
				mLoadingView = (AnimationDrawable) faaImage.getBackground();
			}
			if (mLoadingView != null)
				mLoadingView.start();
		} else 
		{
			isAlreadyPlayed = true;
			if (mLoadingView != null) 
			{
				mLoadingView.stop();
			}
			if (faaImage != null) 
			{
				faaImage.setVisibility(View.GONE);
			}

		}
	}


	/** 更新播放进度条 **/
	private void updateProcessSeekBar(int currentPosition, int duration) 
	{	
//		LogUtil.e(tag, "updateProcessSeekBar","curChanne:" + curShowScheduleList.get(curSchedule).toString());
//		LogUtil.e(tag, "updateProcessSeekBar","updateProcessSeekBar -->当前位置是：" + (currentPosition /1000) + " , EPG给出总时长是： " + duration + " 播放器取得总时长是： " + surfaceView.getDuration() + " ,lastAllTime:" + lastAllTime);
		LogUtil.e(tag, "updateProcessSeekBar","updateProcessSeekBar -->当前位置是：" + (currentPosition /1000) + " , EPG给出总时长是： " + duration + " 播放器取得总时长是： " + surfaceView.getDuration());
		System.out.println("abcdefgh: 播放器的getDuration()时长是：" +surfaceView.getDuration());
		if (currentPosition < 0 || duration <= 0) 
		{
			return;
		}
//		int curSecond = currentPosition / 1000 - lastAllTime;
		int curSecond = currentPosition / 1000;
		int durationTime = duration / 1000;
		
//		while (curSecond > durationTime) 
//		{
//			curSecond = curSecond - durationTime;
//			lastAllTime += durationTime;
//			curSchedule ++;
//			if(curSchedule < curShowScheduleList.size())
//			{
//				durationTime = Integer.parseInt(curShowScheduleList.get(curSchedule).getDuration());
//			}else {
//				curSchedule = curShowScheduleList.size() - 1;
//			}
//		}
		
		LogUtil.e(tag, "updateProcessSeekBar","updateProcessSeekBar -->修正后当前位置是：" + curSecond + " , 总时长是： " + durationTime);
		
		process_seekBar.setMax(durationTime);
		int hh = durationTime / 3600;
		int mm = (durationTime - hh * 3600) / 60;
		int ss = durationTime - hh * 3600 - mm * 60;
		String allTime = getHHMMSS(hh + "", mm + "", ss + "");
//		LogUtil.e(tag, "updateProcessSeekBar","hh:" + hh + ",mm:" + mm + ",ss:" + ss);
		tv_allTime.setText(allTime);
		int curhh = curSecond / 3600;
		int curmm = (curSecond - curhh * 3600) / 60;
		int curss = curSecond - curhh * 3600 - curmm * 60;
		String curTime = getHHMMSS(curhh + "", curmm + "", curss + "");
		
		int marginLeft = curSecond * 1000 / durationTime;
		if(marginLeft < 0)
		{
//			LogUtil.e(tag, "updateProcessSeekBar","updateProcessSeekBar -->" + marginLeft);
		}
		process_seekBar.setProgress(curSecond);
		tv_curPosition.setText(curTime);
		Log.e(tag, "总时长为：" + allTime + " 当前播放为：" + curTime);
//		Log.e(tag, "控件移动为：" + marginLeft);
	}

	/** 拼凑格式为HH:MM:SS的时间字符串 **/
	private String getHHMMSS(String hh, String mm, String ss) 
	{
		if (hh.length() < 2) 
		{
			hh = "0" + hh;
		}
		if (mm.length() < 2) 
		{
			mm = "0" + mm;
		}
		if (ss.length() < 2) 
		{
			ss = "0" + ss;
		}
		return hh + ":" + mm + ":" + ss;
	}
	
	/**
	 * 2014年5月4日，广电要求更换播控图片
	 * 
	 * **/
	AlwaysMarqueeTextView tv_backplaying_progame = null;
	
	/**重新设置左下角正在会看的节目名称**/
	private void setBackplayingText() 
	{
		tv_backplaying_progame.setText(curShowScheduleList.get(curSchedule).getProgramName().toString().trim());
	}

	@Override
	public void playfinished() 
	{
		rl_backplay_main.setVisibility(View.VISIBLE);
		rl_backplay_process.setVisibility(View.GONE);
		iv_pause_play.setVisibility(View.INVISIBLE);
		showFrameAnimation(false);
		tv_backplay_icon.setVisibility(View.INVISIBLE);
		
		SystemProperties.set("media.amplayer.displast_frame", "0");
		SystemProperties.set("libplayer.livets.softdemux", "1");
		uploadObject.setProgramStatus(1);
		DataAnalyzer.creator().uploadResee(uploadObject);
		surfaceView.stop();
	}
	
	/**加上这个变量，在播放出回看节目之前，禁止seek，播放出回看节目后允许时移**/
	private boolean isAlreadyPlayed = false;  
	
	
	private Handler myHandler = new Handler() 
	{
		public void handleMessage(Message msg) 
		{
			
			Bundle b = msg.getData();
			String info = b.getString("msg");
			LogUtil.i("MARK", "handleMessage ","myHandler -->msg:" + info);
//			LogUtil.e(tag, "received msg = " + info);
			if (info.equals("2")) //切换频道,请求节目单
			{
				dealWithMessageTwo();
				return;
			}
			
			if (info.equals("3")) 
			{
				dealWithMessageThree();
				return;
			}
			
			if (info.equals("4")) 
			{
				dealWithMessageFour();
				return;
			}
			if (info.equals("9")) 
			{
				dealWithMessageNine();
				return;
			}
		}
	};
	

	
	private void dealWithMessageNine() 
	{
		if (jumpStep > 60 * 10) 
		{
			return;
		}
		jumpStep = jumpStep * 2;
	}

	private void dealWithMessageFour() 
	{
		curVolView.setVisibility(View.GONE);
		if (iv_pause_play.getVisibility() != View.VISIBLE) 
		{
			rl_backplay_process.setVisibility(View.GONE);
		}
		
	}

	private void dealWithMessageThree() 
	{
		LogUtil.e(tag, "dealWithMessageThree","**********发送回看跳转请求*************");
		jumpStep = 10;
		jumpStepTime = -1;
		if (process_seekBar.getProgress() == process_seekBar.getMax()) 
		{
			playfinished();
			return;
		}
		DataAnalyzer.creator().uploadResee(uploadObject);
		surfaceView.seekTo(process_seekBar.getProgress() * 1000);
		if(curShowScheduleList != null && curSchedule < curShowScheduleList.size())
		{
			updateProcessSeekBar(surfaceView.getCurrentPosition(), 
					surfaceView.getDuration());
		}
	}
	
	private void requestScheduleInChannelList(){
		/* 重新请求新频道当天的节目单 */
		if (curShowChannelList != null && curChannel < curShowChannelList.size()) 
		{
			//先清除上个频道的节目单
			if(lv_backplay_schedule != null)
			{
				lv_backplay_schedule.clearDrawList();
			}
			//先清除上个频道的节目单
			Calendar calendar = Calendar.getInstance();
			Date nowdate = calendar.getTime();
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
					"yyyyMMdd");
			int n = curDate - 3;
			Date start_date = new Date(nowdate.getTime() + n * 24 * 60 * 60 * 1000);
			String start = simpleDateFormat.format(start_date);
			curShowScheduleList = ScheduleCache.getReseeSchedule(curShowChannelList.get(curChannel)
					.getContentId(), start);
			if (curShowScheduleList == null) 
			{
				requestNewChannelSchedule(curShowChannelList.get(curChannel).getContentId(),null);
			}else {
//				从缓存读取出节目单，进行配置流程
				dealWithScheduleList();
				if(curShowScheduleList.size() == 0 || curShowChannelList.size() == 0)
				{
					if(lv_backplay_schedule != null)
					{
						lv_backplay_schedule.clearDrawList();
					}
					return;
				}
				int index = reOrderSchedule_list();
				LogUtil.e(tag, "updateData","当前的节目单焦点是：" + index);
				reloadScheduleListView(index);
			}
		}else {
			LogUtil.e(tag, "dealWithMessageTwo()", "request channel error");
		}
	}
	
	private void requestScheduleInDateList(){
		/* 请求某天的节目单 */
		if (curShowChannelList != null && curChannel < curShowChannelList.size()) 
		{
			/*//先清除上个date频道的节目单
			if(lv_backplay_schedule != null)
			{
				lv_backplay_schedule.clearDrawList();
			}
			//先清除上个上个date的节目单*/
			Calendar calendar = Calendar.getInstance();
			Date nowdate = calendar.getTime();
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
					"yyyyMMdd");
			int n = curDate - 3;
			Date start_date = new Date(nowdate.getTime() + n * 24 * 60 * 60 * 1000);
			String start = simpleDateFormat.format(start_date);
			curShowScheduleList = ScheduleCache.getReseeSchedule(curShowChannelList.get(curChannel)
					.getContentId(), start);
			if (curShowScheduleList == null) 
			{
				requestScheduleTimes = 0;
				requestNewDateSchedule(curShowChannelList.get(curChannel)
						.getContentId(), curShowDateList.get(curDate)
						.getMonthDay());
			}else {
//				从缓存读取出节目单，进行配置流程
				dealWithScheduleList();
				if(curShowScheduleList.size() == 0 || curShowChannelList.size() == 0)
				{
					if(lv_backplay_schedule != null)
					{
						lv_backplay_schedule.clearDrawList();
					}
					return;
				}
				int index = reOrderSchedule_list();
				LogUtil.e(tag, "updateData","当前的节目单焦点是：" + index);
				System.out.println("A:"+curShowScheduleList.get(0).getStartDate() + " B:" + start);
				if (curShowScheduleList.get(0).getStartDate().contains(start)){ //在实际配置列表之前，再进行一次判断，比对日期是否一致
					requestScheduleTimes=0;
					reloadScheduleListView(index);
				}else{
					//否则的话重新执行一次，但要注意不要构成死循环
					if (requestScheduleTimes>=1){
						if(lv_backplay_schedule != null)
						{
							lv_backplay_schedule.clearDrawList();
						}
						requestScheduleTimes=0;
						return;
					}
					requestScheduleTimes++;
					requestScheduleInDateList();
				}
			}
		}else {
			LogUtil.e(tag, "dealWithMessageTwo()", "request channel error");
		}
	}
	private int requestScheduleTimes = 0;
	private void dealWithMessageTwo() 
	{
		// 根据curList判断发送什么请求
		if (curList == CHANNEL) 
		{
			requestScheduleInChannelList();
		} 
		else if (curList == DATE) 
		{
			requestScheduleInDateList();
		}
	}
	
/***************远程调用部分********************/
	
	private MyRemoteReveiver myReceiver = null;
	
	public class MyRemoteReveiver extends BroadcastReceiver
	{
		@Override
		public void onReceive(Context context, Intent intent) 
		{
			Log.e("stbse broadcast", System.currentTimeMillis()+"");
			int type = intent.getIntExtra("type", -1);
			Config.smartTvChannelName = intent.getStringExtra("name");
			Config.smartProgramStartTime = intent.getStringExtra("time");
			Config.smartProgramStartTime = Config.smartProgramStartTime.replace("-", "").replace(" ", "").replace(":", "");
			if (type == 0) 
			{
				//跳直播
				 Config.smartTvChannelName = intent.getStringExtra("name");
				 fromLivePlay = true;
				 if (surfaceView != null) 
				 {
					surfaceView.stop();
				 }
				 curTime = -1;
				 iFragmentJump.jumpToFramgment(0, 0);
				 return;
			}else
			{
				//回看跳回看
				/*1. 查看是否播放 X
				 *2. 停止播放器   X
				 *3. 请求新节目单
				 *4. 计算该播放哪个节目
				 *5. 重新播放
				 *
				 * */
				
				if (arcPlayControl != null) 
				{
					arcPlayControl.stop();
					arcPlayControl = null;
				}
				
				rl_backplay_main.setVisibility(View.VISIBLE);
				tv_backplay_icon.setVisibility(View.GONE);
				curVolView.setVisibility(View.GONE);
				if (iv_pause_play.getVisibility() != View.VISIBLE) 
				{
					rl_backplay_process.setVisibility(View.GONE);
				}
				
				curDate = 3;
				curSchedule = 4;
				curChannel = calIndex(Config.smartTvChannelName);
				System.out.println("###: " + "remote: curChannel = " + curChannel);
				System.out.println("###: " + "remote: time = " + Config.smartProgramStartTime);
				fromLivePlay = false;
				
				reloadChannelListView(curChannel);
				
				requestNewChannelSchedule(curShowChannelList.get(curChannel).getContentId(),getThatDay());
				
			}
			
		}
	}
	
	@Override
	public void onDestroyView() 
	{
		super.onDestroyView();
		if (myReceiver != null) 
		{
			getActivity().unregisterReceiver(myReceiver);
			myReceiver = null;
		}
	}
	
	
	private void regRemoteReceiver() 
	{
		myReceiver = new MyRemoteReveiver();
		IntentFilter filter = new IntentFilter(Config.BROADCAST_REMOTE_TV_PROGRAM);
		getActivity().registerReceiver(myReceiver, filter);
	}


	@Override
	public void goToNextChannel() {
		
	}


	@Override
	public void goToLastChannel() {
		
	}


	@Override
	public void goToLive() {
		
	}


	@Override
	public void goToChannel(String num, String channelName) 
	{
		String channel2 = "";
		String channel3 = "";
		
		for (ContentChannel channel : Var.allChannels) 
		{
			if (channel.getChannelNumber().length() == 2) 
			{
				channel3 = "0"+channel.getChannelNumber();
			}else if (channel.getChannelNumber().length() ==1 ) 
			{
				channel2 = "00"+channel.getChannelNumber();
			}
			
			if (channel.getChannelNumber().equals(num)||channel2.equals(num)||channel3.equals(num)) 
			{
				saveChannelInfo(channel.getName());
				if (arcPlayControl != null) 
				{
					arcPlayControl.stop();
				}
			
				iFragmentJump.jumpToFramgment(LaunchType.TYPE_LIVE, 0);
				isJumpTo = true;
				break;
			}
		}	
	}


	@Override
	public void goToTvBack() 
	{
		
	}


	@Override
	public void goToSchedule(String num, String name, String starttime, String startdate, String endtime,
			String enddate) 
	{
		isFromVoice = true;
		//1.只有频道没有时间
		if (AbStrUtil.isEmpty(starttime)&&!AbStrUtil.isEmpty(num) && AbStrUtil.isNumber(num)) 
		{
			int index = Var.getChannelIndexByNum(num);
			if (index>0) 
			{
				lv_backplay_channel.setCurFocusIndex(index);
				lv_backplay_channel.setChannlesInCategary(curShowChannelList);
				curChannel = lv_backplay_channel.getCurFocusIndex();
				curDate=3;
				reloadDateListView();
				if (curList == SCHEDULE) 
				{
					myHandler.postDelayed(new Runnable() {
						
						@Override
						public void run() {
							setDateFocusStyle(false);
						}
					}, 50);
					
				}
				
				// 根据curList判断发送什么请求
				if (curList == CHANNEL) 
				{
					requestScheduleInChannelList();
				} 
				else if (curList == DATE || curList == SCHEDULE) 
				{
					requestScheduleInDateList();
				}
				return;
			}
		}else if (AbStrUtil.isEmpty(name) && !AbStrUtil.isEmpty(starttime) && !AbStrUtil.isEmpty(startdate)&&AbStrUtil.isNumber(num))
		{//2.频道+时间
			int index = Var.getChannelIndexByNum(num);
			if (index > 0) 
			{
//				mReseeChannelListView.setMSelection(index,true);
				lv_backplay_channel.setCurFocusIndex(index);
				lv_backplay_channel.setChannlesInCategary(curShowChannelList);
				curChannel = lv_backplay_channel.getCurFocusIndex();
				//计算时间差了多少天
				long now = System.currentTimeMillis();
				long inDate = AbDateUtil.getMillisOfStringDate(startdate, AbDateUtil.dateFormatYMD);
				int offsetDay = Math.abs(AbDateUtil.getOffectDay(now, inDate));
//				System.out.println("###：讯飞传进来的日期和当前日期差了 " + offsetDay+" 天");
				if (offsetDay>3) 
				{
					System.out.println("###:回看范围为3天，超出回看范围");
					return;
				}
				curDate = 3-offsetDay;
				
				int cindex = 3 - curDate;
				replaceDateList(0, Method.getScaleX(80), 0, 0, cindex);
				backplayDateAdapter.removeAll();
				focus = curDate;
				backplayDateAdapter.setFocusIndex(focus);
				int end = focus + 4;
				for (int i = 0; i < end; i++) 
				{
					backplayDateAdapter.addItem(curShowDateList.get(i));
				}
				if (curList == SCHEDULE) 
				{
					myHandler.postDelayed(new Runnable() {
						
						@Override
						public void run() {
							setDateFocusStyle(false);
						}
					}, 50);
					
				}
				
				xunFeiStartTime = starttime;
				if (starttime.contains("00:00:00")) 
				{
					xunFeiStartTime = "";
				}
				// 根据curList判断发送什么请求
				if (curList == CHANNEL) 
				{
					requestScheduleInChannelList();
				} 
				else if (curList == DATE || curList == SCHEDULE) 
				{
					requestScheduleInDateList();
				}
			}
		}
		
	}
	
	private void saveChannelInfo(String name) 
	{
		SharedPreferences sp = context.getSharedPreferences("FragmentLivePlay", 0);
		Editor editor = sp.edit();
		editor.putString("name", name);
		editor.commit();
	}
	
	
	@Override
	public void jumpToLive() 
	{
		if (rl_backplay_main.getVisibility() != View.VISIBLE) 
		{
			rl_backplay_main.setVisibility(View.VISIBLE);
			rl_backplay_process.setVisibility(View.GONE);
			iv_pause_play.setVisibility(View.INVISIBLE);
			if (arcPlayControl != null) 
			{
				uploadObject.setProgramOperateType(3);
				uploadObject.setProgramStatus(1);
				DataAnalyzer.creator().uploadResee(uploadObject);
				arcPlayControl.stop();
				arcPlayControl = null;
			}
			surfaceView.stop();
			stopCountDownOrdismissOrderDialog();
		}else if (rl_backplay_main.getVisibility() == View.VISIBLE) 
		{
			iFragmentJump.jumpToFramgment(LaunchType.TYPE_LIVE, 0);
			isJumpTo = true;
		}
	}
	
	@Override
	public void voicePlay() 
	{
		super.voicePlay();
		
		
		if (rl_backplay_main.getVisibility() == View.VISIBLE) 
		{
			Log.e(tag, "准备要播放");
			/**如果可以播放就播放**/
			playIfPossible();
		}
		
		if (surfaceView != null && !surfaceView.isPlaying()) 
		{
			rl_backplay_process.setVisibility(View.GONE);
			uploadObject.setProgramOperateType(0);
			uploadObject.setProgramStatus(0);
			DataAnalyzer.creator().uploadResee(uploadObject);
			continuePlay();
			return;
		} 
	}
	
	@Override
	public void voicePause() 
	{
		super.voicePause();
		if (surfaceView != null && surfaceView.isPlaying()) 
		{
			pausePlay();
//			updateProcessSeekBar(surfaceView.getCurrentPosition(),surfaceView.getDuration());
			if(curShowScheduleList != null && curSchedule < curShowScheduleList.size())
			{
				LogUtil.e(tag, "pressCenter","节目时长为：" + surfaceView.getDuration());
				if (process_seekBar.getProgress() >= process_seekBar.getMax()) 
				{
					playfinished();
					return;
//					updateProcessSeekBar(Integer.parseInt(curShowScheduleList.get(curSchedule).getDuration()),Integer.parseInt(curShowScheduleList.get(curSchedule).getDuration()));
				}
				updateProcessSeekBar(surfaceView.getCurrentPosition(),surfaceView.getDuration());
			}
		}
	}
	
	@Override
	public void voiceResume() 
	{
		super.voiceResume();
		if (surfaceView != null && !surfaceView.isPlaying()) 
		{
			rl_backplay_process.setVisibility(View.GONE);
			uploadObject.setProgramOperateType(0);
			uploadObject.setProgramStatus(0);
			DataAnalyzer.creator().uploadResee(uploadObject);
			continuePlay();
			return;
		} 
	}
	
	@Override
	public void voiceJumpToSeconds(int seconds) 
	{
		super.voiceJumpToSeconds(seconds);
		if (faaImage.getVisibility() == View.VISIBLE) 
		{
			showFrameAnimation(false);
		}
		if (surfaceView!=null && surfaceView.isPlaying()) 
		{
//			rl_backplay_process.setVisibility(View.GONE);
//			if(faaImage.getVisibility() != View.VISIBLE)
//			{
//				faaImage.setVisibility(View.VISIBLE);
//				faaImage.bringToFront();
//			}
//			process_seekBar.setVisibility(View.VISIBLE);
//			rl_backplay_process.setVisibility(View.VISIBLE);
//			updateRefreshTime();
			surfaceView.seekTo(seconds * 1000);
		}
	}
	
	@Override
	public void voiceJumpBackwardBySeconds(int seconds) 
	{
		super.voiceJumpBackwardBySeconds(seconds);
		if (faaImage.getVisibility() == View.VISIBLE) 
		{
			showFrameAnimation(false);
		}
		if (surfaceView!=null && surfaceView.isPlaying()) 
		{
//			rl_backplay_process.setVisibility(View.GONE);
//			if(faaImage.getVisibility() != View.VISIBLE)
//			{
//				faaImage.setVisibility(View.VISIBLE);
//				faaImage.bringToFront();
//			}
//			showFrameAnimation(true);
//			process_seekBar.setVisibility(View.VISIBLE);
//			rl_backplay_process.setVisibility(View.VISIBLE);
//			updateRefreshTime();
			if ((surfaceView.getCurrentPosition() - seconds * 1000) < 0) 
			{
				playIfPossible();
			}else surfaceView.seekTo(surfaceView.getCurrentPosition() - seconds * 1000);
		}
	}
	
	@Override
	public void voiceJumpForwordBySeconds(int seconds) 
	{
		super.voiceJumpForwordBySeconds(seconds);
		if (faaImage.getVisibility() == View.VISIBLE) 
		{
			showFrameAnimation(false);
		}
		if (surfaceView!=null && surfaceView.isPlaying()) 
		{
//			rl_backplay_process.setVisibility(View.GONE);
//			if(faaImage.getVisibility() != View.VISIBLE)
//			{
//				faaImage.setVisibility(View.VISIBLE);
//				faaImage.bringToFront();
//			}
//			showFrameAnimation(true);
//			process_seekBar.setVisibility(View.VISIBLE);
//			rl_backplay_process.setVisibility(View.VISIBLE);
//			updateRefreshTime();
			if (surfaceView.getCurrentPosition() + seconds * 1000 > surfaceView.getDuration()) 
			{
				surfaceView.seekTo(surfaceView.getDuration() - 5*1000);
			}else
				surfaceView.seekTo(surfaceView.getCurrentPosition() + seconds * 1000);
		}
	}



	private IUpdateData gdiupdata = new IUpdateData() {
		@Override
		public void updateData(String method, String uniId, Object object, boolean isSuccess)
		{
			if (method.equals(GDHttpTools.METHOD_GETLIVESCHEDULELIST) && isSuccess)
			{
				curShowScheduleList = (List<Schedule>) object;
				String data[] = uniId.split("@@@");
				int tempIndex = Integer.valueOf(data[1]);
				int tempDate = Integer.valueOf(data[2]);
 				dealWithScheduleList();
				if(curShowScheduleList.size() == 0 || curShowChannelList.size() == 0)
				{
					if(lv_backplay_schedule != null)
					{
						lv_backplay_schedule.clearDrawList();
					}
					return;
				}
				if (tempIndex < curShowChannelList.size()
						&& !data[0].equals(curShowChannelList.get(tempIndex).getContentId()))
				{
					return;
				}

				Calendar calendar = Calendar.getInstance();
				Date nowdate = calendar.getTime();
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
						"yyyyMMdd");
				int n = tempDate - 3;
				Date start_date = new Date(nowdate.getTime() + n * 24 * 60 * 60 * 1000);
				String start = simpleDateFormat.format(start_date);

				ScheduleCache.setReseeSchedule(curShowChannelList.get(tempIndex).getContentId(), start, curShowScheduleList);

				int index = reOrderSchedule_list();
//				LogUtil.e(tag, "updateData","当前的节目单焦点是：" + index);
//				reloadScheduleListView(index);

				LogUtil.e(tag, "updateData","当前的节目单焦点是：" + index);
				if (curShowScheduleList.get(0).getStartDate().contains(start)){ //在实际配置列表之前，再进行一次判断，比对日期是否一致
					requestScheduleTimes=0;
					reloadScheduleListView(index);
				}else{
					requestScheduleInDateList();
				}
			}
		}
	};

	Handler hd = new Handler();
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
			UIUtils.showToast("该频道为VIP频道");
		}

		@Override
		public void onCountDownStop()
		{
			//			showToast("倒计时停止");
			mernakeCountDownTools = null;//将计数器置空
		}
	};
	/**用来存放准备播放，但是还没进行播放鉴权的频道**/
	private ContentChannel zzTempChannel;
	private void doSomethingWhenCountdownFinished()
	{
		Log.i("MARK", "doSomethingWhenCountdownFinished mContext=="+mContext+"  curPlayingChannel=="+curPlayingChannel);//

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


	private IUpdateData gdIupdata = new IUpdateData()
	{
		@Override
		public void updateData(String method, String uniId, Object object, boolean isSuccess)
		{


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
				}else if (res.getCode().equals("51000-2"))//51A00-2 --> 51000-2
				{//the channel need to buy
					UIUtils.showToast("试看结束，请订购观看");
					GDHttpTools.getInstance().setNeedReplay(false);//不准重新播放，直到订购成功

					if (surfaceView!= null) {
						surfaceView.pause();//暂停播放
					}
					ZZDialogTools.getInstance().showOrderDialog(curShowChannelList.get(curChannel).getName(),Var.userId,curShowChannelList.get(curChannel).getContentId(),Var.allCategoryId,getActivity());
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


}
