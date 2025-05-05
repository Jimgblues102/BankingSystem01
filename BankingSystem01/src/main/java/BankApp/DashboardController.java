package BankApp;



import Dao.TransactionDAO;
import Utils.ThemeManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import Domain.BankAccount;
import Domain.Transaction;
import Domain.Customer;
import Dao.BankAccountDAO;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

//import static BankApp.Session.currentUser;

public class DashboardController {

    @FXML private Label welcomeLabel;
    @FXML private ListView<BankAccount> accountListView;
    @FXML private Label accountNumberLabel;
    @FXML private Label balanceLabel;
    @FXML private Label typeLabel;
    @FXML private TableView<Transaction> transactionTable;
    @FXML private TableColumn<Transaction, String> dateColumn;
    @FXML private TableColumn<Transaction, String> typeColumn;
    @FXML private TableColumn<Transaction, Double> amountColumn;
    @FXML private Customer currentUser;
    List<BankAccount> accounts;

    @FXML
    public void initialize() {
        currentUser = Session.getCurrentUser();
        welcomeLabel.setText("Welcome, " + currentUser.getUsername());


        // TODO: Populate accountListView and transactionTable based on selected account
        try {
            BankAccountDAO dao = new BankAccountDAO();
            accounts = dao.getAccountsByCustomerId(currentUser.getId());
            ObservableList<BankAccount> accountObservableList = FXCollections.observableArrayList(accounts);
            accountListView.setItems(accountObservableList);
            accountNumberLabel.setText(String.valueOf(currentUser.getAccountNumber()));
            // a custom cell factory to show account type
            accountListView.setCellFactory(listView -> new ListCell<>() {
                @Override
                protected void updateItem(BankAccount account, boolean empty) {
                    super.updateItem(account, empty);
                    if (empty || account == null) {
                        setText(null);
                    } else {
                        setText( account.getAccountType() );
                    }
                }
            });

            // Listen for account selection changes
            accountListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal != null) {
                    balanceLabel.setText(String.format("$%.2f", newVal.getBalance()));
                    typeLabel.setText(newVal.getAccountType());
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML private void handleLogout() {
        try {
            Stage stage = (Stage) welcomeLabel.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.setTitle("Bank Login");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCreateAccount() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/createAccount.fxml"));
            Parent root = loader.load();

            // Get controller and pass the logged-in customer
            CreateAccountController controller = loader.getController();
            controller.setCustomer(currentUser); // Make sure currentCustomer is initialized in this controller

            Stage stage = new Stage();
            stage.setTitle("Create New Account");
            stage.initModality(Modality.APPLICATION_MODAL); // Block input to other windows
            stage.setScene(ThemeManager.createThemedScene(root));
            stage.showAndWait();

            // Optional: refresh account list after new account is created
            loadCustomerAccounts(); // You should have a method like this to repopulate accountListView

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void loadCustomerAccounts() {
        try {
            // Assuming currentCustomer is the logged-in customer
            List<BankAccount> accounts = BankAccountDAO.getAccountsByCustomerId(currentUser.getId()); // Fetch accounts for the logged-in customer

            // Create an ObservableList to populate the ListView
            ObservableList<BankAccount> accountList = FXCollections.observableArrayList(accounts);
            accountListView.setItems(accountList); // Set the items of the ListView
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void handleViewTransactions() {
        // Get selected account from the ListView
        BankAccount selectedAccount = accountListView.getSelectionModel().getSelectedItem();

        if (selectedAccount == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Account Selected");
            alert.setHeaderText(null);
            alert.setContentText("Please select an account to view its transactions.");
            alert.showAndWait();
            return;
        }

        try {
            TransactionDAO transactionDAO = new TransactionDAO();


            int accountId = selectedAccount.getId();

            // transactions for selected account
            List<Transaction> transactions = transactionDAO.getTransactionsByAccountId(accountId);

            // Wrap in ObservableList for TableView
            ObservableList<Transaction> transactionObservableList = FXCollections.observableArrayList(transactions);


            dateColumn.setCellValueFactory(new PropertyValueFactory<>("timestamp"));
            typeColumn.setCellValueFactory(new PropertyValueFactory<>("details"));
            amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));

            // Set data into the table
            transactionTable.setItems(transactionObservableList);

        } catch (Exception e) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Error");
            errorAlert.setHeaderText("Failed to load transactions");
            errorAlert.setContentText(e.getMessage());
            errorAlert.showAndWait();

            e.printStackTrace();
        }
    }



    @FXML
    private void handleDepositWithdraw() {
        BankAccount selectedAccount = accountListView.getSelectionModel().getSelectedItem();

        if (selectedAccount == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Account Selected");
            alert.setHeaderText(null);
            alert.setContentText("Please select an account first.");
            alert.showAndWait();
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/deposit_Withdraw.fxml"));
            Parent root = loader.load();

            // Get the controller and pass the selected account ID
            Deposit_WithdrawController controller = loader.getController();
            controller.setSelectedAccountId(selectedAccount.getId());

            Stage stage = new Stage();
            stage.setTitle("Deposit / Withdraw");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(ThemeManager.createThemedScene(root));
            stage.showAndWait();

            // After closing the deposit/withdraw window, refresh account info
            loadCustomerAccounts(); // Re-fetch account list to update balances
            accountListView.getSelectionModel().select(selectedAccount); // Re-select current account
            balanceLabel.setText(String.format("$%.2f", selectedAccount.getBalance())); // Update balance

        } catch (Exception e) {
            e.printStackTrace();
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Error");
            errorAlert.setHeaderText("Could not open deposit/withdraw window");
            errorAlert.setContentText(e.getMessage());
            errorAlert.showAndWait();
        }
    }


    @FXML
    private void handleTransferFunds() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/transferFunds.fxml"));
            Parent root = loader.load();

            // Optional: pass currentUser if needed
            TransferFundsController controller = loader.getController();
            controller.setCurrentUser(currentUser); // only if required

            Stage stage = new Stage();
            stage.setTitle("Transfer Funds");
            stage.setScene(ThemeManager.createThemedScene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
