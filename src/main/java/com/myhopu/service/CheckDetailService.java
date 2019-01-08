package com.myhopu.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.myhopu.entity.CheckDetail;
import com.myhopu.entity.CheckDetailExample;
import com.myhopu.entity.CheckDetailExample.Criteria;
import com.myhopu.mapper.CheckDetailMapper;

@Service
public class CheckDetailService {

	@Autowired
	private CheckDetailMapper checkDetailMapper;

	public void insertPitch(List<CheckDetail> cdList) {
		 checkDetailMapper.insertPitch(cdList);
	}

	public long selectByCheckCodeTimeCheck(Long checkCode, Date timeCheck) {
		CheckDetailExample cde = new CheckDetailExample();
		Criteria c = cde.createCriteria();
		c.andCheckCodeEqualTo(checkCode);
		c.andTimeCheckEqualTo(timeCheck);
		
		return checkDetailMapper.selectByExample(cde).size();
	}

	/**
	 * 根据考勤机号码，和年月查出此人当月的所有打卡记录
	 * @param mapQuery
	 * @return
	 */
	public List<CheckDetail> selectByCheckCodeTime(Map<String, Object> mapQuery) {
		return checkDetailMapper.selectByCheckCodeTime(mapQuery);
	}
	
}
