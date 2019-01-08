<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'CheckDateOption.jsp' starting page</title>
    
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
			  		url: '${pageContext.request.contextPath }/checkDateOption/getCheckDateOptionList?year='+t[0]+'&mounth='+t[1]
			  	});	
		  	}
		  	
		  	getcdo();
		  	
		  	
		  	/*
		  		点击保存按钮时
		  		var xicx = $("#xicx").combobox("getValue");
		  	*/
		  	$("#save").on("click",function(){
		  		
		  		 var rows = $("#mydate").datagrid("getRows"); //这段代码是获取当前页的所有行，比如说我们的月份一般是5行，
				 for(var i=0;i<rows.length;i++)
				 { 
  					var count = Object.keys(rows[i].row).length;//获取子项个数，也就是每一行有多少个对象
  					//alert(count);
					 	 
					 	 
					 	 //判断当前处于哪一个对象，得到其对象，修改其对应am，pm的值也就是上下午是否上班的状态
					 	 if(rows[i].row.mon !=undefined){
					 	 	
					 	 	rows[i].row.mon.amStatus = $("#am"+rows[i].row.mon.daynum).val();//取对应am下拉框value
					 	 	rows[i].row.mon.pmStatus =  $("#pm"+rows[i].row.mon.daynum).val();//取对应pm下拉框value
					 	 	
					 	 } if(rows[i].row.tue != undefined ){
					 	 	rows[i].row.tue.amStatus = $("#am"+rows[i].row.tue.daynum).val();//取对应am下拉框value
					 	 	rows[i].row.tue.pmStatus =  $("#pm"+rows[i].row.tue.daynum).val();//取对应pm下拉框value
					 	 } if(rows[i].row.wed != undefined ){
					 	 	rows[i].row.wed.amStatus = $("#am"+rows[i].row.wed.daynum).val();//取对应am下拉框value
					 	 	rows[i].row.wed.pmStatus =  $("#pm"+rows[i].row.wed.daynum).val();//取对应pm下拉框value
					 	 } if(rows[i].row.thurs != undefined ){
					 	 	rows[i].row.thurs.amStatus = $("#am"+rows[i].row.thurs.daynum).val();//取对应am下拉框value
					 	 	rows[i].row.thurs.pmStatus =  $("#pm"+rows[i].row.thurs.daynum).val();//取对应pm下拉框value
					 	 } if(rows[i].row.fri != undefined ){
					 	 	rows[i].row.fri.amStatus = $("#am"+rows[i].row.fri.daynum).val();//取对应am下拉框value
					 	 	rows[i].row.fri.pmStatus =  $("#pm"+rows[i].row.fri.daynum).val();//取对应pm下拉框value
					 	 } if(rows[i].row.sat != undefined ){
					 	 	rows[i].row.sat.amStatus = $("#am"+rows[i].row.sat.daynum).val();//取对应am下拉框value
					 	 	rows[i].row.sat.pmStatus =  $("#pm"+rows[i].row.sat.daynum).val();//取对应pm下拉框value
					 	 } if(rows[i].row.sun != undefined ){
					 	 	rows[i].row.sun.amStatus = $("#am"+rows[i].row.sun.daynum).val();//取对应am下拉框value
					 	 	rows[i].row.sun.pmStatus =  $("#pm"+rows[i].row.sun.daynum).val();//取对应pm下拉框value
					 	 }
				 	 
				 }
				 
				 console.log(rows);
				 //JSON.stringify(rows)将复杂js类型转为json字符串
				 console.log(JSON.stringify(rows));
				 var json = JSON.stringify(rows)+"";
		  		
		  		$.ajax({
		  			url:"${pageContext.request.contextPath }/checkDateOption/saveOrUpdate",
		  			type:"POST",
		  			dataType:"json",      
                	contentType:"application/json",
		  			data:JSON.stringify(rows),
		  			success:function(result){
		  				console.log(result);
		  				
		  				if(result.code==100){
		  					$.messager.show({
								msg : '保存成功',
								title : '消息提示'
							});
							
							//保存成功后刷新数据网格
							$("#mydate").datagrid('reload');
		  				}else{
		  					$.messager.show({
								msg : '保存失败',
								title : '消息提示'
							});
		  				}
		  				
		  			}
		  		});
		  		
		  	});
		
		});
		
		/*
			根据传入的节点对象，返回对应的select字符串
		*/
		function getResultStr(o){
			
			var result = "<br><b style='font-size:18px;'>"+o.daynum+"</b><br><br>";
			//判断是否有节假日提示信息
			var hoilday;
			if(o.isHoliday==2){//是节假日
				hoilday = o.message;
			}else{
				hoilday = "&nbsp";
			}
			//判断上下班状态
			var am;
			var pm;
					if(o.amStatus=="0"){
					
						am = 'AM:<select class="easyui-combobox" name="am'+o.daynum+'" id="am'+o.daynum+'" style="width:100px" panelHeight="auto" data-options="editable:false">'+
		'							<option value="0" selected="selected">上班</option>'+
			'						<option value="1">休息</option>'+
			'					</select><br><br/>';
					}else if(o.amStatus==1){
						am = 'AM:<select class="easyui-combobox" name="am'+o.daynum+'" id="am'+o.daynum+'" style="width:100px;" panelHeight="auto" data-options="editable:false">'+
		'							<option value="0" >上班</option>'+
			'						<option value="1" selected="selected">休息</option>'+
			'					</select><br></br>';
					}
					
					
					if(o.pmStatus==0){
						pm = 'PM:<select class="easyui-combobox" name="pm'+o.daynum+'" id="pm'+o.daynum+'" style="width:100px" panelHeight="auto" data-options="editable:false">'+
'									<option value="0" selected="selected">上班</option>'+
			'						<option value="1">休息</option>'+
			'					</select><div style="color:red">'+hoilday+'</div>';
					}else if(o.pmStatus==1){
						pm = 'PM:<select class="easyui-combobox" name="pm'+o.daynum+'" id="pm'+o.daynum+'" style="width:100px" panelHeight="auto" data-options="editable:false">'+
		'							<option value="0" >上班</option>'+
			'						<option value="1" selected="selected">休息</option>'+
			'					</select><div style="color:red">'+hoilday+'</div>';
					}
				
					result =result+am+pm;
						
						
					
					return result;
		}
		
		
		/**
			初始化easyui的列的时候，
			formatter方法不能放在$(document).ready(function(){中
			因为这个方法是在页面加载的时候执行，$(document).ready(function(){是页面加载完成后执行
		**/
		
		function fmon(value,row,index){//星期一
			//console.log(row);
			
			for(var o in row.row){
				if(o=="mon"){
				
					return getResultStr(row.row.mon);
				}
			}
			
			//alert(value+"--"+row.row1+"--"+index);
			return "";
		}
		
		function ftue(value,row,index){//星期2
			//console.log(row);
			
			for(var o in row.row){
				if(o=="tue"){
					return getResultStr(row.row.tue);
				}
			}
			
			//alert(value+"--"+row.row1+"--"+index);
			return "";
		}
		
		function fwed(value,row,index){//星期3
			//console.log(row);
			
			for(var o in row.row){
				if(o=="wed"){
					return getResultStr(row.row.wed);
				}
			}
			
			return "";
		}
		
		function fthurs(value,row,index){//星期4
			//console.log(row);
			
			for(var o in row.row){
				if(o=="thurs"){
					return getResultStr(row.row.thurs);
				}
			}
			return "";
		}
		
		function ffri(value,row,index){//星期5
			//console.log(row);
			
			for(var o in row.row){
				if(o=="fri"){
					return getResultStr(row.row.fri);
				}
			}
			
			return "";
		}
		
		function fsat(value,row,index){//星期6
			//console.log(row);
			
			for(var o in row.row){
				if(o=="sat"){
					return getResultStr(row.row.sat);
				}
			}
			
			return "";
		}
		
		function fsun(value,row,index){//星期7
			//console.log(row);
			
			for(var o in row.row){
				if(o=="sun"){
					return getResultStr(row.row.sun);
				}
			}
			
			return "";
		}
		
		
	</script>
	
  </head>
  
<body class="easyui-layout">
	
	<div id="tb" style="padding:5px;height:auto">
		<div align="center">
			
			<input id="datetime1" class="easyui-datebox" style="width:100px;">
			
			<a href="javascript:void(0)" id="save" class="easyui-linkbutton" iconCls="icon-save">保存设置</a>
		</div>
	</div>
	
	<div align="center">
		<table  id="mydate"
				class="easyui-datagrid" style="width:80%;"
				url="" 
				title="请选择要编辑考勤的月份：" toolbar="#tb"
				singleSelect="true" fitColumns="true" >
			<thead>
				<tr>
					<th field="mon" width="60" align="center" formatter="fmon">星期一</th>
					<th field="tue" width="60" align="center" formatter="ftue">星期二</th>
					<th field="wed" width="60" align="center" formatter="fwed">星期三</th>
					<th field="thurs" width="60" align="center" formatter="fthurs">星期四</th>
					<th field="fri" width="60" align="center" formatter="ffri">星期五</th>
					<th field="sat" width="60" align="center" formatter="fsat">星期六</th>
					<th field="sun" width="60" align="center" formatter="fsun">星期天</th>
					
				</tr>
			</thead>
		</table>
	</div>
	
  </body>
 

</html>