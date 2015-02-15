package models;

import java.util.UUID;

import play.libs.Json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import de.htwg.chess.controller.IChessController;
import de.htwg.util.observer.Event;
import de.htwg.util.observer.IObserver;

public class GameInstance implements IObserver {

	private UUID gameId;
	private String gameName;
	private Player player1;
	private Player player2;
	private IChessController controller;
	private boolean run;

	public GameInstance(String gameName, Player player,
			IChessController controller) {
		this.gameId = UUID.randomUUID();
		this.gameName = gameName;
		this.player1 = player;
		this.player1.setGame(this);
		this.controller = controller;
		this.controller.addObserver(this);
		this.run = false;
	}

	public UUID getGameId() {
		return this.gameId;
	}

	public String getGameName() {
		return this.gameName;
	}

	public Player getPlayer1() {
		return this.player1;
	}

	public Player getPlayer2() {
		return this.player2;
	}

	public IChessController getController() {
		return this.controller;
	}

	public boolean isRunning() {
		return this.run;
	}

	public void join(Player player) {
		this.run = true;
		this.player2 = player;
		this.player2.setGame(this);
		notifyPlayer(this.player1, "start");
	}

	private void notifyPlayer(Player player, String type) {
		if (this.run) {
			ObjectNode json = Json.newObject();
			json.put("type", type);
			player.getOutStream().write(json);
		}
	}

	public boolean isCurrentTurn(Player player) {
		if (this.controller.isWhiteTurn()) {
			return player.equals(this.player1);
		} else {
			return player.equals(this.player2);
		}
	}

	public void playerLeftGame(Player player) {
		if (player.equals(this.player1)) {
			notifyPlayer(this.player2, "won");
			this.player1 = null;
		} else {
			notifyPlayer(this.player1, "won");
			this.player2 = null;
		}
		this.run = false;
		player.setGame(null);
	}

	@Override
	public void update(Event e) {
		JsonNode json = Json.parse(this.controller.toJson());
		this.player1.getOutStream().write(json);
		this.player2.getOutStream().write(json);
		if (this.controller.isGameover()) {
			if (this.controller.getCheckmate().isMateBlack()) {
				notifyPlayer(this.player1, "won");
				notifyPlayer(this.player2, "lost");
			} else {
				notifyPlayer(this.player1, "lost");
				notifyPlayer(this.player2, "win");
			}
			this.run = false;
		}
	}
}
