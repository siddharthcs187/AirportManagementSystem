import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

import com.formdev.flatlaf.FlatDarkLaf;
import net.proteanit.sql.DbUtils;

public class PassengerView extends JFrame {
    private JTabbedPane tabbedPane;
    private int passenger;

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
            String password = "mysql@1704";
            Connection conn = DriverManager.getConnection(url, username, password);

            String procedure = "GetShops";
            String call = "{call " + procedure + "()}";

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

    private JPanel createStatusPanel() {
        JPanel shopsPanel = createPanel();

        JTable shopsTable = createTable();
        try {
            String url = "jdbc:mysql://localhost:3306/airportdb";
            String username = "root";
            String password = "mysql@1704";
            Connection conn = DriverManager.getConnection(url, username, password);

            String procedure = "GetFlightStatus";
            String call = "{call " + procedure + "(?)}";

            CallableStatement stmt = conn.prepareCall(call);
            stmt.setInt(1, passenger);
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

        JTextField shopIdField = new JTextField(20);
        JTextField amountField = new JTextField(10);

        JLabel shopIdLabel = new JLabel("Shop ID:");
        JLabel amountLabel = new JLabel("Amount:");

        JButton buyButton = new JButton("Buy");
        buyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String shopId = shopIdField.getText();
                String amount = amountField.getText();
                try{
                    String url = "jdbc:mysql://localhost:3306/airportdb";
                    String username = "root";
                    String password = "mysql@1704";
                    Connection conn = DriverManager.getConnection(url, username, password);

                    String procedure = "InsertShoppingOrder";
                    String call = "{call " + procedure + "(?,?,?)}";
                    CallableStatement stmt = conn.prepareCall(call);
                    stmt.setString(1, shopId);
                    stmt.setInt(2, passenger);
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

        GroupLayout layout = new GroupLayout(passengerDetailsPanel);
        passengerDetailsPanel.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        GroupLayout.SequentialGroup hGroup = layout.createSequentialGroup();
        hGroup.addGroup(layout.createParallelGroup()
                .addComponent(shopIdLabel)
                .addComponent(amountLabel)
                .addComponent(buyButton));
        hGroup.addGroup(layout.createParallelGroup()
                .addComponent(shopIdField)
                .addComponent(amountField));
        layout.setHorizontalGroup(hGroup);

        GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(shopIdLabel)
                .addComponent(shopIdField));
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(amountLabel)
                .addComponent(amountField));
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(buyButton));
        layout.setVerticalGroup(vGroup);

        return passengerDetailsPanel;
    }


    public PassengerView(int passenger) {
        this.passenger = passenger;

        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize FlatLaf");
        }

        setTitle("Airport Management System - Passenger View");
        setExtendedState(JFrame.MAXIMIZED_BOTH); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); 

        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setBackground(new Color(36, 36, 36));

        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.setBackground(new Color(36, 36, 36));
        tabbedPane.setForeground(Color.WHITE);

        JPanel shopsPanel = createShopsPanel();
        JPanel passengerDetailsPanel = createPassengerDetailsPanel();
        JPanel lostBagPanel = createLostBagPanel();
        JPanel statusPanel = createStatusPanel();


        tabbedPane.addTab("Shops", shopsPanel);
        tabbedPane.addTab("Place Order", passengerDetailsPanel);
        tabbedPane.addTab("Lost Bag", lostBagPanel);
        tabbedPane.addTab("Flight Status", statusPanel);


        contentPane.add(tabbedPane, BorderLayout.CENTER);

        setContentPane(contentPane);
    }
    private JPanel createLostBagPanel() {
        JPanel passengerDetailsPanel = createPanel();


        JTextField bagIdField1 = new JTextField(20);
        bagIdField1.setFont(new Font("Arial", Font.PLAIN, 14));
        bagIdField1.setBorder(BorderFactory.createLineBorder(new Color(204, 204, 204), 1));


        JLabel bagIdLabel1 = new JLabel("Lost Bag's ID:");
        bagIdLabel1.setFont(new Font("Arial", Font.BOLD, 14));
        bagIdLabel1.setForeground(new Color(255, 255, 255));


        JButton flagBagButton = new JButton("Lost");
        flagBagButton.setFont(new Font("Arial", Font.BOLD, 14));
        flagBagButton.setForeground(Color.WHITE);
        flagBagButton.setBackground(new Color(200, 50, 50));
        flagBagButton.setBorderPainted(false);


        flagBagButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLostAction(bagIdField1.getText());
                bagIdField1.setText("");
            }
        });


        GroupLayout layout = new GroupLayout(passengerDetailsPanel);
        passengerDetailsPanel.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);


        GroupLayout.SequentialGroup hGroup = layout.createSequentialGroup();
        hGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(bagIdLabel1)
                .addComponent(bagIdField1)
                .addComponent(flagBagButton)
        );
        layout.setHorizontalGroup(hGroup);


        GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(bagIdLabel1)
        );
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(bagIdField1)
        );
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(flagBagButton)
        );
        layout.setVerticalGroup(vGroup);


        return passengerDetailsPanel;
    }




    private void handleLostAction(String bagId) {
        System.out.println("Lost bag ID: " + bagId);
        try {
            String url = "jdbc:mysql://localhost:3306/airportdb";
            String username = "root";
            String password = "mysql@1704";
            Connection conn = DriverManager.getConnection(url, username, password);
            String proc = "LostBag";
            String call = "{call " + proc + "(?)}";




            CallableStatement pstmt = conn.prepareCall(call);
            pstmt.setString(1, bagId);
            Statement stmt = conn.createStatement();
            ResultSet rs = pstmt.executeQuery();
            JOptionPane.showMessageDialog(this, "Lost Bag reported successfully.");


            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Bag couldn't be reported as Lost.");
            e.printStackTrace();
        }
    }

}
