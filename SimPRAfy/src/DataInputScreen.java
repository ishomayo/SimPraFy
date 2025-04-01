import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DataInputScreen extends JPanel {
    private JPanel mainPanel;
    private CardLayout layout;
    private Image backgroundImage;
    private App app;
    private JPanel customPanel;
    
    public DataInputScreen(App app, CardLayout layout, JPanel mainPanel) {
        this.app = app;
        this.layout = layout;
        this.mainPanel = mainPanel;

        backgroundImage = new ImageIcon(CommonConstants.inputMethod).getImage();

        setLayout(new BorderLayout());
        showDataInputSelection();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
        setOpaque(false);
    }

    public void showDataInputSelection() {
        this.setLayout(null);

        // Create and position buttons
        JButton randomButton = createStyledButtonDATAINPUT(CommonConstants.randomDefault, CommonConstants.randomHover, CommonConstants.randomHover);
        JButton userInputButton = createStyledButtonDATAINPUT(CommonConstants.userInputDefault, CommonConstants.userInputHover, CommonConstants.userInputHover);
        JButton fileInputButton = createStyledButtonDATAINPUT(CommonConstants.fileDefault, CommonConstants.fileHover, CommonConstants.fileHover);
        JButton backButton = createStyledButton(CommonConstants.backDefault, CommonConstants.backClick, CommonConstants.backClick);

        positionButtons(randomButton, userInputButton, fileInputButton, backButton);

        // Add buttons to the panel
        this.add(randomButton);
        this.add(userInputButton);
        this.add(fileInputButton);
        this.add(backButton);

        // Back button functionality
        backButton.addActionListener(e -> {
            resetCustomPanel();
            layout.show(mainPanel, "Lobby");
        });

        // Add custom panel with components
        customPanel = createCustomPanel();
        customPanel.setEnabled(false); // Disable custom panel initially
        customPanel.setVisible(false); // Hide custom panel initially
        this.add(customPanel);

        // Enable/Disable custom panel based on button selection
        randomButton.addActionListener(e -> enableCustomPanel(true)); // When a button is selected, enable the customPanel
        userInputButton.addActionListener(e -> enableCustomPanel(true));
        fileInputButton.addActionListener(e -> enableCustomPanel(true));

        // Refresh layout and repaint
        this.revalidate();
        this.repaint();
    }

    private void positionButtons(JButton randomButton, JButton userInputButton, JButton fileInputButton, JButton backButton) {
        int x = 210, y = 350, spacing = 130;
        randomButton.setBounds(x, y, 200, 92);
        userInputButton.setBounds(x, y + spacing, 200, 92);
        fileInputButton.setBounds(x, y + 2 * spacing, 200, 92);
        backButton.setBounds(20, 200, 50, 50);
    }

    private JPanel createCustomPanel() {
        JPanel customPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Image image = new ImageIcon(CommonConstants.box).getImage();
                if (image != null) {
                    g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
                } else {
                    System.err.println("Image not loaded: " + CommonConstants.box);
                }
            }
        };

        customPanel.setBounds(550, 230, 850, 550);
        customPanel.setOpaque(false);
        customPanel.setLayout(null);

        addCustomComponents(customPanel);

        return customPanel;
    }

    private void addCustomComponents(JPanel customPanel) {
        addImageLabel(customPanel, CommonConstants.refLen, 50, 50, 500, 37);
        addDropdown(customPanel, createIntegerArray(10, 40), 100, 100, 100, 40);

        addImageLabel(customPanel, CommonConstants.refString, 50, 200, 500, 37);
        addTextArea(customPanel, "0-20 (Separated by space)", 100, 250, 660, 70);

        addImageLabel(customPanel, CommonConstants.frameSize, 50, 350, 500, 37);
        addDropdown(customPanel, createIntegerArray(3, 10), 100, 400, 100, 40);
    }

    private void addImageLabel(JPanel panel, String imagePath, int x, int y, int width, int height) {
        ImageIcon imageIcon = new ImageIcon(imagePath);
        JLabel imageLabel = new JLabel(imageIcon);
        imageLabel.setBounds(x, y, width, height);
        panel.add(imageLabel);
    }

    private void addDropdown(JPanel panel, Integer[] options, int x, int y, int width, int height) {
        JComboBox<Integer> dropdown = new JComboBox<>(options);
        dropdown.setSelectedItem(null);
        dropdown.setBounds(x, y, width, height);
        dropdown.setFont(new Font("Arial", Font.BOLD, 24));
        dropdown.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
        panel.add(dropdown);
    }

    private void addTextArea(JPanel panel, String placeholderText, int x, int y, int width, int height) {
        JTextArea textArea = new JTextArea();
        textArea.setBounds(x, y, width, height);
        textArea.setFont(new Font("Arial", Font.PLAIN, 24));
        textArea.setText(placeholderText);
        textArea.setForeground(Color.GRAY);

        textArea.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (textArea.getText().equals(placeholderText)) {
                    textArea.setText("");
                    textArea.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (textArea.getText().isEmpty()) {
                    textArea.setText(placeholderText);
                    textArea.setForeground(Color.GRAY);
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBounds(x, y, width, height);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        panel.add(scrollPane);
    }

    private Integer[] createIntegerArray(int start, int end) {
        Integer[] array = new Integer[end - start + 1];
        for (int i = 0; i < array.length; i++) {
            array[i] = start + i;
        }
        return array;
    }

    private static JButton createStyledButtonDATAINPUT(String defaultIconPath, String clickIconPath, String hoverIconPath) {
        return createStyledButton(defaultIconPath, hoverIconPath, clickIconPath, new Dimension(200, 92));
    }

    private static JButton createStyledButton(String defaultIconPath, String hoverIconPath, String clickIconPath) {
        return createStyledButton(defaultIconPath, hoverIconPath, clickIconPath, new Dimension(50, 50));
    }

    private static JButton createStyledButton(String defaultIconPath, String hoverIconPath, String clickIconPath, Dimension size) {
        JButton button = new JButton();
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(size);
    
        // Load icons
        ImageIcon defaultIcon = scaleImage(defaultIconPath, size);
        ImageIcon hoverIcon = scaleImage(hoverIconPath, size);
        ImageIcon clickIcon = scaleImage(clickIconPath, size);
    
        button.setIcon(defaultIcon);
    
        // Track clicked status for each button
        final boolean[] isButtonClicked = {false};  // Use an array to hold the flag (as arrays are mutable)
    
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (!isButtonClicked[0]) {
                    button.setIcon(hoverIcon);  // Set hover icon only if not clicked
                }
            }
    
            @Override
            public void mouseExited(MouseEvent e) {
                if (!isButtonClicked[0]) {
                    button.setIcon(defaultIcon);  // Set back to default if not clicked
                }
            }
    
            @Override
            public void mousePressed(MouseEvent e) {
                button.setIcon(hoverIcon);  // Set to hover icon when pressed
                isButtonClicked[0] = true;  // Mark this button as clicked
            }
    
            @Override
            public void mouseReleased(MouseEvent e) {
                if (isButtonClicked[0]) {
                    button.setIcon(hoverIcon);  // Keep hover icon after click
                }
            }
        });
    
        // Add a listener to handle when the mouse exits the screen
        Toolkit.getDefaultToolkit().addAWTEventListener(e -> {
            if (e.getID() == MouseEvent.MOUSE_EXITED) {
                // Reset all buttons to default icon when the mouse leaves the screen
                button.setIcon(defaultIcon);  // Reset to default icon for this button
                isButtonClicked[0] = false;  // Mark the button as not clicked
            }
        }, AWTEvent.MOUSE_EVENT_MASK);
    
        return button;
    }        

    private static ImageIcon scaleImage(String imagePath, Dimension size) {
        ImageIcon icon = new ImageIcon(imagePath);
        Image img = icon.getImage().getScaledInstance(size.width, size.height, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }

    private void enableCustomPanel(boolean enable) {
        if (customPanel != null) {
            customPanel.setEnabled(enable);
            customPanel.setVisible(enable); // Also control visibility for better user experience
        } else {
            System.err.println("Custom panel is not initialized!");
        }
    }

    private void resetCustomPanel() {
        // Reset the customPanel to be disabled and not visible
        if (customPanel != null) {
            customPanel.setEnabled(false);
            customPanel.setVisible(false);
        }
    }
}