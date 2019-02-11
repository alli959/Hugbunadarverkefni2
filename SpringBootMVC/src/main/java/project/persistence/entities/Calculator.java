package project.persistence.entities;

public class Calculator {

  public static int[] sumAll(int[][] data) {
    int[] total = new int[data.length];
    for (int i = 0; i < total.length; i++)
      total[i] = sum(data[i]);
    return total;
  }

  public static int sum (int[] data) {
    int sum = 0;
    for (int a : data)
      sum += a;
    return sum;
  }
}
