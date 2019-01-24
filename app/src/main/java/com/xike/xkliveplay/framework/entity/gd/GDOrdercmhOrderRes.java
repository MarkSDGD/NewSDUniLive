/**
 * @Title: GDOrdercmhOrderRes.java
 * @Package com.xike.xkliveplay.framework.entity.gd
 * @Description: TODO
 * Copyright: Copyright (c) 2015
 * Company:青岛博瑞立方网络科技有限公司
 * 
 * @author Mernake
 * @date 2018年7月19日 上午10:47:49
 * @version V1.0
 */
package com.xike.xkliveplay.framework.entity.gd;

/**
  * @ClassName: GDOrdercmhOrderRes
  * @Description: TODO
  * @author Mernake
  * @date 2018年7月19日 上午10:47:49
  *
  */
public class GDOrdercmhOrderRes extends GDAuthBase
{
	private GDOrdercmhOrderData data = new GDOrdercmhOrderData();
	
	
	
	
	/**
	 * @return the data
	 */
	public GDOrdercmhOrderData getData() {
		return data;
	}


	
	


	/*
	  * <p>Title: toString</p>
	  * <p>Description: </p>
	  * @return
	  * @see java.lang.Object#toString()
	  */
	@Override
	public String toString() {
		return "GDOrdercmhOrderRes [toString()=" + super.toString() + "]";
	}






	/**
	 * @param data the data to set
	 */
	public void setData(GDOrdercmhOrderData data) {
		this.data = data;
	}




	public class GDOrdercmhOrderData{
		private String code = "";
		private String orderID = "";
		private String payURL = "";
		/**
		 * @return the code
		 */
		public String getCode() {
			return code;
		}
		/**
		 * @param code the code to set
		 */
		public void setCode(String code) {
			this.code = code;
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
		 * @return the payURL
		 */
		public String getPayURL() {
			return payURL;
		}
		/**
		 * @param payURL the payURL to set
		 */
		public void setPayURL(String payURL) {
			this.payURL = payURL;
		}
		/*
		  * <p>Title: toString</p>
		  * <p>Description: </p>
		  * @return
		  * @see java.lang.Object#toString()
		  */
		@Override
		public String toString() {
			return "GDOrdercmhOrderData [code=" + code + ", orderID=" + orderID + ", payURL=" + payURL + "]";
		}
		
		
	}
}
