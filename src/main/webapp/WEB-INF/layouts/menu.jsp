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
		<ul class="dropdown-menu dropdown-user">
			<li><a
				href="<c:out value='${pageContext.request.contextPath}/profile/show.html'/>"><spring:message
						code="navigation.me.myProfile" /></a></li>
			<li><a
				href="<c:out value='${pageContext.request.contextPath}/soulmates/showMySoulmates.html'/>"><spring:message
						code="navigation.me.mySoulmates" /></a></li>

			<li class="divider"></li>
			<li><a
				href="<c:out value='${pageContext.request.contextPath}/messaging/showMyConversations.html'/>"><spring:message
						code="navigation.conversations" /></a></li>

			<li class="divider"></li>

			<li><a
				href="<c:out value='${pageContext.request.contextPath}/questions/create.html'/>"><spring:message
						code="navigation.question" /></a>
			<li><a
				href="<c:out value='${pageContext.request.contextPath}/questions/create.html'/>"><spring:message
						code="navigation.question.createQuestion" /></a></li>
			<li><a
				href="<c:out value='${pageContext.request.contextPath}/questions/showAll.html'/>"><spring:message
						code="navigation.question.myQuestions" /> <!-- <span class="bubble">23</span> --></a></li>

			<li class="divider"></li>

			<li><a
				href="<c:out value='${pageContext.request.contextPath}/replies/create.html'/>"><img
					src="${pageContext.request.contextPath}/img/check_12x10.png"
					alt="Answers" /> <spring:message code="navigation.answer" /> <!-- <span class="bubble-blue">6</span> --></a>
			<li><a
				href="<c:out value='${pageContext.request.contextPath}/replies/create.html'/>"><spring:message
						code="navigation.answer.answerQuestion" /></a></li>
			<li><a
				href="<c:out value='${pageContext.request.contextPath}/replies/showAll.html'/>"><spring:message
						code="navigation.answer.myAnswers" /></a></li>
			<li><a
				href="<c:out value='${pageContext.request.contextPath}/replies/showSkipped.html'/>"><spring:message
						code="navigation.answer.skippedQuestions" /> <!-- <span class="bubble">4</span> --></a></li>

			<li class="divider"></li>
			<li><a
				href="${pageContext.request.contextPath}/matches/showFindSoulmates.html"><img
					src="${pageContext.request.contextPath}/img/star_12x12.png"
					alt="Soulmates" /> <spring:message code="navigation.findSoulmates" /></a></li>

			<!-- if logged in show logout button, otherwise show login button -->
			<c:choose>
				<c:when
					test='${(not empty sessionScope.isRegistered) && (sessionScope.isRegistered == true)}'>
					<li class="floatr"><a
						href="<c:out value='${pageContext.request.contextPath}/logout/bye.html'/>"><spring:message
								code="navigation.general.logout" /></a></li>
				</c:when>
				<c:otherwise>
					<li class="floatr"><a
						href="<c:out value='${pageContext.request.contextPath}/login/register.html'/>"><spring:message
								code="navigation.general.login" /></a></li>
				</c:otherwise>
			</c:choose>

		</ul> <!-- /.dropdown-user --></li>
	<!-- /User -->
	
	<li><a href="<c:out value='${pageContext.request.contextPath}/logout/bye.html'/>"><span class="glyphicon glyphicon-log-out"></span></a></li>

</ul>


<form class="navbar-form navbar-right" role="search">
<div class="form-group">

<input type="text" class="form-control" placeholder="Search" >
			</div>
			<button type="submit" class="btn btn-default">Search</button>
			
			</form>
