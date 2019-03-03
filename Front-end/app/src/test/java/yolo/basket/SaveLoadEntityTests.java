package yolo.basket;

import org.json.JSONException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import yolo.basket.db.Database;
import yolo.basket.db.player.Player;
import yolo.basket.db.team.Team;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class SaveLoadEntityTests {

    Team defaultTeam;

    @Before
    public void setup() throws IOException, JSONException {
        Database.init();
        String registerStatus = Database.register("test1", "test2", "test3", "test4");
        System.out.println(registerStatus);
        boolean loginStatus = Database.login("test1", "test2");
        System.out.println(loginStatus);

        defaultTeam = new Team();
        defaultTeam.setName("soviet russia");
        defaultTeam.setUserOwner("test1");
        defaultTeam = (Team) Database.team.save(defaultTeam);
        System.out.println(defaultTeam.getId());
    }

    @Test
    public void createPlayer() throws IOException, JSONException {
        Player player = new Player();
        player.setName("boris spassky");
        player.setPlayerNr(69L);
        player.setPlayerPos("Grandmaser");
        player.setTeamId(defaultTeam.getId());
        Player playerFromDatabase = (Player) Database.player.save(player);
        Assert.assertEquals(player.getName(), playerFromDatabase.getName());
        Assert.assertEquals(player.getPlayerNr(), playerFromDatabase.getPlayerNr());
        Assert.assertEquals(player.getPlayerPos(), playerFromDatabase.getPlayerPos());
        Assert.assertEquals(player.getTeamId(), playerFromDatabase.getTeamId());
        Assert.assertArrayEquals(player.getGamesPlayed().toArray(), playerFromDatabase.getGamesPlayed().toArray());
        Database.player.remove(playerFromDatabase.getId());
    }

    @After
    public void teardown() throws IOException, JSONException {
        Database.setCredentials("admin", "admin");

    }
}

