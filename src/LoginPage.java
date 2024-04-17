import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

import com.formdev.flatlaf.FlatDarkLaf;
import net.proteanit.sql.DbUtils;

public class LoginPage extends JFrame implements ActionListener {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> viewSelector;

    public LoginPage() {
        // Set the FlatDarkLaf look and feel
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize FlatDarkLaf look and feel");
        }

        // Set up the frame
        setTitle("Airport Management System - Login");
        setSize(300, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the frame
        JPanel contentPane = new JPanel(new GridLayout(4, 2, 10, 10));
        contentPane.setBackground(new Color(60, 63, 65)); // Set a dark background color
        contentPane.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Create UI components
        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField();
        JLabel viewLabel = new JLabel("View:");
        viewSelector = new JComboBox<>(new String[]{"Admin", "Security", "Airlines", "Passenger"});
        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(this);

        // Add components to the frame
        contentPane.add(usernameLabel);
        contentPane.add(usernameField);
        contentPane.add(passwordLabel);
        contentPane.add(passwordField);
        contentPane.add(viewLabel);
        contentPane.add(viewSelector);
        contentPane.add(new JLabel());
        contentPane.add(loginButton);
        setContentPane(contentPane);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String username = usernameField.getText();
        char[] passwordChars = passwordField.getPassword();
        String password = new String(passwordChars);
        String selectedView = (String) viewSelector.getSelectedItem();
        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter user name", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter password", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        switch (selectedView) {
            case "Admin":
                if (username.equals("admin") && password.equals("password")) {
                    dispose();
                    SwingUtilities.invokeLater(() -> {
                        AdminView adminView = new AdminView();
                        adminView.setVisible(true);
                    });
                }else {
                    JOptionPane.showMessageDialog(this, "Invalid username or password", "Login Error", JOptionPane.ERROR_MESSAGE);
                }
                break;
            case "Security":
                if (username.equals("security") && password.equals("password")) {
                    dispose();
                    SwingUtilities.invokeLater(() -> {
                        SecurityScreen securityScreen = new SecurityScreen();
                        securityScreen.setVisible(true);
                    });
                }else {
                    JOptionPane.showMessageDialog(this, "Invalid username or password", "Login Error", JOptionPane.ERROR_MESSAGE);
                }
                break;
            case "Airlines":
                try {
                    String url = "jdbc:mysql://127.0.0.1:3306/airportdb";
                    String user = "root";
                    String pass = "dbsproject:(";
                    Connection conn = DriverManager.getConnection(url, user, pass);

                    String query = "SELECT * FROM airlines";
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    while (rs.next()) {
                        String flightId = rs.getString("IATA_Code");
                        String flightName = rs.getString("Airline_Name");
                        if (flightId.equals(username) && flightName.equals(password)) {
                            dispose();
                            SwingUtilities.invokeLater(() -> {
                                AirlineView airlineView = new AirlineView(flightId);
                                airlineView.setVisible(true);
                            });
                            break;
                        }
                    }
                    rs.close();
                    stmt.close();
                    conn.close();
                } catch (SQLException event) {
                    event.printStackTrace();
                }
                break;
            case "Passenger":
                break;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginPage loginPage = new LoginPage();
            loginPage.setVisible(true);
        });
    }
}