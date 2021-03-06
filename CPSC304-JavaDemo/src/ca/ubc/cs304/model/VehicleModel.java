package ca.ubc.cs304.model;

/**
 * Model for a Vehicle Object
 */
public class VehicleModel {
    private final int vid;
    private final String vlicense;
    private final String make;
    private final int year;
    private final String color;
    private final double odometer; // Assume in km
    private final String status;
    private final String vtname;
    private final String location;

    public VehicleModel(int vid, String vlicense, String make, int year, String color, double odometer, String status,
            String vtname, String location) {
        this.vid = vid;
        this.vlicense = vlicense;
        this.make = make;
        this.year = year;
        this.color = color;
        this.odometer = odometer;
        this.status = status;
        this.vtname = vtname;
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

    public String getVtname() {
        return vtname;
    }

    public String getStatus() {
        return status;
    }

    public double getOdometer() {
        return odometer;
    }

    public String getColor() {
        return color;
    }

    public int getYear() {
        return year;
    }

    public String getMake() {
        return make;
    }

    public String getVlicense() {
        return vlicense;
    }

    public int getVid() {
        return vid;
    }

}
