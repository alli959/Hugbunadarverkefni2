package yolo.basket.db.user;

import org.json.JSONException;
import org.json.JSONObject;

import yolo.basket.db.Entity;
import yolo.basket.db.EntityController;

public class UserController<User, String> extends EntityController {

    public UserController() {
        removeURL = "removeUser";
        saveURL = "method_does_not_exists_on_backend";
        getOneURL = "method_does_not_exist_on_backend";
    }

    @Override
    protected Entity jsonToEntity(JSONObject json) throws JSONException {
        return null;
    }
}
