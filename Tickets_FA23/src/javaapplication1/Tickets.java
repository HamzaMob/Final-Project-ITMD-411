package javaapplication1;

import javax.swing.*;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@SuppressWarnings("serial")
public class Tickets extends JFrame implements ActionListener {

    private Dao dao = new Dao();
    private Boolean chkIfAdmin = null;

    private JMenu mnuFile = new JMenu("File");
    private JMenu mnuAdmin = new JMenu("Admin");
    private JMenu mnuTickets = new JMenu("Tickets");

    private JMenuItem mnuItemExit;
    private JMenuItem mnuItemUpdate;
    private JMenuItem mnuItemDelete;
    private JMenuItem mnuItemOpenTicket;
    private JMenuItem mnuItemViewTicket;
    private JMenuItem mnuItemToggleStatus;

    public Tickets(Boolean isAdmin) {
        chkIfAdmin = isAdmin;
        createMenu();
        prepareGUI();
    }

    private void createMenu() {
        mnuItemExit = new JMenuItem("Exit");
        mnuFile.add(mnuItemExit);

        mnuItemUpdate = new JMenuItem("Update Ticket");
        mnuAdmin.add(mnuItemUpdate);

        mnuItemDelete = new JMenuItem("Delete Ticket");
        mnuAdmin.add(mnuItemDelete);

        mnuItemOpenTicket = new JMenuItem("Open Ticket");
        mnuTickets.add(mnuItemOpenTicket);

        mnuItemViewTicket = new JMenuItem("View Ticket");
        mnuTickets.add(mnuItemViewTicket);

        // Add a new menu item for toggling ticket status
        mnuItemToggleStatus = new JMenuItem("Toggle Ticket Status");
        mnuTickets.add(mnuItemToggleStatus);
        mnuItemToggleStatus.addActionListener(this);
    }

    private void prepareGUI() {
        JMenuBar bar = new JMenuBar();
        bar.add(mnuFile);
        bar.add(mnuAdmin);
        bar.add(mnuTickets);
        setJMenuBar(bar);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent wE) {
                System.exit(0);
            }
        });

        setSize(400, 400);
        getContentPane().setBackground(Color.LIGHT_GRAY);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == mnuItemExit) {
            System.exit(0);
        } else if (e.getSource() == mnuItemOpenTicket) {
            // ... existing code ...
        } else if (e.getSource() == mnuItemViewTicket) {
            getAllDetails();
        } else if (e.getSource() == mnuItemUpdate) {
            // ... existing code ...
        } else if (e.getSource() == mnuItemDelete) {
            // ... existing code ...
        } else if (e.getSource() == mnuItemToggleStatus) {
            toggleTicketStatus();
        }
    }

    private void getAllDetails() {
        try {
            JTable jt = new JTable(ticketsJTable.buildTableModel(dao.readRecords()));
            jt.setBounds(30, 40, 200, 400);
            JScrollPane sp = new JScrollPane(jt);
            add(sp);
            setVisible(true);
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
    }

    private void toggleTicketStatus() {
        String ticketIdToUpdate = JOptionPane.showInputDialog(null, "Enter the ID of the ticket to toggle status:");
        int ticketId;
        if (ticketIdToUpdate != null && !ticketIdToUpdate.trim().isEmpty()) {
            ticketId = Integer.parseInt(ticketIdToUpdate);
            dao.toggleTicketStatus(ticketId);
            getAllDetails();
        } else {
            System.out.println("Invalid Ticket ID");
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Login());
    }
}
