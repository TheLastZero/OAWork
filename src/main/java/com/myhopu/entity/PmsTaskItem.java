package com.myhopu.entity;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the PMS_TASK_ITEM database table.
 * 
 */
@Entity
@Table(name="PMS_TASK_ITEM")
@NamedQuery(name="PmsTaskItem.findAll", query="SELECT p FROM PmsTaskItem p")
public class PmsTaskItem implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private long iid;

	private String iname;

	private String irequirement;

	private int iscore;

	private int iscore2;

	private long isortkey;

	//bi-directional many-to-one association to PmsTask
	@ManyToOne
	@JoinColumn(name="TID")
	private PmsTask pmsTask;

	public PmsTaskItem() {
	}

	public long getIid() {
		return this.iid;
	}

	public void setIid(long iid) {
		this.iid = iid;
	}

	public String getIname() {
		return this.iname;
	}

	public void setIname(String iname) {
		this.iname = iname;
	}

	public String getIrequirement() {
		return this.irequirement;
	}

	public void setIrequirement(String irequirement) {
		this.irequirement = irequirement;
	}

	public int getIscore() {
		return this.iscore;
	}

	public void setIscore(int iscore) {
		this.iscore = iscore;
	}

	public int getIscore2() {
		return this.iscore2;
	}

	public void setIscore2(int iscore2) {
		this.iscore2 = iscore2;
	}

	public long getIsortkey() {
		return this.isortkey;
	}

	public void setIsortkey(long isortkey) {
		this.isortkey = isortkey;
	}

	public PmsTask getPmsTask() {
		return this.pmsTask;
	}

	public void setPmsTask(PmsTask pmsTask) {
		this.pmsTask = pmsTask;
	}

}