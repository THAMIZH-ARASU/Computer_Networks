import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class FactorialServer {
    public static void main(String[] args) {
        try {
            // Start RMI registry
            LocateRegistry.createRegistry(12345); // Use a different port
            System.out.println("RMI Registry started.");

            // Create the implementation and bind it
            FactorialService service = new FactorialServiceImpl();
            Naming.rebind("//localhost:12345/FactorialService", service);

            System.out.println("Factorial Service is running...");
        } catch (MalformedURLException | RemoteException e) {
            System.err.println("Server exception: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
