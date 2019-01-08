package com.myhopu.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.socket.TextMessage;

import com.myhopu.entity.SysUser;
import com.myhopu.service.PmsChatService;
import com.myhopu.utils.JsonUtil;
import com.myhopu.websocket.SpringWebSocketHandler;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@Scope("prototype")
public class PmsChatController {
	@Bean // 这个注解会从Spring容器拿出Bean
	public SpringWebSocketHandler infoHandler() {
		return new SpringWebSocketHandler();
	}

	@Resource
	PmsChatService pmsChatService;

	/*
	 * 查所有，部门用户，json
	 */
	@RequestMapping("/ChatDeptUser.do")
	public void select(HttpServletResponse response) {
		List deptusers = pmsChatService.findAllDeptUser();

		JSONArray jsonArray = new JSONArray();
		for (int i = 0; i < deptusers.size(); i++) {
			Map map = (Map) deptusers.get(i);
			JSONObject obj = new JSONObject();
			obj.put("id", map.get("ID"));
			obj.put("parentId", map.get("PARENTID"));
			obj.put("name", map.get("NAME"));
			jsonArray.add(obj);
		}
		JsonUtil.outJsonString(response, jsonArray.toString());
	}

	/*
	 * 发送消息
	 */
	@RequestMapping("/ChatMsgSend.do")
	public void sendMsg(String chatMsg, String chatReceiver, HttpServletRequest request, HttpServletResponse response)
			throws UnsupportedEncodingException {
		SysUser sysUser = (SysUser) request.getSession().getAttribute("sysUser");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("chatMsg", URLDecoder.decode(chatMsg, "UTF-8"));
		params.put("userid", sysUser.getUserid());
		params.put("chatReceiver", chatReceiver.split(","));

		pmsChatService.sendMsg(params);

		// 尝试给所有在线用户推送消息提醒
		String[] names = chatReceiver.split(",");
		for (int i = 0; i < names.length; i++) {
			// 校验是否是用户（传过来的name里混杂了部门名称）
			if (pmsChatService.checkIsUser(names[i])) {
				infoHandler().sendMessageToUser(names[i], new TextMessage("Yes"));
			}
		}

		JSONObject result = new JSONObject();
		result.put("success", true);
		JsonUtil.outJsonString(response, result.toString());
	}

	/*
	 * 接收消息
	 */
	@RequestMapping("/ChatMsgRcv.do")
	public void rcvMsg(HttpServletRequest request, HttpServletResponse response) {
		SysUser sysUser = (SysUser) request.getSession().getAttribute("sysUser");

		List list = pmsChatService.rcvMsg(sysUser.getUserid());

		JSONObject result = new JSONObject();
		result.put("success", true);
		String s = "";
		for (int i = 0; i < list.size(); i++) {
			Map map = (HashMap) list.get(i);
			s += map.get("FASONG_NAME") + "&nbsp;&nbsp;&nbsp;&nbsp;>>>&nbsp;&nbsp;&nbsp;&nbsp;"
					+ map.get("JIESHOU_NAME") + "&nbsp;&nbsp;" + map.get("TIME2") + "&nbsp;&nbsp"
					+ map.get("FASONG_ZHUANGTAI") + " <br/>" + map.get("FASONG_CONTENT") + "<br/><br/>";
		}
		result.put("msg", s);
		JsonUtil.outJsonString(response, result.toString());

		// 改变消息阅读状态（设置我的未读消息状态为已读）
		changeState(request, response);
	}

	/*
	 * 改变消息阅读状态（设为已读）
	 */
	@RequestMapping("/ChatChangeState.do")
	public void changeState(HttpServletRequest request, HttpServletResponse response) {
		SysUser sysUser = (SysUser) request.getSession().getAttribute("sysUser");
		pmsChatService.changeState(sysUser.getUserid());

		JSONObject result = new JSONObject();
		result.put("success", true);
		String s = "";
		result.put("msg", s);
		JsonUtil.outJsonString(response, result.toString());
	}

	/*
	 * 获取消息阅读状态
	 */
	@RequestMapping("/ChatGetState.do")
	public void getState(HttpServletRequest request, HttpServletResponse response) {
		SysUser sysUser = (SysUser) request.getSession().getAttribute("sysUser");

		List list = pmsChatService.getState(sysUser.getUserid());

		JSONObject result = new JSONObject();
		result.put("success", true);
		boolean hasUnRead = false;// 是否有未读消息
		if (list.size() > 0) {
			hasUnRead = true;
		}
		result.put("msg", hasUnRead);
		JsonUtil.outJsonString(response, result.toString());
	}
}
