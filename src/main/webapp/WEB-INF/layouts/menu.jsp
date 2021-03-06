<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<nav style="margin-right: 30px;margin-top: 10px">
  <ul class="nav nav-pills pull-right">
  	<li role="presentation" class="hidden-xs hidden-sm"><input type="text" class="form-control" placeholder="Search" name="searchPersonBar" id="searchPersonBar"></li>
    <li role="presentation"><a href="<c:out value='${pageContext.request.contextPath}/messaging/showMyConversations.html'/>"><span class="glyphicon glyphicon-envelope"></span> Messages</a></li>
    <li role="presentation"><a href="<c:out value='${pageContext.request.contextPath}/soulmates/showMySoulmates.html'/>"><span class="glyphicon glyphicon-heart"></span> Soulmates</a></li>
    <li role="presentation"><a href="<c:out value='${pageContext.request.contextPath}/profile/show.html'/>"><span class="glyphicon glyphicon-user"></span> My Profile</a></li>
    <li role="presentation"><a href="<c:out value='${pageContext.request.contextPath}/logout/bye.html'/>"><span class="glyphicon glyphicon-log-out"></span> Logout</a></li>
  </ul>
</nav>