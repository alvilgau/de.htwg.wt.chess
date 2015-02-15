$(function() {
	$("#exchangeAlert").hide();
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

				// mark possible moves
				data.possMoves.forEach(function(entry) {
					var pos = "pos" + entry[0] + entry[1];
					$("#" + pos).addClass("possible")
				})
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
