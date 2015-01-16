function tagit(tagitEl, singleFieldEl, tags) {
	tagitEl.tagit({
			availableTags: tags,
			singleField: true,
			singleFieldNode: singleFieldEl,
			allowSpaces: true,
			caseSensitive: false
	});
}
	
function locationAutocomplete() {
   $('#location-name').autocomplete({
		source: function( request, response ) {
			$.getJSON(baseUrl + "/autocomplete/searchLocation.html", {locationNameSearchTerm: request.term}, function( data ) 
				{
					response( $.map( data.geonames, function( item ) {
						return {
							label: item.name + (item.adminName1 ? ", " + item.adminName1 : "") + ", " + item.countryName,
							value: item
						};
					}));
			});
		},
		minLength: 2,
		select: function( event, ui ) {
			event.preventDefault();
			$('#location-name').val(ui.item.label);
			$('#location-id').val(ui.item.value.geonameId);
			$('#location-id').data('location-name', ui.item.label);
			$('#location-longitude').val(ui.item.value.lng);
			$('#location-latitude').val(ui.item.value.lat);
		},
		focus: function( event, ui ) {
			event.preventDefault();
			$('#location-name').val(ui.item.value.name);
		},
		open: function() {
			$( this ).removeClass( "ui-corner-all" ).addClass( "ui-corner-top" );
		},
		close: function() {
			$( this ).removeClass( "ui-corner-top" ).addClass( "ui-corner-all" );
		}
	});
   
	$('#location-name').change(function() {
		if($('#location-id').data('location-name') != $('#location-name').val()){
			clearDependentLocationFields();
		}
	});
	
	$('#location-name').focusout(function() {
		if($('#location-id').data('location-name') != $('#location-name').val()){
			$('#location-name').val('');
			$('#location-id').data('location-name', '');
			clearDependentLocationFields();
		}
	});
	
	function clearDependentLocationFields() {
		$('#location-longitude').val('');
		$('#location-latitude').val('');
		$('#location-id').val('');
	}
}