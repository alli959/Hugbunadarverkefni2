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
  private List<GameEvent> gameEvents = new ArrayList<>();
  @ManyToMany
  private List<Player> bench = new ArrayList<>();
  @ManyToMany
  private List<Player> startingLineup = new ArrayList<>();
  private String stadiumName = "Not set";
  private long timeOfGame = 0;

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

  public List<Player> getAllPlayers() {
    List<Player> all = new ArrayList<>();
    all.addAll(bench);
    all.addAll(startingLineup);
    return all;
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

  public void setGameEvents(List<GameEvent> gameEvents) {
    this.gameEvents = gameEvents;
  }

  public List<Player> getBench() {
    return bench;
  }

  public void setBench(List<Player> bench) {
    this.bench = bench;
  }


  public List<Player> getStartingLineup() {
    return startingLineup;
  }

  public void setStartingLineup(List<Player> startingLineup) {
    this.startingLineup = startingLineup;
  }

}
