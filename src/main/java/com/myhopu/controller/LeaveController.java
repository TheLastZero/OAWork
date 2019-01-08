package com.myhopu.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ser.std.StdKeySerializers.Default;
import com.myhopu.entity.Leave;
import com.myhopu.service.LeaveService;
import com.myhopu.utils.DocumentHandler;
import com.myhopu.utils.Download;
import com.myhopu.utils.ExcelUtil;
import com.myhopu.utils.Msg;

@RequestMapping("/leave")
@Controller
public class LeaveController {

	@Autowired
	private LeaveService leaveService;

	/**
	 * 下载Excel版的通过请假的请假条信息
	 */
	@RequestMapping("/downloadLeaveExcel")
	public void downloadLeaveExcel(Integer leaveId,HttpServletRequest request, HttpServletResponse response) {

		//查询通过请假的信息list，改造封装为map
		List<Leave> leavelist = leaveService.getListByStatus(5l);

		String title = "批假表";
		String[] columnName = new String[]{"序号","请假类型","请假开始时间","请假截止时间","请假使用时间","请假人","请假状态"};
		List<Object[]> dataList = new ArrayList<Object[]>();
		Object[] objs;
		int i=1;
		for (Leave l : leavelist) {
			objs = new Object[columnName.length];
			objs[0] = i;
			objs[1] = l.getLeaveType();
			Date dStart = new Date(l.getTimeStart().getTime());
			if(dStart.getHours()==8) {
				objs[2] = dStart.toLocaleString()+" 上午";
			}else if(dStart.getHours()==14){
				objs[2] = dStart.toLocaleString()+" 下午";
			}

			Date dEnd = new Date(l.getTimeEnd().getTime());
			if(dEnd.getHours()==8) {
				objs[3] = dEnd.toLocaleString()+" 上午";
			}else if(dEnd.getHours()==14){
				objs[3] = dEnd.toLocaleString()+" 下午";
			}
			objs[4] = l.getTimeUsed();
			objs[5] = l.getUser().getRealname();
			objs[6] = "已通过";
			dataList.add(objs);
		}
		//实例化工具类
		ExcelUtil ex = new ExcelUtil("批假表",title, columnName, dataList,request,response);
		try {
			//导出excel
			ex.export();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 下载word版的通过请假的请假条信息
	 * downloadLeave
	 */
	@RequestMapping("/downloadLeaveWord")
	public void downloadLeaveWord(Integer leaveId,HttpServletRequest request, HttpServletResponse response) {

		//查询通过请假的信息list，改造封装为map
		List<Leave> leavelist = leaveService.getListByStatus(5l);

		Map dataMap = new HashMap();
		dataMap.put("title", "批假表");    
		int i = 1;
		List<Map<String, Object>> newsList=new ArrayList<Map<String,Object>>();  
		for(Leave l :leavelist){  
			Map<String, Object> map=new HashMap<String, Object>();  
			map.put("num", i);  
			map.put("leaveType", l.getLeaveType());  

			Date dStart = new Date(l.getTimeStart().getTime());
			if(dStart.getHours()==8) {
				map.put("timeStart", dStart.toLocaleString()+" 上午");
			}else if(dStart.getHours()==14){
				map.put("timeStart", dStart.toLocaleString()+" 下午");
			}

			Date dEnd = new Date(l.getTimeEnd().getTime());
			if(dEnd.getHours()==8) {
				map.put("timeEnd", dEnd.toLocaleString()+" 上午");
			}else if(dEnd.getHours()==14){
				map.put("timeEnd", dEnd.toLocaleString()+" 下午");
			}


			map.put("timeUsed", l.getTimeUsed());
			map.put("username", l.getUser().getRealname());
			map.put("leaveStatus", "已通过");
			newsList.add(map);  
			i++;
		}  
		dataMap.put("listTest",newsList);  

		//文件名为当前用户id加上时间戳
		String fileName = leaveId+System.currentTimeMillis()+"";

		String outUrl = DocumentHandler.createDoc(dataMap,"word.ftl",fileName);  
		System.out.println("文件路径为："+outUrl);

		Download.download("批假表文档.doc", outUrl, request, response);

	}

	/**
	 * 根据id删除请假记录
	 */
	@RequestMapping("/removeByids")
	@ResponseBody
	public Msg removeByids(String ids) {

		String [] s = ids.split(",");
		System.out.println("..."+Arrays.asList(s));

		long l=0;
		for(int i=0;i<s.length;i++) {
			l += leaveService.removeByids(Long.parseLong(s[i]));
		}

		if(l>0) {
			return Msg.success();
		}else{
			return Msg.fail();
		}


	}

	/**
	 * 根据请假id，修改请假状态
	 * @param leaveId
	 * @param leaveStatus
	 * @return
	 */
	@RequestMapping("/updateLeaveStatusById")
	@ResponseBody
	public Msg updateLeaveById(Leave leave) {

		long l = leaveService.update(leave);

		if(l>0) {
			return Msg.success();
		}else {
			return Msg.fail();
		}
	}

	/**
	 * getUserLeaveListAll
	 * 
	 * 人事查询所有请假信息，有直接同意驳回权
	 */
	@RequestMapping("/getUserLeaveListAll")
	@ResponseBody
	public Map<String, Object> getUserLeaveListAll(
			@RequestParam(value="page",defaultValue="1")String page,//当前第几页
			@RequestParam(value="rows",defaultValue="5")String rows//每页显示的记录条数
			) {
		/*
		 * 在查询之前，调用这个方法的一定是人事经理，只要经理打开了请假审核页面,
		 * 就将所有请假表状态为2的记录，更新状态为3，表示人事经理开始审核
		 */
		Leave l = new Leave();
		l.setLeaveStatus(3);
		leaveService.updateLeaveStatus(l,2);

		//返回map
		Map<String, Object> map = new HashMap<String, Object>();

		//查询条件map
		Map<String, Object> mapQuery = new HashMap<String, Object>();
		mapQuery.put("page", page);
		mapQuery.put("rows", rows);

		//按照条件查询总数量
		Long leaveCount  = leaveService.getCountAll();
		//按照条件查询具体数据
		List<Object> leaveList = leaveService.getUserLeaveListAll(mapQuery);


		map.put("rows", leaveList);//每页存放记录数
		map.put("total", leaveCount);

		return map;
	}

	/**
	 * getUserLeaveListByDid 
	 * 带分页和排序的部门经理查询，查询人所在部门下所有同部门员工的请假记录
	 * @param userId
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("/getUserLeaveListByDid")
	@ResponseBody
	public Map<String, Object> getUserLeaveListByDid(Long dpid,
			@RequestParam(value="page",defaultValue="1")String page,//当前第几页
			@RequestParam(value="rows",defaultValue="5")String rows//每页显示的记录条数
			) {

		/*
		 * 在查询之前，调用这个方法的一定是部门经理，只要经理打开了请假审核页面,
		 * 就将所有请假表状态为0的记录，更新状态为1，表示部门经理开始审核
		 */
		Leave l = new Leave();
		l.setLeaveStatus(1);
		leaveService.updateLeaveStatus(l,0);

		//返回map
		Map<String, Object> map = new HashMap<String, Object>();

		//查询条件map
		Map<String, Object> mapQuery = new HashMap<String, Object>();
		mapQuery.put("dpid", dpid);
		mapQuery.put("page", page);
		mapQuery.put("rows", rows);

		//按照条件查询总数量
		Long leaveCount  = leaveService.getCountByDid(mapQuery);
		//按照条件查询具体数据
		List<Object> leaveList = leaveService.getUserLeaveListByDid(mapQuery);


		map.put("rows", leaveList);//每页存放记录数
		map.put("total", leaveCount);

		return map;
	}

	/**
	 * getUserLeaveList
	 * @param userId
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("/getUserLeaveList")
	@ResponseBody
	public Map<String, Object> getUserLeaveList(Long userId,
			@RequestParam(value="page",defaultValue="1")String page,//当前第几页
			@RequestParam(value="rows",defaultValue="5")String rows//每页显示的记录条数
			) {

		//返回map
		Map<String, Object> map = new HashMap<String, Object>();

		//查询条件map
		Map<String, Object> mapQuery = new HashMap<String, Object>();
		mapQuery.put("userId", userId);
		mapQuery.put("page", page);
		mapQuery.put("rows", rows);

		//按照条件查询总数量
		Long leaveCount  = leaveService.selectCountByMapWithUser(mapQuery);
		//按照条件查询具体数据
		List<Object> leaveList = leaveService.selectByMapWithUser(mapQuery);


		map.put("rows", leaveList);//每页存放记录数
		map.put("total", leaveCount);

		return map;
	}

	/**
	 * beforeUpdate
	 * 修改前查询请假记录，然后转发到修改界面
	 */
	@RequestMapping("/beforeUpdate")
	public String beforeUpdate(Long leaveId,HttpServletRequest request) {

		Leave leave = leaveService.selectById(leaveId);
		request.setAttribute("leave", leave);

		return "/leave/ApplyLeave";
	}

	/**
	 * saveOrUpdate
	 */
	@RequestMapping("/saveOrUpdate")
	@ResponseBody
	public Msg saveOrUpdate(Leave leave,String ap,String timeStarts,String timeEnds) {//ap是选择的上下午
		DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd"); 
		Date dStart = null;
		try {
			dStart = format1.parse(timeStarts);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		/**
		 * 时间处理
		 */
		//上午时间我们默认为8点，下午时间我们默认为2点
		//请假开始时间处理
		if("上午".equals(ap)) {
			dStart.setTime(dStart.getTime()+1000 * 60 * 60 * 8);
		}else if("下午".equals(ap)){
			dStart.setTime(dStart.getTime()+1000 * 60 * 60 * 14);
		}

		String [] te = timeEnds.split(" ");//空格分割时间和上下午

		//请假结束时间处理
		Date dEnd = new Date(te[0]);
		if("上午".equals(te[1])) {
			dEnd.setTime(dEnd.getTime()+1000 * 60 * 60 * 8);
		}else if("下午".equals(te[1])){
			dEnd.setTime(dEnd.getTime()+1000 * 60 * 60 * 14);
		}
		leave.setTimeStart(dStart);
		leave.setTimeEnd(dEnd);

		System.out.println("请假开始时间为："+dStart);
		System.out.println("请假结束时间为："+dEnd);

		//设置请假状态为0已录入
		leave.setLeaveStatus(0);

		System.out.println("要添加的leave为："+leave);

		/**
		 * 请假时间重叠检查
		 */
		//查询当前用户请假记录
		List<Leave> leaveList = leaveService.selectAllByuserId(leave.getUserId(),leave.getLeaveId());
		System.out.println(leaveList.size());
		//		System.out.println(leaveList);
		//		System.out.println(leave.getTimeStart().getTime() == leaveList.get(0).getTimeStart().getTime());
		//		System.out.println(leave.getTimeEnd().getTime() == leaveList.get(0).getTimeEnd().getTime());

		Integer i=0;
		boolean f = false;//是否可以添加

		if(leaveList.size()==0) {
			/*
			 * 1、没有请假记录时，直接添加
			 */
			i = leaveService.add(leave);//返回的是执行成功影响的条数
			return Msg.success();
		}else if(leaveList.size()==1) {


			/*
			 * 2、 添加只有一条请假记录时，
			 *  我们只需要判断请假开始日期是否大于或等于
			 *  数据库中那一条请假记录的截止日期
			 *  或者
			 *  结束日期小于等于数据库中那一条记录的开始日期
			 */
			if(leave.getTimeStart().getTime() >= leaveList.get(0).getTimeEnd().getTime() || 
					leave.getTimeEnd().getTime() <= leaveList.get(0).getTimeStart().getTime()) {
				f = true;
			}

		}else if(leaveList.size() >=2) {

			/*
			 * 3、当数据库请假记录>=2条时，遍历所有记录
			 *  	1)、如果当前记录的截止时间 比 在所有请假记录中最早的一条请假记录的开始时间还要早，那么可以添加
			 *  	2)、在循环中如果当前记录的开始时间 比 list(x)的截止时间大 且截止时间比list(x+1)的开始时间小 那么可以添加
			 * 		3)、如果当前记录的开始时间 比 在所有请假记录中最晚的一条请假记录的截止时间还要晚，那么可以添加
			 * 
			 * 		除了以上三种情况之外，全部返回false
			 */
			for(int j=0;j<leaveList.size()-1;j++) {


				//1)、的实现
				if(leave.getTimeEnd().getTime() <= leaveList.get(0).getTimeStart().getTime()) {
					f = true;
					break;
				}

				//3)、的实现
				if(leave.getTimeStart().getTime() >= leaveList.get(leaveList.size()-1).getTimeEnd().getTime()) {
					f = true;
					break;
				}

				//2)、的实现
				if(leave.getTimeStart().getTime() >= leaveList.get(j).getTimeEnd().getTime() && 
						leave.getTimeEnd().getTime() <= leaveList.get(j+1).getTimeStart().getTime() ) {
					f = true;
					break;
				}

			}
		}



		if(f==true) {
			//判断是添加还是修改
			if(leave.getLeaveId()==null) {//添加
				i = leaveService.add(leave);//返回的是执行成功影响的条数
			}else {//修改
				i = leaveService.update(leave);
			}

			if(i>0) {
				return Msg.success();
			}

		}else {
			return Msg.fail().add("leaveMessage", "请假日期重叠，请检查历史请假记录");
		}




		return Msg.fail().add("leaveMessage", "未知错误");
	}

	/**
	 * 跳转到人事处理请假页面
	 */
	@RequestMapping("/adminMaxLeaveList")
	public String AdminMaxLeaveList() {

		//判断当前用户的权限，查询权限管理下所有的请假信息

		return "/leave/AdminMaxLeaveList";
	}

	/**
	 * 跳转到部门处理请假页面
	 */
	@RequestMapping("/leaveList")
	public String leaveList() {

		//判断当前用户的权限，查询权限管理下所有的请假信息

		return "/leave/AdminLeaveList";
	}

	/**
	 * 跳转到请假页面
	 * @return
	 */
	@RequestMapping("/goApplyLeave")
	public String goApplyLeave() {
		return "/leave/ApplyLeave";
	}

}
