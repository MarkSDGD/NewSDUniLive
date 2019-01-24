package com.xike.xkliveplay.framework.tools;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.TypedValue;
import android.view.View;

public class Method {

	public static final String NET_URL_DOWNLOAD = "http://219.146.10.236/source";
	
	public static int screenWidth = 0;
	public static int screenHeight = 0;
	public static float scaleX = 1.0f;
	public static float scaleY = 1.0f;
	
	public static Bitmap decodeResource(Resources resources, int id) {
	    TypedValue value = new TypedValue();
	    resources.openRawResource(id, value);
	    BitmapFactory.Options opts = new BitmapFactory.Options();
	    opts.inTargetDensity = value.density;
	    return BitmapFactory.decodeResource(resources, id, opts);
	}
	
	@SuppressWarnings("deprecation")
	public static int getScreenHeight(Activity activity) {
		int height =  activity.getWindowManager().getDefaultDisplay().getHeight();
		scaleY = (float)height / 720;
		System.out.println("----------scaleY:" + scaleY);
		return height;
	}

	public static int getScreenWidth(Activity activity) {
		int width =  activity.getWindowManager().getDefaultDisplay().getWidth();
		scaleX = (float)width / 1280;
		System.out.println("-----scaleX:" + scaleX);
		return width;
	}

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	public static int getScaleX(int x)
	{
		return (int) (x * scaleX);
	}
	
	public static int getScaleY(int y)
	{
		return (int)(y * scaleY);
	}
	public static Bitmap convertViewToBitmap(View paramView) 
	{//根不能够为关系布局，如果需要关系布局，在最外面在套一个线性布局
		paramView.measure(View.MeasureSpec.makeMeasureSpec(0, 0), View.MeasureSpec.makeMeasureSpec(0, 0));
		paramView.layout(0, 0, paramView.getMeasuredWidth(), paramView.getMeasuredHeight());
		paramView.buildDrawingCache();
		return paramView.getDrawingCache();
	}
}
