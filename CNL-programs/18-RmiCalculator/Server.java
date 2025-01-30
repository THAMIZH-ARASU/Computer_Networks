import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class Server {

    public static void main(String[] args) {
        try {
            // Start the RMI registry
            LocateRegistry.createRegistry(1099);

            // Create an instance of the calculator implementation
            CalculatorImpl calculator = new CalculatorImpl();

            // Bind the calculator object to the registry with a name
            Naming.rebind("CalculatorService", calculator);

            System.out.println("Calculator Server is ready.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
