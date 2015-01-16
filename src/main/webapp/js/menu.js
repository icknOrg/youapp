(function( $ ) {
	
	$.fn.menu = function( options ) {
		
		// default options
		var settings = $.extend( {
			'effect' : 'fade'
		}, options);
		
		// store this
		$this = $(this);
		
		// fade effect
		if (settings.effect == 'fade')
		{
			$("li", $this).hover(function() {
				$('> ul, > div', this).hide().fadeIn(200);
			}, function () {
				$('> ul, > div', this).fadeOut(200);
			});
		}
		
		// slide effect
		else if (settings.effect == 'slide')
		{
			$("li", $this).hover(function()
			{
				$('> ul, > div', this)
					.stop()
					.hide()
					.css({
						display : 'block',
						opacity : 0,
						marginTop : 50
					})
					.animate({
						opacity : 1,
						marginTop : 0
					}, 250);
			}, 
			function ()
			{
				$('> ul, > div', this)
					.stop()
					.animate({
						opacity : 0,
						marginTop : 50
					}, 250, function() {
						$(this).hide();
					});
			});
		}
		
		// add bubble effects
		$('> li > a', $this).hover(function()
		{
			$('.bubble, .bubble-macosx, .bubble-red, .bubble-blue, .bubble-orange, .bubble-purple', this)
				.stop()
				.animate({
					marginTop : -3
				}, 200);
		}, 
		function () 
		{
			$('.bubble, .bubble-macosx, .bubble-red, .bubble-blue, .bubble-orange, .bubble-purple', this)
				.stop()
				.animate({
					marginTop : 0
				}, 200);
		});
		
		// return this object
		return this;
	}
	
})( jQuery );