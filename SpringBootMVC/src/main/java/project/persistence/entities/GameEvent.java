

package project.persistence.entities;

import javax.persistence.*;
import static javax.persistence.GenerationType.*;

/*
   This is a huge database table for the shots, hits or miss
   */
@Entity
public class GameEvent {

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
  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Long id;
  private long timeOfEvent;
  private int eventType;
  private int location;
  private long playerId;

  public static int getLocationByName(String location) throws Exception {
    switch (location) {
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

  public static int getEventTypeByName(String type) throws Exception {
    switch (type) {
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

  public GameEvent(int location, int eventType, int timeOfEvent, long playerId) {
    this.location = location;
    this.eventType = eventType;
    this.timeOfEvent = timeOfEvent;
    this.playerId = playerId;
  }

  public GameEvent() {

  }

  public boolean isThreePointLocation() {
    return
      location == LEFT_WING ||
      location == RIGHT_WING ||
      location == TOP ||
      location == LEFT_CORNER ||
      location == RIGHT_CORNER;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public long getTimeOfEvent() {
    return timeOfEvent;
  }

  public void setTimeOfEvent(long timeOfEvent) {
    this.timeOfEvent = timeOfEvent;
  }

  public long getPlayerId() {
    return playerId;
  }

  public void setPlayerId(long playerId) {
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
