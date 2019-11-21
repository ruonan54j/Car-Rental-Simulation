package ca.ubc.cs304.model;

import java.time.Instant;

/**
 * Model for a Rental object
 */
public class RentalModel {

    // Fields for defining "rental"
    private final int rid;
    private final int vid;
    private final int confNo;
    private final double startOdometer;
    private final Instant beginTimestamp;

    // Fields for defining "return"
    private final Instant returnTimestamp; // If this field is null, vehicle has not been returned
    private final double endOdometer;
    private final boolean fulltank;
    private final double finalcost;

    public RentalModel(int rid, int vid, int confNo, int startOdometer, Instant beginTimestamp, Instant returnTimestamp,
            int endOdometer, boolean fulltank, double finalcost) {
        this.rid = rid;
        this.vid = vid;
        this.confNo = confNo;
        this.startOdometer = startOdometer;
        this.beginTimestamp = beginTimestamp;

        this.returnTimestamp = returnTimestamp;
        this.endOdometer = endOdometer;
        this.fulltank = fulltank;
        this.finalcost = finalcost;
    }

    public int getVid() {
        return vid;
    }

    public Instant getBeginTimestamp() {
        return beginTimestamp;
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

    public int getRid() {
        return rid;
    }
}
