<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/replyForm.js"></script>
<script type="text/javascript">  
    function insertData(data){
    	if($.trim(data) !== ""){    	
    		$('#question').html(data);
    		$('#message-no-more-questions').hide();
    		$('#buttons').show();
    	}else{
    		$('#question').empty();
    		$('#message-no-more-questions').show();
    		$('#buttons').hide();
    	}
    }
    
    $(document).ready(
            function() {
                $('button#save-question').click(function() {                   
                	var button = $(this);
                    button.addClass('loading-animation');
                    $('button#skip-question').attr('disabled', 'disabled');

                    var form = $('#question form');
                    $.post(baseUrl + '/replies/reply.html', form.serialize(), function(data){
                    	insertData(data);
                    }).complete(function() {
                        button.removeClass('loading-animation');
                        $('button#skip-question').removeAttr('disabled');
                    });
                });
                
                $('button#skip-question').click(function(){
                    var button = $(this);
                    button.addClass('loading-animation');
                	$('button#save-question').attr('disabled', 'disabled');
                    
                    var questionId = $('#questionId').val();
                    $.get(baseUrl + '/replies/skip.html?selectedquestion='+ questionId, function(data) {
                    	insertData(data);
                    }).complete(function() {
                        button.removeClass('loading-animation');
                        $('button#save-question').removeAttr('disabled');
                    });     
                 });

            });
</script>

<h2 class="content-heading"><spring:message code="replies.create.headings.title" /></h2>
<h3 class="content-heading"><spring:message code="replies.create.headings.subtitle"/></h3>
<p id="message-no-more-questions" style="display:none;"><spring:message code="replies.create.empty.label"/></p>
<div id="question">
	<c:choose>
	<c:when test="${hasQuestions}">
		<jsp:include page="edit.jsp"></jsp:include>
	</c:when>
	<c:otherwise>
		<script type="text/javascript">
			$(document).ready(
	            function() {
	        		$('#message-no-more-questions').show();
	        		$('#buttons').hide();
	        });
		</script>
	</c:otherwise>
	</c:choose>
</div>
<br />
<div id="buttons">
    <button class="btn-green btn-start btn-fullwidth" id="save-question"><spring:message code="replies.create.button.submit"/></button>
    <button class="btn-yellow btn-start btn-fullwidth" id="skip-question"><spring:message code="replies.create.button.skip"/></button>
</div>