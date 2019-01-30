package project.persistence.entities;

import javax.persistence.*;
import java.util.List;
import java.util.ArrayList;

@Entity
public class Game {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

  @OneToMany
  private List<GameEvent> gameEvents;
  @OneToMany
  private List<Player> players;
  @OneToMany
  private List<Player> startingLineup;
  private String stadiumName = "Not set";
  private long timeOfGame;

  public Game() { }

  // Duplicate code yes, but for speed
  // The if sentence a few lines down runs many times
  public int[][] compileStats(long playerId) {
    int[][] stats = new int[GameEvent.N_GAME_EVENTS][GameEvent.N_LOCATIONS];
    for (GameEvent ge : gameEvents)
      if (playerId == ge.getPlayerId() || playerId == 0)
        stats[ge.getEventType()][ge.getLocation()]++;
    return stats;
  }

  public int[][] compileStats() {
    int[][] stats = new int[GameEvent.N_GAME_EVENTS][GameEvent.N_LOCATIONS];
    for (GameEvent ge : gameEvents)
      stats[ge.getEventType()][ge.getLocation()]++;
    return stats;
  }

  public void addGameEvent(GameEvent gameEvent) {
    this.gameEvents.add(gameEvent);
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getStadiumName() {
    return stadiumName;
  }

  public void setStadiumName(String stadiumName) {
    this.stadiumName = stadiumName;
  }

  public long getTimeOfGame() {
    return timeOfGame;
  }

  public void setTimeOfGame(long timeOfGame) {
    this.timeOfGame = timeOfGame;
  }

  public List<GameEvent> getGameEvents() {
    return gameEvents;
  }

  public void setGameEvents(ArrayList<GameEvent> gameEvents) {
    this.gameEvents = gameEvents;
  }

  public List<Player> getPlayers() {
    return players;
  }

  public void setPlayers(ArrayList<Player> players) {
    this.players = players;
  }


  public List<Player> getStartingLineup() {
    return startingLineup;
  }

  public void setStartingLineup(ArrayList<Player> startingLineup) {
    this.startingLineup = startingLineup;
  }


}
