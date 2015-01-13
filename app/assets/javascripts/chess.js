$(function() {
	connect();
});

// Handle figure movement
function handleMovement(column, row) {
	$.ajax({
		type : "GET",
		url : "move/" + column + row,
		success : function(data) {
			refreshStatusMessage(data);

			if (data.select) {
				// set border at selected field
				$("#pos" + column + row).addClass("selected");
			} else if (data.exchange) {
				// TODO: exchange
				alert("exchange");
			} 
		}
	});
}

// Update turn message and refresh game content
function refreshGameContent(data) {
	// update turn message
	$("#statusMessages #turn").text("Turn: " + data.turnMessage);
	// refresh game content
	$("#gameContent").load("/chess #playground");
}

// Update status message
function refreshStatusMessage(data) {
	$("#statusMessages #status").text(
			"Status: " + data.statusMessage + " " + data.checkmateMessage);
}

// Connect with WebSocket
function connect() {
	var protocol = location.protocol == "http:" ? "ws://" : "wss://";
	var socket = new WebSocket(protocol + location.host + "/socket");

	socket.onmessage = function(msg) {
		var data = JSON.parse(msg.data);
		if (!data.select && !data.exchange) {
			refreshGameContent(data);
		}
	};
}

// Start a new game
function restartGame() {
	$.ajax({
		type : "GET",
		url : "/restart",
		success : function(data) {
			refreshStatusMessage(data);
		}
	});
}