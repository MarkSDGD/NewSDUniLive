package com.xike.xkliveplay.framework.service.yinhe;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by wrunqi on 17-11-22.
 * 这个跑在主进程里面
 */
public class ControlReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.d("test", action);
        if ("com.gitv.tv.REMOTE_ACTION".equals(action)) {
            int key = intent.getIntExtra("key", -1);
            if (key != -1) {
                Log.d("test", "处理按键=" + key);
                Key_Action.sendKeyUpAndDown(key);
            }
        }
    }
}
