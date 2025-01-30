import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import javax.imageio.ImageIO;

public class ScreenServer {
    public static void main(String[] args) {
        final int PORT = 5000;

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Screen Capture Server is running on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());

                try (OutputStream outputStream = clientSocket.getOutputStream()) {
                    // Capture the screen
                    Robot robot = new Robot();
                    Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
                    BufferedImage screenImage = robot.createScreenCapture(screenRect);

                    // Send the image as bytes
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    ImageIO.write(screenImage, "png", baos);
                    byte[] imageBytes = baos.toByteArray();

                    DataOutputStream dos = new DataOutputStream(outputStream);
                    dos.writeInt(imageBytes.length); // Send the size of the image
                    dos.write(imageBytes);          // Send the image bytes

                    System.out.println("Screenshot sent to client.");
                } catch (IOException | AWTException e) {
                    System.err.println("Error handling client: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        }
    }
}
