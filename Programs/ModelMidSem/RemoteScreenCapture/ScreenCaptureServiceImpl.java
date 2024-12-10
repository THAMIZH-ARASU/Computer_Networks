import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import javax.imageio.ImageIO;

public class ScreenCaptureServiceImpl extends UnicastRemoteObject implements ScreenCaptureService {

    protected ScreenCaptureServiceImpl() throws RemoteException {
        super();
    }

    @Override
    public byte[] captureScreen() throws RemoteException {
        try {
            // Capture the screen
            Robot robot = new Robot();
            Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
            BufferedImage screenImage = robot.createScreenCapture(screenRect);

            // Convert the image to a byte array
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(screenImage, "png", baos);
            return baos.toByteArray();
        } catch (AWTException | HeadlessException | IOException e) {
            e.printStackTrace();
            throw new RemoteException("Failed to capture screen", e);
        }
    }
}
