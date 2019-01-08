package com.myhopu.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.myhopu.mapper.PmsChatMapper;

@Service
public class PmsChatService {
	@Resource
	PmsChatMapper pmsChatMapper;
	
	public List findAllDeptUser() {
		List list = null;
		list = pmsChatMapper.findAllDeptUser();
		return list;
	}

	public void sendMsg(Map map) {
		pmsChatMapper.sendMsg(map);
	}

	public List rcvMsg(long userid) {
		List list = null;
		list = pmsChatMapper.rcvMsg(userid);
		return list;
	}

	public List getState(long userid) {
		List list = null;
		list = pmsChatMapper.getState(userid);
		return list;
	}

	public void changeState(long userid) {
		pmsChatMapper.changeState(userid);
	}

	public boolean checkIsUser(String name) {
		List list = pmsChatMapper.checkIsUser(name);
		if (list.size() > 0) {
			return true;
		}
		return false;
	}

}
