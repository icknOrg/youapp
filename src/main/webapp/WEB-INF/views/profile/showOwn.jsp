<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>

<jsp:useBean id="profileUtility" type="youapp.utility.ProfileUtility"
	scope="application" />

<script type="text/javascript">
	$(document)
			.ready(
					function() {
						$('button#edit-profile')
								.click(
										function() {
											button = $(this);
											button
													.addClass('loading-animation');

											$
													.get(
															baseUrl
																	+ '/profile/edit.html',
															function(data) {
																$('div#profile')
																		.html(
																				data);

																button.hide();
																$(
																		'button#save-profile')
																		.show();
															})
													.complete(
															function() {
																button
																		.removeClass('loading-animation');
															});
										});

						$('button#save-profile')
								.click(
										function() {
											button = $(this);
											button
													.addClass('loading-animation');

											$
													.post(
															baseUrl
																	+ '/profile/edit.html',
															$('form')
																	.serialize(),
															function(data) {
																$('div#profile')
																		.html(
																				data);

																if ($(
																		'div#profile .error')
																		.size() == 0) {
																	button
																			.hide();
																	$(
																			'button#edit-profile')
																			.show();
																}
															})
													.complete(
															function() {
																button
																		.removeClass('loading-animation');
															});
											;

										});

						$('button#update-picture')
								.click(
										function() {
											button = $(this);
											button
													.addClass('loading-animation');

											$
													.get(
															baseUrl
																	+ '/profile/updateProfilePicture.html',
															function(data) {
																// Update image view, with Date force Browser to reload image
																d = new Date();
																$(
																		"#profile-picture")
																		.css(
																				'background-image',
																				"url('"
																						+ data
																						+ "&"
																						+ d
																								.getTime()
																						+ "')");
															})
													.complete(
															function() {
																button
																		.removeClass('loading-animation');
															});
											;
										});
						$('button#send-status-update')
								.click(
										function() {
											var button = $(this);
											button
													.addClass('loading-animation');

											var description = $(
													'form#create-status-update textarea')
													.val();
											var moodRating = $(
													'form#create-status-update input[type=radio]:checked')
													.val();

											if (typeof moodRating == "undefined")
												moodRating = '';

											$
													.post(
															baseUrl
																	+ '/statusupdate/createStatusUpdate.html',
															{
																description : description,
																moodRating : moodRating
															},
															function(data) {
																if ($
																		.trim(data) !== "") {
																	$(data)
																			.hide()
																			.prependTo(
																					'#status-updates')
																			.slideDown(
																					1000);
																}

																// Reset the form
																$('form#create-status-update')[0]
																		.reset();
																$(
																		'button#send-status-update')
																		.attr(
																				'disabled',
																				'disabled');
															})
													.complete(
															function() {
																button
																		.removeClass('loading-animation');
															});
											;
										});

						// de-and activate send button by form content 
						$(
								'form#create-status-update input[type=radio], form#create-status-update textarea')
								.bind(
										'change keyup',
										function() {
											var description = $(
													'form#create-status-update textarea')
													.val();
											var moodRating = $(
													'form#create-status-update input[type=radio]:checked')
													.val();

											if (typeof moodRating == "undefined"
													&& description == "") {
												$('button#send-status-update')
														.attr('disabled',
																'disabled');
											} else {
												$('button#send-status-update')
														.removeAttr('disabled');
											}
										});
					});
</script>

<!-- Main Content -->
<div class="center-content">

	<!-- Status update (With profil picture) -->
	<div class="row statusupdate">
		<div class="col-xs-2 col-sm-2">
			<div class="profile-picture">
				<div id="profile-picture">
					<img class="img-thumbnail"
						src="${ profileUtility.getProfilePictureUrl ( person.id)}">
				</div>
				<button type="button" class="btn btn-primary" id="update-picture">
					<spring:message code="profile.show.button.updatePicture" />
				</button>
			</div>
		</div>
		<div class="col-xs-8 col-sm-8">
			<div class="information-input">
				<form id="create-status-update">
					<div class="smilies" id="smileys-buttons">
						<input type="radio" name="radio" id="radio1" value="5"><label
							for="radio1"><img
							src="${pageContext.request.contextPath}/img/great.gif"
							class="emoticon"></label> <input type="radio" name="radio"
							id="radio2" value="0"><label for="radio2"><img
							src="${pageContext.request.contextPath}/img/ok.gif"
							class="emoticon"></label> <input type="radio" name="radio"
							id="radio3" value="-5"><label for="radio3"><img
							src="${pageContext.request.contextPath}/img/sad.gif"
							style="padding-right: 15px;" class="emoticon"></label>
					</div>

					<spring:message code="statusupdate.showMyStream.create.message"
						var="message" />
					<textarea class="form-control" placeholder="${message}"></textarea>
				</form>
				<button type="button" class="btn btn-primary" disabled="disabled"
					id="send-status-update">
					<spring:message code="statusupdate.showMyStream.create.button.send" />
				</button>
			</div>
		</div>
	</div>


	<!-- Tab Navigation -->
	<jsp:include page="navigateProfile.jsp">
		<jsp:param name="showSite" value="showOwn" />
	</jsp:include>

	<!-- Profile information -->
	<div class="right-content">
		<div class="content">


			<!-- Numberdata -->
			<div class="about-right">
				<h2 class="content-heading">
					<c:out value="${person.nickName}" />
					<spring:message code="profile.show.headings.yourProfile" />
				</h2>
			</div>
			
			<div class="row">
				<div class="col-md-6">
					<div class="panel panel-primary">
						<div class="panel-heading">
							<div class="row">
								<div class="col-xs-3">
									<i class="glyphicon glyphicon-music glyphicon-profile-numbers"></i>
								</div>
								<div class="col-xs-9 text-right">
									<div class="huge"><c:out value="${sinceRegistered}" /></div>
									<div><spring:message
							code="profile.show.numbers.sinceRegistered" /></div>
								</div>
							</div>
						</div>
						<a href="<c:out value='${pageContext.request.contextPath}/statusupdate/showMyStream.html'/>">
							<div class="panel-footer">
								<span class="pull-left">View Details</span> <span
									class="pull-right"><i class="glyphicon glyphicon-circle-arrow-right"></i></span>
								<div class="clearfix"></div>
							</div>
						</a>
					</div>
				</div>

				<div class="col-md-6">
					<div class="panel panel-green">
						<div class="panel-heading">
							<div class="row">
								<div class="col-xs-3">
									<i class="glyphicon glyphicon-heart glyphicon-profile-numbers"></i>
								</div>
								<div class="col-xs-9 text-right">
									<div class="huge"><c:out value="${numberOfSoulmates}" /></div>
									<div><spring:message
							code="profile.show.numbers.soulmates" /></div>
								</div>
							</div>
						</div>
						<a href="<c:out value='${pageContext.request.contextPath}/soulmates/showMySoulmates.html'/>"">
							<div class="panel-footer">
								<span class="pull-left">View Details</span> <span
									class="pull-right"><i class="glyphicon glyphicon-circle-arrow-right"></i></span>
								<div class="clearfix"></div>
							</div>
						</a>
					</div>
				</div>
			</div>

			<!-- General data -->
			<h2 class="content-heading">
				<spring:message code="profile.show.headings.personal" />
			</h2>
			<div id="profile" class="numbers">

				<jsp:include page="showProfilePart.jsp">
					<jsp:param name="showOwnProfile" value="${true}" />
				</jsp:include>

			</div>


			<button class="btn btn-primary" id="save-profile" class="floatr"
				style="display: none;">Save</button>
			<button class="btn btn-primary" id="edit-profile" class="floatr">Edit</button>


		</div>
	</div>
</div>
<!-- /center-content -->