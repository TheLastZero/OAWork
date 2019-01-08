package com.myhopu.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.myhopu.entity.SysUser;
import com.myhopu.mapper.SysUserMapper;

@Service
public class SysUserService {

	@Resource
	SysUserMapper sysUserMapper;

	public List findAll(long did) {
		return sysUserMapper.findAll(did);
	}

	public void add(SysUser sysUser) {
		sysUserMapper.add(sysUser);
	}

	public void upd(SysUser sysUser) {
		sysUserMapper.upd(sysUser);
	}

	public void del(String ids) {
		String[] ids2 = ids.split(",");
		for (int i = 0; i < ids2.length; i++) {
			sysUserMapper.del(Long.parseLong(ids2[i]));
		}		
	}
	
	public SysUser login(SysUser bean) {
		return sysUserMapper.login(bean);
	}

	/**
	 * 根据考勤机id查询user
	 * @param mapQuery
	 * @return
	 */
	public SysUser selectByCheckcode(Map<String, Object> mapQuery) {
		return sysUserMapper.selectByCheckcode(mapQuery);
	}

	public List<SysUser> selectByCheckcode0WithDept() {
		return sysUserMapper.selectByCheckcode0WithDept();
	}

	public List<SysUser> findOne(Map<String, Object> mapQuery) {
		return sysUserMapper.findOne(mapQuery);
	}

}
