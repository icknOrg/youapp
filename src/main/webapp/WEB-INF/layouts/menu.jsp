<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>




			
<ul class="nav navbar-top-links navbar-right">
	<!-- Show Messages -->
	

	<li class="dropdown"><a class="dropdown-toggle"
		data-toggle="dropdown"
		href="<c:out value='${pageContext.request.contextPath}/messaging/showMyConversations.html'/>">
			<span class="glyphicon glyphicon-envelope"></span>
	</a></li>
	<!-- / Messages -->

	<!-- Tasc bars -->
	<li class="dropdown"><a class="dropdown-toggle"
		data-toggle="dropdown"
		href="<c:out value='${pageContext.request.contextPath}/soulmates/showMySoulmates.html'/>">
			<span class="glyphicon glyphicon-heart"></span>
	</a></li>
	<!-- User -->
	<li><a class="dropdown-toggle"
		href="<c:out value='${pageContext.request.contextPath}/profile/show.html'/>">
			<span class="glyphicon glyphicon-user"></span>
	</a>
</li>
	<!-- /User -->
	
	<li><a href="<c:out value='${pageContext.request.contextPath}/logout/bye.html'/>"><span class="glyphicon glyphicon-log-out"></span></a></li>

</ul>

<div class="navbar-collapse">
<form class="navbar-form navbar-right" role="search">
<div class="form-group search-bar" style="position: absolute; right: 200px; background-color:  #f7f7f7;">

<input type="text" class="form-control" placeholder="Search" name="searchPersonBar" id="searchPersonBar">
			</div>
			
			</form>
</div>