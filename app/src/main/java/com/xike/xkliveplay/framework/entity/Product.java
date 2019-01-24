/**
 * @author liwei
 * @d2014-2-26
 */
package com.xike.xkliveplay.framework.entity;

/**
 * @author lw
 * 
 */
public class Product {
	/**产品ID**/
	private String productId = "";
	/**产品名称**/
	private String productName = "";
	/**产品价格，以分为单位**/
	private String fee = "";
	/**付费类型	0：包月支付	1：按次支付	2：免费	3：ppv，一次性支付**/
	private String purchaseType = "";
	/**产品描述**/
	private String productDesc = "";
	/**标称价格，以分为单位**/
	private String listPrice = "";
	/**租期,单位小时**/
	private String rentalTerm = "";
	/**可使用次数**/
	private String limitTimes = "";

	public Product() {
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getFee() {
		return fee;
	}

	public void setFee(String fee) {
		this.fee = fee;
	}

	public String getPurchaseType() {
		return purchaseType;
	}

	public void setPurchaseType(String purchaseType) {
		this.purchaseType = purchaseType;
	}

	public String getProductDesc() {
		return productDesc;
	}

	public void setProductDesc(String productDesc) {
		this.productDesc = productDesc;
	}

	public String getListPrice() {
		return listPrice;
	}

	public void setListPrice(String listPrice) {
		this.listPrice = listPrice;
	}

	public String getRentalTerm() {
		return rentalTerm;
	}

	public void setRentalTerm(String rentalTerm) {
		this.rentalTerm = rentalTerm;
	}

	public String getLimitTimes() {
		return limitTimes;
	}

	public void setLimitTimes(String limitTimes) {
		this.limitTimes = limitTimes;
	}

	@Override
	public String toString() {
		return "Product [productId=" + productId + ", productName="
				+ productName + ", fee=" + fee + ", purchaseType="
				+ purchaseType + ", productDesc=" + productDesc
				+ ", listPrice=" + listPrice + ", rentalTerm=" + rentalTerm
				+ ", limitTimes=" + limitTimes + "]";
	}

}
