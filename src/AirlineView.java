import javax.swing.*;
import java.awt.*;
import com.formdev.flatlaf.FlatDarkLaf;

public class AirlineView extends JFrame {
    private JTabbedPane tabbedPane;
    private String airline;

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
        JPanel crewPanel = createCrewPanel();
        JPanel maintenancePanel = createMaintenancePanel();
        JPanel baggagePanel = createBaggagePanel();

        // Add panels to the tabbed pane
        tabbedPane.addTab("Flights", flightsPanel);
        tabbedPane.addTab("Crew", crewPanel);
        tabbedPane.addTab("Maintenance", maintenancePanel);
        tabbedPane.addTab("Baggage", baggagePanel);

        // Add the tabbed pane to the content pane
        contentPane.add(tabbedPane, BorderLayout.CENTER);

        // Set the content pane for the frame
        setContentPane(contentPane);
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

    private JPanel createCrewPanel() {
        // Create a panel for the crew view
        JPanel crewPanel = new JPanel();
        crewPanel.setBackground(new Color(36, 36, 36));
        crewPanel.setForeground(Color.WHITE);

        // Add UI components for the crew view here
        // e.g., JTable, JButtons, etc.

        return crewPanel;
    }

    private JPanel createMaintenancePanel() {
        // Create a panel for the maintenance view
        JPanel maintenancePanel = new JPanel();
        maintenancePanel.setBackground(new Color(36, 36, 36));
        maintenancePanel.setForeground(Color.WHITE);

        // Add UI components for the maintenance view here
        // e.g., JTable, JButtons, etc.

        return maintenancePanel;
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AirlineView airlineView = new AirlineView("Emirates");
            airlineView.setVisible(true);
        });
    }
}