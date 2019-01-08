 package com.myhopu.entity;

import java.io.Serializable;
import javax.persistence.*;

import java.util.Date;
import java.util.List;


/**
 * The persistent class for the SYS_USER database table.
 * 
 */
@Entity
@Table(name="SYS_USER")
@NamedQuery(name="SysUser.findAll", query="SELECT s FROM SysUser s")
public class SysUser implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private long userid;

	private String realname;

	private String username;

	private String userpass;

	private long did;//部门编号
	
	private long checkCode;//考勤机登录号
	
	private long salaryBase;//基本工资
	
	private Date createTime;//加入公司的时间
	
	//bi-directional many-to-one association to PmsTask
	@OneToMany(mappedBy="sysUser")
	private List<PmsTask> pmsTasks;

	//bi-directional many-to-many association to SysRole
	@ManyToMany(mappedBy="sysUsers")
	private List<SysRole> sysRoles;

	//bi-directional many-to-one association to SysDept
	@ManyToOne
	@JoinColumn(name="DID")
	private SysDept sysDept;

	public SysUser() {
	}

	public long getCheckCode() {
		return checkCode;
	}

	public void setCheckCode(long checkCode) {
		this.checkCode = checkCode;
	}


	public long getDid() {
		return did;
	}


	public void setDid(long did) {
		this.did = did;
	}


	public long getUserid() {
		return this.userid;
	}

	public void setUserid(long userid) {
		this.userid = userid;
	}

	public String getRealname() {
		return this.realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUserpass() {
		return this.userpass;
	}

	public void setUserpass(String userpass) {
		this.userpass = userpass;
	}

	public List<PmsTask> getPmsTasks() {
		return this.pmsTasks;
	}

	public void setPmsTasks(List<PmsTask> pmsTasks) {
		this.pmsTasks = pmsTasks;
	}

	public PmsTask addPmsTask(PmsTask pmsTask) {
		getPmsTasks().add(pmsTask);
		pmsTask.setSysUser(this);

		return pmsTask;
	}

	public PmsTask removePmsTask(PmsTask pmsTask) {
		getPmsTasks().remove(pmsTask);
		pmsTask.setSysUser(null);

		return pmsTask;
	}

	public List<SysRole> getSysRoles() {
		return this.sysRoles;
	}

	public void setSysRoles(List<SysRole> sysRoles) {
		this.sysRoles = sysRoles;
	}

	public SysDept getSysDept() {
		return this.sysDept;
	}

	public void setSysDept(SysDept sysDept) {
		this.sysDept = sysDept;
	}

	public long getSalaryBase() {
		return salaryBase;
	}

	public void setSalaryBase(long salaryBase) {
		this.salaryBase = salaryBase;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Override
	public String toString() {
		return "SysUser [userid=" + userid + ", realname=" + realname + ", username=" + username + ", userpass="
				+ userpass + ", did=" + did + ", checkCode=" + checkCode + ", salaryBase=" + salaryBase
				+ ", createTime=" + createTime + ", pmsTasks=" + pmsTasks + ", sysRoles=" + sysRoles + ", sysDept="
				+ sysDept + "]";
	}

	
	
	

}