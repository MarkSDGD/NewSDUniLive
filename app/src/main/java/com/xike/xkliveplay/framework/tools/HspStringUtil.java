package com.xike.xkliveplay.framework.tools;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class HspStringUtil {

	/**
	 * 字符串转换为ascii码
	 * */
	public static String stringToAscii(String value) {
		StringBuffer sbu = new StringBuffer();
		char[] chars = value.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			sbu.append((int) chars[i]);
		}
		return sbu.toString();
	}

	/**
	 * 字符串转换为ascii码，不足24位的补Ascii码0
	 * */
	public static String stringToAscii2(String value) {
		StringBuffer sbu = new StringBuffer();
		char[] chars = value.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			sbu.append((int) chars[i]);
		}
		if (sbu.length() < 24) {
			for (int i = sbu.length(); i < 24; i++) {
				sbu.append("0");
			}
		}
		return sbu.toString();
	}

	public static char ascii2Char(int ASCII) {
		return (char) ASCII;
	}

	public static String ascii2String(String ASCIIs) {
		String[] ASCIIss = ASCIIs.split(",");
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < ASCIIss.length; i++) {
			sb.append((char) ascii2Char(Integer.parseInt(ASCIIss[i])));
		}
		return sb.toString();
	}

	/**
	 * byte数组转换为16进制数
	 * */
	public static String bytesToHexString(byte[] src) {
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString().toUpperCase();
	}

	/**
	 * 替换xml文件中属性中的"<" 和 ">"
	 * */
	public static String getCorrectXmlString(String xmlString) {
//		System.out.println("xmlString的长度"+xmlString.length());
		String correctXmlString = xmlString;
		int namePos = xmlString.indexOf("programName =\"");
		if (namePos > -1) {
			String prexString = xmlString.substring(0, namePos
					+ "programName =\"".length());
			xmlString = xmlString.substring(namePos
					+ "programName =\"".length());
			
			
			int nameEndPos = xmlString.indexOf("\"");
			String name = xmlString.substring(0, nameEndPos);
			name = name.replace("<", "《");
			name = name.replace(">", "》");
			String suffixString = xmlString.substring(nameEndPos);
			suffixString = getCorrectXmlString(suffixString);
			correctXmlString = prexString + name + suffixString;
		}
		return correctXmlString;
	}

	private static String[] weeks = new String[] { "周日", "周一", "周二", "周三",
			"周四", "周五", "周六" };

	/**
	 * 取得包括当天的前四天和后三天
	 * */
//	public static List<TimeBean> getWeekTimeList() {
//		Calendar cal = Calendar.getInstance();
//		cal.add(Calendar.DAY_OF_MONTH, -4);
//		List<TimeBean> timeList = new ArrayList<TimeBean>();
//		for (int i = 0; i < 4; i++) {
//			cal.add(Calendar.DAY_OF_MONTH, 1);
//			int year = cal.get(Calendar.YEAR);
//			int month = cal.get(Calendar.MONTH);
//			int day = cal.get(Calendar.DAY_OF_MONTH);
//			int week = cal.get(Calendar.DAY_OF_WEEK);
//
//			TimeBean time = new TimeBean();
//			time.setMonthDay((month + 1) + "月" + day + "日");
//			time.setWeek(weeks[week - 1]);
//
//			String monthStr = "00" + (month + 1);
//			monthStr = monthStr.substring(monthStr.length() - 2);
//			String dayStr = "00" + day;
//			dayStr = dayStr.substring(dayStr.length() - 2);
//			time.setYmdDate(year + monthStr + dayStr);
//
//			timeList.add(time);
//		}
//		
//		Calendar cal2 = Calendar.getInstance();
//		cal2.add(Calendar.DAY_OF_MONTH, 0);
//		for (int i = 0; i < 3; i++) {
//			cal2.add(Calendar.DAY_OF_MONTH, 1);
//			int year = cal2.get(Calendar.YEAR);
//			int month = cal2.get(Calendar.MONTH);
//			int day = cal2.get(Calendar.DAY_OF_MONTH);
//			int week = cal2.get(Calendar.DAY_OF_WEEK);
//
//			TimeBean time = new TimeBean();
//			time.setMonthDay((month + 1) + "月" + day + "日");
//			time.setWeek(weeks[week - 1]);
//
//			String monthStr = "00" + (month + 1);
//			monthStr = monthStr.substring(monthStr.length() - 2);
//			String dayStr = "00" + day;
//			dayStr = dayStr.substring(dayStr.length() - 2);
//			time.setYmdDate(year + monthStr + dayStr);
//
//			timeList.add(time);
//		}
//		
//
//		return timeList;
//
//	}

	private static String[] week2s = new String[] { "星期日", "星期一", "星期二", "星期三",
			"星期四", "星期五", "星期六" };

	/**
	 * 取得包括日期和时间以及星期
	 * */
//	public static String[] getWeekTimes() {
//		Calendar cal = Calendar.getInstance();
//		String[] times = new String[3];
//		int year = cal.get(Calendar.YEAR);
//		int month = cal.get(Calendar.MONTH);
//		int day = cal.get(Calendar.DAY_OF_MONTH);
//		int week = cal.get(Calendar.DAY_OF_WEEK);
//		int hour = cal.get(Calendar.HOUR_OF_DAY);
//		int minute = cal.get(Calendar.MINUTE);
//
//		times[0] = FormatUtils.getFillString(String.valueOf(year), "0", 4)
//				+ "年"
//				+ FormatUtils.getFillString(String.valueOf(month + 1), "0", 2)
//				+ "月" + FormatUtils.getFillString(String.valueOf(day), "0", 2)
//				+ "日";
//		times[1] = FormatUtils.getFillString(String.valueOf(hour), "0", 2)
//				+ ":"
//				+ FormatUtils.getFillString(String.valueOf(minute), "0", 2);
//		times[2] = week2s[week - 1];
//
//		return times;
//
//	}
	
	public static String getNotNullString(String str) {
		if (str==null) {
			return "";
		}
		return str.trim();
	}
	
	public static boolean isEmptyString(String str) {
		if (str==null || str.trim().length()==0 || "NULL".equals(str.trim().toUpperCase())) {
			return true;
		}
		return false;
	}
	
//	public static int getPositionFromList(String cmpStr, List<String> strList) {
//		int position = 0;
//		for (int i = 0; strList!=null && i < strList.size(); i++) {
//			if (cmpStr!=null && cmpStr.equals(strList.get(i))) {
//				return i;
//			}
//		}
//		return position;
//	}
	/***
	 * 新添加的Bean类型的判断方法
	 * */
//	public static int getPositionFromList(String cmpStr, List<SSQBean> beanList) {
//		int position = 0;
//		for (int i = 0; beanList!=null && i < beanList.size(); i++) {
//			if(beanList.get(i)!=null && cmpStr !=null
//					&& beanList.get(i).getName().equals(cmpStr)){//省
//				return i;
//			}
//		}
//		return position;
//	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String s = stringToAscii2("123456");
		// System.out.println(s);
		String xmlString = "<GetScheduleListResult totalCount =\"35\" currentCount =\"20\"><ScheduleList><Schedule contentId=\"000000072013120200000554\" channelId =\"CHANNEL00000001\" channelName=\"null\" programName =\"天使艾美丽(21)\" startDate =\"20131204\" startTime=\"140400\" duration=\"3060\" status=\"1\" description=\"\" playUrl=\"http://219.146.10.201:8081/00/SNM/CHANNEL00000001/index.m3u8?startTime=1386137040&amp;endTime=1386140100\"/></ScheduleList></GetScheduleListResult>";

//		System.out.println(getCorrectXmlString(xmlString));
	}
}
