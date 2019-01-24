package com.xike.xkliveplay.framework.http;

import java.util.Calendar;
import java.util.Random;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.os.Build;
import android.os.SystemProperties;
import android.util.Log;

import com.xike.xkliveplay.framework.entity.ActivateTerminal;
import com.xike.xkliveplay.framework.entity.Auth;
import com.xike.xkliveplay.framework.entity.Authentication;
import com.xike.xkliveplay.framework.entity.Authenticator;
import com.xike.xkliveplay.framework.entity.ContentChannel;
import com.xike.xkliveplay.framework.entity.FavoriteManage;
import com.xike.xkliveplay.framework.entity.GetAccountInfo;
import com.xike.xkliveplay.framework.entity.GetCategoryList;
import com.xike.xkliveplay.framework.entity.GetContentList;
import com.xike.xkliveplay.framework.entity.GetFavoriteList;
import com.xike.xkliveplay.framework.entity.GetScheduleList;
import com.xike.xkliveplay.framework.entity.LiveUpgrageRequest;
import com.xike.xkliveplay.framework.entity.ServiceAuth;
import com.xike.xkliveplay.framework.httpclient.HttpActivateTerminal;
import com.xike.xkliveplay.framework.httpclient.HttpAuth;
import com.xike.xkliveplay.framework.httpclient.HttpAuthentication;
import com.xike.xkliveplay.framework.httpclient.HttpFavoriteManage;
import com.xike.xkliveplay.framework.httpclient.HttpGetAccountInfo;
import com.xike.xkliveplay.framework.httpclient.HttpGetCategoryList;
import com.xike.xkliveplay.framework.httpclient.HttpGetContentList;
import com.xike.xkliveplay.framework.httpclient.HttpGetFavoriteList;
import com.xike.xkliveplay.framework.httpclient.HttpGetScheduleList;
import com.xike.xkliveplay.framework.httpclient.HttpGetUpdate;
import com.xike.xkliveplay.framework.httpclient.HttpServiceAuth;
import com.xike.xkliveplay.framework.tools.DateUtil;
import com.xike.xkliveplay.framework.tools.GetStbinfo;
import com.xike.xkliveplay.framework.tools.LogUtil;
import com.xike.xkliveplay.framework.tools.ThreeDes;
import com.xike.xkliveplay.framework.varparams.Var;


/**
 * @author LiWei <br>
 * CreateTime: 2014年11月12日 上午10:06:28<br>
 * <b>Info:</b><br>
 *<br>
 *   <b>Method:</b> <br>
 */
public class HttpUtil 
{
	private static final String tag = HttpUtil.class.getSimpleName();
	
	private static final String AUTH_A_ACTION = "Login";
	
	public static final String CATEGORY_ROOT_ID = "0";
	
	private static HttpUtil httpUtil = null;
	
	public static HttpUtil create()
	{
		if (httpUtil == null) 
		{
			httpUtil = new HttpUtil();
		}
		return httpUtil;
	}
	
	/**
	 * 
	 * function:	发送检测升级请求 
	 * @param
	 * @return
	 */
	
	public void postUpdate(IUpdateData iUpdateData,Context context)
	{
		LiveUpgrageRequest getUpdateReq = new LiveUpgrageRequest();
		String operator = SystemProperties.get("ro.build.operator");
		String hard = SystemProperties.get("ro.build.hard");
		String equipment = SystemProperties.get("ro.build.equipment");
		LogUtil.e(tag, "getUpdateVaram:","operator= " + operator + " hard= " + hard + " equipment= " + equipment);
		getUpdateReq.setMac(GetStbinfo.getLocalMacAddress().trim());
		getUpdateReq.setEquipment(equipment);
		getUpdateReq.setHard(hard);
		getUpdateReq.setOperators(operator);
		getUpdateReq.setVersion(GetStbinfo.getStrVersion(context));
		HttpGetUpdate httpGetUpdate = new HttpGetUpdate(getUpdateReq, iUpdateData);
		httpGetUpdate.post();
	}
	
	/**
	 * 
	 * function: 激活接口 
	 * @param
	 * @return
	 */
	public void postActiveTerminal(IUpdateData iUpdateData,String data[])
	{
		//解析得到的结果，并同步给西维尔平台；
		ActivateTerminal activate_req = new ActivateTerminal();
		activate_req.setUserId(data[0]);
		activate_req.setMac(data[1]);
		HttpActivateTerminal httpActivateTerminal = new HttpActivateTerminal(activate_req, iUpdateData);
		httpActivateTerminal.post();
	}
	
	/**
	 * 
	 * function: 发送MAC反查userID的请求 
	 * @param
	 * @return
	 */
	public void postGetAccountInfo(IUpdateData iUpdateData,String mac,Context context)
	{
		GetAccountInfo req = new GetAccountInfo();
		if (mac == null) 
		{
			return;
		}
		req.setMac(mac);
		HttpGetAccountInfo httpGetAccountInfo = new HttpGetAccountInfo(req,iUpdateData);
		httpGetAccountInfo.post(context);
	}
	
	/**
	 * 
	 * function: 用户认证A接口 
	 * @param
	 * @return
	 */
	public void postAuthentication(IUpdateData iUpdateData,String userId) 
	{
		Authentication req = new Authentication();
		req.setUserId(userId);
		req.setAction(AUTH_A_ACTION);
		HttpAuthentication httpAuthentication = new HttpAuthentication(req,iUpdateData);
		httpAuthentication.post();
	}
	
	/**
	 * 
	 * function:用户认证B接口 
	 * @param
	 * @return
	 */
	@SuppressWarnings("static-access")
	public void postAuth(IUpdateData iUpdateData,String mac,String encryToken,String ip,String userId,String userPassWord)
	{
		Auth authReq = new Auth();
		authReq.setUserId(userId);
		Authenticator authenticator = new Authenticator();
		authenticator.setEncryToken(encryToken);
		authenticator.setMac(mac);
		authenticator.setUserId(userId);
		authenticator.setReserved("0");
		authenticator.setIp(ip);
		authenticator.setRandom(Integer.toHexString(new Random().nextInt(9999999)));
		Build bd = new Build();
		authenticator.setTerminalId(bd.MODEL);
		//获取设备终端型号
//		authenticator.setTerminalId(GetStbinfo.getSTBINFO("SDGD_PRODUCTMODEL"));
		LogUtil.e(tag, "updateData", "Authenticator = " + authenticator.toString());
		String data = getAuthenticatorStr(authenticator,userPassWord,userId,encryToken);
		authReq.setAuthenticator(data);
		LogUtil.e(tag, "updateData", "authReq String is: " + authReq.toString());
		HttpAuth httpAuth = new HttpAuth(authReq, iUpdateData);
		httpAuth.post();
	}
	
	/**拼接成Authenticator字符串**/
	private String getAuthenticatorStr(Authenticator authenticator,String userPassWord,String userId,String encryToken) 
	{
		Log.e(tag, "userPassword = " + userPassWord);
		int lenth = userPassWord.length();
		for (int i = 0; i < 24 - lenth; i++) 
		{
			userPassWord = userPassWord + "0";
		}
		byte[] keybyte = userPassWord.getBytes();
		String authStr = authenticator.getRandom() + "$" + encryToken + "$" + userId
				 +"$" + authenticator.getTerminalId() + "$" + authenticator.getIp() + "$" + authenticator.getMac()
				 + "$" + authenticator.getReserved() + "$" + "CTC";
		LogUtil.e(tag, "updateData", "clear text is: " + authStr);
		byte[] source = authStr.getBytes();
		byte[] data3 = ThreeDes.ecrypt(keybyte, source);
		String data = "";
		try 
		{
			data = ThreeDes.byte2Hex(data3);
		} catch (Exception e) 
		{
			e.printStackTrace();
		}
		LogUtil.e(tag, "getAuthenticatorStr","ciphertext is: " + data);
		return data;
	}
	
//	/**
//	 * 
//	 * function:获取产品id，下次接口结构调整后会取消 
//	 * @param
//	 * @return
//	 */
//	public void postGetProductId(IUpdateData iUpdateData,Context context)
//	{
//		String packageName = "";
//		try 
//		{
//			packageName =  context.getPackageManager().getPackageInfo(context.getPackageName(), 0).packageName;
//		} catch (NameNotFoundException e1) 
//		{
//			e1.printStackTrace();
//			if (isLiveStarted(context)) 
//			{
////				DialogError.getInstance().showDialog(context, ErrorCode.ERROR_GET_PRODUCT_ID_EXCEPTION);
//			}
//		}
//		
//		LogUtil.e(tag,"updateData", "package name is: " + packageName);
//		HttpGetProductId httpGetProductId = new HttpGetProductId(iUpdateData,packageName);
//		httpGetProductId.post();
//	}
	
	/**
	 * 
	 * function: 获取业务鉴权信息 
	 * @param
	 * @return
	 */
	public void postServiceAuth(IUpdateData iUpdateData,String productId,String userId,String userToken) 
	{
		ServiceAuth serviceAuthReq = new ServiceAuth();
		serviceAuthReq.setContentID("");
		serviceAuthReq.setProductId(productId);
		serviceAuthReq.setUserId(userId);
		serviceAuthReq.setUserToken(userToken);
		HttpServiceAuth httpServiceAuth = new HttpServiceAuth(serviceAuthReq,iUpdateData);
		httpServiceAuth.post();
	}
	
	/**
	 * 
	 * function: 请求类别 
	 * @param
	 * @return
	 */
	public void postGetCategoryList(IUpdateData iUpdateData,String id) 
	{
		HttpGetCategoryList categoryList = new HttpGetCategoryList(iUpdateData);
		GetCategoryList req = new GetCategoryList();
		req.setParentCategoryId(id);
		LogUtil.e(tag,"postGetCategoryList", "id = " + id);
		req.setUserId(Var.userId);
		req.setUserToken(Var.userToken);
		categoryList.setReq(req);
		categoryList.post();
	}
	
	/**
	 * 
	 * function: 请求频道
	 * @param
	 * @return
	 */
	public void postGetContentList(IUpdateData iUpdateData,String categoryId) 
	{
		HttpGetContentList hContentList = new HttpGetContentList(iUpdateData);
		GetContentList req = new GetContentList();
		req.setCategoryId(categoryId);
		req.setCount("500");
		req.setOffset("0");
		req.setUserId(Var.userId);
		req.setUserToken(Var.userToken);
		hContentList.setReq(req);
		hContentList.post();
	}
	
	/**
	 * 
	 * function: 获取收藏列表 
	 * @param
	 * @return
	 */
	public void postGetFavoriteList(IUpdateData iUpdateData) 
	{
		HttpGetFavoriteList favoriteList = new HttpGetFavoriteList(iUpdateData);

		GetFavoriteList req = new GetFavoriteList();
		req.setCount("100");
		req.setOffset("0");
		req.setFavType("2");
		req.setUserId(Var.userId);
		req.setUserToken(Var.userToken);
		favoriteList.setReq(req);
		favoriteList.post();
	}
	
	public void postFavoriteManage(IUpdateData iUpdateData,String opt,ContentChannel curPlayingChannel)
	{
		HttpFavoriteManage manage = new HttpFavoriteManage(iUpdateData);

		FavoriteManage req = new FavoriteManage();
		req.setAction(opt);
		req.setContentId(curPlayingChannel.getContentId());
		req.setUserId(Var.userId);
		req.setUserToken(Var.userToken);
		req.setContentType("2");
		req.setSeriesContentIndex("");
		req.setSeriesContentID("");
		req.setCategoryID(curPlayingChannel.getLanguage());
		req.setContentProvider("");

		manage.setReq(req);
		manage.post();
	}
	
	/**
	 * 
	 * function:获取节目单接口 
	 * @param
	 * @return
	 */
	public void postNowNextScheduleList(IUpdateData iUpdateData,String channelId) 
	{
		HttpGetScheduleList scheduleList = new HttpGetScheduleList(iUpdateData);
		GetScheduleList req = new GetScheduleList();
		req.setCount("2");
		req.setOffset("0");
		req.setChannelIds(channelId);
		req.setStartDateTime(DateUtil.getDateStr4(Calendar.getInstance()));
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.HOUR_OF_DAY, 4);
		req.setEndDateTime(DateUtil.getDateStr4(calendar));

		req.setUserId(Var.userId);
		req.setUserToken(Var.userToken);
		scheduleList.setReq(req);
		scheduleList.post();
	}
	
	
	/**请求新的频道列表**/
	public void postGetAllNewChannelSchedule(IUpdateData iUpdateData,String contentId) 
	{
		LogUtil.e(tag, "requestNewChannelSchedule","requestNewChannelSchedule");
		GetScheduleList req = new GetScheduleList();
		req.setChannelIds(contentId);
		req.setCount("200");
		req.setUserId(Var.userId);
		req.setUserToken(Var.userToken);
		String[] str_time = DateUtil.getToday();
		req.setStartDateTime(str_time[0]);
		req.setEndDateTime(str_time[1]);
		req.setOffset("0");
		HttpGetScheduleList httpGetScheduleList = new HttpGetScheduleList(iUpdateData,
				req);
		httpGetScheduleList.post();
	}
	
	/**
	 * 
	 * function: 判断直播是否已经启动 
	 * @param
	 * @return
	 */
	@SuppressWarnings("unused")
	private boolean isLiveStarted(Context context)
	{
		String processName = "com.xike.xkliveplay";
		ActivityManager am = (ActivityManager) context.getSystemService(Activity.ACTIVITY_SERVICE);
		// The first in the list of RunningTasks is always the foreground task.
		RunningTaskInfo foregroundTaskInfo = am.getRunningTasks(1).get(0);
		if(foregroundTaskInfo.baseActivity.getPackageName().equals(processName))
		{
			return false;
		}
		
		return true;
	}
}
