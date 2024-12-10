import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class ScreenCaptureServer {
    public static void main(String[] args) {
        try {
            // Start RMI registry
            LocateRegistry.createRegistry(1099);
            System.out.println("RMI Registry started.");

            // Create and bind the service
            ScreenCaptureService service = new ScreenCaptureServiceImpl();
            Naming.rebind("ScreenCaptureService", service);

            System.out.println("Screen Capture Service is running...");
        } catch (MalformedURLException | RemoteException e) {
            System.err.println("Server exception: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
