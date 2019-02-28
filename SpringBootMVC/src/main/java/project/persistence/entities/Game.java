package project.persistence.entities;

import javax.persistence.*;
import java.util.List;
import java.util.ArrayList;

@Entity
public class Game extends Stats {

  @Id
  private Long id;

  @OneToMany
  private List<GameEvent> gameEvents = new ArrayList<>();
  @ManyToMany
  private List<Player> bench = new ArrayList<>();
  @ManyToMany
  private List<Player> startingLineup = new ArrayList<>();
  private String stadiumName = "Not set";
  private Long timeOfGame = 0L;
  private Long teamId;

  public Game() { }

  // Get the int[][] data to put into a Stats object for only one player
  public int[][] compileStats(Long playerId) {
    int[][] stats = new int[GameEvent.N_GAME_EVENTS][GameEvent.N_LOCATIONS];
    for (GameEvent ge : gameEvents)
      if (playerId == ge.getPlayerId() || playerId == 0)
        stats[ge.getEventType()][ge.getLocation()]++;
    return stats;
  }

  // Get the int[][] totals for the entire game
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

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getStadiumName() {
    return stadiumName;
  }

  public void setStadiumName(String stadiumName) {
    this.stadiumName = stadiumName;
  }

  public Long getTimeOfGame() {
    return timeOfGame;
  }

  public void setTimeOfGame(Long timeOfGame) {
    this.timeOfGame = timeOfGame;
  }

  public Long getTeamId() {
    return teamId;
  }

  public void setTeamId(Long teamId) {
    this.teamId = teamId;
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
