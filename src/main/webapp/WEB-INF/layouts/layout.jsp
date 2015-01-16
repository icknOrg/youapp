<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <tiles:importAttribute name="title" />
        <title><spring:message code="${title}"/></title>
        
        <!-- Style sheets -->
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/styles/style.css" media="screen, projection" />  
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/styles/tempStyle.css" media="screen, projection" /> 
	    <link rel="stylesheet" type="text/css" href="//ajax.googleapis.com/ajax/libs/jqueryui/1.8/themes/ui-lightness/jquery-ui.css">
	   	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/styles/jquery.tagit.css">
	   	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/styles/autoSuggest.css">
	    
	    <!-- Google Fonts-->
    	<link href='https://fonts.googleapis.com/css?family=Indie+Flower' rel='stylesheet' type='text/css'>
    	<link href='https://fonts.googleapis.com/css?family=Open+Sans' rel='stylesheet' type='text/css'>
	    
	    <!-- scripts -->
	    <script type="text/javascript" src="${pageContext.request.contextPath}/javascript/globalVariables.html"></script>
	    <script src="//ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js" type="text/javascript"></script>
	    <script src="//ajax.googleapis.com/ajax/libs/jqueryui/1.8.18/jquery-ui.min.js" type="text/javascript"></script>
	    <script type="text/javascript" src="${pageContext.request.contextPath}/js/tag-it.js"></script>
	    <script type="text/javascript" src="${pageContext.request.contextPath}/js/general.js"></script>      
	    <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.scrollTo-1.4.0-min.js"></script>  	
	    <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.placeholder.min.js"></script>
	    <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.autoSuggest.modified.js"></script>
		<script type="text/javascript">
		jQuery.fn.topLink = function(settings) {
			settings = jQuery.extend({
				min: 1,
				fadeSpeed: 200,
				ieOffset: 50
			}, settings);
			return this.each(function() {
				//listen for scroll
				var el = $(this);
				el.css('display','none'); //in case the user forgot
				$(window).scroll(function() {
					if(!jQuery.support.hrefNormalized) {
						el.css({
							'position': 'absolute',
							'top': $(window).scrollTop() + $(window).height() - settings.ieOffset
						});
					}
					if($(window).scrollTop() >= settings.min)
					{
						el.fadeIn(settings.fadeSpeed);
					}
					else
					{
						el.fadeOut(settings.fadeSpeed);
					}
				});
			});
		};
		
		$(document).ready(function() {
			$('#top-link').topLink({
				min: 400,
				fadeSpeed: 500
			});
			//smoothscroll
			$('#top-link').click(function(e) {
				e.preventDefault();
				$.scrollTo(0,300);
			});
		});
		</script>
		<c:if test="${sessionScope.justLoggedIn && sessionScope.isRegistered}">
		  <script type="text/javascript">
		  $(document).ready(
			        function() {  
		                var buttons = {};
		                buttons[messages['statusupdate.createLoginStatusUpdate.button.send']] = function() {
		                    var sendButton = $(this);
		                    sendButton.addClass('loading-animation');
		                    
		                    var description = $('#create-login-status-update form textarea').val();
		                    var moodRating = $('#create-login-status-update form input[type=radio]:checked').val();
		                    
		                    if(typeof moodRating == "undefined") 
		                        moodRating = '';
		                    
		                    $.post(baseUrl + '/statusupdate/createStatusUpdate.html', {description: description , moodRating: moodRating}, function(data) {
		                    	if ($.trim(data) !== "") {
		                        	sendButton.dialog('close');                      
		                        }
		                    }).complete(function() {
		                        sendButton.removeClass('loading-animation');
		                    });;
		                };
			        	
			         $('#create-login-status-update').dialog({
	                        modal: true,
	                        resizable: true,
	                        draggable: true,
	                        buttons: buttons,
	                        width: 'auto',
	                        closeOnEscape: false,
	                        open: function(event, ui) { $(".ui-dialog-titlebar").hide(); }
	                  });
			        });
		  </script>
		</c:if>
	</head>
	<body>
		<div class="topheader"><a name="top"></a>
			<div id="topbar">
		    	<jsp:include page="header.jsp" />
			</div>
		</div>
		
		<!-- Main Container 960px-->    
		<div class="container">
			<tiles:insertAttribute name="content" />
		</div>
		
		<tiles:insertAttribute name="footer" />
		
		<c:if test="${sessionScope.justLoggedIn}">
		  <div id="create-login-status-update" style="display: none;">
		      <jsp:include page="../views/statusupdate/createLoginStatusUpdate.jsp" />
		  </div>
		  <c:set var="justLoggedIn" value="false" scope="session" />
		</c:if>
	</body>
</html>