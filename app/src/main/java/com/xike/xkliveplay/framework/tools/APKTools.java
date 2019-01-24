package com.xike.xkliveplay.framework.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import com.xike.xkliveplay.framework.entity.ContentChannel;

/**
 * @author LiWei <br>
 * CreateTime: 2014年12月18日 上午11:01:49<br>
 * <b>Info:</b><br>
 * APK的相关方法
 *<br>
 *   <b>Method:</b> <br>
 *   1.{@link #isAPKComplete(Context, String)} 验证apk是否完整<br>
 *   2.{@link #getStrVersion(Context)} 获得apk版本号 <br>
 */
public class APKTools 
{
	/**
	 * 
	 * function: 验证apk是否完整
	 * @param
	 * @return
	 */
	public static boolean isAPKComplete(Context context,String path)
	{
		PackageManager pm = context.getPackageManager();
		PackageInfo info = pm.getPackageArchiveInfo(path,
		PackageManager.GET_ACTIVITIES);
		if (info == null) 
		{
			return false;
		}else {
			return true;
		}
		
	}
	
	/**
	 * 
	 * function: 获得apk版本号 
	 * @param
	 * @return
	 */
	  public static String getStrVersion(Context context) 
		{
			PackageManager manager = context.getPackageManager();
			String appVersion = null;
			try 
			{
			       PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
			       appVersion = info.versionName;
			} catch (NameNotFoundException e) 
			{
			        e.printStackTrace();
			}
			return appVersion;
		}
	  
	  public static int getIntVersionCode(Context context)
	  {
		  PackageManager manager = context.getPackageManager();
			try 
			{
			     PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
			     return info.versionCode;
			} catch (NameNotFoundException e) 
			{
			        e.printStackTrace();
			        return 0;
			}
	  }
	  
	// 获取本地IP函数
			public static String getLocalIPAddress() {
				try {
					for (Enumeration<NetworkInterface> mEnumeration = NetworkInterface
							.getNetworkInterfaces(); mEnumeration.hasMoreElements();) {
						NetworkInterface intf = mEnumeration.nextElement();
						for (Enumeration<InetAddress> enumIPAddr = intf
								.getInetAddresses(); enumIPAddr.hasMoreElements();) {
							InetAddress inetAddress = enumIPAddr.nextElement();
							// 如果不是回环地址
							if (!inetAddress.isLoopbackAddress()) {
								// 直接返回本地IP地址
								return inetAddress.getHostAddress().toString();
							}
						}
					}
				} catch (SocketException ex) {
					System.err.print("error");
				}
				return null;
			}
			
		public static	String GetNetIp(String ipaddr){
                URL infoUrl = null;
                InputStream inStream = null;
                String ipLine = "";
                HttpURLConnection httpConnection = null;
                try {
                    infoUrl = new URL(ipaddr);
                    URLConnection connection = infoUrl.openConnection();
                    httpConnection = (HttpURLConnection)connection;
                    int responseCode = httpConnection.getResponseCode();
                    if(responseCode == HttpURLConnection.HTTP_OK)
                      {    
                       inStream = httpConnection.getInputStream();   
                       BufferedReader reader = new BufferedReader(new InputStreamReader(inStream,"utf-8"));
                       StringBuilder strber = new StringBuilder();
                       String line = null;
                       while ((line = reader.readLine()) != null) 
                           strber.append(line + "\n");
                      
                       Pattern pattern = Pattern.compile("((?:(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){3}(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d))))");
                       Matcher matcher = pattern.matcher(strber.toString());
                       if (matcher.find()) {
                                               ipLine = matcher.group();
                                   }
                     }
                   
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }finally{
                         try {
                                        inStream.close();
                                         httpConnection.disconnect();
                                } catch (IOException e) {
                                        e.printStackTrace();
                                }
                }
                System.out.println("################## ip = " + ipLine);
               return ipLine;
            }    
		
		/**
		 * 用来获取手机拨号上网（包括CTWAP和CTNET）时由PDSN分配给手机终端的源IP地址。
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
								{// ipV6的地址
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
		
		public static List<ContentChannel> getOrderList(List<ContentChannel> oList)
		{
			List<ContentChannel> mList = new ArrayList<ContentChannel>();
			for (int i = 0; i < oList.size(); i++) 
			{
				if (oList.get(i).getCountry().equals("0")) 
				{
					System.out.println("###未订购的频道：" + oList.get(i).getName());
					continue;
				}
				mList.add(oList.get(i));
			}
			
			return mList;
		}
		
		/**
		  * getVersionCodeFromPath(读取指定路径下的APK文件的versionCode)
		  * @param path
		  * @param context
		  * @return int    返回类型
		  * @throws  异常处理
		  * @modifyHistory  createBy Mernake
		 */
		public static int getVersionCodeFromPath(String path,Context context)
		{
			PackageManager pm = context.getPackageManager();    
	        PackageInfo info = pm.getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES); 
	        if (info!=null) 
	        {
	        	return info.versionCode;
			}
			return 0;
		}
		
		/** 
		 * 判断应用是否正在运行 
		 *  
		 * @param context 
		 * @param packageName 
		 * @return 
		 */  
		public static  boolean isRunning(Context context, String packageName) {  
		    ActivityManager am = (ActivityManager) context  
		            .getSystemService(Context.ACTIVITY_SERVICE);  
		    List<RunningAppProcessInfo> list = am.getRunningAppProcesses();  
		    for (RunningAppProcessInfo appProcess : list) {  
		        String processName = appProcess.processName;  
		        if (processName != null && processName.equals(packageName)) {  
		            return true;  
		        }  
		    }  
		    return false;  
		} 
		
		public static String getWIFISSID(Context context)
		{
			   WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
			   WifiInfo wifiInfo = wifiManager.getConnectionInfo();
			   if (wifiInfo == null) 
			   {
				return "";
			   }
//			   System.out.println("####:ssid:"+wifiInfo.getSSID());
			   return wifiInfo.getSSID();
		}

	public static boolean isPkgInstalled(Context context,String packageName) {

		if (packageName == null || "".equals(packageName))
			return false;
		// android.content.pm.ApplicationInfo info = null;
		try {
			return context.getPackageManager().getLaunchIntentForPackage(packageName) != null;

		} catch (Exception e) {
			return false;
		}
	}
}
