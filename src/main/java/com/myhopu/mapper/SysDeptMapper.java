package com.myhopu.mapper;

import java.util.List;

import com.myhopu.entity.SysDept;

public interface SysDeptMapper {

	public void add(SysDept bean);

	public void del(long id);

	public void upd(SysDept bean);

	public SysDept findOne(long id);

	public List findAll();

}
