import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.Queue;

/**
 * Panel representing a single page replacement algorithm visualization
 */
public class AlgorithmPanel extends JPanel {
    private String algorithmName;
    private int frameSize;
    private int[] referenceString;
    private JPanel gridPanel;
    private JLabel titleLabel;
    private JLabel pageFaultLabel;
    private JLabel pageHitLabel;
    private JScrollBar horizontalScrollBar;

    // Algorithm-specific state
    private int currentStep;
    private int pageFaults;
    private int pageHits;
    private LinkedList<Integer> frames; // Used for all algorithms to maintain frames order
    private HashMap<Integer, Integer> referenceMap; // For Second Chance and Enhanced Second Chance (reference bit)
    private HashMap<Integer, Boolean> modifyMap; // For Enhanced Second Chance (modify bit)
    private int clockPointer; // For Second Chance algorithms
    private HashMap<Integer, Integer> frequencyMap; // For LFU and MFU
    private HashMap<Integer, Long> accessTimeMap; // For LRU (timestamp of last access)

    public AlgorithmPanel(String algorithmName, int frameSize) {
        this.algorithmName = algorithmName;
        this.frameSize = frameSize;
        this.currentStep = 0;
        this.pageFaults = 0;
        this.pageHits = 0;

        // Initialize algorithm-specific data structures
        initializeDataStructures();

        // Setup the UI
        setupUI();
    }

    private void initializeDataStructures() {
        frames = new LinkedList<>();
        referenceMap = new HashMap<>();
        modifyMap = new HashMap<>();
        clockPointer = 0;
        frequencyMap = new HashMap<>();
        accessTimeMap = new HashMap<>();
    }

    private void setupUI() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createLineBorder(Color.GREEN, 2));
        setBackground(new Color(2, 13, 25));

        // Title
        titleLabel = new JLabel(algorithmName, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(Color.WHITE);
        add(titleLabel, BorderLayout.NORTH);

        // Center grid for frames visualization
        gridPanel = new JPanel();
        gridPanel.setLayout(new BoxLayout(gridPanel, BoxLayout.X_AXIS));
        gridPanel.setBackground(new Color(2, 13, 25));

        JScrollPane scrollPane = new JScrollPane(gridPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.GREEN, 5));
        scrollPane.getViewport().setBackground(new Color(2, 13, 25));
        scrollPane.setBorder(null);

        horizontalScrollBar = scrollPane.getHorizontalScrollBar();

        add(scrollPane, BorderLayout.CENTER);

        // Stats panel at the bottom
        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
        statsPanel.setBackground(new Color(2, 13, 25));

        pageFaultLabel = new JLabel("Page Faults: 0");
        pageFaultLabel.setForeground(Color.WHITE);
        statsPanel.add(pageFaultLabel);

        pageHitLabel = new JLabel("Page Hits: 0");
        pageHitLabel.setForeground(Color.WHITE);
        statsPanel.add(pageHitLabel);

        add(statsPanel, BorderLayout.SOUTH);
    }

    public void reset(int frameSize, int[] referenceString) {
        this.frameSize = frameSize;
        this.referenceString = referenceString;
        this.currentStep = 0;
        this.pageFaults = 0;
        this.pageHits = 0;

        // Reset algorithm data structures
        initializeDataStructures();

        // Clear the grid
        gridPanel.removeAll();
        gridPanel.revalidate();
        gridPanel.repaint();

        // Reset labels
        pageFaultLabel.setText("Page Faults: 0");
        pageHitLabel.setText("Page Hits: 0");
    }

    public void step() {
        if (currentStep < referenceString.length) {
            int page = referenceString[currentStep];
            boolean isHit = false;

            // Execute the appropriate algorithm
            switch (algorithmName) {
                case "FIFO":
                    isHit = executeFifo(page);
                    break;
                case "LRU":
                    isHit = executeLru(page);
                    break;
                case "OPT":
                    isHit = executeOpt(page);
                    break;
                case "SC": // Second Chance
                    isHit = executeSecondChance(page);
                    break;
                case "ESC": // Enhanced Second Chance
                    isHit = executeEnhancedSecondChance(page);
                    break;
                case "LFU":
                    isHit = executeLfu(page);
                    break;
                case "MFU":
                    isHit = executeMfu(page);
                    break;
            }

            // Update UI with current frame state
            updateUI(page, isHit);

            // Scroll to show latest
            SwingUtilities.invokeLater(() -> {
                horizontalScrollBar.setValue(horizontalScrollBar.getMaximum());
            });

            currentStep++;
        }
    }

    private boolean executeFifo(int page) {
        // Check if page is already in frames
        if (frames.contains(page)) {
            pageHits++;
            return true;
        } else {
            pageFaults++;
            // If frames full, remove oldest page (first in)
            if (frames.size() >= frameSize) {
                frames.removeFirst();
            }
            // Add new page at the end
            frames.addLast(page);
            return false;
        }
    }

    private boolean executeLru(int page) {
        // Update the access time for this page
        long currentTime = System.nanoTime();

        // Check if page is already in frames
        boolean isHit = frames.contains(page);
        if (isHit) {
            // Update access time and move the page to the end (most recently used)
            frames.remove((Integer) page);
            frames.addLast(page);
            accessTimeMap.put(page, currentTime);
            pageHits++;
        } else {
            pageFaults++;
            // If frames full, remove least recently used page
            if (frames.size() >= frameSize) {
                // Find least recently used page
                long oldestTime = Long.MAX_VALUE;
                int lruPage = -1;

                for (int p : frames) {
                    long accessTime = accessTimeMap.getOrDefault(p, 0L);
                    if (accessTime < oldestTime) {
                        oldestTime = accessTime;
                        lruPage = p;
                    }
                }

                // Remove LRU page
                if (lruPage != -1) {
                    frames.remove((Integer) lruPage);
                    accessTimeMap.remove(lruPage);
                }
            }

            // Add new page
            frames.addLast(page);
            accessTimeMap.put(page, currentTime);
        }

        return isHit;
    }

    private boolean executeOpt(int page) {
        // Check if page is already in frames
        if (frames.contains(page)) {
            pageHits++;
            return true;
        } else {
            pageFaults++;

            // If frames not full, add page
            if (frames.size() < frameSize) {
                frames.addLast(page);
            } else {
                // Find page to replace - one that won't be used for longest
                int farthestPage = -1;
                int farthestDistance = -1;

                for (int currentPage : frames) {
                    int nextUseDistance = Integer.MAX_VALUE;

                    // Find next use of this page
                    for (int i = currentStep + 1; i < referenceString.length; i++) {
                        if (referenceString[i] == currentPage) {
                            nextUseDistance = i - currentStep;
                            break;
                        }
                    }

                    // Update farthest page if this one is used later
                    if (nextUseDistance > farthestDistance) {
                        farthestDistance = nextUseDistance;
                        farthestPage = currentPage;
                    }
                }

                // If found a page to replace
                if (farthestPage != -1) {
                    frames.remove((Integer) farthestPage);
                    frames.addLast(page);
                } else {
                    // If no page found (shouldn't happen normally), replace first
                    frames.removeFirst();
                    frames.addLast(page);
                }
            }

            return false;
        }
    }

    private boolean executeSecondChance(int page) {
        // Check if page is already in frames
        if (frames.contains(page)) {
            // Set reference bit to 1 for this page
            referenceMap.put(page, 1);
            pageHits++;
            return true;
        } else {
            pageFaults++;

            // If frames not full
            if (frames.size() < frameSize) {
                frames.addLast(page);
                referenceMap.put(page, 1); // Set reference bit to 1 for new page
            } else {
                // Initialize clock pointer if needed
                if (clockPointer >= frames.size()) {
                    clockPointer = 0;
                }

                // Second chance algorithm
                while (true) {
                    int victimPage = frames.get(clockPointer);
                    int refBit = referenceMap.getOrDefault(victimPage, 0);

                    if (refBit == 0) {
                        // No second chance, replace this page
                        frames.remove(clockPointer);
                        frames.add(clockPointer, page);
                        referenceMap.remove(victimPage);
                        referenceMap.put(page, 1); // Set reference bit for new page

                        // Move pointer
                        clockPointer = (clockPointer + 1) % frameSize;
                        break;
                    } else {
                        // Give second chance by clearing reference bit
                        referenceMap.put(victimPage, 0);

                        // Move to next frame
                        clockPointer = (clockPointer + 1) % frameSize;
                    }
                }
            }

            return false;
        }
    }

    private boolean executeEnhancedSecondChance(int page) {
        // Generate random modify bit for simulation if accessing a page
        boolean isModified = Math.random() > 0.5;

        // Check if page is already in frames
        if (frames.contains(page)) {
            // Set reference bit to 1 for this page
            referenceMap.put(page, 1);
            // Update modify bit based on probability
            if (isModified) {
                modifyMap.put(page, true);
            }
            pageHits++;
            return true;
        } else {
            pageFaults++;

            // If frames not full
            if (frames.size() < frameSize) {
                frames.addLast(page);
                referenceMap.put(page, 1); // Set reference bit to 1 for new page
                modifyMap.put(page, isModified); // Set modify bit based on probability
            } else {
                // Enhanced Second Chance algorithm
                int startPointer = clockPointer;
                boolean replaced = false;

                // First pass: find (0,0) - not referenced, not modified
                for (int i = 0; i < frameSize && !replaced; i++) {
                    int index = (startPointer + i) % frameSize;
                    int victimPage = frames.get(index);
                    int refBit = referenceMap.getOrDefault(victimPage, 0);
                    boolean modBit = modifyMap.getOrDefault(victimPage, false);

                    if (refBit == 0 && !modBit) {
                        // Found (0,0) page - replace it
                        frames.remove(index);
                        frames.add(index, page);
                        referenceMap.remove(victimPage);
                        modifyMap.remove(victimPage);
                        referenceMap.put(page, 1);
                        modifyMap.put(page, isModified);
                        clockPointer = (index + 1) % frameSize;
                        replaced = true;
                        break;
                    }
                }

                // Second pass: find (0,1) - not referenced, modified
                if (!replaced) {
                    for (int i = 0; i < frameSize && !replaced; i++) {
                        int index = (startPointer + i) % frameSize;
                        int victimPage = frames.get(index);
                        int refBit = referenceMap.getOrDefault(victimPage, 0);
                        boolean modBit = modifyMap.getOrDefault(victimPage, false);

                        if (refBit == 0 && modBit) {
                            // Found (0,1) page - replace it
                            frames.remove(index);
                            frames.add(index, page);
                            referenceMap.remove(victimPage);
                            modifyMap.remove(victimPage);
                            referenceMap.put(page, 1);
                            modifyMap.put(page, isModified);
                            clockPointer = (index + 1) % frameSize;
                            replaced = true;
                            break;
                        }
                    }
                }

                // Third pass: reset reference bits and look for (1,0)
                if (!replaced) {
                    for (int i = 0; i < frameSize; i++) {
                        int index = (startPointer + i) % frameSize;
                        int victimPage = frames.get(index);
                        int refBit = referenceMap.getOrDefault(victimPage, 0);
                        boolean modBit = modifyMap.getOrDefault(victimPage, false);

                        if (refBit == 1 && !modBit) {
                            // Clear reference bit
                            referenceMap.put(victimPage, 0);
                        }
                    }

                    // Try again for (0,0) pages
                    for (int i = 0; i < frameSize && !replaced; i++) {
                        int index = (startPointer + i) % frameSize;
                        int victimPage = frames.get(index);
                        int refBit = referenceMap.getOrDefault(victimPage, 0);
                        boolean modBit = modifyMap.getOrDefault(victimPage, false);

                        if (refBit == 0 && !modBit) {
                            // Found (0,0) page after clearing - replace it
                            frames.remove(index);
                            frames.add(index, page);
                            referenceMap.remove(victimPage);
                            modifyMap.remove(victimPage);
                            referenceMap.put(page, 1);
                            modifyMap.put(page, isModified);
                            clockPointer = (index + 1) % frameSize;
                            replaced = true;
                            break;
                        }
                    }
                }

                // Fourth pass: reset all reference bits and take any page (1,1)
                if (!replaced) {
                    // Reset all reference bits
                    for (int i = 0; i < frameSize; i++) {
                        int victimPage = frames.get(i);
                        referenceMap.put(victimPage, 0);
                    }

                    // Just replace current clock position
                    int victimPage = frames.get(clockPointer);
                    frames.remove(clockPointer);
                    frames.add(clockPointer, page);
                    referenceMap.remove(victimPage);
                    modifyMap.remove(victimPage);
                    referenceMap.put(page, 1);
                    modifyMap.put(page, isModified);
                    clockPointer = (clockPointer + 1) % frameSize;
                }
            }

            return false;
        }
    }

    private boolean executeLfu(int page) {
        // Increment frequency for accessed page
        frequencyMap.put(page, frequencyMap.getOrDefault(page, 0) + 1);

        // Check if page is already in frames
        if (frames.contains(page)) {
            pageHits++;
            return true;
        } else {
            pageFaults++;

            // If frames not full
            if (frames.size() < frameSize) {
                frames.addLast(page);
            } else {
                // Find least frequently used page
                int minFreq = Integer.MAX_VALUE;
                int lfuPage = -1;
                int lfuIndex = -1;

                for (int i = 0; i < frames.size(); i++) {
                    int p = frames.get(i);
                    int freq = frequencyMap.getOrDefault(p, 0);

                    if (freq < minFreq) {
                        minFreq = freq;
                        lfuPage = p;
                        lfuIndex = i;
                    }
                }

                // Replace least frequently used page
                if (lfuIndex != -1) {
                    frames.remove(lfuIndex);
                    frames.add(lfuIndex, page);
                }
            }

            return false;
        }
    }

    private boolean executeMfu(int page) {
        // Increment frequency for accessed page
        frequencyMap.put(page, frequencyMap.getOrDefault(page, 0) + 1);

        // Check if page is already in frames
        if (frames.contains(page)) {
            pageHits++;
            return true;
        } else {
            pageFaults++;

            // If frames not full
            if (frames.size() < frameSize) {
                frames.addLast(page);
            } else {
                // Find most frequently used page
                int maxFreq = -1;
                int mfuPage = -1;
                int mfuIndex = -1;

                for (int i = 0; i < frames.size(); i++) {
                    int p = frames.get(i);
                    int freq = frequencyMap.getOrDefault(p, 0);

                    if (freq > maxFreq) {
                        maxFreq = freq;
                        mfuPage = p;
                        mfuIndex = i;
                    }
                }

                // Replace most frequently used page
                if (mfuIndex != -1) {
                    frames.remove(mfuIndex);
                    frames.add(mfuIndex, page);
                }
            }

            return false;
        }
    }

    private void updateUI(int page, boolean isHit) {
        // Create a fixed-size column for current step
        JPanel columnPanel = new JPanel();
        columnPanel.setLayout(new BorderLayout());
        columnPanel.setPreferredSize(new Dimension(40, 150));
        columnPanel.setMaximumSize(new Dimension(40, 150));
        columnPanel.setMinimumSize(new Dimension(40, 150));
        columnPanel.setBorder(BorderFactory.createLineBorder(Color.GREEN, 1));
        columnPanel.setBackground(new Color(20, 20, 20));

        // Panel for frame contents
        JPanel framePanel = new JPanel(new GridLayout(frameSize, 1, 0, 1));
        framePanel.setBackground(new Color(20, 20, 20));

        // Create a header with page number
        JLabel pageLabel = new JLabel("Page " + page, SwingConstants.CENTER);
        pageLabel.setForeground(Color.YELLOW);
        pageLabel.setFont(new Font("Arial", Font.BOLD, 12));
        columnPanel.add(pageLabel, BorderLayout.NORTH);

        // Display each frame with reference & modify bits when relevant
        for (int i = 0; i < frameSize; i++) {
            JPanel cellPanel = new JPanel(new BorderLayout());
            cellPanel.setBackground(new Color(20, 20, 20));

            JLabel cell = new JLabel("", SwingConstants.CENTER);
            cell.setForeground(Color.GREEN);
            cell.setFont(new Font("Arial", Font.BOLD, 14));

            // Show page number and bits for Second Chance algorithms
            if (i < frames.size()) {
                int frameValue = frames.get(i);
                cell.setText(String.valueOf(frameValue));

                // Add reference and modify bits for SC and ESC
                // Inside the updateUI method, modify the part where it adds reference and
                // modify bits:

                // Add reference and modify bits for SC and ESC
                if (algorithmName.equals("SC")) {
                    int refBit = referenceMap.getOrDefault(frameValue, 0);
                    // Make the reference bit part of the main cell text instead of a separate label
                    cell.setText(frameValue + " (R:" + refBit + ")");
                    // Alternatively, use a distinct color for the frame with reference bit set
                    if (refBit == 1) {
                        cellPanel.setBackground(new Color(30, 50, 30)); // Slightly brighter green background
                    }
                } else if (algorithmName.equals("ESC")) {
                    int refBit = referenceMap.getOrDefault(frameValue, 0);
                    boolean modBit = modifyMap.getOrDefault(frameValue, false);
                    // Include the bits in the main cell text
                    cell.setText(frameValue + " (R:" + refBit + ",M:" + (modBit ? "1" : "0") + ")");
                    // Use color coding for different bit combinations
                    if (refBit == 1 && modBit) {
                        cellPanel.setBackground(new Color(50, 20, 20)); // Red tint for (1,1)
                    } else if (refBit == 1) {
                        cellPanel.setBackground(new Color(30, 50, 30)); // Green tint for (1,0)
                    } else if (modBit) {
                        cellPanel.setBackground(new Color(50, 50, 20)); // Yellow tint for (0,1)
                    }
                }

                // Highlight the clock pointer for SC and ESC
                if ((algorithmName.equals("SC") || algorithmName.equals("ESC")) && i == clockPointer) {
                    cellPanel.setBorder(BorderFactory.createLineBorder(Color.RED, 1));
                }

                // Show frequency for LFU/MFU
                if (algorithmName.equals("LFU") || algorithmName.equals("MFU")) {
                    int freq = frequencyMap.getOrDefault(frameValue, 0);
                    JLabel freqLabel = new JLabel("F:" + freq, SwingConstants.RIGHT);
                    freqLabel.setForeground(Color.ORANGE);
                    freqLabel.setFont(new Font("Arial", Font.PLAIN, 8));
                    cellPanel.add(freqLabel, BorderLayout.EAST);
                }
            }

            cellPanel.add(cell, BorderLayout.CENTER);
            framePanel.add(cellPanel);
        }

        columnPanel.add(framePanel, BorderLayout.CENTER);

        // Add hit/miss label
        JLabel statusLabel = new JLabel(isHit ? "Hit" : "Miss", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 10));
        statusLabel.setForeground(isHit ? Color.GREEN : Color.RED);
        columnPanel.add(statusLabel, BorderLayout.SOUTH);

        // Add to grid
        gridPanel.add(columnPanel);
        gridPanel.add(Box.createRigidArea(new Dimension(2, 0))); // Small gap

        // Update stats
        pageFaultLabel.setText("Page Faults: " + pageFaults);
        pageHitLabel.setText("Page Hits: " + pageHits);

        // Update UI
        gridPanel.revalidate();
        gridPanel.repaint();
    }
}