import javax.swing.ImageIcon;
import javax.swing.JButton;
// import javax.swing.JCheckBox;
// import javax.swing.JComboBox;
import javax.swing.JFrame;
// import javax.swing.JLabel;
// import javax.swing.JOptionPane;
import javax.swing.JPanel;
// import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import java.awt.image.BufferedImage;
// import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
// import java.awt.event.ActionListener;

public class App extends JFrame {

    protected static int width = 900, height = 600;
    private CardLayout layout = new CardLayout();
    private JPanel mainPanel = new JPanel(layout);
    private DataInputScreen dataInputScreen;

    private static final int BASE_X = 250; // Base X position for adjustments
    private static final int BASE_Y = 400; // Base Y position for adjustments
    private static final double SCALE = 1.2; // Adjust this for scaling

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
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(200, 0, 0, 0)); // Move it down by 200px

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
        JButton startButton = createStyledButton(CommonConstants.startDefault,
                CommonConstants.startHover, CommonConstants.startClick);
        JButton creditsButton = createStyledButton(CommonConstants.creditsDefault,
                CommonConstants.creditsHover, CommonConstants.creditsClick);
        JButton helpButton = createStyledButton(CommonConstants.helpDefault,
                CommonConstants.helpHover, CommonConstants.helpClick);
        JButton exitButton = createStyledButton(CommonConstants.exitDefault,
                CommonConstants.exitHover, CommonConstants.exitClick);

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
    private void configureButtonActions(JButton startButton, JButton creditsButton, JButton helpButton,
            JButton exitButton) {
        exitButton.addActionListener(e -> System.exit(0));
        startButton.addActionListener(e -> layout.show(mainPanel, "DataInputScreen"));
        creditsButton.addActionListener(e -> layout.show(mainPanel, "Credits"));
        helpButton.addActionListener(e -> layout.show(mainPanel, "Help"));
    }

    // ****************************************************************************************
    //
    // HELPER METHODS
    //
    // ****************************************************************************************

    // Helper method for button styling
    // Button icon with sharp scaling
    private static JButton createStyledButton(String defaultIconPath, String hoverIconPath, String clickIconPath) {
        JButton button = new JButton();
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(150, 50));

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
        // Load the image
        ImageIcon icon = new ImageIcon(imagePath);
        Image img = icon.getImage();
    
        // Create a new BufferedImage to store the scaled image
        BufferedImage bufferedImage = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_ARGB);
    
        // Create Graphics2D object for scaling the image with rendering hints
        Graphics2D g2d = bufferedImage.createGraphics();
        
        // Use high-quality interpolation for scaling
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);  // Better scaling
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);  // Anti-aliasing for smooth edges
        
        // Draw the image at the desired size
        g2d.drawImage(img, 0, 0, size.width, size.height, null);
        g2d.dispose(); // Clean up
    
        return new ImageIcon(bufferedImage);  // Return the scaled image as ImageIcon
    }
    

    public void selectAlgorithmScreen() {
        ImageIcon backgroundImage = new ImageIcon(CommonConstants.selectAlgoBG);

        JPanel algorithmPanel = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };

        algorithmPanel.setOpaque(false);

        this.setLayout(null);

        JButton backButton = createStyledButton(CommonConstants.backDefault,
                CommonConstants.backClick, CommonConstants.backClick);
        backButton.setBounds(20, 200, 50, 50);
        algorithmPanel.add(backButton);
        backButton.addActionListener(e -> layout.show(mainPanel, "Lobby"));

        JButton fifoButton = createStyledButton(CommonConstants.fifoDefault,
                CommonConstants.fifoDefault, CommonConstants.fifoDefault);
        JButton lruButton = createStyledButton(CommonConstants.lruDefault,
                CommonConstants.lruDefault, CommonConstants.lruDefault);
        JButton scaButton = createStyledButton(CommonConstants.scaDefault,
                CommonConstants.scaDefault, CommonConstants.scaDefault);
        JButton escaButton = createStyledButton(CommonConstants.escaDefault,
                CommonConstants.escaDefault, CommonConstants.escaDefault);
        JButton optButton = createStyledButton(CommonConstants.optDefault,
                CommonConstants.optDefault, CommonConstants.optDefault);
        JButton lfuButton = createStyledButton(CommonConstants.lfuDefault,
                CommonConstants.lfuDefault, CommonConstants.lfuDefault);
        JButton mfuButton = createStyledButton(CommonConstants.mfuDefault,
                CommonConstants.mfuDefault, CommonConstants.mfuDefault);
        JButton allButton = createStyledButton(CommonConstants.allDefault,
                CommonConstants.allDefault, CommonConstants.allDefault);

        positionButtons(fifoButton, lruButton, scaButton, escaButton, optButton, lfuButton, mfuButton, allButton);

        algorithmPanel.add(fifoButton);
        algorithmPanel.add(lruButton);
        algorithmPanel.add(scaButton);
        algorithmPanel.add(escaButton);
        algorithmPanel.add(optButton);
        algorithmPanel.add(lfuButton);
        algorithmPanel.add(mfuButton);
        algorithmPanel.add(allButton);

        mainPanel.add(algorithmPanel, "AlgorithmSelection");
        layout.show(mainPanel, "AlgorithmSelection"); // Show the Algorithm Selection screen
    }

    private void positionButtons(JButton fifo, JButton lru, JButton sca, JButton esca, JButton opt, JButton lfu,
            JButton mfu, JButton all) {
        fifo.setBounds(280, 350, 300, 201);
        lru.setBounds(590, 350, 217, 201);
        sca.setBounds(280, 560, 258, 105);
        esca.setBounds(548, 560, 258, 105);
        opt.setBounds(817, 350, 284, 120);
        lfu.setBounds(817, 480, 284, 186);
        mfu.setBounds(1110, 350, 109, 201);
        all.setBounds(1110, 561, 109, 105);
    }
}