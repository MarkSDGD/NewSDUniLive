/**
 * @Title: GDAuthGetAccountRes.java
 * @Package com.xike.xkliveplay.framework.entity.gd
 * @Description: TODO
 * Copyright: Copyright (c) 2015
 * Company:青岛博瑞立方网络科技有限公司
 * 
 * @author Mernake
 * @date 2018年7月18日 上午10:47:29
 * @version V1.0
 */
package com.xike.xkliveplay.framework.entity.gd;

/**
  * @ClassName: GDAuthGetAccountRes
  * @Description: TODO
  * @author Mernake
  * @date 2018年7月18日 上午10:47:29
  *
  */
public class GDAuthGetAccountRes extends GDAuthBase
{
	private GDAuthGetAccountData data = new GDAuthGetAccountData();
	
	
	
	/**
	 * @return the data
	 */
	public GDAuthGetAccountData getData() {
		return data;
	}



	/**
	 * @param data the data to set
	 */
	public void setData(GDAuthGetAccountData data) {
		this.data = data;
	}



	@Override
	public String toString() {
		return "GDAuthGetAccountRes [toString()=" + super.toString() + "]";
	}



	public class GDAuthGetAccountData{
		private String account = "";
		private String password = "";
		/**
		 * @return the account
		 */
		public String getAccount() {
			return account;
		}
		/**
		 * @param account the account to set
		 */
		public void setAccount(String account) {
			this.account = account;
		}
		/**
		 * @return the password
		 */
		public String getPassword() {
			return password;
		}
		/**
		 * @param password the password to set
		 */
		public void setPassword(String password) {
			this.password = password;
		}
		/*
		  * <p>Title: toString</p>
		  * <p>Description: </p>
		  * @return
		  * @see java.lang.Object#toString()
		  */
		@Override
		public String toString() {
			return "GDAuthGetAccountData [account=" + account + ", password=" + password + "]";
		}
		
		
	}
}
