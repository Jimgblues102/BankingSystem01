package Dao;

import Domain.BankAccount;
import DBConnect.DBConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BankAccountDAO {


    public boolean save(BankAccount account) {
        String sql = "INSERT INTO bank_account (customer_id, account_type, balance) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, account.getCustomerId());
            stmt.setString(2, account.getAccountType());
            stmt.setDouble(3, account.getBalance());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                return false; // insert failed
            }

            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                account.setId(keys.getInt(1)); // assign generated ID back to account
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }


    public void updateBalance(BankAccount account) {
        String sql = "UPDATE bank_account SET balance = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, account.getBalance());
            stmt.setInt(2, account.getId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<BankAccount> getAccountsByCustomerId(int customerId) {
        List<BankAccount> accounts = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM bank_account WHERE customer_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, customerId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                BankAccount acc = new BankAccount(
                        rs.getInt("customer_id"),
                        rs.getDouble("balance"),
                        rs.getString("account_type")
                );
                acc.setId(rs.getInt("id"));
                accounts.add(acc);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return accounts;
    }

    public boolean depositToAccount(int accountId, double amount) {
        String updateQuery = "UPDATE bank_account SET balance = balance + ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(updateQuery)) {

            stmt.setDouble(1, amount);
            stmt.setInt(2, accountId);

            if (stmt.executeUpdate() > 0) {
                logTransaction(conn, accountId, "Deposit", amount);
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean withdrawFromAccount(int accountId, double amount) {
        String checkQuery = "SELECT balance FROM bank_account WHERE id = ?";
        String updateQuery = "UPDATE bank_account SET balance = balance - ? WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {

            checkStmt.setInt(1, accountId);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                double currentBalance = rs.getDouble("balance");

                if (amount > currentBalance) return false;

                try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                    updateStmt.setDouble(1, amount);
                    updateStmt.setInt(2, accountId);

                    if (updateStmt.executeUpdate() > 0) {
                        logTransaction(conn, accountId, "Withdraw", amount);
                        return true;
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // 👇 Shared private method to log transaction
    private void logTransaction(Connection conn, int accountId, String type, double amount) throws SQLException {
        String insertQuery = "INSERT INTO transaction (account_id, details, amount) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(insertQuery)) {
            stmt.setInt(1, accountId);
            stmt.setString(2, type);
            stmt.setDouble(3, amount);
            stmt.executeUpdate();
        }
    }

    public boolean transferFunds(int senderId, int receiverId, double amount) throws SQLException {
        if (amount <= 0) return false;

        String deductSQL = "UPDATE bank_account SET balance = balance - ? WHERE customer_id = ? AND balance >= ? AND account_type = ?";
        String addSQL = "UPDATE bank_account SET balance = balance + ? WHERE customer_id = ? AND account_type = ?";

        // Debugging output
        System.out.println("Attempting transfer: Sender ID: " + senderId + " Receiver ID: " + receiverId + " Amount: " + amount);

        Connection connection = DBConnection.getConnection();
        try {
            connection.setAutoCommit(false);

            // Deduct from sender
            try (PreparedStatement deductStmt = connection.prepareStatement(deductSQL)) {
                deductStmt.setDouble(1, amount);
                deductStmt.setInt(2, senderId);
                deductStmt.setDouble(3, amount);
                deductStmt.setString(4, "Cheque");
                int updated = deductStmt.executeUpdate();
                if (updated == 0) {
                    throw new SQLException("Sender not found or insufficient funds.");
                }
            }

            // Add to receiver
            try (PreparedStatement addStmt = connection.prepareStatement(addSQL)) {
                addStmt.setDouble(1, amount);
                addStmt.setInt(2, receiverId);
                addStmt.setString(3, "Cheque");
                int updated = addStmt.executeUpdate();
                if (updated == 0) {
                    throw new SQLException("Receiver account not found.");
                }
            }

            connection.commit();
            return true;

        } catch (SQLException e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(true);
        }
    }
}

