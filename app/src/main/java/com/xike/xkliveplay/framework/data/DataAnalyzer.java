/**
 * @Title: DataAnalyzer.java
 * @Package com.xike.xkliveplay.framework.data
 * @Description: TODO
 * Copyright: Copyright (c) 2015
 * Company:青岛博瑞立方网络科技有限公司
 * 
 * @author Mernake
 * @date 2016年6月27日 下午1:59:52
 * @version V1.0
 */
package com.xike.xkliveplay.framework.data;


import android.content.Context;
import android.os.SystemProperties;

import com.starv.sdsdk.DACQAPI;
import com.xike.xkliveplay.framework.entity.UploadObject;
import com.xike.xkliveplay.framework.tools.APKTools;
import com.xike.xkliveplay.framework.varparams.Var;

/**
 * @ClassName: DataAnalyzer
 * @Description: 新红桉数据分析工具
 * @author Mernake
 * @date 2016年6月27日 下午1:59:52
 *
 */
public class DataAnalyzer 
{
	private static final String TAG = "XinHongAn";
	private static final String APPKEY = "470fbdef-13ac-4fe3-a6b2-9d4844ddb982";
	
	
	private String USERID = "";
	private String STBMAC = "";
	private String OPERATORID = "";
	private String TERRACEID = "";
	private String CONTENTID = "";
	private String BRAND = "";
	private String MODE = "";
	private String IPADDRESS = "";
	private String APKVERSION = "";
	private String RESERVEGROUP = "";
	private String SSID = "";
	private String RESERVE2 = "";
	
	
	/**
	  * initParam(获取参数)
	  
	  * @return void    返回类型
	  * @throws  异常处理
	  * @modifyHistory  createBy Mernake
	  */
	private void initParam(Context context,String groupID) 
	{
		USERID = Var.userId;
		STBMAC = Var.mac;
		OPERATORID = SystemProperties.get("ro.build.operator");
		BRAND = SystemProperties.get("ro.build.hard");
		MODE = SystemProperties.get("ro.build.equipment");
		APKVERSION = APKTools.getStrVersion(context);
		SSID = APKTools.getWIFISSID(context);
//		TERRACEID = groupID;
		RESERVEGROUP = groupID;
	}
	
	
	
	
	@Override
	public String toString() {
		return "DataAnalyzer [USERID=" + USERID + ", STBMAC=" + STBMAC
				+ ", OPERATORID=" + OPERATORID + ", TERRACEID=" + TERRACEID
				+ ", CONTENTID=" + CONTENTID + ", BRAND=" + BRAND + ", MODE="
				+ MODE + ", IPADDRESS=" + IPADDRESS + ", APKVERSION="
				+ APKVERSION + ", RESERVEGROUP=" + RESERVEGROUP + ", RESERVE2=" + RESERVE2 + ", SSID="+SSID+"]";
	}




	/**采集开关标志位，true为关闭，false为打开**/
	public static final boolean SWITCHOFF = false;
	
	private static DataAnalyzer analyzer = null;
	
	public static DataAnalyzer creator()
	{
		if (analyzer == null) 
		{
			analyzer = new DataAnalyzer();
		}
		
		return analyzer;
	}
	
	private void showLog(String text,String method)
	{
		System.out.println(TAG + "::" + method + "::" + text);
	}
	
	/**
	 * 
	  * init(初始化方法)
	  * @return void    返回类型
	  * @throws  异常处理
	  * @modifyHistory  createBy Mernake
	 */
	public void init(Context mContext,String groupId)
	{
		showLog("switch:"+SWITCHOFF, "init");
		if (SWITCHOFF) return;
		initParam(mContext,groupId);
		showLog(toString(), "init");
		DACQAPI.getInstance().Init(APPKEY, STBMAC, USERID,OPERATORID,TERRACEID,CONTENTID,BRAND,MODE,IPADDRESS,APKVERSION,RESERVEGROUP,mContext,SSID,RESERVE2);
	}
	

	/**
	 * 
	  * uploadLivePlay(上传用户直播行为)
	  * @param obj  
	  * @return void    返回类型
	  * @throws  异常处理
	  * @modifyHistory  createBy Mernake
	 */
	public void uploadLivePlay(UploadObject obj)
	{
		if (SWITCHOFF) return;
		String str = DACQAPI.getInstance().LiveDataAcq(obj.getChannelId(), obj.getChannelName(), obj.getEnterChannelType()+"", obj.getChannelStatus()+"","","");
		showLog(str, "upLoadLivePlay");
		System.out.println(TAG + "  enterType=" + obj.getEnterChannelType() +"  channelStatus:"+ obj.getChannelStatus() + " channelName:"+obj.getChannelName());
	}
	
	/**
	 * 
	  * uploadResee(上传用户回看行为)
	  * @param obj  
	  * @return void    返回类型
	  * @throws  异常处理
	  * @modifyHistory  createBy Mernake
	 */
	public void uploadResee(UploadObject obj)
	{
		if (SWITCHOFF) return; 
		String str = DACQAPI.getInstance().ReplayDataAcq(obj.getChannelId(), obj.getChannelName(),obj.getAsssetId(),obj.getProgramName(), obj.getProgramDuration(),obj.getProgramOperateType(), obj.getPace(),"2",obj.getReseeStartTime(),"","");
		showLog(str, "uploadResee");
		System.out.println(TAG + "  programOperateType=" + obj.getProgramOperateType() + "  programStatus:"+ obj.getProgramStatus() + " channelid="+obj.getChannelId() + " channelName="+obj.getChannelName() + " assetid="+obj.getAsssetId()+" programName="+obj.getProgramName());
	}
	
	/**
	 * 
	  * uploadTimeShift(上传用户时移行为)
	  * @param obj  
	  * @return void    返回类型
	  * @throws  异常处理
	  * @modifyHistory  createBy Mernake
	 */
	public void uploadTimeShift(UploadObject obj)
	{
		if (SWITCHOFF) return; 
		String str = DACQAPI.getInstance().TimeShiftDataAcq(obj.getChannelId(), obj.getChannelName(), obj.getProgramStatus(),obj.getPace(), obj.getTimeshiftStartTime(),"","");
		showLog(str, "uploadTimeShift");
		System.out.println(TAG + "  enterType=" + obj.getEnterChannelType() +"  programStatus:"+ obj.getProgramStatus() + " channelName:"+obj.getChannelName() + " channelName:"+obj.getChannelName() + " pace:"+obj.getPace() + " time:"+obj.getTimeshiftStartTime());
	}




	/**
	  * clearParam(这里用一句话描述这个方法的作用)
	  
	  * @return void    返回类型
	  * @throws  异常处理
	  * @modifyHistory  createBy Mernake
	  */
	public void clearParam() 
	{
		STBMAC = "";
	}
}
