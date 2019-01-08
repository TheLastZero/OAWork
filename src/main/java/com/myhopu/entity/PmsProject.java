package com.myhopu.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the PMS_PROJECT database table.
 * 
 */
@Entity
@Table(name="PMS_PROJECT")
@NamedQuery(name="PmsProject.findAll", query="SELECT p FROM PmsProject p")
public class PmsProject implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private long pid;

	private String pname;

	private String premark;

	//bi-directional many-to-one association to SysDept
	@ManyToOne
	@JoinColumn(name="DID")
	private SysDept sysDept;

	//bi-directional many-to-one association to PmsTask
	@OneToMany(mappedBy="pmsProject")
	private List<PmsTask> pmsTasks;

	public PmsProject() {
	}

	public long getPid() {
		return this.pid;
	}

	public void setPid(long pid) {
		this.pid = pid;
	}

	public String getPname() {
		return this.pname;
	}

	public void setPname(String pname) {
		this.pname = pname;
	}

	public String getPremark() {
		return this.premark;
	}

	public void setPremark(String premark) {
		this.premark = premark;
	}

	public SysDept getSysDept() {
		return this.sysDept;
	}

	public void setSysDept(SysDept sysDept) {
		this.sysDept = sysDept;
	}

	public List<PmsTask> getPmsTasks() {
		return this.pmsTasks;
	}

	public void setPmsTasks(List<PmsTask> pmsTasks) {
		this.pmsTasks = pmsTasks;
	}

	public PmsTask addPmsTask(PmsTask pmsTask) {
		getPmsTasks().add(pmsTask);
		pmsTask.setPmsProject(this);

		return pmsTask;
	}

	public PmsTask removePmsTask(PmsTask pmsTask) {
		getPmsTasks().remove(pmsTask);
		pmsTask.setPmsProject(null);

		return pmsTask;
	}

}