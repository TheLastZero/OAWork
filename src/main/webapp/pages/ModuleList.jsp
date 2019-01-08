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

	var currentNodeid = 0;// 当前树节点ID，如果没有选择树节点，则置为0

	$(function() {

		// 实例化树形菜单
		$("#tree").tree({
			url : '${pageContext.request.contextPath }/ModuleSelect.do',
			loadFilter : function(selectedRows) {
				return convert(selectedRows);
			},
			onClick : function(node) {
				currentNodeid = node.id;

				//如果点击的是模块类别节点，则执行查询操作
				if (node.url == undefined) {
					$("#dg").datagrid({
						url : "${pageContext.request.contextPath }/ModuleSelect2.do?mpid=" + currentNodeid
					});
				}

				//if ($('#tree').tree('isLeaf', node.target)) { // 叶子节点
				//    $("#btn_add").linkbutton('disable');
				//} else {
				//    $("#btn_add").linkbutton('enable');
				//}
			}
		});

		// 实例化数据网格
		$("#dg").datagrid({
			url : "${pageContext.request.contextPath }/ModuleSelect2.do",// 加载的URL
			pagination : true,// 是否显示分页
			rownumbers : true,// 是否显示行号
			pageSize : 5,// 初始分页大小
			pageList : [ 5, 10, 15, 20 ],// 初始分页大小选择列表
			fit : true,
			fitColumns : true,// 真正的自动展开/收缩列的大小，以适应网格的宽度，防止水平滚动
			singleSelect: true,  //选中行唯一
			columns : [ [ {
				field : 'mid',
				title : '模块编号',
				width : 100
			}, {
				field : 'mpid',
				title : '模块父编号',
				width : 100
			}, {
				field : 'mname',
				title : '模块名称',
				width : 100,
				editor : {
					type : 'validatebox',
					options : {
						required : true
					}
				}
			}, {
				field : 'mpath',
				title : '模块路径',
				width : 100,
				editor : {
					type : 'validatebox',
					options : {}
				}
			}, {
				field : 'msortkey',
				title : '排序码',
				width : 100,
				editor : {
					type : 'numberbox',
					options : {
						min : 0,
						precision : 0, //小数分隔符之后的最大位数
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

					$.messager.confirm('确定', '您确定要删除吗', function(t) {
						if (t) {
							$.ajax({
								type : "post",
								url : "${pageContext.request.contextPath}/ModuleDel.do",
								data : {
									mid : selectedRows[0].mid
								},
								dataType : 'json',
								success : function(r) {
									if (r.success) {
										$("#dg").datagrid('acceptChanges');
										$("#tree").tree('reload');// 刷新左侧树结构
										$.messager.show({
											msg : r.msg,
											title : '消息提示'
										});
										rowEditor = undefined;
										$("#dg").datagrid('reload');
									} else {
										$("#dg").datagrid('beginEdit', rowEditor);
										$.messager.alert("消息提示", r.msg, 'error');
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
						url : "${pageContext.request.contextPath }/ModuleSelect2.do"
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
					url = "${pageContext.request.contextPath }/ModuleAdd.do";
				}
				if (updated.length > 0) {
					url = "${pageContext.request.contextPath }/ModuleUpd.do";
				}

				rowData.mpid = currentNodeid;// 往行数据对象rowData里附加一项数据

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
							$("#tree").tree('reload');// 刷新左侧树结构
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

	<div region="west" class="west" title="模块树列表">
		<ul id="tree"></ul>
	</div>

	<div region="center" border="false">
		<table id="dg"></table>
	</div>

</body>
</html>