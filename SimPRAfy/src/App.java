import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class App extends JFrame {

    protected static int width = 1500, height = 844;
    private CardLayout layout = new CardLayout();
    private JPanel mainPanel = new JPanel(layout);
    private DataInputScreen dataInputScreen;

    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeLater(() -> {
            SplashScreen splash = new SplashScreen();
            splash.showSplash();
        });
    }

    public static void startApplication() {
        SwingUtilities.invokeLater(() -> {
            App app = new App();
            app.setVisible(true);
        });
    }

    public App() {
        setSize(width, height);
        setUndecorated(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        add(mainPanel);

        InformationScreens infoScreens = new InformationScreens(layout, mainPanel);
        dataInputScreen = new DataInputScreen(this, layout, mainPanel);

        mainPanel.add(dataInputScreen, "DataInputScreen");

        Lobby(); // Call Lobby to add the panel
        infoScreens.showCredits();

        layout.show(mainPanel, "Lobby");
    }

    public void Lobby() {
        BackgroundPanel lobbyPanel = new BackgroundPanel(CommonConstants.lobbyBG);
        lobbyPanel.setLayout(new BorderLayout()); // Main layout for lobbyPanel

        // Create and configure the button panel
        JPanel buttonPanel = createButtonPanel();

        // Add padding to move buttonPanel lower
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(300, 0, 0, 0)); // Move it down by 200px

        // Add buttonPanel to CENTER of lobbyPanel
        lobbyPanel.add(buttonPanel, BorderLayout.CENTER);

        // Add lobbyPanel to mainPanel
        mainPanel.add(lobbyPanel, "Lobby");
    }

    // Helper method to create and configure the button panel
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setOpaque(false); // Transparent button panel

        // Create buttons
        JButton startButton = createStyledButton(CommonConstants.startDefault, CommonConstants.startHover, CommonConstants.startClick);
        JButton creditsButton = createStyledButton(CommonConstants.creditsDefault, CommonConstants.creditsHover, CommonConstants.creditsClick);
        JButton helpButton = createStyledButton(CommonConstants.helpDefault, CommonConstants.helpHover, CommonConstants.helpClick);
        JButton exitButton = createStyledButton(CommonConstants.exitDefault, CommonConstants.exitHover, CommonConstants.exitClick);

        // Add spacing between buttons and center-align them
        addButtonsToPanel(buttonPanel, startButton, creditsButton, helpButton, exitButton);

        // Add action listeners
        configureButtonActions(startButton, creditsButton, helpButton, exitButton);

        return buttonPanel;
    }

    // Helper method to add buttons to the panel with spacing and alignment
    private void addButtonsToPanel(JPanel buttonPanel, JButton... buttons) {
        buttonPanel.add(Box.createVerticalStrut(20)); // Add spacing above
        for (JButton button : buttons) {
            button.setAlignmentX(Component.CENTER_ALIGNMENT);
            buttonPanel.add(button);
            buttonPanel.add(Box.createVerticalStrut(15)); // Spacing between buttons
        }
        buttonPanel.add(Box.createVerticalStrut(20)); // Add spacing below
    }

    // Helper method to configure button actions
    private void configureButtonActions(JButton startButton, JButton creditsButton, JButton helpButton, JButton exitButton) {
        exitButton.addActionListener(e -> System.exit(0));
        startButton.addActionListener(e -> layout.show(mainPanel, "DataInputScreen"));
        creditsButton.addActionListener(e -> layout.show(mainPanel, "Credits"));
        helpButton.addActionListener(e -> layout.show(mainPanel, "Help"));
    }

    //****************************************************************************************
    //
    //                                 HELPER METHODS
    //                            
    //****************************************************************************************
    
    // Helper method for button styling
    private static JButton createStyledButton(String defaultIconPath, String hoverIconPath, String clickIconPath) {
        JButton button = new JButton();
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(278, 80));

        // Load and scale the images
        ImageIcon defaultIcon = scaleImage(defaultIconPath, button.getPreferredSize());
        ImageIcon hoverIcon = scaleImage(hoverIconPath, button.getPreferredSize());
        ImageIcon clickIcon = scaleImage(clickIconPath, button.getPreferredSize());

        button.setIcon(defaultIcon);

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setIcon(hoverIcon);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setIcon(defaultIcon);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                button.setIcon(clickIcon);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                button.setIcon(hoverIcon);
            }
        });

        return button;
    }

    // Helper method to scale an image to fit the button
    private static ImageIcon scaleImage(String imagePath, Dimension size) {
        ImageIcon icon = new ImageIcon(imagePath);
        Image img = icon.getImage().getScaledInstance(size.width, size.height, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }
}