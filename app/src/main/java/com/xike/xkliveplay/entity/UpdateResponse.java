package com.xike.xkliveplay.entity;

/**
 * Created by Jason Wei on 2017/7/6.
 */

public class UpdateResponse {
    private int resultCode;
    private int operators;
    private String hard;
    private String equipment;
    private int version;
    private int newVersion;
    private String url;
    private int isMust;
    private String name;
    private boolean need; // 是否需要更新
    private long fileSize; // 文件大小
    // 升级的状态 1需要升级,2正在下载,3下载完成,4正在升级,5成功升级,6升级失败,7不需要更新
    // 11正在检查,12检查失败
    // 50 sd卡不能读写,51 response解析失败
    private int status;
    private int progress; // 下载进度
    private long sdTotalSize; // sd卡总容量
    private long sdFreeSize; // sd卡剩余容量

    public UpdateResponse() {
    }

    public UpdateResponse(int resultCode, int operators, String hard, String equipment, int version, int newVersion, String url, int isMust, String name, boolean need, long fileSize, int status, int progress, long sdTotalSize, long sdFreeSize) {
        this.resultCode = resultCode;
        this.operators = operators;
        this.hard = hard;
        this.equipment = equipment;
        this.version = version;
        this.newVersion = newVersion;
        this.url = url;
        this.isMust = isMust;
        this.name = name;
        this.need = need;
        this.fileSize = fileSize;
        this.status = status;
        this.progress = progress;
        this.sdTotalSize = sdTotalSize;
        this.sdFreeSize = sdFreeSize;
    }

    @Override
    public String toString() {
        return "UpdateResponse{" +
                "resultCode=" + resultCode +
                ", operators=" + operators +
                ", hard='" + hard + '\'' +
                ", equipment='" + equipment + '\'' +
                ", version=" + version +
                ", newVersion=" + newVersion +
                ", url='" + url + '\'' +
                ", isMust=" + isMust +
                ", name='" + name + '\'' +
                ", need=" + need +
                ", fileSize=" + fileSize +
                ", status=" + status +
                ", progress=" + progress +
                ", sdTotalSize=" + sdTotalSize +
                ", sdFreeSize=" + sdFreeSize +
                '}';
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public int getOperators() {
        return operators;
    }

    public void setOperators(int operators) {
        this.operators = operators;
    }

    public String getHard() {
        return hard;
    }

    public void setHard(String hard) {
        this.hard = hard;
    }

    public String getEquipment() {
        return equipment;
    }

    public void setEquipment(String equipment) {
        this.equipment = equipment;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getNewVersion() {
        return newVersion;
    }

    public void setNewVersion(int newVersion) {
        this.newVersion = newVersion;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getIsMust() {
        return isMust;
    }

    public void setIsMust(int isMust) {
        this.isMust = isMust;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isNeed() {
        return need;
    }

    public void setNeed(boolean need) {
        this.need = need;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public long getSdTotalSize() {
        return sdTotalSize;
    }

    public void setSdTotalSize(long sdTotalSize) {
        this.sdTotalSize = sdTotalSize;
    }

    public long getSdFreeSize() {
        return sdFreeSize;
    }

    public void setSdFreeSize(long sdFreeSize) {
        this.sdFreeSize = sdFreeSize;
    }
}
