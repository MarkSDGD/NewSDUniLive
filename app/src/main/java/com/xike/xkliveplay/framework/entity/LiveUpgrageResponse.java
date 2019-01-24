package com.xike.xkliveplay.framework.entity;

/**
 * 
 * @author LiWei <br>
 * CreateTime: 2014年10月30日 下午1:47:11<br>
 * <b>Info:</b><br>
 *<br>
 *   <b>Method:</b> <br>
 */
public class LiveUpgrageResponse 
{
	/**返回结果状态码
	 * 
	 * 9001	请求直播APK升级参数出错
	 * 9002	没有查询到直播APK相关的厂商品牌信息
	 * 9003  没有查询到直播APK相关的型号信息
	 * 9004	没有查询到直播APK相关的版本信息
	 * 9005  查询到直播APK相关的版本混乱,返回多条信息
	 * 9006  没有查询到品牌与型号的直播APK升级包相关信息
	 * 9007  直播APK服务器版本和机顶盒版本相同
	 * 0	成功返回信息
	 * -1   其它错误
	 * 
	 * **/
	private String resultCode = "";
	/**支撑运营商**/
	private String operators = "";
	/**硬件厂商	例：Hisense**/
	private String hard = "";
	/**设备型号	例：IP508H(88T1)**/
	private String equipment = "";
	/**直播apk版本	例：1.21**/
	private String version = "";
	/**直播apk新版本	例：1.22**/
	private String newVersion = "";
	/**url下载地址**/
	private String url = "";
	/**是否强制升级 
	 * 1---是
	 * 2---否
	 * **/
	private String isMust = "";
	
	
	public String getIsMust() {
		return isMust;
	}


	public void setIsMust(String isMust) {
		this.isMust = isMust;
	}


	public LiveUpgrageResponse() 
	{
	}


	public String getResultCode() {
		return resultCode;
	}


	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}


	public String getOperators() {
		return operators;
	}


	public void setOperators(String operators) {
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


	public String getVersion() {
		return version;
	}


	public void setVersion(String version) {
		this.version = version;
	}


	public String getNewVersion() {
		return newVersion;
	}


	public void setNewVersion(String newVersion) {
		this.newVersion = newVersion;
	}


	public String getUrl() {
		return url;
	}


	public void setUrl(String url) {
		this.url = url;
	}


	@Override
	public String toString() {
		return "GetUpdateRes [resultCode=" + resultCode + ", operators="
				+ operators + ", hard=" + hard + ", equipment=" + equipment
				+ ", version=" + version + ", newVersion=" + newVersion
				+ ", url=" + url + ", isMust=" + isMust + "]";
	}
	
	
	
	
	
}
