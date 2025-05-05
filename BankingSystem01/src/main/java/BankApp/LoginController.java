package BankApp;

import Domain.Customer;
import Dao.CustomerDAO;
import Utils.ThemeManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.*;
import Utils.ConfigHelper;

import java.util.List;

public class LoginController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label statusLabel;

    // 👇 Register fields (updated)
    @FXML private TextField registerName;
    @FXML private TextField registerUsername;
    @FXML private PasswordField registerPassword;
    @FXML private Label registerStatus;

    @FXML
    private Button themeToggleBtn;

    private boolean isDarkMode;

    @FXML
    public void initialize() {
        String savedTheme = ConfigHelper.loadTheme();
        isDarkMode = "dark".equals(savedTheme);

        Scene scene = themeToggleBtn.getScene();
        if (scene != null) {
            applyTheme(scene);
        } else {
            // If scene not yet initialized, delay until it is
            themeToggleBtn.sceneProperty().addListener((obs, oldScene, newScene) -> {
                if (newScene != null) {
                    applyTheme(newScene);
                }
            });
        }
    }

    @FXML
    public void toggleTheme() {
        isDarkMode = !isDarkMode;
        ConfigHelper.saveTheme(isDarkMode ? "dark" : "light");

        Scene scene = themeToggleBtn.getScene();
        if (scene != null) {
            applyTheme(scene);
            List<String> stylesheets = scene.getStylesheets();
            for (String path : stylesheets) {
                System.out.println("Current theme path: " + path);
                ThemeManager.setCurrentTheme(path);
            }
        }

    }

    private void applyTheme(Scene scene) {
        scene.getStylesheets().clear();
        if (isDarkMode) {
            scene.getStylesheets().add(getClass().getResource("/styles/dark-theme.css").toExternalForm());
        } else {
            scene.getStylesheets().add(getClass().getResource("/styles/light-theme.css").toExternalForm());
        }
    }

    @FXML
    private void handleLogin() {
        String user = usernameField.getText();
        String pass = passwordField.getText();

        Customer customer = CustomerDAO.findByUsername(user);

        if (customer != null && customer.getPassword().equals(pass)) {
            Session.setCurrentUser(customer);
            openDashboard();
        } else {
            statusLabel.setText("Invalid login");
        }
    }

    @FXML
    private void handleRegister() {
        String name = registerName.getText();
        String user = registerUsername.getText();
        String pass = registerPassword.getText();

        if (CustomerDAO.findByUsername(user) == null) {
            // 👇 Use name instead of email now
            Customer newCustomer = new Customer(name, user, pass);
            CustomerDAO.save(newCustomer);
            registerStatus.setText("Registered successfully!");
        } else {
            registerStatus.setText("Username already exists.");
        }
    }

    private void openDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/dashboard.fxml"));
            //Scene dashboardScene = new Scene(loader.load());
            Scene dashboardScene = ThemeManager.createThemedScene(loader.load());
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(dashboardScene);
            stage.setTitle("Dashboard");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
