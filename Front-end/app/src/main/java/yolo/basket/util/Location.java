package yolo.basket.util;

public class Location {
    double x;
    double y;

    int courtLocation;

    public Location(double x, double y, int courtLocation) {
        this.x = x;
        this.y = y;
        this.courtLocation = courtLocation;
    }

    public double squaredDistance(Location location) {
        double diffX = x - location.getX();
        double diffY = y - location.getY();
        return Math.pow(diffY, 2) + Math.pow(diffY, 2);
    }

    public double distanceTo(Location location) {
        return Math.sqrt(squaredDistance(location));
    }

    private double getY() {
        return y;
    }

    private double getX() {
        return x;
    }

    public int getCourtLocation() {
        return courtLocation;
    }
}
