package com.myhopu.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.myhopu.entity.CheckDateOption;
import com.myhopu.entity.CheckDateOptionExample;
import com.myhopu.entity.CheckDateOptionExample.Criteria;
import com.myhopu.mapper.CheckDateOptionMapper;

@Service
public class CheckDateOptionService {

	@Autowired
	private CheckDateOptionMapper checkDateOptionMapper;

	/**
	 * 添加
	 * @param cdoList
	 * @return
	 */
	public long addPitch(List<CheckDateOption> cdoList) {
		long l = 0;
		
		for(CheckDateOption c:cdoList) {
			checkDateOptionMapper.insertSelective(c);
			l++;
		}
		return l;
	}

	/**
	 * 修改
	 * @param cdoList
	 * @return
	 */
	public long updatePitch(List<CheckDateOption> cdoList) {
		
		
		long l = 0;
		
		
		CheckDateOption cc = new CheckDateOption();
		for(CheckDateOption c:cdoList) {
			CheckDateOptionExample ce = new CheckDateOptionExample();
			Criteria cr = ce.createCriteria();
			cr.andSettingIdEqualTo(c.getSettingId());//设置修改条件
			
			//由于我们每次只修改am，pm两个值，把其他所有字段都更新太浪费了
			cc.setAmStatus(c.getAmStatus());
			cc.setPmStatus(c.getPmStatus());
			
			checkDateOptionMapper.updateByExampleSelective(cc, ce);
			l++;
		}
		
		return l;
	}


	public List<CheckDateOption> selectByYearMounth(Map<String, Object> mapQuery) {
		
		return checkDateOptionMapper.selectByYearMounth(mapQuery);
	}
	
}
