<%@ tag description="Template Base" pageEncoding="UTF-8" %>
<%@ attribute name="scripts" required="false" fragment="true" %>
<%@ attribute name="styles" required="false" fragment="true" %>
<%@ attribute name="title" required="true" %>
<%@ attribute name="init" required="true" %>
<%@ attribute name="openMenu" required="false" %>

<%@ taglib uri="http://gumga.com.br/jsp/tags" prefix="g" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<fmt:bundle basename="messages_template"></fmt:bundle>
<jsp:useBean id="gumgaProperties" class="gumga.framework.presentation.GumgaSystemProperties" scope="application" />
<c:set var="path" scope="application" value="${empty gumgaProperties.path ? pageContext.request.contextPath : gumgaProperties.path}" />


<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<meta charset="utf-8" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="viewport" content="initial-scale=1, maximum-scale=1, user-scalable=no" />

<title><fmt:message key="app.title" /> - ${title}</title>

<link rel="stylesheet" href="<c:url value='${gumgaProperties.path}/static/styles/main.css' />" />
<link rel="stylesheet" href="<c:url value='${gumgaProperties.path}/static/styles/gumga.css' />" />

<jsp:invoke fragment="styles" />

</head>
<body class="gumga-offcanvas">

	<div class="navbar navbar-gumga navbar-fixed-top" role="navigation">
		<div class="navbar-header">
			<button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
				<span class="sr-only">Toggle navigation</span> 
				<span class="icon-bar"></span> 
				<span class="icon-bar"></span>  
				<span class="icon-bar"></span>
			</button>
			<button class="gumga-offcanvas-reveal">Exibir Menu</button>
			<a class="navbar-brand" href="#"><fmt:message key="app.title" /></a>
		</div>
		<div class="collapse navbar-collapse">
			<ul class="nav navbar-nav">
				<!--
					Aqui deve ir o menu de topo
					 
					<li class="active"><a href="#">Home</a></li>
					<li><a href="#about">About</a></li>
					<li><a href="#contact">Contact</a></li>
				 -->
			</ul>
			
			<div class="collapse navbar-collapse pull-right" style="color: white;">
				<ul class="nav navbar-nav">
					<li class="dropdown">
						<a href="#" dropdown-toggle>Você está logado como: <strong>${username}</strong> <b class="caret"></b></a>
              			<ul class="dropdown-menu">
							<li><a href="security/logout"><span class="glyphicon glyphicon-off"></span> Sair</a></li>
              			</ul>
            	 	</li>
				</ul>
			</div>
		</div>
		
		<!--/.nav-collapse -->
	</div>
	
	<div class="gumga-offcanvas-sidebar">
		<gumga:menu open="'${openMenu}'" class="gumga-menu">
			<g:menu items="${menu.menu}" />
		</gumga:menu>
	</div>
	
	<div id="gumga-growl-container" class='notifications top-right'></div>

	<div class="gumga-offcanvas-content">
		<div id="container" class="gumga-content">
			<h1 class="gumga-title">${title}</h1>
			<jsp:doBody />
		</div>
	</div>

    <script src="<c:url value='${gumgaProperties.path}/static/scripts/vendor/require.js' />"></script>
    <script src="<c:url value='${gumgaProperties.path}/static/scripts/config.js' />"></script>
    <script src="<c:url value='${gumgaProperties.path}/static/scripts/app-config.js' />"></script>
	<script>
    	requirejs.config({ baseUrl: '${path}/static/scripts/' });
    	
    	requirejs(['angular', '${init}', 'gumga/components/menu', 'gumga/components/offcanvas', 'angular-locale_pt-br'], function(angular, initModule) {
    		var app = angular.module('app', [initModule.name, 'gumga.components.menu', 'gumga.components.offcanvas', 'ngLocale']);
    		app.constant('contextPath', '${path}');
    		angular.bootstrap(document, [app.name]);
    	});
    	
	</script>
	<jsp:invoke fragment="scripts" />
</body>
</html>