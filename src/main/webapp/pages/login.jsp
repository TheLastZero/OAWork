<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" />
<title>OA·办公管理系统</title>
<link rel="shortcut icon"
	href="${pageContext.request.contextPath }/favicon.ico" />
<link rel="bookmark"
	href="${pageContext.request.contextPath }/facicon.ico" />
<style>
td {
	font-size: 12px;
	color: #007AB5;
}

form {
	margin: 1px;
	padding: 1px;
}

input {
	border: 0px;
	height: 26px;
	color: #007AB5;
	border: thin none #FFFFFF;
}

body {
	background-repeat: no-repeat;
	background-color: #9CDCF9;
	background-position: 0px 0px;
}
</style>
<!--[if IE]>
<script src="http://html5shiv.googlecode.com/svn/trunk/html5.js"></script>
<![endif]-->

<script type="text/javascript">
	function login() {
		if (document.getElementById("txtUsername").value == "") {
			alert("请填写用户名！");
			return false;
		}
		if (document.getElementById("txtPassword").value == "") {
			alert("请填写密码！");
			return false;
		}
		return true;
	}
</script>
</head>

<body>
	<form method="post"
		action="${pageContext.request.contextPath }/User_login.do">
		<table width="681" border="0" align="center" cellpadding="0"
			cellspacing="0" style="margin-top: 120px">
			<tbody>
				<tr>
					<td width="353" height="259" align="center" valign="bottom"
						background="${pageContext.request.contextPath }/images/login_1.gif"
						style="vertical-align: top;"><br /> <br /> <br /> <br />
						<br />
						<h1>
							<i>OA·办公管理系统</i>
						</h1></td>
					<td width="195"
						background="${pageContext.request.contextPath }/images/login_2.gif">
						<table width="190" height="106" border="0" align="center"
							cellpadding="2" cellspacing="0">
							<tbody>
								<tr>
									<td height="50" colspan="2" align="left">&nbsp;</td>
								</tr>
								<%
									String uname = "";
									String upass = "";
									Cookie[] cs = request.getCookies();//获取请求中所有的cookie
									if (cs != null) {// 如果存在cookie
										for (Cookie c : cs) {//循环遍历所有的cookie
											if ("uname".equals(c.getName())) {//查找名为uname的cookie
												uname = c.getValue();//获取这个cookie的值，给uname这个变量
											}
											if ("upass".equals(c.getName())) {
												upass = c.getValue();
											}
										}
									}
								%>
								<tr>
									<td width="60" height="30" align="left">帐号：</td>
									<td><input name="username" type="text" value="<%=uname%>"
										id="txtUsername"
										style="border: solid 1px #27B3FE; height: 20px; background-color: #FFFFFF; width: 130px;">
									</td>
								</tr>
								<tr>
									<td height="30" align="left">密码：</td>
									<td><input name="userpass" type="password"
										value="<%=upass%>" id="txtPassword"
										style="border: solid 1px #27B3FE; height: 20px; background-color: #FFFFFF; width: 130px;" />
									</td>
								</tr>
								<tr>
									<td colspan="2" align="center"><input
										name="rememberNamePass" type="checkbox" checked="checked"
										style="vertical-align: middle;" />记住帐号密码<br /></td>
								</tr>
								<tr>
									<td colspan="2" align="center"><input type="submit"
										name="btnLogin" value="登&nbsp;录" onclick="return login();"
										id="btnLogin" /></td>
								</tr>
								<tr>
									<td height="5" colspan="2" align="center"
										style="padding-top: 15px;">${errorMsg }</td>
								</tr>
							</tbody>
						</table>
					</td>
					<td width="133"
						background="${pageContext.request.contextPath }/images/login_3.gif">&nbsp;</td>
				</tr>
				<tr>
					<td height="161" colspan="3"
						background="${pageContext.request.contextPath }/images/login_4.gif"></td>
				</tr>
			</tbody>
		</table>
	</form>
</body>
</html>