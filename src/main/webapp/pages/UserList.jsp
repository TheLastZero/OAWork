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

	var currentNodeid;

	// 员工类型 json格式
	var usertypeCombo = [ {
		"value" : "1",
		"text" : "管理员"
	}, {
		"value" : "2",
		"text" : "组长"
	}, {
		"value" : "3",
		"text" : "组员"
	} ];

	$(function() {

		// 实例化树形菜单
		$("#tree").tree({
			url : '${pageContext.request.contextPath }/DeptSelect.do',
			loadFilter : function(rows) {
				return convert(rows);
			},
			onClick : function(node) {
				currentNodeid = node.id;

				//如果点击的是部门节点，则执行查询操作
				if (node.url == undefined) {
					$("#dg").datagrid({
						url : "${pageContext.request.contextPath }/UserSelect2.do?did=" + currentNodeid
					});
				}

				$("#btn_add").linkbutton('enable');
			}
		});

		// 实例化数据网格
		$("#dg").datagrid({
			url : "${pageContext.request.contextPath }/UserSelect2.do",
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
				field : 'userid',
				title : '用户编号',
				width : 100
			}, {
				field : 'did',
				title : '所在部门ID',
				hidden : true,
			}, {
				field : 'dname',
				title : '所在部门',
				width : 100
			}, {
				field : 'username',
				title : '帐号',
				width : 100,
				editor : {
					type : 'validatebox',
					options : {
						required : true
					}
				}
			}, {
				field : 'userpass',
				title : '密码',
				width : 100,
				editor : {
					type : 'validatebox',
					options : {
						required : true
					}
				}
			}, {
				field : 'realname',
				title : '用户姓名',
				width : 100,
				editor : {
					type : 'validatebox',
					options : {
						required : true
					}
				}
			}, {
				field : 'usertype',
				title : '用户类型（已废弃）',
				width : 100,
				formatter : function(value) {
					for (var i = 0; i < usertypeCombo.length; i++) {
						if (usertypeCombo[i].value == value)
							return usertypeCombo[i].text;
					}
					return value;
				},
				editor : {
					type : 'combobox',
					options : {
						data : usertypeCombo,
						valueField : "value",
						textField : "text",
						editable : false,
						panelHeight : 100,
						required : false
					}
				}
			} ] ],
			toolbar : [ {
				id : "btn_add",
				text : "增加",
				iconCls : "icon-add",
				disabled : "disabled",
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
						strIds.push(selectedRows[i].userid);
					}
					var ids = strIds.join(",");
					$.messager.confirm("系统提示", "您确认要删掉这<font color=red>" + selectedRows.length + "</font>条数据吗？", function(t) {
						if (t) {
							$.ajax({
								type : "post",
								url : "${pageContext.request.contextPath}/UserDel.do",
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
						url : "${pageContext.request.contextPath }/UserSelect2.do"
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
				if (inserted.length < 1 && updated.length < 1) {
					rowEditor = undefined;
					$("#dg").datagrid('unselectAll');
					return;
				}
				var url = '';
				if (inserted.length > 0) {
					url = "${pageContext.request.contextPath }/UserAdd.do";
				}
				if (updated.length > 0) {
					url = "${pageContext.request.contextPath }/UserUpd.do";
				}

				// 往行数据对象rowData里附加一项数据
				rowData["sysDept.did"] = currentNodeid;

				$.ajax({
					type : "post",
					url : url,
					data : rowData,
					dataType : 'json',
					success : function(r) {
						if (r.success) {
							$("#dg").datagrid('acceptChanges');
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
	});
</script>
</head>

<body class="easyui-layout">

	<div region="west" class="west" title="所在部门">
		<ul id="tree"></ul>
	</div>

	<div region="center" border="false">
		<table id="dg"></table>
	</div>

</body>
</html>