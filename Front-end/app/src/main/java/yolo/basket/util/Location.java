package yolo.basket.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import yolo.basket.db.gameEvent.GameEvent;

public class Location {
    double x;
    double y;

    int courtLocation;

    // Gives the GameEvent location type a location on the image
    // Used to calculate which one is closest for approximate solution
    private static final List<Location> LOCATIONS = new ArrayList<>(Arrays.asList(
            new Location(0.09, 0.18, GameEvent.LEFT_CORNER),
            new Location(0.90, 0.18, GameEvent.RIGHT_CORNER),
            new Location(0.86, 0.59, GameEvent.RIGHT_WING),
            new Location(0.66, 0.64, GameEvent.LEFT_WING),
            new Location(0.74, 0.16, GameEvent.RIGHT_SHORT),
            new Location(0.74, 0.27, GameEvent.RIGHT_SHORT),
            new Location(0.26, 0.16, GameEvent.LEFT_SHORT),
            new Location(0.26, 0.27, GameEvent.LEFT_SHORT),
            new Location(0.50, 0.29, GameEvent.LAY_UP),
            new Location(0.50, 0.50, GameEvent.TOP),
            new Location(0.31, 0.79, GameEvent.RIGHT_TOP),
            new Location(0.66, 0.80, GameEvent.LEFT_TOP)
    ));

    public Location(double x, double y, int courtLocation) {
        this.x = x;
        this.y = y;
        this.courtLocation = courtLocation;
    }

    public static int getLocation(Location clickLocation) {
        Location closestLocation = LOCATIONS.stream().reduce(
                (loc1, loc2) -> clickLocation.distanceTo(loc1) < clickLocation.distanceTo(loc2) ?
                        loc1 : loc2).get();
        return closestLocation.getCourtLocation();
    }

    private double squaredDistance(Location location) {
        double diffX = x - location.getX();
        double diffY = y - location.getY();
        return Math.pow(diffX, 2) + Math.pow(diffY, 2);
    }


    private double distanceTo(Location location) {
        return Math.sqrt(squaredDistance(location));
    }

    private double getY() {
        return y;
    }

    private double getX() {
        return x;
    }

    private int getCourtLocation() {
        return courtLocation;
    }
}
