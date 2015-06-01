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
	   

<!-- Bootstrap Styles --->
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/styles/bootstrap.css">
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/styles/bootstrapAdmin.css">
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/styles/bootstrapFontAwesome.css">
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/styles/bootstrapMetisMenu.css">
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/styles/bootstrapMorris.css">
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/styles/bootstrapTimeline.css">
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/styles/ibd-components.css">
	


	    <!-- Google Fonts-->
    	<link href='https://fonts.googleapis.com/css?family=Indie+Flower' rel='stylesheet' type='text/css'>
    	<link href='https://fonts.googleapis.com/css?family=Open+Sans' rel='stylesheet' type='text/css'>

	    <!-- scripts -->
	    <script type="text/javascript" src="${pageContext.request.contextPath}/javascript/globalVariables.html"></script>
	    <script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.4/jquery.min.js" type="text/javascript"></script>
	    <script src="//ajax.googleapis.com/ajax/libs/jqueryui/1.11.2/jquery-ui.min.js" type="text/javascript"></script>
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

						$('#conversationParticipants')
						.autoSuggest(
								baseUrl
										+ '/autocomplete/searchPeople.html',
								{
									retrieveLimit: 5,
				    				queryParam : 'name',
									selectedItemProp : 'name',
									selectedValuesProp : 'id',
									searchObjProps : 'name',
									minChars : 2,
									startText : messages['messaging.sendMessage.conversationParticipantsAutocomplete.startText'],
									emptyText : messages['messaging.sendMessage.conversationParticipantsAutocomplete.emptyText'],
									asHtmlID : 'conversationparticipants',
									formatList : function(data, elem) {
										return elem
												.html('<img src="' + data.thumbnailUrl + '" />'
														+ data.name);
									},

								});
						$('#searchPersonBar')
						.autoSuggest(
								baseUrl
										+ '/autocomplete/searchPeople.html',
								{
									retrieveLimit : 5,
									queryParam : 'name',
									selectedItemProp : 'name',
									selectedValuesProp : 'id',
									searchObjProps : 'name',
									minChars : 2,
									startText : 'Search Person',
									emptyText : messages['messaging.sendMessage.conversationParticipantsAutocomplete.emptyText'],
									asHtmlID : 'searchPersonBar',
									formatList : function(data, elem) {
										return elem
												.html('<img src="' + data.thumbnailUrl + '" />'
														+ data.name);
									},
									selectionLimit : 1,
									resultClick : function(data) {
										window.location.href = baseUrl
												+ '/profile/show.html?personId='
												+ data.attributes.id;
									}
								});

			        });
		  </script>
		</c:if>
	
	</head>
	
	<body>
	
		<div id="wrapper">
	
		<div class="topheader"><a name="top"></a>
			<div id="topbar">
		    	<jsp:include page="header.jsp" />
			</div>
		</div>

		<!-- Main Container 960px-->	
		<div id="page-wrapper">

	
		<div class="container">
			<tiles:insertAttribute name="content" />
		</div>
		</div>

	<!-- /.panel-body -->
			<div class="panel-footer">
		
		<tiles:insertAttribute name="footer" />
</div>
	</div>

	<!-- Bootstrap Core JavaScript -->
	<script src="${pageContext.request.contextPath}/js/bootstrap.js"></script>

	<!-- Metis Menu Plugin JavaScript -->
	<script src="${pageContext.request.contextPath}/js/metisMenu.js"></script>

	<!-- Custom Theme JavaScript -->
	<script src="${pageContext.request.contextPath}/js/sbAdmin.js"></script>
	
	
	</body>
</html>
