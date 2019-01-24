package com.xike.xkliveplay.activity.fragment.view;

import java.util.ArrayList;
import java.util.List;

import com.xike.xkliveplay.R;
import com.xike.xkliveplay.framework.entity.Schedule;
import com.xike.xkliveplay.framework.tools.LogUtil;
import com.xike.xkliveplay.framework.tools.Method;

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

@SuppressLint("DrawAllocation")
public class ViewReSeeSchedulelList extends View {

	private static final String tag = "ViewReSeeSchedulelList";
	private static  int NUM_LEFT = 240;
	private static int TOP = 0;
	private static  int TEXT_LEFT = 260;
	private static  int TEXT_H = 58;
	private static int CURBITMAP_LEFT = 130;
	private static int FOCUS_MARGIN_LEFT = 0;
	private static int FOCUS_RIGHT_PARAM = 621;
	
	/** 节目单列表  **/
	private List<Schedule> schedules = null;
	
	/** 当前要画得节目单 **/
	private List<Schedule> curDrawSchedule = new ArrayList<Schedule>();
	/** 画笔  **/
	private Paint mPaint;
	
	/** 焦点图片**/
	private Bitmap bitmapFocus = null;
	/** 非焦点图片**/
	@SuppressWarnings("unused")
	private Bitmap bitmapNoFocus = null;
	/** 代表当前播放的图片**/
	private Bitmap bitmapIcon = null;
	
	/** 当前焦点序号**/
	private int curFocusIndex = 0;

	/** 列表是否是循环  **/
	@SuppressWarnings("unused")
	private boolean isloop = false;
	
	private boolean isFocus = true;
	
	public ViewReSeeSchedulelList(Context context)
	{
		super(context);
		NUM_LEFT = Method.getScaleX(240);
		TEXT_LEFT = Method.getScaleX(260);
		TEXT_H = Method.getScaleY(56);
		CURBITMAP_LEFT = (int) getResources().getDimension(R.dimen.resee_cur_bitmap_left);
		FOCUS_MARGIN_LEFT = (int) getResources().getDimension(R.dimen.resee_focus_left);
		FOCUS_RIGHT_PARAM = (int) getResources().getDimension(R.dimen.resee_focus_right);
	}
	
	public ViewReSeeSchedulelList(Context context, AttributeSet attrs) {
		super(context, attrs);
		NUM_LEFT = Method.getScaleX(240);
		TEXT_LEFT = Method.getScaleX(260);
		TEXT_H = Method.getScaleY(56);
		CURBITMAP_LEFT = (int) getResources().getDimension(R.dimen.resee_cur_bitmap_left);
		FOCUS_MARGIN_LEFT = (int) getResources().getDimension(R.dimen.resee_focus_left);
		FOCUS_RIGHT_PARAM = (int) getResources().getDimension(R.dimen.resee_focus_right);
		initParam(context);
	}
	
	
	private void initParam(Context context)
	{
		mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(getResources().getDimensionPixelSize(R.dimen.px_6));
        mPaint.setColor(Color.WHITE);
        
        bitmapFocus = BitmapFactory.decodeResource(context.getResources(), R.drawable.backplay_schedule_focus);
        bitmapNoFocus  = BitmapFactory.decodeResource(context.getResources(), R.color.transparent_background);
        bitmapIcon =  BitmapFactory.decodeResource(context.getResources(), R.drawable.backplay_current);
	}

	int top = 0;
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
//		LogUtil.e(tag, "onDraw","schedule on draw start");
		canvas.drawColor(Color.TRANSPARENT);
		
		int bH = TEXT_H * 4;
		if(curDrawSchedule.size() > 0)
		{
			if(isFocus)
			{
				canvas.drawBitmap(bitmapFocus, new Rect(0, 0, bitmapFocus.getWidth(), bitmapFocus.getHeight()),
						new Rect(Method.getScaleX(7) + FOCUS_MARGIN_LEFT, bH+Method.getScaleX(20), FOCUS_RIGHT_PARAM, bH + Method.getScaleY(76)), mPaint);
			}
			
		}
		
		canvas.save();
		
		canvas.translate(0,Method.getScaleY(60) - top);
		
		drawScheduleNum(canvas);
		
		drawScheduleText(canvas);
		
		canvas.restore();
		
	}

	/**
	 *           
	 * @param src
	 * @param isReloadCurCategary
	 */
	public void setSchedules(List<Schedule> src)
	{
		if(src == null)
		{
			LogUtil.e(tag, "setSchedules","setSchedules is null");
			curDrawSchedule = new ArrayList<Schedule>();
			postInvalidate();
			return;
		}
		
		schedules = src;
		TOP = 0;
		getNoLoopCurDrawList();
		isloop = false;
		
		postInvalidate();
	}
	/** 鍚戜笅绉诲姩  **/
	public void keyDown_Down()
	{
		LogUtil.i(tag, "setSchedules","moveDown is start:");
		
		if(schedules == null || schedules.size() == 0)
		{
			LogUtil.e(tag, "setSchedules","schedules is null or size is 0");
			return;
		}
		

		curFocusIndex = curFocusIndex + 1;
		if(curFocusIndex > schedules.size() - 1)
		{
			curFocusIndex = schedules.size() - 1;
			return;
		}
		
		refreshList();
			
	}
	
	
	/** 鍚戜笂绉诲姩 **/
	public void keyDown_up()
	{
		LogUtil.i(tag, "keyDown_up","moveUp is start:curFocusIndex-->" + curFocusIndex);
		if(schedules == null || schedules.size() == 0)
		{
			LogUtil.e(tag, "keyDown_up","schedules is null or size is 0");
			return;
		}
		
		curFocusIndex = curFocusIndex - 1;
		if(curFocusIndex < 0)
		{
			curFocusIndex = 0;
			return;
		}
		
		
		refreshList();
		
	}
	
	public void refreshList()
	{
		getNoLoopCurDrawList();
		
		postInvalidate();
	}
	
	/**
	 *      缁樺埗棰戦亾锟�
	 */
	private void drawScheduleNum(Canvas canvas)
	{
		mPaint.setTextAlign(Paint.Align.RIGHT);
		mPaint.setTextSize(getResources().getDimension(R.dimen.resee_schedule_time_size));
		for(int i = 0; i < curDrawSchedule.size();i++)
		{
			if (curDrawSchedule.get(i).getStatus().equals("1")) {
				mPaint.setColor(getResources().getColor(R.color.backplay_future_text));
			}else {
				mPaint.setColor(getResources().getColor(R.color.white));
			}
			if (curDrawSchedule.get(i).isCurrent()) {
				Paint _mPaint = new Paint();
		        _mPaint.setAntiAlias(true);
		        _mPaint.setStrokeWidth(getResources().getDimensionPixelSize(R.dimen.px_6));
		        _mPaint.setColor(Color.WHITE);
				canvas.drawBitmap(bitmapIcon, CURBITMAP_LEFT, TOP + TEXT_H * i - Method.getScaleY(20), _mPaint);
			}
			String text = getTime(i);
			canvas.drawText(text, NUM_LEFT, TOP + TEXT_H * i-2, mPaint);
			
		}
	}
	
	/**将startTime转换成为HH：mm的形式**/
	private String getTime(int i) 
	{
		String res = curDrawSchedule.get(i).getStartTime();
		String hh = res.substring(0,2);
		String mm = res.substring(2, 4);
		return hh + ":" + mm;
	}

	/**
	 *    缁樺埗棰戦亾
	 */
	private void drawScheduleText(Canvas canvas)
	{
		mPaint.setTextAlign(Paint.Align.LEFT);
		mPaint.setTextSize(getResources().getDimension(R.dimen.resee_channel_text_size));
		for(int i = 0; i < curDrawSchedule.size();i++)
		{
			if (curDrawSchedule.get(i).getStatus().equals("1")) 
			{
				mPaint.setColor(getResources().getColor(R.color.backplay_future_text));
			}else 
			{
				mPaint.setColor(getResources().getColor(R.color.white));
			}
			if (curDrawSchedule.get(i).getProgramName().trim().length() > 10) 
			{
				canvas.drawText(curDrawSchedule.get(i).getProgramName().trim(), 0, Method.getScaleX(10), TEXT_LEFT, TOP + TEXT_H * i, mPaint);
			}else
			{
				canvas.drawText(curDrawSchedule.get(i).getProgramName().trim(), TEXT_LEFT, TOP + TEXT_H * i, mPaint);
			}
		}
	}
	
	/**
	 *     鑾峰緱褰撳墠鍒楄〃寰幆鐨勬暟锟�
	 */
	private void getLoopCurDrawList()
	{
		LogUtil.i(tag, "getLoopCurDrawList","getCurDrawList is start:");
		
		removeAllDrawSchedule();

		

		int index = 0;
		//涓婇潰4锟�
		for(int i = 3; i > 0 ;i--)
		{
			index = curFocusIndex - i;
			if(index < 0)
			{
//				index = schedules.size() + index;
				TOP = TOP + TEXT_H;
			}
			
			addDrawSchedule(getSingleSchedules(index));
		}
		
		//鐒︾偣
		addDrawSchedule(getSingleSchedules(curFocusIndex));

		//涓嬮潰4锟�
		for(int i = 1; i < 4;i++)
		{
			index = curFocusIndex + i;
			if(index > schedules.size() - 1)
			{
				index = index - schedules.size();
			}
			
			addDrawSchedule(getSingleSchedules(index));
		}
		
		LogUtil.i(tag, "getLoopCurDrawList","curFocusIndex:" + curFocusIndex);
//		printContentSchedule(curDrawSchedule);
	
		
	}
	
	/**
	 *    鑾峰緱褰撳墠缁樺埗鍒楄〃涓嶅惊鐜殑鏁版嵁
	 */
	private void getNoLoopCurDrawList()
	{
		if(schedules == null)
		{
			return;
		}
		TOP = 0;
		removeAllDrawSchedule();
		LogUtil.e(tag,"getNoLoopCurDrawList", "start");
//		curDrawSchedule = schedules;
//		LogUtil.e(tag, "22222222222222222 + schedules.size = " + schedules);
		
//		if (schedules.size() > 10) {
		int index = 0;
		//涓婇潰4锟�
		for(int i = 4; i > 0 ;i--)
		{
			index = curFocusIndex - i;
			if(index < 0)
			{
//				index = schedules.size() + index;
				TOP = TOP + TEXT_H;
				continue;
			}
			addDrawSchedule(getSingleSchedules(index));
		}
		//鐒︾偣
		addDrawSchedule(getSingleSchedules(curFocusIndex));
		//涓嬮潰4锟�
		for(int i = 1; i < 6;i++)
		{
			index = curFocusIndex + i;
			if(index > schedules.size() - 1)
			{
//				index = index - schedules.size();
				continue;
			}
			
			addDrawSchedule(getSingleSchedules(index));
		}
		
		LogUtil.i(tag, "getNoLoopCurDrawList","curFocusIndex:" + curFocusIndex);
		
	}
	
	@SuppressWarnings("unused")
	private void initData()
	{
		schedules = new ArrayList<Schedule>();
		Schedule schedule = null;
		for(int i = 0; i < 4;i++)
		{
			schedule = new Schedule();
			schedule.setStartTime("11:20");
			schedule.setProgramName("CCTV" + i);
			schedules.add(schedule);
		}
		
		if(schedules.size() >= 7)
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
	
	private Schedule getSingleSchedules(int index)
	{
		if(schedules == null)
		{
			LogUtil.e(tag, "getSingleSchedules","getSingleSchedules is null");
			return null;
		}
		
		if(index < 0 || index > schedules.size() - 1)
		{
			LogUtil.e(tag, "getSingleSchedules","getSingleSchedules array over index:" + index + ",size:" + schedules.size());
			return null;
		}
		
		return schedules.get(index);
	}
	
	private void addDrawSchedule(Schedule schedule)
	{
		if(schedule == null)
		{
			LogUtil.i(tag, "addDrawSchedule","addDrawSchedule is null");
			return;
		}
		
		curDrawSchedule.add(schedule);
	}
	
	private void removeAllDrawSchedule()
	{
		if(curDrawSchedule == null)
		{
			LogUtil.i(tag, "removeAllDrawSchedule","removeAllDrawSchedule is null");
			return;
		}
		
		curDrawSchedule.clear();
	}
	
	@SuppressWarnings("unused")
	private void printContentSchedule(List<Schedule> list) {
		if (list == null) {
			LogUtil.e("printContentSchedule", "printContentSchedule","list isChangeChangeInNumber null");
			return;
		}

//		for (int i = 0; i < list.size(); i++) {
//			LogUtil.i(tag,list.get(i).getProgramName(), list.get(i).toString());
//		}
	}
	
	public boolean isFocus() {
		return isFocus;
	}

	public void setFocus(boolean isFocus) {
		this.isFocus = isFocus;
		postInvalidate();
	}
	
	public int getCurFocusIndex() {
		return curFocusIndex;
	}

	public void setCurFocusIndex(int curFocusIndex) {
		this.curFocusIndex = curFocusIndex;
	}
	
	public void clearDrawList()
	{
		curDrawSchedule.clear();
		postInvalidate();
	}
}
