package com.xike.xkliveplay.framework.tools;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.text.TextUtils;

import java.util.List;

/**
 * Activity状态管理
 * Created by wangfangxu on 2017/6/26.
 */

public class ActivityState {
    /**
     * 判断某Activity是否正在运行
     * @param mContext
     * @param activityClassName
     * @return
     */
    public static boolean isActivityRunning(Context mContext, String activityClassName){
        ActivityManager activityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> info = activityManager.getRunningTasks(1);
        if(info != null && info.size() > 0){
            ComponentName component = info.get(0).topActivity;
            if(activityClassName.equals(component.getClassName())){
                return true;
            }
        }
        return false;
    }
        /**
         * 判断某个Activity 界面是否在前台
         * @param context
         * @param className 某个界面名称
         * @return
         */
        public static boolean  isForeground(Context context, String className) {
            if (context == null || TextUtils.isEmpty(className)) {
                return false;
            }

            ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(1);
            if (list != null && list.size() > 0) {
                ComponentName cpn = list.get(0).topActivity;
                if (className.equals(cpn.getClassName())) {
                    return true;
                }
            }
            return false;
        }

}
