$(function() {
	// handle active navigation
	$(".nav li.active").removeClass("active");
	if (location.hash) {
		// setting active link for drop down
		$(".nav a[href='" + location.pathname + location.hash + "']").parents().eq(2).addClass("active");
	} else {
		$(".nav a[href='" + location.pathname + "']").parent().addClass("active");
	}

	// create a new multiplayer game
	$("#formCreateGame").submit(function(event) {
		var gameName = $("#gameName").val();
		$("#pageContent").load("/create/" + gameName);
		event.preventDefault();
	});

});

// Update status messages
function refreshStatusMessages(data) {
	// update status message
	$("#status").text(data.statusMessage + " " + data.checkmateMessage);
	// update turn message
	$("#turn").text(data.turnMessage);
}

// Join game with passed id
function joinGame(id) {
	$("#pageContent").load("join/" + id);
}

function showModalGameInfo(title, content) {
	$("#modalGameInfoTitle").text(title)
	$("#modalGameInfoContent").text(content);
	$("#modalGameInfo").modal("show");
}
