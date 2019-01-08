package com.myhopu.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the PMS_TASK database table.
 * 
 */
@Entity
@Table(name="PMS_TASK")
@NamedQuery(name="PmsTask.findAll", query="SELECT p FROM PmsTask p")
public class PmsTask implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private long tid;

	@Temporal(TemporalType.DATE)
	private Date tdate1;

	@Temporal(TemporalType.DATE)
	private Date tdate2;

	private String tname;

	private String tremark;

	private String tresponse;

	private long tsortkey;

	private int tworkday;

	//bi-directional many-to-one association to PmsProject
	@ManyToOne
	@JoinColumn(name="PID")
	private PmsProject pmsProject;

	//bi-directional many-to-one association to SysUser
	@ManyToOne
	@JoinColumn(name="USERID")
	private SysUser sysUser;

	//bi-directional many-to-one association to PmsTaskItem
	@OneToMany(mappedBy="pmsTask")
	private List<PmsTaskItem> pmsTaskItems;

	public PmsTask() {
	}

	public long getTid() {
		return this.tid;
	}

	public void setTid(long tid) {
		this.tid = tid;
	}

	public Date getTdate1() {
		return this.tdate1;
	}

	public void setTdate1(Date tdate1) {
		this.tdate1 = tdate1;
	}

	public Date getTdate2() {
		return this.tdate2;
	}

	public void setTdate2(Date tdate2) {
		this.tdate2 = tdate2;
	}

	public String getTname() {
		return this.tname;
	}

	public void setTname(String tname) {
		this.tname = tname;
	}

	public String getTremark() {
		return this.tremark;
	}

	public void setTremark(String tremark) {
		this.tremark = tremark;
	}

	public String getTresponse() {
		return this.tresponse;
	}

	public void setTresponse(String tresponse) {
		this.tresponse = tresponse;
	}

	public long getTsortkey() {
		return this.tsortkey;
	}

	public void setTsortkey(long tsortkey) {
		this.tsortkey = tsortkey;
	}

	public int getTworkday() {
		return this.tworkday;
	}

	public void setTworkday(int tworkday) {
		this.tworkday = tworkday;
	}

	public PmsProject getPmsProject() {
		return this.pmsProject;
	}

	public void setPmsProject(PmsProject pmsProject) {
		this.pmsProject = pmsProject;
	}

	public SysUser getSysUser() {
		return this.sysUser;
	}

	public void setSysUser(SysUser sysUser) {
		this.sysUser = sysUser;
	}

	public List<PmsTaskItem> getPmsTaskItems() {
		return this.pmsTaskItems;
	}

	public void setPmsTaskItems(List<PmsTaskItem> pmsTaskItems) {
		this.pmsTaskItems = pmsTaskItems;
	}

	public PmsTaskItem addPmsTaskItem(PmsTaskItem pmsTaskItem) {
		getPmsTaskItems().add(pmsTaskItem);
		pmsTaskItem.setPmsTask(this);

		return pmsTaskItem;
	}

	public PmsTaskItem removePmsTaskItem(PmsTaskItem pmsTaskItem) {
		getPmsTaskItems().remove(pmsTaskItem);
		pmsTaskItem.setPmsTask(null);

		return pmsTaskItem;
	}

}