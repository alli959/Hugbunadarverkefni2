package project.persistence.entities;

// Used to calculate stats for the Stats object
public class Calculator {

  // Used to get data from many different games into a single int[][]
  public static void addToData(int[][] out, int[][] toBeAdded) {
    for (int i = 0; i < out.length; i++)
      for (int j = 0; j < out[i].length; j++)
        out[i][j] += toBeAdded[i][j];
  }

  // Sums each of the subArrays to get totals for stats
  public static int[] sumAll(int[][] data) {
    int[] total = new int[data.length];
    for (int i = 0; i < total.length; i++)
      total[i] = sum(data[i]);
    return total;
  }

  // basic sum of array
  public static int sum (int[] data) {
    int sum = 0;
    for (int a : data)
      sum += a;
    return sum;
  }
}
