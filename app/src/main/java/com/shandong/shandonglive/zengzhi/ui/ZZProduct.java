/**
 * @Title: ZZProduct.java
 * @Package com.shandong.shandonglive.zengzhi.ui
 * @Description: TODO
 * Copyright: Copyright (c) 2015
 * Company:青岛博瑞立方网络科技有限公司
 * 
 * @author Mernake
 * @date 2018年7月10日 上午11:04:37
 * @version V1.0
 */
package com.shandong.shandonglive.zengzhi.ui;

/**
  * @ClassName: ZZProduct
  * @Description: TODO
  * @author Mernake
  * @date 2018年7月10日 上午11:04:37
  *
  */
public class ZZProduct 
{
	private String name = "";
	private String priceLeft = "";
	private String price = "";
	private String priceRight = "";
	private String oriPrice = "";
	private String date = "";
	private String info = "";

	private String productDesc = "";

	private String channelid = "";
	private String startTime = "";
	private String productId = "";
	private String serviceId = "";
	private String continueAble = "";

	public String getContinueAble() {
		return continueAble;
	}

	public void setContinueAble(String continueAble) {
		this.continueAble = continueAble;
	}

	private int productType;

	public int getProductType() {
		return productType;
	}

	public void setProductType(int productType) {
		this.productType = productType;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getChannelid() {
		return channelid;
	}

	public void setChannelid(String channelid) {
		this.channelid = channelid;
	}

	public String getProductDesc() {
		return productDesc;
	}

	public void setProductDesc(String productDesc) {
		this.productDesc = productDesc;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the priceLeft
	 */
	public String getPriceLeft() {
		return priceLeft;
	}
	/**
	 * @param priceLeft the priceLeft to set
	 */
	public void setPriceLeft(String priceLeft) {
		this.priceLeft = priceLeft;
	}
	/**
	 * @return the price
	 */
	public String getPrice() {
		return price;
	}
	/**
	 * @param price the price to set
	 */
	public void setPrice(String price) {
		this.price = price;
	}
	/**
	 * @return the priceRight
	 */
	public String getPriceRight() {
		return priceRight;
	}
	/**
	 * @param priceRight the priceRight to set
	 */
	public void setPriceRight(String priceRight) {
		this.priceRight = priceRight;
	}
	/**
	 * @return the oriPrice
	 */
	public String getOriPrice() {
		return oriPrice;
	}
	/**
	 * @param oriPrice the oriPrice to set
	 */
	public void setOriPrice(String oriPrice) {
		this.oriPrice = oriPrice;
	}
	/**
	 * @return the date
	 */
	public String getDate() {
		return date;
	}
	/**
	 * @param date the date to set
	 */
	public void setDate(String date) {
		this.date = date;
	}
	/**
	 * @return the info
	 */
	public String getInfo() {
		return info;
	}
	/**
	 * @param info the info to set
	 */
	public void setInfo(String info) {
		this.info = info;
	}
	/*
	  * <p>Title: toString</p>
	  * <p>Description: </p>
	  * @return
	  * @see java.lang.Object#toString()
	  */
	@Override
	public String toString() {
		return "ZZProduct [name=" + name + ", priceLeft=" + priceLeft + ", price=" + price + ", priceRight="
				+ priceRight + ", oriPrice=" + oriPrice + ", date=" + date + ", info=" + info + "]";
	}
	
	
}
