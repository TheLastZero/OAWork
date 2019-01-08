package com.myhopu.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.myhopu.entity.SysUser;
import com.myhopu.service.SysUserService;
import com.myhopu.utils.JsonUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@Scope("prototype")
public class SysUserController {

	@Resource
	SysUserService userService;

	/*
	 * 增
	 */
	@RequestMapping("/UserAdd.do")
	public void add(SysUser sysUser, HttpServletResponse response) {
		System.out.println("获取到的用户信息为："+sysUser);
		JSONObject result = new JSONObject();
		try {
			userService.add(sysUser);
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
	@RequestMapping("/UserDel.do")
	public void del(String ids, HttpServletResponse response) {
		JSONObject result = new JSONObject();
		try {
			userService.del(ids);
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
	@RequestMapping("/UserUpd.do")
	public void upd(SysUser sysUser, HttpServletResponse response) {
		JSONObject result = new JSONObject();
		try {
			userService.upd(sysUser);
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
	@RequestMapping("/UserList.do")
	public String list() {
		return "UserList";
	}
	
	/*
	 * 查所有，用于下拉框，json
	 */
	@RequestMapping("/UserSelect.do")
	public void select(HttpSession session, HttpServletResponse response) {
		// 获取登录人所在部门的ID
		SysUser sysUser = (SysUser) session.getAttribute("sysUser");
		long did = sysUser.getSysDept().getDid();
		List list = userService.findAll(did);

		JSONArray jsonArray = new JSONArray();
		for (int i = 0; i < list.size(); i++) {
			SysUser user = (SysUser) list.get(i);
			JSONObject obj = new JSONObject();
			obj.put("id", user.getUserid());
			obj.put("name", user.getRealname());
			jsonArray.add(obj);
		}
		JsonUtil.outJsonString(response, jsonArray.toString());
	}
	
	/*
	 * 查所有，用于数据网格，json
	 */
	@RequestMapping("/UserSelect2.do")
	public void select2(@RequestParam(value = "did", defaultValue = "-1") long did, HttpServletResponse response) {
		List list = userService.findAll(did);

		JSONArray jsonArray = new JSONArray();
		for (int i = 0; i < list.size(); i++) {
			SysUser user = (SysUser) list.get(i);
			JSONObject obj = new JSONObject();
			obj.put("userid", user.getUserid());
			obj.put("did", user.getSysDept().getDid());
			obj.put("dname", user.getSysDept().getDname());
			obj.put("username", user.getUsername());
			obj.put("userpass", user.getUserpass());
			obj.put("realname", user.getRealname());
			jsonArray.add(obj);
		}
		JsonUtil.outJsonString(response, jsonArray.toString());
	}
	
	/*
	 * 登录
	 */
	@RequestMapping("/User_login.do")
	public String login(SysUser sysUser, HttpServletRequest request, HttpServletResponse response) {
		sysUser = userService.login(sysUser);

		if (sysUser != null && sysUser.getUserid() > 0) {
			// 将登录人信息放入session
			request.getSession().setAttribute("sysUser", sysUser);// 登录人的信息
			
			request.getSession().setAttribute("SESSION_USERNAME", sysUser.getUserid() + "");// 即时通讯用

			String rememberNamePass = request.getParameter("rememberNamePass");
			if (rememberNamePass != null) {
				// 将登录人信息放入cookie
				Cookie cookie = new Cookie("uname", sysUser.getUsername());// 创建Cookie
				cookie.setMaxAge(60 * 60 * 24 * 30);// 设置cookie命长为30天
				response.addCookie(cookie);// 保存cookie
				Cookie cookie2 = new Cookie("upass", sysUser.getUserpass());
				cookie2.setMaxAge(60 * 60 * 24 * 30);
				response.addCookie(cookie2);
			} else {
				Cookie cookie = new Cookie("uname", "");
				cookie.setMaxAge(0);
				response.addCookie(cookie);
				Cookie cookie2 = new Cookie("upass", "");
				cookie2.setMaxAge(0);
				response.addCookie(cookie2);
			}
			return "redirect:/index.do";
		} else {
			request.setAttribute("errorMsg", "用户登录失败，请检查输入！");
			return "login";
		}
	}
	
	/*
	 * 首页
	 */
	@RequestMapping("/index.do")
	public String index() {
		return "index";
	}
}
