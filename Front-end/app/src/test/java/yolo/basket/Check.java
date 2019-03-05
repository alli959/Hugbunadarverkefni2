package yolo.basket;

import org.junit.Assert;

import java.util.List;

import yolo.basket.db.Entity;
import yolo.basket.db.game.Game;
import yolo.basket.db.player.Player;
import yolo.basket.db.team.Team;



// I literally made this one so the tests
// can be written with lines like:
//      Check.ifEqual(team1, team2);



public class Check {
    public static void ifPlayerListIsEqual(List<Player> list1, List<Player> list2) {
        Assert.assertEquals(list1.size(), list2.size());
        if (list1.size() == list2.size())
            for (int i = 0; i < list1.size(); i++)
                Check.ifEqual(list1.get(i), list2.get(i));
    }

    public static void ifEqual(Team team1, Team team2) {
        Assert.assertEquals(team1.getName(), team2.getName());
        Assert.assertEquals(team1.getUserOwner(), team2.getUserOwner());
        Assert.assertArrayEquals(team1.getGamesPlayed().toArray(), team2.getGamesPlayed().toArray());
        Check.ifPlayerListIsEqual(team1.getPlayers(), team2.getPlayers());
    }

    public static void ifEqual(Player player1, Player player2) {
        Assert.assertEquals(player1.getName(), player2.getName());
        Assert.assertEquals(player1.getPlayerNr(), player2.getPlayerNr());
        Assert.assertEquals(player1.getPlayerPos(), player2.getPlayerPos());
        Assert.assertEquals(player1.getTeamId(), player2.getTeamId());
    }

    public static void ifEqual(Game game1, Game game2) {
        Assert.assertEquals(game1.getStadiumName(), game2.getStadiumName());
        Assert.assertEquals(game1.getTeamId(), game2.getTeamId());
        Assert.assertEquals(game1.getTimeOfGame(), game2.getTimeOfGame());
        Check.ifPlayerListIsEqual(game1.getBench(), game2.getBench());
        Check.ifPlayerListIsEqual(game1.getStartingLineup(), game2.getStartingLineup());
    }
}
