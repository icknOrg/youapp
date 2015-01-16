<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<jsp:useBean id="profileUtility" type="youapp.utility.ProfileUtility"
	scope="application" />
<jsp:useBean id="timeUtility" type="youapp.utility.TimeUtility"
	scope="application" />

<c:forEach items="${conversation.conversationMessages}" var="message">
	<c:choose>
		<c:when test="${message.senderId ne ownPersonId}">
			<div class="message status-update" style="background:#F3F7FF; border-bottom:1px dotted #555;padding-top:10px;">
				<img style="border-radius:50px;" alt="Profile Picture of Sender"
					src="${profileUtility.getProfileThumbnailUrl(message.senderId)}">
				<c:choose>
				    <c:when test="${isConversationParticipantBlockedMap[message.senderId]}">
				        <c:out value="${profileUtility.getProfileName(conversationParticipantsMap[message.senderId], true)}" />
				    </c:when>
				    <c:otherwise>
				        <a href="${profileUtility.getProfileUrl(message.senderId)}">
				            <c:out value="${profileUtility.getProfileName(conversationParticipantsMap[message.senderId], isConversationParticipantPublicMap[message.senderId])}" />
				        </a>
				    </c:otherwise>
				</c:choose>
				<c:out value="${message.text}" />
				<br>
				<span style="font-size:9px;" title="${message.timestamp}"><c:out value="${timeUtility.getElapsedTime(message.timestamp, pageContext.response.locale)}"/></span>
			</div>
		</c:when>
		<c:otherwise>
			<div class="message own-message status-update" style="background:#F3F7FF; border-bottom:1px dotted #555;padding-top:10px;">
				<img style="border-radius:50px;" alt="Profile Picture of Sender"
					src="${profileUtility.getProfileThumbnailUrl(message.senderId)}">
				<spring:message code="messaging.showMessagesList.sender.me"/>
				<c:out value="${message.text}" />
				<br>
				<span style="font-size:9px;" title="${message.timestamp}"><c:out value="${timeUtility.getElapsedTime(message.timestamp, pageContext.response.locale)}"/></span>
			</div>
		</c:otherwise>
	</c:choose>
</c:forEach>