import javax.swing.*;
import java.awt.*;
import com.formdev.flatlaf.FlatDarkLaf;
import java.sql.*;
import net.proteanit.sql.DbUtils;

public class AdminView extends JFrame {
    private JTabbedPane tabbedPane;

    public AdminView() {
        // Set the FlatDarkLaf look and feel
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize FlatDarkLaf look and feel");
        }

        // Set up the frame
        setTitle("Airport Management System - Admin View");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Set window to full-screen mode
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the frame

        // Create a content pane with a dark background color
        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setBackground(new Color(36, 36, 36));

        // Create a tabbed pane with vertical tabs on the left
        tabbedPane = new JTabbedPane(JTabbedPane.LEFT);
        tabbedPane.setBackground(new Color(36, 36, 36));
        tabbedPane.setForeground(Color.WHITE);

        // Create panels for each view
        JPanel baggagePanel = createBaggagePanel();
        JPanel passengersPanel = createPassengersPanel();
        JPanel flightsPanel = createFlightsPanel();
        JPanel staffPanel = createStaffPanel();

        // Add panels to the tabbed pane
        tabbedPane.addTab("Baggage", baggagePanel);
        tabbedPane.addTab("Passengers", passengersPanel);
        tabbedPane.addTab("Flights", flightsPanel);
        tabbedPane.addTab("Staff", staffPanel);

        // Add the tabbed pane to the content pane
        contentPane.add(tabbedPane, BorderLayout.CENTER);

        // Set the content pane for the frame
        setContentPane(contentPane);
    }

    private JPanel createBaggagePanel() {
        JPanel baggagePanel = new JPanel(new BorderLayout());
        baggagePanel.setBackground(new Color(36, 36, 36));
        baggagePanel.setForeground(Color.WHITE);

        JTable baggageTable = new JTable();
        baggageTable.setBackground(new Color(36, 36, 36));
        baggageTable.setForeground(Color.WHITE);
        baggageTable.setSelectionBackground(new Color(51, 51, 51));
        baggageTable.setSelectionForeground(Color.WHITE);
        baggageTable.setGridColor(Color.DARK_GRAY);

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
        passengersPanel.setBackground(new Color(36, 36, 36));
        passengersPanel.setForeground(Color.WHITE);

        JTable passengerTable = new JTable();
        passengerTable.setBackground(new Color(36, 36, 36));
        passengerTable.setForeground(Color.WHITE);
        passengerTable.setSelectionBackground(new Color(51, 51, 51));
        passengerTable.setSelectionForeground(Color.WHITE);
        passengerTable.setGridColor(Color.DARK_GRAY);

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
        flightsPanel.setBackground(new Color(36, 36, 36));
        flightsPanel.setForeground(Color.WHITE);

        JTable flightsTable = new JTable();
        flightsTable.setBackground(new Color(36, 36, 36));
        flightsTable.setForeground(Color.WHITE);
        flightsTable.setSelectionBackground(new Color(51, 51, 51));
        flightsTable.setSelectionForeground(Color.WHITE);
        flightsTable.setGridColor(Color.DARK_GRAY);

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
        staffPanel.setBackground(new Color(36, 36, 36));
        staffPanel.setForeground(Color.WHITE);

        JTable staffTable = new JTable();
        staffTable.setBackground(new Color(36, 36, 36));
        staffTable.setForeground(Color.WHITE);
        staffTable.setSelectionBackground(new Color(51, 51, 51));
        staffTable.setSelectionForeground(Color.WHITE);
        staffTable.setGridColor(Color.DARK_GRAY);

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
}