/**
 * @author liwei
 * @d2014-4-9
 */
package com.xike.xkliveplay.framework.entity;

/**
 * @author Administrator
 *
 */
public class LiveUpgrageRequest 
{
	/**֧����Ӫ��
	 *  1��	�ƶ�
	 *  2��	����
	 *  3��	��ͨ
	 *  4��   ����
	 * **/
	private String operators = "";
	/**Ӳ������	����Hisense**/
	private String hard = "";
	/**�豸�ͺ�	����IP508H(88T1)**/
	private String equipment = "";
	/**ֱ��apk�汾	����1.21**/
	private String version = "";
	/**MAC��ַ		����00:11:22:33:44:55**/
	private String mac = "";
	
	private String packageName = "";
	
	
	
	
	/**
	 * @return the packageName
	 */
	public String getPackageName() {
		return packageName;
	}

	/**
	 * @param packageName the packageName to set
	 */
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public LiveUpgrageRequest() 
	{
		
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

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	@Override
	public String toString() {
		return "LiveUpgrageRequest [operators=" + operators + ", hard=" + hard
				+ ", equipment=" + equipment + ", version=" + version
				+ ", mac=" + mac + "]";
	}
	
	
}
