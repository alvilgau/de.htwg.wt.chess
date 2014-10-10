package controllers;

import play.mvc.Controller;
import play.mvc.Result;
import de.htwg.chess.Chess;
import de.htwg.chess.controller.IChessController;

public class MainController extends Controller {

	static IChessController controller = Chess.getInstance().getController();

	public static Result index() {
		return ok(views.html.index.render("Hello Chess", controller));
	}

	public static Result commandline(String command) {
		Chess.getInstance().getTui().processInputLine(command);
		return ok(views.html.index.render("Got your position " + command,
				controller));
	}

}
