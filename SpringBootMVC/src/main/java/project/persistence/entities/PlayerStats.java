
package project.persistence.entities;

import javax.persistence.*;
import static javax.persistence.GenerationType.*;

import java.util.ArrayList;


@Entity
public class PlayerStats {
  public PlayerStats() { }

  private int hits = 0;

  public PlayerStats(int[][] data) {
    this.data = data;
    // do some calculation to get the data into
    // variables so they can be sent via request
  }

  private int[][] data;

  public int[][] getData() {
    return data;
  }

  public void setData() {
    this.data = data;
  }

}
