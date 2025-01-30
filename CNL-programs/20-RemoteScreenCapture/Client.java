// ScreenClient.java
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import javax.imageio.ImageIO;
import javax.swing.*;

public class Client {
    public static void main(String[] args) {
        final String SERVER_ADDRESS = "localhost";
        final int PORT = 5000;

        try (Socket socket = new Socket(SERVER_ADDRESS, PORT)) {
            System.out.println("Connected to the server.");

            // Receive the image bytes
            InputStream inputStream = socket.getInputStream();
            DataInputStream dis = new DataInputStream(inputStream);

            int imageSize = dis.readInt(); // Read the size of the image
            byte[] imageBytes = new byte[imageSize];
            dis.readFully(imageBytes);     // Read the image bytes

            // Convert the byte array back to an image
            ByteArrayInputStream bais = new ByteArrayInputStream(imageBytes);
            BufferedImage image = ImageIO.read(bais);

            // Save the image to a file
            File outputFile = new File("client_screenshot.png");
            ImageIO.write(image, "png", outputFile);
            System.out.println("Screenshot saved as 'client_screenshot.png'");

            // Display the image in a JFrame
            ImageDisplay frame = new ImageDisplay("client_screenshot.png");
            frame.setVisible(true);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        } catch (IOException e) {
            System.err.println("Client error: " + e.getMessage());
        }
    }
}
