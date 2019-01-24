package com.xike.xkliveplay.activity.dialogerror;

import java.util.HashMap;
import java.util.Map;

import com.xike.xkliveplay.framework.error.ErrorBroadcastAction;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.Toast;

/**
 * @author LiWei <br>
 * CreateTime: 2014-7-11下午12:11:47<br>
 * info: 错误代码
 * 
 */
public class ErrorCode 
{
	/**
	 * 无网络/网络连接失败/网络超时/服务器拒绝连接
	 */
	public static final int ERROR_INTERNET_EXCEPTION = 51001;
	
	/**
	 * 调用认证鉴权插件时，系统返回-1001异常/读取launcher返回的STBID和CLIENTID失败
	 */
	public static final int ERROR_AUTH_PLUG_IN_EXCEPTION = 51002;
	
	/**
	 * 读取/sys/class/net/eth0/address中的MAC地址失败
	 */
	public static final int ERROR_READ_MAC_EXCEPTION = 51003;
	
	
	/**
	 * userId获取失败：mac地址获取后获取相应的账号信息失败
	 */
//	public static final int ERROR_GET_USERID_EXCEPTION = 51004;
	
	/**
	 * 直播id获取失败：直播apk需要获得“直播”根栏目ID
	 */
	public static final int ERROR_GET_ROOT_CATEGORY_ID_EXCEPTION = 51005;
	
	/**
	 * 栏目分类获取失败：直播apk需要获得各个栏目分类信息
	 */
	public static final int ERROR_GET_CATEGORY_EXCEPTION = 51006;
	
	/**
	 * 频道列表获取失败：直播apk需要获得全部频道列表
	 */
	public static final int ERROR_GET_CHANNEL_LIST_EXCEPTION = 51007;
	
	/**
	 * 频道列表缓存失败：获得到的频道列表要插入数据库做缓存
	 */
//	public static final int ERROR_CACHE_DB_EXCEPTION = 51008;
	
	/**
	 * 获取产品信息失败：获取productID失败
	 */
	public static final int ERROR_GET_PRODUCT_ID_EXCEPTION = 51009; 
	
	
	
	/**
	 * 用户没有订购相关产品
	 */
	public static final int ERROR_NOT_BUY_EXCEPTION = 51010;
	
	/**
	 * epg向AAA请求鉴权失败
	 */
	public static final int ERROR_AUTH_EPG_EXCEPTION = 51011;
	
	/**
	 * 终端账号没有注册或无效
	 */
	public static final int ERROR_STB_ILLEGAL_EXCEPTION = 51012;
	
	/**
	 * 直播apk后台比较版本失败/直播apk下载失败/直播apk安装失败
	 */
//	public static final int ERROR_APK_UPDATE_EXCEPTION = 108;
	
	/**
	 * 直播apk后台比较版本失败
	 */
//	public static final int ERROR_COMPARE_APK_VERSION_EXCEPTION = 51013;
	
	/**
	 * 直播apk下载失败
	 */
//	public static final int ERROR_APK_DOWNLOAD_EXCEPTION = 51014;
	
	/**
	 * 直播apk安装失败
	 */
//	public static final int ERROR_APK_UPDATE_EXCEPTION = 51015;
	
	/**
	 * 收藏列表获取失败
	 */
	public static final int ERROR_GET_SAVE_LIST_EXCEPTION = 51016;
	
	/**
	 * 节目单列表获取失败
	 */
//	public static final int ERROR_GET_SCHEDULE_LIST_EXCEPTION = 51017;
	
	/**
	 *账号同步失败 
	 */
	public static final int ERROR_ACCOUNT_SYNCHRONOUS = 51018;
	
	/**
	 * 激活异常
	 */
	public static final int ERROR_ACTIVE_ACOUNT = 51019;
	
	/**
	 * 写stbinfo文件失败
	 */
	public static final int ERROR_WRITE_STBINFO_EXCEPTION = 51020;
	
	/**
	 * 域名解析失败
	 */
	public static final int ERROR_DNS_EXCEPTION = 51021;
	
	/**
	 * 服务器拒绝连接
	 */
	public static final int ERROR_CONNECT_REFUSED = 51022;
	
	/**
	 * 服务器连接超时
	 */
	public static final int ERROR_CONNECT_TIMEOUT = 51023;
	
	
	/**
	 * 访问接口时，返回500错误码
	 */
	public static final int ERROR_UNKNOWN_EXCEPTION = 51050;
	
	public static final String ERROR_INTERNET_TEXT = "网络连接失败，请检查机顶盒\n网络连接是否正常。如确认网\n络连接正常请联系10086。";
	public static final String ERROR_AUTH_PLUG_IN_TEXT = "请重启机顶盒再试一次。\n如问题未解决请联系10086。";
	public static final String ERROR_READ_MAC_TEXT = "请联系10086。";
	
	public static final String ERROR_GET_USERID_TEXT = "UserId获取失败。";
	public static final String ERROR_GET_ROOT_CATEGORY_ID_TEXT = "请尝试重新从主页进入频道，\n或重启机顶盒再试一次。\n如问题未解决请联系10086。";
	public static final String ERROR_GET_CATEGORY_TEXT = "请尝试重新从主页进入频道，\n或重启机顶盒再试一次。\n如问题未解决请联系10086。";
	
	public static final String ERROR_GET_CHANNEL_LIST_TEXT = "请尝试重新从主页进入频道，\n或重启机顶盒再试一次。\n如问题未解决请联系10086。";
	public static final String ERROR_CACHE_DB_TEXT = "频道列表缓存失败。";
	public static final String ERROR_GET_PRODUCT_ID_TEXT = "请尝试重新从主页进入频道，\n或重启机顶盒再试一次。\n如问题未解决请联系10086。";
	
	public static final String ERROR_NOT_BUY_TEXT = "此频道未订购，请进行订购。\n如已订购请联系10086。";
	public static final String ERROR_AUTH_EPG_TEXT = "请尝试重新从主页进入频道，\n或重启机顶盒再试一次。\n如问题未解决请联系10086。";
	public static final String ERROR_STB_ILLEGAL_TEXT = "请联系10086。";
	
	public static final String ERROR_COMPARE_APK_VERSION_TEXT = "直播apk升级检测失败或服务不可达。";
	public static final String ERROR_APK_DOWNLOAD_TEXT = "直播apk下载失败。";
	public static final String ERROR_APK_UPDATE_TEXT = "直播apk升级失败。";
	
	public static final String ERROR_GET_SAVE_LIST_TEXT = "请尝试重新从主页进入频道，\n如问题未解决请联系10086。";
	public static final String ERROR_GET_SCHEDULE_LIST_TEXT = "节目单列表获取失败。";
	public static final String ERROR_ACCOUNT_SYNCHRONOUS_TEXT = "账号未同步完成，请联系10086。";
	
	public static final String ERROR_ACTIVE_ACOUNT_TEXT = "请恢复出厂设置并重新登录，\n如问题未解决请联系10086。";
	public static final String ERROR_WRITE_STBINFO_TEXT = "用户信息缓存失败，请联系10086。";
	public static final String ERROR_DNS_TEXT = "域名解析异常，请重启光猫、\n路由器。如问题未解决请联系\n10086。";
	public static final String ERROR_CONNECT_REFUSED_TEXT = "服务器拒绝连接，请检查网络。\n如问题未解决请联系10086。";
	public static final String ERROR_CONNECT_TIMEOUT_TEXT = "服务器连接超时，请检查网络。\n如问题未解决请联系10086。";
	
	
	public static final String ERROR_UNKNOWN_TEXT = "请重启光猫、路由器及机顶盒。\n如问题未解决请联系10086。";
	
	@SuppressLint("UseSparseArrays")
	public static Map<Integer, String> errorMap = new HashMap<Integer, String>();
	
	
	static{
		errorMap.put(ERROR_INTERNET_EXCEPTION, ERROR_INTERNET_TEXT);
		errorMap.put(ERROR_AUTH_PLUG_IN_EXCEPTION, ERROR_AUTH_PLUG_IN_TEXT);
		errorMap.put(ERROR_READ_MAC_EXCEPTION, ERROR_READ_MAC_TEXT);
		
//		errorMap.put(ERROR_GET_USERID_EXCEPTION, ERROR_GET_USERID_TEXT);
		errorMap.put(ERROR_GET_ROOT_CATEGORY_ID_EXCEPTION, ERROR_GET_ROOT_CATEGORY_ID_TEXT);
		errorMap.put(ERROR_GET_CATEGORY_EXCEPTION, ERROR_GET_CATEGORY_TEXT);
		
		errorMap.put(ERROR_GET_CHANNEL_LIST_EXCEPTION, ERROR_GET_CHANNEL_LIST_TEXT);
//		errorMap.put(ERROR_CACHE_DB_EXCEPTION, ERROR_CACHE_DB_TEXT);
		errorMap.put(ERROR_GET_PRODUCT_ID_EXCEPTION, ERROR_GET_PRODUCT_ID_TEXT);
		
		errorMap.put(ERROR_NOT_BUY_EXCEPTION, ERROR_NOT_BUY_TEXT);
		errorMap.put(ERROR_AUTH_EPG_EXCEPTION, ERROR_AUTH_EPG_TEXT);
		errorMap.put(ERROR_STB_ILLEGAL_EXCEPTION, ERROR_STB_ILLEGAL_TEXT);
		
//		errorMap.put(ERROR_COMPARE_APK_VERSION_EXCEPTION, ERROR_COMPARE_APK_VERSION_TEXT);
//		errorMap.put(ERROR_APK_DOWNLOAD_EXCEPTION, ERROR_APK_DOWNLOAD_TEXT);
//		errorMap.put(ERROR_APK_UPDATE_EXCEPTION, ERROR_APK_UPDATE_TEXT);
		
		errorMap.put(ERROR_GET_SAVE_LIST_EXCEPTION, ERROR_GET_SAVE_LIST_TEXT);
//		errorMap.put(ERROR_GET_SCHEDULE_LIST_EXCEPTION, ERROR_GET_SCHEDULE_LIST_TEXT);
		
//		errorMap.put(ERROR_APK_UPDATE_EXCEPTION, ERROR_APK_UPDATE_TEXT);
		errorMap.put(ERROR_UNKNOWN_EXCEPTION, ERROR_UNKNOWN_TEXT);
		errorMap.put(ERROR_ACCOUNT_SYNCHRONOUS, ERROR_ACCOUNT_SYNCHRONOUS_TEXT);
		errorMap.put(ERROR_ACTIVE_ACOUNT, ERROR_ACTIVE_ACOUNT_TEXT);
		
		errorMap.put(ERROR_WRITE_STBINFO_EXCEPTION, ERROR_WRITE_STBINFO_TEXT);
		errorMap.put(ERROR_DNS_EXCEPTION, ERROR_DNS_TEXT);
		errorMap.put(ERROR_CONNECT_REFUSED, ERROR_CONNECT_REFUSED_TEXT);
		errorMap.put(ERROR_CONNECT_TIMEOUT, ERROR_CONNECT_TIMEOUT_TEXT);
	}
	
	@SuppressLint("UseSparseArrays")
	public static void initMap()
	{
		if (errorMap != null && errorMap.size() != 0) 
		{
			return;
		}else{
			errorMap = new HashMap<Integer, String>();
		}
		
		errorMap.put(ERROR_INTERNET_EXCEPTION, ERROR_INTERNET_TEXT);
		errorMap.put(ERROR_AUTH_PLUG_IN_EXCEPTION, ERROR_AUTH_PLUG_IN_TEXT);
		errorMap.put(ERROR_READ_MAC_EXCEPTION, ERROR_READ_MAC_TEXT);
		
//		errorMap.put(ERROR_GET_USERID_EXCEPTION, ERROR_GET_USERID_TEXT);
		errorMap.put(ERROR_GET_ROOT_CATEGORY_ID_EXCEPTION, ERROR_GET_ROOT_CATEGORY_ID_TEXT);
		errorMap.put(ERROR_GET_CATEGORY_EXCEPTION, ERROR_GET_CATEGORY_TEXT);
		
		errorMap.put(ERROR_GET_CHANNEL_LIST_EXCEPTION, ERROR_GET_CHANNEL_LIST_TEXT);
//		errorMap.put(ERROR_CACHE_DB_EXCEPTION, ERROR_CACHE_DB_TEXT);
		errorMap.put(ERROR_GET_PRODUCT_ID_EXCEPTION, ERROR_GET_PRODUCT_ID_TEXT);
		
		errorMap.put(ERROR_NOT_BUY_EXCEPTION, ERROR_NOT_BUY_TEXT);
		errorMap.put(ERROR_AUTH_EPG_EXCEPTION, ERROR_AUTH_EPG_TEXT);
		errorMap.put(ERROR_STB_ILLEGAL_EXCEPTION, ERROR_STB_ILLEGAL_TEXT);
		
//		errorMap.put(ERROR_COMPARE_APK_VERSION_EXCEPTION, ERROR_COMPARE_APK_VERSION_TEXT);
//		errorMap.put(ERROR_APK_DOWNLOAD_EXCEPTION, ERROR_APK_DOWNLOAD_TEXT);
//		errorMap.put(ERROR_APK_UPDATE_EXCEPTION, ERROR_APK_UPDATE_TEXT);
		
		errorMap.put(ERROR_GET_SAVE_LIST_EXCEPTION, ERROR_GET_SAVE_LIST_TEXT);
//		errorMap.put(ERROR_GET_SCHEDULE_LIST_EXCEPTION, ERROR_GET_SCHEDULE_LIST_TEXT);
		
//		errorMap.put(ERROR_APK_UPDATE_EXCEPTION, ERROR_APK_UPDATE_TEXT);
		errorMap.put(ERROR_UNKNOWN_EXCEPTION, ERROR_UNKNOWN_TEXT);
		errorMap.put(ERROR_ACCOUNT_SYNCHRONOUS, ERROR_ACCOUNT_SYNCHRONOUS_TEXT);
		errorMap.put(ERROR_ACTIVE_ACOUNT, ERROR_ACTIVE_ACOUNT_TEXT);
		
		errorMap.put(ERROR_WRITE_STBINFO_EXCEPTION, ERROR_WRITE_STBINFO_TEXT);
		errorMap.put(ERROR_DNS_EXCEPTION, ERROR_DNS_TEXT);
		errorMap.put(ERROR_CONNECT_REFUSED, ERROR_CONNECT_REFUSED_TEXT);
		errorMap.put(ERROR_CONNECT_TIMEOUT, ERROR_CONNECT_TIMEOUT_TEXT);
	}
	
	
	public static void makeErrorToast(int errorCode,Context context) 
	{
		if (context == null) 
		{
			return;
		}
		if (errorMap == null) 
		{
			initMap();
		}
		
		if(errorMap != null && null != ErrorCode.errorMap.get(errorCode) )
		{
			String str = "错误代码：" + errorCode + "  " + ErrorCode.errorMap.get(errorCode);
			Toast.makeText(context, str, Toast.LENGTH_LONG).show();
		}
	}
	
	public static String[] getErrorCodeMsg(String action)
	{
		String[] res ={"",""};
		if (action.equals(ErrorBroadcastAction.ERROR_INTERNET_ACTION)) 
		{
			res[0] = ERROR_INTERNET_EXCEPTION + "";
			res[1] = errorMap.get(ERROR_INTERNET_EXCEPTION);
		}else if (action.equals(ErrorBroadcastAction.ERROR_AUTH_PLUG_IN_ACTION)) 
		{
			res[0] = ERROR_AUTH_PLUG_IN_EXCEPTION + "";
			res[1] = errorMap.get(ERROR_AUTH_PLUG_IN_EXCEPTION);
		}else if (action.equals(ErrorBroadcastAction.ERROR_READ_MAC_ACTION)) 
		{
			res[0] = ERROR_READ_MAC_EXCEPTION + "";
			res[1] = errorMap.get(ERROR_READ_MAC_EXCEPTION);
		}else if (action.equals(ErrorBroadcastAction.ERROR_GET_ROOT_CATEGORY_ID_ACTION)) 
		{
			res[0] = ERROR_GET_ROOT_CATEGORY_ID_EXCEPTION + "";
			res[1] = errorMap.get(ERROR_GET_ROOT_CATEGORY_ID_EXCEPTION);
		}else if (action.equals(ErrorBroadcastAction.ERROR_GET_CATEGORY_ACTION)) 
		{
			res[0] = ERROR_GET_CATEGORY_EXCEPTION + "";
			res[1] = errorMap.get(ERROR_GET_CATEGORY_EXCEPTION);
		}else if (action.equals(ErrorBroadcastAction.ERROR_GET_CHANNEL_LIST_ACTION)) 
		{
			res[0] = ERROR_GET_CHANNEL_LIST_EXCEPTION + "";
			res[1] = errorMap.get(ERROR_GET_CHANNEL_LIST_EXCEPTION);
		}else if (action.equals(ErrorBroadcastAction.ERROR_AUTH_EPG_ACTION)) 
		{
			res[0] = ERROR_AUTH_EPG_EXCEPTION + "";
			res[1] = errorMap.get(ERROR_AUTH_EPG_EXCEPTION);
		}else if (action.equals(ErrorBroadcastAction.ERROR_STB_ILLEGAL_ACTION)) 
		{
			res[0] = ERROR_STB_ILLEGAL_EXCEPTION + "";
			res[1] = errorMap.get(ERROR_STB_ILLEGAL_EXCEPTION);
		}else if (action.equals(ErrorBroadcastAction.ERROR_GET_SAVE_LIST_ACTION)) 
		{
			res[0] = ERROR_GET_SAVE_LIST_EXCEPTION + "";
			res[1] = errorMap.get(ERROR_GET_SAVE_LIST_EXCEPTION);
		}else if (action.equals(ErrorBroadcastAction.ERROR_ACCOUNT_SYNCHRONOUS_ACTION)) 
		{
			res[0] = ERROR_ACCOUNT_SYNCHRONOUS + "";
			res[1] = errorMap.get(ERROR_ACCOUNT_SYNCHRONOUS);
		}else if (action.equals(ErrorBroadcastAction.ERROR_ACTIVE_ACOUNT_ACTION)) 
		{
			res[0] = ERROR_ACTIVE_ACOUNT + "";
			res[1] = errorMap.get(ERROR_ACTIVE_ACOUNT);
		}else if (action.equals(ErrorBroadcastAction.ERROR_UNKNOWN_ACTION)) 
		{
			res[0] = ERROR_UNKNOWN_EXCEPTION + "";
			res[1] = errorMap.get(ERROR_UNKNOWN_EXCEPTION);
		}else if (action.equals(ErrorBroadcastAction.ERROR_NOT_BUY_ACTION)) 
		{
			res[0] = ERROR_NOT_BUY_EXCEPTION + "";
			res[1] = errorMap.get(ERROR_NOT_BUY_EXCEPTION);
		}else if (action.equals(ErrorBroadcastAction.ERROR_DNS_ACTION)) 
		{
			res[0] = ERROR_DNS_EXCEPTION + "";
			res[1] = errorMap.get(ERROR_DNS_EXCEPTION);
		}else if (action.equals(ErrorBroadcastAction.ERROR_WRITE_STBINFO_ACTION)) 
		{
			res[0] = ERROR_WRITE_STBINFO_EXCEPTION + "";
			res[1] = errorMap.get(ERROR_WRITE_STBINFO_EXCEPTION);
		}else if (action.equals(ErrorBroadcastAction.ERROR_CONNECT_REFUSED)) 
		{
			res[0] = ERROR_CONNECT_REFUSED + "";
			res[1] = errorMap.get(ERROR_CONNECT_REFUSED);
		}else if (action.equals(ErrorBroadcastAction.ERROR_CONNECT_TIMEOUT)) 
		{
			res[0] = ERROR_CONNECT_TIMEOUT + "";
			res[1] = errorMap.get(ERROR_CONNECT_TIMEOUT);
		}
		
		return res;
	}
}
