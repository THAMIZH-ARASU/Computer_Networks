import java.rmi.Naming;
import java.util.Scanner;

public class PalindromeClient {
    public static void main(String[] args) {
        try {
            // Get a reference to the remote object (the server)
            PalindromeCheck palindromeService = (PalindromeCheck) Naming.lookup("rmi://localhost/PalindromeService");

            // Get input from the user
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter a string to check if it's a palindrome: ");
            String inputString = scanner.nextLine();

            // Invoke the remote method
            String result = palindromeService.isPalindrome(inputString);

            // Display the result
            System.out.println("Is the string a palindrome? " + result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
