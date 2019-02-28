package yolo.basket.db.user;

import org.json.JSONException;
import org.json.JSONObject;

import yolo.basket.db.Entity;
import yolo.basket.db.EntityController;

public class UserController<User> extends EntityController {
    @Override
    protected Entity jsonToEntity(JSONObject json) throws JSONException {
        return null;
    }
}
