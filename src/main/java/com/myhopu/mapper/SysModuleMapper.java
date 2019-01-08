package com.myhopu.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.myhopu.entity.SysModule;

public interface SysModuleMapper {

	public void add(SysModule bean);

	public void del(long id);

	public void upd(SysModule bean);

	public SysModule findOne(long id);

	public List findAll(@Param(value = "mpid") long mpid);

	public List findAllMy(long userid);

}
