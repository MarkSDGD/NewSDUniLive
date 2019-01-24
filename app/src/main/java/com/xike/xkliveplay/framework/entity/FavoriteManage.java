/**
 * @author liwei
 * @d2014-2-25
 */
package com.xike.xkliveplay.framework.entity;

/**
 * @author lw �ղع���ӿ�����
 */
public class FavoriteManage {
	/** �����ղصĲ���ADD��DELETE **/
	private String action = "";
	/** ��ǰѡ��Ľ�Ŀ��ID **/
	private String contentId = "";
	/** �û�ҵ���� **/
	private String userId = "";
	/** ҵ�����ƽ̨Ϊ���û��������ʱ���֤�� **/
	private String userToken = "";
	/** �������ͣ� 1��VOD��Ŀ 2��Ƶ�� 3����Ŀ�� 4�������� **/
	private String contentType = "";
	/** ��ǰѡ��Ľ�Ŀ��������缯���� **/
	private String seriesContentIndex = "";
	/** ��ǰѡ��Ľ�Ŀ��������ID **/
	private String seriesContentID = "";
	/** ��ĿID **/
	private String categoryID = "";
	/** ��Ŀ�ṩ�̴��� **/
	private String contentProvider = "";

	public FavoriteManage() {
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getContentId() {
		return contentId;
	}

	public void setContentId(String contentId) {
		this.contentId = contentId;
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

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getSeriesContentIndex() {
		return seriesContentIndex;
	}

	public void setSeriesContentIndex(String seriesContentIndex) {
		this.seriesContentIndex = seriesContentIndex;
	}

	public String getSeriesContentID() {
		return seriesContentID;
	}

	public void setSeriesContentID(String seriesContentID) {
		this.seriesContentID = seriesContentID;
	}

	public String getCategoryID() {
		return categoryID;
	}

	public void setCategoryID(String categoryID) {
		this.categoryID = categoryID;
	}

	public String getContentProvider() {
		return contentProvider;
	}

	public void setContentProvider(String contentProvider) {
		this.contentProvider = contentProvider;
	}

}
