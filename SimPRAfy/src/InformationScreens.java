
import javax.swing.*;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class InformationScreens {
    private CardLayout layout;
    private JPanel mainPanel;

    // Constructor to initialize layout and mainPanel from Main.java
    public InformationScreens(CardLayout layout, JPanel mainPanel) {
        this.layout = layout;
        this.mainPanel = mainPanel;
    }

    public void showCredits() {
        BackgroundPanel panel = new BackgroundPanel(CommonConstants.credits);
        panel.setLayout(null);
    
        JButton backButton = createStyledButton(CommonConstants.backDefault, CommonConstants.backClick, 
                CommonConstants.backClick);
    
        backButton.setBounds(60, 70, 50, 50);
    
        backButton.addActionListener(e -> layout.show(mainPanel, "Lobby"));
    
        panel.add(backButton);
        
        mainPanel.add(panel, "Credits");
    }
    

    public void showHelp() {
        BackgroundPanel panel = new BackgroundPanel(CommonConstants.help);
        panel.setLayout(null);
    
        JButton backButton = createStyledButton(CommonConstants.backDefault, CommonConstants.backClick, 
                CommonConstants.backClick);
    
        backButton.setBounds(60, 70, 50, 50);
    
        backButton.addActionListener(e -> layout.show(mainPanel, "Lobby"));
    
        panel.add(backButton);
        
        mainPanel.add(panel, "Help");
    }

    // Helper method for button styling
    private static JButton createStyledButton(String defaultIconPath, String hoverIconPath, String clickIconPath) {
        JButton button = new JButton();
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(50, 50));

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