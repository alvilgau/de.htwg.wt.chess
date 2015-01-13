$(function() {
	$("#exchangeAlert").hide();
	connect();
});

// Handle figure movement
function handleMovement(column, row) {
	$.ajax({
		type : "GET",
		url : "move/" + column + row,
		success : function(data) {
			if (data.select) {
				// set border at selected field
				$("#pos" + column + row).addClass("selected");
			} else if (data.exchange) {
				$("#exchangeAlert").show();
			}
		}
	});
}

function exchange(figure) {
	$.ajax({
		type : "GET",
		url : "exchange/" + figure
	});
	$("#exchangeAlert").hide();
}

// Start a new game
function restartGame() {
	$.ajax({
		type : "GET",
		url : "/restart"
	});
	$("#exchangeAlert").hide();
}

// Update status messages
function refreshStatusMessages(data) {
	// update status message
	$("#statusMessages #status").text(
			"Status: " + data.statusMessage + " " + data.checkmateMessage);
	// update turn message
	$("#statusMessages #turn").text("Turn: " + data.turnMessage);
}

// Connect with WebSocket
function connect() {
	var protocol = location.protocol == "http:" ? "ws://" : "wss://";
	var socket = new WebSocket(protocol + location.host + "/socket");

	socket.onmessage = function(msg) {
		var data = JSON.parse(msg.data);
		refreshStatusMessages(data);
		if (!data.select && !data.exchange) {
			// refresh game content
			$("#gameContent").load("/chess #playground");
		}
		if (data.gameover) {
			$("#modalGameOverContent").text(data.checkmateMessage);
			$("#modalGameOver").modal("show");
		}
	};
}