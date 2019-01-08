<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="common.jsp"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" />
<title></title>
<style>
.west {
	width: 200px;
	padding: 10px;
}
</style>

<script type="text/javascript">
	var rowEditor = undefined;
	
	var currentRoleid = 0;
	var currentNodeids;

	$(function() {

		// 实例化数据网格
		$("#dg").datagrid({
			url : "${pageContext.request.contextPath }/RoleSelect2.do",
			pagination : true,
			rownumbers : true,
			pageSize : 5,
			pageList : [ 5, 10, 15, 20 ],
			fit : true,
			fitColumns : true,
			singleSelect: true,
			columns : [ [ {
				field : 'roleid',
				title : '角色编号',
				width : 100
			}, {
				field : 'rolename',
				title : '角色名称',
				width : 100,
				editor : {
					type : 'validatebox',
					options : {
						required : true
					}
				}
			} ] ],
			onClickRow : function(rowIndex, rowData) { // 单击行事件
				// 点击角色行时，查询任务项数据
				currentRoleid = rowData.roleid;

				// 刷新数据网格
				$("#dg2").datagrid({
					url : "${pageContext.request.contextPath }/RoleModuleSelect2.do?roleid=" + currentRoleid
				});
				// 刷新左侧树结构
				$("#tree").tree({
					url : "${pageContext.request.contextPath }/RoleModuleSelect.do?roleid=" + currentRoleid
				});
			}
		});

		// 实例化树形菜单
		$("#tree").tree({
			//url : "",
			loadFilter : function(rows) {
				return convert(rows);
			},
			onClick : function(node) {
			}
		});

		// 实例化数据网格
		$("#dg2").datagrid({
			//url : "",
			pagination : true,
			rownumbers : true,
			pageSize : 5,
			pageList : [ 5, 10, 15, 20 ],
			fit : true,
			fitColumns : true,
			columns : [ [ {
				field : 'cb',
				checkbox : true,
				width : 10
			}, {
				field : 'rmid',
				title : '角色模块编号',
				width : 100
			}, {
				field : 'mid',
				title : '模块编号',
				width : 100
			}, {
				field : 'mname',
				title : '模块名称',
				width : 100
			} ] ],
			toolbar : [ {
				id : "btn_add2",
				text : "-->>添加关联",
				iconCls : "icon-add",
				handler : function() {
					currentNodeids = '';

					var nodes = $('#tree').tree('getChecked');
					for (var i = 0; i < nodes.length; i++) {
						currentNodeids += nodes[i].id + ',';
					}

					if (currentNodeids != '') {
						$.ajax({
							type : "post",
							url : "${pageContext.request.contextPath}/RoleModuleAdd.do",
							data : {
								roleid : currentRoleid,
								mids : currentNodeids
							},
							dataType : 'json',
							success : function(r) {
								if (r.success) {
									$.messager.show({
										msg : r.msg,
										title : '消息提示'
									});

									$("#dg2").datagrid('reload');// 刷新数据网格
									$("#tree").tree('reload');// 刷新左侧树结构
								} else {
									$.messager.alert("消息提示", r.msg, 'error');
								}
								$("#dg2").datagrid('unselectAll');
							}
						});
					} else {
						$.messager.show({
							msg : '请选择需要关联的用户！',
							title : '消息提示'
						});
					}
				}
			}, {
				id : "btn_remove2",
				text : "取消关联",
				iconCls : "icon-remove",
				handler : function() {
					var selectedRows = $("#dg2").datagrid('getSelections');
					if (selectedRows.length == 0) {
						$.messager.show({
							msg : '请选择要删除的数据！',
							title : '消息提示'
						});
						return;
					}

					var strIds = [];
					for (var i = 0; i < selectedRows.length; i++) {
						strIds.push(selectedRows[i].rmid);
					}
					var ids = strIds.join(",");
					$.messager.confirm("系统提示", "您确认要删掉这<font color=red>" + selectedRows.length + "</font>条数据吗？", function(t) {
						if (t) {
							$.ajax({
								type : "post",
								url : "${pageContext.request.contextPath}/RoleModuleDel.do",
								data : {
									rmids : ids
								},
								dataType : 'json',
								success : function(r) {
									if (r.success) {
										$.messager.show({
											msg : r.msg,
											title : '消息提示'
										});
										$("#dg2").datagrid('reload');// 刷新数据网格
										// 刷新左侧树结构
										$("#tree").tree({
											url : "${pageContext.request.contextPath }/RoleModuleSelect.do?roleid=" + currentRoleid
										});
									} else {
										$.messager.alert("消息提示", r.msg, 'error');
									}
									$("#dg2").datagrid('unselectAll');
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
	<div region="north" border="false" border="false" style="height: 280px">
		<table id="dg"></table>
	</div>

	<div region="center" border="false">
		<div class="easyui-layout" fit="true">
			<div region="west" class="west" title="未关联角色的模块">
				<ul id="tree" data-options="animate:true,checkbox:true"></ul>
			</div>

			<div region="center" border="false">
				<table id="dg2"></table>
			</div>
		</div>
	</div>
</body>
</html>