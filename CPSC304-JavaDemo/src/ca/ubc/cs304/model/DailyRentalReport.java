package ca.ubc.cs304.model;

import java.util.*;

/**
 * Model for a container for vehicles and their return price
 */
public class DailyRentalReport {

    // Fields for defining "rental"
    public Map<String, DailyRentalReportBranch> branchReports; //Branch name to each report
    public int totalVehicles;

    //We can assume lists are already sorted by branch, and then vehicle type
    public DailyRentalReport (List<VehicleModel> vehicles) {
        this.branchReports = new HashMap<>();
        totalVehicles = 0;

        int i = 0;
        while (i < vehicles.size()){
            String currBranch = vehicles.get(i).getLocation();

            List<VehicleModel> vehiclesInBranch = new ArrayList<>();

            while (i < vehicles.size() && vehicles.get(i).getLocation().equals(currBranch)){
                vehiclesInBranch.add(vehicles.get(i));
                totalVehicles ++;
                i ++;
            }

            DailyRentalReportBranch branchReport = new DailyRentalReportBranch(vehiclesInBranch, currBranch);
            branchReports.put(currBranch, branchReport);

        }
    }

}
