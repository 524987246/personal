<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE HTML>
<html>
<head>
<meta charset="utf-8">
<meta name="renderer" content="webkit|ie-comp|ie-stand">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport"
	content="width=device-width,initial-scale=1,minimum-scale=1.0,maximum-scale=1.0,role-scalable=no" />
<meta http-equiv="Cache-Control" content="no-siteapp" />
<link rel="Bookmark" href="/favicon.ico">
<link rel="Shortcut Icon" href="/favicon.ico" />
<!--[if lt IE 9]>
<script type="text/javascript" src="lib/html5shiv.js"></script>
<script type="text/javascript" src="lib/respond.min.js"></script>
<![endif]-->
<%@ include file="/WEB-INF/include/newtitlehead.jsp"%>
<shiro:hasPermission name="sys:role:update">
	<meta id="edit" content="1" />
</shiro:hasPermission>
<shiro:hasPermission name="sys:role:save">
	<meta id="save" content="1" />
</shiro:hasPermission>
<title>角色修改</title>
</head>
<body>
	<article class="page-container">
		<form class="form form-horizontal" id="form-main" method="post">
			<input type="hidden" name="id" id="id" value="${sysRole.id }">
			<div class="row cl">
				<label class="form-label col-xs-4 col-sm-2">名称：</label>
				<div class="formControls col-xs-8 col-sm-9">
					<input type="text" class="input-text required"
						value="${sysRole.name }" placeholder="名称" id="name" name="name">
					<span class="c-red">*</span>
				</div>
			</div>
			<div class="row cl">
				<label class="form-label col-xs-4 col-sm-2">状态：</label>
				<div class="formControls col-xs-8 col-sm-9">
					<input type="radio" name="isemploy" value="1" checked="true">正常
					<input type="radio" name="isemploy" value="3">禁用 &nbsp;<span
						class="c-red">*</span>
				</div>
			</div>
			<div class="row cl">
				<label class="form-label col-xs-4 col-sm-2">菜单：</label>
				<div class="formControls col-xs-8 col-sm-9">
					<ul id="menu_ztree" class="ztree"></ul>
				</div>
			</div>
			<div class="row cl">
				<div class="col-xs-8 col-sm-9 col-xs-offset-4 col-sm-offset-2">
					<button onClick="save();" class="btn btn-primary radius"
						type="button">
						<i class="Hui-iconfont">&#xe632;</i> 确定
					</button>
					<button onClick="removeIframe();" class="btn btn-default radius"
						type="button">&nbsp;&nbsp;取消&nbsp;&nbsp;</button>
				</div>
			</div>
		</form>
	</article>
	<!--请在下方写此页面业务相关的脚本-->
	<script type="text/javascript"
		src="${ctx}/statis/newjsp/lib/jquery.validation/1.14.0/jquery.validate.js"></script>
	<script type="text/javascript"
		src="${ctx}/statis/newjsp/lib/jquery.validation/1.14.0/validate-methods.js"></script>
	<script type="text/javascript"
		src="${ctx}/statis/newjsp/lib/jquery.validation/1.14.0/messages_zh.js"></script>
	<!-- 属性选择框文本 -->
	<script type="text/javascript">
		var sysRole = {
			id : "${sysRole.id }",
			isemploy : "${sysRole.isemploy }",
			list : stringToArray("${sysRole.list }")
		}
		var form_url = "";
	
		if (sysRole.id != null && sysRole.id != "") {
			if ($("#edit").attr("content") != null) {
				form_url = "Reception/sys/role/update.html";
			}
		} else {
			if ($("#save").attr("content") != null) {
				form_url = "Reception/sys/role/save.html";
			}
		}
	</script>
	<script type="type= text/template" id="menu_select_template">
		<div class="content-1">
		<input  type="text" class="input-xlarge" placeholder="关键字" style="width: 60%;line-height: inherit;height: 30px; margin-bottom: 0px;"> <input
		type="button" onclick="zTreeMenuByName()" style="width: 30%;height: 30px;" value="查询"  class="btn btn-primary">
		</div>
		<div class="content-1">
		<ul id="menu_ztree" class="ztree"></ul>
		</div>
	</script>
	<script type="text/javascript" src="${ctx}/js/zTreeObject.js"></script>
	<script type="text/javascript" src="${ctx}/viewjs/sys/role-one.js"></script>
	<!--/请在上方写此页面业务相关的脚本-->
</body>
</html>