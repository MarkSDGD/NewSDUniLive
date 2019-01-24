/**
 * @Title: GDOrdercmhCheckOrder.java
 * @Package com.xike.xkliveplay.framework.entity.gd
 * @Description: TODO
 * Copyright: Copyright (c) 2015
 * Company:青岛博瑞立方网络科技有限公司
 * 
 * @author Mernake
 * @date 2018年7月19日 上午10:20:28
 * @version V1.0
 */
package com.xike.xkliveplay.framework.entity.gd;

/**
  * @ClassName: GDOrdercmhCheckOrder
  * @Description: TODO
  * @author Mernake
  * @date 2018年7月19日 上午10:20:28
  *
  */
public class GDOrdercmhCheckOrderRes extends GDAuthBase
{
	private GDOrdercmhCheckOrderData data = new GDOrdercmhCheckOrderData();
	
	
	
	
	
	@Override
	public String toString() {
		return "GDOrdercmhCheckOrder [toString()=" + super.toString() + "]";
	}





	/**
	 * @return the data
	 */
	public GDOrdercmhCheckOrderData getData() {
		return data;
	}





	/**
	 * @param data the data to set
	 */
	public void setData(GDOrdercmhCheckOrderData data) {
		this.data = data;
	}





	public class GDOrdercmhCheckOrderData {
		private String createTime = "";
		private String orderID = "";
		private String orderStatus = "";
		/**
		 * @return the createTime
		 */
		public String getCreateTime() {
			return createTime;
		}
		/**
		 * @param createTime the createTime to set
		 */
		public void setCreateTime(String createTime) {
			this.createTime = createTime;
		}
		/**
		 * @return the orderID
		 */
		public String getOrderID() {
			return orderID;
		}
		/**
		 * @param orderID the orderID to set
		 */
		public void setOrderID(String orderID) {
			this.orderID = orderID;
		}
		/**
		 * @return the orderStatus
		 */
		public String getOrderStatus() {
			return orderStatus;
		}
		/**
		 * @param orderStatus the orderStatus to set
		 */
		public void setOrderStatus(String orderStatus) {
			this.orderStatus = orderStatus;
		}
		/*
		  * <p>Title: toString</p>
		  * <p>Description: </p>
		  * @return
		  * @see java.lang.Object#toString()
		  */
		@Override
		public String toString() {
			return "GDOrdercmhCheckOrderData [createTime=" + createTime + ", orderID=" + orderID + ", orderStatus="
					+ orderStatus + "]";
		}
		
		
	}
}
