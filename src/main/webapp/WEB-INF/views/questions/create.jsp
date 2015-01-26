<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>



<jsp:useBean id="profileUtility" type="youapp.utility.ProfileUtility"
	scope="application" />



<!-- Main Content Central 940px + border-->
<div class="center-content">
  <div class="side-rail left-rail">
    <div id="profile-picture" style="overflow: hidden; width:200px; height:200px; border-radius:100px; background-image: url(${profileUtility.getProfilePictureUrl(myProfile.id)}); background-position: center; background-size: cover;">
    </div>
   </div>
</div>
<!-- End Main Content -->


<form:form action="create.html" method="post" commandName="questionForm">
	<spring:hasBindErrors name="questionForm">
		<c:if test="${errors.globalErrorCount > 0}">
			<form:errors cssClass="error" />
		</c:if>
	</spring:hasBindErrors>
	
	<!-- Tab Navigation -->
	<jsp:include page="navigateProfile.jsp">
		<jsp:param name="showSite" value="create.jsp" />
	</jsp:include>


  
<!-- new Code -->
	<div class= "container">
		<div class="row">
			<div class="col-md-6 col-md-offset-3">
				<div class="well well-sm">
					<form class="form-horizontal" method="post" action="">
						<fieldset>
						<div class="panel panel-primary">
		<div class="panel-heading">
			<h3 class="panel-title">
								<spring:message code="questions.create.headings.title"/>
							</h3>	
		</div>
	</div>
							
							
							<div class="form-group">
								<label class="col-md-3 control-label" for="message">Your message</label>
								<div class="col-md-9">
									<form:textarea type="text" class="form-control" rows="5" placeholder="Type your question here" path="question" />
									<form:errors path="question" cssClass="error" /><br />
							</div>
							</div>
							<div class="form-group">
								<label class="col-md-3 control-label" for="name">Answer 1</label>
								<div class="col-md-9">
									<form:input id="answer" class="form-control" path="answerA" placeholder="Type here" />
									<form:errors path="answerA" cssClass="error" /><br/>
								</div>
								</div>
							<div class="fom-gropu">
								<label class="col-md-3 control-label" for="name">Answer 2</label>
								<div class="col-md-9">
									<form:input id="answer" class="form-control" path="answerB" placeholder="Type here" />
									<form:errors path="answerB" cssClass="error" /><br />
								</div>
							</div>
							<div class="fom-gropu">
								<label class="col-md-3 control-label" for="name">Answer 3</label>
								<div class="col-md-9">
									<form:input id="answer" class="form-control" path="answerC" placeholder="Type here"  />
									<form:errors path="answerC" cssClass="error" /><br />
								</div>
							</div>
							<div class="fom-gropu">
								<label class="col-md-3 control-label" for="name">Answer 4</label>
								<div class="col-md-9">
									<form:input id="answer" class="form-control" path="answerD" placeholder="Type here"  />
									<form:errors path="answerD" cssClass="error" /><br />
								</div>
							</div>
							
							<div class="form-group">
							<div class="col-md-9"></div>
								<div class="col-md-3">
									<button class="btn btn-success" type="submit" name="submit"><spring:message code="questions.create.button.submit"/></button>
								</div>
							</div>
						</fieldset>
						</form>
				</div>
				
			</div>
		
		</div>
	</div>





</form:form>