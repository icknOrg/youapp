<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<script type="text/javascript">
$(document).ready(function(){
    // de-and activate send button by form content 
    $('#add-message #text').bind('change keyup', function() {
        var text = $('#add-message #text').val();
        
        if(text == "") {
        	$('#add-message button').attr('disabled', 'disabled');
        } else {
        	$('#add-message button').removeAttr('disabled');
        }
    });
	
	$('#add-message button').click(function() {
    	var thisButton = $(this);
    	thisButton.addClass('loading-animation');
    	
        $.post(baseUrl + '/messaging/createMessageByGroupId.html', { conversationGroupId: thisButton.data('conversationgroupid'), text: $('#add-message #text').val() }, function(data){
            if($.trim(data) !== ""){
          		$(data).hide().appendTo('#messages').slideDown(600);
            }
            
            $('#add-message #text').val('');
            thisButton.attr('disabled', 'disabled');
        }).complete(function() {
            thisButton.removeClass('loading-animation');
        });
    });
	
	   $('#show-older-messages').click(function() {
	        var thisButton = $(this);
	        thisButton.addClass('loading-animation');
	        
	        var conversationGroupId =$('#messages').data('conversationgroupid'); 
            var startDate = $('#messages').data('firstmessage');
            var offset = $('#messages').data('offset');
            var resultSize = $('#messages').data('resultsize');
	        
	        $.get(baseUrl + '/messaging/showMessagesList.html', { conversationGroupId: conversationGroupId, startDate: startDate, offset: offset, resultSize: resultSize }, function(data){
	            if($.trim(data) !== ""){
	                $(data).filter('.message').hide().prependTo('#messages').show("highlight", null, 1000);
	                
	                $('#messages').data('offset', offset + resultSize);

	                // If in the response are less messages then it should be, there are probably no more messages.
                    if($(data).filter('.message').length < resultSize) {
                    	thisButton.remove();
                    }
                } else {
	            	thisButton.remove();
	            }
	        }).complete(function() {
	            thisButton.removeClass('loading-animation');
	        });
	    });
	   
       $('#clear-conversation').click(function() {
           var thisButton = $(this);
           
           var buttons = {};
           buttons[messages['messaging.showConversation.clearConversation.confirmDialog.ok']] = function() {
               $(this).dialog('close');
               thisButton.addClass('loading-animation');
               
               var conversationGroupId = thisButton.data('conversationgroupid'); 
               
               $.post(baseUrl + '/messaging/clearConversation.html', { conversationGroupId: conversationGroupId }, function(data) {
                   if(data == ""){
                	   window.location.replace(baseUrl + '/messaging/showMyConversations.html');                   
                   } 
               }).complete(function() {
                   thisButton.removeClass('loading-animation');
               });
           };
           buttons[messages['messaging.showConversation.clearConversation.confirmDialog.cancel']] = function() {$(this).dialog('close'); };
           
           $.confirm(messages['messaging.showConversation.clearConversation.confirmDialog.message'], messages['messaging.showConversation.clearConversation.confirmDialog.title'], buttons);
       });
});
</script>
<h2 class="content-heading"><spring:message code="messaging.showConversation.title.conversationWith"/>
	<c:forEach items="${conversation.conversationMembers}"
		var="conversationParticipantId" varStatus="conversationMembersStatus">
		<c:if test="${conversationParticipantId ne ownPersonId}">
			<c:out
				value="${profileUtility.getProfileName(conversationParticipantsMap[conversationParticipantId], isConversationParticipantPublicMap[conversationParticipantId])}" />
			<c:if test="${!conversationMembersStatus.last}">
				<c:out value=", " />
			</c:if>
		</c:if>
	</c:forEach>
</h2>

<c:if test="${fn:length(conversation.conversationMessages) > 0}">
    <button id="clear-conversation" data-conversationgroupid="${conversation.conversationGroupId}"><spring:message code="messaging.showConversation.clearConversation.button"/></button>
</c:if>
<c:if test="${fn:length(conversation.conversationMessages) >= messagesResultSize}">
    <button id="show-older-messages"><spring:message code="messaging.showConversation.showOlderMessages.button"/></button>
</c:if>
<div id="messages" data-conversationgroupid="${conversation.conversationGroupId}" data-firstmessage="${newestMessageTimestamp}" data-offset="${messagesOffset}" data-resultsize="${messagesResultSize}">
    <c:choose>
        <c:when test="${fn:length(conversation.conversationMessages) > 0}">
            <jsp:include page="showMessagesList.jsp"/>
        </c:when>
        <c:otherwise>
            <spring:message code="messaging.showConversation.noMessagesAvailable"/>
        </c:otherwise>
    </c:choose>
</div>
<br>
<div id="add-message" data-conversationgroupid="${conversation.conversationGroupId}">
    <textarea name="text" id="text"></textarea>
    <button class="btn-green btn-message" disabled="disabled" data-conversationgroupid="${conversation.conversationGroupId}" >
    <spring:message code="messaging.showConversation.sendMessage.button"/></button>
</div>
