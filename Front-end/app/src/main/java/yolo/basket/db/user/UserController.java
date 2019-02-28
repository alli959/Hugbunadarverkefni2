package yolo.basket.db;

import org.json.JSONException;
import org.json.JSONObject;

public class UserController<User> extends EntityController {
    @Override
    protected Entity jsonToEntity(JSONObject json) throws JSONException {
        return null;
    }
}
