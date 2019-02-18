package com.xike.xkliveplay.activity.fragment.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.xike.xkliveplay.R;
import com.xike.xkliveplay.framework.entity.ContentChannel;
import com.xike.xkliveplay.framework.httpclient.VarParam;
import com.xike.xkliveplay.framework.tools.LogUtil;
import com.xike.xkliveplay.framework.tools.Method;
import com.xike.xkliveplay.framework.varparams.Var;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("DrawAllocation")
public class ViewLiveChannelList extends View {

	private static final String tag = "ViewList";
	private static  int NUM_LEFT = 85;
	private static int TOP = 0;
	private static  int TEXT_LEFT = 115;
	private static  int TEXT_H = 90;
	
	private static  int CHANNEL_NUM_SIZE = 42;
	private static  int CHANNEL_TEXT_SIZE = 33;
	private static  int SAVE_ICON_LEFT = 338;
	
	
	/** �?��的频道列�?**/
	private List<ContentChannel> channels = null;
	/** 当前绘制的频道列�?**/
	private List<ContentChannel> curDrawChannels = new ArrayList<ContentChannel>();
	/** 画笔  **/
	private Paint mPaint;
	
	/** 当前选中焦点的图�?**/
	private Bitmap bitmapFocus = null;
	/** 当前频道收藏的图�?**/
	private Bitmap bitmapSave = null;
	
	private Bitmap bitmapOrder = null;
	private Bitmap bitmapNoOrder = null;
	
	/** 当前焦点�?��频道列表的位�?**/
	private int curFocusIndex = 0;

	/** 列表是否首位相接  **/
	private boolean isloop = true;
	
	public ViewLiveChannelList(Context context)
	{
		super(context);
	}
	
	public ViewLiveChannelList(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		initParam(context);
	}
	
	
	private void initParam(Context context)
	{
		mPaint = new Paint();
        mPaint.setAntiAlias(false);
//        mPaint.setStrokeWidth(9);
        mPaint.setTextSize(getResources().getDimensionPixelSize(R.dimen.sp_20));
        mPaint.setColor(Color.WHITE);
        
        bitmapFocus = BitmapFactory.decodeResource(context.getResources(), R.drawable.channel_focus);
        bitmapSave  = BitmapFactory.decodeResource(context.getResources(), R.drawable.save);
        bitmapNoOrder =  BitmapFactory.decodeResource(context.getResources(), R.drawable.zz_channel_notbuy);
        bitmapOrder = BitmapFactory.decodeResource(context.getResources(), R.drawable.zz_channel_buy);
        
//        initData();
        
        NUM_LEFT = Method.getScaleX(65);
    	TOP = 0;
    	TEXT_LEFT = (int) getResources().getDimension(R.dimen.live_channel_name_margin_left);
    	TEXT_H = Method.getScaleX(90);
    	
    	CHANNEL_NUM_SIZE = getResources().getDimensionPixelSize(R.dimen.live_channel_num_text_size);;
    	CHANNEL_TEXT_SIZE = getResources().getDimensionPixelSize(R.dimen.live_channel_name_text_size);;
    	SAVE_ICON_LEFT = getResources().getDimensionPixelSize(R.dimen.px_338);;
        
	}

	int top = 0;
	
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		
		canvas.drawColor(Color.TRANSPARENT);
		
		int bH = TEXT_H * 3 + 5;
//		System.out.println("w:" + bitmapFocus.getWidth() + ",h:" + bitmapFocus.getHeight());
		if(curDrawChannels.size() > 0)
		{
			canvas.drawBitmap(bitmapFocus, new Rect(0, 0, bitmapFocus.getWidth(), bitmapFocus.getHeight()),
					new Rect(0, bH, Method.getScaleX(420), bH + Method.getScaleY(80)), mPaint);
		}
		
		canvas.save();
		
		canvas.translate(0,Method.getScaleY(60) - top);
		
		drawChannelNum(canvas);
		
		drawChannelText(canvas);
		
		canvas.restore();
		
	}

	private void drawOrderFlag(Canvas canvas) 
	{
		
		for(int i = 0; i < curDrawChannels.size();i++)
		{
			
		}
	}

	/**
	 *           
	 * @param src
	 * @param isReloadCurCategary
	 */
	public void setChannlesInCategary(List<ContentChannel> src,boolean isReloadCurCategary)
	{
		if(src == null)
		{
			LogUtil.e(tag, "setChannlesInCategary","setChannles is null");
			channels = null;
			curDrawChannels = new ArrayList<ContentChannel>();
			postInvalidate();
			return;
		}
		
		channels = src;
		
		if(channels.size() >= 7)
		{
			if(isReloadCurCategary)
			{
				curFocusIndex = 3;
				top = 0;
			}
			TOP = 0;
			getLoopCurDrawList();
			isloop = true;
		}else {
			if(isReloadCurCategary)
			{
				curFocusIndex = 0;
				top = 0;
			}
			TOP = TEXT_H * 3;
			getNoLoopCurDrawList();
			isloop = false;
		}
		
		postInvalidate();
	}
	/** 向下移动  **/
	public void keyDown_Down()
	{
		LogUtil.i(tag, "keyDown_Down","*****************moveDown is start:curFocusIndex:" + curFocusIndex);
		
		if(channels == null || channels.size() == 0)
		{
			LogUtil.e(tag, "keyDown_Down","channels is null or size is 0");
			return;
		}
		
		if(isloop)
		{
			curFocusIndex = curFocusIndex + 1;
			if(curFocusIndex > channels.size() - 1)
			{
				curFocusIndex = 0;
			}
			
			getLoopCurDrawList();
			
			postInvalidate();
		}else 
		{
			curFocusIndex = curFocusIndex + 1;
			if(curFocusIndex > channels.size() - 1)
			{
				curFocusIndex = channels.size() - 1;
				return;
			}
			
			getNoLoopCurDrawList();
			
			postInvalidate();
			
			top += TEXT_H;
		}
		
	}
	
	public void onkeyUp()
	{
		
	}
	
	/** 向上移动 **/
	public void keyDown_up()
	{
		LogUtil.i(tag, "keyDown_up","*****************moveUp is start:curFocusIndex-->" + curFocusIndex);
		if(channels == null || channels.size() == 0)
		{
			LogUtil.e(tag, "keyDown_up","moveDown --> channels is null or size is 0");
			return;
		}
		
		if(isloop)
		{
			curFocusIndex = curFocusIndex - 1;
			if(curFocusIndex < 0)
			{
				curFocusIndex = channels.size() - 1;
			}
			
			getLoopCurDrawList();
			
			postInvalidate();
		}else {
			curFocusIndex = curFocusIndex - 1;
			if(curFocusIndex < 0)
			{
				curFocusIndex = 0;
				return;
			}
			
			getNoLoopCurDrawList();
			
			postInvalidate();
			
			top -= TEXT_H;
		}
		
	}
	
	/**
	 *      绘制频道�?
	 */
	private void drawChannelNum(Canvas canvas)
	{
		if (VarParam.url.contains("msnm")) 
		{
//			mPaint.setStrokeWidth(1);
//			mPaint.setStyle(Style.FILL_AND_STROKE);
			mPaint.setFakeBoldText(true);
		}
		
		mPaint.setTextAlign(Paint.Align.RIGHT);
		mPaint.setTextSize(CHANNEL_NUM_SIZE);
		for(int i = 0; i < curDrawChannels.size();i++)
		{
			canvas.drawText(curDrawChannels.get(i).getChannelNumber(), NUM_LEFT, TOP + TEXT_H * i, mPaint);
		}
	}
	
	/**
	 *    绘制频道
	 */
	private void drawChannelText(Canvas canvas)
	{
		mPaint.setFakeBoldText(false);
//		mPaint.setStyle(Style.FILL);
		mPaint.setTextAlign(Paint.Align.LEFT);
		mPaint.setTextSize(CHANNEL_TEXT_SIZE);
		Rect rect = new Rect();
		for(int i = 0; i < curDrawChannels.size();i++)
		{
			mPaint.getTextBounds(curDrawChannels.get(i).getName(),0,curDrawChannels.get(i).getName().length(), rect);
			canvas.drawText(curDrawChannels.get(i).getName(), TEXT_LEFT, TOP + TEXT_H * i, mPaint);
			if(curDrawChannels.get(i).getZipCode().equals("save"))
			{
				canvas.drawBitmap(bitmapSave, SAVE_ICON_LEFT, TOP + TEXT_H * i - Method.getScaleX(20), mPaint);
			}
			
			if (!Var.isZZEnabled) {
				continue;
			}
			
			/*if(curDrawChannels.get(i).getDescription().equals("1"))
			{
				canvas.drawBitmap(bitmapNoOrder, SAVE_ICON_LEFT+6, TOP + TEXT_H * i - Method.getScaleX(20)-35, mPaint);
			}else if (curDrawChannels.get(i).getDescription().equals("2")) 
			{
				canvas.drawBitmap(bitmapOrder, SAVE_ICON_LEFT+6, TOP + TEXT_H * i - Method.getScaleX(20)-35, mPaint);
			}*/
			if(curDrawChannels.get(i).getCountry().equals("true")) //增值 VIP
			{
				canvas.drawBitmap(bitmapNoOrder, SAVE_ICON_LEFT+6, TOP + TEXT_H * i - Method.getScaleX(20)-35, mPaint);
			}
		}
	}
	
	/**
	 *     获得当前列表循环的数�?
	 */
	private void getLoopCurDrawList()
	{
		LogUtil.i(tag, "getLoopCurDrawList","**********getCurDrawList is start:");
		
		removeAllDrawChannel();
		
		int index = 0;
		//上面4�?
		for(int i = 3; i > 0 ;i--)
		{
			index = curFocusIndex - i;
			if(index < 0)
			{
				index = channels.size() + index;
			}
			
			addDrawChannel(getSingleChannels(index));
		}
		
		//焦点
		addDrawChannel(getSingleChannels(curFocusIndex));

		//下面4�?
		for(int i = 1; i < 4;i++)
		{
			index = curFocusIndex + i;
			if(index > channels.size() - 1)
			{
				index = index - channels.size();
			}
			
			addDrawChannel(getSingleChannels(index));
		}
		
		LogUtil.i(tag, "getLoopCurDrawList","curFocusIndex:" + curFocusIndex);
//		printContentChannel(curDrawChannels);
	}
	
	/**
	 *    获得当前绘制列表不循环的数据
	 */
	private void getNoLoopCurDrawList()
	{
		if(channels == null)
		{
			return;
		}
		
		curDrawChannels = channels;
	}
	
	@SuppressWarnings("unused")
	private void initData()
	{
		channels = new ArrayList<ContentChannel>();
		ContentChannel channel = null;
		for(int i = 0; i < 4;i++)
		{
			channel = new ContentChannel();
			channel.setChannelNumber(Integer.toString(i));
			channel.setName("CCTV" + i);
			if(i % 2 == 0)
			{
				channel.setZipCode("save");
			}
			channels.add(channel);
		}
		
		if(channels.size() >= 7)
		{
			curFocusIndex = 3;
			TOP = 0;
			isloop = true;
			getLoopCurDrawList();
		}else {
			curFocusIndex = 0;
			TOP = TEXT_H * 3;
			isloop = false;
			getNoLoopCurDrawList();
		}
		
	}
	
	private ContentChannel getSingleChannels(int index)
	{
		if(channels == null)
		{
			LogUtil.e(tag, "getSingleChannels","getSingleChannels is null");
			return null;
		}
		
		if(index < 0 || index > channels.size() - 1)
		{
			LogUtil.e(tag, "getSingleChannels","getSingleChannels array over index:" + index + ",size:" + channels.size());
			return null;
		}
		
		return channels.get(index);
	}
	
	private void addDrawChannel(ContentChannel channel)
	{
		if(channel == null)
		{
			LogUtil.i(tag, "addDrawChannel","addDrawChannel is null");
			return;
		}
		
		curDrawChannels.add(channel);
	}
	
	private void removeAllDrawChannel()
	{
		if(curDrawChannels == null)
		{
			LogUtil.i(tag, "removeAllDrawChannel","removeAllDrawChannel is null");
			return;
		}
		
		curDrawChannels.clear();
	}
	
	private void printContentChannel(List<ContentChannel> list) 
	{
		if (list == null) {
			LogUtil.i(tag,"printContentChannel", "list isChangeChangeInNumber null");
			return;
		}

		for (int i = 0; i < list.size(); i++) 
		{
			LogUtil.i(tag,list.get(i).getName(), list.get(i).toString());
		}
	}
	
	public int getCurFocusIndex() {
		return curFocusIndex;
	}

	public void setCurFocusIndex(int curFocusIndex) {
		this.curFocusIndex = curFocusIndex;
	}
	
	public void refreshList()
	{
		getLoopCurDrawList();
		postInvalidate();
	}
}
