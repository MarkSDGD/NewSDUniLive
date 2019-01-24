package com.xike.xkliveplay.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemProperties;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ab.util.AbSharedUtil;
import com.arcsoft.media.Common;
import com.iflytek.xiri.Feedback;
import com.iflytek.xiri.scene.ISceneListener;
import com.iflytek.xiri.scene.Scene;
import com.umeng.analytics.MobclickAgent;
import com.xike.xkliveplay.R;
import com.xike.xkliveplay.activity.fragment.FragmentBackPlayBase;
import com.xike.xkliveplay.activity.fragment.FragmentBase;
import com.xike.xkliveplay.activity.fragment.FragmentLivePlayBase;
import com.xike.xkliveplay.activity.fragment.FragmentRecommendBase;
import com.xike.xkliveplay.activity.fragment.IFragmentJump;
import com.xike.xkliveplay.framework.db.DBManager;
import com.xike.xkliveplay.framework.entity.AuthRes;
import com.xike.xkliveplay.framework.entity.AuthenticationRes;
import com.xike.xkliveplay.framework.entity.Category;
import com.xike.xkliveplay.framework.entity.ContentChannel;
import com.xike.xkliveplay.framework.entity.GetAccountInfoRes;
import com.xike.xkliveplay.framework.entity.LiveUpgrageResponse;
import com.xike.xkliveplay.framework.error.ErrorBroadcastAction;
import com.xike.xkliveplay.framework.error.SendBroadcastTools;
import com.xike.xkliveplay.framework.http.HttpUtil;
import com.xike.xkliveplay.framework.http.IUpdateData;
import com.xike.xkliveplay.framework.httpclient.VarParam;
import com.xike.xkliveplay.framework.tools.APKTools;
import com.xike.xkliveplay.framework.tools.AuthTools;
import com.xike.xkliveplay.framework.tools.GetStbinfo;
import com.xike.xkliveplay.framework.tools.LogUtil;
import com.xike.xkliveplay.framework.tools.MernakeLogTools;
import com.xike.xkliveplay.framework.tools.MessageType;
import com.xike.xkliveplay.framework.tools.Method;
import com.xike.xkliveplay.framework.tools.NetStatusChange;
import com.xike.xkliveplay.framework.tools.P2pSettingTool;
import com.xike.xkliveplay.framework.tools.SharedPreferenceTools;
import com.xike.xkliveplay.framework.tools.UIUtils;
import com.xike.xkliveplay.framework.varparams.Var;
import com.xike.xkliveplay.smarttv.Config;
import com.xike.xkliveplay.xunfei.XunfeiInsideConstant;
import com.xike.xkliveplay.xunfei.XunfeiTools;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;

/**
 * @author LiWei <br>
 * CreateTime: 2014年10月20日 下午1:53:57<br>
 * <b>Info:</b><br>
 * <br>
 * <b>Method:</b> <br>
 */
public class ActivityLaunchBase extends FragmentActivity implements IUpdateData, IFragmentJump {

    private static final String tag = ActivityLaunchBase.class.getSimpleName();
    public boolean isActivateTerminal = false;

    /**
     * Current type id
     **/
    public int curType = 0;

    /**
     * Animation compoent
     **/
    private ImageView faaImage = null;
    /**
     * Animation view
     **/
    private AnimationDrawable mLoadingView = null;

    /**
     * 收藏的临时列表
     **/
    private List<ContentChannel> saveList = null;

    /**
     * Current fragment
     **/
    public FragmentBase curFragment = null;

    /**
     * Update live apk save path
     **/
    public String downloadApkPath = "";

    /**
     * Handler message.DATABASE has data to read.
     **/
    public static final int MSG_GET_DATABASE_CACHE = 1;

    public String encryToken = "";
    /**
     * 用户认证b接口
     **/
    public String userToken = "";
    public String epg_domain_url = "";
    public String drm_domain_url = "";

    /**
     * 账号获取接口
     **/
    public String userId = "";
    public String userPassWord = "";

    /**
     * 获取productId接口
     **/
    public String productId = "";

    /**
     * 用来存放信息的map
     **/
    public static HashMap<String, String> stb_map = null;

    public String jumpChannelName = "";
    public String jumpChannelNum = "";
    private String jumpReseeUrl = "";
    public String jumpCategoryId = "";

    public DBManager dbManager = null;

    private Scene mScene = null;
    private Feedback mFeedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_launch);
        XunfeiTools.getInstance().sendLiveStatusBroadcast(this, true);
        Var.isActivityLaunched = true;
        initVersion();
        initParam();
        mScene = new Scene(this);
        mFeedback = new Feedback(this);
    }

    //	private void updateConfig()
    //	{
    //		Config.clear();
    //		Config.smartTvChannelName = this.getIntent().getStringExtra("name");
    //		Config.smartTvChannelName = Config.getChannelName(Config.smartTvChannelName);
    //		if (Config.smartTvChannelName == null)
    //		{
    //			Config.smartTvChannelName = "CCTV-1";
    //		}
    //		Config.smartProgramStartTime = this.getIntent().getStringExtra("time");
    ////		Config.smartProgramStartTime = this.getIntent().getExtras().getString("time");
    //	}


    @Override
    protected void onResume() {
        super.onResume();
        NetStatusChange.getInstance().reg(this);
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }


    /**
     * initVersion(这里用一句话描述这个方法的作用)
     *
     * @Title: initVersion
     * @Description: 初始化版本号
     */
    private void initVersion() {
        String appVersion = null;
        TextView tv = (TextView) findViewById(R.id.tv_version);
        PackageManager manager = this.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            appVersion = info.versionName; // 版本名，versionCode同理
            String appVersionCode = info.versionCode + "";
            Log.e(tag, "******************XKLiveplay versioncode is: " + appVersionCode);
            Log.e(tag, "******************XKLiveplay versionname is: " + appVersion);
            tv.setText(appVersion);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        makeDirs();
        String lastVersion = SharedPreferenceTools.getSavedStringPreference(getApplicationContext(), SharedPreferenceTools.SHARED_KEY_VERSION, SharedPreferenceTools.SHARED_FILE_LIVEPLAY);
        if (lastVersion.equals(appVersion))  //最后一次记录的版本号与当前的版本号相同，不需要重写配置文件
        {
            P2pSettingTool.writeSettingFile(false);
        } else   //最后一次记录的版本号与当前的版本号不同，需要重写配置文件
        {
            delVaramFile();
            SharedPreferenceTools.setSavedStringPreference(getApplicationContext(), SharedPreferenceTools.SHARED_KEY_VERSION, appVersion, SharedPreferenceTools.SHARED_FILE_LIVEPLAY);
            P2pSettingTool.writeSettingFile(true);
        }
    }

    private void delVaramFile() {
        String infoPath = "/mnt/sdcard/stbinfo";
        File file = new File(infoPath);
        if (file != null && file.exists()) {
            file.delete();
        }
    }

    /**
     * initParam(这里用一句话描述这个方法的作用)
     *
     * @Title: initParam
     * @Description: 初始化参数
     */
    public void initParam() {
        dbManager = new DBManager(getApplicationContext());
        stb_map = new HashMap<String, String>();
        initDownloadAPKPath(); // Init update path.
        updateConfig(); //Init params about SmartTV.
        initAnimation(); // Init cache animation.
        initP2P(); //Init p2p settings.
        initCurType();
        Method.getScreenHeight(this);
        Method.getScreenWidth(this);
    }


    /**
     * initCurType(这里用一句话描述这个方法的作用)
     *
     * @Title: initCurType
     * @Description: 初始化进入时获得的跳转码，来完成跳转。
     */
    private void initCurType() {
        if (getIntent().getExtras() == null) {
            curType = LaunchType.TYPE_RECOMMEND;
        } else {
            curType = getIntent().getExtras().getInt("type", 0);
            jumpChannelName = getIntent().getExtras().getString("channelName");
            jumpChannelNum = getIntent().getExtras().getString("channelNum");
            jumpCategoryId = getIntent().getExtras().getString("categoryId");
        }
        MernakeLogTools.showLog("", "jumpChannelName:" + jumpChannelName);
        MernakeLogTools.showLog("", "jumpChannelNum:" + jumpChannelNum);
        MernakeLogTools.showLog("", "jumpCategoryId:" + jumpCategoryId);
        MernakeLogTools.showLog("", "curType:" + curType);
    }

    /**
     * function: Init p2p settings.
     *
     * @param
     * @return
     */
    @SuppressLint("SdCardPath")
    private void initP2P() {
        Common.LoadP2P("/mnt/sdcard/p2p/", true, "0xffffffff", "0", 10);
    }


    /**
     * function: Init cache animation.
     *
     * @param
     * @return
     */
    private void initAnimation() {
        faaImage = (ImageView) findViewById(R.id.faa);
        showFrameAnimation(true);
    }

    /**
     * function: Make dirs.
     *
     * @param
     * @return
     */
    private void makeDirs() {
        makeIfNoSDcard(); // Create /mnt/sdcard directory if it's not exist.
        makeDirIfNop2p(); // Create /mnt/sdcard/p2p directory if it's not exist.
    }


    /**
     * function: Init downloaded live apk save path.
     *
     * @param
     * @return
     */
    private void initDownloadAPKPath() {
        downloadApkPath = this.getFilesDir().getAbsolutePath() + "/xklive.apk";
    }

    /**
     * function: Init params about SmartTV.
     *
     * @param
     * @return
     */
    private void updateConfig() {
        Config.clear();
        Config.smartTvChannelName = this.getIntent().getStringExtra("name");
        Config.smartTvChannelName = Config.getChannelName(Config.smartTvChannelName);
        if (Config.smartTvChannelName == null) {
            Config.smartTvChannelName = "";
        }
        Config.smartProgramStartTime = this.getIntent().getStringExtra("time");
        LogUtil.i(tag, "updateConfig:", "channelName: " + Config.smartTvChannelName);
        LogUtil.i(tag, "updateConfig:", "startTime: " + Config.smartProgramStartTime);
    }


    /**
     * function:
     *
     * @param
     * @return
     */
    public boolean paramSetting2() {
        if (Var.initStbVars(this) == Var.INIT_OK) {
            if (dbManager == null || !dbManager.isDBOpen()) {
                dbManager = new DBManager(getApplicationContext());
            }
            @SuppressWarnings("unchecked") List<ContentChannel> list = (List<ContentChannel>) dbManager.queryAll("channel", ContentChannel.class, null);
            @SuppressWarnings("unchecked") List<Category> categories = (List<Category>) dbManager.queryAll("category", Category.class, null);
            if (list == null || categories == null || list.size() <= 0 || categories.size() <= 0) {
                delSqliteDatabase();
                return false;
                //				postGetAccountInfo(); //数据库没有缓存，重新请求
            } else {
                int flag = AbSharedUtil.getInt(getApplicationContext(), Var.KEY_ISFINISHED);
                System.out.println("======读取标志位：" + flag);
                if (flag == 0) {
                    return false;
                }

                hd.sendEmptyMessage(MSG_GET_DATABASE_CACHE); //数据库有缓存，将数据库的内容读出
            }
            return true;
        } else {
            delSqliteDatabase();
            //			postGetAccountInfo();
            return false;
        }
    }

    @SuppressLint("HandlerLeak")
    public Handler hd = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MSG_GET_DATABASE_CACHE) {
                getInfoFromDatabase();
                return;
            } else if (msg.what == MessageType.MSG_NET_CONNECTED) {
                System.out.println("@@@外网是联通的");
                //				Toast.makeText(ActivityLaunchBase.this, "外网是联通的", Toast.LENGTH_SHORT).show();
                //				DialogError.getInstance().cancelDialog();
            } else if (msg.what == MessageType.MSG_NET_DISCONNECTED) {
                System.out.println("@@@外网断开了的");
                //				Toast.makeText(ActivityLaunchBase.this, "外网是断开的", Toast.LENGTH_SHORT).show();
                //				DialogError.getInstance().showDialog(ActivityLaunchBase.this, ErrorCode.ERROR_INTERNET_EXCEPTION);
            }
        }
    };

    @SuppressWarnings("unchecked")
    public void getInfoFromDatabase() {
        if (Var.allCategorys.size() == 0 || Var.allChannels.size() == 0) {
            Var.allCategorys = (List<Category>) dbManager.queryAll("category", Category.class, "cid");
            Var.allChannels = (List<ContentChannel>) dbManager.queryAll("channel", ContentChannel.class, "id");
        }

        Category category = getAllChannelCategory(Var.allCategorys);
        if (category != null) {
            Var.allCategoryId = category.getId();
        }
        dbManager.closeDB();
        jumpToFragment();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (curType != LaunchType.TYPE_REPLAY)  // Hisense.
        {
            SystemProperties.set("media.amplayer.displast_frame", "1");
        }
        mScene.init(listener);
    }

    private ISceneListener listener = new ISceneListener() {
        @Override
        public String onQuery() {
            System.out.println("#####:讯飞调用onQuery：" + XunfeiInsideConstant.COMMANDS);
            return XunfeiInsideConstant.COMMANDS;
        }

        @Override
        public void onExecute(Intent intent) {
            System.out.println("#####:讯飞调用onExecute");
            mFeedback.begin(intent);
            Bundle bundle = intent.getExtras();
            for (String str : bundle.keySet()) {
                if (str.equals("position") || str.equals("offset")) {
                    System.out.println("#####:讯飞：key=" + str + "  value=" + bundle.getInt(str));
                } else
                    System.out.println("#####:讯飞：key=" + str + "  value=" + bundle.getString(str));

            }


            if (intent.hasExtra(XunfeiInsideConstant.KEY_SCENE) && intent.getStringExtra(XunfeiInsideConstant.KEY_SCENE).equals("com.xike.xkliveplay.testScene")) {
                if (intent.hasExtra(XunfeiInsideConstant.KEY_COMMAND)) {
                    String command = intent.getStringExtra(XunfeiInsideConstant.KEY_COMMAND);
                    if ("key1".equals(command)) {
                        mFeedback.feedback("退出", Feedback.SILENCE);
                        // key1 -> 固定命令(打开， 打开详情)
                        System.out.println("#####:讯飞反馈的命令：" + command);
                        if (curFragment != null) {
                            if (curFragment instanceof FragmentBackPlayBase && VarParam.url.contains("msnm")) {
                                curFragment.jumpToLive();
                            } else {
                                curFragment.forceCloseAPK();
                            }
                        }
                    } else if ("key2".equals(command)) {
                        dealWithXunFeiInsideAction(intent.getExtras());
                    }
                }
            }
        }
    };

    /**
     * dealWithXunFeiInsideAction(这里用一句话描述这个方法的作用)
     *
     * @return void    返回类型
     * @modifyHistory createBy Mernake
     */
    protected void dealWithXunFeiInsideAction(Bundle bundle) {
        if (curFragment == null)
            return;
        String feedBackStr = "";
        String action = bundle.getString(XunfeiInsideConstant.KEY_ACTION);
        if (action.equals(XunfeiInsideConstant.ACTION_PLAY)) {
            curFragment.voicePlay();
            feedBackStr = "播放";
        } else if (action.equals(XunfeiInsideConstant.ACTION_BAKCWARD)) {
            if (bundle.containsKey(XunfeiInsideConstant.KEY_OFFSET)) {
                feedBackStr = "快退" + bundle.getInt(XunfeiInsideConstant.KEY_OFFSET) + "秒";
                curFragment.voiceJumpBackwardBySeconds(bundle.getInt(XunfeiInsideConstant.KEY_OFFSET));
            }
        } else if (action.equals(XunfeiInsideConstant.ACTION_FORWARD)) {
            if (bundle.containsKey(XunfeiInsideConstant.KEY_OFFSET)) {
                feedBackStr = "快进" + bundle.getInt(XunfeiInsideConstant.KEY_OFFSET) + "秒";
                //				curFragment.voiceJumpBackwardBySeconds(bundle.getInt(XunfeiInsideConstant.KEY_OFFSET)
                curFragment.voiceJumpForwordBySeconds(bundle.getInt(XunfeiInsideConstant.KEY_OFFSET));
            }
        } else if (action.equals(XunfeiInsideConstant.ACTION_PAUSE)) {
            feedBackStr = "暂停";
            curFragment.voicePause();
        } else if (action.equals(XunfeiInsideConstant.ACTION_RESTART)) {
            feedBackStr = "从头播放";
            curFragment.voiceRestartPlay();
        } else if (action.equals(XunfeiInsideConstant.ACTION_RESUME)) {
            feedBackStr = "恢复播放";
            curFragment.voiceResume();
        } else if (action.equals(XunfeiInsideConstant.ACTION_SEEK)) {
            if (bundle.containsKey(XunfeiInsideConstant.KEY_POSITION)) {
                int seconds = bundle.getInt(XunfeiInsideConstant.KEY_POSITION);
                int mm = seconds / 60;
                int ss = seconds % 60;
                feedBackStr = "跳转到" + mm + "分" + ss + "秒";
                curFragment.voiceJumpToSeconds(bundle.getInt(XunfeiInsideConstant.KEY_POSITION));
            }
        }

        mFeedback.feedback(feedBackStr, Feedback.SILENCE);

        //   	Toast.makeText(getApplicationContext(), "action="+action, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        LogUtil.i(tag, "onKeyDown:", "pressed button keycode is: " + keyCode);
        if (curFragment != null) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                curFragment.onkeyDownReturn(); //This action for return ErrorDialog.
                return true;
            } else {
                curFragment.onKeyDown(keyCode, event);
            }

            if (keyCode == KeyEvent.KEYCODE_MUTE || keyCode == KeyEvent.KEYCODE_VOLUME_MUTE) {
                curFragment.onKeyDown(keyCode, event);
                return super.onKeyDown(keyCode, event);
            }
        }

        if (keyCode == KeyEvent.KEYCODE_BACK)  // Maybe not used.
        {
            finish();
        }
        return true;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return super.onKeyUp(keyCode, event);
    }


    @Override
    public void updateData(String method, String uniId, Object object, boolean isSuccess) {

    }


    public void delSqliteDatabase() {
        if (dbManager != null) {
            dbManager.deleteAll("category");
            dbManager.deleteAll("channel");
            dbManager.deleteAll("xunfei");
        }
    }

    /**
     * function: Request category list with specific category id.
     *
     * @param
     * @return
     */
    public void postGetCategoryList(String id) {
        HttpUtil.create().postGetCategoryList(this, id);
    }

    /**
     * function: Request channel list with specific category id.
     *
     * @param
     * @return
     */
    public void postGetContentList(String categoryId) {
        HttpUtil.create().postGetContentList(this, categoryId);
    }

    /**
     * function: Get favorite channel list. Maybe useless here.
     *
     * @param
     * @return
     */
    public void postGetFavoriteList() {
        HttpUtil.create().postGetFavoriteList(this);
    }


    @SuppressWarnings("unchecked")
    public int dealResWithCategorys(Object object) {

        if (((List<Category>) object).size() <= 0) {
            return 0;
        }
        Var.allCategorys = (List<Category>) object;
        // 获得频道下的所有节目
        Category category = getAllChannelCategory(Var.allCategorys);
        if (category != null) {
            Var.allCategoryId = category.getId();
            postGetContentList(Var.allCategoryId);
        }

        Category category1 = new Category();
        category1.setId(Var.Category_Recent);
        category1.setName("最近");
        Var.allCategorys.add(category1);

        Category categorySave = new Category();
        categorySave.setId("save");
        categorySave.setName("收藏");
        Var.allCategorys.add(categorySave);
        return 1;
    }

    /**
     * 处理获取指定频道类别下的所有频道
     *
     * @param object
     * @param uniId  ： 请求响应ID
     */
    @SuppressWarnings("unchecked")
    public int dealResWithContentChannels(Object object, String uniId) {
        if (((List<ContentChannel>) object).size() <= 0) {
            return 0;
        }
        if (uniId == Var.allCategoryId) {
            //			Var.allChannels = APKTools.getOrderList((List<ContentChannel>) object);
            Var.allChannels = (List<ContentChannel>) object;
        }
        return 1;
    }

    /**
     * 获取所有收藏信息
     *
     * @param object
     */
    @SuppressWarnings("unchecked")
    public void dealResFavorites(Object object) {
        saveList = (List<ContentChannel>) object;
        for (int i = 0; i < saveList.size(); i++) {
            for (int j = 0; j < Var.allChannels.size(); j++) {
                if (saveList.get(i).getChannelNumber().equals(Var.allChannels.get(j).getChannelNumber())) {
                    Var.allChannels.get(j).setZipCode(Var.Category_Save);
                }
            }
        }
        LogUtil.e(tag, "dealResFavorites", "频道列表缓存成功");
    }

    /**
     * function: 跳转到直播界面，当收藏列表缓存成功或失败时调用
     *
     * @param
     * @return
     */
    public void jumpLive() {
        Category category = getAllChannelCategory(Var.allCategorys);

        if (category != null) {
            Var.allCategoryId = category.getId();
        }
        jumpToFragment();
    }

    public class SaveThread extends Thread {
        @Override
        public void run() {
            saveDataToDB();
        }
    }

    /**
     * function: 将缓存内容存入数据库
     *
     * @param
     * @return
     */
    private void saveDataToDB() {
        dbManager.deleteAll("category");
        for (Category category2 : Var.allCategorys)  //缓存类别列表
        {
            dbManager.add("category", category2);
        }

        XunfeiTools.getInstance().saveChannels(dbManager, getApplicationContext());//缓存频道列表进xunfei的表
        AbSharedUtil.putInt(getApplicationContext(), Var.KEY_ISFINISHED, 1);
        dbManager.closeDB();
    }

    /**
     * 获得userId
     **/
    public int getAccountInfo(GetAccountInfoRes getAccountInfoRes, String pluginUserId) {
        if (getAccountInfoRes == null || getAccountInfoRes.getAccount() == null) {
            return 0;
        }
        userId = getAccountInfoRes.getAccount();
        userPassWord = getAccountInfoRes.getPassword();
        //这里要添加一个逻辑，当getaccountinfo返回的userid和运营商鉴权插件返回的userid不一致时，要强制访问激活接口

        if (!userId.equals(pluginUserId)) {
            return 2;
        }

        if (userId.length() <= 1) {
            LogUtil.e(tag, "getAccountInfo", "because userId's length is less than 1 ,so Auth failed");
            return 0;
        }
        if (!userId.equals("")) {
            stb_map.put("userId", userId);
            stb_map.put("userPassWord", userPassWord);
            return 1;
        }
        return 0;
    }

    /**
     * 发送用户认证A接口请求
     **/
    public void postAuthentication(String userId2) {
        HttpUtil.create().postAuthentication(this, userId2);
    }

    /**
     * 获得encryToken
     **/
    public int getEncryToken(AuthenticationRes res) {
        if (res == null) {
            return 0;
        }
        encryToken = res.getEncryToken();
        if (!encryToken.equals("")) {
            return 1;
        }
        return 0;
    }

    /**
     * 获取用户auth认证的主要信息
     **/
    public int getAuth(AuthRes authRes) {
        if (authRes == null) {
            return 0;
        }
        userToken = authRes.getUserToken();
        epg_domain_url = authRes.getEpgDomain();
        drm_domain_url = authRes.getDrmDomain();
        VarParam.url = epg_domain_url;
        stb_map.put("epg_domain_url", epg_domain_url);
        stb_map.put("drm_domain_url", drm_domain_url);
        if (!userToken.equals("")) {
            stb_map.put("userToken", userToken);
            return 1;
        }
        return 0;
    }

    /**
     * function: 开始请求类别频道信息
     *
     * @param
     * @return
     */
    public void startLiveRequest() {
        if (Var.initStbVars(getApplicationContext()) == 1) {
            postGetCategoryList("0");
        }
    }

    /**
     * 发送获取userId的请求
     **/
    public void postGetAccountInfo() {
        String mac = getReqMac();
        if (mac == null) {
            return;
        }
        HttpUtil.create().postGetAccountInfo(this, mac, this);
    }

    /**
     * 返回格式为ac:4a:fe:44:cd:c9的mac地址
     * ac4afe44cdc9
     * ac:4afe44cdc9
     * ac:4a:fe44cdc9
     * ac:4a:fe:44cdc9
     * ac:4a:fe:44:cdc9
     * ac:4a:fe:44:cd:c9
     **/
    private String getReqMac() {
        String mac = GetStbinfo.getLocalMacAddress();
        if (mac == null) {
            SendBroadcastTools.sendErrorBroadcast(this, ErrorBroadcastAction.ERROR_READ_MAC_ACTION, "mac地址为空", "");
            return null;
        } else {
            boolean isMac = AuthTools.isMac(mac);
            if (!isMac) {
                SendBroadcastTools.sendErrorBroadcast(this, ErrorBroadcastAction.ERROR_READ_MAC_ACTION, mac, "");
                return null;
            }
        }
        LogUtil.e(tag, "getReqMac", "mac = " + mac + "mac.length = " + mac.length());
        stb_map.put("mac", mac.trim());
        return mac.trim();
    }

    /**
     * 发送用户认证b接口的请求
     **/
    public void postAuth() {
        String mac = getReqMac();
        if (mac == null) {
            return;
        }
        HttpUtil.create().postAuth(this, mac, encryToken, getPsdnIp(), userId, userPassWord);
    }

    /**
     * 用来获取手机拨号上网（包括CTWAP和CTNET）时由PDSN分配给手机终端的源IP地址。
     *
     * @return
     * @throws SocketException
     * @author SHANHY
     */
    @SuppressLint("DefaultLocale")
    public static String getPsdnIp() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                if (intf.getName().toLowerCase().equals("eth0") || intf.getName().toLowerCase().equals("wlan0")) {
                    for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                        InetAddress inetAddress = enumIpAddr.nextElement();
                        if (!inetAddress.isLoopbackAddress()) {
                            String ipaddress = inetAddress.getHostAddress().toString();
                            if (!ipaddress.contains("::")) {// ipV6的地址
                                return ipaddress;
                            }
                        }
                    }
                } else {
                    continue;
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }

        return "";
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mScene.release();
        //		stopAuthenticationService(this);
    }

    public void jumpToFragment() {
        if (Var.allCategorys.size() == 0 || Var.allChannels.size() == 0) {
            LogUtil.e(tag, "jumpToFragment:", "Categories size is 0 or Channels size is 0!");
            return;
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        Bundle bundle = new Bundle();
        switch (curType) {
            case LaunchType.TYPE_LIVE:
                bundle.putBoolean("type", false);
                bundle.putInt("index", Var.allCategoryIndex);
                bundle.putBoolean("first", true);
                curFragment = new FragmentLivePlayBase(this);
                curFragment.setArguments(bundle);
                break;
            case LaunchType.TYPE_SAVE:
                bundle.putBoolean("type", true);
                bundle.putInt("index", Var.allCategorys.size() - 1);
                curFragment = new FragmentLivePlayBase(this);
                curFragment.setArguments(bundle);
                break;
            case LaunchType.TYPE_REPLAY:
                curFragment = new FragmentBackPlayBase(this);
                break;
            case LaunchType.TYPE_RECOMMEND:
                curFragment = new FragmentRecommendBase(this);
                break;

            default:
                break;
        }

        fragmentTransaction.add(R.id.root_liveplay, curFragment);
        fragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    public void jumpToFramgment(int fragmentType, int curIndex) {
        LogUtil.i(tag, "jumpToFramgment -->", "fragmentType:" + fragmentType + ",curIndex:" + curIndex);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Bundle bundle = new Bundle();

        if (fragmentType == LaunchType.TYPE_REPLAY) {
            bundle.putInt("index", curIndex);
            curFragment = new FragmentBackPlayBase(this);
            curFragment.setArguments(bundle);
        } else {
            switch (fragmentType) {
                case LaunchType.TYPE_LIVE:
                    bundle.putBoolean("type", false);
                    bundle.putInt("index", Var.allCategoryIndex);
                    curFragment = new FragmentLivePlayBase(this);
                    curFragment.setArguments(bundle);
                    break;
                case LaunchType.TYPE_SAVE:
                    bundle.putBoolean("type", true);
                    bundle.putInt("index", Var.allCategorys.size() - 1);
                    curFragment = new FragmentLivePlayBase(this);
                    curFragment.setArguments(bundle);
                    break;
                default:
                    break;
            }
        }

        fragmentTransaction.replace(R.id.root_liveplay, curFragment);
        fragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    public void jumpToFragmentWithBundles(int fragmentType, int curIndex, Bundle bundle) {
        LogUtil.i(tag, "####jumpToFramgmentWithBundes -->", "fragmentType:" + fragmentType + ",curIndex:" + curIndex);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (fragmentType == LaunchType.TYPE_REPLAY) {
            bundle.putInt("index", curIndex);
            curFragment = new FragmentBackPlayBase(this);
            curFragment.setArguments(bundle);
        } else {
            switch (fragmentType) {
                case LaunchType.TYPE_LIVE:
                    bundle.putBoolean("type", false);
                    if ( -1 == bundle.getInt("index", -1) ) {//如果启动直播的时候带着Index，那么就用带过来的，否则就用全部
                        bundle.putInt("index", Var.allCategoryIndex);
                    }
                   // bundle.putInt("index", Var.allCategoryIndex);
                    curFragment = new FragmentLivePlayBase(this);
                    curFragment.setArguments(bundle);
                    break;
                case LaunchType.TYPE_SAVE:
                    bundle.putBoolean("type", true);
                    bundle.putInt("index", Var.allCategorys.size() - 1);
                    curFragment = new FragmentLivePlayBase(this);
                    curFragment.setArguments(bundle);
                    break;
                default:
                    break;
            }
        }

        fragmentTransaction.replace(R.id.root_liveplay, curFragment);
        fragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    public void jumpToVod(Bundle bundle) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

       if(APKTools.isPkgInstalled(this,"com.shandong.vod")){
           ComponentName cn = new ComponentName("com.shandong.vod", "com.shandong.vod.ui.activity.GoActivity");
           intent.setComponent(cn);
           intent.putExtras(bundle);
           startActivity(intent);
           //		this.finish();
       }else{
           UIUtils.showToast("点播APK没有安装！");
       }

    }

    /**
     * function: Get 'ALL' category.
     *
     * @param
     * @return
     */
    private Category getAllChannelCategory(List<Category> list) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getName().equals("全部")) {
                Var.allCategoryIndex = i;
                return list.get(i);
            }
        }

        return null;
    }

    /**
     * function:Get 'ALL' category index in group.
     *
     * @param
     * @return
     */
    @SuppressWarnings("unused")
    private void getAllCategoryIndex(List<Category> list) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getName().equals("全部")) {
                Var.allCategoryIndex = i;
                return;
            }
        }
    }


    /**
     * function:Animation controller.
     *
     * @param
     * @return
     */
    private void showFrameAnimation(boolean isShow) {
        if (isShow) {
            if (faaImage != null) {
                faaImage.setVisibility(View.GONE);
                faaImage.setVisibility(View.VISIBLE);
                faaImage.bringToFront();
                mLoadingView = (AnimationDrawable) faaImage.getBackground();
            }
            if (mLoadingView != null)
                mLoadingView.start();
        } else {
            if (mLoadingView != null) {
                mLoadingView.stop();
            }
            if (faaImage != null) {
                faaImage.setVisibility(View.GONE);
            }

        }
    }


    /**
     * function:Make /mnt/sdcard/p2p directory if it's not exist.
     *
     * @param
     * @return
     */
    private void makeDirIfNop2p() {
        String path = "mnt/sdcard/p2p";
        File file = new File(path);
        if (file == null || !file.isDirectory()) {
            LogUtil.i(tag, "makeDirIfNop2p", "there is no /mnt/sdcard/p2p directory,recreate");
            file.mkdir();
        } else {
            LogUtil.i(tag, "makeDirIfNop2p", "there is /mnt/sdcard/p2p directory");
        }
    }

    /*********************** UPDATE ******************************************/

    @SuppressWarnings("unused")
    private String request = null;
    @SuppressWarnings("unused")
    private OutputStream outStream;
    @SuppressWarnings("unused")
    private String jsonStr;
    public ProgressDialog pBar;

    private static final int MSG_UPDATE_BEGIN_UPDATE = 0;
    private static final int MSG_UPDATE_INIT_PROGRESSBAR = 1;
    private static final int MSG_UPDATE_PROGRESS_UPDATE = 2;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MSG_UPDATE_BEGIN_UPDATE) //Already downloaded,next should be update.
            {
                pBar.setCancelable(true);
                pBar.cancel();
                //	            update();
            } else if (msg.what == MSG_UPDATE_INIT_PROGRESSBAR) //Ready to download,init progressbar.
            {
                pBar.setMax(msg.arg1);
                pBar.setProgress(0);
            } else if (msg.what == MSG_UPDATE_PROGRESS_UPDATE) //Downloading,keep on updating progressbar.
            {
                pBar.setProgress(msg.arg1);
            }
        }
    };

    /**
     *
     * function:If need update function,invoke this method.
     * @param
     * @return
     */
    //	public boolean updateIfNeedUpdate()
    {
        //
        //		LiveUpgrageRequest request = getUpdateVaram();
        //		if (request == null)
        //		{
        //			LogUtil.i(tag, "updateIfNeedUpdate", "liveplay update error!");
        //			return false;
        //		}
        //		HttpGetUpdate httpGetUpdate = new HttpGetUpdate(request, this);
        //		httpGetUpdate.post();
        //		return true;
        //	}

        //	private LiveUpgrageRequest getUpdateVaram()
        //	{
        //		LiveUpgrageRequest getUpdateReq = new LiveUpgrageRequest();
        //		String operator = SystemProperties.get("ro.build.operator");
        //		String hard = SystemProperties.get("ro.build.hard");
        //		String equipment = SystemProperties.get("ro.build.equipment");
        //		LogUtil.e(tag, "getUpdateVaram:","operator= " + operator + " hard= " + hard + " equipment= " + equipment);
        //
        //		if (operator.equals("") || hard.equals("") || equipment.equals(""))
        //		{
        //			return null;
        //		}
        //		getUpdateReq.setMac(GetStbinfo.getLocalMacAddress().trim());
        //		getUpdateReq.setEquipment(equipment);
        //		getUpdateReq.setHard(hard);
        //		getUpdateReq.setOperators(operator);
        //
        //		getUpdateReq.setVersion(GetStbinfo.getStrVersion(this));
        //		return getUpdateReq;
        //	}


        //	/**
        //	 *
        //	 * function:Install apk with hint.
        //	 * @param
        //	 * @return
        //	 */
        //	public void update()
        //	{
        //		if (!APKTools.isAPKComplete(getApplicationContext(), downloadApkPath))
        //		{
        //			LogUtil.e(tag, "update", "Pity,the download apk is not complete!");
        //			return;
        //		}
        //        Intent intent = new Intent(Intent.ACTION_VIEW);
        //        intent.setDataAndType(Uri.fromFile(new File(downloadApkPath)),
        //                "application/vnd.android.package-archive");
        //        startActivity(intent);
        //		silentInstallAPK(Environment.getExternalStorageDirectory()+"/xklive.apk");
        //	 }

        //	/**
        //	 *
        //	 * function: Silent install apk.
        //	 * @param
        //	 * @return
        //	 */
        //	@SuppressWarnings("unused")
        //	private void silentInstallAPK(String apkPath)
        //	{
        //		Uri uri = Uri.fromFile(new File(apkPath));
        //
        //		int installFlags = 0;
        //		PackageManager pm = getPackageManager();
        //		String packageName = getPackageName();
        //		try
        //		{
        //			PackageInfo pi = pm.getPackageInfo(packageName,
        //					PackageManager.GET_UNINSTALLED_PACKAGES);
        //			if (pi != null)
        //			{
        //				installFlags |= PackageManager.INSTALL_REPLACE_EXISTING;
        //			}
        //		} catch (NameNotFoundException e)
        //		{
        //			e.printStackTrace();
        //		}
        //		PackageInstallObserver observer = new PackageInstallObserver();
        //		pm.installPackage(uri, observer, installFlags, packageName);

    }

    //	class PackageInstallObserver extends IPackageInstallObserver.Stub
    //	{
    //		public void packageInstalled(String packageName, int returnCode)
    //		{
    //			LogUtil.e(tag, "PackageInstallObserver:","install result is: " + returnCode);
    //		}
    //	};

    /**
     * function: Deal with update returned message.
     *
     * @param
     * @return
     */
    public void dealWithUpdate(LiveUpgrageResponse object) {
        if (object.getUrl() != null && object.getUrl().length() > 5) {
            showUpdateDialog(object.getUrl(), true);
        } else {
            paramSetting2();
            //			bindYDAuthComponent();
        }
    }

    /**
     * function:Show update dialog.
     *
     * @param
     * @return
     */
    public void showUpdateDialog(final String url, boolean isForce) {
        Dialog dialog = new AlertDialog.Builder(ActivityLaunchBase.this).setTitle("软件更新").setMessage("发现新版本，是否更新")
                // set contents.
                .setPositiveButton("更新",// set confirm button.
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                pBar = new ProgressDialog(ActivityLaunchBase.this);
                                pBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                                pBar.setTitle("正在下载");
                                downFile(url);
                            }
                        }).setNegativeButton("暂不更新", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                        //You should put those bind auth aidl service code here.
                        //								paramSetting2();
                        //								bindYDAuthComponent();
                    }
                }).create();

        dialog.show();
    }

    public void downFile(final String url) {
        pBar.show();
        new Thread() {
            @SuppressLint("WorldReadableFiles")
            public void run() {
                HttpClient client = new DefaultHttpClient();
                HttpGet get = new HttpGet(url);
                HttpResponse response;
                try {
                    response = client.execute(get);
                    HttpEntity entity = response.getEntity();
                    long length = entity.getContentLength();
                    Message msg = Message.obtain();
                    msg.arg1 = (int) length;
                    msg.what = MSG_UPDATE_INIT_PROGRESSBAR;
                    handler.sendMessage(msg);
                    InputStream is = entity.getContent();
                    FileOutputStream fileOutputStream = null;
                    if (is != null) {
                        @SuppressWarnings("unused") File file = new File(downloadApkPath);
                        fileOutputStream = getApplicationContext().openFileOutput("xklive.apk", MODE_WORLD_READABLE);
                        byte[] buf = new byte[1024];
                        int ch = -1;
                        int count = 0;
                        while ((ch = is.read(buf)) != -1) {
                            fileOutputStream.write(buf, 0, ch);
                            count += ch;
                            msg = Message.obtain();
                            msg.arg1 = count;
                            msg.what = MSG_UPDATE_PROGRESS_UPDATE;
                            handler.sendMessage(msg);
                            if (length > 0) {
                            }
                        }
                    }
                    fileOutputStream.flush();
                    if (fileOutputStream != null) {
                        fileOutputStream.close();
                    }
                    handler.sendEmptyMessage(MSG_UPDATE_BEGIN_UPDATE);
                } catch (ClientProtocolException e) {
                    cancelUpdatePbar();
                    e.printStackTrace();
                } catch (IOException e) {
                    cancelUpdatePbar();
                    e.printStackTrace();
                }
            }
        }.start();
    }

    /**
     * function:Cancel update progress bar.
     *
     * @param
     * @return
     */
    private void cancelUpdatePbar() {
        pBar.setCancelable(true);
        pBar.dismiss();
        paramSetting2();
        //  	  bindYDAuthComponent();
    }

    private void makeIfNoSDcard() {
        File file = new File("/mnt/sdcard");
        if (file == null || !file.isDirectory()) {
            file.mkdir();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        NetStatusChange.getInstance().unReg(this);
        //		System.out.println("###直播APK退出### 网信退出APK函数的返回值是： " + Common.closeApk());
    }

    /**
     * function: 判断直播是否已经启动
     *
     * @param
     * @return
     */
    public boolean isLiveStarted(Context context) {
        String processName = "com.xike.xkliveplay";
        ActivityManager am = (ActivityManager) context.getSystemService(Activity.ACTIVITY_SERVICE);
        // The first in the list of RunningTasks is always the foreground task.
        RunningTaskInfo foregroundTaskInfo = am.getRunningTasks(1).get(0);
        if (foregroundTaskInfo.baseActivity.getPackageName().equals(processName)) {
            return false;
        }

        return true;
    }

    /**
     * function:
     *
     * @param
     * @return
     */
    public void showDownload(String url) {
        pBar = new ProgressDialog(this);
        pBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pBar.setTitle("正在下载升级包");
        downFile(url);
    }

    /**
     * function:获取到非强制更新dialog
     *
     * @param
     * @return
     */
    public Dialog getNonForceDialog(final String url) {
        Dialog dialog = new AlertDialog.Builder(ActivityLaunchBase.this).setTitle("软件更新").setMessage("发现新版本，是否更新")
                // set contents.
                .setPositiveButton("更新",// set confirm button.
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                pBar = new ProgressDialog(ActivityLaunchBase.this);
                                pBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                                pBar.setTitle("正在下载");
                                downFile(url);
                            }
                        }).setNegativeButton("暂不更新", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                        //						iProcessResult.returnMessage(MessageType.TYPE_UPDATE, MessageType.MSG_FAILED);
                        paramSetting2();
                    }
                }).create();
        return dialog;
    }

    public void showGDErrorDialog(String code, String msg, String mac, String userid) {

    }

    public int dealResWithCategorysWithoutRequestLiveContent(Object object) {

        if (object == null || ((List<Category>) object).size() <= 0) {
            return 0;
        }

        Var.allCategorys = new ArrayList<Category>();
        // 获得“全部类别”
        Category category = getAllChannelCategory(((List<Category>) object));
        if (category != null) {
            Var.allCategoryId = category.getId();
        }
        Var.allCategorys.add(category);//添加全部
        Category category1 = new Category(); //添加常看
        category1.setId(Var.Category_Recent);
        category1.setName("常看频道");
        Var.allCategorys.add(category1);

        for (int i = 0; i < ((List<Category>) object).size(); i++) {
            if (category.getId().equals(((List<Category>) object).get(i).getId())) {
                continue;
            }
            Var.allCategorys.add(((List<Category>) object).get(i));
        }
        return 1;
    }

    //	public void jumpToFragmentWithBundles(int fragmentType, int curIndex, Bundle bundle)
    //	{
    //		LogUtil.i(tag, "####jumpToFramgmentWithBundes -->","fragmentType:" + fragmentType + ",curIndex:" + curIndex);
    //		FragmentManager fragmentManager = getSupportFragmentManager();
    //		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    //
    //		if(fragmentType == LaunchType.TYPE_REPLAY)
    //		{
    //			bundle.putInt("index", curIndex);
    //			curFragment = new FragmentBackPlayBase(this);
    //			curFragment.setArguments(bundle);
    //		}else {
    //			switch (fragmentType)
    //			{
    //			case LaunchType.TYPE_LIVE:
    //				bundle.putBoolean("type", false);
    //				bundle.putInt("index", Var.allCategoryIndex);
    //				curFragment = new FragmentLivePlayBase(this);
    //				curFragment.setArguments(bundle);
    //				break;
    //			case LaunchType.TYPE_SAVE:
    //				bundle.putBoolean("type", true);
    //				bundle.putInt("index", Var.allCategorys.size() - 1);
    //				curFragment = new FragmentLivePlayBase(this);
    //				curFragment.setArguments(bundle);
    //				break;
    //			default:
    //				break;
    //			}
    //		}
    //
    //		fragmentTransaction.replace(R.id.root_liveplay,curFragment);
    //		fragmentTransaction.commitAllowingStateLoss();
    //	}


}
