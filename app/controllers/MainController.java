package controllers;

import models.FieldObserver;
import play.libs.F.Callback0;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.WebSocket;

import com.fasterxml.jackson.databind.JsonNode;

import de.htwg.chess.Chess;
import de.htwg.chess.controller.IChessController;
import de.htwg.util.observer.IObserver;

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
		return ok(Json.toJson(controller));
	}

	public static WebSocket<JsonNode> connectWebSocket() {
		return new WebSocket<JsonNode>() {

			public void onReady(WebSocket.In<JsonNode> in,
					WebSocket.Out<JsonNode> out) {

				IObserver fieldObserver = new FieldObserver(controller, out);

				in.onClose(new Callback0() {
					public void invoke() throws Throwable {
						controller.removeObserver(fieldObserver);
					}
				});
			}

		};
	}

}
