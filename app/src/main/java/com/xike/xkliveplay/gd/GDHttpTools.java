/**
 * @Title: GDHttpTools.java
 * @Package com.xike.xkliveplay.gd
 * @Description: TODO
 * Copyright: Copyright (c) 2015
 * Company:青岛博瑞立方网络科技有限公司
 * 
 * @author Mernake
 * @date 2018年7月18日 上午9:25:23
 * @version V1.0
 */
package com.xike.xkliveplay.gd;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.google.gson.Gson;
import com.shandong.sdk.shandongsdk.bean.CMHwAuthReturn;
import com.shandong.sdk.shandongsdk.bean.LiveRecommendData;
import com.shandong.sdk.shandongsdk.way.SDKMethods;
import com.shandong.shandonglive.zengzhi.ui.ZZFileTools;
import com.xike.xkliveplay.framework.entity.Category;
import com.xike.xkliveplay.framework.entity.ContentChannel;
import com.xike.xkliveplay.framework.entity.GetContentList;
import com.xike.xkliveplay.framework.entity.GetContentListRes;
import com.xike.xkliveplay.framework.entity.GetScheduleList;
import com.xike.xkliveplay.framework.entity.Schedule;
import com.xike.xkliveplay.framework.entity.gd.CMHOrderListData;
import com.xike.xkliveplay.framework.entity.gd.GDAuth4in1Res;
import com.xike.xkliveplay.framework.entity.gd.GDAuthActivateTerminalRes;
import com.xike.xkliveplay.framework.entity.gd.GDAuthAidlRes;
import com.xike.xkliveplay.framework.entity.gd.GDAuthAuthRes;
import com.xike.xkliveplay.framework.entity.gd.GDAuthAuthenticationRes;
import com.xike.xkliveplay.framework.entity.gd.GDAuthGetAccountRes;
import com.xike.xkliveplay.framework.entity.gd.GDAuthGetStaticContent;
import com.xike.xkliveplay.framework.entity.gd.GDAuthLiveCategory;
import com.xike.xkliveplay.framework.entity.gd.GDCMSMSCode;
import com.xike.xkliveplay.framework.entity.gd.GDCancelCMHOrderRes;
import com.xike.xkliveplay.framework.entity.gd.GDOrderGetProductListInfoRes;
import com.xike.xkliveplay.framework.entity.gd.GDOrderPlayAuthCMHWRes;
import com.xike.xkliveplay.framework.entity.gd.GDOrderbestoPlayAuthRes;
import com.xike.xkliveplay.framework.entity.gd.GDOrdercmhCheckOrderRes;
import com.xike.xkliveplay.framework.entity.gd.GDOrdercmhOrderRes;
import com.xike.xkliveplay.framework.entity.gd.GDOrdercmhOrderSyncRes;
import com.xike.xkliveplay.framework.http.IUpdateData;
import com.xike.xkliveplay.framework.httpclient.DataModel;
import com.xike.xkliveplay.framework.httpclient.HttpAll6Schedules;
import com.xike.xkliveplay.framework.httpclient.HttpGetCategoryList;
import com.xike.xkliveplay.framework.httpclient.HttpGetContentList;
import com.xike.xkliveplay.framework.httpclient.HttpGetScheduleList;
import com.xike.xkliveplay.framework.varparams.Var;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
  * @ClassName: GDHttpTools
  * @Description:  广电I6请求接口
  * @author Mernake
  * @date 2018年7月18日 上午9:25:23
  *
  */
public class GDHttpTools
{

	private static final String logtag = "GDHttpTools";
	private static GDHttpTools tools = null;
	private boolean isGDOrderEnable = false;
	private boolean isGDI6Enable = false;

	/**平台号，0--CTHW,1--CMHW,2--CMZTE**/
	private String tag = "";
	/**移动的一个什么epgurl**/
	private String epgurlAIDL = "";
	/**移动的一个什么usertoken**/
	private String usertokenAIDL = "";

	private String userId = "";

	/**已经鉴权的频道列表 key--channelid，value--lastauthtime**/
	private Map<String,String> authedList = new HashMap<String, String>();
	private Map<String,GDOrderPlayAuthCMHWRes> playAuthList = new HashMap<String, GDOrderPlayAuthCMHWRes>();

	private boolean isNeedReplay = false;


	GDOrderGetProductListInfoRes gdOrderGetProductListInfoRes;
	public static final String METHOD_GETRECOMMENDDATA = "METHOD_GETRECOMMENDDATA";//获取首页数据

	public static final String METHOD_I6_4IN1 = "METHOD_I6_4IN1";
	public static final String METHOD_I6_GETACCOUNT = "METHOD_I6_GETACCOUNT";
	public static final String METHOD_I6_ACTIVATETERMINAL = "METHOD_I6_ACTIVATETERMINAL";
	public static final String METHOD_I6_AUTHENTICATION = "METHOD_I6_AUTHENTICATION";
	public static final String METHOD_I6_AUTH = "METHOD_I6_AUTH";
	public static final String METHOD_ORDER_PLAYAUTH = "METHOD_ORDER_PLAYAUTH";
	public static final String METHOD_ORDER_BESTOPLAYAUTH = "METHOD_ORDER_BESTOPLAYAUTH";
	public static final String METHOD_ORDER_CMHWPLAYAUTH = "METHOD_ORDER_CMHWPLAYAUTH";
	public static final String METHOD_ORDER_CMHBESTOPROLIST = "METHOD_ORDER_CMHBESTOPROLIST";
	public static final String METHOD_ORDER_GETPRODUCTLISTINFO = "METHOD_ORDER_GETPRODUCTLISTINFO";
	public static final String METHOD_ORDER_CMHCHECKORDER = "METHOD_ORDER_CMHCHECKORDER";
	public static final String METHOD_ORDER_CMHORDER = "METHOD_ORDER_CMHORDER";
	public static final String METHOD_ORDER_CMHORDERSYNC = "METHOD_ORDER_CMHORDERSYNC";
	public static final String METHOD_GETLIVECATEGORYLIST = "METHOD_GETLIVECATEGORYLIST";
	public static final String METHOD_GETLIVECONTENTLIST = "METHOD_GETLIVECONTENTLIST";
	public static final String METHOD_GETLIVESCHEDULEALLLIST = "METHOD_GETLIVESCHEDULEALLLIST";
	public static final String METHOD_GETLIVESCHEDULELIST = "METHOD_GETLIVESCHEDULELIST";
	public static final String METHOD_GETAIDLDATA = "METHOD_GETAIDLDATA";
	public static final String METHOD_ORDER_QUERYORDERLIST = "METHOD_ORDER_QUERYORDERLIST";//查询所有的订购记录

	public static final String METHOD_ORDER_CANCELORDER = "METHOD_ORDER_CANCELORDER";//取消订单
	public static final String METHOD_ORDER_CMSENDSMS = "METHOD_ORDER_CMSENDSMS";
	public static final String GD_CMHORDERRES = "qrorder";//用于在订购后，华为返回成功信息之后，保存下这个订单信息来，这个是五分钟内不刷新二维码用的，所以同时这里也要保存一个时间，用来记录这个5min的开始时间
	public static final String GD_CMHORDERSUCCES_STARTTIME = "qrordertime";//用来记录这个5min的开始时间
	public static final String GD_SMSCODE = "smscode";
	public static GDHttpTools getInstance(){
		if (tools == null)
		{
			tools = new GDHttpTools();
		}
		return tools;
	}

	public String getTag() {
		return tag;
	}

	public String getEpgurlAIDL() {
		return epgurlAIDL;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setAIDLParam(String epgAIDLURL, String usertokenAIDL, String userId)
	{
		showLog("epgurlAIDL:"+epgurlAIDL + " usertokenAIDL:"+usertokenAIDL + " userid:"+userId);
		this.epgurlAIDL = epgAIDLURL;
		this.usertokenAIDL = usertokenAIDL;
		this.userId = userId;
	}

	public boolean isNeedReplay() {
		return isNeedReplay;
	}

	public void setNeedReplay(boolean needReplay) {
		isNeedReplay = needReplay;
	}

	public String getUsertokenAIDL() {
		return usertokenAIDL;
	}


	/**
	 * 设置平台 0--CTHW,1-CMHW,2-CMZTE
	 * @param tag
	 */
	public void setTag(String tag) {
		this.tag = tag;
	}

	public boolean isGDOrderEnable() {
		return isGDOrderEnable;
	}

	public void setLiveUrl(String liveurl)
	{
		showLog("设置SDK的访问地址为："+liveurl);
	    if (liveurl!=null&&liveurl.length()>0) SDKMethods.setLiveUrl(liveurl);
    }

	public void setGDOrderEnable(boolean isGDOrderEnable) {
		showLog("设置订购开关是否打开："+isGDOrderEnable);
		this.isGDOrderEnable = isGDOrderEnable;
	}

	public boolean isGDI6Enable() {
		return isGDI6Enable;
	}

	public void setGDI6Enable(boolean isGDI6Enable)
	{
		showLog("设置I6接口开关是否打开："+isGDI6Enable);
		this.isGDI6Enable = isGDI6Enable;
	}

	/**
	 *
	  * initSDK(初始化sdk)
	  * @param context
	  * @return void    返回类型
	  * @modifyHistory  createBy Mernake
	 */
	public void initSDK(Context context)
	{
		if (isGDI6Enable||isGDOrderEnable)
		{
			showLog("初始化广电SDK");
			SDKMethods.init(context,"0");
//			setPlatformTag(context);
		}
	}

	private void setPlatformTag(Context context)
	{
//		setTag("0");
		ContentResolver resolver = context.getContentResolver();
		Cursor cursor = resolver.query(Uri.parse("content://com.huawei.sd.yd.launcher.config/configs"),null,null,null,null);
		String platformtypeStr = "";
		if (cursor != null && cursor.getCount() > 0)
		{
			cursor.moveToFirst();
			platformtypeStr = cursor.getString(cursor.getColumnIndex("platformtypeStr"));
			setTag(platformtypeStr);
			cursor.close();
		}
	}


	/**
	 *  Get CM/CT AIDL data(EPGURL/EPGTOKEN/IPTVACCOUNT)
	 * @param context context
	 * @param tag 0--cthw,1--cmhw,2--cmzte
	 * @param iUpdateData callback interface
	 * @return gdauthaidlres
	 */
	public GDAuthAidlRes getAIDLData(Context context, String tag, final IUpdateData iUpdateData)
	{
		if (isGDI6Enable){
			showLog("广电getAIDLData接口：tag："+tag);
			SDKMethods.getAIDLMessage(context, tag, new SDKMethods.RequestCallback()
			{
				@Override
				public void onGetData(String str)
				{
					showLog("广电getAIDLData接口："+str);
					if (str == null || str.length() == 0) iUpdateData.updateData(METHOD_GETAIDLDATA,null,null,false);
					GDAuthAidlRes res = new Gson().fromJson(str,GDAuthAidlRes.class);
					if (res.getCode().contains("51000")){
						//success
						iUpdateData.updateData(METHOD_GETAIDLDATA,"",res,true);
					}else{
						iUpdateData.updateData(METHOD_GETAIDLDATA,"",res,false);
					}
				}
			});

		}
		return null;
	}

	/**
	 *
	  * apkAuth(反查激活鉴权4合一接口)
	  * @param aidlAccount
	  * @param localMac
	  * @param type 0--live,1--vod
	  * @param tag  0-CTHW,1--CMHW,2--CMZTE
	  * @param iUpdateData
	  * @return void    返回类型
	  * @modifyHistory  createBy Mernake
	 */
	public void apkAuth(String aidlAccount,String localMac,String type,String tag,final IUpdateData iUpdateData)
	{
		if (isGDI6Enable)
		{
		    showLog("广电apkAuth接口：请求");
			showLog("广电apkAuth接口：aidlAccount："+aidlAccount);
			showLog("广电apkAuth接口：localMac："+localMac);
			showLog("广电apkAuth接口：type："+type);
			showLog("广电apkAuth接口：tag："+tag);
			SDKMethods.apkAuth(aidlAccount, localMac, type, tag, new SDKMethods.RequestCallback()
			{
				@Override
				public void onGetData(String str)
				{
					showLog("广电apkAuth接口返回："+str);
					if (str!=null&&str.length()>0){
						GDAuth4in1Res res = new Gson().fromJson(str, GDAuth4in1Res.class);
						showLog("广电apkAuth接口：data："+res.getData().toString());
						showLog("广电apkAuth接口：res："+res.toString());
						if (res.getCode().contains("51000"))
						{
							iUpdateData.updateData(METHOD_I6_4IN1, "", res, true);
						}else{
							iUpdateData.updateData(METHOD_I6_4IN1, "", res, false);
						}
					}
				}
			});
		}
	}

	/**
	 *
	 * apkAuth2(反查激活鉴权4合一接口)
	 * @param aidlAccount
	 * @param localMac
	 * @param type 0--live,1--vod
	 * @param tag  0-CTHW,1--CMHW,2--CMZTE
	 * @param iUpdateData
	 * @return void    返回类型
	 * @modifyHistory  createBy Mernake
	 */
	public void apkAuth2(String aidlAccount,String localMac,String type,String tag,final IUpdateData iUpdateData)
	{
		if (isGDI6Enable)
		{
			showLog("广电apkAuth接口：请求");
			showLog("广电apkAuth接口：aidlAccount："+aidlAccount);
			showLog("广电apkAuth接口：localMac："+localMac);
			showLog("广电apkAuth接口：type："+type);
			showLog("广电apkAuth接口：tag："+tag);
			SDKMethods.apkAuth2(aidlAccount, localMac, type, tag, new SDKMethods.RequestCallback()
			{
				@Override
				public void onGetData(String str)
				{
					showLog("广电apkAuth接口返回："+str);
					if (str!=null&&str.length()>0){
						GDAuth4in1Res res = new Gson().fromJson(str, GDAuth4in1Res.class);
						showLog("广电apkAuth接口：data："+res.getData().toString());
						showLog("广电apkAuth接口：res："+res.toString());
						if (res.getCode().contains("51000"))
						{
							iUpdateData.updateData(METHOD_I6_4IN1, "", res, true);
						}else{
							iUpdateData.updateData(METHOD_I6_4IN1, "", res, false);
						}
					}
				}
			});
		}
	}

	/**
	 *
	  * getAccount(获取账号/密码)
	  * @param type
	  * @param tag
	  * @param mac
	  * @param iUpdateData
	  * @return void    返回类型
	  * @modifyHistory  createBy Mernake
	 */
	public void getAccount(String type,String tag,String mac,final IUpdateData iUpdateData)
	{
		if (isGDI6Enable)
		{
			showLog("广电getAccount接口：type："+type);
			showLog("广电getAccount接口：tag："+tag);
			showLog("广电getAccount接口：mac："+mac);

			SDKMethods.getAccount(type, tag, mac, new SDKMethods.RequestCallback()
			{
				@Override
				public void onGetData(String str)
				{
					GDAuthGetAccountRes res = new Gson().fromJson(str, GDAuthGetAccountRes.class);
					showLog("广电getAccount接口：data："+res.getData().toString());
					showLog("广电getAccount接口：res："+res.toString());
					if (res.getCode().equals("51000"))
					{
						iUpdateData.updateData(METHOD_I6_GETACCOUNT, "", res, true);
					}else{
						iUpdateData.updateData(METHOD_I6_GETACCOUNT, "", res, false);
					}
				}
			});
		}
	}

	/**
	 *
	  * activateTerminal(广电激活接口)
	  * @param type
	  * @param tag
	  * @param account
	  * @param mac
	  * @param iUpdateData
	  * @return void    返回类型
	  * @modifyHistory  createBy Mernake
	 */
	public void activateTerminal(String type,String tag,String account,String mac,final IUpdateData iUpdateData)
	{
		if (isGDI6Enable)
		{
			showLog("广电activateTerminal接口：type："+type);
			showLog("广电activateTerminal接口：tag："+tag);
			showLog("广电activateTerminal接口：account："+account);
			showLog("广电activateTerminal接口：mac："+mac);
			SDKMethods.activateTerminal(type, tag, account, mac, new SDKMethods.RequestCallback()
			{
				@Override
				public void onGetData(String str)
				{
					GDAuthActivateTerminalRes res = new Gson().fromJson(str, GDAuthActivateTerminalRes.class);
					showLog("广电activateTerminal接口：data："+res.getData().toString());
					showLog("广电activateTerminal接口：res："+res.toString());
					if (res.getCode().equals("51000"))
					{
						iUpdateData.updateData(METHOD_I6_ACTIVATETERMINAL, "", res, true);
					}else{
						iUpdateData.updateData(METHOD_I6_ACTIVATETERMINAL, "", res, false);
					}
				}
			});
		}
	}

	/**
	 *
	  * authentication(广电authentication接口)
	  * @param type
	  * @param tag
	  * @param account
	  * @param iUpdateData
	  * @return void    返回类型
	  * @modifyHistory  createBy Mernake
	 */
	public void authentication(String type,String tag,String account,final IUpdateData iUpdateData)
	{
		if (isGDI6Enable)
		{
			showLog("广电authentication接口：type："+type);
			showLog("广电authentication接口：tag："+tag);
			showLog("广电authentication接口：account："+account);
			SDKMethods.authentication(type, tag, account, new SDKMethods.RequestCallback()
			{
				@Override
				public void onGetData(String str)
				{
					GDAuthAuthenticationRes res = new Gson().fromJson(str, GDAuthAuthenticationRes.class);
					showLog("广电authentication接口：res："+res.toString());
					if (res.getCode().equals("51000"))
					{
						iUpdateData.updateData(METHOD_I6_AUTHENTICATION, "", res, true);
					}else{
						iUpdateData.updateData(METHOD_I6_AUTHENTICATION, "", res, false);
					}
				}
			});
		}
	}

	/**
	 *
	  * auth(auth接口)
	  * @param type
	  * @param tag
	  * @param account
	  * @param iUpdateData
	  * @return void    返回类型
	  * @modifyHistory  createBy Mernake
	 */
	public void auth(String type,String tag,String account,final IUpdateData iUpdateData){
		if (isGDI6Enable)
		{
			showLog("广电auth接口：type："+type);
			showLog("广电auth接口：tag："+tag);
			showLog("广电auth接口：account："+account);
			SDKMethods.auth(type, tag, account, new SDKMethods.RequestCallback()
			{
				@Override
				public void onGetData(String  str)
				{
					GDAuthAuthRes res = new Gson().fromJson(str, GDAuthAuthRes.class);
					showLog("广电auth接口：data："+res.getData().toString());
					showLog("广电auth接口：res："+res.toString());
					if (res.getCode().equals("51000"))
					{
						iUpdateData.updateData(METHOD_I6_AUTH, "", res, true);
					}else{
						iUpdateData.updateData(METHOD_I6_AUTH, "", res, false);
					}
				}
			});

		}
	}

	/**
	 *
	  * playAuth(播放鉴权接口)
	  * @param type
	  * @param tag
	  * @param programID
	  * @param categoryID
	  * @param superCid
	  * @param userId
	  * @param userToken
	  * @param epgUrlAidl
	  * @param usertokenAIDL
	  * @param iUpdateData
	  * @return void    返回类型
	  * @modifyHistory  createBy Mernake
	 */
//	private String testplayauthstr = "{\"code\":\"60000-2\",\"data\":[{\"endtime\":\"20180618154638ð20991231235959\",\"point\":\"0\",\"starttime\":\"20180518154638\",\"price\":\"2000\",\"authfalg\":\"1\",\"continueable\":\"1\",\"productid\":\"LW001\",\"name\":\"海看大片VIP包月\",\"producttype\":1,\"serviceid\":\"smgpack0000000000000000000000281\"},{\"endtime\":\"20180618154638ð20991231235959\",\"point\":\"0\",\"starttime\":\"20180518154638\",\"price\":\"200\",\"authfalg\":\"1\",\"continueable\":\"1\",\"productid\":\"LW002\",\"name\":\"海看大片VIP包季\",\"producttype\":1,\"serviceid\":\"smgpack0000000000000000000000281\"},{\"endtime\":\"20180618154638ð20991231235959\",\"point\":\"0\",\"starttime\":\"20180518154638\",\"price\":\"10\",\"authfalg\":\"1\",\"continueable\":\"1\",\"productid\":\"LW003\",\"name\":\"海看大片VIP包年\",\"producttype\":1,\"serviceid\":\"smgpack0000000000000000000000281\"}],\"description\":\"订购\",\"endTime\":\"2018-07-14 13:52:59.423\",\"msg\":\"order\",\"startTime\":\"2018-07-14 13:52:58.822\",\"state\":\"success\"}";
//	private String testplayauthstr = "{\"code\":\"60000-2\",\"data\":[{\"endtime\":\"20180618154638ð20991231235959\",\"point\":\"0\",\"starttime\":\"20180518154638\",\"price\":\"2000\",\"authfalg\":\"1\",\"continueable\":\"1\",\"productid\":\"LW001\",\"name\":\"海看大片VIP包月\",\"producttype\":\"1\",\"serviceid\":\"smgpack0000000000000000000000281\"},{\"endtime\":\"20180618154638ð20991231235959\",\"point\":\"0\",\"starttime\":\"20180518154638\",\"price\":\"200\",\"authfalg\":\"1\",\"continueable\":\"1\",\"productid\":\"LW002\",\"name\":\"海看大片VIP包季\",\"producttype\":\"1\",\"serviceid\":\"smgpack0000000000000000000000281\"},{\"endtime\":\"20180618154638ð20991231235959\",\"point\":\"0\",\"starttime\":\"20180518154638\",\"price\":\"10\",\"authfalg\":\"1\",\"continueable\":\"1\",\"productid\":\"LW003\",\"name\":\"海看大片VIP包年\",\"producttype\":\"1\",\"serviceid\":\"smgpack0000000000000000000000281\"}],\"description\":\"订购\",\"endTime\":\"2018-07-14 13:52:59.423\",\"msg\":\"order\",\"startTime\":\"2018-07-14 13:52:58.822\",\"state\":\"success\"}";
//	private String testplayauthstr1 = "{\"code\":\"60000-1\",\"data\":[],\"description\":\"播放\",\"endTime\":\"2018-08-09 10:22:38.083\",\"msg\":\"play\",\"startTime\":\"2018-08-09 10:22:37.608\",\"state\":\"success\"}";
	/*public void playAuth(final Context context, final String type, final String tag, final String programID, final String categoryID, String superCid, String userId, String userToken, String epgUrlAidl, String usertokenAIDL, final IUpdateData iUpdateData)
	{
		if (isGDOrderEnable)
		{
			showLog("广电playAuth接口：type："+type);
			showLog("广电playAuth接口：tag："+tag);
			showLog("广电playAuth接口：programID："+programID);
			showLog("广电playAuth接口：categoryID："+categoryID);
			showLog("广电playAuth接口：superCid："+superCid);
			showLog("广电playAuth接口：userId："+userId);
			showLog("广电playAuth接口：userToken："+userToken);
			showLog("广电playAuth接口：epgUrlAidl："+epgUrlAidl);
			showLog("广电playAuth接口：usertokenAIDL："+usertokenAIDL);

			SDKMethods.playAuth(type, tag, programID, categoryID, superCid, userId, userToken, epgUrlAidl, usertokenAIDL, new SDKMethods.RequestCallback()
			{
				@Override
				public void onGetData(String str)
				{
					showLog("广电playAuth接口："+str);
					if (tag.equals("1"))  //移动华为平台
					{
						GDOrderPlayAuthCMHWRes res = new Gson().fromJson(str, GDOrderPlayAuthCMHWRes.class);
						showLog("广电playAuth接口：res："+res.toString());
						if (res.getCode().contains("51000"))
						{
							playAuthList.put(programID,res);
                            ZZFileTools.getInstance().saveZZData(context,programID,res);

							if (iUpdateData!=null)iUpdateData.updateData(METHOD_ORDER_PLAYAUTH, "", res, true);
						}else{
							if (iUpdateData!=null)iUpdateData.updateData(METHOD_ORDER_PLAYAUTH, "", res, false);
						}
					}
				}
			});
		}
	}
*/
	/**
	 *
	  * bestoPlayAuth(广电百途播放鉴权方法)
	  * @param type
	  * @param tag
	  * @param programid
	  * @param categoryId
	  * @param userid
	  * @param usertoken
	  * @param iUpdateData
	  * @return void    返回类型
	  * @modifyHistory  createBy Mernake
	 */
	public void bestoPlayAuth(String type,String tag,String programid,String categoryId,String userid,String usertoken,final IUpdateData iUpdateData)
	{
		if (isGDOrderEnable) {
			showLog("广电bestoPlayAuth接口：type："+type);
			showLog("广电bestoPlayAuth接口：tag："+tag);
			showLog("广电bestoPlayAuth接口：programID："+programid);
			showLog("广电bestoPlayAuth接口：categoryID："+categoryId);
			showLog("广电bestoPlayAuth接口：userid："+userid);
			showLog("广电bestoPlayAuth接口：usertoken："+usertoken);

			SDKMethods.bestoPlayAuth(type, tag, programid, categoryId, userid, usertoken, new SDKMethods.RequestCallback()
			{
				@Override
				public void onGetData(String str)
				{
					showLog("广电bestoPlayAuth接口："+str);
					GDOrderbestoPlayAuthRes res = new Gson().fromJson(str, GDOrderbestoPlayAuthRes.class);
					showLog("广电bestoPlayAuth接口：res："+res.toString());
					if (res.getCode().contains("51000")) {
						iUpdateData.updateData(METHOD_ORDER_BESTOPLAYAUTH, "", res, true);
					}else {
						iUpdateData.updateData(METHOD_ORDER_BESTOPLAYAUTH, "", res, false);
					}
				}
			});
		}
	}

	/**
	 *
	  * cmhPlayAuth(移动华为播放鉴权)
	  * @param type
	  * @param programId
	  * @param supercid
	  * @param epgurlaidl
	  * @param usertoeknaidl
	  * @param iUpdateData
	  * @return void    返回类型
	  * @modifyHistory  createBy Mernake
	 */
	public void cmhPlayAuth(String type,String programId,String supercid,String epgurlaidl,String usertoeknaidl,final IUpdateData iUpdateData)
	{
		if (isGDOrderEnable)
		{
			showLog("广电cmhPlayAuth接口：type："+type);
			showLog("广电cmhPlayAuth接口：programId："+programId);
			showLog("广电cmhPlayAuth接口：supercid："+supercid);
			showLog("广电cmhPlayAuth接口：epgurlaidl："+epgurlaidl);
			showLog("广电cmhPlayAuth接口：usertoeknaidl："+usertoeknaidl);
			SDKMethods.cmhPlayAuth(type, programId, supercid, epgurlaidl, usertoeknaidl, new SDKMethods.RequestCallback()
			{
				@Override
				public void onGetData(String str)
				{
					showLog("广电cmhPlayAuth接口："+str);
					GDOrderPlayAuthCMHWRes res = new Gson().fromJson(str, GDOrderPlayAuthCMHWRes.class);
					showLog("广电cmhPlayAuth接口：res："+res.toString());
					if (res.getCode().contains("51000"))
					{
						iUpdateData.updateData(METHOD_ORDER_CMHWPLAYAUTH, "", res, true);
					}else {
						iUpdateData.updateData(METHOD_ORDER_CMHWPLAYAUTH, "", res, false);
					}
				}
			});
		}
	}

	/**
	 * "{\"code\":\"12000\",\"data\":[{\"endtime\":\"20180618154638ð20991231235959\",\"point\":\"0\",\"starttime\":\"20180518154638\",\"price\":\"2000\",\"authfalg\":\"1\",\"continueable\":\"1\",\"productid\":\"TVDY20Y\",\"name\":\"海看大片VIP包月\",\"producttype\":\"0\",\"serviceid\":\"smgpack0000000000000000000000281\"},{\"endtime\":\"20180618154638ð20991231235959\",\"point\":\"0\",\"starttime\":\"20180518154638\",\"price\":\"2000\",\"authfalg\":\"1\",\"continueable\":\"1\",\"productid\":\"TVDY20E\",\"name\":\"海看大片VIP包季\",\"producttype\":\"0\",\"serviceid\":\"smgpack0000000000000000000000282\"}],\"description\":\"获取移动华为侧百途产品列表成功\",\"endTime\":\"2018-07-1413:52:59.423\",\"msg\":\"getCMhbestoProductListsucceed\",\"startTime\":\"2018-07-1413:52:58.822\",\"state\":\"success\"}"
	  * cmhbestoProList(移动华为平台百途产品列表获取方法)
	  * @param type
	  * @param categoryId
	  * @param programId
	  * @param usertoken
	  * @param iUpdateData
	  * @return void    返回类型
	  * @modifyHistory  createBy Mernake
	 */

//	private String testcmhbestoproliststr = "{\"code\":\"12000\",\"data\":[{\"endtime\":\"20180618154638ð20991231235959\",\"point\":\"0\",\"starttime\":\"20180518154638\",\"price\":\"2000\",\"authfalg\":\"1\",\"continueable\":\"1\",\"productid\":\"LW001\",\"name\":\"海看大片VIP包月\",\"producttype\":\"0\",\"serviceid\":\"smgpack0000000000000000000000281\"},{\"endtime\":\"20180618154638ð20991231235959\",\"point\":\"0\",\"starttime\":\"20180518154638\",\"price\":\"2000\",\"authfalg\":\"1\",\"continueable\":\"1\",\"productid\":\"LW002\",\"name\":\"海看大片VIP包季\",\"producttype\":\"0\",\"serviceid\":\"smgpack0000000000000000000000282\"}],\"description\":\"获取移动华为侧百途产品列表成功\",\"endTime\":\"2018-07-1413:52:59.423\",\"msg\":\"getCMhbestoProductListsucceed\",\"startTime\":\"2018-07-1413:52:58.822\",\"state\":\"success\"}";
	public void cmhbestoProList(String type, String categoryId, final String programId, String usertoken, final IUpdateData iUpdateData)
	{
		if (isGDOrderEnable) {
			showLog("广电cmhbestoProList接口：type："+type);
			showLog("广电cmhbestoProList接口：categoryId："+categoryId);
			showLog("广电cmhbestoProList接口：programId："+programId);
			showLog("广电cmhbestoProList接口：usertoken："+usertoken);
			SDKMethods.CMhBestoProList(type, categoryId, programId, usertoken, new SDKMethods.RequestCallback()
			{
				@Override
				public void onGetData(String str)
				{
					showLog("广电cmhbestoProList接口："+str);
					GDOrderPlayAuthCMHWRes res = new Gson().fromJson(str, GDOrderPlayAuthCMHWRes.class);
					showLog("广电cmhbestoProList接口：res："+res.toString());
					if (res.getCode().equals("51000"))
					{
						updateProductListToChannel(programId,res);
						iUpdateData.updateData(METHOD_ORDER_CMHBESTOPROLIST, "", res, true);
					}else{
						iUpdateData.updateData(METHOD_ORDER_CMHBESTOPROLIST, "", res, false);
					}
				}
			});
		}

	}

	private void updateProductListToChannel(String contentID,GDOrderPlayAuthCMHWRes res)
	{
		if (playAuthList.get(contentID) == null) return;
		playAuthList.get(contentID).setData(res.getData());
		playAuthList.get(contentID).setCode("51A00-2");
	}

//private String testproductlistinfo = "{\"code\":\"13000\",\"data\":{\"imgUrl\":\"http://tsnm-hk.snctv.cn:85/pic/ott/asm/20180423/fb5c8b76604f4f21bc89250edbc3a573.png\",\"authTime\":28800,\"productTypeDesc\":[\"包月产品\",\"按次产品\"],\"isVisible\":\"1\",\"productMsgList\":[{\"oldPrice\":\"\",\"peiceHead\":\"折扣价\",\"priceEnd\":\"元/部\",\"productID\":\"LW001\",\"vipDescription\":\"购买后72小时有效\",\"vipTime\":\"72小时\"},{\"oldPrice\":\"原价9.9元\",\"peiceHead\":\"折扣价\",\"priceEnd\":\"元/部\",\"productID\":\"LW002\",\"vipDescription\":\"购买后72小时有效\",\"vipTime\":\"72小时\"},{\"oldPrice\":\"原价20元\",\"peiceHead\":\"折扣价\",\"priceEnd\":\"元/月\",\"productID\":\"LW003\",\"vipDescription\":\"尊享海看少儿VIP特权\",\"vipTime\":\"30天\"}],\"title1\":\"开通VIP套餐畅享专属特权\",\"title2\":\"购买海看VIP影视包即享会员专属特权\"},\"description\":\"获取到产品信息数据\",\"endTime\":\"2018-07-1413:52:59.423\",\"msg\":\"getproductInfodata\",\"startTime\":\"2018-07-14 13:52:58.822\",\"state\":\"success\"}";
	/**
	 * 
	  * getProductListInfo(获取产品列表详情接口)
	  * @param type
	  * @param tag
	  * @param iUpdateData  
	  * @return void    返回类型
	  * @modifyHistory  createBy Mernake
	 */
	public void getProductListInfo(final Context context, String type, String tag, final IUpdateData iUpdateData)
	{
		if (isGDOrderEnable) 
		{
			showLog("广电getProductListInfo接口：type："+type);
			showLog("广电getProductListInfo接口：tag："+tag);

//			String str = testproductlistinfo;
//			gdOrderGetProductListInfoRes = new Gson().fromJson(str, GDOrderGetProductListInfoRes.class);
//			showLog("广电getProductListInfo接口：res："+gdOrderGetProductListInfoRes.toString());
//			ZZFileTools.getInstance().setTIMEOUT_SECONDS(context,gdOrderGetProductListInfoRes.getData().getAuthTime());
//			if (gdOrderGetProductListInfoRes.getCode().equals("13000"))
//			{
//				iUpdateData.updateData(METHOD_ORDER_GETPRODUCTLISTINFO, "", gdOrderGetProductListInfoRes, true);
//			}else{
//				iUpdateData.updateData(METHOD_ORDER_GETPRODUCTLISTINFO, "", gdOrderGetProductListInfoRes, false);
//			}

			SDKMethods.getProductListInfo(type, tag, new SDKMethods.RequestCallback()
			{
				@Override
				public void onGetData(String str)
				{
					showLog("广电getProductListInfo接口："+str);
					gdOrderGetProductListInfoRes = new Gson().fromJson(str, GDOrderGetProductListInfoRes.class);
					showLog("广电getProductListInfo接口：res："+gdOrderGetProductListInfoRes.toString());
					ZZFileTools.getInstance().setTIMEOUT_SECONDS(context,gdOrderGetProductListInfoRes.getData().getAuthTime());
					if (gdOrderGetProductListInfoRes.getCode().equals("51000"))
					{
						iUpdateData.updateData(METHOD_ORDER_GETPRODUCTLISTINFO, "", gdOrderGetProductListInfoRes, true);
					}else{
						iUpdateData.updateData(METHOD_ORDER_GETPRODUCTLISTINFO, "", gdOrderGetProductListInfoRes, false);
					}
				}
			});
		}
	}


//	private String testCMHCheckOrder = "{\"code\":\"11000-1\",\"data\":{\"createtime\":\"20180618154638ð20991231235959\",\"orderID\":\"356145630\",\"orderStatus\":\"0\"},\"description\":\"已订购\",\"endTime\":\"2018-07-1413:52:59.423\",\"msg\":\"alreadyorder\",\"startTime\":\"2018-07-1413:52:58.822\",\"state\":\"success\"}";
	/**
	 * 
	  * cmhCheckOrder(移动华为平台订购查询方法)
	  * @param type
	  * @param account
	  * @param programId
	  * @param productId
	  * @param userTokenAIDL
	  * @param iUpdateData  
	  * @return void    返回类型
	  * @modifyHistory  createBy Mernake
	 */
	public void cmhCheckOrder(String type,String account,String programId,String productId,String userTokenAIDL,final IUpdateData iUpdateData)
	{
		if (isGDOrderEnable) 
		{
			showLog("广电cmhCheckOrder接口：type："+type);
			showLog("广电cmhCheckOrder接口：account："+account);
			showLog("广电cmhCheckOrder接口：programId："+programId);
			showLog("广电cmhCheckOrder接口：productId："+productId);
			showLog("广电cmhCheckOrder接口：userTokenAIDL："+userTokenAIDL);
			
			SDKMethods.cmhCheckOrder(type, account, programId, productId, userTokenAIDL, new SDKMethods.RequestCallback()
			{
				@Override
				public void onGetData(String str) 
				{
					showLog("广电cmhCheckOrder接口："+str);
					GDOrdercmhCheckOrderRes res = new Gson().fromJson(str, GDOrdercmhCheckOrderRes.class);
					showLog("广电cmhCheckOrder接口：res："+res.toString());
					if (res.getCode().contains("51000-1"))
					{
						iUpdateData.updateData(METHOD_ORDER_CMHCHECKORDER, "", res, true);
					}else{
						iUpdateData.updateData(METHOD_ORDER_CMHCHECKORDER, "", res, false);
					}
				}
			});
		}
	}

//	private String cmhorderteststr = "{\"code\":\"14000\",\"data\":{\"code\":\"0\",\"orderID\":\"356145630\",\"payURL\":\"0\"},\"description\":\"话费支付成功\",\"endTime\":\"2018-07-1413:52:59.423\",\"msg\":\"telephonefarepaysuccess\",\"startTime\":\"2018-07-1413:52:58.822\",\"state\":\"success\"}";
//	private String cmhorderteststr2 = "{\"code\":\"14000\",\"data\":{\"code\":\"0\",\"orderID\":\"356145630\",\"payURL\":\"http://tsnm-hk.snctv.cn:85/pic/ott/0edbc3a573.png\"},\"description\":\"支付宝二维码获取成功\",\"endTime\":\"2018-07-1413:52:59.423\",\"msg\":\"zhifubaoPayURLgetsuccess\",\"startTime\":\"2018-07-1413:52:58.822\",\"state\":\"success\"}";
	/**
	 * 
	  * cmhOrder(移动华为订购)
	  * @param type
	  * @param continueAble
	  * @param orderMode
	  * @param account
	  * @param userTokenAIDL
	  * @param product
	  * @param iUpdateData  
	  * @return void    返回类型
	  * @modifyHistory  createBy Mernake
	 */
	public void cmhOrder(final Context context, String type, String continueAble, final String orderMode, String account, String userTokenAIDL, final CMHwAuthReturn.ProductlistBean product, final IUpdateData iUpdateData)
	{
		if (isGDOrderEnable) 
		{
			if (orderMode.equals("56")) continueAble = "0";
			showLog("广电cmhOrder接口：type："+type);
			showLog("广电cmhOrder接口：continueAble："+continueAble);
			showLog("广电cmhOrder接口：orderMode："+orderMode);
			showLog("广电cmhOrder接口：account："+account);
			showLog("广电cmhOrder接口：userTokenAIDL："+userTokenAIDL);
			SDKMethods.cmhOrder(type, continueAble, orderMode, account, userTokenAIDL, product, new SDKMethods.RequestCallback()
			{
				@Override
				public void onGetData(String str) 
				{
					showLog("广电cmhOrder接口："+str);
//					if (orderMode.equals("56")){
//						str = cmhorderteststr2;
//					}else{
//						str = cmhorderteststr;
//					}
					GDOrdercmhOrderRes res = new Gson().fromJson(str, GDOrdercmhOrderRes.class);
					showLog("广电cmhOrder接口：res："+res.toString());
					if (res.getCode().contains("51000"))
					{
						if (orderMode.equals("56"))
						{
							//存图片URL
							ZZFileTools.getInstance().savePayUrls(context,product.getProductid(),res.getData().getPayURL(), System.currentTimeMillis());
						}
						iUpdateData.updateData(METHOD_ORDER_CMHORDER, orderMode, res, true);
					}else{
						iUpdateData.updateData(METHOD_ORDER_CMHORDER, "", res, false);
					}
				}
			});
		}
	}

//	private String testsyncstr = "{\"code\":\"15000-1\",\"data\":\"0\",\"description\":\"订购关系同步成功\",\"endTime\":\"2018-07-1413:52:59.423\",\"msg\":\"syncsuccess\",\"startTime\":\"2018-07-1413:52:58.822\",\"state\":\"success\"}";
	/**
	 * 
	  * cmhOrderSync(移动华为订购关系同步)
	  * @param type
	  * @param createTime
	  * @param account
	  * @param userToken
	  * @param orderID
	  * @param productId
	  * @param programId
	  * @param iUpdateData  
	  * @return void    返回类型
	  * @modifyHistory  createBy Mernake
	 */
	public void cmhOrderSync(final Context context, String type, String createTime, String account, String userToken, String orderID, String productId, final String programId, final IUpdateData iUpdateData)
	{
		if (isGDOrderEnable) 
		{
			showLog("广电cmhOrderSync接口：type："+type);
			showLog("广电cmhOrderSync接口：createTime："+createTime);
			showLog("广电cmhOrderSync接口：account："+account);
			showLog("广电cmhOrderSync接口：userToken："+userToken);
			showLog("广电cmhOrderSync接口：orderID："+orderID);
			showLog("广电cmhOrderSync接口：productId："+productId);
			showLog("广电cmhOrderSync接口：programId："+programId);
			SDKMethods.cmhOrderSync(type, createTime, account, userToken, orderID, productId, programId, new SDKMethods.RequestCallback()
			{
				@Override
				public void onGetData(String str) 
				{
					showLog("广电cmhOrderSync接口："+str);
					ZZFileTools.getInstance().saveTime(context, (int) (System.currentTimeMillis()/1000));
					GDOrdercmhOrderSyncRes res = new Gson().fromJson(str, GDOrdercmhOrderSyncRes.class);
					showLog("广电cmhOrderSync接口：res："+res.toString());
					if (res.getCode().contains("51000-1"))
					{
//						testplayauthstr = testplayauthstr1;
						iUpdateData.updateData(METHOD_ORDER_CMHORDERSYNC, programId, res, true);
					}else{
						iUpdateData.updateData(METHOD_ORDER_CMHORDERSYNC, programId, res, false);
					}
				}
			});
		}
	}
	
	/**
	 * 
	  * getLiveCategoryList(获取直播类别)
	  * @param tag
	  * @param userId
	  * @param userToken
	  * @param iUpdateData  
	  * @return void    返回类型
	  * @modifyHistory  createBy Mernake
	 */
	public void getLiveCategoryList(String tag, String userId, String userToken, final IUpdateData iUpdateData)
	{
		if (isGDI6Enable) 
		{
			showLog("广电getLiveCategoryList接口：tag："+tag);
			showLog("广电getLiveCategoryList接口：userId："+userId);
			showLog("广电getLiveCategoryList接口：userToken："+userToken);
			SDKMethods.getLiveCategoryList(tag, userId, userToken, new SDKMethods.RequestCallback()
			{
				@Override
				public void onGetData(String str) 
				{
					showLog("广电getLiveCategoryList接口："+str);
					if (str == null || str.length() == 0) {
						iUpdateData.updateData(METHOD_GETLIVECATEGORYLIST,null,null,false);
						return;
					}
					HttpGetCategoryList httpGetCategoryList = new HttpGetCategoryList(null);
					List<Category> list = httpGetCategoryList.parser(str);
					if (list==null||list.size()==0)iUpdateData.updateData(METHOD_GETLIVECATEGORYLIST,null,null,false);
					else iUpdateData.updateData(METHOD_GETLIVECATEGORYLIST,null,list,true);
				}
			});
		}
	}

    public void getDefaultLiveContentList(IUpdateData iUpdateData)
    {
        StringBuilder sb = new StringBuilder();
        if (getTag().equals("0"))
        {
            showLog("读取电信预置列表");
            sb = VarChannels.getCTChannels();
        }else if (getTag().equals("1")){//移动华为平台
            showLog("读取移动华为预置列表");
            sb = VarChannels.getCMHWChannels();
        }else if (getTag().equals("2")){
            showLog("读取移动中兴预置列表");
            sb = VarChannels.getCMZTEChannels();
        }

        GDAuthGetStaticContent res = new Gson().fromJson(sb.toString(),GDAuthGetStaticContent.class);
        List<ContentChannel> list = new ArrayList<ContentChannel>();
        for (GDAuthGetStaticContent.GDAuthGetStaticContentData data:res.getData())
        {
            ContentChannel contentChannel = new ContentChannel();
            contentChannel.setCallSign(data.getCallSign());
            contentChannel.setChannelNumber(data.getChannelnumber());
            contentChannel.setCity(data.getCity());
            contentChannel.setContentId(data.getCode());
            contentChannel.setCountry(data.getCountry());
            contentChannel.setDescription(data.getDescription());
            contentChannel.setCountry(data.getIsvip());
            contentChannel.setLanguage(data.getLaunguage());
            contentChannel.setName(data.getName());
            contentChannel.setPlayURL(data.getPlayurl());
            contentChannel.setCallSign(data.getSelected());
            contentChannel.setState(data.getState());
            contentChannel.setTimeShift(data.getTimeshift());
            contentChannel.setTimeShiftDuration(data.getTimeshiftduration());
            contentChannel.setTimeShiftURL(data.getTimeshifturl());
            contentChannel.setDescription(data.getVipfileurl());
            contentChannel.setZipCode(data.getZipcode());
            list.add(contentChannel);
        }
        Var.allChannels = list;
        if (list.size()==0)iUpdateData.updateData(METHOD_GETLIVECONTENTLIST,"",null,false);
        else{
            iUpdateData.updateData(METHOD_GETLIVECONTENTLIST,"",list,true);
        }
    }

	public void getStaticLiveCategoryList(String tag, final IUpdateData iUpdateData){
		showLog("广电getStaticLiveCategoryList接口：tag："+tag);
		SDKMethods.getStaticLiveCategoryList(tag, new SDKMethods.RequestCallback() {
			@Override
			public void onGetData(String str)
			{
				showLog("广电getStaticLiveCategoryList接口："+str);
				if (str == null || str.length() == 0) {
					iUpdateData.updateData(METHOD_GETLIVECATEGORYLIST,null,null,false);
					return;
				}
				GDAuthLiveCategory res = new Gson().fromJson(str,GDAuthLiveCategory.class);
				if (res!=null&&res.getCode().equals("51000")){
					List<Category> list = new ArrayList<Category>();
					for (GDAuthLiveCategory.GDAuthLiveCategoryData data:res.getData()){
						Category category = new Category();
						category.setPosition(data.getPostion());
						category.setSequence(data.getSequence());
						category.setDescription(data.getDescription());
						category.setName(data.getName());
						category.setImgurl(data.getFileurl());
						category.setPfile(data.getPfile());
						category.setPname(data.getPname());
						category.setChilds(data.getChilds());
						category.setDisplaytype(data.getDisplaytype());
						category.setParentid(data.getParentid());
						category.setId(data.getPrimaryid());
						list.add(category);
					}
					iUpdateData.updateData(METHOD_GETLIVECATEGORYLIST,null,list,true);
				}else{
					iUpdateData.updateData(METHOD_GETLIVECATEGORYLIST,null,res,false);
				}
			}
		});
	}

	
	/**
	 * 
	  * getLiveContentList(获取频道列表)
	  * @param tag
	  * @param userid
	  * @param usertoken
	  * @param categoryid
	  * @param iUpdateData  
	  * @return void    返回类型
	  * @modifyHistory  createBy Mernake
	 */
	public void getLiveContentList(String tag, String userid, String usertoken, final String categoryid, final IUpdateData iUpdateData){
		if (isGDI6Enable) {
			showLog("广电getLiveContentList接口：tag："+tag);
			showLog("广电getLiveContentList接口：userId："+userid);
			showLog("广电getLiveContentList接口：userToken："+usertoken);
			showLog("广电getLiveContentList接口：categoryid："+categoryid);
			SDKMethods.getLiveContentList(tag, userid, usertoken, categoryid, new SDKMethods.RequestCallback()
			{
				@Override
				public void onGetData(String str) 
				{
					showLog("广电getLiveContentList接口："+str);
					if (str==null||str.length()==0) iUpdateData.updateData(METHOD_GETLIVECONTENTLIST,categoryid,null,false);
					HttpGetContentList httpGetContentList = new HttpGetContentList(null);
                    GetContentList req = new GetContentList();
                    req.setCategoryId(categoryid);
                    httpGetContentList.setReq(req);
					GetContentListRes res = httpGetContentList.parser(str);
					if (res==null||res.getTotalCount()==null||res.getTotalCount().equals("0"))iUpdateData.updateData(METHOD_GETLIVECONTENTLIST,categoryid,null,false);
					else iUpdateData.updateData(METHOD_GETLIVECONTENTLIST,categoryid,res.getChannelList(),true);
				}
			});
		}
	}

	public void getStaticLiveContentList(String tag, final String categoryid, String userGroupId, final IUpdateData iUpdateData){
		if (isGDI6Enable){
			showLog("广电getStaticLiveContentList接口：tag："+tag);
			showLog("广电getStaticLiveContentList接口：categoryId："+categoryid);
			showLog("广电getStaticLiveContentList接口：userGroupId："+userGroupId);
			SDKMethods.getStaticLiveContentList(tag, categoryid, userGroupId, new SDKMethods.RequestCallback()
			{
				@Override
				public void onGetData(String str)
				{
					showLog("广电getStaticLiveContentList接口："+str);
					if (str==null||str.length()==0) iUpdateData.updateData(METHOD_GETLIVECONTENTLIST,categoryid,null,false);
					GDAuthGetStaticContent res = new Gson().fromJson(str,GDAuthGetStaticContent.class);
					if (!res.getCode().equals("51000")){
						iUpdateData.updateData(METHOD_GETLIVECONTENTLIST,null,res,false);
						return;
					}
					if (res==null||res.getData()==null||res.getData().size()==0)iUpdateData.updateData(METHOD_GETLIVECONTENTLIST,categoryid,null,false);
					List<ContentChannel> list = new ArrayList<ContentChannel>();
					for (GDAuthGetStaticContent.GDAuthGetStaticContentData data:res.getData())
					{
						ContentChannel contentChannel = new ContentChannel();
						contentChannel.setCallSign(data.getCallSign());
						contentChannel.setChannelNumber(data.getChannelnumber());
						contentChannel.setCity(data.getCity());
						contentChannel.setContentId(data.getCode());
						contentChannel.setCountry(data.getCountry());
						contentChannel.setDescription(data.getDescription());
						contentChannel.setCountry(data.getIsvip());
						contentChannel.setLanguage(data.getLaunguage());
						contentChannel.setName(data.getName());
						contentChannel.setPlayURL(data.getPlayurl());
						contentChannel.setCallSign(data.getSelected());
						contentChannel.setState(data.getState());
						contentChannel.setTimeShift(data.getTimeshift());
						contentChannel.setTimeShiftDuration(data.getTimeshiftduration());
						contentChannel.setTimeShiftURL(data.getTimeshifturl());
						contentChannel.setDescription(data.getVipfileurl());
						contentChannel.setZipCode(data.getZipcode());
						list.add(contentChannel);
					}
					if (list.size()==0)iUpdateData.updateData(METHOD_GETLIVECONTENTLIST,categoryid,null,false);
					else{
						iUpdateData.updateData(METHOD_GETLIVECONTENTLIST,categoryid,list,true);
					}
				}
			});
		}
	}
	
	/**
	 * 
	  * getLiveScheduleListXML(获取全部频道未来6小时的节目单)
	  * @param tag  
	  * @return void    返回类型
	  * @modifyHistory  createBy Mernake
	 */
	public void getLiveScheduleListXML(String tag, final IUpdateData iUpdateData)
	{
		if (isGDI6Enable) 
		{
			showLog("广电getLiveScheduleListXML接口：tag："+tag);
			SDKMethods.getLiveScheduleListXML(tag, new SDKMethods.RequestCallback()
			{
				@Override
				public void onGetData(String str) 
				{
					showLog("广电getLiveScheduleListXML接口："+str);
					if (str==null||str.length()==0)return;
					//TODO 解析xml
					HttpAll6Schedules httpAll6Schedules = new HttpAll6Schedules(null);
					List<Schedule> list = httpAll6Schedules.parser(str);
					DataModel.getInstance().initAll6Schedules(list);
					if (iUpdateData!=null)iUpdateData.updateData(METHOD_GETLIVESCHEDULEALLLIST,"","",true);
				}
			});
		}
	}
	
	/**
	 * 
	  * getLiveScheduleList()
	  * @param tag
	  * @param channelId
	  * @param endDateTime
	  * @param startDateTime
	  * @param userid
	  * @param usertoken
	  * @param iUpdateData  
	  * @return void    返回类型
	  * @modifyHistory  createBy Mernake
	 */
	public void getLiveScheduleList(String tag, final String channelId, String endDateTime, String startDateTime, String userid, String usertoken, final IUpdateData iUpdateData, final int curChannel, final int curDate)
	{
		if (isGDI6Enable) {
			showLog("广电getLiveScheduleList接口：tag："+tag);
			showLog("广电getLiveScheduleList接口：channelId："+channelId);
			showLog("广电getLiveScheduleList接口：endDateTime："+endDateTime);
			showLog("广电getLiveScheduleList接口：startDateTime："+startDateTime);
			showLog("广电getLiveScheduleList接口：userid："+userid);
			showLog("广电getLiveScheduleList接口：usertoken："+usertoken);
			SDKMethods.getLiveScheduleList(tag, channelId, endDateTime, startDateTime, userid, usertoken, new SDKMethods.RequestCallback()
			{
				@Override
				public void onGetData(String str) 
				{
					showLog("广电getLiveScheduleList接口："+str);
					if (str == null || str.length() == 0) iUpdateData.updateData(METHOD_GETLIVESCHEDULELIST,null,null,false);
					HttpGetScheduleList httpGetScheduleList = new HttpGetScheduleList(null);
					GetScheduleList getScheduleList = new GetScheduleList();
					getScheduleList.setChannelIds(channelId);
					httpGetScheduleList.setReq(getScheduleList);
					List<Schedule> list = httpGetScheduleList.parser(str);
					if (list==null||list.size()==0)iUpdateData.updateData(METHOD_GETLIVESCHEDULELIST, channelId,null,false);
					else {
					    String uniid = channelId + "@@@" + curChannel + "@@@"+curDate;
					    System.out.println("###############################:::" + uniid);
                        iUpdateData.updateData(METHOD_GETLIVESCHEDULELIST, uniid,list,true);
                    }
				}
			});
			
		}
	}


	
//	public void requestAllVipPlayAuth(Context context)
//	{
//		if (isGDOrderEnable){
//			setAIDLParam(context);
//			for (ContentChannel channel: Var.allChannels){
//				playAuth(context,"0",getTag(),channel.getContentId(),Var.allCategoryId,"",Var.userId,Var.userToken,epgurlAIDL,usertokenAIDL,null);
//			}
//		}
//	}


	public GDOrderPlayAuthCMHWRes getGDOrderPlayAuthCMHWRes(String contentId){
		return playAuthList.get(contentId);
	}

	public GDOrderGetProductListInfoRes.GDOrderProductList getProductInfo(String productId)
	{
		for (int i=0;i<gdOrderGetProductListInfoRes.getData().getProductMsgList().size();i++){
			if (gdOrderGetProductListInfoRes.getData().getProductMsgList().get(i).getProductID().equals(productId)){
				return gdOrderGetProductListInfoRes.getData().getProductMsgList().get(i);
			}
		}
		return null;
	}

	public GDOrderGetProductListInfoRes getProductListInfoRes(){
		return gdOrderGetProductListInfoRes;
	}

	public CMHwAuthReturn.ProductlistBean getProductListBeanByProductId(String contentid, String productId)
	{
		if (playAuthList==null||playAuthList.size()==0)return null;
		GDOrderPlayAuthCMHWRes res = playAuthList.get(contentid);
		for (CMHwAuthReturn.ProductlistBean bean:res.getData()){
			if (bean.getProductid().equals(productId)) return bean;
		}
		return null;
	}

	public String getPayUrl(Context context,String productId){
		return ZZFileTools.getInstance().getPayUrls(context,productId);
	}

	public void addPlayAuthList(String contentid,GDOrderPlayAuthCMHWRes res){
	    playAuthList.put(contentid,res);
    }




	private void showLog(String content)
	{
		System.out.println(logtag + "=====>" + content);
//		System.out.println(logtag + "=====>Print time is:" + AbDateUtil.getStringByFormat(System.currentTimeMillis(),AbDateUtil.dateFormatYMDHMS));
	}

	//wangfangxu
	/**
	 * 获取首页数据
	 * @param tag
	 * @param iUpdateData
	 * @return
	 */
	public GDAuthAidlRes getRecommendData(String tag, final IUpdateData iUpdateData)
	{
		if (isGDI6Enable){
			showLog("广电getRecommendData接口：tag："+tag);
			if (tag .equals("2")) {//访问SDK接口变更，0-电信，1-移动，这个接口不再区分移动华为和移动中兴
				tag = "1";
				showLog("广电getRecommendData接口：tag值改变："+tag);
			}
			SDKMethods.getCMLiveRecommend(tag, new SDKMethods.RequestCallback()
			{
				@Override
				public void onGetData(String str)
				{
					showLog("广电getRecommendData接口："+str);
					if (str == null || str.length() == 0) iUpdateData.updateData(METHOD_GETRECOMMENDDATA,null,null,false);
					LiveRecommendData res = new Gson().fromJson(str,LiveRecommendData.class);
					if (res.getCode().contains("51000")){//51A00 --> 51000
						//success
						iUpdateData.updateData(METHOD_GETRECOMMENDDATA,"",res,true);
					}else{
						iUpdateData.updateData(METHOD_GETRECOMMENDDATA,"",res,false);
					}
				}
			});

		}
		return null;
	}
	//wangfangxu

	/**
	 *
	 * @param context
	 * @param tag 电信：0 移动华为：1 移动中兴：2
	 * @param queryType 包月产品：0 按次产品：1
	 * @param account 用户账户
	 * @param userTokenAIDL AIDL返回的userToken
	 * @param iUpdateData 回调方法
	 */
	public void queryOrderList(final Context context, String tag, String queryType, String account, String userTokenAIDL, final IUpdateData iUpdateData) {

		SDKMethods.queryOrderList(tag, queryType, account, userTokenAIDL, new SDKMethods.RequestCallback() {

			@Override
			public void onGetData(String str) {
				showLog("queryOrderList："+str);
				CMHOrderListData res = new Gson().fromJson(str, CMHOrderListData.class);
				showLog("queryOrderList："+res.toString());
				if (res.getCode().equals("51000")) {
					iUpdateData.updateData(METHOD_ORDER_QUERYORDERLIST, "", res, true);
				}else {
					iUpdateData.updateData(METHOD_ORDER_QUERYORDERLIST, "", res, false);
				}
			}
		});

	}

	/**
	 *取消订单接口
	 * @param context
	 * @param tag 直播：0 点播：1
	 * @param orderID 订单号
	 * @param account 用户账号
	 * @param usertokenAIDL
	 * @param iUpdateData
	 */
	public void cancelCMHOrder(final Context context, String tag, String orderID, String account, String usertokenAIDL, final IUpdateData iUpdateData) {
		showLog("广电cancelCMHOrder接口：tag："+tag);
		showLog("广电cancelCMHOrder接口：orderID："+orderID);
		showLog("广电cancelCMHOrder接口：account："+account);
		showLog("广电cancelCMHOrder接口：usertokenAIDL："+usertokenAIDL);
		SDKMethods.cancleCMhOrder(tag, orderID, account, usertokenAIDL, new SDKMethods.RequestCallback(){

			@Override
			public void onGetData(String str) {
				showLog("广电cancelCMHOrder接口："+str);
				GDCancelCMHOrderRes res = new Gson().fromJson(str, GDCancelCMHOrderRes.class);
				showLog("广电cancelCMHOrder接口："+res.toString());
				if (res.getCode().equals("51000")) {
					iUpdateData.updateData(METHOD_ORDER_CANCELORDER, "", res, true);
				}else {
					iUpdateData.updateData(METHOD_ORDER_CANCELORDER, "", res,false);
				}
			}
		});

	}

	public void sendcmSMS(String type, String tag, String userId, String usertokenAIDL, String EPGURLAIDL, String smsCode, final IUpdateData iUpdateData) {
		showLog("广电sendcmSMS接口：type："+type);
		showLog("广电sendcmSMS接口：tag："+tag);
		showLog("广电sendcmSMS接口：account："+userId);
		showLog("广电sendcmSMS接口：usertokenAIDL："+usertokenAIDL);
		showLog("广电sendcmSMS接口：EPGURLAIDL："+EPGURLAIDL);
		showLog("广电sendcmSMS接口：smsCode："+smsCode);
		//temp Code[S]
		//		userId = "18364127058";
		//temp Code[E]
		SDKMethods.sendcmSMS(type, tag, userId, usertokenAIDL, EPGURLAIDL, smsCode, new SDKMethods.RequestCallback(){
			@Override
			public void onGetData(String str) {
				showLog("广电sendcmSMS接口："+str);
				GDCMSMSCode res = new Gson().fromJson(str, GDCMSMSCode.class);
				showLog("广电sendcmSMS接口："+res.toString());
				if (res.getCode().equals("51000")) {
					iUpdateData.updateData(METHOD_ORDER_CMSENDSMS,"", res, true);
				}else {
					iUpdateData.updateData(METHOD_ORDER_CMSENDSMS,"", res, false);
				}
			}
		});
	}

	/**
	 *
	 * playAuth(播放鉴权接口)
	 * @param type
	 * @param tag
	 * @param programID
	 * @param categoryID
	 * @param superCid
	 * @param userId
	 * @param userToken
	 * @param epgUrlAidl
	 * @param usertokenAIDL
	 * @param iUpdateData
	 * @return void    返回类型
	 * @modifyHistory  createBy Mernake
	 */
	//	private String testplayauthstr = "{\"code\":\"60000-2\",\"data\":[{\"endtime\":\"20180618154638ð20991231235959\",\"point\":\"0\",\"starttime\":\"20180518154638\",\"price\":\"2000\",\"authfalg\":\"1\",\"continueable\":\"1\",\"productid\":\"LW001\",\"name\":\"海看大片VIP包月\",\"producttype\":1,\"serviceid\":\"smgpack0000000000000000000000281\"},{\"endtime\":\"20180618154638ð20991231235959\",\"point\":\"0\",\"starttime\":\"20180518154638\",\"price\":\"200\",\"authfalg\":\"1\",\"continueable\":\"1\",\"productid\":\"LW002\",\"name\":\"海看大片VIP包季\",\"producttype\":1,\"serviceid\":\"smgpack0000000000000000000000281\"},{\"endtime\":\"20180618154638ð20991231235959\",\"point\":\"0\",\"starttime\":\"20180518154638\",\"price\":\"10\",\"authfalg\":\"1\",\"continueable\":\"1\",\"productid\":\"LW003\",\"name\":\"海看大片VIP包年\",\"producttype\":1,\"serviceid\":\"smgpack0000000000000000000000281\"}],\"description\":\"订购\",\"endTime\":\"2018-07-14 13:52:59.423\",\"msg\":\"order\",\"startTime\":\"2018-07-14 13:52:58.822\",\"state\":\"success\"}";
	//	private String testplayauthstr = "{\"code\":\"60000-2\",\"data\":[{\"endtime\":\"20180618154638ð20991231235959\",\"point\":\"0\",\"starttime\":\"20180518154638\",\"price\":\"2000\",\"authfalg\":\"1\",\"continueable\":\"1\",\"productid\":\"LW001\",\"name\":\"海看大片VIP包月\",\"producttype\":\"1\",\"serviceid\":\"smgpack0000000000000000000000281\"},{\"endtime\":\"20180618154638ð20991231235959\",\"point\":\"0\",\"starttime\":\"20180518154638\",\"price\":\"200\",\"authfalg\":\"1\",\"continueable\":\"1\",\"productid\":\"LW002\",\"name\":\"海看大片VIP包季\",\"producttype\":\"1\",\"serviceid\":\"smgpack0000000000000000000000281\"},{\"endtime\":\"20180618154638ð20991231235959\",\"point\":\"0\",\"starttime\":\"20180518154638\",\"price\":\"10\",\"authfalg\":\"1\",\"continueable\":\"1\",\"productid\":\"LW003\",\"name\":\"海看大片VIP包年\",\"producttype\":\"1\",\"serviceid\":\"smgpack0000000000000000000000281\"}],\"description\":\"订购\",\"endTime\":\"2018-07-14 13:52:59.423\",\"msg\":\"order\",\"startTime\":\"2018-07-14 13:52:58.822\",\"state\":\"success\"}";
	//	private String testplayauthstr1 = "{\"code\":\"60000-1\",\"data\":[],\"description\":\"播放\",\"endTime\":\"2018-08-09 10:22:38.083\",\"msg\":\"play\",\"startTime\":\"2018-08-09 10:22:37.608\",\"state\":\"success\"}";
	public void playAuth(final Context context, final String type, final String tag, final String programID, final String categoryID, String superCid, String userId, String userToken, String epgUrlAidl, String usertokenAIDL, final IUpdateData iUpdateData)
	{
		if (isGDOrderEnable)
		{
			showLog("广电playAuth接口：type："+type);
			showLog("广电playAuth接口：tag："+tag);
			showLog("广电playAuth接口：programID："+programID);
			showLog("广电playAuth接口：categoryID："+categoryID);
			showLog("广电playAuth接口：superCid："+superCid);
			showLog("广电playAuth接口：userId："+userId);
			showLog("广电playAuth接口：userToken："+userToken);
			showLog("广电playAuth接口：epgUrlAidl："+epgUrlAidl);
			showLog("广电playAuth接口：usertokenAIDL："+usertokenAIDL);

			SDKMethods.playAuth(type, tag, programID, categoryID, superCid, userId, userToken, epgUrlAidl, usertokenAIDL, new SDKMethods.RequestCallback()
			{
				@Override
				public void onGetData(String str)
				{
					showLog("广电playAuth接口："+str);
					if (tag.equals("1"))  //移动华为平台
					{
						GDOrderPlayAuthCMHWRes res = new Gson().fromJson(str, GDOrderPlayAuthCMHWRes.class);
						showLog("广电playAuth接口：res："+res.toString());
						if (res.getCode().contains("51000"))//code含有51000只有两种情况51000-1（播放）和51000-2（订购）两种状态
						{
							playAuthList.put(programID,res);
							if ("51000-1".equals(res.getCode())) {//只在51000-1能够播放的情况下，才保存到缓存
								ZZFileTools.getInstance().saveZZData(context,programID,res);
							}
							if (iUpdateData!=null)iUpdateData.updateData(METHOD_ORDER_PLAYAUTH, "", res, true);
						}else{
							//51041-506和51041-507的情况下，都是要有产品列表返回的，因为这个时候，需要再次弹出订购页面。需要拿着这个产品ID，继续去订购
							if (("51041-506".equals(res.getCode()) || "51041-507".equals(res.getCode())) && res.getData() != null) {
								LogUtils.i("xumin", "res.getCode(): " + res.getCode() + "  res.getData(): " + res.getData().toString());
								playAuthList.put(programID,res);
							}
							if (iUpdateData!=null)iUpdateData.updateData(METHOD_ORDER_PLAYAUTH, "", res, false);
						}
					}
				}
			});
		}
	}

}
