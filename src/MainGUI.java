import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class MainGUI extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private String userEmail;

    public MainGUI(String userEmail) {
        this.userEmail = userEmail;
        setTitle("Main Dashboard - Expenzo");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Sidebar with buttons
        JPanel sidebarPanel = new JPanel(new GridLayout(7, 1, 5, 5));
        sidebarPanel.setBackground(new Color(35, 0, 245));
        sidebarPanel.setPreferredSize(new Dimension(200, 800));

        JButton dashboardButton = createSidebarButton("Dashboard");
        JButton profileButton = createSidebarButton("Profile");
        JButton addIncomeButton = createSidebarButton("Add Income");
        JButton addExpenseButton = createSidebarButton("Add Expense");
        JButton expenseReportButton = createSidebarButton("Expense Report");
        JButton logoutButton = createSidebarButton("Logout");

        // Add buttons to sidebar
        sidebarPanel.add(new JLabel("EXPENZO", JLabel.CENTER));
        sidebarPanel.add(dashboardButton);
        sidebarPanel.add(profileButton);
        sidebarPanel.add(addIncomeButton);
        sidebarPanel.add(addExpenseButton);
        sidebarPanel.add(expenseReportButton);
        sidebarPanel.add(logoutButton);

        // Main content panel with CardLayout
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Create panels for each screen
        JPanel dashboardPanel = createDashboardPanel();
        JPanel profilePanel = createProfilePanel();
        JPanel addIncomePanel = createAddIncomePanel();
        JPanel addExpensePanel = createAddExpensePanel();
        JPanel expenseReportPanel = createExpenseReportPanel();

        // Add panels to mainPanel
        mainPanel.add(dashboardPanel, "Dashboard");
        mainPanel.add(profilePanel, "Profile");
        mainPanel.add(addIncomePanel, "Add Income");
        mainPanel.add(addExpensePanel, "Add Expense");
        mainPanel.add(expenseReportPanel, "Expense Report");

        // Button actions to switch panels
        dashboardButton.addActionListener(e -> cardLayout.show(mainPanel, "Dashboard"));
        profileButton.addActionListener(e -> cardLayout.show(mainPanel, "Profile"));
        addIncomeButton.addActionListener(e -> cardLayout.show(mainPanel, "Add Income"));
        addExpenseButton.addActionListener(e -> cardLayout.show(mainPanel, "Add Expense"));
        expenseReportButton.addActionListener(e -> cardLayout.show(mainPanel, "Expense Report"));

        // Logout button action
        logoutButton.addActionListener(e -> {
            // Dispose the current window and show login screen
            dispose();
            new LoginGUI(); // Redirect to login screen
        });

        // Add components to frame
        add(sidebarPanel, BorderLayout.WEST);
        add(mainPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    // Helper method to create sidebar buttons
    private JButton createSidebarButton(String text) {
        JButton button = new JButton(text);
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(64, 0, 128));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        return button;
    }

    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10));

        // Title for Dashboard
        JLabel titleLabel = new JLabel("Dashboard", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(titleLabel);

        // Fetch total income, total expense, and remaining amount from the database
        double totalIncome = getTotalIncome();
        double totalExpense = getTotalExpense();
        double remainingAmount = totalIncome - totalExpense;
        // Display the financial data
        JLabel incomeLabel = new JLabel("Total Income: " + String.format("%.2f", totalIncome));
        JLabel expenseLabel = new JLabel("Total Expense: " + String.format("%.2f", totalExpense));
        JLabel remainingLabel = new JLabel("Remaining Amount: " + String.format("%.2f", remainingAmount));

        // Customize the labels
        incomeLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        expenseLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        remainingLabel.setFont(new Font("Arial", Font.PLAIN, 16));

        panel.add(incomeLabel);
        panel.add(expenseLabel);
        panel.add(remainingLabel);

        // Create the Refresh button
        JButton refreshButton = new JButton("Refresh");
        refreshButton.setFont(new Font("Arial", Font.BOLD, 14));
        refreshButton.setBackground(new Color(64, 0, 128));
        refreshButton.setForeground(Color.WHITE);

        // Add action listener to the refresh button
        refreshButton.addActionListener(e -> {
            // Fetch total income, total expense, and remaining amount again
            double totalInc = getTotalIncome();
            double totalExp = getTotalExpense();
            double rem = totalInc - totalExp;

            // Update the labels with the new data
            incomeLabel.setText("Total Income: " + String.format("%.2f", totalInc));
            expenseLabel.setText("Total Expense: " + String.format("%.2f", totalExp));
            remainingLabel.setText("Remaining Amount: " + String.format("%.2f", rem));
        });

        // Add the refresh button to the panel
        panel.add(refreshButton);

        return panel;
    }

    // Method to fetch total income from the database
    private double getTotalIncome() {
        double totalIncome = 0;
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT SUM(amount) FROM income WHERE user_email = ?")) {
            statement.setString(1, userEmail);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                totalIncome = resultSet.getDouble(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching total income.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return totalIncome;
    }

    // Method to fetch total expenses from the database
    private double getTotalExpense() {
        double totalExpense = 0;
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT SUM(amount) FROM expenses WHERE user_email = ?")) {
            statement.setString(1, userEmail);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                totalExpense = resultSet.getDouble(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching total expense.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return totalExpense;
    }

    // Create Profile Panel
    private JLabel nameLabel;
    private JLabel emailLabel;
    private JLabel phoneLabel;
    private JLabel addressLabel;

    private JPanel createProfilePanel() {
        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10)); // Adjust layout for better alignment

        // Initialize labels and placeholders
        panel.add(new JLabel("Name:"));
        nameLabel = new JLabel(); // Placeholder for name
        panel.add(nameLabel);

        panel.add(new JLabel("Email:"));
        emailLabel = new JLabel(); // Placeholder for email
        panel.add(emailLabel);

        panel.add(new JLabel("Phone:"));
        phoneLabel = new JLabel(); // Placeholder for phone
        panel.add(phoneLabel);

        // Fetch and display profile data from the database
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT name, email, phone, address FROM users WHERE email = ?")) {
            statement.setString(1, userEmail);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                nameLabel.setText(resultSet.getString("name"));
                emailLabel.setText(resultSet.getString("email"));
                phoneLabel.setText(resultSet.getString("phone"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching profile data.", "Error", JOptionPane.ERROR_MESSAGE);
        }

        return panel;
    }

    // Create Add Income Panel
    private JPanel createAddIncomePanel() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.add(new JLabel("Date:"));
        JTextField dateField = new JTextField();
        panel.add(dateField);

        panel.add(new JLabel("Amount:"));
        JTextField amountField = new JTextField();
        panel.add(amountField);

        panel.add(new JLabel("Description:"));
        JTextField descriptionField = new JTextField();
        panel.add(descriptionField);

        JButton saveButton = new JButton("Save");
        panel.add(saveButton);
        panel.add(new JLabel()); // Empty placeholder
        // Add functionality to save income to the database
        saveButton.addActionListener(e -> {
            String date = dateField.getText();
            String amount = amountField.getText();
            String description = descriptionField.getText();

            try (Connection connection = DBConnection.getConnection();
                 PreparedStatement statement = connection.prepareStatement("INSERT INTO income (user_email, date, amount, description) VALUES (?, ?, ?, ?)")) {
                statement.setString(1, userEmail);
                statement.setString(2, date);
                statement.setString(3, amount);
                statement.setString(4, description);
                statement.executeUpdate();
                JOptionPane.showMessageDialog(this, "Income saved successfully!");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        return panel;
    }

    // Create Add Expense Panel
    private JPanel createAddExpensePanel() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.add(new JLabel("Date:"));
        JTextField dateField = new JTextField();
        panel.add(dateField);

        panel.add(new JLabel("Amount:"));
        JTextField amountField = new JTextField();
        panel.add(amountField);

        panel.add(new JLabel("Category:"));
        JTextField categoryField = new JTextField();
        panel.add(categoryField);

        JButton saveButton = new JButton("Save");
        panel.add(saveButton);
        panel.add(new JLabel()); // Empty placeholder
        // Add functionality to save expense to the database
        saveButton.addActionListener(e -> {
            String date = dateField.getText();
            String amount = amountField.getText();
            String category = categoryField.getText();

            try (Connection connection = DBConnection.getConnection();
                 PreparedStatement statement = connection.prepareStatement("INSERT INTO expenses (user_email, date, amount, category) VALUES (?, ?, ?, ?)")) {
                statement.setString(1, userEmail);
                statement.setString(2, date);
                statement.setString(3, amount);
                statement.setString(4, category);
                statement.executeUpdate();
                JOptionPane.showMessageDialog(this, "Expense saved successfully!");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        return panel;
    }

    // Create Expense Report Panel
    private JTextArea reportArea;

    private JPanel createExpenseReportPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Add a label for the title
        JLabel titleLabel = new JLabel("Expense Report", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(titleLabel, BorderLayout.NORTH);

        // Initialize and configure reportArea
        reportArea = new JTextArea();
        reportArea.setEditable(false); // Read-only
        reportArea.setFont(new Font("Monospaced", Font.PLAIN, 14)); // Use monospaced font for better alignment
        JScrollPane scrollPane = new JScrollPane(reportArea); // Add scroll pane for long content
        panel.add(scrollPane, BorderLayout.CENTER);

        // Create the Refresh button
        JButton refreshButton = new JButton("Refresh");
        refreshButton.setFont(new Font("Arial", Font.BOLD, 14));
        refreshButton.setBackground(new Color(64, 0, 128));
        refreshButton.setForeground(Color.WHITE);

        // Add action listener to the refresh button
        refreshButton.addActionListener(e -> refreshExpenseReport());

        // Add the refresh button at the bottom of the panel
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(refreshButton);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        // Initial data fetch when the panel is first loaded
        refreshExpenseReport();

        return panel;
    }

    private void refreshExpenseReport() {
        // Fetch and display expense report data
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT category, SUM(amount) as total_amount FROM expenses WHERE user_email = ? GROUP BY category")) {
            statement.setString(1, userEmail);
            ResultSet resultSet = statement.executeQuery();

            // Build the report string
            StringBuilder report = new StringBuilder("Category\tTotal Amount\n");
            report.append("--------\t------------\n");
            int rowCount = 0;  // Debug: count rows
            while (resultSet.next()) {
                rowCount++;
                report.append(resultSet.getString("category")).append("\t")
                        .append(resultSet.getDouble("total_amount")).append("\n");
            }

            System.out.println("Number of rows fetched: " + rowCount);  // Debug log

            // If no data is found
            if (rowCount == 0) {
                report.append("No data found for this user.");
            }

            // Set the generated report to the text area
            reportArea.setText(report.toString());
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching expense report.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}