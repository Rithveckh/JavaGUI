import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class AddExpenseGUI extends JFrame {
    public AddExpenseGUI() {
        setTitle("Add Expense");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        JLabel dateLabel = new JLabel("Date:");
        dateLabel.setBounds(50, 30, 100, 30);
        JTextField dateField = new JTextField();
        dateField.setBounds(150, 30, 200, 30);

        JLabel amountLabel = new JLabel("Amount:");
        amountLabel.setBounds(50, 70, 100, 30);
        JTextField amountField = new JTextField();
        amountField.setBounds(150, 70, 200, 30);

        JLabel categoryLabel = new JLabel("Category:");
        categoryLabel.setBounds(50, 110, 100, 30);
        String[] categories = {"Food", "Rent", "Travel", "Other"};
        JComboBox<String> categoryDropdown = new JComboBox<>(categories);
        categoryDropdown.setBounds(150, 110, 200, 30);

        JLabel descriptionLabel = new JLabel("Description:");
        descriptionLabel.setBounds(50, 150, 100, 30);
        JTextField descriptionField = new JTextField();
        descriptionField.setBounds(150, 150, 200, 30);

        JButton submitButton = new JButton("Submit");
        submitButton.setBounds(150, 200, 100, 30);

        add(dateLabel);
        add(dateField);
        add(amountLabel);
        add(amountField);
        add(categoryLabel);
        add(categoryDropdown);
        add(descriptionLabel);
        add(descriptionField);
        add(submitButton);

        submitButton.addActionListener(e -> {
            String date = dateField.getText();
            String amount = amountField.getText();
            String category = (String) categoryDropdown.getSelectedItem();
            String description = descriptionField.getText();

            if (addExpenseToDatabase(date, amount, category, description)) {
                JOptionPane.showMessageDialog(this, "Expense added successfully!");
                dispose();
                new HomePageGUI();
            } else {
                JOptionPane.showMessageDialog(this, "Error adding expense.");
            }
        });

        setVisible(true);
    }

    private boolean addExpenseToDatabase(String date, String amount, String category, String description) {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement("INSERT INTO expenses (date, amount, category, description) VALUES (?, ?, ?, ?)")) {

            statement.setString(1, date);
            statement.setString(2, amount);
            statement.setString(3, category);
            statement.setString(4, description);
            statement.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
