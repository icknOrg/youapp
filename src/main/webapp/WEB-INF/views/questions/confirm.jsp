<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<h1><spring:message code="questions.confirm.headings.title"/></h1>
<h2><spring:message code="questions.confirm.headings.subtitle.prompt"/></h2>
<table class="striped100">
    <tr>
    	<th class="graybackground">
    		#<c:out value="${questionForm.questionId}"/>&nbsp;
    		<c:out value="${questionForm.question}"/>
    	</th>
	</tr>
	<tr>
		<td>
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
<br />
<h2><spring:message code="questions.confirm.headings.subtitle.similar"/></h2>
<table class="striped100">
	<c:forEach items="${questionsList}" var="question">
    	<tr>
    		<td>
    			<table class="striped100">
    				<tr>
    					<th class="graybackground">
    						#<c:out value="${question.questionId}"/>&nbsp;
    						<c:out value="${question.question}"/>
    					</th>
    				</tr>
    				<tr>
    					<td>
    						<ul>
    							<c:if test="${not empty question.answerA}">
									<li><c:out value="${question.answerA}" /></li>
								</c:if>
								<c:if test="${not empty question.answerB}">
									<li><c:out value="${question.answerB}" /></li>
								</c:if>
								<c:if test="${not empty question.answerC}">
									<li><c:out value="${question.answerC}" /></li>
								</c:if>
								<c:if test="${not empty question.answerD}">
									<li><c:out value="${question.answerD}" /></li>
								</c:if>
    						</ul>
    					</td>
    				</tr>
    			</table>
    		</td>
    	</tr>
    </c:forEach>
</table>
<br />
<form:form cssClass="striped" action="confirm.html" method="post" commandName="questionForm">
	<form:hidden path="question" />
	<form:hidden path="answerA" />
	<form:hidden path="answerB" />
	<form:hidden path="answerC" />
	<form:hidden path="answerD" />
	<p>
		<input type="submit" name="create" value="<spring:message code="questions.confirm.button.create"/>" />
		<input type="submit" name="discard" value="<spring:message code="questions.confirm.button.discard"/>" />
	</p>
</form:form>