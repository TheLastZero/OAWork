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
	var datagrid2;

	$(function() {

		// 实例化数据网格
		datagrid2 = $("#dg2").datagrid({
			url : "${pageContext.request.contextPath}/MyTaskItemSelect2.do",
			//pagination : true,
			rownumbers : true,
			//pageSize : 10,
			//pageList : [ 5, 10, 15, 20 ],
			fit : true,
			fitColumns : true,
			columns : [ [ {
				field : 'pname',
				title : '所属项目',
				width : 150
			}, {
				field : 'tname',
				title : '任务名称',
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
			} ] ]
		});
	});
</script>
</head>

<body class="easyui-layout">

	<!-- 任务项信息面板 -->

	<div region="center" border="false">
		<table id="dg2" class="easyui-datagrid" data-options="nowrap:false">
		</table>
	</div>

</body>
</html>