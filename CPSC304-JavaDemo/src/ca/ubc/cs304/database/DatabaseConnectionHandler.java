package ca.ubc.cs304.database;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.time.Instant;
import java.time.Duration;
import java.util.*;
import ca.ubc.cs304.model.BranchModel;
import ca.ubc.cs304.model.CustomerModel;
import ca.ubc.cs304.model.DailyRentalReport;
import ca.ubc.cs304.model.DailyRentalReportBranch;
import ca.ubc.cs304.model.DailyReturnReport;
import ca.ubc.cs304.model.DailyReturnReportBranch;
import ca.ubc.cs304.model.RentalModel;
import ca.ubc.cs304.model.RentalReceipt;
import ca.ubc.cs304.model.ReservationModel;
import ca.ubc.cs304.model.ReservationReceipt;
import ca.ubc.cs304.model.VehicleModel;
import ca.ubc.cs304.model.ReturnReceipt;

/**
 * This class handles all database related transactions
 */
public class DatabaseConnectionHandler {
	private static final String ORACLE_URL = "jdbc:oracle:thin:@localhost:1522:stu";
	private static final String EXCEPTION_TAG = "[EXCEPTION]";
	private static final String WARNING_TAG = "[WARNING]";
	
	private Connection connection = null;
	
	public DatabaseConnectionHandler() {
		try {
			// Load the Oracle JDBC driver
			// Note that the path could change for new drivers
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());

		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
		}
	}
	
	public void close() {
		try {
			if (connection != null) {
				connection.close();
			}
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
		}
	}


	/* OUR ADDED CODE HERE */

	/*
	Returns a list of vehicles matching query parameters
	Returns an empty list of no vehicles matching query was found
	*/
	public VehicleModel[] getVehicles(String vtname, String location, Instant startTimestamp, Instant endTimestamp){

		ArrayList<VehicleModel> result = new ArrayList<VehicleModel>();
		try {
			String query = "SELECT v.* FROM Vehicles v WHERE v.status = ?";
			if (vtname != null){
				query += " AND v.vtname = ?";
			}
			if (location != null){
				query += " AND v.location = ?";
			}

			if (startTimestamp != null && endTimestamp != null){ 
				query += " AND NOT EXISTS (SELECT * FROM Vehicles v2, reservations res WHERE v2.vid = v.vid AND res.vid = v2.vid AND res.startTimestamp < ? AND res.endTimestamp > ?)";
			}
			PreparedStatement ps = connection.prepareStatement(query);

			query += " AND NOT EXISTS (SELECT * FROM Vehicles v2, reservations res WHERE v2.vid = v.vid AND res.vid = v2.vid AND res.startTimestamp < ? AND res.endTimestamp > ?)";

			/* Insert queries dependent on which ones aren't null */
			int argInd = 1;
			ps.setString(argInd++, "Available");

			if (vtname != null)
				ps.setString(argInd++, vtname);
			if (location != null)
				ps.setString(argInd++, location);
			if (startTimestamp != null && endTimestamp != null){
				ps.setTimestamp(argInd++, Timestamp.from(endTimestamp));
				ps.setTimestamp(argInd++, Timestamp.from(startTimestamp));
			}

			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				VehicleModel model = new VehicleModel(rs.getInt("vid"),
													rs.getString("vlicense"),
													rs.getString("make"),
													rs.getInt("year"),
													rs.getString("color"),
													rs.getDouble("odometer"),
													rs.getString("status"),
													rs.getString("vtname"),
													rs.getString("location"));
				result.add(model);
			}
			rs.close();
			ps.close();
			return result.toArray(new VehicleModel[result.size()]);
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			rollbackConnection();
			return null;
		}
	}

	/*
	Creates a new customer account entry
	Returns false if the dlicense already exists
	*/
	public boolean createCustomerAccount(String dlicense, String cellphone, String name, String address){

		try {
			String update = "INSERT INTO Customers VALUES (?,?,?,?)";

			PreparedStatement ps = connection.prepareStatement(update);
			ps.setString(1, dlicense);
			ps.setString(2, cellphone);
			ps.setString(3, name);
			ps.setString(4, address);

			ps.executeUpdate();
			connection.commit();
			ps.close();
			return true;
		} catch (SQLException e) { //Invalid query or duplicate dlicense
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			rollbackConnection();
			return false;
		}
	}
	/*
	Creates a new reservation entry and return the confirmation id
	Returns null if no vehicles could be found in the specified location and time frame
	*/
	public ReservationReceipt createReservation(String vtname, String location, String dlicense, Instant startTimestamp, Instant endTimestamp, String cardName, String cardNo, Instant expDate){

		try {
			//Find a suitable vehicle
			VehicleModel[] matches = getVehicles(vtname, location, startTimestamp, endTimestamp);
			int vid;
			if (matches.length > 0){
				vid = matches[0].getVid();
			}
			else{ // No vehicles available for rental
				return null;
			}

			String update = "INSERT INTO Reservations (vid, dlicense, startTimestamp, endTimestamp, cardName, cardNo, expDate) VALUES (?,?,?,?,?,?,?)"; //confNo autogenerated
			PreparedStatement ps = connection.prepareStatement(update, new String[]{"confNo"});

			//Auto increment key should handle creating the confNo for us
			ps.setInt(1, vid);
			ps.setString(2, dlicense);
			ps.setTimestamp(3, Timestamp.from(startTimestamp));
			ps.setTimestamp(4, Timestamp.from(endTimestamp));
			ps.setString(5, cardName);
			ps.setString(6, cardNo);
			ps.setTimestamp(7, Timestamp.from(expDate));

			ps.executeUpdate(); //Returns 1
			connection.commit();

			ResultSet rs = ps.getGeneratedKeys();

			if (rs.next()){
				int confNo = rs.getInt(1);
				ps.close();
				rs.close();
				return new ReservationReceipt(confNo, vid, dlicense, startTimestamp, endTimestamp, cardName, cardNo, expDate, vtname, location);
			}
			else{
				ps.close();
				rs.close();
				return null;
			}
		} catch (SQLException e) { //Invalid query
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			rollbackConnection();
			return null;
		}
	}

	/*
	Creates a rental without a prior reservation
	If return value is null, no vehicles of the specified type were available at the location in the time frame
	"now" should be retrieved from Instant.now()
	*/
	public RentalReceipt createRentalNoRes(String location, Instant now, String cardName, String cardNo, Instant expDate, String vtname, String dlicense, Instant endTimestamp){

		ReservationReceipt res = createReservation(vtname, location, dlicense, now, endTimestamp, cardName, cardNo, expDate);
		if (res == null){
			return null;
		}
		return createRentalWithRes(res.getConfNo(), now);
	}

	/*
	Creates a new rental entry (requires a reservation beforehand)
	If return value is null, the confNo was invalid
	*/
	public RentalReceipt createRentalWithRes(int confNo, Instant now){

		try {
			// Confirm that reservation exists
			String query = "SELECT * FROM Reservations WHERE confNo = ?";
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setInt(1, confNo);
			ResultSet rs = ps.executeQuery();
			int vid;
			String dlicense;
			Instant endTimestamp;
			String cardName;
			String cardNo;
			Instant expDate;
			if (rs.next()){ //Reservation info
				vid = rs.getInt(2);
				dlicense = rs.getString(3);
				endTimestamp = rs.getTimestamp(5).toInstant();
				cardName = rs.getString(6);
				cardNo = rs.getString(7);
				expDate = rs.getTimestamp(8).toInstant();
			}
			else{ //Res does not exist
				return null;
			}
			ps.close();
			rs.close();

			//Sanity check that vehicle is still available and get odometer if it is
			query = "SELECT * FROM Vehicles WHERE vid = ?";
			ps = connection.prepareStatement(query);
			ps.setInt(1, vid);
			rs = ps.executeQuery();
			String vehicleType;
			String location;
			double startOdometer;
			if (rs.next()){ //Vehicle exists
				vehicleType = rs.getString(9);
				location = rs.getString(10);
				startOdometer = rs.getDouble(7);
			}
			else{ //Vehicle somehow deleted or is no longer available
				return null;
			}
			ps.close();
			rs.close();
			
			String update = "INSERT INTO Rentals (vid, confNo, startOdometer, beginTimestamp, returnTimestamp, endOdometer, fullTank, finalCost) VALUES (?,?,?,?,NULL,NULL,NULL,NULL)"; //last 4 NULLs represent return info

			//need vid, dlicense, start, end, odometer, cardname, cardno, expdate, confno
			ps = connection.prepareStatement(update, new String[]{"rid"});
			ps.setInt(1, vid);
			ps.setInt(2, confNo);
			ps.setDouble(3, startOdometer);
			ps.setTimestamp(4, Timestamp.from(now)); //Begin timestamp for actual rental start

			ps.executeUpdate();
			connection.commit();

			rs = ps.getGeneratedKeys();
			int rid;

			if (rs.next()){
				rid = rs.getInt(1); //Gets rental id
				ps.close();
				rs.close();
			}
			else{
				ps.close();
				rs.close();
				return null; //Insert failed
			}

			// Update vehicle entry with "Away" from "Available"
			update = "UPDATE Vehicles SET status = ? WHERE vid = ?";

			ps = connection.prepareStatement(update);
			ps.setString(1, "Away");
			ps.setInt(2, vid);

			ps.executeUpdate();
			connection.commit();
			ps.close();

			System.out.println("RENTAL SUCCESSFUL: starting: " + Timestamp.from(now) + " ON " + vid);
			return new RentalReceipt(rid, confNo, startOdometer, cardName, expDate, cardNo, dlicense, vid, vehicleType, location, now, endTimestamp);

		} catch (SQLException e) { //Invalid query
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			rollbackConnection();
			return null;
		}
	}

	/*
	Peforms a vehicle return.
	Returns a "receipt" of the cost breakdown
	Return timestamp should just be the current time from Instant.now()
	Return null if the rid given was invalid
	*/
	public ReturnReceipt returnVehicle(int rid, Instant returnTimestamp, double endOdometer, boolean fullTank){
		try {

			// Get original rental info
			String query = "SELECT rent.startOdometer, res.vid, res.startTimestamp, res.endTimestamp, res.confNo FROM Rentals rent, Reservations res WHERE rent.rid = ? AND rent.confNo = res.confNo";
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setInt(1, rid);

			ResultSet rs = ps.executeQuery();
			double startOdometer;
			int vid;
			Instant startTimestamp;
			Instant endTimestamp;
			int confNo;
			if (rs.next()){ //Rental info
				startOdometer = rs.getInt(1);
				vid = rs.getInt(2);
				startTimestamp = rs.getTimestamp(3).toInstant();
				endTimestamp = rs.getTimestamp(4).toInstant();
				confNo = rs.getInt(5);
			}
			else{ //Rental does not exist
				ps.close();
				rs.close();
				return null;
			}
			ps.close();
			rs.close();

			// Get rates for vehicle type
			query = "SELECT vt.hourlyRate, vt.kiloRate, vt.kiloLimitPerHour, vt.tankRefillFee FROM Vehicles v, VehicleTypes vt WHERE v.vid = ? AND v.vtname = vt.vtname";
			ps = connection.prepareStatement(query);
			ps.setInt(1, vid);

			rs = ps.executeQuery();
			double hourlyRate;
			double kiloRate; //Only applied if exceed allotted amount
			double kiloLimitPerHour;
			double tankRefillFee;
			if (rs.next()){ //Rental info
				hourlyRate = rs.getDouble(1);
				kiloRate = rs.getDouble(2); 
				kiloLimitPerHour = rs.getDouble(3);
				tankRefillFee = rs.getDouble(4);
			}
			else{ //Vehicle type does not exist (this should never happen)
				ps.close();
				rs.close();
				return null;
			}
			ps.close();
			rs.close();

			// Update rental entry with return info
			String update = "UPDATE Rentals SET returnTimestamp = ?, endOdometer = ?, fullTank = ?, finalCost = ? WHERE rid = ?";

			// Rate calculation
			int hoursElapsed = Math.max((int)Duration.between(startTimestamp, endTimestamp).toHours(), (int)Duration.between(startTimestamp, returnTimestamp).toHours()); //Customer must pay for at least the time reserved (not when they get the car)
			double kilosAllowed = hoursElapsed * kiloLimitPerHour;
			double kilosUsed = endOdometer - startOdometer; 
			double kilosOverAllowed = Math.max(0, kilosUsed - kilosAllowed);
			double hourlyTotal = hoursElapsed * hourlyRate;
			double distTotal = kilosOverAllowed * kiloRate;
			double gasTotal = fullTank ? 0 : tankRefillFee;
			double finalCost = hourlyTotal + distTotal + gasTotal;

			ps = connection.prepareStatement(update);
			ps.setTimestamp(1, Timestamp.from(returnTimestamp));
			ps.setDouble(2, endOdometer);
			ps.setBoolean(3, fullTank);
			ps.setDouble(4, finalCost);
			ps.setInt(5, rid);

			ps.executeUpdate();
			connection.commit();
			ps.close();

			// Update vehicle entry with "Available" from "Away" and with new odometer rating
			update = "UPDATE Vehicles SET status = ?, odometer = ? WHERE vid = ?";

			ps = connection.prepareStatement(update);
			ps.setString(1, "Available");
			ps.setDouble(2, endOdometer);
			ps.setInt(3, vid);

			ps.executeUpdate();
			connection.commit();
			ps.close();

			return new ReturnReceipt(confNo, rid, hourlyRate, hoursElapsed, hourlyTotal, kiloRate, kilosOverAllowed,distTotal, gasTotal, finalCost);
		} catch (SQLException e) { //Invalid query or duplicate dlicense
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			rollbackConnection();
			return null;
		}
	}

	/*
	Daily rentals (for the current day)
	Refer to "DailyRentalReport.java" for how to retrieve the desired values
	*/
	public DailyRentalReport getDailyRentals() {
		ArrayList<VehicleModel> vehicles = new ArrayList<VehicleModel>();

		try {
			String query = "SELECT v.* FROM Vehicles v, Rentals r WHERE TRUNC(r.beginTimestamp) = TRUNC(sysdate) AND r.vid = v.vid ORDER BY v.location, v.vtname";

			PreparedStatement ps = connection.prepareStatement(query);

			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				VehicleModel model = new VehicleModel(rs.getInt("vid"),
													rs.getString("vlicense"),
													rs.getString("make"),
													rs.getInt("year"),
													rs.getString("color"),
													rs.getDouble("odometer"),
													rs.getString("status"),
													rs.getString("vtname"),
													rs.getString("location"));
				vehicles.add(model);
			}
			rs.close();
			ps.close();
			System.out.println("DAILY RENTAL REPORT SIZE: " + vehicles.size());
			return new DailyRentalReport(vehicles);
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			rollbackConnection();
			return null;
		}
	}

	/*
	Daily rentals for a branch(for the current day)
	Refer to "DailyRentalReportBranch.java" for how to retrieve the desired values
	*/
	public DailyRentalReportBranch getDailyRentalsBranch(String branch) {
		ArrayList<VehicleModel> vehicles = new ArrayList<VehicleModel>();

		try {
			String query = "SELECT v.* FROM Vehicles v, Rentals r WHERE TRUNC(r.beginTimestamp) = TRUNC(sysdate) AND r.vid = v.vid AND v.location = ? ORDER BY v.location, v.vtname";

			PreparedStatement ps = connection.prepareStatement(query);
			ps.setString(1, branch);

			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				VehicleModel model = new VehicleModel(rs.getInt("vid"),
													rs.getString("vlicense"),
													rs.getString("make"),
													rs.getInt("year"),
													rs.getString("color"),
													rs.getDouble("odometer"),
													rs.getString("status"),
													rs.getString("vtname"),
													rs.getString("location"));
				vehicles.add(model);
			}
			rs.close();
			ps.close();
			return new DailyRentalReportBranch(vehicles, branch);
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			rollbackConnection();
			return null;
		}
	}

	/*
	Daily returns (for the current day)
	Refer to "DailyReturnReport.java" for how to retrieve the desired values
	*/
	public DailyReturnReport getDailyReturns() {
		ArrayList<VehicleModel> vehicles = new ArrayList<>();
		ArrayList<Double> prices = new ArrayList<>();

		try {
			String query = "SELECT v.*, r.finalCost FROM Vehicles v, Rentals r WHERE TRUNC(r.returnTimestamp) = TRUNC(sysdate) AND r.vid = v.vid ORDER BY v.location, v.vtname";

			PreparedStatement ps = connection.prepareStatement(query);

			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				VehicleModel vehicle = new VehicleModel(rs.getInt("vid"),
													rs.getString("vlicense"),
													rs.getString("make"),
													rs.getInt("year"),
													rs.getString("color"),
													rs.getDouble("odometer"),
													rs.getString("status"),
													rs.getString("vtname"),
													rs.getString("location"));
				double price = rs.getDouble("finalCost");
				vehicles.add(vehicle);
				prices.add(price);
			}
			rs.close();
			ps.close();
			return new DailyReturnReport(vehicles, prices);
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			rollbackConnection();
			return null;
		}
	}

	/*
	Daily returns for a branch (for the current day)
	Refer to "DailyReturnReportBranch.java" for how to retrieve the desired values
	*/
	public DailyReturnReportBranch getDailyReturnsBranch(String branch) {
		ArrayList<VehicleModel> vehicles = new ArrayList<>();
		ArrayList<Double> prices = new ArrayList<>();

		try {
			String query = "SELECT v.*, r.finalCost FROM Vehicles v, Rentals r WHERE TRUNC(r.returnTimestamp) = TRUNC(sysdate) AND r.vid = v.vid AND v.location = ? ORDER BY v.location, v.vtname";

			PreparedStatement ps = connection.prepareStatement(query);
			ps.setString(1, branch);

			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				VehicleModel vehicle = new VehicleModel(rs.getInt("vid"),
													rs.getString("vlicense"),
													rs.getString("make"),
													rs.getInt("year"),
													rs.getString("color"),
													rs.getDouble("odometer"),
													rs.getString("status"),
													rs.getString("vtname"),
													rs.getString("location"));
				double price = rs.getDouble("finalCost");
				vehicles.add(vehicle);
				prices.add(price);
			}
			rs.close();
			ps.close();
			return new DailyReturnReportBranch(vehicles, prices, branch);
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			rollbackConnection();
			return null;
		}
	}

	/* OUR ADDED CODE ENDS HERE */

	public void deleteBranch(int branchId) {
		try {
			PreparedStatement ps = connection.prepareStatement("DELETE FROM branch WHERE branch_id = ?");
			ps.setInt(1, branchId);
			
			int rowCount = ps.executeUpdate(); //ResultSet rs = stmt.executeQuery("SELECT * FROM branch");
			if (rowCount == 0) {
				System.out.println(WARNING_TAG + " Branch " + branchId + " does not exist!");
			}
			
			connection.commit();
	
			ps.close();
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			rollbackConnection();
		}
	}
	
	public void insertBranch(BranchModel model) {
		try {
			PreparedStatement ps = connection.prepareStatement("INSERT INTO branch VALUES (?,?,?,?,?)");
			ps.setInt(1, model.getId());
			ps.setString(2, model.getName());
			ps.setString(3, model.getAddress());
			ps.setString(4, model.getCity());
			if (model.getPhoneNumber() == 0) {
				ps.setNull(5, java.sql.Types.INTEGER);
			} else {
				ps.setInt(5, model.getPhoneNumber());
			}

			ps.executeUpdate();
			connection.commit();

			ps.close();
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			rollbackConnection();
		}
	}
	
	public BranchModel[] getBranchInfo() {
		ArrayList<BranchModel> result = new ArrayList<BranchModel>();
		
		try {
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM branch");
		
//    		// get info on ResultSet
//    		ResultSetMetaData rsmd = rs.getMetaData();
//
//    		System.out.println(" ");
//
//    		// display column names;
//    		for (int i = 0; i < rsmd.getColumnCount(); i++) {
//    			// get column name and print it
//    			System.out.printf("%-15s", rsmd.getColumnName(i + 1));
//    		}
			
			while(rs.next()) {
				BranchModel model = new BranchModel(rs.getString("branch_addr"),
													rs.getString("branch_city"),
													rs.getInt("branch_id"),
													rs.getString("branch_name"),
													rs.getInt("branch_phone"));
				result.add(model);
			}

			rs.close();
			stmt.close();
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
		}	
		
		return result.toArray(new BranchModel[result.size()]);
	}
	
	public void updateBranch(int id, String name) {
		try {
		  PreparedStatement ps = connection.prepareStatement("UPDATE branch SET branch_name = ? WHERE branch_id = ?");
		  ps.setString(1, name);
		  ps.setInt(2, id);
		
		  int rowCount = ps.executeUpdate();
		  if (rowCount == 0) {
		      System.out.println(WARNING_TAG + " Branch " + id + " does not exist!");
		  }
	
		  connection.commit();
		  
		  ps.close();
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			rollbackConnection();
		}	
	}
	
	public boolean login(String username, String password) {
		try {
			if (connection != null) {
				connection.close();
			}
	
			connection = DriverManager.getConnection(ORACLE_URL, username, password);
			connection.setAutoCommit(false);
	
			System.out.println("\nConnected to Oracle!");
			//File file=new File("/Users/ruonanjia/Desktop/school/304/CPSC304Project/CPSC304-JavaDemo/src/script.sql");
			URL path = ClassLoader.getSystemResource("script.sql");
			try{
				File file = new File(path.toURI());
				executeSqlScript(file);
			}catch(Exception e){
				return false;
			}
			return true;
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			return false;
		}
	}

	private void rollbackConnection() {
		try  {
			connection.rollback();	
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
		}
	}

	public void executeSqlScript(File inputFile) {

		// Delimiter
		String delimiter = ";";
	
		// Create scanner
		Scanner scanner;
		try {
			scanner = new Scanner(inputFile).useDelimiter(delimiter);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
			return;
		}
	
		// Loop through the SQL file statements 
		Statement currentStatement = null;
		while(scanner.hasNext()) {
	
			// Get statement 
			String rawStatement = scanner.next();
			if(rawStatement.trim().length() > 0){
			try {
				// Execute statement
				currentStatement = connection.createStatement();
				currentStatement.execute(rawStatement.trim());
				//commit in
				if(rawStatement.contains("insert")){
					connection.commit();
				}
			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println("err1");
			} finally {
				// Release resourcesß
				if (currentStatement != null) {
					try {
						currentStatement.close();
					} catch (SQLException e) {
						//e.printStackTrace();
						System.out.println("err2");
					}
				}
				currentStatement = null;
			}
		}
	}
	scanner.close();
	}
}
