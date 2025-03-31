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

    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeLater(() -> {
            SplashScreen splash = new SplashScreen();
            splash.showSplash();
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

        Lobby(); // Call Lobby to add the panel
        infoScreens.showCredits();

        layout.show(mainPanel, "Lobby");
    }

    public void Lobby() {
        BackgroundPanel lobbyPanel = new BackgroundPanel(CommonConstants.lobbyBG);
        lobbyPanel.setLayout(new BorderLayout()); // Main layout for lobbyPanel

        // Button panel with BoxLayout (to keep buttons vertically aligned)
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setOpaque(false); // Transparent button panel

        // Add buttons
        JButton startButton = createStyledButton(CommonConstants.startDefault, CommonConstants.startHover,
                CommonConstants.startClick);
        JButton creditsButton = createStyledButton(CommonConstants.creditsDefault, CommonConstants.creditsHover,
                CommonConstants.creditsClick);
        JButton helpButton = createStyledButton(CommonConstants.helpDefault, CommonConstants.helpHover,
                CommonConstants.helpClick);
        JButton exitButton = createStyledButton(CommonConstants.exitDefault, CommonConstants.exitHover,
                CommonConstants.exitClick);

        // Add spacing between buttons and center-align them
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        creditsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        helpButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        buttonPanel.add(Box.createVerticalStrut(20)); // Add spacing above
        buttonPanel.add(startButton);
        buttonPanel.add(Box.createVerticalStrut(15)); // Spacing between buttons
        buttonPanel.add(creditsButton);
        buttonPanel.add(Box.createVerticalStrut(15));
        buttonPanel.add(helpButton);
        buttonPanel.add(Box.createVerticalStrut(15));
        buttonPanel.add(exitButton);
        buttonPanel.add(Box.createVerticalStrut(20)); // Add spacing below

        // Add padding to move buttonPanel lower
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(300, 0, 0, 0)); // Move it down by 200px

        // Add buttonPanel to CENTER of lobbyPanel
        lobbyPanel.add(buttonPanel, BorderLayout.CENTER);

        // Add lobbyPanel to mainPanel
        mainPanel.add(lobbyPanel, "Lobby");

        // Add action listeners
        exitButton.addActionListener(e -> System.exit(0));
        startButton.addActionListener(e -> layout.show(mainPanel, "DataInputSelection"));
        creditsButton.addActionListener(e -> layout.show(mainPanel, "Credits"));
        helpButton.addActionListener(e -> layout.show(mainPanel, "Help"));
    }

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

    public static void startApplication() {
        SwingUtilities.invokeLater(() -> {
            App app = new App();
            app.setVisible(true);
        });
    }
}