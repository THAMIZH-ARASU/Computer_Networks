import java.rmi.Remote;
import java.rmi.RemoteException;

public interface FactorialService extends Remote {
    // Method to calculate the factorial
    long calculateFactorial(int number) throws RemoteException;
}
