package yolo.basket.db.stats;

// This entity is NOT to be stored in the DATABASE!
// Rather it is calculated by the current data in the database
//
// The purpose is to receive data, calculate it and
// be sent back through a response as JSON so it 
// doesn't have to be calculated on the front end.

import java.util.Arrays;
import java.util.List;

import yolo.basket.db.Calculator;
import yolo.basket.db.Entity;
import yolo.basket.db.Param;
import yolo.basket.db.gameEvent.GameEvent;

/**
 *
 * Important:
 *
 * The constants for the int[][] data object are located in 
 * GameEvent.java, since it's an int[][] array then we have
 * names for each of these integers so we know what they are
 * 
 * Example:
 * you will have 
 *    data[GameEvent.HIT][GameEvent.LEFT_CORNER] == 5
 * or equivalent
 *    data[1][4] == 5
 *
 */

public class Stats extends Entity {
  // Input
  private int[][] data;

  // Unused
  private long id;
  private long playerId;

  // Calculated
  private int block;
  private int turnover;
  private int miss;
  private int hit;
  private int assist;
  private int rebound;
  private int[] hitsByLocation;
  private int[] missByLocation;
  private int[] hitsByPoints;
  private int[] missByPoints;

  public void calculatePointsByLocation(int[] hits, int[] miss) {
    hitsByPoints = new int[4];
    missByPoints = new int[4];

    for (int i = 0; i < hits.length; i++)
      hitsByPoints[GameEvent.locationPoints(i)] += hits[i];

    for (int i = 0; i < miss.length; i++)
      missByPoints[GameEvent.locationPoints(i)] += miss[i];
  }

  public void recalculateStats() {
    this.hitsByLocation = data[GameEvent.HIT];
    this.missByLocation = data[GameEvent.MISS];
    calculatePointsByLocation(hitsByLocation, missByLocation);

    int[] totals = Calculator.sumAll(data);

    this.block = totals[GameEvent.BLOCK];
    this.turnover = totals[GameEvent.TURNOVER];
    this.miss = totals[GameEvent.MISS];
    this.hit = totals[GameEvent.HIT];
    this.assist = totals[GameEvent.ASSIST];
    this.rebound = totals[GameEvent.REBOUND];
  }

  public Stats() {
    this.data = blankData();
  }

  // Not used, this method is only needed when sending the data to the server
  @Override
  public List<Param> getParameters() {
    return null;
  }

  // To calculate stats for a specific player
  public Stats(int[][] data)  {
    this.data = data;
    recalculateStats();
  }

  public static int[][] blankData() {
    return new int[GameEvent.N_GAME_EVENTS][GameEvent.N_LOCATIONS];
  }

  public int sum(int[] array) {
    return Arrays.stream(array).sum();
  }

  public double ratioHit() {
    return (double) hit / (hit + miss);
  }

  public void addData(int[][] moreData) {
    Calculator.addToData(this.data, moreData);
  }

  public int[][] getData() {
    return data;
  }

  public void setData(int[][] data) {
    this.data = data;
  }

  public int getBlock() {
    return block;
  }

  public void setBlock(int block) {
    this.block = block;
  }

  public int getTurnover() {
    return turnover;
  }

  public void setTurnover(int turnover) {
    this.turnover = turnover;
  }

  public int getMiss() {
    return miss;
  }

  public void setMiss(int miss) {
    this.miss = miss;
  }

  public int getRebound() {
    return rebound;
  }

  public void setRebound(int rebound) {
    this.rebound = rebound;
  }

  public int getHit() {
    return hit;
  }

  public void setHit(int hit) {
    this.hit = hit;
  }

  public int getAssist() {
    return assist;
  }

  public void setAssist(int assist) {
    this.assist = assist;
  }

  public int[] getHitsByPoints() {
    return hitsByPoints;
  }

  public void setHitsByPoints(int[] hitsByPoints) {
    this.hitsByPoints = hitsByPoints;
  }

  public int[] getMissByPoints() {
    return missByPoints;
  }

  public void setMissByPoints(int[] missByPoints) {
    this.missByPoints = missByPoints;
  }  
  public int[] getHitsByLocation() {
    return hitsByLocation;
  }

  public void setHitsByLocation(int[] hitsByLocation) {
    this.hitsByLocation = hitsByLocation;
  }

  public int[] getMissByLocation() {
    return missByLocation;
  }

  public void setMissByLocation(int[] missByLocation) {
    this.missByLocation = missByLocation;
  }
}

