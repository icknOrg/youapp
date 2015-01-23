<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>







<!-- Navigation -->
<nav class="navbar navbar-default navbar-static-top" role="navigation"
	style="margin-bottom: 0">
	<div class="navbar-header">
		<button type="button" class="navbar-toggle" data-toggle="collapse"
			data-target=".navbar-collapse">
			<span class="sr-only">Toggle navigation</span> <span class="icon-bar"></span>
			<span class="icon-bar"></span> <span class="icon-bar"></span>
		</button>
		<a class="navbar-brand"
			href="<c:out value='${pageContext.request.contextPath}'/>"> <img
			class="logo" style="max-height: 37px; width: auto;" src="${pageContext.request.contextPath}/img/logo.png" "/>
		</a>
	</div>
	<!-- /.navbar-header -->


	<!-- 
<div class="divlogo">
	<a href="<c:out value='${pageContext.request.contextPath}'/>" >
		<img class="logo" src="${pageContext.request.contextPath}/img/logo.gif" style="float:left; "/>
	</a>
</div>
 -->


	<tiles:insertAttribute name="menu" ignore="true" />
	<!-- /.navbar-static-side -->
</nav>