/**
 * @author liwei
 * @d2014-2-25
 */
package com.xike.xkliveplay.framework.entity;

/**
 * @author lw
 * 
 */
public class Schedule {
	/** ȫ��Ψһ��ʶ **/
	private String contentId = "";
	/** Ƶ��ID **/
	private String channelId = "";
	/** Ƶ������ **/
	private String channelName = "";
	/** ��Ŀ���� **/
	private String programName = "";
	/** ��Ŀ��������YYYYMMDD **/
	private String startDate = "";
	/** ��Ŀ����ʱ��HH24MISS **/
	private String startTime = "";
	/** ��Ŀʱ������λ�� **/
	private String duration = "";
	/** ״̬��ʶ 0��ʧЧ 1����Ч **/
	private String status = "";
	/** ���� **/
	private String description = "";
	/** ����url **/
	private String playUrl = "";

	private boolean isCurrent = false;
	
	private boolean isFocus = false;

	public boolean isFocus() {
		return isFocus;
	}

	public void setFocus(boolean isFocus) {
		this.isFocus = isFocus;
	}

	public boolean isCurrent() {
		return isCurrent;
	}

	public void setCurrent(boolean isCurrent) {
		this.isCurrent = isCurrent;
	}

	public Schedule() {
	}

	public String getContentId() {
		return contentId;
	}

	public void setContentId(String contentId) {
		this.contentId = contentId;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public String getProgramName() {
		return programName;
	}

	public void setProgramName(String programName) {
		this.programName = programName;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPlayUrl() {
		return playUrl;
	}

	public void setPlayUrl(String playUrl) {
		this.playUrl = playUrl;
	}

	@Override
	public String toString() {
		return "Schedule [contentId=" + contentId + ", channelId=" + channelId
				+ ", channelName=" + channelName + ", programName="
				+ programName + ", startDate=" + startDate + ", startTime="
				+ startTime + ", duration=" + duration + ", status=" + status
				+ ", description=" + description + ", playUrl=" + playUrl
				+ ", isCurrent=" + isCurrent + "]";
	}

}
