package com.myhopu.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.myhopu.entity.SysDept;
import com.myhopu.mapper.SysDeptMapper;

@Service
public class SysDeptService {
	@Resource
	SysDeptMapper sysDeptMapper;

	public List findAll() {
		return sysDeptMapper.findAll();
	}

	public void add(SysDept sysDept) {
		sysDeptMapper.add(sysDept);
	}

	public void upd(SysDept sysDept) {
		sysDeptMapper.upd(sysDept);
	}

	public void del(String ids) {
		String[] ids2 = ids.split(",");
		for (int i = 0; i < ids2.length; i++) {
			sysDeptMapper.del(Long.parseLong(ids2[i]));
		}
	}

	
	
}
