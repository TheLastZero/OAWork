package com.myhopu.utils;

import org.junit.Test;

public class MyCalender {

//	@Test
//	public void test() {
//		System.out.println(getWeekByymd(2018, 11, 16));
//	}
	
	/**
	 * 
	 * 求某年某月某天是星期几
	 * 
	 * 1-31
	 * 平年2-28
	 * 润年2-29
	 * 3-31
	 * 4-30
	 * 5-31
	 * 6-30
	 * 7-31
	 * 8-31
	 * 9-30
	 * 10-31
	 * 11-30
	 * 12-31
	 * 
	 * 1900年1月1日是星期一
	 * 
	 * 判断求的这一天距离1900年多少天，除以7靠余数判断星期几
	 */
	//@Test
	public static Integer getWeekByymd(int year,int mounth,int day) {

		int startYear = 1900;

//		int year = 2026;//求的年份
//		int mounth = 5;//求的月份
//		int day = 11;//求的号数
		
		int pastDay = 0;//过去的天数
		
		Integer week=null;//返回的星期几

		//1、求现在这一年距离1900年过了多少天
		while(startYear<year) {

			//能被4整除，或者能被400整除
			if((startYear%4==0 && startYear%100!=0) || startYear%400==0) {//闰年366
//				System.out.println(startYear+"是闰年");
				pastDay +=366;
			}else {//平年365
//				System.out.println(startYear+"是平年");
				pastDay += 365;

			}
			startYear++;
		}

		//2、求这个月距离1月份过了多少天
		int i=0;
		while(i<=mounth) {

			if(i==1) {
				
				if(mounth==1) {
					pastDay += day;
				}else {
					pastDay +=31;
				}
				
			}else if(i==2) {
					
				if(mounth==2) {
					pastDay += day;
				}else {
					if((year%4==0 && year%100!=0) || year%400==0) {//闰年
						pastDay += 29;
					}else {//平年
						pastDay += 28;
					}
				}
			}else if(i==3) {
				if(mounth==3) {
					pastDay += day;
				}else {
					pastDay +=31;
				}
			}else if(i==4) {
				if(mounth==4) {
					pastDay += day;
				}else {
					pastDay +=30;
				}
			}else if(i==5) {
				if(mounth==5) {
					pastDay += day;
				}else {
					pastDay +=31;
				}
			}else if(i==6) {
				if(mounth==6) {
					pastDay += day;
				}else {
					pastDay +=30;
				}
			}else if(i==7) {
				if(mounth==7) {
					pastDay += day;
				}else {
					pastDay +=31;
				}
			}else if(i==8) {
				if(mounth==8) {
					pastDay += day;
				}else {
					pastDay +=31;
				}
			}else if(i==9) {
				if(mounth==9) {
					pastDay += day;
				}else {
					pastDay +=30;
				}
			}else if(i==10) {
				if(mounth==10) {
					pastDay += day;
				}else {
					pastDay +=31;
				}
			}else if(i==11) {
				if(mounth==11) {
					pastDay += day;
				}else {
					pastDay +=30;
				}
			}else if(i==12) {
				if(mounth==12) {
					pastDay += day;
				}else {
					pastDay +=31;
				}
			}
			
			
			
			i++;
		}
//		System.out.println("距离1月一号份过去了:"+pastDay);
		//3、最后加上求的天数
//		pastDay += day;

		//4、求出这一天是星期几
		if(pastDay%7==0) {//星期天
			week = 7;
		}else if(pastDay%7==1) {//星期一
			week = 1;
		}else if(pastDay%7==2) {//星期2
			week = 2;
		}else if(pastDay%7==3) {//星期3
			week = 3;
		}else if(pastDay%7==4) {//星期4
			week = 4;
		}else if(pastDay%7==5) {//星期5
			week = 5;
		}else if(pastDay%7==6) {//星期6
			week = 6;
		}
		//System.out.println(year+"/"+mounth+"/"+day+"是星期"+week);
		
		return week;
	}
	
	
//	@Test
//	public void test() {
//		System.out.println(getDaysByYearMounth(2000, 2));
//	}
	
	/**
	 * 根据年份月份，返回那一年的那个月有多少天
	 * @return
	 */
	public static Integer getDaysByYearMounth(int startYear,int mounth) {
		
		switch (mounth) {
			case 1:
				return 31;
			case 2:
					
				if((startYear%4==0 && startYear%100!=0) || startYear%400==0) {//闰年
					return 29;
				}else {//平年
					return 28;
	
				}
			
			case 3:
				return 31;
			case 4:
				return 30;
			case 5:
				return 31;
			case 6:
				return 30;
			case 7:
				return 31;
			case 8:
				return 31;
			case 9:
				return 30;
			case 10:
				return 31;
			case 11:
				return 30;
			case 12:
				return 31;	
		}
		
		return null;
	}
	
	
}