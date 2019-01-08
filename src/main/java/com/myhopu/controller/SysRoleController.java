package com.myhopu.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.myhopu.entity.SysRole;
import com.myhopu.service.SysRoleService;
import com.myhopu.utils.JsonUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@Scope("prototype")
public class SysRoleController {

	@Resource
	SysRoleService roleService;

	/*
	 * 增
	 */
	@RequestMapping("/RoleAdd.do")
	public void add(SysRole sysRole, HttpServletResponse response) {
		JSONObject result = new JSONObject();
		try {
			roleService.add(sysRole);
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
	@RequestMapping("/RoleDel.do")
	public void del(String ids, HttpServletResponse response) {
		JSONObject result = new JSONObject();
		try {
			roleService.del(ids);
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
	@RequestMapping("/RoleUpd.do")
	public void upd(SysRole sysRole, HttpServletResponse response) {
		JSONObject result = new JSONObject();
		try {
			roleService.upd(sysRole);
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
	@RequestMapping("/RoleList.do")
	public String list() {
		return "RoleList";
	}

	/*
	 * 查所有，用于数据网格，json
	 */
	@RequestMapping("/RoleSelect2.do")
	public void select2(@RequestParam(value = "roleid", defaultValue = "-1") long roleid, HttpServletResponse response) {
		List list = roleService.findAll(roleid);
		JSONArray jsonArray = JSONArray.fromObject(list);
		JsonUtil.outJsonString(response, jsonArray.toString());
	}
	
	/*
	 * 查所有，未关联指定角色的部门用户，json
	 */
	@RequestMapping("/RoleDeptUserSelect.do")
	public void select3(long roleid, HttpServletResponse response) {
		List deptuser = roleService.getDeptUser(roleid);
		JSONArray jsonArray = new JSONArray();
		for (int i = 0; i < deptuser.size(); i++) {
			Map map = (HashMap) deptuser.get(i);
			JSONObject obj = new JSONObject();
			obj.put("id", map.get("ID"));
			obj.put("parentId", map.get("PARENTID"));
			obj.put("name", map.get("NAME"));
			jsonArray.add(obj);
		}
		JsonUtil.outJsonString(response, jsonArray.toString());
	}

	/*
	 * 关联角色与用户
	 */
	@RequestMapping("/RoleUserAdd.do")
	public void add2(long roleid, String userids, HttpServletResponse response) {
		JSONObject result = new JSONObject();
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("roleid", roleid);
			if (userids.endsWith(",")) {
				userids = userids.substring(0, userids.length() - 1);
			}
			params.put("userids", userids.split(","));
			
			roleService.add2(params);
			
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
	 * 查所有，角色关联的用户，用于数据网格，json
	 */
	@RequestMapping("/RoleUserSelect2.do")
	public void select4(long roleid, HttpServletResponse response) {
		List list = roleService.findAll2(roleid);

		JSONArray jsonArray = new JSONArray();
		for (int i = 0; i < list.size(); i++) {
			Map map = (HashMap) list.get(i);
			JSONObject obj = new JSONObject();
			obj.put("ruid", map.get("RUID"));
			obj.put("userid", map.get("USERID"));
			obj.put("did", map.get("DID"));
			obj.put("dname", map.get("DNAME"));
			obj.put("username", map.get("USERNAME"));
			obj.put("userpass", map.get("USERPASS"));
			obj.put("realname", map.get("REALNAME"));
			jsonArray.add(obj);
		}
		JsonUtil.outJsonString(response, jsonArray.toString());
	}
	
	/*
	 * 删，批量，解除角色所关联的用户
	 */
	@RequestMapping("/RoleUserDel.do")
	public void del2(String ruids, HttpServletResponse response) {
		JSONObject result = new JSONObject();
		try {
			roleService.del2(ruids);
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
	@RequestMapping("/PrivList.do")
	public String list2() {
		return "PrivList";
	}
	
	/*
	 * 查所有，未关联指定角色的模块，json
	 */
	@RequestMapping("/RoleModuleSelect.do")
	public void select5(long roleid, HttpServletResponse response) {
		List deptuser = roleService.getModule(roleid);
		JSONArray jsonArray = new JSONArray();
		for (int i = 0; i < deptuser.size(); i++) {
			Map map = (HashMap) deptuser.get(i);
			JSONObject obj = new JSONObject();
			obj.put("id", map.get("ID"));
			obj.put("parentId", map.get("PARENTID"));
			obj.put("name", map.get("NAME"));
			jsonArray.add(obj);
		}
		JsonUtil.outJsonString(response, jsonArray.toString());
	}

	/*
	 * 关联角色与模块
	 */
	@RequestMapping("/RoleModuleAdd.do")
	public void add3(long roleid, String mids, HttpServletResponse response) {
		JSONObject result = new JSONObject();
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("roleid", roleid);
			if (mids.endsWith(",")) {
				mids = mids.substring(0, mids.length() - 1);
			}
			params.put("mids", mids.split(","));
			roleService.add3(params);
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
	 * 查所有，角色关联的用户，用于数据网格，json
	 */
	@RequestMapping("/RoleModuleSelect2.do")
	public void select6(long roleid, HttpServletResponse response) {
		List list = roleService.findAll3(roleid);

		JSONArray jsonArray = new JSONArray();
		for (int i = 0; i < list.size(); i++) {
			Map map = (HashMap) list.get(i);
			JSONObject obj = new JSONObject();
			obj.put("rmid", map.get("RMID"));
			obj.put("mid", map.get("MID"));
			obj.put("mname", map.get("MNAME"));
			jsonArray.add(obj);
		}
		JsonUtil.outJsonString(response, jsonArray.toString());
	}
	
	/*
	 * 删，批量，解除角色所关联的用户
	 */
	@RequestMapping("/RoleModuleDel.do")
	public void del3(String rmids, HttpServletResponse response) {
		JSONObject result = new JSONObject();
		try {
			roleService.del3(rmids);
			result.put("success", true);
			result.put("msg", "操作成功！");
		} catch (Exception e) {
			e.printStackTrace();
			result.put("success", false);
			result.put("msg", "操作失败！<br/>异常消息：" + e.getMessage());
		}
		JsonUtil.outJsonString(response, result.toString());
	}

}
