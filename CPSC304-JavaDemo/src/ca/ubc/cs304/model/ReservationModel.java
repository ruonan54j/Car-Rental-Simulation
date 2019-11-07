package ca.ubc.cs304.model;

import java.time.LocalDateTime;

/**
 * Model for a Reservation Object
 */
public class ReservationModel {
    private final int confNo;
    private final String vtname;
    private final String dlicense;
    private final LocalDateTime start;
    private final LocalDateTime end;

    public ReservationModel(int confNo, String vtname, String dlicense, LocalDateTime start, LocalDateTime end) {
        this.confNo = confNo;
        this.vtname = vtname;
        this.dlicense = dlicense;
        this.start = start;
        this.end = end;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public LocalDateTime getStart() {
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
