package ca.ubc.cs304.model;

/**
 * Model for a return receipt Includes original confirmation number, rental id
 * cost break down, return date, etc.
 */
public class ReturnReceipt {

    private final int confNo;
    private final int rentId;
    private final double hourlyRate;
    private final double hoursRented;
    private final double hourlyTotal;
    private final double kiloRate;
    private final double kilosOverAllowed;
    private final double distTotal; //Cost from distance travelled
    private final double gasTotal;
    private final double finalTotal;

    public ReturnReceipt(int confNo, int rentId, double hourlyRate, double hoursRented, double hourlyTotal,
            double kiloRate, double kilosOverAllowed, double distTotal, double gasTotal, double finalTotal) {
        this.confNo = confNo;
        this.rentId = rentId;
        this.hourlyRate = hourlyRate;
        this.hoursRented = hoursRented;
        this.hourlyTotal = hourlyTotal;
        this.kiloRate = kiloRate;
        this.kilosOverAllowed = kilosOverAllowed;
        this.distTotal = distTotal;
        this.gasTotal = gasTotal;
        this.finalTotal = finalTotal;
    }

    public double getDistTotal() {
        return distTotal;
    }

    public int getConfNo() {
        return confNo;
    }

    public int getRentId() {
        return rentId;
    }

    public double getHourlyRate() {
        return hourlyRate;
    }

    public double getHoursRented() {
        return hoursRented;
    }

    public double getHourlyTotal() {
        return hourlyTotal;
    }

    public double getKiloRate() {
        return kiloRate;
    }

    public double getKilosOverAllowed() {
        return kilosOverAllowed;
    }

    public double getGasTotal() {
        return gasTotal;
    }

    public double getFinalTotal() {
        return finalTotal;
    }

}
