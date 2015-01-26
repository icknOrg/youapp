<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>







<!-- Navigation -->
<nav class="navbar navbar-default navbar-static-top" role="navigation"
	style="margin-bottom: 0">
	<div class="navbar-header">
		<button type="button" class="navbar-toggle" data-toggle="collapse"
			data-target=".navbar-collapse">
			<span class="sr-only">Toggle navigation</span> <span class="icon-bar"></span>
			<span class="icon-bar"></span> <span class="icon-bar"></span>
		</button>
		<div class="col-12 col-6">
		<a class="navbar-brand"

			href="<c:out value='${pageContext.request.contextPath}'/>"> <img
			class="logo" style="max-height: 37px; width: auto;" src="${pageContext.request.contextPath}/img/logo.png" />


		</a>

		
			<div class="input-group custom-search-form">
				<input class="form-control" type="text" placeholder="Seach...">
				<span class="input-group-btn">
					<button class="btn btn-default" type="button">
						<i class="fa fa-search"></i>	
					</button>
				</span>
			</div>
	 	
		
		
		
       
	</div>
	
	<div id="navbar" class="navbar-collapse collapse">
<form class="navbar-form navbar-right">




</form>
</div>

	</div>	

	<!-- /.navbar-header -->






	<tiles:insertAttribute name="menu" ignore="true" />
	<!-- /.navbar-static-side -->
</nav>