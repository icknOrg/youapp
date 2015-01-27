<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>

<jsp:useBean id="profileUtility" type="youapp.utility.ProfileUtility"
	scope="application" />

<h2 class="content-heading"><spring:message code="questions.create.headings.title"/></h2>
<form:form action="create.html" method="post" commandName="questionForm">
	<spring:hasBindErrors name="questionForm">
		<c:if test="${errors.globalErrorCount > 0}">
			<form:errors cssClass="error" />
		</c:if>
	</spring:hasBindErrors>
	
							
							<div class="form-group">
								<!--  <label class="col-md-3 control-label" for="message">Your message</label>
								<div class="col-md-9">-->
									<form:textarea type="text" class="form-control" rows="5" placeholder="Type your question here" path="question" />
									<form:errors path="question" cssClass="error" /><br />
							</div>
							<!--  </div>-->
							<div class="form-group">
								<!--<label class="col-md-3 control-label" for="name">Answer 1</label>
								<div class="col-md-9">-->
									<form:input id="answer" class="form-control" path="answerA" placeholder="Type here answer here" />
									<form:errors path="answerA" cssClass="error" /><br/>
								</div>
							<!-- </div> -->	
							<div class="fom-gropu">
							<!--	<label class="col-md-3 control-label" for="name">Answer 2</label>
								<div class="col-md-9">-->
									<form:input id="answer" class="form-control" path="answerB" placeholder="Type here answer here" />
									<form:errors path="answerB" cssClass="error" /><br />
								</div>
						<!-- </div> -->	
							<div class="fom-gropu">
							<!--	<label class="col-md-3 control-label" for="name">Answer 3</label>
								<div class="col-md-9">-->
									<form:input id="answer" class="form-control" path="answerC" placeholder="Type here answer here"  />
									<form:errors path="answerC" cssClass="error" /><br />
								</div>
						<!-- </div> -->	
							<div class="fom-gropu">
							<!--	<label class="col-md-3 control-label" for="name">Answer 4</label>
								<div class="col-md-9">-->
									<form:input id="answer" class="form-control" path="answerD" placeholder="Type here answer here"  />
									<form:errors path="answerD" cssClass="error" /><br />
								</div>
						<!-- </div> -->	
							
							<div class="form-group">
							
									<button class="btn btn-success" type="submit" name="submit"><spring:message code="questions.create.button.submit"/></button>
							
						</div>
		
</form:form>


<div class="panel panel-default"></div>

			<a	href="<c:out value='${pageContext.request.contextPath}/questions/showAll.html'/>">Deine erstellen Fragen<!-- <span class="bubble">23</span> --></a>
