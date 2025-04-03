import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class MultiAlgorithmSimulationScreen extends JPanel {
    private JTextField refInput, frameInput;
    private JPanel algorithmsPanel;
    private JLabel titleLabel;
    private JSlider speedSlider;
    private Timer timer;
    private String referenceStringInput;
    private int frameSize;
    private int refLen;
    private List<AlgorithmPanel> algorithmPanels;
    private int currentStep = 0;
    private JButton startButton, stopButton, saveButton;
    private ImageSaver imageSaver;

    public MultiAlgorithmSimulationScreen(CardLayout layout, JPanel mainPanel, int refLen, String referenceStringInput,
            int frameSize) {
        this.setLayout(null);
        this.setBackground(new Color(2, 13, 25));
        this.setPreferredSize(new Dimension(1500, 844));

        this.refLen = refLen;
        this.referenceStringInput = referenceStringInput;
        this.frameSize = frameSize;
        this.algorithmPanels = new ArrayList<>();

        // Back Button
        JButton backButton = createStyledButton(CommonConstants.backDefault,
                CommonConstants.backClick, CommonConstants.backClick, new Dimension(50, 50));
        backButton.setBounds(20, 20, 50, 50);
        this.add(backButton);
        backButton.addActionListener(e -> layout.show(mainPanel, "AlgorithmSelection"));

        // Reference String Input
        JLabel refLabel = new JLabel("Reference String");
        refLabel.setForeground(Color.WHITE);
        refLabel.setBounds(170, 30, 150, 30);
        refLabel.setFont(new Font("Arial", Font.BOLD, 15));
        this.add(refLabel);
        refInput = new JTextField();
        refInput.setText(referenceStringInput);
        refInput.setBorder(BorderFactory.createLineBorder(Color.GREEN, 1));
        refInput.setBounds(300, 30, 800, 30);
        this.add(refInput);

        // Frame Size Input
        JLabel frameLabel = new JLabel("Frame Size");
        frameLabel.setForeground(Color.WHITE);
        frameLabel.setBounds(1120, 30, 100, 30);
        frameLabel.setFont(new Font("Arial", Font.BOLD, 15));
        this.add(frameLabel);
        frameInput = new JTextField();
        frameInput.setText(String.valueOf(frameSize));
        frameInput.setBorder(BorderFactory.createLineBorder(Color.GREEN, 1));
        frameInput.setBounds(1225, 30, 50, 30);
        this.add(frameInput);

        // Simulation Title
        titleLabel = new JLabel("MULTIPLE ALGORITHM SIMULATION", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(0, 80, 1500, 40);
        this.add(titleLabel);

        // Create algorithms panel with GridLayout (2x4, though we'll only use 7 slots)
        algorithmsPanel = new JPanel(new GridLayout(2, 4, 10, 10));
        algorithmsPanel.setOpaque(false);
        algorithmsPanel.setBounds(50, 130, 1400, 500);
        this.add(algorithmsPanel);

        // Initialize algorithm panels
        initializeAlgorithmPanels();

        // Speed Slider
        JLabel speedLabel = new JLabel("SPEED");
        speedLabel.setForeground(Color.WHITE);
        speedLabel.setBounds(150, 658, 100, 30);
        this.add(speedLabel);
        speedSlider = new JSlider(JSlider.HORIZONTAL, 100, 2000, 500);
        speedSlider.setBounds(200, 660, 300, 30);
        speedSlider.setOpaque(false);
        this.add(speedSlider);

        JLabel slowLabel = new JLabel("Slow");
        slowLabel.setForeground(Color.WHITE);
        slowLabel.setBounds(200, 690, 50, 20);
        this.add(slowLabel);

        JLabel fastLabel = new JLabel("Fast");
        fastLabel.setForeground(Color.WHITE);
        fastLabel.setBounds(450, 690, 50, 20);
        this.add(fastLabel);

        // Control Buttons
        startButton = createStyledButton(CommonConstants.startDefaultSIM,
                CommonConstants.startHoveSIM, CommonConstants.startClickSIM, new Dimension(250, 75));
        startButton.setBounds(610, 650, 250, 75);
        this.add(startButton);
        
        stopButton = createStyledButton(CommonConstants.stopDefaultSIM,
                CommonConstants.stopHoveSIM, CommonConstants.stopClickSIM, new Dimension(250, 75));
        stopButton.setBounds(870, 650, 250, 75);
        this.add(stopButton);
        
        saveButton = createStyledButton(CommonConstants.saveDefaultSIM,
                CommonConstants.saveHoveSIM, CommonConstants.saveClickSIM, new Dimension(250, 75));
        saveButton.setBounds(1130, 650, 250, 75);
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
        
        for (int i = 0; i < algorithmNames.length; i++) {
            AlgorithmPanel panel = new AlgorithmPanel(algorithmNames[i], frameSize);
            panel.setName(displayNames[i]); // Store full name as component name
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