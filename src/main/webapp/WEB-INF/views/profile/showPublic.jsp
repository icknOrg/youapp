<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>

<jsp:useBean id="profileUtility" type="youapp.utility.ProfileUtility" scope="application"/>

<script type="text/javascript">
$(document).ready(
        function() {   
            $('button#send-soulmates-request').click(function() {
                var button = $(this);
            	button.addClass('loading-animation');
            	$.get(baseUrl + '/soulmates/createSoulmatesRequest.html?requestedId=' + button.data('personid'), function(data) {
            		if (data == "") {
            			  button.hide();
            			  $('#sent-soulmates-request').show();
            		}
                	button.removeClass('loading-animation');
                }).complete(function() {
                    button.removeClass('loading-animation');
                });;
            });
            
            $('button#cancel-soulmates-request').click(function() {
                var button = $(this);
                button.addClass('loading-animation');
                $.get(baseUrl + '/soulmates/deleteSoulmates.html?personBId=' + button.data('personid'), function(data) {
                    if (data == "") {
                    	$('#confirm-buttons').hide();
                    	$('#send-buttons').show();
                    	
                    	$('#sent-soulmates-request').hide();
                        $('#send-soulmates-request').show();
                    }
                    button.removeClass('loading-animation');
                }).complete(function() {
                    button.removeClass('loading-animation');
                });;
            });
            
            $('button#confirm-soulmates-request').click(function() {
                var button = $(this);
                button.addClass('loading-animation');
                $.get(baseUrl + '/soulmates/createSoulmatesRequest.html?requestedId=' + button.data('personid'), function(data) {
                    if (data == "") {
                    	  $('#confirm-buttons').hide();
                          location.reload();
                    }
                    button.removeClass('loading-animation');
                }).complete(function() {
                    button.removeClass('loading-animation');
                });;
            });
            
            $('button#block-person').click(function() {
                var button = $(this);
                
                var buttons = {};
                buttons[messages['profile.show.block.confirmDialog.ok']] = function() {
                    $(this).dialog('close');
                    
                    button.addClass('loading-animation');
                    $.get(baseUrl + '/blocker/blockPerson.html?blockedId=' + button.data('personid'), function(data) {
                        if (data == "") {
                            button.attr('disabled', 'disabled');
                            window.location.replace(baseUrl);
                        }
                        button.removeClass('loading-animation');
                    }).complete(function() {
                        button.removeClass('loading-animation');
                    });;
                };
                buttons[messages['profile.show.block.confirmDialog.cancel']] = function() {$(this).dialog('close'); };
                
                $.confirm(messages['profile.show.block.confirmDialog.message'], messages['profile.show.block.confirmDialog.title'], buttons);
            });
});
</script>

<!-- Main Content Central 940px + border-->
<div class="center-content">
  <div class="side-rail left-rail">
  	<div id="profile-picture" style="overflow: hidden; width:200px; height:200px; border-radius:100px; background-image: url(${profileUtility.getProfilePictureUrl(person.id)}); background-position: center; background-size: cover;">
    </div>
        <div id="send-buttons" style="display: ${!confirmSoulmatesRequest ? 'block' : 'none'}">
		    <button data-personid="${person.id}" id="send-soulmates-request" style="display: ${!isSoulmatesRequestSent ? 'block' : 'none'}"><spring:message code="profile.show.soulmates.button.addSoulmate" /></button>
		    
		    <span id="sent-soulmates-request" style="display: ${isSoulmatesRequestSent ? 'block' : 'none'}">
		        <spring:message code="profile.show.soulmates.soulmatesRequestPending"/>
		        <button data-personid="${person.id}" id="cancel-soulmates-request"><spring:message code="profile.show.soulmates.button.cancelSoulmatesRequest"/></button>
		    </span>
        </div>
        
        <div id="confirm-buttons" style="display: ${confirmSoulmatesRequest ? 'block' : 'none'}">
            <button data-personid="${person.id}" id="confirm-soulmates-request"><spring:message code="profile.show.soulmates.button.confirmSoulmatesRequest"/></button>
            <button data-personid="${person.id}" id="cancel-soulmates-request"><spring:message code="profile.show.soulmates.button.declineSoulmatesRequest"/></button>
        </div>
        
        <button data-personid="${person.id}" id="block-person"><spring:message code="profile.show.block.button.blockPerson"/></button>
  </div>

  <!-- Right Box -->
  <div class="right-content">
	<!-- Right Content-->
    <div class="content">
  		<div class="about-right">
  			<h2 class="content-heading"><c:out value="${person.nickName}" /></h2>
  		</div>
  		<div class="numbers" style="border-bottom:0px solid #eee;">
          	<div class="numbers-column"><span class="number"><c:out value="${sinceRegistered}" /></span><span class="numberdescription"><spring:message code="profile.show.numbers.sinceRegistered" /></span></div>
          	<div class="numbers-column"><span class="number"><c:out value="${numberOfSoulmates}" /></span><span class="numberdescription"><spring:message code="profile.show.numbers.soulmates" /></span></div>

          	<div class="clear"></div>
          	<br>
  		</div>
  		<h2 class="content-heading"><spring:message code="profile.show.headings.personal" />
  		</h2>
  		<div id="profile" class="numbers" style="border-bottom:0px solid #eee;">
  			<jsp:include page="showProfilePart.jsp"></jsp:include>
  		</div>
  	</div>
  </div>
</div> <!-- /center-content -->
