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
	<div class="side-rail left-rail">
		<div
			style="overflow: hidden; width:200px; height:200px; border-radius:100px; background-image: url(${profilePictureUrl}); background-position: center; background-size: cover;">
		</div>
	</div>

	<!-- Right Box -->
	<div class="right-content">
		<!-- Right Content-->
		<div class="content">
			<div class="about-right">
				<h2 class="content-heading">
					<spring:message code="profile.edit.headings.titleRegister" />
					<span style="color: #DDD; font-weight: 300;"><spring:message
							code="profile.edit.headings.subtitleRegister" /></span>
				</h2>
			</div>
			<form:form id="register" action="register.html" method="post"
				commandName="person">
				<p>
					<span class="text-wrapper"><spring:message
							code="profile.edit.textWrapper.part1" /></span>
					<spring:message code="profile.edit.firstName.label"
						var="firstNameLabel" />
					<form:input path="firstName" placeholder="${firstNameLabel}" />
					<form:errors path="firstName" cssClass="error" />
					<spring:message code="profile.edit.lastName.label"
						var="lastNameLabel" />
					<form:input path="lastName" placeholder="${lastNameLabel}" />
					<form:errors path="lastName" cssClass="error" />
					<span class="text-wrapper"><spring:message
							code="profile.edit.textWrapper.part2" /></span>
					<spring:message code="profile.edit.location.label"
						var="locationLabel" />
					<form:input path="location.name" id="location-name"
						placeholder="${locationLabel}" />
					<form:errors path="location" cssClass="error" />
					<form:hidden path="location.longitude" id="location-longitude" />
					<form:hidden path="location.latitude" id="location-latitude" />
					<form:hidden path="location.id" id="location-id" />
					<span class="text-wrapper"><spring:message
							code="profile.edit.textWrapper.part3" /></span>
					<spring:message code="profile.edit.nickName.label"
						var="nickNameLabel" />
					<form:input path="nickName" placeholder="${nickNameLabel}" />
					<form:errors path="nickName" cssClass="error" />
					<span class="text-wrapper"><spring:message
							code="profile.edit.textWrapper.part4" /></span> <span
						class="text-wrapper"><spring:message
							code="profile.edit.textWrapper.part5" /></span> <span
						id="birthday-wrapper"> <spring:message
							code="profile.edit.birthday.format" var="birthdayFormat" /> <form:input
							path="birthday" placeholder="${birthdayFormat}" /> <form:errors
							path="birthday" cssClass="error" />
					</span> <span class="text-wrapper"><spring:message
							code="profile.edit.textWrapper.part6" /></span> <span
						class="text-wrapper"><spring:message
							code="profile.edit.textWrapper.part7" /></span>
					<form:select path="gender">
						<form:option value="M">
							<spring:message code="profile.edit.gender.male" />
						</form:option>
						<form:option value="F">
							<spring:message code="profile.edit.gender.female" />
						</form:option>
					</form:select>
					<form:errors path="gender" cssClass="error" />
					<span class="text-wrapper"><spring:message
							code="profile.edit.textWrapper.part8" /></span> <br> <span
						class="text-wrapper"><spring:message
							code="profile.edit.textWrapper.part9" /></span>
					<spring:message code="profile.edit.description.placeholder"
						var="descriptionPlaceholder" />
					<form:textarea path="description" maxlength="160"
						placeholder="${descriptionPlaceholder}" />
					<br>
					<spring:message code="profile.edit.description.label" />
					<form:errors path="description" cssClass="error" />
				</p>
				<h2 class="content-heading" style="border-bottom: 0px solid #fff;">
					<spring:message code="profile.edit.profileTags.title" />
				</h2>
				<spring:message code="profile.edit.profileTags.label" />
				<div class="clear">
					<form:hidden path="profileTags" id="profiletags-field" />
					<ul id="profiletags-tagit"></ul>
					<form:errors path="profileTags" cssClass="error" />
				</div>
				<h2 class="content-heading" style="border-bottom: 0px solid #fff;">
					<spring:message code="profile.edit.medications.title" />
				</h2>
				<spring:message code="profile.edit.medications.label" />
				<div class="clear">
					<form:hidden path="medications" id="medications-field" />
					<ul id="medications-tagit"></ul>
					<form:errors path="medications" cssClass="error" />
				</div>
				<h2 class="content-heading" style="border-bottom: 0px solid #fff;">
					<spring:message code="profile.edit.symptoms.title" />
				</h2>
				<spring:message code="profile.edit.symptoms.label" />
				<div class="clear">
					<form:hidden path="symptoms" id="symptoms-field" />
					<ul id="symptoms-tagit"></ul>
					<form:errors path="symptoms" cssClass="error" />
				</div>
				<h2 class="content-heading" style="border-bottom: 0px solid #fff;">
					<spring:message code="profile.edit.useForMatching.title" />
				</h2>
				<%-- 	<spring:message code="profile.edit.useForMatching.lable" /> --%>
				<div class="clear">
					<h2 style="border-bottom: 20px;">
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
					</h2>
				</div>
				<spring:message code="profile.edit.button.submit" var="submitLabel" />
				<input
					style="width: 700px; margin-left: 0px !important; margin-bottom: 15px; margin-top: 10px;"
					type="submit" value="${submitLabel}" name="submit"
					class="btn-green btn-status">
			</form:form>
		</div>
	</div>
</div>