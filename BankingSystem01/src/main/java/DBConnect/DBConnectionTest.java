package DBConnect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnectionTest {

    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/bankdb"; // replace with your DB name
        String user = "postgres"; // replace with your DB username
        String password = "JimGene@9875"; // replace with your DB password

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.println("✅ Connected to PostgreSQL database!");
        } catch (SQLException e) {
            System.out.println("❌ Connection failed!");
            e.printStackTrace();
        }
    }
}

