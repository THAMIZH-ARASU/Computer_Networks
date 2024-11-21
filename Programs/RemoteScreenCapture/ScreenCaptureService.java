import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ScreenCaptureService extends Remote {
    // Return the screen capture as a byte array
    byte[] captureScreen() throws RemoteException;
}
