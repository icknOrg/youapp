<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="">
<meta name="author" content="YouMeIBD-Admins">


<tiles:importAttribute name="title" />
<title><spring:message code="${title}" /></title>


<!-- Style sheets -
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/styles/style.css"
	media="screen, projection" />
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/styles/tempStyle.css"
	media="screen, projection" />
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/styles/jquery.tagit.css">
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/styles/autoSuggest.css">

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


<!-- scripts -->
	

<script type="text/javascript"
	src="${pageContext.request.contextPath}/javascript/globalVariables.html"></script>




<!-- jQuery -->
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>

<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/tag-it.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/general.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/jquery.scrollTo-1.4.0-min.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/jquery.placeholder.min.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/jquery.autoSuggest.modified.js"></script>
	
<script type="text/javascript">
	jQuery.fn.topLink = function(settings) {
		settings = jQuery.extend({
			min : 1,
			fadeSpeed : 200,
			ieOffset : 50
		}, settings);
		return this.each(function() {
			//listen for scroll
			var el = $(this);
			el.css('display', 'none'); //in case the user forgot
			$(window).scroll(
					function() {
						if (!jQuery.support.hrefNormalized) {
							el.css({
								'position' : 'absolute',
								'top' : $(window).scrollTop()
										+ $(window).height()
										- settings.ieOffset
							});
						}
						if ($(window).scrollTop() >= settings.min) {
							el.fadeIn(settings.fadeSpeed);
						} else {
							el.fadeOut(settings.fadeSpeed);
						}
					});
		});
	};

	$(document).ready(function() {
		$('#top-link').topLink({
			min : 400,
			fadeSpeed : 500
		});
		//smoothscroll
		$('#top-link').click(function(e) {
			e.preventDefault();
			$.scrollTo(0, 300);
		});
	});
</script>
<c:if test="${sessionScope.justLoggedIn && sessionScope.isRegistered}">
	<script type="text/javascript">
		$(document)
				.ready(
						function() {
							var buttons = {};
							buttons[messages['statusupdate.createLoginStatusUpdate.button.send']] = function() {
								var sendButton = $(this);
								sendButton.addClass('loading-animation');

								var description = $(
										'#create-login-status-update form textarea')
										.val();
								var moodRating = $(
										'#create-login-status-update form input[type=radio]:checked')
										.val();

								if (typeof moodRating == "undefined")
									moodRating = '';

								$
										.post(
												baseUrl
														+ '/statusupdate/createStatusUpdate.html',
												{
													description : description,
													moodRating : moodRating
												},
												function(data) {
													if ($.trim(data) !== "") {
														sendButton
																.dialog('close');
													}
												})
										.complete(
												function() {
													sendButton
															.removeClass('loading-animation');
												});
								;
							};

							$('#create-login-status-update').dialog({
								modal : true,
								resizable : true,
								draggable : true,
								buttons : buttons,
								width : 'auto',
								closeOnEscape : false,
								open : function(event, ui) {
									$(".ui-dialog-titlebar").hide();
								}
							});
						});
	</script>
</c:if>

<!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
<!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
        <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->

</head>
<body>

	<div id="wrapper">

		<div class="topheader">
			<a name="top"></a>
			<div id="topbar">
				<jsp:include page="header.jsp" />
			</div>
		</div>

		<!-- Main Container 960px-->
		<div id="page-wrapper">

			<div class="container">
				<tiles:insertAttribute name="content" />
			</div>

			<!-- /.panel-body -->
			<div class="panel-footer">
					<tiles:insertAttribute name="footer" />
			</div>
			<!-- /.panel-footer -->

		</div>

		<!--
		//Askes for current state of member 
		<c:if test="${sessionScope.justLoggedIn}">
			<div id="create-login-status-update" style="display: none;">
				<jsp:include
					page="../views/statusupdate/createLoginStatusUpdate.jsp" />DAS hier
			</div>
			<c:set var="justLoggedIn" value="false" scope="session" />
		</c:if>
		-->
	</div>



	<!-- Bootstrap Core JavaScript -->
	<script src="${pageContext.request.contextPath}/js/bootstrap.js"></script>

	<!-- Metis Menu Plugin JavaScript -->
	<script src="${pageContext.request.contextPath}/js/metisMenu.js"></script>

	<!-- Custom Theme JavaScript -->
	<script src="${pageContext.request.contextPath}/js/xsbAdmin.js"></script>


</body>
</html>