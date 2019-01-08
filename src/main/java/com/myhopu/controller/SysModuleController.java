package com.myhopu.controller;

import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.myhopu.entity.SysModule;
import com.myhopu.entity.SysUser;
import com.myhopu.service.SysModuleService;
import com.myhopu.utils.JsonUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@Scope("prototype")
public class SysModuleController {

	@Resource
	SysModuleService moduleService;

	/*
	 * 增
	 */
	@RequestMapping("/ModuleAdd.do")
	public void add(SysModule sysModule, HttpServletResponse response) {
		JSONObject result = new JSONObject();
		try {
			moduleService.add(sysModule);
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
	 * 删
	 */
	@RequestMapping("/ModuleDel.do")
	public void del(long mid, HttpServletResponse response) {
		JSONObject result = new JSONObject();
		try {
			moduleService.del(mid);
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
	@RequestMapping("/ModuleUpd.do")
	public void upd(SysModule sysModule, HttpServletResponse response) {
		JSONObject result = new JSONObject();
		try {
			moduleService.upd(sysModule);
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
	@RequestMapping("/ModuleList.do")
	public String list() {
		return "ModuleList";
	}

	/*
	 * 查所有，当前用户的授权模块，用于树，json
	 */
	@RequestMapping("/MyModuleSelect.do")
	public void selectMy(HttpServletRequest request, HttpServletResponse response) {
		SysUser sysUser = (SysUser) request.getSession().getAttribute("sysUser");

		List list = moduleService.findAllMy(sysUser.getUserid());

		JSONArray jsonArray = new JSONArray();
		for (int i = 0; i < list.size(); i++) {
			SysModule module = (SysModule) list.get(i);
			JSONObject obj = new JSONObject();
			obj.put("id", module.getMid());
			obj.put("parentId", module.getMpid());
			obj.put("name", module.getMname());
			obj.put("url", module.getMpath());
			jsonArray.add(obj);
		}
		JsonUtil.outJsonString(response, jsonArray.toString());
	}
	
	/*
	 * 查所有，用于树，json
	 */
	@RequestMapping("/ModuleSelect.do")
	public void select(HttpServletRequest request, HttpServletResponse response) {
		List list = moduleService.findAll(-1);

		JSONArray jsonArray = new JSONArray();
		for (int i = 0; i < list.size(); i++) {
			SysModule module = (SysModule) list.get(i);
			JSONObject obj = new JSONObject();
			obj.put("id", module.getMid());
			obj.put("parentId", module.getMpid());
			obj.put("name", module.getMname());
			obj.put("url", module.getMpath());
			jsonArray.add(obj);
		}
		JsonUtil.outJsonString(response, jsonArray.toString());
	}

	/*
	 * 查所有，用于数据网格，json
	 */
	@RequestMapping("/ModuleSelect2.do")
	public void select2(@RequestParam(value = "mpid", defaultValue = "-1") long mpid, HttpServletResponse response) {
		List list = moduleService.findAll(mpid);
		
		JSONArray jsonArray = JSONArray.fromObject(list);
		JsonUtil.outJsonString(response, jsonArray.toString());
	}

}
