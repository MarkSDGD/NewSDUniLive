package com.xike.xkliveplay.framework.service;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import com.ab.util.AbSharedUtil;
import com.xike.xkliveplay.framework.db.DBManager;
import com.xike.xkliveplay.framework.entity.AuthRes;
import com.xike.xkliveplay.framework.entity.AuthenticationRes;
import com.xike.xkliveplay.framework.entity.Category;
import com.xike.xkliveplay.framework.entity.ContentChannel;
import com.xike.xkliveplay.framework.entity.GetAccountInfoRes;
import com.xike.xkliveplay.framework.http.HttpUtil;
import com.xike.xkliveplay.framework.http.IUpdateData;
import com.xike.xkliveplay.framework.httpclient.HttpAuth;
import com.xike.xkliveplay.framework.httpclient.HttpAuthentication;
import com.xike.xkliveplay.framework.httpclient.HttpGetAccountInfo;
import com.xike.xkliveplay.framework.httpclient.HttpGetCategoryList;
import com.xike.xkliveplay.framework.httpclient.HttpGetContentList;
import com.xike.xkliveplay.framework.httpclient.HttpGetFavoriteList;
import com.xike.xkliveplay.framework.httpclient.VarParam;
import com.xike.xkliveplay.framework.tools.GetStbinfo;
import com.xike.xkliveplay.framework.tools.GetVaram;
import com.xike.xkliveplay.framework.tools.LogUtil;
import com.xike.xkliveplay.framework.varparams.Var;
import com.xike.xkliveplay.xunfei.XunfeiTools;

import java.io.File;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;

@SuppressLint("DefaultLocale")
public class AuthenticationServiceBase extends Service implements IUpdateData
{
	
	private static final String tag = "AuthenticationService";
	private static final String TIME = "Time";
	private String encryToken = "";
	private String userToken = "";
	private String epg_domain_url = "";
	private String drm_domain_url = "";
	
	private String userId = "";
	private String userPassWord = "";
	
	public static  HashMap<String, String> stb_map = null;
	
	public static boolean isStop = false;
	
	DBManager dbManager = null;
	
	
	

	
	

	@SuppressLint("SdCardPath")
	@Override
	public void onCreate() 
	{
		LogUtil.e(tag, "onCreate()","****************************XKLIVESERVICE is oncreate**************************");
		super.onCreate();
	}
	
	
	
	private void delSqliteDatabase() 
	{
		dbManager.deleteAll("category");
		dbManager.deleteAll("channel");
		dbManager.deleteAll("xuifei");
	}

	/**
	 * 
	 * @param
	 * @return
	 */
	public boolean isLiveStarted(Context context)
	{
		String packageName = "com.xike.xkliveplay";
		ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
		if (am == null) 
		{
			return false;
		}
		
		List<RunningTaskInfo> list = am.getRunningTasks(100);
		if (list == null) 
		{
			return false;
		}
		for (RunningTaskInfo info : list) 
		{
			if (info == null) 
			{
				continue;
			}
		    if (info.topActivity.getPackageName().equals(packageName) && 
		        info.baseActivity.getPackageName().equals(packageName)) 
		    {
		    	return true;
		    }
		}
		return false;
	}


	private void makeIfNoSDcard() 
	{
		File file = new File("/mnt/sdcard");
		if (file == null || !file.isDirectory()) 
		{
			file.mkdir();
		}
	}


	@Override
	public int onStartCommand(Intent intent, int flags, int startId) 
	{
		LogUtil.e(tag, "onStartCommand","*****************************XKLIVESERVICE onstartCommand start**************************");
		AbSharedUtil.putInt(getApplicationContext(), Var.KEY_ISFINISHED, 0);
		dbManager = new DBManager(getApplicationContext());
		SharedPreferences sp = getApplicationContext().getSharedPreferences("FragmentLivePlay", 0);
		Editor editor = sp.edit();
		editor.putString("name", "empty");
		editor.commit();
		if (intent != null && intent.getStringExtra("isStop") != null) 
		{
			if (intent.getStringExtra("flag") != null && intent.getStringExtra("flag").equals("reqdb")) 
			{
//				delSqliteDatabase();
				postGetCategoryList("0");
				return super.onStartCommand(intent, flags, startId);
			}
			else if (intent.getStringExtra("flag") != null && intent.getStringExtra("flag").equals("first")) 
			{
				
				makeIfNoSDcard();
				LogUtil.e(tag,"onStartCommand", "begin service");
				LogUtil.e(tag,"onStartCommand", "*****************************XKLIVESERVICE is start**************************");
				if (stb_map != null) 
				{
					stb_map.clear();
					stb_map = null;
				}
				stb_map = new HashMap<String, String>();
//				delVaramFile();
//				LogUtil.e(tag, "onStartCommand","*****************************deleted former stbinfo file**************************");
//				makeDirIfNop2p();
//				LogUtil.e(tag, "onStartCommand","*****************************ensure there is p2p directory**************************");
//				delSqliteDatabase();
//				LogUtil.e(tag, "onStartCommand","*****************************deleted former table**************************");
				new Thread(new MyRunnable()).start();
			}
			if (intent.getStringExtra("isStop").equals("false")) 
			{
				isStop = false;
			}else if (intent.getStringExtra("isStop").equals("true")) 
			{
				isStop = true;
			}
		}
		return super.onStartCommand(intent, flags, startId);
	}



	@SuppressLint("HandlerLeak")
	Handler hd = new Handler()
	{
		public void handleMessage(Message msg) 
		{
			super.handleMessage(msg);
			if (msg.what == 0) 
			{

			}else if (msg.what == 1) 
			{
			}
		}
		
	};
	
	
	@SuppressLint("SdCardPath")
	private void delVaramFile() 
	{
		String infoPath = "/mnt/sdcard/stbinfo";
		File file = new File(infoPath);
		if (file != null && file.exists()) 
		{
			file.delete();
		}
	}

	public boolean isNetworkConnected(Context context)
	{  
	    if (context != null) 
	    {  
	        ConnectivityManager mConnectivityManager = (ConnectivityManager) context  
	                .getSystemService(Context.CONNECTIVITY_SERVICE);  
	        NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();  
	        if (mNetworkInfo != null) 
	        {  
	            return mNetworkInfo.isAvailable();  
	        }  
	    }  
	    return false;  
	}

	@SuppressLint("SdCardPath")
	private void makeDirIfNop2p() 
	{
		String path = "/mnt/sdcard/p2p";
		File file = new File(path);
		if (file == null || !file.isDirectory()) 
		{
			LogUtil.e(tag, "makeDirIfNop2p","There is no /mnt/sdcard/p2p directory , recreate");
			file.mkdir();
		}else 
		{
			LogUtil.e(tag, "makeDirIfNop2p","There is /mnt/sdcard/p2p directory");
		}
	}
	
	@Override
	public IBinder onBind(Intent intent) 
	{
		return null;
	}
	
	private void postGetAccountInfo()
	{
		String mac = getReqMac();
		if (mac == null) 
		{
			return;
		}
		HttpUtil.create().postGetAccountInfo(this, mac, this);
	}

	@Override
	public void updateData(String method, String uniId, Object object,
			boolean isSuccess) 
	{
		if (isLiveStarted(getApplicationContext())) 
		{
//			delSqliteDatabase();
			stopService(new Intent("com.xike.AuthenticationService"));
			return;
		}
		
		if (method.equals(HttpGetAccountInfo.Method))
		{
			if (uniId != null && uniId.equals("500")) 
			{
				isStop = true;
				LogUtil.e(tag, "updateData", "Mac����userIdʧ��");
//				GetVaram.createNewEmptyStbinfo(getApplicationContext());
				return;
			}
			
			int res = getAccountInfo((GetAccountInfoRes)object);
			if (res == 1) 
			{
				postAuthentication(userId);
				return;
			}else 
			{
				isStop = true;
				LogUtil.e(tag, "updateData", "Mac����userIdʧ��");
				return;
			}
		}
		else if (method.equals(HttpAuthentication.Method))
		{
			int res = getEncryToken((AuthenticationRes)object);
			if (res == 1) 
			{
				postAuth();
				return;
			}else
			{
				isStop = true;
				LogUtil.e(tag, "updateData", "Auth��֤ʧ��");
			    return;
			}
		}
		else if (method.equals(HttpAuth.Method)) 
		{
			int res = getAuth((AuthRes)object);
			if (res == 1) 
			{
				stb_map.put("productId", "1001");
				int saveRes = GetVaram.write2SDcard(stb_map);
				if (saveRes == -1) 
				{
					isStop = true;
					LogUtil.e(tag, "updateData", "д��stbinfo�ļ�ʧ��");
					return;
				}else if(saveRes == 0) 
				{
					isStop = true;
					LogUtil.e(tag, "updateData","write stbinfo sucess");
				}
				LogUtil.e(tag, "updateData","*****************************XKLIVESERVICE is end**************************");
				LogUtil.e(tag,"updateData", "*************Request EPG start**************");
				startLiveRequest();
				LogUtil.e(TIME, "updateData","finish all SERVICE params request need: " + System.currentTimeMillis());
				return;
			}else
			{
				isStop = true;
				LogUtil.e(tag, "updateData", "Auth��֤ʧ��");
				return;
			}
		}
		
		if (HttpGetCategoryList.Method.equals(method)) 
		{
			LogUtil.e(tag, "updateData","unid = " + uniId);
			if (uniId == null) 
			{
				isStop = true;
				LogUtil.e(tag, "updateData", "��ȡ����Ŀʧ��");
				return;
			}
			if(uniId.equals("0"))
			{
				LogUtil.e(tag, "updateData","���ʸ����ͷ���");
				if (object == null) 
				{
					isStop = true;
					LogUtil.e(tag, "updateData", "��ȡ����Ŀ���ʧ��");
					return;
				}
				@SuppressWarnings("unchecked")
				List<Category> categoryList = (List<Category>)object;
				if (categoryList != null && categoryList.size() >= 1) 
				{
					LogUtil.e(tag, "updateData","�����������");
					postGetCategoryList(categoryList.get(0).getId());
					return;
				}
			
			}else 
			{
//				LogUtil.e(TIME, "get column list time is: " + System.currentTimeMillis());
				if (object == null) 
				{
					isStop = true;
					LogUtil.e(tag, "updateData", "��ȡ���ʧ��");
					return;
				}
				int res = dealResWithCategorys(object);
				if (res == 0) 
				{
					isStop = true;
					LogUtil.e(tag, "updateData", "��ȡ���ʧ��");
					return;
				}
			}

			return;
		}
		if (HttpGetContentList.Method.equals(method)) 
		{
			if (object == null) 
			{
				isStop = true;
				LogUtil.e(tag, "updateData", "��ȡƵ���б�ʧ��");
				return;
			}
			
			int res = dealResWithContentChannels(object, uniId);
			if (res == 0) 
			{
				isStop = true;
				LogUtil.e(tag, "updateData", "��ȡƵ���б�ʧ��");
				return;
			}
			
			postGetFavoriteList();
			return;
		}
		
		if(HttpGetFavoriteList.Method.equals(method))
		{
			if (!isSuccess) 
			{
				isStop = true;
				LogUtil.e(tag, "updateData", "��ȡ�ղ�Ƶ��ʧ��");
				return;
			}
			if (object == null) 
			{
				isStop = true;
				return;
			}
			dealResFavorites(object);
			stopService(new Intent("com.xike.AuthenticationService"));
			return;
		}
	}
	
	private int getAuth(AuthRes authRes)
	{
		if (authRes == null) 
		{
			return 0;
		}
		userToken = authRes.getUserToken();
		epg_domain_url = authRes.getEpgDomain();
		drm_domain_url = authRes.getDrmDomain();
		VarParam.url = epg_domain_url;
		stb_map.put("epg_domain_url", epg_domain_url);
		stb_map.put("drm_domain_url", drm_domain_url);
		if (!userToken.equals("")) 
		{
			stb_map.put("userToken", userToken);
			return 1;
		}
		return 0;
	}

	private void postAuth()
	{
		HttpUtil.create().postAuth(this, getReqMac(), encryToken, getPsdnIp(), userId, userPassWord);
	}


	private void postAuthentication(String userId2)
	{
		HttpUtil.create().postAuthentication(this, userId2);
	}

	private int getAccountInfo(GetAccountInfoRes getAccountInfoRes)
	{
		if (getAccountInfoRes == null || getAccountInfoRes.getAccount() == null) 
		{
			return 0;
		}
		userId = getAccountInfoRes.getAccount();
		userPassWord = getAccountInfoRes.getPassword();
		if (userId.length() <= 1) 
		{
			LogUtil.e(tag, "getAccountInfo","because userId's length is less than 1 ,so Auth failed");
			return 0;
		}
		if (!userId.equals("")) 
		{
			stb_map.put("userId", userId);
			stb_map.put("userPassWord", userPassWord);
			return 1;
		}
		return 0;
	}
	
	
	private int getEncryToken(AuthenticationRes res)
	{	
		if (res == null) 
		{
			return 0;
		}
		encryToken = res.getEncryToken();
		if (!encryToken.equals("")) {
			return 1;
		}
		return 0;
	}
	
	/*   ac4afe44cdc9
	 *   ac:4afe44cdc9
	 *   ac:4a:fe44cdc9
	 *   ac:4a:fe:44cdc9
	 *   ac:4a:fe:44:cdc9
	 *   ac:4a:fe:44:cd:c9
	 * **/
	private String getReqMac()
	{
		String mac = GetStbinfo.getLocalMacAddress();
		if (mac == null) 
		{
			LogUtil.e(tag, "getReqMac", "MAC��ַ��ȡʧ��");
			isStop = true;
			return null;
		}
		LogUtil.e(tag, "getReqMac","mac = " + mac + "mac.length = " + mac.length());
		stb_map.put("mac", mac.trim());
		return mac.trim();
	}
	
	
	/**
	 *
	 * @return
	 * @author SHANHY
	 * @throws SocketException 
	 */
	@SuppressLint("DefaultLocale")
	public static String getPsdnIp() 
 {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements();) 
			{
				NetworkInterface intf = en.nextElement();
				if (intf.getName().toLowerCase().equals("eth0")
						|| intf.getName().toLowerCase().equals("wlan0")) 
				{
					for (Enumeration<InetAddress> enumIpAddr = intf
							.getInetAddresses(); enumIpAddr.hasMoreElements();) 
					{
						InetAddress inetAddress = enumIpAddr.nextElement();
						if (!inetAddress.isLoopbackAddress()) 
						{
							String ipaddress = inetAddress.getHostAddress()
									.toString();
							if (!ipaddress.contains("::"))
							{//
								return ipaddress;
							}
						}
					}
				} else 
				{
					continue;
				}
			}
		} catch (SocketException e) 
		{
			e.printStackTrace();
		}

		return "";
	}
	
	class MyRunnable implements Runnable
	{
		@Override
		public void run() 
		{
			while (true) 
			{
				if (!isStop && isNetworkConnected(AuthenticationServiceBase.this)) 
				{
					LogUtil.e(tag,"MyRunnable", "*****************************XKLIVESERVICE is starting send internet request**************************");
					isStop = true;
					hd.sendEmptyMessage(0);
				}else{
					LogUtil.e(tag, "MyRunnable()","****************************Thread.sleep(500)**************************");

					try
						{
							Thread.sleep(500);
						} catch (InterruptedException e) 
						{
							e.printStackTrace();
						}
					 }
			}
		}
	}
	
	   

	private void startLiveRequest()
	{
		if (Var.initStbVars(getApplicationContext()) == 1) 
		{
			postGetCategoryList("0");
		}
	}
	
	private void postGetContentList(String categoryId)
	{
		LogUtil.e(tag, "postGetContentList", "׼�������Ƶ���б����������id�ǣ�" + categoryId);
		HttpUtil.create().postGetContentList(this, categoryId);
	}
	
	private void postGetCategoryList(String id)
	{
		LogUtil.e(tag,"postGetCategoryList", "id = " + id);
		HttpUtil.create().postGetCategoryList(this, id);
	}
	
	private void postGetFavoriteList()
	{
		HttpUtil.create().postGetFavoriteList(this);
	}
	
	@SuppressWarnings("unchecked")
	private int dealResWithCategorys(Object object) 
	{
		
		if (((List<Category>) object).size() <= 0) 
		{
			return 0;
		}
		Var.allCategorys = (List<Category>) object;
		
		
		Category category = getAllChannelCategory(Var.allCategorys);
		if (category != null) 
		{
			Var.allCategoryId = category.getId();
			postGetContentList(Var.allCategoryId);
		}
		
		Category category1 = new Category();
		category1.setId(Var.Category_Recent);
		category1.setName("���");
		Var.allCategorys.add(category1);
		
		Category categorySave = new Category();
		categorySave.setId("save");
		categorySave.setName("�ղ�");
		Var.allCategorys.add(categorySave);
		dbManager.deleteAll("category");
		for (Category category2 : Var.allCategorys) 
		{
			int res = dbManager.add("category", category2);
			if (res == 0) 
			{
				LogUtil.e(tag, "dealResWithCategorys", "���ݿ⻺��ʧ��");
				return 0;
			}
		}
		return 1;
	}
	
	private Category getAllChannelCategory(List<Category> list)
	{
		for (int i = 0; i < list.size(); i++) 
		{
			if (list.get(i).getName().equals("ȫ��")) 
			{
				Var.allCategoryIndex = i;
				return list.get(i);
			}
		}

		return null;
	}
	
	@SuppressWarnings("unchecked")
	private int dealResWithContentChannels(Object object, String uniId) 
	{
		if (((List<ContentChannel>) object).size() <= 0 ) 
		{
			return 0;
		}
		if (uniId == Var.allCategoryId) 
		{
			Var.allChannels = (List<ContentChannel>) object;
		}
		
		XunfeiTools.getInstance().saveChannels(dbManager,getApplicationContext());//����Ƶ���б��xunfei�ı�
		if (!VarParam.CM_DEFAULT_URL.equals("http://msnm.snctv.cn/")) 
		{
			dbManager.closeDB();
			AbSharedUtil.putInt(getApplicationContext(), Var.KEY_ISFINISHED, 1);
		}
//		for (ContentChannel channel : Var.allChannels) 
//		{
//			int res = dbManager.add("channel", channel);
//			XunfeiObject xunfeiObject = new XunfeiObject();
//			xunfeiObject.setName(channel.getName());
//			xunfeiObject.setNo(channel.getChannelNumber());
//			dbManager.add("xunfei", xunfeiObject);
//			if (res == 0) 
//			{
//				return 0;
//			}
//		}
		return 1;
	}
	
	private void dealResFavorites(Object object)
	{
		List<ContentChannel> saveList = (List<ContentChannel>)object;
		for(int i = 0; i < saveList.size();i++)
		{
			for(int j = 0; j < Var.allChannels.size();j++)
			{
				if(saveList.get(i).getChannelNumber().equals(Var.allChannels.get(j).getChannelNumber()))
				{
					ContentChannel contentChannel = new ContentChannel();
					contentChannel.setChannelNumber(Var.allChannels.get(j).getChannelNumber());
					Var.allChannels.get(j).setZipCode(Var.Category_Save);
					int res = dbManager.update("channel", Var.allChannels.get(j), contentChannel);
					if (res == 0) 
					{
//						ErrorCode.makeErrorToast(ErrorCode.ERROR_CACHE_DB_EXCEPTION, getApplicationContext());
//						dbManager.closeDB();
//						sendDBOKBroadcast(DB_FAILED);
						break;
					}
				}
			}
		}
		dbManager.closeDB();
		AbSharedUtil.putInt(getApplicationContext(), Var.KEY_ISFINISHED, 1);
		LogUtil.e(tag, "dealResFavorites", "Ƶ���б���ɹ�");
	}

	
}
