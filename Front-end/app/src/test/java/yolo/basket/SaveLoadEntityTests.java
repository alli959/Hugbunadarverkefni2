package yolo.basket;

import org.json.JSONException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import yolo.basket.db.Database;
import yolo.basket.db.game.Game;
import yolo.basket.db.gameEvent.GameEvent;
import yolo.basket.db.player.Player;
import yolo.basket.db.team.Team;

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

    public Player createPlayer(Long teamId) throws IOException, JSONException {
        Player player = new Player();
        player.setName("boris spassky");
        player.setPlayerNr(69L);
        player.setPlayerPos("Grandmaster");
        player.setTeamId(teamId);
        Player playerFromDatabase = (Player) Database.player.save(player);

        Check.ifEqual(player, playerFromDatabase);
        return playerFromDatabase;
    }

    // Careful, this one creates a player and removes it
    @Test
    public void PlayerTest() throws IOException, JSONException {
        Player player = createPlayer(defaultTeam.getId());
        Player player2 = (Player) Database.player.save(player);
        Assert.assertEquals(player.getId(), player2.getId());
        // Remove data
        Database.player.remove(player.getId());
    }

    public Team createTeam() throws IOException, JSONException {
       Team team = new Team();
        team.setName("dream$team`!#`");
        team.setUserOwner("test1");
        team = (Team) Database.team.save(team);

        List<Player> players = new ArrayList<>();

        for (int i = 0; i < 15; i++) {
            players.add(createPlayer(team.getId()));
        }
        team.setPlayers(players);
        Team teamFromDatabase = (Team) Database.team.save(team);
        Check.ifEqual(team, teamFromDatabase);
        return teamFromDatabase;
    }

    @Test
    public void TeamTest() throws IOException, JSONException {
        Team teamFromDatabase = createTeam();
        Team checkId = (Team) Database.team.save(teamFromDatabase);
        Assert.assertEquals(teamFromDatabase.getId(), checkId.getId());
        // Remove data
        for (Player player : teamFromDatabase.getPlayers())
            Database.player.remove(player);
        Database.team.remove(teamFromDatabase);
    }

    public Game createGame() throws IOException, JSONException {
        Team team = createTeam();
        Game game = new Game();
        game.setTeamId(team.getId());
        List<Player> bench = new ArrayList<>();
        List<Player> startingLineup = new ArrayList<>();
        System.out.println(team.getPlayers().size() + " is size of team");
        for (int i = 0; i < team.getPlayers().size(); i++)
            if (i < 5)
                startingLineup.add(team.getPlayers().get(i));
            else
                bench.add(team.getPlayers().get(i));
        game.setStartingLineup(startingLineup);
        game.setBench(bench);
        game.setTimeOfGame(System.currentTimeMillis());
        game.setStadiumName("Laugardalshöll");
        Game gameFromDatabase = (Game) Database.game.save(game);
        Check.ifEqual(game, gameFromDatabase);
        return gameFromDatabase;
    }

    @Test
    public void GameTest() throws IOException, JSONException {
        Game game1 = createGame();
        Game game2 = (Game) Database.game.save(game1);
        Assert.assertEquals(game1.getId(), game2.getId());
    }

    public GameEvent createGameEvent() throws IOException, JSONException {
        GameEvent gameEvent = new GameEvent();
        gameEvent.setEventType(5);
        gameEvent.setLocation(5);
        gameEvent.setTimeOfEvent(50000L);
        gameEvent.setPlayerId(5L);
        GameEvent gameEventFromDatabase = (GameEvent) Database.gameEvent.save(gameEvent);
        Check.ifEqual(gameEvent, gameEventFromDatabase);
        return gameEventFromDatabase;
    }

    @Test
    public void GameEventTest() throws IOException, JSONException {
        GameEvent gameEvent = createGameEvent();
        GameEvent gameEvent2 = (GameEvent) Database.gameEvent.save(gameEvent);

    }

    // TODO:
    //  CREATE GAME TEST
    //  TEST THE SHIT OUT OF EVERYTHING :@

    @After
    public void teardown() throws IOException, JSONException {
        Database.setCredentials("admin", "admin");
        Database.user.remove("test1");
    }
}

