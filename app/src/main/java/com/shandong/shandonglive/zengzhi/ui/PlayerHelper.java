package com.shandong.shandonglive.zengzhi.ui;

import android.os.Handler;

/**
 * The programer: created by Mernake on 2018/9/10 14:28
 * Main Function:
 */
public class PlayerHelper
{
    private static PlayerHelper playerHelper = null;

    public static PlayerHelper getInstance(){
        if (playerHelper==null)playerHelper = new PlayerHelper();
        return playerHelper;
    }

    private boolean isStopSendBufferMessage = false;
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            isStopSendBufferMessage = false;
        }
    };
    public void setStopSendBufferMessage(){
        isStopSendBufferMessage = true;
        refreshStopSendBufferMessage();
    }

    private void refreshStopSendBufferMessage(){
        handler.removeCallbacks(runnable);
        handler.postDelayed(runnable,2000);
    }

    public boolean isStopSendBufferMessage(){
        return isStopSendBufferMessage;
    }
}
