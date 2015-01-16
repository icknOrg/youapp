<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<script type="text/javascript">
$(document).ready(
        function() { 
		$("#radio").buttonset();
	});
</script>

<form:form cssClass="reply" action="edit.html" method="post"
	commandName="reply">
	<spring:hasBindErrors name="reply">
		<c:if test="${errors.globalErrorCount > 0}">
			<form:errors cssClass="error" />
		</c:if>
	</spring:hasBindErrors>
	<fieldset>
		<div id="question_box_wrapper">
			<div id="question_box">
				<p>
					<legend>
						<c:out value="${question.question}" />
					</legend>
				</p>
			</div>
		</div>
		<form:hidden path="questionId" />
		<form:hidden path="personId" />
		<form:hidden path="skipped" />
		<form:hidden path="inPrivate" />
		<form:hidden path="critical" />
		<form:hidden path="lastUpdate" />

		<fieldset>
			<legend>
				<spring:message code="replies.create.importance.label" />
			</legend>
			<div id="radio">
				<c:forEach var="weight" items="${weights}">
					<form:radiobutton cssClass="radio_button" path="importance"
						value="${weight.weight}" label="${weight.description}" />
				</c:forEach>
			</div>
			<form:errors path="importance" cssClass="error" />

		</fieldset>
		<form:radiobutton cssClass="radio_button" path="answerIndex" value="0"
			label="${question.answerA}" />
		<form:radiobutton cssClass="radio_button" path="answerIndex" value="1"
			label="${question.answerB}" />
		<c:if test="${not empty question.answerC}">
			<form:radiobutton cssClass="radio_button" path="answerIndex"
				value="2" label="${question.answerC}" />
			<c:if test="${not empty question.answerD}">
				<form:radiobutton cssClass="radio_button" path="answerIndex"
					value="3" label="${question.answerD}" />
				<c:if test="${not empty question.answerE}">
					<form:radiobutton cssClass="radio_button" path="answerIndex"
						value="4" label="${question.answerE}" />

				</c:if>
			</c:if>
		</c:if>
		<form:errors path="questionId" cssClass="error" />
		<form:errors path="answerIndex" cssClass="error" />
	</fieldset>

</form:form>
