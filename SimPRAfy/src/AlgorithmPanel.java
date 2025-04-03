import javax.swing.*;
import java.awt.*;
import java.util.*;
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
    private Queue<Integer> frames; // For FIFO
    private LinkedList<Integer> lruList; // For LRU
    private HashMap<Integer, Integer> pageMap; // For Second Chance and Enhanced Second Chance
    private HashMap<Integer, Boolean> referenceMap; // For Enhanced Second Chance (reference bit)
    private HashMap<Integer, Boolean> modifiedMap; // For Enhanced Second Chance (modified bit)
    private int clockPointer; // For Second Chance algorithms
    private HashMap<Integer, Integer> frequencyMap; // For LFU and MFU

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
        lruList = new LinkedList<>();
        pageMap = new HashMap<>();
        referenceMap = new HashMap<>();
        modifiedMap = new HashMap<>();
        clockPointer = 0;
        frequencyMap = new HashMap<>();
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
        if (frames.contains(page)) {
            pageHits++;
            return true;
        } else {
            pageFaults++;
            if (frames.size() >= frameSize) {
                frames.poll(); // Remove oldest page
            }
            frames.add(page);
            return false;
        }
    }
    
    private boolean executeLru(int page) {
        boolean isHit = lruList.contains(page);
        
        // Remove page if it exists (to update its position)
        lruList.remove((Integer) page);
        
        // Add page to the end (most recently used position)
        lruList.add(page);
        
        // If cache is full and it was a miss, remove least recently used
        if (!isHit) {
            pageFaults++;
            if (lruList.size() > frameSize) {
                lruList.removeFirst(); // Remove LRU page
            }
        } else {
            pageHits++;
        }
        
        // Update frames to match LRU state
        frames.clear();
        frames.addAll(lruList);
        
        return isHit;
    }
    
    private boolean executeOpt(int page) {
        // Check if page already in frames
        ArrayList<Integer> optFrames = new ArrayList<>(frames);
        
        if (optFrames.contains(page)) {
            pageHits++;
            return true;
        }
        
        pageFaults++;
        
        // If frames not full, add page
        if (optFrames.size() < frameSize) {
            optFrames.add(page);
        } else {
            // Find page to replace - one that won't be used for longest
            int farthestIndex = -1;
            int replaceIndex = 0;
            
            for (int i = 0; i < optFrames.size(); i++) {
                int currentPage = optFrames.get(i);
                int nextIndex = findNextUse(currentPage, currentStep + 1);
                
                if (nextIndex == -1) {
                    replaceIndex = i; // Page never used again, replace it
                    break;
                }
                
                if (nextIndex > farthestIndex) {
                    farthestIndex = nextIndex;
                    replaceIndex = i;
                }
            }
            
            optFrames.set(replaceIndex, page);
        }
        
        // Update frames queue to match optimal state
        frames.clear();
        frames.addAll(optFrames);
        
        return false;
    }
    
    private int findNextUse(int page, int startIndex) {
        for (int i = startIndex; i < referenceString.length; i++) {
            if (referenceString[i] == page) {
                return i;
            }
        }
        return -1; // Not found
    }
    
    private boolean executeSecondChance(int page) {
        // Check if page is in frames
        if (pageMap.containsKey(page)) {
            // Set reference bit to 1
            pageMap.put(page, 1);
            pageHits++;
            return true;
        }
        
        pageFaults++;
        
        // Convert frames to list for easier manipulation
        ArrayList<Integer> framesList = new ArrayList<>(frames);
        
        // If frames not full
        if (framesList.size() < frameSize) {
            framesList.add(page);
            pageMap.put(page, 1); // Set reference bit to 1
        } else {
            // Second chance algorithm
            while (true) {
                int victimPage = framesList.get(clockPointer);
                
                if (pageMap.get(victimPage) == 0) {
                    // Replace this page
                    framesList.set(clockPointer, page);
                    pageMap.remove(victimPage);
                    pageMap.put(page, 1); // Set reference bit for new page
                    
                    // Move pointer for next victim
                    clockPointer = (clockPointer + 1) % frameSize;
                    break;
                } else {
                    // Give second chance
                    pageMap.put(victimPage, 0); // Clear reference bit
                    clockPointer = (clockPointer + 1) % frameSize;
                }
            }
        }
        
        // Update frames
        frames.clear();
        frames.addAll(framesList);
        
        return false;
    }
    
    private boolean executeEnhancedSecondChance(int page) {
        // Check if page is in frames
        ArrayList<Integer> framesList = new ArrayList<>(frames);
        
        if (framesList.contains(page)) {
            // Set reference bit to 1, simulate a random modified bit (0 or 1)
            referenceMap.put(page, true);
            if (!modifiedMap.containsKey(page)) {
                modifiedMap.put(page, Math.random() > 0.5);
            }
            pageHits++;
            return true;
        }
        
        pageFaults++;
        
        // If frames not full
        if (framesList.size() < frameSize) {
            framesList.add(page);
            referenceMap.put(page, true); // Set reference bit to 1
            modifiedMap.put(page, Math.random() > 0.5); // Random modified bit
        } else {
            // Enhanced Second Chance (Clock) algorithm
            // Look for (0,0) pages - not referenced, not modified
            boolean replaced = false;
            int startPointer = clockPointer;
            
            // First pass: look for (0,0) - not referenced, not modified
            do {
                int victimPage = framesList.get(clockPointer);
                boolean referenced = referenceMap.getOrDefault(victimPage, false);
                boolean modified = modifiedMap.getOrDefault(victimPage, false);
                
                if (!referenced && !modified) {
                    // Found (0,0) page
                    framesList.set(clockPointer, page);
                    referenceMap.remove(victimPage);
                    modifiedMap.remove(victimPage);
                    referenceMap.put(page, true);
                    modifiedMap.put(page, Math.random() > 0.5);
                    replaced = true;
                    break;
                }
                
                clockPointer = (clockPointer + 1) % frameSize;
            } while (clockPointer != startPointer);
            
            // Second pass: look for (0,1) - not referenced, modified
            if (!replaced) {
                clockPointer = startPointer;
                do {
                    int victimPage = framesList.get(clockPointer);
                    boolean referenced = referenceMap.getOrDefault(victimPage, false);
                    boolean modified = modifiedMap.getOrDefault(victimPage, false);
                    
                    if (!referenced && modified) {
                        // Found (0,1) page
                        framesList.set(clockPointer, page);
                        referenceMap.remove(victimPage);
                        modifiedMap.remove(victimPage);
                        referenceMap.put(page, true);
                        modifiedMap.put(page, Math.random() > 0.5);
                        replaced = true;
                        break;
                    }
                    
                    clockPointer = (clockPointer + 1) % frameSize;
                } while (clockPointer != startPointer);
            }
            
            // Third pass: look for (1,0) - referenced, not modified
            if (!replaced) {
                clockPointer = startPointer;
                do {
                    int victimPage = framesList.get(clockPointer);
                    boolean referenced = referenceMap.getOrDefault(victimPage, false);
                    boolean modified = modifiedMap.getOrDefault(victimPage, false);
                    
                    if (referenced && !modified) {
                        // Clear reference bit and move to next frame
                        referenceMap.put(victimPage, false);
                    }
                    
                    clockPointer = (clockPointer + 1) % frameSize;
                } while (clockPointer != startPointer);
                
                // Now retry for (0,0) pages
                clockPointer = startPointer;
                do {
                    int victimPage = framesList.get(clockPointer);
                    boolean referenced = referenceMap.getOrDefault(victimPage, false);
                    boolean modified = modifiedMap.getOrDefault(victimPage, false);
                    
                    if (!referenced && !modified) {
                        // Found (0,0) page after clearing reference bits
                        framesList.set(clockPointer, page);
                        referenceMap.remove(victimPage);
                        modifiedMap.remove(victimPage);
                        referenceMap.put(page, true);
                        modifiedMap.put(page, Math.random() > 0.5);
                        replaced = true;
                        break;
                    }
                    
                    clockPointer = (clockPointer + 1) % frameSize;
                } while (clockPointer != startPointer);
            }
            
            // Fourth pass: look for (1,1) or take any frame
            if (!replaced) {
                // Just replace the current frame and move pointer
                int victimPage = framesList.get(clockPointer);
                framesList.set(clockPointer, page);
                referenceMap.remove(victimPage);
                modifiedMap.remove(victimPage);
                referenceMap.put(page, true);
                modifiedMap.put(page, Math.random() > 0.5);
            }
            
            // Move pointer for next victim
            clockPointer = (clockPointer + 1) % frameSize;
        }
        
        // Update frames
        frames.clear();
        frames.addAll(framesList);
        
        return false;
    }
    
    private boolean executeLfu(int page) {
        ArrayList<Integer> framesList = new ArrayList<>(frames);
        
        // Check if page is in frames
        if (framesList.contains(page)) {
            // Increment frequency counter for the page
            frequencyMap.put(page, frequencyMap.getOrDefault(page, 0) + 1);
            pageHits++;
            return true;
        }
        
        pageFaults++;
        
        // Increment frequency counter for the new page
        frequencyMap.put(page, frequencyMap.getOrDefault(page, 0) + 1);
        
        // If frames not full
        if (framesList.size() < frameSize) {
            framesList.add(page);
        } else {
            // Find least frequently used page
            int minFreq = Integer.MAX_VALUE;
            int victimIndex = -1;
            
            for (int i = 0; i < framesList.size(); i++) {
                int currentPage = framesList.get(i);
                int freq = frequencyMap.getOrDefault(currentPage, 0);
                
                if (freq < minFreq) {
                    minFreq = freq;
                    victimIndex = i;
                }
            }
            
            // Replace the victim page
            framesList.set(victimIndex, page);
        }
        
        // Update frames
        frames.clear();
        frames.addAll(framesList);
        
        return false;
    }
    
    private boolean executeMfu(int page) {
        ArrayList<Integer> framesList = new ArrayList<>(frames);
        
        // Check if page is in frames
        if (framesList.contains(page)) {
            // Increment frequency counter for the page
            frequencyMap.put(page, frequencyMap.getOrDefault(page, 0) + 1);
            pageHits++;
            return true;
        }
        
        pageFaults++;
        
        // Increment frequency counter for the new page
        frequencyMap.put(page, frequencyMap.getOrDefault(page, 0) + 1);
        
        // If frames not full
        if (framesList.size() < frameSize) {
            framesList.add(page);
        } else {
            // Find most frequently used page
            int maxFreq = -1;
            int victimIndex = -1;
            
            for (int i = 0; i < framesList.size(); i++) {
                int currentPage = framesList.get(i);
                int freq = frequencyMap.getOrDefault(currentPage, 0);
                
                if (freq > maxFreq) {
                    maxFreq = freq;
                    victimIndex = i;
                }
            }
            
            // Replace the victim page
            framesList.set(victimIndex, page);
        }
        
        // Update frames
        frames.clear();
        frames.addAll(framesList);
        
        return false;
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
        
        // Create a consistent frame display
        ArrayList<Integer> currentFrames = new ArrayList<>(frames);
        for (int i = 0; i < frameSize; i++) {
            JLabel cell = new JLabel("", SwingConstants.CENTER);
            cell.setForeground(Color.GREEN);
            cell.setFont(new Font("Arial", Font.BOLD, 14));
            
            if (i < currentFrames.size()) {
                cell.setText(String.valueOf(currentFrames.get(i)));
            }
            
            framePanel.add(cell);
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