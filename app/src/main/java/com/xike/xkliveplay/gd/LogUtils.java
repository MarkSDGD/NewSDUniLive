package com.xike.xkliveplay.gd;

import android.util.Log;

import com.xike.xkliveplay.BuildConfig;

public class LogUtils {

    private LogUtils(){

    }
//    // 如果想屏蔽所有log,可以设置为0
//    private static final int LOG_LEVEL = 6;
//
//    private static final int VERBOSE = 5;
//    private static final int DEBUG = 4;
//    private static final int INFO = 3;
//    private static final int WARN = 2;
//    private static final int ERROR = 1;

    private static boolean isDebug = BuildConfig.DEBUG;

    public static void v(String tag, String msg) {
        if (isDebug) {
            Log.v(tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (isDebug) {
            Log.d(tag, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (isDebug) {
            Log.i(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (isDebug) {
            Log.w(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (isDebug) {
            Log.e(tag, msg);
        }
    }

}
