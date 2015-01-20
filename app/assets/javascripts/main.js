$(function() {
	
	// handle active navigation
	$(".nav li.active").removeClass("active");
	
	if(location.hash) {
		// setting active link for drop down
		$(".nav a[href='" + location.pathname + location.hash + "']").parents().eq(2).addClass("active");
	}
	else {
		$(".nav a[href='" + location.pathname + "']").parent().addClass("active");
	}
	
});
