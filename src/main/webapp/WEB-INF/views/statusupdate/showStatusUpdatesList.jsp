<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<jsp:useBean id="profileUtility" type="youapp.utility.ProfileUtility"
	scope="application" />
<jsp:useBean id="timeUtility" class="youapp.utility.TimeUtility"
	scope="application" />

<div class="panel panel-default"></div>
	
	 <div class="panel-heading">
	<i class="fa fa-clock-o fa-fw"></i>
	Timeline
	</div>
	
	
<c:forEach items="${statusUpdates}" var="statusUpdate">
	
	
	
					
	<div class="panel-body">
	
	 <ul class="timeline">
			<li>
				<div class="timeline-badge" style="width:63px; height:63px; overflow: hidden; border-radius:100px; background-image: url(${profileUtility.getProfilePictureUrl(statusUpdate.person.id)}); background-position: center; background-size: cover;"></div>
					
					
		
		  <div class="timeline-panel">
		  <div class="timeline-heading">
		  <h4 class="timeline-title"><c:out value="${statusUpdate.person.firstName} " />says</h4>
		<p>
		<small class="text-muted">
		<i class="fa fa-clock-o"></i>
		<span class="when" title="${statusUpdate.when}"><c:out
					value="${timeUtility.getElapsedTime(statusUpdate.when, pageContext.response.locale)}" /></span>
		</small>
		</p>
		</div>
		
		<div class="timeline-body">
			<p><c:out value="${statusUpdate.description}" /></p>
			
			<p><c:if test="${myProfile.id eq statusUpdate.person.id}">
			<button data-statusupdatetimestamp="${statusUpdate.when}"
				class="delete-status-update">
				<spring:message code="statusupdate.showMyStream.delete.button" />
			</button>
		</c:if></p>
		</div>
		</div>
	

		
	</li>
	<li class="timeline-inverted">
					<div class="timeline-badge " style="width:63px; height:63px; overflow: hidden; border-radius:100px; background-image: url(${profileUtility.getProfilePictureUrl(statusUpdate.person.id)}); background-position: center; background-size: cover;"></div>
					
					<div class="timeline-panel">
						<div class="timeline-heading">
							<h4 class="timeline-title"><c:out value="${statusUpdate.person.firstName} " />says</h4>
						</div>
						<p><small class="text-muted">
		<i class="fa fa-clock-o"></i>
		<span class="when" title="${statusUpdate.when}"><c:out
					value="${timeUtility.getElapsedTime(statusUpdate.when, pageContext.response.locale)}" /></span>
		</small></p>
					</div>
					<div class="timeline-body">
					
					
					
					
					</div>
					
					</li>
		</ul>
		</div>
	

</c:forEach>
	
	
