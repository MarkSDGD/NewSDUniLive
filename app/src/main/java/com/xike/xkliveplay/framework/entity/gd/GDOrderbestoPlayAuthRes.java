/**
 * @Title: GDOrderbestoPlayAuthRes.java
 * @Package com.xike.xkliveplay.framework.entity.gd
 * @Description: TODO
 * Copyright: Copyright (c) 2015
 * Company:青岛博瑞立方网络科技有限公司
 * 
 * @author Mernake
 * @date 2018年7月18日 下午3:18:02
 * @version V1.0
 */
package com.xike.xkliveplay.framework.entity.gd;

/**
  * @ClassName: GDOrderbestoPlayAuthRes
  * @Description: TODO
  * @author Mernake
  * @date 2018年7月18日 下午3:18:02
  *
  */
public class GDOrderbestoPlayAuthRes extends GDAuthBase
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

	/*
	  * <p>Title: toString</p>
	  * <p>Description: </p>
	  * @return
	  * @see java.lang.Object#toString()
	  */
	@Override
	public String toString() {
		return "GDOrderbestoPlayAuthRes [data=" + data + ", toString()=" + super.toString() + "]";
	}
	
	
}
