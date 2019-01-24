package com.xike.xkliveplay.framework.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author LiWei <br>
 * CreateTime: 2014年10月30日 下午1:44:43<br>
 * <b>Info:</b><br>
 *<br>
 *   <b>Method:</b> <br>
 */
public class GetScheduleListRes {
	/**总记录数**/
	private String totalCount = "";
	/**本次返回的记录数**/
	private String currentCount = "";
	/**包含多个Schedule的集合**/
	private List<Schedule> scheduleList = new ArrayList<Schedule>();

	public GetScheduleListRes() {
	}

	public String getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(String totalCount) {
		this.totalCount = totalCount;
	}

	public String getCurrentCount() {
		return currentCount;
	}

	public void setCurrentCount(String currentCount) {
		this.currentCount = currentCount;
	}

	public List<Schedule> getScheduleList() {
		return scheduleList;
	}

	public void setScheduleList(List<Schedule> scheduleList) {
		this.scheduleList = scheduleList;
	}

	@Override
	public String toString() {
		return "GetScheduleListRes [totalCount=" + totalCount
				+ ", currentCount=" + currentCount + ", scheduleList="
				+ scheduleList + "]";
	}

}
