package yolo.basket.db.game;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import yolo.basket.db.Database;
import yolo.basket.db.EntityController;
import yolo.basket.db.Param;
import yolo.basket.db.Request;
import yolo.basket.db.gameEvent.GameEvent;
import yolo.basket.db.player.Player;

public class GameController extends EntityController {

    public GameController() {
        saveURL = "user/createGame";
        getOneURL = "user/getOneGame";
        removeURL = "user/removeGame";
    }

    public Boolean addGameEvent(GameEvent gameEvent) throws IOException, JSONException {
        String method = "user/addGameEvent";
        List<Param> params = gameEvent.getParameters();
        Request request = new Request(method, params);
        request.resolve();
        return true;
    }

    public Boolean endGame() throws Exception {
        String method = "user/endGame";
        Request request = new Request(method);
        request.resolve();
        return true;
    }

    public Game getActiveGame() throws Exception {
        String method = "user/getActiveGame";
        Request request = new Request(method);
        JSONArray json = request.resolve();
        return jsonToEntity(json.getJSONObject(0));
    }

    @Override
    public Game jsonToEntity(JSONObject json) throws JSONException {
        System.out.println(json.toString());
        Game game = new Game();
        game.setStadiumName(json.getString("stadiumName"));
        game.setTimeOfGame(json.getLong("timeOfGame"));
        game.setTeamId(json.getLong("teamId"));
        game.setId(json.getLong("id"));

        JSONArray benchArray = json.getJSONArray("bench");
        JSONArray startingLineupArray = json.getJSONArray("startingLineup");
        JSONArray gameEventsArray = json.getJSONArray("gameEvents");
        List<Player> bench = new ArrayList<>();
        List<Player> startingLineup = new ArrayList<>();
        List<GameEvent> gameEvents = new ArrayList<>();

        for (int i = 0; i < benchArray.length(); i++)
            bench.add(Database.player.jsonToEntity(benchArray.getJSONObject(i)));
        for (int i = 0; i < startingLineupArray.length(); i++)
            startingLineup.add(Database.player.jsonToEntity(benchArray.getJSONObject(i)));
        for (int i = 0; i < gameEventsArray.length(); i++)
            gameEvents.add(Database.gameEvent.jsonToEntity(gameEventsArray.getJSONObject(i)));
        game.setStartingLineup(startingLineup);
        game.setBench(bench);
        game.setGameEvents(gameEvents);
        return game;
    }
}
