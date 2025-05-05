package Domain;

import java.time.LocalDateTime;

public class Transaction {
    private int id;
    private int accountId;
    private String details;

    private  String amount;
    private LocalDateTime timestamp;

    public Transaction(int accountId, String details, String amount) {
        this.accountId = accountId;
        this.details = details;
        this.timestamp = LocalDateTime.now();
        this.amount = amount;
    }

    public Transaction() {

    }

    // Getters
    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public int getAccountId() { return accountId; }

    public String getDetails() { return details; }

    public LocalDateTime getTimestamp() { return timestamp; }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
