package com.myhopu.entity;

import java.util.Date;

public class ReturnCheckDetail {

	private long userid;//员工id
	private String dname;//员工所在部门名字
	private String realname;//员工名字 
	private String timeShould;//应出勤日期
	private String timeCheck;//打卡日期时间
	private long moneyBase;//基本工资
	private Date createTime;//加入公司的时间
	
	private CheckDetail checkDetail;//请假条详细记录
	private String status;//出勤状态
	
	private long moneySalary;//工龄奖
	private long moneyAbsent;//旷工扣款
	private long moneyLater;//迟到扣款
	private long moneyThing;//事假扣款
	private long moneyIll;//病假扣款
	private long salary;//应发工资
	
	public String getDname() {
		return dname;
	}

	public void setDname(String dname) {
		this.dname = dname;
	}

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public CheckDetail getCheckDetail() {
		return checkDetail;
	}

	public void setCheckDetail(CheckDetail checkDetail) {
		this.checkDetail = checkDetail;
	}

	
	
	public String getTimeCheck() {
		return timeCheck;
	}

	public void setTimeCheck(String timeCheck) {
		this.timeCheck = timeCheck;
	}

	public String getTimeShould() {
		return timeShould;
	}

	public void setTimeShould(String timeShould) {
		this.timeShould = timeShould;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public long getUserid() {
		return userid;
	}

	public void setUserid(long userid) {
		this.userid = userid;
	}

	public long getMoneyBase() {
		return moneyBase;
	}

	public void setMoneyBase(long moneyBase) {
		this.moneyBase = moneyBase;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public long getMoneySalary() {
		return moneySalary;
	}

	public void setMoneySalary(long moneySalary) {
		this.moneySalary = moneySalary;
	}

	public long getMoneyAbsent() {
		return moneyAbsent;
	}

	public void setMoneyAbsent(long moneyAbsent) {
		this.moneyAbsent = moneyAbsent;
	}

	public long getMoneyLater() {
		return moneyLater;
	}

	public void setMoneyLater(long moneyLater) {
		this.moneyLater = moneyLater;
	}

	public long getMoneyThing() {
		return moneyThing;
	}

	public void setMoneyThing(long moneyThing) {
		this.moneyThing = moneyThing;
	}

	public long getMoneyIll() {
		return moneyIll;
	}

	public void setMoneyIll(long moneyIll) {
		this.moneyIll = moneyIll;
	}

	public long getSalary() {
		return salary;
	}

	public void setSalary(long salary) {
		this.salary = salary;
	}

	@Override
	public String toString() {
		return "ReturnCheckDetail [userid=" + userid + ", dname=" + dname + ", realname=" + realname + ", timeShould="
				+ timeShould + ", timeCheck=" + timeCheck + ", moneyBase=" + moneyBase + ", createTime=" + createTime
				+ ", checkDetail=" + checkDetail + ", status=" + status + ", moneySalary=" + moneySalary
				+ ", moneyAbsent=" + moneyAbsent + ", moneyLater=" + moneyLater + ", moneyThing=" + moneyThing
				+ ", moneyIll=" + moneyIll + ", salary=" + salary + "]";
	}

	

	

}
