package com.shandong.shandonglive.zengzhi.ui;

import android.content.Context;
import android.os.Handler;
import android.view.KeyEvent;
import android.widget.Toast;

/**
 * The programer: created by Mernake on 2018/9/5 09:59
 * Main Function:
 * {@link #getInstance()} getInstance
 * {@link #setTestFunctionEnabled(boolean)} open/close the function
 * {@link #showToast(Context, String)} show a toast
 * {@link #calHotKey(int)} calculate hotkey value
 *
 */
public class TesturlTools
{
    private static TesturlTools tool = null;
    private boolean enabled = false;
    private static final int TARGET_HOT_KEY_VALUE = KeyEvent.KEYCODE_MENU + KeyEvent.KEYCODE_DPAD_LEFT * 2 + KeyEvent.KEYCODE_DPAD_RIGHT * 2;
    private int curHotKeyValue = 0;
    private Handler handler = new Handler();
    private boolean isNeedSwitchPlayUrl = false;
    private String playUrl;

    public boolean isNeedSwitchPlayUrl() {
        return isNeedSwitchPlayUrl;
    }
    private String url1 = "http://ucdn-zte.sd.chinamobile.com:8089/shandong_cabletv.live.zte.com/223.99.253.7:8081/00/SNM/CHANNEL00000405/index.m3u8";
    private String url2 = "http://ucdn-test.sd.chinamobile.com:8089/00/SNM/CHANNEL00000405/index.m3u8";
    public String getTestUrl(boolean isSwitch)
    {
        if (isSwitch)return url1;
        else return url2;
    }

    public boolean isSame(String url){
        if (url.equals(url1) || url.equals(url2))return true;
        else return false;
    }

    public boolean isEnabled(){
        return enabled;
    }

    public String getUrl1(){
        return url1;
    }


    public static TesturlTools getInstance(){
        if (tool==null)tool = new TesturlTools();
        return tool;
    }

    public void setTestFunctionEnabled(boolean enabled)
    {
        this.enabled = enabled;
    }

    public void showToast(Context context,String content)
    {
        if (!enabled)return;
        Toast.makeText(context,content,Toast.LENGTH_LONG).show();
    }

    public boolean calHotKey(int keycode)
    {
        if (!enabled)return false;
        if (curHotKeyValue==0 && keycode!= KeyEvent.KEYCODE_MENU)return false;
        if (curHotKeyValue>TARGET_HOT_KEY_VALUE)return false;
        System.out.println("键值："+keycode);
        curHotKeyValue+=keycode;
        System.out.println("总值："+curHotKeyValue + " 目标值:"+TARGET_HOT_KEY_VALUE);
        if (curHotKeyValue==TARGET_HOT_KEY_VALUE)
        {
            isNeedSwitchPlayUrl = !isNeedSwitchPlayUrl;
            return true;
        }
        refreshCount();
        return false;
    }

    private void refreshCount(){
        handler.removeCallbacks(runnable);
        handler.postDelayed(runnable,2000);
    }

    private Runnable runnable = new Runnable()
    {
        @Override
        public void run()
        {
            curHotKeyValue=0;
        }
    };

    private boolean isSwitch = false;
    public String getPlayUrl()
    {
        if (isSwitch) {
            return url2;
        }else{
            return url1;
        }
    }

    public boolean isSwitch() {
        return isSwitch;
    }

    public void setSwitch(boolean aSwitch) {
        isSwitch = aSwitch;
    }
}
