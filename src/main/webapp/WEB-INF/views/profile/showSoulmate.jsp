<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>

<jsp:useBean id="profileUtility" type="youapp.utility.ProfileUtility"
	scope="application" />

<script type="text/javascript">
	$(document)
			.ready(
					function() {
						$('button#remove-soulmate').click(
										function() {
											var button = $(this);

											var buttons = {};
											buttons[messages['profile.show.soulmates.remove.confirmDialog.ok']] = function() {
												$(this).dialog('close');
												button
														.addClass('loading-animation');

												$
														.get(
																'${pageContext.request.contextPath}/soulmates/deleteSoulmates.html?personBId='
																		+ button
																				.data('personid'),
																function(data) {
																	if (data == "") {
																		button
																				.hide();
																		location
																				.reload();
																	}
																	button
																			.removeClass('loading-animation');
																})
														.complete(
																function() {
																	button
																			.removeClass('loading-animation');
																});
												;
											};
											buttons[messages['profile.show.soulmates.remove.confirmDialog.cancel']] = function() {
												$(this).dialog('close');
											};

											$
													.confirm(
															messages['profile.show.soulmates.remove.confirmDialog.message'],
															messages['profile.show.soulmates.remove.confirmDialog.title'],
															buttons);
										});
						

						$('button#block-person')
								.click(
										function() {
											var button = $(this);

											var buttons = {};
											buttons[messages['profile.show.block.confirmDialog.ok']] = function() {
												$(this).dialog('close');

												button
														.addClass('loading-animation');
												$
														.get(
																baseUrl
																		+ '/blocker/blockPerson.html?blockedId='
																		+ button
																				.data('personid'),
																function(data) {
																	if (data == "") {
																		button
																				.attr(
																						'disabled',
																						'disabled');
																		window.location
																				.replace(baseUrl);
																	}
																	button
																			.removeClass('loading-animation');
																})
														.complete(
																function() {
																	button
																			.removeClass('loading-animation');
																});
												;
											};
											buttons[messages['profile.show.block.confirmDialog.cancel']] = function() {
												$(this).dialog('close');
											};

											$
													.confirm(
															messages['profile.show.block.confirmDialog.message'],
															messages['profile.show.block.confirmDialog.title'],
															buttons);
										});
					});
</script>

<!-- Main Content Central 940px + border-->
<div class="center-content">


	<div class="row profilBox">
		<div class="col-xs-4 col-sm-4">
			<div class="profile-picture">
				<div id="profile-picture">
					<img class="img-thumbnail"
						src="${profileUtility.getProfilePictureUrl(person.id)}">
				</div>
			</div>
		</div>
		<div class="col-xs-6 col-sm-6">
			<div class="information-input">
				<button data-personid="${person.id}" id="remove-soulmate"
					class="btn btn-primary">
					<spring:message code="profile.show.soulmates.button.removeSoulmate" />
				</button>
				<br><button data-personid="${person.id}" id="block-person"
					class="btn btn-danger">
					<spring:message code="profile.show.block.button.blockPerson" />
				</button>
			</div>
		</div>
	</div>

	<!-- Right Box -->
	<div class="right-content">
		<!-- Right Content-->
		<div class="content">
			<div class="about-right">
				<h2 class="content-heading">
					<c:out value="${person.nickName}" />
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
					</div>
				</div>
			</div>




			<h2 class="content-heading">
				<spring:message code="profile.show.headings.personal" />
			</h2>
			<div id="profile" class="numbers"
				style="border-bottom: 0px solid #eee;">
				<jsp:include page="showProfilePart.jsp"></jsp:include>
			</div>
		</div>
	</div>
</div>
<!-- /center-content -->
