package com.xike.xkliveplay.framework.tools;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;


public class VoicePropertiesUtils {

	private static ResourceBundle rb;

	private final static String PROPERTY_FILE_NAME = "propertyVoice.txt";

	public final static String VOICE_VAL = "VOICE_VAL";
	public final static String VOICE_TYPE = "VOICE_TYPE";
	public static String OTT_MAINPATH = "/sdcard/sdctv/";// Ott��Ŀ¼
	public static void initPropertyFile() {
		try {
			File file = new File(OTT_MAINPATH);
			if (!file.exists()) {
				file.mkdirs();
			}
			File proFile = new File(OTT_MAINPATH
					+ PROPERTY_FILE_NAME);
			if (!proFile.exists()) {
				proFile.createNewFile();
			}
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
//				Log_log.WriteLog("getPropertiesBundle FileNotFoundException:",
//						e.getMessage());
				// e.printStackTrace();
				initPropertyFile();
				try {
					in = new BufferedInputStream(new FileInputStream(
							OTT_MAINPATH + PROPERTY_FILE_NAME));
					rb = new PropertyResourceBundle(in);
				} catch (Exception e2) {
					System.out.println("getPropertiesBundle Exception:"
							+ e2.getMessage());
				}
			} catch (IOException e) {
//				Log_log.WriteLog("getPropertiesBundle IOException:",
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

	public static void putVoiceTypeVal(String type, String val) {
		putPropertyValues(new String[][] { new String[] { VOICE_TYPE, type },
				new String[] { VOICE_VAL, val } });
	}

	public static void putPropertyValue(String[] keyvalue) {
		putPropertyValues(new String[][] { keyvalue });
	}

	public static void putPropertyValues(String[][] keyvalues) {
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(OTT_MAINPATH
					+ PROPERTY_FILE_NAME);

			for (int i = 0; keyvalues != null && i < keyvalues.length; i++) {
				if (HspStringUtil.isEmptyString(keyvalues[i][0])) {
					continue;
				}
				if (HspStringUtil.isEmptyString(keyvalues[i][1])) {
					continue;
				}
				System.out.println("putPropertyValues keyvalues[" + i
						+ "] [0]:" + keyvalues[i][0] + " [1]:"
						+ keyvalues[i][1]);
				fileWriter.write(keyvalues[i][0] + "=" + keyvalues[i][1]
						+ "  \r\n");
			}
			fileWriter.flush();
		} catch (FileNotFoundException e) {
			System.out.println("getPropertiesBundle FileNotFoundException:"+
					e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("getPropertiesBundle IOException:"+e.getMessage());
			e.printStackTrace();
		} finally {
			if (fileWriter != null) {
				try {
					fileWriter.close();
				} catch (IOException e) {
				}
			}
			rb = null;
		}

	}

	public static String getPropertyValue(String key) {
		String[] values = getPropertyValues(new String[] { key });
		return (values != null && values.length > 0) ? values[0].trim() : "";
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
