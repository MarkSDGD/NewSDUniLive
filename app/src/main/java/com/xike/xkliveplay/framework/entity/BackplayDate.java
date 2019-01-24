/**
 * @author liwei
 * @d2014-2-27
 */
package com.xike.xkliveplay.framework.entity;

/**
 * @author lw
 * 
 */
public class BackplayDate {
	/** ���գ�2��16�� **/
	private String MonthDay = "";
	/** ���ڼ��� ������ **/
	private String weekday = "";

	/** �ǵ�ǰ�죬���Ӧ�Ľ�Ŀ��Ӧ������һ�����ǵı�ʾ  ����ʾ���ڲ��ŵĽ�Ŀ**/
	private boolean isFuture = false;
	
	private boolean isFocused = false;
	
	
	
	

	/**
	 * @return the isFocused
	 */
	public boolean isFocused() {
		return isFocused;
	}

	/**
	 * @param isFocused the isFocused to set
	 */
	public void setFocused(boolean isFocused) {
		this.isFocused = isFocused;
	}

	public boolean isFuture() {
		return isFuture;
	}

	public void setFuture(boolean isFuture) {
		this.isFuture = isFuture;
	}

	public BackplayDate() {
	}

	public BackplayDate(String monthDay, String weekday) {
		super();
		MonthDay = monthDay;
		this.weekday = weekday;
	}

	@Override
	public String toString() {
		return "BackplayDate [MonthDay=" + MonthDay + ", weekday=" + weekday
				+ ", isFuture=" + isFuture + "]";
	}

	public String getMonthDay() {
		return MonthDay;
	}

	public void setMonthDay(String monthDay) {
		MonthDay = monthDay;
	}

	public String getWeekday() {
		return weekday;
	}

	public void setWeekday(String weekday) {
		this.weekday = weekday;
	}

}
