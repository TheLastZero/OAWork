package com.myhopu.utils;

import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.servlet.http.HttpServletResponse;

import org.junit.Test;

public class JsonUtil {
	public static void outJsonString(HttpServletResponse response, String json) {
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/plain;charset=UTF-8"); // 使用text/plain的目的是兼容旧版浏览器，在页面需要用eval进行转换
		// 设置页面不缓存
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("expires", 0L);// 在代理服务器端防止缓冲
		try {
			PrintWriter out = response.getWriter();
			System.out.println(json);
			out.write(json);
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}