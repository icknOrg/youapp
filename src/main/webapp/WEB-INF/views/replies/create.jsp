<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<jsp:useBean id="profileUtility" type="youapp.utility.ProfileUtility" scope="application"/>
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


<div class="center-content">


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
    <button class="btn btn-start btn-fullwidth" id="save-question"><spring:message code="replies.create.button.submit"/></button>
    <button class="btn btn-start btn-fullwidth" id="skip-question"><spring:message code="replies.create.button.skip"/></button>
</div>
<br/>

<div class="panel panel-default"></div>

 <a class="btn btn-default" href="<c:out value='${pageContext.request.contextPath}/replies/showAll.html'/>"><spring:message
						code="navigation.answer.myAnswers" /></a>

	<a class="btn btn-default" href="<c:out value='${pageContext.request.contextPath}/replies/showSkipped.html'/>"><spring:message
						code="navigation.answer.skippedQuestions" /></a>
						
		
				
</div> 




<!-- old code -->
 <!--
<div class="center-content">
<!-- Main Content Central 940px + border
<div class="row">
<div class="col-md-3">
  <div class="side-rail left-rail">
    <div id="profile-picture" style="overflow: hidden; width:200px; height:200px; border-radius:100px; background-image: url(${profileUtility.getProfilePictureUrl(myProfile.id)}); background-position: center; background-size: cover;">
    </div>
    <a href="<c:out value='${pageContext.request.contextPath}/messaging/showMyConversations.html'/>"><i class="icon-envelope"></i> Messages <span class="badge badge-info">4</span></a>
    <br/>
  </div>
  <br/>
</div>
 

  
  <!-- Right Box 
  <div class="col-md-9">
  <!-- old code -->
  
 <!--  	<h2 style="border-bottom: 0px dotted #CCC;" class="content-heading">${myProfile.firstName}<spring:message code="statusupdate.showMyStream.create.howAreYou"/></h2>
		 <form id="create-status-update">
            <div id="smileys-buttons">
		        <input type="radio" name="radio" id="radio1" value="5"><label for="radio1"><img src="${pageContext.request.contextPath}/img/great.gif" class="emoticon"></label>
		        <input type="radio" name="radio" id="radio2" value="0"><label for="radio2"><img src="${pageContext.request.contextPath}/img/ok.gif" class="emoticon"></label>
		        <input type="radio" name="radio" id="radio3" value="-5"><label for="radio3"><img src="${pageContext.request.contextPath}/img/sad.gif" style="padding-right:15px;" class="emoticon"></label>
    	    </div>
            
             <spring:message code="statusupdate.showMyStream.create.message" var="message"/>   
             
             
            <textarea placeholder="${message}"></textarea>
          </form>  -->
          
<!--  new code 

		 <form id="create-status-update">
            <div id="smileys-buttons">
		        <input type="radio" name="radio" id="radio1" value="5"><label for="radio1"><img src="${pageContext.request.contextPath}/img/great.gif" class="emoticon"></label>
		        <input type="radio" name="radio" id="radio2" value="0"><label for="radio2"><img src="${pageContext.request.contextPath}/img/ok.gif" class="emoticon"></label>
		        <input type="radio" name="radio" id="radio3" value="-5"><label for="radio3"><img src="${pageContext.request.contextPath}/img/sad.gif" style="padding-right:15px;" class="emoticon"></label>
    	    </div> 
            <textarea placeholder="${myProfile.firstName}<spring:message code="statusupdate.showMyStream.create.howAreYou"/>" style="width:80%" rows="5" ></textarea>
          </form>

          <br/>
		  <button disabled="disabled" id="send-status-update" class="btn btn-success"><spring:message code="statusupdate.showMyStream.create.button.send"/></button>
		  </div>
		  </div>
				
					<!-- Tab Navigation 
	<jsp:include page="navigateProfile.jsp">
		<jsp:param name="showSite" value="create.jsp" />
	</jsp:include>
				
<!-- End Main Content 

<h2 class="content-heading"><spring:message code="replies.create.headings.title" /></h2>
<!-- <h3 class="content-heading"><spring:message code="replies.create.headings.subtitle"/></h3>  
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
    <button class="btn btn-success btn-fullwidth" id="save-question"><spring:message code="replies.create.button.submit"/></button>
    <button class="btn btn-start btn-fullwidth" id="skip-question"><spring:message code="replies.create.button.skip"/></button>
</div>
</div> -->


			