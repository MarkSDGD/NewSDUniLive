package com.xike.xkliveplay.entity;

import com.xike.xkliveplay.framework.entity.PictureList;

/**
 * 
 * @author LiWei <br>
 * CreateTime: 2014年10月30日 上午10:01:14<br>
 * <b>Info:</b><br>
 *<br>
 *   <b>Method:</b> <br>
 */
public class Series {
	/** 全局唯一标识 **/
	private String contentId = "";
	/** 连续剧名称 **/
	private String name = "";
	/** 原名 **/
	private String originalName = "";
	/** 总集数 **/
	private String volumnCount = "";
	/** 首播日期(YYYYMMDD) **/
	private String orgAirDate = "";
	/** 描述信息 **/
	private String description = "";
	/** 节目提供商 **/
	private String contentProvider = "";
	/** 看点，非常简短的剧情描述 **/
	private String viewPoint = "";
	/** 推荐星级从1-10，数字越大推荐星级越高，缺省为3颗星 **/
	private String starLevel = "";
	/** 限制类别，采用国际通用的Rating等级 **/
	private String rating = "";
	/** 所含奖项，多个奖项之间使用；分隔 **/
	private String awards = "";
	/** 海报列表，包含多个picture **/
	private PictureList pictureList = new PictureList();
	/** 是否有收藏 1：表示收藏过；2：表示未收藏过 **/
	private String favoriteFlg = "";

	public Series() {
	}

	public String getContentId() {
		return contentId;
	}

	public void setContentId(String contentId) {
		this.contentId = contentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOriginalName() {
		return originalName;
	}

	public void setOriginalName(String originalName) {
		this.originalName = originalName;
	}

	public String getVolumnCount() {
		return volumnCount;
	}

	public void setVolumnCount(String volumnCount) {
		this.volumnCount = volumnCount;
	}

	public String getOrgAirDate() {
		return orgAirDate;
	}

	public void setOrgAirDate(String orgAirDate) {
		this.orgAirDate = orgAirDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getContentProvider() {
		return contentProvider;
	}

	public void setContentProvider(String contentProvider) {
		this.contentProvider = contentProvider;
	}

	public String getViewPoint() {
		return viewPoint;
	}

	public void setViewPoint(String viewPoint) {
		this.viewPoint = viewPoint;
	}

	public String getStarLevel() {
		return starLevel;
	}

	public void setStarLevel(String starLevel) {
		this.starLevel = starLevel;
	}

	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public String getAwards() {
		return awards;
	}

	public void setAwards(String awards) {
		this.awards = awards;
	}

	public PictureList getPictureList() {
		return pictureList;
	}

	public void setPictureList(PictureList pictureList) {
		this.pictureList = pictureList;
	}

	public String getFavoriteFlg() {
		return favoriteFlg;
	}

	public void setFavoriteFlg(String favoriteFlg) {
		this.favoriteFlg = favoriteFlg;
	}

	@Override
	public String toString() {
		return "Series [contentId=" + contentId + ", name=" + name
				+ ", originalName=" + originalName + ", volumnCount="
				+ volumnCount + ", orgAirDate=" + orgAirDate + ", description="
				+ description + ", contentProvider=" + contentProvider
				+ ", viewPoint=" + viewPoint + ", starLevel=" + starLevel
				+ ", rating=" + rating + ", awards=" + awards
				+ ", pictureList=" + pictureList + ", favoriteFlg="
				+ favoriteFlg + "]";
	}
	

}
