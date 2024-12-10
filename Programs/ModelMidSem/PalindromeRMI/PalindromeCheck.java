import java.rmi.Remote;
import java.rmi.RemoteException;

public interface PalindromeCheck extends Remote {
    String isPalindrome(String str) throws RemoteException;
}
