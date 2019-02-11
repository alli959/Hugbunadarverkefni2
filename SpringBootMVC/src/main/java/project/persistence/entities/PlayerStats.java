
package project.persistence.entities;

import project.persistence.entities.GameEvent;
import project.persistence.entities.Calculator;

import javax.persistence.*;

@Entity
public class PlayerStats {
  public PlayerStats() { }

  @Id
  private long playerId;

  private int blocks;
  private int turnover;
  private int miss;
  private int hit;
  private int assist;
  private int rebound;
  private int[] hitsByLocation;
  private int[] missByLocation;

  private int hits = 0;

  // We use this constructor to get the raw data in and
  // putting into readable form. That way it can be returned
  // from the controller as JSON.

  public void recalculateStats() {
    this.hitsByLocation = data[GameEvent.HIT];
    this.missByLocation = data[GameEvent.MISS];

    int[] totals = Calculator.sumAll(data);

    this.blocks = totals[GameEvent.BLOCK];
    this.turnover = totals[GameEvent.TURNOVER];
    this.miss = totals[GameEvent.MISS];
    this.hit = totals[GameEvent.HIT];
    this.assist = totals[GameEvent.ASSIST];
    this.rebound = totals[GameEvent.REBOUND];
  }

  public PlayerStats(int[][] data, Long playerId)  {
    this.playerId = playerId;
    this.data = data;
    recalculateStats();
  }

  private int[][] data;

  public int[][] getData() {
    return data;
  }

  public void setData(int[][] data) {
    this.data = data;
  }

}
