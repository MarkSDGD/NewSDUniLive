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
public class ViewReSeeChannelList extends View {

	private static final String tag = "ViewReSeeChannelList";
	private static  int NUM_LEFT = 115;
	private static int TOP = 0;
	private static  int TEXT_LEFT = 140;
	private static  int TEXT_H = 56;
	private static int LINE_WIDTH = 354;
	
	
	
	/** 锟�锟斤拷鐨勯閬撳垪锟�**/
	private List<ContentChannel> channels = null;
	/** 褰撳墠缁樺埗鐨勯閬撳垪锟�**/
	private List<ContentChannel> curDrawChannels = new ArrayList<ContentChannel>();
	/** 鐢荤瑪  **/
	private Paint mPaint;
	
	/** 褰撳墠閫変腑鐒︾偣鐨勫浘锟�**/
	private Bitmap bitmapFocus = null;
	private Bitmap bitmapNoFocus = null;
	
	/** 褰撳墠鐒︾偣锟�锟斤拷棰戦亾鍒楄〃鐨勪綅锟�**/
	private int curFocusIndex = 0;

	/** 鍒楄〃鏄惁棣栦綅鐩告帴  **/
	private boolean isloop = true;
	
	private boolean isFocus = true;
	private Bitmap bitmapNoOrder=null;

	public ViewReSeeChannelList(Context context,int type)
	{
		super(context);
		TEXT_H = Method.getScaleY(56);
		TEXT_LEFT = (int) getResources().getDimension(R.dimen.resee_channel_text_left);
		NUM_LEFT = (int) getResources().getDimension(R.dimen.resee_channel_num_left);
		LINE_WIDTH = (int) getResources().getDimension(R.dimen.resee_channel_line_width);
	}
	
	public ViewReSeeChannelList(Context context, AttributeSet attrs) {
		super(context, attrs);
		TEXT_H = Method.getScaleY(56);
		TEXT_LEFT = (int) getResources().getDimension(R.dimen.resee_channel_text_left);
		NUM_LEFT = (int) getResources().getDimension(R.dimen.resee_channel_num_left);
		LINE_WIDTH = (int) getResources().getDimension(R.dimen.resee_channel_line_width);
		initParam(context);
	}
	
	
	private void initParam(Context context)
	{
		mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(6);
        mPaint.setColor(Color.WHITE);
        
        if (VarParam.default_url.contains("ysnm")) 
        {
        	bitmapFocus = BitmapFactory.decodeResource(context.getResources(), R.drawable.backplay_old_channel_focus);
            bitmapNoFocus = BitmapFactory.decodeResource(context.getResources(), R.drawable.backplay_old_channel_unfocus);
		}else {
			bitmapFocus = BitmapFactory.decodeResource(context.getResources(), R.drawable.backplay_channel_focus);
			bitmapNoFocus = BitmapFactory.decodeResource(context.getResources(), R.drawable.backplay_channel_unfocus);
		}
		bitmapNoOrder =  BitmapFactory.decodeResource(context.getResources(), R.drawable.zz_channel_notbuy);

	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		
		canvas.drawColor(Color.TRANSPARENT);
		
		int bH = TEXT_H * 4;
		if(curDrawChannels.size() > 0)
		{
			if(isFocus)
			{
				canvas.drawBitmap(bitmapFocus, new Rect(0, 0, bitmapFocus.getWidth(), bitmapFocus.getHeight()),
						new Rect(Method.getScaleX(7), bH, LINE_WIDTH, bH + Method.getScaleX(56)), mPaint);
			}else {
				canvas.drawBitmap(bitmapNoFocus, new Rect(0, 0, bitmapFocus.getWidth(), bitmapFocus.getHeight()),
						new Rect(Method.getScaleX(7), bH, LINE_WIDTH, bH + Method.getScaleX(56)), mPaint);
			}
			
		}
		
		canvas.save();
		
		canvas.translate(0,Method.getScaleX(40));
		
		drawChannelNum(canvas);
		
		drawChannelText(canvas);
		
		canvas.restore();
		
	}

	/**
	 *           
	 * @param src
	 * @param isReloadCurCategary
	 */
	public void setChannlesInCategary(List<ContentChannel> src)
	{
		if(src == null)
		{
			LogUtil.e(tag, "setChannlesInCategary","setChannles is null");
			curDrawChannels = new ArrayList<ContentChannel>();
			postInvalidate();
			return;
		}
		
		channels = src;
		
		TOP = 0;
		getLoopCurDrawList();
		isloop = true;
		
		postInvalidate();
	}
	/** 鍚戜笅绉诲姩  **/
	public void keyDown_Down()
	{
		LogUtil.i(tag, "keyDown_Down","moveDown is start:");
		
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
		}
	}
	
	public void onkeyUp()
	{
		
	}
	
	/** 鍚戜笂绉诲姩 **/
	public void keyDown_up()
	{
		LogUtil.i(tag, "keyDown_Down","curFocusIndex-->" + curFocusIndex);
		if(channels == null || channels.size() == 0)
		{
			LogUtil.e(tag, "keyDown_Down","channels is null or size is 0");
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
		}
		
	}
	
	/**
	 *      缁樺埗棰戦亾锟�
	 */
	private void drawChannelNum(Canvas canvas)
	{
		mPaint.setTextSize(getResources().getDimension(R.dimen.resee_channel_num_size));
		mPaint.setTextAlign(Paint.Align.RIGHT);
		for(int i = 0; i < curDrawChannels.size();i++)
		{
			canvas.drawText(curDrawChannels.get(i).getChannelNumber(), NUM_LEFT, TOP + TEXT_H * i + 2, mPaint);
		}
	}
	
	/**
	 *    缁樺埗棰戦亾
	 */
	private void drawChannelText(Canvas canvas)
	{
		mPaint.setTextSize(getResources().getDimension(R.dimen.resee_schedule_text_size));
		mPaint.setTextAlign(Paint.Align.LEFT);
		Rect rect = new Rect();
		for(int i = 0; i < curDrawChannels.size();i++)
		{
			mPaint.getTextBounds(curDrawChannels.get(i).getName(),0,curDrawChannels.get(i).getName().length(), rect);
			canvas.drawText(curDrawChannels.get(i).getName(), TEXT_LEFT, TOP + TEXT_H * i, mPaint);

			if(Var.isZZEnabled&&curDrawChannels.get(i).getCountry().equals("true")) //增值 VIP
			{
				canvas.drawBitmap(bitmapNoOrder, 308, TOP + TEXT_H * i - Method.getScaleX(20)-20, mPaint);
			}

		}





	}
	
	/**
	 *     鑾峰緱褰撳墠鍒楄〃寰幆鐨勬暟锟�
	 */
	private void getLoopCurDrawList()
	{
		LogUtil.i(tag, "getLoopCurDrawList","getCurDrawList is start:");
		
		removeAllDrawChannel();
		
		int index = 0;
		//涓婇潰4锟�
		for(int i = 4; i > 0 ;i--)
		{
			index = curFocusIndex - i;
			if(index < 0)
			{
				index = channels.size() + index;
			}
			
			addDrawChannel(getSingleChannels(index));
		}
		
		//鐒︾偣
		addDrawChannel(getSingleChannels(curFocusIndex));

		//涓嬮潰4锟�
		for(int i = 1; i < 6;i++)
		{
			index = curFocusIndex + i;
			if(index > channels.size() - 1)
			{
				index = index - channels.size();
			}
			
			addDrawChannel(getSingleChannels(index));
		}
		
		LogUtil.i(tag, "getLoopCurDrawList","curFocusIndex:" + curFocusIndex);
		printContentChannel(curDrawChannels);
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
			LogUtil.i(tag, "getSingleChannels","addDrawChannel is null");
			return;
		}
		
		curDrawChannels.add(channel);
	}
	
	private void removeAllDrawChannel()
	{
		if(curDrawChannels == null)
		{
			LogUtil.i(tag, "getSingleChannels","removeAllDrawChannel is null");
			return;
		}
		
		curDrawChannels.clear();
	}
	
	private void printContentChannel(List<ContentChannel> list) {
		if (list == null) {
			LogUtil.i(tag, "getSingleChannels","list isChangeChangeInNumber null");
			return;
		}

//		for (int i = 0; i < list.size(); i++) {
//			LogUtil.i(tag,list.get(i).getName(), list.get(i).toString());
//		}
	}
	
	public int getCurFocusIndex() {
		return curFocusIndex;
	}

	public void setCurFocusIndex(int curFocusIndex) {
		this.curFocusIndex = curFocusIndex;
	}
	
	public boolean isFocus() {
		return isFocus;
	}

	public void setFocus(boolean isFocus) {
		this.isFocus = isFocus;
		postInvalidate();
	}
}
