package com.xike.xkliveplay.framework.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;



import com.xike.xkliveplay.framework.entity.LiveUpgrageRequest;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

//获取机顶盒信息
public class GetStbinfo 
{
	
	private static final String tag = GetStbinfo.class.getSimpleName();
	// 机顶盒信息存放路径
	public static String StbinfoPath = Environment.getRootDirectory()
			.getAbsolutePath() + "/etc/STBINFO";
	
	public static String mac_path = Environment.getRootDirectory()
			.getAbsolutePath() + "/etc/MAC";

	// 读取机顶盒中有关信息内容
	public static String setSTBINFO() {
		HashMap<String, String> mQueryMap = new HashMap<String, String>();
		String content = ""; // 文件内容字符串
		// 打开文件
		File file = new File(StbinfoPath);
		if (!file.exists()) {
			return "empty";
		}
		try {
			InputStream instream = new FileInputStream(file);
			if (instream != null) {
				InputStreamReader inputreader = new InputStreamReader(instream);
				BufferedReader buffreader = new BufferedReader(inputreader);
				String line;
				// 分行读取
				while ((line = buffreader.readLine()) != null) {
					content += line + "\n";
					String[] keyvalue = line.split("=");
					mQueryMap.put(keyvalue[0], keyvalue[1]);
				}
				instream.close();
			}
		} catch (java.io.FileNotFoundException e) {
			Log.d("TestFile", "文件不存在");
		} catch (IOException e) {
			Log.d("TestFile", e.getMessage());
		}
		Set<String> set = mQueryMap.keySet();
		for (String string : set) {
			System.out.println("-------- " + string + ","
					+ mQueryMap.get(string));
		}
		JSONObject jsonObject = new JSONObject(mQueryMap);
		return jsonObject.toString();
	}

	// 根据传递进来的相应参数取得配置文件中对应的值
	public static String getaSTBINFO(String Params) {
		String json = setSTBINFO();
		if (json.equals("empty")) {
			return "0";
		}
		System.out.println(json);
		String Param = null;
		if (json != null && json.length() > 0) {
			try {
				JSONObject jsonObject = new JSONObject(json);
				Param = jsonObject.getString(Params);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return Param != null ? Param.toString() : null;
		}
		return "";
	}

	/**
	 * 截取系统升级包包名，以从服务器返回的数据从最后一个“/”开始到.zip结束为包名并且去掉“/”
	 * 760325fae1e3e21128b9dc09915be76d.zip
	 */
	public static String getzipstring(String url) {
		if (url != null) {
			String zipString = url.substring(url.lastIndexOf("/")).replaceAll(
					"/", "");
			return zipString != null ? zipString : null;
		}
		return null;
	}

//	/**
//	 * 海信提供的获取mac的方法,取得的数据去掉“:”并且转换为大写
//	 */
	// public static String GetMac() {
	// // 海信取得mac的类
	// String SYSTEM_PROPERTIES_MAC = SystemProperties
	// .get("ubootenv.var.ethaddr");
	// String MAC = SYSTEM_PROPERTIES_MAC.replaceAll(":", "").toUpperCase()
	// .substring(0, 12);
	// Log.i("MAC", MAC);
	// return MAC != null ? MAC : null;
	//
	// }
	
	public static LiveUpgrageRequest getMac()
	{
		// 打开文件
				File file = new File(mac_path);
				if (!file.exists()) 
				{
//					Toast.makeText(context, "MAC获取失败", Toast.LENGTH_SHORT).show();
					LogUtil.e(tag,"getMac()", "MAC file is not exist.");
					return null;
				}
				LiveUpgrageRequest req = new LiveUpgrageRequest();
				try {
					InputStream instream = new FileInputStream(file);
					if (instream != null) 
					{
						InputStreamReader inputreader = new InputStreamReader(instream);
						BufferedReader buffreader = new BufferedReader(inputreader);
						String line;
//						// 分行读取
//						while ((line = buffreader.readLine()) != null) {
//							content += line + "\n";
//						}
//						line = buffreader.readLine();
						req.setMac(buffreader.readLine());
						req.setHard(buffreader.readLine());
						req.setOperators(buffreader.readLine());
//						LogUtil.e("MAC", "mac是：" + line);
						instream.close();
						return req;
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
		return null;
	}
	
	public static String getLocalMacAddress() {
		String mac = null;
		try{
			String path="sys/class/net/eth0/address";
			FileInputStream fis_name = new FileInputStream(path);
			byte[] buffer_name = new byte[1024*8];
	        int byteCount_name = fis_name.read(buffer_name);
	        if(byteCount_name>0)
	        {
	            mac = new String(buffer_name, 0, byteCount_name, "utf-8");
	        }
	        fis_name.close();
		}catch(Exception io)
		{
			return null;
		}
//		return "18:61:52:10:88:70";
//		return "BC:20:BA:45:69:C1";
//		return "08:a5:c8:3b:a3:06";
//		return "08:a5:c8:3b:a3:06";
//		return "00:21:26:02:45:a4";
//		return "90:d8:f3:11:e3:d8";
//		return "90:d8:f3:11:e3:B1";//移动
//		return "5c:c6:d0:99:8a:49"; //联通？电信？
//		return "08:a5:c8:ab:8b:94";//移动
//		return "00:07:63:c7:04:d4"; //移动
//		return "bc:20:ba:45:69:c1";
//		return "54:93:59:ca:6d:fb";
		return mac;
	}
	
	  public static String getStrVersion(Context context) 
		{
			PackageManager manager = context.getPackageManager();
			String appVersion = null;
			try 
			{
			       PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
//			       appVersion = info.versionName; // �汾����versionCodeͬ��
			       appVersion = info.versionCode + "";
			} catch (NameNotFoundException e) 
			{
			        e.printStackTrace();
			}
			return appVersion;
		}
}
