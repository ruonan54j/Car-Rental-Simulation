package ca.ubc.cs304.model;

/**
 * Model for a container for vehicles and their return price
 */
public class VehicleReturnWithPrice {

    // Fields for defining "rental"
    private final VehicleModel vehicle;
    private final double price;

    public VehicleReturnWithPrice(VehicleModel vehicle, double price) {
        this.vehicle = vehicle;
        this.price = price;
    }

    public VehicleModel getVehicle() {
        return vehicle;
    }

    public double getPrice() {
        return price;
    }


}
