<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="common.jsp"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" />
<title>OA·办公管理系统</title>
<link rel="shortcut icon"
	href="${pageContext.request.contextPath }/favicon.ico" />
<link rel="bookmark"
	href="${pageContext.request.contextPath }/facicon.ico" />
<style type="text/css">
article, aside, figure, footer, header, hgroup, menu, nav, section {
	display: block;
}

.west {
	width: 200px;
	padding: 10px;
}

.north {
	height: 100px;
}
</style>
<!--[if IE]>
<script src="http://html5shiv.googlecode.com/svn/trunk/html5.js"></script>
<![endif]-->

<script src="${pageContext.request.contextPath }/js/sockjs.min.js"></script>

<script type="text/javascript">
	// 实例化WebSocket对象
	var websocket = null;
	if ('WebSocket' in window || 'MozWebSocket' in window) {
		// 使用HTML5原生的WebSocket
		websocket = new WebSocket("ws://" + window.location.host + "${pageContext.request.contextPath}" + "/websocket/socketServer.do");
	} else {
		// 使用SockJS，SockJS的一大好处在于提供了浏览器的兼容性，在浏览器不支持WebSocket的时候会选用其它降级方式（如：轮询）。Spring也对SockJS提供了支持。
		websocket = new SockJS("http://" + window.location.host + "${pageContext.request.contextPath}" + "/sockjs/socketServer.do");
	}

	// 打开连接事件
	websocket.onopen = function(evt) {
	};

	// 获得消息事件
	websocket.onmessage = function(evt) {
		// alert(evt.data);

		// 改变消息状态图标为动态图
		document.all.webchat.src = "${pageContext.request.contextPath }/images/webchat.gif";

		// 点击消息状态图标后获取未读消息

		// 当用户切换到“即时通讯”标签页时，会取消提醒（改变消息状态图标为静态图）
	};

	// 发生错误事件
	websocket.onerror = function() {
	};

	// 关闭连接事件
	websocket.onclose = function() {
	};

	// 发送消息
	function doSend() {
		if (websocket.readyState == websocket.OPEN) {
			var msg = document.getElementById("inputMsg").value;
			websocket.send(msg);//调用后台handleTextMessage方法
		} else {
			alert("连接失败!");
		}
	}

	// 关闭浏览器时关闭WebSocket连接
	window.close = function() {
		websocket.onclose();
	}

	// 获取消息阅读状态（检查是否有新消息）
	function getState() {
		$.ajax({
			type : "post",//请求方式
			url : "${pageContext.request.contextPath }/ChatGetState.do",//请求路径
			contentType : "application/x-www-form-urlencoded; charset=UTF-8",
			data : {//传参
			},
			dataType : "json",
			success : function(data) {
				var state = data.msg;
				if (state == true) {
					// 改变消息状态图标为动态图
					document.all.webchat.src = "${pageContext.request.contextPath }/images/webchat.gif";
				}
			}
		});
	}

	// 改变消息阅读状态（设置我的未读消息状态为已读）
	function changeState() {
		$.ajax({
			type : "post",//请求方式
			url : "${pageContext.request.contextPath }/ChatChangeState.do",//请求路径
			contentType : "application/x-www-form-urlencoded; charset=UTF-8",
			data : {//传参
			},
			dataType : "json",
			success : function(data) {
			}
		});
	}

	// 获取最新消息
	function getNewMsg() {
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

		// 取消提醒（改变消息状态图标为静态图）
		document.all.webchat.src = "${pageContext.request.contextPath }/images/webchat.PNG";
	}

	// 打开聊天窗口
	/*
	function openChatWindow() {
		// 改变消息阅读状态（设置我的未读消息状态为已读）
		changeState();
		
		// 打开聊天窗口
		window.open("${pageContext.request.contextPath }/pages/chatWindow.jsp", "newwindow",
				"height=400,width=600,top=200,left=300,toolbar=no,menubar=no,scrollbars=no,resizable=no,location=no,status=no");
		
		// 取消提醒（改变消息状态图标为静态图）
		document.all.webchat.src = "${pageContext.request.contextPath }/images/webchat.PNG";
	}
	 */

	$(function() {
		/*
		// 定时检查是否有未读消息，若有，切换消息状态图标为动态图
		window.setInterval(function() {
			$.ajax({
				type : "post",//请求方式
				url : "${pageContext.request.contextPath }/ChatGetState.do",//请求路径
				contentType : "application/x-www-form-urlencoded; charset=UTF-8",
				data : {//传参
				},
				dataType : "json",
				success : function(data) {
					var state = data.msg;
					if (state == true) {
						document.all.webchat.src = "${pageContext.request.contextPath }/images/webchat.gif";
					}
				}
			});
		}, 3000);
		 */

		// 实例化树形菜单（部门员工树）
		$("#tree1").tree({
			url : '${pageContext.request.contextPath }/ChatDeptUser.do',
			loadFilter : function(rows) {
				return convert(rows);
			}
		});

		// 获取消息阅读状态（检查是否有新消息）
		getState();

	});
</script>

<script type="text/javascript">
	$(function() {

		// 实例化树形菜单
		$("#tree").tree({
			url : "${pageContext.request.contextPath }/MyModuleSelect.do",
			loadFilter : function(rows) {
				return convert(rows);
			},
			onClick : function(node) {
				if (node.url) {
					Open(node.text, "${pageContext.request.contextPath }" + node.url);
				}
			}
		});

		// 在右边center区域打开菜单，新增tab
		function Open(text, url) {
			if ($("#tabs").tabs('exists', text)) {
				$('#tabs').tabs('select', text);
			} else {
				$('#tabs').tabs('add', {
					title : text,
					closable : true,
					content : '<iframe scrolling="auto" frameborder="0"  src="' + url + '" style="width:100%;height:100%;"></iframe>'
				});
			}
		}

		// 绑定tabs的右键菜单
		$("#tabs").tabs({
			onContextMenu : function(e, title) {
				e.preventDefault();
				$('#tabsMenu').menu('show', {
					left : e.pageX,
					top : e.pageY
				}).data("tabTitle", title);
			}
		});

		// 实例化menu的onClick事件
		$("#tabsMenu").menu({
			onClick : function(item) {
				CloseTab(this, item.name);
				refreshTab();
				// 取消“即时通讯”的消息提醒
				if (item.name == "即时通讯") {
					document.all.webchat.src = "${pageContext.request.contextPath }/images/webchat.PNG";
				}
			}
		});

		// 几个关闭事件的实现
		function CloseTab(menu, type) {
			var curTabTitle = $(menu).data("tabTitle");
			var tabs = $("#tabs");
			if (type === "close") {
				tabs.tabs("close", curTabTitle);
				return;
			}
			var allTabs = tabs.tabs("tabs");
			var closeTabsTitle = [];
			$.each(allTabs, function() {
				var opt = $(this).panel("options");
				if (opt.closable && opt.title != curTabTitle && type === "closeOther") {
					closeTabsTitle.push(opt.title);
				} else if (opt.closable && type === "closeAll") {
					closeTabsTitle.push(opt.title);
				}
			});
			for (var i = 0; i < closeTabsTitle.length; i++) {
				tabs.tabs("close", closeTabsTitle[i]);
			}
		}

		// 刷新
		function refreshTab() {
			var currTab = $('#tabs').tabs('getSelected');
			var url = $(currTab.panel('options').content).attr('src');
			if (url) {
				$('#tabs').tabs('update', {
					tab : currTab,
					options : {
						content : '<iframe scrolling="auto" frameborder="0"  src="' + url + '" style="width:100%;height:100%;"></iframe>'
					}
				});
			}
		}
	});
</script>

<script type="text/javascript">
	
</script>

<style type="text/css">
body {
	margin: 0;
	border: 0;
	height: 100%;
	overflow-y: auto;
}

#chat {
	display: block;
	bottom: 3px;
	left: 6px;
	position: fixed;
}

* html {
	overflow-x: auto;
	overflow-y: hidden;
}
</style>
</head>

<body class="easyui-layout">

	<div region="north" class="north"
		style="padding-left: 20px;background-image: url(${pageContext.request.contextPath }/images/banner.jpg);">
		<h1 style="">OA·办公管理系统</h1>
		<div id="chat" style="">
			<a href="#" onclick="javascript:getNewMsg();"><img id="webchat"
				src="${pageContext.request.contextPath }/images/webchat.PNG" /></a>
		</div>
	</div>

	<div region="west" class="west" title="导航菜单">
		<ul id="tree"></ul>
	</div>

	<div region="center">
		<div class="easyui-tabs" border="false" fit="true" id="tabs">

			<div title="即时通讯">
				<div id="panel" class="easyui-panel" maximized="true">
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
														// 获取最新消息
														getNewMsg();

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
								<div region="center" border="false"
									style="border: 1px solid #ccc;">
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
			</div>

		</div>
	</div>

	<div id="tabsMenu" class="easyui-menu" style="width: 120px;">
		<div name="close">关闭</div>
		<div name="closeOther">关闭其它</div>
		<div name="closeAll">关闭所有</div>
		<div name="refresh">刷新</div>
	</div>
</body>
</html>