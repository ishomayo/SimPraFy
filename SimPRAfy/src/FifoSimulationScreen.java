import javax.swing.*;
import java.awt.*;

public class FifoSimulationScreen extends JPanel {
    public FifoSimulationScreen(CardLayout layout, JPanel mainPanel) {
        this.setLayout(null); // Use absolute positioning

        // Background customization
        this.setOpaque(true);
        this.setBackground(Color.LIGHT_GRAY);

        // Simulation Title Label
        JLabel simulationLabel = new JLabel("FIFO Simulation", SwingConstants.CENTER);
        simulationLabel.setFont(new Font("Arial", Font.BOLD, 24));
        simulationLabel.setBounds(150, 50, 300, 50); // Position label at the top
        this.add(simulationLabel);

        // Back Button
        JButton backButton = new JButton("Back");
        backButton.setBounds(20, 20, 100, 50); // Explicit positioning
        this.add(backButton);

        // Action to go back to Algorithm Selection screen
        backButton.addActionListener(e -> layout.show(mainPanel, "AlgorithmSelection"));
    }
}
