package com.xike.xkliveplay.activity.launch;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.mernake.framework.tools.MernakeSharedTools;
import com.xike.xkliveplay.R;
import com.xike.xkliveplay.activity.ActivityLaunchBase;
import com.xike.xkliveplay.activity.LaunchType;
import com.xike.xkliveplay.activity.dialogerror.DialogBroadcastReceiver;
import com.xike.xkliveplay.activity.dialogerror.gd.GDDialogTools;
import com.xike.xkliveplay.activity.fragment.FragmentBackPlayBase;
import com.xike.xkliveplay.activity.fragment.FragmentLivePlayBase;
import com.xike.xkliveplay.activity.fragment.FragmentRecommendBase;
import com.xike.xkliveplay.framework.entity.ActivateTerminal;
import com.xike.xkliveplay.framework.entity.ActivateTerminalRes;
import com.xike.xkliveplay.framework.entity.AuthRes;
import com.xike.xkliveplay.framework.entity.AuthenticationRes;
import com.xike.xkliveplay.framework.entity.Category;
import com.xike.xkliveplay.framework.entity.ContentChannel;
import com.xike.xkliveplay.framework.entity.GetAccountInfoRes;
import com.xike.xkliveplay.framework.entity.LiveUpgrageResponse;
import com.xike.xkliveplay.framework.entity.gd.GDAuth4in1Res;
import com.xike.xkliveplay.framework.entity.gd.GDAuthAidlRes;
import com.xike.xkliveplay.framework.entity.gd.GDAuthGetStaticContent;
import com.xike.xkliveplay.framework.entity.gd.GDAuthLiveCategory;
import com.xike.xkliveplay.framework.error.ErrorBroadcastAction;
import com.xike.xkliveplay.framework.error.SendBroadcastTools;
import com.xike.xkliveplay.framework.http.IUpdateData;
import com.xike.xkliveplay.framework.httpclient.HttpActivateTerminal;
import com.xike.xkliveplay.framework.httpclient.HttpAuth;
import com.xike.xkliveplay.framework.httpclient.HttpAuthentication;
import com.xike.xkliveplay.framework.httpclient.HttpGetAccountInfo;
import com.xike.xkliveplay.framework.httpclient.HttpGetCategoryList;
import com.xike.xkliveplay.framework.httpclient.HttpGetContentList;
import com.xike.xkliveplay.framework.httpclient.HttpGetFavoriteList;
import com.xike.xkliveplay.framework.httpclient.HttpGetUpdate;
import com.xike.xkliveplay.framework.httpclient.VarParam;
import com.xike.xkliveplay.framework.service.LivePlayBackService;
import com.xike.xkliveplay.framework.tools.APKTools;
import com.xike.xkliveplay.framework.tools.AbStrUtil;
import com.xike.xkliveplay.framework.tools.AuthTools;
import com.xike.xkliveplay.framework.tools.GetStbinfo;
import com.xike.xkliveplay.framework.tools.GetVaram;
import com.xike.xkliveplay.framework.tools.LogUtil;
import com.xike.xkliveplay.framework.tools.MacTool;
import com.xike.xkliveplay.framework.tools.NetStatusChange;
import com.xike.xkliveplay.framework.tools.NetStatusChange.INetStatusChanged;
import com.xike.xkliveplay.framework.tools.P2pSettingTool;
import com.xike.xkliveplay.framework.varparams.Var;
import com.xike.xkliveplay.gd.GDHttpTools;
import com.xike.xkliveplay.xunfei.XunfeiTools;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import tv.cmcc.vendor.aidl.IAuthenticationService;


public class ActivityLaunch extends ActivityLaunchBase {
    private static final String tag = "ActivityLaunch";

    private int INVOKE_YD_AUTH = 3;
    private int MSG_INVOKE_AUTH_ERROR = 4;

    private String[] idMacArray = {"",""};

    private ActivateTerminal activate_req = null;

    private DialogBroadcastReceiver receiver = null;

    private void regReceiver() {
        receiver = new DialogBroadcastReceiver(ActivityLaunch.this);
        IntentFilter filter = new IntentFilter();
        filter.addAction(ErrorBroadcastAction.ERROR_LIVEPLAY_ACTION);
        this.registerReceiver(receiver, filter);
    }

    private void unRegReceiver() {
        if (receiver != null) {
            this.unregisterReceiver(receiver);
            receiver = null;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (serviceConnection != null) {
            unbindService(serviceConnection);
        }
        stopService(new Intent(this, LivePlayBackService.class));
        unRegReceiver();
        sendXunfeiExitBroadcast();
    }


    /**
     * sendXunfeiBroadcast(这里用一句话描述这个方法的作用)
     *
     * @return void    返回类型
     * @modifyHistory createBy Mernake
     */
    private void sendXunfeiExitBroadcast() {
        if (curFragment == null) {
            return;
        }

        if (curFragment instanceof FragmentLivePlayBase) {
            XunfeiTools.getInstance().sendLiveStatusBroadcast(this, false);
        } else if (curFragment instanceof FragmentBackPlayBase) {
            XunfeiTools.getInstance().sendBackPlayBroadcast(this, false);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MernakeSharedTools.setSharedFileName("FragmentLivePlay");
        P2pSettingTool.FILE_CONTENT = "front_move=10\ncache_maxnum=10\nuse_mem_file=1\nrep2server_enable=0\nserver_port=65500\nserver_host=cmco.snctv.cn\nlog_logcat=1\nuse_dns=1\n";
        FragmentLivePlayBase.TIME_SHIFT_TOTAL_TIME = 60 * 3;
        Var.netIP = APKTools.getPsdnIp();


        GDHttpTools.getInstance().setGDI6Enable(true);
        GDHttpTools.getInstance().initSDK(this);
        settings();

        super.onCreate(savedInstanceState);
        NetStatusChange.getInstance().setiChanged(iChange);
        if (!NetStatusChange.getInstance().checkNet(this))        // Check network is ok.
        {
            return;
        }
        startService(new Intent(this, LivePlayBackService.class));
    }

    private void settings() {
        Var.isStatic = true;
        Var.isZZEnabled = true;
        GDHttpTools.getInstance().setGDOrderEnable(true);
        //		Toast.makeText(getApplicationContext(),"修改了SDK的域名为测试域名："+"http://223.99.253.49:8080",Toast.LENGTH_LONG).show();
        //GDHttpTools.getInstance().setLiveUrl("http://223.99.253.49:8080");  //测试地址
        VarParam.url = VarParam.CM_URL;
        VarParam.default_url = VarParam.CM_DEFAULT_URL;
        VarParam.update_url = VarParam.CM_UPDATE_URL;
    }

    private INetStatusChanged iChange = new INetStatusChanged() {
        @Override
        public void onNetStatusChange(boolean isConnected) {
            Log.i("MARK","onNetStatusChange isConnected=="+isConnected+"  curFragment=="+curFragment);
            if (curFragment == null) {
                //		    	readDefault();
                //首先要赋值tag，也就是需要进行getAIDL访问
                int tag = 1;
                GDHttpTools.getInstance().getAIDLData(getApplicationContext(), tag + "", new IUpdateData() {
                    @Override
                    public void updateData(String method, String uniId, Object object, boolean isSuccess) {
                        if (isSuccess && method.equals(GDHttpTools.METHOD_GETAIDLDATA)) {
                            GDAuthAidlRes res = (GDAuthAidlRes) object;
                            String operaTag = "";
                            if (res.getData().getOperaTag().equals("HW")) {
                                operaTag = "1";
                            } else if (res.getData().getOperaTag().equals("ZTE")) {
                                operaTag = "2";
                            }
                            GDHttpTools.getInstance().setTag(operaTag);
                        }
                    }
                });
                boolean isLiveStarted = (Boolean) MernakeSharedTools.get(getApplicationContext(), "isLiveStarted", false);
                Log.i("MARK","isLiveStarted=="+isLiveStarted);
                if (isLiveStarted) { //如果为真，则说明直播已经启动过了
                    boolean jumpSign = (Boolean) MernakeSharedTools.get(getApplicationContext(), "jumpSign", false);
                    Log.i("MARK","jumpSign=="+jumpSign);
                    if (jumpSign) { //如果为真，说明要跳过
                        //Log.i("MARK","paramSetting2()=="+paramSetting2());
                        if (!paramSetting2()) { //如果paramSetting的返回值是真就不用处理，会直接跳转直播的
                            //如果是假的，说明缓存缺失，按照旭哥的逻辑，应该是去读取预置频道列表的
                            readDefault();
                        }
                    } else {//如果为假，说明不能跳过
                        GDHttpTools.getInstance().getAIDLData(getApplicationContext(), tag + "", iGDupdata);
                    }
                } else { //说明这次启动机顶盒之后，直播还没有启动过
                    GDHttpTools.getInstance().getAIDLData(getApplicationContext(), tag + "", iGDupdata);
                }
            }else if(curFragment instanceof FragmentRecommendBase){
                if (!NetStatusChange.getInstance().checkNet(ActivityLaunch.this))        // Check network is ok.
                {
                    return;
                }else{
                    ((FragmentRecommendBase)curFragment).getData();
                }
            }
        }
    };

    private boolean isUseDefault = false;

    private void readDefault() {
        isUseDefault = true;
        addALLAndRecentCategory();
        GDHttpTools.getInstance().getDefaultLiveContentList(iGDupdata);
    }

    public void addALLAndRecentCategory() {
        Var.allCategorys = new ArrayList<Category>();
        // 获得“全部类别”
        Category category = new Category();
        category.setId("quanbu");
        category.setName("全部");
        Var.allCategorys.add(category);//添加全部
        Category category1 = new Category(); //添加常看
        category1.setId(Var.Category_Recent);
        category1.setName("最近");
        Var.allCategorys.add(category1);
        Var.allCategoryId = "quanbu";
        Var.allCategoryIndex = 0;
    }

    @Override
    public void updateData(String method, String uniId, Object object, boolean isSuccess) {

        LogUtil.i(tag, "updateData", "updateData start:--> method:" + method + ",uniId:" + uniId + "isSuccess:" + isSuccess);

        if (uniId != null && uniId.equals("500") && method.equals(HttpGetAccountInfo.Method)) {
            if (isActivateTerminal) {
                if (!paramSetting2()) {//报错
                    System.out.println("主要接口出现错误：应该跳过，但是存在缓存缺失，只能报错");
                    SendBroadcastTools.sendErrorBroadcast(this, ErrorBroadcastAction.ERROR_AUTH_EPG_ACTION, "", "");
                }
            } else {
                isActivateTerminal = true;
                activate_req = new ActivateTerminal();
                activate_req.setUserId(idMacArray[0]);
                if (!idMacArray[1].contains(":")) {
                    activate_req.setMac(MacTool.getTypeBMac(idMacArray[1].trim()));
                } else {
                    activate_req.setMac(idMacArray[1].trim());
                }
                HttpActivateTerminal httpActivateTerminal = new HttpActivateTerminal(activate_req, this);
                httpActivateTerminal.post();
            }
            return;
        }

        if (uniId != null && uniId.equals("500")) {
            System.out.println("主要接口出现错误：跳过");
            if (!paramSetting2()) {
                System.out.println("主要接口出现错误：应该跳过，但是存在缓存缺失，只能报错");
                SendBroadcastTools.sendErrorBroadcast(this, ErrorBroadcastAction.ERROR_UNKNOWN_ACTION, "", "");
            }
            return;
        }

        if (uniId != null && uniId.equals("unknownhost")) {
            System.out.println("主要接口出现错误：跳过");
            if (!paramSetting2()) {
                System.out.println("主要接口出现错误：应该跳过，但是存在缓存缺失，只能报错");
                SendBroadcastTools.sendErrorBroadcast(this, ErrorBroadcastAction.ERROR_DNS_ACTION, "", "");
            }
            return;
        }

        if (uniId != null && uniId.equals("sockettimeout")) {
            System.out.println("主要接口出现错误：跳过");
            if (!paramSetting2()) {
                System.out.println("主要接口出现错误：应该跳过，但是存在缓存缺失，只能报错");
                SendBroadcastTools.sendErrorBroadcast(this, ErrorBroadcastAction.ERROR_CONNECT_TIMEOUT, "", "");
            }
            return;
        }

        if (uniId != null && uniId.equals("connectionrefused")) {
            System.out.println("主要接口出现错误：跳过");
            if (!paramSetting2()) {
                System.out.println("主要接口出现错误：应该跳过，但是存在缓存缺失，只能报错");
                SendBroadcastTools.sendErrorBroadcast(this, ErrorBroadcastAction.ERROR_CONNECT_REFUSED, "", "");
            }
            return;
        }

        if (uniId == null && object == null && !method.equals(HttpGetFavoriteList.Method)) {
            System.out.println("主要接口出现错误：跳过");
            if (!paramSetting2()) {
                System.out.println("主要接口出现错误：应该跳过，但是存在缓存缺失，只能报错");
                SendBroadcastTools.sendErrorBroadcast(this, ErrorBroadcastAction.ERROR_UNKNOWN_ACTION, "", "");
            }
            return;
        }

        //获取用户userId接口返回结果
        if (method.equals(HttpGetAccountInfo.Method)) {
            int res = getAccountInfo((GetAccountInfoRes) object, idMacArray[0]);
            if (res == 1) {
                if (isActivateTerminal) {//2.经过激活之后的mac反查，这种就需要强制重新请求stbinfo
                    VarParam.url = VarParam.CM_URL;
                    VarParam.default_url = VarParam.CM_DEFAULT_URL;
                    postAuthentication(userId);
                    return;
                }

                if (!paramSetting2()) {
                    VarParam.url = VarParam.CM_URL;
                    VarParam.default_url = VarParam.CM_DEFAULT_URL;
                    postAuthentication(userId);
                }
                return;
            } else if (res == 2) //运营商鉴权插件的userid和mac反查的userid不一致，强制进行激活接口
            {
                if (isActivateTerminal) {
                    if (!paramSetting2()) {//报错
                        System.out.println("主要接口出现错误：应该跳过，但是存在缓存缺失，只能报错");
                        SendBroadcastTools.sendErrorBroadcast(this, ErrorBroadcastAction.ERROR_ACTIVE_ACOUNT_ACTION, "", "");
                    }
                    return;
                }
                VarParam.url = VarParam.CM_URL;
                VarParam.default_url = VarParam.CM_DEFAULT_URL;
                isActivateTerminal = true;
                activate_req = new ActivateTerminal();
                activate_req.setUserId(idMacArray[0]);
                if (!idMacArray[1].contains(":")) {
                    activate_req.setMac(MacTool.getTypeBMac(idMacArray[1].trim()));
                } else {
                    activate_req.setMac(idMacArray[1].trim());
                }
                HttpActivateTerminal httpActivateTerminal = new HttpActivateTerminal(activate_req, this);
                httpActivateTerminal.post();
            } else {
                SendBroadcastTools.sendErrorBroadcast(this, ErrorBroadcastAction.ERROR_STB_ILLEGAL_ACTION, "", "");
                LogUtil.e(tag, "updateData", HttpGetAccountInfo.Method + " returned is empty");
                return;
            }
        }  //获取用户认证A接口的返回结果
        else if (method.equals(HttpAuthentication.Method)) {
            int res = getEncryToken((AuthenticationRes) object);
            if (res == 1) {
                //开始拼接字符串，准备进行用户认证B接口
                postAuth();
                return;
            } else {
                SendBroadcastTools.sendErrorBroadcast(this, ErrorBroadcastAction.ERROR_AUTH_EPG_ACTION, "", "");
                LogUtil.e(tag, "updateData", HttpAuthentication.Method + " returned is empty");
                return;
            }
        } // 获取用户认证B接口的返回结果
        else if (method.equals(HttpAuth.Method)) {
            int res = getAuth((AuthRes) object);
            if (res == 1) {
                stb_map.put("productId", "1001");
                int saveRes = GetVaram.write2SDcard(stb_map);
                if (saveRes == -1) {
                    //							DialogError.getInstance().showDialog(this, ErrorCode.ERROR_UNKNOWN_EXCEPTION);
                    SendBroadcastTools.sendErrorBroadcast(this, ErrorBroadcastAction.ERROR_WRITE_STBINFO_ACTION, "", "");
                    LogUtil.e(tag, "updateData", "write stbinfo failed");
                    return;
                } else if (saveRes == 0) {
                    LogUtil.e(tag, "updateData", "write stbinfo sucess");
                }
                LogUtil.e(tag, "updateData", "*************Request EPG start**************");
                startLiveRequest();
                //						LogUtil.e(TIME, "updateData","finish all SERVICE params request need: " + System.currentTimeMillis());
                return;
            } else {
                SendBroadcastTools.sendErrorBroadcast(this, ErrorBroadcastAction.ERROR_AUTH_EPG_ACTION, "", "");
                LogUtil.e(tag, "updateData", HttpAuth.Method + " returned is empty");
                return;
            }
        }
        if (method.equals(HttpGetUpdate.Method)) {
            if (object == null || isSuccess == false || ((LiveUpgrageResponse) object).getUrl() == null || ((LiveUpgrageResponse) object).getUrl().length() < 5) {
                bindYDAuthComponent();
                return;
            }
            /**根据must来确定选用何种升级形式**/
            dealWithUpdate((LiveUpgrageResponse) object);
            return;
        }

        if (HttpActivateTerminal.Method.equals(method)) {
            if (object == null || isSuccess == false) {
                SendBroadcastTools.sendErrorBroadcast(this, ErrorBroadcastAction.ERROR_ACTIVE_ACOUNT_ACTION, activate_req.getMac(), activate_req.getUserId());
                return;
            }
            ActivateTerminalRes res = (ActivateTerminalRes) object;
            if (res.getStatus().equals("1")) {
                postGetAccountInfo();
            } else if (res.getStatus().equals("3")) {
                SendBroadcastTools.sendErrorBroadcast(this, ErrorBroadcastAction.ERROR_ACCOUNT_SYNCHRONOUS_ACTION, activate_req.getMac(), activate_req.getUserId());
            } else if (res.getStatus().equals("0")) {
                SendBroadcastTools.sendErrorBroadcast(this, ErrorBroadcastAction.ERROR_ACTIVE_ACOUNT_ACTION, activate_req.getMac(), activate_req.getUserId());
            }

        }

        if (HttpGetCategoryList.Method.equals(method)) {// 获取所有栏目
            LogUtil.e(tag, "updateData", "unid = " + uniId);
            if (uniId == null) {
                SendBroadcastTools.sendErrorBroadcast(this, ErrorBroadcastAction.ERROR_GET_ROOT_CATEGORY_ID_ACTION, "", "");
                return;
            }
            if (uniId.equals("0")) {
                LogUtil.e(tag, "updateData", "访问根类型返回");
                if (object == null) {
                    SendBroadcastTools.sendErrorBroadcast(this, ErrorBroadcastAction.ERROR_GET_ROOT_CATEGORY_ID_ACTION, "", "");
                    return;
                }
                @SuppressWarnings("unchecked") List<Category> categoryList = (List<Category>) object;
                if (categoryList != null && categoryList.size() >= 1) {
                    LogUtil.e(tag, "updateData", "请求所有类别");
                    postGetCategoryList(categoryList.get(0).getId());
                    //					postGetCategoryList(categoryList.get(1).getId());
                    return;
                }

            } else {
                if (object == null) {
                    SendBroadcastTools.sendErrorBroadcast(this, ErrorBroadcastAction.ERROR_GET_CATEGORY_ACTION, "", "");
                    return;
                }
                int res = dealResWithCategorys(object);
                if (res == 0) {
                    SendBroadcastTools.sendErrorBroadcast(this, ErrorBroadcastAction.ERROR_GET_CATEGORY_ACTION, "", "");
                    return;
                }
            }

            return;
        }
        if (HttpGetContentList.Method.equals(method)) {// 获取所有频道
            if (object == null) {
                SendBroadcastTools.sendErrorBroadcast(this, ErrorBroadcastAction.ERROR_GET_CHANNEL_LIST_ACTION, "", "");
                return;
            }

            int res = dealResWithContentChannels(object, uniId);
            if (res == 0) {
                SendBroadcastTools.sendErrorBroadcast(this, ErrorBroadcastAction.ERROR_GET_CHANNEL_LIST_ACTION, "", "");
                return;
            }
            jumpLive();
            new SaveThread().start();
            postGetFavoriteList();
            return;
        }

        /** 收藏请求响应 **/
        if (HttpGetFavoriteList.Method.equals(method)) {
            if (object != null) {
                dealResFavorites(object);
            }
            return;
        }

    }

    /**
     * function: 绑定移动鉴权插件
     *
     * @param
     * @return
     */
    private void bindYDAuthComponent() {
        String sName = "tv.cmcc.vendor.aidl.IAuthenticationService";
        LogUtil.e(tag, "bindYDAuthComponent", "Start to bind AIDL service:" + sName);
        boolean flag = bindService(new Intent(sName), serviceConnection, Context.BIND_AUTO_CREATE);
        LogUtil.e(tag, "bindYDAuthComponent", "Result of bind AIDL service is " + flag);
        if (!flag) {
            hd.sendEmptyMessage(MSG_INVOKE_AUTH_ERROR);
        }
    }

    private IAuthenticationService myService = null;
    private ServiceConnection serviceConnection = null;

    @SuppressLint("HandlerLeak")
    private Handler hd = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                return;
            }
            if (msg.what == 1) //从数据库中读取频道信息
            {
                getInfoFromDatabase();

            }
            if (msg.what == 2) {
                finish();
            }

            if (msg.what == INVOKE_YD_AUTH) {
                postAIDLAuth();
            }

            if (msg.what == MSG_INVOKE_AUTH_ERROR) {
                //				DialogError.getInstance().showDialog(ActivityLaunch.this, ErrorCode.ERROR_AUTH_PLUG_IN_EXCEPTION);
                SendBroadcastTools.sendErrorBroadcast(ActivityLaunch.this, ErrorBroadcastAction.ERROR_AUTH_PLUG_IN_ACTION, "", "");
                return;
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        sendXunfeiEnterBroadcast();
        regReceiver();
    }

    /**
     * sendXunfeiEnterBroadcast(这里用一句话描述这个方法的作用)
     *
     * @return void    返回类型
     * @modifyHistory createBy Mernake
     */
    private void sendXunfeiEnterBroadcast() {
        if (curFragment == null || curFragment instanceof FragmentLivePlayBase) {
            XunfeiTools.getInstance().sendLiveStatusBroadcast(this, true);
        } else if (curFragment instanceof FragmentBackPlayBase) {
            XunfeiTools.getInstance().sendBackPlayBroadcast(this, true);
        }
    }

    /**
     * function:  发送鉴权请求
     *
     * @param
     * @return
     */
    private void postAIDLAuth() {
        String res = null;
        try {
            res = myService.Authentication("{}");
            if (res == null) {
                SendBroadcastTools.sendErrorBroadcast(this, ErrorBroadcastAction.ERROR_AUTH_PLUG_IN_ACTION, "", "");
                return;
            }
        } catch (RemoteException e1) {
            SendBroadcastTools.sendErrorBroadcast(this, ErrorBroadcastAction.ERROR_AUTH_PLUG_IN_ACTION, "", "");
            e1.printStackTrace();
            return;
        }
        LogUtil.e(tag, "postAuth", "res =  " + res);
        idMacArray = getIdAndMac(res);
        if (idMacArray == null) {
            SendBroadcastTools.sendErrorBroadcast(this, ErrorBroadcastAction.ERROR_AUTH_PLUG_IN_ACTION, "", "");
            return;
        }

        if (idMacArray != null && idMacArray[1].equals("")) {
            SendBroadcastTools.sendErrorBroadcast(this, ErrorBroadcastAction.ERROR_AUTH_PLUG_IN_ACTION, idMacArray[0], "");
            return;
        }

        if (!AuthTools.isMac(MacTool.getTypeBMac(idMacArray[1].trim()))) {
            SendBroadcastTools.sendErrorBroadcast(this, ErrorBroadcastAction.ERROR_AUTH_PLUG_IN_ACTION, idMacArray[1], "");
            return;
        }

        postGetAccountInfo();
        //		//解析得到的结果，并同步给西维尔平台；
        //		activate_req = new ActivateTerminal();
        //		activate_req.setUserId(idMacArray[0]);
        //		if (!idMacArray[1].contains(":"))
        //		{
        //			activate_req.setMac(MacTool.getTypeBMac(idMacArray[1].trim()));
        //		}else {
        //			activate_req.setMac(idMacArray[1].trim());
        //		}
        //		HttpActivateTerminal httpActivateTerminal = new HttpActivateTerminal(activate_req, this);
        //		httpActivateTerminal.post();
    }

    private String[] getIdAndMac(String res) {
        String[] arrayIdAndMac = new String[2];
        try {
            JSONObject jsonObject = new JSONObject(res);
            int resCode = -1;
            resCode = jsonObject.getInt("RESULT");
            if (resCode != 0) {
                arrayIdAndMac[0] = resCode + "";
                arrayIdAndMac[1] = "";
                return arrayIdAndMac;
            }
            arrayIdAndMac[0] = jsonObject.getString("USERID");
            arrayIdAndMac[1] = jsonObject.getString("STBMAC");
            LogUtil.e(tag, "getIdAndMac", "plug: userid = " + arrayIdAndMac[0] + " stbmac = " + arrayIdAndMac[1]);
            return arrayIdAndMac;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void dealWithUpdate(LiveUpgrageResponse object) {
        if (object.getUrl() != null && object.getUrl().length() > 5) {
            if (object.getIsMust().contains("0")) {
                VarParam.isForceUpdate = false;
            } else {
                VarParam.isForceUpdate = true;
            }
            showUpdateDialog(object.getUrl(), VarParam.isForceUpdate);
        } else {
            bindYDAuthComponent();
        }
    }

    @Override
    public void showUpdateDialog(final String url, boolean isForce) {
        SendBroadcastTools.sendErrorBroadcast(getApplicationContext(), ErrorBroadcastAction.ERROR_LIVEPLAY_CANCEL_DIALOG, "", "");
        if (isForce) {
            showDownload(url);
            return;
        } else {
            Dialog dialog = null;
            dialog = getNonForceDialog(url);
            dialog.setCancelable(!isForce);
            dialog.show();
        }
    }

    @Override
    public void jumpToFragment() {

        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey("xunfeinum")) //解决讯飞跳转
        {
            jumpToFragmentWithBundles(LaunchType.TYPE_REPLAY, 0, getIntent().getExtras());
            return;
        }

        if (Var.allCategorys.size() == 0 || Var.allChannels.size() == 0) {
            LogUtil.e(tag, "jumpToFragment:", "Categories size is 0 or Channels size is 0!");
            return;
        }

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
                if (channel.getChannelNumber().equals(jumpChannelNum) || channel2.equals(jumpChannelNum) || channel3.equals(jumpChannelNum)) {
                    Var.isJumpNumExist = true;
                    saveChannelInfo(channel.getName());
                    jumpChannelNum = "";
                    break;
                }
            }
        }


        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        Bundle bundle = new Bundle();
        switch (curType) {
            case LaunchType.TYPE_LIVE:
                bundle.putBoolean("type", false);
                int index = 0;
                if (jumpCategoryId != null && jumpCategoryId.length() > 0) {
                    index = Var.getCategoryIndex(jumpCategoryId);
                }
                bundle.putInt("index", index);

                bundle.putBoolean("first", true);
                bundle.putString("channelName", jumpChannelName);
                bundle.putString("channelNum", jumpChannelNum);
                curFragment = new FragmentLivePlay(this);
                curFragment.setArguments(bundle);
                break;
            case LaunchType.TYPE_SAVE:
                bundle.putBoolean("type", true);
                bundle.putInt("index", Var.allCategorys.size() - 1);
                curFragment = new FragmentLivePlay(this);
                curFragment.setArguments(bundle);
                break;
            case LaunchType.TYPE_REPLAY:
                curFragment = new FragmentBackplay(this);
                break;
            case LaunchType.TYPE_RECOMMEND:
                curFragment = new FragmentRecommend(this);
                Log.e("xumin", "jumpToFragment: FragmentRecommend");
                break;
            default:
                break;
        }
        fragmentTransaction.add(R.id.root_liveplay, curFragment);
        fragmentTransaction.commitAllowingStateLoss();

    }


    private void saveChannelInfo(String name) {
        //		Toast.makeText(this, "存放下的频道是："+name, Toast.LENGTH_LONG).show();
        SharedPreferences sp = getSharedPreferences("FragmentLivePlay", 0);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("name", name);
        editor.commit();
    }

    /* (non-Javadoc)
     * @see com.xike.xkliveplay.activity.ActivityLaunchBase#jumpToFramgment(int, int)
     */
    @Override
    public void jumpToFramgment(int fragmentType, int curIndex) {

        LogUtil.i(tag, "jumpToFramgment -->", "fragmentType:" + fragmentType + ",curIndex:" + curIndex);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Bundle bundle = new Bundle();

        if (fragmentType == LaunchType.TYPE_REPLAY) {
            bundle.putInt("index", curIndex);
            curFragment = new FragmentBackplay(this);
            curFragment.setArguments(bundle);
        } else {
            switch (fragmentType) {
                case LaunchType.TYPE_LIVE:
                    bundle.putBoolean("type", false);
                    //增加逻辑，如果传进来其他类别的id，要直接跳转到该类别下
                    int index = 0;
                    if (jumpCategoryId != null && jumpCategoryId.length() > 0) {
                        index = Var.getCategoryIndex(jumpCategoryId);
                    }
                    bundle.putInt("index", index);
                    //				bundle.putString("channelName", jumpChannelName);
                    curFragment = new FragmentLivePlay(this);
                    curFragment.setArguments(bundle);
                    break;
                case LaunchType.TYPE_SAVE:
                    bundle.putBoolean("type", true);
                    bundle.putInt("index", Var.allCategorys.size() - 1);
                    curFragment = new FragmentLivePlay(this);
                    curFragment.setArguments(bundle);
                    break;
                case LaunchType.TYPE_RECOMMEND:
                    curFragment = new FragmentRecommend(this);
                    break;
                default:
                    break;
            }
        }

        fragmentTransaction.replace(R.id.root_liveplay, curFragment);
        fragmentTransaction.commitAllowingStateLoss();

    }

    /*********************************************广电SDK相关*************************************************************/
    private IUpdateData iGDupdata = new IUpdateData() {
        @Override
        public void updateData(String method, String uniId, Object object, boolean isSuccess) {
            if (isSuccess) {
                dealGDSuccess(method, uniId, object);
            } else {
                dealGDFailed(method, uniId, object);
            }
        }
    };


    /**
     * 广电SDK接口访问失败
     *
     * @param method
     * @param uniId
     * @param object
     */
    private void dealGDFailed(String method, String uniId, Object object) {
        if (method.equals(GDHttpTools.METHOD_I6_4IN1)) {
            showLog("4 in 1 request failed!!!");
            GDAuth4in1Res res = (GDAuth4in1Res) object;
            if (res.getCode().equals("51043-1") || res.getCode().equals("51043-2")) { //说明4合一接口访问超时了
                if (!paramSetting2()) //如果找缓存成功，就直接进直播
                {
                    //这里应该读取预置的频道列表进入直播
                    readDefault();
                }
                //这个时候要保存一个不准下次跳过的标识
                MernakeSharedTools.set(getApplicationContext(), "jumpSign", false);
            } else {
                GDDialogTools.creater().showDialog(this, res.getCode(), res.getDescription(), GDHttpTools.getInstance().getUserId(), GetStbinfo.getLocalMacAddress());
            }
        } else if (method.equals(GDHttpTools.METHOD_GETLIVECATEGORYLIST)) {
            showLog("Get live category list request failed!!!");
            if (object == null) {
                SendBroadcastTools.sendErrorBroadcast(this, ErrorBroadcastAction.ERROR_GET_CATEGORY_ACTION, "", "");
            } else {
                GDAuthLiveCategory res = (GDAuthLiveCategory) object;
                GDDialogTools.creater().showDialog(this, res.getCode(), res.getDescription(), GDHttpTools.getInstance().getUserId(), GetStbinfo.getLocalMacAddress());
            }
        } else if (method.equals(GDHttpTools.METHOD_GETLIVECONTENTLIST)) {
            showLog("Get live content list request failed!!!");
            if (object == null) {
                SendBroadcastTools.sendErrorBroadcast(this, ErrorBroadcastAction.ERROR_GET_CHANNEL_LIST_ACTION, "", "");
            } else {
                GDAuthGetStaticContent res = (GDAuthGetStaticContent) object;
                GDDialogTools.creater().showDialog(this, res.getCode(), res.getDescription(), GDHttpTools.getInstance().getUserId(), GetStbinfo.getLocalMacAddress());
            }

        } else if (method.equals(GDHttpTools.METHOD_GETAIDLDATA)) {
            showLog("Get AIDL data failed!!!");
            if (object == null) {
                SendBroadcastTools.sendErrorBroadcast(this, ErrorBroadcastAction.ERROR_AUTH_PLUG_IN_ACTION, GetStbinfo.getLocalMacAddress(), GDHttpTools.getInstance().getUserId());
            } else {
                GDAuthAidlRes res = (GDAuthAidlRes) object;
                //				showLog("aidl结果："+res.toString());
                GDDialogTools.creater().showDialog(this, res.getCode(), res.getDescription(), GDHttpTools.getInstance().getUserId(), GetStbinfo.getLocalMacAddress());
            }
        }
    }

    @Override
    public void showGDErrorDialog(String code, String msg, String mac, String userid) {
        GDDialogTools.creater().showDialog(this, code, msg, idMacArray[0], idMacArray[1]);
    }

    /**
     * 广电SDK接口访问成功
     *
     * @param method
     * @param uniId
     * @param object
     */
    private void dealGDSuccess(String method, String uniId, Object object) {
        if (method.equals(GDHttpTools.METHOD_I6_4IN1)) {
           // showLog("4 in 1 request success!!!");
            Log.i("MARK","4 in 1 request success!!!");
            GDAuth4in1Res res = (GDAuth4in1Res) object;
            stb_map.put("userId", GDHttpTools.getInstance().getUserId());
            stb_map.put("epg_domain_url", res.getData().getEpgDomain());
            stb_map.put("userToken", res.getData().getUserToken());
            stb_map.put("userGroupNMB", res.getData().getUserGroupNMB());
            int saveRes = GetVaram.write2SDcard(stb_map);
            if (saveRes == -1) {
                SendBroadcastTools.sendErrorBroadcast(this, ErrorBroadcastAction.ERROR_WRITE_STBINFO_ACTION, "", "");
                LogUtil.e(tag, "updateData", "write newstbinfo failed");
            } else if (saveRes == 0) {
                Var.initStbVars(getApplicationContext());
                LogUtil.e(tag, "updateData", "write newstbinfo sucess");
                //鉴权已经成功，开始请求epg
                //如果10000-1说明是鉴权成功，一切需要重新请求
                //如果10000-2说明是跳过鉴权成功，检查缓存有就进入直播，没有再请求缓存
                if (!paramSetting2()) {
                    if (Var.isStatic)
                        GDHttpTools.getInstance().getStaticLiveCategoryList(GDHttpTools.getInstance().getTag(), iGDupdata);
                    else
                        GDHttpTools.getInstance().getLiveCategoryList(GDHttpTools.getInstance().getTag(), Var.userId, Var.userToken, iGDupdata);
                }
                //                if (res.getCode().equals("10000-2"))
                //                {
                //                    if (!paramSetting2()) GDHttpTools.getInstance().getStaticLiveCategoryList(GDHttpTools.getInstance().getTag(),iGDupdata);
                //                }else if (res.getCode().equals("10000-1"))
                //                {
                //                    GDHttpTools.getInstance().getStaticLiveCategoryList(GDHttpTools.getInstance().getTag(),iGDupdata);
                //                }
            }
        } else if (method.equals(GDHttpTools.METHOD_GETLIVECATEGORYLIST)) {
            showLog("Get live category list success!!!");
            int res = dealResWithCategorysWithoutRequestLiveContent(object);
            if (res == 0) {
                SendBroadcastTools.sendErrorBroadcast(this, ErrorBroadcastAction.ERROR_GET_CATEGORY_ACTION, "", "");
            } else {
                //请求类别已经成功，开始请求全部频道
                if (Var.isStatic) {
                    GDHttpTools.getInstance().getStaticLiveContentList(GDHttpTools.getInstance().getTag(), Var.allCategoryId, Var.userGroupId, iGDupdata);
                } else
                    GDHttpTools.getInstance().getLiveContentList(GDHttpTools.getInstance().getTag(), Var.userId, Var.userToken, Var.allCategoryId, iGDupdata);
            }
        } else if (method.equals(GDHttpTools.METHOD_GETLIVECONTENTLIST)) {
            showLog("Get live content list success!!!");
            int res = dealResWithContentChannels(object, uniId);
            if (res == 0) {
                SendBroadcastTools.sendErrorBroadcast(this, ErrorBroadcastAction.ERROR_GET_CHANNEL_LIST_ACTION, "", "");
            } else {
                //请求全部频道已经成功，准备跳转直播
                jumpLive();
                if (!isUseDefault)
                    new SaveThread().start();
            }
        } else if (method.equals(GDHttpTools.METHOD_GETAIDLDATA)) {
            showLog("Get AIDL data success!!!");
            GDAuthAidlRes res = (GDAuthAidlRes) object;
            showLog("aidl结果：" + res.toString());
            String operaTag = "";
            if (res.getData().getOperaTag().equals("HW")) {
                operaTag = "1";
            } else if (res.getData().getOperaTag().equals("ZTE")) {
                operaTag = "2";
            }

            GDHttpTools.getInstance().setTag(operaTag);
            GDHttpTools.getInstance().setAIDLParam(res.getData().getEPGURLAIDL(), res.getData().getUserTokenAIDL(), res.getData().getIPTVAccountAIDL());
            GDHttpTools.getInstance().apkAuth2(GDHttpTools.getInstance().getUserId(), GetStbinfo.getLocalMacAddress(), "0", GDHttpTools.getInstance().getTag(), iGDupdata);
        }
    }


    private void showLog(String content) {
        System.out.println("MainActivity:GDHTTP:" + content);
    }

}
