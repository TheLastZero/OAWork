package com.myhopu.controller;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.JsonObject;
import com.myhopu.entity.CheckDateOption;
import com.myhopu.entity.CheckDetail;
import com.myhopu.entity.Leave;
import com.myhopu.entity.ReturnCheckDetail;
import com.myhopu.entity.ReturnLeavePersonal;
import com.myhopu.entity.SysUser;
import com.myhopu.service.CheckDateOptionService;
import com.myhopu.service.CheckDetailService;
import com.myhopu.service.LeaveService;
import com.myhopu.service.SysUserService;
import com.myhopu.utils.HttpRequestUtil;
import com.myhopu.utils.Msg;
import com.myhopu.utils.MyCalender;
import com.myhopu.utils.MyDateFormatUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@RequestMapping("/checkDateOption")
@Controller
public class CheckDateOptionController {
	
	@Autowired
	private CheckDateOptionService checkDateOptionService;
	
	@Autowired
	private CheckDetailService checkDetailService;
	
	@Autowired
	private SysUserService userService;
	
	@Autowired
	private LeaveService leaveService;
	
	/**
	 * 返回个人的考勤信息，页面上显示日历格式
	 * 
	 * 
	 */
	@RequestMapping("/getCheckDatePersonal")
	@ResponseBody
	public Map<String, Object> getCheckDatePersonal(Integer year,Integer mounth,Long userid) {
		
		System.out.println("获取到的年月为:"+year+"-"+mounth);
		System.out.println("用户的id为："+userid);
		
		
		List<CheckDateOption> cdoList=null;
		
		//根据年月，和用户id，返回考勤月报表List
		List<ReturnCheckDetail> returnDetailList = getMounthDetailList(year, mounth,userid);
		
		//
//		for(ReturnCheckDetail r:returnDetailList) {
//			System.out.println("获取："+r);
//		}
		
		
		//3、解析考勤表list集合，封装为map返回
		//返回map
		Map<String, Object> map = new HashMap<String, Object>();
		List<Object> returnList =new ArrayList<Object>();
		
		Map<String, Object> mapWeek = new HashMap<String, Object>();
		//根据每一天是星期几，来存放考勤记录到map中
		int i=1;//用于判断当前日期天数
		for(int j=0;j<returnDetailList.size(); j++){
			ReturnCheckDetail cdo2 = returnDetailList.get(j);
			
			ReturnLeavePersonal rl = new ReturnLeavePersonal();
			rl.setDaynum(i);
			rl.setAm(returnDetailList.get(j));
			if(j == returnDetailList.size()-1) {//最后一个
				rl.setPm(returnDetailList.get(j));
			}else {
				rl.setPm(returnDetailList.get(j+1));
				j++;
			}
			
			
//			System.out.println("现在是："+cdo2);
			Date d3 = MyDateFormatUtil.getDate2(cdo2.getTimeShould());
			//当前为星期几
			int w = MyCalender.getWeekByymd(d3.getYear()+1900, d3.getMonth()+1, d3.getDate());

			if(w==7) {//星期天
				mapWeek.put("sun", rl);
				
				//每当走了一个星期天或者到最后一条，就封装一个row对象数据
				Map<String, Object> map2 = new HashMap<String, Object>();
				map2.put("row", mapWeek);
				
				returnList.add(map2);
				
				mapWeek = new HashMap<String, Object>();
				
			}else if(w==1) {//星期一
				mapWeek.put("mon", rl);
				
			}else if(w==2) {//星期2
				mapWeek.put("tue", rl);
				
			}else if(w==3) {//星期3
				mapWeek.put("wed", rl);
				
			}else if(w==4) {//星期4
				mapWeek.put("thurs", rl);
				
			}else if(w==5) {//星期5
				mapWeek.put("fri", rl);
				
			}else if(w==6) {//星期6
				mapWeek.put("sat", rl);
			}
			
			if(i==MyCalender.getDaysByYearMounth(year, mounth)) {
				//每当走到最后一条，就封装一个row对象数据
				Map<String, Object> map2 = new HashMap<String, Object>();
				map2.put("row", mapWeek);
				
				returnList.add(map2);
				mapWeek = new HashMap<String, Object>();
			}
			
			i++;
		}
		

		map.put("rows", returnList);
		
		return map;
	}

	/**
	 * 根据年月，返回考勤月报表List
	 * @param year
	 * @param mounth
	 * @return
	 */
	private List<ReturnCheckDetail> getMounthDetailList(Integer year, Integer mounth,Long userid) {
		//1、先查询数据库中是否存在当年当月的考勤设置，如果有我们获取list集合
		//查询条件map
		Map<String, Object> mapQuery2 = new HashMap<String, Object>();
		mapQuery2.put("time", year+"-"+mounth);

		List<CheckDateOption> cdoList = checkDateOptionService.selectByYearMounth(mapQuery2);

		//查询条件map
		Map<String, Object> mapQuery = new HashMap<String, Object>();
		mapQuery.put("time", year+"/"+mounth);
		mapQuery.put("userid", userid);
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
	 * 根据userid获取指定年月的考情记录
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
		List<SysUser> userList = userService.findOne(mapQuery);
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
	 * saveOrUpdate
	 */
	@RequestMapping("/saveOrUpdate")
	@ResponseBody
	public Msg saveOrUpdate(@RequestBody Object json) {
//		System.out.println(json);
		
		
		/*
		 * 1、取得要保存或者更新的数据
		 */
		JSONArray array = JSONArray.fromObject(json);
		List<Object> rowList = JSONArray.toList(array);//每一行的数据 {row=
		
		//System.out.println("所有行的数据"+rowList.get(0));//
		//System.out.println(array.getJSONObject(0).get("row"));//{"thurs":{"settingId":0,"se...
		
		List<CheckDateOption> cdoList = new ArrayList<CheckDateOption>();
		for(Object row: rowList) {
			JSONObject row2 = JSONObject.fromObject(row);
			Object week = row2.get("row");
			
			JSONObject row3 = JSONObject.fromObject(week);
			//System.out.println(row3.get("thurs"));
			
			Map<String, Object> map =row3; 
		    for (Entry<String, Object> entry : map.entrySet()) { //键是星期的英文，值是checkDateOption对象
		      //System.out.println(entry.getKey()+"="+entry.getValue());
		    	Object o =  entry.getValue();
		    	JSONObject j = JSONObject.fromObject(o);
		    	CheckDateOption cdo = (CheckDateOption) JSONObject.toBean(j, CheckDateOption.class);
		    	
		    	/*
		    	 * 因为JSONObject转时间字符串的时候会出现问题，
		    	 * 所以时间这个字段我们自己手动转，并且设置格式
		    	 */
		    	Long date = (Long) j.get("settingDate");
		    	Date d = new Date();
		    	d.setTime(date);
		    	
		    	//设置时间为早上八点
		    	DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd"); 
				Date dStart = null;
				try {
					dStart = format1.parse((d.getYear()+1900)+"-"+(d.getMonth()+1)+"-"+d.getDate());
					dStart.setTime(dStart.getTime()+1000 * 60 * 60 * 8);
				} catch (Exception e) {
					e.printStackTrace();
				}
		    	
		    	cdo.setSettingDate(dStart);
		    	
		    	//添加到集合中
		    	cdoList.add(cdo);
		    }   

		}
		
		System.out.println("得到的结果为：");
		for(CheckDateOption c: cdoList) {
			System.out.println("得到的结果为"+c);
		}
		
		/*
		 * 2、根据第一条数据是否有id，就可以判断是保存还是更新
		 */
		long l=0;
		if(cdoList.get(0).getSettingId()==null) {//保存
			l = checkDateOptionService.addPitch(cdoList);
		}else {//更新，只需要更新am，pm字段即可
			l = checkDateOptionService.updatePitch(cdoList);
		}

		if(l>0) {
			return Msg.success();
		}else {
			return Msg.fail();
		}
		
		
	}
	
	/**
	 * checkDateOptionList
	 *  根据传入的年份月份		
	 *  查询考勤日历，如果存在当月的考勤设置数据，我们就返回当月的设置数据
	 *  否则，我们直接创建新的考情设置
	 *  
	 */
	@RequestMapping("/getCheckDateOptionList")
	@ResponseBody
	public Map<String, Object> getcheckDateOptionList(Integer year,Integer mounth) {
		
		List<CheckDateOption> cdoList=null;
		
		//1、先查询数据库中是否存在当年当月的考勤设置，如果有我们获取list集合
		//查询条件map
		Map<String, Object> mapQuery = new HashMap<String, Object>();
		mapQuery.put("time", year+"-"+mounth);
		cdoList = checkDateOptionService.selectByYearMounth(mapQuery);
		
		if(cdoList.size()==0) {
			
			System.out.println("查询结果为0，开始初始化当月考勤日历");
			
			//2、如果数据库中没有当年当月的考勤设置，我们就先自己初始化考勤日历信息
			cdoList =new ArrayList<CheckDateOption>();
			Map<String, Object> cdoMap = new HashMap<String, Object>();
			//遍历月份天数
			for(int i=1;i<=MyCalender.getDaysByYearMounth(year, mounth);i++) {
				CheckDateOption cdo = new CheckDateOption();
				Date d = new Date(2018,5,5);
			
				GregorianCalendar gc = new GregorianCalendar();
				gc.set(year, mounth-1, i);
				d.setTime(gc.getTimeInMillis());
				
				cdo.setSettingDate(d);//设置日期
				//System.out.println(cdo.getSettingDate().toLocaleString());
				
				//根据是否为工作日判断上下班的状态
				int week = MyCalender.getWeekByymd(year, mounth, i);
				if(week==6 || week==7) {//周末
					cdo.setAmStatus((short)1);
					cdo.setPmStatus((short)1);
					
					cdo.setIsHoliday((short)1);
				}else {//默认周一到周五是工作日
					cdo.setAmStatus((short)0);
					cdo.setPmStatus((short)0);
					
					cdo.setIsHoliday((short)0);
				}
				
				//判断节假日信息
				JsonObject res = null;   
		        res = HttpRequestUtil.getXpath("http://lanfly.vicp.io/api/holiday/info/"+year+"-"+mounth+"-"+i);
		        //判断这个holiday的值是否为空
		        if(res.get("holiday").isJsonNull()) {//非节假日
		        	//cdo.setMessage("");
		        }else {//节假日
		        	JsonObject j = (JsonObject) res.get("holiday");
		        	cdo.setMessage(j.get("name").toString());//设置假日名字
		        	
		        	cdo.setAmStatus((short)1);
					cdo.setPmStatus((short)1);
					cdo.setIsHoliday((short)2);
		        }
				
		        
				cdo.setWeek((short)week);
				cdo.setDaynum(i+0l);
				
				
				cdoList.add(cdo);//添加到list集合中
			}
		}
		
		
		
		
		//3、解析考勤表list集合，封装为map返回
		//返回map
		Map<String, Object> map = new HashMap<String, Object>();
		List<Object> returnList =new ArrayList<Object>();
		
		Map<String, Object> mapWeek = new HashMap<String, Object>();
		//根据每一天是星期几，来存放考勤记录到map中
		int i=1;//用于判断当前日期天数
		for(CheckDateOption cdo2: cdoList){
//			System.out.println("现在是："+cdo2);
			int w = cdo2.getWeek();

			if(w==7) {//星期天
				mapWeek.put("sun", cdo2);
				
				//每当走了一个星期天或者到最后一条，就封装一个row对象数据
				Map<String, Object> map2 = new HashMap<String, Object>();
				map2.put("row", mapWeek);
				
				returnList.add(map2);
				
				mapWeek = new HashMap<String, Object>();
				
			}else if(w==1) {//星期一
				mapWeek.put("mon", cdo2);
				
			}else if(w==2) {//星期2
				mapWeek.put("tue", cdo2);
				
			}else if(w==3) {//星期3
				mapWeek.put("wed", cdo2);
				
			}else if(w==4) {//星期4
				mapWeek.put("thurs", cdo2);
				
			}else if(w==5) {//星期5
				mapWeek.put("fri", cdo2);
				
			}else if(w==6) {//星期6
				mapWeek.put("sat", cdo2);
			}
			
			if(i==MyCalender.getDaysByYearMounth(year, mounth)) {
				//每当走到最后一条，就封装一个row对象数据
				Map<String, Object> map2 = new HashMap<String, Object>();
				map2.put("row", mapWeek);
				
				returnList.add(map2);
				mapWeek = new HashMap<String, Object>();
			}
			
			i++;
		}
		

		map.put("rows", returnList);
		
		return map;
	}
	
	
	/**
	 * 跳转到考勤页面
	 * @return
	 */
	@RequestMapping("/checkDateOptionList")
	public String checkDateOptionList() {
		return "/checkDateOption/CheckDateOption";
	}
	
	/**
	 * 跳转到个人的考勤页面
	 * 
	 */
	@RequestMapping("/goCheckDatePersonal")
	public String goCheckDatePersonal() {
		
		return "/checkDateOption/CheckDatePersonal";
	}
	
}
