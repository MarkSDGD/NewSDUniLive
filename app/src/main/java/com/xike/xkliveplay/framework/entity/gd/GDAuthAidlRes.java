package com.xike.xkliveplay.framework.entity.gd;

/**
 * The programer: created by Mernake on 2018/8/17 10:05
 * Main Function:
 */
public class GDAuthAidlRes extends  GDAuthBase
{
    private GDAuthAIDLData data = new GDAuthAIDLData();

    public GDAuthAIDLData getData() {
        return data;
    }

    public void setData(GDAuthAIDLData data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return super.toString() + data.toString();
    }

    public class GDAuthAIDLData {
        private String UserTokenAIDL = "";
        private String IPTVAccountAIDL = "";
        private String EPGURLAIDL = "";
        private String operaTag = "";


        public String getOperaTag() {
            return operaTag;
        }

        public void setOperaTag(String operaTag) {
            this.operaTag = operaTag;
        }

        public String getUserTokenAIDL() {
            return UserTokenAIDL;
        }

        public void setUserTokenAIDL(String userTokenAIDL) {
            this.UserTokenAIDL = userTokenAIDL;
        }

        public String getIPTVAccountAIDL() {
            return IPTVAccountAIDL;
        }

        public void setIPTVAccountAIDL(String IPTVAccountAIDL) {
            this.IPTVAccountAIDL = IPTVAccountAIDL;
        }

        public String getEPGURLAIDL() {
            return EPGURLAIDL;
        }

        public void setEPGURLAIDL(String EPGURLAIDL) {
            this.EPGURLAIDL = EPGURLAIDL;
        }

        @Override
        public String toString() {
            return "GDAuthAIDLData{" +
                    "UserTokenAIDL='" + UserTokenAIDL + '\'' +
                    ", IPTVAccountAIDL='" + IPTVAccountAIDL + '\'' +
                    ", EPGURLAIDL='" + EPGURLAIDL + '\'' +
                    ", operaTag='" + operaTag + '\'' +
                    '}';
        }
    }
}
