package Utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigHelper {

    // Path to the properties file
    private static final String CONFIG_FILE_PATH = "src/main/resources/config/config.properties";

    // Load the current theme (either "light" or "dark") from the config file
    public static String loadTheme() {
        Properties properties = new Properties();
        try (FileInputStream inputStream = new FileInputStream(CONFIG_FILE_PATH)) {
            properties.load(inputStream);
            return properties.getProperty("theme", "light");  // Default to "light" if no theme is set
        } catch (IOException e) {
            e.printStackTrace();
            return "light";  // Default to "light" if error occurs
        }
    }

    // Save the selected theme to the config file
    public static void saveTheme(String theme) {
        Properties properties = new Properties();
        try (FileOutputStream outputStream = new FileOutputStream(CONFIG_FILE_PATH)) {
            properties.setProperty("theme", theme);
            properties.store(outputStream, null);  // Save to the properties file
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
