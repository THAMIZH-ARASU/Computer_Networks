import java.rmi.Naming;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) {
        try {
            // Look up the remote object in the RMI registry
            Calculator calculator = (Calculator) Naming.lookup("//localhost/CalculatorService");

            // Create a scanner to read user input
            Scanner scanner = new Scanner(System.in);

            System.out.println("Enter first number: ");
            double num1 = scanner.nextDouble();
            System.out.println("Enter second number: ");
            double num2 = scanner.nextDouble();

            System.out.println("Choose operation (+, -, *, /): ");
            String operation = scanner.next();

            double result = 0;

            switch (operation) {
                case "+":
                    result = calculator.add(num1, num2);
                    break;
                case "-":
                    result = calculator.subtract(num1, num2);
                    break;
                case "*":
                    result = calculator.multiply(num1, num2);
                    break;
                case "/":
                    result = calculator.divide(num1, num2);
                    break;
                default:
                    System.out.println("Invalid operation.");
                    return;
            }

            System.out.println("The result is: " + result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
