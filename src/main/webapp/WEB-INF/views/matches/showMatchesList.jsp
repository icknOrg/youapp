<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<jsp:useBean id="profileUtility" type="youapp.utility.ProfileUtility" scope="application"/>

<c:forEach items="${matchesList}" var="match">
	<div class="match">
		<div class="matching">
			<div class="stat_number">
			    <spring:message code="matches.showMatchesList.scores.final" var="tooltipFinalScore"/>
				<span class="hours" title="${tooltipFinalScore}"><fmt:formatNumber type="percent" maxFractionDigits="0" value="${match.finalScore}" /></span>
			</div>
		</div>
		<div class="bluebox">
			<a href="${profileUtility.getProfileUrl(match.destinationPerson.id)}"> <img
				src="${profileUtility.getProfileThumbnailUrl(match.destinationPerson.id)}"
				alt="profile thumbnail" /> <span class="name"><c:out
						value="${profileUtility.getProfileName(match.destinationPerson, true)}" /></span>
			</a>
			<ul class="clear tags">
				<c:forEach items="${match.destinationPerson.profileTags}"
					var="profileTag">
					<li><c:out value="${profileTag.name}" /></li>
				</c:forEach>
			</ul>
			<span class="description"> <c:out
					value="${match.destinationPerson.description}" />
			</span>
			<div class="scores">
				<div class="score">
					<span class="score-description"><spring:message code="matches.showMatchesList.scores.replies" /></span> <span
						class="score-number"><fmt:formatNumber type="percent"
							maxFractionDigits="0" value="${match.repliesScore}" /></span>
				</div>
				<div class="score">
					<span class="score-description"><spring:message code="matches.showMatchesList.scores.facebookFriends" /></span> <span
						class="score-number"><fmt:formatNumber type="percent"
							maxFractionDigits="0" value="${match.friendsScore}" /></span>
				</div>
				<div class="score">
					<span class="score-description"><spring:message code="matches.showMatchesList.scores.facebookPages" /></span> <span
						class="score-number"><fmt:formatNumber type="percent"
							maxFractionDigits="0" value="${match.pagesScore}" /></span>
				</div>
			</div>
			<div class="buttons">
				<button data-personid="${match.destinationPerson.id}" class="send-soulmates-request"><spring:message code="matches.showMatchesList.soulmates.button.addSoulmate" /></button>

				<div class="sent-soulmates-request" style="display: none;">
					<spring:message code="matches.showMatchesList.soulmates.soulmatesRequestPending" />
					<button data-personid="${match.destinationPerson.id}"
						class="cancel-soulmates-request"><spring:message code="matches.showMatchesList.soulmates.button.cancelSoulmateRequest" /></button>
				</div>
			</div>
		</div>
	</div>
</c:forEach>