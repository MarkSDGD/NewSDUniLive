/*
 * Copyright (C) 2012 www.amsoft.cn
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.xike.xkliveplay.framework.tools;

import android.annotation.SuppressLint;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

// TODO: Auto-generated Javadoc

/**
 * © 2012 amsoft.cn 名称：AbDateUtil.java 描述：日期处理类.
 * 
 * @author 还如一梦中
 * @version v1.0
 * @date：2013-01-18 下午11:52:13
 */
@SuppressLint("SimpleDateFormat")
public class AbDateUtil
{

	/** 时间日期格式化到年月日时分秒. */
	public static final String dateFormatYMDHMS = "yyyy-MM-dd HH:mm:ss";

	/** 时间日期格式化到年月日时分秒. */
	public static final String dateFormatymdhms = "yyyyMMddHHmmss";

	/** 时间日期格式化到年月日时分秒. */
	public static final String dateFormatymdhms1 = "yyyyMMdd235959";

	/** 时间日期格式化到年月日. */
	public static final String dateFormatYMD = "yyyy-MM-dd";

	/** 时间日期格式化到年月. */
	public static final String dateFormatYM = "yyyy-MM";

	/** 时间日期格式化到年月日时分. */
	public static final String dateFormatYMDHM = "yyyy-MM-dd HH:mm";

	/** 时间日期格式化到月日. */
	public static final String dateFormatMD = "MM/dd";
	/** 时间日期格式化到月日.M月d日 */
	public static final String dateFormatMd = "M月d日";
	/** 时间日期格式化到月日.M-d */
	public static final String dateFormatmd = "M-d";

	/** 时分秒. */
	public static final String dateFormatHMS = "HH:mm:ss";

	/** 时分. */
	public static final String dateFormatHM = "HH:mm";
	
	/** 分秒. */
	public static final String dateFormatMS = "mm:ss";

	/** 上午. */
	public static final String AM = "AM";

	/** 下午. */
	public static final String PM = "PM";

	private static String[] WeekName = new String[] { "周日", "周一", "周二", "周三", "周四", "周五", "周六" };

	/**
	 * 描述：String类型的日期时间转化为Date类型.
	 * 
	 * @param strDate
	 *            String形式的日期时间
	 * @param format
	 *            格式化字符串，如："yyyy-MM-dd HH:mm:ss"
	 * @return Date Date类型日期时间
	 */
	public static Date getDateByFormat(String strDate, String format)
	{
		SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
		Date date = null;
		try
		{
			date = mSimpleDateFormat.parse(strDate);
		} catch (ParseException e)
		{
			e.printStackTrace();
		}
		return date;
	}

	public static long getMillisOfCurDateTime(String dateTime)
	{
		Calendar c = Calendar.getInstance(Locale.CHINA);
		try
		{
			Date date = formatDate(dateTime, "yyyy-MM-dd HH:mm:ss");
			c.setTime(date);
			return c.getTimeInMillis();
		} catch (ParseException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return 0;
	}
	
	public static long getMillisOfStringDate(String dateTime,String dateFormat)
	{
		Calendar c = Calendar.getInstance(Locale.CHINA);
		try
		{
			Date date = formatDate(dateTime, dateFormat);
			c.setTime(date);
			return c.getTimeInMillis();
		} catch (ParseException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return 0;
	}

	public static Date formatDate(String date, String pattern) throws ParseException
	{
		if (null == pattern || "".equals(pattern))
		{
			return formatDate(date);
		}

		SimpleDateFormat sdf = new SimpleDateFormat(pattern);

		return sdf.parse(date);
	}

	public static Date formatDate(String date) throws ParseException
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.parse(date);
	}

	/** 获取时间 **/
	public static String getTime()
	{
		SimpleDateFormat sdf = new SimpleDateFormat("HH mm");
		return sdf.format(new Date());
	}

	/**
	 * 获取播放的时间段
	 * 
	 * @param startTime
	 *            : 开始时间 YYYY-MM-DD HH:MM
	 * @param timeLongth
	 *            : 分钟
	 * @return
	 */
	public static String getPlayTime(String startTime, String timeLongth)
	{
		if (AbStrUtil.isEmpty(startTime) || AbStrUtil.isEmpty(timeLongth))
		{
			return "";
		}
		Date startDate = null;
		String info = "";
		try
		{
			startDate = formatDate(startTime, dateFormatYMDHMS);
			if (AbStrUtil.isNumber(timeLongth) && !timeLongth.equals("-"))
			{
				Date endDate = add(startDate, Calendar.MINUTE, Integer.valueOf(timeLongth));
				info = parseDate(startDate, "HH:mm") + "～" + parseDate(endDate, "HH:mm");
			} else
			{
				info = parseDate(startDate, "HH:mm") + "～" + "00:00";
			}

		} catch (ParseException e)
		{
			info = "00:00 ~ 00:00";
			e.printStackTrace();
		}

		return info;
	}

	/**
	 * 按指定规则进行添加
	 * 
	 * @param date
	 * @param field
	 * @param num
	 * @return
	 */
	public static Date add(Date date, int field, int num)
	{
		Calendar c = setCalendar(date);
		c.add(field, num);
		return c.getTime();
	}

	/**
	 * 设置日历时间
	 * 
	 * @param date
	 * @return
	 */
	public static Calendar setCalendar(Date date)
	{
		Calendar c = Calendar.getInstance(Locale.CHINA);
		c.setTime(date);
		return c;
	}

	public static String parseDate(Date date, String pattern)
	{
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		try
		{
			return format.format(date);
		} catch (Exception e)
		{
			return null;
		}
	}

	/**
	 * 描述：获取偏移之后的Date.
	 * 
	 * @param date
	 *            日期时间
	 * @param calendarField
	 *            Calendar属性，对应offset的值，
	 *            如(Calendar.DATE,表示+offset天,Calendar.HOUR_OF_DAY,表示＋offset小时)
	 * @param offset
	 *            偏移(值大于0,表示+,值小于0,表示－)
	 * @return Date 偏移之后的日期时间
	 */
	public Date getDateByOffset(Date date, int calendarField, int offset)
	{
		Calendar c = new GregorianCalendar();
		try
		{
			c.setTime(date);
			c.add(calendarField, offset);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return c.getTime();
	}

	/**
	 * 描述：获取指定日期时间的字符串(可偏移).
	 * 
	 * @param strDate
	 *            String形式的日期时间
	 * @param format
	 *            格式化字符串，如："yyyy-MM-dd HH:mm:ss"
	 * @param calendarField
	 *            Calendar属性，对应offset的值，
	 *            如(Calendar.DATE,表示+offset天,Calendar.HOUR_OF_DAY,表示＋offset小时)
	 * @param offset
	 *            偏移(值大于0,表示+,值小于0,表示－)
	 * @return String String类型的日期时间
	 */
	public static String getStringByOffset(String strDate, String format, int calendarField, int offset)
	{
		String mDateTime = null;
		try
		{
			Calendar c = new GregorianCalendar();
			SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
			c.setTime(mSimpleDateFormat.parse(strDate));
			c.add(calendarField, offset);
			mDateTime = mSimpleDateFormat.format(c.getTime());
		} catch (ParseException e)
		{
			e.printStackTrace();
		}
		return mDateTime;
	}

	/**
	 * 描述：Date类型转化为String类型(可偏移).
	 * 
	 * @param date
	 *            the date
	 * @param format
	 *            the format
	 * @param calendarField
	 *            the calendar field
	 * @param offset
	 *            the offset
	 * @return String String类型日期时间
	 */
	public static String getStringByOffset(Date date, String format, int calendarField, int offset)
	{
		String strDate = null;
		try
		{
			Calendar c = new GregorianCalendar();
			SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
			c.setTime(date);
			c.add(calendarField, offset);
			strDate = mSimpleDateFormat.format(c.getTime());
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return strDate;
	}

	/**
	 * 描述：Date类型转化为String类型.
	 * 
	 * @param date
	 *            the date
	 * @param format
	 *            the format
	 * @return String String类型日期时间
	 */
	public static String getStringByFormat(Date date, String format)
	{
		SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
		String strDate = null;
		try
		{
			strDate = mSimpleDateFormat.format(date);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return strDate;
	}

	/**
	 * 描述：获取指定日期时间的字符串,用于导出想要的格式.
	 * 
	 * @param strDate
	 *            String形式的日期时间，必须为yyyy-MM-dd HH:mm:ss格式
	 * @param format
	 *            输出格式化字符串，如："yyyy-MM-dd HH:mm:ss"
	 * @return String 转换后的String类型的日期时间
	 */
	public static String getStringByFormat(String strDate, String format)
	{
		String mDateTime = null;
		try
		{
			Calendar c = new GregorianCalendar();
			SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
			c.setTime(mSimpleDateFormat.parse(strDate));
			SimpleDateFormat mSimpleDateFormat2 = new SimpleDateFormat(format);
			mDateTime = mSimpleDateFormat2.format(c.getTime());
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return mDateTime;
	}
	
	public static String getStringByFormat(String strDate, String informat ,String outformat)
	{
		String mDateTime = null;
		try
		{
			Calendar c = new GregorianCalendar();
			SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(informat);
			c.setTime(mSimpleDateFormat.parse(strDate));
			SimpleDateFormat mSimpleDateFormat2 = new SimpleDateFormat(outformat);
			mDateTime = mSimpleDateFormat2.format(c.getTime());
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return mDateTime;
	}
	
	

	/**
	 * 描述：获取milliseconds表示的日期时间的字符串.
	 * 
	 * @param milliseconds
	 *            the milliseconds
	 * @param format
	 *            格式化字符串，如："yyyy-MM-dd HH:mm:ss"
	 * @return String 日期时间字符串
	 */
	public static String getStringByFormat(long milliseconds, String format)
	{
		String thisDateTime = null;
		try
		{
			SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
			thisDateTime = mSimpleDateFormat.format(milliseconds);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return thisDateTime;
	}

	/**
	 * 描述：获取表示当前日期时间的字符串.
	 * 
	 * @param format
	 *            格式化字符串，如："yyyy-MM-dd HH:mm:ss"
	 * @return String String类型的当前日期时间
	 */
	public static String getCurrentDate(String format)
	{
		String curDateTime = null;
		try
		{
			SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
			Calendar c = new GregorianCalendar();
			curDateTime = mSimpleDateFormat.format(c.getTime());
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return curDateTime;

	}

	/**
	 * 描述：获取表示当前日期时间的字符串(可偏移).
	 * 
	 * @param format
	 *            格式化字符串，如："yyyy-MM-dd HH:mm:ss"
	 * @param calendarField
	 *            Calendar属性，对应offset的值，
	 *            如(Calendar.DATE,表示+offset天,Calendar.HOUR_OF_DAY,表示＋offset小时)
	 * @param offset
	 *            偏移(值大于0,表示+,值小于0,表示－)
	 * @return String String类型的日期时间
	 */
	public static String getCurrentDateByOffset(String format, int calendarField, int offset)
	{
		String mDateTime = null;
		try
		{
			SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
			Calendar c = new GregorianCalendar();
			c.add(calendarField, offset);
			mDateTime = mSimpleDateFormat.format(c.getTime());
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return mDateTime;

	}

	/**
	 * 描述：计算两个日期所差的天数.
	 * 
	 * @param milliseconds1
	 *            the milliseconds1
	 * @param milliseconds2
	 *            the milliseconds2
	 * @return int 所差的天数
	 */
	public static int getOffectDay(long milliseconds1, long milliseconds2)
	{
		Calendar calendar1 = Calendar.getInstance();
		calendar1.setTimeInMillis(milliseconds1);
		Calendar calendar2 = Calendar.getInstance();
		calendar2.setTimeInMillis(milliseconds2);
		// 先判断是否同年
		int y1 = calendar1.get(Calendar.YEAR);
		int y2 = calendar2.get(Calendar.YEAR);
		int d1 = calendar1.get(Calendar.DAY_OF_YEAR);
		int d2 = calendar2.get(Calendar.DAY_OF_YEAR);
		int maxDays = 0;
		int day = 0;
		if (y1 - y2 > 0)
		{
			maxDays = calendar2.getActualMaximum(Calendar.DAY_OF_YEAR);
			day = d1 - d2 + maxDays;
		} else if (y1 - y2 < 0)
		{
			maxDays = calendar1.getActualMaximum(Calendar.DAY_OF_YEAR);
			day = d1 - d2 - maxDays;
		} else
		{
			day = d1 - d2;
		}
		return day;
	}

	/**
	 * 描述：计算两个日期所差的小时数.
	 * 
	 * @param date1
	 *            第一个时间的毫秒表示
	 * @param date2
	 *            第二个时间的毫秒表示
	 * @return int 所差的小时数
	 */
	public static int getOffectHour(long date1, long date2)
	{
		Calendar calendar1 = Calendar.getInstance();
		calendar1.setTimeInMillis(date1);
		Calendar calendar2 = Calendar.getInstance();
		calendar2.setTimeInMillis(date2);
		int h1 = calendar1.get(Calendar.HOUR_OF_DAY);
		int h2 = calendar2.get(Calendar.HOUR_OF_DAY);
		int h = 0;
		int day = getOffectDay(date1, date2);
		h = h1 - h2 + day * 24;
		return h;
	}

	/**
	 * 描述：计算两个日期所差的分钟数.
	 * 
	 * @param date1
	 *            第一个时间的毫秒表示
	 * @param date2
	 *            第二个时间的毫秒表示
	 * @return int 所差的分钟数
	 */
	public static int getOffectMinutes(long date1, long date2)
	{
		Calendar calendar1 = Calendar.getInstance();
		calendar1.setTimeInMillis(date1);
		Calendar calendar2 = Calendar.getInstance();
		calendar2.setTimeInMillis(date2);
		int m1 = calendar1.get(Calendar.MINUTE);
		int m2 = calendar2.get(Calendar.MINUTE);
		int h = getOffectHour(date1, date2);
		int m = 0;
		m = m1 - m2 + h * 60;
		return m;
	}

	/**
	 * 描述：获取本周一.
	 * 
	 * @param format
	 *            the format
	 * @return String String类型日期时间
	 */
	public static String getFirstDayOfWeek(String format)
	{
		return getDayOfWeek(format, Calendar.MONDAY);
	}

	/**
	 * 描述：获取本周日.
	 * 
	 * @param format
	 *            the format
	 * @return String String类型日期时间
	 */
	public static String getLastDayOfWeek(String format)
	{
		return getDayOfWeek(format, Calendar.SUNDAY);
	}

	/**
	 * 描述：获取本周的某一天.
	 * 
	 * @param format
	 *            the format
	 * @param calendarField
	 *            the calendar field
	 * @return String String类型日期时间
	 */
	private static String getDayOfWeek(String format, int calendarField)
	{
		String strDate = null;
		try
		{
			Calendar c = new GregorianCalendar();
			SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
			int week = c.get(Calendar.DAY_OF_WEEK);
			if (week == calendarField)
			{
				strDate = mSimpleDateFormat.format(c.getTime());
			} else
			{
				int offectDay = calendarField - week;
				if (calendarField == Calendar.SUNDAY)
				{
					offectDay = 7 - Math.abs(offectDay);
				}
				c.add(Calendar.DATE, offectDay);
				strDate = mSimpleDateFormat.format(c.getTime());
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return strDate;
	}

	/**
	 * 描述：获取本月第一天.
	 * 
	 * @param format
	 *            the format
	 * @return String String类型日期时间
	 */
	public static String getFirstDayOfMonth(String format)
	{
		String strDate = null;
		try
		{
			Calendar c = new GregorianCalendar();
			SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
			// 当前月的第一天
			c.set(GregorianCalendar.DAY_OF_MONTH, 1);
			strDate = mSimpleDateFormat.format(c.getTime());
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return strDate;

	}

	/**
	 * 描述：获取本月最后一天.
	 * 
	 * @param format
	 *            the format
	 * @return String String类型日期时间
	 */
	public static String getLastDayOfMonth(String format)
	{
		String strDate = null;
		try
		{
			Calendar c = new GregorianCalendar();
			SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
			// 当前月的最后一天
			c.set(Calendar.DATE, 1);
			c.roll(Calendar.DATE, -1);
			strDate = mSimpleDateFormat.format(c.getTime());
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return strDate;
	}

	/**
	 * 描述：获取表示当前日期的0点时间毫秒数.
	 * 
	 * @return the first time of day
	 */
	public static long getFirstTimeOfDay()
	{
		Date date = null;
		try
		{
			String currentDate = getCurrentDate(dateFormatYMD);
			date = getDateByFormat(currentDate + " 00:00:00", dateFormatYMDHMS);
			return date.getTime();
		} catch (Exception e)
		{
		}
		return -1;
	}

	/**
	 * 描述：获取表示当前日期24点时间毫秒数.
	 * 
	 * @return the last time of day
	 */
	public static long getLastTimeOfDay()
	{
		Date date = null;
		try
		{
			String currentDate = getCurrentDate(dateFormatYMD);
			date = getDateByFormat(currentDate + " 24:00:00", dateFormatYMDHMS);
			return date.getTime();
		} catch (Exception e)
		{
		}
		return -1;
	}

	/**
	 * 描述：判断是否是闰年()
	 * <p>
	 * (year能被4整除 并且 不能被100整除) 或者 year能被400整除,则该年为闰年.
	 * 
	 * @param year
	 *            年代（如2012）
	 * @return boolean 是否为闰年
	 */
	public static boolean isLeapYear(int year)
	{
		if ((year % 4 == 0 && year % 400 != 0) || year % 400 == 0)
		{
			return true;
		} else
		{
			return false;
		}
	}

	/**
	 * 描述：根据时间返回格式化后的时间的描述. 小于1小时显示多少分钟前 大于1小时显示今天＋实际日期，大于今天全部显示实际时间
	 * 
	 * @param strDate
	 *            the str date
	 * @param outFormat
	 *            the out format
	 * @return the string
	 */
	public static String formatDateStr2Desc(String strDate, String outFormat)
	{

		DateFormat df = new SimpleDateFormat(dateFormatYMDHMS);
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		try
		{
			c2.setTime(df.parse(strDate));
			c1.setTime(new Date());
			int d = getOffectDay(c1.getTimeInMillis(), c2.getTimeInMillis());
			if (d == 0)
			{
				int h = getOffectHour(c1.getTimeInMillis(), c2.getTimeInMillis());
				if (h > 0)
				{
					return "今天" + getStringByFormat(strDate, dateFormatHM);
					// return h + "小时前";
				} else if (h < 0)
				{
					// return Math.abs(h) + "小时后";
				} else if (h == 0)
				{
					int m = getOffectMinutes(c1.getTimeInMillis(), c2.getTimeInMillis());
					if (m > 0)
					{
						return m + "分钟前";
					} else if (m < 0)
					{
						// return Math.abs(m) + "分钟后";
					} else
					{
						return "刚刚";
					}
				}

			} else if (d > 0)
			{
				if (d == 1)
				{
					// return "昨天"+getStringByFormat(strDate,outFormat);
				} else if (d == 2)
				{
					// return "前天"+getStringByFormat(strDate,outFormat);
				}
			} else if (d < 0)
			{
				if (d == -1)
				{
					// return "明天"+getStringByFormat(strDate,outFormat);
				} else if (d == -2)
				{
					// return "后天"+getStringByFormat(strDate,outFormat);
				} else
				{
					// return Math.abs(d) +
					// "天后"+getStringByFormat(strDate,outFormat);
				}
			}

			String out = getStringByFormat(strDate, outFormat);
			if (!AbStrUtil.isEmpty(out))
			{
				return out;
			}
		} catch (Exception e)
		{
		}

		return strDate;
	}

	/**
	 * 取指定日期为星期几.
	 * 
	 * @param strDate
	 *            指定日期
	 * @param inFormat
	 *            指定日期格式
	 * @return String 星期几
	 */
	public static String getWeekNumber(String strDate, String inFormat)
	{
		String week = "星期日";
		Calendar calendar = new GregorianCalendar();
		DateFormat df = new SimpleDateFormat(inFormat);
		try
		{
			calendar.setTime(df.parse(strDate));
		} catch (Exception e)
		{
			return "错误";
		}
		int intTemp = calendar.get(Calendar.DAY_OF_WEEK) - 1;
		switch (intTemp)
		{
		case 0:
			week = "星期日";
			break;
		case 1:
			week = "星期一";
			break;
		case 2:
			week = "星期二";
			break;
		case 3:
			week = "星期三";
			break;
		case 4:
			week = "星期四";
			break;
		case 5:
			week = "星期五";
			break;
		case 6:
			week = "星期六";
			break;
		}
		return week;
	}

	/**
	 * 根据给定的日期判断是否为上下午.
	 * 
	 * @param strDate
	 *            the str date
	 * @param format
	 *            the format
	 * @return the time quantum
	 */
	public static String getTimeQuantum(String strDate, String format)
	{
		Date mDate = getDateByFormat(strDate, format);
		@SuppressWarnings("deprecation")
		int hour = mDate.getHours();
		if (hour >= 12)
			return "PM";
		else
			return "AM";
	}

	/**
	 * 根据给定的毫秒数算得时间的描述.
	 * 
	 * @param milliseconds
	 *            the milliseconds
	 * @return the time description
	 */
	public static String getTimeDescription(long milliseconds)
	{
		if (milliseconds > 1000)
		{
			// 大于一分
			if (milliseconds / 1000 / 60 > 1)
			{
				long minute = milliseconds / 1000 / 60;
				long second = milliseconds / 1000 % 60;
				return minute + "分" + second + "秒";
			} else
			{
				// 显示秒
				return milliseconds / 1000 + "秒";
			}
		} else
		{
			return milliseconds + "毫秒";
		}
	}

	/**
	 * Purpose: 将毫秒转换成分秒形式，如“1小时30分58秒” Create Time: 2014-8-28 下午2:19:18
	 * 
	 * @param L
	 *            毫秒
	 * @return Version: 1.0
	 */
	public static String formatLongToTimeStr(Long L)
	{
		String time = "";
		int hour = 0;
		int minute = 0;
		int second = 0;

		second = L.intValue() / 1000;

		if (second > 60)
		{
			minute = second / 60;
			second = second % 60;
		}
		if (minute > 60)
		{
			hour = minute / 60;
			minute = minute % 60;
		}
		if (hour == 0)
		{
			time = minute + "分" + second + "秒";
		} else
		{
			time = hour + "小时" + minute + "分" + second + "秒";
		}
		return time;
	}
	

	/**
	 * 返回日历的日期字符串（格式："yyyy-mm-dd"）
	 * 
	 * @param cal
	 *            Calendar
	 * @return String
	 */
	public static String getDate(Calendar cal)
	{
		return getYear(cal) + "-" + getMonth(cal) + "-" + getDay(cal);
	}

	/**
	 * 返回日历的日期字符串（格式："yyyy-mm-dd"）
	 * 
	 * @param cal
	 *            Calendar
	 * @return String
	 */
	public static String getDateStrLikedateFormatYMD(Calendar cal)
	{
		return getYear(cal) + "-" + getMonth(cal) + "-" + getDay(cal);
	}

	/**
	 * 返回日历的日期字符串（格式："yyyy-MM-dd HH:mm:ss"）
	 * 
	 * @param cal
	 *            Calendar
	 * @return String
	 */
	public static String getdateStrLikedateFormatYMDHMS(Calendar cal)
	{
		return getYear(cal) + "-" + getMonth(cal) + "-" + getDay(cal) + " " + getHour(cal) + ":" + getMinute(cal) + ":" + getSecond(cal);
	}

	/**
	 * 返回日历的年字符串
	 * 
	 * @param cal
	 *            Calendar
	 * @return String
	 */
	public static String getYear(Calendar cal)
	{
		return String.valueOf(cal.get(Calendar.YEAR));
	}

	/**
	 * 返回日历的月字符串(两位)
	 * 
	 * @param cal
	 *            Calendar
	 * @return String
	 */
	public static String getMonth(Calendar cal)
	{
		return strLen(String.valueOf(cal.get(Calendar.MONTH) + 1), 2);
	}

	/**
	 * 返回日历的日字符串(两位)
	 * 
	 * @param cal
	 *            Calendar
	 * @return String
	 */
	public static String getDay(Calendar cal)
	{
		return strLen(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)), 2);
	}

	/**
	 * @param s
	 *            String
	 * @param len
	 *            int
	 * @return String
	 */
	public static String strLen(String s, int len)
	{
		String ret = s;
		if (AbStrUtil.isEmpty(ret))
		{
			ret = "";
		}
		if (ret.length() == 8)
		{
			return ret;
		}
		for (int i = 0; i < len - ret.length(); i++)
		{
			ret = "0" + ret;
			if (ret.length() == 8)
			{
				break;
			}
		}
		return ret;
	}

	/** 根据传入的差，获取和当前天差多少天的时间 ,返回格式为yyyy-mm-dd **/
	public static String getPreAndNextDate(int preNum)
	{
		String result = null;
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, preNum);
		result = getDate(cal);
		return result;
	}

	/** 根据传入的差，获取和当前天差多少天的时间 ,返回格式为yyyy-mm-dd **/
	public static String getPreAndNextDateLikedateFormatYMD(int preNum)
	{
		String result = null;
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, preNum);
		result = getDateStrLikedateFormatYMD(cal);
		return result;
	}

	/** 根据传入的差，获取和当前天差多少天的时间,返回格式为yyyy-MM-dd HH:mm:ss **/
	public static String getPreAndNextDateLikedateFormatYMDHMS(int preNum)
	{
		String result = null;
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, preNum);
		result = getdateStrLikedateFormatYMDHMS(cal);
		return result;
	}

	public static int getWeek()
	{
		Calendar calendar = Calendar.getInstance();
		return calendar.get(Calendar.DAY_OF_WEEK);
	}

	/** 获取星期 **/
	public static String getWeekString()
	{
		int index = getCalendarWeek();
		String[] curWeek = { "", "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
		return curWeek[index];
	}

	/** 获得当前周几 **/
	public static int getCalendarWeek()
	{
		return Calendar.getInstance(Locale.CHINA).get(Calendar.DAY_OF_WEEK);
	}

	public static String getPreAndNextWeek(int index)
	{
		int week = getWeek();
		int cur = week + index;
		if (cur > 7)
		{
			cur = cur - 7;
		}
		if (cur < 1)
		{
			cur = cur + 7;
		}

		return WeekName[cur - 1];
	}

	/**
	 * 取当前日期的整形串，格式为"yyyymmddhhmmss"
	 * 
	 * @return String
	 */
	public static String getNumDate2()
	{
		Calendar now = Calendar.getInstance();
		return getDateStr4(now);
	}

	/**
	 * 返回日历的日期时间字符串（格式："yyyymmddhhmmss"�?
	 * 
	 * @param cal
	 *            Calendar
	 * @return String
	 */
	public static String getDateStr4(Calendar cal)
	{
		return getYear(cal) + getMonth(cal) + getDay(cal) + getHour(cal) + getMinute(cal) + getSecond(cal);
	}

	/**
	 * 返回日历的时字符串(两位)
	 * 
	 * @param cal
	 *            Calendar
	 * @return String
	 */
	public static String getHour(Calendar cal)
	{
		return strLen(String.valueOf(cal.get(Calendar.HOUR_OF_DAY)), 2);
	}

	/**
	 * 返回日历的分字符串(两位)
	 * 
	 * @param cal
	 *            Calendar
	 * @return String
	 */
	public static String getMinute(Calendar cal)
	{
		return strLen(String.valueOf(cal.get(Calendar.MINUTE)), 2);
	}

	public static String getSecond(Calendar cal)
	{
		return strLen(String.valueOf(cal.get(Calendar.SECOND)), 2);
	}

	/**
	 * 
	 * getTodayBeginTime
	 * 
	 * @Title: getTodayBeginTime
	 * @Description:一天当中最早时刻：0:0:0
	 * @return
	 * @return Calendar
	 */
	public static Calendar getTodayBeginTime()
	{

		Calendar todayBegin = Calendar.getInstance();
		todayBegin.set(Calendar.HOUR_OF_DAY, 0);
		todayBegin.set(Calendar.MINUTE, 0);
		todayBegin.set(Calendar.SECOND, 0);

		return todayBegin;
	}

	/**
	 * 
	 * getTodayEndTime
	 * 
	 * @Title: getTodayEndTime
	 * @Description:一天当中最晚时刻：23:59:59
	 * @return
	 * @return Calendar
	 */
	public static Calendar getTodayEndTime()
	{
		Calendar todayEnd = Calendar.getInstance();
		todayEnd.set(Calendar.HOUR_OF_DAY, 23);
		todayEnd.set(Calendar.MINUTE, 59);
		todayEnd.set(Calendar.SECOND, 59);

		return todayEnd;
	}

	/**
	 * 
	 * isValidDate(这里用一句话描述这个方法的作用)
	 * 
	 * @Title: isValidDate
	 * @Description:给定的日期格式是否符合给定的日期格式
	 * @param s
	 * @return
	 * @return boolean
	 */
	public static boolean isValidDate(String date, String Format)
	{
		SimpleDateFormat sdf = new SimpleDateFormat(Format);
		try
		{
			sdf.parse(date);
			return true;
		} catch (Exception e)
		{
			// 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
			return false;
		}
	}

	/**
	 * 
	 * @Title: compareDate <br>
	 * @Description: 用于比较给定的日期是小于今天还是大于今天， <br>
	 * @param @param strDate 格式为"yyyy-MM-dd HH:mm:ss"
	 * @param @return <br>
	 * @return int 负数代表今天以前，0代表今天，正数代表大于今天<br>
	 * @throws <br>
	 */
	public static int compareDate(String strDate)
	{
		int returnVal = 0;

		try
		{
			Date date1 = getDateByFormat(strDate, dateFormatYMDHMS);

			Date date2 = getDateByFormat(getCurrentDate(dateFormatYMD) + " 00:00:00", dateFormatYMDHMS);

			returnVal = (int) ((date1.getTime() - date2.getTime()) / (1000 * 60 * 60 * 24));
		} catch (Exception e)
		{
			e.printStackTrace();
		}

		return returnVal;
	}

	/** 获得公历年 **/
	public static int getCalendarYear()
	{
		return Calendar.getInstance(Locale.CHINA).get(Calendar.YEAR);
	}

	/** 获得公历月 **/
	public static int getCalendarMonth()
	{
		return Calendar.getInstance(Locale.CHINA).get(Calendar.MONTH) + 1;
	}

	/** 获得公历日 **/
	public static int getCalendarDay()
	{
		return Calendar.getInstance(Locale.CHINA).get(Calendar.DATE);
	}

	/** 获得公历当年的月份一共有多少天 **/
	public static int getMonthDays(int year, int month)
	{
		if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12)
		{
			return 31;
		}

		if (month == 4 || month == 6 || month == 9 || month == 11)
		{
			return 30;
		}

		if (month == 2)
		{
			if (isLeapYear(year))
			{
				return 29;
			} else
			{
				return 28;
			}
		}

		return 30;
	}

	/** 得到当前年第几月一共有多少天 **/
	public static int getDaysToMonth(int year, int month)
	{
		int days = 0;
		for (int i = 1; i < month; i++)
		{
			days += AbDateUtil.getMonthDays(year, i);
		}
		return days;
	}

	/** 获取日期 **/
	public static String getDate()
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(new Date()).replace("-", "/");
	}

	/**
	 * The main method.
	 * 
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args)
	{
		System.out.println(formatDateStr2Desc("2012-3-2 12:2:20", "MM月dd日  HH:mm"));
	}

    /**
     * addDayInCurDate(获得当前日期的前后多少天)
   
     * @param addDays
     * @return  
     * @return String    返回类型
     * @throws  异常处理
     * @modifyHistory  createBy zhangwei
    */
   public static String addDayInCurDate(int addDays)
   {
       String dates = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
       return  addDay(dates,addDays);
   }
	
	/**
	  * addDay(获得指定日期的)
	
	  * @param dateString: 当前日期
	  * @param addDays  : 增加或者减少多少天
	  * @return  
	  * @return String    返回类型
	  * @throws  异常处理
	  * @modifyHistory  createBy zhangwei
	 */
  public static String addDay(String dateString, int addDays) {   
      try {   
          SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");   
          Calendar cd = Calendar.getInstance();   
          cd.setTime(sdf.parse(dateString));   
          cd.add(Calendar.DATE, addDays);//增加一天   
          //cd.add(Calendar.MONTH, n);//增加一个月   

          return sdf.format(cd.getTime());   

      } catch (Exception e) {   
          return null;   
      }   
  }  
}
