package fr.bourgmapper.tub.domain;

/**
 * Class that represents a stop in the domain layer.
 */
public class Stop {

    private final long stopId;
    private String label;
    private String latitude;
    private String longitude;

    public Stop(long stopId) {
        this.stopId = stopId;
    }

    public long getStopId() {
        return stopId;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
