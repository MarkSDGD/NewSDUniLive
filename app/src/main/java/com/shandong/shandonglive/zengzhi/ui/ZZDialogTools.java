package com.shandong.shandonglive.zengzhi.ui;

import android.app.Activity;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.xike.xkliveplay.R;

public class ZZDialogTools
{

    private static ZZDialogTools dialogTools = null;
    private ZZOrderDialog zzOrderDialog;

    public static ZZDialogTools getInstance(){
        if (dialogTools == null){
            dialogTools = new ZZDialogTools();
        }
        return dialogTools;
    }

    public void showOrderDialog(String channelName,String userId,String channelId,String categoryId,Activity activity)
    {
        if (zzOrderDialog!=null && zzOrderDialog.isShowing()) zzOrderDialog.dismiss();
        if (zzOrderDialog==null)
        {
            zzOrderDialog = new ZZOrderDialog(activity, R.style.MyDialog);
            Window win = zzOrderDialog.getWindow();
            win.setBackgroundDrawableResource(R.color.transparent_background);
            WindowManager.LayoutParams params = new WindowManager.LayoutParams();
            params.gravity = Gravity.CENTER;
            win.setAttributes(params);
        }
        zzOrderDialog.show();
        zzOrderDialog.refreshText(channelName,userId);
        zzOrderDialog.setCategoryId(categoryId);
        zzOrderDialog.setChannelId(channelId);
    }
}
