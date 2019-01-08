<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="common.jsp"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" />
<title></title>

<script type="text/javascript">
	var url = "";

	$(function() {

		// 实例化数据网格
		$("#dg").datagrid({
			url : "${pageContext.request.contextPath}/ProjectSelect2.do",
			pagination : true,
			rownumbers : true,
			pageSize : 10,
			pageList : [ 5, 10, 15, 20 ],
			fit : true,
			fitColumns : true,
			singleSelect: true,
			columns : [ [ {
				field : 'cb',
				checkbox : true,
				width : 10
			}, {
				field : 'pid',
				title : '项目编号',
				width : 100
			}, {
				field : 'dname',
				title : '所属部门',
				width : 100
			}, {
				field : 'pname',
				title : '项目名称',
				width : 200
			}, {
				field : 'premark',
				title : '备注',
				width : 100
			} ] ],
			onDblClickRow : function(rowIndex, rowData) { // 双击行事件
				openProjectModifyDialog();
			}
		});
	});

	// 增加对话框
	function openProjectAddDialog() {
		$('#dlg').dialog('open').dialog("setTitle", "项目信息");
		
		resetValue();
		
		url = "${pageContext.request.contextPath}/ProjectAdd.do";
	}

	// 修改对话框
	function openProjectModifyDialog() {
		var selectedRows = $("#dg").datagrid('getSelections');// 选中行
		if (selectedRows.length != 1) {
			$.messager.show({
				msg : '请选择要修改的一行数据！',
				title : '消息提示'
			});
			return;
		}
		var row = selectedRows[0];

		$("#dlg").dialog("open").dialog("setTitle", "编辑项目信息");

		// 赋值
		$("#dname").val(row.dname);
		$("#pname").val(row.pname);
		$("#premark").val(row.premark);

		url = "${pageContext.request.contextPath}/ProjectUpd.do?pid=" + row.pid;
	}

	// 关闭对话框
	function closeProjectDialog() {
		$('#dlg').dialog("close");
		resetValue();
	}

	// 重置
	function resetValue() {
		$('#pname').val("");
		$('#premark').val("");
	}

	// 保存
	function saveProject() {
		$('#fm').form("submit", {
			url : url,
			onSubmit : function() {
				return $(this).form("validate");
			},
			success : function(data) {
				var r = eval('(' + data + ')');
				if (r.success) {
					$.messager.show({
						msg : "操作成功",
						title : '消息提示'
					});
					resetValue();
					$("#dlg").dialog("close");
					$("#dg").datagrid("reload");
				} else {
					$.messager.alert("消息提示", r.msg, 'error');
					return;
				}
			}
		});
	};

	// 删除
	function deleteProject() {
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
			strIds.push(selectedRows[i].pid);
		}
		var ids = strIds.join(",");
		$.messager.confirm("系统提示", "您确认要删掉这<font color=red>" + selectedRows.length + "</font>条数据吗？", function(t) {
			if (t) {
				$.ajax({
					type : "post",
					url : "${pageContext.request.contextPath}/ProjectDel.do",
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
							$("#dg").datagrid("reload");// 刷新数据网格
						} else {
							$.messager.alert("消息提示", r.msg, 'error');
						}
					}
				});
			}
		});
	}

	// 查询
	function searchProject() {
		$("#dg").datagrid({
			url : "${pageContext.request.contextPath}/ProjectSelect2.do"
		});
	}
</script>
</head>

<body>
	<table id="dg" class="easyui-datagrid" toolbar="#tb">
	</table>

	<div style="padding: 5px;" id="tb">
		<a href="javascript:openProjectAddDialog()" class="easyui-linkbutton"
			id="btn_add" data-options="iconCls:'icon-add'" plain="true">添加</a> <a
			href="javascript:openProjectModifyDialog()" class="easyui-linkbutton"
			id="btn_edit" data-options="iconCls:'icon-edit'" plain="true">修改</a>
		<a href="javascript:deleteProject()" class="easyui-linkbutton"
			id="btn_remove" data-options="iconCls:'icon-remove'" plain="true">删除</a>
		<a href="javascript:searchProject()" class="easyui-linkbutton"
			id="btn_search" data-options="iconCls:'icon-search'" plain="true">查询</a>
	</div>

	<div id="dlg" class="easyui-dialog"
		style="width: 400px; height: 280px; padding: 10px 20px;" closed="true"
		buttons="#dlg-buttons">
		<form id="fm" method="post">
			<table>
				<tr>
					<td>所属部门：<input type="hidden" name="sysDept.did"
						value="${sysUser.sysDept.did }" /></td>
					<td><input type="text" name="dname" id="dname"
						class="easyui-validatebox" value="${sysUser.sysDept.dname }"
						disabled="disabled"></td>
				</tr>
				<tr>
					<td valign="top">项目名称：</td>
					<td><textarea rows="3" cols="30" name="pname" id="pname"
							class="easyui-validatebox"></textarea></td>
				</tr>
				<tr>
					<td valign="top">备注：</td>
					<td><textarea rows="3" cols="30" name="premark" id="premark"
							class="easyui-validatebox"></textarea></td>
				</tr>
			</table>
		</form>
	</div>

	<div id="dlg-buttons">
		<a href="javascript:saveProject()" class="easyui-linkbutton"
			iconCls="icon-ok">保存</a> <a href="javascript:closeProjectDialog()"
			class="easyui-linkbutton" iconCls="icon-cancel">关闭</a>
	</div>
</body>
</html>