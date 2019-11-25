package ca.ubc.cs304.model;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

/**
 * Model for a rental receipt
 */
public class RentalReceipt {

    private DateTimeFormatter formatter =
        DateTimeFormatter.ofLocalizedDateTime( FormatStyle.SHORT )
                     .withLocale( Locale.CANADA )
                     .withZone( ZoneId.systemDefault() );

    // Fields for defining "rental"
    private final int rid;
    private final int confNo;
    private final double startOdometer;
    private final String cardName;
    private final String expDate;
    private final String cardNo;
    private final String dlicense;

    // Fees and vehicle info
    private final int vid;
    private final String vehicleType;
    private final String location;

    // Alloted time
    private final String startTimestamp;
    private final String endTimestamp;

    public RentalReceipt(int rid, int confNo, double startOdometer, String cardName, Instant expDate, String cardNo,
            String dlicense, int vid, String vehicleType, String location, Instant startTimestamp,
            Instant endTimestamp) {
        this.rid = rid;
        this.confNo = confNo;
        this.startOdometer = startOdometer;
        this.cardName = cardName;
        this.expDate = formatter.format(expDate); //Format
        this.cardNo = cardNo;
        this.dlicense = dlicense;
        this.vid = vid;
        this.vehicleType = vehicleType;
        this.location = location;
        this.startTimestamp = formatter.format(startTimestamp); //Changed
        this.endTimestamp = formatter.format(endTimestamp); //Changed
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

    public String getExpDate() {
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

    public String getStartTimestamp() {
        return startTimestamp;
    }

    public String getEndTimestamp() {
        return endTimestamp;
    }

}
