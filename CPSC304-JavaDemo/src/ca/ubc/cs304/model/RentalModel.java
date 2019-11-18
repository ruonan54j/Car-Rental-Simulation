package ca.ubc.cs304.model;

import java.time.Instant;

/**
 * Model for a Rental object
 */
public class RentalModel {

    // Fields for defining "rental"
    private final String rid;
    private final int confNo;
    private final double startOdometer;
    private final String cardName;
    private final Instant expDate; // For credit card?
    private final String cardNo;

    // Fields for defining "return"
    private final Instant returnTimestamp; // If this field is null, vehicle has not been returned
    private final double endOdometer;
    private final boolean fulltank;
    private final double finalcost;

    public RentalModel(String rid, int confNo, int startOdometer, String cardName,
                Instant expDate, String cardNo, Instant returnTimestamp, int endOdometer,
                boolean fulltank, double finalcost) {
        this.rid = rid;
        this.confNo = confNo;
        this.startOdometer = startOdometer;
        this.cardName = cardName;
        this.expDate = expDate;
        this.cardNo = cardNo;

        this.returnTimestamp = returnTimestamp;
        this.endOdometer = endOdometer;
        this.fulltank = fulltank;
        this.finalcost = finalcost;
    }

    public double getFinalcost() {
        return finalcost;
    }

    public boolean isFulltank() {
        return fulltank;
    }

    public double getEndOdometer() {
        return endOdometer;
    }

    public Instant getReturnTimestamp() {
        return returnTimestamp;
    }

    public double getStartOdometer() {
        return startOdometer;
    }

    public int getConfNo() {
        return confNo;
    }

    public Instant getExpDate() {
        return expDate;
    }

    public String getCardNo() {
        return cardNo;
    }

    public String getCardName() {
        return cardName;
    }

    public String getRid() {
        return rid;
    }
}
