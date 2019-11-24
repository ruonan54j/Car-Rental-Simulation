package ca.ubc.cs304.delegates;

import java.time.Instant;

import ca.ubc.cs304.model.CustomerModel;
import ca.ubc.cs304.model.ReservationReceipt;
import ca.ubc.cs304.model.ReturnReceipt;

public interface ClientInterfaceDelegate{
	public void home();
    public void getVehicles(String type, String location, String startTime, String endTime);
	public void getCustomerAccount(String license, String cellphone,String name, String address);    
	public ReservationReceipt createReservation(String vtname, String location, String dlicense, Instant startInstant,
			Instant endInstant, String valueOf, String valueOf2, Instant expInstant);
}
