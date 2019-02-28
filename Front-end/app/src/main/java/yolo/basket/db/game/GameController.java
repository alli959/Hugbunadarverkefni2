package yolo.basket.db.game;

import org.json.JSONException;
import org.json.JSONObject;

import yolo.basket.db.Entity;
import yolo.basket.db.EntityController;

public class GameController<Game> extends EntityController {
    @Override
    protected Entity jsonToEntity(JSONObject json) throws JSONException {
        return null;
    }
}
