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
	var url2 = "";

	var currentNodeid;
	var currentNodename;
	var currentTid;

	$(function() {

		// 实例化树形菜单
		$("#tree").tree({
			url : '${pageContext.request.contextPath }/ProjectSelect.do',
			loadFilter : function(rows) {
				return convert(rows);
			},
			onClick : function(node) {
				currentNodeid = node.id;
				currentNodename = node.text;

				$("#btn_add").linkbutton('enable');

				//如果点击的是项目节点，则执行查询操作
				if (node.url == undefined) {
					$("#dg").datagrid({
						url : "${pageContext.request.contextPath }/TaskSelect2.do?pid=" + currentNodeid
					});
				}
			}
		});

		// 右侧上面板

		// 实例化数据网格
		$("#dg").datagrid({
			//url: "",
			//pagination: true,
			rownumbers : true,
			//pageSize: 10,
			//pageList: [5, 10, 15, 20],
			fit : true,
			fitColumns : true,
			singleSelect: true,
			columns : [ [ {
				field : 'cb',
				checkbox : true,
				width : 10
			}, {
				field : 'tid',
				title : '任务编号',
				width : 100
			}, {
				field : 'pid',
				title : '所属项目ID',
				hidden : true
			}, {
				field : 'pname',
				title : '所属项目',
				width : 150
			}, {
				field : 'tname',
				title : '任务名称',
				width : 100
			}, {
				field : 'userid',
				title : '任务责任人ID',
				hidden : true
			}, {
				field : 'realname',
				title : '任务责任人',
				width : 100
			}, {
				field : 'tdate1',
				title : '任务下达时间',
				width : 100
			}, {
				field : 'tworkday',
				title : '任务工期（天）',
				width : 100
			}, {
				field : 'tremark',
				title : '备注',
				width : 100
			}, {
				field : 'tsortkey',
				title : '排序码',
				width : 100
			} ] ],
			onClickRow : function(rowIndex, rowData) { // 单击行事件
				// 点击任务行时，查询任务项数据
				currentTid = rowData.tid;

				$("#dg2").datagrid({
					url : "${pageContext.request.contextPath }/TaskItemSelect2.do?tid=" + currentTid
				});
			},
			onDblClickRow : function(rowIndex, rowData) { // 双击行事件
				openTaskModifyDialog(rowData);
			}
		});

		// 右侧下面板

		// 实例化数据网格
		$("#dg2").datagrid({
			url : "${pageContext.request.contextPath}/TaskItemSelect2.do",
			//pagination : true,
			rownumbers : true,
			//pageSize : 10,
			//pageList : [ 5, 10, 15, 20 ],
			fit : true,
			fitColumns : true,
			singleSelect: true,
			columns : [ [ {
				field : 'cb',
				checkbox : true,
				width : 10
			}, {
				field : 'iid',
				title : '任务项编号',
				width : 100
			}, {
				field : 'tid',
				title : '所属任务编号',
				hidden : true
			}, {
				field : 'iname',
				title : '任务项名称',
				width : 100
			}, {
				field : 'irequirement',
				title : '具体要求',
				width : 100
			}, {
				field : 'iscore',
				title : '分值',
				width : 100
			}, {
				field : 'isortkey',
				title : '排序码',
				width : 100
			} ] ],
			onDblClickRow : function(rowIndex, rowData) { // 双击行事件
				openTaskModifyDialog2(rowData);
			}
		});
	});

	// 右侧上面板

	// 初始化任务责任人下拉框
	function initData1() {
		$.ajax({
			type : "post",
			url : "${pageContext.request.contextPath}/UserSelect.do",
			dataType : "json",
			async : false,
			success : function(r) {
				//绑定第一个下拉框
				$('#userid').combobox({
					data : r,
					valueField : 'id',
					textField : 'name'
				});
			}
		});
	}

	//初始化日期时间选择框
	function initData2() {
		var curr_time = new Date();
		var strDate = curr_time.getFullYear() + "-";
		strDate += curr_time.getMonth() + 1 + "-";
		strDate += curr_time.getDate() + "-";
		strDate += " " + curr_time.getHours() + ":";
		strDate += curr_time.getMinutes() + ":";
		strDate += curr_time.getSeconds();
		$('#tdate1').datetimebox({
			value : strDate,
			required : true,
			showSeconds : false
		});
	}

	// 增加对话框
	function openTaskAddDialog() {
		$('#dlg').dialog('open').dialog("setTitle", "任务信息");

		resetValue();
		
		$('#pid').val(currentNodeid);
		$('#pname').val(currentNodename);

		// 初始化任务责任人下拉框
		initData1();
		// 初始化日期时间选择框
		initData2();
		url = "${pageContext.request.contextPath}/TaskAdd.do";
	}

	// 修改对话框
	function openTaskModifyDialog(rowData) {
		var selectedRows = $("#dg").datagrid('getSelections');// 选中行
		if (selectedRows.length != 1) {
			$.messager.show({
				msg : '请选择要修改的一行数据！',
				title : '消息提示'
			});
			return;
		}
		var row = selectedRows[0];

		$("#dlg").dialog("open").dialog("setTitle", "编辑任务信息");

		// 初始化任务责任人下拉框
		initData1();
		// 初始化日期时间选择框
		initData2();

		$("#pid").val(row.pid);
		$("#pname").val(row.pname);
		$("#tname").val(row.tname);
		$('#userid').combobox('setValue', row.userid);
		$('#userid').combobox('setText', row.realname);
		$("#tdate1").val(row.tdate1);
		$("#tworkday").val(row.tworkday);
		$("#tremark").val(row.tremark);
		$("#tsortkey").val(row.tsortkey);

		url = "${pageContext.request.contextPath}/TaskUpd.do?tid=" + row.tid;
	}

	// 关闭对话框
	function closeTaskDialog() {		
		$('#dlg').dialog("close");
		resetValue();
	}

	// 重置
	function resetValue() {
		$('#pname').val("");
		$('#tname').val("");
		$('#userid').combobox('setValue', '');
		$('#userid').combobox('setText', '');
		$('#tdate1').val("");
		$('#tworkday').val("");
		$('#tremark').val("");
		$("#tsortkey").val("");
	}

	// 保存
	function saveTask() {
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
	function deleteTask() {
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
			strIds.push(selectedRows[i].tid);
		}
		var ids = strIds.join(",");
		$.messager.confirm("系统提示", "您确认要删掉这<font color=red>" + selectedRows.length + "</font>条数据吗？", function(t) {
			if (t) {
				$.ajax({
					type : "post",
					url : "${pageContext.request.contextPath}/TaskDel.do",
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
	function searchTask() {
		$("#dg").datagrid({
			url : "${pageContext.request.contextPath}/TaskSelect2.do?pid=" + currentNodeid
		});
	}

	//右侧下面板

	// 增加对话框
	function openTaskAddDialog2() {
		var selectedRows = $("#dg").datagrid('getSelections');// 选中行
		if (selectedRows.length != 1) {
			$.messager.show({
				msg : '请先选择一个任务！',
				title : '消息提示'
			});
			return;
		}

		$('#dlg2').dialog('open').dialog("setTitle", "任务项信息");
		
		resetValue();
		
		$('#tid2').val(currentTid);
		
		url2 = "${pageContext.request.contextPath}/TaskItemAdd.do";
	}

	// 修改对话框
	function openTaskModifyDialog2(rowData) {
		var selectedRows = $("#dg2").datagrid('getSelections');// 选中行
		if (selectedRows.length != 1) {
			$.messager.show({
				msg : '请选择要修改的一行数据！',
				title : '消息提示'
			});
			return;
		}
		var row = selectedRows[0];

		$("#dlg2").dialog("open").dialog("setTitle", "编辑任务项信息");

		// 赋值
		$("#tid2").val(row.tid);
		$("#iid").val(row.iid);
		$("#iname").val(row.iname);
		$("#irequirement").val(row.irequirement);
		$("#iscore").val(row.iscore);
		$("#isortkey").val(row.isortkey);

		url2 = "${pageContext.request.contextPath}/TaskItemUpd.do?iid=" + row.iid;
	}

	// 关闭对话框
	function closeTaskDialog2() {
		$('#dlg2').dialog("close");
		resetValue2();
	}

	// 重置
	function resetValue2() {
		$('#iname').val("");
		$('#irequirement').val("");
		$('#iscore').val("");
		$('#isortkey').val("");
	}

	// 保存
	function saveTask2() {
		$('#fm2').form("submit", {
			url : url2,
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
					resetValue2();
					$("#dlg2").dialog("close");
					$("#dg2").datagrid("reload");
				} else {
					$.messager.alert("消息提示", r.msg, 'error');
					return;
				}
			}
		});
	};

	// 删除
	function deleteTask2() {
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
			strIds.push(selectedRows[i].iid);
		}
		var ids = strIds.join(",");
		if (selectedRows.length != 0) {
			$.messager.confirm("系统提示", "您确认要删掉这<font color=red>" + selectedRows.length + "</font>条数据吗？", function(t) {
				if (t) {
					$.ajax({
						type : "post",
						url : "${pageContext.request.contextPath}/TaskItemDel.do",
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
								// 刷新数据网格
								$("#dg").datagrid("reload");
							} else {
								$.messager.alert("消息提示", r.msg, 'error');
							}
						}
					});
				}
			});
		} else {
			$.messager.show({
				msg : '您没有选中任何行！',
				title : '消息提示'
			});
		}
	}
</script>
</head>

<body class="easyui-layout">

	<div region="west" class="west" title="所属项目">
		<ul id="tree"></ul>
	</div>

	<div region="center" border="false">
		<div class="easyui-layout" fit="true">

			<!-- 任务信息面板 -->

			<div region="north" border="false" style="height: 280px">
				<table id="dg" class="easyui-datagrid" data-options="nowrap:false"
					toolbar="#tb">
				</table>

				<div style="padding: 5px;" id="tb">
					<a href="javascript:openTaskAddDialog()" class="easyui-linkbutton"
						id="btn_add" data-options="iconCls:'icon-add'" plain="true"
						disabled="true">添加</a> <a href="javascript:openTaskModifyDialog()"
						class="easyui-linkbutton" id="btn_edit"
						data-options="iconCls:'icon-edit'" plain="true">修改</a> <a
						href="javascript:deleteTask()" class="easyui-linkbutton"
						id="btn_remove" data-options="iconCls:'icon-remove'" plain="true">删除</a>
					<a href="javascript:searchTask()" class="easyui-linkbutton"
						id="btn_search" data-options="iconCls:'icon-search'" plain="true">查询</a>
				</div>

				<div id="dlg" class="easyui-dialog"
					style="width: 480px; height: 360px; padding: 10px 20px;"
					closed="true" buttons="#dlg-buttons">
					<form id="fm" method="post">
						<table>
							<tr>
								<td>所属项目：<input type="hidden" name="pmsProject.pid"
									id="pid" value="" /></td>
								<td><input type="text" name="pname" id="pname"
									class="easyui-validatebox" value="" disabled="disabled"></td>
							</tr>
							<tr>
								<td valign="top">任务名称：</td>
								<td><textarea rows="2" cols="30" name="tname" id="tname"
										class="easyui-validatebox" required="true"></textarea></td>
							</tr>
							<tr>
								<!-- 定义下拉框：任务责任人只能是该组成员 -->
								<td>任务责任人：</td>
								<td><input id="userid" name="sysUser.userid"
									class="easyui-combobox" width="50px" required="true"></td>
							</tr>
							<tr>
								<td>任务下达时间：</td>
								<td><input id="tdate1" type="text" name="tdate1"></input></td>
							</tr>
							<tr>
								<td>任务工期（天）：</td>
								<td><input type="text" name="tworkday" id="tworkday"></input></td>
							</tr>
							<tr>
								<td valign="top">备注：</td>
								<td><textarea rows="5" cols="30" name="tremark"
										id="tremark" class="easyui-validatebox"></textarea></td>
							</tr>
							<tr>
								<td>排序码：</td>
								<td><input type="text" name="tsortkey" id="tsortkey"
									required="true"></input></td>
							</tr>
						</table>
					</form>
				</div>

				<div id="dlg-buttons">
					<a href="javascript:saveTask()" class="easyui-linkbutton"
						iconCls="icon-ok">保存</a> <a href="javascript:closeTaskDialog()"
						class="easyui-linkbutton" iconCls="icon-cancel">关闭</a>
				</div>
			</div>

			<!-- 任务项信息面板 -->

			<div region="center" border="false">
				<table id="dg2" class="easyui-datagrid" data-options="nowrap:false"
					toolbar="#tb2">
				</table>
				<div style="padding: 5px;" id="tb2">
					<a href="javascript:openTaskAddDialog2()" class="easyui-linkbutton"
						id="btn_add2" data-options="iconCls:'icon-add'" plain="true">添加</a>
					<a href="javascript:openTaskModifyDialog2()"
						class="easyui-linkbutton" id="btn_edit2"
						data-options="iconCls:'icon-edit'" plain="true">修改</a> <a
						href="javascript:deleteTask2()" class="easyui-linkbutton"
						id="btn_remove2" data-options="iconCls:'icon-remove'" plain="true">删除</a>
				</div>

				<div id="dlg2" class="easyui-dialog"
					style="width: 420px; height: 270px; padding: 10px 20px;"
					closed="true" buttons="#dlg-buttons2">
					<form id="fm2" method="post">
						<table>
							<tr>
								<td>任务项名称：<input type="hidden" name="pmsTask.tid" id="tid2"
									value="" /></td>
								<td><input type="text" name="iname" id="iname"
									class="easyui-validatebox" required="true"></input></td>
							</tr>
							<tr>
								<td valign="top">具体要求：</td>
								<td><textarea rows="5" cols="30" name="irequirement"
										id="irequirement" class="easyui-validatebox" required="true"></textarea></td>
							</tr>
							<tr>
								<td>分值：</td>
								<td><input type="text" name="iscore" id="iscore"
									class="easyui-validatebox" required="true"></input></td>
							</tr>
							<tr>
								<td>排序码：</td>
								<td><input type="text" name="isortkey" id="isortkey"
									required="true"></input></td>
							</tr>
						</table>
					</form>
				</div>

				<div id="dlg-buttons2">
					<a href="javascript:saveTask2()" class="easyui-linkbutton"
						iconCls="icon-ok">保存</a> <a href="javascript:closeTaskDialog2()"
						class="easyui-linkbutton" iconCls="icon-cancel">关闭</a>
				</div>
			</div>

		</div>
	</div>

</body>
</html>