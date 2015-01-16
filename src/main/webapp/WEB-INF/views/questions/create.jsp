 <%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<h2 class="content-heading"><spring:message code="questions.create.headings.title"/></h2>
<form:form action="create.html" method="post" commandName="questionForm">
	<spring:hasBindErrors name="questionForm">
		<c:if test="${errors.globalErrorCount > 0}">
			<form:errors cssClass="error" />
		</c:if>
	</spring:hasBindErrors>
<div id="search_box2">
<form:textarea  type="text" placeholder="Type your question here" path="question"/>
</div>
<form:errors path="question" cssClass="error" />
<br />
<div id="answer_box">
		<form:input  id="answer" path="answerA" placeholder="Answer 1 goes here" />
		<br />
		<form:errors path="answerA" cssClass="error" />
	    <br />
		<form:input  id="answer" path="answerB" placeholder="Answer 2 goes here"/>
		<br />
		<form:errors path="answerB" cssClass="error" />
		<br />
		<form:input  id="answer" path="answerC" placeholder="Answer 3 goes here"/>
		<br />
		<form:errors path="answerC" cssClass="error" />
      	<br />
		<form:input  id="answer" path="answerD" placeholder="Answer 4 goes here"/>
		<br />
		<form:errors path="answerD" cssClass="error" />
</div>
<p><input class="btn-green btn-start btn-fullwidth" type="submit" name="submit" value="<spring:message code="questions.create.button.submit"/>" /></p>

</form:form>