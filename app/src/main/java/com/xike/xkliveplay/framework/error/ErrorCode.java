package com.xike.xkliveplay.framework.error;

import java.util.HashMap;
import java.util.Map;


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
	public static final int ERROR_GET_USERID_EXCEPTION = 51004;
	
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
	public static final int ERROR_CACHE_DB_EXCEPTION = 51008;
	
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
	public static final int ERROR_COMPARE_APK_VERSION_EXCEPTION = 51013;
	
	/**
	 * 直播apk下载失败
	 */
	public static final int ERROR_APK_DOWNLOAD_EXCEPTION = 51014;
	
	/**
	 * 直播apk安装失败
	 */
	public static final int ERROR_APK_UPDATE_EXCEPTION = 51015;
	
	/**
	 * 收藏列表获取失败
	 */
	public static final int ERROR_GET_SAVE_LIST_EXCEPTION = 51016;
	
	/**
	 * 节目单列表获取失败
	 */
	public static final int ERROR_GET_SCHEDULE_LIST_EXCEPTION = 51017;
	
	/**
	 *账号同步失败 
	 */
	public static final int ERROR_ACCOUNT_SYNCHRONOUS = 51018;
	
	/**
	 * 激活异常
	 */
	public static final int ERROR_ACTIVE_ACOUNT = 51019;
	
	
	
	/**
	 * 其他未知错误
	 */
	public static final int ERROR_UNKNOWN_EXCEPTION = 51050;
	
	public static final String ERROR_INTERNET_TEXT = "网络异常";
	public static final String ERROR_AUTH_PLUG_IN_TEXT = "调用鉴权插件异常";
	public static final String ERROR_READ_MAC_TEXT = "Mac读取失败";
	
	public static final String ERROR_GET_USERID_TEXT = "UserId获取失败";
	public static final String ERROR_GET_ROOT_CATEGORY_ID_TEXT = "直播ID获取失败";
	public static final String ERROR_GET_CATEGORY_TEXT = "栏目分类获取失败";
	
	public static final String ERROR_GET_CHANNEL_LIST_TEXT = "频道列表获取失败";
	public static final String ERROR_CACHE_DB_TEXT = "频道列表缓存失败";
	public static final String ERROR_GET_PRODUCT_ID_TEXT = "获取产品信息失败";
	
	public static final String ERROR_NOT_BUY_TEXT = "未订购产品";
	public static final String ERROR_AUTH_EPG_TEXT = "业务鉴权失败";
	public static final String ERROR_STB_ILLEGAL_TEXT = "认证失败，终端不合法";
	
	public static final String ERROR_COMPARE_APK_VERSION_TEXT = "直播apk升级检测失败或服务不可达";
	public static final String ERROR_APK_DOWNLOAD_TEXT = "直播apk下载失败";
	public static final String ERROR_APK_UPDATE_TEXT = "直播apk升级失败";
	
	public static final String ERROR_GET_SAVE_LIST_TEXT = "收藏列表获取失败";
	public static final String ERROR_GET_SCHEDULE_LIST_TEXT = "节目单列表获取失败";
	public static final String ERROR_ACCOUNT_SYNCHRONOUS_TEXT = "账号未同步完成";
	
	public static final String ERROR_ACTIVE_ACOUNT_TEXT = "激活异常";
	
	public static final String ERROR_UNKNOWN_TEXT = "其他未知错误";
	
	@SuppressLint("UseSparseArrays")
	public static Map<Integer, String> errorMap = new HashMap<Integer, String>();
	
	
	static{
		errorMap.put(ERROR_INTERNET_EXCEPTION, ERROR_INTERNET_TEXT);
		errorMap.put(ERROR_AUTH_PLUG_IN_EXCEPTION, ERROR_AUTH_PLUG_IN_TEXT);
		errorMap.put(ERROR_READ_MAC_EXCEPTION, ERROR_READ_MAC_TEXT);
		
		errorMap.put(ERROR_GET_USERID_EXCEPTION, ERROR_GET_USERID_TEXT);
		errorMap.put(ERROR_GET_ROOT_CATEGORY_ID_EXCEPTION, ERROR_GET_ROOT_CATEGORY_ID_TEXT);
		errorMap.put(ERROR_GET_CATEGORY_EXCEPTION, ERROR_GET_CATEGORY_TEXT);
		
		errorMap.put(ERROR_GET_CHANNEL_LIST_EXCEPTION, ERROR_GET_CHANNEL_LIST_TEXT);
		errorMap.put(ERROR_CACHE_DB_EXCEPTION, ERROR_CACHE_DB_TEXT);
		errorMap.put(ERROR_GET_PRODUCT_ID_EXCEPTION, ERROR_GET_PRODUCT_ID_TEXT);
		
		errorMap.put(ERROR_NOT_BUY_EXCEPTION, ERROR_NOT_BUY_TEXT);
		errorMap.put(ERROR_AUTH_EPG_EXCEPTION, ERROR_AUTH_EPG_TEXT);
		errorMap.put(ERROR_STB_ILLEGAL_EXCEPTION, ERROR_STB_ILLEGAL_TEXT);
		
		errorMap.put(ERROR_COMPARE_APK_VERSION_EXCEPTION, ERROR_COMPARE_APK_VERSION_TEXT);
		errorMap.put(ERROR_APK_DOWNLOAD_EXCEPTION, ERROR_APK_DOWNLOAD_TEXT);
		errorMap.put(ERROR_APK_UPDATE_EXCEPTION, ERROR_APK_UPDATE_TEXT);
		
		errorMap.put(ERROR_GET_SAVE_LIST_EXCEPTION, ERROR_GET_SAVE_LIST_TEXT);
		errorMap.put(ERROR_GET_SCHEDULE_LIST_EXCEPTION, ERROR_GET_SCHEDULE_LIST_TEXT);
		
//		errorMap.put(ERROR_APK_UPDATE_EXCEPTION, ERROR_APK_UPDATE_TEXT);
		errorMap.put(ERROR_UNKNOWN_EXCEPTION, ERROR_UNKNOWN_TEXT);
		errorMap.put(ERROR_ACCOUNT_SYNCHRONOUS, ERROR_ACCOUNT_SYNCHRONOUS_TEXT);
		errorMap.put(ERROR_ACTIVE_ACOUNT, ERROR_ACTIVE_ACOUNT_TEXT);
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
		
		errorMap.put(ERROR_GET_USERID_EXCEPTION, ERROR_GET_USERID_TEXT);
		errorMap.put(ERROR_GET_ROOT_CATEGORY_ID_EXCEPTION, ERROR_GET_ROOT_CATEGORY_ID_TEXT);
		errorMap.put(ERROR_GET_CATEGORY_EXCEPTION, ERROR_GET_CATEGORY_TEXT);
		
		errorMap.put(ERROR_GET_CHANNEL_LIST_EXCEPTION, ERROR_GET_CHANNEL_LIST_TEXT);
		errorMap.put(ERROR_CACHE_DB_EXCEPTION, ERROR_CACHE_DB_TEXT);
		errorMap.put(ERROR_GET_PRODUCT_ID_EXCEPTION, ERROR_GET_PRODUCT_ID_TEXT);
		
		errorMap.put(ERROR_NOT_BUY_EXCEPTION, ERROR_NOT_BUY_TEXT);
		errorMap.put(ERROR_AUTH_EPG_EXCEPTION, ERROR_AUTH_EPG_TEXT);
		errorMap.put(ERROR_STB_ILLEGAL_EXCEPTION, ERROR_STB_ILLEGAL_TEXT);
		
		errorMap.put(ERROR_COMPARE_APK_VERSION_EXCEPTION, ERROR_COMPARE_APK_VERSION_TEXT);
		errorMap.put(ERROR_APK_DOWNLOAD_EXCEPTION, ERROR_APK_DOWNLOAD_TEXT);
		errorMap.put(ERROR_APK_UPDATE_EXCEPTION, ERROR_APK_UPDATE_TEXT);
		
		errorMap.put(ERROR_GET_SAVE_LIST_EXCEPTION, ERROR_GET_SAVE_LIST_TEXT);
		errorMap.put(ERROR_GET_SCHEDULE_LIST_EXCEPTION, ERROR_GET_SCHEDULE_LIST_TEXT);
		
//		errorMap.put(ERROR_APK_UPDATE_EXCEPTION, ERROR_APK_UPDATE_TEXT);
		errorMap.put(ERROR_UNKNOWN_EXCEPTION, ERROR_UNKNOWN_TEXT);
		errorMap.put(ERROR_ACCOUNT_SYNCHRONOUS, ERROR_ACCOUNT_SYNCHRONOUS_TEXT);
		errorMap.put(ERROR_ACTIVE_ACOUNT, ERROR_ACTIVE_ACOUNT_TEXT);
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
	
}
