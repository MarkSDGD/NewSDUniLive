/**
 * @Title: UploadObject.java
 * @Package com.xike.xkliveplay.framework.entity
 * @Description: TODO
 * Copyright: Copyright (c) 2015
 * Company:青岛博瑞立方网络科技有限公司
 * 
 * @author Mernake
 * @date 2016年6月27日 下午2:19:33
 * @version V1.0
 */
package com.xike.xkliveplay.framework.entity;

/**
 * @ClassName: UploadObject
 * @Description: TODO
 * @author Mernake
 * @date 2016年6月27日 下午2:19:33
 *
 */
public class UploadObject 
{
	private String channelId = "";
	private String channelName = "";
	/**进入入直播频道方式(0:上键，1:下键，2:数字键，3:呼出频道列表按确定键)**/
	private int enterChannelType = 2;
	/**直播频道播放状态；(0:进入,1:离开)**/
	private int channelStatus = 0;
	/**回看节目的媒资ID**/
	private String asssetId = "";
	/**回看节目的起始时间**/
	private String reseeStartTime = "";
	/**回看节目的结束时间**/
	private String reseeEndTime = "";
	/**回看节目总时长，单位秒**/
	private int programDuration = 0;
	/**回看节目播放状态(0:进入,1:离开)**/
	private int programStatus = 0;
	/**回看节目的操作类型(0：播放;1：快进;2：快退;3：暂停)**/
	private int programOperateType = 0;
	/**播放倍速(1×;2×;4×;8×;16×;32×)**/
	private String pace = "1x";
	/**时移开始时间**/
	private String timeshiftStartTime = "";
	private String programName = "";
	
	
	/**栏目id**/
	private String categoryID = "";
	/**父栏目id**/
	private String categoryParentId = "";
	/**栏目类型**/
	private String categoryType = "";
	/**栏目级数**/
	private String categoryLevel = "";
	/**栏目名称**/
	private String cateogryName = "";
	
	/**页面路径**/
	private String pagePath = "";
	/**上一级页面路径**/
	private String srcPagePath = "";
	/**页面名称**/
	private String pageName = "";
	/**如果是VOD详情页面，传入详情页面VOD节目的媒资ID,否则为空。**/
	private String pageAssetId = "";
	/**如果是VOD的详情页从推荐位进入的，传入推荐位ID，否则为0**/
	private String pageId = "0";
	
	
	public String getProgramName() {
		return programName;
	}
	public void setProgramName(String programName) {
		this.programName = programName;
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
	public int getEnterChannelType() {
		return enterChannelType;
	}
	public void setEnterChannelType(int enterChannelType) {
		this.enterChannelType = enterChannelType;
	}
	public int getChannelStatus() {
		return channelStatus;
	}
	public void setChannelStatus(int channelStatus) {
		this.channelStatus = channelStatus;
	}
	public String getAsssetId() {
		return asssetId;
	}
	public void setAsssetId(String asssetId) {
		this.asssetId = asssetId;
	}
	public String getReseeStartTime() {
		return reseeStartTime;
	}
	public void setReseeStartTime(String reseeStartTime) {
		this.reseeStartTime = reseeStartTime;
	}
	public String getReseeEndTime() {
		return reseeEndTime;
	}
	public void setReseeEndTime(String reseeEndTime) {
		this.reseeEndTime = reseeEndTime;
	}
	public int getProgramDuration() {
		return programDuration;
	}
	public void setProgramDuration(int programDuration) {
		this.programDuration = programDuration;
	}
	public int getProgramStatus() {
		return programStatus;
	}
	public void setProgramStatus(int programStatus) {
		this.programStatus = programStatus;
	}
	public int getProgramOperateType() {
		return programOperateType;
	}
	public void setProgramOperateType(int programOperateType) {
		this.programOperateType = programOperateType;
	}
	public String getPace() {
		return pace;
	}
	public void setPace(String pace) {
		this.pace = pace;
	}
	public String getTimeshiftStartTime() {
		return timeshiftStartTime;
	}
	public void setTimeshiftStartTime(String timeshiftStartTime) {
		this.timeshiftStartTime = timeshiftStartTime;
	}
	public String getCategoryID() {
		return categoryID;
	}
	public void setCategoryID(String categoryID) {
		this.categoryID = categoryID;
	}
	public String getCategoryParentId() {
		return categoryParentId;
	}
	public void setCategoryParentId(String categoryParentId) {
		this.categoryParentId = categoryParentId;
	}
	public String getCategoryType() {
		return categoryType;
	}
	public void setCategoryType(String categoryType) {
		this.categoryType = categoryType;
	}
	public String getCategoryLevel() {
		return categoryLevel;
	}
	public void setCategoryLevel(String categoryLevel) {
		this.categoryLevel = categoryLevel;
	}
	public String getCateogryName() {
		return cateogryName;
	}
	public void setCateogryName(String cateogryName) {
		this.cateogryName = cateogryName;
	}
	public String getPagePath() {
		return pagePath;
	}
	public void setPagePath(String pagePath) {
		this.pagePath = pagePath;
	}
	public String getSrcPagePath() {
		return srcPagePath;
	}
	public void setSrcPagePath(String srcPagePath) {
		this.srcPagePath = srcPagePath;
	}
	public String getPageName() {
		return pageName;
	}
	public void setPageName(String pageName) {
		this.pageName = pageName;
	}
	public String getPageAssetId() {
		return pageAssetId;
	}
	public void setPageAssetId(String pageAssetId) {
		this.pageAssetId = pageAssetId;
	}
	public String getPageId() {
		return pageId;
	}
	public void setPageId(String pageId) {
		this.pageId = pageId;
	}
	
	@Override
	public String toString() 
	{
		return "UploadObject [channelId=" + channelId + ", channelName="
				+ channelName + ", enterChannelType=" + enterChannelType
				+ ", channelStatus=" + channelStatus + ", asssetId=" + asssetId
				+ ", reseeStartTime=" + reseeStartTime + ", reseeEndTime="
				+ reseeEndTime + ", programDuration=" + programDuration
				+ ", programStatus=" + programStatus + ", programOperateType="
				+ programOperateType + ", pace=" + pace
				+ ", timeshiftStartTime=" + timeshiftStartTime
				+ ", categoryID=" + categoryID + ", categoryParentId="
				+ categoryParentId + ", categoryType=" + categoryType
				+ ", categoryLevel=" + categoryLevel + ", cateogryName="
				+ cateogryName + ", pagePath=" + pagePath + ", srcPagePath="
				+ srcPagePath + ", pageName=" + pageName + ", pageAssetId="
				+ pageAssetId + ", pageId=" + pageId + "]";
	}
}
