import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Calculator extends Remote {
    double add(double num1, double num2) throws RemoteException;
    double subtract(double num1, double num2) throws RemoteException;
    double multiply(double num1, double num2) throws RemoteException;
    double divide(double num1, double num2) throws RemoteException;
}
