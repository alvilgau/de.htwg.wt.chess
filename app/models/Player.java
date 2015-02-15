package models;

import play.mvc.WebSocket;

import com.fasterxml.jackson.databind.JsonNode;

public class Player {

	private GameInstance game;
	private WebSocket.In<JsonNode> inStream;
	private WebSocket.Out<JsonNode> outStream;

	public GameInstance getGame() {
		return this.game;
	}

	public void setGame(GameInstance game) {
		this.game = game;
	}

	public WebSocket.In<JsonNode> getInStream() {
		return this.inStream;
	}

	public void setInStream(WebSocket.In<JsonNode> inStream) {
		this.inStream = inStream;
	}

	public WebSocket.Out<JsonNode> getOutStream() {
		return this.outStream;
	}

	public void setOutStream(WebSocket.Out<JsonNode> outStream) {
		this.outStream = outStream;
	}
}
