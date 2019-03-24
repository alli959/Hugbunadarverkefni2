package yolo.basket.db.player;

import yolo.basket.db.EntityController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PlayerController<Ent extends Player, IdType> extends EntityController {

	public PlayerController() {
		saveURL = "user/savePlayer";
		removeURL = "user/removePlayer";
		getOneURL = "user/getPlayer";
	}

	public Ent getOnePlayer(long id) throws JSONException {
		List<Ent> listOfOne = listOfOne(id);
		return listOfOne.get(0);
	}

	public Ent jsonToEntity(JSONObject json) throws JSONException {
	    Player player = new Player();
	    player.setTeamId(json.getLong("teamId"));
	    player.setId(json.getLong("id"));
	    player.setName(json.getString("name"));
	    player.setPlayerPos(json.getString("playerPos"));
        player.setPlayerNr(json.getLong("playerNr"));
        JSONArray gamesPlayedJSON = json.getJSONArray("gamesPlayed");
        List<Long> gamesPlayed = new ArrayList<>();
        for (int i = 0; i < gamesPlayedJSON.length(); i++)
            gamesPlayed.add(gamesPlayedJSON.getLong(i));
        player.setGamesPlayed(gamesPlayed);
	    return (Ent) player;
	}
}
