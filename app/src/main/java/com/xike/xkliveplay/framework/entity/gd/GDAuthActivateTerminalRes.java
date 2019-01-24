/**
 * @Title: GDAuthActivateTerminalRes.java
 * @Package com.xike.xkliveplay.framework.entity.gd
 * @Description: TODO
 * Copyright: Copyright (c) 2015
 * Company:青岛博瑞立方网络科技有限公司
 * 
 * @author Mernake
 * @date 2018年7月18日 上午11:00:43
 * @version V1.0
 */
package com.xike.xkliveplay.framework.entity.gd;

/**
  * @ClassName: GDAuthActivateTerminalRes
  * @Description: TODO
  * @author Mernake
  * @date 2018年7月18日 上午11:00:43
  *
  */
public class GDAuthActivateTerminalRes extends GDAuthBase
{
	private GDAuthActivateTerminalData data = new GDAuthActivateTerminalData();
	
	
	
	/**
	 * @return the data
	 */
	public GDAuthActivateTerminalData getData() {
		return data;
	}



	/**
	 * @param data the data to set
	 */
	public void setData(GDAuthActivateTerminalData data) {
		this.data = data;
	}



	public class GDAuthActivateTerminalData {
		private String status = "";
		private String userId = "";
		/**
		 * @return the status
		 */
		public String getStatus() {
			return status;
		}
		/**
		 * @param status the status to set
		 */
		public void setStatus(String status) {
			this.status = status;
		}
		/**
		 * @return the userId
		 */
		public String getUserId() {
			return userId;
		}
		/**
		 * @param userId the userId to set
		 */
		public void setUserId(String userId) {
			this.userId = userId;
		}
		/*
		  * <p>Title: toString</p>
		  * <p>Description: </p>
		  * @return
		  * @see java.lang.Object#toString()
		  */
		@Override
		public String toString() {
			return "GDAuthActivateTerminalData [status=" + status + ", userId=" + userId + "]";
		}
		
		
	}



	/*
	  * <p>Title: toString</p>
	  * <p>Description: </p>
	  * @return
	  * @see java.lang.Object#toString()
	  */
	@Override
	public String toString() {
		return "GDAuthActivateTerminalRes [toString()=" + super.toString() + "]";
	}
	
	
}
