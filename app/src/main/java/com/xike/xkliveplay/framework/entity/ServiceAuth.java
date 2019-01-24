/**
 * @author 章伟
 * @date:2014-2-24 下午06:47:26
 * @version : V1.0
 *
 */

package com.xike.xkliveplay.framework.entity;

/**
 * @author zw
 *     业务鉴权请求接口
 *
 */
public class ServiceAuth {

	/** 业务类型ID  1: 表示订购   2: 表示退订**/
	private String contentId = "";
	/** 产品ID **/
	private String productId = "";	
	/** 用户业务编号 **/
	private String userId = "";
	/** 业务管理平台为用户分配的临时身份证明 **/	
	private String userToken = "";
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserToken() {
		return userToken;
	}
	public void setUserToken(String userToken) {
		this.userToken = userToken;
	}
	public String getContentID() {
		return contentId;
	}
	public void setContentID(String contentID) {
		this.contentId = contentID;
	}
	
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	
	
}
