/**
 * @author liwei
 * @d2014-2-25
 */
package com.xike.xkliveplay.framework.entity;

/**
 * @author lw
 * ��ȡ��Ŀ���������б�
 */
public class GetContentList {
	/**��Ŀ����Id**/
	private String categoryId = "";
	/**ȡ������**/
	private String count = "";
	/**��Ե�һ����ƫ����**/
	private String offset = "";
	/**��ʼ����������Ǵ��ڵ���1������������Ϊ��1**/
	private String depthStart = "";
	/**��ֹ�����������������������Ϊ��1�������С��depthStart��������������Ϊ�ǵ���depthStart**/
	private String depthEnd = "";
	/**�û�ҵ����**/
	private String userId = "";
	/**ҵ�����ƽ̨Ϊ���û��������ʱ���֤��**/
	private String userToken = "";

	public GetContentList() {
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public String getOffset() {
		return offset;
	}

	public void setOffset(String offset) {
		this.offset = offset;
	}

	public String getDepthStart() {
		return depthStart;
	}

	public void setDepthStart(String depthStart) {
		this.depthStart = depthStart;
	}

	public String getDepthEnd() {
		return depthEnd;
	}

	public void setDepthEnd(String depthEnd) {
		this.depthEnd = depthEnd;
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
