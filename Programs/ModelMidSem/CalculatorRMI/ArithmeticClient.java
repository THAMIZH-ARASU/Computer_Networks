import java.rmi.*;
import java.util.Scanner;

public class ArithmeticClient {
    public static void main(String[] args) {
        try {
            // Look up the remote object in the RMI registry
            ArithmeticService service = (ArithmeticService) Naming.lookup("rmi://localhost/ArithmeticService");

            Scanner scanner = new Scanner(System.in);
            int choice;
            double num1, num2;

            while (true) {
                // Display menu options
                System.out.println("Arithmetic Calculator Menu");
                System.out.println("1. Add");
                System.out.println("2. Subtract");
                System.out.println("3. Multiply");
                System.out.println("4. Divide");
                System.out.println("5. Exit");

                // Get user choice
                System.out.print("Enter your choice: ");
                choice = scanner.nextInt();

                if (choice == 5) {
                    System.out.println("Exiting calculator.");
                    break;
                }

                // Get numbers for arithmetic operation
                System.out.print("Enter first number: ");
                num1 = scanner.nextDouble();
                System.out.print("Enter second number: ");
                num2 = scanner.nextDouble();

                // Perform the selected arithmetic operation
                switch (choice) {
                    case 1:
                        System.out.println("Result: " + service.add(num1, num2));
                        break;
                    case 2:
                        System.out.println("Result: " + service.subtract(num1, num2));
                        break;
                    case 3:
                        System.out.println("Result: " + service.multiply(num1, num2));
                        break;
                    case 4:
                        try {
                            System.out.println("Result: " + service.divide(num1, num2));
                        } catch (ArithmeticException e) {
                            System.out.println("Error: " + e.getMessage());
                        }
                        break;
                    default:
                        System.out.println("Invalid choice, try again.");
                }
            }
            scanner.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
