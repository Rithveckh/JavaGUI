import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class ProfileGUI extends JFrame {
    private String userEmail;

    public ProfileGUI(String email) {
        this.userEmail = email;
        setTitle("Set Up Profile - Expenzo");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // North Panel for Logo and Title
        JPanel northPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("Set Up Your Profile", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        northPanel.add(titleLabel, BorderLayout.CENTER);

        // Center Panel for Profile Fields
        JPanel centerPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        JLabel dobLabel = new JLabel("Date of Birth:");
        JTextField dobField = new JTextField();
        JLabel phoneLabel = new JLabel("Phone Number:");
        JTextField phoneField = new JTextField();
        JLabel recoveryEmailLabel = new JLabel("Recovery Email:");
        JTextField recoveryEmailField = new JTextField();
        JLabel incomeTargetLabel = new JLabel("Monthly Income Target:");
        JTextField incomeTargetField = new JTextField();
        JLabel expenditureLabel = new JLabel("Monthly Expected Expenditure:");
        JTextField expenditureField = new JTextField();
        JButton saveButton = new JButton("Save Profile");
        JButton skipButton = new JButton("Skip");

        centerPanel.add(dobLabel);
        centerPanel.add(dobField);
        centerPanel.add(phoneLabel);
        centerPanel.add(phoneField);
        centerPanel.add(recoveryEmailLabel);
        centerPanel.add(recoveryEmailField);
        centerPanel.add(incomeTargetLabel);
        centerPanel.add(incomeTargetField);
        centerPanel.add(expenditureLabel);
        centerPanel.add(expenditureField);
        centerPanel.add(saveButton);
        centerPanel.add(skipButton);

        // South Panel for Additional Buttons
        JPanel southPanel = new JPanel();
        JButton cancelButton = new JButton("Cancel");
        southPanel.add(cancelButton);

        add(northPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);

        // Disable skip button until profile is complete
        skipButton.setEnabled(false); // Disable skip initially

        saveButton.addActionListener(e -> {
            String dob = dobField.getText();
            String phone = phoneField.getText();
            String recoveryEmail = recoveryEmailField.getText();
            String incomeTarget = incomeTargetField.getText();
            String expenditure = expenditureField.getText();

            // Check if all profile fields are filled before enabling Skip button
            if (dob.isEmpty() || phone.isEmpty() || recoveryEmail.isEmpty() || incomeTarget.isEmpty() || expenditure.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all the fields before saving the profile.");
            } else {
                if (saveUserProfile(dob, phone, recoveryEmail, incomeTarget, expenditure)) {
                    JOptionPane.showMessageDialog(this, "Profile saved successfully!");
                    skipButton.setEnabled(true); // Enable the skip button after saving the profile
                    dispose();
//                    new MainGUI(userEmail);  // Redirect to Main Dashboard
                } else {
                    JOptionPane.showMessageDialog(this, "Error saving profile. Please try again.");
                }
            }
        });

        skipButton.addActionListener(e -> {
            // Prevent skipping without completing the profile
            JOptionPane.showMessageDialog(this, "You must complete your profile setup before proceeding to the main screen.");
        });

        cancelButton.addActionListener(e -> {
            // Cancel and go back to the login screen or close the application
            dispose();
            new LoginGUI(); // Or close the app
        });

        setVisible(true);
    }

    private boolean saveUserProfile(String dob, String phone, String recoveryEmail, String incomeTarget, String expenditure) {
        try (Connection connection = DBConnection.getConnection()) {
            String updateQuery = "UPDATE users SET dob = ?, phone = ?, recovery_email = ?, income_target = ?, expenditure = ?, is_profile_complete = ? WHERE email = ?";
            PreparedStatement statement = connection.prepareStatement(updateQuery);
            statement.setString(1, dob);
            statement.setString(2, phone);
            statement.setString(3, recoveryEmail);
            statement.setString(4, incomeTarget);
            statement.setString(5, expenditure);
            statement.setBoolean(6, true);  // Set profile complete flag
            statement.setString(7, userEmail);
            int rowsUpdated = statement.executeUpdate();

            // If the profile is updated successfully, show the message and redirect to the MainGUI
            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(this, "Profile updated successfully!");
                dispose();
                new MainGUI(userEmail);  // Go to Main Dashboard
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
