import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginGUI extends JFrame {
    public LoginGUI() {
        setTitle("Login");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // North Panel for Logo and Title
        JPanel northPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("Welcome to Expenzo", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        JLabel logoPlaceholder = new JLabel("Logo Here", JLabel.CENTER);
        logoPlaceholder.setPreferredSize(new Dimension(100, 100));

        northPanel.add(logoPlaceholder, BorderLayout.NORTH);
        northPanel.add(titleLabel, BorderLayout.SOUTH);

        // Center Panel for Input Fields
        JPanel centerPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        JLabel emailLabel = new JLabel("Email Address:");
        JTextField emailField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField();
        JButton loginButton = new JButton("Login");
        JButton forgotPasswordButton = new JButton("Forgot Password?");
        centerPanel.add(emailLabel);
        centerPanel.add(emailField);
        centerPanel.add(passwordLabel);
        centerPanel.add(passwordField);
        centerPanel.add(loginButton);
        centerPanel.add(forgotPasswordButton);

        // South Panel for Signup Button
        JPanel southPanel = new JPanel();
        JButton signupButton = new JButton("Don't have an account yet? Sign up.");
        southPanel.add(signupButton);

        add(northPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);

        loginButton.addActionListener(e -> {
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());
            if (loginUser(email, password)) {
                JOptionPane.showMessageDialog(this, "Login Successful!");
            } else {
                JOptionPane.showMessageDialog(this, "Invalid email or password.");
            }
        });

        forgotPasswordButton.addActionListener(e -> {
            dispose();
            new ForgotPasswordGUI();
        });

        signupButton.addActionListener(e -> {
            dispose();
            new SignupGUI();
        });

        setVisible(true);
    }

    private boolean loginUser(String email, String password) {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE email = ? AND password = ?")) {

            statement.setString(1, email);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                boolean isProfileComplete = resultSet.getBoolean("is_profile_complete");
//                JOptionPane.showMessageDialog(this, "Profile Complete: " + isProfileComplete);  // Debugging line
                if (!isProfileComplete) {
                    // Redirect to Profile Screen
                    dispose();
                    new ProfileGUI(email);  // Pass email to ProfileGUI constructor
                } else {
                    // Redirect to Main Dashboard
                    dispose();
                    new MainGUI(email);  // Pass email to MainGUI constructor
                }
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void main(String[] args) {
        new LoginGUI();
    }
}
