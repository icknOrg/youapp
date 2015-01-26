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

							});
</script>

<!-- Main Content -->
<div class="center-content">

		<jsp:include page="showOwnProfilBox.jsp"></jsp:include>


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