package Domain;

import java.util.ArrayList;
import java.util.List;

public class BankAccount {
    private int id;
    private int customerId;
    private double balance;
    private String accountType;
    private List<String> transactionHistory;

    public BankAccount(int customerId, double balance, String accountType) {
        this.customerId = customerId;
        this.accountType = accountType;
        this.balance = balance;
        this.transactionHistory = new ArrayList<>();
    }

    // Deposit method
    public void deposit(double amount) {
        if (amount <= 0) {
            System.out.println("Deposit amount must be positive.");
            return;
        }
        balance += amount;
        transactionHistory.add("Deposited: $" + amount);
    }

    // Withdraw method
    public void withdraw(double amount) {
        if (amount <= 0) {
            System.out.println("Withdrawal amount must be positive.");
            return;
        }
        if (amount > balance) {
            System.out.println("Insufficient funds.");
            return;
        }
        balance -= amount;
        transactionHistory.add("Withdrew: $" + amount);
    }

    public double checkBalance() {
        return balance;
    }

    public List<String> getTransactionHistory() {
        return transactionHistory;
    }

    // Getters and Setters
    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public int getCustomerId() { return customerId; }

    public void setCustomerId(int customerId) { this.customerId = customerId; }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getAccountType() { return accountType; }

    public double getBalance() { return balance; }

    public void setBalance(double balance) { this.balance = balance; }

    public String getAccountNumber() {

        return null;
    }
}
