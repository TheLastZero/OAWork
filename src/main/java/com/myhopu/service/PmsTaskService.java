package com.myhopu.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.myhopu.entity.PmsTask;
import com.myhopu.mapper.PmsTaskMapper;

@Service
public class PmsTaskService {

	@Resource
	PmsTaskMapper pmsTaskMapper;

	public List findAll(long pid) {
		return pmsTaskMapper.findAll(pid);
	}

	public void add(PmsTask pmsTask) {
		pmsTaskMapper.add(pmsTask);
	}

	public void upd(PmsTask pmsTask) {
		pmsTaskMapper.upd(pmsTask);
	}

	public void del(String ids) {
		String[] ids2 = ids.split(",");
		for (int i = 0; i < ids2.length; i++) {
			pmsTaskMapper.del(Long.parseLong(ids2[i]));
		}		
	}

}
