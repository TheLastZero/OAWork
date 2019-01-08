package com.myhopu.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.myhopu.entity.SysUser;

public interface SysUserMapper {

	public void add(SysUser bean);

	public void del(long id);

	public void upd(SysUser bean);

	public SysUser findOne(long id);

	public List findAll(@Param(value="did") long did); // 使用 @Param 的方式传参数

	public SysUser login(SysUser bean);

	public SysUser selectByCheckcode(Map<String, Object> mapQuery);//根据考勤机id查询user

	public List<SysUser> selectByCheckcode0WithDept();

	public List<SysUser> findOne(Map<String, Object> mapQuery);

}
