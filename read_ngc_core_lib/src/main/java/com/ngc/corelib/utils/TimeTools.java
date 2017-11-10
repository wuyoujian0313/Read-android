package com.ngc.corelib.utils;

import java.util.Date;

/**
 * 时间工具类
 * 
 * @Title: TimeTools.java
 * @Package: com.silupay.corelib.utils
 * @Description: TODO(用一句话描述该文件做什么)
 * @author:acorn
 * @date: 2014年8月27日 下午5:12:32
 * @version: V1.0
 */
public class TimeTools {

	/**
	 * 计算时间间隔
	 * 
	 * @param date
	 * @return
	 */
	public static String getTimeSpan(Date date) {
		long timeSpan = date.getTime() - System.currentTimeMillis();
		timeSpan = timeSpan / 1000 / 60 / 60 / 24;
		return "" + timeSpan;
	}

	/**
	 * 
	 * @param 要转换的毫秒数
	 * @return 该毫秒数转换为 * days * hours * minutes * seconds 后的格式
	 * @author fy.zhang
	 */
	public static String formatDuring(long mss) {
		if (mss <= 0)
			return "0Days00:00:00";
		long days = mss / (1000 * 60 * 60 * 24);
		long hours = (mss % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
		long minutes = (mss % (1000 * 60 * 60)) / (1000 * 60);
		long seconds = (mss % (1000 * 60)) / 1000;
		return days + "Days" + getDoubleNum(hours) + ":" + getDoubleNum(minutes) + ":" + getDoubleNum(seconds);
	}

	private static String getDoubleNum(long num) {
		return num < 10 ? "0" + num : "" + num;
	}

	public static String formatDuringSecond(long sec) {
		return formatDuring(sec * 1000);
	}

	/**
	 * 
	 * @param begin
	 *            时间段的开始
	 * @param end
	 *            时间段的结束
	 * @return 输入的两个Date类型数据之间的时间间格用* days * hours * minutes * seconds的格式展示
	 * @author fy.zhang
	 */
	public static String formatDuring(Date begin, Date end) {
		return formatDuring(end.getTime() - begin.getTime());
	}

}
