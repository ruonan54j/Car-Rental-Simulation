package ca.ubc.cs304.ui;

import java.util.ArrayList;
import java.awt.event.*;
import java.rmi.registry.LocateRegistry;
import java.awt.*; 
import javax.swing.*; 
import javax.swing.text.MaskFormatter;
import ca.ubc.cs304.delegates.GenericInterfaceDelegate;
import ca.ubc.cs304.model.VehicleModel;

import javax.swing.JFormattedTextField;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * The class is only responsible for displaying and handling the login GUI. 
 */
public class GenericInterface extends JFrame{

	private static final int TEXT_FIELD_WIDTH = 10;
	// components of the login window
	private JTextField carType;
	private JTextField location;
	private JFormattedTextField formatDateStart;
	private JFormattedTextField formatDateEnd;
	// delegate
	private GenericInterfaceDelegate delegate;
	JPanel contentPane;

	public void showFrame(GenericInterfaceDelegate delegate) {

		this.delegate = delegate;

		JLabel chooseLabel = new JLabel("Choose User Type");
		
		JButton clientButton = new JButton("Client");
        JButton clerkButton = new JButton("Clerk");

		contentPane = new JPanel();
		this.setContentPane(contentPane);
		
		
		JPanel rowPane1 = new JPanel();
		rowPane1.setLayout(new FlowLayout(FlowLayout.CENTER));
        rowPane1.add(chooseLabel);
	
	
		JPanel rowPane2 = new JPanel();
		rowPane2.setLayout(new FlowLayout(FlowLayout.CENTER));
        rowPane2.add(clientButton);
		rowPane2.add(clerkButton);

		contentPane.add(rowPane1);
		contentPane.add(rowPane2);
        
		// set Box Layout 
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));

		// register button with action event handler
        clientButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
                openChoice(1);
			}
         });
         clerkButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
                openChoice(2);
			}
         });
         
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


	public void openChoice(int id){
        delegate.openChoice(id);
    }
}
