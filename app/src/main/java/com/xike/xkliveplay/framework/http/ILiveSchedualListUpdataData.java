package com.xike.xkliveplay.framework.http;

/**
 * 回看节目单专用的一个接口
 * #18034 创维E900-S在回看页面中操作日期与节目单，日期对应的节目单混乱
 */
public interface ILiveSchedualListUpdataData {

        public void updateData(String method, String uniId, String start, Object object, boolean isSuccess);

}
