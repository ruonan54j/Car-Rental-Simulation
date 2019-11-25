package ca.ubc.cs304.model;

import java.util.*;

/**
 * Model for a container for vehicles and their return price
 */
public class DailyReturnReportBranch {

    public Map<String, Integer> numVehicles; //Number of vehicles per vehicle type
    public Map<String, Double> revenue; //Revenue per vehicle type
    public int totalBranchVehicles;
    public double totalBranchRevenue;
    public String branch;

    //We can assume lists are already sorted by vehicle type
    public DailyReturnReportBranch (List<VehicleModel> vehicles, List<Double> revs, String branch) {
        this.branch = branch;
        numVehicles = new HashMap<>();
        revenue = new HashMap<>();
        totalBranchVehicles = 0;
        totalBranchRevenue = 0;

        for (int i=0; i<vehicles.size(); i++){
            String vtype = vehicles.get(i).getVtname();
            Double rev = revs.get(i);

            Integer currNumVehicles = numVehicles.get(vtype);
            if (currNumVehicles == null)
                currNumVehicles = 0;
            numVehicles.put(vtype, currNumVehicles + 1);
            totalBranchVehicles ++;

            Double currRevenue = revenue.get(vtype);
            if (currRevenue == null)
               currRevenue = 0.0;
            revenue.put(vtype, currNumVehicles + rev);
            totalBranchRevenue += rev;
        }
    }

}


