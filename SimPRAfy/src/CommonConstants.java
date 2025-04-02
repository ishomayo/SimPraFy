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
    public static final String inputMethod = BASE_PATH + "SelectionBackground.jpg";
    public static final String selectAlgoBG = BASE_PATH + "SelectAlgorithmBackground.png";
    public static final String box = BASE_PATH + "Box.png";

    // Screens
    public static final String credits = BASE_PATH + "Credits.png";

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

    // Input Method Buttons
    public static final String randomDefault = BASE_PATH + "random-default.png";
    public static final String randomHover = BASE_PATH + "random-hover.png";
    public static final String userInputDefault = BASE_PATH + "userinput-default.png";
    public static final String userInputHover = BASE_PATH + "userinput-hover.png";
    public static final String fileDefault = BASE_PATH + "file-default.png";
    public static final String fileHover = BASE_PATH + "file-hover.png";

    public static final String refLen = BASE_PATH + "Ref-length.png";
    public static final String refString = BASE_PATH + "Ref-string.png";
    public static final String frameSize = BASE_PATH + "Frame-size.png";

    public static final String uploadDisabled = BASE_PATH + "upload-disabled.png";
    public static final String uploadDefault = BASE_PATH + "upload-default.png";
    public static final String uploadHover = BASE_PATH + "upload-hover.png";

    public static final String generateDisabled = BASE_PATH + "generate-disabled.png";
    public static final String generateDefault = BASE_PATH + "generate-default.png";
    public static final String generateHover = BASE_PATH + "generate-hover.png";

    public static final String continueDisabled = BASE_PATH + "continue-disabled.png";
    public static final String continueDefault = BASE_PATH + "continue-default.png";
    public static final String continueHover = BASE_PATH + "continue-hover.png";

    // Algorithm Buttons
    public static final String fifoDefault = BASE_PATH + "fifo-default.png";
    public static final String lruDefault = BASE_PATH + "lru-default.png";
    public static final String escaDefault = BASE_PATH + "esca-default.png";
    public static final String scaDefault = BASE_PATH + "sca-default.png";
    public static final String allDefault = BASE_PATH + "all-default.png";
    public static final String mfuDefault = BASE_PATH + "mfu-default.png";
    public static final String lfuDefault = BASE_PATH + "lfu-default.png";
    public static final String optDefault = BASE_PATH + "opt-default.png";

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