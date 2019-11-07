package ca.ubc.cs304.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.time.LocalDateTime;

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
	public VehicleModel[] getVehicles(String vtname, String location, String city, 
			LocalDateTime start, LocalDateTime end){

		ArrayList<VehicleModel> result = new ArrayList<VehicleModel>();

		try {
			// TODO: start, end must be queried from rental and reservation tables
			// NOTE: currently, reservation table does not contain any connection to vehicle, only rental -> do we need to check reservation table then?
			/*
			PreparedStatement ps = connection.prepareStatement("SELECT v.* FROM vehicle v WHERE v.vtname = ? AND v.location = ? AND v.city = ? AND v.vid " + 
												"AND v.vid NOT IN (SELECT v.* FROM vehicle v, rental rent, reservation res WHERE " +
																	"rent.vid = v.vid AND rent.start >= ? AND rent.end <= ?" +
																	"res.confNo = rent.confNo AND res.start >= ? AND res.end <= ?)"); */

			String query = "SELECt v.* FROM vehicle v WHERE";
			boolean andFlag = false;
			if (vtname != null){
				if (andFlag){
					query += " AND v.vtname = ?";
				}
				else{
					query += " v.vtname = ?";
				}
				andFlag = true;
			}
			if (location != null){
				if (andFlag){
					query += " AND v.location = ?";
				}
				else{
					query += " v.location = ?";
				}
				andFlag = true;
			}
			if (city != null){
				if (andFlag){
					query += " AND v.city = ?";
				}
				else{
					query += " v.city = ?";
				}
				andFlag = true;
			}

			if (start != null && end != null){
				if (andFlag){
					query += " AND v.vid NOT IN (SELECT v.* FROM vehicle v, rental rent, reservation res WHERE rent.vid = v.vid AND rent.start < ? AND rent.end > ?)";
				}
				else{
					query += " v.vid NOT IN (SELECT v.* FROM vehicle v, rental rent, reservation res WHERE rent.vid = v.vid AND rent.start < ? AND rent.end > ?)";
				}
			}
			PreparedStatement ps = connection.prepareStatement(query);

			/*Need a way to insert optional queries, this needs testing */
			int argInd = 1;
			if (vtname != null)
				ps.setString(argInd++, vtname);
			if (location != null)
				ps.setString(argInd++, location);
			if (city != null)
				ps.setString(argInd++, city);
			if (start != null && end != null){
				ps.setString(argInd++, end.toString());
				ps.setString(argInd++, start.toString());
			}

			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				VehicleModel model = new VehicleModel(rs.getString("vid"),
													rs.getString("vlicense"),
													rs.getString("make"),
													rs.getInt("year"),
													rs.getString("color"),
													rs.getInt("odometer"),
													rs.getInt("status"),
													rs.getString("vtname"),
													rs.getString("location"),
													rs.getString("city"));
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
