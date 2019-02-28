package yolo.basket.db.gameEvent;

import org.json.JSONException;
import org.json.JSONObject;

import yolo.basket.db.Entity;
import yolo.basket.db.EntityController;

public class GameEventController<GameEvent> extends EntityController {
    public GameEventController() {
    }

    @Override
    protected Entity jsonToEntity(JSONObject json) throws JSONException {
        return null;
    }
}
