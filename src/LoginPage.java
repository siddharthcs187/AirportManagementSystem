import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.formdev.flatlaf.FlatDarkLaf;

public class LoginPage extends JFrame implements ActionListener {

    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginPage() {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize FlatDarkLaf look and feel");
        }

        // Set up the frame
        setTitle("Login");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the frame

        JPanel contentPane = new JPanel(new GridLayout(3, 2, 10, 10));
        contentPane.setBackground(new Color(60, 63, 65)); // Set a dark background color
        contentPane.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Create UI components
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setForeground(Color.WHITE); // Set label text color to white
        usernameField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setForeground(Color.WHITE); // Set label text color to white
        passwordField = new JPasswordField();
        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(this);

        // Add components to the content pane
        contentPane.add(usernameLabel);
        contentPane.add(usernameField);
        contentPane.add(passwordLabel);
        contentPane.add(passwordField);
        contentPane.add(new JLabel());
        contentPane.add(loginButton);

        // Set the content pane for the frame
        setContentPane(contentPane);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Get the username and password from the text fields
        String username = usernameField.getText();
        char[] passwordChars = passwordField.getPassword();
        String password = new String(passwordChars);

        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter user name", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter password", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (username.equals("admin") && password.equals("admin_password")) {
            // Close the login page
            dispose();

            // Open the admin page
            SwingUtilities.invokeLater(() -> {
                AdminView adminView = new AdminView();
                adminView.setVisible(true);
            });
        }else if (username.equals("security") && password.equals("security_password")){
            // Close the login page
            dispose();

            // Open the security page
            SwingUtilities.invokeLater(() -> {
                SecurityScreen securityScreen = new SecurityScreen();
                securityScreen.setVisible(true);
            });
        }else {
            JOptionPane.showMessageDialog(this, "Invalid username or password", "Login Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginPage loginPage = new LoginPage();
            loginPage.setVisible(true);
        });
    }
}