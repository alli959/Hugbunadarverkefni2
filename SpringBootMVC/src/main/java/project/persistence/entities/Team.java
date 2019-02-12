
package project.persistence.entities;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import static javax.persistence.GenerationType.*;

@Entity
public class Team {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String name;
    private String userOwner;
    @OneToMany
    private List<Player> players = new ArrayList<>();
    @ElementCollection
    private List<Long> gamesPlayed = new ArrayList<>();

    public Team(){ }

    public Team(Long id, String name, String userOwner){
        this.id = id;
        this.name = name;
        this.userOwner = userOwner;
    }

    public void addPlayer(Player player) {
      players.add(player);
    }

    public void addGamePlayed(Long gameId) {
      gamesPlayed.add(gameId);
    }

    public Long getId(){ return id; }

    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }

    public void setName(String name) {this.name = name;}

    public String getUserOwner() { return userOwner; }

    public void setUserOwner(String userOwner) {this.userOwner = userOwner;}

    public List<Long> getGamesPlayed() {
      return gamesPlayed;
    }

    public void setGamesPlayed(List<Long> gamesPlayed) {
      this.gamesPlayed = gamesPlayed;
    }

    public List<Player> getPlayers() {
      return players;
    }

    public void setPlayers(List<Player> players) {
      this.players = players;
    }


    //public Player getPlayer() {return player;}

    //public void setPlayer(Player player) {this.player = player;}
    // This is for easier debug.
    @Override
    public String toString() {
        return String.format(
                "Team [id=%s, name=%s, userOwner=%s]",
                id, name, userOwner) + super.toString();
    }

}
