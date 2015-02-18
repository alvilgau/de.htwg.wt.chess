package controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import models.GameInstance;
import models.Player;

import org.pac4j.play.java.JavaController;
import org.pac4j.play.java.RequiresAuthentication;

import play.data.Form;
import play.libs.F.Callback0;
import play.libs.Json;
import play.mvc.Http.Context;
import play.mvc.Result;
import play.mvc.Security.Authenticated;
import play.mvc.Security.Authenticator;
import play.mvc.WebSocket;

import com.fasterxml.jackson.databind.JsonNode;

import de.htwg.chess.controller.IChessController;
import de.htwg.chess.controller.impl.ChessController;

public class MainController extends JavaController {

	private static final String SESSION_PLAYER_ID = "PLAYER_ID";
	private static Map<String, GameInstance> gameInstances = new HashMap<String, GameInstance>();
	private static Map<String, Player> players = new HashMap<String, Player>();
	private static List<Player> playersInLobby = new ArrayList<>();

	public static Result index() {
		return ok(views.html.index.render());
	}

	public static Result rules() {
		return ok(views.html.rules.render());
	}

	@Authenticated(Secured.class)
	public static Result lobby() {
		Player player = getCurrentPlayer();
		if (player == null) {
			createPlayer();
		}
		return ok(views.html.lobby.render(gameInstances));
	}

	public static Result createGame(String gameName) {
		Player player = getCurrentPlayer();
		if (player.getGame() != null) {
			flash("error",
					"Could not create a new game because you are already in a game.");
			return redirect(routes.MainController.lobby());
		}
		GameInstance instance = new GameInstance(gameName, player,
				new ChessController());
		gameInstances.put(instance.getGameId().toString(), instance);
		playersInLobby.remove(player);
		notifyPlayersInLobby();
		return ok(views.html.wait.render());
	}

	public static Result joinGame(String id) {
		GameInstance instance = gameInstances.get(id);
		if (instance == null) {
			flash("error", "Could not find the game with the id: " + id);
			return redirect(routes.MainController.lobby());
		}
		Player player = getCurrentPlayer();
		if (player.getGame() != null) {
			flash("error",
					"Could not join the game because you are already in a game.");
			return redirect(routes.MainController.lobby());
		}
		instance.join(player);
		playersInLobby.remove(player);
		notifyPlayersInLobby();
		return ok(views.html.chess.render(instance.getController()));
	}

	public static Result chess() {
		GameInstance instance = getCurrentPlayer().getGame();
		if (instance == null) {
			return redirect(routes.MainController.lobby());
		}
		return ok(views.html.chess.render(instance.getController()));
	}

	public static Result handleMovement(String command) {
		Player player = getCurrentPlayer();
		GameInstance instance = player.getGame();
		if (instance == null) {
			return redirect(routes.MainController.lobby());
		} else if (instance.isRunning() && instance.isCurrentTurn(player)) {
			IChessController controller = instance.getController();
			int posX = Integer.parseInt(command.substring(0, 1));
			int posY = Integer.parseInt(command.substring(1, 2));
			controller.handleMovement(posX, posY);
			return ok(Json.parse(controller.toJson()));
		}
		return ok();
	}

	public static Result exchange(String figure) {
		Player player = getCurrentPlayer();
		GameInstance instance = player.getGame();
		if (instance == null) {
			return redirect(routes.MainController.lobby());
		} else if (instance.isRunning() && !instance.isCurrentTurn(player)) {
			IChessController controller = instance.getController();
			if (figure.equals("knight")) {
				controller.exchangeKnight();
			} else if (figure.equals("bishop")) {
				controller.exchangeBishop();
			} else if (figure.equals("rook")) {
				controller.exchangeRook();
			} else {
				controller.exchangeQueen();
			}
		}
		return ok();
	}

	public static WebSocket<JsonNode> connectWebSocket() {
		Player player = getCurrentPlayer();
		player.addSocketCount(1);
		if (!playersInLobby.contains(player)) {
			playersInLobby.add(player);
		}

		return new WebSocket<JsonNode>() {

			@Override
			public void onReady(WebSocket.In<JsonNode> in,
					WebSocket.Out<JsonNode> out) {
				if (player.getOutStream() == null) {
					player.setOutStream(out);
				}

				in.onClose(new Callback0() {
					@Override
					public void invoke() throws Throwable {
						player.addSocketCount(-1);
						if (player.getSocketCount() == 0) {
							GameInstance instance = player.getGame();
							if (instance != null) {
								instance.playerLeftGame(player);
								gameInstances.remove(instance.getGameId()
										.toString());
								notifyPlayersInLobby();
							}
							player.clear();
						}
					}
				});
			}

		};
	}

	public static Result login() {
		return ok(views.html.login.render(Form.form(Login.class)));
	}

	public static Result logout() {
		flash("success", "You have been logged out!");
		players.remove(session(SESSION_PLAYER_ID));
		session().clear();
		return redirect(routes.MainController.index());
	}

	public static Result authenticate() {
		Form<Login> loginForm = Form.form(Login.class).bindFromRequest();

		if (loginForm.hasErrors()) {
			return badRequest(views.html.login.render(loginForm));
		} else {
			session().clear();
			session("email", loginForm.get().email);
			createPlayer();
			return redirect(routes.MainController.lobby());
		}
	}

	@RequiresAuthentication(clientName = "Google2Client")
	public static Result googleLogin() {
		session("email", getUserProfile().getEmail());
		createPlayer();
		return redirect(routes.MainController.lobby());
	}

	private static void createPlayer() {
		String playerId = UUID.randomUUID().toString();
		players.put(playerId, new Player());
		session(SESSION_PLAYER_ID, playerId);
	}

	private static Player getCurrentPlayer() {
		return players.get(session(SESSION_PLAYER_ID));
	}

	private static void notifyPlayersInLobby() {
		for (Player player : playersInLobby) {
			player.notifyPlayer("reloadLobby");
		}
	}

	public static class Login {

		private final static String defaultEmail = "admin@chess.de";
		private final static String defaultPasswort = "admin";

		public String email;
		public String password;

		public String validate() {
			if (this.email.equals(defaultEmail)
					&& this.password.equals(defaultPasswort)) {
				return null;
			} else {
				return "Invalid user or password";
			}
		}
	}

	public static class Secured extends Authenticator {

		@Override
		public String getUsername(Context ctx) {
			return ctx.session().get("email");
		}

		@Override
		public Result onUnauthorized(Context ctx) {
			return redirect(routes.MainController.login());
		}
	}

}
