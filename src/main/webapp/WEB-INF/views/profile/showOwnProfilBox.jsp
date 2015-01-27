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

	<!-- Status update (With profil picture) -->
	<div class="container">
	<div class="row ">
		<div class="col-md-3">
			<div class="profile-picture">
				<div id="profile-picture">
					<img class="img-thumbnail"
						src="${ profileUtility.getProfilePictureUrl ( person.id)}">
				</div>
				<br/>
				<button type="button" class="btn btn-primary" id="update-picture">
					<spring:message code="profile.show.button.updatePicture" />
				</button>
			</div>
		</div>
		<div class="col-md-9">
			<div class="information-input">
				<form id="create-status-update">
					<div class="smilies" id="smileys-buttons">
						<input type="radio" name="radio" id="radio1" value="5"><label
							for="radio1"><img
							src="${pageContext.request.contextPath}/img/great.gif"
							class="emoticon"></label> 
							<input type="radio" name="radio"
							id="radio2" value="0"><label for="radio2"><img
							src="${pageContext.request.contextPath}/img/ok.gif"
							class="emoticon"></label> <input type="radio" name="radio"
							id="radio3" value="-5"><label for="radio3"><img
							src="${pageContext.request.contextPath}/img/sad.gif"
							style="padding-right: 15px;" class="emoticon"></label>
					</div>

					<spring:message code="statusupdate.showMyStream.create.message"
						var="message" />
					 <textarea placeholder="${message}<spring:message code="statusupdate.showMyStream.create.howAreYou"/>" style="width:80%" rows="5" ></textarea>
				</form>
			
				<button type="button" class="btn btn-primary" disabled="disabled"
					id="send-status-update">
					<spring:message code="statusupdate.showMyStream.create.button.send" />
				</button>
			</div>
		</div>
	</div>
	</div>
