import java.io.*;
import java.net.*;

public class ChatClient {
    private static final String SERVER_ADDRESS = "localhost";  // Use 'localhost' if running on the same machine
    private static final int SERVER_PORT = 12345;              // Must match the server's port

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
            System.out.println("Connected to the chat server.");

            // Start a new thread to read messages from the server
            new Thread(new ReadThread(socket)).start();

            // Main thread will handle sending messages
            new WriteThread(socket).run();
        } catch (IOException e) {
            System.out.println("Error connecting to server: " + e.getMessage());
        }
    }
}

class ReadThread implements Runnable {
    private BufferedReader in;

    public ReadThread(Socket socket) {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            System.out.println("Error getting input stream: " + e.getMessage());
        }
    }

    @Override
    public void run() {
        String serverMessage;
        try {
            while ((serverMessage = in.readLine()) != null) {
                System.out.println(serverMessage);
            }
        } catch (IOException e) {
            System.out.println("Connection to the server was closed: " + e.getMessage());
        }
    }
}

class WriteThread {
    private PrintWriter out;
    private BufferedReader consoleReader;

    public WriteThread(Socket socket) {
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            consoleReader = new BufferedReader(new InputStreamReader(System.in));
        } catch (IOException e) {
            System.out.println("Error getting output stream: " + e.getMessage());
        }
    }

    public void run() {
        String clientMessage;
        try {
            while ((clientMessage = consoleReader.readLine()) != null) {
                out.println(clientMessage);  // Send the message to the server
                if (clientMessage.equalsIgnoreCase("exit")) {
                    System.out.println("You have left the chat.");
                    break;  // Exit chat when user types "exit"
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading input: " + e.getMessage());
        }
    }
}
