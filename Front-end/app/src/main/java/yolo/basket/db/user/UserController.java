package yolo.basket.db.user;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import yolo.basket.db.Database;
import yolo.basket.db.Entity;
import yolo.basket.db.EntityController;
import yolo.basket.db.Param;
import yolo.basket.db.Request;
import yolo.basket.db.game.Game;

public class UserController extends EntityController {

    public UserController() {
        removeURL = "removeUser";
        saveURL = "method_does_not_exists_on_backend";
        getOneURL = "method_does_not_exist_on_backend";
    }

    public void setActiveGame(Game game) throws IOException, JSONException {
        String method = "user/setActiveGame";
        Param param = new Param("id", "" + game.getId());
        Request request = new Request(method, param);
        request.resolve();
    }

    public Game getActiveGame() throws Exception {
        String method = "user/getActiveGame";
        Request request = new Request(method);
        JSONObject json = request.resolve().getJSONObject(0);
        return (Game) Database.game.jsonToEntity(json);
    }

    @Override
    protected User jsonToEntity(JSONObject json) throws JSONException {
        return null;
    }
}
