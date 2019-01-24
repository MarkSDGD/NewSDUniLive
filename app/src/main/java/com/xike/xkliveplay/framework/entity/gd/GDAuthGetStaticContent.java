package com.xike.xkliveplay.framework.entity.gd;

import java.util.List;

/**
 * The programer: created by Mernake on 2018/8/24 10:21
 * Main Function:
 */
public class GDAuthGetStaticContent extends GDAuthBase
{
//    private GDAuthGetStaticContentData data = new GDAuthGetStaticContentData();

   private List<GDAuthGetStaticContentData> data;

    public List<GDAuthGetStaticContentData> getData() {
        return data;
    }

    public void setData(List<GDAuthGetStaticContentData> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    public class GDAuthGetStaticContentData{
        private String callSign;
        private String channelnumber;
        private String city;
        private String code;
        private String country;
        private String description;
        private String isvip;
        private String launguage;
        private String name;
        private String playurl;
        private String selected;
        private String state;
        private String timeshift;
        private String timeshiftduration;
        private String timeshifturl;
        private String vipfileurl;
        private String zipcode;

        public String getCallSign() {
            return callSign;
        }

        public void setCallSign(String callSign) {
            this.callSign = callSign;
        }

        public String getChannelnumber() {
            return channelnumber;
        }

        public void setChannelnumber(String channelnumber) {
            this.channelnumber = channelnumber;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getIsvip() {
            return isvip;
        }

        public void setIsvip(String isvip) {
            this.isvip = isvip;
        }

        public String getLaunguage() {
            return launguage;
        }

        public void setLaunguage(String launguage) {
            this.launguage = launguage;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPlayurl() {
            return playurl;
        }

        public void setPlayurl(String playurl) {
            this.playurl = playurl;
        }

        public String getSelected() {
            return selected;
        }

        public void setSelected(String selected) {
            this.selected = selected;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getTimeshift() {
            return timeshift;
        }

        public void setTimeshift(String timeshift) {
            this.timeshift = timeshift;
        }

        public String getTimeshiftduration() {
            return timeshiftduration;
        }

        public void setTimeshiftduration(String timeshiftduration) {
            this.timeshiftduration = timeshiftduration;
        }

        public String getTimeshifturl() {
            return timeshifturl;
        }

        public void setTimeshifturl(String timeshifturl) {
            this.timeshifturl = timeshifturl;
        }

        public String getVipfileurl() {
            return vipfileurl;
        }

        public void setVipfileurl(String vipfileurl) {
            this.vipfileurl = vipfileurl;
        }

        public String getZipcode() {
            return zipcode;
        }

        public void setZipcode(String zipcode) {
            this.zipcode = zipcode;
        }

        @Override
        public String toString() {
            return "GDAuthGetStaticContentData{" +
                    "callSign='" + callSign + '\'' +
                    ", channelnumber='" + channelnumber + '\'' +
                    ", city='" + city + '\'' +
                    ", code='" + code + '\'' +
                    ", country='" + country + '\'' +
                    ", description='" + description + '\'' +
                    ", isvip='" + isvip + '\'' +
                    ", launguage='" + launguage + '\'' +
                    ", name='" + name + '\'' +
                    ", playurl='" + playurl + '\'' +
                    ", selected='" + selected + '\'' +
                    ", state='" + state + '\'' +
                    ", timeshift='" + timeshift + '\'' +
                    ", timeshiftduration='" + timeshiftduration + '\'' +
                    ", timeshifturl='" + timeshifturl + '\'' +
                    ", vipfileurl='" + vipfileurl + '\'' +
                    ", zipcode='" + zipcode + '\'' +
                    '}';
        }
    }
}
