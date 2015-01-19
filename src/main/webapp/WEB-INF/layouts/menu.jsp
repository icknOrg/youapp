<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>



<ul class="nav navbar-top-links navbar-right">
	<!-- Show Messages -->
	<li class="dropdown"><a class="dropdown-toggle"
		data-toggle="dropdown" href="#"> <span
			class="glyphicon glyphicon-envelope"></span>
	</a>

		<ul class="dropdown-menu dropdown-messages">
			<li><a href="#">
					<div>
						<strong>John Smith</strong> <span class="pull-right text-muted">
							<em>Yesterday</em>
						</span>
					</div>
					<div>Lorem ipsum dolor sit amet, consectetur adipiscing elit.
						Pellentesque eleifend...</div>
			</a></li>
			<li class="divider"></li>
			<li><a href="#">
					<div>
						<strong>John Smith</strong> <span class="pull-right text-muted">
							<em>Yesterday</em>
						</span>
					</div>
					<div>Lorem ipsum dolor sit amet, consectetur adipiscing elit.
						Pellentesque eleifend...</div>
			</a></li>
			<li class="divider"></li>
			<li><a href="#">
					<div>
						<strong>John Smith</strong> <span class="pull-right text-muted">
							<em>Yesterday</em>
						</span>
					</div>
					<div>Lorem ipsum dolor sit amet, consectetur adipiscing elit.
						Pellentesque eleifend...</div>
			</a></li>
			<li class="divider"></li>
			<li><a class="text-center" href="#"> <strong>Read
						All Messages</strong> <i class="fa fa-angle-right"></i>
			</a></li>
		</ul></li>
	<!-- / Messages -->

	<!-- Tasc bars -->
	<li class="dropdown"><a class="dropdown-toggle"
		data-toggle="dropdown" href="#"> <span
			class="glyphicon glyphicon-tasks"></span>
	</a>

		<ul class="dropdown-menu dropdown-tasks">
			<li><a href="#">
					<div>
						<p>
							<strong>Task 1</strong> <span class="pull-right text-muted">40%
								Complete</span>
						</p>
						<div class="progress progress-striped active">
							<div class="progress-bar progress-bar-success" role="progressbar"
								aria-valuenow="40" aria-valuemin="0" aria-valuemax="100"
								style="width: 40%">
								<span class="sr-only">40% Complete (success)</span>
							</div>
						</div>
					</div>
			</a></li>
			<li class="divider"></li>
			<li><a href="#">
					<div>
						<p>
							<strong>Task 2</strong> <span class="pull-right text-muted">20%
								Complete</span>
						</p>
						<div class="progress progress-striped active">
							<div class="progress-bar progress-bar-info" role="progressbar"
								aria-valuenow="20" aria-valuemin="0" aria-valuemax="100"
								style="width: 20%">
								<span class="sr-only">20% Complete</span>
							</div>
						</div>
					</div>
			</a></li>
			<li class="divider"></li>
			<li><a href="#">
					<div>
						<p>
							<strong>Task 3</strong> <span class="pull-right text-muted">60%
								Complete</span>
						</p>
						<div class="progress progress-striped active">
							<div class="progress-bar progress-bar-warning" role="progressbar"
								aria-valuenow="60" aria-valuemin="0" aria-valuemax="100"
								style="width: 60%">
								<span class="sr-only">60% Complete (warning)</span>
							</div>
						</div>
					</div>
			</a></li>
			<li class="divider"></li>
			<li><a href="#">
					<div>
						<p>
							<strong>Task 4</strong> <span class="pull-right text-muted">80%
								Complete</span>
						</p>
						<div class="progress progress-striped active">
							<div class="progress-bar progress-bar-danger" role="progressbar"
								aria-valuenow="80" aria-valuemin="0" aria-valuemax="100"
								style="width: 80%">
								<span class="sr-only">80% Complete (danger)</span>
							</div>
						</div>
					</div>
			</a></li>
			<li class="divider"></li>
			<li><a class="text-center" href="#"> <strong>See
						All Tasks</strong> <i class="fa fa-angle-right"></i>
			</a></li>
		</ul></li>
	<!-- /Tasc bars -->

	<!-- Alerts -->
	<li class="dropdown"><a class="dropdown-toggle"
		data-toggle="dropdown" href="#"> <span
			class="glyphicon glyphicon-bell"></span>
	</a>

		<ul class="dropdown-menu dropdown-alerts">
			<li><a href="#">
					<div>
						<i class="fa fa-comment fa-fw"></i> New Comment <span
							class="pull-right text-muted small">4 minutes ago</span>
					</div>
			</a></li>
			<li class="divider"></li>
			<li><a href="#">
					<div>
						<i class="fa fa-twitter fa-fw"></i> 3 New Followers <span
							class="pull-right text-muted small">12 minutes ago</span>
					</div>
			</a></li>
			<li class="divider"></li>
			<li><a href="#">
					<div>
						<i class="fa fa-envelope fa-fw"></i> Message Sent <span
							class="pull-right text-muted small">4 minutes ago</span>
					</div>
			</a></li>
			<li class="divider"></li>
			<li><a href="#">
					<div>
						<i class="fa fa-tasks fa-fw"></i> New Task <span
							class="pull-right text-muted small">4 minutes ago</span>
					</div>
			</a></li>
			<li class="divider"></li>
			<li><a href="#">
					<div>
						<i class="fa fa-upload fa-fw"></i> Server Rebooted <span
							class="pull-right text-muted small">4 minutes ago</span>
					</div>
			</a></li>
			<li class="divider"></li>
			<li><a class="text-center" href="#"> <strong>See
						All Alerts</strong> <i class="fa fa-angle-right"></i>
			</a></li>
		</ul></li>
	<!-- /Alerts -->

	<!-- User -->
	<li class="dropdown"><a class="dropdown-toggle"
		data-toggle="dropdown" href="#"> <span
			class="glyphicon glyphicon-user"></span>
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
					alt="Soulmates" />
				<spring:message code="navigation.findSoulmates" /></a></li>

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

</ul>


