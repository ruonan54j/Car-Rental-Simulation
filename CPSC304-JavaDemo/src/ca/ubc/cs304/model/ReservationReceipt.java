package ca.ubc.cs304.model;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

/**
 * Model for a Reservation Object
 */
public class ReservationReceipt {
    private final int confNo;
    private final int vid;
    private final String dlicense;
    private final String startTimestamp;
    private final String endTimestamp;
    private final String cardName;
    private final String cardNo;
    private final String expDate; // of card
    private final String vehicleType;
    private final String location;

    private DateTimeFormatter formatter =
    DateTimeFormatter.ofLocalizedDateTime( FormatStyle.SHORT )
                 .withLocale( Locale.CANADA )
                 .withZone( ZoneId.systemDefault() );

    public ReservationReceipt(int confNo, int vid, String dlicense, Instant startTimestamp, Instant endTimestamp,
            String cardName, String cardNo, Instant expDate, String vehicleType, String location) {
        this.confNo = confNo;
        this.vid = vid;
        this.dlicense = dlicense;
        this.startTimestamp = formatter.format(startTimestamp);
        this.endTimestamp = formatter.format(endTimestamp);
        this.cardName = cardName;
        this.cardNo = cardNo;
        this.expDate = formatter.format(expDate);
        this.vehicleType = vehicleType;
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public String getExpDate() {
        return expDate;
    }

    public String getCardNo() {
        return cardNo;
    }

    public String getCardName() {
        return cardName;
    }

    public String getendTimestamp() {
        return endTimestamp;
    }

    public String getstartTimestamp() {
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
