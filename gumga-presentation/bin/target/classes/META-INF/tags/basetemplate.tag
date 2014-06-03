<%@tag description="Template Base" pageEncoding="UTF-8"%>
<%@ attribute name="scripts" required="false" fragment="true" %>
<%@ attribute name="title" required="true"  %>
<%@ taglib uri="http://gumga.com.br/jsp/tags" prefix="g" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<fmt:bundle basename="messages_template"></fmt:bundle>

<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<meta charset="utf-8" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<title><fmt:message key="app.title" /> - ${title}</title>

<link rel="stylesheet" href="https://s3-us-west-1.amazonaws.com/gumga-piloto/static/styles/main.css" />
<link rel="stylesheet" href="https://s3-us-west-1.amazonaws.com/gumga-piloto/static/styles/gumga.css" />
<link rel="stylesheet" href="https://s3-us-west-1.amazonaws.com/gumga-piloto/static/styles/menu.css" />

</head>
<body>
	<div class="navbar navbar-inverse navbar-fixed-top" role="navigation">
		<div class="navbar-header">
			<button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
				<span class="sr-only">Toggle navigation</span> 
				<span class="icon-bar"></span> 
				<span class="icon-bar"></span>  
				<span class="icon-bar"></span>
			</button>
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
						<a href="#" class="dropdown-toggle" data-toggle="dropdown">Você está logado como: <strong>${username}</strong> <b class="caret"></b></a>
              			<ul class="dropdown-menu">
							<li><a href="security/logout"><span class="glyphicon glyphicon-off"></span> Sair</a></li>
              			</ul>
            	 	</li>
				</ul>
			</div>
		</div>
		
		<!--/.nav-collapse -->
	</div>

	<div id="gumga-growl-container" class='notifications top-right'></div>

	<div id="container">
		<div class="gumga-sidebar">
			<div class="gumga-menu" id="cssmenu">
				<g:menu items="${menu.menu}" />
			</div>
		</div>
		<div class="gumga-content">
			<jsp:doBody />
		</div>
	</div>

	<script src="https://s3-us-west-1.amazonaws.com/gumga-piloto/static/scripts/vendor/require.js"></script>
	<script src="https://s3-us-west-1.amazonaws.com/gumga-piloto/static/scripts/config.js"></script>
	<script src="https://s3-us-west-1.amazonaws.com/gumga-piloto/static/scripts/app-config.js"></script>
	<script>
		requirejs.config({ baseUrl: 'https://s3-us-west-1.amazonaws.com/gumga-piloto/static/scripts/' });
	</script>
	<jsp:invoke fragment="scripts" />
</body>
</html>