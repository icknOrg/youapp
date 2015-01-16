<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page import="org.joda.time.DateTime" %>
<%@ page import="org.joda.time.Years" %>

<div class="numbers-column">
	<span class="numberchange"
		style="font-weight: 200 !important; padding-right: 15px;"><c:out
			value="${person.firstName}" /></span> <span class="numberdescription"><spring:message
			code="profile.show.firstName.label" /></span>
</div>
<div class="numbers-column">
	<span class="numberchange" style="font-weight: 200 !important;"><c:out
			value="${person.lastName}" /></span> <span class="numberdescription"><spring:message
			code="profile.show.lastName.label" /></span>
</div>
<div class="numbers-column">
	<span class="numberchange"
		style="font-weight: 200 !important; padding-right: 15px;"><c:out
			value="${person.location.name}" /></span> <span class="numberdescription"><spring:message
			code="profile.show.location.label" /></span>
</div>
<div class="numbers-column">
	
	<c:choose>
		<c:when test="${param.showOwnProfile}">
			<span class="numberchange" style="font-weight: 200 !important; padding-right: 15px;">
				<c:out value="${person.birthday}" />
			</span>
			<span class="numberdescription"><spring:message code="profile.show.birthday.label" /></span>
		</c:when>
		<c:otherwise>
 			<span class="numberchange" style="font-weight: 200 !important; padding-right: 15px;">
 				<c:out value="${person.age}" />
			</span> 
			<span class="numberdescription"><spring:message code="profile.show.age.label" /></span>
		</c:otherwise>
	</c:choose> 	
</div>
<br>
<br>
<div class="numbers-column">
	<span class="numberchange" style="font-weight: 200 !important;">
		<c:choose>
			<c:when test="${person.gender == 'M'}">
				<spring:message code="profile.show.gender.male" />
			</c:when>
			<c:otherwise>
				<spring:message code="profile.show.gender.female" />
			</c:otherwise>
		</c:choose>
	</span> <span class="numberdescription"><spring:message
			code="profile.show.gender.label" /></span>
</div>
<div class="clear"></div>
<br>
<div id="description_box">
	<textarea readonly class="numberchange"
		style="font-weight: 200 !important;">
	<c:out value="${person.description}" />
	</textarea>
</div>
max 140 chars. This information will appear in your public profile.
<br>
<br>
<h2 class="content-heading clear" style="border-bottom: 0px solid #fff;">
	<spring:message code="profile.show.headings.myTags" />
</h2>
<ul>
	<c:forEach items="${person.profileTags}" var="profileTag">
		<li><c:out value="${profileTag.name}" /></li>
	</c:forEach>
</ul>
<h2 class="content-heading" style="border-bottom: 0px solid #fff;">
	<spring:message code="profile.show.headings.mySymptoms" />
</h2>

<ul>
	<c:forEach items="${person.symptoms}" var="symptom">
		<li><c:out value="${symptom.name}" /></li>
	</c:forEach>
</ul>

<h2 class="content-heading" style="border-bottom: 0px solid #fff;">
	<spring:message code="profile.show.headings.myMedications" />
</h2>
<ul>
	<c:forEach items="${person.medications}" var="medication">
		<li><c:out value="${medication.name}" /></li>
	</c:forEach>
</ul>

<h2 class="content-heading" style="border-bottom: 0px solid #fff;">
	<spring:message code="profile.show.headings.Matching" />
</h2>
<h2 style="border-bottom: 20px;">
	<form:checkbox path="person.useQuestionMatching"
		value="${person.useQuestionMatching}" disabled="true" label="Quiz"
		style="margin-left:10px " />
	<form:checkbox path="person.useFBMatching"
		value="${person.useFBMatching}" disabled="true" label="Facebook info"
		style="margin-left:10px" />
	<form:checkbox path="person.useDistanceMatching"
		value="${person.useDistanceMatching}" disabled="true" label="Distance"
		style="margin-left:10px" />
	<form:checkbox path="person.useSymptomsMatching"
		value="${person.useSymptomsMatching}" disabled="true" label="Symptoms"
		style="margin-left:10px" />
	<form:checkbox path="person.useMedicationMatching"
		value="${person.useMedicationMatching}" disabled="true"
		label="Medication" style="margin-left:10px" />
</h2>



