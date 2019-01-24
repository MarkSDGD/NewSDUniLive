package com.xike.xkliveplay.framework.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author LiWei <br>
 * CreateTime: 2014年10月30日 上午9:59:21<br>
 * <b>Info:</b><br>
 *<br>
 *   <b>Method:</b> <br>
 */
public class GetContentListRes {
	/** 总记录数 **/
	private String totalCount = "";
	/** 本次返回的记录数 **/
	private String currentCount = "";
	/** 内容记录集合 **/
	private Content contentList = new Content();
	/** 频道 **/
	private List<ContentChannel> channelList = new ArrayList<ContentChannel>();

	public List<ContentChannel> getChannelList() {
		return channelList;
	}

	public void setChannelList(List<ContentChannel> channelList) {
		this.channelList = channelList;
	}

	public GetContentListRes() {
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

	public Content getContentList() {
		return contentList;
	}

	public void setContentList(Content contentList) {
		this.contentList = contentList;
	}

	@Override
	public String toString() {
		return "GetContentListRes [totalCount=" + totalCount
				+ ", currentCount=" + currentCount + ", contentList="
				+ contentList + ", channelList=" + channelList + "]";
	}



}
