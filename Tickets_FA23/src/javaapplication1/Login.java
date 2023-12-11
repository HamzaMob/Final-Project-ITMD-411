package javaapplication1;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@SuppressWarnings("serial")
public class Login extends JFrame {

    private Dao conn;

    public Login() {
        super("🚀 IIT HELP DESK LOGIN 🚀");

        conn = new Dao();
        conn.createTables();
        setSize(600, 300);
        setLayout(new GridLayout(5, 2));
        setLocationRelativeTo(null);

        JLabel lblUsername = new JLabel("👤 Username", JLabel.CENTER);
        JLabel lblPassword = new JLabel("🔒 Password", JLabel.CENTER);
        JLabel lblStatus = new JLabel(" ", JLabel.CENTER);

        JTextField txtUname = new JTextField(10);
        JPasswordField txtPassword = new JPasswordField();
        JButton btn = new JButton("🚀 Submit");
        JButton btnExit = new JButton("🚪 Exit");

        lblStatus.setForeground(Color.RED);
        lblStatus.setFont(new Font("Arial", Font.BOLD, 14));

        add(lblUsername);
        add(txtUname);
        add(lblPassword);
        add(txtPassword);
        add(btn);
        add(btnExit);
        add(lblStatus);

        btn.addActionListener(new ActionListener() {
            int count = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                boolean admin = false;
                count = count + 1;

                String query = "SELECT * FROM hamza_jpapa_users WHERE uname = ? and upass = ?;";
                try (PreparedStatement stmt = conn.getConnection().prepareStatement(query)) {
                    stmt.setString(1, txtUname.getText());
                    stmt.setString(2, new String(txtPassword.getPassword()));
                    ResultSet rs = stmt.executeQuery();
                    if (rs.next()) {
                        admin = rs.getBoolean("admin");
                        new Tickets(admin);
                        setVisible(false);
                        dispose();
                    } else {
                        lblStatus.setText("❌ Try again! " + (3 - count) + " / 3 attempt(s) left");
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        btnExit.addActionListener(e -> System.exit(0));

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Login());
    }
}
