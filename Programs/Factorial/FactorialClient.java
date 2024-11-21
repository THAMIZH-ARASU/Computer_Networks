import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;

public class FactorialClient {
    public static void main(String[] args) {
        try {
            // Lookup the remote service
            FactorialService service = (FactorialService) Naming.lookup("rmi://localhost:12345/FactorialService");


            // Input number
            System.out.println("Enter a number:");
            try (Scanner scanner = new Scanner(System.in)) {
                int number = scanner.nextInt(); // Example input
                long result = service.calculateFactorial(number);

                System.out.println("Factorial of " + number + " is: " + result);
            }
        } catch (MalformedURLException | NotBoundException | RemoteException e) {
            System.err.println("Client exception: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
