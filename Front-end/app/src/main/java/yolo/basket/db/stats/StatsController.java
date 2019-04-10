package yolo.basket.db.stats;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import yolo.basket.db.Database;
import yolo.basket.db.Entity;
import yolo.basket.db.EntityController;
import yolo.basket.db.Param;
import yolo.basket.db.Request;
import yolo.basket.db.gameEvent.GameEvent;
import yolo.basket.db.player.Player;
import yolo.basket.db.team.Team;

public class StatsController extends EntityController {

    @Override
    protected Stats jsonToEntity(JSONObject json) throws JSONException {
        Stats stats = new Stats();

        int nActions = GameEvent.N_GAME_EVENTS;
        int nLocations = GameEvent.N_LOCATIONS;

        int[][] data = new int[GameEvent.N_GAME_EVENTS][GameEvent.N_LOCATIONS];
        JSONArray jsonData = json.getJSONArray("data");

        for (int i = 0; i < nActions; i++)
            for (int j = 0; j < nLocations; j++)
                data[i][j] = jsonData.getJSONArray(i).getInt(j);

        stats.setData(data);
        stats.recalculateStats();
        return stats;
    }

    public Stats getStatsForTeam(Team team) throws Exception {
        String method = "user/stats/teamStats";
        Param teamId = new Param("teamId", "" + team.getId());
        Request request = new Request(method, teamId);

        return jsonToEntity(request.resolve().getJSONObject(0));
    }

    public Stats getStatsForPlayer(Player player) throws Exception {
        String method = "user/getStatsForPlayer";

        Param playerId = new Param("playerId", "" + player.getId());
        Request request = new Request(method, playerId);

        return jsonToEntity(request.resolve().getJSONObject(0));
    }
}
