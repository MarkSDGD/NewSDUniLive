/**
 * @Title: GDAuthAuthenticationRes.java
 * @Package com.xike.xkliveplay.framework.entity.gd
 * @Description: TODO
 * Copyright: Copyright (c) 2015
 * Company:青岛博瑞立方网络科技有限公司
 * 
 * @author Mernake
 * @date 2018年7月18日 上午11:07:43
 * @version V1.0
 */
package com.xike.xkliveplay.framework.entity.gd;

/**
  * @ClassName: GDAuthAuthenticationRes
  * @Description: TODO
  * @author Mernake
  * @date 2018年7月18日 上午11:07:43
  *
  */
public class GDAuthAuthenticationRes extends GDAuthBase
{
	private String data = "";

	/**
	 * @return the data
	 */
	public String getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(String data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "GDAuthAuthenticationRes [data=" + data + ", toString()=" + super.toString() + "]";
	}
	
	
}
