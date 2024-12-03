import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HomePageGUI extends JFrame {
    public HomePageGUI() {
        setTitle("Home Page");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        JButton addIncomeButton = new JButton("Add Income");
        addIncomeButton.setBounds(100, 50, 200, 40);
        JButton addExpenseButton = new JButton("Add Expense");
        addExpenseButton.setBounds(100, 120, 200, 40);

        add(addIncomeButton);
        add(addExpenseButton);

        addIncomeButton.addActionListener(e -> {
            dispose(); // Close the HomePage
            new AddIncomeGUI();
        });

        addExpenseButton.addActionListener(e -> {
            dispose(); // Close the HomePage
            new AddExpenseGUI();
        });

        setVisible(true);
    }
}
