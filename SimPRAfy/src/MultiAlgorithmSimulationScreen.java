import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class MultiAlgorithmSimulationScreen extends JPanel {
    private JTextField refInput, frameInput;
    private JPanel algorithmsPanel;
    private JSlider speedSlider;
    private Timer timer;
    private String referenceStringInput;
    private int frameSize;
    private int refLen;
    private List<AlgorithmPanel> algorithmPanels;
    private int currentStep = 0;
    private JButton startButton, stopButton, saveButton;
    private ImageSaver imageSaver;

    // Define fixed size for algorithm panels
    private static final int ALGORITHM_PANEL_WIDTH = 600;
    private static final int ALGORITHM_PANEL_HEIGHT = 400;

    public MultiAlgorithmSimulationScreen(CardLayout layout, JPanel mainPanel, int refLen, String referenceStringInput,
            int frameSize) {
        this.setLayout(null);
        this.setBackground(new Color(2, 13, 25)); // Dark blue background
        this.setPreferredSize(new Dimension(1500, 844));

        this.refLen = refLen;
        this.referenceStringInput = referenceStringInput;
        this.frameSize = frameSize;
        this.algorithmPanels = new ArrayList<>();

        // Back Button (left green circle with arrow)
        JButton backButton = createStyledButton(CommonConstants.backDefault,
                CommonConstants.backClick2, CommonConstants.backClick2, new Dimension(50, 50));
        backButton.setBounds(20, 70, 50, 50);
        this.add(backButton);
        backButton.addActionListener(e -> layout.show(mainPanel, "AlgorithmSelection"));

        /// Reference String Input
        JLabel refLabel = new JLabel("Reference String");
        refLabel.setForeground(Color.WHITE);
        refLabel.setBounds(170, 30, 150, 30);
        refLabel.setFont(new Font("Arial", Font.BOLD, 15));
        this.add(refLabel);
        refInput = new JTextField();
        refInput.setText(referenceStringInput);
        refInput.setBorder(BorderFactory.createLineBorder(Color.GREEN, 1));
        refInput.setBounds(300, 30, 800, 30);
        refInput.setFont(new Font("Arial", Font.BOLD, 16));
        refInput.setForeground(Color.BLACK);
        refInput.setEditable(false);
        this.add(refInput);

        // Frame Size Input
        JLabel frameLabel = new JLabel("Frame Size");
        frameLabel.setForeground(Color.WHITE);
        frameLabel.setBounds(1120, 30, 100, 30);
        frameLabel.setFont(new Font("Arial", Font.BOLD, 15));
        this.add(frameLabel);
        frameInput = new JTextField();
        frameInput.setText(String.valueOf(frameSize));
        frameInput.setBounds(1225, 30, 50, 30);
        frameInput.setBorder(BorderFactory.createLineBorder(Color.GREEN, 1));
        frameInput.setFont(new Font("Arial", Font.BOLD, 16));
        frameInput.setForeground(Color.BLACK);
        frameInput.setEditable(false);
        this.add(frameInput);

        // Main container panel with border and scrolling
        JPanel mainContainer = new JPanel();
        mainContainer.setLayout(null);
        mainContainer.setBounds(135, 120, 1300, 500);
        mainContainer.setBorder(BorderFactory.createLineBorder(Color.GREEN, 1, true));
        mainContainer.setBackground(new Color(5, 20, 35)); // Slightly lighter than background
        this.add(mainContainer);

        // Create algorithms panel with FlowLayout instead of GridLayout
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 20)); // Left-aligned with gaps
        contentPanel.setOpaque(false);
        
        // Create scroll pane for algorithms
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBounds(15, 15, 1270, 470);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        
        // Customize scrollbar
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); // Faster scrolling
        scrollPane.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = new Color(50, 205, 50); // Green thumb
                this.trackColor = new Color(10, 30, 50); // Dark track
            }
        });
        
        mainContainer.add(scrollPane);
        algorithmsPanel = contentPanel; // Set the algorithms panel to our content panel

        // Initialize algorithm panels
        initializeAlgorithmPanels();

        // Speed Slider
        JLabel speedLabel = new JLabel("SPEED");
        speedLabel.setForeground(Color.WHITE);
        speedLabel.setBounds(150, 708, 100, 30);
        this.add(speedLabel);
        speedSlider = new JSlider(JSlider.HORIZONTAL, 100, 2000, 500);
        speedSlider.setBounds(200, 710, 300, 30);
        speedSlider.setOpaque(false);
        this.add(speedSlider);

        JLabel slowLabel = new JLabel("Slower");
        slowLabel.setForeground(Color.WHITE);
        slowLabel.setBounds(200, 740, 50, 20);
        this.add(slowLabel);

        JLabel fastLabel = new JLabel("Faster");
        fastLabel.setForeground(Color.WHITE);
        fastLabel.setBounds(450, 740, 50, 20);
        this.add(fastLabel);

        // Control Buttons
        startButton = createStyledButton(CommonConstants.startDefaultSIM,
                CommonConstants.startHoverSIM, CommonConstants.startClickSIM, new Dimension(250, 75));
        startButton.setBounds(610, 675, 250, 75);
        this.add(startButton);
        
        stopButton = createStyledButton(CommonConstants.stopDefaultSIM,
                CommonConstants.stopHoverSIM, CommonConstants.stopClickSIM, new Dimension(250, 75));
        stopButton.setBounds(870, 675, 250, 75);
        this.add(stopButton);
        
        saveButton = createStyledButton(CommonConstants.saveDefaultSIM,
                CommonConstants.saveHoverSIM, CommonConstants.saveClickSIM, new Dimension(250, 75));
        saveButton.setBounds(1130, 675, 250, 75);
        saveButton.setEnabled(false);
        this.add(saveButton);

        startButton.addActionListener(e -> startSimulation());
        stopButton.addActionListener(e -> stopSimulation());
        saveButton.addActionListener(e -> saveSimulation());
    }

    private void initializeAlgorithmPanels() {
        // Create and add algorithm panels with correct names and short codes
        String[] algorithmNames = {
                "FIFO", "LRU", "OPT", "SC", "ESC", "LFU", "MFU"
        };

        String[] displayNames = {
                "FIRST IN FIRST OUT", "LEAST RECENTLY USED", "OPTIMAL",
                "SECOND CHANCE", "ENHANCED SECOND CHANCE", "LEAST FREQUENTLY USED",
                "MOST FREQUENTLY USED"
        };

        // Create all algorithm panels and add them to the scrollable layout
        for (int i = 0; i < algorithmNames.length; i++) {
            AlgorithmPanel panel = new AlgorithmPanel(algorithmNames[i], frameSize);
            panel.setName(displayNames[i]); // Store full name as component name
            
            // Set fixed size for all algorithm panels
            panel.setPreferredSize(new Dimension(ALGORITHM_PANEL_WIDTH, ALGORITHM_PANEL_HEIGHT));
            panel.setMinimumSize(new Dimension(ALGORITHM_PANEL_WIDTH, ALGORITHM_PANEL_HEIGHT));
            panel.setMaximumSize(new Dimension(ALGORITHM_PANEL_WIDTH, ALGORITHM_PANEL_HEIGHT));
            
            algorithmPanels.add(panel);
            algorithmsPanel.add(panel);
        }
    }

    private void startSimulation() {
        try {
            // Parse reference string and frame size
            String refString = refInput.getText().trim();
            frameSize = Integer.parseInt(frameInput.getText().trim());

            String[] refStrArr = refString.split(" ");
            int[] referenceString = new int[refStrArr.length];
            for (int i = 0; i < refStrArr.length; i++) {
                referenceString[i] = Integer.parseInt(refStrArr[i]);
            }

            // Reset all algorithm panels
            currentStep = 0;
            for (AlgorithmPanel panel : algorithmPanels) {
                panel.reset(frameSize, referenceString);
            }

            // Start the timer for synchronized stepping
            timer = new Timer(2100 - speedSlider.getValue(), e -> {
                if (currentStep < referenceString.length) {
                    for (AlgorithmPanel panel : algorithmPanels) {
                        panel.step();
                    }
                    currentStep++;
                } else {
                    timer.stop();
                    saveButton.setEnabled(true);
                }
            });

            timer.start();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid Input!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void stopSimulation() {
        if (timer != null) {
            timer.stop();
        }
    }

    private void saveSimulation() {
        Object[] choices = { "PNG", "PDF", "CANCEL" };
        Object selected = JOptionPane.showOptionDialog(null, "Select format to save.", "Save",
                JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, choices, choices[0]);

        if ((int) selected != 2) {
            String option = (int) selected == 0 ? "PNG" : "PDF";
            boolean ok = saveOutput(algorithmsPanel, option);
            if (ok)
                JOptionPane.showMessageDialog(null, "Saved successfully.", "Save",
                        JOptionPane.INFORMATION_MESSAGE);
            else
                JOptionPane.showMessageDialog(null, "Cannot save at the moment.", "Save",
                        JOptionPane.WARNING_MESSAGE);
        }
    }

    public boolean saveOutput(JPanel panel, String extension) {
        ImageSaver is = new ImageSaver(panel);
        if (extension.equals("PNG"))
            is.saveAsImage();
        else
            is.saveAsPDF();
        return !is.getHasError();
    }

    // Helper method for button styling (same as your original code)
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

    private static ImageIcon scaleImage(String imagePath, Dimension size) {
        ImageIcon icon = new ImageIcon(imagePath);
        Image img = icon.getImage().getScaledInstance(size.width, size.height, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }
}