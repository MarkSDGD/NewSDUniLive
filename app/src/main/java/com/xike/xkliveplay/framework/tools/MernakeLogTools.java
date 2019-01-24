package com.xike.xkliveplay.framework.tools;

import android.util.Log;

/**
 * The programer: created by Mernake on 2018/10/18 10:15
 * Main Function:
 */
public class MernakeLogTools
{
    private static String DEFAULT_TAG = "ShanDongLive";


    public static void showLog(String tag,String content)
    {
        if (tag==null||tag.length()==0) {
            Log.i(DEFAULT_TAG,content);
        }else Log.i(tag,content);
    }
}
