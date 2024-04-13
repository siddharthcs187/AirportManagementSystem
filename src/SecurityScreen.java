import javax.swing.*;
import java.awt.*;
import com.formdev.flatlaf.FlatDarkLaf;

public class SecurityScreen extends JFrame {
    public SecurityScreen() {
        // Set the FlatLaf dark theme
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize FlatLaf");
        }

        // Create the security screen components
        JLabel bagIdLabel = new JLabel("Enter Bag ID:");
        JTextField bagIdField = new JTextField(20);
        JButton flagButton = new JButton("Flag");

        // Create a panel to hold the bag ID components
        JPanel bagIdPanel = new JPanel();
        bagIdPanel.setLayout(new BoxLayout(bagIdPanel, BoxLayout.X_AXIS));
        bagIdPanel.add(Box.createHorizontalGlue()); // Add glue to center align
        bagIdPanel.add(bagIdField);
        bagIdPanel.add(flagButton);
        bagIdPanel.add(Box.createHorizontalGlue()); // Add more glue to center align

        // Create a panel to hold all components
        JPanel panel = new JPanel(new FlowLayout());
        panel.add(bagIdLabel);
        panel.add(bagIdPanel);

        // Set the content pane and other window properties
        setContentPane(panel);
        setTitle("Airport Security");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 150);
        setLocationRelativeTo(null); // Center the window
        setVisible(true);
    }
}
