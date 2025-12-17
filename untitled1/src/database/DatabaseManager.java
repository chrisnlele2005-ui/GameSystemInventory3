package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {
    private static final String URL = "jdbc:mysql://localhost:3306/game_inventory";
    private static final String USER = "root";
    private static final String PASSWORD = "7233";

    static {
        // Valgfrit: indlæs driver (kan hjælpe i nogle miljøer)
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            // Ikke fatal hvis driver håndteres af classpath (men log det)
            System.err.println("JDBC driver ikke fundet automatisk: " + e.getMessage());
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}

