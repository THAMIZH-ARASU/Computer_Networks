import java.rmi.Remote;
import java.rmi.RemoteException;

// Define the remote interface for the calculator service
public interface ArithmeticService extends Remote {
    // Method for addition
    public double add(double num1, double num2) throws RemoteException;
    
    // Method for subtraction
    public double subtract(double num1, double num2) throws RemoteException;
    
    // Method for multiplication
    public double multiply(double num1, double num2) throws RemoteException;
    
    // Method for division
    public double divide(double num1, double num2) throws RemoteException;
}
