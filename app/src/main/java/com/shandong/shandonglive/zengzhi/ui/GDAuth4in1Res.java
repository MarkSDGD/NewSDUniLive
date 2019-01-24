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
package com.shandong.shandonglive.zengzhi.ui;

/**
  * @ClassName: GDAuth4in1Res
  * @Description: TODO
  * @author Mernake
  * @date 2018年7月18日 上午9:45:20
  *
  */
public class GDAuth4in1Res 
{
	private String code = "";
	private GDData data = new GDData();
	private String description = "";
	private String endTime = "";
	private String msg = "";
	private String startTime = "";
	private String state = "";
	
	
	
	
	
	
	
	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}







	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}







	/**
	 * @return the data
	 */
	public GDData getData() {
		return data;
	}







	/**
	 * @param data the data to set
	 */
	public void setData(GDData data) {
		this.data = data;
	}







	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}







	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}







	/**
	 * @return the endTime
	 */
	public String getEndTime() {
		return endTime;
	}







	/**
	 * @param endTime the endTime to set
	 */
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}







	/**
	 * @return the msg
	 */
	public String getMsg() {
		return msg;
	}







	/**
	 * @param msg the msg to set
	 */
	public void setMsg(String msg) {
		this.msg = msg;
	}







	/**
	 * @return the startTime
	 */
	public String getStartTime() {
		return startTime;
	}







	/**
	 * @param startTime the startTime to set
	 */
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}







	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}







	/**
	 * @param state the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}







	public class GDData {
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
		 * @param drmDomain the drmDomain to set
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
		 * @param drmDomainBackup the drmDomainBackup to set
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
		 * @param epgDomain the epgDomain to set
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
		 * @param epgDomainBackup the epgDomainBackup to set
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
		 * @param epgGroupNMB the epgGroupNMB to set
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
		 * @param managementDomain the managementDomain to set
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
		 * @param managementDomainBackup the managementDomainBackup to set
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
		 * @param ntpDomain the ntpDomain to set
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
		 * @param ntpDomainBackup the ntpDomainBackup to set
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
		 * @param upgradeDomain the upgradeDomain to set
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
		 * @param upgradeDomainBackup the upgradeDomainBackup to set
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
		 * @param userGroupNMB the userGroupNMB to set
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
		 * @param userToken the userToken to set
		 */
		public void setUserToken(String userToken) {
			this.userToken = userToken;
		}
		/*
		  * <p>Title: toString</p>
		  * <p>Description: </p>
		  * @return
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
		return "GDAuth4in1Res [code=" + code + ", description=" + description + ", endTime=" + endTime + ", msg=" + msg
				+ ", startTime=" + startTime + ", state=" + state + "]";
	}
	
	
}
