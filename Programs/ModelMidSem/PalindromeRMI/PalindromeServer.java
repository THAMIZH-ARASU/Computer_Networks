import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class PalindromeServer extends UnicastRemoteObject implements PalindromeCheck {

    // Constructor for the server
    public PalindromeServer() throws RemoteException {
        super();
    }

    @Override
    public String isPalindrome(String str) throws RemoteException {
        // Removing spaces and converting to lowercase for case-insensitive comparison
        str = str.replaceAll("\\s", "").toLowerCase();
        String reversed = new StringBuilder(str).reverse().toString();
        
        // Checking if the string is a palindrome
        if (str.equals(reversed)) {
            return "Yes";
        } else {
            return "No";
        }
    }

    public static void main(String[] args) {
        try {
            // Create an instance of the server and bind it to the registry
            PalindromeServer server = new PalindromeServer();
            Naming.rebind("rmi://localhost/PalindromeService", server);
            System.out.println("Palindrome Server is ready.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
    