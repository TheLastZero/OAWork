<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="common.jsp"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" />
<title>即时通讯</title>

<script type="text/javascript">
	$(function() {
		// 实例化树形菜单
		$("#tree1").tree({
			url : '${pageContext.request.contextPath }/ChatDeptUser.do',
			loadFilter : function(rows) {
				return convert(rows);
			}
		});

		window.setInterval(function() {
			// 显示最新消息交互情况
			$.ajax({
				type : "post",//请求方式
				url : "${pageContext.request.contextPath }/ChatMsgRcv.do",//请求路径
				contentType : "application/x-www-form-urlencoded; charset=UTF-8",
				data : {//传参
				},
				dataType : "json",
				success : function(data) {
					$("#chatMsgShow").html(data.msg);
				}
			});
		}, 3000);
	});
</script>
</head>
<body>
	<div id="panel" class="easyui-panel" iconCls="icon-search" maximized="true">
		<div class="easyui-layout" fit="true">
			<div region="center" border="false">
				<div class="easyui-layout" fit="true">
					<div region="east" border="false" class="p-right"
						style="width: 200px; left: 300px; top: 0px;">

						<div class="easyui-panel" style="padding: 5px" fit="true">
							<ul id="tree1" class="easyui-tree"
								data-options="animate:true,checkbox:true"></ul>
						</div>
						<script type="text/javascript">
							// 发送消息
							function sendMsg() {
								// 检查消息内容是否为空
								var chatmsg = $("#chatMsg").text;
								if (chatmsg == '') {
									alert("消息内容不能为空！");
								}

								var currentNodeids = '';
								var currentNodenames = '';

								var nodes = $('#tree1').tree('getChecked');
								for (var i = 0; i < nodes.length; i++) {
									currentNodeids += nodes[i].id + ',';
								}

								if (currentNodeids != '') {
									$.ajax({
										type : "post",//请求方式
										url : "${pageContext.request.contextPath }/ChatMsgSend.do",//请求路径
										contentType : "application/x-www-form-urlencoded; charset=UTF-8",
										data : {//传参
											chatMsg : encodeURIComponent($("#chatMsg").val()),
											chatReceiver : currentNodeids
										},
										dataType : "json",
										success : function(data) {
											// 接收消息
											$.ajax({
												type : "post",//请求方式
												url : "${pageContext.request.contextPath }/ChatMsgRcv.do",//请求路径
												contentType : "application/x-www-form-urlencoded; charset=UTF-8",
												data : {//传参
												},
												dataType : "json",
												success : function(data) {
													$("#chatMsgShow").html(data.msg);
												}
											});

											$("#chatMsg").val('');
										}
									});
								} else {
									$.messager.show({
										msg : '请选择需要关联的用户！',
										title : '消息提示'
									});
								}
							}
						</script>
					</div>
					<div region="center" border="false" style="border: 1px solid #ccc;">
						<div class="easyui-layout" fit="true">
							<div region="south" split="true" border="true"
								style="height: 100px; overflow-y: hidden;">
								<textarea id="chatMsg"
									style="border: 0; width: 74%; height: 100%; resize: none"></textarea>
								<input
									style="float: right; margin-top: 30px; margin-right: 5px;"
									type="image"
									src="${pageContext.request.contextPath }/images/send_btn.jpg"
									onclick="sendMsg()" />
							</div>
							<div id="chatMsgShow" region="center" border="false"></div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>