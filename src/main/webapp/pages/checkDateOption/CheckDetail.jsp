<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'CheckDetail.jsp' starting page</title>
    
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
	
	<!-- 图表插件 -->
	<script src="${pageContext.request.contextPath}/js/echarts.js"></script>
	<script src="${pageContext.request.contextPath}/js/echarts-gl.js"></script>
	
	<script type="text/javascript">
		
		$(document).ready(function(){
			
			/*
				加载easyui日期月份控件
			*/
			//获取当前时间
			var now = new Date();
			//本来月份只需要+1即可，但是下面我们自定义了月份所以这里正确的月份需要加2设置的时候才是正常的
			var m = parseInt(now.getMonth())+2;
			if(m<10){
				m="0"+m;
			}
			
			//alert(now.getMonth());
			 $('#datetime1').datebox({  
			 	editable:false,//禁止开始日期手动输入
			 	onChange: function () {
			 		//获取当前选中的日期
			 		//var time = $('#datetime1').datebox('getValue');
	            	//alert(time);
	            	
	            	getcdo();
	            },
			 	value:now.getFullYear()+"-"+m,
		        onShowPanel : function() {// 显示日趋选择对象后再触发弹出月份层的事件，初始化时没有生成月份层  
		            span.trigger('click'); // 触发click事件弹出月份层  
		            if (!tds)  
		                setTimeout(function() {// 延时触发获取月份对象，因为上面的事件触发和对象生成有时间间隔  
		                    tds = p.find('div.calendar-menu-month-inner td');  
		                    tds.click(function(e) {  
		                        e.stopPropagation(); // 禁止冒泡执行easyui给月份绑定的事件  
		                        var year = /\d{4}/.exec(span.html())[0]// 得到年份  
		                        , month = parseInt($(this).attr('abbr'), 10) + 1; // 月份  
		                        $('#datetime1').datebox('hidePanel')// 隐藏日期对象  
		                        .datebox('setValue', year + '-' + month); // 设置日期的值  
		                    });  
		                }, 0);  
		        },  
		        parser : function(s) {// 配置parser，返回选择的日期  
		            if (!s)  
		                return new Date();  
		            var arr = s.split('-');  
		            return new Date(parseInt(arr[0], 10), parseInt(arr[1], 10) - 1, 1);  
		        },  
		        formatter : function(d) {  
		        	var month = d.getMonth();
		        	if(month<10){
		        		month = "0"+month;
		        	}
		            if (d.getMonth() == 0) {  
		                return d.getFullYear()-1 + '-' + 12;  
		            } else {  
		                return d.getFullYear() + '-' + month;  
		            }  
		        },// 配置formatter，只返回年月  
		    });  
		    var p = $('#datetime1').datebox('panel'), // 日期选择对象  
		    tds = false, // 日期选择对象中月份  
		    span = p.find('span.calendar-text'); // 显示月份层的触发控件  
		  	
		  	//获取当前年月时间，并且加载考勤信息
		  	function getcdo(){
		  		var time = $('#datetime1').datebox('getValue');
			  	var t = time.split("-");
			  	$('#mydate').datagrid({ 
			  		rowStyler: function(index, row) {
				         //设置行背景色为白色
				        return 'background-color:white;';
				    },
			  		url: '${pageContext.request.contextPath }/checkDetail/getDetailListByPage?year='+t[0]+'&mounth='+t[1]
			  	});	
			  	
			  	//刷新图表
			  	$.ajax({
			  		url:"${pageContext.request.contextPath }/checkDetail/removeMoneyGraph?year="+t[0]+"&mounth="+t[1],
			  		type:"POST",
			  		success:function(result){
			  			console.log(result);
			  			
			  			var dx = new Array();
						var dy = new Array();
			  			
			  			$.each(result.extend.salaryList,function(index,v){
			  				
			  				dx[index] = v.realname;
							dy[index] = parseInt(v.moneyAbsent)+parseInt(v.moneyLater)+parseInt(v.moneyThing)+parseInt(v.moneyIll);
			  				
			  			});
			  			
			  			if(dx.length!=0){
			  				lodingGraph(dx,dy);
			  			}
			  			
			  		}
			  	});
			  	
		  	}
		  	
		  	//根据当前月份加载一下数据
			getcdo();
		
			
			/*
				提交按钮被点击时
			*/
			$("#sub").on("click",function(){	
				//校验文件格式是否正确
				var file = $("#file").val();
                if (file == "") {
                    alert("请选择要上传的文件");
                    return false
                } else {
                    //检验文件类型是否正确
                    var exec = (/[.]/.exec(file)) ? /[^.]+$/.exec(file.toLowerCase()) : '';
                    if (exec != "xlsx" && exec!="xls") {
                        alert("文件格式不对，请上传Excel文件!");
                        return false;
                    }
                }
				
				
				/*
					上传文件
				*/
				//获取要上传的文件
				var formData = new FormData($( "#cdcUpload" )[0]);  
		        var ajaxUrl = "${pageContext.request.contextPath }/checkDetail/cdcFileUpload";
		        //alert(ajaxUrl);
		        //$('#uploadPic').serialize() 无法序列化二进制文件，这里采用formData上传
		        //需要浏览器支持：Chrome 7+、Firefox 4+、IE 10+、Opera 12+、Safari 5+。
		        $.ajax({
		            type: "POST",
		            //dataType: "text",
		            url: ajaxUrl,
		            data: formData,
		            async: false,  
		            cache: false,  
		            contentType: false,  
		            processData: false,
		            beforeSend: function () {
						ajaxLoading();//弹出正在加载层
					},
		            success: function (result) {
		           		 console.log(result);
		           		 
		           		 ajaxLoadEnd();//移除正在加载样式
		           		 
		           		 if(result.code==100){
		           		 	$.messager.show({
								msg : '上传成功',
								title : '消息提示'
							});
							
							//刷新数据网格，和图表
							getcdo();
							
							
		           		 }else{
		           		 	$.messager.show({
								msg : result.extend.excelMessage,
								title : '消息提示'
							});
							
		           		 }
		            }
		           
		        });
			});
			
			function ajaxLoading(){   
			    $("<img class=\"datagrid-mask\" style='position: fixed;z-index: 9999;' src='${pageContext.request.contextPath }/static/img/loading3.gif'></img>").css({display:"block",width:"100%",height:$(window).height()}).appendTo("#tool");   
			 }   
			 function ajaxLoadEnd(){
			 		setTimeout(function(){
			 			$(".datagrid-mask").remove();		
					},1000); 
			 }
			 
			  
			 /*
			 	点击导出考勤明细表Excel的操作
			 */
			 $("#checkDetailExcel").on("click",function(){
			 
			 	var time = $('#datetime1').datebox('getValue');
			  	var t = time.split("-");
			 	window.location.href = "${pageContext.request.contextPath }/checkDetail/downloadcCeckDetailExcel?year="+t[0]+"&mounth="+t[1];
			 });
			 
			 
			 /*
			 	点击导出考勤月报表Excel的操作
			 */
			  $("#checkDetailMounthExcel").on("click",function(){
			 
			 	//alert();
			 	var time = $('#datetime1').datebox('getValue');
			  	var t = time.split("-");
			  	window.location.href = "${pageContext.request.contextPath }/checkDetail/downloadCheckDetailMounthExcel?year="+t[0]+"&mounth="+t[1];
			 	//window.location.href = "${pageContext.request.contextPath }/checkDetail/downloadCheckDetailMounthExcel?year="+t[0]+"&mounth="+t[1];
			 });
			 
			 /*
			 	点击导出薪酬月报表Excel的操作
			 */
			 $("#moneyMounthExcel").on("click",function(){
			 
			 	//alert();
			 	var time = $('#datetime1').datebox('getValue');
			  	var t = time.split("-");
			  	window.location.href = "${pageContext.request.contextPath }/checkDetail/downloadMoneyMounthExcel?year="+t[0]+"&mounth="+t[1];
			 	//window.location.href = "${pageContext.request.contextPath }/checkDetail/downloadCheckDetailMounthExcel?year="+t[0]+"&mounth="+t[1];
			 });
			 
			 
			 /*
			 	加载图表信息
			 */
			 
			function lodingGraph(dx,dy){
				var dom = document.getElementById("removeSalaryGraph");
				var myChart = echarts.init(dom);
				var app = {};
				option = null;
				app.title = '坐标轴刻度与标签对齐';
				
				option = {
				    color: ['#3398DB'],
				    tooltip : {
				        trigger: 'axis',
				        axisPointer : {            // 坐标轴指示器，坐标轴触发有效
				            type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
				        }
				    },
				    grid: {
				        left: '3%',
				        right: '4%',
				        bottom: '3%',
				        containLabel: true
				    },
				    xAxis : [
				        {
				            type : 'category',
				            data : dx,
				            axisTick: {
				                alignWithLabel: true
				            }
				        }
				    ],
				    yAxis : [
				        {
				            type : 'value'
				        }
				    ],
				    series : [
				        {
				            name:'罚款金额',
				            type:'bar',
				            barWidth: '60%',
				            data:dy
				        }
				    ]
				};
				;
				if (option && typeof option === "object") {
				    myChart.setOption(option, true);
				}
			}
			 
			
			 
			 
		});
		
			
		
		function checkId(value,row,index){
			
			return row.checkDetail.checkId;
		}
		
		function checkCode(value,row,index){
			
			return row.checkDetail.checkCode;
		}
		
		function checkNum(value,row,index){
			
			return row.checkDetail.checkNum;
		}
		
		function checkType(value,row,index){
			
			return row.checkDetail.checkType;
		}
	</script>

  </head>
  
  <body class="easyui-layout">
  
	<div id="tool" data-options="region:'center'" style="background:#eee;">
		<!-- 工具栏 -->
    	<div id="tb" style="height:auto;">
			<div style="margin-top:5px">
				<form id="cdcUpload" method="POST" enctype="multipart/form-data">
			    	<input id="file" type="file" name="file">
			    	<input id="sub" type="button" value="上传">
			    	<a id="checkDetailExcel" href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" plain="true">考勤明细报表导出</a>
			    	<a id="checkDetailMounthExcel" href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" plain="true">考勤月报表导出</a>
			    	<a id="moneyMounthExcel" href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" plain="true">月薪酬报表导出</a>
			    	 <center>
			    		选择考勤月份：<input id="datetime1" class="easyui-datebox" style="width:100px;">
			    	</center>
			    </form>
			    
			   
			</div>
		</div>
		
		<!-- 数据网格 -->
		<table id="mydate" class="easyui-datagrid" style="height: 100%;"
				url="" 
				title="考勤明细表" toolbar="#tb"
				singleSelect="true" fitColumns="true"
				rownumbers="true" pagination="true">
			<thead>
				<tr>
					<th field="checkDetail.checkId" width="60" formatter="checkId">记录编号</th>
					<th field="dname" width="60">部门名称</th>
					<th field="realname" width="60">员工姓名</th>
					<th field="checkCode" width="60" formatter="checkCode">登记号码</th>
					<th field="timeCheck" width="60" >打卡日期时间</th>
					<th field="checkNum" width="60" formatter="checkNum">机器号</th>
					<th field="checkType" width="60" formatter="checkType">对比方式</th>
				</tr>
			</thead>
		</table>
		
    </div>
	
    <div data-options="region:'south',title:'罚款图表分析',split:true" style="height:60%;">
    	
    	<center>
    		<div id="removeSalaryGraph" style="height: 100%;width: 60%;">
    		
    		</div>
    	</center>
    	
    	
    </div>
    
</body>
</html>
