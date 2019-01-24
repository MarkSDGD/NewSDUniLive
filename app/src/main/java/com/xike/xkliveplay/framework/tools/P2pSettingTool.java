package com.xike.xkliveplay.framework.tools;

import android.annotation.SuppressLint;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author LiWei <br>
 * CreateTime: 2015年1月6日 下午2:00:22<br>
 * <b>Info:</b><br>
 *<br>
 *   <b>Method:</b> <br>
 *   <b>Public:</b> <br>
 *   <b>Private:</b> <br>
 */
public class P2pSettingTool 
{
	@SuppressLint("SdCardPath")
	private static final String FILE_DIR = "/mnt/sdcard/p2p";
	private static final String FILE_NAME = "p2pconfig.txt";
	public static String FILE_CONTENT = "front_move=10";
	private static String fileName = "";
	
	/**
	 * 
	 * function: 通过isWrite来判断是否需要强制重写p2pSetting 
	 * @param
	 * @return
	 */
	public static void writeSettingFile(boolean isWrite)
	{
		File dir = new File(FILE_DIR);
		if (!dir.exists()) 
		{
			dir.mkdirs();
		}
		
		fileName = FILE_DIR + File.separator + FILE_NAME;
		File file = new File(fileName);
		if (!file.exists()) 
		{
			writeFile(file);
		}else {
			if (isWrite) 
			{
				file.delete();
				writeFile(file);
			}
		}
	}
	
	private static void writeFile(File file)
	{
		try {
			file.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		print(FILE_CONTENT);
	}
	
	private static void print(String content)
	{
		FileWriter fw = null;
		BufferedWriter bw = null;
		try {
			fw = new FileWriter(fileName, false);
			bw = new BufferedWriter(fw);
			bw.write(content);
			bw.flush();
			bw.close();
			fw.close();
		} catch (IOException e) 
		{
			e.printStackTrace();
			try {
				if (bw!=null) 
				{
					bw.close();
					fw.close();
				}
			} catch (IOException e1) 
			{
				e1.printStackTrace();
			}
		}
	}
}
