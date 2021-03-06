package ca.ubc.cs304.model;

import java.time.Instant;

/**
 * Model for a Reservation Object
 */
public class ReservationModel {
    private final int confNo;
    private final int vid;
    private final String dlicense;
    private final Instant startTimestamp;
    private final Instant endTimestamp;
    private final String cardName;
    private final String cardNo;
    private final Instant expDate; // of card

    public ReservationModel(int confNo, int vid, String dlicense, Instant startTimestamp, Instant endTimestamp,
            String cardName, String cardNo, Instant expDate) {
        this.confNo = confNo;
        this.vid = vid;
        this.dlicense = dlicense;
        this.startTimestamp = startTimestamp;
        this.endTimestamp = endTimestamp;
        this.cardName = cardName;
        this.cardNo = cardNo;
        this.expDate = expDate;
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

    public Instant getendTimestamp() {
        return endTimestamp;
    }

    public Instant getstartTimestamp() {
        return startTimestamp;
    }

    public String getDlicense() {
        return dlicense;
    }

    public int getVid() {
        return vid;
    }

    public int getConfNo() {
        return confNo;
    }
}
