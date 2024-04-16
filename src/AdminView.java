import javax.swing.*;
import java.awt.*;
import com.formdev.flatlaf.FlatLightLaf;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import net.proteanit.sql.DbUtils;

public class AdminView extends JFrame {
    private JTabbedPane tabbedPane;
    private JTextField searchField;
    private JButton searchButton;

    public AdminView() {
        // Set the FlatLaf light theme
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize FlatLaf");
        }

        // Set up the frame
        setTitle("Airport Management System - Admin View");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Set window to full-screen mode
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the frame

        // Create a content pane
        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setBackground(new Color(243, 243, 243));

        // Add search bar components
        JPanel searchBarPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchField = new JTextField(20);
        searchButton = new JButton("Search");
        searchBarPanel.add(searchField);
        searchBarPanel.add(searchButton);
        contentPane.add(searchBarPanel, BorderLayout.NORTH);

        // Create a tabbed pane with top tabs
        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.setBackground(new Color(243, 243, 243));
        tabbedPane.setForeground(new Color(51, 51, 51));

        // Create panels for each view
        JPanel baggagePanel = createBaggagePanel();
        JPanel passengersPanel = createPassengersPanel();
        JPanel flightsPanel = createFlightsPanel();
        JPanel staffPanel = createStaffPanel(); // Added this line

        // Add panels to the tabbed pane
        tabbedPane.addTab("Baggage", baggagePanel);
        tabbedPane.addTab("Passengers", passengersPanel);
        tabbedPane.addTab("Flights", flightsPanel);
        tabbedPane.addTab("Staff", staffPanel);

        // Add the tabbed pane to the content pane
        contentPane.add(tabbedPane, BorderLayout.CENTER);

        // Set the content pane for the frame
        setContentPane(contentPane);

        // Attach search button listener
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchText = searchField.getText();
                // Perform search operation based on searchText
                // Update UI if needed
            }
        });
    }

    // Other methods (createBaggagePanel, createPassengersPanel, etc.) remain unchanged



private JPanel createBaggagePanel() {
        JPanel baggagePanel = new JPanel(new BorderLayout());
        baggagePanel.setBackground(new Color(243, 243, 243));

        JTable baggageTable = new JTable();
        baggageTable.setBackground(new Color(243, 243, 243));
        baggageTable.setForeground(new Color(51, 51, 51));
        baggageTable.setSelectionBackground(new Color(204, 204, 204));
        baggageTable.setSelectionForeground(new Color(51, 51, 51));
        baggageTable.setGridColor(new Color(204, 204, 204));

        try {
            String url = "jdbc:mysql://127.0.0.1:3306/airportdb";
            String username = "root";
            String password = "dbsproject:(";
            Connection conn = DriverManager.getConnection(url, username, password);

            String query = "SELECT * FROM bags";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            baggageTable.setModel(DbUtils.resultSetToTableModel(rs));

            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Add the JTable to a JScrollPane
        JScrollPane scrollPane = new JScrollPane(baggageTable);
        baggagePanel.add(scrollPane, BorderLayout.CENTER);

        return baggagePanel;
    }

    private JPanel createPassengersPanel() {
        JPanel passengersPanel = new JPanel(new BorderLayout());
        passengersPanel.setBackground(new Color(243, 243, 243));

        JTable passengerTable = new JTable();
        passengerTable.setBackground(new Color(243, 243, 243));
        passengerTable.setForeground(new Color(51, 51, 51));
        passengerTable.setSelectionBackground(new Color(204, 204, 204));
        passengerTable.setSelectionForeground(new Color(51, 51, 51));
        passengerTable.setGridColor(new Color(204, 204, 204));

        try {
            String url = "jdbc:mysql://127.0.0.1:3306/airportdb";
            String username = "root";
            String password = "dbsproject:(";
            Connection conn = DriverManager.getConnection(url, username, password);

            String query = "SELECT * FROM passengers";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            passengerTable.setModel(DbUtils.resultSetToTableModel(rs));

            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        JScrollPane scrollPane = new JScrollPane(passengerTable);
        passengersPanel.add(scrollPane, BorderLayout.CENTER);

        return passengersPanel;
    }

    private JPanel createFlightsPanel() {
        JPanel flightsPanel = new JPanel(new BorderLayout());
        flightsPanel.setBackground(new Color(243, 243, 243));

        JTable flightsTable = new JTable();
        flightsTable.setBackground(new Color(243, 243, 243));
        flightsTable.setForeground(new Color(51, 51, 51));
        flightsTable.setSelectionBackground(new Color(204, 204, 204));
        flightsTable.setSelectionForeground(new Color(51, 51, 51));
        flightsTable.setGridColor(new Color(204, 204, 204));

        try {
            String url = "jdbc:mysql://127.0.0.1:3306/airportdb";
            String username = "root";
            String password = "dbsproject:(";
            Connection conn = DriverManager.getConnection(url, username, password);

            String query = "SELECT * FROM flights";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

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

    private JPanel createStaffPanel() {
        JPanel staffPanel = new JPanel(new BorderLayout());
        staffPanel.setBackground(new Color(243, 243, 243));

        JTable staffTable = new JTable();
        staffTable.setBackground(new Color(243, 243, 243));
        staffTable.setForeground(new Color(51, 51, 51));
        staffTable.setSelectionBackground(new Color(204, 204, 204));
        staffTable.setSelectionForeground(new Color(51, 51, 51));
        staffTable.setGridColor(new Color(204, 204, 204));

        try {
            String url = "jdbc:mysql://127.0.0.1:3306/airportdb";
            String username = "root";
            String password = "dbsproject:(";
            Connection conn = DriverManager.getConnection(url, username, password);

            String query = "SELECT * FROM staff";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            staffTable.setModel(DbUtils.resultSetToTableModel(rs));

            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        JScrollPane scrollPane = new JScrollPane(staffTable);
        staffPanel.add(scrollPane, BorderLayout.CENTER);

        return staffPanel;
    }

    public static void main(String[] args) {
        // Create and display the GUI
        SwingUtilities.invokeLater(() -> {
            AdminView adminView = new AdminView();
            adminView.setVisible(true);
        });
    }
}