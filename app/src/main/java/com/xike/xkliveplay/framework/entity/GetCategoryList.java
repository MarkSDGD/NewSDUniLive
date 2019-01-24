/**
 * @author liwei
 * @d2014-2-25
 */
package com.xike.xkliveplay.framework.entity;

/**
 * @author lw
 * 
 */
public class GetCategoryList {
	/**��һ������Id��ֵ��Ϊ0ʱ�������ø���Ŀ**/
	private String parentCategoryId = "";
	/**�û�ҵ����**/
	private String userId = "";
	/**ҵ�����ƽ̨Ϊ�û��������ʱ���֤��**/
	private String userToken = "";

	public GetCategoryList() {
	}

	public String getParentCategoryId() {
		return parentCategoryId;
	}

	public void setParentCategoryId(String parentCategoryId) {
		this.parentCategoryId = parentCategoryId;
	}

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

}
