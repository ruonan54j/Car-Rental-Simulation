package ca.ubc.cs304.model;

import java.util.*;

/**
 * Model for a container for vehicles and their return price
 */
public class DailyRentalReportBranch {

    public Map<String, Integer> numVehicles; //Number of vehicles per vehicle type
    public int totalBranchVehicles;
    public String branch;

    //We can assume lists are already sorted by vehicle type
    public DailyRentalReportBranch (final List<VehicleModel> vehicles, String branch) {
        this.branch = branch;
        numVehicles = new HashMap<>();
        totalBranchVehicles = 0;

        for (int i=0; i<vehicles.size(); i++){
            final String vtype = vehicles.get(i).getVtname();

            Integer currNumVehicles = numVehicles.get(vtype);
            if (currNumVehicles == null)
                currNumVehicles = 0;
            numVehicles.put(vtype, currNumVehicles + 1);
            totalBranchVehicles ++;
        }
    }

}


