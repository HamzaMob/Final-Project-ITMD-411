package javaapplication1;
import javax.swing.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
            connect = DriverManager.getConnection("jdbc:mysql://www.papademas.net:3307/tickets?autoReconnect=true&useSSL=false" + "&user=fp411&password=411");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connect;
    }

    public void createTables() {
        final String createTicketsTable = "CREATE TABLE hamza_jpapa_tickets (tid INT PRIMARY KEY AUTO_INCREMENT,user VARCHAR(255) NOT NULL,ticket_desc VARCHAR(400),start_date DATE,end_date DATE);";
        final String createUsersTable = "CREATE TABLE hamza_jpapa_users(uid INT AUTO_INCREMENT PRIMARY KEY, uname VARCHAR(30), upass VARCHAR(30), admin int)";

        try {
            statement = getConnection().createStatement();
            if (statement != null) {
                statement.executeUpdate(createTicketsTable);
                statement.executeUpdate(createUsersTable);
                System.out.println("Created tables in the given database...");

                statement.close();
                connect.close();
            } else {
                System.out.println("Failed to establish a connection.");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        addUsers();
    }

    public void addUsers() {
        String sql;
        Statement statement;
        BufferedReader br;
        List<List<String>> array = new ArrayList<>();

        try {
            br = new BufferedReader(new FileReader(new String("./userlist.csv")));
            String line;
            while ((line = br.readLine()) != null) {
                array.add(Arrays.asList(line.split(",")));
            }
        } catch (Exception e) {
            System.out.println("There was a problem loading the file");
        }

        try {
            statement = getConnection().createStatement();
            for (List<String> rowData : array) {
                sql = "INSERT INTO hamza_jpapa_users(uname,upass,admin) " + "VALUES('" + rowData.get(0) + "'," + " '"
                        + rowData.get(1) + "','" + rowData.get(2) + "');";
                statement.executeUpdate(sql);
            }
            System.out.println("Inserts completed in the given database...");
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
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        return results;
    }

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
                    JOptionPane.showMessageDialog(null, "SUCCESS FULL", "Record updated successfully!", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    System.out.println("No records were updated.");
                    JOptionPane.showMessageDialog(null, "FAIL", "No records were updated.", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteRecords(int ticketId) {
        try {
            String deleteQuery = "DELETE FROM hamza_jpapa_tickets WHERE tid = ?";
            try (PreparedStatement preparedStatement = connect.prepareStatement(deleteQuery)) {
                preparedStatement.setInt(1, ticketId);

                int affectedRows = preparedStatement.executeUpdate();

                if (affectedRows > 0) {
                    System.out.println("Record deleted successfully!");
                    JOptionPane.showMessageDialog(null, "SUCCESSS FULL", "Record deleted successfully!", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    System.out.println("No records were deleted.");
                    JOptionPane.showMessageDialog(null, "FAIL", "No records were deleted.", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // New method to toggle the status of a ticket
    public void toggleTicketStatus(int ticketId) {
        try {
            String toggleQuery = "UPDATE hamza_jpapa_tickets SET status = NOT status WHERE tid = ?";
            try (PreparedStatement preparedStatement = connect.prepareStatement(toggleQuery)) {
                preparedStatement.setInt(1, ticketId);

                int affectedRows = preparedStatement.executeUpdate();

                if (affectedRows > 0) {
                    System.out.println("Ticket status toggled successfully!");
                    JOptionPane.showMessageDialog(null, "SUCCESSS FULL", "Ticket status toggled successfully!", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    System.out.println("No ticket status toggled.");
                    JOptionPane.showMessageDialog(null, "FAIL", "No ticket status toggled.", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
