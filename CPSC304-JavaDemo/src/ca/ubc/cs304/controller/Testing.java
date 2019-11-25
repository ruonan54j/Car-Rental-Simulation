package ca.ubc.cs304.controller;

import ca.ubc.cs304.database.DatabaseConnectionHandler;
import ca.ubc.cs304.delegates.LoginWindowDelegate;
import ca.ubc.cs304.delegates.ClientInterfaceDelegate;
import ca.ubc.cs304.ui.ClientInterface;
import ca.ubc.cs304.ui.LoginWindow;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import ca.ubc.cs304.model.VehicleModel;
import ca.ubc.cs304.model.DailyRentalReport;
import ca.ubc.cs304.model.DailyRentalReportBranch;
import ca.ubc.cs304.model.DailyReturnReport;
import ca.ubc.cs304.model.DailyReturnReportBranch;
import ca.ubc.cs304.model.RentalReceipt;
import ca.ubc.cs304.model.ReservationReceipt;
import ca.ubc.cs304.model.ReturnReceipt;
/**
 * This is the main controller class that will orchestrate everything.
 */
public class Testing implements LoginWindowDelegate {
	public DatabaseConnectionHandler dbHandler = null;
	// private ClerkInterface clerkInterface = null;
	public Testing() {
		dbHandler = new DatabaseConnectionHandler();
	}

	private void start() {
		login("ora_wgu","a31875164");
	}

	/**
	 * LoginWindowDelegate Implementation
	 * 
	 * connects to Oracle database with supplied username and password
	 */
	public void login(String username, String password) {
		boolean didConnect = dbHandler.login(username, password);
	}

	
	//main method
	public static void main(String args[]) throws Exception {
		Testing t = new Testing();
        t.start();
        
        String vtname = "Truck";
        String location = "Vancouver";
        Instant startTimestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2019-11-20 02:00:00").toInstant();
        Instant endTimestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2019-11-21 02:00:00").toInstant();
		VehicleModel[] vehicles = t.dbHandler.getVehicles(vtname, location, startTimestamp, endTimestamp);
		System.out.println(vehicles.length);

		String dlicense = "ABCDE";
		String cellphone = "123-456-7890";
		String name = "John Smith";
		String address = "123 Cedar Street";
		boolean createAccountSuccess = t.dbHandler.createCustomerAccount(dlicense, cellphone, name, address);
		System.out.println(createAccountSuccess);

		String vtnameRes = "Truck";
		String locationRes = "Vancouver";
		// Same dlicense
		Instant startRes = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2019-11-20 02:00:00").toInstant();
		Instant endRes = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2019-11-24 06:00:00").toInstant();
		String cardName = "Visa";
		String cardNo = "1234";
		Instant expDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2025-11-24 00:00:00").toInstant();
		ReservationReceipt tt = t.dbHandler.createReservation(vtnameRes, locationRes, dlicense, startRes, endRes, cardName, cardNo, expDate); //Insertion works, but result NOT returned
		System.out.println(tt.getConfNo());
		System.out.println(tt.getVid());

		RentalReceipt rentReceipt = t.dbHandler.createRentalWithRes(tt.getConfNo(), Instant.now()); //Confirming that confirmation works, but no results returned...
		System.out.println("Rent receipt 1 rid: " + rentReceipt.getRid());
		System.out.println("Rent receipt 1 conf: " + rentReceipt.getConfNo());
		System.out.println("Rent receipt 1 vid: " + rentReceipt.getVid());
		System.out.println("Rent receipt 1 odometer: " + rentReceipt.getStartOdometer());
		System.out.println("Rent receipt 1 endtime: " + rentReceipt.getEndTimestamp());

		RentalReceipt rentReceipt2 = t.dbHandler.createRentalNoRes(location, Instant.now(), cardName, cardNo, expDate, vtname, dlicense, endTimestamp); //Try for same location and vehicle type
		System.out.println("Rent receipt 2 vid: " + rentReceipt2.getVid());

		// Test no available cars for rental
		RentalReceipt rentReceipt3 = t.dbHandler.createRentalNoRes("Burnaby", Instant.now(), cardName, cardNo, expDate, vtname, dlicense, endTimestamp); //Try when no vehicles are available at the location
		System.out.println("Rent receipt 3 null?: " + (rentReceipt3 == null));

		// Test return..
		ReturnReceipt returnReceipt = t.dbHandler.returnVehicle(1, Instant.now(),  500, true);
		System.out.println("Return conf noggggg: " + returnReceipt.getConfNo());
		System.out.println("Return distance total: " + returnReceipt.getDistTotal());
		System.out.println("Return hourly total: " + returnReceipt.getHourlyTotal());
		System.out.println("Return final total: " + returnReceipt.getFinalTotal());

		// Test non existant return
		ReturnReceipt returnReceipt2 = t.dbHandler.returnVehicle(52, Instant.now(), 600, true);
		System.out.println("Return receipt 2 null ? " + (returnReceipt2 == null));

		DailyRentalReport dailyRentals = t.dbHandler.getDailyRentals();
		System.out.println(dailyRentals.totalVehicles);

		DailyRentalReportBranch dailyRentalsBranch = t.dbHandler.getDailyRentalsBranch("Vancouver");
		System.out.println(dailyRentalsBranch.totalBranchVehicles);

		DailyRentalReportBranch dailyRentalsBranch2 = t.dbHandler.getDailyRentalsBranch("Richmond");
		System.out.println(dailyRentalsBranch2.totalBranchVehicles);

		DailyReturnReport dailyReturns = t.dbHandler.getDailyReturns();
		System.out.println(dailyReturns.totalVehicles);
		System.out.println(dailyReturns.totalRevenue);

		DailyReturnReportBranch dailyReturnsBranch = t.dbHandler.getDailyReturnsBranch("Vancouver");
		System.out.println(dailyReturnsBranch.totalBranchVehicles);
		System.out.println(dailyReturnsBranch.totalBranchRevenue);

		DailyReturnReportBranch dailyReturnsBranch2 = t.dbHandler.getDailyReturnsBranch("Richmond");
		System.out.println(dailyReturnsBranch2 == null);

	}
}
