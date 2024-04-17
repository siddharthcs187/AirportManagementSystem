import javax.swing.*;
import java.awt.*;
import com.formdev.flatlaf.FlatDarkLaf;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import net.proteanit.sql.DbUtils;

public class AdminView extends JFrame {
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

    private JTabbedPane tabbedPane;

    public AdminView() {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
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
        contentPane.setBackground(new Color(36, 36, 36));

        // Create a tabbed pane with top tabs
        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
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
        JPanel baggagePanel = createPanel();
        JTable baggageTable = createTable();
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

        // Add search bar components
        JPanel searchBarPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JTextField searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");
        searchBarPanel.add(searchField);
        searchBarPanel.add(searchButton);
        baggagePanel.add(searchBarPanel, BorderLayout.NORTH);

        // Add the JTable to a JScrollPane
        JScrollPane scrollPane = new JScrollPane(baggageTable);
        baggagePanel.add(scrollPane, BorderLayout.CENTER);

        // Attach search button listener
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchText = searchField.getText();
                searchInTable("GetBagDetails", baggageTable, searchText);
            }
        });

        return baggagePanel;
    }

    private JPanel createPassengersPanel() {
        JPanel passengersPanel = createPanel();
        JTable passengerTable = createTable();
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

        // Add search bar components
        JPanel searchBarPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JTextField searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");
        searchBarPanel.add(searchField);
        searchBarPanel.add(searchButton);
        passengersPanel.add(searchBarPanel, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(passengerTable);
        passengersPanel.add(scrollPane, BorderLayout.CENTER);

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchText = searchField.getText();
                searchInTable("GetPassengerDetails", passengerTable, searchText);
            }
        });

        return passengersPanel;
    }

    private JPanel createFlightsPanel() {
        JPanel flightsPanel = createPanel();

        JTable flightsTable = createTable();
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

        // Add search bar components
        JPanel searchBarPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JTextField searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");
        searchBarPanel.add(searchField);
        searchBarPanel.add(searchButton);
        flightsPanel.add(searchBarPanel, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(flightsTable);
        flightsPanel.add(scrollPane, BorderLayout.CENTER);

        // Attach search button listener
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchText = searchField.getText();
                searchInTable("GetFlightDetails", flightsTable, searchText);
            }
        });

        return flightsPanel;
    }
    private JPanel createStaffPanel() {
        JPanel staffPanel = createPanel();
        JTable staffTable = createTable();

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

        // Add search bar components
        JPanel searchBarPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JTextField searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");
        searchBarPanel.add(searchField);
        searchBarPanel.add(searchButton);
        staffPanel.add(searchBarPanel, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(staffTable);
        staffPanel.add(scrollPane, BorderLayout.CENTER);

        // Attach search button listener
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchText = searchField.getText();
                searchInTable("GetStaffDetails", staffTable, searchText);
            }
        });

        return staffPanel;
    }

    private void searchInTable(String procedure, JTable table, String searchText) {
        try {
            String url = "jdbc:mysql://127.0.0.1:3306/airportdb";
            String username = "root";
            String password = "dbsproject:(";
            Connection conn = DriverManager.getConnection(url, username, password);

            // Corrected call string with procedure name and input parameter placeholder
            String call = "{call " + procedure + "(?)}"; // Note the correct concatenation

            // Prepare the call to the stored procedure
            CallableStatement pstmt = conn.prepareCall(call);
            pstmt.setString(1, searchText);
            ResultSet rs = pstmt.executeQuery();

            table.setModel(DbUtils.resultSetToTableModel(rs));

            rs.close();
            pstmt.close();
            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

        public static void main(String[] args) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    AdminView adminView = new AdminView();
                    adminView.setVisible(true);
                }
            });
        }
}

