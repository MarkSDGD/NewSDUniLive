package com.xike.xkliveplay.framework.tools;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Set;


public class GetVaram {

	public static String StbinfoPath = Environment.getRootDirectory()
			.getAbsolutePath() + "/etc/STBINFO";
	@SuppressLint("SdCardPath")
	public static String StbinfoPath2 = "/mnt/sdcard/newstbinfo";
	

	public static String setSTBINFO() {
		HashMap<String, String> mQueryMap = new HashMap<String, String>();
		@SuppressWarnings("unused")
		String content = "";
		File file = new File(StbinfoPath2);
		try {
			InputStream instream = new FileInputStream(file);
			if (instream != null) {
				InputStreamReader inputreader = new InputStreamReader(instream);
				BufferedReader buffreader = new BufferedReader(inputreader);
				String line;
				// ���ж�ȡ
				while ((line = buffreader.readLine()) != null) 
				{
					content += line + "\n";
					String[] keyvalue = line.split("=");
					if (keyvalue != null) 
					{
						if (keyvalue.length == 1) 
						{
							mQueryMap.put(keyvalue[0], "-1");
						}else if (keyvalue.length == 2) 
						{
							mQueryMap.put(keyvalue[0], keyvalue[1]);
						}
					}
				}
				instream.close();
			}
		} catch (java.io.FileNotFoundException e) {
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

	public static String getSTBINFO(String Params) {
		String json = setSTBINFO();
		String Param = null;
		if (json != null && json.length() > 0) {
			try {
				JSONObject jsonObject = new JSONObject(json);
				Param = jsonObject.getString(Params);
				if (Param.equals("-1")) 
				{
					return null;
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return Param != null ? Param.toString() : null;
		}
		return null;
	}
	
	
	public static int write2SDcard(HashMap<String, String> map)
	{
//		LogUtil.e("�ļ�����", "�ļ����·��Ϊ��" + StbinfoPath2);
		FileOutputStream fos = null;
		try 
		{
			File file = new File(StbinfoPath2);
			if (!file.exists()) 
			{
				file.createNewFile();
			}
				fos = new FileOutputStream(file);
			String writeStr = "";
			Set<String> set = map.keySet();
//			LogUtil.e("�ļ�����", "map�Ĵ�С�ǣ�" + map.size());
			for (String string : set) 
			{	String temp = string + "=" + map.get(string) + "\n";
				writeStr += temp; 
			}
			byte[] bytes = writeStr.getBytes();
				fos.write(bytes);
			
		} catch (IOException e) 
		{
			e.printStackTrace();
			return -1;
		}finally{
			try 
			{
				if (fos != null) {
					fos.close();
				}
			} catch (IOException e)
			{
				e.printStackTrace();
				return -1;
			}
		}
		return 0;
	}
	
	   @SuppressLint("DefaultLocale")
	public static String getMacAddress(Context paramContext)
	    {
	      try
	      {
	        String str = loadFileAsString("/sys/class/net/eth0/address").toUpperCase().substring(0, 17);
	        return str;
	      }
	      catch (IOException localIOException)
	      {
	      }
	      return null;
	    }
	   
	  public static String loadFileAsString(String paramString)
	      throws IOException
	    {
	      StringBuffer localStringBuffer = new StringBuffer(1000);
	      BufferedReader localBufferedReader = new BufferedReader(new FileReader(paramString));
	      char[] arrayOfChar = new char[1024];
	      while (true)
	      {
	        int i = localBufferedReader.read(arrayOfChar);
	        if (i == -1)
	        {
	          localBufferedReader.close();
	          return localStringBuffer.toString();
	        }
	        localStringBuffer.append(String.valueOf(arrayOfChar, 0, i));
	      }
	    }
	  

	  
	  public static int getStbinfoFileInfo()
	  {	  
		  File file = new File(StbinfoPath2);
		  if (file == null || !file.exists()) 
		  { 
			 return -1;
		  }
		  if (!file.isDirectory() && file.length() == 0) 
		  {	
			 return 0;
		  }
		  return (int) file.length();
	  }

	public static void createNewEmptyStbinfo(Context context) 
	{
		File file = new File(StbinfoPath2);
		if (file != null && file.exists() && !file.isDirectory() && file.length() > 0) 
		{
			file.delete();
		}
		try {
			file.createNewFile();
		} catch (IOException e) 
		{
//			DialogError.getInstance().showDialog(context, ErrorCode.ERROR_UNKNOWN_EXCEPTION);
			e.printStackTrace();
		}
	}
}
