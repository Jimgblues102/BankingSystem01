package Dao;

import Domain.Customer;
import DBConnect.DBConnection;

import java.sql.*;

public class CustomerDAO {

    public static void save(Customer customer) {
        String sql = "INSERT INTO customer (name, username, password, account_number) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, customer.getName());
            stmt.setString(2, customer.getUsername());
            stmt.setString(3, customer.getPassword());
            stmt.setLong(4, customer.getAccountNumber());
            stmt.executeUpdate();

            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                customer.setId(keys.getInt(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Customer findByUsername(String username) {
        String sql = "SELECT * FROM customer WHERE username = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Customer customer = new Customer(
                        rs.getString("name"),
                        rs.getString("username"),
                        rs.getString("password")
                );
                customer.setAccountNumber(rs.getLong("account_number"));
                customer.setId(rs.getInt("id"));
                return customer;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


    public static Customer findByAccountNumber(long accountNumber) {
        String sql = "SELECT * FROM customer WHERE account_number = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, accountNumber);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Customer customer = new Customer(
                        rs.getString("name"),
                        rs.getString("username"),
                        rs.getString("password")
                );
                customer.setAccountNumber(rs.getLong("account_number"));
                customer.setId(rs.getInt("id"));
                return customer;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
