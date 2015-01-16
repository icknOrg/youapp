<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<div class="divnavigation">
    <ul class="menu gray">
        <li><a href="<c:out value='${pageContext.request.contextPath}/profile/show.html'/>"><img src="${pageContext.request.contextPath}/img/user_9x12.png" alt="Me" /><spring:message code="navigation.me" /><!-- <span class="bubble-purple">4</span> --></a>
            <!-- start level 2 -->
            <ul>
                <li><a href="<c:out value='${pageContext.request.contextPath}/profile/show.html'/>"><spring:message code="navigation.me.myProfile" /></a></li>
                <li><a href="<c:out value='${pageContext.request.contextPath}/soulmates/showMySoulmates.html'/>"><spring:message code="navigation.me.mySoulmates" /></a></li>
            </ul>
        </li>
        <li><a href="<c:out value='${pageContext.request.contextPath}/messaging/showMyConversations.html'/>"><img src="${pageContext.request.contextPath}/img/chat_12x12.png" alt="Conversation" /><spring:message code="navigation.conversations" /></a></li>
        <li><a href="<c:out value='${pageContext.request.contextPath}/questions/create.html'/>"><img src="${pageContext.request.contextPath}/img/comment_alt1_fill_12x12.png" alt="Question" /><spring:message code="navigation.question" /></a>
            <!-- start level 2 -->
            <ul>
                <li><a href="<c:out value='${pageContext.request.contextPath}/questions/create.html'/>"><spring:message code="navigation.question.createQuestion" /></a></li>
                <li><a href="<c:out value='${pageContext.request.contextPath}/questions/showAll.html'/>"><spring:message code="navigation.question.myQuestions" /><!-- <span class="bubble">23</span> --></a></li>
            </ul>
        </li>
        <li><a href="<c:out value='${pageContext.request.contextPath}/replies/create.html'/>"><img src="${pageContext.request.contextPath}/img/check_12x10.png" alt="Answers" /><spring:message code="navigation.answer" /><!-- <span class="bubble-blue">6</span> --></a>
            <!-- start level 2 -->
            <ul>
                <li><a href="<c:out value='${pageContext.request.contextPath}/replies/create.html'/>"><spring:message code="navigation.answer.answerQuestion" /></a></li>
                <li><a href="<c:out value='${pageContext.request.contextPath}/replies/showAll.html'/>"><spring:message code="navigation.answer.myAnswers" /></a></li>
                <li><a href="<c:out value='${pageContext.request.contextPath}/replies/showSkipped.html'/>"><spring:message code="navigation.answer.skippedQuestions" /><!-- <span class="bubble">4</span> --></a></li>
            </ul>
        </li>
        <li><a href="${pageContext.request.contextPath}/matches/showFindSoulmates.html"><img src="${pageContext.request.contextPath}/img/star_12x12.png" alt="Soulmates" /><spring:message code="navigation.findSoulmates" /></a></li>
        
        <!-- if logged in show logout button, otherwise show login button -->
        <c:choose>
              <c:when test='${(not empty sessionScope.isRegistered) && (sessionScope.isRegistered == true)}'>
                  <li class="floatr"><a href="<c:out value='${pageContext.request.contextPath}/logout/bye.html'/>"><spring:message code="navigation.general.logout" /></a></li>
              </c:when>
              <c:otherwise>
                  <li class="floatr"><a href="<c:out value='${pageContext.request.contextPath}/login/register.html'/>"><spring:message code="navigation.general.login"/></a></li>
              </c:otherwise>
        </c:choose>
    </ul>
</div>
