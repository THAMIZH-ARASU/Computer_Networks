import java.rmi.*;
import java.rmi.server.*;

// Implement the ArithmeticService interface
public class ArithmeticServer extends UnicastRemoteObject implements ArithmeticService {

    // Constructor
    public ArithmeticServer() throws RemoteException {
        super();
    }

    @Override
    public double add(double num1, double num2) throws RemoteException {
        return num1 + num2;
    }

    @Override
    public double subtract(double num1, double num2) throws RemoteException {
        return num1 - num2;
    }

    @Override
    public double multiply(double num1, double num2) throws RemoteException {
        return num1 * num2;
    }

    @Override
    public double divide(double num1, double num2) throws RemoteException {
        if (num2 == 0) {
            throw new ArithmeticException("Division by zero is not allowed.");
        }
        return num1 / num2;
    }

    public static void main(String[] args) {
        try {
            // Start the RMI registry
            System.setProperty("java.rmi.server.hostname", "localhost");
            ArithmeticServer server = new ArithmeticServer();

            // Bind the remote object to a name in the RMI registry
            Naming.rebind("rmi://localhost/ArithmeticService", server);
            System.out.println("Arithmetic Server is ready.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
