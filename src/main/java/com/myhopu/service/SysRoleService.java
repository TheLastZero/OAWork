package com.myhopu.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.myhopu.entity.SysRole;
import com.myhopu.mapper.SysRoleMapper;

@Service
public class SysRoleService {

	@Resource
	SysRoleMapper sysRoleMapper;

	public List findAll(long roleid) {
		return sysRoleMapper.findAll(roleid);
	}

	public void add(SysRole sysRole) {
		sysRoleMapper.add(sysRole);
	}

	public void upd(SysRole sysRole) {
		sysRoleMapper.upd(sysRole);
	}

	public void del(String ids) {
		String[] ids2 = ids.split(",");
		for (int i = 0; i < ids2.length; i++) {
			sysRoleMapper.del(Long.parseLong(ids2[i]));
		}
	}
	

	public List getDeptUser(long roleid) {
		return sysRoleMapper.findAllDeptUser(roleid);
	}

	public void add2(Map map) {
		sysRoleMapper.add2(map);
	}
	
	public List findAll2(long roleid) {
		return sysRoleMapper.findAll2(roleid);
	}
	
	public void del2(String ids) {
		String[] ids2 = ids.split(",");
		for (int i = 0; i < ids2.length; i++) {
			sysRoleMapper.del2(Long.parseLong(ids2[i]));
		}
	}
	
	
	public List getModule(long roleid) {
		return sysRoleMapper.findAllModule(roleid);
	}

	public void add3(Map map) {
		sysRoleMapper.add3(map);
	}
	
	public List findAll3(long roleid) {
		return sysRoleMapper.findAll3(roleid);
	}
	
	public void del3(String ids) {
		String[] ids2 = ids.split(",");
		for (int i = 0; i < ids2.length; i++) {
			sysRoleMapper.del3(Long.parseLong(ids2[i]));
		}
	}

}