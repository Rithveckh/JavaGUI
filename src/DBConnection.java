import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    // Database URL, username, and password
    private static final String URL = "jdbc:mysql://localhost:3306/incomeexpensemanagement";  // Replace 'iems' with your actual database name
    private static final String USER = "root";  // Replace with your MySQL username
    private static final String PASSWORD = "Rithu@18/24";  // Replace with your MySQL password

    // Method to establish a connection to the database
    public static Connection getConnection() {
        try {
            // Load MySQL driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Return the connection to the database
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();  // Print any connection errors for debugging
            return null;
        }
    }

    // Test the database connection (you can remove this later)
    public static void main(String[] args) {
        try (Connection connection = getConnection()) {
            if (connection != null) {
                System.out.println("Database connection successful!");
            } else {
                System.out.println("Failed to connect to the database.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
