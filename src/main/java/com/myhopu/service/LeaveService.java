package com.myhopu.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.myhopu.entity.Leave;
import com.myhopu.entity.LeaveExample;
import com.myhopu.entity.LeaveExample.Criteria;
import com.myhopu.mapper.LeaveMapper;

@Service
public class LeaveService {

	@Autowired
	private LeaveMapper leaveMapper;

	public Integer add(Leave leave) {
		
		return leaveMapper.insertSelective(leave);
	}

	/**
	 * 根据用户id查询记录，根据请假开始日期排序返回
	 * @param userId
	 * @param leaveId 
	 * @return
	 */
	public List<Leave> selectAllByuserId(Long userId, Long leaveId) {
		LeaveExample le = new LeaveExample();
		le.setOrderByClause("TIME_START asc");
		Criteria c = le.createCriteria();
		c.andUserIdEqualTo(userId);
		//若为修改时间检查时，排除自身
		if(leaveId!=null) {
			c.andLeaveIdNotEqualTo(leaveId);
		}
		
		
		return leaveMapper.selectByExample(le);
	}

	public List<Object> selectByMapWithUser(Map<String, Object> mapQuery) {
		// TODO Auto-generated method stub
		return leaveMapper.selectByMapWithUser(mapQuery);
	}

	/**
	 * 根据map条件查询总数量
	 * @param mapQuery
	 * @return
	 */
	public Long selectCountByMapWithUser(Map<String, Object> mapQuery) {
		
		return leaveMapper.selectCountByMapWithUser(mapQuery);
	}

	public Leave selectById(Long leaveId) {
		LeaveExample le = new LeaveExample();
		Criteria c = le.createCriteria();
		c.andLeaveIdEqualTo(leaveId);
		
		return leaveMapper.selectByExample(le).get(0);
	}

	/**
	 * 根据id修改
	 * @param leave
	 * @return
	 */
	public Integer update(Leave leave) {
		LeaveExample le = new LeaveExample();
		Criteria c = le.createCriteria();
		c.andLeaveIdEqualTo(leave.getLeaveId());
		
		return leaveMapper.updateByExampleSelective(leave, le);
	}

	/**
	 * 根据id删除
	 * @param leaveId
	 * @return
	 */
	public long removeByids(Long leaveId) {
		return leaveMapper.deleteByPrimaryKey(leaveId);
	}

	/**
	 * 根据dpid查询记录数量
	 * @param mapQuery
	 * @return
	 */
	public Long getCountByDid(Map<String, Object> mapQuery) {
		
		return leaveMapper.getCountByDid(mapQuery);
	}

	/**
	 * 根据dpid查询所有当前部门下的所有记录
	 * @param mapQuery
	 * @return
	 */
	public List<Object> getUserLeaveListByDid(Map<String, Object> mapQuery) {
		return leaveMapper.getUserLeaveListByDid(mapQuery);
	}

	/**
	 * 根据状态修改请假状态
	 * 如果有leaveid，就加上此条件
	 * 
	 * 如：所有请假表状态为0的记录，更新状态为1，表示部门经理开始审核
	 * @param setLeaveStatus
	 */
	public void updateLeaveStatus(Leave leave,long changStatus ) {
		LeaveExample le = new LeaveExample();
		Criteria c = le.createCriteria();
		c.andLeaveStatusEqualTo(changStatus);
		
		if(leave.getLeaveId()!=null) {
			c.andLeaveIdEqualTo(leave.getLeaveId());
		}
		
		leaveMapper.updateByExampleSelective(leave, le);
		
	}

	/**
	 * 查询请假数量总和
	 * @param mapQuery
	 * @return
	 */
	public Long getCountAll() {
		return leaveMapper.getCountAll();
	}

	/**
	 * 分页查询所有请假记录
	 * @param mapQuery
	 * @return
	 */
	public List<Object> getUserLeaveListAll(Map<String, Object> mapQuery) {
		return leaveMapper.getUserLeaveListAll(mapQuery);
	}

	public List<Leave> getListByStatus(Long status) {
		
		return leaveMapper.selectListByStatus(status);
	}

	public List<Leave> selectByUseridTimeStart(Map<String, Object> map) {
		
		return leaveMapper.selectByUseridTimeStart(map);
	}

	
	

}
