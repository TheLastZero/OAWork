package com.myhopu.controller;

import java.text.SimpleDateFormat;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.myhopu.entity.PmsTaskItem;
import com.myhopu.entity.SysUser;
import com.myhopu.service.PmsTaskItemService;
import com.myhopu.utils.JsonUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@Scope("prototype")
public class PmsTaskItemController {

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

	@Resource
	PmsTaskItemService pmsTaskItemService;

	/*
	 * 增
	 */
	@RequestMapping("/TaskItemAdd.do")
	public void add(PmsTaskItem pmsTaskItem, HttpServletResponse response) {
		JSONObject result = new JSONObject();
		try {
			pmsTaskItemService.add(pmsTaskItem);
			result.put("success", true);
			result.put("msg", "操作成功！");
		} catch (Exception e) {
			e.printStackTrace();
			result.put("success", false);
			result.put("msg", "操作失败！<br/>异常消息：" + e.getMessage());
		}
		JsonUtil.outJsonString(response, result.toString());
	}

	/*
	 * 删，批量
	 */
	@RequestMapping("/TaskItemDel.do")
	public void del(String ids, HttpServletResponse response) {
		JSONObject result = new JSONObject();
		try {
			pmsTaskItemService.del(ids);
			result.put("success", true);
			result.put("msg", "操作成功！");
		} catch (Exception e) {
			e.printStackTrace();
			result.put("success", false);
			result.put("msg", "操作失败！<br/>异常消息：" + e.getMessage());
		}
		JsonUtil.outJsonString(response, result.toString());
	}

	/*
	 * 改
	 */
	@RequestMapping("/TaskItemUpd.do")
	public void upd(PmsTaskItem pmsTaskItem, HttpServletResponse response) {
		JSONObject result = new JSONObject();
		try {
			pmsTaskItemService.upd(pmsTaskItem);
			result.put("success", true);
			result.put("msg", "操作成功！");
		} catch (Exception e) {
			e.printStackTrace();
			result.put("success", false);
			result.put("msg", "操作失败！<br/>异常消息：" + e.getMessage());
		}
		JsonUtil.outJsonString(response, result.toString());
	}

	/*
	 * 查
	 */
	@RequestMapping("/TaskItemList.do")
	public String list() {
		return "TaskList";
	}

	/*
	 * 查所有，用于数据网格，json
	 */
	@RequestMapping("/TaskItemSelect2.do")
	public void select2(@RequestParam(value = "tid", defaultValue = "-1") long tid, HttpServletResponse response) {
		List list = pmsTaskItemService.findAll(tid);

		JSONArray jsonArray = new JSONArray();
		for (int i = 0; i < list.size(); i++) {
			PmsTaskItem item = (PmsTaskItem) list.get(i);
			JSONObject obj = new JSONObject();
			obj.put("iid", item.getIid());
			obj.put("tid", item.getPmsTask().getTid());
			obj.put("iname", item.getIname());
			obj.put("irequirement", item.getIrequirement());
			obj.put("iscore", item.getIscore());
			obj.put("iscore2", item.getIscore2());
			obj.put("isortkey", item.getIsortkey());
			jsonArray.add(obj);
		}
		JsonUtil.outJsonString(response, jsonArray.toString());
	}

	/*
	 * 查
	 */
	@RequestMapping("/MyTaskList.do")
	public String list2() {
		return "MyTaskList";
	}

	/*
	 * 查所有，我的任务，用于数据网格，json
	 */
	@RequestMapping("/MyTaskItemSelect2.do")
	public void select2My(HttpServletRequest request, HttpServletResponse response) {
		SysUser sysUser = (SysUser) request.getSession().getAttribute("sysUser");

		List list = pmsTaskItemService.findAllMy(sysUser.getUserid());

		JSONArray jsonArray = new JSONArray();
		for (int i = 0; i < list.size(); i++) {
			PmsTaskItem item = (PmsTaskItem) list.get(i);
			JSONObject obj = new JSONObject();

			obj.put("pname", item.getPmsTask().getPmsProject().getPname());
			obj.put("tname", item.getPmsTask().getTname());
			obj.put("tdate1", item.getPmsTask().getTdate1() == null ? "" : sdf.format(item.getPmsTask().getTdate1()));
			obj.put("tworkday", item.getPmsTask().getTworkday());
			obj.put("tremark", item.getPmsTask().getTremark());
			obj.put("tsortkey", item.getPmsTask().getTsortkey());

			obj.put("iid", item.getIid());
			obj.put("tid", item.getPmsTask().getTid());
			obj.put("iname", item.getIname());
			obj.put("irequirement", item.getIrequirement());
			obj.put("iscore", item.getIscore());
			obj.put("iscore2", item.getIscore2());
			obj.put("isortkey", item.getIsortkey());
			jsonArray.add(obj);
		}
		JsonUtil.outJsonString(response, jsonArray.toString());
	}

}
