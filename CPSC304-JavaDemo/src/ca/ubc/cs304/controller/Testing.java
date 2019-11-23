package ca.ubc.cs304.controller;

import ca.ubc.cs304.database.DatabaseConnectionHandler;
import ca.ubc.cs304.delegates.LoginWindowDelegate;
import ca.ubc.cs304.delegates.ClientInterfaceDelegate;
import ca.ubc.cs304.ui.ClientInterface;
import ca.ubc.cs304.ui.LoginWindow;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import ca.ubc.cs304.model.VehicleModel;
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
		login("ora_rjia","a33550161");
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
	public static void main(String args[]) {
		Testing t = new Testing();
        t.start();
        
        String vtname = null;
        String location = null;
        Instant startTimestamp = null;
        Instant endTimestamp = null;
		VehicleModel[] vehicles = t.dbHandler.getVehicles(vtname, location, startTimestamp, endTimestamp);
		System.out.println(vehicles.length);
	}
}
