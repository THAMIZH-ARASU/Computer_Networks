import java.io.*;
import java.net.*;

public class Server {
    public static void main(String[] args) {
        int port = 12345; // The port number where the server will listen

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Echo server is listening on port " + port);

            while (true) {
                try ( // Accept client connection
                        Socket socket = serverSocket.accept()) {
                    System.out.println("Client connected");
                    // Create input and output streams
                    InputStream input = socket.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    OutputStream output = socket.getOutputStream();
                    PrintWriter writer = new PrintWriter(output, true);
                    String receivedMessage;
                    // Read client input and echo it back
                    while ((receivedMessage = reader.readLine()) != null) {
                        System.out.println("Received: " + receivedMessage);
                        writer.println("Echo: " + receivedMessage); // Echo the received message back to the client
                    }
                    // Close the connection
                }
                System.out.println("Client disconnected");
            }

        } catch (IOException e) {
            System.out.println("Server error: " + e.getMessage());
        }
    }
}