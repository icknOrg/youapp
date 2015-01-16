<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<jsp:useBean id="profileUtility" type="youapp.utility.ProfileUtility" scope="application"/>

<script type="text/javascript">
$(document).ready(
        function() { 
            $('button.unblock-person').click(function() {
                var thisButton = $(this);
                
                var buttons = {};
                buttons[messages['soulmates.showMySoulmates.unblock.confirmDialog.ok']] = function() {
                    thisButton.addClass('loading-animation');       
                    $(this).dialog('close');
                    $.get('${pageContext.request.contextPath}/blocker/unblockPerson.html?blockedId=' + thisButton.data('personid'), function(data) {
                        if (data == "") {
                            thisButton.parent('.person-blocked').remove();
                            if($('#people-blocked .person-blocked').length == 0) {
                            	$('#people-blocked').remove();
                            }
                        }
                    }).complete(function() {
                        thisButton.removeClass('loading-animation');
                    });;
                };
                buttons[messages['soulmates.showMySoulmates.unblock.confirmDialog.cancel']] = function() {$(this).dialog('close'); };
                
                $.confirm(messages['soulmates.showMySoulmates.unblock.confirmDialog.message'], messages['soulmates.showMySoulmates.unblock.confirmDialog.title'], buttons);
            });
            
            $('button.confirm-soulmates-request').click(function() {
                var thisButton = $(this);
                thisButton.addClass('loading-animation');
                $.get(baseUrl + '/soulmates/createSoulmatesRequest.html?requestedId=' + thisButton.data('personid'), function(data) {
                    if (data == "") {
                          location.reload();
                    }
                }).complete(function() {
                    thisButton.removeClass('loading-animation');
                });;
            });
            
            $('button.decline-soulmates-request').click(function() {
                var thisButton = $(this);
                thisButton.addClass('loading-animation');
                $.get(baseUrl + '/soulmates/deleteSoulmates.html?personBId=' + thisButton.data('personid'), function(data) {
                    if (data == "") {
                        thisButton.parents('.person-requested').remove();
                        if($('#people-requested .person-requested').length == 0) {
                            $('#people-requested').remove();
                        }
                    }
                    thisButton.removeClass('loading-animation');
                }).complete(function() {
                    thisButton.removeClass('loading-animation');
                });;
            });
});
</script>

<c:if test="${not empty requestedPeopleList}">
    <div id="people-requested">
        <h2 class="content-heading"><spring:message code="soulmates.showMySoulmates.title.soulmateRequests"/></h2>
        <c:forEach items="${requestedPeopleList}" var="personRequested">
            <div class="soulmate person-requested">
                <div class="soulmate">
                    <a href="${profileUtility.getProfileUrl(personRequested.id)}"> <img
                        src="${profileUtility.getProfileThumbnailUrl(personRequested.id)}"
                        alt="profile thumbnail" style="border-radius:50px;"/> <span class="namelist"><c:out
                                value="${personRequested.firstName} ${personRequested.lastName} (@${personRequested.nickName})" /></span>
                    </a>
                    <button data-personid="${personRequested.id}" class="send-message"><spring:message code="soulmates.showMySoulmates.button.sendMessage"/></button>
                    <button data-personid="${personRequested.id}" class="confirm-soulmates-request"><spring:message code="soulmates.showMySoulmates.button.confirmSoulmatesRequest"/></button>
                    <button data-personid="${personRequested.id}" class="decline-soulmates-request"><spring:message code="soulmates.showMySoulmates.button.declineSoulmatesRequest"/></button>
                </div>
            </div>
        </c:forEach> 
    </div>
</c:if>

<c:choose>
<c:when test="${not empty soulmatesList}">
    <h2 class="content-heading"><spring:message code="soulmates.showMySoulmates.title.soulmates"/></h2>
	<div id="soulmates">
	    <c:forEach items="${soulmatesList}" var="soulmate">
		    <div class="soulmate">
		        <div class="matchinglist">
		            <div class="stat_number">
		                <span class="hours"><fmt:formatNumber type="percent"
		                        maxFractionDigits="0" value="${matchesMap[soulmate.id].finalScore}" /></span>
		            </div>
		        </div>
		        <div style="clear: both;">
		            <a style="float: left;" href="${profileUtility.getProfileUrl(soulmate.id)}"> 
		              <img src="${profileUtility.getProfileThumbnailUrl(soulmate.id)}" style="border-radius:50px;" alt="profile thumbnail" />
		              <span class="namelist"><c:out value="${soulmate.firstName} ${soulmate.lastName} (@${soulmate.nickName})" /></span>
		            </a>
		            <br>
		            <span style="display: block; float: left; margin-left: -107px;" class="description"> 
                      <c:out value="${soulmate.description}" />
                    </span>
                    <br>
		            <c:if test="${not empty statusUpdatesMap[soulmate.id]}">
			            <span style="display:block; margin-left: -107px; float:left;" class="last-status-update">
				            <c:choose>
				                <c:when test="${soulmate.gender == 'M'}">
				                    The last time he said: 
				                </c:when>
				                <c:otherwise>
				                    The last time she said:
    				            </c:otherwise> 
				            </c:choose>
				            <c:out value="${statusUpdatesMap[soulmate.id].description}" />
		                </span>
	                </c:if>
		           
		       
		        </div>
		    </div>
		      <button class="btn-message-list btn-green send-message" data-personid="${soulmate.id}"><spring:message code="soulmates.showMySoulmates.button.sendMessage"/></button>
	    </c:forEach> 
	</div>
</c:when>
<c:otherwise>
<spring:message code="soulmates.showMySoulmates.noSoulmatesAvailable" arguments="${pageContext.request.contextPath}/matches/showFindSoulmates.html"/>
</c:otherwise>
</c:choose>

<c:if test="${not empty personBlockedList}">
	<div id="people-blocked">
	    <h2 class="content-heading"><spring:message code="soulmates.showMySoulmates.title.blockedPeople"/></h2>
	    <ul>
	        <c:forEach items="${personBlockedList}" var="personBlocked">
	            <li class="person-blocked">
	                <c:out value="${personBlocked.firstName} (@${personBlocked.nickName})"/><br>
	                <button data-personid="${personBlocked.id}" class="unblock-person">
	                    <spring:message code="soulmates.showMySoulmates.unblock.button"/>
	                </button>
	            </li>
	        </c:forEach>
	    </ul>
	</div>
</c:if>