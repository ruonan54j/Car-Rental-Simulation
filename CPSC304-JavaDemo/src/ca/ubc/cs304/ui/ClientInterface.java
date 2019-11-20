package ca.ubc.cs304.ui;

import java.util.ArrayList;
import java.awt.event.*;
import java.rmi.registry.LocateRegistry;
import java.awt.*; 
import javax.swing.*; 
import javax.swing.text.MaskFormatter;
import ca.ubc.cs304.delegates.ClientInterfaceDelegate;
import ca.ubc.cs304.model.VehicleModel;

import javax.swing.JFormattedTextField;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * The class is only responsible for displaying and handling the login GUI. 
 */
public class ClientInterface extends JFrame implements ActionListener {

	private static final int TEXT_FIELD_WIDTH = 10;
	// components of the login window
	private JTextField carType;
	private JTextField location;
	private JFormattedTextField formatDateStart;
	private JFormattedTextField formatDateEnd;
	// delegate
	private ClientInterfaceDelegate delegate;
	JPanel contentPane;

    private MaskFormatter createFormatter(String s) {
        MaskFormatter formatter = null;
        try {
            formatter = new MaskFormatter(s);
        } catch (java.text.ParseException exc) {
            System.err.println("formatter is bad: " + exc.getMessage());
            System.exit(-1);
        }
        return formatter;
	}
	


	public void showFrame(ClientInterfaceDelegate delegate) {
        formatDateStart = new JFormattedTextField(createFormatter("####-##-## ##:##:##"));
		formatDateEnd = new JFormattedTextField(createFormatter("####-##-## ##:##:##"));

		this.delegate = delegate;

		JLabel typeLabel = new JLabel("Enter Vehicle Type: ");
		JLabel locationLabel = new JLabel("Enter Location: ");
		JLabel dateLabelStart = new JLabel("Enter Start Date and Time: ");
		JLabel dateLabelEnd = new JLabel("Enter End Date and Time: ");

		carType = new JTextField(TEXT_FIELD_WIDTH);
		location = new JTextField(TEXT_FIELD_WIDTH);
		
		JButton searchButton = new JButton("Search");

		contentPane = new JPanel();
		this.setContentPane(contentPane);
		
		
		JPanel rowPane1 = new JPanel();
		rowPane1.setLayout(new FlowLayout(FlowLayout.TRAILING));
        rowPane1.add(typeLabel);
		rowPane1.add(carType);
	
		JPanel rowPane2 = new JPanel();
		rowPane2.setLayout(new FlowLayout(FlowLayout.TRAILING));
        rowPane2.add(locationLabel);
		rowPane2.add(location);
	
		JPanel rowPane3 = new JPanel();
		rowPane3.setLayout(new FlowLayout(FlowLayout.TRAILING));
        rowPane3.add(dateLabelStart);
		rowPane3.add(formatDateStart);

		JPanel rowPane4 = new JPanel();
		rowPane4.setLayout(new FlowLayout(FlowLayout.TRAILING));
        rowPane4.add(dateLabelEnd);
		rowPane4.add(formatDateEnd);
		
		contentPane.add(rowPane1);
		contentPane.add(rowPane2);
		contentPane.add(rowPane3);
		contentPane.add(rowPane4);

		contentPane.add(searchButton);
		// set Box Layout 
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));

		// register search button with action event handler
		searchButton.addActionListener(this);

		// anonymous inner class for closing the window
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		this.pack();
		// center the frame
		Dimension d = this.getToolkit().getScreenSize();
		Rectangle r = this.getBounds();
		this.setLocation( (d.width - r.width)/2, (d.height - r.height)/2 );

		// make the window visible
		 this.setVisible(true);

		// place the cursor in the text field for the username
		carType.requestFocus();
	}

	public void addVehicle(ArrayList<VehicleModel> vlist, int count){
		// layout components using the GridBag layout manager
		GridBagLayout gb = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		JLabel countL = new JLabel("total available: "+count);
		JButton moreButton = new JButton("View Details");
		// register search button with action event handler
		moreButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				 viewDetails(vlist,c,gb);
			}
		 });

		// place interval label
		c.gridwidth = GridBagConstraints.RELATIVE;
		c.insets = new Insets(0, 10, 10, 0);
		gb.setConstraints(countL, c);
		contentPane.add(countL);
		

		// place the start field 
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.insets = new Insets(0, 0, 10, 10);
		gb.setConstraints(moreButton, c);
		contentPane.add(moreButton);


		this.pack();

		// center the frame
		Dimension d = this.getToolkit().getScreenSize();
		Rectangle r = this.getBounds();
		this.setLocation( (d.width - r.width)/2, (d.height - r.height)/2 );

		// make the window visible
		 this.setVisible(true);
		
	
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		delegate.getVehicles(String.valueOf(carType.getText()), String.valueOf(location.getText()),String.valueOf(formatDateStart.getText()),String.valueOf(formatDateEnd.getText()));
	}

	//show details of car
	public void viewDetails(ArrayList<VehicleModel> vlist,GridBagConstraints c, GridBagLayout gb ) {
		
		for(VehicleModel v : vlist){
			JPanel rowPane = new JPanel();
			rowPane.setLayout(new FlowLayout(FlowLayout.LEADING));
			JPanel rowPane2 = new JPanel();
			rowPane2.setLayout(new FlowLayout(FlowLayout.LEADING));
			System.out.println(v.getColor());
			JLabel makeL = new JLabel("make: "+v.getMake());
			JLabel vlicenseL = new JLabel("license: "+v.getVlicense());
			JLabel yearL = new JLabel("year: "+v.getYear());
			JLabel colorL = new JLabel("color: "+v.getColor());
			JLabel odometerL = new JLabel("odometer: "+v.getOdometer());
			JLabel statusL = new JLabel("status: "+v.getStatus());
			JLabel vtnameL = new JLabel("type: "+v.getVtname());
			JLabel locationL = new JLabel("location: "+v.getLocation());
			
		
			rowPane.add(makeL);
			rowPane.add(vlicenseL);
			rowPane.add(yearL);
			rowPane.add(colorL);
			rowPane2.add(odometerL);
			rowPane2.add(statusL);
			rowPane2.add(vtnameL);
			rowPane2.add(locationL);
			contentPane.add(rowPane);
			contentPane.add(rowPane2);
		}
		this.pack();

		Dimension d = this.getToolkit().getScreenSize();
		Rectangle r = this.getBounds();
		this.setLocation( (d.width - r.width)/2, (d.height - r.height)/2 );

		// make the window visible
		 this.setVisible(true);
	}
	
}
