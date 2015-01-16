<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt"%>
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
    $("#birthday-wrapper").birthdaypicker({
    	hiddenDate: "birthday",
    	defaultDate: true
    });
    $('#birthday').hide();
	tagit($('#symptoms-tagit'), $('#symptoms-field'), [${allSymptoms}]);
	tagit($('#medications-tagit'), $('#medications-field'), [${allMedications}]);
	tagit($('#profiletags-tagit'), $('#profiletags-field'), [${allProfileTags}]);
	locationAutocomplete();
});
</script>

<form:form action="edit.html" method="post" commandName="person">
	<spring:hasBindErrors name="person">
		<c:if test="${errors.globalErrorCount > 0}">
			<form:errors cssClass="error" />
		</c:if>
	</spring:hasBindErrors>

	<div class="numbers-column">
		<span class="numberchange" style="font-weight: 200 !important;">
			<form:input path="firstName" /> <form:errors path="firstName"
				cssClass="error" />
		</span> <span class="numberdescription"> <spring:message
				code="profile.show.firstName.label" />
		</span>
	</div>

	<div class="numbers-column">
		<span class="numberchange" style="font-weight: 200 !important;">
			<form:input path="lastName" /> <form:errors path="lastName"
				cssClass="error" />
		</span> <span class="numberdescription"> <spring:message
				code="profile.show.lastName.label" />
		</span>
	</div>
	<br>
	<br>
	<div class="numbers-column clear">
		<span class="numberchange" style="font-weight: 200 !important;">
			<form:input path="location.name" id="location-name" /> <form:errors
				path="location" cssClass="error" />
		</span> <span class="numberdescription"><spring:message
				code="profile.show.location.label" /></span>
		<form:hidden path="location.longitude" id="location-longitude" />
		<form:hidden path="location.latitude" id="location-latitude" />
		<form:hidden path="location.id" id="location-id"
			data-location-name="${person.location.name}" />
	</div>
	<br>
	<br>
	<div class="numbers-column">
		<span id="birthday-wrapper" class="numberchange"
			style="font-weight: 200 !important;"> <spring:message
				code="profile.edit.birthday.format" var="birthdayFormat" /> <form:input
				path="birthday" placeholder="${birthdayFormat}" /> <form:errors
				path="birthday" cssClass="error" />
		</span> <span class="numberdescription"><spring:message
				code="profile.show.birthday.label" /></span>
	</div>
	<br>
	<br>
	<div class="numbers-column">
		<form:select path="gender">
			<form:option value="M">
				<spring:message code="profile.edit.gender.male" />
			</form:option>
			<form:option value="F">
				<spring:message code="profile.edit.gender.female" />
			</form:option>
		</form:select>
		<form:errors path="gender" cssClass="error" />
		<span class="numberdescription"><spring:message
				code="profile.show.gender.label" /></span>
	</div>
	<br>
	<br>
	<div class="numbers-column clear">
		<span id="description_box" class="numberchange"
			style="font-weight: 200 !important;"> <spring:message
				code="profile.edit.description.placeholder"
				var="descriptionPlaceholder" /> <form:textarea path="description"
				maxlength="160" placeholder="${descriptionPlaceholder}" /> <form:errors
				path="description" cssClass="error" />
		</span>
		<spring:message code="profile.edit.description.label" />
		<span class="numberdescription"><spring:message
				code="profile.show.description.label" /></span> <br>
	</div>


	<h2 class="content-heading clear"
		style="border-bottom: 0px solid #fff;">
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
			<form:checkbox path="useMedicationMatching" style="margin-left:10px"></form:checkbox>
			Medication
		</h2>
	</div>
</form:form>