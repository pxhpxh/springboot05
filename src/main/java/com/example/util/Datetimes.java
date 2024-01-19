package com.example.util;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.logging.log4j.util.Strings;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;

public class Datetimes {
	public static final String datetimeStyle = "yyyy-MM-dd HH:mm:ss";
	public static final String dateStyle = "yyyy-MM-dd";
	public static final String dateStyleWithoutYear = "MM-dd";
	public static final String datetimeStartWithMonthStyle = "MM-dd HH:mm";
	public static final String datetimeWithoutSecondStyle = "yyyy-MM-dd HH:mm";
	public static final String datetimeAllStyle = "yyyy-MM-dd HH:mm:ss.S";
	private static final int[] seasonFirstMonth = { 0, 0, 0, 3, 3, 3, 6, 6, 6, 9, 9, 9 };
	private static final int[] seasonLastMonth = { 2, 2, 2, 5, 5, 5, 8, 8, 8, 11, 11, 11 };

	public static Date getDateNullReplace(String dtStr,String replaceStr){
		SimpleDateFormat sdf=new SimpleDateFormat(datetimeStyle);
		try {
			return sdf.parse(dtStr);
		} catch (Exception e){
			try{
				return sdf.parse(replaceStr);
			} catch (Exception e1){
				return null;
			}
		}
	}
	public static Date getFirstDayInWeek(Date date, Locale locale)
	{
		GregorianCalendar calendar = new GregorianCalendar(locale);
		calendar.setTime(date);
		int firstDayOfWeek = calendar.getFirstDayOfWeek();

		int week = calendar.get(7);

		int dayOfWeek = 0;
		if (firstDayOfWeek == week) {
			dayOfWeek = 1;
		} else if (firstDayOfWeek > week) {
			dayOfWeek = 7 - week + firstDayOfWeek - 1;
		} else if (firstDayOfWeek < week) {
			dayOfWeek = week - firstDayOfWeek + 1;
		}
		calendar.add(7, 1 - dayOfWeek);
		calendar.set(11, calendar.getActualMinimum(11));
		calendar.set(12, calendar.getActualMinimum(12));
		calendar.set(13, calendar.getActualMinimum(13));
		calendar.set(14, 0);

		return calendar.getTime();
	}

	public static Date getFirstDayInWeek(Date date)
	{
		return getFirstDayInWeek(date, Locale.getDefault());
	}

	public static Date getFirstDayInWeek0(Date date)
	{
		Date date0 = getFirstDayInWeek(date, Locale.getDefault());
		return parse(formatNoTimeZone(date0, "yyyy-MM-dd HH:mm:ss"), "yyyy-MM-dd HH:mm:ss");
	}

	public static Date getLastDayInWeek(Date date, Locale locale)
	{
		GregorianCalendar calendar = new GregorianCalendar(locale);
		calendar.setTime(date);

		int firstDayOfWeek = calendar.getFirstDayOfWeek();
		int week = calendar.get(7);

		int dayOfWeek = 0;
		if (firstDayOfWeek == week) {
			dayOfWeek = 1;
		} else if (firstDayOfWeek > week) {
			dayOfWeek = 7 - week + firstDayOfWeek - 1;
		} else if (firstDayOfWeek < week) {
			dayOfWeek = week - firstDayOfWeek + 1;
		}
		calendar.add(7, 7 - dayOfWeek);
		calendar.set(11, calendar.getActualMaximum(11));
		calendar.set(12, calendar.getActualMaximum(12));
		calendar.set(13, calendar.getActualMaximum(13));
		calendar.set(14, 0);

		return calendar.getTime();
	}

	public static Date getLastDayInWeek(Date date)
	{
		return getLastDayInWeek(date, Locale.getDefault());
	}

	public static Date getLastDayInWeek0(Date date)
	{
		Date date0 = getLastDayInWeek(date, Locale.getDefault());
		return parse(formatNoTimeZone(date0, "yyyy-MM-dd HH:mm:ss"), "yyyy-MM-dd HH:mm:ss");
	}

	public static Date getFirstDayInMonth(Date date, Locale locale)
	{
		GregorianCalendar calendar = new GregorianCalendar(locale);
		calendar.setTime(date);

		int day = calendar.getActualMinimum(5);
		calendar.set(5, day);
		calendar.set(11, calendar.getActualMinimum(11));
		calendar.set(12, calendar.getActualMinimum(12));
		calendar.set(13, calendar.getActualMinimum(13));
		calendar.set(14, 0);

		return calendar.getTime();
	}

	public static Date getFirstDayInMonth(Date date)
	{
		return getFirstDayInMonth(date, Locale.getDefault());
	}

	public static Date getFirstDayInMonth2(Date date,String pattern)
	{
		Date date0 = getFirstDayInMonth(date, Locale.getDefault());
		return parse(formatNoTimeZone(date0, pattern), pattern);
	}

	public static Date getFirstDayInMonth0(Date date)
	{
		Date date0 = getFirstDayInMonth(date, Locale.getDefault());
		return parse(formatNoTimeZone(date0, "yyyy-MM-dd HH:mm:ss"), "yyyy-MM-dd HH:mm:ss");
	}

	public static Date getLastDayInMonth(Date date, Locale locale)
	{
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(date);

		int day = calendar.getActualMaximum(5);
		calendar.set(5, day);
		calendar.set(11, calendar.getActualMaximum(11));
		calendar.set(12, calendar.getActualMaximum(12));
		calendar.set(13, calendar.getActualMaximum(13));
		calendar.set(14, 0);

		return calendar.getTime();
	}

	public static Date getLastDayInMonth(Date date)
	{
		return getLastDayInMonth(date, Locale.getDefault());
	}

	public static Date getLastDayInMonth0(Date date)
	{
		Date date0 = getLastDayInMonth(date, Locale.getDefault());
		return parse(formatNoTimeZone(date0, "yyyy-MM-dd HH:mm:ss"), "yyyy-MM-dd HH:mm:ss");
	}

	public static Date getFirstDayInSeason(Date date, Locale locale)
	{
		GregorianCalendar calendar = new GregorianCalendar(locale);
		calendar.setTime(getStartDayInMonth(date));

		int currentMonth = calendar.get(2);

		calendar.set(2, seasonFirstMonth[currentMonth]);
		calendar.set(11, calendar.getActualMinimum(11));
		calendar.set(12, calendar.getActualMinimum(12));
		calendar.set(13, calendar.getActualMinimum(13));
		calendar.set(14, 0);


		GregorianCalendar calendar2 = new GregorianCalendar();
		calendar2.setTime(calendar.getTime());

		int day = calendar2.getActualMinimum(5);
		calendar2.set(5, day);
		calendar2.set(11, calendar2.getActualMinimum(11));
		calendar2.set(12, calendar2.getActualMinimum(12));
		calendar2.set(13, calendar2.getActualMinimum(13));
		calendar2.set(14, 0);

		return calendar2.getTime();
	}

	private static Date getStartDayInMonth(Date date)
	{
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(date);

		int day = calendar.getActualMinimum(5);
		calendar.set(5, day);
		calendar.set(11, calendar.getActualMinimum(11));
		calendar.set(12, calendar.getActualMinimum(12));
		calendar.set(13, calendar.getActualMinimum(13));
		calendar.set(14, 0);

		return calendar.getTime();
	}

	public static Date getFirstDayInSeason(Date date)
	{
		return getFirstDayInSeason(date, Locale.getDefault());
	}

	public static Date getFirstDayInSeason0(Date date)
	{
		Date date0 = getFirstDayInSeason(date, Locale.getDefault());
		return parse(formatNoTimeZone(date0, "yyyy-MM-dd HH:mm:ss"), "yyyy-MM-dd HH:mm:ss");
	}

	public static Date getLastDayInSeason(Date date, Locale locale)
	{
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(getStartDayInMonth(date));

		int currentMonth = calendar.get(2);

		calendar.set(2, seasonLastMonth[currentMonth]);
		calendar.set(11, calendar.getActualMaximum(11));
		calendar.set(12, calendar.getActualMaximum(12));
		calendar.set(13, calendar.getActualMaximum(13));
		calendar.set(14, 0);



		GregorianCalendar calendar2 = new GregorianCalendar();
		calendar2.setTime(calendar.getTime());

		int day = calendar2.getActualMaximum(5);
		calendar2.set(5, day);
		calendar2.set(11, calendar2.getActualMaximum(11));
		calendar2.set(12, calendar2.getActualMaximum(12));
		calendar2.set(13, calendar2.getActualMaximum(13));
		calendar2.set(14, 0);

		return calendar2.getTime();
	}

	public static Date getLastDayInSeason(Date date)
	{
		return getLastDayInSeason(date, Locale.getDefault());
	}

	public static Date getLastDayInSeason0(Date date)
	{
		Date date0 = getLastDayInSeason(date, Locale.getDefault());
		return parse(formatNoTimeZone(date0, "yyyy-MM-dd HH:mm:ss"), "yyyy-MM-dd HH:mm:ss");
	}

	public static Date getFirstDayInYear(Date date, Locale locale)
	{
		GregorianCalendar calendar = new GregorianCalendar(locale);
		calendar.setTime(date);

		calendar.set(2, calendar.getActualMinimum(2));
		calendar.set(11, calendar.getActualMinimum(11));
		calendar.set(12, calendar.getActualMinimum(12));
		calendar.set(13, calendar.getActualMinimum(13));
		calendar.set(14, 0);


		GregorianCalendar calendar2 = new GregorianCalendar();
		calendar2.setTime(calendar.getTime());

		int day = calendar.getActualMinimum(5);
		calendar2.set(5, day);
		calendar2.set(11, calendar2.getActualMinimum(11));
		calendar2.set(12, calendar2.getActualMinimum(12));
		calendar2.set(13, calendar2.getActualMinimum(13));
		calendar2.set(14, 0);

		return calendar2.getTime();
	}

	public static Date getFirstDayInYear(Date date)
	{
		return getFirstDayInYear(date, Locale.getDefault());
	}

	public static Date getFirstDayInYear0(Date date)
	{
		Date date0 = getFirstDayInYear(date, Locale.getDefault());
		return parse(formatNoTimeZone(date0, "yyyy-MM-dd HH:mm:ss"), "yyyy-MM-dd HH:mm:ss");
	}

	public static Date getLastDayInYear(Date date, Locale locale)
	{
		GregorianCalendar calendar = new GregorianCalendar(locale);
		calendar.setTime(date);

		calendar.set(2, calendar.getActualMaximum(2));
		calendar.set(11, calendar.getActualMaximum(11));
		calendar.set(12, calendar.getActualMaximum(12));
		calendar.set(13, calendar.getActualMaximum(13));
		calendar.set(14, 0);

		GregorianCalendar calendar2 = new GregorianCalendar();
		calendar2.setTime(calendar.getTime());

		int day = calendar2.getActualMaximum(5);
		calendar2.set(5, day);
		calendar2.set(11, calendar2.getActualMaximum(11));
		calendar2.set(12, calendar2.getActualMaximum(12));
		calendar2.set(13, calendar2.getActualMaximum(13));
		calendar2.set(14, 0);

		return calendar2.getTime();
	}

	public static Date getLastDayInYear(Date date)
	{
		return getLastDayInYear(date, Locale.getDefault());
	}

	public static Date getLastDayInYear0(Date date)
	{
		Date date0 = getLastDayInYear(date, Locale.getDefault());
		return parse(formatNoTimeZone(date0, "yyyy-MM-dd HH:mm:ss"), "yyyy-MM-dd HH:mm:ss");
	}

	public static Date getTodayFirstTime()
	{
		Calendar calendar = new GregorianCalendar();
		calendar.set(11, calendar.getActualMinimum(11));
		calendar.set(12, calendar.getActualMinimum(12));
		calendar.set(13, calendar.getActualMinimum(13));
		calendar.set(14, 0);

		return parse(formatNoTimeZone(calendar.getTime(), "yyyy-MM-dd HH:mm:ss"), "yyyy-MM-dd HH:mm:ss");
	}

	public static Date getTodayFirstTimeNoTimeZone()
	{
		Calendar calendar = new GregorianCalendar();
		calendar.set(11, calendar.getActualMinimum(11));
		calendar.set(12, calendar.getActualMinimum(12));
		calendar.set(13, calendar.getActualMinimum(13));
		calendar.set(14, 0);
		return calendar.getTime();
	}

	public static Date getTodayFirstTime(Date date)
	{
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.set(11, calendar.getActualMinimum(11));
		calendar.set(12, calendar.getActualMinimum(12));
		calendar.set(13, calendar.getActualMinimum(13));
		calendar.set(14, 0);

		return calendar.getTime();
	}

	public static Date getTodayFirstTime(String dateStr)
	{
		return getTodayFirstTime(parseNoTimeZone(dateStr, "yyyy-MM-dd"));
	}

	public static String getFirstTimeStr(String dateStr)
	{
		String date = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try
		{
			date = sdf.format(sdf.parse(dateStr)) + " 00:00:00";
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return date;
	}

	public static Date getFirstTime(Date date)
	{
		return parse(getFirstTimeStr(formatNoTimeZone(date, "yyyy-MM-dd")), "yyyy-MM-dd HH:mm:ss");
	}

	public static Date getFirstNoTimeZone(Date date)
	{
		return parseNoTimeZone(getFirstTimeStr(formatNoTimeZone(date, "yyyy-MM-dd")), "yyyy-MM-dd HH:mm:ss");
	}

	public static String getServiceFirstTime(String date)
	{
		return formatNoTimeZone(getFirstTime(parseNoTimeZone(date, null)), "yyyy-MM-dd HH:mm:ss");
	}

	public static Date getLastTime(Date date)
	{
		return parse(getLastTimeStr(formatNoTimeZone(date, "yyyy-MM-dd")), "yyyy-MM-dd HH:mm:ss");
	}

	public static Date getLastTimeNoTimeZone(Date date)
	{
		return parseNoTimeZone(getLastTimeStr(formatNoTimeZone(date, "yyyy-MM-dd")), "yyyy-MM-dd HH:mm:ss");
	}

	public static String getServiceLastTime(String date)
	{
		return formatNoTimeZone(getLastTime(parseNoTimeZone(date, null)), "yyyy-MM-dd HH:mm:ss");
	}

	public static Date getTodayLastTime()
	{
		Calendar calendar = new GregorianCalendar();
		calendar.set(11, calendar.getActualMaximum(11));
		calendar.set(12, calendar.getActualMaximum(12));
		calendar.set(13, calendar.getActualMaximum(13));
		calendar.set(14, 0);

		return parse(formatNoTimeZone(calendar.getTime(), "yyyy-MM-dd HH:mm:ss"), "yyyy-MM-dd HH:mm:ss");
	}

	public static Date getTodayLastTimeNoTimeZone()
	{
		Calendar calendar = new GregorianCalendar();
		calendar.set(11, calendar.getActualMaximum(11));
		calendar.set(12, calendar.getActualMaximum(12));
		calendar.set(13, calendar.getActualMaximum(13));
		calendar.set(14, 0);
		return calendar.getTime();
	}

	public static Date getTodayLastTime(Date date)
	{
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.set(11, calendar.getActualMaximum(11));
		calendar.set(12, calendar.getActualMaximum(12));
		calendar.set(13, calendar.getActualMaximum(13));
		calendar.set(14, 0);

		return calendar.getTime();
	}

	public static Date getTodayLastTime(String dateStr)
	{
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(parseNoTimeZone(dateStr, "yyyy-MM-dd"));
		calendar.set(11, calendar.getActualMaximum(11));
		calendar.set(12, calendar.getActualMaximum(12));
		calendar.set(13, calendar.getActualMaximum(13));
		calendar.set(14, 0);

		return calendar.getTime();
	}

	public static String getLastTimeStr(String dateStr)
	{
		String date = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try
		{
			date = sdf.format(sdf.parse(dateStr)) + " 23:59:59";
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return date;
	}

	public static String formatDatetime(Date datetime)
	{
		return format(datetime, "yyyy-MM-dd HH:mm:ss");
	}

	public static String formatDatetime(Date datetime, TimeZone timezone)
	{
		return format(datetime, "yyyy-MM-dd HH:mm:ss", timezone);
	}

	public static String formatDatetimeWithoutSecond(Date datetime)
	{
		return format(datetime, "yyyy-MM-dd HH:mm");
	}

	public static String formatDate(Date date)
	{
		if (date == null) {
			return null;
		}
		return format(date, "yyyy-MM-dd");
	}

	public static String format(Date date, String pattern)
	{
		if (date == null) {
			return null;
		}
		TimeZone timeZone = null;
		timeZone = TimeZone.getDefault();
		return DateFormatUtils.format(date, pattern, timeZone);
	}

	public static String formatNoTimeZone(Date date, String pattern)
	{
		if (date == null) {
			return null;
		}
		return DateFormatUtils.format(date, pattern);
	}

	public static String formatDate(Date date, Locale locale)
	{
		if (date == null) {
			return null;
		}
		return format(date, "yyyy-MM-dd", locale);
	}

	public static String format(Date date, Locale locale)
	{
		if (date == null) {
			return null;
		}
		return DateFormatUtils.format(date, "yyyy-MM-dd HH:mm:ss", locale);
	}

	public static String format(Date date, String pattern, Locale locale)
	{
		if (date == null) {
			return null;
		}
		TimeZone timeZone = TimeZone.getDefault();
		return DateFormatUtils.format(date, pattern, timeZone, locale);
	}

	public static String format(Date date, String pattern, TimeZone timeZone)
	{
		if (date == null) {
			return null;
		}
		return DateFormatUtils.format(date, pattern, timeZone);
	}

	public static String format(Date date, String pattern, TimeZone zone, Locale locale)
	{
		if (date == null) {
			return null;
		}
		return DateFormatUtils.format(date, pattern, zone, locale);
	}

	public static String formatDateOrDatetime(Date date)
	{
		if (date == null) {
			return null;
		}
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		if ((calendar.get(11) == 0) &&
				(calendar.get(12) == 0) &&
				(calendar.get(13) == 0) &&
				(calendar.get(14) == 0)) {
			return formatDate(date);
		}
		return formatDatetime(date);
	}

	public static Date parseDatetime(String datetime)
	{
		return parse(datetime, null, "yyyy-MM-dd HH:mm:ss");
	}

	public static Date parseDatetime(String datetime, TimeZone timeZone)
	{
		return parse(datetime, timeZone, "yyyy-MM-dd HH:mm:ss");
	}

	public static Date parseDate(String date)
	{
		return parse(date, null, "yyyy-MM-dd");
	}

	public static Date parseDatetimeWithoutSecond(String date)
	{
		return parse(date, null, "yyyy-MM-dd HH:mm");
	}

	public static Date parse(String dateStr)
	{
		return parse(dateStr, null, null);
	}

	public static Date parse(String dateStr, TimeZone timeZone)
	{
		return parse(dateStr, timeZone, null);
	}

	public static Date parse(String dateStr, String pattern)
	{
		return parse(dateStr, null, pattern);
	}

	public static Date parse(String dateStr, TimeZone timeZone, String pattern)
	{
		if (Strings.isBlank(dateStr)) {
			return null;
		}
		if (null == timeZone) {
			timeZone = TimeZone.getDefault();
		}
		Date date = null;
		SimpleDateFormat parser = null;
		ParsePosition pos = new ParsePosition(0);
		parser = new SimpleDateFormat();
		parser.setTimeZone(timeZone);
		try
		{
			if (Strings.isNotBlank(pattern))
			{
				parser.applyPattern(pattern);
				date = parser.parse(dateStr, pos);
			}
			if (date == null)
			{
				String[] parsePatterns = { "yyyy-MM-dd", "yyyy-MM-dd HH:mm", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm:ss.S" };
				for (int i = 0; i < parsePatterns.length; i++)
				{
					parser.applyPattern(parsePatterns[i]);
					pos.setIndex(0);
					date = parser.parse(dateStr, pos);
					if ((date != null) && (pos.getIndex() == dateStr.length())) {
						break;
					}
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return date;
	}

	public static Date parseNoTimeZone(String dateStr, String pattern)
	{
		if (Strings.isBlank(dateStr)) {
			return null;
		}
		String[] parsePatterns = null;
		if (Strings.isBlank(pattern)) {
			parsePatterns = new String[] { "yyyy-MM-dd", "yyyy-MM-dd HH:mm", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm:ss.S" };
		} else {
			parsePatterns = new String[] { pattern };
		}
		try
		{
			SimpleDateFormat parser = null;
			ParsePosition pos = new ParsePosition(0);
			for (int i = 0; i < parsePatterns.length; i++)
			{
				parser = new SimpleDateFormat(parsePatterns[i]);
				parser.applyPattern(parsePatterns[i]);

				pos.setIndex(0);
				Date date = parser.parse(dateStr, pos);
				if ((date != null) && ((pos.getIndex() == dateStr.length()) || (!Strings.isBlank(pattern)))) {
					return date;
				}
				if (dateStr.length() > "yyyy-MM-dd HH:mm:ss".length()) {
					return date;
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public static Date addSecond(Date date, int amount)
	{
		return add(date, 13, amount);
	}

	public static Date addMinute(Date date, int amount)
	{
		return add(date, 12, amount);
	}

	public static Date addHour(Date date, int amount)
	{
		return add(date, 11, amount);
	}

	public static Date addDate(Date date, int amount)
	{
		return add(date, 5, amount);
	}

	public static Date addMonth(Date date, int amount)
	{
		return add(date, 2, amount);
	}

	public static Date addYear(Date date, int amount)
	{
		return add(date, 1, amount);
	}

	private static Date add(Date date, int field, int amount)
	{
		if (amount == 0) {
			return date;
		}
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(date);

		calendar.add(field, amount);

		return calendar.getTime();
	}

	public static boolean between(Date checkupDate, Date startDate, Date endDate, boolean isIncludeBorderline)
	{
		if (!startDate.before(endDate))
		{
			Date temp = startDate;
			startDate = endDate;
			endDate = temp;
		}
		if ((checkupDate.after(startDate)) && (checkupDate.before(endDate))) {
			return true;
		}
		if ((isIncludeBorderline) && (
				(checkupDate.equals(startDate)) || (checkupDate.equals(endDate)))) {
			return true;
		}
		return false;
	}

	public static boolean checkOverup(Date startDate1, Date endDate1, Date startDate2, Date endDate2, boolean allowIncludeBorderline)
	{
		if (!startDate1.before(endDate1))
		{
			Date temp = startDate1;
			startDate1 = endDate1;
			endDate1 = temp;
		}
		if (!startDate2.before(endDate2))
		{
			Date temp = startDate2;
			startDate2 = endDate2;
			endDate2 = temp;
		}
		if ((endDate1.before(startDate2)) || (endDate2.before(startDate1))) {
			return false;
		}
		if ((allowIncludeBorderline) && ((endDate1.equals(startDate2)) || (endDate2.equals(startDate1)))) {
			return false;
		}
		return true;
	}

	public static Date getNextPeriodMinute(Date date, int period, boolean clearSecond)
	{
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		if (clearSecond) {
			calendar.set(13, calendar.getActualMaximum(13));
		}
		int minute = calendar.get(12);
		int newMinute = 0;
		int i = 0;
		for (;;)
		{
			newMinute = i++ * period;
			if (newMinute > minute) {
				break;
			}
		}
		int amount = newMinute - minute;

		calendar.add(12, amount);

		return calendar.getTime();
	}

	public static Date conversionToServerDate(Date localDate, TimeZone localZone)
	{
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(localDate);

		int amount = TimeZone.getDefault().getRawOffset() - localZone.getRawOffset();
		calendar.add(14, amount);

		return calendar.getTime();
	}

	public static long minusDay(Date theDay, Date otherDay)
	{
		long minusDay = theDay.getTime() - otherDay.getTime();
		return minusDay / 86400000L;
	}

	public static long[] detailInterval(Date theDay, Date otherDay)
	{
		if (otherDay == null) {
			return null;
		}
		long seconds = (otherDay.getTime() - theDay.getTime()) / 1000L;
		long date = seconds / 86400L;
		long hour = (seconds - date * 24L * 60L * 60L) / 3600L;
		long minut = (seconds - date * 24L * 60L * 60L - hour * 60L * 60L) / 60L;
		long second = seconds - date * 24L * 60L * 60L - hour * 60L * 60L - minut * 60L;

		return new long[] { date, hour, minut, second };
	}

	public static long[] formatLongToTimeStr(long millisecond)
	{
		long seconds = millisecond / 1000L;
		long date = seconds / 86400L;
		long hour = (seconds - date * 24L * 60L * 60L) / 3600L;
		long minut = (seconds - date * 24L * 60L * 60L - hour * 60L * 60L) / 60L;
		long second = seconds - date * 24L * 60L * 60L - hour * 60L * 60L - minut * 60L;
		return new long[] { date, hour, minut, second };
	}

	public static Date transDateToCustomer(Date date, String pattern)
	{
		return parseNoTimeZone(format(date, pattern), pattern);
	}

	public static String formatDateStr(String dateStr, String pattern)
	{
		return format(parseNoTimeZone(dateStr, pattern), pattern);
	}
}
