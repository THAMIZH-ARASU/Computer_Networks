import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.*;

public class ImageDisplay extends JFrame {

    private BufferedImage image;

    public ImageDisplay(String imagePath) {
        try {
            // Load the image from the given path
            image = ImageIO.read(new File(imagePath));
        } catch (IOException e) {
            System.err.println("Error loading image: " + e.getMessage());
            System.exit(1);
        }

        // Set JFrame properties
        setTitle("Image Display");
        setSize(800, 500); // Initial window size, adjust as needed
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window
        
        // Set the content panel
        JPanel panel = new ImagePanel();
        add(panel);
    }

    private class ImagePanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            
            if (image != null) {
                // Calculate scaled dimensions
                int imgWidth = image.getWidth();
                int imgHeight = image.getHeight();
                int panelWidth = getWidth();
                int panelHeight = getHeight();

                double scaleWidth = panelWidth / (double) imgWidth;
                double scaleHeight = panelHeight / (double) imgHeight;
                double scale = Math.min(scaleWidth, scaleHeight);

                int scaledWidth = (int) (imgWidth * scale);
                int scaledHeight = (int) (imgHeight * scale);

                // Draw the scaled image
                g.drawImage(image, 0, 0, scaledWidth, scaledHeight, this);
            }
        }
    }

    public static void main(String[] args) {
        // Provide the path to your image file
        String imagePath = "screenshot.png";

        // Create and display the JFrame
        SwingUtilities.invokeLater(() -> {
            ImageDisplay frame = new ImageDisplay(imagePath);
            frame.setVisible(true);
        });
    }
}
