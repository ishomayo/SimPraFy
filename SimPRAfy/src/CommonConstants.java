import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class CommonConstants {
    // Define resource paths using a more robust method
    private static final String BASE_PATH;

    static {
        // Get the base path of the resources folder
        URL resourceUrl = CommonConstants.class.getClassLoader().getResource("resources");

        if (resourceUrl != null) {
            // Add trailing slash to prevent path concatenation errors
            BASE_PATH = resourceUrl.getPath() + File.separator;
        } else {
            // Fallback to current directory if resource folder is not found
            String currentDir = System.getProperty("user.dir");

            if (new File(currentDir + "/src/resources").exists()) {
                BASE_PATH = currentDir + "/src/resources/";
            } else {
                BASE_PATH = currentDir + "/resources/";
                new File(BASE_PATH).mkdirs(); // Create if missing
            }
        }

        System.out.println("Using resource path: " + BASE_PATH);
    }

    // File paths using the determined BASE_PATH
    public static final String splashScreen = BASE_PATH + "SplashScreen.jpg";
    public static final String lobbyBG = BASE_PATH + "LobbyBackground.jpg";

    // Screens
    public static final String credits = BASE_PATH + "Credits.jpg";

    // Lobby Buttons
    public static final String startDefault = BASE_PATH + "start-default.png";
    public static final String startHover = BASE_PATH + "start-hover.png";
    public static final String startClick = BASE_PATH + "start-click.png";
    public static final String helpDefault = BASE_PATH + "help-default.png";
    public static final String helpHover = BASE_PATH + "help-hover.png";
    public static final String helpClick = BASE_PATH + "help-click.png";
    public static final String creditsDefault = BASE_PATH + "credits-default.png";
    public static final String creditsHover = BASE_PATH + "credits-hover.png";
    public static final String creditsClick = BASE_PATH + "credits-click.png";
    public static final String exitDefault = BASE_PATH + "exit-default.png";
    public static final String exitHover = BASE_PATH + "exit-hover.png";
    public static final String exitClick = BASE_PATH + "exit-click.png";
    public static final String backDefault = BASE_PATH + "back.png";
    public static final String backClick = BASE_PATH + "back-click.png";


    // Utility method to load images properly
    public static Image loadImage(String path) {
        try {
            File imageFile = new File(path);
            if (!imageFile.exists()) {
                System.err.println("File not found: " + path);
                return null;
            }
            return ImageIO.read(imageFile);
        } catch (IOException e) {
            System.err.println("Failed to load image: " + path);
            e.printStackTrace();
            return null;
        }
    }
}