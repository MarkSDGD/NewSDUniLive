/**
 * @Title: GDAuth4in1Res.java
 * @Package com.xike.xkliveplay.framework.entity
 * @Description: TODO
 * Copyright: Copyright (c) 2015
 * Company:青岛博瑞立方网络科技有限公司
 * 
 * @author Mernake
 * @date 2018年7月18日 上午9:45:20
 * @version V1.0
 */
package com.xike.xkliveplay.framework.entity.gd;

/**
 * @ClassName: GDAuth4in1Res
 * @Description: TODO
 * @author Mernake
 * @date 2018年7月18日 上午9:45:20
 *
 */
public class GDAuth4in1Res extends GDAuthBase {
	private GDAuth4in1Data data = new GDAuth4in1Data();


	/**
	 * @return the data
	 */
	public GDAuth4in1Data getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(GDAuth4in1Data data) {
		this.data = data;
	}

	public class GDAuth4in1Data {
		private String drmDomain = "";
		private String drmDomainBackup = "";
		private String epgDomain = "";
		private String epgDomainBackup = "";
		private String epgGroupNMB = "";
		private String managementDomain = "";
		private String managementDomainBackup = "";
		private String ntpDomain = "";
		private String ntpDomainBackup = "";
		private String upgradeDomain = "";
		private String upgradeDomainBackup = "";
		private String userGroupNMB = "";
		private String userToken = "";

		/**
		 * @return the drmDomain
		 */
		public String getDrmDomain() {
			return drmDomain;
		}

		/**
		 * @param drmDomain
		 *            the drmDomain to set
		 */
		public void setDrmDomain(String drmDomain) {
			this.drmDomain = drmDomain;
		}

		/**
		 * @return the drmDomainBackup
		 */
		public String getDrmDomainBackup() {
			return drmDomainBackup;
		}

		/**
		 * @param drmDomainBackup
		 *            the drmDomainBackup to set
		 */
		public void setDrmDomainBackup(String drmDomainBackup) {
			this.drmDomainBackup = drmDomainBackup;
		}

		/**
		 * @return the epgDomain
		 */
		public String getEpgDomain() {
			return epgDomain;
		}

		/**
		 * @param epgDomain
		 *            the epgDomain to set
		 */
		public void setEpgDomain(String epgDomain) {
			this.epgDomain = epgDomain;
		}

		/**
		 * @return the epgDomainBackup
		 */
		public String getEpgDomainBackup() {
			return epgDomainBackup;
		}

		/**
		 * @param epgDomainBackup
		 *            the epgDomainBackup to set
		 */
		public void setEpgDomainBackup(String epgDomainBackup) {
			this.epgDomainBackup = epgDomainBackup;
		}

		/**
		 * @return the epgGroupNMB
		 */
		public String getEpgGroupNMB() {
			return epgGroupNMB;
		}

		/**
		 * @param epgGroupNMB
		 *            the epgGroupNMB to set
		 */
		public void setEpgGroupNMB(String epgGroupNMB) {
			this.epgGroupNMB = epgGroupNMB;
		}

		/**
		 * @return the managementDomain
		 */
		public String getManagementDomain() {
			return managementDomain;
		}

		/**
		 * @param managementDomain
		 *            the managementDomain to set
		 */
		public void setManagementDomain(String managementDomain) {
			this.managementDomain = managementDomain;
		}

		/**
		 * @return the managementDomainBackup
		 */
		public String getManagementDomainBackup() {
			return managementDomainBackup;
		}

		/**
		 * @param managementDomainBackup
		 *            the managementDomainBackup to set
		 */
		public void setManagementDomainBackup(String managementDomainBackup) {
			this.managementDomainBackup = managementDomainBackup;
		}

		/**
		 * @return the ntpDomain
		 */
		public String getNtpDomain() {
			return ntpDomain;
		}

		/**
		 * @param ntpDomain
		 *            the ntpDomain to set
		 */
		public void setNtpDomain(String ntpDomain) {
			this.ntpDomain = ntpDomain;
		}

		/**
		 * @return the ntpDomainBackup
		 */
		public String getNtpDomainBackup() {
			return ntpDomainBackup;
		}

		/**
		 * @param ntpDomainBackup
		 *            the ntpDomainBackup to set
		 */
		public void setNtpDomainBackup(String ntpDomainBackup) {
			this.ntpDomainBackup = ntpDomainBackup;
		}

		/**
		 * @return the upgradeDomain
		 */
		public String getUpgradeDomain() {
			return upgradeDomain;
		}

		/**
		 * @param upgradeDomain
		 *            the upgradeDomain to set
		 */
		public void setUpgradeDomain(String upgradeDomain) {
			this.upgradeDomain = upgradeDomain;
		}

		/**
		 * @return the upgradeDomainBackup
		 */
		public String getUpgradeDomainBackup() {
			return upgradeDomainBackup;
		}

		/**
		 * @param upgradeDomainBackup
		 *            the upgradeDomainBackup to set
		 */
		public void setUpgradeDomainBackup(String upgradeDomainBackup) {
			this.upgradeDomainBackup = upgradeDomainBackup;
		}

		/**
		 * @return the userGroupNMB
		 */
		public String getUserGroupNMB() {
			return userGroupNMB;
		}

		/**
		 * @param userGroupNMB
		 *            the userGroupNMB to set
		 */
		public void setUserGroupNMB(String userGroupNMB) {
			this.userGroupNMB = userGroupNMB;
		}

		/**
		 * @return the userToken
		 */
		public String getUserToken() {
			return userToken;
		}

		/**
		 * @param userToken
		 *            the userToken to set
		 */
		public void setUserToken(String userToken) {
			this.userToken = userToken;
		}

		/*
		 * <p>Title: toString</p> <p>Description: </p>
		 * 
		 * @return
		 * 
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "GDData [drmDomain=" + drmDomain + ", drmDomainBackup=" + drmDomainBackup + ", epgDomain="
					+ epgDomain + ", epgDomainBackup=" + epgDomainBackup + ", epgGroupNMB=" + epgGroupNMB
					+ ", managementDomain=" + managementDomain + ", managementDomainBackup=" + managementDomainBackup
					+ ", ntpDomain=" + ntpDomain + ", ntpDomainBackup=" + ntpDomainBackup + ", upgradeDomain="
					+ upgradeDomain + ", upgradeDomainBackup=" + upgradeDomainBackup + ", userGroupNMB=" + userGroupNMB
					+ ", userToken=" + userToken + "]";
		}

	}

	/*
	  * <p>Title: toString</p>
	  * <p>Description: </p>
	  * @return
	  * @see java.lang.Object#toString()
	  */
	@Override
	public String toString() {
		return super.toString();
	}

	
	
}
