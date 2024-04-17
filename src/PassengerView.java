import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

import com.formdev.flatlaf.FlatDarkLaf;
import net.proteanit.sql.DbUtils;

public class PassengerView extends JFrame {
    private JTabbedPane tabbedPane;
    private String passenger;

    private JPanel createPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(36, 36, 36));
        panel.setForeground(Color.WHITE);

        return panel;
    }

    private JTable createTable() {
        JTable table = new JTable();
        table.setBackground(new Color(36, 36, 36));
        table.setForeground(Color.WHITE);
        table.setSelectionBackground(new Color(51, 51, 51));
        table.setSelectionForeground(Color.WHITE);
        table.setGridColor(Color.DARK_GRAY);
        return table;
    }

    private JPanel createShopsPanel() {
        JPanel shopsPanel = createPanel();

        JTable shopsTable = createTable();
        try {
            String url = "jdbc:mysql://localhost:3306/airportdb";
            String username = "root";
            String password = "dbsproject:(";
            Connection conn = DriverManager.getConnection(url, username, password);

            // Corrected call string with procedure name and input parameter placeholder
            String procedure = "GetShops";
            String call = "{call " + procedure + "()}";

            // Prepare the call to the stored procedure
            CallableStatement stmt = conn.prepareCall(call);
            ResultSet rs = stmt.executeQuery();

            shopsTable.setModel(DbUtils.resultSetToTableModel(rs));

            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        JScrollPane scrollPane = new JScrollPane(shopsTable);
        shopsPanel.add(scrollPane, BorderLayout.CENTER);

        return shopsPanel;
    }

    private JPanel createPassengerDetailsPanel() {
        JPanel passengerDetailsPanel = createPanel();

        // Create input fields for passenger details
        JTextField shopIdField = new JTextField(20);
        JTextField passengerIdField = new JTextField(20);
        JTextField amountField = new JTextField(10);

        // Create labels for input fields
        JLabel shopIdLabel = new JLabel("Shop ID:");
        JLabel passengerIdLabel = new JLabel("Passenger ID:");
        JLabel amountLabel = new JLabel("Amount:");

        // Create Buy button
        JButton buyButton = new JButton("Buy");
        buyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Call a method or perform actions when Buy button is clicked
                // Implement the logic here
                String shopId = shopIdField.getText();
                String passengerId = passengerIdField.getText();
                String amount = amountField.getText();
                try{
                    String url = "jdbc:mysql://localhost:3306/airportdb";
                    String username = "root";
                    String password = "dbsproject:(";
                    Connection conn = DriverManager.getConnection(url, username, password);

                    String procedure = "InsertShoppingOrder";
                    String call = "{call " + procedure + "(?,?,?)}";
                    CallableStatement stmt = conn.prepareCall(call);
                    stmt.setString(1, shopId);
                    stmt.setString(2, passengerId);
                    stmt.setString(3, amount);
                    ResultSet rs = stmt.executeQuery();

                    rs.close();
                    stmt.close();
                    conn.close();
                }catch(SQLException ex){
                    ex.printStackTrace();
                }
            }
        });

        // Create a layout for the panel
        GroupLayout layout = new GroupLayout(passengerDetailsPanel);
        passengerDetailsPanel.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        // Set up the horizontal group
        GroupLayout.SequentialGroup hGroup = layout.createSequentialGroup();
        hGroup.addGroup(layout.createParallelGroup()
                .addComponent(shopIdLabel)
                .addComponent(passengerIdLabel)
                .addComponent(amountLabel)
                .addComponent(buyButton));
        hGroup.addGroup(layout.createParallelGroup()
                .addComponent(shopIdField)
                .addComponent(passengerIdField)
                .addComponent(amountField));
        layout.setHorizontalGroup(hGroup);

        // Set up the vertical group
        GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(shopIdLabel)
                .addComponent(shopIdField));
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(passengerIdLabel)
                .addComponent(passengerIdField));
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(amountLabel)
                .addComponent(amountField));
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(buyButton));
        layout.setVerticalGroup(vGroup);

        return passengerDetailsPanel;
    }


    public PassengerView(String passenger) {
        this.passenger = passenger;

        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize FlatLaf");
        }

        // Set up the frame
        setTitle("Airport Management System - Passenger View");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Set window to full-screen mode
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the frame

        // Create a content pane
        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setBackground(new Color(36, 36, 36));

        // Create a tabbed pane with top tabs
        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.setBackground(new Color(36, 36, 36));
        tabbedPane.setForeground(Color.WHITE);

        // Create panels for each view
        JPanel shopsPanel = createShopsPanel();
        JPanel passengerDetailsPanel = createPassengerDetailsPanel();

        // Add panels to the tabbed pane
        tabbedPane.addTab("Shops", shopsPanel);
        tabbedPane.addTab("Place Order", passengerDetailsPanel);

        // Add the tabbed pane to the content pane
        contentPane.add(tabbedPane, BorderLayout.CENTER);

        // Set the content pane for the frame
        setContentPane(contentPane);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PassengerView passengerView = new PassengerView("John");
            passengerView.setVisible(true);
        });
    }
}
