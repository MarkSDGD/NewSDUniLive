/**
 * @Title: GDOrderGetProductListInfoRes.java
 * @Package com.xike.xkliveplay.framework.entity.gd
 * @Description: TODO
 * Copyright: Copyright (c) 2015
 * Company:青岛博瑞立方网络科技有限公司
 * 
 * @author Mernake
 * @date 2018年7月19日 上午9:50:15
 * @version V1.0
 */
package com.xike.xkliveplay.framework.entity.gd;

import java.util.ArrayList;
import java.util.List;

/**
  * @ClassName: GDOrderGetProductListInfoRes
  * @Description: TODO
  * @author Mernake
  * @date 2018年7月19日 上午9:50:15
  *
  */
public class GDOrderGetProductListInfoRes extends GDAuthBase
{
	private GDOrderGetProductListInfoData data;
	
	
	
	
	/**
	 * @return the data
	 */
	public GDOrderGetProductListInfoData getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(GDOrderGetProductListInfoData data) {
		this.data = data;
	}
	
	
	@Override
	public String toString() {
		return "GDOrderGetProductListInfoRes [toString()=" + super.toString() + "]";
	}



	public class GDOrderGetProductListInfoData
	{
		private String imgUrl = "";
		private String isVisible = "";
		private String title1 = "";
		private String title2 = "";
		private String[] productTypeDesc;
		private int authTime = 8 * 3600;



		private List<GDOrderProductList> productMsgList = new ArrayList<GDOrderProductList>();


		public String[] getProductTypeDesc() {
			return productTypeDesc;
		}

		public void setProductTypeDesc(String[] productTypeDesc) {
			this.productTypeDesc = productTypeDesc;
		}

		public int getAuthTime() {
			return authTime;
		}

		public void setAuthTime(int authTime) {
			this.authTime = authTime;
		}

		/**
		 * @return the imgUrl
		 */
		public String getImgUrl() {
			return imgUrl;
		}
		/**
		 * @param imgUrl the imgUrl to set
		 */
		public void setImgUrl(String imgUrl) {
			this.imgUrl = imgUrl;
		}
		/**
		 * @return the isVisible
		 */
		public String getIsVisible() {
			return isVisible;
		}
		/**
		 * @param isVisible the isVisible to set
		 */
		public void setIsVisible(String isVisible) {
			this.isVisible = isVisible;
		}
		/**
		 * @return the title1
		 */
		public String getTitle1() {
			return title1;
		}
		/**
		 * @param title1 the title1 to set
		 */
		public void setTitle1(String title1) {
			this.title1 = title1;
		}
		/**
		 * @return the title2
		 */
		public String getTitle2() {
			return title2;
		}
		/**
		 * @param title2 the title2 to set
		 */
		public void setTitle2(String title2) {
			this.title2 = title2;
		}
		/**
		 * @return the productMsgList
		 */
		public List<GDOrderProductList> getProductMsgList() {
			return productMsgList;
		}
		/**
		 * @param productMsgList the productMsgList to set
		 */
		public void setProductMsgList(List<GDOrderProductList> productMsgList) {
			this.productMsgList = productMsgList;
		}
		/*
		  * <p>Title: toString</p>
		  * <p>Description: </p>
		  * @return
		  * @see java.lang.Object#toString()
		  */
		@Override
		public String toString() {
			return "GDOrderGetProductListInfoData [imgUrl=" + imgUrl + ", isVisible=" + isVisible + ", title1=" + title1
					+ ", title2=" + title2 + "]";
		}
		
		
	
	}
	
	public class GDOrderProductList{
		private String oldPrice = "";
		private String peiceHead = "";
		private String priceEnd = "";
		private String productID = "";
		private String vipDescription = "";
		private String vipTime = "";
		/**
		 * @return the oldPrice
		 */
		public String getOldPrice() {
			return oldPrice;
		}
		/**
		 * @param oldPrice the oldPrice to set
		 */
		public void setOldPrice(String oldPrice) {
			this.oldPrice = oldPrice;
		}
		/**
		 * @return the peiceHead
		 */
		public String getPeiceHead() {
			return peiceHead;
		}
		/**
		 * @param peiceHead the peiceHead to set
		 */
		public void setPeiceHead(String peiceHead) {
			this.peiceHead = peiceHead;
		}
		/**
		 * @return the priceEnd
		 */
		public String getPriceEnd() {
			return priceEnd;
		}
		/**
		 * @param priceEnd the priceEnd to set
		 */
		public void setPriceEnd(String priceEnd) {
			this.priceEnd = priceEnd;
		}
		/**
		 * @return the productID
		 */
		public String getProductID() {
			return productID;
		}
		/**
		 * @param productID the productID to set
		 */
		public void setProductID(String productID) {
			this.productID = productID;
		}
		/**
		 * @return the vipDescription
		 */
		public String getVipDescription() {
			return vipDescription;
		}
		/**
		 * @param vipDescription the vipDescription to set
		 */
		public void setVipDescription(String vipDescription) {
			this.vipDescription = vipDescription;
		}
		/**
		 * @return the vipTime
		 */
		public String getVipTime() {
			return vipTime;
		}
		/**
		 * @param vipTime the vipTime to set
		 */
		public void setVipTime(String vipTime) {
			this.vipTime = vipTime;
		}
		/*
		  * <p>Title: toString</p>
		  * <p>Description: </p>
		  * @return
		  * @see java.lang.Object#toString()
		  */
		@Override
		public String toString() {
			return "GDOrderProductList [oldPrice=" + oldPrice + ", peiceHead=" + peiceHead + ", priceEnd=" + priceEnd
					+ ", productID=" + productID + ", vipDescription=" + vipDescription + ", vipTime=" + vipTime + "]";
		}
		
		
	}
}
