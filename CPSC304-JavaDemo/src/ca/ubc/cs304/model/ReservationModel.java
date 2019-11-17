package ca.ubc.cs304.model;

import java.time.Instant;

/**
 * Model for a Reservation Object
 */
public class ReservationModel {
    private final int confNo;
    private final String vtname;
    private final String dlicense;
    private final Instant start;
    private final Instant end;

    public ReservationModel(int confNo, String vtname, String dlicense, Instant start, Instant end) {
        this.confNo = confNo;
        this.vtname = vtname;
        this.dlicense = dlicense;
        this.start = start;
        this.end = end;
    }

    public Instant getEnd() {
        return end;
    }

    public Instant getStart() {
        return start;
    }

    public String getDlicense() {
        return dlicense;
    }

    public String getVtname() {
        return vtname;
    }

    public int getConfNo() {
        return confNo;
    }
}
