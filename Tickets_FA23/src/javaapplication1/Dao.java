package javaapplication1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JOptionPane;

public class Dao {
	// instance fields
	static Connection connect = null;
	Statement statement = null;

	// constructor
	public Dao() {
	  
	}

	public Connection getConnection() {
		// Setup the connection with the DB
		try {
			connect = DriverManager
					.getConnection("jdbc:mysql://www.papademas.net:3307/tickets?autoReconnect=true&useSSL=false"
							+ "&user=fp411&password=411");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return connect;
	}

	// CRUD implementation

	public void createTables() {
		// variables for SQL Query table creations
		//final String createTicketsTable = "CREATE TABLE hamza_jpapa_tickets(ticket_id INT AUTO_INCREMENT PRIMARY KEY, ticket_issuer VARCHAR(30), ticket_description VARCHAR(200))";
		final String createTicketsTable = "CREATE TABLE hamza_jpapa_tickets (tid INT PRIMARY KEY AUTO_INCREMENT,user VARCHAR(255) NOT NULL,ticket_desc VARCHAR(400),start_date DATE,end_date DATE);";
		final String createUsersTable = "CREATE TABLE hamza_jpapa_users(uid INT AUTO_INCREMENT PRIMARY KEY, uname VARCHAR(30), upass VARCHAR(30), admin int)";

		try {

			// execute queries to create tables

			statement = getConnection().createStatement();

			statement.executeUpdate(createTicketsTable);
			statement.executeUpdate(createUsersTable);
			System.out.println("Created tables in given database...");

			// end create table
			// close connection/statement object
			statement.close();
			connect.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		// add users to user table
		addUsers();
	}

	public void addUsers() {
		// add list of users from userlist.csv file to users table

		// variables for SQL Query inserts
		String sql;

		Statement statement;
		BufferedReader br;
		List<List<String>> array = new ArrayList<>(); // list to hold (rows & cols)

		// read data from file
		try {
			br = new BufferedReader(new FileReader(new File("./userlist.csv")));

			String line;
			while ((line = br.readLine()) != null) {
				array.add(Arrays.asList(line.split(",")));
			}
		} catch (Exception e) {
			System.out.println("There was a problem loading the file");
		}

		try {

			// Setup the connection with the DB

			statement = getConnection().createStatement();

			// create loop to grab each array index containing a list of values
			// and PASS (insert) that data into your User table
			for (List<String> rowData : array) {

				sql = "insert into hamza_jpapa_users(uname,upass,admin) " + "values('" + rowData.get(0) + "'," + " '"
						+ rowData.get(1) + "','" + rowData.get(2) + "');";
				statement.executeUpdate(sql);
			}
			System.out.println("Inserts completed in the given database...");

			// close statement object
			statement.close();

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	 public int insertRecord(String ticketName, String ticketDesc, Date startDate, Date endDate) {
	        int id = 0;

	        try (Connection connection = getConnection();
	             PreparedStatement preparedStatement = connection.prepareStatement(
	                     "INSERT INTO hamza_jpapa_tickets (user, ticket_desc, start_date, end_date) VALUES (?, ?, ?, ?)",
	                     PreparedStatement.RETURN_GENERATED_KEYS)) {

	            preparedStatement.setString(1, ticketName);
	            preparedStatement.setString(2, ticketDesc);
	            preparedStatement.setDate(3, startDate);
	            preparedStatement.setDate(4, endDate);

	            int affectedRows = preparedStatement.executeUpdate();

	            if (affectedRows > 0) {
	                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
	                    if (generatedKeys.next()) {
	                        id = generatedKeys.getInt(1);
	                    } else {
	                        throw new SQLException("Failed to retrieve auto-generated key.");
	                    }
	                }
	            }

	        } catch (SQLException e) {
	            e.printStackTrace();
	        }

	        return id;
	    }

	public ResultSet readRecords() {

		ResultSet results = null;
		try {
			statement = connect.createStatement();
			results = statement.executeQuery("SELECT * FROM hamza_jpapa_tickets");
			System.out.println(results);
			if(results!=null)
				System.out.println("result set not null");
			//connect.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		return results;
	}
	
	
	
	// continue coding for updateRecords implementation
	
	public void updateRecords(int ticketId, String newTicketDesc, Date newStartDate, Date newEndDate) {
	    try {
	        String updateQuery = "UPDATE hamza_jpapa_tickets SET ticket_desc = ?, start_date = ?, end_date = ? WHERE tid = ?";
	        try (PreparedStatement preparedStatement = connect.prepareStatement(updateQuery)) {
	            preparedStatement.setString(1, newTicketDesc);
	            preparedStatement.setDate(2, newStartDate);
	            preparedStatement.setDate(3, newEndDate);
	            preparedStatement.setInt(4, ticketId);

	            int affectedRows = preparedStatement.executeUpdate();

	            if (affectedRows > 0) {
	                System.out.println("Record updated successfully!");
	                JOptionPane.showMessageDialog(null,"SUCCESS FULL", "Record updated successfully!",JOptionPane.INFORMATION_MESSAGE);
	            } else {
	                System.out.println("No records were updated.");
	                JOptionPane.showMessageDialog(null,"FAIL", "No records were updated.",JOptionPane.INFORMATION_MESSAGE);

	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

	// continue coding for deleteRecords implementation
	public void deleteRecords(int ticketId) {
	    try {
	        String deleteQuery = "DELETE FROM hamza_jpapa_tickets WHERE tid = ?";
	        try (PreparedStatement preparedStatement = connect.prepareStatement(deleteQuery)) {
	            preparedStatement.setInt(1, ticketId);

	            int affectedRows = preparedStatement.executeUpdate();

	            if (affectedRows > 0) {
	                System.out.println("Record deleted successfully!");
	                JOptionPane.showMessageDialog(null,"SUCCESSS FULL", "Record deleted successfully!",JOptionPane.INFORMATION_MESSAGE);

	            } else {
	                System.out.println("No records were deleted.");
	                JOptionPane.showMessageDialog(null,"FAIL", "No records were deleted.",JOptionPane.INFORMATION_MESSAGE);

	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

}
