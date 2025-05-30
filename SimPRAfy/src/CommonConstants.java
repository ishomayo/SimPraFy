import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class CommonConstants {
    // Define resource paths using a more robust method
    private static final String BASE_PATH;

    static {
        // Try to get the resources directory from the classpath
        URL resourceUrl = CommonConstants.class.getClassLoader().getResource("simulator/resources");
        
        if (resourceUrl != null) {
            // Use the URL path if available
            BASE_PATH = resourceUrl.getPath() + File.separator;
        } else {
            // Fallback to a relative path from the current working directory
            String currentDir = System.getProperty("user.dir");
            
            // Check if we're running from the project root or from within the src directory
            if (new File(currentDir + "/SimPRAfy/src/resources").exists()) {
                BASE_PATH = "SimPRAfy/src/resources/";
            } else if (new File(currentDir + "/src/resources").exists()) {
                BASE_PATH = "src/resources/";
            } else if (new File(currentDir + "/simulator/resources").exists()) {
                BASE_PATH = "resources/";
            } else {
                // If none of the above paths work, try to use a resource folder in the current directory
                BASE_PATH = "resources/";
                
                // Create the resources directory if it doesn't exist
                new File(BASE_PATH).mkdirs();
                
                System.out.println("Warning: Resource directory not found. Using " + 
                                  new File(BASE_PATH).getAbsolutePath() + " instead.");
            }
        }
        
        System.out.println("Using resource path: " + BASE_PATH);
    }

    // File paths using the determined BASE_PATH
    public static final String splashScreen = BASE_PATH + "SplashScreen.jpg";
    public static final String lobbyBG = BASE_PATH + "LobbyBackground.png";
    public static final String inputMethod = BASE_PATH + "inputSelectionBackground.png";
    public static final String selectAlgoBG = BASE_PATH + "SelectAlgorithmBackground.png";
    public static final String box = BASE_PATH + "Box.png";

    // Screens
    public static final String credits = BASE_PATH + "Credits.png";
    public static final String help = BASE_PATH + "Help.png";

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
    public static final String backClick2 = BASE_PATH + "back-click2.png";

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

    public static final String startDefaultSIM = BASE_PATH + "start-simul-default.png";
    public static final String startHoverSIM = BASE_PATH + "start-simul-hover.png";
    public static final String startClickSIM = BASE_PATH + "start-simul-clicked.png";

    public static final String stopDefaultSIM = BASE_PATH + "stop-simul-default.png";
    public static final String stopHoverSIM = BASE_PATH + "stop-simul-hover.png";
    public static final String stopClickSIM = BASE_PATH + "stop-simul-clicked.png";

    public static final String saveDefaultSIM = BASE_PATH + "save-simul-default.png";
    public static final String saveHoverSIM = BASE_PATH + "save-simul-hover.png";
    public static final String saveClickSIM = BASE_PATH + "save-simul-clicked.png";

    // Algorithm Buttons
    public static final String fifoDefault = BASE_PATH + "fifo-default.png";
    public static final String fifoHover = BASE_PATH + "fifo-hover.png";
    public static final String fifoClick = BASE_PATH + "fifo-click.png";

    public static final String lruDefault = BASE_PATH + "lru-default.png";
    public static final String lruHover = BASE_PATH + "lru-hover.png";
    public static final String lruClick = BASE_PATH + "lru-click.png";

    public static final String escaDefault = BASE_PATH + "esca-default.png";
    public static final String escaHover = BASE_PATH + "esca-hover.png";
    public static final String escaClick = BASE_PATH + "esca-click.png";

    public static final String scaDefault = BASE_PATH + "sca-default.png";
    public static final String scaHover = BASE_PATH + "sca-hover.png";
    public static final String scaClick = BASE_PATH + "sca-click.png";

    public static final String allDefault = BASE_PATH + "all-default.png";
    public static final String allHover = BASE_PATH + "all-hover.png";
    public static final String allClick = BASE_PATH + "all-click.png";

    public static final String mfuDefault = BASE_PATH + "mfu-default.png";
    public static final String mfuHover = BASE_PATH + "mfu-hover.png";
    public static final String mfuClick = BASE_PATH + "mfu-click.png";

    public static final String lfuDefault = BASE_PATH + "lfu-default.png";
    public static final String lfuHover = BASE_PATH + "lfu-hover.png";
    public static final String lfuClick = BASE_PATH + "lfu-click.png";

    public static final String optDefault = BASE_PATH + "opr-default.png";
    public static final String optHover = BASE_PATH + "opr-hover.png";
    public static final String optClick = BASE_PATH + "opr-click.png";

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