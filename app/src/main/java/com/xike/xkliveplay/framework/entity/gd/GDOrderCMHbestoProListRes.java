/**
 * @Title: GDOrderbestoProListRes.java
 * @Package com.xike.xkliveplay.framework.entity.gd
 * @Description: TODO
 * Copyright: Copyright (c) 2015
 * Company:青岛博瑞立方网络科技有限公司
 * 
 * @author Mernake
 * @date 2018年7月19日 上午9:34:45
 * @version V1.0
 */
package com.xike.xkliveplay.framework.entity.gd;

import com.shandong.sdk.shandongsdk.bean.CMHwAuthReturn;

import java.util.ArrayList;
import java.util.List;

/**
  * @ClassName: GDOrderbestoProListRes
  * @Description: TODO
  * @author Mernake
  * @date 2018年7月19日 上午9:34:45
  *
  */
public class GDOrderCMHbestoProListRes extends GDAuthBase
{
	private List<CMHwAuthReturn.ProductlistBean> data = new ArrayList<CMHwAuthReturn.ProductlistBean>();

	/**
	 * @return the data
	 */
	public List<CMHwAuthReturn.ProductlistBean> getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(List<CMHwAuthReturn.ProductlistBean> data) {
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
		return "GDOrderbestoProListRes [toString()=" + super.toString() + "]";
	}
	
	
}
