package com.xike.xkliveplay.framework.entity.gd;

import java.util.List;

/**
 * Created by WangFangXu on 2018/7/31.
 */

public class CMHOrderListData {
    private String state;//数据获取状态
    private String code;//返回码
    private String msg;//返回信息
    private String description;//信息描述
    private String startTime;//开始时间
    private String endTime;//结束时间
    private List<CMHOrderListResult.CMHOrderListBean> data;//移动华为订购操作返回的数据

    public CMHOrderListData() {
    }

    public CMHOrderListData(String state, String code, String msg, String description, String startTime, String endTime, List<CMHOrderListResult.CMHOrderListBean> data) {
        this.state = state;
        this.code = code;
        this.msg = msg;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.data = data;
    }

    @Override
    public String toString() {
        return "CMHOrderListData{" +
                "state='" + state + '\'' +
                ", code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", description='" + description + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", data=" + data +
                '}';
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public List<CMHOrderListResult.CMHOrderListBean> getData() {
        return data;
    }

    public void setData(List<CMHOrderListResult.CMHOrderListBean> data) {
        this.data = data;
    }
}
