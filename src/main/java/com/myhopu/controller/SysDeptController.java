package com.myhopu.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.myhopu.entity.SysDept;
import com.myhopu.service.SysDeptService;
import com.myhopu.utils.JsonUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@Scope("prototype")
public class SysDeptController {
	@Resource
	SysDeptService deptService;

	/*
	 * 增
	 */
	@RequestMapping("/DeptAdd.do")
	public void add(SysDept sysDept, HttpServletResponse response) {
		JSONObject result = new JSONObject();
		try {
			deptService.add(sysDept);
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
	@RequestMapping("/DeptDel.do")
	public void del(String ids, HttpServletResponse response) {
		JSONObject result = new JSONObject();
		try {
			deptService.del(ids);
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
	@RequestMapping("/DeptUpd.do")
	public void upd(SysDept sysDept, HttpServletResponse response) {
		JSONObject result = new JSONObject();
		try {
			deptService.upd(sysDept);
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
	@RequestMapping("/DeptList.do")
	public String list() {
		return "DeptList";
	}

	/*
	 * 查所有，用于树，json
	 */
	@RequestMapping("/DeptSelect.do")
	public void select(HttpServletResponse response) {
		List list = deptService.findAll();

		JSONArray jsonArray = new JSONArray();
		for (int i = 0; i < list.size(); i++) {
			SysDept dept = (SysDept) list.get(i);
			JSONObject obj = new JSONObject();
			obj.put("id", dept.getDid());
			obj.put("parentId", dept.getDpid());
			obj.put("name", dept.getDname());
			jsonArray.add(obj);
		}
		JsonUtil.outJsonString(response, jsonArray.toString());
	}

	/*
	 * 查所有，用于数据网格，json
	 */
	@RequestMapping("/DeptSelect2.do")
	public void select2(HttpServletResponse response) {
		List list = deptService.findAll();
		JSONArray jsonArray = JSONArray.fromObject(list);
		JsonUtil.outJsonString(response, jsonArray.toString());
	}
}
