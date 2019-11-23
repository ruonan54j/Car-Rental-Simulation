package ca.ubc.cs304.controller;

import ca.ubc.cs304.database.DatabaseConnectionHandler;
import ca.ubc.cs304.delegates.LoginWindowDelegate;
import ca.ubc.cs304.delegates.ClientInterfaceDelegate;
import ca.ubc.cs304.delegates.GenericInterfaceDelegate;
import ca.ubc.cs304.ui.ClientInterface;
import ca.ubc.cs304.ui.GenericInterface;
import ca.ubc.cs304.ui.LoginWindow;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import ca.ubc.cs304.model.VehicleModel;
import ca.ubc.cs304.model.ReservationReceipt;
import ca.ubc.cs304.model.ReturnReceipt;
/**
 * This is the main controller class that will orchestrate everything.
 */
public class SuperRent implements LoginWindowDelegate, ClientInterfaceDelegate,GenericInterfaceDelegate {
	private DatabaseConnectionHandler dbHandler = null;
	private LoginWindow loginWindow = null;
	private ClientInterface clientInterface = null;
	private GenericInterface genericInterface = null;
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
			genericInterface = new GenericInterface();
			genericInterface.showFrame(this);

		} else {
			loginWindow.handleLoginFailed();

			if (loginWindow.hasReachedMaxLoginAttempts()) {
				loginWindow.dispose();
				System.out.println("You have exceeded your number of allowed attempts");
				System.exit(-1);
			}
		}
	}

	public void openChoice(int id){
		if(id==1){
			clientInterface = new ClientInterface();
			clientInterface.showFrame(this);
		}
		if(id==2){

		}
	}

	public void getVehicles(String type, String location, String startTime, String endTime) {
		Date dateStart=null;
		Date dateEnd=null;
		VehicleModel[] vehicles;
		String typeParse = null;
		String locationParse = null;
		Instant startInstant = null;
		Instant endInstant = null;
		if(location.trim().length() != 0){
			locationParse = location;
		}
		if(type.trim().length() != 0){
			typeParse = type;
		}
		try {
			dateStart = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(startTime);
			dateEnd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(endTime);	
			startInstant = dateStart.toInstant();
			endInstant = dateEnd.toInstant();

			
		} catch (ParseException e) {
		
		}
		vehicles = dbHandler.getVehicles(type, location, startInstant, endInstant);
		clientInterface.addVehicle(vehicles, vehicles.length);
		return;
		
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
