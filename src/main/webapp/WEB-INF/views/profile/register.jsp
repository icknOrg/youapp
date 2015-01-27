<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/tag-it.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/profileEdit.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/bday-picker.js"></script>
<script type="text/javascript">
$(document).ready(function(){
	$('input, textarea').placeholder();
    $("#birthday-wrapper").birthdaypicker({
    	hiddenDate: "birthday",
    	defaultDate: true
    });
    $('#birthday').hide();
	tagit($('#symptoms-tagit'), $('#symptoms-field'), [${allSymptoms}]);
	tagit($('#medications-tagit'), $('#medications-field'), [${allMedications}]);
	tagit( $('#profiletags-tagit'), $('#profiletags-field'), [${allProfileTags}]);
	locationAutocomplete();
});
</script>

<!-- Main Content Central 940px + border-->
<div class="center-content">
	<h2 class="content-heading">
		<spring:message code="profile.edit.headings.titleRegister" />
		<span style="color: #DDD; font-weight: 300;"><spring:message
				code="profile.edit.headings.subtitleRegister" /></span>
	</h2>

	<form:form id="register" action="register.html" method="post"
		commandName="person">


		<div class="container">
			<div class="row ">
				<div class="col-md-3">
					<div class="profile-picture">
						<div id="profile-picture">
							<img class="img-thumbnail" src="${profilePictureUrl}">
						</div>
					</div>
				</div>
				<div class="col-md-9">
					<div class="information-input">

						<span class="text-wrapper"><spring:message
								code="profile.edit.textWrapper.part8" /></span> <br> <span
							class="text-wrapper"><spring:message
								code="profile.edit.textWrapper.part9" /></span>


						<spring:message code="profile.edit.description.placeholder"
							var="descriptionPlaceholder" />
						<br>
						<form:textarea class="form-control" path="description"
							maxlength="160" placeholder="${descriptionPlaceholder}" />
						<br>
						<spring:message code="profile.edit.description.label" />
						<form:errors path="description" cssClass="error" />



					</div>
				</div>
			</div>
		</div>

		<!-- Right Box -->
		<div class="right-content">
			<!-- Right Content-->
			<div class="content">
				<div class="about-right"></div>
				<p>
					<b><span class="text-wrapper"><spring:message
								code="profile.edit.textWrapper.part1" /></span> <spring:message
							code="profile.edit.firstName.label" var="firstNameLabel" /></b>
					<form:input path="firstName" class="form-control"
						placeholder="${firstNameLabel}" />
					<form:errors path="firstName" cssClass="error" />
					<spring:message code="profile.edit.lastName.label"
						var="lastNameLabel" />
					<form:input path="lastName" class="form-control"
						placeholder="${lastNameLabel}" />
					<form:errors path="lastName" cssClass="error" />

					<br> <b><span class="text-wrapper"><spring:message
								code="profile.edit.textWrapper.part2" /></span> <spring:message
							code="profile.edit.location.label" var="locationLabel" /></b>
					<form:input path="location.name" class="form-control"
						id="location-name" placeholder="${locationLabel}" />
					<form:errors path="location" cssClass="error" />
					<form:hidden path="location.longitude" id="location-longitude" />
					<form:hidden path="location.latitude" id="location-latitude" />
					<form:hidden path="location.id" id="location-id" />

					<b><span class="text-wrapper"><spring:message
								code="profile.edit.textWrapper.part3" /></span> <spring:message
							code="profile.edit.nickName.label" var="nickNameLabel" /></b>
					<form:input path="nickName" class="form-control"
						placeholder="${nickNameLabel}" />
					<form:errors path="nickName" cssClass="error" />

					<b><span class="text-wrapper"> <spring:message
								code="profile.edit.textWrapper.part4" /></span> <span
						class="text-wrapper"><spring:message
								code="profile.edit.textWrapper.part5" /></span></b> <span
						id="birthday-wrapper"> <spring:message
							code="profile.edit.birthday.format" var="birthdayFormat" /> <form:input
							path="birthday" placeholder="${birthdayFormat}"
							class="form-control" /> <form:errors path="birthday"
							cssClass="error" />
					</span> <br> <b><span class="text-wrapper"><spring:message
								code="profile.edit.textWrapper.part6" /></span> <span
						class="text-wrapper"><spring:message
								code="profile.edit.textWrapper.part7" /></span></b>
					<form:select class="form-control" path="gender">
						<form:option value="M">
							<spring:message code="profile.edit.gender.male" />
						</form:option>
						<form:option value="F">
							<spring:message code="profile.edit.gender.female" />
						</form:option>
					</form:select>
					<form:errors path="gender" cssClass="error" />


				</p>
				<h3>
					<spring:message code="profile.edit.profileTags.title" />
				</h3>
				<spring:message code="profile.edit.profileTags.label" />
				<div class="clear">
					<form:hidden path="profileTags" id="profiletags-field" />
					<ul id="profiletags-tagit"></ul>
					<form:errors path="profileTags" cssClass="error" />
				</div>
				<h3>
					<spring:message code="profile.edit.medications.title" />
				</h3>
				<spring:message code="profile.edit.medications.label"/>
				<div class="clear">
					<form:hidden path="medications" id="medications-field" />
					<ul id="medications-tagit"></ul>
					<form:errors path="medications" cssClass="error" />
				</div>
				<h3>
					<spring:message code="profile.edit.symptoms.title" />
				</h3>
				<spring:message code="profile.edit.symptoms.label" />
				<div class="clear">
					<form:hidden path="symptoms" id="symptoms-field" />
					<ul id="symptoms-tagit"></ul>
					<form:errors path="symptoms" cssClass="error" />
				</div>
				<h3>
					<spring:message code="profile.edit.useForMatching.title" />
				</h3>
				<%-- 	<spring:message code="profile.edit.useForMatching.lable" /> --%>
				<div class="clear">
						<form:checkbox path="useQuestionMatching" style="margin-left:10px"></form:checkbox>
						Quiz
						<form:checkbox path="useFBMatching" style="margin-left:10px"></form:checkbox>
						Facebook info
						<form:checkbox path="useDistanceMatching" style="margin-left:10px"></form:checkbox>
						Distance
						<form:checkbox path="useSymptomsMatching" style="margin-left:10px"></form:checkbox>
						Symptoms
						<form:checkbox path="useMedicationMatching"
							style="margin-left:10px"></form:checkbox>
						Medication
					
				</div>
				<spring:message code="profile.edit.button.submit" var="submitLabel" />
				<input
					style="width: 700px; margin-left: 0px !important; margin-bottom: 15px; margin-top: 10px;"
					type="submit" value="${submitLabel}" name="submit"
					class="btn-green btn-status">

			</div>
		</div>
	</form:form>
</div>