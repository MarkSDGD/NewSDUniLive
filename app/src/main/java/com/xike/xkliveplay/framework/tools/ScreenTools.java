package com.xike.xkliveplay.framework.tools;

import android.content.Context;
import android.view.WindowManager;

/**
 * @author Mernake <br>
 * CreateTime: 2015年5月18日 下午5:42:13<br>
 * <b>Info:</b><br>
 *<br>
 *   <b>Method:</b> <br>
 *   <b>Public:</b> <br>
 *   <b>Private:</b> <br>
 */
public class ScreenTools 
{
	public static int getScreenHeight(Context context)
	{
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		 int height = wm.getDefaultDisplay().getHeight();
		 return height;
	}
	
	public static int getScreenWidth(Context context)
	{
		 WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		 int width = wm.getDefaultDisplay().getWidth();
		 return width;
	}
}
