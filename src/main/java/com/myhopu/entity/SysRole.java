package com.myhopu.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the SYS_ROLE database table.
 * 
 */
@Entity
@Table(name = "SYS_ROLE")
@NamedQuery(name = "SysRole.findAll", query = "SELECT s FROM SysRole s")
public class SysRole implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private long roleid;

	private String rolename;

	// bi-directional many-to-many association to SysModule
	@ManyToMany
	@JoinTable(name = "SYS_ROLE_REL_MODULE", joinColumns = { @JoinColumn(name = "ROLEID") }, inverseJoinColumns = {
			@JoinColumn(name = "MID") })
	private List<SysModule> sysModules;

	// bi-directional many-to-many association to SysUser
	@ManyToMany
	@JoinTable(name = "SYS_ROLE_REL_USER", joinColumns = { @JoinColumn(name = "ROLEID") }, inverseJoinColumns = {
			@JoinColumn(name = "USERID") })
	private List<SysUser> sysUsers;

	public SysRole() {
	}

	public long getRoleid() {
		return this.roleid;
	}

	public void setRoleid(long roleid) {
		this.roleid = roleid;
	}

	public String getRolename() {
		return this.rolename;
	}

	public void setRolename(String rolename) {
		this.rolename = rolename;
	}

	public List<SysModule> getSysModules() {
		return this.sysModules;
	}

	public void setSysModules(List<SysModule> sysModules) {
		this.sysModules = sysModules;
	}

	public List<SysUser> getSysUsers() {
		return this.sysUsers;
	}

	public void setSysUsers(List<SysUser> sysUsers) {
		this.sysUsers = sysUsers;
	}

}