package Domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Customer {
    private int id;
    private String name;
    private long accountNumber;
    private String username;
    private String password;
    private List<BankAccount> accounts = new ArrayList<>();

    public Customer(String name, String username, String password) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.accountNumber = generateAccountNumber();
    }

    // Getters
    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getName() { return name; }

    public String getUsername() { return username; }

    public boolean checkPassword(String inputPassword) {
        return this.password.equals(inputPassword);
    }

    public List<BankAccount> getAccounts() { return accounts; }

    public void addAccount(BankAccount account) {
        accounts.add(account);
    }

    public String getPassword() {
        return password;
    }

    public long getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(long accountNumber) {
        this.accountNumber = accountNumber;
    }

    public static long generateAccountNumber() {
        Random random = new Random();
        // Minimum 12-digit number: 100000000000
        // Maximum 12-digit number: 999999999999
        return 100_000_000_000L + (long)(random.nextDouble() * 899_999_999_999L);
    }
}
