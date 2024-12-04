import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class SignupGUI extends JFrame {
    public SignupGUI() {
        setTitle("Signup");
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
        JPanel centerPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        JLabel nameLabel = new JLabel("Name:");
        JTextField nameField = new JTextField();
        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField();
        JLabel questionLabel = new JLabel("Security Question:");
        JTextField questionField = new JTextField();
        JLabel answerLabel = new JLabel("Answer:");
        JTextField answerField = new JTextField();

        centerPanel.add(nameLabel);
        centerPanel.add(nameField);
        centerPanel.add(emailLabel);
        centerPanel.add(emailField);
        centerPanel.add(passwordLabel);
        centerPanel.add(passwordField);
        centerPanel.add(questionLabel);
        centerPanel.add(questionField);
        centerPanel.add(answerLabel);
        centerPanel.add(answerField);

        // South Panel for Buttons
        JPanel southPanel = new JPanel();
        JButton submitButton = new JButton("Submit");
        JButton backButton = new JButton("Back to Login");

        southPanel.add(submitButton);
        southPanel.add(backButton);

        add(northPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);

        submitButton.addActionListener(e -> {
            String name = nameField.getText();
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());
            String question = questionField.getText();
            String answer = answerField.getText();
            if (registerUser(name, email, password, question, answer)) {
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

    private boolean registerUser(String name, String email, String password, String question, String answer) {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO users (name, email, password, security_question, security_answer) VALUES (?, ?, ?, ?, ?)")) {

            statement.setString(1, name);
            statement.setString(2, email);
            statement.setString(3, password);
            statement.setString(4, question);
            statement.setString(5, answer);
            statement.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
