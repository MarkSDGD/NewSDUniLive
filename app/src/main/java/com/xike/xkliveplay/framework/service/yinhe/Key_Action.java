package com.xike.xkliveplay.framework.service.yinhe;


import android.app.Instrumentation;
import android.view.KeyEvent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by wrunqi on 17-6-20.
 * 模拟按键操作最好不要跑在主线程里面, 
 * 而且有时候可能说用户可能频繁操作遥控, 用线程池是比较好的选择
 */
public class Key_Action {
    private static final String TAG = "control";

    private static final long INTERVAL_DEFAULT = 30;
    private static Instrumentation instrumentation = new Instrumentation();
    static ExecutorService threads;

    public static void init() {
        if (threads == null) {
            threads = new ThreadPoolExecutor(5, 10, 50, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(20), new ThreadPoolExecutor.CallerRunsPolicy());
        }
    }

    public static void sendKeyUpAndDown(final int key) 
    {
        init();
        threads.execute(new Runnable() 
        {
            @Override
            public void run() 
            {
                final long downTimeMs = System.currentTimeMillis();
                final long upTimeMs = downTimeMs + INTERVAL_DEFAULT;
                sendKey(downTimeMs, KeyEvent.ACTION_DOWN, key, 0);
                sendKey(upTimeMs, KeyEvent.ACTION_UP, key, 0);
            }
        });
    }

    private static void sendKey(long actionTime, int action, int key, int repeat) {
        try {
            instrumentation.sendKeySync(new KeyEvent(actionTime, actionTime, action, key, repeat));
        } catch (Exception e) {
        }
    }
}
