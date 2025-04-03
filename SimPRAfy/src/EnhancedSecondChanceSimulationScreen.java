import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
// import java.util.LinkedList;
// import java.util.Queue;

class Page {
    int number;
    boolean referenceBit;
    boolean modifyBit; // Needed for Enhanced Second Chance

    public Page(int number) {
        this.number = number;
        this.referenceBit = true; // Initially set to 1
        this.modifyBit = false;  // Set to false initially
    }
}

public class EnhancedSecondChanceSimulationScreen extends JPanel {
    private JTextField refInput, frameInput;
    private JPanel gridPanel;
    private JLabel pageFaultLabel, pageHitLabel;
    private JSlider speedSlider;
    private Timer timer;
    private int[] referenceString;
    private int frameSize;
    private ArrayList<Page> frames;
    private int step;
    private JScrollPane scrollPane;
    private boolean[] referenceBits;  // Reference bits for each frame
    private boolean[] modifyBits;     // Modify bits for each frame
    private int clockHand;            // Clock hand for Enhanced Second-Chance Algorithm

    private String referenceStringInput;

    public EnhancedSecondChanceSimulationScreen(CardLayout layout, JPanel mainPanel, int refLen, String referenceStringInput,
            int frameSize) {
        this.setLayout(null);
        this.setBackground(new Color(2, 13, 25));
        this.setPreferredSize(new Dimension(1500, 844));

        this.referenceStringInput = referenceStringInput;
        this.frameSize = frameSize;

        // Back Button
        JButton backButton = createStyledButton(CommonConstants.backDefault,
                CommonConstants.backClick2, CommonConstants.backClick2, new Dimension(50, 50));
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
        frameInput.setBounds(1225, 30, 50, 30);
        frameInput.setBorder(BorderFactory.createLineBorder(Color.GREEN, 1));
        this.add(frameInput);

        // Simulation Title
        JLabel title = new JLabel("ENHANCED SECOND CHANCE ALGORITHM", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 30));
        title.setForeground(Color.WHITE);
        title.setBounds(0, 80, 1500, 40);
        this.add(title);

        // Create a container panel with fixed size that will hold gridPanel
        JPanel containerPanel = new JPanel(new BorderLayout());
        containerPanel.setOpaque(false);

        // Setup gridPanel with horizontal BoxLayout
        gridPanel = new JPanel();
        gridPanel.setLayout(new BoxLayout(gridPanel, BoxLayout.X_AXIS));
        gridPanel.setOpaque(false);

        // Add gridPanel to the container
        containerPanel.add(gridPanel, BorderLayout.CENTER);

        // Create scroll pane with ALWAYS show horizontal scrollbar
        scrollPane = new JScrollPane(containerPanel);
        scrollPane.setBounds(100, 150, 1300, 500);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.GREEN, 5));
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        // Customize the scrollbar
        JScrollBar hScrollBar = scrollPane.getHorizontalScrollBar();
        hScrollBar.setUnitIncrement(20);
        hScrollBar.setBlockIncrement(100);

        // Make scrollbar visible by setting a custom UI
        hScrollBar.setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = Color.GREEN;
                this.trackColor = new Color(40, 40, 40);
            }
        });

        this.add(scrollPane);

        // Page Fault and Hit Labels
        pageFaultLabel = new JLabel("Total Page Faults: 0");
        pageFaultLabel.setForeground(Color.WHITE);
        pageFaultLabel.setBounds(110, 675, 300, 30);
        pageFaultLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        this.add(pageFaultLabel);

        pageHitLabel = new JLabel("Total Page Hits: 0");
        pageHitLabel.setForeground(Color.WHITE);
        pageHitLabel.setBounds(399, 675, 300, 30);
        pageHitLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        this.add(pageHitLabel);

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

        startButton.addActionListener(e -> startSimulation(saveButton));
        stopButton.addActionListener(e -> stopSimulation());
    }

    private void startSimulation(JButton saveButton) {
        System.out.println(referenceStringInput);
        try {
            String[] refStrArr = referenceStringInput.trim().split(" ");
            referenceString = new int[refStrArr.length];
            for (int i = 0; i < refStrArr.length; i++) {
                referenceString[i] = Integer.parseInt(refStrArr[i]);
            }
            frameSize = Integer.parseInt(frameInput.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid Input!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        frames = new ArrayList<>();
        referenceBits = new boolean[frameSize];
        modifyBits = new boolean[frameSize]; // Track modified pages
        step = 0;
        clockHand = 0;
        gridPanel.removeAll();

        gridPanel.add(Box.createRigidArea(new Dimension(20, 0)));

        // Set an initial size for gridPanel to ensure scrollbar appears
        gridPanel.setPreferredSize(new Dimension(100, 250));

        timer = new Timer(2100 - speedSlider.getValue(), new ActionListener() {
            int pageFaults = 0, pageHits = 0;
            int totalWidth = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (step < referenceString.length) {
                    int pageNumber = referenceString[step];

                    // Create a fixed-size column
                    JPanel columnPanel = new JPanel();
                    columnPanel.setLayout(new BorderLayout());
                    columnPanel.setPreferredSize(new Dimension(80, 250));
                    columnPanel.setMaximumSize(new Dimension(80, 250));
                    columnPanel.setMinimumSize(new Dimension(80, 250));
                    columnPanel.setBorder(BorderFactory.createLineBorder(Color.GREEN, 1));
                    columnPanel.setBackground(new Color(30, 30, 30));

                    JPanel pageContainer = new JPanel(new GridLayout(frameSize, 1));
                    pageContainer.setBackground(new Color(30, 30, 30));

                    boolean isHit = false;
                    int pageIndex = -1;
                    for (int i = 0; i < frames.size(); i++) {
                        if (frames.get(i).number == pageNumber) {
                            isHit = true;
                            pageIndex = i;
                            break;
                        }
                    }

                    JLabel statusLabel = new JLabel(isHit ? "Hit" : "Miss", SwingConstants.CENTER);
                    statusLabel.setFont(new Font("Arial", Font.BOLD, 16));
                    statusLabel.setForeground(isHit ? Color.GREEN : Color.RED);

                    JLabel clockHandLabel = new JLabel("Clock: " + clockHand, SwingConstants.CENTER);
                    clockHandLabel.setFont(new Font("Arial", Font.BOLD, 14));
                    clockHandLabel.setForeground(Color.YELLOW);

                    if (isHit) {
                        // Set reference bit to 1 for the hit page
                        referenceBits[pageIndex] = true;
                        pageHits++;
                    } else {
                        // Page fault - need to use Enhanced Second-Chance Algorithm
                        pageFaults++;

                        if (frames.size() < frameSize) {
                            // If there's space, just add the page
                            frames.add(new Page(pageNumber));
                            referenceBits[frames.size() - 1] = true;
                            modifyBits[frames.size() - 1] = false; // Assume it's not modified initially
                        } else {
                            // Enhanced Second-Chance Algorithm logic
                            boolean replaced = false;
                            while (!replaced) {
                                int rBit = referenceBits[clockHand] ? 1 : 0;
                                int mBit = modifyBits[clockHand] ? 1 : 0;

                                if (rBit == 0 && mBit == 0) {
                                    // (0,0) Best case - replace immediately
                                    frames.set(clockHand, new Page(pageNumber));
                                    referenceBits[clockHand] = true;
                                    modifyBits[clockHand] = false;
                                    replaced = true;
                                } else if (rBit == 0 && mBit == 1) {
                                    // (0,1) Write to disk then replace
                                    frames.set(clockHand, new Page(pageNumber));
                                    referenceBits[clockHand] = true;
                                    modifyBits[clockHand] = false;
                                    replaced = true;
                                } else {
                                    // (1,X) Clear R-bit and move forward
                                    referenceBits[clockHand] = false;
                                    clockHand = (clockHand + 1) % frameSize;
                                }
                            }
                        }
                    }

                    // Create frame display with reference bits
                    JPanel labelPanel = new JPanel(new BorderLayout());
                    labelPanel.add(statusLabel, BorderLayout.SOUTH);
                    labelPanel.add(clockHandLabel, BorderLayout.NORTH);
                    labelPanel.setOpaque(false);

                    for (int i = 0; i < frameSize; i++) {
                        JPanel framePanel = new JPanel(new BorderLayout());
                        framePanel.setBackground(new Color(30, 30, 30));

                        if (i < frames.size()) {
                            Page currentPage = frames.get(i);

                            // Display page number
                            JLabel pageLabel = new JLabel(String.valueOf(currentPage.number), SwingConstants.CENTER);
                            pageLabel.setFont(new Font("Arial", Font.BOLD, 18));
                            pageLabel.setForeground(Color.GREEN);

                            // Display reference and modify bits
                            JLabel refBitLabel = new JLabel(
                                    "R=" + (referenceBits[i] ? "1" : "0") + " M=" + (modifyBits[i] ? "1" : "0"),
                                    SwingConstants.CENTER);
                            refBitLabel.setFont(new Font("Arial", Font.PLAIN, 12));
                            refBitLabel.setForeground(referenceBits[i] ? Color.YELLOW : Color.ORANGE);

                            // Highlight the position of clock hand
                            if (i == clockHand) {
                                framePanel.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 1));
                            }

                            framePanel.add(pageLabel, BorderLayout.CENTER);
                            framePanel.add(refBitLabel, BorderLayout.SOUTH);
                        }

                        pageContainer.add(framePanel);
                    }

                    columnPanel.add(pageContainer, BorderLayout.CENTER);
                    columnPanel.add(labelPanel, BorderLayout.SOUTH);

                    gridPanel.add(columnPanel);
                    gridPanel.add(Box.createRigidArea(new Dimension(10, 0))); // Add spacing between columns

                    // Update total width and set as preferred size
                    totalWidth += 90; // 80 for panel + 10 for spacing
                    gridPanel.setPreferredSize(new Dimension(totalWidth, 250));

                    // Ensure the newest column is visible by scrolling to it
                    SwingUtilities.invokeLater(() -> {
                        JScrollBar horizontalScrollBar = scrollPane.getHorizontalScrollBar();
                        horizontalScrollBar.setValue(horizontalScrollBar.getMaximum());
                        scrollPane.revalidate();
                    });

                    gridPanel.revalidate();
                    gridPanel.repaint();
                    pageFaultLabel.setText("Total Page Faults: " + pageFaults);
                    pageHitLabel.setText("Total Page Hits: " + pageHits);
                    step++;
                } else {
                    timer.stop();
                }
            }
        });
        timer.start();
    }

    private void stopSimulation() {
        if (timer != null && timer.isRunning()) {
            timer.stop();
        }
    }

    // Helper method to create a button with icons for different states
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
