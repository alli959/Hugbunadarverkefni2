package yolo.basket.db.team;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import yolo.basket.db.Database;
import yolo.basket.db.EntityController;

public class TeamController<Ent extends Team, IdType> extends EntityController {

    public TeamController() {
        saveURL = "user/saveTeam";
        removeURL = "user/removeTeam";
        getOneURL = "user/getTeam";
    }

    @Override
    protected Ent jsonToEntity(JSONObject json) throws JSONException {
        System.out.println(json.toString());
        Team team = new Team();
        team.setId(getAsLong("id", json));

        team.setUserOwner((String) json.get("userOwner"));
        team.setName((String) json.get("name"));
        JSONArray gamesPlayed = json.getJSONArray("gamesPlayed");
        JSONArray players = json.getJSONArray("players");
        for (int i = 0; i < gamesPlayed.length(); i++)
            team.addGamePlayed((java.lang.Long) gamesPlayed.get(i));
        for (int i = 0; i < players.length(); i++)
            team.addPlayer(Database.player.jsonToEntity(players.getJSONObject(i)));
        return (Ent) team;
    }
}
