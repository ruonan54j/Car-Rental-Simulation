package ca.ubc.cs304.model;

import java.util.*;
import ca.ubc.cs304.model.DailyReturnReportBranch;

/**
 * Model for a container for vehicles and their return price
 */
public class DailyReturnReport {

    public Map<String, DailyReturnReportBranch> branchReports; //Branch name to each report
    public int totalVehicles;
    public double totalRevenue;

    //We can assume lists are already sorted by branch, and then vehicle type
    public DailyReturnReport (List<VehicleModel> vehicles, List<Double> prices) {
        this.branchReports = new HashMap<>();
        totalVehicles = 0;
        totalRevenue = 0;

        int i = 0;
        while (i < vehicles.size()){
            String currBranch = vehicles.get(i).getLocation();

            List<VehicleModel> vehiclesInBranch = new ArrayList<>();
            List<Double> pricesInBranch = new ArrayList<>();

            while (i < vehicles.size() && vehicles.get(i).getLocation().equals(currBranch)){
                vehiclesInBranch.add(vehicles.get(i));
                pricesInBranch.add(prices.get(i));
                totalVehicles ++;
                totalRevenue += prices.get(i);
                i ++;
            }

            DailyReturnReportBranch branchReport = new DailyReturnReportBranch(vehiclesInBranch, pricesInBranch, currBranch);
            branchReports.put(currBranch, branchReport);

        }
    }

}
