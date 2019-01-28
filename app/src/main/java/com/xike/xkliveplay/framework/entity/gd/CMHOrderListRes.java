package com.xike.xkliveplay.framework.entity.gd;
import java.util.List;

/**
 * Created by WangFangXu on 2018/7/31.
 * 移动订购记录接口的返回bean
 */

public class CMHOrderListRes {
    private String code;//返回码
    private String description;//返回码描述
    private List<CMHOrderListBean> subInfoList;//订购列表

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<CMHOrderListBean> getSubInfoList() {
        return subInfoList;
    }

    public void setSubInfoList(List<CMHOrderListBean> subInfoList) {
        this.subInfoList = subInfoList;
    }

    @Override
    public String toString() {
        return "CMHOrderListResult{" +
                "code='" + code + '\'' +
                ", description='" + description + '\'' +
                ", subInfoList=" + subInfoList +
                '}';
    }

    public static class CMHOrderListBean {
        private String contentID;
        private String productID;
        private String productName;
        private String productType;
        private String startTime;
        private String endTime;
        private String buyMode;
        private String fee;

        public String getContentID() {
            return contentID;
        }

        public void setContentID(String contentID) {
            this.contentID = contentID;
        }

        public String getProductID() {
            return productID;
        }

        public void setProductID(String productID) {
            this.productID = productID;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public String getProductType() {
            return productType;
        }

        public void setProductType(String productType) {
            this.productType = productType;
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

        public String getBuyMode() {
            return buyMode;
        }

        public void setBuyMode(String buyMode) {
            this.buyMode = buyMode;
        }

        public String getFee() {
            return fee;
        }

        public void setFee(String fee) {
            this.fee = fee;
        }

        @Override
        public String toString() {
            return "CMHOrderListBean{" +
                    "contentID='" + contentID + '\'' +
                    ", productID='" + productID + '\'' +
                    ", productName='" + productName + '\'' +
                    ", productType='" + productType + '\'' +
                    ", startTime='" + startTime + '\'' +
                    ", endTime='" + endTime + '\'' +
                    ", buyMode='" + buyMode + '\'' +
                    ", fee='" + fee + '\'' +
                    '}';
        }
    }
}

