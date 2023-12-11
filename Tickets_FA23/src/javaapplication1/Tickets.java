package javaapplication1;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;

@SuppressWarnings("serial")
public class Tickets extends JFrame implements ActionListener {

	// class level member objects
	Dao dao = new Dao(); // for CRUD operations
	Boolean chkIfAdmin = null;

	// Main menu object items
	private JMenu mnuFile = new JMenu("File");
	private JMenu mnuAdmin = new JMenu("Admin");
	private JMenu mnuTickets = new JMenu("Tickets");

	// Sub menu item objects for all Main menu item objects
	JMenuItem mnuItemExit;
	JMenuItem mnuItemUpdate;
	JMenuItem mnuItemDelete;
	JMenuItem mnuItemOpenTicket;
	JMenuItem mnuItemViewTicket;

	public Tickets(Boolean isAdmin) {

		chkIfAdmin = isAdmin;
		createMenu();
		prepareGUI();

	}

	private void createMenu() {

		/* Initialize sub menu items **************************************/

		// initialize sub menu item for File main menu
		mnuItemExit = new JMenuItem("Exit");
		// add to File main menu item
		mnuFile.add(mnuItemExit);

		// initialize first sub menu items for Admin main menu
		mnuItemUpdate = new JMenuItem("Update Ticket");
		// add to Admin main menu item
		mnuAdmin.add(mnuItemUpdate);

		// initialize second sub menu items for Admin main menu
		mnuItemDelete = new JMenuItem("Delete Ticket");
		// add to Admin main menu item
		mnuAdmin.add(mnuItemDelete);

		// initialize first sub menu item for Tickets main menu
		mnuItemOpenTicket = new JMenuItem("Open Ticket");
		// add to Ticket Main menu item
		mnuTickets.add(mnuItemOpenTicket);

		// initialize second sub menu item for Tickets main menu
		mnuItemViewTicket = new JMenuItem("View Ticket");
		// add to Ticket Main menu item
		mnuTickets.add(mnuItemViewTicket);

		// initialize any more desired sub menu items below

		/* Add action listeners for each desired menu item *************/
		mnuItemExit.addActionListener(this);
		mnuItemUpdate.addActionListener(this);
		mnuItemDelete.addActionListener(this);
		mnuItemOpenTicket.addActionListener(this);
		mnuItemViewTicket.addActionListener(this);

		/*
		 * continue implementing any other desired sub menu items (like for update and
		 * delete sub menus for example) with similar syntax & logic as shown above
		 */

	}

	private void prepareGUI() {

		// create JMenu bar
		JMenuBar bar = new JMenuBar();
		bar.add(mnuFile); // add main menu items in order, to JMenuBar
		bar.add(mnuAdmin);
		bar.add(mnuTickets);
		// add menu bar components to frame
		setJMenuBar(bar);

		addWindowListener(new WindowAdapter() {
			// define a window close operation
			public void windowClosing(WindowEvent wE) {
				System.exit(0);
			}
		});
		// set frame options
		setSize(400, 400);
		getContentPane().setBackground(Color.LIGHT_GRAY);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// implement actions for sub menu items
		if (e.getSource() == mnuItemExit) {
			System.exit(0);
		} else if (e.getSource() == mnuItemOpenTicket) {

			// get ticket information
			String ticketName, ticketDesc;
			try {
				ticketName = JOptionPane.showInputDialog(null, "Enter your name");
				if (ticketName.equals(null))
					return;
			} catch (Exception e1) {
				return;
			}

			try {
				ticketDesc = JOptionPane.showInputDialog(null, "Enter a ticket description");
				if (ticketDesc.equals(null))
					return;
			} catch (Exception e1) {
				return;
			}

			String starDateS = JOptionPane.showInputDialog(null, "Enter start date (yyyy-MM-dd):");
			String endDateS = JOptionPane.showInputDialog(null, "Enter start date (yyyy-MM-dd):");
			Date startdate = null, endDate = null;
			try {
				startdate = convertStringToSqlDate(starDateS);

			} catch (Exception e1) {
				System.out.println("Error in Start Date You Entered");
				return;
			}
			try {
				endDate = convertStringToSqlDate(endDateS);

			} catch (Exception e1) {
				System.out.println("Error in End Date You Entered");
				return;
			}

			// insert ticket information to database

			int id = dao.insertRecord(ticketName, ticketDesc, startdate, endDate);

			// display results if successful or not to console / dialog box
			if (id != 0) {
				System.out.println("Ticket ID : " + id + " created successfully!!!");
				JOptionPane.showMessageDialog(null, "Ticket id: " + id + " created");
			} else
				System.out.println("Ticket cannot be created!!!");
		}

		else if (e.getSource() == mnuItemViewTicket) {

			// retrieve all tickets details for viewing in JTable

			getAllDetails();
		} else if (e.getSource() == mnuItemUpdate) {
			System.out.println("Update Button Selected");
			// Update Ticket
			String ticketIdToUpdate = JOptionPane.showInputDialog(null, "Enter the ID of the ticket to update:");
			int ticketId;
			if (ticketIdToUpdate != null && !ticketIdToUpdate.trim().isEmpty()) {
				ticketId = Integer.parseInt(ticketIdToUpdate);
			} else {
				System.out.println("Invalid Ticked ID");
				return;
			}

			String ticketName = null, ticketDesc = null;
			;
			try {
				ticketName = JOptionPane.showInputDialog(null, "Enter your name");
				if (ticketName.equals(null))
					return;
			} catch (Exception e1) {
				return;
			}

			try {
				ticketDesc = JOptionPane.showInputDialog(null, "Enter a ticket description");
				if (ticketDesc.equals(null))
					return;
			} catch (Exception e1) {
				return;
			}

			String starDateS = JOptionPane.showInputDialog(null, "Enter start date (yyyy-MM-dd):");

			Date startdate = null, endDate = null;
			try {
				startdate = convertStringToSqlDate(starDateS);

			} catch (Exception e1) {
				System.out.println("Error in Start Date You Entered");
				return;
			}

			try {
				String endDateS = JOptionPane.showInputDialog(null, "Enter start date (yyyy-MM-dd):");
				endDate = convertStringToSqlDate(endDateS);

			} catch (Exception e1) {
				System.out.println("Error in End Date You Entered");
				return;
			}
			dao.updateRecords(ticketId, ticketDesc, startdate, endDate);
			getAllDetails();

		} else if (e.getSource() == mnuItemDelete)

		{
			System.out.println("Deleted option selected");
			// Delete Ticket
			String ticketIdToDelete = JOptionPane.showInputDialog(null, "Enter the ID of the ticket to delete:");

			if (ticketIdToDelete != null && !ticketIdToDelete.trim().isEmpty()) {
				int ticketId = Integer.parseInt(ticketIdToDelete);

				// You can prompt the user for confirmation before deleting the ticket
				int confirmResult = JOptionPane.showConfirmDialog(null,
						"Are you sure you want to delete ticket ID " + ticketId + "?", "Confirm Deletion",
						JOptionPane.YES_NO_OPTION);

				if (confirmResult == JOptionPane.YES_OPTION) {
					// Perform the deletion in the database
					 dao.deleteRecords(ticketId);

				} else {
					System.out.println("Deletion canceled by user.");
				}
			} else {
				System.out.println("Invalid ticket ID entered.");
			}
			getAllDetails();
		}
	}

	private void getAllDetails() {
		System.out.println("retrive all tickets");

		try {

			// Use JTable built in functionality to build a table model and
			// display the table model off your result set!!!
			JTable jt = new JTable(ticketsJTable.buildTableModel(dao.readRecords()));
			jt.setBounds(30, 40, 200, 400);
			JScrollPane sp = new JScrollPane(jt);
			add(sp);
			setVisible(true); // refreshes or repaints frame on screen

		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}

	private static Date convertStringToSqlDate(String dateString) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		try {
			java.util.Date utilDate = dateFormat.parse(dateString);
			return new Date(utilDate.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
}
