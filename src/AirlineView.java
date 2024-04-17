import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

import com.formdev.flatlaf.FlatDarkLaf;
import net.proteanit.sql.DbUtils;

public class AirlineView extends JFrame {
    private JTabbedPane tabbedPane;
    private String airline;

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

    public AirlineView(String airline) {
        this.airline = airline;

        // Set the FlatDarkLaf look and feel
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize FlatDarkLaf look and feel");
        }

        // Set up the frame
        setTitle("Airline View - " + airline);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Set window to full-screen mode
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the frame

        // Create a content pane with a dark background color
        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setBackground(new Color(36, 36, 36));

        // Create a tabbed pane with vertical tabs on the left
        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.setBackground(new Color(36, 36, 36));
        tabbedPane.setForeground(Color.WHITE);

        // Create panels for each view
        JPanel flightsPanel = createFlightsPanel();
        JPanel passengersPanel = createPassengersPanel();
        JPanel beltLanePanel = createBeltLanePanel();
        JPanel redPassengers = createRedPassengers();
        JPanel redBags = createRedBags();
        JPanel excessBags = createExcessBags();
        JPanel lostBags = createLostBags();
        JPanel status = createStatus();
        JPanel inventory = createInventoryPanel();
        JPanel staff = createStaffPanel();
        JPanel privilege = createPrivilegePanel();

        // Add panels to the tabbed pane
        tabbedPane.addTab("Flights", flightsPanel);
        tabbedPane.addTab("Passengers", passengersPanel);
        tabbedPane.addTab("Belt/Lane Info", beltLanePanel);
        tabbedPane.addTab("Red Flagged Passengers", redPassengers);
        tabbedPane.addTab("Unsafe Bags", redBags);
        tabbedPane.addTab("Excess Bags", excessBags);
        tabbedPane.addTab("Lost Bags", lostBags);
        tabbedPane.addTab("Status", status);
        tabbedPane.addTab("Inventory", inventory);
        tabbedPane.addTab("Staff", staff);
        tabbedPane.addTab("Privilege", privilege);

        // Add the tabbed pane to the content pane
        contentPane.add(tabbedPane, BorderLayout.CENTER);

        // Set the content pane for the frame
        setContentPane(contentPane);
    }

    private JPanel createFlightsPanel() {
        JPanel flightsPanel = createPanel();
        JTable flightsTable = createTable();
        try {
            String url = "jdbc:mysql://127.0.0.1:3306/airportdb";
            String username = "root";
            String password = "dbsproject:(";
            Connection conn = DriverManager.getConnection(url, username, password);

            String query = "SELECT * FROM flights WHERE IATA_Code = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, airline);
            ResultSet rs = stmt.executeQuery();
            flightsTable.setModel(DbUtils.resultSetToTableModel(rs));

            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        JScrollPane scrollPane = new JScrollPane(flightsTable);
        flightsPanel.add(scrollPane, BorderLayout.CENTER);

        return flightsPanel;
    }

    private JPanel createPassengersPanel() {
        return tableFromProc("GetPassengerInfoByAirline");
    }

    private JPanel createBeltLanePanel() {
        return tableFromProc("GetBeltAndLaneInfo");
    }

    private JPanel createRedPassengers() {
        return tableFromProc("ViewRedFlaggedPassengersByAirline");
    }

    private JPanel createRedBags() {
        return tableFromProc("ViewUnsecureBagsByAirline");
    }

    private JPanel createExcessBags() {
        return tableFromProc("GetExcessBaggage");
    }

    private JPanel createLostBags() {
        return tableFromProc("ViewLostBagsByAirline");
    }

    private JPanel createStatus() {
        JPanel statusPanel = createPanel();
        JPanel flightStatusPanel = new JPanel(new GridBagLayout());
        flightStatusPanel.setBackground(new Color(36, 36, 36));
        flightStatusPanel.setForeground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Flight Number Input
        JLabel flightNumberLabel = new JLabel("Flight Number:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        flightStatusPanel.add(flightNumberLabel, gbc);

        JTextField flightNumberField = new JTextField(10);
        gbc.gridx = 1;
        gbc.gridy = 0;
        flightStatusPanel.add(flightNumberField, gbc);

        // Status Dropdown
        JLabel statusLabel = new JLabel("Status:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        flightStatusPanel.add(statusLabel, gbc);

        JComboBox<String> statusDropdown = new JComboBox<>(new String[]{"Delayed", "Arrived", "Boarding", "On-Time"});
        gbc.gridx = 1;
        gbc.gridy = 1;
        flightStatusPanel.add(statusDropdown, gbc);

        JButton updateButton = new JButton("Update Status");
        updateButton.addActionListener(e -> {
            String flightNumber = flightNumberField.getText();
            String newStatus = (String) statusDropdown.getSelectedItem();
            try {
                String url = "jdbc:mysql://127.0.0.1:3306/airportdb";
                String username = "root";
                String password = "dbsproject:(";
                Connection conn = DriverManager.getConnection(url, username, password);

                String call = "{call SetStatus" + "(?, ?, ?)}";
                CallableStatement pstmt = conn.prepareCall(call);
                pstmt.setString(1, flightNumber);
                pstmt.setString(2, newStatus);
                pstmt.setString(3, airline);
                pstmt.execute();

                pstmt.close();
                conn.close();
            } catch (SQLException error) {
                error.printStackTrace();
            }

        });
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        flightStatusPanel.add(updateButton, gbc);

        statusPanel.add(flightStatusPanel, BorderLayout.NORTH);

        return statusPanel;
    }

    private JPanel createInventoryPanel() {
        JPanel inventoryPanel = new JPanel(new BorderLayout());
        inventoryPanel.setBackground(new Color(36, 36, 36));
        inventoryPanel.setForeground(Color.WHITE);

        // Create a panel for the inventory management
        JPanel inventoryManagementPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        inventoryManagementPanel.setBackground(new Color(36, 36, 36));
        inventoryManagementPanel.setForeground(Color.WHITE);

        // Panel for adding inventory
        JPanel addInventoryPanel = new JPanel(new GridBagLayout());
        addInventoryPanel.setBackground(new Color(36, 36, 36));
        addInventoryPanel.setForeground(Color.WHITE);
        addInventoryPanel.setBorder(BorderFactory.createTitledBorder("Add Inventory"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // ID Input
        JLabel idLabel = new JLabel("Flight ID:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        addInventoryPanel.add(idLabel, gbc);

        JTextField idField = new JTextField(10);
        gbc.gridx = 1;
        gbc.gridy = 0;
        addInventoryPanel.add(idField, gbc);

        // Type Dropdown
        JLabel typeLabel = new JLabel("Type:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        addInventoryPanel.add(typeLabel, gbc);

        JComboBox<String> typeDropdown = new JComboBox<>(new String[]{"Pushback Tug", "Catering Truck", "Fuel Truck", "Baggage Cart", "Passenger Bus"});
        gbc.gridx = 1;
        gbc.gridy = 1;
        addInventoryPanel.add(typeDropdown, gbc);

        // Add Button
        JButton addButton = new JButton("Add");
        addButton.addActionListener(e -> {
            String id = idField.getText();
            String type = (String) typeDropdown.getSelectedItem();
            try {
                String url = "jdbc:mysql://127.0.0.1:3306/airportdb";
                String username = "root";
                String password = "dbsproject:(";
                Connection conn = DriverManager.getConnection(url, username, password);

                String call = "{call addEquipment" + "(?, ?)}";
                CallableStatement pstmt = conn.prepareCall(call);
                pstmt.setString(1, id);
                pstmt.setString(2, type);
                pstmt.execute();

                pstmt.close();
                conn.close();
            } catch (SQLException error) {
                error.printStackTrace();
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        addInventoryPanel.add(addButton, gbc);

        // Panel for removing inventory
        JPanel removeInventoryPanel = new JPanel(new GridBagLayout());
        removeInventoryPanel.setBackground(new Color(36, 36, 36));
        removeInventoryPanel.setForeground(Color.WHITE);
        removeInventoryPanel.setBorder(BorderFactory.createTitledBorder("Remove Inventory"));

        // ID Input
        JLabel removeIdLabel = new JLabel("ID:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        removeInventoryPanel.add(removeIdLabel, gbc);

        JTextField removeIdField = new JTextField(10);
        gbc.gridx = 1;
        gbc.gridy = 0;
        removeInventoryPanel.add(removeIdField, gbc);

        // Remove Button
        JButton removeButton = new JButton("Remove");
        removeButton.addActionListener(e -> {
            String id = removeIdField.getText();
            try {
                String url = "jdbc:mysql://127.0.0.1:3306/airportdb";
                String username = "root";
                String password = "dbsproject:(";
                Connection conn = DriverManager.getConnection(url, username, password);

                String call = "{call removeEquipment" + "(?)}";
                CallableStatement pstmt = conn.prepareCall(call);
                pstmt.setString(1, id);
                pstmt.execute();

                pstmt.close();
                conn.close();
            } catch (SQLException error) {
                error.printStackTrace();
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        removeInventoryPanel.add(removeButton, gbc);

        inventoryManagementPanel.add(addInventoryPanel);
        inventoryManagementPanel.add(removeInventoryPanel);

        inventoryPanel.add(inventoryManagementPanel, BorderLayout.NORTH);

        return inventoryPanel;
    }

    private JPanel createStaffPanel() {
        JPanel staffPanel = new JPanel(new BorderLayout());
        staffPanel.setBackground(new Color(36, 36, 36));
        staffPanel.setForeground(Color.WHITE);

        // Create a panel for the staff management
        JPanel staffManagementPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        staffManagementPanel.setBackground(new Color(36, 36, 36));
        staffManagementPanel.setForeground(Color.WHITE);

        // Panel for adding staff
        JPanel addStaffPanel = new JPanel(new GridBagLayout());
        addStaffPanel.setBackground(new Color(36, 36, 36));
        addStaffPanel.setForeground(Color.WHITE);
        addStaffPanel.setBorder(BorderFactory.createTitledBorder("Add Staff"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Role Input
        JLabel roleLabel = new JLabel("Role:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        addStaffPanel.add(roleLabel, gbc);
        JTextField roleField = new JTextField(10);
        gbc.gridx = 1;
        gbc.gridy = 0;
        addStaffPanel.add(roleField, gbc);

        // First Name Input
        JLabel firstNameLabel = new JLabel("First Name:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        addStaffPanel.add(firstNameLabel, gbc);
        JTextField firstNameField = new JTextField(10);
        gbc.gridx = 1;
        gbc.gridy = 1;
        addStaffPanel.add(firstNameField, gbc);

        // Last Name Input
        JLabel lastNameLabel = new JLabel("Last Name:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        addStaffPanel.add(lastNameLabel, gbc);
        JTextField lastNameField = new JTextField(10);
        gbc.gridx = 1;
        gbc.gridy = 2;
        addStaffPanel.add(lastNameField, gbc);

        // Add Button
        JButton addButton = new JButton("Add");
        addButton.addActionListener(e -> {
            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();
            String role = roleField.getText();
            try {
                String url = "jdbc:mysql://127.0.0.1:3306/airportdb";
                String username = "root";
                String password = "dbsproject:(";
                Connection conn = DriverManager.getConnection(url, username, password);

                String call = "{call AddStaff" + "(?, ?, ?, ?)}";
                CallableStatement pstmt = conn.prepareCall(call);
                pstmt.setString(1, role);
                pstmt.setString(2, airline);
                pstmt.setString(3, firstName);
                pstmt.setString(4, lastName);
                pstmt.execute();

                pstmt.close();
                conn.close();
            } catch (SQLException error) {
                error.printStackTrace();
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        addStaffPanel.add(addButton, gbc);

        // Panel for removing inventory (unchanged)
        JPanel removeInventoryPanel = new JPanel(new GridBagLayout());
        removeInventoryPanel.setBackground(new Color(36, 36, 36));
        removeInventoryPanel.setForeground(Color.WHITE);
        removeInventoryPanel.setBorder(BorderFactory.createTitledBorder("Remove Staff"));

        // ID Input
        JLabel removeIdLabel = new JLabel("ID:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        removeInventoryPanel.add(removeIdLabel, gbc);
        JTextField removeIdField = new JTextField(10);
        gbc.gridx = 1;
        gbc.gridy = 0;
        removeInventoryPanel.add(removeIdField, gbc);

        // Remove Button
        JButton removeButton = new JButton("Remove");
        removeButton.addActionListener(e -> {
            int id = Integer.parseInt(removeIdField.getText());
            try {
                String url = "jdbc:mysql://127.0.0.1:3306/airportdb";
                String username = "root";
                String password = "dbsproject:(";
                Connection conn = DriverManager.getConnection(url, username, password);

                String call = "{call RemoveStaff" + "(?, ?)}";
                CallableStatement pstmt = conn.prepareCall(call);
                pstmt.setInt(1, id);
                pstmt.setString(2, airline);
                pstmt.execute();

                pstmt.close();
                conn.close();
            } catch (SQLException error) {
                error.printStackTrace();
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        removeInventoryPanel.add(removeButton, gbc);

        staffManagementPanel.add(addStaffPanel);
        staffManagementPanel.add(removeInventoryPanel);
        staffPanel.add(staffManagementPanel, BorderLayout.NORTH);

        return staffPanel;
    }

    private JPanel createPrivilegePanel() {
        JPanel privilegePanel = createPanel();
        JPanel updatePrivilegePanel = new JPanel(new GridBagLayout());
        updatePrivilegePanel.setBackground(new Color(36, 36, 36));
        updatePrivilegePanel.setForeground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // ID Input
        JLabel idLabel = new JLabel("Passenger ID:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        updatePrivilegePanel.add(idLabel, gbc);

        JTextField idField = new JTextField(10);
        gbc.gridx = 1;
        gbc.gridy = 0;
        updatePrivilegePanel.add(idField, gbc);

        // Privilege Dropdown
        JLabel privilegeLabel = new JLabel("Privilege:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        updatePrivilegePanel.add(privilegeLabel, gbc);

        JComboBox<String> privilegeDropdown = new JComboBox<>(new String[]{"Economy", "Business", "First Class"});
        gbc.gridx = 1;
        gbc.gridy = 1;
        updatePrivilegePanel.add(privilegeDropdown, gbc);

        JButton updateButton = new JButton("Update Privilege");
        updateButton.addActionListener(e -> {
            String id = idField.getText();
            String privilege = (String) privilegeDropdown.getSelectedItem();
            try {
                String url = "jdbc:mysql://127.0.0.1:3306/airportdb";
                String username = "root";
                String password = "dbsproject:(";
                Connection conn = DriverManager.getConnection(url, username, password);

                String call = "{call ChangePrivilege" + "(?, ?)}";
                CallableStatement pstmt = conn.prepareCall(call);
                pstmt.setString(1, id);
                pstmt.setString(2, privilege);
                pstmt.execute();

                pstmt.close();
                conn.close();
            } catch (SQLException error) {
                error.printStackTrace();
            }

        });
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        updatePrivilegePanel.add(updateButton, gbc);

        privilegePanel.add(updatePrivilegePanel, BorderLayout.NORTH);

        return privilegePanel;
    }

    private JPanel tableFromProc(String query) {
        JPanel panel = createPanel();
        JTable table = createTable();
        try {
            String url = "jdbc:mysql://127.0.0.1:3306/airportdb";
            String username = "root";
            String password = "dbsproject:(";
            Connection conn = DriverManager.getConnection(url, username, password);

            // Corrected call string with procedure name and input parameter placeholder
            String call = "{call " + query + "(?)}"; // Note the correct concatenation

            // Prepare the call to the stored procedure
            CallableStatement pstmt = conn.prepareCall(call);
            pstmt.setString(1, airline);
            ResultSet rs = pstmt.executeQuery();

            table.setModel(DbUtils.resultSetToTableModel(rs));

            rs.close();
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AirlineView airlineView = new AirlineView("6E");
            airlineView.setVisible(true);
        });
    }
}