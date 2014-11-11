// Handle figure movement
function handleMovement(column, row) {
	$.ajax({
		type : "GET",
		url : "move/" + column + row,
		success : function(data) {
			// refresh game content
			$(".well").load("/chess #gameContent", function() {
				toogleSelection(data.selected, column, row);
			});
		}
	});
}

// Setting border at the selected field
function toogleSelection(selected, column, row) {
	if (selected) {
		$("#pos" + column + row).addClass("selected");
	} else {
		$(".playground div.selected").removeClass("selected");
	}
}