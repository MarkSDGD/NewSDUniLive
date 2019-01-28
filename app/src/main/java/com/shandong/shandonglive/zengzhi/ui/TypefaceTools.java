/**
 * @Title: TypefaceTools.java
 * @Package com.xike.xkliveplay.activity.newlist
 * @Description: TODO
 * Copyright: Copyright (c) 2015
 * Company:青岛博瑞立方网络科技有限公司
 * 
 * @author Mernake
 * @date 2017年6月6日 下午2:41:25
 * @version V1.0
 */
package com.shandong.shandonglive.zengzhi.ui;

import android.content.Context;
import android.graphics.Typeface;

/**
  * @ClassName: TypefaceTools
  * @Description: TODO
  * @author Mernake
  * @date 2017年6月6日 下午2:41:25
  *
  */
public class TypefaceTools 
{
	public static Typeface channeNumFont = null;
	public static Typeface yuantiFont = null;
	public static Typeface dateNumFont = null;
	
	public static Typeface getChannelNumFont(Context context)
	{
		if (channeNumFont == null) 
		{
			channeNumFont = Typeface.createFromAsset(context.getAssets(), "fonts/channelnum.ttf");
		}
		return channeNumFont;
	}
	
	public static Typeface getYuanTiFont(Context context)
	{
		if (yuantiFont == null) 
		{
			yuantiFont = Typeface.createFromAsset(context.getAssets(), "fonts/yuanti.TTF");
		}
		return yuantiFont;
	}
	
	public static Typeface getDateNumFont(Context context)
	{
		if (dateNumFont == null) 
		{
			dateNumFont = Typeface.createFromAsset(context.getAssets(), "fonts/datenum.otf");
		}
		return dateNumFont;
	}
	
	
}
