package yolo.basket.db.team;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import yolo.basket.db.Database;
import yolo.basket.db.EntityController;

public class TeamController extends EntityController {

    public TeamController() {
        saveURL = "user/saveTeam";
        removeURL = "user/team/remove";
        getOneURL = "user/team/getOne";
        getAllURL = "user/team";
    }

    @Override
    protected Team jsonToEntity(JSONObject json) throws JSONException {
        // System.out.println(json.toString());
        Team team = new Team();
        team.setId(getAsLong("id", json));
        team.setUserOwner(json.getString("userOwner"));
        team.setName(json.getString("name"));
        JSONArray gamesPlayed = json.getJSONArray("gamesPlayed");
        JSONArray players = json.getJSONArray("players");
        for (int i = 0; i < gamesPlayed.length(); i++)
            team.addGamePlayed(gamesPlayed.getLong(i));
        for (int i = 0; i < players.length(); i++)
            team.addPlayer(Database.player.jsonToEntity(players.getJSONObject(i)));
        return team;
    }
}
