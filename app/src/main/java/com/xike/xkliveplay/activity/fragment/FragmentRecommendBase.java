package com.xike.xkliveplay.activity.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.arcsoft.media.ArcPlayer;
import com.mernake.framework.tools.MernakeSharedTools;
import com.shandong.sdk.shandongsdk.bean.LiveRecommendData;
import com.shandong.sdk.shandongsdk.bean.LiveRecommendDataData;
import com.shandong.shandonglive.recommend.focus.EffectNoDrawBridge;
import com.shandong.shandonglive.recommend.focus.MainUpView;
import com.xike.xkliveplay.R;
import com.xike.xkliveplay.activity.LaunchType;
import com.xike.xkliveplay.framework.arcplay.ArcPlayControl;
import com.xike.xkliveplay.framework.arcplay.ArcSurfaceView;
import com.xike.xkliveplay.framework.arcplay.IPlayControl;
import com.xike.xkliveplay.framework.arcplay.IPlayerStateListener;
import com.xike.xkliveplay.framework.entity.ContentChannel;
import com.xike.xkliveplay.framework.http.IUpdateData;
import com.xike.xkliveplay.framework.httpclient.DataModel;
import com.xike.xkliveplay.framework.tools.AbStrUtil;
import com.xike.xkliveplay.framework.tools.ImageUtils;
import com.xike.xkliveplay.framework.tools.LogUtil;
import com.xike.xkliveplay.framework.varparams.Var;
import com.xike.xkliveplay.gd.GDHttpTools;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


//import android.view.ViewGroup.LayoutParams;

/**
 * @author LiWei <br>
 * CreateTime: 2014年10月29日 下午2:32:40<br>
 * <b>Info:</b><br>
 * <br>
 * <b>Method:</b> <br>
 */
@SuppressLint("ValidFragment")
public class FragmentRecommendBase extends FragmentBase implements View.OnClickListener, IPlayerStateListener {
    public IFragmentJump iFragmentJump = null;
    private Context mContext;
    private View curView;
    private List<LiveRecommendDataData> recommendDataList;
    private ArcSurfaceView videoView;//视频框
    private RelativeLayout[] relativeLayouts;//首页模块
    private TextView[] textViews;//首页文字
    private ImageView[] imageViews;//首页图片

    //焦点控件
    private MainUpView mainUpView;
    private EffectNoDrawBridge bridget;
    private View mNewFocus, mOldFocus;
    private boolean isFirst;//默认false

    //fragment跳转
    private FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    FragmentBase turnFragment;

    //直播跳转参数
    private String jumpChannelNum;
    private String jumpChannelName;
    private String jumpCategoryId;

    //退出时间
    private long exitTime;

    //页面标识
    public static String instanse;

    //小视频框地址
    private String playUrl;
    private ContentChannel playChannel;

    //时钟控件
    private TextView timeDateTxt;
    private SimpleDateFormat dateFormat;
    private long nowTime;

    //跑马灯
    private TextView marqueenTextView;
    //记录当前是哪个Fragment和上个Fragment（也就是从哪个Fragment来）
    private int curLaunchType = LaunchType.TYPE_RECOMMEND;
    private int lastLaunchType = 1000;//这是个假定的type，一个原则是不与正在使用的launchType重复

    private ArcPlayControl arcPlayControl = null;
    //handler机制
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                mNewFocus.setSelected(true);
            } else if (msg.what == 2) {
                setFirstFocus();
            } else if (msg.what == 3) {
                Log.e("MARK", "setData: allChannels size is = " + Var.allChannels.size());
                for (ContentChannel channel : Var.allChannels) {
                    Log.e("MARK", "setData: getContentId = " + channel.getChannelNumber());
                    if (recommendDataList.get(0).getContentId() != null && channel.getChannelNumber() != null && channel.getChannelNumber().equals(recommendDataList.get(0).getContentId())) {
                        //播放频道
                        Log.e("MARK", "recommendDataList.get(0).getContentId(): " + recommendDataList.get(0).getContentId());
                        playUrl = channel.getPlayURL();
                        Log.e("MARK", "playUrl is: " + playUrl);
                        if (playUrl != null && !"".equals(playUrl)) {
                            playChannel = channel;
                            break;
                        }
                    }
                }
                if (playChannel != null) {
                    playVideo(playChannel);
                }
            } else if (msg.what == 4) {
                nowTime = System.currentTimeMillis();
                dateFormat = new SimpleDateFormat("MM月dd日  EEEE  hh:mm");
                String times = dateFormat.format(nowTime);
                timeDateTxt.setText(times);
            }
        }
    };

    public FragmentRecommendBase() {

    }

    public FragmentRecommendBase(IFragmentJump _fJump) {
        iFragmentJump = _fJump;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        DataModel.getInstance();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        LogUtil.e("MARK", "onCreate", "STARTING");
        super.onCreate(savedInstanceState);
        MernakeSharedTools.set(getActivity(), "isLiveStarted", true);
        mContext = getActivity().getApplicationContext();
    }

    @Override
    public void onResume() {

        if (videoView != null && videoView.getVisibility() == View.INVISIBLE) {
            videoView.setVisibility(View.VISIBLE);//这个VISIBLE是为了在跳入点播的时候，背后不能有声音,然后返回的时候，需要再展示出来
        }
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        if (arcPlayControl != null) {
            arcPlayControl.stop();
        }
       /* //这是为了小窗进大窗的时候，不再显示一遍小窗
        if (videoView != null) {
            videoView.stopPlayback();
            videoView.setVisibility(View.GONE);
            videoView.setVisibility(View.VISIBLE);
            videoView.setVisibility(View.INVISIBLE);//这个INVISIBLE是为了在跳入点播的时候，背后不能有声音
            //			videoView.setDisplayNull();
        }*/
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        curView = inflater.inflate(R.layout.activity_recommend, container, false);
        instanse = "Recommend";
        //初始化控件
        initView();
        //初始化数据
        initData();
        //初始化焦点
        initViewMove();
        //获取首页数据
        getData();
        //延迟设置焦点
        handler.sendEmptyMessageDelayed(2, 100);
        //事件监听
        setListener();
        return curView;
    }

    //初始化数据
    private void initData() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(4);
            }
        }, 0, 1000);
    }

    //初始化控件
    private void initView() {
        //时间控件
        timeDateTxt = (TextView) curView.findViewById(R.id.time_date_txt);
        //跑马灯
        marqueenTextView = (TextView) curView.findViewById(R.id.marquee_txt);
        videoView = (ArcSurfaceView) curView.findViewById(R.id.videoView);

        relativeLayouts = new RelativeLayout[10];
        imageViews = new ImageView[10];
        textViews = new TextView[10];
        relativeLayouts[0] = (RelativeLayout) curView.findViewById(R.id.item_1);
        relativeLayouts[1] = (RelativeLayout) curView.findViewById(R.id.item_2);
        relativeLayouts[2] = (RelativeLayout) curView.findViewById(R.id.item_3);
        relativeLayouts[3] = (RelativeLayout) curView.findViewById(R.id.item_4);
        relativeLayouts[4] = (RelativeLayout) curView.findViewById(R.id.item_5);
        relativeLayouts[5] = (RelativeLayout) curView.findViewById(R.id.item_6);
        relativeLayouts[6] = (RelativeLayout) curView.findViewById(R.id.item_7);
        relativeLayouts[7] = (RelativeLayout) curView.findViewById(R.id.item_8);
        relativeLayouts[8] = (RelativeLayout) curView.findViewById(R.id.item_9);
        relativeLayouts[9] = (RelativeLayout) curView.findViewById(R.id.item_10);
        imageViews[0] = (ImageView) curView.findViewById(R.id.img_1);
        imageViews[1] = (ImageView) curView.findViewById(R.id.img_2);
        imageViews[2] = (ImageView) curView.findViewById(R.id.img_3);
        imageViews[3] = (ImageView) curView.findViewById(R.id.img_4);
        imageViews[4] = (ImageView) curView.findViewById(R.id.img_5);
        imageViews[5] = (ImageView) curView.findViewById(R.id.img_6);
        imageViews[6] = (ImageView) curView.findViewById(R.id.img_7);
        imageViews[7] = (ImageView) curView.findViewById(R.id.img_8);
        imageViews[8] = (ImageView) curView.findViewById(R.id.img_9);
        imageViews[9] = (ImageView) curView.findViewById(R.id.img_10);
        textViews[0] = (TextView) curView.findViewById(R.id.txt_1);
        textViews[1] = (TextView) curView.findViewById(R.id.txt_2);
        textViews[2] = (TextView) curView.findViewById(R.id.txt_3);
        textViews[3] = (TextView) curView.findViewById(R.id.txt_4);
        textViews[4] = (TextView) curView.findViewById(R.id.txt_5);
        textViews[5] = (TextView) curView.findViewById(R.id.txt_6);
        textViews[6] = (TextView) curView.findViewById(R.id.txt_7);
        textViews[7] = (TextView) curView.findViewById(R.id.txt_8);
        textViews[8] = (TextView) curView.findViewById(R.id.txt_9);
        textViews[9] = (TextView) curView.findViewById(R.id.txt_10);
        //初始化fragment相关信息
        fragmentManager = getActivity().getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
    }

    /**
     * 初始化焦点样式
     */
    public void initViewMove() {
        Log.e("MARK", "Recommend,initViewMove");
        // 焦点样式
        mainUpView = (MainUpView) curView.findViewById(R.id.mainUpView);
        mainUpView.setEffectBridge(new EffectNoDrawBridge());
        mainUpView.setUpRectResource(R.drawable.sdtv_focus2); // 设置移动边框的图片.
        mainUpView.setDrawUpRectPadding(new Rect(38, 38, 30, 38));
        bridget = (EffectNoDrawBridge) mainUpView.getEffectBridge();
        bridget.setTranDurAnimTime(200);
    }

    //设置第一个焦点
    private void setFirstFocus() {
        if (mOldFocus != null) {
            mOldFocus.requestFocus();
            Log.e("MARK", "setFirstFocus: mOldFocus是否有焦点：" + mOldFocus.isFocused());
            mainUpView.setFocusView(mOldFocus, 1.0f);
        } else {
            videoView.requestFocus();
            mOldFocus = videoView;
            Log.e("MARK", "setFirstFocus: videoView是否有焦点：" + videoView.isFocused());
            mainUpView.setFocusView(videoView, 1.0f);
        }
    }

    //获取首页数据
    private void getData() {
        GDHttpTools.getInstance().getRecommendData(GDHttpTools.getInstance().getTag(), new IUpdateData() {
            @Override
            public void updateData(String method, String uniId, Object object, boolean isSuccess) {
                if (isSuccess && method != null && method.equals(GDHttpTools.METHOD_GETRECOMMENDDATA)) {//获取首页数据的方法
                    LiveRecommendData liveRecommendData = (LiveRecommendData) (object);
                    recommendDataList = liveRecommendData.getData();
                    //加载数据
                    if (recommendDataList != null && recommendDataList.size() >= 12) {
                        setData();
                    }
                } else if (!isSuccess) {
                    Toast.makeText(getActivity(), "首页数据获取失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //加载数据
    private void setData() {
        for (int i = 0; i < recommendDataList.size(); i++) {
            if (i == 0) {//视频框
                handler.sendEmptyMessageDelayed(3, 100);
            } else if (recommendDataList.get(i).getType() != null && recommendDataList.get(i).getType().equals("marqueen")) {
                if (recommendDataList.get(i).getContentId() != null) {
                    marqueenTextView.setSelected(true);
                    marqueenTextView.setText(recommendDataList.get(i).getContentId());
                }
            } else {
                //图片
                if (recommendDataList.get(i).getImgUrl() != null && imageViews.length > i - 1) {
                    Log.i("MARK","****** name:" + getActivity().getClass().getName());


                    if (i == 8) {
                        ImageUtils.setImage(recommendDataList.get(i).getImgUrl(), getActivity(), imageViews[i - 1], R.drawable.zhan_529_733_ico, getActivity().getClass().getName());
                    } else {
                        ImageUtils.setImage(recommendDataList.get(i).getImgUrl(), getActivity(), imageViews[i - 1], R.drawable.zhan_380_280_horzition, getActivity().getClass().getName());
                    }
                }
                //文字
                if (recommendDataList.get(i).getName() != null && imageViews.length > i - 1) {
                    if (!recommendDataList.get(i).getName().equals("")) {
                        textViews[i - 1].setText(recommendDataList.get(i).getName());
                    } else {
                        textViews[i - 1].setText("暂无信息");
                    }
                }
            }
        }
    }

    /*private void playVideo(ContentChannel playChannel)
    {
        LogUtil.i("xumin","playVideo","******playChannel.getPlayURL():" + playChannel.getPlayURL());
        if (playChannel == null)
        {
            LogUtil.e("xumin", "playVideo","playVideo is null");
            return;
        }



    }*/
    private void playVideo(ContentChannel playChannel) {

        LogUtil.i("MARK", "playVideo", "********************playVideo is start**********************curPlayingChannelId:");
        if (playChannel == null) {
            LogUtil.e("MARK", "playVideo", "playVideo is null");
            return;
        }


        if (arcPlayControl != null) {
            arcPlayControl.stop();
            arcPlayControl = null;
        }
       // arcPlayControl = new ArcPlayControl(mContext, videoView, null);

        arcPlayControl = new ArcPlayControl(mContext, videoView,
                new IPlayControl() {
                    @SuppressLint("NewApi")
                    @Override
                    public void isBufferData(boolean isBuffer)
                    {
                        System.out.println("视频缓冲状态："+isBuffer);

                        if (!isBuffer)
                        {

                        }
                    }
                });
       /* arcPlayControl.setServiceID(playChannel.getContentId());


        arcPlayControl.setUri(playChannel.getPlayURL());

        //		arcPlayControl.setDmrURL(Var.drm_domain_url);
        arcPlayControl.arcPlayer_play();*/
        arcPlayControl.setDeviceID(Var.mac);
        arcPlayControl.setDrmType(ArcPlayer.TYPE_DRM.P2P_NODRM);
        //		arcPlayControl.setDrmType(TYPE_DRM.P2P_DRM);
        arcPlayControl.setProductid(Var.productId);
        arcPlayControl.setServiceID(playChannel.getContentId());
         arcPlayControl.setTokenID(Var.userToken);
        arcPlayControl.setType(0);
        arcPlayControl.setUri(playChannel.getPlayURL());
        arcPlayControl.setUserID(Var.userId);
        //		arcPlayControl.setDmrURL(Var.drm_domain_url);
        arcPlayControl.arcPlayer_play();
    }


    //监听事件
    private void setListener() {
        videoView.requestFocus();
        videoView.getViewTreeObserver().addOnGlobalFocusChangeListener(new ViewTreeObserver.OnGlobalFocusChangeListener() {
            @Override
            public void onGlobalFocusChanged(View oldFocus, View newFocus) {
                Log.e("MARK", "onGlobalFocusChanged: 全局焦点");
                //取消上一个焦点的选中状态
                if (mOldFocus != null) {
                    mOldFocus.setSelected(false);
                }
                //设置新焦点状态
                if (newFocus != null) {
                    mNewFocus = newFocus;
                    handler.sendEmptyMessageDelayed(1, 100);
                    if (newFocus.getId() == R.id.videoView) {
                        mainUpView.setDrawUpRectPadding(new Rect(38, 38, 38, 38));
                    } else {
                        mainUpView.setDrawUpRectPadding(new Rect(38, 38, 38, 38));
                    }
                    mainUpView.setFocusView(newFocus, mOldFocus, 1.0f);
                    mOldFocus = newFocus;
                }
            }
        });
        //点击事件
        videoView.setOnClickListener(this);
        for (int i = 0; i < relativeLayouts.length; i++) {
            relativeLayouts[i].setOnClickListener(this);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.e("MARK", "onkeyDownReturn: FragmentRecommendBase keyCode other");
        if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
            curView.findViewById(mOldFocus.getNextFocusUpId()).requestFocus();
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
            curView.findViewById(mOldFocus.getNextFocusDownId()).requestFocus();
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
            curView.findViewById(mOldFocus.getNextFocusLeftId()).requestFocus();
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
            curView.findViewById(mOldFocus.getNextFocusRightId()).requestFocus();
        }
        return super.onKeyDown(keyCode, event);
    }

    //返回按键
    @Override
    public boolean onkeyDownReturn() {
        Log.e("MARK", "onkeyDownReturn: FragmentRecommendBase keyCode back");
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(getActivity(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            System.exit(0);
        }
        return super.onkeyDownReturn();
    }

    @Override
    public void onClick(View v) {
        Log.e("MARK", "onClick: 触发点击");
        if (v.getId() == R.id.videoView) {
            recommendIntent(0);
        } else if (v.getId() == R.id.item_1) {
            recommendIntent(1);
        } else if (v.getId() == R.id.item_2) {
            recommendIntent(2);
        } else if (v.getId() == R.id.item_3) {
            recommendIntent(3);
        } else if (v.getId() == R.id.item_4) {
            recommendIntent(4);
        } else if (v.getId() == R.id.item_5) {
            recommendIntent(5);
        } else if (v.getId() == R.id.item_6) {
            recommendIntent(6);
        } else if (v.getId() == R.id.item_7) {
            recommendIntent(7);
        } else if (v.getId() == R.id.item_8) {
            recommendIntent(8);
        } else if (v.getId() == R.id.item_9) {
            recommendIntent(9);
        } else if (v.getId() == R.id.item_10) {
            recommendIntent(10);
        }
    }

    //推荐位跳转
    public void recommendIntent(int num) {
        if (recommendDataList.size() > num) {
            //存下焦点位置
            Bundle bundle = new Bundle();
            //跳转判断
            Log.e("MARK", "recommendIntent: 跳转类型 = " + recommendDataList.get(num).getType());
            if (recommendDataList.get(num).getType() != null && recommendDataList.get(num).getType().equals("live")) {//直播
                jumpChannelNum = recommendDataList.get(num).getContentId();
                jumpCategoryId = recommendDataList.get(num).getCategoryId();
                Log.e("MARK", "recommendIntent: 跳转直播，jumpChannelNum = " + jumpChannelNum);
                Log.e("MARK", "recommendIntent: 跳转直播，jumpCategoryId = " + jumpCategoryId);
                //频道信息
                if (!AbStrUtil.isEmpty(jumpChannelNum)) {
                    for (ContentChannel channel : Var.allChannels) {
                        String channel2 = "";
                        String channel3 = "";
                        if (channel.getChannelNumber() != null && channel.getChannelNumber().length() == 1) {
                            channel2 = "0" + channel.getChannelNumber();
                            channel3 = "00" + channel.getChannelNumber();
                        } else if (channel.getChannelNumber() != null && channel.getChannelNumber().length() == 2) {
                            channel3 = "0" + channel.getChannelNumber();
                        }

                        //这种情况不能匹配到jumpchannelNum是小于3位的情况，比如jumpchanellNum是30，而频道列表里边是030，就无法匹配到
                        if (channel.getChannelNumber().equals(jumpChannelNum) || channel2.equals(jumpChannelNum) || channel3.equals(jumpChannelNum)) {
                            Var.isJumpNumExist = true;
                            saveChannelInfo(channel.getName());
                            jumpChannelNum = "";
                            break;
                        }

                        //所以这里要加反向匹配
                        String jumpString2 = "";
                        String jumpString3 = "";
                        if (jumpChannelNum.length() == 1) {
                            jumpString2 = "0" + jumpChannelNum;
                            jumpString3 = "00" + jumpChannelNum;
                        } else if (jumpChannelNum.length() == 2) {
                            jumpString3 = "0" + jumpChannelNum;
                        }

                        if (channel.getChannelNumber().equals(jumpChannelNum) || channel.getChannelNumber().equals(jumpString2) || channel.getChannelNumber().equals(jumpString3)) {
                            Var.isJumpNumExist = true;
                            saveChannelInfo(channel.getName());
                            jumpChannelNum = "";
                            break;
                        }

                    }
                }
                //bundle传值
                bundle.putBoolean("type", false);
                //增加逻辑，如果传进来其他类别的id，要直接跳转到该类别下
                int index = 0;
                //分组信息
                if (jumpCategoryId != null && jumpCategoryId.length() > 0) {
                    index = Var.getCategoryIndex(jumpCategoryId);
                }
                bundle.putInt("index", index);
                bundle.putBoolean("first", true);
                bundle.putInt("lastLaunchType", curLaunchType);//将首页这个fragment作为上个fragment传给直播
                iFragmentJump.jumpToFragmentWithBundles(LaunchType.TYPE_LIVE, index, bundle);
            } else if (recommendDataList.get(num).getType() != null && recommendDataList.get(num).getType().equals("rePlay")) {//回看
                Log.e("MARK", "recommendIntent: 跳转回看");
                Bundle bundleRePlay = new Bundle();
                bundleRePlay.putInt("lastLaunchType", curLaunchType);
                int index = 0;
                iFragmentJump.jumpToFragmentWithBundles(LaunchType.TYPE_REPLAY, index, bundleRePlay);
            } else if (recommendDataList.get(num).getType() != null && recommendDataList.get(num).getType().equals("vod")) {//点播
                Log.e("MARK", "recommendIntent: 跳转点播");
                bundle.putString("type", recommendDataList.get(num).getTurnPage());
                if (recommendDataList.get(num).getContentId() != null && !recommendDataList.get(num).getContentId().equals("")) {
                    bundle.putString("code", recommendDataList.get(num).getContentId());
                } else if (recommendDataList.get(num).getCategoryId() != null && !recommendDataList.get(num).getCategoryId().equals("")) {
                    bundle.putString("code", recommendDataList.get(num).getCategoryId());
                }
                iFragmentJump.jumpToVod(bundle);
            }
        } else {
            Toast.makeText(getActivity(), "数据获取错误，跳转失败。", Toast.LENGTH_SHORT).show();
        }
    }

    //保存频道信息
    private void saveChannelInfo(String name) {
        SharedPreferences sp = getActivity().getSharedPreferences("FragmentLivePlay", 0);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("name", name);
        editor.commit();
    }

    @Override
    public void onPlayerStateChanged(int state, int what, int extra) {
        //首页播放，目前不需要做任何事情，只是备用的
    }
}
