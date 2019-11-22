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
public class SuperRent implements LoginWindowDelegate, ClientInterfaceDelegate {
	private DatabaseConnectionHandler dbHandler = null;
	private LoginWindow loginWindow = null;
	private ClientInterface clientInterface = null;
	// private ClerkInterface clerkInterface = null;
	public SuperRent() {
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

		if (didConnect) {
			// Once connected, remove login window and start text transaction flow
			clientInterface = new ClientInterface();
			clientInterface.showFrame(this);

		} else {
			loginWindow.handleLoginFailed();

			if (loginWindow.hasReachedMaxLoginAttempts()) {
				loginWindow.dispose();
				System.out.println("You have exceeded your number of allowed attempts");
				System.exit(-1);
			}
		}
	}

	public void getVehicles(String type, String location, String startTime, String endTime) {
		Date dateStart;
		Date dateEnd;
		VehicleModel[] vehicles;
		try {
			dateStart = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(startTime);
			dateEnd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(endTime);	
			Instant startInstant = dateStart.toInstant();
			Instant endInstant = dateEnd.toInstant();

			

			vehicles = dbHandler.getVehicles(type, location, startInstant, endInstant);
			//System.out.println(vehicles[0]);
		} catch (ParseException e) {
		}
		
		//test
		VehicleModel v = new VehicleModel(1, "123", "A", 1998, "blue",12.4, "Available", "honda", "Vancouver");			
		VehicleModel v2 = new VehicleModel(2, "122", "B", 1998, "red",123.4, "Available", "toyota", "Vancouver");
		ArrayList<VehicleModel> vlist  = new ArrayList<VehicleModel>();
		vlist.add(v);
		vlist.add(v2);
		clientInterface.addVehicle(vlist, 1);
		//end test
		
	}
	
	public void makeReservation(String vtname, String location, String dlicense, String endTime,Instant startTimestamp, Instant endTimestamp, String cardName, String cardNo, Instant expDate) {
		//try creating res
		ReservationReceipt receipt = dbHandler.createReservation(vtname, location, dlicense, startTimestamp, endTimestamp, cardName, cardNo, expDate);
		if (receipt != null){
			//call gui to display confirmation
		} else{
			//show error
		}
	}
	
	
	//main method
	public static void main(String args[]) {
		SuperRent superRent = new SuperRent();
		superRent.start();
	}
}
