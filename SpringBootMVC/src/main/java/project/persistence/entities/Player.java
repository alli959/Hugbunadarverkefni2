
package project.persistence.entities;

import javax.persistence.*;
import static javax.persistence.GenerationType.*;

import java.util.ArrayList;

@Entity
public class Player {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private Long playerNr;
    private String name;
    private String playerPos;
    private Long teamId;
    private ArrayList<Long> gamesPlayed;

    public Player() {

    }

    public Player(Long id, String name, String playerPos, Long playerNr, Long teamId) {
        this.id = id;
        this.name = name;
        this.playerPos = playerPos;
        this.playerNr = playerNr;
        this.teamId = teamId;


    }

    public Long getId() {return id;}

    public void setId(Long id) {this.id = id;}

    public String getName() {return name;}

    public void setName(String name) { this.name = name;}

    public String getPlayerPos() { return playerPos;}

    public void setPlayerPos(String playerPos) { this.playerPos = playerPos;}

    public Long getPlayerNr() { return playerNr;}

    public void setPlayerNr(Long playerNr) { this.playerNr = playerNr;}

    public Long getTeamId() { return teamId;}

    public void setTeamId(Long teamId) {this.teamId =  teamId;}

    public void setGamesPlayed(ArrayList<Long> gamesPlayed) { this.gamesPlayed = gamesPlayed; }

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
