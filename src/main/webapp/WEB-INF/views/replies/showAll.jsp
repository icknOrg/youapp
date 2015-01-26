<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<script type="text/javascript">
	$(document).ready(
			function() {
				$('button.edit-question').click(function() {
                    var button = $(this);
                    button.addClass('loading-animation');		
                    
					var parent = $(this).parent();
					//get the id of the question
					var questionId = parent.data('questionid');
					var insert = parent.find("ul");
				
					$(parent).find(".delete-question").attr('disabled', 'disabled');
					
					//get the edit form of the question with the questionId
					$.get(baseUrl + '/replies/edit.html?selectedquestion='+ questionId, function(data) {
						insert.html(data);
    
                        $(parent).find(".edit-question").hide();
                        $(parent).find(".save-question").show();
					}).complete(function() {
	                    button.removeClass('loading-animation');+
	                    $(parent).find(".delete-question").removeAttr('disabled');
	                });;				
				});

				$('button.save-question').click(function() {
				    var button = $(this);
	                button.addClass('loading-animation');       
					
					var parent = $(this).parent();
					var form = parent.find("form");
				    var insert = parent.find("ul");
				    
				    $(parent).find(".delete-question").attr('disabled', 'disabled');
				    
				    // post the form input to the controller
				    $.post(baseUrl + '/replies/edit.html', form.serialize(), function(data){
				    	insert.html(data);

                        if(!insert.find('.error').length > 0) {
                            $(parent).find(".edit-question").show();
                            $(parent).find(".save-question").hide();
                        }
                        
                        $(parent).find(".loading-animation").hide();
				    }).complete(function() {
	                    button.removeClass('loading-animation');
	                    $(parent).find(".delete-question").removeAttr('disabled');
	                });
				    
				    
				});
				
				$('button.delete-question').click(function(){
				    var button = $(this);
				    
				    var buttons = {};
				    buttons[messages['replies.showAll.remove.confirmDialog.ok']] = function() {
                        button.addClass('loading-animation');       
                        $(this).dialog('close');
                         var parent = button.parent();
                         //get the id of the question
                         var questionId = parent.data('questionid');
                         
                         $(parent).find(".save-question").attr('disabled', 'disabled');
                         $(parent).find(".edit-question").attr('disabled', 'disabled');
                         
                         $.get(baseUrl + '/replies/delete.html?selectedquestion='+ questionId, function(data) {
                             button.removeClass('loading-animation');
                             parent.slideUp("normal", function() {parent.remove();});
                         }).error(function() {
                             button.removeClass('loading-animation');
                             $(parent).find(".save-question").removeAttr('disabled');
                             $(parent).find(".edit-question").removeAttr('disabled');
                         });
				    };
				    buttons[messages['replies.showAll.remove.confirmDialog.cancel']] = function() {$(this).dialog('close'); };
                    
				    $.confirm(messages['replies.showAll.remove.confirmDialog.message'], messages['replies.showAll.remove.confirmDialog.title'], buttons);
				});
			});
</script>
<h2 class="content-heading"><spring:message code="replies.showAll.headings.title"/></h2>
<h3 class="content-heading"><spring:message code="replies.showAll.headings.subtitle"/></h3>


