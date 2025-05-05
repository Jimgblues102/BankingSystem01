package Utils;

import javafx.scene.Parent;
import javafx.scene.Scene;
import java.net.URL;

public class ThemeManager {

    private static String currentTheme = "/styles/theme-dark.css"; // Default theme

    public static void setCurrentTheme(String themePath) {
        currentTheme = themePath;
    }

    public static String getCurrentTheme() {
        return currentTheme;
    }

    public static void applyTheme(Scene scene) {
        scene.getStylesheets().clear();

        // Convert file URL back to relative classpath path
        String resourcePath;
        if (currentTheme.startsWith("file:")) {
            // Remove "file:" prefix and extract only classpath relative part
            int index = currentTheme.indexOf("/styles/");
            if (index != -1) {
                resourcePath = currentTheme.substring(index);
            } else {
                System.err.println("Invalid theme path format: " + currentTheme);
                return;
            }
        } else {
            resourcePath = currentTheme; // Already relative
        }

        URL cssURL = ThemeManager.class.getResource(resourcePath);
        if (cssURL == null) {
            System.err.println("Theme CSS not found at: " + resourcePath);
            return;
        }

        scene.getStylesheets().add(cssURL.toExternalForm());
    }


    // Use this when creating a new Scene
    public static Scene createThemedScene(Parent root) {
        Scene scene = new Scene(root);
        applyTheme(scene);
        return scene;
    }
}
