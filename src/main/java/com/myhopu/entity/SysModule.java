package com.myhopu.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the SYS_MODULE database table.
 * 
 */
@Entity
@Table(name="SYS_MODULE")
@NamedQuery(name="SysModule.findAll", query="SELECT s FROM SysModule s")
public class SysModule implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private long mid;

	private String mname;

	private String mpath;

	private long mpid;

	private long msortkey;

	//bi-directional many-to-many association to SysRole
	@ManyToMany(mappedBy="sysModules")
	private List<SysRole> sysRoles;

	public SysModule() {
	}

	public long getMid() {
		return this.mid;
	}

	public void setMid(long mid) {
		this.mid = mid;
	}

	public String getMname() {
		return this.mname;
	}

	public void setMname(String mname) {
		this.mname = mname;
	}

	public String getMpath() {
		return this.mpath;
	}

	public void setMpath(String mpath) {
		this.mpath = mpath;
	}

	public long getMpid() {
		return this.mpid;
	}

	public void setMpid(long mpid) {
		this.mpid = mpid;
	}

	public long getMsortkey() {
		return this.msortkey;
	}

	public void setMsortkey(long msortkey) {
		this.msortkey = msortkey;
	}

	public List<SysRole> getSysRoles() {
		return this.sysRoles;
	}

	public void setSysRoles(List<SysRole> sysRoles) {
		this.sysRoles = sysRoles;
	}

}