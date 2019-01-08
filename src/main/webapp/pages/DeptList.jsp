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
	var url = "";

	var currentNodeid = 0;
	var currentNodename;

	$(function() {

		// 实例化树形菜单
		$("#tree").tree({
			url : '${pageContext.request.contextPath }/DeptSelect.do',
			loadFilter : function(rows) {
				return convert(rows);
			},
			onClick : function(node) {
				currentNodeid = node.id;
				currentNodename = node.text;

				//如果点击的是部门节点，则执行查询操作
				//if (node.url == undefined) {
				//	$("#dg").datagrid({
				//		url : "${pageContext.request.contextPath }/DeptSelect2.do?did=" + currentNodeid
				//	});
				//}
			}
		});

		// 实例化数据网格
		$("#dg").datagrid({
			url : "${pageContext.request.contextPath}/DeptSelect2.do",
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
				field : 'did',
				title : '部门编号',
				width : 100
			}, {
				field : 'dpid',
				title : '部门父编号',
				width : 100
			}, {
				field : 'dname',
				title : '部门名称',
				width : 100
			}, {
				field : 'dremark',
				title : '备注',
				width : 200
			}, {
				field : 'dsortkey',
				title : '排序码',
				width : 100
			} ] ],
			onDblClickRow : function(rowIndex, rowData) { // 双击行事件
				openDeptModifyDialog();
			}
		});
	});

	// 增加对话框
	function openDeptAddDialog() {
		$('#dlg').dialog('open').dialog("setTitle", "部门信息");
		
		resetValue();

		$('#dpid').val(currentNodeid);
		$('#dpname').val(currentNodename);

		url = "${pageContext.request.contextPath}/DeptAdd.do";
	}

	// 修改对话框
	function openDeptModifyDialog() {
		var selectedRows = $("#dg").datagrid('getSelections');// 选中行
		if (selectedRows.length != 1) {
			$.messager.show({
				msg : '请选择要修改的一行数据！',
				title : '消息提示'
			});
			return;
		}
		var row = selectedRows[0];
		
		$("#dlg").dialog("open").dialog("setTitle", "编辑部门信息");
		
		// 赋值
		$("#dpid").val(row.dpid);
		$("#dpname").val(row.dpname);
		$("#dname").val(row.dname);
		$("#dremark").val(row.dremark);
		$("#dsortkey").val(row.dsortkey);

		url = "${pageContext.request.contextPath}/DeptUpd.do?did=" + row.did;
	}

	// 关闭对话框
	function closeDeptDialog() {
		$('#dlg').dialog("close");
		resetValue();
	}

	// 重置
	function resetValue() {
		$('#dpname').val("");
		$('#dname').val("");
		$('#dremark').val("");
		$('#dsortkey').val("");
	}

	// 保存
	function saveDept() {
		$('#fm').form("submit", {
			url : url,
			onSubmit : function() {
				return $(this).form("validate");
			},
			success : function(data) {
				var r = eval('(' + data + ')');
				if (r.success) {
					$.messager.show({
						msg : r.msg,
						title : '消息提示'
					});
					resetValue();
					$("#dlg").dialog("close");
					$("#dg").datagrid("reload");// 刷新数据网格
					$("#tree").tree('reload');// 刷新左侧树结构
				} else {
					$.messager.alert("消息提示", r.msg, 'error');
					return;
				}
			}
		});
	}

	// 删除
	function deleteDept() {
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
			strIds.push(selectedRows[i].did);
		}
		var ids = strIds.join(",");
		$.messager.confirm("系统提示", "您确认要删掉这<font color=red>" + selectedRows.length + "</font>条数据吗？", function(t) {
			if (t) {
				$.ajax({
					type : "post",
					url : "${pageContext.request.contextPath}/DeptDel.do",
					data : {
						ids : ids
					},
					dataType : 'json',
					success : function(r) {
						if (r.success) {
							$("#dg").datagrid("reload");// 刷新数据网格
							$("#tree").tree('reload');// 刷新左侧树结构
						} else {
							$.messager.alert("消息提示", r.msg, 'error');
						}
					}
				});
			}
		});
	}

	// 查询
	function searchDept() {
		$("#dg").datagrid({
			url : "${pageContext.request.contextPath}/DeptSelect2.do"
		});
	}
</script>
<style>
</style>
</head>
<body class="easyui-layout">

	<div region="west" class="west" title="所在部门">
		<ul id="tree"></ul>
	</div>

	<div region="center" border="false">
		<table id="dg" class="easyui-datagrid" toolbar="#tb">
		</table>
	</div>

	<div style="padding: 5px;" id="tb">
		<a href="javascript:openDeptAddDialog()" class="easyui-linkbutton"
			id="btn_add" data-options="iconCls:'icon-add'" plain="true">添加</a> <a
			href="javascript:openDeptModifyDialog()" class="easyui-linkbutton"
			id="btn_edit" data-options="iconCls:'icon-edit'" plain="true">修改</a>
		<a href="javascript:deleteDept()" class="easyui-linkbutton"
			id="btn_remove" data-options="iconCls:'icon-remove'" plain="true">删除</a>
		<a href="javascript:searchDept()" class="easyui-linkbutton"
			id="btn_search" data-options="iconCls:'icon-search'" plain="true">查询</a>
	</div>

	<div id="dlg" class="easyui-dialog"
		style="width: 400px; height: 300px; padding: 10px 20px;" closed="true"
		buttons="#dlg-buttons">
		<form id="fm" method="post">
			<table>
				<tr>
					<td>上级部门：<input type="hidden" name="dpid" id="dpid" value="" /></td>
					<td><input type="text" id="dpname" class="easyui-validatebox"
						value="" disabled="disabled"></td>
				</tr>
				<tr>
					<td>部门名称：</td>
					<td><input type="text" name="dname" id="dname"
						class="easyui-validatebox" required="true"></td>
				</tr>
				<tr>
					<td valign="top">备注：</td>
					<td><textarea rows="7" cols="30" name="dremark" id="dremark"
							class="easyui-validatebox"></textarea></td>
				</tr>
				<tr>
					<td>排序码：</td>
					<td><input type="text" name="dsortkey" id="dsortkey"
						class="easyui-validatebox" required="true"></td>
				</tr>
			</table>
		</form>
	</div>

	<div id="dlg-buttons">
		<a href="javascript:saveDept()" class="easyui-linkbutton"
			iconCls="icon-ok">保存</a> <a href="javascript:closeDeptDialog()"
			class="easyui-linkbutton" iconCls="icon-cancel">关闭</a>
	</div>
</body>
</html>