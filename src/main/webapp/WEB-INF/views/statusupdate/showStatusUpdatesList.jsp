<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<jsp:useBean id="profileUtility" type="youapp.utility.ProfileUtility"
	scope="application" />
<jsp:useBean id="timeUtility" class="youapp.utility.TimeUtility"
	scope="application" />

<c:forEach items="${statusUpdates}" var="statusUpdate">
	<div class="status-update">
		<div class="profile-picture"
			style="width:63px; height:63px; overflow: hidden; border-radius:100px; background-image: url(${profileUtility.getProfilePictureUrl(statusUpdate.person.id)}); background-position: center; background-size: cover;"></div>
		<div class="content">
			<c:out value="${statusUpdate.person.firstName}" />
			says
			<c:out value="${statusUpdate.description}" />
			<span class="when" title="${statusUpdate.when}"><c:out
					value="${timeUtility.getElapsedTime(statusUpdate.when, pageContext.response.locale)}" /></span>
		</div>
		<c:if test="${myProfile.id eq statusUpdate.person.id}">
			<button data-statusupdatetimestamp="${statusUpdate.when}"
				class="delete-status-update">
				<spring:message code="statusupdate.showMyStream.delete.button" />
			</button>
		</c:if>
	</div>
</c:forEach>
