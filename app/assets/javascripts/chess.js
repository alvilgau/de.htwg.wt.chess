$(function() {
	connect();
});

// Handle figure movement
function handleMovement(column, row) {
	$.ajax({
		type : "GET",
		url : "move/" + column + row,
		success : function(data) {
			// update status message
			$("#statusMessages #status").text(
					"Status: " + data.statusMessage + " "
							+ data.checkmateMessage);

			if (data.select) {
				// set border at selected field
				$("#pos" + column + row).addClass("selected");
			} else if (data.exchange) {
				// TODO: exchange
				alert("exchange");
			} else {
				refreshGameContent(data);
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