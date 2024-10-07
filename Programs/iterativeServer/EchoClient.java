import java.io.*;
import java.net.*;

public class EchoClient {

    private static final String SERVER_ADDRESS = "localhost";  // Server's address
    private static final int SERVER_PORT = 12345;  // Server's port number

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))) {

            System.out.println("Enter your name: ");
            String clientName = stdIn.readLine();
            out.println(clientName);  // Send the name to the server

            System.out.println("Connected to the echo server as " + clientName);
            String userInput;

            // Keep reading input from the user and send it to the server
            while ((userInput = stdIn.readLine()) != null) {
                out.println(userInput);  // Send user input to the server

                // If the user types 'exit', close the client
                if (userInput.equalsIgnoreCase("exit")) {
                    System.out.println(in.readLine());  // Print server's goodbye message
                    break;  // Exit the loop and close the client
                }

                System.out.println("Echo from server: " + in.readLine());  // Read the echoed message from the server
            }

        } catch (IOException e) {
            System.err.println("Client error: " + e.getMessage());
        }
    }
}
