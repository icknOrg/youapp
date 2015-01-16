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
            $('button#edit-profile').click(function() {                   
                button = $(this);
            	button.addClass('loading-animation');
            	
            	$.get(baseUrl + '/profile/edit.html', function(data) {
                    $('div#profile').html(data);
                    
                    button.hide();
                    $('button#save-profile').show();
                }).complete(function() {
                	button.removeClass('loading-animation');
                });
            });
            
            
            $('button#save-profile').click(function() {                   
                button = $(this);
            	button.addClass('loading-animation');
                
                $.post(baseUrl + '/profile/edit.html', $('form').serialize(), function(data) {
                    $('div#profile').html(data);
                    
                    if($('div#profile .error').size() == 0) {
                        button.hide();
                        $('button#edit-profile').show();
                    }
                }).complete(function() {
                    button.removeClass('loading-animation');
                });;
                
            });
            
            $('button#update-picture').click(function() {
                button = $(this);
            	button.addClass('loading-animation');
            	
            	$.get(baseUrl + '/profile/updateProfilePicture.html', function(data) {
                	// Update image view, with Date force Browser to reload image
                	d = new Date();
                	$("#profile-picture").css('background-image', "url('"+ data +"&" + d.getTime() + "')");
                }).complete(function() {
                    button.removeClass('loading-animation');
                });;
            });
});
</script>

<!-- Main Content Central 940px + border-->
<div class="center-content">
  <div class="side-rail left-rail">
  	<div id="profile-picture" style="overflow: hidden; margin-left:30px; width:150px; height:150px; border-radius:100px; background-image: url(${profileUtility.getProfilePictureUrl(person.id)}); background-position: center; background-size: cover;">
    </div>
    <button style="position:relative; top:20px; left:42px; width: 107px; height: 100%;" class="btn-fb btn-small" id="update-picture"><spring:message code="profile.show.button.updatePicture" /></button>
  </div>

  <!-- Right Box -->
  <div class="right-content">
	<!-- Right Content-->
    <div class="content">
  		<div class="about-right">
  			<h2 class="content-heading"><c:out value="${person.nickName}" /><spring:message code="profile.show.headings.yourProfile" /></h2>
  		</div>
  		<div class="numbers" style="border-bottom:0px solid #eee;">
          	<div  class="numbers-column"><span class="number" style="font-weight: 200 !important;"><c:out value="${sinceRegistered}" /></span><span class="numberdescription" style="padding-right:20px;"><spring:message code="profile.show.numbers.sinceRegistered" /></span></div>
          	<div class="numbers-column"><span class="number" style="font-weight: 200 !important;"><c:out value="${numberOfSoulmates}" /></span><span class="numberdescription"><spring:message code="profile.show.numbers.soulmates" /></span></div>
          	<div class="clear"></div>
  		</div>
  		<br>
  		<h2 class="content-heading"><spring:message code="profile.show.headings.personal" /></h2>
  		<div id="profile" class="numbers" style="border-bottom:0px solid #eee;">
  			
   			<jsp:include page="showProfilePart.jsp">
  				  <jsp:param name="showOwnProfile" value="${true}" />
  			</jsp:include>
  			 
  		</div>
  		<button class="btn-green btn-start btn-fullwidth" id="save-profile" class="floatr" style="display: none;">Save</button>
  		<button class="btn-green btn-start btn-fullwidth" id="edit-profile" class="floatr">Edit</button>
  	</div>
  </div>
</div> <!-- /center-content -->
