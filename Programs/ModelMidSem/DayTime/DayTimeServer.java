import java.io.*;
import java.net.*;
import java.util.*;

public class DayTimeServer {
    public static void main(String[] args) {
        try {
            // Create a server socket that listens on port 4078
            ServerSocket serverSocket = new ServerSocket(4078);
            System.out.println("DayTime Server is waiting for clients...");

            // Infinite loop to keep the server running
            while (true) {
                // Accept a connection from a client
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());

                // Get the current date and time
                Date now = new Date();

                // Create input/output streams for communication
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                // Read the client's request (optional)
                String clientRequest = in.readLine();
                System.out.println("Received request: " + clientRequest);

                // Send the current date and time to the client
                out.println("Current date and time: " + now.toString());

                // Close the client socket after responding
                clientSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
