package yolo.basket.db;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
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

    public static void init() {
        setCredentials("anonymous", "anonymous");
    }


    public static void setCredentials(String userName, String password) {
        Request.setUserName(userName);
        Request.setPassword(password);
    }

    public static String register(String userName, String password, String name, String email) {
        // Set credentials to anonymous:anonymous
        init();
        User user = new User();
        user.setEmail(email);
        user.setName(name);
        user.setPassword(password);
        user.setUserName(userName);
        List<Param> params = user.getParameters();
        Request request = null;
        try {
            request = new Request("register", params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        JSONArray json;
        try {
            json = request.resolve();
            return json.get(0).toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to register user";
        }
    }

    public static boolean isLoggedIn() {
        try {
            String userName = Request.getUserName();
            Request request = new Request("user/whatismyusername");
            String string = (String) request.resolve().get(0);
            return string.equals(userName);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Returns true if logged in otherwise false
    public static boolean login(String userName, String password) {
        Request.setUserName(userName);
        Request.setPassword(password);
        try {
            Request request = new Request("user/whatismyusername");
            String string = (String) request.resolve().get(0);
            return string.equals(userName);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String unregister(String userName) {
        Request request;
        try {
            request = new Request("removeUser", new Param("id", userName));
            return request.resolve().get(0).toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return "Failure";
        } catch (IOException e) {
            e.printStackTrace();
            return "Failure";
        }
    }

    //--------Team And Player methods

}
