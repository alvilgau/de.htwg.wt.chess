package models;

import play.libs.Json;
import play.mvc.WebSocket.Out;

import com.fasterxml.jackson.databind.JsonNode;

import de.htwg.chess.controller.IChessController;
import de.htwg.util.observer.Event;
import de.htwg.util.observer.IObserver;

public class FieldObserver implements IObserver {

	private IChessController controller;
	private Out<JsonNode> out;

	public FieldObserver(IChessController controller, Out<JsonNode> out) {
		this.out = out;
		this.controller = controller;
		this.controller.addObserver(this);
	}

	@Override
	public void update(Event arg0) {
		out.write(Json.toJson(controller));
	}

}
