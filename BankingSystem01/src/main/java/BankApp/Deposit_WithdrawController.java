package BankApp;

import Dao.BankAccountDAO;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class Deposit_WithdrawController {

    @FXML private ComboBox<String> transactionTypeBox;
    @FXML private TextField amountField;
    @FXML private Label messageLabel;

    private int selectedAccountId;

    public void setSelectedAccountId(int accountId) {
        this.selectedAccountId = accountId;
    }

    @FXML
    private void handleTransaction() {
        String type = transactionTypeBox.getValue();
        String amountText = amountField.getText();

        if (type == null || amountText.isEmpty()) {
            messageLabel.setText("Please select type and enter amount.");
            return;
        }

        try {
            double amount = Double.parseDouble(amountText);
            if (amount <= 0) {
                messageLabel.setText("Amount must be greater than zero.");
                return;
            }

            BankAccountDAO dao = new BankAccountDAO();

            if ("Deposit".equals(type)) {
                boolean success = dao.depositToAccount(selectedAccountId, amount);
                messageLabel.setText(success ? "Deposited " + amount + " successfully." : "Account not found.");
            } else if ("Withdraw".equals(type)) {
                boolean success = dao.withdrawFromAccount(selectedAccountId, amount);
                messageLabel.setText(success ? "Withdrew " + amount + " successfully." : "Insufficient funds or account not found.");
            } else {
                messageLabel.setText("Invalid transaction type.");
            }

        } catch (NumberFormatException e) {
            messageLabel.setText("Invalid amount.");
        } catch (Exception e) {
            e.printStackTrace();
            messageLabel.setText("Transaction failed due to system error.");
        }
    }
}
