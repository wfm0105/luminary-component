package com.luminroy.component.util.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DateUtil {

	private static final Logger logger = LoggerFactory.getLogger(DateUtil.class);
	
	private static final SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static final SimpleDateFormat SF_YM = new SimpleDateFormat("yyyyMM");
	private static final SimpleDateFormat SF_YMD = new SimpleDateFormat("yyyy-MM-dd");
	private static final SimpleDateFormat SF_YMD2 = new SimpleDateFormat("yyyy/MM/dd");
	private static final SimpleDateFormat SF_YMD3 = new SimpleDateFormat("yyyyMMdd");
	private static final SimpleDateFormat SF_FULL_TIME = new SimpleDateFormat("yyyyMMddHHmmss");
	private static final SimpleDateFormat SF_HOUR = new SimpleDateFormat("HH");
	
	/**
	 * 日期格式化为字符串
	 * @param date
	 * @return
	 */
	public static String format(Date date) {
		return sf.format(date);
	}
	
	/**
	 * 日期格式化为字符串(年月,ex:201611)
	 * @param date
	 * @return
	 */
	public static String formatYearAndMonth(Date date) {
		return SF_YM.format(date);
	}
	
	/**
	 * 日期格式化为字符串(年月,ex:2016-01-01)
	 * @param date
	 * @return
	 */
	public static String formatYearMonthDay(Date date) {
		return SF_YMD.format(date);
	}
	
	/**
	 * 日期格式化为字符串(年月,ex:2016/01/01)
	 * @param date
	 * @return
	 */
	public static String formatYearMonthDay2(Date date) {
		return SF_YMD2.format(date);
	}
	
	/**
	 * 日期格式化为字符串(年月,ex:20160101)
	 * @param date
	 * @return
	 */
	public static String formatYearMonthDay3(Date date) {
		return SF_YMD3.format(date);
	}
	
	/**
	 * 日期格式化为字符串(年月,ex:201611121212)
	 * @param date
	 * @return
	 */
	public static String formatFullTime(Date date) {
		return SF_FULL_TIME.format(date);
	}
	
	/**
	 * 字符串转日期
	 * @param timeStr
	 * @return
	 */
	public static Date stringToDate(String timeStr) {
		Date stringdata = null;
		try {
			stringdata = sf.parse(timeStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return stringdata;
	}
	
	/**
	 * 字符串转日期(转成年-月-日格式)
	 * @param timeStr
	 * @return
	 */
	public static Date stringToYMD(String timeStr) {
		Date stringdata = null;
		try {
			stringdata = SF_YMD.parse(timeStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return stringdata;
	}
	
	/**
	 * 比较两个时间大小
	 * 
	 * @param firstDate
	 * @param secondDate
	 * @return 1:返回0，两个时间相等<br>2:返回整数，firstDate大于secondDate<br>3:返回负数，firstDate小于secondDate<br>
	 */
	public static long compareDate(Date firstDate, Date secondDate)   {
		return firstDate.getTime()-secondDate.getTime();
	}
	
	public static String getHours(Date date)   {
		return SF_HOUR.format(date);
	}
	
	/**
	 * 返回yyyyMMddHHmmss格式日期字符串
	 * 
	 * @return
	 */
	public static String getYyyyMMddHHmmss(){
		return	SF_FULL_TIME.format(new Date());
	}

	/**
	 * 当前时间的加减多少天
	 * @param DateCount
	 * 				天数
	 * @return
	 */
	public static Date getDate(int DateCount){
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, DateCount);
		date = calendar.getTime();
		return date;
	}
	
	public static int getCurrentMonth() {
    	Calendar localTime = Calendar.getInstance();
    	return localTime.get(Calendar.MONTH)+1;
    }
    
    public static Date getDateBefore(Date base, int interval) {
    	Calendar localTime =Calendar.getInstance();  
    	localTime.setTime(base);  
    	localTime.set(Calendar.DATE,localTime.get(Calendar.DATE)-interval);  
    	return localTime.getTime();  
    }
    
    public static Date getDateAfter(Date base, int interval) {
    	Calendar localTime =Calendar.getInstance();  
    	localTime.setTime(base);  
    	localTime.set(Calendar.DATE,localTime.get(Calendar.DATE)+interval);  
    	return localTime.getTime();  
    }
    
    public static Date getStartDateOfMonth() {
    	Calendar localTime = Calendar.getInstance();
    	logger.info(localTime.get(Calendar.YEAR)+"-"+(localTime.get(Calendar.MONTH)-1));
		localTime.set(
				localTime.get(Calendar.YEAR), 
				localTime.get(Calendar.MONTH)-1, 
				1,0,0,0);
		return localTime.getTime();
    }
    
    public static Date getEndDateOfMonth() {
    	Calendar localTime = Calendar.getInstance();
    	logger.info(localTime.get(Calendar.YEAR)+"-"+(localTime.get(Calendar.MONTH)-1));
    	localTime.set(Calendar.YEAR, localTime.get(Calendar.YEAR));
    	localTime.set(Calendar.MONTH, localTime.get(Calendar.MONTH));
		localTime.set(Calendar.DAY_OF_MONTH, 0);
		localTime.set(Calendar.HOUR, 0);
		localTime.set(Calendar.MINUTE, 0);
		localTime.set(Calendar.SECOND, 0);
		return localTime.getTime();
    }
	
	public static void main(String[] args) {
		System.out.println(formatFullTime(new Date()));
	}

}
