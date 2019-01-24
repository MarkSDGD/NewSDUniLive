/**
 * @author 章伟
 * @date:2014-2-24 下午06:58:55
 * @version : V1.0
 *
 */

package com.xike.xkliveplay.framework.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zw 业务鉴权响应接口
 */
public class ServiceAuthRes {

	/** 内容的播放URL，业务鉴权通过携带此参数 **/
	private String playUrl = "";
	/**
	 * 本订购关系的鉴权失效时间。用绝对时间表示YYYYDDMMHHMMSS, 如本参数为空，则表示该订购关系的鉴权长期有效，如本参数为当前时间，
	 * 则表示该订购关系为一次性有效
	 **/
	private String expiredTime = "";
	/** 预付费用户余额，单位为分。如为后付费用户，该参数缺失或为空 **/
	private String balance = "";

	private List<Product> productList = new ArrayList<Product>();

	public List<Product> getProductList() {
		return productList;
	}

	public void setProductList(List<Product> productList) {
		this.productList = productList;
	}

	public ServiceAuthRes() {
	}

	public String getPlayUrl() {
		return playUrl;
	}

	public void setPlayUrl(String playUrl) {
		this.playUrl = playUrl;
	}

	public String getExpiredTime() {
		return expiredTime;
	}

	public void setExpiredTime(String expiredTime) {
		this.expiredTime = expiredTime;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	@Override
	public String toString() {
		return "ServiceAuthRes [playUrl=" + playUrl + ", expiredTime="
				+ expiredTime + ", balance=" + balance + ", productList="
				+ productList + "]";
	}

}
