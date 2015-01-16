<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<c:choose>
	<c:when test="${empty discardedMessage && not empty questionForm}">
		<h1><spring:message code="questions.show.headings.title.question" />&nbsp;#<c:out value="${questionForm.questionId}" /></h1>
		<table class="striped100">
			<tr>
				<td class="whiteborder"><spring:message code="questions.show.created.label" />:</td>
				<td class="whiteborder"><fmt:formatDate value="${questionForm.created}" pattern="MM/dd/yyyy" /></td>
			</tr>
			<tr>
				<td class="whiteborder"><spring:message code="questions.show.author.label" />:</td>
				<td class="whiteborder"><c:out value="${questionForm.authorNick}" /></td>
			</tr>
		</table>
		<br />
		<table class="striped100">
			<tr>
				<th class="graybackground"><spring:message code="questions.show.question.label" />:</th>
			</tr>
			<tr>
				<td class="whiteborder"><c:out value="${questionForm.question}" />
				</td>
			</tr>
			<tr>
				<th class="graybackground"><spring:message code="questions.show.answers.label" />:</th>
			</tr>
			<tr>
				<td class="whiteborder">
					<ul>
						<c:if test="${not empty questionForm.answerA}">
							<li><c:out value="${questionForm.answerA}" /></li>
						</c:if>
						<c:if test="${not empty questionForm.answerB}">
							<li><c:out value="${questionForm.answerB}" /></li>
						</c:if>
						<c:if test="${not empty questionForm.answerC}">
							<li><c:out value="${questionForm.answerC}" /></li>
						</c:if>
						<c:if test="${not empty questionForm.answerD}">
							<li><c:out value="${questionForm.answerD}" /></li>
						</c:if>
					</ul>
				</td>
			</tr>
		</table>
	</c:when>
	<c:when test="${not empty discardedMessage}">
		<!-- If the discarded message is set, the message has been discarded by the user or by the system (because it was not valid) -->
		<h1><spring:message code="questions.show.headings.title.discarded" /></h1>
		<h2><c:out value="${discardedMessage}" /></h2>
	</c:when>
</c:choose>