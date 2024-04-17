import javax.swing.*;
import java.awt.*;
import com.formdev.flatlaf.FlatDarkLaf;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;

import net.proteanit.sql.DbUtils;

class Passenger {
    private String firstName;
    private String lastName;
    private Date dob;
    private String privilege;
    private String mobileNo;
    private boolean redFlag;
    private int weight;
    private boolean isSecure;
    private boolean isFragile;
    private boolean priority;
    private boolean lost;

    // Constructor
    public Passenger(String firstName, String lastName, Date dob, String privilege, String mobileNo,
                     boolean redFlag, int weight, boolean isSecure, boolean isFragile, boolean priority, boolean lost) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dob = dob;
        this.privilege = privilege;
        this.mobileNo = mobileNo;
        this.redFlag = redFlag;
        this.weight = weight;
        this.isSecure = isSecure;
        this.isFragile = isFragile;
        this.priority = priority;
        this.lost = lost;
    }

    // Getters
    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public java.sql.Date getDob() {
        return (java.sql.Date) dob;
    }

    public String getPrivilege() {
        return privilege;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public boolean isRedFlag() {
        return redFlag;
    }

    public int getWeight() {
        return weight;
    }

    public boolean isSecure() {
        return isSecure;
    }

    public boolean isFragile() {
        return isFragile;
    }

    public boolean isPriority() {
        return priority;
    }

    public boolean isLost() {
        return lost;
    }

    // toString method for printing
    @Override
    public String toString() {
        return "Passenger{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", dob=" + dob +
                ", privilege='" + privilege + '\'' +
                ", mobileNo='" + mobileNo + '\'' +
                ", redFlag=" + redFlag +
                ", weight=" + weight +
                ", isSecure=" + isSecure +
                ", isFragile=" + isFragile +
                ", priority=" + priority +
                ", lost=" + lost +
                '}';
    }
}

class CSVParser {
    public static List<Passenger> readCSV(String csvFile) throws IOException {
//        String csvFile = "C:\\Users\\sunda\\Downloads\\dummy.csv";
        String line;
        String cvsSplitBy = ",";

        List<Passenger> passengers = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            // Skip the header line
            br.readLine();

            while ((line = br.readLine()) != null) {
                String[] data = line.split(cvsSplitBy);

                // Parse CSV data and create Passenger objects
                Date dob = Date.valueOf(data[2]);
                boolean redFlag = Boolean.parseBoolean(data[5]);
                int weight = Integer.parseInt(data[6]);
                boolean isSecure = Boolean.parseBoolean(data[7]);
                boolean isFragile = Boolean.parseBoolean(data[8]);
                boolean priority = Boolean.parseBoolean(data[9]);
                boolean lost = Boolean.parseBoolean(data[10]);

                Passenger passenger = new Passenger(data[0], data[1], dob, data[3], data[4], redFlag, weight,
                        isSecure, isFragile, priority, lost);

                passengers.add(passenger);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Print passenger details
        for (Passenger passenger : passengers) {
            System.out.println(passenger);
        }
        return passengers;
    }
}



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
        JPanel incomingFlightsPanel = createIncomingFlightsPanel();
        JPanel outgoingFlightsPanel = createOutgoingFlightsPanel();
        JPanel staffPanel = createStaffPanel();
        JPanel inventoryPanel = createInventoryPanel();
        JPanel assetsPanel = createAssetsPanel();
        JPanel airlinePanel = createAirlinesPanel();
        JPanel flaggedPassPanel = createFlaggedPassengersPanel();
        JPanel flaggedBagsPanel = createFlaggedBagsPanel();
        JPanel salesPanel = createSalesPanel();
        JPanel addFlightsPanel = addFlight();

        // Add panels to the tabbed pane
        tabbedPane.addTab("Baggage", baggagePanel);
        tabbedPane.addTab("Passengers", passengersPanel);
        tabbedPane.addTab("Incoming Flights", incomingFlightsPanel);
        tabbedPane.addTab("Outgoing Flights", outgoingFlightsPanel);
        tabbedPane.addTab("Staff", staffPanel);
        tabbedPane.addTab("Inventory",inventoryPanel);
        tabbedPane.addTab("Assets",assetsPanel);
        tabbedPane.addTab("Airline",airlinePanel);
        tabbedPane.addTab("Flagged Passengers", flaggedPassPanel);
        tabbedPane.addTab("Flagged Bags", flaggedBagsPanel);
        tabbedPane.addTab("Sales", salesPanel);
        tabbedPane.addTab("Add Flight", addFlightsPanel);
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

    private JPanel addFlight() {
        JPanel addFlightPanel = createPanel();

        // Create input fields for flight details
        JTextField fileNameField = new JTextField(20);
        JTextField airlineNameField = new JTextField(20);
        JTextField flightCodeField = new JTextField(20);
        JTextField dateTimeField = new JTextField(20);
        JTextField originField = new JTextField(20);
        JTextField destinationField = new JTextField(20);
        JTextField baggageBeltField = new JTextField(10);
        JTextField checkinLaneField = new JTextField(10);
        JTextField statusField = new JTextField(20);
        JTextField isArrivingField = new JTextField(20);

        // Set maximum height for input fields
        fileNameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        airlineNameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        flightCodeField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        dateTimeField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        originField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        destinationField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        baggageBeltField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        checkinLaneField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        statusField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        isArrivingField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        // Create labels for input fields
        JLabel flightNameLabel = new JLabel("Enter Flight Name:");
        JLabel airlineNameLabel = new JLabel("Enter Airline Name:");
        JLabel flightCodeLabel = new JLabel("Enter IATA Code:");
        JLabel dateTimeLabel = new JLabel("Enter Date Time:");
        JLabel originLabel = new JLabel("Enter Origin:");
        JLabel destinationLabel = new JLabel("Enter Destination:");
        JLabel baggageBeltLabel = new JLabel("Enter Baggage Belt:");
        JLabel checkinLaneLabel = new JLabel("Enter Check-in Lane:");
        JLabel statusLabel = new JLabel("Enter Status:");
        JLabel isArrivingLabel = new JLabel("isArriving :");

        // Create a button for adding the flight
        JButton addButton = new JButton("Add Flight");

        // Create a layout for the panel
        GroupLayout layout = new GroupLayout(addFlightPanel);
        addFlightPanel.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        // Set up the horizontal group
        GroupLayout.SequentialGroup hGroup = layout.createSequentialGroup();
        hGroup.addGroup(layout.createParallelGroup()
                .addComponent(flightNameLabel)
                .addComponent(airlineNameLabel)
                .addComponent(flightCodeLabel)
                .addComponent(dateTimeLabel)
                .addComponent(originLabel)
                .addComponent(destinationLabel)
                .addComponent(baggageBeltLabel)
                .addComponent(checkinLaneLabel)
                .addComponent(statusLabel)
                .addComponent(isArrivingLabel));
        hGroup.addGroup(layout.createParallelGroup()
                .addComponent(fileNameField)
                .addComponent(airlineNameField)
                .addComponent(flightCodeField)
                .addComponent(dateTimeField)
                .addComponent(originField)
                .addComponent(destinationField)
                .addComponent(baggageBeltField)
                .addComponent(checkinLaneField)
                .addComponent(statusField)
                .addComponent(isArrivingField)
                .addComponent(addButton));
        layout.setHorizontalGroup(hGroup);

        // Set up the vertical group
        GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(flightNameLabel)
                .addComponent(fileNameField));
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(airlineNameLabel)
                .addComponent(airlineNameField));
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(flightCodeLabel)
                .addComponent(flightCodeField));
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(dateTimeLabel)
                .addComponent(dateTimeField));
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(originLabel)
                .addComponent(originField));
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(destinationLabel)
                .addComponent(destinationField));
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(baggageBeltLabel)
                .addComponent(baggageBeltField));
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(checkinLaneLabel)
                .addComponent(checkinLaneField));
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(statusLabel)
                .addComponent(statusField));
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(isArrivingLabel)
                .addComponent(isArrivingField));
        vGroup.addComponent(addButton);
        layout.setVerticalGroup(vGroup);

        // Add action listener for the add button
// Add action listener for the add button
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement your logic here for adding the flight using the input values
                String fileName = fileNameField.getText();
                String airlineName = airlineNameField.getText();
                String flightCode = flightCodeField.getText();
                String dateTime = dateTimeField.getText();
                String origin = originField.getText();
                String destination = destinationField.getText();
                int baggageBelt = Integer.parseInt(baggageBeltField.getText());
                int checkinLane = Integer.parseInt(checkinLaneField.getText());
                String status = statusField.getText();
                boolean isArriving = Boolean.parseBoolean(isArrivingField.getText());
                CSVParser csv = new CSVParser();
                List<Passenger> lp = null;
                try {
                    lp = csv.readCSV(fileName);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

                Connection conn = null;
                try {
                    String url = "jdbc:mysql://localhost:3306/airportdb";
                    String username = "root";
                    String password = "siddharth";
                    conn = DriverManager.getConnection(url, username, password);
                    conn.setAutoCommit(false); // Start transaction

                    // Add the flight
                    String proc = "AddFlightAndAirline";
                    String call = "{call " + proc + "(?,?,?,?,?,?,?,?,?)}";
                    PreparedStatement ps = conn.prepareStatement(call);
                    ps.setString(1, flightCode);
                    ps.setString(2, airlineName);
                    ps.setString(3, dateTime);
                    ps.setBoolean(4, isArriving);
                    ps.setString(5, origin);
                    ps.setString(6, destination);
                    ps.setInt(7, baggageBelt);
                    ps.setInt(8, checkinLane);
                    ps.setString(9, status);
                    ps.executeUpdate();

                    // Fetch the maximum flight number
                    String getMaxFlightNoQuery = "SELECT MAX(Flight_No) AS MaxFlightNo FROM flights";
                    PreparedStatement getMaxFlightNoStmt = conn.prepareStatement(getMaxFlightNoQuery);
                    ResultSet maxFlightNoResult = getMaxFlightNoStmt.executeQuery();
                    int maxFlightNo = 0;
                    if (maxFlightNoResult.next()) {
                        maxFlightNo = maxFlightNoResult.getInt("MaxFlightNo");
                    }
                    maxFlightNoResult.close();
                    getMaxFlightNoStmt.close();

                    // Add passengers and bags
                    String proc1 = "AddPassengerOnFlight";
                    String proc2 = "AddBag";
                    String call1 = "{call " + proc1 + "(?,?,?,?,?,?)}";
                    String call2 = "{call " + proc2 + "(?,?,?,?,?)}";
                    for (Passenger p : lp) {
                        PreparedStatement pstmt = conn.prepareStatement(call1);
                        pstmt.setString(1, p.getFirstName());
                        pstmt.setString(2, p.getLastName());
                        pstmt.setDate(3, p.getDob());
                        pstmt.setInt(4, maxFlightNo);
                        pstmt.setString(5, p.getPrivilege());
                        pstmt.setString(6, p.getMobileNo());
                        pstmt.executeUpdate();

                        // Fetch the maximum passenger ID
                        String getMaxPassengerIDQuery = "SELECT MAX(Pass_ID) AS MaxPassengerID FROM passengers";
                        PreparedStatement getMaxPassengerIDStmt = conn.prepareStatement(getMaxPassengerIDQuery);
                        ResultSet maxPassengerIDResult = getMaxPassengerIDStmt.executeQuery();
                        int maxPassengerID = 0;
                        if (maxPassengerIDResult.next()) {
                            maxPassengerID = maxPassengerIDResult.getInt("MaxPassengerID");
                        }
                        maxPassengerIDResult.close();
                        getMaxPassengerIDStmt.close();

                        PreparedStatement pstmt2 = conn.prepareStatement(call2);
                        pstmt2.setInt(1, maxPassengerID);
                        pstmt2.setInt(2, p.getWeight());
                        pstmt2.setBoolean(3, p.isFragile());
                        pstmt2.setBoolean(4, p.isPriority());
                        pstmt2.setInt(5, maxFlightNo);
                        pstmt2.executeUpdate();
                    }

                    conn.commit(); // Commit transaction
                    JOptionPane.showMessageDialog(null, "Flight and passengers added successfully");
                } catch (Exception err) {
                    try {
                        if (conn != null) {
                            conn.rollback(); // Rollback transaction if an error occurs
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                    err.printStackTrace();
                } finally {
                    try {
                        if (conn != null) {
                            conn.setAutoCommit(true); // Restore auto-commit mode
                            conn.close(); // Close connection
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        return addFlightPanel;
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

    private JPanel createIncomingFlightsPanel() {
        JPanel flightsPanel = createPanel();

        JTable flightsTable = createTable();
        try {
            String url = "jdbc:mysql://127.0.0.1:3306/airportdb";
            String username = "root";
            String password = "dbsproject:(";
            Connection conn = DriverManager.getConnection(url, username, password);

            String query = "SELECT * FROM flights where is_Arriving = 1";
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

    private JPanel createOutgoingFlightsPanel() {
        JPanel flightsPanel = createPanel();

        JTable flightsTable = createTable();
        try {
            String url = "jdbc:mysql://127.0.0.1:3306/airportdb";
            String username = "root";
            String password = "dbsproject:(";
            Connection conn = DriverManager.getConnection(url, username, password);

            String query = "SELECT * FROM flights where is_Arriving = 0";
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

    private JPanel createFlaggedPassengersPanel() {
        JPanel passengersPanel = createPanel();
        JTable passengerTable = createTable();
        try {
            String url = "jdbc:mysql://localhost:3306/airportdb";
            String username = "root";
            String password = "siddharth";
            Connection conn = DriverManager.getConnection(url, username, password);
            String proc = "GetPassengersWithRedFlag";
            String call = "{call " + proc + "()}"; // Remove the searchText parameter


            // Prepare the call to the stored procedure
            CallableStatement pstmt = conn.prepareCall(call);
            Statement stmt = conn.createStatement();
            ResultSet rs = pstmt.executeQuery();


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


    private JPanel createSalesPanel() {
        JPanel salesPanel = createPanel();
        JTable salesTable = createTable();
        try {
            String url = "jdbc:mysql://localhost:3306/airportdb";
            String username = "root";
            String password = "siddharth";
            Connection conn = DriverManager.getConnection(url, username, password);
            String proc = "GetSales";
            String call = "{call " + proc + "()}"; // Remove the searchText parameter


            // Prepare the call to the stored procedure
            CallableStatement pstmt = conn.prepareCall(call);
            Statement stmt = conn.createStatement();
            ResultSet rs = pstmt.executeQuery();


            salesTable.setModel(DbUtils.resultSetToTableModel(rs));


            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        JScrollPane scrollPane = new JScrollPane(salesTable);
        salesPanel.add(scrollPane, BorderLayout.CENTER);


        return salesPanel;
    }


    private JPanel createFlaggedBagsPanel() {
        JPanel bagsPanel = createPanel();
        JTable bagsTable = createTable();
        try {
            String url = "jdbc:mysql://localhost:3306/airportdb";
            String username = "root";
            String password = "siddharth";
            Connection conn = DriverManager.getConnection(url, username, password);
            String proc = "GetInsecureBags";
            String call = "{call " + proc + "()}"; // Remove the searchText parameter


            // Prepare the call to the stored procedure
            CallableStatement pstmt = conn.prepareCall(call);
            Statement stmt = conn.createStatement();
            ResultSet rs = pstmt.executeQuery();


            bagsTable.setModel(DbUtils.resultSetToTableModel(rs));


            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        JScrollPane scrollPane = new JScrollPane(bagsTable);
        bagsPanel.add(scrollPane, BorderLayout.CENTER);


        return bagsPanel;
    }



    private JPanel createInventoryPanel() {
        JPanel inventoryPanel = createPanel(); // Assuming createPanel() method creates a JPanel

        JTable inventoryTable = createTable(); // Assuming createTable() method creates a JTable
        try {
            String url = "jdbc:mysql://127.0.0.1:3306/airportdb";
            String username = "root";
            String password = "dbsproject:(";
            Connection conn = DriverManager.getConnection(url, username, password);

            String query = "SELECT * FROM inventory";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            inventoryTable.setModel(DbUtils.resultSetToTableModel(rs));

            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        JScrollPane scrollPane = new JScrollPane(inventoryTable);
        inventoryPanel.add(scrollPane, BorderLayout.CENTER);

        return inventoryPanel;
    }

    private JPanel createAssetsPanel() {
        JPanel assetsPanel = createPanel(); // Assuming createPanel() method creates a JPanel

        JTable assetsTable = createTable(); // Assuming createTable() method creates a JTable
        try {
            String url = "jdbc:mysql://127.0.0.1:3306/airportdb";
            String username = "root";
            String password = "dbsproject:(";
            Connection conn = DriverManager.getConnection(url, username, password);

            String query = "SELECT * FROM ground_assets";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            assetsTable.setModel(DbUtils.resultSetToTableModel(rs));

            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        JScrollPane scrollPane = new JScrollPane(assetsTable);
        assetsPanel.add(scrollPane, BorderLayout.CENTER);

        return assetsPanel;
    }

    private JPanel createAirlinesPanel() {
        JPanel airlinesPanel = createPanel(); // Assuming createPanel() method creates a JPanel

        JTable airlinesTable = createTable(); // Assuming createTable() method creates a JTable
        try {
            String url = "jdbc:mysql://127.0.0.1:3306/airportdb";
            String username = "root";
            String password = "dbsproject:(";
            Connection conn = DriverManager.getConnection(url, username, password);

            String query = "SELECT * FROM airlines";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            airlinesTable.setModel(DbUtils.resultSetToTableModel(rs));

            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        JScrollPane scrollPane = new JScrollPane(airlinesTable);
        airlinesPanel.add(scrollPane, BorderLayout.CENTER);

        return airlinesPanel;
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

