package com.myhopu.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.myhopu.entity.PmsProject;
import com.myhopu.service.PmsProjectService;
import com.myhopu.utils.JsonUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@Scope("prototype")
public class PmsProjectController {
	@Resource
	PmsProjectService projectService;

	/*
	 * 增
	 */
	@RequestMapping("/ProjectAdd.do")
	public void add(PmsProject sysProject, HttpServletResponse response) {
		JSONObject result = new JSONObject();
		try {
			projectService.add(sysProject);
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
	@RequestMapping("/ProjectDel.do")
	public void del(String ids, HttpServletResponse response) {
		JSONObject result = new JSONObject();
		try {
			projectService.del(ids);
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
	@RequestMapping("/ProjectUpd.do")
	public void upd(PmsProject sysProject, HttpServletResponse response) {
		JSONObject result = new JSONObject();
		try {
			projectService.upd(sysProject);
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
	@RequestMapping("/ProjectList.do")
	public String list() {
		return "ProjectList";
	}
	
	/*
	 * 查所有，用于树，json
	 */
	@RequestMapping("/ProjectSelect.do")
	public void select(HttpServletResponse response) {
		List list = projectService.findAll();

		JSONArray jsonArray = new JSONArray();
		for (int i = 0; i < list.size(); i++) {
			PmsProject project = (PmsProject) list.get(i);
			JSONObject obj = new JSONObject();
			obj.put("id", project.getPid());
			obj.put("parentId", project.getSysDept().getDid());
			obj.put("name", project.getPname());
			jsonArray.add(obj);
		}
		JsonUtil.outJsonString(response, jsonArray.toString());
	}

	/*
	 * 查所有，用于数据网格，json
	 */
	@RequestMapping("/ProjectSelect2.do")
	public void select2(HttpServletResponse response) {
		List list = projectService.findAll();

		JSONArray jsonArray = new JSONArray();
		for (int i = 0; i < list.size(); i++) {
			PmsProject project = (PmsProject) list.get(i);
			JSONObject obj = new JSONObject();
			obj.put("pid", project.getPid());
			obj.put("dname", project.getSysDept().getDname());
			obj.put("pname", project.getPname());
			obj.put("premark", project.getPremark());
			jsonArray.add(obj);
		}
		JsonUtil.outJsonString(response, jsonArray.toString());
	}
}
