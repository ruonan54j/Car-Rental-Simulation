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
    private final int odometer; // Assume in km?
    private final int status; // Change to boolean or use enum?
    private final String vtname;
    private final String location;

    public VehicleModel(int vid, String vlicense, String make, int year, String color, int odometer, int status,
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

    public int getStatus() {
        return status;
    }

    public int getOdometer() {
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
