package ca.ubc.cs304.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.time.Instant;

import ca.ubc.cs304.model.BranchModel;

import ca.ubc.cs304.model.VehicleModel;


/**
 * This class handles all database related transactions
 */
public class DatabaseConnectionHandler {
	private static final String ORACLE_URL = "jdbc:oracle:thin:@dbhost.students.cs.ubc.ca:1522:stu";
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
	*/
	public VehicleModel[] getVehicles(String vtname, String location, Instant start, Instant end){

		ArrayList<VehicleModel> result = new ArrayList<VehicleModel>();

		try {
			String query = "SELECT v.* FROM vehicle v WHERE";
			boolean andFlag = false;
			if (vtname != null){
				if (andFlag){
					query += " AND";
				}
				query += " v.vtname = ?";
				andFlag = true;
			}
			if (location != null){
				if (andFlag){
					query += " AND";
				}
				query += " v.location = ?";
				andFlag = true;
			}

			if (start != null && end != null){ //TODO: This doesn't consider if vehicle is rented but never returned (need to use rent.returndate as well)
				if (andFlag){
					query += " AND";
				}
				query += " NOT EXISTS (SELECT * FROM vehicle v2, rental r WHERE v2.vid = v.vid AND r.vid = v2.vid AND r.start < ? AND rent.end > ?)";
			}
			PreparedStatement ps = connection.prepareStatement(query);

			/* Insert queries dependent on which ones aren't null */
			int argInd = 1;
			if (vtname != null)
				ps.setString(argInd++, vtname);
			if (location != null)
				ps.setString(argInd++, location);
			if (start != null && end != null){
				ps.setTimestamp(argInd++, Timestamp.from(end));
				ps.setTimestamp(argInd++, Timestamp.from(start));
			}

			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				VehicleModel model = new VehicleModel(rs.getInt("vid"),
													rs.getString("vlicense"),
													rs.getString("make"),
													rs.getInt("year"),
													rs.getString("color"),
													rs.getInt("odometer"),
													rs.getInt("status"),
													rs.getString("vtname"),
													rs.getString("location"));
				result.add(model);
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			rollbackConnection();
		}

		return result.toArray(new VehicleModel[result.size()]);
	}

	/*
	Creates a new customer account entry
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
	Returns -1 if error occured
	*/
	public int createReservation(String vtname, String dlicense, Instant start, Instant end){

		try {
			String update = "INSERT INTO Reservations VALUES (?,?,?,?)";

			PreparedStatement ps = connection.prepareStatement(update);
			//Auto increment key should handle creating the confNo for us
			ps.setString(1, vtname);
			ps.setString(2, dlicense);
			ps.setTimestamp(3, Timestamp.from(start));
			ps.setTimestamp(4, Timestamp.from(end));

			ps.executeUpdate();
			connection.commit();

			ResultSet rs = ps.getGeneratedKeys();
			ps.close();

			if (rs.next()){
				int confNo = rs.getInt(1); //Gets "key"
				rs.close();
				return confNo;
			}
			else{
				rs.close();
				return -1;
			}
		} catch (SQLException e) { //Invalid query OR invalid dlicense
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			rollbackConnection();
			return -1;
		}
	}

	public int createRentalNoRes(String location, String cardName, String cardNo, Instant expDate, String vtname, String dlicense, Instant start, Instant end){

		int confNo = createReservation(vtname, dlicense, start, end);
		if (confNo == -1){
			return -1;
		}
		return createRentalWithRes(confNo, location, cardName, cardNo, expDate);
	}

	/*
	Creates a new rental entry (requires a reservation beforehand)
	*/
	public int createRentalWithRes(int confNo, String location, String cardName, String cardNo, Instant expDate){

		try {
			// Confirm that reservation exists
			String query = "SELECT * FROM Reservations WHERE confNo = ?";
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setInt(1, confNo);
			ResultSet rs = ps.executeQuery();
			String vtname;
			String dlicense;
			Instant start;
			Instant end;
			if (rs.next()){ //Reservation info
				vtname = rs.getString(2);
				dlicense = rs.getString(3);
				start = rs.getTimestamp(4).toInstant();
				end = rs.getTimestamp(5).toInstant();
			}
			else{ //Res does not exist
				return -1;
			}
			ps.close();

			//Find a suitable vehicle

			VehicleModel[] matches = getVehicles(vtname, location, start, end);
			int vid;
			int odometer;
			if (matches.length > 0){
				vid = matches[0].getVid();
				odometer = matches[0].getOdometer();
			}
			else{ // No vehicles available for rental
				return -1;
			}

			String update = "INSERT INTO Rentals VALUES (?,?,?,?,?,?,?,?,?,NULL,NULL,NULL,NULL)";
			//last 4 NULLs represent return info
			
			//need vid, dlicense, start, end, odometer, cardname, cardno, expdate, confno
			ps = connection.prepareStatement(update);
			ps.setInt(1, vid);
			ps.setString(2, dlicense);
			ps.setTimestamp(3, Timestamp.from(start));
			ps.setTimestamp(4, Timestamp.from(end));
			ps.setInt(5, odometer);
			ps.setString(6, cardName);
			ps.setString(7, cardNo);
			ps.setTimestamp(8, Timestamp.from(expDate));
			ps.setInt(9, confNo);

			ps.executeUpdate();
			connection.commit();

			rs = ps.getGeneratedKeys();
			ps.close();

			if (rs.next()){
				int rid = rs.getInt(1); //Gets rental id
				rs.close();
				return rid;
			}
			else{
				rs.close();
				return -1;
			}
		} catch (SQLException e) { //Invalid query
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			rollbackConnection();
			return -1;
		}
	}

	public int returnVehicle(String rid, Instant retTime, int odometer, boolean fulltank){
		try {
			String update = "UPDATE Rentals WHERE <rid>";

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
}
