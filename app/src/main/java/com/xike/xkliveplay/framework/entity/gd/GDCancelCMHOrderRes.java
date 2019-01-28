package com.xike.xkliveplay.framework.entity.gd;

public class GDCancelCMHOrderRes extends GDAuthBase {

    private GDCancelCHMOrderBean data = new GDCancelCHMOrderBean();

    public GDCancelCHMOrderBean getData() {
        return data;
    }

    public void setData(GDCancelCHMOrderBean data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "GDCancelCMHOrderRes{" +
                "gdCancelCHMOrderBean=" + data +
                '}';
    }

    public class GDCancelCHMOrderBean {
        //返回码 直播取消成功:51000
        private String code;
        //描述信息
        private String description;
        //订单号
        private String orderID;
        //订购方式 话费:0 支付宝:56
        private String orderMode;

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

        public String getOrderID() {
            return orderID;
        }

        public void setOrderID(String orderID) {
            this.orderID = orderID;
        }

        public String getOrderMode() {
            return orderMode;
        }

        public void setOrderMode(String orderMode) {
            this.orderMode = orderMode;
        }

        @Override
        public String toString() {
            return "GDCancelCHMOrderBean{" +
                    "code='" + code + '\'' +
                    ", description='" + description + '\'' +
                    ", orderID='" + orderID + '\'' +
                    ", orderMode='" + orderMode + '\'' +
                    '}';
        }
    }
}
