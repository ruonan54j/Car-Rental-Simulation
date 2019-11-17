package ca.ubc.cs304.model;

import java.time.Instant;

/**
 * Model for a Rental object
 */
public class RentalModel {
    private final String rid;
    private final String vid;
    private final String dlicense;
    private final Instant start;
    private final Instant end;
    private final int odometer;
    private final String cardName;
    private final Instant expDate; //For credit card?
    private final String cardNo;
    private final int confNo;

    //Fields for defining "return"
    /*
    private final Instant returnDate;
    private final int returnOdometer;
    private final boolean fulltank;
    private final int value; //idk what this is for
    */

    public RentalModel(String rid, String vid, String dlicense, Instant start, Instant end, int odometer,
            String cardName, String cardNo, Instant expDate, int confNo) {
        this.rid = rid;
        this.vid = vid;
        this.dlicense = dlicense;
        this.start = start;
        this.end = end;
        this.odometer = odometer;
        this.cardName = cardName;
        this.cardNo = cardNo;
        this.expDate = expDate;
        this.confNo = confNo;
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

    public int getOdometer() {
        return odometer;
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

    public String getVid() {
        return vid;
    }

    public String getRid() {
        return rid;
    }
}
