package com.xike.xkliveplay.framework.entity;

import java.io.Serializable;

/**
 * 
 * @author LiWei <br>
 * CreateTime: 2014年11月5日 下午5:08:47<br>
 * <b>Info:</b><br>
 *<br>
 *   <b>Method:</b> <br>
 */
public class ContentChannel implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 全局唯一标识 **/
	private String contentId = "";
	/** 频道号 **/
	private String channelNumber = "";
	/** 频道名称 **/
	private String name = "";
	/** 台标名称 **/
	private String callSign = "";
	/** 时移标志 0：不生效 1：生效 **/
	private String timeShift = "";
	/** 默认时移时长，单位分钟 **/
	private String timeShiftDuration = "";
	/**用这个来存下vip角标的下载地址吧**/
	private String description = "";
	/** 国家 -- 用来保存是否可以播放，替换扩充的字段orderFlag,0代表未订购**/
	private String country = "";
	/** 州/省 用来保存在回看中的时候是否获得了焦点 **/
	private String state = "";
	/** 保存是否是最近观看  **/
	private String city = "";
	/** 邮编 --用来保存是否收藏 save **/
	private String zipCode = "";
	/** 频道播放URL **/
	private String playURL = "";
	/** 频道Logo URL 用这个来存下一下当前播放的节目吧**/
	private String logoURL = "";
	/** 语言 --来存一下当前频道所属的类别 **/
	private String language = "";
	/** 时移URL **/
	private String timeShiftURL = "";


	

	public String getTimeShiftURL() 
	{
		return timeShiftURL;
	}

	public void setTimeShiftURL(String timeShiftURL) 
	{
		this.timeShiftURL = timeShiftURL;
	}

	public ContentChannel() {
	}

	public String getContentId() {
		return contentId;
	}

	public void setContentId(String contentId) {
		this.contentId = contentId;
	}

	public String getChannelNumber() {
		return channelNumber;
	}

	public void setChannelNumber(String channelNumber) {
		this.channelNumber = channelNumber;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCallSign() {
		return callSign;
	}

	public void setCallSign(String callSign) {
		this.callSign = callSign;
	}

	public String getTimeShift() {
		return timeShift;
	}

	public void setTimeShift(String timeShift) {
		this.timeShift = timeShift;
	}

	public String getTimeShiftDuration() {
		return timeShiftDuration;
	}

	public void setTimeShiftDuration(String timeShiftDuration) {
		this.timeShiftDuration = timeShiftDuration;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCity() {
		return city;
	}
	/** 保存是否是最近观看  recent**/
	public void setCity(String city) {
		this.city = city;
	}

	/** 邮编 --用来保存是否收藏 **/
	public String getZipCode() {
		return zipCode;
	}
	/** 邮编 --用来保存是否收藏save **/
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getPlayURL() {
		return playURL;
	}

	public void setPlayURL(String playURL) {
		this.playURL = playURL;
	}

	public String getLogoURL() {
		return logoURL;
	}

	public void setLogoURL(String logoURL) {
		this.logoURL = logoURL;
	}

	/** 语言 --来存一下当前频道所属的类别 **/
	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	@Override
	public String toString() {
		return "Channel [contentId=" + contentId + ", channelNumber="
				+ channelNumber + ", name=" + name + ", city="
				+ city + ", zipCode=" + zipCode + ", playURL=" + playURL
				+ ", language=" + language + "]";
	}

}
