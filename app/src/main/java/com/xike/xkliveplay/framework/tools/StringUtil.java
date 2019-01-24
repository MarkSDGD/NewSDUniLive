package com.xike.xkliveplay.framework.tools;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <字符串操作>
 * 
 * @author cKF46828
 * @version [版本号, 2011-7-5]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public final class StringUtil {

	private StringUtil() {
	}

	/**
	 * 是否为null或空字符串
	 * 
	 * @param str
	 * @return [参数说明]
	 * @return boolean [返回类型说明]
	 * @exception throws [违例类型] [违例说明]
	 * @see [类、类#方法、类#成员]
	 */
	public static boolean isEmpty(String str) {
		if (str == null || "".equals(str.trim())) {
			return true;
		}
		return false;
	}

	/**
	 * 是否为null或空字符串 创建人：pananz 创建时间：2013-1-5 - 上午11:21:15
	 * 
	 * @param str
	 * @return 返回类型：boolean
	 * @exception throws
	 */
	public static boolean isNotEmpty(String str) {
		if (str == null || "".equals(str.trim())) {
			return false;
		}
		return true;
	}

	/**
	 * <判断是否为手机号码>
	 * 
	 * @param str
	 * @return boolean [返回类型说明]
	 * @exception throws [违例类型] [违例说明]
	 * @see [类、类#方法、类#成员]
	 */
	public static boolean isPhoneNumber(String phoneNumber) {
		String reg = "1[3,5,8]{1}\\d{9}";
		return phoneNumber.matches(reg);
	}

	/**
	 * <判断是否是数字>
	 * 
	 * @param str
	 * @return boolean [返回类型说明]
	 * @exception throws [违例类型] [违例说明]
	 * @see [类、类#方法、类#成员]
	 */
	public static boolean isNumber(String str) {
		String reg = "[0-9]+";
		return str.matches(reg);
	}

	/**
	 * 字符串转为整数(如果转换失败,则返回 -1)
	 * 
	 * @param str
	 * @return [参数说明]
	 * @return int [返回类型说明]
	 * @exception throws [违例类型] [违例说明]
	 * @see [类、类#方法、类#成员]
	 */
	public static int stringToInt(String str) {
		if (isEmpty(str)) {
			return -1;
		}
		try {
			return Integer.parseInt(str.trim());
		} catch (NumberFormatException e) {
			return -1;
		}
	}

	/**
	 * 字体串转为boolean (如果转换失败,则返回false)
	 * 
	 * @param str
	 * @return [参数说明]
	 * @return boolean [返回类型说明]
	 * @exception throws [违例类型] [违例说明]
	 * @see [类、类#方法、类#成员]
	 */
	public static boolean stringToBoolean(String str) {
		if (isEmpty(str)) {
			return false;
		}
		try {
			return Boolean.parseBoolean(str.trim());
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * boolean转为字体串
	 * 
	 * @param str
	 * @return boolean [返回类型说明]
	 * @exception throws [违例类型] [违例说明]
	 * @see [类、类#方法、类#成员]
	 */
	public static String BooleanToString(Boolean bool) {
		String booleanString = "false";
		if (bool) {
			booleanString = "true";
		}
		return booleanString;
	}

	/**
	 * <从异常中获取调用栈>
	 * 
	 * @param ex
	 * @return String [返回类型说明]
	 * @exception throws [违例类型] [违例说明]
	 * @see [类、类#方法、类#成员]
	 */
	public static String getExceptionStackTrace(Throwable ex) {
		Writer writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		ex.printStackTrace(printWriter);
		Throwable cause = ex.getCause();
		while (cause != null) {
			cause.printStackTrace(printWriter);
			cause = cause.getCause();
		}
		printWriter.close();
		String result = writer.toString();
		return result;
	}


	/**
	 * 字条串截取
	 * 
	 * @param str
	 *            源字符串
	 * @param start
	 *            开始位置
	 * @param end
	 *            结束位置
	 * @return [参数说明]
	 * @return String [返回类型说明]
	 * @exception throws [违例类型] [违例说明]
	 * @see [类、类#方法、类#成员]
	 */
	public static String substring(String str, int start, int end) {
		if (isEmpty(str)) {
			return "";
		}
		int len = str.length();
		if (start > end) {
			return "";
		}
		if (start > len) {
			return "";
		}
		if (end > len) {
			return str.substring(start, len);
		}
		return str.substring(start, end);
	}

	/**
	 * 字条串截取
	 * 
	 * @param str
	 *            源字符串
	 * @param start
	 *            开始位置
	 * @return [参数说明]
	 * @return String [返回类型说明]
	 * @exception throws [违例类型] [违例说明]
	 * @see [类、类#方法、类#成员]
	 */
	public static String substring(String str, int start) {
		if (isEmpty(str)) {
			return "";
		}
		int len = str.length();
		if (start > len) {
			return "";
		}
		return str.substring(start);
	}

	/**
	 * 流转换成字符串
	 * 
	 * @param input
	 *            [输入流]
	 * @return [参数说明]
	 * @return String [字符串]
	 */
	public static String streamToString(InputStream input) {

		StringBuffer sb = new StringBuffer();
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(input,
					"UTF-8"));
			String temp = br.readLine();
			while (null != temp && !"".equals(temp)) {
				sb.append(temp);
				temp = br.readLine();
			}
		} catch (IOException e) {
			return "";
		}
		return sb.toString();
	}

	public static long parseLong(String str) {
		try {
			return Long.valueOf(str);
		} catch (Exception e) {
			return 0;
		}
	}

	public static int parseInt(String str) {
		try {
			return Integer.valueOf(str);
		} catch (Exception e) {
			return 0;
		}
	}

	public static float parseFloat(String str) {
		try {
			return Float.valueOf(str);
		} catch (Exception e) {
			return 0;
		}
	}

	public static double parseDouble(String str) {
		try {
			return Double.valueOf(str);
		} catch (Exception e) {
			return 0;
		}
	}

	public static boolean isChinese(char c) {
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
		if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
				|| ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
				|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
				|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
			return true;
		}

		return false;

	}

	public static boolean isChinese(String strName) {
		char[] ch = strName.toCharArray();
		for (int i = 0; i < ch.length; i++) {
			char c = ch[i];
			if (isChinese(c) == true) {
				return true;
			}
		}
		return false;
	}

	public static boolean isMessyCode(String strName) {
		Pattern p = Pattern.compile("\\s*|\t*|\r*|\n*");
		Matcher m = p.matcher(strName);
		String after = m.replaceAll("");
		String temp = after.replaceAll("\\p{P}", "");
		char[] ch = temp.trim().toCharArray();
		float chLength = ch.length;
		float count = 0;
		for (int i = 0; i < ch.length; i++) {
			char c = ch[i];
			if (!Character.isLetterOrDigit(c)) {
				if (!isChinese(c)) {
					count = count + 1;
					System.out.println(c);
				}
			}
		}
		float result = count / chLength;
		if (result > 0.4) {
			return true;
		} else {
			return false;
		}

	}

	public static void main(String[] args) {
		System.out.println(isMessyCode("???f"));
	}
}
