import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class SignupGUI extends JFrame {
    public SignupGUI() {
        setTitle("SignUp");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(4, 2));

        JLabel nameLabel = new JLabel("Name:");
        JTextField nameField = new JTextField();
        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField();
        JButton submitButton = new JButton("Submit");
        JButton backButton = new JButton("Back to Login");

        add(nameLabel);
        add(nameField);
        add(emailLabel);
        add(emailField);
        add(passwordLabel);
        add(passwordField);
        add(submitButton);
        add(backButton);

        submitButton.addActionListener(e -> {
            String name = nameField.getText();
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());
            if (registerUser(name, email, password)) {
                JOptionPane.showMessageDialog(this, "Signup Successful! Please Login.");
                dispose();
                new LoginGUI();
            } else {
                JOptionPane.showMessageDialog(this, "Error: Email already exists.");
            }
        });

        backButton.addActionListener(e -> {
            dispose();
            new LoginGUI();
        });

        setVisible(true);
    }

    private boolean registerUser(String name, String email, String password) {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement("INSERT INTO users (name, email, password) VALUES (?, ?, ?)")) {

            statement.setString(1, name);
            statement.setString(2, email);
            statement.setString(3, password);
            statement.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
