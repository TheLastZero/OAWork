package com.myhopu.entity;

public class ReturnLeavePersonal {

	private int daynum;//当前几号
	
	private ReturnCheckDetail am;
	private ReturnCheckDetail pm;
	
	
	public ReturnCheckDetail getAm() {
		return am;
	}
	public void setAm(ReturnCheckDetail am) {
		this.am = am;
	}
	public ReturnCheckDetail getPm() {
		return pm;
	}
	public void setPm(ReturnCheckDetail pm) {
		this.pm = pm;
	}
	public int getDaynum() {
		return daynum;
	}
	public void setDaynum(int daynum) {
		this.daynum = daynum;
	}
	@Override
	public String toString() {
		return "ReturnLeavePersonal [daynum=" + daynum + ", am=" + am + ", pm=" + pm + "]";
	}
	
	
	
	
	
	
}
