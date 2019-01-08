package com.myhopu.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the SYS_DEPT database table.
 * 
 */
@Entity
@Table(name="SYS_DEPT")
@NamedQuery(name="SysDept.findAll", query="SELECT s FROM SysDept s")
public class SysDept implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private long did;

	private String dname;

	private long dpid;

	private String dremark;

	private long dsortkey;

	//bi-directional many-to-one association to PmsProject
	@OneToMany(mappedBy="sysDept")
	private List<PmsProject> pmsProjects;

	//bi-directional many-to-one association to SysUser
	@OneToMany(mappedBy="sysDept")
	private List<SysUser> sysUsers;

	public SysDept() {
	}

	public long getDid() {
		return this.did;
	}

	public void setDid(long did) {
		this.did = did;
	}

	public String getDname() {
		return this.dname;
	}

	public void setDname(String dname) {
		this.dname = dname;
	}

	public long getDpid() {
		return this.dpid;
	}

	public void setDpid(long dpid) {
		this.dpid = dpid;
	}

	public String getDremark() {
		return this.dremark;
	}

	public void setDremark(String dremark) {
		this.dremark = dremark;
	}

	public long getDsortkey() {
		return this.dsortkey;
	}

	public void setDsortkey(long dsortkey) {
		this.dsortkey = dsortkey;
	}

	public List<PmsProject> getPmsProjects() {
		return this.pmsProjects;
	}

	public void setPmsProjects(List<PmsProject> pmsProjects) {
		this.pmsProjects = pmsProjects;
	}

	public PmsProject addPmsProject(PmsProject pmsProject) {
		getPmsProjects().add(pmsProject);
		pmsProject.setSysDept(this);

		return pmsProject;
	}

	public PmsProject removePmsProject(PmsProject pmsProject) {
		getPmsProjects().remove(pmsProject);
		pmsProject.setSysDept(null);

		return pmsProject;
	}

	public List<SysUser> getSysUsers() {
		return this.sysUsers;
	}

	public void setSysUsers(List<SysUser> sysUsers) {
		this.sysUsers = sysUsers;
	}

	public SysUser addSysUser(SysUser sysUser) {
		getSysUsers().add(sysUser);
		sysUser.setSysDept(this);

		return sysUser;
	}

	public SysUser removeSysUser(SysUser sysUser) {
		getSysUsers().remove(sysUser);
		sysUser.setSysDept(null);

		return sysUser;
	}

	@Override
	public String toString() {
		return "SysDept [did=" + did + ", dname=" + dname + ", dpid=" + dpid + ", dremark=" + dremark + ", dsortkey="
				+ dsortkey + ", pmsProjects=" + pmsProjects + ", sysUsers=" + sysUsers + "]";
	}
	
	

}