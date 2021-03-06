<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>ITU BMS Administration Page-Powered by ITU</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/jquery-easyui-1.3.3/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/jquery-easyui-1.3.3/themes/icon.css">
<script type="text/javascript" src="${pageContext.request.contextPath}/static/jquery-easyui-1.3.3/jquery.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/static/jquery-easyui-1.3.3/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/static/jquery-easyui-1.3.3/locale/easyui-lang-en.js"></script>
<script type="text/javascript">

function openTab(text,url,iconCls){
	if($("#tabs").tabs("exists",text)){
		$("#tabs").tabs("select",text);
	}else{
		var content="<iframe frameborder=0 scrolling='auto' style='width:100%;height:100%' src='/public/"+url+"'></iframe>";
		$("#tabs").tabs("add",{
			title:text,
			iconCls:iconCls,
			closable:true,
			content:content
		});
	}
}

function openPasswordModifyDialog(){
	$("#dlg").dialog("open").dialog("setTitle","Change Password");
	url="${pageContext.request.contextPath}/user/modifyPassword.do";
}

function modifyPassword(){
	$("#fm").form("submit",{
		url:url,
		onSubmit:function(){
			var newPassword=$("#newPassword").val();
			var newPassword2=$("#newPassword2").val();
			if(!$(this).form("validate")){
				return false;
			}
			if(newPassword!=newPassword2){
				$.messager.alert("System","New password not match!");
				return false;
			}
			return true;
		},
		success:function(result){
			var result=eval('('+result+')');
			if(result.success){
				$.messager.alert("System","Password changed!");
				resetValue();
				$("#dlg").dialog("close");
			}else{
				$.messager.alert("System","Change password failed!");
				return;
			}
		}
	});
}

function closePasswordModifyDialog(){
	resetValue();
	$("#dlg").dialog("close");
}

function resetValue(){
	$("#newPassword").val("");
	$("#newPassword2").val("");
}

function refreshSystem(){
	$.post("${pageContext.request.contextPath}/public/refreshSystem.do",{},function(result){
		if(result.success){
			$.messager.alert("System", "System cache refreshed!");
		}else{
			$.messager.alert("System", "Refresh system cache failed!");
		}
	},"json");
}

function logout(){
	$.messager.confirm("System", "Please confirm to logout from system!", function(r){
		if(r){
			window.location.href="${pageContext.request.contextPath}/user/logout.do";
		}
	});
}

</script>
</head>
<body class="easyui-layout">
<div region="north" style="height: 140px;background-color: #ffffff">
	<table style="padding: 5px" width="100%">
		<tr>
			<td width="50%">
			<img alt="logo" src="/static/images/bms_logo.png">
			</td>
			<td valign="bottom" align="right" width="50%">
				<font size="5">&nbsp;&nbsp;<strong>Welcome：</strong>${currentUser.userName }</font>
			</td>
		</tr>
	</table>
</div>

<div region="center">
	<div class="easyui-tabs" fit="true" border="false" id="tabs">
		<div title="Home" data-options="iconCls:'icon-home'">
			<div align="center" style="padding-top: 100px"><font color="red" size="10">Welcome</font></div>
		</div>
	</div>
</div>

<div region="west" style="width: 200px" title="Menu" split="true">
	<div class="easyui-accordion" data-options="fit:true,border:false">
		<div title="Common Operations" data-options="selected:true,iconCls:'icon-item'" style="padding: 10px">
			<a href="javascript:openTab('View battery status','batteryInfo.jsp','icon-review')" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-review'" style="width: 150px">Batteries</a>
			<a href="javascript:openTab('View Errors','errorReview.jsp','icon-review')" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-review'" style="width: 150px">View New Errors</a>
			<a href="javascript:openTab('View Alarms','alarmReview.jsp','icon-review')" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-review'" style="width: 150px">View New Alarms</a>
		</div>

		<div title="Manage Errors"  data-options="iconCls:'icon-plgl'" style="padding:10px">
			<a href="javascript:openTab('View Errors','errorReview.jsp','icon-review')" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-review'" style="width: 150px">View New Errors</a>
			<a href="javascript:openTab('Manage Errors','errorManage.jsp','icon-plgl')" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-plgl'" style="width: 150px;">Manage Errors</a>
		</div>
				

		<div title="Manage Alarms"  data-options="iconCls:'icon-plgl'" style="padding:10px">
			<a href="javascript:openTab('View Alarms','alarmReview.jsp','icon-review')" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-review'" style="width: 150px">View New Alarms</a>
			<a href="javascript:openTab('Manage Alarms','alarmManage.jsp','icon-plgl')" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-plgl'" style="width: 150px;">Manage Alarms</a>
		</div>
		
		
		<div title="Manage System"  data-options="iconCls:'icon-system'" style="padding:10px">
			<a href="javascript:openPasswordModifyDialog()" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-modifyPassword'" style="width: 150px;">Change Password</a>
			<a href="javascript:refreshSystem()" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-refresh'" style="width: 150px;">Refresh System</a>
			<a href="javascript:logout()" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-exit'" style="width: 150px;">Log out</a>
		</div>
	</div>
</div>


<div region="south" style="height: 25px;padding: 5px" align="center">
	Copyright © 2017 www.itu.edu
</div>
<div id="dlg" class="easyui-dialog" style="width:400px; height:200px; padding:10px 20px" closed="true" buttons="#dlg-buttons">
	<form id="fm" method="post">
		<table cellspacing="8px">
			<tr>
				<td>User Name:</td>
				<td>
					<input type="text" id="userName" name="userName" value="${currentUser.userName}" readonly="readonly" style="width:200px"/>	
				</td>
			</tr>
			<tr>
				<td>New Password:</td>
				<td>
					<input type="password" id="newPassword" name="newPassword" class="easyui-validatebox" required="true" style="width:200px"/>	
				</td>
			</tr>
			<tr>
				<td>Confirm Password:</td>
				<td>
					<input type="password" id="newPassword2" name="newPassword2" class="easyui-numberbox" required="true" style="width: 200px"/>
				</td>
			</tr>
		</table>
	</form>
</div>
<div id="dlg-buttons">
	<a href="javascript:modifyPassword()" class="easyui-linkbutton" iconCls="icon-ok">Save</a>
	<a href="javascript:closePasswordModifyDialog()" class="easyui-linkbutton" iconCls="icon-cancel">Cancel</a>
</div>

</body>
</html>