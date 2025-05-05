package BankApp;

import Dao.BankAccountDAO;
import Dao.CustomerDAO;
import Dao.TransactionDAO;
import Domain.BankAccount;
import Domain.Customer;
import Domain.Transaction;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.sql.SQLException;
import java.util.List;



public class TransferFundsController {

    @FXML
    private TextField recipientAccountField;

    @FXML
    private TextField amountField;

    @FXML
    private Label messageLabel;

    private Customer currentUser;

    private TransactionDAO transactionDAO = new TransactionDAO();
    Customer customer;
    CustomerDAO customerDAO = new CustomerDAO();
    public void setCurrentUser(Customer user) {
        this.currentUser = user;
    }

    @FXML
    private void handleTransfer() {
        String recipientAccount = recipientAccountField.getText().trim();
        String amountText = amountField.getText().trim();

        if (recipientAccount.isEmpty() || amountText.isEmpty()) {
            messageLabel.setText("All fields are required.");
            return;
        }

        try {
            long recipientAccNum = Long.parseLong(recipientAccount);
            double amount = Double.parseDouble(amountText);
            customer = customerDAO.findByAccountNumber(recipientAccNum);

            // TODO: Validate and perform transfer using DAO/Service layer
            BankAccountDAO bankAccountDAO = new BankAccountDAO();
            bankAccountDAO.transferFunds(currentUser.getId(), customer.getId(), amount);


            senderTransactionLog(currentUser.getId(), recipientAccNum, amount);
            receiverTransactionLog(String.valueOf(currentUser.getAccountNumber()), recipientAccNum, amount);

            messageLabel.setStyle("-fx-text-fill: green;");
            messageLabel.setText("Transfer successful.");
        } catch (NumberFormatException e) {
            messageLabel.setText("Invalid account number or amount.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void senderTransactionLog(int senderAcc, long receiverAcc, double amount) {
        BankAccountDAO dao = new BankAccountDAO();
        String details = "Transfer to " + receiverAcc;
        try {
            List<BankAccount> senderAcc_ID = dao.getAccountsByCustomerId(senderAcc);
            for (BankAccount bankAccount : senderAcc_ID) {
                if (currentUser.getId() == bankAccount.getCustomerId()) {
                    Transaction transaction = new Transaction();
                    transactionDAO.transferLog(bankAccount.getId(), details, amount);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void receiverTransactionLog(String senderAcc, long receiverAcc, double amount) {
        BankAccountDAO dao = new BankAccountDAO();
        String details = "Transfer from " + senderAcc;
        try {
            customer = customerDAO.findByAccountNumber(receiverAcc);
            List<BankAccount> receiverAcc_ID = dao.getAccountsByCustomerId(customer.getId());
            for (BankAccount bankAccount : receiverAcc_ID) {
                if (customer.getId() == bankAccount.getCustomerId()) {
                    transactionDAO.transferLog(bankAccount.getId(), details, amount);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}