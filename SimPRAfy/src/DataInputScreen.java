import javax.swing.*;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.event.DocumentEvent;

import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.awt.event.ActionListener;
import java.util.Random;

public class DataInputScreen extends JPanel {
    private JPanel mainPanel;
    private CardLayout layout;
    private Image backgroundImage;
    private App app;
    private JComboBox<Integer> dropdownRefLen;
    private JTextArea textArea;
    private JComboBox<Integer> dropdownFrameSize;
    
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
        JPanel panel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        panel.setOpaque(false);

        this.setLayout(null);

        JButton randomButton = createStyledButtonDATAINPUT(CommonConstants.randomDefault, 
            CommonConstants.randomHover, CommonConstants.randomHover);
        JButton userInputButton = createStyledButtonDATAINPUT(CommonConstants.userInputDefault, 
            CommonConstants.userInputHover, CommonConstants.userInputHover);
        JButton fileInputButton = createStyledButtonDATAINPUT(CommonConstants.fileDefault, 
            CommonConstants.fileHover, CommonConstants.fileHover);

        JButton backButton = createStyledButton(CommonConstants.backDefault, 
            CommonConstants.backClick, CommonConstants.backClick);

        positionButtons(randomButton, userInputButton, fileInputButton, backButton);

        this.add(randomButton);
        this.add(userInputButton);
        this.add(fileInputButton);
        this.add(backButton);

        backButton.addActionListener(e -> layout.show(mainPanel, "Lobby"));

        randomButton.addActionListener(e -> showGenerateRandomScreen());
        userInputButton.addActionListener(e -> showUserInputScreen());
        fileInputButton.addActionListener(e -> showFileInputScreen());
    
        mainPanel.add(panel, "DataInputSelection");
    }

    private void showGenerateRandomScreen() {
        ImageIcon backgroundImage = new ImageIcon(CommonConstants.inputMethod); // Replace with your image file

        JPanel randomDataPanel = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        randomDataPanel.setOpaque(false);

        JButton backButton = createStyledButton(CommonConstants.backDefault, CommonConstants.backClick, CommonConstants.backClick);
        backButton.setBounds(20, 200, 50, 50);
        randomDataPanel.add(backButton);
        backButton.addActionListener(e -> layout.show(mainPanel, "Lobby"));

        ImageIcon randomIcon = scaleImage(CommonConstants.randomHover, new Dimension(200, 92));
        JLabel randomLabel = new JLabel(randomIcon);
        randomLabel.setBounds(210, 350, 200, 92);
        
        ImageIcon userInputIcon = scaleImage(CommonConstants.userInputDefault, new Dimension(200, 92));
        JLabel userInputLabel = new JLabel(userInputIcon);
        userInputLabel.setBounds(210, 480, 200, 92);

        ImageIcon fileIcon = scaleImage(CommonConstants.fileDefault, new Dimension(200, 92));
        JLabel fileLabel = new JLabel(fileIcon);
        fileLabel.setBounds(210, 610, 200, 92);

        JPanel customPanel = createCustomPanel("random");
        
        randomDataPanel.add(randomLabel);
        randomDataPanel.add(userInputLabel);
        randomDataPanel.add(fileLabel);
        randomDataPanel.add(customPanel);

        mainPanel.add(randomDataPanel, "RandomDataScreen");
        layout.show(mainPanel, "RandomDataScreen");
    }

    private void showUserInputScreen() {
        ImageIcon backgroundImage = new ImageIcon(CommonConstants.inputMethod); // Replace with your image file

        JPanel userInputPanel = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        userInputPanel.setOpaque(false);

        JButton backButton = createStyledButton(CommonConstants.backDefault, CommonConstants.backClick, CommonConstants.backClick);
        backButton.setBounds(20, 200, 50, 50);
        userInputPanel.add(backButton);
        backButton.addActionListener(e -> layout.show(mainPanel, "Lobby"));

        ImageIcon randomIcon = scaleImage(CommonConstants.randomDefault, new Dimension(200, 92));
        JLabel randomLabel = new JLabel(randomIcon);
        randomLabel.setBounds(210, 350, 200, 92);
        
        ImageIcon userInputIcon = scaleImage(CommonConstants.userInputHover, new Dimension(200, 92));
        JLabel userInputLabel = new JLabel(userInputIcon);
        userInputLabel.setBounds(210, 480, 200, 92);

        ImageIcon fileIcon = scaleImage(CommonConstants.fileDefault, new Dimension(200, 92));
        JLabel fileLabel = new JLabel(fileIcon);
        fileLabel.setBounds(210, 610, 200, 92);

        JPanel customPanel = createCustomPanel("user input");
        
        userInputPanel.add(randomLabel);
        userInputPanel.add(userInputLabel);
        userInputPanel.add(fileLabel);
        userInputPanel.add(customPanel);

        mainPanel.add(userInputPanel, "UserInputScreen");
        layout.show(mainPanel, "UserInputScreen");
    }

    private void showFileInputScreen() {
        ImageIcon backgroundImage = new ImageIcon(CommonConstants.inputMethod); // Replace with your image file

        // Create custom JPanel to paint background
        JPanel fileInputPanel = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };

        fileInputPanel.setOpaque(false);

        JButton backButton = createStyledButton(CommonConstants.backDefault, CommonConstants.backClick, CommonConstants.backClick);
        backButton.setBounds(20, 200, 50, 50);
        fileInputPanel.add(backButton);
        backButton.addActionListener(e -> layout.show(mainPanel, "Lobby"));

        ImageIcon randomIcon = scaleImage(CommonConstants.randomDefault, new Dimension(200, 92));
        JLabel randomLabel = new JLabel(randomIcon);
        randomLabel.setBounds(210, 350, 200, 92);
        
        ImageIcon userInputIcon = scaleImage(CommonConstants.userInputDefault, new Dimension(200, 92));
        JLabel userInputLabel = new JLabel(userInputIcon);
        userInputLabel.setBounds(210, 480, 200, 92);

        ImageIcon fileIcon = scaleImage(CommonConstants.fileHover, new Dimension(200, 92));
        JLabel fileLabel = new JLabel(fileIcon);
        fileLabel.setBounds(210, 610, 200, 92);

        JPanel customPanel = createCustomPanel("file input");
        
        fileInputPanel.add(randomLabel);
        fileInputPanel.add(userInputLabel);
        fileInputPanel.add(fileLabel);
        fileInputPanel.add(customPanel);

        mainPanel.add(fileInputPanel, "FileInputScreen");
        layout.show(mainPanel, "FileInputScreen");
    }

    private void positionButtons(JButton randomButton, JButton userInputButton, JButton fileInputButton, JButton backButton) {
        int x = 210, y = 350, spacing = 130;
        randomButton.setBounds(x, y, 200, 92);
        userInputButton.setBounds(x, y + spacing, 200, 92);
        fileInputButton.setBounds(x, y + 2 * spacing, 200, 92);
        backButton.setBounds(20, 200, 50, 50);
    }

    private JPanel createCustomPanel(String method) {
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

        addCustomComponents(customPanel, method);

        return customPanel;
    }

    private void addCustomComponents(JPanel customPanel, String method) {
        addImageLabel(customPanel, CommonConstants.refLen, 50, 50, 500, 37);
        dropdownRefLen = new JComboBox<>();
        dropdownRefLen = addDropdown(customPanel, createIntegerArray(10, 40), 100, 100, 100, 40, method);

        addImageLabel(customPanel, CommonConstants.refString, 50, 200, 500, 37);
        textArea = new JTextArea();
        textArea = addTextArea(customPanel, "0-20 (Separated by space)", 100, 250, 660, 70, method);

        addImageLabel(customPanel, CommonConstants.frameSize, 50, 350, 500, 37);
        dropdownFrameSize = new JComboBox<>();
        dropdownFrameSize = addDropdown(customPanel, createIntegerArray(3, 10), 100, 400, 100, 40, method);

        if(method.equalsIgnoreCase("user input")) {
            addUserInputButtons(customPanel, dropdownRefLen, textArea, dropdownFrameSize);
        } else if(method.equalsIgnoreCase("random")) {
            // Random Data Input
            addRandomBottomButtons(customPanel, dropdownRefLen, textArea, dropdownFrameSize);
        } else {
            addFileInputButtons(customPanel, dropdownRefLen, textArea, dropdownFrameSize);
        }
    }

    private void addImageLabel(JPanel panel, String imagePath, int x, int y, int width, int height) {
        ImageIcon imageIcon = new ImageIcon(imagePath);
        JLabel imageLabel = new JLabel(imageIcon);
        imageLabel.setBounds(x, y, width, height);
        panel.add(imageLabel);
    }

    private JComboBox<Integer> addDropdown(JPanel panel, Integer [] option, int x, int y, int width, int height, String method) {
        JComboBox<Integer> dropdown = new JComboBox<>(option);
        dropdown.setSelectedItem(null);
        dropdown.setBounds(x, y, width, height);
        dropdown.setFont(new Font("Arial", Font.BOLD, 24));
        dropdown.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
        dropdown.setEnabled("user input".equalsIgnoreCase(method));
        panel.add(dropdown);
        return dropdown;
    }

    private JTextArea addTextArea(JPanel panel, String placeholderText, int x, int y, int width, int height, String method) {
        JTextArea textArea = new JTextArea();
        textArea.setBounds(x, y, width, height);
        textArea.setFont(new Font("Arial", Font.PLAIN, 24));
        textArea.setText(placeholderText);
        textArea.setForeground(Color.GRAY);
        textArea.setEnabled("user input".equalsIgnoreCase(method));

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
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        panel.add(scrollPane);

        return textArea;
    }

    private Integer[] createIntegerArray(int start, int end) {
        Integer[] array = new Integer[end - start + 1];
        for (int i = 0; i < array.length; i++) {
            array[i] = start + i;
        }
        return array;
    }

    private void addUserInputButtons(JPanel panel, JComboBox<Integer> dropdownRefLen, JTextArea textArea, JComboBox<Integer> dropdownFrameSize) {
        JButton continueButton = createStyledButton(
            CommonConstants.continueDefault, 
            CommonConstants.continueHover, 
            CommonConstants.continueHover, 
            200, 50
        );
    
        int panelWidth = panel.getWidth();
        int panelHeight = panel.getHeight();
        int buttonY = panelHeight - 80;
        continueButton.setBounds((panelWidth - 200) / 2, buttonY, 200, 50);

        continueButton.setEnabled(false);

        // Add listeners to enable the button when all fields are valid
        ActionListener validationListener = new ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                boolean isValid = validateUserInput(dropdownRefLen, textArea, dropdownFrameSize);
                continueButton.setEnabled(isValid);
            }
        };

        continueButton.addActionListener(event -> {
            if (continueButton.isEnabled()) {
                app.selectAlgorithmScreen(); 
                layout.show(mainPanel, "SelectAlgorithmScreen");
            }
        });

        dropdownRefLen.addActionListener(validationListener);
        textArea.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { validationListener.actionPerformed(null); }
            public void removeUpdate(DocumentEvent e) { validationListener.actionPerformed(null); }
            public void changedUpdate(DocumentEvent e) { validationListener.actionPerformed(null); }
        });
        dropdownFrameSize.addActionListener(validationListener);

        panel.add(continueButton);
    }

    private boolean validateUserInput(JComboBox<Integer> dropdownRefLen, JTextArea textArea, JComboBox<Integer> dropdownFrameSize) {
        if (dropdownRefLen.getSelectedItem() == null || dropdownFrameSize.getSelectedItem() == null) {
            return false;
        }

        int refLen = (Integer) dropdownRefLen.getSelectedItem();
        String text = textArea.getText().trim();
        String[] values = text.split(" ");

        if (values.length != refLen) {
            return false;
        }

        for (String value : values) {
            try {
                int num = Integer.parseInt(value.trim());
                if (num < 0 || num > 20) {
                    return false;
                }
            } catch (NumberFormatException e) {
                return false;
            }
        }

        return true;
    }

    private void addFileInputButtons(JPanel panel, JComboBox<Integer> dropdownRefLen, JTextArea textArea, JComboBox<Integer> dropdownFrameSize) {
        JButton uploadButton = createStyledButton(
            CommonConstants.uploadDefault, 
            CommonConstants.uploadHover, 
            CommonConstants.uploadHover, 
            200, 50
        );
    
        JButton continueButton = createStyledButton(
            CommonConstants.continueDefault, 
            CommonConstants.continueHover, 
            CommonConstants.continueHover, 
            200, 50
        );
    
        // Position the buttons at the bottom of customPanel
        int panelWidth = panel.getWidth();
        int panelHeight = panel.getHeight();
        int buttonY = panelHeight - 80; // Adjusted position from bottom
    
        uploadButton.setBounds(panelWidth / 2 - 220, buttonY, 200, 50);
        continueButton.setBounds(panelWidth / 2 + 20, buttonY, 200, 50);

        continueButton.setEnabled(false);

        uploadButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("Text Files", "txt"));
            int returnValue = fileChooser.showOpenDialog(null);
            
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                try (BufferedReader reader = new BufferedReader(new FileReader(selectedFile))) {
                    String refLenStr = reader.readLine();
                    String referenceString = reader.readLine();
                    String frameSizeStr = reader.readLine();

                    if (refLenStr != null && referenceString != null && frameSizeStr != null) {
                        int refLen = Integer.parseInt(refLenStr.trim());
                        int frameSize = Integer.parseInt(frameSizeStr.trim());

                        // Validate the reference string
                        String[] values = referenceString.split(" ");
                        if (values.length == refLen) {
                            boolean isValid = true;
                            for (String value : values) {
                                int num = Integer.parseInt(value.trim());
                                if (num < 0 || num > 20) {
                                    isValid = false;
                                    break;
                                }
                            }

                            if (isValid) {
                                dropdownRefLen.setSelectedItem(refLen);
                                textArea.setText(referenceString);
                                dropdownFrameSize.setSelectedItem(frameSize);
                                continueButton.setEnabled(true);
                            } else {
                                JOptionPane.showMessageDialog(null, "Invalid reference string values.");
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "Reference string length mismatch.");
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "File format incorrect.");
                    }
                } catch (IOException | NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Error reading the file.");
                }
            }
        });

        continueButton.addActionListener(event -> {
            if (continueButton.isEnabled()) {
                app.selectAlgorithmScreen(); 
                layout.show(mainPanel, "SelectAlgorithmScreen");
            }
        });

        panel.add(uploadButton);
        panel.add(continueButton);
    }

    private void addRandomBottomButtons(JPanel panel, JComboBox<Integer> dropdownRefLen, JTextArea textArea, JComboBox<Integer> dropdownFrameSize) {
        JButton generateButton = createStyledButton(
            CommonConstants.generateDefault, 
            CommonConstants.generateHover, 
            CommonConstants.generateHover, 
            200, 50
        );
    
        JButton continueButton = createStyledButton(
            CommonConstants.continueDefault, 
            CommonConstants.continueHover, 
            CommonConstants.continueHover, 
            200, 50
        );
    
        // Position the buttons at the bottom of customPanel
        int panelWidth = panel.getWidth();
        int panelHeight = panel.getHeight();
        int buttonY = panelHeight - 80; // Adjusted position from bottom
    
        generateButton.setBounds(panelWidth / 2 - 220, buttonY, 200, 50);
        continueButton.setBounds(panelWidth / 2 + 20, buttonY, 200, 50);

        continueButton.setEnabled(false);

        generateButton.addActionListener(e -> {
            generateRandomData(dropdownRefLen, textArea, dropdownFrameSize);
            continueButton.setEnabled(true);
        });

        continueButton.addActionListener(event -> {
            if (continueButton.isEnabled()) {
                app.selectAlgorithmScreen(); 
                layout.show(mainPanel, "SelectAlgorithmScreen");
            }
        });
    
        panel.add(generateButton);
        panel.add(continueButton);
    }

    private void generateRandomData(JComboBox<Integer> dropdownRefLen, JTextArea textArea, JComboBox<Integer> dropdownFrameSize) {
        Random random = new Random();

        // Generate random Length of Reference String
        int lengthOfReferenceString = random.nextInt(31) + 10; // Random number between 10 and 40
        dropdownRefLen.setSelectedItem(lengthOfReferenceString);

        // Generate random Reference String
        StringBuilder referenceStringBuilder = new StringBuilder();
        for (int i = 0; i < lengthOfReferenceString; i++) {
            int value = random.nextInt(21); // Random number between 0 and 20
            referenceStringBuilder.append(value).append(" ");
        }
        textArea.setText(referenceStringBuilder.toString().trim());
        System.err.println(referenceStringBuilder.toString().trim());

        // Generate random Frame Size
        int frameSize = random.nextInt(8) + 3; // Random number between 3 and 10
        dropdownFrameSize.setSelectedItem(frameSize);
    }

    public JComboBox<Integer> getDropdownRefLen() {
        return dropdownRefLen;
    }

    public JTextArea getTextArea() {
        return textArea;
    }

    public JComboBox<Integer> getDropdownFrameSize() {
        return dropdownFrameSize;
    }
    
    private JButton createStyledButton(String defaultImg, String hoverImg, String clickImg, int width, int height) {
        JButton button = new JButton(new ImageIcon(defaultImg));
    
        // Set button properties
        button.setPreferredSize(new Dimension(width, height));
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);

        button.setIcon(scaleImage(defaultImg, new Dimension(width, height)));
    
        // MouseListener for hover and click effects
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setIcon(scaleImage(hoverImg, new Dimension(width, height)));
            }
    
            @Override
            public void mouseExited(MouseEvent e) {
                button.setIcon(scaleImage(defaultImg, new Dimension(width, height)));
            }
    
            @Override
            public void mousePressed(MouseEvent e) {
                button.setIcon(scaleImage(clickImg, new Dimension(width, height)));
            }
    
            @Override
            public void mouseReleased(MouseEvent e) {
                button.setIcon(scaleImage(hoverImg, new Dimension(width, height)));
            }
        });
    
        return button;
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
    
        button.addMouseListener(new MouseAdapter() {
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setIcon(defaultIcon);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                button.setIcon(clickIcon);
            }

            public void mouseEntered(MouseEvent e) {
                button.setIcon(hoverIcon);
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