<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Battery Info Page</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/jquery-easyui-1.3.3/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/jquery-easyui-1.3.3/themes/icon.css">
<script type="text/javascript" src="${pageContext.request.contextPath}/static/jquery-easyui-1.3.3/jquery.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/static/jquery-easyui-1.3.3/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/static/jquery-easyui-1.3.3/locale/easyui-lang-en.js"></script>
<script type="text/javascript">

 	function formatDataTitle(val, row){
 		if(val==null){
 			return "<font color='red'>Data has been deleted!</font>";
 		}else{
 			return "<a target='_blank' href='${pageContext.request.contextPath}/public/batteryData/details/"+val+".html'>"+val+"</a>";
 		}
 	}
 	
 	function formatState(val, row){
 		if(val==0){
 			return "Not yet reviewed!"
 		}else{
 			return "Review passed!"
 		}
 	}
 	
 	function formatTime(val, row){
 		return row.latestBatteryData.timestp;
 	}
 	
 	function formatChargeCurrent(val, row){
 		return row.latestBatteryData.ch_cur;
 	}
 	
 	function formatDischargeCurrent(val, row){
 		return row.latestBatteryData.dis_cur;
 	}
 	
 	function formatBatteryStatus(val, row){
 		return row.latestBatteryData.battery_status;
 	}
 	
 	function formatSoC(val, row){
 		return row.latestBatteryData.stateofcharge;
 	}
 	
 	function formatTemperature(val, row){
 		return row.latestBatteryData.temperature;
 	}
 	
 	function connect(){
		$.post("${pageContext.request.contextPath}/public/system/connect.do", function(result){
			if(result.success){
				$.messager.alert("System","Connection succeed!");
			}else{
				$.messager.alert("System","Connection Failed!");
			}
		},"json");
	}
 	
</script>
</head>
<body style="margin: 1px">
<table id="dg" title="Battery Info" class="easyui-datagrid" 
  fitColumns="true" pagination="true" rownumbers="true"
  url="${pageContext.request.contextPath}/public/battery/list.do" fit="true" toolbar="#tb">
  <thead>
    <tr>
  		<th colspan="3"  width="200" align="center">Battery Info</th>
  		<th colspan="6"  width="200" align="center">Latest Data</th>    	
  	</tr>
  	<tr>
  		<th field="id" width="20" align="center">ID</th>
  		<th field="batteryName" width="200" align="center">Battery Name</th>
  		<th field="batteryInfo" width="100" align="center">Battery Info</th>
  		<th field="latestBatteryData.timestp" width="100" align="center" formatter="formatTime">Time</th> 		
  		<th field="latestBatteryData.ch_cur" width="200" align="center" formatter="formatChargeCurrent">Charge Current</th>
  		<th field="latestBatteryData.dis_cur" width="200" align="center" formatter="formatDischargeCurrent">Discharge Current</th>
  		<th field="latestBatteryData.battery_status" width="200" align="center" formatter="formatBatteryStatus">Battery Status</th>
  		<th field="latestBatteryData.stateofcharge" width="200" align="center" formatter="formatSoC">SoC(%)</th>
  		<th field="latestBatteryData.temperature" width="100" align="center" formatter="formatTemperature">Temperature(C)</th>
  	</tr>

  </thead>
</table>
<div id="tb">
	<div>
		<a href="javascript:connect()" class="easyui-linkbutton" plain="true">Connect CAN-BUS</a>
	</div>
</div>
</body>
</html>