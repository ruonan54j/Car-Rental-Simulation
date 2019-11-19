package ca.ubc.cs304.ui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import ca.ubc.cs304.delegates.UserInterfaceDelegate;

/**
 * The class is only responsible for displaying and handling the login GUI. 
 */
public class UserInterface extends JFrame implements ActionListener {

	private static final int TEXT_FIELD_WIDTH = 10;
	// components of the login window
	private JTextField carType;
	private JTextField location;
	private JTextField timeInterval;
	// delegate
	private UserInterfaceDelegate delegate;

	public void showFrame(UserInterfaceDelegate delegate) {
		this.delegate = delegate;
		

		JLabel typeLabel = new JLabel("Enter Vehicle Type: ");
		JLabel locationLabel = new JLabel("Enter Location: ");
		JLabel intervalLabel = new JLabel("Enter Time Interval: ");

		carType = new JTextField(TEXT_FIELD_WIDTH);
		location = new JTextField(TEXT_FIELD_WIDTH);
		timeInterval = new JTextField(TEXT_FIELD_WIDTH);

		JButton searchButton = new JButton("Search");

		JPanel contentPane = new JPanel();
		this.setContentPane(contentPane);

		// layout components using the GridBag layout manager
		GridBagLayout gb = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();

		contentPane.setLayout(gb);
		contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		// place the type label 
		c.gridwidth = GridBagConstraints.RELATIVE;
		c.insets = new Insets(10, 10, 5, 0);
		gb.setConstraints(typeLabel, c);
		contentPane.add(typeLabel);

		// place the text field for the type
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.insets = new Insets(10, 0, 5, 10);
		gb.setConstraints(carType, c);
		contentPane.add(carType);

		// place location label
		c.gridwidth = GridBagConstraints.RELATIVE;
		c.insets = new Insets(0, 10, 10, 0);
		gb.setConstraints(locationLabel, c);
		contentPane.add(locationLabel);

		// place the location field 
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.insets = new Insets(0, 0, 10, 10);
		gb.setConstraints(location, c);
		contentPane.add(location);

		// place interval label
		c.gridwidth = GridBagConstraints.RELATIVE;
		c.insets = new Insets(0, 10, 10, 0);
		gb.setConstraints(intervalLabel, c);
		contentPane.add(intervalLabel);

		// place the interval field 
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.insets = new Insets(0, 0, 10, 10);
		gb.setConstraints(timeInterval, c);
		contentPane.add(timeInterval);

		// place the search button
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.insets = new Insets(5, 10, 10, 10);
		c.anchor = GridBagConstraints.CENTER;
		gb.setConstraints(searchButton, c);
		contentPane.add(searchButton);

		// register login button with action event handler
		searchButton.addActionListener(this);

		// anonymous inner class for closing the window
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		// size the window to obtain a best fit for the components
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
	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println("here!");
		//delegate.login(carType.getText(), String.valueOf(location.getText()));
	}
	
}
