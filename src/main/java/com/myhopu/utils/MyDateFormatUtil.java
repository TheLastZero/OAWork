package com.myhopu.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.Test;

import com.myhopu.entity.ReturnCheckDetail;

/**
 * 时间转换工具类
 * @author zero
 *
 */
public class MyDateFormatUtil {

	@Test
	public void toLocal() {
		Date d = new Date();
		
		//System.out.println(d.toString());
		
//		//date转字符串，2018-11-17 下午 16:19
//		String s = new SimpleDateFormat("yyyy-MM-dd a HH:mm").format(d);
//		System.out.println(s);
//		
//		
//		//字符串转date 2015-12-01 上午 07:59
//    	DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd a HH:mm"); 
//		Date dStart = null;
//		try {
//			dStart = format1.parse("2015-12-01 上午 07:59");
//			System.out.println(dStart.toLocaleString());
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
		
		System.out.println(getString(d));
		
		System.out.println(getString(getDate(getString(d))));
		
	}
	
	/**
	 * Date转为如下字符串
	 * 2018-11-17 下午 16:19
	 */
	public static String getString(Date d) {
		return new SimpleDateFormat("yyyy-MM-dd a HH:mm").format(d);
	}
	
	/**
	 * 将date转为String，转为如下格式
	 * 2018-11-10 上午
	 * @param d
	 * @return 
	 */
	public static String getString2(Date d) {
		return new SimpleDateFormat("yyyy-MM-dd a").format(d);
	}
	
	/**
	 * 将 2018-12-01 上午 格式的字符串转为date类型
	 * 
	 * @param d
	 * @return
	 */
	public static Date getDate2(String s) {
		DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd a"); 
		Date d = null;
		try {
			d = format1.parse(s);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return d;
	}
	
	/**
	 * 2015-12-02 下午 05:38
	 * 将上述日期格式字符串转为date
	 */
	public static Date getDate(String s) {
		
		DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd a HH:mm"); 
		Date d = null;
		try {
			d = format1.parse(s);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String [] s2 = s.split("\\s");
		
		System.out.println(Arrays.asList(s2));
		
		if(s2[1].equals("上午")) {
			
		}else if(s2[1].equals("下午") && d.getHours() <= 12) {//下午的时间加上12个小时，如果小时小于12表示为12小时格式的再加上12个小时得到正确的24小时格式
			d.setTime(d.getTime()+1000*60*60*12);
		}
		
		return d;
	}
	
	
	
	
	
	@Test
	public void testGetDate() {
		//System.out.println(getDate("2015-12-02 上午 05:38"));
		
		Date d = new Date();
		System.out.println(d.getMonth());
		GregorianCalendar gc = new GregorianCalendar();
		gc.set(2016, 11-1, 10,10,0,0);
		d.setTime(gc.getTimeInMillis());
		
		String s =new SimpleDateFormat("yyyy-MM-dd a").format(d);
		
		Date d2 = new Date();
		
		//System.out.println(s);
		Date d3 = getDate2("2018-12-01 上午");
		System.out.println((d3.getYear()+1900)+"-"+(d3.getMonth()+1)+"-"+d3.getDate());
	}
	
	@Test
	public void test3() {
		boolean apm = true;//用来记录上下午的boolean，true上午，false下午
		//遍历考情设置信息
		for(int i = 0;i<10;i++) {//从第一天开始到最后一天的考勤信息
				if(apm==true) {//上午
					System.out.println("上午");
					apm = false;
					i--;
				}
				else {//下午
					System.out.println("下午");	
					apm = true;
				}
			
		}
	}
	
}
