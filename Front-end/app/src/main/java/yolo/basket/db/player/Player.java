
package yolo.basket.db.player;

import java.util.ArrayList;
import java.util.List;
import yolo.basket.db.Pair;

import yolo.basket.db.Entity;

public class Player extends Entity {

    private Long id;
    private Long playerNr;
    private String name;
    private String playerPos;
    private Long teamId;
    private List<Long> gamesPlayed = new ArrayList<>();

    public Player() {}

    public Player(Long id, String name, String playerPos, Long playerNr, Long teamId) {
        this.id = id;
        this.name = name;
        this.playerPos = playerPos;
        this.playerNr = playerNr;
        this.teamId = teamId;
    }

    public Long getId() {return id;}

    public void setId(Long id) {this.id = id;}

    @Override
    public List<Pair<String, String>> getParameters() {
        return null;
    }

    public String getName() {return name;}

    public void setName(String name) { this.name = name;}

    public String getPlayerPos() { return playerPos;}

    public void setPlayerPos(String playerPos) { this.playerPos = playerPos;}

    public Long getPlayerNr() { return playerNr;}

    public void setPlayerNr(Long playerNr) { this.playerNr = playerNr;}

    public Long getTeamId() { return teamId;}

    public void setTeamId(Long teamId) {this.teamId =  teamId;}

    public List<Long> getGamesPlayed() {
      return gamesPlayed;
    }

    public void setGamesPlayed(List<Long> gamesPlayed) { this.gamesPlayed = gamesPlayed; }

    public void addGamePlayed(Long gameId) {
      this.gamesPlayed.add(gameId);
    }



    // This is for easier debug.
    @Override
    public String toString() {
        return String.format(
                "Player[name=%s, playerPos=%s]",
                name,playerPos) + super.toString();
    }


}
