<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<jsp:useBean id="profileUtility" type="youapp.utility.ProfileUtility" scope="application"/>

<script type="text/javascript">
$(document).ready(
        function() {  
            $('button#send-status-update').click(function() {
                var button = $(this);
                button.addClass('loading-animation');
                
                var description = $('form#create-status-update textarea').val();
                var moodRating = $('form#create-status-update input[type=radio]:checked').val();
                
                if(typeof moodRating == "undefined") 
                	moodRating = '';
                
                $.post(baseUrl + '/statusupdate/createStatusUpdate.html', {description: description , moodRating: moodRating}, function(data) {
                    if ($.trim(data) !== "") {
                        $(data).hide().prependTo('#status-updates').slideDown(1000);                    	  
                    }
                    
                    // Reset the form
                    $('form#create-status-update')[0].reset();
                    $('button#send-status-update').attr('disabled', 'disabled');
                }).complete(function() {
                    button.removeClass('loading-animation');
                });;
            });
            
            // de-and activate send button by form content 
            $('form#create-status-update input[type=radio], form#create-status-update textarea').bind('change keyup', function() {
                var description = $('form#create-status-update textarea').val();
                var moodRating = $('form#create-status-update input[type=radio]:checked').val();
                
                if(typeof moodRating == "undefined" && description == "") {
                	 $('button#send-status-update').attr('disabled', 'disabled');
                } else {
                	$('button#send-status-update').removeAttr('disabled');
                }
            });
            
           $('#show-more-status-updates').click(function() {
        	  var thisButton = $(this);
        	  thisButton.addClass('loading-animation'); 
        	   
        	  var startDate = $('#status-updates').data('firststatusupdate');
        	  var offset = $('#status-updates').data('offset');
        	  var resultSize = $('#status-updates').data('resultsize');
        	  
        	  $.get(baseUrl + '/statusupdate/showStatusUpdatesList.html?startDate=' + startDate + '&offset=' + offset + '&resultSize=' + resultSize, function(data) {
        		 if($.trim(data) !== "") {
        			 $(data).appendTo('#status-updates');
                     
        			 $('#status-updates').data('offset', offset + resultSize);
                     
                     // If in the response are less status updates then it should be, there are probably no more status updates.
                     if($(data).filter('.status-update').length < resultSize) {
                    	 $('#no-more-status-updates').show();
                         thisButton.remove();
                     }
        		 } else {
                     $('#no-more-status-updates').show();
                     thisButton.remove();
        		 }
        	  }).complete(function() {
        		  thisButton.removeClass('loading-animation');
        	  });
           });
           
           $('.status-update .delete-status-update').live('click', (function() {
        	   var button = $(this);
               
               var buttons = {};
               buttons[messages['statusupdate.showMyStream.delete.confirmDialog.ok']] = function() {
                   button.addClass('loading-animation');       
                   $(this).dialog('close');
                   
                   var statusUpdateTimeStamp = button.data('statusupdatetimestamp');
                   var parent = $(this).parents('.status-update');
                   
                   $.get(baseUrl + '/statusupdate/deleteStatusUpdate.html?when=' + statusUpdateTimeStamp, function(data) {
                       if(data === "") {
                           parent.slideUp("normal",function() {parent.remove();});
                       }
                   }).complete(function() {
                       button.removeClass('loading-animation');
                   });
               };
               buttons[messages['statusupdate.showMyStream.delete.confirmDialog.cancel']] = function() {$(this).dialog('close'); };
               
               $.confirm(messages['statusupdate.showMyStream.delete.confirmDialog.message'], messages['statusupdate.showMyStream.delete.confirmDialog.title'], buttons);
           }));
        });
</script>

<!-- Main Content Central 940px + border-->
<div class="center-content">
  <div class="side-rail left-rail">
    <div id="profile-picture" style="overflow: hidden; width:200px; height:200px; border-radius:100px; background-image: url(${profileUtility.getProfilePictureUrl(myProfile.id)}); background-position: center; background-size: cover;">
    </div>
    
    <jsp:include page="../widgets/showLastConversations.jsp"/>
  </div>

  <!-- Right Box -->
  <div class="right-content">
    <!-- Right Content-->
    <div class="content">
		<div class="about-right">
		  <h2 style="border-bottom: 0px dotted #CCC; padding-left:190px;" class="content-heading">${myProfile.firstName}<spring:message code="statusupdate.showMyStream.create.howAreYou"/></h2>

		  <form id="create-status-update">
            <div id="smileys-buttons">
		        <input type="radio" name="radio" id="radio1" value="5"><label for="radio1"><img src="${pageContext.request.contextPath}/img/great.gif" class="emoticon"></label>
		        <input type="radio" name="radio" id="radio2" value="0"><label for="radio2"><img src="${pageContext.request.contextPath}/img/ok.gif" class="emoticon"></label>
		        <input type="radio" name="radio" id="radio3" value="-5"><label for="radio3"><img src="${pageContext.request.contextPath}/img/sad.gif" style="padding-right:15px;" class="emoticon"></label>
    	    </div>
            
            <spring:message code="statusupdate.showMyStream.create.message" var="message"/>
            <textarea placeholder="${message}"></textarea>
          </form>
		  <button disabled="disabled" id="send-status-update"><spring:message code="statusupdate.showMyStream.create.button.send"/></button>
		
		<c:choose> 
			<c:when test="${not empty statusUpdates}">   
			
		
				<div id="status-updates" class="clear" data-firststatusupdate="${statusUpdates[0].when}" data-offset="${statusupdatesOffset}" data-resultsize="${statusupdatesResultSize}">
		            <jsp:include page="showStatusUpdatesList.jsp"/>
				</div>
				
				<span id="no-more-status-updates" style="display: none;">
				   <spring:message code="statusupdate.showMyStream.loadMoreStatusUpdates.noMoreAvailable"/>
				</span>
				<button id="show-more-status-updates">
				   <spring:message code="statusupdate.showMyStream.loadMoreStatusUpdates.showMore"/>
				</button>
			</c:when>
			<c:otherwise>
			    <div id="status-updates">
			    </div>
				<span id="no-status-updates">
				  <spring:message code="statusupdate.showMyStream.noStatusUpdatesAvailable"/>
				</span>
			</c:otherwise>
		</c:choose>
    </div>
  </div>
 </div>
</div> <!-- /center-content -->
