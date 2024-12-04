import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ForgotPasswordGUI extends JFrame {
    public ForgotPasswordGUI() {
        setTitle("Forgot Password");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(3, 1));

        JLabel emailLabel = new JLabel("Enter your email:");
        JTextField emailField = new JTextField();
        JButton nextButton = new JButton("Next");
        add(emailLabel);
        add(emailField);
        add(nextButton);

        nextButton.addActionListener(e -> {
            String email = emailField.getText();
            String question = getSecurityQuestion(email);
            if (question != null) {
                dispose();
                new VerifyAnswerGUI(email, question);
            } else {
                JOptionPane.showMessageDialog(this, "Email not found!");
            }
        });

        setVisible(true);
    }

    private String getSecurityQuestion(String email) {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT security_question FROM users WHERE email = ?")) {

            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("security_question");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

class VerifyAnswerGUI extends JFrame {
    public VerifyAnswerGUI(String email, String question) {
        setTitle("Verify Answer");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(4, 1));

        JLabel questionLabel = new JLabel("Security Question: " + question);
        JLabel answerLabel = new JLabel("Your Answer:");
        JTextField answerField = new JTextField();
        JButton submitButton = new JButton("Submit");

        add(questionLabel);
        add(answerLabel);
        add(answerField);
        add(submitButton);

        submitButton.addActionListener(e -> {
            String answer = answerField.getText();
            String password = getPassword(email, answer);
            if (password != null) {
                JOptionPane.showMessageDialog(this, "Your password is: " + password);
                dispose();
                new LoginGUI();
            } else {
                JOptionPane.showMessageDialog(this, "Incorrect answer!");
            }
        });

        setVisible(true);
    }

    private String getPassword(String email, String answer) {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT password FROM users WHERE email = ? AND security_answer = ?")) {

            statement.setString(1, email);
            statement.setString(2, answer);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("password");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}