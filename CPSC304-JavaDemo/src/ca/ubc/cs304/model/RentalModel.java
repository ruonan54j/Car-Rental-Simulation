package ca.ubc.cs304.model;

import java.time.LocalDateTime;

/**
 * Model for a Reservation Object
 */
public class RentalModel {
    private final String rid;
    private final String vid;
    private final String dlicense;
    private final LocalDateTime start;
    private final LocalDateTime end;
    private final int odometer;
    private final String cardName;
    private final int cardNo;
    private final LocalDateTime expDate;
    private final int confNo;

    public RentalModel(String rid, String vid, String dlicense, LocalDateTime start, LocalDateTime end, int odometer,
            String cardName, int cardNo, LocalDateTime expDate, int confNo) {
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

    public LocalDateTime getExpDate() {
        return expDate;
    }

    public int getCardNo() {
        return cardNo;
    }

    public String getCardName() {
        return cardName;
    }

    public int getOdometer() {
        return odometer;
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

    public String getVid() {
        return vid;
    }

    public String getRid() {
        return rid;
    }
}
