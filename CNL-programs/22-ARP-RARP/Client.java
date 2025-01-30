// Client.java
import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 5000;
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            try {
                System.out.println("\nChoose an operation:");
                System.out.println("1. Save IP-MAC pair");
                System.out.println("2. Retrieve address");
                System.out.println("3. Delete pair");
                System.out.println("4. Exit");
                
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                
                if (choice == 4) {
                    System.out.println("Goodbye!");
                    break;
                }

                String request = buildRequest(choice);
                if (request != null) {
                    String response = sendRequest(request);
                    System.out.println("Server response: " + response);
                }

            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        scanner.close();
    }

    private static String buildRequest(int choice) {
        try {
            switch (choice) {
                case 1: // Save
                    System.out.print("Enter IP address: ");
                    String ip = scanner.nextLine();
                    System.out.print("Enter MAC address: ");
                    String mac = scanner.nextLine();
                    return "SAVE," + ip + "," + mac;

                case 2: // Retrieve
                    System.out.println("What do you want to retrieve?");
                    System.out.println("1. IP address (using MAC)");
                    System.out.println("2. MAC address (using IP)");
                    int retrieveChoice = scanner.nextInt();
                    scanner.nextLine(); // Consume newline

                    if (retrieveChoice == 1) {
                        System.out.print("Enter MAC address: ");
                        String searchMac = scanner.nextLine();
                        return "RETRIEVE,IP," + searchMac;
                    } else {
                        System.out.print("Enter IP address: ");
                        String searchIp = scanner.nextLine();
                        return "RETRIEVE,MAC," + searchIp;
                    }

                case 3: // Delete
                    System.out.print("Enter IP address to delete: ");
                    String ipToDelete = scanner.nextLine();
                    return "DELETE," + ipToDelete;

                default:
                    System.out.println("Invalid choice");
                    return null;
            }
        } catch (Exception e) {
            System.out.println("Error building request: " + e.getMessage());
            return null;
        }
    }

    private static String sendRequest(String request) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            out.println(request);
            return in.readLine();

        } catch (IOException e) {
            return "Error communicating with server: " + e.getMessage();
        }
    }
}