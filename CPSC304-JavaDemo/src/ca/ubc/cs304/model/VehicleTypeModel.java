package ca.ubc.cs304.model;

/**
 * Model for a Vehicle Object
 */
public class VehicleTypeModel {

    private final String vtname;
    private final String features;
    private final int hourlyRate;
    private final int kiloRate;
    private final int kiloLimitPerHour;

    public VehicleTypeModel(String vtname, String features, int hourlyRate, int kiloRate, int kiloLimitPerHour) {
        this.vtname = vtname;
        this.features = features;
        this.hourlyRate = hourlyRate;
        this.kiloRate = kiloRate;
        this.kiloLimitPerHour = kiloLimitPerHour;
    }

    public String getVtname() {
        return vtname;
    }

    public String getFeatures() {
        return features;
    }

    public int getHourlyRate() {
        return hourlyRate;
    }

    public int getKiloRate() {
        return kiloRate;
    }

    public int getKiloLimitPerHour() {
        return kiloLimitPerHour;
    }
}
