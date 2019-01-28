package com.xike.xkliveplay.framework.tools;

import android.content.Context;

import com.orhanobut.hawk.Hawk;

public class AppContext {

    private static final String APP_CONFIG_PWD = "22kHIPFATR9xKzfV";

    //========
    private static AppContext sharedAppContext;

    public static AppContext getSharedAppContext(Context context) {
        if (sharedAppContext == null) {
            synchronized (AppContext.class) {
                sharedAppContext = new AppContext(context);
            }
        }
        return sharedAppContext;
    }
    //========

    private Context mContext;

    private AppContext(Context context) {
        this.mContext = context;
        initHawk();
    }

    private void initHawk() {
        Hawk.init(mContext).build();
    }

    //版本号
    private static final String CONFIG_KEY_SAVED_VERSION_CODE = "KeySavedVersionCode";

    public int getSavedVersionCode() {
        return Hawk.get(CONFIG_KEY_SAVED_VERSION_CODE, -999999999);
    }

    public void updateSavedVersionCode(Integer versionCode) {
        Hawk.put(CONFIG_KEY_SAVED_VERSION_CODE, versionCode);
    }

    //上次播放集数和时间
    public int getSavedViewedSequence(String name) {
        return Hawk.get(name, 0);
    }

    public void updateSavedViewedSequence(String name, int sequence) {
        Hawk.put(name, sequence);
    }

    //
    public Object getSavedVersionObject(String name) {
        return Hawk.get(name, null);
    }

    public void savedVersionObject(String name, Object object) {
        Hawk.put(name, object);
    }

    public int getSavedViewedTime(String name) {
        return Hawk.get(name, 0);
    }

    public void updateSavedViewedTime(String name, int sequence) {
        Hawk.put(name, sequence);
    }

    //鉴权数据
    public String getSavedAuthentication(String name) {
        return Hawk.get(name, "");
    }

    public void savedViewedAuthenticationParameter(String name, String parameter) {
        Hawk.put(name, parameter);
    }

    //通用Hawk读存String
    public String getHawkString(String name) {
        return Hawk.get(name, "");
    }

    public boolean saveHawkString(String name, String str) {
        return Hawk.put(name, str);
    }

    //通用Hawk读存Object
    public Object getSaveObject(String name) {
        return Hawk.get(name, null);
    }

    public void saveObject(String name, Object object) {
        Hawk.put(name, object);
    }

    public boolean deleteObject(String name){
        return  Hawk.delete(name);
    }

    //通用Hawk读存Boolean
    public boolean getBoolean(String name) {
        return Hawk.get(name, false);
    }

    public void saveBoolean(String name, boolean b) {
        Hawk.put(name, b);
    }

    //通用Hawk读存long
    public long getLong(String name) {
        return Hawk.get(name, Long.valueOf(0 + ""));
    }

    public void saveLong(String name, long content) {
        Hawk.put(name, content);
    }

    // 记住的用户名
    private static final String SAVED_USER_ACCOUNT = "SavedUserName";

    public String getSavedUserAccount() {
        return Hawk.get(SAVED_USER_ACCOUNT);
    }

    public void saveUserAccount(String userAccount) {
        Hawk.put(SAVED_USER_ACCOUNT, userAccount);
    }


}

