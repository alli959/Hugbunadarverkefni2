package yolo.basket.util;

import java.text.DecimalFormat;
import java.util.HashMap;

import yolo.basket.db.gameEvent.GameEvent;
import yolo.basket.db.stats.Stats;

public class StatsFormatter {

    private final int gamesPlayed;
    private Stats stats;

    public StatsFormatter(Stats stats, int gamesPlayed) {
        this.stats = stats;
        this.gamesPlayed = gamesPlayed;
    }

    public HashMap<String, String> getSummaryMap() {
        int totalMiss = stats.getMiss();
        int totalHit = stats.getHit();
        double ratio = stats.ratioHit();
        double hitsPerGame = ratioOf(totalHit, gamesPlayed);
        double missPerGame = ratioOf(totalMiss, gamesPlayed);

        HashMap<String, String> totalsMap = new HashMap<>();
        totalsMap.put("Shots hit", intToString(totalHit));
        totalsMap.put("Shots missed", intToString(totalMiss));
        totalsMap.put("Ratio hit", formatPercentage(ratio));
        totalsMap.put("Hits per game", formatDouble(hitsPerGame));
        totalsMap.put("Misses per game", formatDouble(missPerGame));
        return totalsMap;
    }

    public HashMap<String, String[]> getHitsByLocationMap() {
        int[] hitsByLocation = stats.getHitsByLocation();
        int[] missByLocation = stats.getMissByLocation();

        double[] ratios = ratioOf(hitsByLocation, missByLocation);

        HashMap<String, String[]> hitsByLocationMap = new HashMap<>();
        hitsByLocationMap.put("Left short", totalsAndRatios(hitsByLocation, ratios, GameEvent.LEFT_SHORT));
        hitsByLocationMap.put("Right short", totalsAndRatios(hitsByLocation, ratios, GameEvent.RIGHT_SHORT));
        hitsByLocationMap.put("Left top", totalsAndRatios(hitsByLocation, ratios, GameEvent.LEFT_TOP));
        hitsByLocationMap.put("Right top", totalsAndRatios(hitsByLocation, ratios, GameEvent.RIGHT_TOP));
        hitsByLocationMap.put("Top", totalsAndRatios(hitsByLocation, ratios, GameEvent.TOP));
        hitsByLocationMap.put("Right corner", totalsAndRatios(hitsByLocation, ratios, GameEvent.RIGHT_CORNER));
        hitsByLocationMap.put("Left corner", totalsAndRatios(hitsByLocation, ratios, GameEvent.LEFT_CORNER));
        hitsByLocationMap.put("Right wing", totalsAndRatios(hitsByLocation, ratios, GameEvent.RIGHT_WING));
        hitsByLocationMap.put("Left wing", totalsAndRatios(hitsByLocation, ratios, GameEvent.LEFT_WING));
        return hitsByLocationMap;
    }

    public HashMap<String, String[]> pointsMap() {
        int[] hitsByPoints = stats.getHitsByPoints();
        int[] missByPoints = stats.getMissByPoints();

        double[] ratios = ratioOf(hitsByPoints, missByPoints);

        HashMap<String, String[]> pointsMap = new HashMap<>();
        pointsMap.put("3 pointers", totalsAndRatios(hitsByPoints, ratios, 3));
        pointsMap.put("2 pointers", totalsAndRatios(hitsByPoints, ratios, 2));
        pointsMap.put("Free throws", totalsAndRatios(hitsByPoints, ratios, 1));
        return pointsMap;
    }

    private String[] totalsAndRatios(int[] hits, double[] ratios, int index) {
        return new String[]{ intToString(hits[index]), formatPercentage(ratios[index])};
    }

    private double ratioOf(int a, int b) {
        return (double) a / b;
    }

    private double[] ratioOf(int[] a, int[] b) {
        double[] ratios = new double[a.length];
        for (int i = 0; i < ratios.length; i++)
            ratios[i] = ratioOf(a[i], b[i]);
        return ratios;
    }

    private String formatPercentage(double d) {
         String percentage = new DecimalFormat("#.#").format(d * 100);
         return percentage + "%";
    }

    private String formatDouble(double d) {
        return new DecimalFormat("#.#").format(d);
    }

    private String intToString(int k) {
        return String.valueOf(k);
    }


}
