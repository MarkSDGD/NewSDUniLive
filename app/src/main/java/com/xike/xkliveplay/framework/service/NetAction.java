/**
 * @Title: NetAction.java
 * @Package com.xike.xkliveplay.framework.service
 * @Description: TODO
 * Copyright: Copyright (c) 2015
 * Company:青岛博瑞立方网络科技有限公司
 * 
 * @author Mernake
 * @date 2016年5月30日 上午10:56:10
 * @version V1.0
 */
package com.xike.xkliveplay.framework.service;

import android.content.Context;
import android.os.SystemProperties;

import com.xike.xkliveplay.framework.entity.LiveUpgrageRequest;
import com.xike.xkliveplay.framework.http.IUpdateData;
import com.xike.xkliveplay.framework.httpclient.HttpGetUpdate;
import com.xike.xkliveplay.framework.tools.GetStbinfo;
import com.xike.xkliveplay.framework.tools.LogUtil;

/**
 * @ClassName: NetAction
 * @Description: TODO
 * @author Mernake
 * @date 2016年5月30日 上午10:56:10
 *
 */
public class NetAction 
{
	private static String tag = "NetAction";
	
	/**
	 * 
	  * requestUpdate(请求升级)
	  * @param iUpdateData
	  * @param context  
	  * @return void    返回类型
	  * @throws  异常处理
	  * @modifyHistory  createBy Mernake
	 */
	public static void requestUpdate(IUpdateData iUpdateData,Context context)
	{
		LiveUpgrageRequest request = getUpdateVaram(context);
		if (request == null) 
		{
			LogUtil.i(tag, "updateIfNeedUpdate", "liveplay update error!");
			return;
		}
		HttpGetUpdate httpGetUpdate = new HttpGetUpdate(request, iUpdateData);
		httpGetUpdate.post();
	}
	
	/**
	 * 
	  * getUpdateVaram(获取升级参数)
	  * @param context
	  * @return  
	  * @return LiveUpgrageRequest    返回类型
	  * @throws  异常处理
	  * @modifyHistory  createBy Mernake
	 */
	private static LiveUpgrageRequest getUpdateVaram(Context context)
	{
		LiveUpgrageRequest getUpdateReq = new LiveUpgrageRequest();
		String operator = SystemProperties.get("ro.build.operator");
		String hard = SystemProperties.get("ro.build.hard");
		String equipment = SystemProperties.get("ro.build.equipment");
		LogUtil.e(tag, "getUpdateVaram:","operator= " + operator + " hard= " + hard + " equipment= " + equipment);
		if (operator.equals("") || hard.equals("") || equipment.equals("")) 
		{
			return null;
		}
		getUpdateReq.setMac(GetStbinfo.getLocalMacAddress().trim());
		getUpdateReq.setEquipment(equipment);
		getUpdateReq.setHard(hard);
		getUpdateReq.setOperators(operator);

		getUpdateReq.setVersion(GetStbinfo.getStrVersion(context));
		return getUpdateReq;
	}
	
}
