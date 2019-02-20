package com.xike.xkliveplay.framework.varparams;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager.NameNotFoundException;

import com.xike.xkliveplay.framework.entity.Category;
import com.xike.xkliveplay.framework.entity.ContentChannel;
import com.xike.xkliveplay.framework.httpclient.VarParam;
import com.xike.xkliveplay.framework.tools.GetStbinfo;
import com.xike.xkliveplay.framework.tools.GetVaram;
import com.xike.xkliveplay.framework.tools.LogUtil;
import com.xike.xkliveplay.framework.tools.aPropertiesUtils;
import com.xike.xkliveplay.gd.GDHttpTools;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author LiWei <br>
 * CreateTime: 2014年10月20日 下午2:43:33<br>
 * <b>Info:</b><br>
 *<br>
 *   <b>Method:</b> <br>
 */
public class Var {

	public static int count = 0;
	
	public static final String Category_Save = "save";
	public static final String Category_Recent = "recent";
	public  static final int vipTryPlayLength = 90;//试看定为90s
	public static final int INIT_OK = 1;
	public static final int INIT_FAILED = 2;
	
	private final static String TIME = "Time";

	//是否启用容灾
	public static boolean isRongZaiEnabled = false;

	/** 所有频道**/
	public static List<ContentChannel> allChannels = new ArrayList<ContentChannel>();
	/** 所有类别**/
	public static List<Category> allCategorys = new ArrayList<Category>();
	/** 'ALL' category's id**/
	public static String allCategoryId = "";
	/** 'ALL' category's index in group**/
	public static int allCategoryIndex = 0;
	
	public static String mac = "";
	public static String productId = "";
	public static String userToken = "";
	public static String userId = "";
	public static String drm_domain_url = "";	
	public static String deviceId = "";
	public static String netIP = "";
	public static String userGroupId = "";
	
	public static boolean isZZEnabled = false;
	
	public static boolean isActivityLaunched = false;
	
	public static final String KEY_ISFINISHED = "isServiceFinished";

	/**是否使用静态文件的方式访问EPG**/
	public static boolean isStatic = true;

	/**表示是否是携带频道号来跳转，有两种可能，从launcher点击跳转，另一种讯飞语音跳转**/
	public static boolean isJumpNumExist = false;

	/**Init params about liveplay**/
	public static int initStbVars(Context context)
	{
		boolean res;
		if (GDHttpTools.getInstance().isGDI6Enable()){
			res = getGDHttpParams();
		}else{
			res = getParams();
		}
		if(!res)
		{
			return INIT_FAILED;
		}else
		{
			LogUtil.i("initStbVars","initStbVars",
					"mac = " + mac + "\n"
							+ "epg_domain = " + VarParam.url +"\n"
							+ "userId = " + userId + "\n"
							+ "userToken = " + userToken + "\n"
							+ "drm_domain = " + drm_domain_url + "\n"
							+ "productId = " + productId);
			return INIT_OK;
		}

	}


	public static boolean getGDHttpParams()
	{
//		mac = GetVaram.getSTBINFO("mac");
		VarParam.url = GetVaram.getSTBINFO("epg_domain_url");
		if (VarParam.url == null || VarParam.url.length() < 5)
		{
			VarParam.url = VarParam.default_url;
		}
		userId = GetVaram.getSTBINFO("userId");
		userToken = GetVaram.getSTBINFO("userToken");
		userGroupId = GetVaram.getSTBINFO("userGroupNMB");
		mac = GetStbinfo.getLocalMacAddress().trim();
//		drm_domain_url = GetVaram.getSTBINFO("drm_domain_url");
//		productId = GetVaram.getSTBINFO("productId");
		if (userId == null || userToken == null || VarParam.url == null)
		{
			return false;
		}else{
			return true;
		}
	}

	public static boolean getParams() 
	{
		mac = GetVaram.getSTBINFO("mac");
		VarParam.url = GetVaram.getSTBINFO("epg_domain_url");
		if (VarParam.url == null || VarParam.url.length() < 5) 
		{
			VarParam.url = VarParam.default_url;
		}
		userId = GetVaram.getSTBINFO("userId");
		userToken = GetVaram.getSTBINFO("userToken");
		drm_domain_url = GetVaram.getSTBINFO("drm_domain_url");
		productId = GetVaram.getSTBINFO("productId");
		if (mac == null || userId == null || userToken == null || drm_domain_url == null || productId == null) 
		{
			return false;
		}else{
			return true;
		}
	}
	public static void clear()
	{
		allCategorys.clear();
		allChannels.clear();
	}

	public static int initOldParam() {
		
		mac = aPropertiesUtils.getPropertyValue(aPropertiesUtils.AUTH_MAC);
		deviceId = aPropertiesUtils.getPropertyValue(aPropertiesUtils.AUTH_MAC);
		productId = aPropertiesUtils.getPropertyValue(aPropertiesUtils.AUTH_PRODUCTID);
		drm_domain_url = aPropertiesUtils.getPropertyValue(aPropertiesUtils.DRM_DOMAIN);
		VarParam.url = aPropertiesUtils.getPropertyValue(aPropertiesUtils.EPG_DOMAIN);
		
		LogUtil.e("Var","initStbVars", 
			      "mac = " + mac + "\n"
				+ "epg_domain = " + VarParam.url +"\n"
			 	+ "userId = " + userId + "\n"
				+ "userToken = " + userToken + "\n"
			 	+ "drm_domain = " + drm_domain_url + "\n"
				+ "productId = " + productId
				+ "deviceId = " + deviceId);
		
		if (mac.equals("") || userId.equals("") || userToken.equals("") || productId.equals("") || drm_domain_url.equals("") || VarParam.url.equals("")) 
		{
			return INIT_FAILED;
		}
		return INIT_OK;
	}
	
	 public static String getDataDir(Context context){                                                                                                                                     
	       String dir = null;
	       try {
	           String nameString = context.getPackageName();
	           ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(nameString, 0);
	           dir = applicationInfo.dataDir;
	          
	       } catch (NameNotFoundException e) {
	           // TODO Auto-generated catch block
	           e.printStackTrace();
	       }
	       return dir;
	    }
	 
		public static int getChannelIndexByNum(String channelNum)
		{
			if (Var.allChannels!=null) 
			{
				for (int i = 0; i < Var.allChannels.size(); i++) 
				{
					if (Var.allChannels.get(i).getChannelNumber().equals(channelNum)) 
					{
						return i;
					}
				}
			}
			return -1;
		}

	public static String getChannelNameByChannelId(String channelId)
	{
		if (Var.allChannels!=null)
		{
			for (int i = 0; i < Var.allChannels.size(); i++)
			{
				if (Var.allChannels.get(i).getContentId().equals(channelId)){
					return Var.allChannels.get(i).getName();
				}
			}
		}
		return "";
	}

	public static int getCategoryIndex(String categoryId)
	{
		System.out.println("传进来的categoryId:"+categoryId);
		String caName = "";
		if (categoryId.trim().equals("bendi"))
		{
			caName = "本地";
		}else if (categoryId.trim().equals("yangshi"))
		{
			caName = "央视";
		}else if (categoryId.trim().equals("weishi"))
		{
			caName = "卫视";
		}else if (categoryId.trim().equals("shaoer"))
		{
			caName = "少儿";
		}else if (categoryId.trim().equals("gaoqing"))
		{
			caName = "高清";
		}else if (categoryId.trim().equals("quanbu"))
		{
			caName = "全部";
		}else if (categoryId.trim().equals("haikan"))
		{
			caName = "海看";
		}else if (categoryId.trim().equals("lewan"))
		{
			caName = "乐玩";
		}else if (categoryId.trim().equals("zongyi"))
		{
			caName = "综艺";
		}else if (categoryId.trim().equals("tiyu"))
		{
			caName = "体育";
		}else if (categoryId.trim().equals("fufei"))
		{
			caName = "付费";
		}

		for (int i = 0; i < allCategorys.size(); i++)
		{
			if (allCategorys.get(i).getName().trim().equals(caName))
			{
				return i;
			}
		}


		for (int i = 0; i < allCategorys.size(); i++)
		{
			if (allCategorys.get(i).getId().trim().equals(categoryId))
			{
				return i;
			}
		}
		return allCategoryIndex;
	}
}
