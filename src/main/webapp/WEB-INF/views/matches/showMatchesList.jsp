<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<jsp:useBean id="profileUtility" type="youapp.utility.ProfileUtility"
	scope="application" />

<c:forEach items="${matchesList}" var="match">

	<div class="row">
		<div class="profilBox" >
			<div class="col-xs-6 col-md-2">
				<div class="profil-picture">
					<a
						href="${profileUtility.getProfileUrl(match.destinationPerson.id)}">
						<img class="img-thumbnail"
						src="${profileUtility.getProfilePictureUrl(match.destinationPerson.id)}" />
					</a>
				</div>
			</div>
			<div class="col-xs-6 col-md-3">
				<div class="information-input">
					<span class="namelist"><c:out
							value="${match.destinationPerson.firstName} ${match.destinationPerson.lastName} (@${match.destinationPerson.nickName})" /></span>
					<br>

					<blockquote class="blockquote-reverse show-matches">
						<c:choose>
							<c:when test="${not empty match.destinationPerson.description}">
								<c:out value="${match.destinationPerson.description}" />
							</c:when>
							<c:otherwise>
								<span><spring:message
										code="profile.show.description.error" /></span>
							</c:otherwise>
						</c:choose>
					</blockquote>

				</div>
			</div>
			<div class="col-xs-6 col-md-4">
			<b><span><spring:message
								code="matches.showMatchesList.score" /></span></b>
				<div class="information-input">

					<span class="glyphicon glyphicon-heart"></span> <span><spring:message code="matches.showMatchesList.scores.final"
						/></span>
					<span class="hours" title="${tooltipFinalScore}"><fmt:formatNumber
							type="percent" maxFractionDigits="0" value="${match.finalScore}" /></span>


					<div class="score">
						<span class="score-description"><spring:message
								code="matches.showMatchesList.scores.replies" /></span> <span
							class="score-number"><fmt:formatNumber type="percent"
								maxFractionDigits="0" value="${match.repliesScore}" /></span>
					</div>
					<div class="score">
						<span class="score-description"><spring:message
								code="matches.showMatchesList.scores.facebookFriends" /></span> <span
							class="score-number"><fmt:formatNumber type="percent"
								maxFractionDigits="0" value="${match.friendsScore}" /></span>
					</div>
					<div class="score">
						<span class="score-description"><spring:message
								code="matches.showMatchesList.scores.facebookPages" /></span> <span
							class="score-number"><fmt:formatNumber type="percent"
								maxFractionDigits="0" value="${match.pagesScore}" /></span>
					</div>



				</div>
			</div>
			<div class="col-xs-6 col-md-2">
				<div class="information-input">

					<button data-personid="${match.destinationPerson.id}"
						class="send-soulmates-request btn btn-success">
						<spring:message
							code="matches.showMatchesList.soulmates.button.addSoulmate" />
					</button>

					<div class="sent-soulmates-request" style="display: none;">
						<spring:message
							code="matches.showMatchesList.soulmates.soulmatesRequestPending" />
						<button data-personid="${match.destinationPerson.id}"
							class="cancel-soulmates-request btn btn-warning">
							<spring:message
								code="matches.showMatchesList.soulmates.button.cancelSoulmateRequest" />
						</button>
					</div>




				</div>
			</div>
		</div>
	</div>



</c:forEach>