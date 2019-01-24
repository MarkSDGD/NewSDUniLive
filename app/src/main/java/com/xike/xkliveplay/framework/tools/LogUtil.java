package com.xike.xkliveplay.framework.tools;
/**
 * 日志类包装，方便调用
 * 
 * 
 * @author  wKF46829
 * @version  [v1.0, 2011-4-23]
 * @see  
 * @since  
 */
public class LogUtil {

    private static final boolean DEBUG_ON = true;

    private static final String TAG = "XKLive";
    
    private LogUtil() {
    }

    public static void i(String tag, String method, String msg) {
        if (DEBUG_ON) {
            android.util.Log.i(TAG, tag + "--->" + method + ":" + msg);
        }
    }

    public static void w(String tag, String method, String msg) {
        if (DEBUG_ON) {
            android.util.Log.w(TAG, tag + "--->" + method + ":" + msg);
        }
    }

    public static void e(String tag, String method, String msg){
        if (DEBUG_ON) {
            android.util.Log.e(TAG, tag + "--->" + method + ":" + msg);
        }
    }
}
