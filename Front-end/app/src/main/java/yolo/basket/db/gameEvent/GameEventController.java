package yolo.basket.db.gameEvent;

import org.json.JSONException;
import org.json.JSONObject;

import yolo.basket.db.Entity;
import yolo.basket.db.EntityController;

public class GameEventController extends EntityController {
    public GameEventController() {
    }

    @Override
    public GameEvent jsonToEntity(JSONObject json) throws JSONException {
        System.out.println(json.toString());
        GameEvent gameEvent = new GameEvent();
        gameEvent.setPlayerId(json.getLong("playerId"));
        gameEvent.setTimeOfEvent(json.getLong("timeOfEvent"));
        gameEvent.setLocation(json.getInt("location"));
        gameEvent.setEventType(json.getInt("eventType"));
        return gameEvent;
    }
}
