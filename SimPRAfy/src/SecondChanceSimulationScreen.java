import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;

import java.awt.event.MouseEvent;

import java.awt.event.MouseAdapter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class SecondChanceSimulationScreen extends JPanel {
    private JTextField refInput, frameInput;
    private JPanel gridPanel;
    private JLabel pageFaultLabel, pageHitLabel;
    private JSlider speedSlider;
    private Timer timer;
    private int[] referenceString;
    private int frameSize;
    private List<PageFrame> frames;
    private int pointer; // Pointer to the next candidate for replacement
    private int step;
    private JScrollPane scrollPane;

    // Class to represent a page frame with a reference bit
    private static class PageFrame {
        int pageNumber;
        boolean referenceBit;

        public PageFrame(int pageNumber) {
            this.pageNumber = pageNumber;
            this.referenceBit = false;
        }
    }

    public SecondChanceSimulationScreen(CardLayout layout, JPanel mainPanel) {
        this.setLayout(null);
        this.setBackground(new Color(2, 13, 25));
        this.setPreferredSize(new Dimension(1500, 844));

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
        frameInput.setBounds(1225, 30, 50, 30);
        frameInput.setBorder(BorderFactory.createLineBorder(Color.GREEN, 1));
        this.add(frameInput);

        // Simulation Title
        JLabel title = new JLabel("SECOND CHANCE ALGORITHM", SwingConstants.CENTER);
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

        // Pointer Label
        JLabel pointerLabel = new JLabel("Pointer Position: 0");
        pointerLabel.setForeground(Color.WHITE);
        pointerLabel.setBounds(688, 675, 300, 30);
        pointerLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        this.add(pointerLabel);

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
                CommonConstants.startHoveSIM, CommonConstants.startClickSIM, new Dimension(250, 75));
        startButton.setBounds(610, 710, 250, 75);
        this.add(startButton);
        JButton stopButton = createStyledButton(CommonConstants.stopDefaultSIM,
                CommonConstants.stopHoveSIM, CommonConstants.stopClickSIM, new Dimension(250, 75));
        stopButton.setBounds(870, 710, 250, 75);
        this.add(stopButton);
        JButton saveButton = createStyledButton(CommonConstants.saveDefaultSIM,
                CommonConstants.saveHoveSIM, CommonConstants.saveClickSIM, new Dimension(250, 75));
        saveButton.setBounds(1130, 710, 250, 75);
        this.add(saveButton);

        startButton.addActionListener(e -> startSimulation());
        stopButton.addActionListener(e -> stopSimulation());
    }

    private void startSimulation() {
        try {
            String[] refStrArr = refInput.getText().trim().split(" ");
            referenceString = new int[refStrArr.length];
            for (int i = 0; i < refStrArr.length; i++) {
                referenceString[i] = Integer.parseInt(refStrArr[i]);
            }
            frameSize = Integer.parseInt(frameInput.getText().trim());
            
            if (frameSize <= 0) {
                JOptionPane.showMessageDialog(this, "Frame size must be greater than 0", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid Input! Please enter numbers only.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Stop any existing timer
        if (timer != null && timer.isRunning()) {
            timer.stop();
        }

        frames = new ArrayList<>();
        pointer = 0;
        step = 0;
        gridPanel.removeAll();
        gridPanel.revalidate();
        gridPanel.repaint();

        gridPanel.add(Box.createRigidArea(new Dimension(20, 0)));

        // Set an initial size for gridPanel to ensure scrollbar appears
        gridPanel.setPreferredSize(new Dimension(100, 250));

        // Reset the labels
        pageFaultLabel.setText("Total Page Faults: 0");
        pageHitLabel.setText("Total Page Hits: 0");
        ((JLabel) this.getComponent(6)).setText("Pointer Position: 0");

        timer = new Timer(2100 - speedSlider.getValue(), new ActionListener() {
            int pageFaults = 0, pageHits = 0;
            int totalWidth = 20; // Start with some initial padding

            @Override
            public void actionPerformed(ActionEvent e) {
                if (step < referenceString.length) {
                    int page = referenceString[step];

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

                    // Check if page is already in frames
                    boolean isHit = false;
                    for (PageFrame frame : frames) {
                        if (frame.pageNumber == page) {
                            isHit = true;
                            frame.referenceBit = true; // Set reference bit for second chance
                            break;
                        }
                    }

                    JLabel referenceLabel = new JLabel("Ref: " + page, SwingConstants.CENTER);
                    referenceLabel.setFont(new Font("Arial", Font.BOLD, 14));
                    referenceLabel.setForeground(Color.WHITE);
                    columnPanel.add(referenceLabel, BorderLayout.NORTH);

                    JLabel statusLabel = new JLabel(isHit ? "Hit" : "Miss", SwingConstants.CENTER);
                    statusLabel.setFont(new Font("Arial", Font.BOLD, 16));
                    statusLabel.setForeground(isHit ? Color.GREEN : Color.RED);

                    if (isHit) {
                        pageHits++;
                    } else {
                        pageFaults++;
                        
                        // If frames are full, find page to replace using second chance algorithm
                        if (frames.size() >= frameSize) {
                            // Find a victim frame according to second chance algorithm
                            boolean victimFound = false;
                            int startPointer = pointer;
                            
                            do {
                                // If reference bit is 0, replace this page
                                if (!frames.get(pointer).referenceBit) {
                                    frames.set(pointer, new PageFrame(page));
                                    int nextPointer = (pointer + 1) % frameSize;
                                    pointer = nextPointer; // Move pointer to next frame
                                    victimFound = true;
                                    break;
                                } else {
                                    // Give second chance by resetting reference bit
                                    frames.get(pointer).referenceBit = false;
                                    pointer = (pointer + 1) % frameSize; // Move pointer
                                }
                            } while (pointer != startPointer && !victimFound);
                            
                            // If we've gone full circle and still haven't found a victim,
                            // replace the current one (this shouldn't happen if the algorithm works correctly)
                            if (!victimFound) {
                                frames.set(pointer, new PageFrame(page));
                                pointer = (pointer + 1) % frameSize;
                            }
                        } else {
                            // If frames aren't full yet, just add the page
                            frames.add(new PageFrame(page));
                        }
                    }

                    // Update pointer label
                    ((JLabel) SecondChanceSimulationScreen.this.getComponent(6)).setText("Pointer Position: " + pointer);

                    // Create cells for all frames
                    for (int i = 0; i < frameSize; i++) {
                        JPanel cellPanel = new JPanel(new BorderLayout());
                        cellPanel.setBackground(new Color(30, 30, 30));
                        
                        JLabel pageLabel = new JLabel("", SwingConstants.CENTER);
                        pageLabel.setForeground(Color.WHITE);
                        pageLabel.setFont(new Font("Arial", Font.BOLD, 20));
                        cellPanel.add(pageLabel, BorderLayout.CENTER);
                        
                        // Add reference bit indicator
                        JLabel refBitLabel = new JLabel("", SwingConstants.CENTER);
                        refBitLabel.setForeground(Color.YELLOW);
                        refBitLabel.setFont(new Font("Arial", Font.PLAIN, 12));
                        cellPanel.add(refBitLabel, BorderLayout.SOUTH);
                        
                        // Add border to indicate cell
                        cellPanel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));
                        
                        pageContainer.add(cellPanel);
                    }
                    
                    // Fill in the data for frames that exist
                    for (int i = 0; i < frames.size(); i++) {
                        JPanel cellPanel = (JPanel)pageContainer.getComponent(i);
                        JLabel pageLabel = (JLabel)cellPanel.getComponent(0);
                        JLabel refBitLabel = (JLabel)cellPanel.getComponent(1);
                        
                        pageLabel.setText(String.valueOf(frames.get(i).pageNumber));
                        refBitLabel.setText("R=" + (frames.get(i).referenceBit ? "1" : "0"));
                        
                        // Highlight the current pointer position
                        if (i == pointer) {
                            cellPanel.setBorder(BorderFactory.createLineBorder(Color.ORANGE, 2));
                        }
                    }

                    columnPanel.add(pageContainer, BorderLayout.CENTER);
                    columnPanel.add(statusLabel, BorderLayout.SOUTH);

                    gridPanel.add(columnPanel);
                    gridPanel.add(Box.createRigidArea(new Dimension(10, 0))); // Add spacing between columns

                    // Update total width and set as preferred size
                    totalWidth += 90; // 80 for panel + 10 for spacing
                    gridPanel.setPreferredSize(new Dimension(Math.max(totalWidth, 100), 250));

                    // Ensure the newest column is visible by scrolling to it
                    SwingUtilities.invokeLater(() -> {
                        JScrollBar horizontalScrollBar = scrollPane.getHorizontalScrollBar();
                        horizontalScrollBar.setValue(horizontalScrollBar.getMaximum());
                        scrollPane.revalidate();
                        scrollPane.repaint();
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
}