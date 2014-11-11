package controllers;

import java.util.HashMap;

import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import com.fasterxml.jackson.databind.JsonNode;

import de.htwg.chess.Chess;
import de.htwg.chess.controller.IChessController;

public class MainController extends Controller {

	static IChessController controller = Chess.getInstance().getController();

	public static Result index() {
		return ok(views.html.index.render());
	}

	public static Result chess() {
		return ok(views.html.chess.render(controller));
	}

	public static Result contact() {
		return ok(views.html.contact.render());
	}

	public static Result handleMovement(String command) {
		int posX = Integer.parseInt(command.substring(0, 1));
		int posY = Integer.parseInt(command.substring(1, 2));
		controller.handleMovement(posX, posY);
		return ok(controllerToJson());
	}

	private static JsonNode controllerToJson() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("selected", controller.isSelect());
		return Json.toJson(map);
	}
}
