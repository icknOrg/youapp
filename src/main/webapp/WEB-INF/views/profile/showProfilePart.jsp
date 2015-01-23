<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page import="org.joda.time.DateTime"%>
<%@ page import="org.joda.time.Years"%>





<div class="row">
	<!-- Own description -->
	<div class="col-xs-6 col-md-4">
		<div class="profile-description">
			<blockquote class="blockquote-reverse">

				<c:choose>
					<c:when test="${not empty person.description}">
						<c:out value="${person.description}" />
					</c:when>
					<c:otherwise>
						<span><spring:message code="profile.show.description.error" /></span>
					</c:otherwise>
				</c:choose>
			</blockquote>
		</div>

	</div>
	<!-- Information like Name, Home, Birthday -->
	<div class="col-xs-12 col-sm-6 col-md-8">
		<div class="numbers-column">
			<span class="numberdescription"><spring:message
					code="profile.show.firstName.label" /></span>: <b><span
				class="numberchange"><c:out value="${person.firstName}" /></span></b>

		</div>

		<div class="numbers-column">
			<span class="numberdescription"><spring:message
					code="profile.show.lastName.label" /></span>: <b> <span
				class="numberchange"><c:out value="${person.lastName}" /></span></b>
		</div>

		<div class="numbers-column">
			<span class="numberdescription"><spring:message
					code="profile.show.location.label" /></span>: <b><span
				class="numberchange"><c:out value="${person.location.name}" /></span>
			</b>
		</div>
		<div class="numbers-column">

			<c:choose>
				<c:when test="${param.showOwnProfile}">
					<span class="numberdescription"><spring:message
							code="profile.show.birthday.label" /></span>:
			<b><span class="numberchange"> <c:out
								value="${person.birthday}" />
					</span></b>
				</c:when>
				<c:otherwise>
					<span class="numberdescription"><spring:message
							code="profile.show.age.label" /></span>:
			<b><span class="numberchange"> <c:out
								value="${person.age}" />
					</span></b>
				</c:otherwise>
			</c:choose>
		</div>
		<div class="numbers-column">
			<span class="numberdescription"><spring:message
					code="profile.show.gender.label" /></span>: <b> <span
				class="numberchange"> <c:choose>
						<c:when test="${person.gender == 'M'}">
							<spring:message code="profile.show.gender.male" />
						</c:when>
						<c:otherwise>
							<spring:message code="profile.show.gender.female" />
						</c:otherwise>
					</c:choose>
			</span></b>
		</div>
	</div>
</div>
<h3 class="content-heading clear">
	<spring:message code="profile.show.headings.myTags" />
</h3>
<ul>
	<c:forEach items="${person.profileTags}" var="profileTag">
		<li><c:out value="${profileTag.name}" /></li>
	</c:forEach>
</ul>
<h3 class="content-heading">
	<spring:message code="profile.show.headings.mySymptoms" />
</h3>

<ul>
	<c:forEach items="${person.symptoms}" var="symptom">
		<li><c:out value="${symptom.name}" /></li>
	</c:forEach>
</ul>

<h3>
	<spring:message code="profile.show.headings.myMedications" />
</h3>
<ul>
	<c:forEach items="${person.medications}" var="medication">
		<li><c:out value="${medication.name}" /></li>
	</c:forEach>
</ul>

<h3 class="content-heading">
	<spring:message code="profile.show.headings.Matching" />
</h3>

<b><form:checkbox path="person.useQuestionMatching"
		value="${person.useQuestionMatching}" disabled="true" label="Quiz"
		style="margin-left:10px " /></b>

<b><form:checkbox path="person.useFBMatching"
		value="${person.useFBMatching}" disabled="true" label="Facebook info"
		style="margin-left:10px" /></b>

<b><form:checkbox path="person.useDistanceMatching"
		value="${person.useDistanceMatching}" disabled="true" label="Distance"
		style="margin-left:10px" /></b>

<b><form:checkbox path="person.useSymptomsMatching"
		value="${person.useSymptomsMatching}" disabled="true" label="Symptoms"
		style="margin-left:10px" /></b>

<b><form:checkbox path="person.useMedicationMatching"
		value="${person.useMedicationMatching}" disabled="true"
		label="Medication" style="margin-left:10px" /></b>



