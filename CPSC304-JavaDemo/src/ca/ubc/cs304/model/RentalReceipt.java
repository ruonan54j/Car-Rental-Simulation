package ca.ubc.cs304.model;

import java.time.Instant;

/**
 * Model for a rental receipt
 */
public class RentalReceipt {

    // Fields for defining "rental"
    private final int rid;
    private final int confNo;
    private final double startOdometer;
    private final String cardName;
    private final Instant expDate;
    private final String cardNo;
    private final String dlicense;

    // Fees and vehicle info
    private final int vid;
    private final String vehicleType;
    private final String location;

    // Alloted time
    private final Instant startTimestamp;
    private final Instant endTimestamp;

    public RentalReceipt(int rid, int confNo, double startOdometer, String cardName, Instant expDate, String cardNo,
            String dlicense, int vid, String vehicleType, String location, Instant startTimestamp,
            Instant endTimestamp) {
        this.rid = rid;
        this.confNo = confNo;
        this.startOdometer = startOdometer;
        this.cardName = cardName;
        this.expDate = expDate;
        this.cardNo = cardNo;
        this.dlicense = dlicense;
        this.vid = vid;
        this.vehicleType = vehicleType;
        this.location = location;
        this.startTimestamp = startTimestamp;
        this.endTimestamp = endTimestamp;
    }

    public int getRid() {
        return rid;
    }

    public int getConfNo() {
        return confNo;
    }

    public double getStartOdometer() {
        return startOdometer;
    }

    public String getCardName() {
        return cardName;
    }

    public Instant getExpDate() {
        return expDate;
    }

    public String getCardNo() {
        return cardNo;
    }

    public String getDlicense() {
        return dlicense;
    }

    public int getVid() {
        return vid;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public String getLocation() {
        return location;
    }

    public Instant getStartTimestamp() {
        return startTimestamp;
    }

    public Instant getEndTimestamp() {
        return endTimestamp;
    }

}
