package controllers;

import play.mvc.Controller;
import play.mvc.Result;
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

	public static Result commandline(String command) {
		Chess.getInstance().getTui().processInputLine(command);
		return ok(views.html.chess.render(controller));
	}

}
