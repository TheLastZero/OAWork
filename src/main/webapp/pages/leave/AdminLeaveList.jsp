<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'UserLeaveList.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<!-- 导入Jquery组件 -->
	<script src="https://code.jquery.com/jquery-3.3.1.min.js"></script>
	<!-- 导入easyui组件 -->
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/jquery-easyui/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/jquery-easyui/themes/icon.css">
	<script type="text/javascript" src="${pageContext.request.contextPath }/jquery-easyui/jquery.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath }/jquery-easyui/jquery.easyui.min.js"></script>
	
	<script type="text/javascript" src="${pageContext.request.contextPath }/jquery-easyui/locale/easyui-lang-zh_CN.js"></script>
	
	<script type="text/javascript">
		
		$(document).ready(function(){
			
		var rowEditor = undefined;
		var currentRoleid = 0;
	
		var currentNodeids;
		
		// 实例化数据网格
		$("#dg").datagrid({
			url : "${pageContext.request.contextPath }/leave/getUserLeaveListByDid?dpid="+${sessionScope.sysUser.did},
			pagination : true,
			rownumbers : true,
			pageSize : 5,
			pageList : [ 5, 10, 15, 20 ],
			fit : true,
			fitColumns : true,
			singleSelect: true,
			columns : [ [
			 {
				field : 'leaveId',
				title : '请假记录编号',
				width : 100
			},  
			{
				field : 'leaveType',
				title : '请假类型',
				width : 100
			},
			{
				field : 'timeStart',
				title : '请假开始时间',
				width : 100,
				formatter:function(value,row,index){
                      
                      var d = new Date(row.timeStart);
                      var h = d.getHours();
                      
                      if(h==8){
                      	return d.toLocaleDateString()+" 上午";
                      }else if(h==14){
                      	return d.toLocaleDateString()+" 下午";
                      }
		        }
			},
			{
				field : 'timeEnd',
				title : '请假截止时间',
				width : 100,
				formatter:function(value,row,index){
                      
                      var d = new Date(row.timeEnd);
                      var h = d.getHours();
                      
                      if(h==8){
                      	return d.toLocaleDateString()+" 上午";
                      }else if(h==14){
                      	return d.toLocaleDateString()+" 下午";
                      }
		        }
			}, 
			{
				field : 'timeUsed',
				title : '请假使用时间',
				width : 100
			},
			{
				field : 'user',
				title : '请假人',
				width : 100,
				formatter:function(value,row,index){
                      
                      return row.user.realname;
		        }
			},
			{
				field : 'leaveStatus',
				title : '请假状态',
				width : 100,
				formatter:function(value,row,index){
                      
                      var s = row.leaveStatus;
                      if(s==0){
                      	return "提交中";
                      }else if(s==1){
                      	return '<span style="color: #f1ae15">待处理</span>';
                      }else if(s==2){
                      	return "部门经理通过审核";
                      }else if(s==3){
                      	return "人事经理审核中";
                      }else if(s==4){
                      	return "人事经理通过审核";
                      }else if(s==31){
                      	return '<span style="color: red">驳回</span>';
                      }else if(s==41){
                      	return '<span style="color: red">人事经理驳回</span>';
                      }else if(s==5){
                      	return '<span style="color: green">请假通过</span>';
                      }
                      
		        }
			}, /* {
				field : 'rolename',
				title : '角色名称',
				width : 100,
				editor : {
					type : 'validatebox',
					options : {
						required : true
					}
				}
			} */ ] ],
			toolbar : [ {
				id : "btn_add",
				text : "同意批假",
				iconCls : "icon-add",
				handler : function() {
					
					//updateLeaveStatusById
					var selectedRows = $("#dg").datagrid('getSelections');
					if (selectedRows.length == 0) {
						$.messager.show({
							msg : '请选择要同意批假的数据！',
							title : '消息提示'
						});
						return;
					}
					
					if(parseInt(selectedRows[0].leaveStatus) > 2){
						$.messager.show({
							msg : '已处理的请假信息，无法同意批假',
							title : '消息提示'
						});
						return false;
					}

					var strIds = [];
					for (var i = 0; i < selectedRows.length; i++) {
						strIds.push(selectedRows[i].leaveId);
					}
					var ids = strIds.join(",");
					$.messager.confirm("系统提示", "您确认要同意批假请假记录编号为<font color=red>" + selectedRows[0].leaveId + "</font>的数据吗？", function(t) {
						if (t) {
						
							var leaveCode;
							if(parseInt(selectedRows[0].timeUsed) <= 3 ){//如果请假时间在3天内，部门主管有权批假
								leaveCode = 5;
							}else{//如果请假时间在3天以上，交由人事主管审批
								leaveCode = 2;
							}
						
							$.ajax({
								type : "post",
								url : "${pageContext.request.contextPath}/leave/updateLeaveStatusById",
								data : {
									"leaveId" : selectedRows[0].leaveId,
									"leaveStatus":leaveCode
								},
								dataType : 'json',
								success : function(r) {
								
								console.log(r);
									if (r.code==100) {
										$.messager.show({
											msg : r.msg,
											title : '消息提示'
										});
										$("#dg").datagrid('reload');// 刷新数据网格
									} else {
										$.messager.alert("消息提示", r.msg, 'error');
										
									}
									$("#dg").datagrid('unselectAll');
								}
							});
						}
					});
					
				}
			}, {
				id : "btn_remove",
				text : "驳回假条",
				iconCls : "icon-remove",
				handler : function() {
					var selectedRows = $("#dg").datagrid('getSelections');
					if (selectedRows.length == 0) {
						$.messager.show({
							msg : '请选择要驳回的数据！',
							title : '消息提示'
						});
						return;
					}
					
					if(parseInt(selectedRows[0].leaveStatus) > 2){
						$.messager.show({
							msg : '已处理的请假信息，无法驳回',
							title : '消息提示'
						});
						return false;
					}

					var strIds = [];
					for (var i = 0; i < selectedRows.length; i++) {
						strIds.push(selectedRows[i].leaveId);
					}
					var ids = strIds.join(",");
					$.messager.confirm("系统提示", "您确认要驳回请假记录编号为<font color=red>" + selectedRows[0].leaveId + "</font>的数据吗？", function(t) {
						if (t) {
							$.ajax({
								type : "post",
								url : "${pageContext.request.contextPath}/leave/updateLeaveStatusById",
								data : {
									"leaveId" : selectedRows[0].leaveId,
									"leaveStatus":31
								},
								dataType : 'json',
								success : function(r) {
								
								console.log(r);
									if (r.code==100) {
										$.messager.show({
											msg : r.msg,
											title : '消息提示'
										});
										$("#dg").datagrid('reload');// 刷新数据网格
									} else {
										$.messager.alert("消息提示", r.msg, 'error');
										
									}
									$("#dg").datagrid('unselectAll');
								}
							});
						}
					});
				}
			} ]
			
		});
			
		});
		
	</script>

  </head>
  
<body class="easyui-layout">

	<div region="north" border="false" border="false" style="height: 100%;">
		<table id="dg"></table>
	</div>
	
  </body>
</html>
