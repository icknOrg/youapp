<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>

<div class="divlogo">
	<a href="<c:out value='${pageContext.request.contextPath}'/>" >
		<img class="logo" src="${pageContext.request.contextPath}/img/logo.gif" style="float:left; "/>
	</a>
</div>

<tiles:insertAttribute name="menu" ignore="true"/>