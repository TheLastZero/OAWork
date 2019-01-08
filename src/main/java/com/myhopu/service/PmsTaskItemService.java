package com.myhopu.service;

import java.util.List;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.myhopu.entity.PmsTaskItem;
import com.myhopu.mapper.PmsTaskItemMapper;

@Service
public class PmsTaskItemService {

	@Resource
	PmsTaskItemMapper pmsTaskItemMapper;

	public List findAll(long pid) {
		return pmsTaskItemMapper.findAll(pid);
	}
	
	public List findAllMy(long userid) {
		return pmsTaskItemMapper.findAllMy(userid);
	}

	public void add(PmsTaskItem pmsTaskItem) {
		pmsTaskItemMapper.add(pmsTaskItem);
	}

	public void upd(PmsTaskItem pmsTaskItem) {
		pmsTaskItemMapper.upd(pmsTaskItem);
	}

	public void del(String ids) {
		String[] ids2 = ids.split(",");
		for (int i = 0; i < ids2.length; i++) {
			pmsTaskItemMapper.del(Long.parseLong(ids2[i]));
		}		
	}

}
