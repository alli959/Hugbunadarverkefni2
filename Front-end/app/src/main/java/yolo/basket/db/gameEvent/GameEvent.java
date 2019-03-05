package yolo.basket.db.gameEvent;

import java.util.ArrayList;
import java.util.List;

import yolo.basket.db.Entity;
import yolo.basket.db.Param;

/*
   This is a huge database table for the shots, hits or miss
   */
public class GameEvent extends Entity {

  /**
   *
   * Important:
   *
   * Basically these constants are here for the int[][] data object
   * For exmaple if there were 5 hits from LEFT_CORNER in the game
   * you will have 
   *    data[HIT][LEFT_CORNER] == 5
   * or simply
   *    data[1][4] == 5
   *
   */
  
  // Location constants
  public static final int NONE = 0;
  public static final int LEFT_WING = 1;
  public static final int RIGHT_WING = 2;
  public static final int TOP = 3;
  public static final int LEFT_CORNER = 4;
  public static final int RIGHT_CORNER = 5;
  public static final int LEFT_SHORT = 6;
  public static final int RIGHT_SHORT = 7;
  public static final int LEFT_TOP = 8;
  public static final int RIGHT_TOP = 9;
  public static final int LAY_UP = 10;
  public static final int FREE_THROW = 11;

  public static final int N_LOCATIONS = 12;

  // Event type constants
  public static final int MISS = 0;
  public static final int HIT = 1;
  public static final int FOUL = 2;
  public static final int ASSIST = 3;
  public static final int REBOUND = 4;
  public static final int BLOCK = 5;
  public static final int TURNOVER = 6;

  public static final int N_GAME_EVENTS = 7;

  // Class data
  private Long id;
  private Long timeOfEvent;
  private int eventType;
  private int location;
  private Long playerId;

  // Because the input of the controller methods are text 
  public static int getLocationByName(String location) throws Exception {
    switch (location.toUpperCase()) {
      case "NONE": return NONE;
      case "TOP": return TOP;
      case "LEFT_WING": return LEFT_WING;
      case "LEFT_CORNER": return LEFT_CORNER;
      case "LEFT_SHORT": return LEFT_SHORT;
      case "LEFT_TOP": return LEFT_TOP;
      case "RIGHT_CORNER": return RIGHT_CORNER;
      case "RIGHT_WING": return RIGHT_WING;
      case "RIGHT_SHORT": return RIGHT_SHORT;
      case "RIGHT_TOP": return RIGHT_TOP;
      case "LAY_UP": return LAY_UP;
      case "FREE_THROW": return FREE_THROW;
      default: throw new Exception("Invallid location: " + location + " valid locations are "
                   + "NONE, TOP, LEFT_CORNER, LEFT_WING, LEFT_TOP, LEFT_SHORT, RIGHT_CORNER"
                   + "RIGHT_WING, RIGHT_SHORT, RIGHT_TOP, TOP, LAY_UP or FREE_THROW");
    }
  }

  // Because the input of the controller methods are text
  public static int getEventTypeByName(String type) throws Exception {
    switch (type.toUpperCase()) {
      case "HIT": return HIT;
      case "MISS": return MISS;
      case "ASSIST": return ASSIST;
      case "FOUL": return FOUL;
      case "REBOUND": return REBOUND;
      case "BLOCK": return BLOCK;
      case "TURNOVER": return TURNOVER;
      default: throw new Exception("Invallid EventType: " + type + " valid eventTypes are "
                   + "HIT, MISS, ASSIST, FOUL, REBOUND, BLOCK or TURNOVER");
    }
  }


  @Override
  public List<Param> getParameters() {
    List<Param> params = new ArrayList<>();
    if (id != null)
      params.add(new Param("id", id.toString()));
    params.add(new Param("timeOfEvent", timeOfEvent.toString()));
    params.add(new Param("eventType", Integer.toString(eventType)));
    params.add(new Param("location", Integer.toString(location)));
    params.add(new Param("playerId", playerId.toString()));
    return params;
  }

  // Useless
  public GameEvent(int location, int eventType, Long timeOfEvent, Long playerId) {
    this.location = location;
    this.eventType = eventType;
    this.timeOfEvent = timeOfEvent;
    this.playerId = playerId;
  }

  public GameEvent() {

  }

  public static int locationPoints(int location) {
    if (location == NONE) return 0;
    if (location == FREE_THROW) return 1;
    return 
      location == LEFT_WING ||
      location == RIGHT_WING ||
      location == TOP ||
      location == LEFT_CORNER ||
      location == RIGHT_CORNER 
      ? 3 : 2;

  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getTimeOfEvent() {
    return timeOfEvent;
  }

  public void setTimeOfEvent(Long timeOfEvent) {
    this.timeOfEvent = timeOfEvent;
  }

  public Long getPlayerId() {
    return playerId;
  }

  public void setPlayerId(Long playerId) {
    this.playerId = playerId;
  }


  public int getEventType() {
    return eventType;
  }

  public void setEventType(int eventType) {
    this.eventType = eventType;
  }

  public int getLocation() {
    return location;
  }

  public void setLocation(int location) {
    this.location = location;
  }

}
