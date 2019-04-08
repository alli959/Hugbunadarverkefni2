package yolo.basket.db.game;

import java.util.List;
import java.util.ArrayList;

import yolo.basket.db.Entity;
import yolo.basket.db.Param;
import yolo.basket.db.gameEvent.GameEvent;
import yolo.basket.db.player.Player;

public class Game extends Entity {

  private Long id;

  private List<GameEvent> gameEvents = new ArrayList<>();
  private List<Player> bench = new ArrayList<>();
  private List<Player> startingLineup = new ArrayList<>();
  private String stadiumName = "Not set";
  private Long timeOfGame = 0L;
  private Long teamId;

  public Game() { }

  @Override
  public List<Param> getParameters() {
    List<Param> params = new ArrayList<>();
    params.addAll(Param.getIdsOfEntitiesAsParams("gameEvent", gameEvents));
    params.addAll(Param.getIdsOfEntitiesAsParams("bench", bench));
    params.addAll(Param.getIdsOfEntitiesAsParams("playing", startingLineup));
    params.add(new Param("stadiumName", stadiumName));
    params.add(new Param("timeOfGame", timeOfGame.toString()));
    params.add(new Param("teamId", teamId.toString()));
    if (id != null)
      params.add(new Param("id", id.toString()));
    return params;
  }

  // Get the int[][] data to put into a Stats object for only getOne player
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
