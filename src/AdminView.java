import javax.swing.*;
import java.awt.*;
import com.formdev.flatlaf.FlatDarkLaf;

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
        // Create a panel for the baggage view
        JPanel baggagePanel = new JPanel();
        baggagePanel.setBackground(new Color(36, 36, 36));
        baggagePanel.setForeground(Color.WHITE);

        // Add UI components for the baggage view here
        // e.g., JTable, JButtons, etc.

        return baggagePanel;
    }

    private JPanel createPassengersPanel() {
        // Create a panel for the passengers view
        JPanel passengersPanel = new JPanel();
        passengersPanel.setBackground(new Color(36, 36, 36));
        passengersPanel.setForeground(Color.WHITE);

        // Add UI components for the passengers view here
        // e.g., JTable, JButtons, etc.

        return passengersPanel;
    }

    private JPanel createFlightsPanel() {
        // Create a panel for the flights view
        JPanel flightsPanel = new JPanel();
        flightsPanel.setBackground(new Color(36, 36, 36));
        flightsPanel.setForeground(Color.WHITE);

        // Add UI components for the flights view here
        // e.g., JTable, JButtons, etc.

        return flightsPanel;
    }

    private JPanel createStaffPanel() {
        // Create a panel for the staff view
        JPanel staffPanel = new JPanel();
        staffPanel.setBackground(new Color(36, 36, 36));
        staffPanel.setForeground(Color.WHITE);

        // Add UI components for the staff view here
        // e.g., JTable, JButtons, etc.

        return staffPanel;
    }
}