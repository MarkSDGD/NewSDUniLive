/**
 * @Title: AnimObject.java
 * @Package com.xike.xkliveplay.framework.entity
 * @Description: TODO
 * Copyright: Copyright (c) 2015
 * Company:青岛博瑞立方网络科技有限公司
 * 
 * @author Mernake
 * @date 2017年10月30日 下午3:42:30
 * @version V1.0
 */
package com.xike.xkliveplay.framework.entity;

import android.view.View;

/**
  * @ClassName: AnimObject
  * @Description: TODO
  * @author Mernake
  * @date 2017年10月30日 下午3:42:30
  *
  */
public class AnimObject 
{
	private int startX = 0;
	private int startY = 0;
	private int endX = 0;
	private int endY = 0;
	private int endWidth = 0;
	private int endHeight = 0;
	private long duration = 150L;
	private int startWidth = 0;
	private int startHeight = 0;
	private int selectedIndex = 0;
	
	private View view = null;
	
	
	
	
	
	/**
	 * @return the selectedIndex
	 */
	public int getSelectedIndex() {
		return selectedIndex;
	}
	/**
	 * @param selectedIndex the selectedIndex to set
	 */
	public void setSelectedIndex(int selectedIndex) {
		this.selectedIndex = selectedIndex;
	}
	
	/*
	  * <p>Title: toString</p>
	  * <p>Description: </p>
	  * @return
	  * @see java.lang.Object#toString()
	  */
	@Override
	public String toString() {
		return "AnimObject [startX=" + startX + ", startY=" + startY + ", endX=" + endX + ", endY=" + endY
				+ ", endWidth=" + endWidth + ", endHeight=" + endHeight + ", duration=" + duration + ", startWidth="
				+ startWidth + ", startHeight=" + startHeight + ", selectedIndex=" + selectedIndex + ", view=" + view
				+ "]";
	}
	/**
	 * @return the startWidth
	 */
	public int getStartWidth() {
		return startWidth;
	}
	/**
	 * @param startWidth the startWidth to set
	 */
	public void setStartWidth(int startWidth) {
		this.startWidth = startWidth;
	}
	/**
	 * @return the startHeight
	 */
	public int getStartHeight() {
		return startHeight;
	}
	/**
	 * @param startHeight the startHeight to set
	 */
	public void setStartHeight(int startHeight) {
		this.startHeight = startHeight;
	}
	/**
	 * @return the view
	 */
	public View getView() {
		return view;
	}
	/**
	 * @param view the view to set
	 */
	public void setView(View view) {
		this.view = view;
	}
	/**
	 * @return the duration
	 */
	public long getDuration() {
		return duration;
	}
	/**
	 * @param duration the duration to set
	 */
	public void setDuration(long duration) {
		this.duration = duration;
	}
	/**
	 * @return the startX
	 */
	public int getStartX() {
		return startX;
	}
	/**
	 * @param startX the startX to set
	 */
	public void setStartX(int startX) {
		this.startX = startX;
	}
	/**
	 * @return the startY
	 */
	public int getStartY() {
		return startY;
	}
	/**
	 * @param startY the startY to set
	 */
	public void setStartY(int startY) {
		this.startY = startY;
	}
	/**
	 * @return the endX
	 */
	public int getEndX() {
		return endX;
	}
	/**
	 * @param endX the endX to set
	 */
	public void setEndX(int endX) {
		this.endX = endX;
	}
	/**
	 * @return the endY
	 */
	public int getEndY() {
		return endY;
	}
	/**
	 * @param endY the endY to set
	 */
	public void setEndY(int endY) {
		this.endY = endY;
	}
	/**
	 * @return the endWidth
	 */
	public int getEndWidth() {
		return endWidth;
	}
	/**
	 * @param endWidth the endWidth to set
	 */
	public void setEndWidth(int endWidth) {
		this.endWidth = endWidth;
	}
	/**
	 * @return the endHeight
	 */
	public int getEndHeight() {
		return endHeight;
	}
	/**
	 * @param endHeight the endHeight to set
	 */
	public void setEndHeight(int endHeight) {
		this.endHeight = endHeight;
	}
	
	
}
