import java.rmi.Naming;
import java.io.FileOutputStream;
import java.io.ByteArrayInputStream;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.rmi.NotBoundException;

public class ScreenCaptureClient {
    public static void main(String[] args) {
        try {
            // Lookup the remote service
            ScreenCaptureService service = (ScreenCaptureService) Naming.lookup("rmi://localhost/ScreenCaptureService");

            // Request the screen capture
            byte[] imageBytes = service.captureScreen();

            // Convert the byte array back to an image
            ByteArrayInputStream bais = new ByteArrayInputStream(imageBytes);
            BufferedImage image = ImageIO.read(bais);

            // Save the image to a file
            FileOutputStream fos = new FileOutputStream("screenshot.png");
            ImageIO.write(image, "png", fos);

            System.out.println("Screenshot saved as 'screenshot.png'");
        } catch (IOException | NotBoundException e) {
            System.err.println("Client exception: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
