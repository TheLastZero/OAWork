package com.myhopu.controller;

import java.io.File;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.myhopu.entity.CheckDateOption;
import com.myhopu.entity.CheckDetail;
import com.myhopu.entity.Leave;
import com.myhopu.entity.ReturnCheckDetail;
import com.myhopu.entity.SysUser;
import com.myhopu.service.CheckDateOptionService;
import com.myhopu.service.CheckDetailService;
import com.myhopu.service.LeaveService;
import com.myhopu.service.SysUserService;
import com.myhopu.utils.DocumentHandler;
import com.myhopu.utils.ExcelUtil;
import com.myhopu.utils.ImportExcelUtil;
import com.myhopu.utils.Msg;
import com.myhopu.utils.MyCalender;
import com.myhopu.utils.MyDateFormatUtil;
import com.sun.net.httpserver.Authenticator.Success;

@RequestMapping("/checkDetail")
@Controller
public class CheckDetailController {

	@Autowired
	private CheckDetailService checkDetailService;

	@Autowired
	private SysUserService userService;

	@Autowired
	private CheckDateOptionService checkDateOptionService;

	@Autowired
	private LeaveService leaveService;

	/**
	 * 返回当月罚款图数据给页面
	 */
	@RequestMapping("/removeMoneyGraph")
	@ResponseBody
	public Msg removeMoneyGraph(
			Integer year,
			Integer mounth) {
		
		//根据月份，获取薪酬集合
		List<ReturnCheckDetail> rList = getSalaryList(year, mounth);
		
		return Msg.success().add("salaryList", rList);
	}
	
	/**
	 * 处理导出薪酬报表
	 */
	@RequestMapping("/downloadMoneyMounthExcel")
	public void downloadMoneyMounthExcel(
			Integer year,
			Integer mounth,
			HttpServletRequest request, HttpServletResponse response) {

		System.out.println("开始处理薪酬报表");
		//根据月份，获取薪酬集合
		List<ReturnCheckDetail> rList = getSalaryList(year, mounth);


		String title = "薪酬月报表("+year+"-"+mounth+")";
		String[] columnName = new String[]{"部门名称","员工姓名","登记号码","月份","基本工资","工龄奖","旷工扣款","迟到扣款","事假扣款","病假扣款","应发工资"};
		List<Object[]> dataList = new ArrayList<Object[]>();
		Object[] objs;
		int i=1;
		for (ReturnCheckDetail r : rList) {
			objs = new Object[columnName.length];
			objs[0] = r.getDname();
			objs[1] = r.getRealname();
			objs[2] = r.getCheckDetail().getCheckCode();
			objs[3] = year+"-"+mounth;
			objs[4] = r.getMoneyBase();
			objs[5] = r.getMoneySalary();
			objs[6] = "-"+r.getMoneyAbsent();
			objs[7] = "-"+r.getMoneyLater();
			objs[8] = "-"+r.getMoneyThing();
			objs[9] = "-"+r.getMoneyIll();
			objs[10] = r.getSalary();
			dataList.add(objs);
		}
		//实例化工具类
		ExcelUtil ex = new ExcelUtil("薪酬月报表("+year+"-"+mounth+")",title, columnName, dataList,request,response);
		try {
			//导出excel
			ex.export();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 根据月份，获取薪酬集合
	 * @param year
	 * @param mounth
	 * @return
	 */
	private List<ReturnCheckDetail> getSalaryList(Integer year, Integer mounth) {
		//根据年月，返回考勤月报表List
		List<ReturnCheckDetail> returnList = getMounthDetailList(year, mounth);

		//导出到Excel的最终结果
		List<ReturnCheckDetail> rList = new ArrayList<ReturnCheckDetail>();

		//		long moneySalary;//工龄奖
		long moneyAbsent = 0;//旷工扣款
		long moneyLater=0;//迟到扣款
		long moneyThing=0;//事假扣款
		long moneyIll=0;//病假扣款
		int illNum = 0;//记录病假次数
		//		long salary;//应发工资

		for(int i = 0;i<returnList.size();i++) {
//			System.out.println("循环"+returnList.get(i));

			//计算旷工扣款，没半天旷工扣一百
			if(returnList.get(i).getStatus().equals("旷工")) {
				moneyAbsent += 100;
			}

			//迟到每次扣十块
			if(returnList.get(i).getStatus().equals("迟到")) {
				moneyLater += 10;
			}

			//事假每半天扣40
			if(returnList.get(i).getStatus().equals("事假")) {
				moneyThing += 40;
			}

			//病假，每月不超过1天时不扣款，超过1天后每半天扣20
			if(returnList.get(i).getStatus().equals("病假")) {
				illNum++;
				if(illNum > 2) {
					moneyIll += 20;
				}

			}

			
			if(i==returnList.size()-1) {//最后一个元素，直接保存
					ReturnCheckDetail rOld = returnList.get(i);
					ReturnCheckDetail r = new ReturnCheckDetail();
					r.setDname(rOld.getDname());
					r.setRealname(rOld.getRealname());
					r.setCheckDetail(rOld.getCheckDetail());
					r.setMoneyBase(rOld.getMoneyBase());

					//1、计算工龄奖  现在的年份减去加入公司时的年份
					Date y = new Date();
					int yearWork = y.getYear() - rOld.getCreateTime().getYear();//工作年份
					if(yearWork == 2) {//满两年每个月有160的工龄奖，
						r.setMoneySalary(160);
					}else if(yearWork > 2){//第三年开始，每年多加80
						r.setMoneySalary((yearWork - 2)*80 + 160);
					}

					//2、计算旷工扣款
					r.setMoneyAbsent(moneyAbsent);

					//3、计算迟到扣款
					r.setMoneyLater(moneyLater);

					//4、计算事假扣款
					r.setMoneyThing(moneyThing);

					//5、计算病假扣款
					r.setMoneyIll(moneyIll);

					//6、计算应发工资
					r.setSalary(
							r.getMoneyBase()+r.getMoneySalary()-moneyAbsent-moneyLater-moneyThing-moneyIll
							);

					rList.add(r);

					//为下一个人初始化
					moneyAbsent = 0;//旷工扣款
					moneyLater=0;//迟到扣款
					moneyThing=0;//事假扣款
					moneyIll=0;//病假扣款
					illNum = 0;//记录病假次数
			}else if(i!=0){//不能等于第一个，不可能只有一个元素，因为一个用户至少有一个月的考勤信息

				//判断本次的用户考勤机id与前一次的id是否相同，不相同的话说明为另一个员工记录的开始
				if(returnList.get(i).getUserid() != returnList.get(i-1).getUserid()) {

					ReturnCheckDetail rOld = returnList.get(i-1);
					ReturnCheckDetail r = new ReturnCheckDetail();
					r.setDname(rOld.getDname());
					r.setRealname(rOld.getRealname());
					r.setCheckDetail(rOld.getCheckDetail());
					r.setMoneyBase(rOld.getMoneyBase());

					//1、计算工龄奖  现在的年份减去加入公司时的年份
					Date y = new Date();
					int yearWork = y.getYear() - rOld.getCreateTime().getYear();//工作年份
					if(yearWork == 2) {//满两年每个月有160的工龄奖，
						r.setMoneySalary(160);
					}else if(yearWork > 2){//第三年开始，每年多加80
						r.setMoneySalary((yearWork - 2)*80 + 160);
					}

					//2、计算旷工扣款
					r.setMoneyAbsent(moneyAbsent);

					//3、计算迟到扣款
					r.setMoneyLater(moneyLater);

					//4、计算事假扣款
					r.setMoneyThing(moneyThing);

					//5、计算病假扣款
					r.setMoneyIll(moneyIll);

					//6、计算应发工资
					r.setSalary(
							r.getMoneyBase()+r.getMoneySalary()-moneyAbsent-moneyLater-moneyThing-moneyIll
							);

					rList.add(r);

					//为下一个人初始化
					moneyAbsent = 0;//旷工扣款
					moneyLater=0;//迟到扣款
					moneyThing=0;//事假扣款
					moneyIll=0;//病假扣款
					illNum = 0;//记录病假次数
				}
			}
		}
		return rList;
	}

	/**
	 * 处理导出考勤月报表
	 */
	/**
	 * @param year
	 * @param mounth
	 * @param request
	 * @param response
	 */
	@RequestMapping("/downloadCheckDetailMounthExcel")
	public void downloadCheckDetailMounthExcel(
			Integer year,
			Integer mounth,
			HttpServletRequest request, HttpServletResponse response) {

		//根据年月，返回考勤月报表List
		List<ReturnCheckDetail> returnList = getMounthDetailList(year, mounth);


		System.out.println("*******************************************");


		//		for(CheckDateOption c :cdoList) {
		//			System.out.println("考勤信息"+c);
		//		}
		//				for(ReturnCheckDetail r:cdList) 
		//					System.out.println("每人考勤记录："+r)
		//				}

		//查看一下最后得到的数
		//		for(ReturnCheckDetail r :returnList) 
		//			System.out.println("循环返回数据："+r)
		//		}
		//		System.out.println("下标集合为："+cdIndexList);


		String title = "考情月报表("+year+"-"+mounth+")";
		String[] columnName = new String[]{"部门名称","员工姓名","登记号码","应出勤日期时间","打卡日期时间","机器号","对比方式","出勤状态"};
		List<Object[]> dataList = new ArrayList<Object[]>();
		Object[] objs;
		int i=1;
		for (ReturnCheckDetail r : returnList) {
			objs = new Object[columnName.length];
			objs[0] = r.getDname();
			objs[1] = r.getRealname();
			objs[2] = r.getCheckDetail().getCheckCode();
			objs[3] = r.getTimeShould();
			objs[4] = r.getTimeCheck();
			objs[5] = r.getCheckDetail().getCheckNum();
			objs[6] = r.getCheckDetail().getCheckType();
			objs[7] = r.getStatus();
			dataList.add(objs);
		}
		//实例化工具类
		ExcelUtil ex = new ExcelUtil("考情月报表("+year+"-"+mounth+")",title, columnName, dataList,request,response);
		try {
			//导出excel
			ex.export();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 根据年月，返回考勤月报表List
	 * @param year
	 * @param mounth
	 * @return
	 */
	private List<ReturnCheckDetail> getMounthDetailList(Integer year, Integer mounth) {
		//1、先查询数据库中是否存在当年当月的考勤设置，如果有我们获取list集合
		//查询条件map
		Map<String, Object> mapQuery2 = new HashMap<String, Object>();
		mapQuery2.put("time", year+"-"+mounth);

		List<CheckDateOption> cdoList = checkDateOptionService.selectByYearMounth(mapQuery2);

		//查询条件map
		Map<String, Object> mapQuery = new HashMap<String, Object>();
		mapQuery.put("time", year+"/"+mounth);
		//2、每个人的所有打卡记录
		List<ReturnCheckDetail> cdList = getCheckDetailList(mapQuery);


		//获取这个月有多少天
		int dates = MyCalender.getDaysByYearMounth(year, mounth);

		//记录每一个员工打卡记录开始的下标集合
		List<Integer> cdIndexList = new ArrayList<Integer>();

		//3、先遍历每个人的打卡记录
		for(int i = 0;i<cdList.size();i++) {

			if(i==0) {//把第一个的下标放入到cdIndexList集合中
				cdIndexList.add(i);
			}else {

				//				System.out.println("判断为：");
				//				System.out.println(cdList.get(i).getUserid() == cdList.get(i-1).getUserid());
				//判断本次的用户考勤机id与前一次的id是否相同，不相同的话说明为另一个员工记录的开始
				if(cdList.get(i).getUserid() != cdList.get(i-1).getUserid()) {
					cdIndexList.add(i);
				}
			}
		}

		//最终返回的list
		List<ReturnCheckDetail> returnList = new ArrayList<ReturnCheckDetail>();



		//上午上班时间date
		Date dam = new Date();

		//下午上班时间date
		Date dpm = new Date();

		GregorianCalendar gc = new GregorianCalendar();

		//根据cdIndexList员工记录开始集合，遍历我们需要遍历几次全部的天数
		for(int j=0;j<cdIndexList.size();j++) {

			//每一个员工考勤记录开始的下标
			int startIndex = cdIndexList.get(j);
			//每个员工的基本信息
			ReturnCheckDetail rBase = cdList.get(j);

			boolean apm = true;//用来记录上下午的boolean，true上午，false下午

			//根据员工id，和年月查询其所有请假记录
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("userid", cdList.get(startIndex).getUserid());
			map.put("time", year+"/"+mounth);
			List<Leave> leaveList = 
					leaveService.selectByUseridTimeStart(map);

			//遍历考情设置信息
			for(int i = 0;i<dates;i++) {//从第一天开始到最后一天的考勤信息
				
				

				//当前返回信息
				ReturnCheckDetail rcd = cdList.get(startIndex+i);
				ReturnCheckDetail rcd2 = new ReturnCheckDetail();
				rcd2.setUserid(rcd.getUserid());
				rcd2.setDname(rcd.getDname());
				rcd2.setRealname(rcd.getRealname());
				rcd2.setCheckDetail(rcd.getCheckDetail());
				rcd2.setMoneyBase(rcd.getMoneyBase());
				rcd2.setCreateTime(rcd.getCreateTime());


				if(apm==true) {//上午***

					
					//初始化这一天上午的日期比对时间
					gc.set(year, mounth-1, i+1,8,0,0);
					dam.setTime(gc.getTimeInMillis());
					rcd2.setTimeShould(MyDateFormatUtil.getString2(dam));
					//rcd.setTimeShould("上午");

					//判断当前日期时间天数是否与遍历天数对应
					if(rcd.getCheckDetail().getTimeCheck().getDate() == i+1) {

						//日期天数对应，进入上下午判断
						if(rcd.getCheckDetail().getTimeCheck().getHours() < 12) {//上午

							rcd2.setTimeCheck(rcd.getTimeCheck());


							//判断上午有没有迟到
							//							System.out.println("判断："+rcd.getCheckDetail().getTimeCheck().toLocaleString());
							//							System.out.println("判断："+dam.toLocaleString());
							if(rcd.getCheckDetail().getTimeCheck().getTime() < dam.getTime()) {//未迟到

								rcd2.setStatus("出勤");

							}else {//迟到
								rcd2.setStatus("迟到");
							}



						}else {//下午，由下午的情况下上午未签到

							//上下午对不上，说明这一天上午请假了
							startIndex--;

							rcd2.setStatus("旷工");

							//对比其所有请假记录，是否包含这一天上午
							//判断当前空白工期的时间，是否在请假记录中
							for(Leave l:leaveList) {

								//								System.out.println("请假信息："+l);
								//								
								//								System.out.println("开始时间："+l.getTimeStart().toLocaleString());
								//								System.out.println("结束时间："+l.getTimeEnd().toLocaleString());
								//								System.out.println("对比的时间为："+dam.toLocaleString());
								//根据上班的时间与leaveList中的请假记录对比，是否有此范围的请假
								if(l.getTimeStart().getTime() <= dam.getTime()
										&& l.getTimeEnd().getTime() > dam.getTime()) {
									System.out.println("请假的类型为："+l.getLeaveType());
									//有此范围的请假记录
									rcd2.setStatus(l.getLeaveType());
									break;
								}else {//没有此范围的请假记录

								}

							}


							rcd2.setTimeCheck("未签到");
							//rcd2.setStatus("上午未签到");
						}



					}else {//天数不对应时，说明这一天都请假了，
						startIndex--;

						rcd2.setStatus("旷工");

						//对比其所有请假记录，是否包含这一天上午
						//判断当前空白工期的时间，是否在请假记录中
						for(Leave l:leaveList) {

							//根据上班的时间与leaveList中的请假记录对比，是否有此范围的请假
							if(l.getTimeStart().getTime() <= dam.getTime()
									&& l.getTimeEnd().getTime() > dam.getTime()) {
								System.out.println("请假的类型为："+l.getLeaveType());
								//有此范围的请假记录
								rcd2.setStatus(l.getLeaveType());
								break;
							}else {//没有此范围的请假记录

							}

						}

						rcd2.setTimeCheck("未签到");
						//rcd2.setStatus("上午未签到2");
					}


					if(cdoList.get(i).getAmStatus()==0) {//考情表设置此时为工作时间，无需在此操作
						
					}else if(cdoList.get(i).getAmStatus()==1) {//考情表设置此时为休息时间
						
						if(cdoList.get(i).getIsHoliday()==1) {//周末
							rcd2.setStatus("周末");
						}else if(cdoList.get(i).getIsHoliday()==2) {//节假日
							rcd2.setStatus(cdoList.get(i).getMessage());
						}
					}

					apm = false;
					i--;
					startIndex++;
				}else{//下午***

					
					
					//初始化这一天下午的日期比对时间
					gc.set(year, mounth-1, i+1,14,0,0);
					dpm.setTime(gc.getTimeInMillis());
					rcd2.setTimeShould(MyDateFormatUtil.getString2(dpm));
					//rcd.setTimeShould("下午");


					//判断当前日期时间天数是否与遍历天数对应
					if(rcd.getCheckDetail().getTimeCheck().getDate() == i+1) {

						//日期天数对应，进入上下午判断
						if(rcd.getCheckDetail().getTimeCheck().getHours() > 12) {//下午

							rcd2.setTimeCheck(rcd.getTimeCheck());


							//判断上午有没有迟到
							if(rcd.getCheckDetail().getTimeCheck().getTime() < dpm.getTime()) {//未迟到

								rcd2.setStatus("出勤");


							}else {//迟到
								rcd2.setStatus("迟到");
							}



						}else {//上午

							//上下午对不上，说明这一天下午请假了
							startIndex--;

							//							rcd2.setTimeCheck("未签到");
							//							rcd2.setStatus("下午未签到");

						}



					}else {//天数不对应时，说明这一天都请假了，或者前一天的下午请假了

						//判断前一天都请假了，还是只有下午请假
						//获取上午的信息
						//ReturnCheckDetail rcdprev = returnList.get(startIndex+i+1-1);

						rcd2.setStatus("旷工");

						//对比其所有请假记录，是否包含这一天上午
						//判断当前空白工期的时间，是否在请假记录中
						for(Leave l:leaveList) {

							//根据上班的时间与leaveList中的请假记录对比，是否有此范围的请假
							if(l.getTimeStart().getTime() <= dpm.getTime()
									&& l.getTimeEnd().getTime() > dpm.getTime()) {
								//有此范围的请假记录
								rcd2.setStatus(l.getLeaveType());
								break;
							}else {//没有此范围的请假记录

							}

						}

						startIndex--;
						rcd2.setTimeCheck("未签到");
						//						rcd2.setStatus("下午请假");
					}


					if(cdoList.get(i).getPmStatus()==0) {//考情表设置此时为工作时间，无需在此操作
						
					}else if(cdoList.get(i).getPmStatus()==1) {//考情表设置此时为休息时间
						
						if(cdoList.get(i).getIsHoliday()==1) {//周末
							rcd2.setStatus("周末");
						}else if(cdoList.get(i).getIsHoliday()==2) {//节假日
							rcd2.setStatus(cdoList.get(i).getMessage());
						}
					}


					apm = true;
				}


				//System.out.println(rcd);



				returnList.add(rcd2);//添加到返回集合中

			}


			//			break;//测试的时候我先看第一条，不然数据太多看不清
		}
		return returnList;
	}

	/**
	 * 处理导出考勤明细表Excel的方法
	 */
	@RequestMapping("/downloadcCeckDetailExcel")
	public void downloadcCeckDetailExcel(
			Integer year,
			Integer mounth,
			HttpServletRequest request, HttpServletResponse response) {

		System.out.println("获取到的为："+year+"-"+mounth);

		//查询条件map
		Map<String, Object> mapQuery = new HashMap<String, Object>();
		mapQuery.put("time", year+"/"+mounth);

		//每个人的所有打卡记录
		List<ReturnCheckDetail> cdList = getCheckDetailList(mapQuery);

		String title = "考情明细表("+year+"-"+mounth+")";
		String[] columnName = new String[]{"部门名称","员工姓名","登记号码","打卡日期时间","机器号","对比方式"};
		List<Object[]> dataList = new ArrayList<Object[]>();
		Object[] objs;
		int i=1;
		for (ReturnCheckDetail r : cdList) {
			objs = new Object[columnName.length];
			objs[0] = r.getDname();
			objs[1] = r.getRealname();
			objs[2] = r.getCheckDetail().getCheckCode();
			objs[3] = r.getTimeCheck();
			objs[4] = r.getCheckDetail().getCheckNum();
			objs[5] = r.getCheckDetail().getCheckType();
			dataList.add(objs);
		}
		//实例化工具类
		ExcelUtil ex = new ExcelUtil("考情明细表("+year+"-"+mounth+")",title, columnName, dataList,request,response);
		try {
			//导出excel
			ex.export();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 根据年份和月份，来查询所有的考勤记录
	 * 需要支持数据清洗（同一时间段，打卡时间只取最早的一条）
	 */
	@SuppressWarnings("unused")
	@RequestMapping("/getDetailListByPage")
	@ResponseBody
	public Map<String, Object> getUserLeaveListByDid(
			Integer year,
			Integer mounth,
			@RequestParam(value="page",defaultValue="1")String page,//当前第几页
			@RequestParam(value="rows",defaultValue="5")String rows//每页显示的记录条数
			) {

		System.out.println("当前第："+page+"页");

		//返回map
		Map<String, Object> map = new HashMap<String, Object>();
		//查询条件map
		Map<String, Object> mapQuery = new HashMap<String, Object>();
		mapQuery.put("time", year+"/"+mounth);
		mapQuery.put("page", page);
		mapQuery.put("rows", rows);

		//返回List
		List<ReturnCheckDetail> returnList = getCheckDetailList(mapQuery);



		//		for(ReturnCheckDetail r : returnList) {
		//			System.out.println("循环："+r);
		//		}

		int page2 = Integer.parseInt(page);
		int row2 = Integer.parseInt(rows);

		List<ReturnCheckDetail> rcList = null;
		if(page2 * row2 > returnList.size()) {//如果请求数量大于总数量
			rcList = returnList.subList((page2-1) * row2, returnList.size());
		}else {
			rcList = returnList.subList((page2-1) * row2, page2*row2);
		}



		map.put("total", returnList.size());
		map.put("rows", rcList);//每页存放记录数


		return map;
	}


	/**
	 * 获取所有人的指定年月的考情记录
	 * 
	 * 1、也就是说根据传入的map条件
	 *  mapQuery.put("time", year+"/"+mounth);
	 *  
		加上mapQuery.put("checkCode", u.getCheckCode());这里的map条件，
		先查询所有 有考勤机id员工 的员工集合

	   2、遍历所有员工，的同时遍历他那个月的考情记录，将员工信息和考勤明细表信息封装到List<ReturnCheckDetail>

	   3、返回的List<ReturnCheckDetail>方便页面easyui显示信息

	 * @param mapQuery
	 * @return
	 */
	public List<ReturnCheckDetail> getCheckDetailList(Map<String, Object> mapQuery){
		List<ReturnCheckDetail> returnList = new ArrayList<ReturnCheckDetail>();

		//1、查询所有CHECKCODE不为0l的用户，并且附带着部门信息
		List<SysUser> userList =  userService.selectByCheckcode0WithDept();
		//System.out.println(userList);

		List<CheckDetail> cdList;

		//2、遍历用户集合，
		for(SysUser u : userList) {

			//2.2根据其CHECKCODE和年月来查询每个用户当月的所有打卡记录
			mapQuery.put("checkCode", u.getCheckCode());//加入checkcode条件
			cdList = checkDetailService.selectByCheckCodeTime(mapQuery);
			System.out.println("输出"+u.getCheckCode()+"当月的考勤记录");

			CheckDetail cAm = null;//上午最早记录
			CheckDetail cPm = null;//下午最早记录

			for(int i=0 ;i<cdList.size();i++) {//遍历当前员工的这个月的考勤记录
				//System.out.println("考情记录："+cdList.get(i));
				//System.out.println("当前小时数为："+cdList.get(i).getTimeCheck().getHours());


				if(i==0){//表示这是第一条记录

					//判断当前是上午还是下午
					if(cdList.get(i).getTimeCheck().getHours() < 12) {//上午
						cAm = cdList.get(i);
					}else {//下午
						cPm = cdList.get(i);
					}

				}else {//表示这是第二条以上的记录


					//							System.out.println("当前小时为："+cdList.get(i).getTimeCheck().getHours());
					//2.2.1判断和上一条记录是否是同一天
					if(cdList.get(i).getTimeCheck().getDate() == cdList.get(i-1).getTimeCheck().getDate()) {//是同一天

						//								System.out.println("同一天处理");

						//2.2.2判断当前是上午还是下午
						if(cdList.get(i).getTimeCheck().getHours() < 12) {//上午

							//2.2.3 判断是当前的值更小，还是上一条记录的上午值更小，我们取最小的一条放到cAm中
							if(cAm == null) {
								cAm = cdList.get(i);
							}else {//cAmp中有值
								if(cdList.get(i).getTimeCheck().getTime() < cAm.getTimeCheck().getTime()) {
									cAm = cdList.get(i);
								}else {

								}
							}


						}else {//下午
							if(cPm == null) {
								cPm = cdList.get(i);
							}else {//cAmp中有值
								if(cdList.get(i).getTimeCheck().getTime() < cPm.getTimeCheck().getTime()) {
									cPm = cdList.get(i);
								}else {

								}
							}
						}

					}else {//不是同一天

						//System.out.println("不同天处理");

						//2.2.4判断cAmp	cPm不为空就加入到returnList中
						if(cAm!=null) {
							ReturnCheckDetail rcd = new ReturnCheckDetail();
							rcd.setUserid(u.getUserid());//员工id
							rcd.setDname(u.getSysDept().getDname());//部门名字
							rcd.setRealname(u.getRealname());//员工名字
							rcd.setTimeCheck(MyDateFormatUtil.getString(cAm.getTimeCheck()));//按照格式设置时间
							rcd.setCheckDetail(cAm);
							rcd.setMoneyBase(u.getSalaryBase());
							rcd.setCreateTime(u.getCreateTime());
							returnList.add(rcd);

							cAm = null;
							cAm = cdList.get(i);
						}

						if(cPm!=null) {
							ReturnCheckDetail rcd = new ReturnCheckDetail();
							rcd.setUserid(u.getUserid());//员工id
							rcd.setDname(u.getSysDept().getDname());//部门名字
							rcd.setRealname(u.getRealname());//员工名字
							rcd.setTimeCheck(MyDateFormatUtil.getString(cPm.getTimeCheck()));//按照格式设置时间
							rcd.setCheckDetail(cPm);
							rcd.setMoneyBase(u.getSalaryBase());
							rcd.setCreateTime(u.getCreateTime());
							returnList.add(rcd);

							cPm = null;
							cAm = cdList.get(i);
						}


					}



				}

				if(i==cdList.size()-1) {//最后一个天有的话直接保存
					//2.2.4判断cAmp	cPm不为空就加入到returnList中
					if(cAm!=null) {
						ReturnCheckDetail rcd = new ReturnCheckDetail();
						rcd.setUserid(u.getUserid());//员工id
						rcd.setDname(u.getSysDept().getDname());//部门名字
						rcd.setRealname(u.getRealname());//员工名字
						rcd.setTimeCheck(MyDateFormatUtil.getString(cAm.getTimeCheck()));//按照格式设置时间
						rcd.setCheckDetail(cAm);
						rcd.setMoneyBase(u.getSalaryBase());
						rcd.setCreateTime(u.getCreateTime());
						returnList.add(rcd);

						cAm = null;
						cAm = cdList.get(i);
					}

					if(cPm!=null) {
						ReturnCheckDetail rcd = new ReturnCheckDetail();
						rcd.setUserid(u.getUserid());//员工id
						rcd.setDname(u.getSysDept().getDname());//部门名字
						rcd.setRealname(u.getRealname());//员工名字
						rcd.setTimeCheck(MyDateFormatUtil.getString(cPm.getTimeCheck()));//按照格式设置时间
						rcd.setCheckDetail(cPm);
						rcd.setMoneyBase(u.getSalaryBase());
						rcd.setCreateTime(u.getCreateTime());
						returnList.add(rcd);

						cPm = null;
						cAm = cdList.get(i);
					}
				}


			}

			//break;
		}
		return returnList;
	}


	/**
	 * 上传考勤明细表
	 */
	@RequestMapping("/cdcFileUpload")
	@ResponseBody
	public Msg cdcFileUpload(@RequestParam("file") MultipartFile file) {

		boolean f = true;
		try {
			//文件原始名字
			String oname = file.getOriginalFilename();
			System.out.println("文件名字："+oname);

			//获取文件输入流
			//			file.getInputStream();
			//			
			//			// 输出文档路径及名称  (可以用来保存这个文件)
			//	        String outUrl = DocumentHandler.class.getClassLoader().getResource("").getPath()+"CheckDeatil/";
			//	        File outFile = new File(outUrl);
			//	        if(!outFile.exists()) {//查看目录是否存在，不存在就创建
			//	        	outFile.mkdirs();
			//	        }
			//			
			//			//这里将上传得到的文件保存至 d:\\temp\\file 目录
			//			FileUtils.copyInputStreamToFile(file.getInputStream(), new File(outUrl, 
			//					System.currentTimeMillis()+ file.getOriginalFilename()));
			//			
			//			System.out.println("上传成功，路径为："+outUrl);

			/*
			 * 读取Excel中的数据，并且保存到数据库中
			 */
			List<String []> s = ImportExcelUtil.readExcel(file);
			/**
			 * 数据格式如下
			 *  [考勤原始数据, , , , , ]
				[部门, 姓名, 登记号码, 日期时间, 机器号, 比对方式]
				[设计部, 老李, 4140, 2015-12-01 上午 07:59, 4, 指纹]
				[设计部, 老李, 4140, 2015-12-02 下午 05:38, 4, 指纹]
				[设计部, 老李, 4140, 2015-12-03 上午 07:20, 4, 指纹]
			 */
			List<CheckDetail> cdList = new ArrayList<CheckDetail>();
			for(int i=0;i<s.size();i++) {//将一行一行的对象封装到cdlist集合中
				//System.out.println(Arrays.asList(s.get(i)));

				//由于第一行是标题，第二行是列名，数据从第三行开始，跳过前两行
				if(i<2) {
					continue;
				}

				CheckDetail cd = new CheckDetail();

				//根据登记号码，查询那个员工的信息，获取到的部门，姓名
				Map<String, Object> mapQuery = new HashMap<String, Object>();
				mapQuery.put("CHECKCODE", Long.parseLong(s.get(i)[2]));
				SysUser user = userService.selectByCheckcode(mapQuery);

				cd.setDeptId(user.getDid());//部门id
				cd.setUserId(user.getUserid());//userid

				//封装其他字段
				cd.setCheckCode(Long.parseLong(s.get(i)[2]));//登记号码
				cd.setTimeCheck(MyDateFormatUtil.getDate(s.get(i)[3]));//日期时间(这里调用了我们自己写的时间格式转换工具类)
				cd.setCheckNum(Long.parseLong(s.get(i)[4]));
				cd.setCheckType(s.get(i)[5]);

				cdList.add(cd);
			}

			for(int i=0; i<cdList.size();i++) {//批量保存之前，先查询每一条记录是否重复，重复则移除


				long l = checkDetailService.selectByCheckCodeTimeCheck(cdList.get(i).getCheckCode(),cdList.get(i).getTimeCheck());
				if(l>0) {//有重复记录
					cdList.remove(i);
					i--;//移除list中的元素后，下标减一，下一次加一的时候才会是应该的下一条s
				}


				//System.out.println(c);
			}

			if(cdList.size()>0) {
				//开始批量保存
				checkDetailService.insertPitch(cdList);
			}



		} catch (Exception e) {
			e.printStackTrace();
			f=false;
		}

		if(f==true) {
			return Msg.success();
		}else {
			return Msg.fail().add("excelMessage", "excel中格式非约定格式");
		}
	}

	/**
	 * 跳转到考勤报表主页
	 */
	@RequestMapping("/goheckDetail")
	public String goheckDetail() {
		return "/checkDateOption/CheckDetail";
	}

	
}
