package com.myhopu.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.myhopu.entity.PmsProject;
import com.myhopu.mapper.PmsProjectMapper;

@Service
public class PmsProjectService {
	@Resource
	PmsProjectMapper pmsProjectMapper;

	public List findAll() {
		return pmsProjectMapper.findAll();
	}

	public void add(PmsProject pmsProject) {
		pmsProjectMapper.add(pmsProject);
	}

	public void upd(PmsProject pmsProject) {
		pmsProjectMapper.upd(pmsProject);
	}

	public void del(String ids) {
		String[] ids2 = ids.split(",");
		for (int i = 0; i < ids2.length; i++) {
			pmsProjectMapper.del(Long.parseLong(ids2[i]));
		}
	}

}
