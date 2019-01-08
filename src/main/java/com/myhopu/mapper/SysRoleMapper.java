package com.myhopu.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.myhopu.entity.SysRole;

public interface SysRoleMapper {

	public void add(SysRole bean);

	public void del(long id);

	public void upd(SysRole bean);

	public List findAll(@Param(value="roleid") long roleid);

	
	public List findAllDeptUser(@Param(value="roleid") long roleid);
	
	public void add2(Map map);
	
	public List findAll2(@Param(value="roleid") long roleid);
	
	public void del2(long id);
	
	
	public List findAllModule(@Param(value="roleid") long roleid);
	
	public void add3(Map map);
	
	public List findAll3(@Param(value="roleid") long roleid);
	
	public void del3(long id);

}
