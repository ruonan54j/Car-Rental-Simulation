package ca.ubc.cs304.ui;

import java.util.ArrayList;
import java.awt.event.*;
import java.rmi.registry.LocateRegistry;
import java.awt.*;
import javax.swing.*;
import javax.swing.text.MaskFormatter;

import ca.ubc.cs304.delegates.ClerkInterfaceDelegate;
import ca.ubc.cs304.delegates.ClientInterfaceDelegate;
import ca.ubc.cs304.model.CustomerModel;
import ca.ubc.cs304.model.RentalReceipt;
import ca.ubc.cs304.model.ReservationReceipt;
import ca.ubc.cs304.model.ReturnReceipt;
import ca.ubc.cs304.model.VehicleModel;

import javax.swing.JFormattedTextField;
import javax.swing.border.EmptyBorder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

/**
 * The class is only responsible for displaying and handling the login GUI. 
 */
public class ClerkInterface extends JFrame{

	private static final int TEXT_FIELD_WIDTH = 10;

	JTextField confField;
	JTextField location;
	JTextField cardName;
	JTextField cardNo;
	JTextField startTimestamp;
	JTextField endTimestamp;
	JTextField expDate;
	JTextField vtname;
	JTextField dlicense; 

	JTextField rid; 
	JTextField returnTimestamp;
	JTextField endOdometer;
	JTextField fullTank;


	// delegate
	private ClerkInterfaceDelegate delegate;
	JPanel contentPane;

    private MaskFormatter createFormatter(String s){
        MaskFormatter formatter = null;
        try {
            formatter = new MaskFormatter(s);
        } catch (java.text.ParseException exc) {
            System.err.println("formatter is bad: " + exc.getMessage());
            System.exit(-1);
        }
        return formatter;
	}
	


	public void showFrame(ClerkInterfaceDelegate delegate) {

		this.delegate = delegate;
		
		JButton rentButton = new JButton("Rent a Vehicle");
		JButton returnButton = new JButton("Return a Vehicle");
		JButton reportButton = new JButton("Generate Daily Report");
		contentPane = new JPanel();
		this.setContentPane(contentPane);
	
		contentPane.add(rentButton);
		contentPane.add(returnButton);
		contentPane.add(reportButton);
        
		// set Box Layout 
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));

		// register button with action event handler
		rentButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
                openRent();
			}
		 });
		 
		returnButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
                openReturn();
			}
         });

		reportButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
                openReport();
			}
         });
		this.pack();
		// center the frame
		Dimension d = this.getToolkit().getScreenSize();
		Rectangle r = this.getBounds();
		this.setLocation( (d.width - r.width)/2, (d.height - r.height)/2 );

		// make the window visible
		 this.setVisible(true);
	}
	
	public void openReport(){

		System.out.println("Report");
	}

	//open rent page
	public void openRent(){
		contentPane.removeAll();
		JPanel rowPane0 = new JPanel();
		rowPane0.setLayout(new FlowLayout(FlowLayout.CENTER));
	
		JButton homeButton = new JButton("Home");
		rowPane0.add(homeButton);
		contentPane.add(rowPane0);
		homeButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				contentPane.removeAll();
				dispose();
				delegate.home();
			}
		 });
		JButton withRes = new JButton("Have Reservation");
		JButton noRes = new JButton("No Reservation");
	
		withRes.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
                openWithRes();
			}
		 });
		noRes.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
                openNoRes();
			}
		 });
		contentPane.add(withRes);
		contentPane.add(noRes);
		this.pack();
		// center the frame
		Dimension d = this.getToolkit().getScreenSize();
		Rectangle r = this.getBounds();
		this.setLocation( (d.width - r.width)/2, (d.height - r.height)/2 );

		// make the window visible
		 this.setVisible(true);
	}
	//page: rent with reservation
	public void openWithRes(){
		contentPane.removeAll();
		JPanel rowPane0 = new JPanel();
		rowPane0.setLayout(new FlowLayout(FlowLayout.CENTER));
	
		JButton homeButton = new JButton("Home");
		rowPane0.add(homeButton);
		contentPane.add(rowPane0);
		homeButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				contentPane.removeAll();
				dispose();
				delegate.home();
			}
		 });
		JLabel confNoL = new JLabel("Enter confirmation number: ");
		confField = new JTextField(TEXT_FIELD_WIDTH);
		JButton rentBtn = new JButton("Rent");
		JPanel rowPane = new JPanel();
		rowPane.setLayout(new FlowLayout(FlowLayout.TRAILING));
		rowPane.add(confNoL);
		rowPane.add(confField);
		
		JPanel rowPane2 = new JPanel();
		rowPane2.setLayout(new FlowLayout(FlowLayout.CENTER));
		rowPane2.add(rentBtn);
		contentPane.add(rowPane);
		contentPane.add(rowPane2);
		//add action to btn
		rentBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				RentalReceipt receipt = delegate.createRentalWithRes(Integer.valueOf(confField.getText()), Instant.now());
				openReceipt(receipt);
			}
		 });

		 this.pack();
		// center the frame
		Dimension d = this.getToolkit().getScreenSize();
		Rectangle r = this.getBounds();
		this.setLocation( (d.width - r.width)/2, (d.height - r.height)/2 );

		// make the window visible
		 this.setVisible(true);
	}
	//page: rent without reservation
	public void openNoRes(){
		contentPane.removeAll();
		JPanel rowPane0 = new JPanel();
		rowPane0.setLayout(new FlowLayout(FlowLayout.CENTER));
	
		JButton homeButton = new JButton("Home");
		rowPane0.add(homeButton);
		contentPane.add(rowPane0);
		homeButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				contentPane.removeAll();
				dispose();
				delegate.home();
			}
		 });
		JLabel locationL = new JLabel("location: ");
		location = new JTextField(TEXT_FIELD_WIDTH);
		JLabel cardNameL = new JLabel("card name: ");
		cardName = new JTextField(TEXT_FIELD_WIDTH);
		JLabel cardNoL = new JLabel("card no: ");
		cardNo = new JTextField(TEXT_FIELD_WIDTH);
		JLabel expDateL = new JLabel("card expiration date: ");
		expDate = new JFormattedTextField(createFormatter("####-##-## ##:##:##"));
		JLabel vtnameL = new JLabel("vehicle type: ");
		vtname = new JTextField(TEXT_FIELD_WIDTH);
		JLabel dlicenseL = new JLabel("drivers license: ");
		dlicense = new JTextField(TEXT_FIELD_WIDTH);
		JLabel startTimestampL = new JLabel("start: ");
		startTimestamp =  new JFormattedTextField(createFormatter("####-##-## ##:##:##"));
		JLabel endTimestampL = new JLabel("end: ");
		endTimestamp =  new JFormattedTextField(createFormatter("####-##-## ##:##:##"));
		JButton rentBtn = new JButton("Rent");
		JPanel rowPane = new JPanel();
		JPanel rowPane1 = new JPanel();
		JPanel rowPane2 = new JPanel();
		JPanel rowPane3 = new JPanel();
		JPanel rowPane4 = new JPanel();
		JPanel rowPane5 = new JPanel();
		JPanel rowPane6 = new JPanel();
		JPanel rowPane7 = new JPanel();
		JPanel rowPane8 = new JPanel();
		rowPane.setLayout(new FlowLayout(FlowLayout.TRAILING));
		rowPane.add(locationL);
		rowPane.add(location);

		rowPane1.setLayout(new FlowLayout(FlowLayout.TRAILING));
		rowPane1.add(dlicenseL);
		rowPane1.add(dlicense);

		rowPane2.setLayout(new FlowLayout(FlowLayout.TRAILING));
		rowPane2.add(startTimestampL);
		rowPane2.add(startTimestamp);
		
		rowPane3.setLayout(new FlowLayout(FlowLayout.TRAILING));
		rowPane3.add(endTimestampL);
		rowPane3.add(endTimestamp);

		rowPane4.setLayout(new FlowLayout(FlowLayout.TRAILING));
		rowPane4.add(vtnameL);
		rowPane4.add(vtname);

		rowPane5.setLayout(new FlowLayout(FlowLayout.TRAILING));
		rowPane5.add(cardNoL);
		rowPane5.add(cardNo);

		rowPane6.setLayout(new FlowLayout(FlowLayout.TRAILING));
		rowPane6.add(cardNameL);
		rowPane6.add(cardName);
		
		rowPane7.setLayout(new FlowLayout(FlowLayout.TRAILING));
		rowPane7.add(expDateL);
		rowPane7.add(expDate);
		rowPane8.setLayout(new FlowLayout(FlowLayout.CENTER));
		rowPane8.add(rentBtn);

		contentPane.add(rowPane);
		contentPane.add(rowPane2);
		contentPane.add(rowPane3);
		contentPane.add(rowPane4);
		contentPane.add(rowPane5);
		contentPane.add(rowPane6);
		contentPane.add(rowPane7);
		contentPane.add(rowPane8);
		
		rentBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				contentPane.removeAll();
				Date dateStart=null;
				Date dateEnd=null;
				Date dateExp = null;
				Instant startInstant = null;
				Instant endInstant = null;
				Instant expInstant =  null;
				try {
					dateStart = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(String.valueOf(startTimestamp.getText()));
					dateEnd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(String.valueOf(endTimestamp.getText()));
					dateExp= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(String.valueOf(expDate.getText()));
					startInstant = dateStart.toInstant();
					endInstant = dateEnd.toInstant();
					expInstant = dateExp.toInstant();
					RentalReceipt receipt = delegate.createRentalNoRes(String.valueOf(location.getText()), Instant.now(), String.valueOf(cardName.getText()), String.valueOf(cardNo.getText()), expInstant, String.valueOf(vtname.getText()), String.valueOf(dlicense.getText()), startInstant, endInstant);
					openReceipt(receipt);
				} catch (ParseException err) {
				
				}
			}
		 });
		
		this.pack();
		// center the frame
		Dimension d = this.getToolkit().getScreenSize();
		Rectangle r = this.getBounds();
		this.setLocation( (d.width - r.width)/2, (d.height - r.height)/2 );

		// make the window visible
		 this.setVisible(true);
	}
	
	public void openReturn(){
		contentPane.removeAll();
		JPanel rowPane0 = new JPanel();
		rowPane0.setLayout(new FlowLayout(FlowLayout.CENTER));
	
		JButton homeButton = new JButton("Home");
		rowPane0.add(homeButton);
		contentPane.add(rowPane0);
		homeButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				contentPane.removeAll();
				dispose();
				delegate.home();
			}
		 });
		JLabel ridL = new JLabel("rental id: ");
		rid = new JTextField(TEXT_FIELD_WIDTH);
		JLabel retL = new JLabel("return time: ");
		returnTimestamp = new JFormattedTextField(createFormatter("####-##-## ##:##:##"));
		JLabel endOdometerL = new JLabel("end odometer: ");
		endOdometer = new JTextField(TEXT_FIELD_WIDTH);
		JLabel fullTankL = new JLabel("full tank: ");
		fullTank = new JTextField(TEXT_FIELD_WIDTH);
		JButton returnBtn = new JButton("Return Vehicle");

		JPanel rowPane = new JPanel();
		JPanel rowPane1 = new JPanel();
		JPanel rowPane2 = new JPanel();
		JPanel rowPane3 = new JPanel();
		JPanel rowPane4 = new JPanel();
		
		rowPane.setLayout(new FlowLayout(FlowLayout.TRAILING));
		rowPane.add(ridL);
		rowPane.add(rid);
		
		rowPane1.setLayout(new FlowLayout(FlowLayout.TRAILING));
		rowPane1.add(endOdometerL);
		rowPane1.add(endOdometer);

		rowPane2.setLayout(new FlowLayout(FlowLayout.TRAILING));
		rowPane2.add(fullTankL);
		rowPane2.add(fullTank);

		rowPane3.setLayout(new FlowLayout(FlowLayout.TRAILING));
		rowPane3.add(retL);
		rowPane3.add(returnTimestamp);
		
		rowPane4.setLayout(new FlowLayout(FlowLayout.CENTER));
		rowPane4.add(returnBtn);

		contentPane.add(rowPane);
		contentPane.add(rowPane2);
		contentPane.add(rowPane3);
		contentPane.add(rowPane4);



		returnBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				contentPane.removeAll();
	
				ReturnReceipt receipt = returnVehicle(Integer.valueOf(rid.getText()), Instant.now(),String.valueOf(endOdometer.getText()), Boolean.valueOf(fullTank.getText()));
				System.out.println(Boolean.valueOf(fullTank.getText()));
				openReturnReceipt(receipt);
				
			}
		 });
		
		this.pack();
		// center the frame
		Dimension d = this.getToolkit().getScreenSize();
		Rectangle r = this.getBounds();
		this.setLocation( (d.width - r.width)/2, (d.height - r.height)/2 );

		// make the window visible
		 this.setVisible(true);
	}

	protected ReturnReceipt returnVehicle(Integer valueOf, Instant returnInstant, String valueOf2, Boolean valueOf3) {
		return null;
	}

	protected ReturnReceipt returnVehicle(String valueOf, Instant returnInstant, String valueOf2, Boolean valueOf3) {
		return null;
	}

	public void openReturnReceipt(ReturnReceipt receipt){
		JPanel rowPane0 = new JPanel();
		rowPane0.setLayout(new FlowLayout(FlowLayout.CENTER));
	
		JButton homeButton = new JButton("Home");
		rowPane0.add(homeButton);
		contentPane.add(rowPane0);
		homeButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				contentPane.removeAll();
				dispose();
				delegate.home();
			}
		 });
		JLabel confno = new JLabel("confirmation number: "+receipt.getConfNo());
		JLabel hourlyRate = new JLabel("hourly rate:"+receipt.getHourlyRate());					
		JLabel hoursRented = new JLabel("hours rented: "+receipt.getHoursRented());
		JLabel hourlyTotal = new JLabel("hourly total: "+receipt.getHourlyTotal());
		JLabel kiloRate = new JLabel("kilo rate: "+receipt.getKiloRate());
		JLabel gasTotal = new JLabel("total gas: "+receipt.getGasTotal());
		JLabel distTotal = new JLabel("total distance: "+receipt.getDistTotal());
		JLabel finalTotal = new JLabel("Final cost: "+receipt.getFinalTotal());

		contentPane.add(confno);
		contentPane.add(hourlyRate);
		contentPane.add(kiloRate);
		contentPane.add(hoursRented);
		contentPane.add(hourlyTotal);
		contentPane.add(gasTotal);
		contentPane.add(distTotal);
		contentPane.add(finalTotal);
		this.pack();
		// center the frame
		Dimension d = this.getToolkit().getScreenSize();
		Rectangle r = this.getBounds();
		this.setLocation( (d.width - r.width)/2, (d.height - r.height)/2 );

		// make the window visible
		 this.setVisible(true);
	}

	public void openReceipt(RentalReceipt receipt) {
		JPanel rowPane0 = new JPanel();
		rowPane0.setLayout(new FlowLayout(FlowLayout.CENTER));
	
		JButton homeButton = new JButton("Home");
		rowPane0.add(homeButton);
		contentPane.add(rowPane0);
		homeButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				contentPane.removeAll();
				dispose();
				delegate.home();
			}
		 });
		JLabel rid = new JLabel("rental id: "+receipt.getRid());
		JLabel confno = new JLabel("confirmation number: "+receipt.getConfNo());
		JLabel location = new JLabel("location:"+receipt.getLocation());					
		JLabel vtype = new JLabel("end time: "+receipt.getVehicleType());
		JLabel start = new JLabel("start time: "+receipt.getStartTimestamp());
		JLabel license= new JLabel("license: "+receipt.getDlicense());
		JLabel odometer = new JLabel("odometer: "+receipt.getStartOdometer());

		contentPane.add(rid);
		contentPane.add(confno);
		contentPane.add(vtype);
		contentPane.add(location);
		contentPane.add(start);
		contentPane.add(license);
		contentPane.add(odometer);
		this.pack();
		// center the frame
		Dimension d = this.getToolkit().getScreenSize();
		Rectangle r = this.getBounds();
		this.setLocation( (d.width - r.width)/2, (d.height - r.height)/2 );

		// make the window visible
		 this.setVisible(true);

	}
}