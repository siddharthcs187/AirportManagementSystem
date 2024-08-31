import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.formdev.flatlaf.FlatLightLaf;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SecurityScreen extends JFrame implements ActionListener {
    private JTextField bagIdField1, bagIdField2, passengerIdField1, passengerIdField2;
    private JButton flagBagButton, unflagBagButton, flagPassengerButton, unflagPassengerButton;

    public SecurityScreen() {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize FlatLaf");
        }

        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.setBackground(new Color(243, 243, 243));
        tabbedPane.setForeground(new Color(51, 51, 51));

        JPanel bagPanel = new JPanel(new GridBagLayout());
        bagPanel.setBackground(new Color(243, 243, 243));
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel bagIdLabel1 = new JLabel("Enter Bag ID to Flag:");
        bagIdLabel1.setFont(new Font("Arial", Font.BOLD, 14));
        bagIdLabel1.setForeground(new Color(51, 51, 51));

        bagIdField1 = new JTextField(20);
        bagIdField1.setFont(new Font("Arial", Font.PLAIN, 14));
        bagIdField1.setBorder(BorderFactory.createLineBorder(new Color(204, 204, 204), 1));

        flagBagButton = new JButton("Flag Bag");
        flagBagButton.setFont(new Font("Arial", Font.BOLD, 14));
        flagBagButton.setForeground(Color.WHITE);
        flagBagButton.setBackground(new Color(51, 153, 255));
        flagBagButton.setBorderPainted(false);
        flagBagButton.addActionListener(this);

        JLabel bagIdLabel2 = new JLabel("Enter Bag ID to Unflag:");
        bagIdLabel2.setFont(new Font("Arial", Font.BOLD, 14));
        bagIdLabel2.setForeground(new Color(51, 51, 51));

        bagIdField2 = new JTextField(20);
        bagIdField2.setFont(new Font("Arial", Font.PLAIN, 14));
        bagIdField2.setBorder(BorderFactory.createLineBorder(new Color(204, 204, 204), 1));

        unflagBagButton = new JButton("Unflag Bag");
        unflagBagButton.setFont(new Font("Arial", Font.BOLD, 14));
        unflagBagButton.setForeground(Color.WHITE);
        unflagBagButton.setBackground(new Color(255, 102, 102));
        unflagBagButton.setBorderPainted(false);
        unflagBagButton.addActionListener(this);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(10, 10, 10, 10);
        bagPanel.add(bagIdLabel1, gbc);
        gbc.gridy = 1;
        bagPanel.add(bagIdField1, gbc);
        gbc.gridy = 2;
        bagPanel.add(flagBagButton, gbc);
        gbc.gridy = 3;
        bagPanel.add(bagIdLabel2, gbc);
        gbc.gridy = 4;
        bagPanel.add(bagIdField2, gbc);
        gbc.gridy = 5;
        bagPanel.add(unflagBagButton, gbc);

        JPanel passengerPanel = new JPanel(new GridBagLayout());
        passengerPanel.setBackground(new Color(243, 243, 243));
        gbc = new GridBagConstraints();

        JLabel passengerIdLabel1 = new JLabel("Enter Passenger ID to Flag:");
        passengerIdLabel1.setFont(new Font("Arial", Font.BOLD, 14));
        passengerIdLabel1.setForeground(new Color(51, 51, 51));

        passengerIdField1 = new JTextField(20);
        passengerIdField1.setFont(new Font("Arial", Font.PLAIN, 14));
        passengerIdField1.setBorder(BorderFactory.createLineBorder(new Color(204, 204, 204), 1));

        flagPassengerButton = new JButton("Flag Passenger");
        flagPassengerButton.setFont(new Font("Arial", Font.BOLD, 14));
        flagPassengerButton.setForeground(Color.WHITE);
        flagPassengerButton.setBackground(new Color(51, 153, 255));
        flagPassengerButton.setBorderPainted(false);
        flagPassengerButton.addActionListener(this);

        JLabel passengerIdLabel2 = new JLabel("Enter Passenger ID to Unflag:");
        passengerIdLabel2.setFont(new Font("Arial", Font.BOLD, 14));
        passengerIdLabel2.setForeground(new Color(51, 51, 51));

        passengerIdField2 = new JTextField(20);
        passengerIdField2.setFont(new Font("Arial", Font.PLAIN, 14));
        passengerIdField2.setBorder(BorderFactory.createLineBorder(new Color(204, 204, 204), 1));

        unflagPassengerButton = new JButton("Unflag Passenger");
        unflagPassengerButton.setFont(new Font("Arial", Font.BOLD, 14));
        unflagPassengerButton.setForeground(Color.WHITE);
        unflagPassengerButton.setBackground(new Color(255, 102, 102));
        unflagPassengerButton.setBorderPainted(false);
        unflagPassengerButton.addActionListener(this);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(10, 10, 10, 10);
        passengerPanel.add(passengerIdLabel1, gbc);
        gbc.gridy = 1;
        passengerPanel.add(passengerIdField1, gbc);
        gbc.gridy = 2;
        passengerPanel.add(flagPassengerButton, gbc);
        gbc.gridy = 3;
        passengerPanel.add(passengerIdLabel2, gbc);
        gbc.gridy = 4;
        passengerPanel.add(passengerIdField2, gbc);
        gbc.gridy = 5;
        passengerPanel.add(unflagPassengerButton, gbc);

        tabbedPane.addTab("Bags", bagPanel);
        tabbedPane.addTab("Passengers", passengerPanel);

        setContentPane(tabbedPane);
        setTitle("Airport Security");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null); 
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == flagBagButton) {
            String bagIdToFlag = bagIdField1.getText();

            try {
                String url = "jdbc:mysql://localhost:3306/airportdb";
                String username = "root";
                String password = "mysql@1704"; 

                try (Connection conn = DriverManager.getConnection(url, username, password)) {
                    String procedure = "FlagBag";
                    String call = "{call " + procedure + "(?)}";
                    try (CallableStatement pstmt = conn.prepareCall(call)) {
                        pstmt.setInt(1, Integer.parseInt(bagIdToFlag)); 
                        pstmt.execute();
                        JOptionPane.showMessageDialog(this, "Bag flagged successfully.");
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Error flagging bag: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                System.err.println("Invalid Bag ID format.");
            }
        }
        else if (e.getSource() == unflagBagButton) {
            String bagIdToUnflag = bagIdField2.getText();

            try {
                String url = "jdbc:mysql://localhost:3306/airportdb";
                String username = "root";
                String password = "mysql@1704"; 

                try (Connection conn = DriverManager.getConnection(url, username, password)) {
                    String procedure = "unFlagBag";
                    String call = "{call " + procedure + "(?)}";
                    try (CallableStatement pstmt = conn.prepareCall(call)) {
                        pstmt.setInt(1, Integer.parseInt(bagIdToUnflag)); 
                        pstmt.execute();
                        JOptionPane.showMessageDialog(this, "Bag unflagged successfully.");
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Error unflagging bag: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                System.err.println("Invalid Bag ID format.");
            }
        }
        else if (e.getSource() == flagPassengerButton) {
            String passengerIdToFlag = passengerIdField1.getText();
            try {
                String url = "jdbc:mysql://localhost:3306/airportdb";
                String username = "root";
                String password = "mysql@1704"; 
                try (Connection conn = DriverManager.getConnection(url, username, password)) {
                    String procedure = "FlagPassenger";
                    String call = "{call " + procedure + "(?)}";
                    try (CallableStatement pstmt = conn.prepareCall(call)) {
                        pstmt.setInt(1, Integer.parseInt(passengerIdToFlag)); 
                        pstmt.execute();
                        JOptionPane.showMessageDialog(this, "Passenger flagged successfully.");
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Error flagging passenger: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                System.err.println("Invalid Passenger ID format.");
            }
        }
        else if (e.getSource() == unflagPassengerButton) {
            String passengerIdToUnflag = passengerIdField2.getText();
            try {
                String url = "jdbc:mysql://localhost:3306/airportdb";
                String username = "root";
                String password = "mysql@1704"; 
                try (Connection conn = DriverManager.getConnection(url, username, password)) {
                    String procedure = "unFlagPassenger";
                    String call = "{call " + procedure + "(?)}";
                    try (CallableStatement pstmt = conn.prepareCall(call)) {
                        pstmt.setInt(1, Integer.parseInt(passengerIdToUnflag)); 
                        pstmt.execute();
                        JOptionPane.showMessageDialog(this, "Passenger unflagged successfully.");
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Error unflagging passenger: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                System.err.println("Invalid Passenger ID format.");
            }
        }
    }
}
