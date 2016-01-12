jQuery(document).ready(function(){
	//Back to top
	jQuery('.gototop').click(function () {
		jQuery('body,html').animate({
			scrollTop: 0
		}, 800);
		return false;
	});
});
