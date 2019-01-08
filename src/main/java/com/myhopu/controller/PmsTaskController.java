package com.myhopu.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.myhopu.entity.PmsTask;
import com.myhopu.service.PmsTaskService;
import com.myhopu.utils.JsonUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@Scope("prototype")
public class PmsTaskController {
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

	@Resource
	PmsTaskService pmsTaskService;

	/*
	 * 增
	 */
	@RequestMapping("/TaskAdd.do")
	public void add(PmsTask pmsTask, HttpServletResponse response) {
		JSONObject result = new JSONObject();
		try {
			pmsTaskService.add(pmsTask);
			result.put("success", true);
			result.put("msg", "操作成功！");
		}catch(Exception e) {
			e.printStackTrace();
			result.put("success", false);
			result.put("msg", "操作失败！<br/>异常消息：" + e.getMessage());
		}
		JsonUtil.outJsonString(response, result.toString());
	}

	/*
	 * 删，批量
	 */
	@RequestMapping("/TaskDel.do")
	public void del(String ids, HttpServletResponse response) {
		JSONObject result = new JSONObject();
		try {
			pmsTaskService.del(ids);
			result.put("success", true);
			result.put("msg", "操作成功！");
		}catch(Exception e) {
			e.printStackTrace();
			result.put("success", false);
			result.put("msg", "操作失败！<br/>异常消息：" + e.getMessage());
		}
		JsonUtil.outJsonString(response, result.toString());
	}

	/*
	 * 改
	 */
	@RequestMapping("/TaskUpd.do")
	public void upd(PmsTask pmsTask, HttpServletResponse response) {
		JSONObject result = new JSONObject();
		try {
			pmsTaskService.upd(pmsTask);
			result.put("success", true);
			result.put("msg", "操作成功！");
		}catch(Exception e) {
			e.printStackTrace();
			result.put("success", false);
			result.put("msg", "操作失败！<br/>异常消息：" + e.getMessage());
		}
		JsonUtil.outJsonString(response, result.toString());
	}

	/*
	 * 查
	 */
	@RequestMapping("/TaskList.do")
	public String list() {
		return "TaskList";
	}
	
	/*
	 * 查
	 */
	@RequestMapping("/TaskControl.do")
	public String list2() {
		return "TaskControl";
	}
	
	/*
	 * 查所有，用于数据网格，json
	 */
	@RequestMapping("/TaskSelect2.do")
	public void select2(@RequestParam(value = "pid", defaultValue = "-1") long pid, HttpServletResponse response) {
		List list = pmsTaskService.findAll(pid);

		JSONArray jsonArray = new JSONArray();
		for (int i = 0; i < list.size(); i++) {
			PmsTask task = (PmsTask) list.get(i);
			JSONObject obj = new JSONObject();
			obj.put("tid", task.getTid());
			obj.put("pid", task.getPmsProject().getPid());
			obj.put("pname", task.getPmsProject().getPname());
			obj.put("tname", task.getTname());
			obj.put("userid", task.getSysUser().getUserid());
			obj.put("realname", task.getSysUser().getRealname());
			obj.put("tdate1", task.getTdate1() == null ? "" : sdf.format(task.getTdate1()));
			obj.put("tworkday", task.getTworkday());
			obj.put("tdate2", task.getTdate2() == null ? "" : sdf.format(task.getTdate2()));
			obj.put("tresponse", task.getTresponse());
			obj.put("tremark", task.getTremark());
			obj.put("tsortkey", task.getTsortkey());
			jsonArray.add(obj);
		}
		JsonUtil.outJsonString(response, jsonArray.toString());
	}
	
	/*
	 * 时间属性编辑器
	 */
	@InitBinder
	public void initBinder(ServletRequestDataBinder bin) {
		CustomDateEditor cust = new CustomDateEditor(sdf, true);
		bin.registerCustomEditor(Date.class, cust);
	}

}
