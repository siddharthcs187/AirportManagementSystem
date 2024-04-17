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

        // Add panels to the tabbed pane
        tabbedPane.addTab("Flights", flightsPanel);
        tabbedPane.addTab("Passengers", passengersPanel);
        tabbedPane.addTab("Belt/Lane Info", beltLanePanel);
        tabbedPane.addTab("Red Flagged Passengers", redPassengers);
        tabbedPane.addTab("Unsafe Bags", redBags);
        tabbedPane.addTab("Excess Bags", excessBags);

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