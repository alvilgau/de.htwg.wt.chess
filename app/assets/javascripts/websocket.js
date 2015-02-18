$(function() {
	connect();
});

// Connect with WebSocket
function connect() {
	var protocol = location.protocol == "http:" ? "ws://" : "wss://";
	var socket = new WebSocket(protocol + location.host + "/socket");

	socket.onmessage = function(msg) {
		var data = JSON.parse(msg.data);
		if (data.type == "reloadLobby") {
			$("#pageContent").load("/play #pageContent > *");
		} else if (data.type == "start") {
			$("#pageContent").load("/chess");
		} else if (data.type == "won") {
			showModalGameInfo("Won", "Congratulation you have won!");
		} else if (data.type == "lost") {
			showModalGameInfo("Lost", "You have lost!");
		} else {
			refreshStatusMessages(data);
			if (!data.select && !data.exchange) {
				// refresh game content
				$("#gameContent").load("/chess #playground");
			}
		}
	};
}