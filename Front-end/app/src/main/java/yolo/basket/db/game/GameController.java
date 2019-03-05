package yolo.basket.db.game;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import yolo.basket.db.Database;
import yolo.basket.db.Entity;
import yolo.basket.db.EntityController;
import yolo.basket.db.player.Player;

public class GameController extends EntityController {

    public GameController() {
        saveURL = "user/createGame";
        getOneURL = "user/getOneGame";
        removeURL = "user/removeGame";
    }

    @Override
    protected Game jsonToEntity(JSONObject json) throws JSONException {
        System.out.println(json.toString());
        Game game = new Game();
        game.setStadiumName(json.getString("stadiumName"));
        game.setTimeOfGame(json.getLong("timeOfGame"));
        game.setTeamId(json.getLong("teamId"));
        game.setId(json.getLong("id"));

        JSONArray benchArray = json.getJSONArray("bench");
        JSONArray startingLineupArray = json.getJSONArray("startingLineup");
        List<Player> bench = new ArrayList<>();
        List<Player> startingLineup = new ArrayList<>();


        for (int i = 0; i < benchArray.length(); i++)
            bench.add(Database.player.jsonToEntity(benchArray.getJSONObject(i)));
        for (int i = 0; i < startingLineupArray.length(); i++)
            startingLineup.add(Database.player.jsonToEntity(benchArray.getJSONObject(i)));
        game.setStartingLineup(startingLineup);
        game.setBench(bench);
        return game;
    }
}
