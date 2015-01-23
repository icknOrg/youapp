<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page import="org.joda.time.DateTime"%>
<%@ page import="org.joda.time.Years"%>




<ul class="nav nav-tabs">

<c:choose>
	<c:when test="${param.showSite == 'showOwn'}">
		<li role="presentation" class="active"><a href="#">Edit Profile</a></li>
 	</c:when>
 	<c:otherwise>
 		<li role="presentation"><a href="<c:out value='${pageContext.request.contextPath}/profile/show.html'/>">Edit Profile</a></li>
 	</c:otherwise>
 	</c:choose>
 	
	<c:choose>
	<c:when test="${param.showSite == 'Questions'}">
		<li role="presentation" class="active"><a href="#">Questions</a></li>
 	</c:when>
 	<c:otherwise>
 		<li role="presentation"><a href="<c:out value='${pageContext.request.contextPath}/questions/showAll.html'/>">Questions</a></li>
 	</c:otherwise>
 	</c:choose>
 		
	<c:choose>
	<c:when test="${param.showSite == 'showMyStream'}">
		<li role="presentation" class="active"><a href="#">My Posts</a></li>
 	</c:when>
 	<c:otherwise>
 		<li role="presentation"><a href="<c:out value='${pageContext.request.contextPath}/statusupdate/showMyStream.html'/>">My Posts</a></li>
 	</c:otherwise>
 	</c:choose>

</ul>