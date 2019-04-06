package yolo.basket.db;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import yolo.basket.db.game.GameController;
import yolo.basket.db.gameEvent.GameEventController;
import yolo.basket.db.player.PlayerController;
import yolo.basket.db.stats.StatsController;
import yolo.basket.db.user.User;
import yolo.basket.db.user.UserController;
import yolo.basket.db.team.TeamController;

public class Database {

    public static final UserController user = new UserController();
    public static final TeamController team = new TeamController();
    public static final StatsController stats = new StatsController();
    public static final PlayerController player = new PlayerController();
    public static final GameEventController gameEvent = new GameEventController();
    public static final GameController game = new GameController();

    public static void useAnonymousCredentials() {
        setCredentials("anonymous", "anonymous");
    }

    public static void setCredentials(String userName, String password) {
        Request.setUserName(userName);
        Request.setPassword(password);
    }

    public static String register(User user) throws Exception {
        // Set credentials to anonymous:anonymous
        useAnonymousCredentials();
        List<Param> params = user.getParameters();
        Request request = new Request("register", params);
        JSONArray json = request.resolve();
        return json.getString(0);
    }

    public static boolean isLoggedIn() throws Exception {
        String userName = Request.getUserName();
        Request request = new Request("user/whatismyusername");
        String string = (String) request.resolve().get(0);
        return string.equals(userName);
    }

    // Returns true if logged in otherwise throws Exception / returns false
    public static boolean login(User user) throws Exception {
        Request.setUserName(user.getUserName());
        Request.setPassword(user.getPassword());
        Request request = new Request("user/whatismyusername");
        String string = request.resolve().getString(0);
        return string.equals(user.getUserName());
    }

    public static String unregister(String userName) throws Exception {
        Request request = new Request("removeUser", new Param("id", userName));
        return request.resolve().getString(0);
    }
}
