import java.io.*;
import java.net.*;

public class ChatClient {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 1234)) { // Connect to server
            System.out.println("Connected to server");

            // Input and output streams
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

            // For sending and receiving messages
            String message;
            BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));

            // Start a thread to listen for server messages
            new Thread(() -> {
                try {
                    String serverMessage;
                    while ((serverMessage = reader.readLine()) != null) {
                        System.out.println("Server: " + serverMessage);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

            // Client sending messages
            while (true) {
                message = consoleReader.readLine();
                writer.println(message);
                if (message.equalsIgnoreCase("bye")) {
                    System.out.println("Client disconnected.");
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
