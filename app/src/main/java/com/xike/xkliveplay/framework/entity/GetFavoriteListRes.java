package com.xike.xkliveplay.framework.entity;

import java.util.ArrayList;
import java.util.List;

public class GetFavoriteListRes {
	/** 总记录数 **/
	private String totalCount = "";
	/** 本次返回的栏目记录数 **/
	private String categoryCount = "";
	private String currentCount = "";

	public String getCurrentCount() {
		return currentCount;
	}

	public void setCurrentCount(String currentCount) {
		this.currentCount = currentCount;
	}

	/** 本次返回的内容记录数 **/
	private String contentCount = "";
	/** 包含多个Category的集合 **/
	private List<Category> categoryList = new ArrayList<Category>();
	/** 包含多个Content的集合 **/
	private List<Content> contentList = new ArrayList<Content>();

	private List<ContentChannel> channelList = new ArrayList<ContentChannel>();

	public List<ContentChannel> getChannelList() {
		return channelList;
	}

	public void setChannelList(List<ContentChannel> channelList) {
		this.channelList = channelList;
	}

	public GetFavoriteListRes() {
	}

	public String getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(String totalCount) {
		this.totalCount = totalCount;
	}

	public String getCategoryCount() {
		return categoryCount;
	}

	public void setCategoryCount(String categoryCount) {
		this.categoryCount = categoryCount;
	}

	public String getContentCount() {
		return contentCount;
	}

	public void setContentCount(String contentCount) {
		this.contentCount = contentCount;
	}

	public List<Category> getCategoryList() {
		return categoryList;
	}

	public void setCategoryList(List<Category> categoryList) {
		this.categoryList = categoryList;
	}

	public List<Content> getContentList() {
		return contentList;
	}

	public void setContentList(List<Content> contentList) {
		this.contentList = contentList;
	}

	@Override
	public String toString() {
		return "GetFavoriteListRes [totalCount=" + totalCount
				+ ", categoryCount=" + categoryCount + ", currentCount="
				+ currentCount + ", contentCount=" + contentCount
				+ ", categoryList=" + categoryList + ", contentList="
				+ contentList + ", channelList=" + channelList + "]";
	}

}
