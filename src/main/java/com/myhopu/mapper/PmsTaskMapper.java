package com.myhopu.mapper;

import java.util.List;

import com.myhopu.entity.PmsTask;

public interface PmsTaskMapper {

	public void add(PmsTask bean);

	public void del(long id);

	public void upd(PmsTask bean);

	public PmsTask findOne(long id);

	public List findAll(long id);

}
