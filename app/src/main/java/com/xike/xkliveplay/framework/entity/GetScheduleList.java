/**
 * @author liwei
 * @d2014-2-25
 */
package com.xike.xkliveplay.framework.entity;

/**
 * @author LW ��ȡ��Ŀ���б�����
 * 
 */
public class GetScheduleList {
	/** ȡ������ **/
	private String count = "";
	/** ��Ե�һ����ƫ���� **/
	private String offset = "";
	/** �����Ƶ���б����Ϊ"",��ʾ���ȫ��������ж��Ƶ��������","�ָ� **/
	private String channelIds = "";
	/** ��ʼʱ���������ʽΪYYYYMMDDHH24MISS **/
	private String startDateTime = "";
	/** ����ʱ���������ʽΪYYYYMMDDHH24MISS **/
	private String endDateTime = "";
	/** �û�ҵ���� **/
	private String userId = "";
	/** ҵ�����ƽ̨Ϊ���û��������ʱ���֤�� **/
	private String userToken = "";

	public GetScheduleList() {
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

	public String getChannelIds() {
		return channelIds;
	}

	public void setChannelIds(String channelIds) {
		this.channelIds = channelIds;
	}

	public String getStartDateTime() {
		return startDateTime;
	}

	public void setStartDateTime(String startDateTime) {
		this.startDateTime = startDateTime;
	}

	public String getEndDateTime() {
		return endDateTime;
	}

	public void setEndDateTime(String endDateTime) {
		this.endDateTime = endDateTime;
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

	@Override
	public String toString() {
		return "GetScheduleListReq [count=" + count + ", offset=" + offset
				+ ", channelIds=" + channelIds + ", startDateTime="
				+ startDateTime + ", endDateTime=" + endDateTime + ", userId="
				+ userId + ", userToken=" + userToken + "]";
	}

	

}
