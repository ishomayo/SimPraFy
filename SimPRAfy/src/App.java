import javax.swing.ImageIcon;
import javax.swing.JButton;
// import javax.swing.JCheckBox;
// import javax.swing.JComboBox;
import javax.swing.JFrame;
// import javax.swing.JLabel;
// import javax.swing.JLabel;
// import javax.swing.JOptionPane;
import javax.swing.JPanel;
// import javax.swing.SwingConstants;
// import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

// import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
// import java.awt.event.ActionListener;

public class App extends JFrame {

    protected static int width = 1500, height = 844;
    private CardLayout layout = new CardLayout();
    private JPanel mainPanel = new JPanel(layout);
    private DataInputScreen dataInputScreen;
    private int refLen, frameSize;
    private String referenceString;

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
        infoScreens.showHelp();

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
        JButton startButton = createStyledButton(CommonConstants.startDefault,
                CommonConstants.startHover, CommonConstants.startClick, new Dimension(278, 80));
        JButton creditsButton = createStyledButton(CommonConstants.creditsDefault,
                CommonConstants.creditsHover, CommonConstants.creditsClick, new Dimension(278, 80));
        JButton helpButton = createStyledButton(CommonConstants.helpDefault,
                CommonConstants.helpHover, CommonConstants.helpClick, new Dimension(278, 80));
        JButton exitButton = createStyledButton(CommonConstants.exitDefault,
                CommonConstants.exitHover, CommonConstants.exitClick, new Dimension(278, 80));

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
    private static JButton createStyledButton(String defaultIconPath, String hoverIconPath, String clickIconPath,
            Dimension size) {
        JButton button = new JButton();
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(size);

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
                CommonConstants.backClick2, CommonConstants.backClick2, new Dimension(50, 50));
        backButton.setBounds(20, 200, 50, 50);
        algorithmPanel.add(backButton);
        backButton.addActionListener(e -> layout.show(mainPanel, "Lobby"));

        JButton fifoButton = createStyledButton(CommonConstants.fifoDefault,
                CommonConstants.fifoHover, CommonConstants.fifoClick, new Dimension(400, 268));
        JButton lruButton = createStyledButton(CommonConstants.lruDefault,
                CommonConstants.lruHover, CommonConstants.lruClick, new Dimension(290, 268));
        JButton scaButton = createStyledButton(CommonConstants.scaDefault,
                CommonConstants.scaHover, CommonConstants.scaClick, new Dimension(345, 138));
        JButton escaButton = createStyledButton(CommonConstants.escaDefault,
                CommonConstants.escaHover, CommonConstants.escaClick, new Dimension(345, 138));
        JButton optButton = createStyledButton(CommonConstants.optDefault,
                CommonConstants.optHover, CommonConstants.optClick, new Dimension(350, 201));
        JButton lfuButton = createStyledButton(CommonConstants.lfuDefault,
                CommonConstants.lfuHover, CommonConstants.lfuClick, new Dimension(350, 205));
        JButton mfuButton = createStyledButton(CommonConstants.mfuDefault,
                CommonConstants.mfuHover, CommonConstants.mfuClick, new Dimension(145, 268));
        JButton allButton = createStyledButton(CommonConstants.allDefault,
                CommonConstants.allHover, CommonConstants.allClick, new Dimension(145, 138));

        positionButtons(fifoButton, lruButton, scaButton, escaButton, optButton, lfuButton, mfuButton, allButton);

        algorithmPanel.add(fifoButton);
        algorithmPanel.add(lruButton);
        algorithmPanel.add(scaButton);
        algorithmPanel.add(escaButton);
        algorithmPanel.add(optButton);
        algorithmPanel.add(lfuButton);
        algorithmPanel.add(mfuButton);
        algorithmPanel.add(allButton);

        fifoButton.addActionListener(e -> showFifoSimulationScreen());
        lruButton.addActionListener(e -> showLRUSimulationScreen());
        optButton.addActionListener(e -> showOptSimulationScreen());
        scaButton.addActionListener(e -> showSecondChanceSimulationScreen()); 
        lfuButton.addActionListener(e -> showLfuSimulationScreen());
        mfuButton.addActionListener(e -> showMfuSimulationScreen());
        escaButton.addActionListener(e -> showEnhancedSecondChanceSimulationScreen());

        mainPanel.add(algorithmPanel, "AlgorithmSelection");
        layout.show(mainPanel, "AlgorithmSelection"); // Show the Algorithm Selection screen
    }

    private void positionButtons(JButton fifo, JButton lru, JButton sca, JButton esca, JButton opt, JButton lfu,
            JButton mfu, JButton all) {
        float center = 57.5f;
        fifo.setBounds(200 - (int) center, 330, 400, 268);
        lru.setBounds(610 - (int) center, 330, 290, 268);
        sca.setBounds(200 - (int) center, 608, 345, 138);
        esca.setBounds(555 - (int) center, 608, 345, 138);
        opt.setBounds(910 - (int) center, 330, 350, 201);
        lfu.setBounds(910 - (int) center, 541, 350, 201);
        mfu.setBounds(1270 - (int) center, 330, 145, 268);
        all.setBounds(1270 - (int) center, 608, 145, 135);
    }

    private void showFifoSimulationScreen() {
        refLen = dataInputScreen.getDropdownRefLen();
        referenceString = dataInputScreen.getTextArea();
        frameSize = dataInputScreen.getDropdownFrameSize();

        FifoSimulationScreen fifoScreen = new FifoSimulationScreen(layout, mainPanel, refLen, referenceString, frameSize);
        mainPanel.add(fifoScreen, "FifoSimulationScreen");
        layout.show(mainPanel, "FifoSimulationScreen");
    }
    
    private void showLRUSimulationScreen() {
        refLen = dataInputScreen.getDropdownRefLen();
        referenceString = dataInputScreen.getTextArea();
        frameSize = dataInputScreen.getDropdownFrameSize();

        LRUSimulationScreen fifoScreen = new LRUSimulationScreen(layout, mainPanel, refLen, referenceString, frameSize);
        mainPanel.add(fifoScreen, "LRUSimulationScreen");
        layout.show(mainPanel, "LRUSimulationScreen");
    }

    private void showOptSimulationScreen() {
        refLen = dataInputScreen.getDropdownRefLen();
        referenceString = dataInputScreen.getTextArea();
        frameSize = dataInputScreen.getDropdownFrameSize();

        OptSimulationScreen fifoScreen = new OptSimulationScreen(layout, mainPanel, refLen, referenceString, frameSize);
        mainPanel.add(fifoScreen, "OptSimulationScreen");
        layout.show(mainPanel, "OptSimulationScreen");
    }

    private void showLfuSimulationScreen() {
        refLen = dataInputScreen.getDropdownRefLen();
        referenceString = dataInputScreen.getTextArea();
        frameSize = dataInputScreen.getDropdownFrameSize();

        LfuSimulationScreen fifoScreen = new LfuSimulationScreen(layout, mainPanel, refLen, referenceString, frameSize);
        mainPanel.add(fifoScreen, "LfuSimulationScreen");
        layout.show(mainPanel, "LfuSimulationScreen");
    }

    private void showMfuSimulationScreen() {
        refLen = dataInputScreen.getDropdownRefLen();
        referenceString = dataInputScreen.getTextArea();
        frameSize = dataInputScreen.getDropdownFrameSize();

        MfuSimulationScreen fifoScreen = new MfuSimulationScreen(layout, mainPanel, refLen, referenceString, frameSize);
        mainPanel.add(fifoScreen, "MfuSimulationScreen");
        layout.show(mainPanel, "MfuSimulationScreen");
    }

    private void showSecondChanceSimulationScreen() {
        refLen = dataInputScreen.getDropdownRefLen();
        referenceString = dataInputScreen.getTextArea();
        frameSize = dataInputScreen.getDropdownFrameSize();

        SecondChanceSimulationScreen fifoScreen = new SecondChanceSimulationScreen(layout, mainPanel, refLen, referenceString, frameSize);
        mainPanel.add(fifoScreen, "SecondChanceSimulationScreen");
        layout.show(mainPanel, "SecondChanceSimulationScreen");
    }

    private void showEnhancedSecondChanceSimulationScreen() {
        refLen = dataInputScreen.getDropdownRefLen();
        referenceString = dataInputScreen.getTextArea();
        frameSize = dataInputScreen.getDropdownFrameSize();
        
        EnhancedSecondChanceSimulationScreen fifoScreen = new EnhancedSecondChanceSimulationScreen(layout, mainPanel, refLen, referenceString, frameSize);
        mainPanel.add(fifoScreen, "EnhancedSecondChanceSimulationScreen");
        layout.show(mainPanel, "EnhancedSecondChanceSimulationScreen");
    }

}