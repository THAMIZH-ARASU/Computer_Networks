import java.io.*;
import java.net.*;

public class ChatServer {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(1234)) {
            System.out.println("Server is waiting for a connection...");
            Socket socket = serverSocket.accept(); // Accept client connection
            System.out.println("Client connected");

            // Input and output streams
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

            // For sending and receiving messages
            String message;
            BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));

            // Start a thread to listen for client messages
            new Thread(() -> {
                try {
                    String clientMessage;
                    while ((clientMessage = reader.readLine()) != null) {
                        System.out.println("Client: " + clientMessage);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

            // Server sending messages
            while (true) {
                message = consoleReader.readLine();
                writer.println(message);
                if (message.equalsIgnoreCase("bye")) {
                    System.out.println("Server disconnected.");
                    break;
                }
            }
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
