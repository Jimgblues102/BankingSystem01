package BankApp;

import Dao.BankAccountDAO;
import Dao.TransactionDAO;
import Domain.BankAccount;
import Domain.Customer;
import Domain.Transaction;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.List;

public class CreateAccountController {

    @FXML private ComboBox<String> accountTypeComboBox;
    @FXML private TextField initialDepositField;
    @FXML private Label statusLabel;

    private Customer currentCustomer; // Set by DashboardController before showing this window

    public void setCustomer(Customer customer) {
        this.currentCustomer = customer;
    }

    @FXML
    private void handleCreateAccount() {
        String accountType = accountTypeComboBox.getValue();
        String depositText = initialDepositField.getText();

        // Input validation
        if (accountType == null || accountType.isEmpty()) {
            statusLabel.setText("Please select an account type.");
            return;
        }

        double initialDeposit;
        try {
            initialDeposit = Double.parseDouble(depositText);
            if (initialDeposit < 0) {
                statusLabel.setText("Deposit must be a positive number.");
                return;
            }
        } catch (NumberFormatException e) {
            statusLabel.setText("Invalid deposit amount.");
            return;
        }

        // Create account
        BankAccount newAccount = new BankAccount(currentCustomer.getId(),initialDeposit,accountType);

        BankAccountDAO dao = new BankAccountDAO();
        boolean success = dao.save(newAccount);

        if (success) {
            try {
                List<BankAccount> list = dao.getAccountsByCustomerId(currentCustomer.getId());
                for(BankAccount bankAccount : list){
                    if(bankAccount.getCustomerId() == currentCustomer.getId()){
                        Transaction transaction = new Transaction(bankAccount.getId(), "deposit",depositText);
                        TransactionDAO transactionDAO = new TransactionDAO();
                        transactionDAO.save(transaction);
                    }
                }
            }catch(Exception e){
                e.printStackTrace();
            }
            closeWindow();
        } else {
            statusLabel.setText("Failed to create account. Try again.");
        }
    }

    @FXML
    private void handleCancel() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) accountTypeComboBox.getScene().getWindow();
        stage.close();
    }
}

