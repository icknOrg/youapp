<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>

<!-- Navigation -->
<nav class="navbar navbar-default navbar-static-top" role="navigation"
	style="margin-bottom: 0">
	<div class="navbar-header">
		<div class="col-6 col-6">
			<a class="navbar-brand"
				href="<c:out value='${pageContext.request.contextPath}'/>"> <img
				class="logo" style="max-height: 47px; width: auto;"
				src="${pageContext.request.contextPath}/img/logo_big.png" />
			</a>
		</div>
		<div id="navbar" class="navbar-collapse collapse">
			<form class="navbar-form navbar-right"></form>
		</div>
	</div>
	<!-- /.navbar-header -->

	<div class="navbar-menu"><span></span>
		<tiles:insertAttribute name="menu" ignore="true" />
	</div>
	<!-- /.navbar-static-side -->
</nav>