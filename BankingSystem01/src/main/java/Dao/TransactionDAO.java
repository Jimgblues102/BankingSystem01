package Dao;

import Domain.Transaction;
import DBConnect.DBConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static DBConnect.DBConnection.getConnection;

public class TransactionDAO {

    public void save(Transaction transaction) {
        String sql = "INSERT INTO transaction (account_id, details, timestamp, amount) VALUES (?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, transaction.getAccountId());
            stmt.setString(2, transaction.getDetails());
            stmt.setTimestamp(3, Timestamp.valueOf(transaction.getTimestamp()));
            stmt.setString(4, transaction.getAmount());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Add this method inside your TransactionDAO class
    public List<Transaction> getTransactionsByAccountId(int accountId) {
        List<Transaction> transactions = new ArrayList<>();

        String query = """
        SELECT t.id, t.account_id, t.details, t.timestamp, t.amount
        FROM transaction t
        INNER JOIN bank_account a ON t.account_id = a.id
        WHERE t.account_id = ?
        ORDER BY t.timestamp DESC
    """;

        try (
                Connection conn = getConnection(); // Make sure this utility class works
                PreparedStatement stmt = conn.prepareStatement(query)
        ) {
            stmt.setInt(1, accountId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Transaction transaction = new Transaction();


                    transaction.setId(rs.getInt("id"));
                    transaction.setAccountId(rs.getInt("account_id"));
                    transaction.setDetails(rs.getString("details"));
                    transaction.setTimestamp(rs.getTimestamp("timestamp").toLocalDateTime());
                    transaction.setAmount(rs.getString("amount")); // Or BigDecimal if you change the type

                    transactions.add(transaction);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error retrieving transactions for account ID " + accountId, e);
        }

        return transactions;
    }

    public void transferLog(int account_id, String details, double amount) throws Exception {
        String logSQL = "INSERT INTO transaction (account_id, details, amount, timestamp) VALUES (?, ?, ?, ?)";
        Connection connection = getConnection();

        try {
            // Disable auto-commit so we can manually control the transaction
            connection.setAutoCommit(false);

            try (PreparedStatement logStmt = connection.prepareStatement(logSQL)) {
                logStmt.setInt(1, account_id);
                logStmt.setString(2, details);
                logStmt.setDouble(3, amount);

                // ❗️Fix: Store timestamp as a proper SQL Timestamp instead of string
                logStmt.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));

                logStmt.executeUpdate();

                // ✅ Commit if everything is successful
                connection.commit();
            } catch (SQLException e) {
                // ❗️Fix: Only rollback if there was an error inside the inner try-block
                connection.rollback();
                throw e;
            }
        } finally {
            // ✅ Always restore auto-commit mode
            connection.setAutoCommit(true);

            // ❗️Fix: Close the connection if it's not managed elsewhere
            // To prevent resource leak
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        }
    }
}

