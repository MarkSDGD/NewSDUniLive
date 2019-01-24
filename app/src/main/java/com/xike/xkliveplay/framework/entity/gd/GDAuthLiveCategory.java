package com.xike.xkliveplay.framework.entity.gd;

import java.util.ArrayList;
import java.util.List;

/**
 * The programer: created by Mernake on 2018/8/23 10:22
 * Main Function:
 */
public class GDAuthLiveCategory extends GDAuthBase
{
    private List<GDAuthLiveCategoryData> data = new ArrayList<GDAuthLiveCategoryData>();

    public List<GDAuthLiveCategoryData> getData() {
        return data;
    }

    public void setData(List<GDAuthLiveCategoryData> data) {
        this.data = data;
    }



    public class GDAuthLiveCategoryData{
        String postion;
        String sequence;
        String description;
        String name;
        String fileurl;
        String pfile;
        String pname;
        String childs;
        String displaytype;
        String parentid;
        String primaryid;

        public String getPostion() {
            return postion;
        }

        public void setPostion(String postion) {
            this.postion = postion;
        }

        public String getSequence() {
            return sequence;
        }

        public void setSequence(String sequence) {
            this.sequence = sequence;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getFileurl() {
            return fileurl;
        }

        public void setFileurl(String fileurl) {
            this.fileurl = fileurl;
        }

        public String getPfile() {
            return pfile;
        }

        public void setPfile(String pfile) {
            this.pfile = pfile;
        }

        public String getPname() {
            return pname;
        }

        public void setPname(String pname) {
            this.pname = pname;
        }

        public String getChilds() {
            return childs;
        }

        public void setChilds(String childs) {
            this.childs = childs;
        }

        public String getDisplaytype() {
            return displaytype;
        }

        public void setDisplaytype(String displaytype) {
            this.displaytype = displaytype;
        }

        public String getParentid() {
            return parentid;
        }

        public void setParentid(String parentid) {
            this.parentid = parentid;
        }

        public String getPrimaryid() {
            return primaryid;
        }

        public void setPrimaryid(String primaryid) {
            this.primaryid = primaryid;
        }

        @Override
        public String toString() {
            return "GDAuthLiveCategoryData{" +
                    "postion='" + postion + '\'' +
                    ", sequence='" + sequence + '\'' +
                    ", description='" + description + '\'' +
                    ", name='" + name + '\'' +
                    ", fileurl='" + fileurl + '\'' +
                    ", pfile='" + pfile + '\'' +
                    ", pname='" + pname + '\'' +
                    ", childs='" + childs + '\'' +
                    ", displaytype='" + displaytype + '\'' +
                    ", parentid='" + parentid + '\'' +
                    ", primaryid='" + primaryid + '\'' +
                    '}';
        }
    }
}
