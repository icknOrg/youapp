<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<h2 class="content-heading"><spring:message code="questions.showAll.headings.title"/></h2>
<h3 class="content-heading"><spring:message code="questions.showAll.headings.subtitle"/></h3>

<c:forEach items="${questionsList}" var="question">
<div class="bluediv">     
          <h4 class="questiontitle"><c:out value="${question.question}"/></h4>

            <ul>
				<c:if test="${not empty question.answerA}">
				    <li>
				        <c:out value="${question.answerA}" />
                    </li>
                </c:if>
			
                <c:if test="${not empty question.answerB}">
                    <li>
                        <c:out value="${question.answerB}" />
                    </li>
                </c:if>
			
                <c:if test="${not empty question.answerC}">
                    <li>
                        <c:out value="${question.answerC}" />
                    </li>
                </c:if>
                
                <c:if test="${not empty question.answerD}">
                    <li>
                        <c:out value="${question.answerD}" />
                    </li>
                </c:if>
            </ul>
</div>  
    </c:forEach>