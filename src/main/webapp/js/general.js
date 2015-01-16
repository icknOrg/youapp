$.extend({ alert: function (message, title) {
	  $("<div></div>").dialog( {
	    buttons: { "OK": function () { $(this).dialog("close"); } },
	    close: function (event, ui) { $(this).remove(); },
	    resizable: false,
	    draggable: false,
	    title: title,
	    modal: true,
	    width: 'auto'
	  }).html(message);
	}
});   

// http://jqueryui.com/demos/dialog/#option-buttons
$.extend({ confirm: function (message, title, buttons) {
	  $("<div></div>").dialog( {
	    buttons: buttons,
	    close: function (event, ui) { $(this).remove(); },
	    resizable: false,
	    draggable: false,
	    title: title,
	    modal: true,
	    width: 'auto'
	  }).html(message);
	}
});   

// for error handling. If error code is returned in any ajax-request
$.ajaxSetup({
	  error: function(xhr, status, error) {
		  var jsonObj = $.parseJSON(xhr.responseText);
		  $.alert(jsonObj.result.message, jsonObj.result.title);
	  }
});

$(document).ready(
        function() {
        	
        	$('#mailtoBugReport').click(function(){
        		document.location.href = "mailto:bugreport.youmeibd@gmail.com?subject=YouMeIBD Bug Report";
        	});
        	
        	
			$('button.send-message').click(function() {
				var thisButton = $(this);
				thisButton.addClass('loading-animation');
				thisButton.attr('disabled', 'disabled');
				
				var messageParticipant = thisButton.data('personid');
				
				if(typeof messageParticipant == "undefined"){
					messageParticipant = "";
				}
					
				var buttons = {};
				buttons[messages['messaging.sendMessage.button.send']] = function() {
			        var sendButton = $(this);
			        sendButton.addClass('loading-animation');
			
			        var form = $('form');
			        $.post(baseUrl + '/messaging/createMessage.html', { conversationParticipants: form.find('#as-values-conversationparticipants').val(), text:  form.find('#text').val()}, function(data){
			        	sendButton.dialog('close');
			        	$.alert(messages['messaging.sendMessage.successfullyAlert.message'], messages['messaging.sendMessage.successfullyAlert.title']);
			        }).complete(function() {
			            sendButton.removeClass('loading-animation');
			        });
				};
				buttons[messages['messaging.sendMessage.button.cancel']] = function() {$(this).dialog('close'); };
				
				$.get(baseUrl + '/messaging/createMessage.html?conversationParticipants='+ messageParticipant, function(data) {
			    	$(data).dialog({
			    		modal: true,
			    		resizable: true,
			    		draggable: true,
			    		title: messages['messaging.sendMessage.title'],
			    	    close: function (event, ui) { $(this).remove(); },
			    	    buttons: buttons,
			    	    width: 'auto'
			    	})
			    	.find('input#conversationParticipants').autoSuggest(
			    			baseUrl + '/autocomplete/searchPeople.html',
			    			{
			    				retrieveLimit: 5,
			    				queryParam: 'name',
			    				selectedItemProp: 'name',
			    				selectedValuesProp: 'id',
			    				searchObjProps: 'name',
			    				minChars: 2,
			    				startText: messages['messaging.sendMessage.conversationParticipantsAutocomplete.startText'],
			    				emptyText: messages['messaging.sendMessage.conversationParticipantsAutocomplete.emptyText'],
			    				asHtmlID: 'conversationparticipants',
			    				preFill: $(data).find('form').data('prefill'),
			    				formatList: function(data, elem){
			    					return elem.html('<img src="' + data.thumbnailUrl + '" />' + data.name);
			    				}
			    			}
			    	).focusout(function() {
			    		var val = $('#as-values-conversationparticipants').val();
			    		if(val.match(/\d+/g) == null || val.match(/[^\d,]+/g) != null) {
			    			$('.as-selection-item').remove();
			    			$('#as-values-conversationparticipants').val('');
			    			$(this).val('');
			    		}
			    		return true;
			    	});
			    }).complete(function() {
			        thisButton.removeClass('loading-animation');
			        thisButton.removeAttr('disabled');
			    });  
			});
});