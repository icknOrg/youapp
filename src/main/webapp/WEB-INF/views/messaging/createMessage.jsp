<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<div id="create-message">
	<form data-prefill='${preFillConversationParticipants}'>
		<input type="text" id="conversationParticipants" name="conversationParticipants" />
		<spring:message code="messaging.sendMessage.text" var="placeholderText"/>
		<textarea id="text" name="text" placeholder="${placeholderText}"><c:out value="${text}"/></textarea>
	</form>
</div>