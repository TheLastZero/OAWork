package com.myhopu.service;

import java.util.List;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.myhopu.entity.SysModule;
import com.myhopu.mapper.SysModuleMapper;

@Service
public class SysModuleService {

	@Resource
	SysModuleMapper sysModuleMapper;

	public List findAll(long mpid) {
		return sysModuleMapper.findAll(mpid);
	}
	
	public List findAllMy(long userid) {
		return sysModuleMapper.findAllMy(userid);
	}

	public void add(SysModule sysModule) {
		sysModuleMapper.add(sysModule);
	}

	public void upd(SysModule sysModule) {
		sysModuleMapper.upd(sysModule);
	}

	public void del(long id) {
		sysModuleMapper.del(id);
	}

}
