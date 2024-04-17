import javax.swing.*;
import java.awt.*;
import com.formdev.flatlaf.FlatLightLaf;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import net.proteanit.sql.DbUtils;

public class AdminView extends JFrame {
    private JTabbedPane tabbedPane;

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

        // Create a tabbed pane with top tabs
        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.setBackground(new Color(243, 243, 243));
        tabbedPane.setForeground(new Color(51, 51, 51));

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
                // Perform search operation based on searchText on the bags table
                // Update baggageTable if needed
            }
        });

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
                String passengerQuery = "SELECT * FROM passengers WHERE first_name LIKE ?";

                try {
                    String url = "jdbc:mysql://127.0.0.1:3306/airportdb";
                    String username = "root";
                    String password = "dbsproject:(";
                    Connection conn = DriverManager.getConnection(url, username, password);

                    // Corrected call string with procedure name and input parameter placeholder
                    String procedure = "GetPassengerDetails";
                    String call = "{call " + procedure + "(?)}"; // Note the correct concatenation

                    // Prepare the call to the stored procedure
                    CallableStatement pstmt = conn.prepareCall(call);
                    pstmt.setString(1, searchText);
                    ResultSet rs = pstmt.executeQuery();

                    passengerTable.setModel(DbUtils.resultSetToTableModel(rs));

                    rs.close();
                    pstmt.close();
                    conn.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

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
                // Perform search operation based on searchText on the passengers table
                // Update passengerTable if needed
            }
        });

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
                searchInTable(staffTable, "staff", searchText);
            }
        });

        return staffPanel;
    }

    private void searchInTable(JTable table, String tableName, String searchText) {
        try {
            String url = "jdbc:mysql://127.0.0.1:3306/airportdb";
            String username = "root";
            String password = "dbsproject:(";
            Connection conn = DriverManager.getConnection(url, username, password);

            String query = "SELECT * FROM " + tableName + " WHERE column_name LIKE ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, "%" + searchText + "%");
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

