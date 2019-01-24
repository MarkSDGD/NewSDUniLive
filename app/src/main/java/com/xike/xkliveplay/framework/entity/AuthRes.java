package com.xike.xkliveplay.framework.entity;
/**
 * 
 * @author LiWei <br>
 * CreateTime: 2014年10月30日 下午3:08:18<br>
 * <b>Info:</b><br>
 *<br>
 *   <b>Method:</b> <br>
 */
public class AuthRes {
	/** epg的域名，格式：http://xxx.yyy.zzz:port/ port默认为80 **/
	private String epgDomain = "";
	/** 备份epg的域名，格式：http://xxx.yyy.zzz:port/ port默认为80 **/
	private String epgDomainBackup = "";
	/** 升级服务器的域名，格式：http://xxx.yyy.zzz:port/ port默认为80 **/
	private String upgradeDomain = "";
	/** 备份升级服务器的域名，格式：http://xxx.yyy.zzz:port/ port默认为80 **/
	private String upgradeDomainBackup = "";
	/** 管理服务器的域名，格式：http://xxx.yyy.zzz:port/ port默认为80 **/
	private String managementDomain = "";
	/** 备份管理服务器的域名，格式：http://xxx.yyy.zzz:port/ port默认为80 **/
	private String managementDomainBackup = "";
	/** 时钟同步服务器地址，格式为IP地址或域名，如：192.168.13.14或ntp.sttri.com.cn **/
	private String ntpDomain = "";
	/** 备份时钟同步服务器地址，格式为IP地址或域名，如：192.168.13.14或ntp.sttri.com.cn **/
	private String ntpDomainBackup = "";
	/** DRM服务器地址，格式为：IP:port 如：192.168.13.14:14001 **/
	private String drmDomain = "";
	/** 备份DRM服务器地址，格式为：IP:port 如：192.168.13.14:14001 **/
	private String drmDomainBackup = "";
	/** 业务管理平台为该用户分配的临时身份证明 **/
	private String userToken = "";
	/** 用户对应的EPG分组信息标识 **/
	private String epgGroupNMB = "";
	/** 用户对应的用户分组信息标识 **/
	private String userGroupNMB = "";

	
	public AuthRes() {
	}

	public String getEpgDomain() {
		return epgDomain;
	}

	public void setEpgDomain(String epgDomain) {
		this.epgDomain = epgDomain;
	}

	public String getEpgDomainBackup() {
		return epgDomainBackup;
	}

	public void setEpgDomainBackup(String epgDomainBackup) {
		this.epgDomainBackup = epgDomainBackup;
	}

	public String getUpgradeDomain() {
		return upgradeDomain;
	}

	public void setUpgradeDomain(String upgradeDomain) {
		this.upgradeDomain = upgradeDomain;
	}

	public String getUpgradeDomainBackup() {
		return upgradeDomainBackup;
	}

	public void setUpgradeDomainBackup(String upgradeDomainBackup) {
		this.upgradeDomainBackup = upgradeDomainBackup;
	}

	public String getManagementDomain() {
		return managementDomain;
	}

	public void setManagementDomain(String managementDomain) {
		this.managementDomain = managementDomain;
	}

	public String getManagementDomainBackup() {
		return managementDomainBackup;
	}

	public void setManagementDomainBackup(String managementDomainBackup) {
		this.managementDomainBackup = managementDomainBackup;
	}

	public String getNtpDomain() {
		return ntpDomain;
	}

	public void setNtpDomain(String ntpDomain) {
		this.ntpDomain = ntpDomain;
	}

	public String getNtpDomainBackup() {
		return ntpDomainBackup;
	}

	public void setNtpDomainBackup(String ntpDomainBackup) {
		this.ntpDomainBackup = ntpDomainBackup;
	}

	public String getDrmDomain() {
		return drmDomain;
	}

	public void setDrmDomain(String drmDomain) {
		this.drmDomain = drmDomain;
	}

	public String getDrmDomainBackup() {
		return drmDomainBackup;
	}

	public void setDrmDomainBackup(String drmDomainBackup) {
		this.drmDomainBackup = drmDomainBackup;
	}

	public String getUserToken() {
		return userToken;
	}

	public void setUserToken(String userToken) {
		this.userToken = userToken;
	}

	public String getEpgGroupNMB() {
		return epgGroupNMB;
	}

	public void setEpgGroupNMB(String epgGroupNMB) {
		this.epgGroupNMB = epgGroupNMB;
	}

	public String getUserGroupNMB() {
		return userGroupNMB;
	}

	public void setUserGroupNMB(String userGroupNMB) {
		this.userGroupNMB = userGroupNMB;
	}

	public String toSting()
	{
		StringBuffer sBuffer = new StringBuffer();
		sBuffer.append("epgDomain=" + epgDomain
					+  " epgDomainBackup=" + epgDomainBackup
					+  " upgradeDomain=" + upgradeDomain
					+  " upgradeBackup=" + upgradeDomainBackup
					+  " managementDomain=" + managementDomain
					+  " managementDomainBackup=" + managementDomainBackup
					+  " ntpDomain=" + ntpDomain
					+  " ntpDomainBackup=" + ntpDomainBackup
					+  " drmDomain=" + drmDomain
					+  " drmDomainBackup=" + drmDomainBackup
					+  " userToken=" + userToken
					+  " epgGroupNMB=" + epgGroupNMB
					+  " userGroupNMB=" + userGroupNMB
				);
		return sBuffer.toString();
	}
	
}
