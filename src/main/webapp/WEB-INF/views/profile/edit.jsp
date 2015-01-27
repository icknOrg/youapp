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
		<b><span class="numberdescription"> <spring:message
				code="profile.show.firstName.label" />
		</span></b>
		<span class="numberchange" >
			<form:input path="firstName" class="form-control" /> <form:errors path="firstName"
				cssClass="error" />
		</span> 
	</div>
<b><span class="numberdescription"> <spring:message
				code="profile.show.lastName.label" />
		</span></b>
	<div class="numbers-column">
		<span class="numberchange" >
			<form:input path="lastName" class="form-control" /> <form:errors path="lastName"
				cssClass="error" />
		</span> 
	</div>
	
	<br>
	<div class="numbers-column clear">
<b>	<span class="numberdescription"><spring:message
				code="profile.show.location.label" /></span></b>
		<span class="numberchange" >
			<form:input path="location.name" class="form-control" id="location-name" /> <form:errors
				path="location" cssClass="error" />
		</span> 
		<form:hidden path="location.longitude" id="location-longitude" />
		<form:hidden path="location.latitude" id="location-latitude" />
		<form:hidden path="location.id" id="location-id"
			data-location-name="${person.location.name}" />
	</div>
	<br>
	<div class="numbers-column">
	<b><span class="numberdescription"><spring:message
				code="profile.show.birthday.label" /></span></b>
	<span id="birthday-wrapper" class="numberchange"
		> <spring:message
				code="profile.edit.birthday.format" var="birthdayFormat" /> <form:input class="form-control"
				path="birthday" placeholder="${birthdayFormat}" /> <form:errors
				path="birthday" cssClass="error" />
		</span> 
	</div>

	<br>
	<div class="numbers-column">
		<b><span class="numberdescription"><spring:message
				code="profile.show.gender.label" /></span></b>
				
		<form:select  class="form-control" path="gender">
			<form:option value="M">
				<spring:message code="profile.edit.gender.male" />
			</form:option>
			<form:option value="F">
				<spring:message code="profile.edit.gender.female" />
			</form:option>
		</form:select>
		<form:errors path="gender" cssClass="error" />
	</div>

	<br>
	<div class="numbers-column clear">
	<b>
		<span class="numberdescription"><spring:message
				code="profile.show.description.label" /></span></b>
		<span id="description_box" class="numberchange"> <spring:message
				code="profile.edit.description.placeholder"
				var="descriptionPlaceholder" /> <form:textarea  class="form-control" path="description"
				maxlength="160" placeholder="${descriptionPlaceholder}" /> <form:errors
				path="description" cssClass="error" />
		</span> 
		<spring:message code="profile.edit.description.label" /><br>
	</div>


	<h3 class="content-heading clear">
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
	<spring:message code="profile.edit.medications.label" />
	<div class="clear">
		<form:hidden path="medications" class="form-control" id="medications-field" />
		<ul id="medications-tagit"  ></ul>
		<form:errors path="medications" cssClass="error" />
	</div>
	
	<h3 class="content-heading" style="border-bottom: 0px solid #fff;">
		<spring:message code="profile.edit.symptoms.title" />
	</h3>
	<spring:message code="profile.edit.symptoms.label" />
	<div class="clear">
		<form:hidden path="symptoms" id="symptoms-field" />
		<ul id="symptoms-tagit"></ul>
		<form:errors path="symptoms" cssClass="error" />
	</div>
	
	<h3 class="content-heading" >
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
			<form:checkbox path="useMedicationMatching" style="margin-left:10px"></form:checkbox>
			Medication
		
	</div>
</form:form>