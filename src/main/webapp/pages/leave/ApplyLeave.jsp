<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'ApplyLeave.jsp' starting page</title>
    
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
			
			/*
				设置开始日期默认值为当前时间
			*/
			var now = new Date();
			
			/* 
				开始日期改变的时候，获取开始日期和，请假天数，计算结束日期的值
			*/
			$('#timeStart').datebox({
				editable:false,//禁止开始日期手动输入
				value:now.toDateString(),
	            onChange: function (date) {
	            	geTimeEnd();
	            }
	        });
	        
	        geTimeEnd();
	        
	        /*
	        	请假天数改变的时候，获取开始日期和，请假天数，计算结束日期的值
	        */
			$('#timeUsed').combobox({
			    onChange:function(newValue,oldValue){
			       geTimeEnd();
			    }
			});
			
			/*
				上午下午单选的值改变的时候，请假天数改变的时候，获取开始日期和，请假天数，计算结束日期的值
			*/
			$("input[name='ap']").on("click",function(){
				geTimeEnd();
			});
			
			
			/*
				计算请假结束日期的方法
			*/
			function geTimeEnd(){
					var timeStr = $('#timeStart').datebox('getValue');
	            	
            		//1、将字符串转换为当前日期
	            	var timeStart = new Date(timeStr);
	            	
	            	//2、获取请假天数
	            	var timeUsed = $('#timeUsed').datebox('getValue');
	            	
					if(isNaN(timeUsed)){
						alert("请假天数必须为数字,小数部分只代表半天！");
						//将请假天数和结束日期清空
						$("#timeUsed").textbox("setValue","");
						$("#timeEnd").textbox("setValue","");
						return false;
					}
	            	
	            	//3、获取上午还是下午
	            	var ap = $("input[name='ap']:checked").val();
	            	
	            		
            		var tu = timeUsed.split(".");//分割非小数部分，和小数部分
            		
            		var t_s = timeStart.getTime();//获取开始时间戳毫秒数
            		
            		if(tu[1] == undefined){
            			//alert("没有小数");
 						timeStart.setTime(t_s+1000 * 60 * 60 * 24*tu[0]);
            			
            			$("#timeEnd").textbox("setValue", timeStart.toLocaleDateString()+" "+ap);
            		}else{
            			//alert("有小数");
            			
            			//判断上午还是下午请的假
            			if(ap=="上午"){//上午请的假，那么请假日期截止到下午
            				timeStart.setTime(t_s+1000 * 60 * 60 * 24*tu[0]);
            			
            				$("#timeEnd").textbox("setValue", timeStart.toLocaleDateString()+" 下午");
            			}else if(ap=="下午"){//下午请的假，那么请假日期截止到上午
            				timeStart.setTime(t_s+1000 * 60 * 60 * 24*(parseInt(tu[0])+1));
            			
            				$("#timeEnd").textbox("setValue", timeStart.toLocaleDateString()+" 上午");
            			}
            			
            			
            		}
			}
			
			/*
				点击提交请假表时
			*/
			$("#submit").on("click",function(){
			
			   var timeUsed = $('#timeUsed').combobox('getValue');
			   
			   //判断请假天数是否为0
			   if(parseFloat(timeUsed)==0){
			   		alert("请假天数不得为0");
			   		return false;
			   }
			   
			   //判断请假天数的小数部分是否为0.5，不是的话就改为0.5
		       var tu = timeUsed.split(".");
		       
		       if(tu[1] != undefined){//有小数时
		       	   if(tu[1]!=5 && tu[1]!=0){
			       		$("#timeUsed").textbox("setValue", parseInt(tu[0])+0.5);
			       		alert("小数部分当前只能为半天，已经自动调整为0.5");
			       }
		       }
				
				$.ajax({
					url:"${pageContext.request.contextPath }/leave/saveOrUpdate",
					type:"POST",
					data:$("#leaveForm").serialize(),
					success:function(result){
						console.log(result);
						//保存能成功后，跳转到当前用户的请假list页面上	
						
						if(result.code==200){//处理失败
							alert(result.extend.leaveMessage);
						}else if(result.code==100){
							//获取当前用户的请假历史记录
							window.location.href = "${pageContext.request.contextPath }/pages/leave/UserLeaveList.jsp";
						}
							
					}
					
				});
				
				
			});
			
			
			/*
				getUserLeaveList点击跳转历史请假页面
			*/
			$("#getUserLeaveList").on("click",function(){
				window.location.href = "${pageContext.request.contextPath }/pages/leave/UserLeaveList.jsp";
			});
			
			
			//如果是修改请假信息，则将请假类型，开始日期，上下午和请假天数赋值为查询到的数据
			if(${requestScope.leave !=undefined}==true){
				
				var dStart = new Date("${requestScope.leave.timeStart}");
				var dEnd = new Date("${requestScope.leave.timeEnd}");
				$("#leaveType").textbox("setValue","${requestScope.leave.leaveType}" );
				$("#timeStart").textbox("setValue",dStart.getFullYear()+"-"+(dStart.getMonth()+1)+"-"+dStart.getDate());
				
				//判断上下午单选框
				$('input:radio[name="ap"]').removeAttr('checked');
				
                 var h = dStart.getHours();
                 var ap = 0;
                 if(h==8){
                 	ap = 0;
                 }else if(h==14){
                 	ap = 1;
                 }
				$('input:radio[name="ap"]:eq('+ap+')').prop('checked', 'checked');
				
				
				$("#timeUsed").textbox("setValue", "${requestScope.leave.timeUsed}");
				
				geTimeEnd();
	        
			}
		});
	
	</script>
	
	
  </head>
  
  <body>
  	<div style="text-align: right;">
  		<a id="getUserLeaveList" href="javascript:void(0)" class="easyui-linkbutton" >查看请假历史</a>
  	</div>
  	
  <center>
  
  
	    <div style="padding:3px 2px;border-bottom:1px solid #ccc">
	    	<h2>
	    		<c:if test="${requestScope.leave ==undefined }">
	    			申请请假
	    		</c:if>
	    		<c:if test="${requestScope.leave !=undefined }">
	    			更改请假
	    		</c:if>
	    	</h2>
	    </div>
	    
	    <div style="margin:20px 0;"></div>
		<div class="easyui-panel" title="请假信息" style="width:100%;max-width:400px;padding:30px 60px;">
			<form id="leaveForm" method="post">
				
				<!-- 如果为修改，那么这里保存leaveId -->
				<input type="hidden" name="leaveId" value="${requestScope.leave.leaveId}">
			
				<!-- 当前登录用户的id -->
				<input type="hidden" name="userId" value="${sessionScope.sysUser.userid}">
				<div style="margin-bottom:20px">
					<!-- 
							panelHeight="auto"去除多余的空白行
							data-options="editable:false" 禁止输入 
						-->
					<select class="easyui-combobox" name="leaveType" id="leaveType" label="请假类别：" style="width:100%" panelHeight="auto" data-options="editable:false">
						<option value="事假" selected="selected">事假</option>
						<option value="病假">病假</option>
					</select>
					
				</div>
				<div style="margin-bottom:20px">
					<input class="easyui-datebox" name="timeStarts" id="timeStart"
    					data-options="required:true,label:'开始日期：'" style="width: 100%">
					
				</div>
				<div>   
					<center>
				    <input type="radio"  name="ap" value="上午" checked="checked">上午
				    <input type="radio"  name="ap" value="下午">下午
				    </center>
				 </div>
				<br>
				<div style="margin-bottom:20px">
					<select class="easyui-combobox" name="timeUsed" id="timeUsed" label="请假天数：" style="width:100%" panelHeight="auto">
						    <option value="0.5">0.5</option>
							<option value="1">1</option>
							<option value="1.5">1.5</option>
							<option value="2">2</option>
					</select>
				</div>
				<div style="margin-bottom:20px">
					<input class="easyui-textbox" name="timeEnds" id="timeEnd" style="width:100%" data-options="label:'结束日期：',required:true" readonly="readonly">
				</div>
				
			</form>
			<div style="text-align:center;padding:5px 0">
				<a id="submit" href="javascript:void(0)" class="easyui-linkbutton" style="width:80px">提交</a>
			</div>
		</div>
	    
   </center>
  </body>
</html>
