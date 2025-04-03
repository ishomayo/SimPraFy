import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

// Update the MultiAlgorithmSimulationScreen class to match the provided design

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
    private int currentPage = 0;
    private JButton nextPageButton, prevPageButton;
    private JLabel pageIndicator;

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
                CommonConstants.backClick, CommonConstants.backClick, new Dimension(50, 50));
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
        this.add(frameInput);

        // Main container panel with border
        JPanel mainContainer = new JPanel();
        mainContainer.setLayout(null);
        mainContainer.setBounds(135, 120, 1300, 500);
        mainContainer.setBorder(BorderFactory.createLineBorder(Color.GREEN, 1, true));
        mainContainer.setBackground(new Color(5, 20, 35)); // Slightly lighter than background
        this.add(mainContainer);

        // Create algorithms panel with GridLayout (2x2)
        algorithmsPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        algorithmsPanel.setOpaque(false);
        algorithmsPanel.setBounds(15, 15, 1270, 470);
        mainContainer.add(algorithmsPanel);

        // Page navigation buttons
        prevPageButton = new JButton("◀");
        prevPageButton.setBounds(135, 620, 50, 30);
        prevPageButton.setForeground(Color.WHITE);
        prevPageButton.setBackground(new Color(20, 30, 45));
        prevPageButton.setFocusPainted(false);
        prevPageButton.addActionListener(e -> showPreviousPage());
        this.add(prevPageButton);

        pageIndicator = new JLabel("Page 1/2", SwingConstants.CENTER);
        pageIndicator.setForeground(Color.WHITE);
        pageIndicator.setBounds(200, 620, 1100, 30);
        this.add(pageIndicator);

        nextPageButton = new JButton("▶");
        nextPageButton.setBounds(1385, 620, 50, 30);
        nextPageButton.setForeground(Color.WHITE);
        nextPageButton.setBackground(new Color(20, 30, 45));
        nextPageButton.setFocusPainted(false);
        nextPageButton.addActionListener(e -> showNextPage());
        this.add(nextPageButton);

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
        JButton startButton = createStyledButton(CommonConstants.startDefaultSIM,
                CommonConstants.startHoverSIM, CommonConstants.startClickSIM, new Dimension(250, 75));
        startButton.setBounds(610, 675, 250, 75);
        this.add(startButton);
        JButton stopButton = createStyledButton(CommonConstants.stopDefaultSIM,
                CommonConstants.stopHoverSIM, CommonConstants.stopClickSIM, new Dimension(250, 75));
        stopButton.setBounds(870, 675, 250, 75);
        this.add(stopButton);
        JButton saveButton = createStyledButton(CommonConstants.saveDefaultSIM,
                CommonConstants.saveHoverSIM, CommonConstants.saveClickSIM, new Dimension(250, 75));
        saveButton.setBounds(1130, 675, 250, 75);
        saveButton.setEnabled(false);
        this.add(saveButton);

        startButton.addActionListener(e -> startSimulation());
        stopButton.addActionListener(e -> stopSimulation());
        saveButton.addActionListener(e -> saveSimulation());

        // Show first page
        showPage(0);
    }

    private void initializeAlgorithmPanels() {
        // Create and add algorithm panels with correct names and short codes
        String[] algorithmNames = {
                "FIRST IN FIRST OUT", "LEAST RECENTLY USED", "OPTIMAL", "SECOND CHANCE", "ENHANCED SECOND CHANCE", "LEAST FREQUENTLY USED", "MOST FREQUENTLY USED"
        };

        String[] displayNames = {
                "FIRST IN FIRST OUT", "LEAST RECENTLY USED", "OPTIMAL",
                "SECOND CHANCE", "ENHANCED SECOND CHANCE", "LEAST FREQUENTLY USED",
                "MOST FREQUENTLY USED"
        };

        // Create all algorithm panels but don't add them to the layout yet
        for (int i = 0; i < algorithmNames.length; i++) {
            AlgorithmPanel panel = new AlgorithmPanel(algorithmNames[i], frameSize);
            panel.setName(displayNames[i]); // Store full name as component name
            algorithmPanels.add(panel);
        }
    }

    private void showPage(int page) {
        algorithmsPanel.removeAll();
        currentPage = page;

        // Calculate total pages needed
        int totalPages = (int) Math.ceil(algorithmPanels.size() / 4.0);

        // Update page indicator
        pageIndicator.setText("Page " + (page + 1) + "/" + totalPages);

        // Enable/disable navigation buttons
        prevPageButton.setEnabled(page > 0);
        nextPageButton.setEnabled(page < totalPages - 1);

        // Add appropriate panels for this page
        int startIdx = page * 4;
        int endIdx = Math.min(startIdx + 4, algorithmPanels.size());

        for (int i = startIdx; i < endIdx; i++) {
            algorithmsPanel.add(algorithmPanels.get(i));
        }

        algorithmsPanel.revalidate();
        algorithmsPanel.repaint();
    }

    private void showNextPage() {
        int totalPages = (int) Math.ceil(algorithmPanels.size() / 4.0);
        if (currentPage < totalPages - 1) {
            showPage(currentPage + 1);
        }
    }

    private void showPreviousPage() {
        if (currentPage > 0) {
            showPage(currentPage - 1);
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