package com.myhopu.mapper;

import java.util.List;

import com.myhopu.entity.PmsProject;

public interface PmsProjectMapper {

	public void add(PmsProject bean);

	public void del(long id);

	public void upd(PmsProject bean);

	public PmsProject findOne(long id);

	public List findAll();

}
