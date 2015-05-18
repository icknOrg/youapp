<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<jsp:useBean id="profileUtility" type="youapp.utility.ProfileUtility"
	scope="application" />
<jsp:useBean id="timeUtility" class="youapp.utility.TimeUtility"
	scope="application" />

<script type="text/javascript">
	$(document)
			.ready(
					function() {
						$('button.unblock-person')
								.click(
										function() {
											var thisButton = $(this);

											var buttons = {};
											buttons[messages['soulmates.showMySoulmates.unblock.confirmDialog.ok']] = function() {
												thisButton
														.addClass('loading-animation');
												$(this).dialog('close');
												$
														.get(
																'${pageContext.request.contextPath}/blocker/unblockPerson.html?blockedId='
																		+ thisButton
																				.data('personid'),
																function(data) {
																	if (data == "") {
																		thisButton
																				.parent(
																						'.person-blocked')
																				.remove();
																		if ($('#people-blocked .person-blocked').length == 0) {
																			$(
																					'#people-blocked')
																					.remove();
																		}
																	}
																})
														.complete(
																function() {
																	thisButton
																			.removeClass('loading-animation');
																});
												;
											};
											buttons[messages['soulmates.showMySoulmates.unblock.confirmDialog.cancel']] = function() {
												$(this).dialog('close');
											};

											$
													.confirm(
															messages['soulmates.showMySoulmates.unblock.confirmDialog.message'],
															messages['soulmates.showMySoulmates.unblock.confirmDialog.title'],
															buttons);
										});

						$('button.confirm-soulmates-request')
								.click(
										function() {
											var thisButton = $(this);
											thisButton
													.addClass('loading-animation');
											$
													.get(
															baseUrl
																	+ '/soulmates/createSoulmatesRequest.html?requestedId='
																	+ thisButton
																			.data('personid'),
															function(data) {
																if (data == "") {
																	location
																			.reload();
																}
															})
													.complete(
															function() {
																thisButton
																		.removeClass('loading-animation');
															});
											;
										});

						$('button.decline-soulmates-request')
								.click(
										function() {
											var thisButton = $(this);
											thisButton
													.addClass('loading-animation');
											$
													.get(
															baseUrl
																	+ '/soulmates/deleteSoulmates.html?personBId='
																	+ thisButton
																			.data('personid'),
															function(data) {
																if (data == "") {
																	thisButton
																			.parents(
																					'.person-requested')
																			.remove();
																	if ($('#people-requested .person-requested').length == 0) {
																		$(
																				'#people-requested')
																				.remove();
																	}
																}
																thisButton
																		.removeClass('loading-animation');
															})
													.complete(
															function() {
																thisButton
																		.removeClass('loading-animation');
															});
											;
										});
					});
</script>

<h1>Soulmates</h1>
<spring:message code="soulmates.showMySoulmates.addSoulmates"
	arguments="${pageContext.request.contextPath}/matches/showFindSoulmates.html" />

<c:if test="${not empty requestedPeopleList}">
	<div id="people-requested">
		<h2 class="content-heading">
			<spring:message
				code="soulmates.showMySoulmates.title.soulmateRequests" />
		</h2>
		<c:forEach items="${requestedPeopleList}" var="personRequested">
		
			<div class="row ">
				<div class="profilBox">
					<div class="col-xs-6 col-md-2">
						<div class="profil-picture">
							<a href="${profileUtility.getProfileUrl(personRequested.id)}">
								<img class="img-thumbnail"
								src="${profileUtility.getProfilePictureUrl(personRequested.id)}" />
							</a>
						</div>
					</div>
					<div class="col-xs-6 col-md-4">
						<div class="information-input">
							<span class="namelist"><c:out
									value="${personRequested.firstName} ${personRequested.lastName} (@${personRequested.nickName})" /></span>
							<br>
							<button data-personid="${personRequested.id}"
								class="btn btn-primary send-message">
								<spring:message
									code="soulmates.showMySoulmates.button.sendMessage" />
							</button>
						</div>
					</div>
					<div class="col-xs-12 col-md-4">
						<div class="information-input">

							<button data-personid="${personRequested.id}"
								class="btn btn-success confirm-soulmates-request">
								<spring:message
									code="soulmates.showMySoulmates.button.confirmSoulmatesRequest" />
							</button>
							<br>
							<button data-personid="${personRequested.id}"
								class="btn btn-warning decline-soulmates-request">
								<spring:message
									code="soulmates.showMySoulmates.button.declineSoulmatesRequest" />
							</button>
						</div>
					</div>
				</div>
			</div>
			
		</c:forEach>
	</div>
</c:if>

<c:choose>
	<c:when test="${not empty soulmatesList}">
		<h2 class="content-heading">
			<spring:message code="soulmates.showMySoulmates.title.soulmates" />
		</h2>
		<div id="soulmates">

			<div class="chat row">
				<c:forEach items="${soulmatesList}" var="soulmate">

					<div class="col-xs-12 col-sm-4 left clearfix">
						<span class="chat-img pull-left"> <img
							src="${profileUtility.getProfileThumbnailUrl(soulmate.id)}"
							alt="User Avatar" class="img-circle" />
						</span>
						<div class="chat-body clearfix">
							<div class="header">
								<a href="${profileUtility.getProfileUrl(soulmate.id)}"> <strong
									class="primary-font">${soulmate.firstName}
										${soulmate.lastName} (@${soulmate.nickName})</strong></a> <small
									class="pull-right text-muted"> <i
									class="glyphicon glyphicon-time"></i> <c:choose>
										<c:when test="${not empty statusUpdatesMap[soulmate.id].when}">
											<span class="when" title="${statusUpdate.when}"><c:out
													value="${timeUtility.getElapsedTime(statusUpdatesMap[soulmate.id].when, pageContext.response.locale)}" /></span>
										</c:when>
										<c:otherwise>
											<spring:message code="soulmates.showMySoulmates.text.noTime" />
										</c:otherwise>
									</c:choose>
								</small>


							</div>
							<p>
								<c:choose>
									<c:when
										test="${not empty statusUpdatesMap[soulmate.id].description}">

										<c:choose>
											<c:when
												test="${fn:length(statusUpdatesMap[soulmate.id].description)>93}">
												<c:out
													value="${fn:substring(statusUpdatesMap[soulmate.id].description, 0, 90)}" />...
										</c:when>
											<c:otherwise>
												<c:out value="${statusUpdatesMap[soulmate.id].description}" />
											</c:otherwise>
										</c:choose>

									</c:when>
									<c:otherwise>
										<spring:message
											code="soulmates.showMySoulmates.text.noComment" />
									</c:otherwise>
								</c:choose>
							</p>

						</div>
					</div>

				</c:forEach>
			</div>
		</div>
	</c:when>
	<c:otherwise>
		<p>
			<spring:message code="soulmates.showMySoulmates.noSoulmatesAvailable"
			arguments="${pageContext.request.contextPath}/matches/showFindSoulmates.html" />
		</p>
	</c:otherwise>
</c:choose>



<c:if test="${not empty personBlockedList}">
	<div id="people-blocked">
		<h2 class="content-heading">
			<spring:message code="soulmates.showMySoulmates.title.blockedPeople" />
		</h2>
		<ul>
			<c:forEach items="${personBlockedList}" var="personBlocked">
				<li class="person-blocked"><c:out
						value="${personBlocked.firstName} (@${personBlocked.nickName})" /><br>
					<button data-personid="${personBlocked.id}"
						class="unblock-person btn btn-warning">
						<spring:message code="soulmates.showMySoulmates.unblock.button" />
					</button></li>
			</c:forEach>
		</ul>
	</div>
</c:if>