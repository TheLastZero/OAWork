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
				field : 'cb',
				checkbox : true,
				width : 10
			}, {
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
			toolbar : [ {
				id : "btn_add",
				text : "增加",
				iconCls : "icon-add",
				handler : function() {
					if (rowEditor == undefined) {
						$("#dg").datagrid('insertRow', { // 如果处于未被点击状态，在第一行开启编辑
							index : 0,
							row : {}
						});
						rowEditor = 0;
						$("#dg").datagrid('beginEdit', rowEditor); // 没有这行，即使开启了也不编辑
					}
				}
			}, {
				id : "btn_edit",
				text : "修改",
				iconCls : "icon-edit",
				handler : function() {
					var selectedRows = $("#dg").datagrid('getSelections');
					if (selectedRows.length != 1) {
						$.messager.show({
							msg : '请选择要修改的一行数据！',
							title : '消息提示'
						});
						return;
					}
					
					rowEditor = $("#dg").datagrid('getRowIndex', selectedRows[0]);
					$("#dg").datagrid('rejectChanges');
					$("#dg").datagrid("selectRow", rowEditor);
					$("#dg").datagrid('beginEdit', rowEditor);
				}
			}, {
				id : "btn_remove",
				text : "删除",
				iconCls : "icon-remove",
				handler : function() {
					var selectedRows = $("#dg").datagrid('getSelections');
					if (selectedRows.length == 0) {
						$.messager.show({
							msg : '请选择要删除的数据！',
							title : '消息提示'
						});
						return;
					}

					var strIds = [];
					for (var i = 0; i < selectedRows.length; i++) {
						strIds.push(selectedRows[i].roleid);
					}
					var ids = strIds.join(",");
					$.messager.confirm("系统提示", "您确认要删掉这<font color=red>" + selectedRows.length + "</font>条数据吗？", function(t) {
						if (t) {
							$.ajax({
								type : "post",
								url : "${pageContext.request.contextPath}/RoleDel.do",
								data : {
									ids : ids
								},
								dataType : 'json',
								success : function(r) {
									if (r.success) {
										$.messager.show({
											msg : r.msg,
											title : '消息提示'
										});
										$("#dg").datagrid('reload');// 刷新数据网格
									} else {
										$.messager.alert("消息提示", r.msg, 'error');
										$("#dg").datagrid('beginEdit', rowEditor);
									}
									$("#dg").datagrid('unselectAll');
								}
							});
						}
					});
				}
			}, {
				id : "btn_search",
				text : "查询",
				iconCls : "icon-search",
				handler : function() {
					$("#dg").datagrid({
						url : "${pageContext.request.contextPath }/RoleSelect2.do"
					});
				}
			}, {
				text : "保存",
				iconCls : "icon-save",
				handler : function() {
					if ($("#dg").datagrid('validateRow', rowEditor)) {
						$("#dg").datagrid('endEdit', rowEditor);
						rowEditor = undefined;
					}
				}
			}, {
				text : "取消编辑",
				iconCls : "icon-redo",
				handler : function() {
					rowEditor = undefined;
					$("#dg").datagrid('rejectChanges');
				}
			} ],
			onAfterEdit : function(rowIndex, rowData, changes) {
				var inserted = $("#dg").datagrid('getChanges', 'inserted');
				var updated = $("#dg").datagrid('getChanges', 'updated');
				var url = '';
				if (inserted.length > 0) {
					url = "${pageContext.request.contextPath }/RoleAdd.do";
				}
				if (updated.length > 0) {
					url = "${pageContext.request.contextPath }/RoleUpd.do";
				}

				$.ajax({
					type : "post",
					url : url,
					data : rowData,
					dataType : 'json',
					success : function(r) {
						if (r.success) {
							$.messager.show({
								msg : r.msg,
								title : '消息提示'
							});
							$("#dg").datagrid('reload');// 刷新数据网格
						} else {
							$.messager.alert("消息提示", r.msg, 'error');
							$("#dg").datagrid('beginEdit', rowEditor);
						}
						$("#dg").datagrid('unselectAll');
					}
				});
			},
			onClickRow : function(rowIndex, rowData) { // 单击行事件
				// 点击角色行时，查询任务项数据
				currentRoleid = rowData.roleid;

				// 刷新数据网格
				$("#dg2").datagrid({
					url : "${pageContext.request.contextPath }/RoleUserSelect2.do?roleid=" + currentRoleid
				});
				// 刷新左侧树结构
				$("#tree").tree({
					url : "${pageContext.request.contextPath }/RoleDeptUserSelect.do?roleid=" + currentRoleid
				});
			},
			onDblClickRow : function(rowIndex, rowData) { // 双击行事件
				if (rowEditor == undefined) {
					$("#dg").datagrid('beginEdit', rowIndex);
					rowEditor = rowIndex;
				} else {
					$("#dg").datagrid('rejectChanges');
					$("#dg").datagrid('beginEdit', rowIndex);
					rowEditor = rowIndex;
				}
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
				field : 'ruid',
				title : '角色用户编号',
				width : 100
			}, {
				field : 'userid',
				title : '用户编号',
				width : 100
			}, {
				field : 'dname',
				title : '所在部门',
				width : 100
			}, {
				field : 'realname',
				title : '用户姓名',
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
							url : "${pageContext.request.contextPath}/RoleUserAdd.do",
							data : {
								roleid : currentRoleid,
								userids : currentNodeids
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
						strIds.push(selectedRows[i].ruid);
					}
					var ids = strIds.join(",");
					$.messager.confirm("系统提示", "您确认要删掉这<font color=red>" + selectedRows.length + "</font>条数据吗？", function(t) {
						if (t) {
							$.ajax({
								type : "post",
								url : "${pageContext.request.contextPath}/RoleUserDel.do",
								data : {
									ruids : ids
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
											url : "${pageContext.request.contextPath }/RoleDeptUserSelect.do?roleid=" + currentRoleid
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
			<div region="west" class="west" title="未关联角色的员工">
				<ul id="tree" data-options="animate:true,checkbox:true"></ul>
			</div>

			<div region="center" border="false">
				<table id="dg2"></table>
			</div>
		</div>
	</div>
</body>
</html>