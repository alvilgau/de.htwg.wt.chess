package models;

import play.mvc.WebSocket;

import com.fasterxml.jackson.databind.JsonNode;

public class Player {

	private GameInstance game;
	private WebSocket.Out<JsonNode> outStream;
	private int socketCount;

	public GameInstance getGame() {
		return this.game;
	}

	public void setGame(GameInstance game) {
		this.game = game;
	}

	public WebSocket.Out<JsonNode> getOutStream() {
		return this.outStream;
	}

	public void setOutStream(WebSocket.Out<JsonNode> outStream) {
		this.outStream = outStream;
	}

	public int getSocketCount() {
		return this.socketCount;
	}

	public void addSocketCount(int socketCount) {
		this.socketCount += socketCount;
	}

	public void clear() {
		this.game = null;
		this.outStream = null;
	}
}
