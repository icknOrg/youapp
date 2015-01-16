<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<jsp:useBean id="profileUtility" type="youapp.utility.ProfileUtility"
	scope="application" />

<div id="conversations">
    <c:forEach items="${widgetConversations}" var="conversation">
    <div style="background:#F3F7FF;padding:5px;border-bottom:1px dotted #888;">
        <a href="${pageContext.request.contextPath}/messaging/showConversation.html?conversationGroupId=${conversation.conversationGroupId}" class="conversation ${conversation.newMessages ? 'new-messages' : ''}">
            <img alt="Profile Picture of last Sender" style="border-radius:50px;" src="${profileUtility.getProfileThumbnailUrl(conversation.conversationMessages[0].senderId)}">
        <span class="text" style="font-size:16px; color:#555!important;"> 
          <c:out value="${fn:substring(conversation.conversationMessages[0].text, 0, 150)}" />
        </span><br>
            <span class="conversation-participants"> 
                <c:forEach items="${conversation.conversationMembers}" var="conversationMember" varStatus="conversationMembersStatus">
                    <c:choose>
                        <c:when test="${conversationMember ne widgetOwnPersonId}">
                            <c:out value="${profileUtility.getProfileName(widgetConversationParticipantsMap[conversationMember], widgetIsConversationParticipantPublicMap[conversationMember])}" />
                        </c:when>
                        <c:otherwise>
                            <span class="sender"><spring:message code="messaging.showMyConversations.sender.me" /></span>
                        </c:otherwise>
                    </c:choose>
                    <c:if test="${!conversationMembersStatus.last}">
                        <c:out value=", " />
                    </c:if>
                </c:forEach>
             </span>
        </a>
        </div>
        <br>
    </c:forEach>
</div>	