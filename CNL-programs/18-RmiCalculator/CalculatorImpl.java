import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class CalculatorImpl extends UnicastRemoteObject implements Calculator {

    protected CalculatorImpl() throws RemoteException {
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
            throw new ArithmeticException("Division by zero is not allowed");
        }
        return num1 / num2;
    }
}
