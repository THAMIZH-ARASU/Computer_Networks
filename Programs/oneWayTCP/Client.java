import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) {
        String hostname = "localhost"; // Server address (localhost for the same machine)
        int port = 12345;              // The port number of the echo server

        try (Socket socket = new Socket(hostname, port)) {
            // Create input and output streams
            OutputStream output = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(output, true);

            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            // Read input from the console and send it to the server
            BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
            String userInput;

            System.out.println("Type your message (type 'exit' to quit):");

            while ((userInput = consoleReader.readLine()) != null) {
                if ("exit".equalsIgnoreCase(userInput)) {
                    break; // Exit on user typing 'exit'
                }

                writer.println(userInput); // Send user input to the server
                String serverResponse = reader.readLine(); // Read echo response from the server
                System.out.println("Server response: " + serverResponse); // Print the server's response
            }

        } catch (UnknownHostException e) {
            System.out.println("Server not found: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("I/O error: " + e.getMessage());
        }
    }
}