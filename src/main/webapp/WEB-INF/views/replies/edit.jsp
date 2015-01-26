<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<jsp:useBean id="profileUtility" type="youapp.utility.ProfileUtility" scope="application"/>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/replyForm.js"></script>
<script type="text/javascript">
$(document).ready(
        function() { 
		$("#radio").buttonset();
	});
</script>

<form:form cssClass="reply" action="edit.html" method="post"
	commandName="reply">
	<spring:hasBindErrors name="reply">
		<c:if test="${errors.globalErrorCount > 0}">
			<form:errors cssClass="error" />
		</c:if>
	</spring:hasBindErrors>
	
		<div id="question_box_wrapper">
			<div id="question_box">
				<p>
				
				</p>
			</div>
		</div>
		<form:hidden path="questionId" />
		<form:hidden path="personId" />
		<form:hidden path="skipped" />
		<form:hidden path="inPrivate" />
		<form:hidden path="critical" />
		<form:hidden path="lastUpdate" />
		
		
<div class="container">
	<div class="row">
	
	<div class="panel panel-success">
		<div class="panel-heading">
			<h3 class="panel-title"><c:out value="${question.question}" /></h3>	
		</div>
	</div>

	<div class="row">
		<div class="col-md-6">
		<div class="panel panel-primary">
			<div class="panel-heading">
				<h3 class="panel-title">
					<spring:message code="replies.create.importance.label" />
				</h3>
			</div>
		
			<legend></legend>
			
			<div class="radio">
				<c:forEach var="weight" items="${weights}">
				<label class="btn-group btn-group-justified">
					<form:radiobutton cssClass="radio_button" path="importance"
						value="${weight.weight}" label="${weight.description}" />
						</label>
				</c:forEach>
			</div>

			<form:errors path="importance" cssClass="error" />

		
		</div>
		</div>
		
		

		
		<div class="col-md-6">
		<div class="panel panel-primary">
			<div class="panel-heading">
				<h3 class="panel-title">Meine Antwort ist...</h3>
			</div>
	<legend></legend>		
		<div class="radio">
			

		
		<label class="btn-group btn-group-justified">
		<form:radiobutton cssClass="radio_button" path="answerIndex" value="0"
			label="${question.answerA}" />
	
	</label>
	
		<label class="btn-group btn-group-justified">
		<form:radiobutton cssClass="radio_button" path="answerIndex" value="1"
			label="${question.answerB}" />
		</label>
		
	
			
		<c:if test="${not empty question.answerC}">
		<label class="btn-group btn-group-justified">
			<form:radiobutton cssClass="radio_button" path="answerIndex"
				value="2" label="${question.answerC}" />
		
			
			</label>
		
			<c:if test="${not empty question.answerD}">
			<label class="btn-group btn-group-justified">	
				<form:radiobutton cssClass="radio_button" path="answerIndex"
					value="3" label="${question.answerD}" />
						</label>
				<c:if test="${not empty question.answerE}">
				<label class="btn-group btn-group-justified">
					<form:radiobutton cssClass="radio_button" path="answerIndex"
						value="4" label="${question.answerE}" />
	</label>
				</c:if>
				
			</c:if>
		
		</c:if>
		<br/>
	
		<form:errors path="questionId" cssClass="error" />
		<form:errors path="answerIndex" cssClass="error" />
	</div>
	</div>

</div>
	
</div>
	

</div>


	</div>
	
	
	
	
</form:form>
