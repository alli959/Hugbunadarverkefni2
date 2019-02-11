
package project.persistence.entities;

import javax.persistence.*;

// Don't know what im doing lol
// Tharf ad skoda hvernig teamstats eru sett upp til ad skila
// godu JSON med relevant upplysingum
@Entity
public class TeamStats {
  public TeamStats() { }

  @Id
  private int hits = 0;

  public TeamStats(int[][] data) {
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
