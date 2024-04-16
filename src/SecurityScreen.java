import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.formdev.flatlaf.FlatLightLaf;

public class SecurityScreen extends JFrame implements ActionListener {
    private JButton flagButton;
    private boolean isFlagged;

    public SecurityScreen() {
        // Set the FlatLaf light theme
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize FlatLaf");
        }

        // Create the security screen components
        JLabel bagIdLabel = new JLabel("Enter Bag ID:");
        bagIdLabel.setFont(new Font("Arial", Font.BOLD, 14));
        bagIdLabel.setForeground(new Color(51, 51, 51));

        JTextField bagIdField = new JTextField(20);
        bagIdField.setFont(new Font("Arial", Font.PLAIN, 14));
        bagIdField.setBorder(BorderFactory.createLineBorder(new Color(204, 204, 204), 1));

        flagButton = new JButton("Flag");
        flagButton.setFont(new Font("Arial", Font.BOLD, 14));
        flagButton.setForeground(Color.WHITE);
        flagButton.setBackground(new Color(51, 153, 255));
        flagButton.setBorderPainted(false);
        flagButton.addActionListener(this);

        // Create a panel to hold the bag ID components
        JPanel bagIdPanel = new JPanel(new BorderLayout());
        bagIdPanel.setBackground(Color.WHITE);
        bagIdPanel.add(bagIdField, BorderLayout.CENTER);
        bagIdPanel.add(flagButton, BorderLayout.EAST);

        // Create a panel to hold all components
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(243, 243, 243));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(10, 10, 10, 10);
        panel.add(bagIdLabel, gbc);
        gbc.gridy = 1;
        panel.add(bagIdPanel, gbc);

        // Set the content pane and other window properties
        setContentPane(panel);
        setTitle("Airport Security");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 200);
        setLocationRelativeTo(null); // Center the window
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == flagButton) {
            isFlagged = !isFlagged;
            if (isFlagged) {
                flagButton.setBackground(new Color(255, 102, 102));
                flagButton.setText("Unflag");
            } else {
                flagButton.setBackground(new Color(51, 153, 255));
                flagButton.setText("Flag");
            }
        }
    }
}