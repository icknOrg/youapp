<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<c:if test="${not empty question.answerA}">
	<li <c:if test="${reply.answerA}"> class="selected"</c:if>><c:out
			value="${question.answerA}" /></li>
</c:if>
<c:if test="${not empty question.answerB}">
	<li <c:if test="${reply.answerB}"> class="selected"</c:if>><c:out
			value="${question.answerB}" /></li>
</c:if>
<c:if test="${not empty question.answerC}">
	<li <c:if test="${reply.answerC}"> class="selected"</c:if>><c:out
			value="${question.answerC}" /></li>
</c:if>
<c:if test="${not empty question.answerD}">
	<li <c:if test="${reply.answerD}"> class="selected"</c:if>><c:out
			value="${question.answerD}" /></li>
</c:if>
<!-- as reserve for other implementations -->
<c:if test="${not empty question.answerE}">
	<li <c:if test="${reply.answerE}"> class="selected"</c:if>><c:out
			value="${question.answerE}" /></li>
</c:if>