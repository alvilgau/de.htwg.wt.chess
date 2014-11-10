$(function() {
	
	// handle active navigation
	$(".nav li.active").removeClass("active");
	$(".nav a[href='" + location.pathname + "']").parent().addClass("active");
	
});
