/**
 * @author liwei
 * @d2014-2-25
 */
package com.xike.xkliveplay.framework.entity;

/**
 * @author lw
 * ��ȡ�ղ��б�
 */
public class GetFavoriteList {
	/** ȡ������ **/
	private String count = "";
	/** ��Ե�һ����ƫ���� **/
	private String offset = "";
	/** �ղ��������� 0�������ղ� 1��VOD��Ŀ 2��Ƶ�� 3����Ŀ�� 4�������� **/
	private String favType = "";
	/** �û�ҵ���� **/
	private String userId = "";
	/** ҵ�����ƽ̨Ϊ���û��������ʱ���֤�� **/
	private String userToken = "";

	public GetFavoriteList() {
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

	public String getFavType() {
		return favType;
	}

	public void setFavType(String favType) {
		this.favType = favType;
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
