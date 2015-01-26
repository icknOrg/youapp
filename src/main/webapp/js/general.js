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
        	
        	
});