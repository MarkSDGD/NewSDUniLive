package com.xike.xkliveplay.framework.tools;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.Set;


public class aPropertiesUtils {

	private static ResourceBundle rb;

	private final static String PROPERTY_FILE_NAME = "property.txt";

	public final static String USER_TOCKEN = "USER_TOCKEN";

	public final static String DRM_DOMAIN = "DRM_DOMAIN";

	public final static String EPG_DOMAIN = "EPG_DOMAIN";

	public final static String AUTH_FLG = "AUTH_FLG";
	public final static String AUTH_PRODUCTID = "AUTH_PRODUCTID";
	public final static String AUTH_USERID = "AUTH_USERID";
	public final static String AUTH_PASSWORD = "AUTH_PASSWORD";
	public final static String AUTH_MAC = "AUTH_MAC";
	public final static String AUTH_IP = "AUTH_IP";
	public final static String AUTH_TERMINALID = "AUTH_TERMINALID";

	public final static String PARENT_ID_FLG = "PARENT_ID_FLG";
	public final static String PARENT_LNL_ID = "PARENT_LNL_ID";
	public final static String PARENT_KDS_ID = "PARENT_KDS_ID";

	public final static String XINWEN_ID = "XINWEN_ID";

	public final static String LOGINTYPE = "LOGINTYPE";
	
	public static String OTT_MAINPATH = "/sdcard/sdctv/";// Ott主目录

	public static void initPropertyFile() {
		try {
			File file = new File(OTT_MAINPATH);
			if (!file.exists()) {
				file.mkdirs();
			}
			File proFile = new File(OTT_MAINPATH
					+ PROPERTY_FILE_NAME);
			if (proFile.exists()) {
				proFile.delete();
			}
			proFile.createNewFile();
		} catch (Exception e) {
		}
	}

	private static ResourceBundle getPropertiesBundle() {
		if (rb == null) {
			InputStream in = null;
			try {
				in = new BufferedInputStream(new FileInputStream(
						OTT_MAINPATH + PROPERTY_FILE_NAME));
				rb = new PropertyResourceBundle(in);
			} catch (FileNotFoundException e) {
			//	Log_log.WriteLog("getPropertiesBundle FileNotFoundException:",
//						e.getMessage());
				e.printStackTrace();
			} catch (IOException e) {
			//	Log_log.WriteLog("getPropertiesBundle IOException:",
//						e.getMessage());
				e.printStackTrace();
			} finally {
				if (in != null) {
					try {
						in.close();
					} catch (IOException e1) {
					}
					in = null;
				}
			}
		}

		return rb;
	}

	public static String getPropertyValue(String key) {
		String[] values = getPropertyValues(new String[] { key });
		return (values != null && values.length > 0) ? values[0] : "";
	}

	public static String[] getPropertyValues(String[] keys) {
		ResourceBundle rb = getPropertiesBundle();
		String[] values = new String[keys.length];

		int i = 0;
		for (String key : keys) {
			values[i] = "";
			try {
				values[i] = (rb != null) ? rb.getString(key) : "";
				values[i] = (values[i] != null) ? values[i] : "";
			} catch (Exception e) {
				values[i] = "";
			}
			i++;
		}

		return values;
	}
}
