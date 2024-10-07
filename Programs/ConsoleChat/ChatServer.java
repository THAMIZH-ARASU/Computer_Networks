import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {
    private static Set<ClientHandler> clientHandlers = new HashSet<>();
    private static final int PORT = 12345;
    private static boolean isRunning = true;

    public static void main(String[] args) {
        System.out.println("Chat server started on port " + PORT + "...");

        // Create a thread to handle server shutdown via console input
        new Thread(() -> {
            BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
            try {
                String command;
                while ((command = consoleReader.readLine()) != null) {
                    if (command.equalsIgnoreCase("shutdown")) {
                        shutdownServer();
                        break;
                    }
                }
            } catch (IOException e) {
                System.out.println("Error reading server commands: " + e.getMessage());
            }
        }).start();

        // Start accepting clients
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (isRunning) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client trying to connect...");

                // Create a new client handler for each connected client
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                clientHandlers.add(clientHandler);

                // Start a new thread for this client
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            if (isRunning) {
                System.out.println("Error in server: " + e.getMessage());
            } else {
                System.out.println("Server shut down.");
            }
        }
    }

    // Method to broadcast a message to all clients except the sender
    public static synchronized void broadcast(String message, ClientHandler excludeClient) {
        for (ClientHandler client : clientHandlers) {
            if (client != excludeClient) {
                client.sendMessage(message);
            }
        }
    }

    // Remove a client from the client list
    public static synchronized void removeClient(ClientHandler clientHandler) {
        clientHandlers.remove(clientHandler);
        System.out.println("Client " + clientHandler.getClientName() + " disconnected.");
    }

    // Shutdown the server and disconnect all clients
    public static synchronized void shutdownServer() {
        System.out.println("Shutting down server...");
        isRunning = false;
        // Disconnect all clients
        for (ClientHandler client : clientHandlers) {
            client.sendMessage("Server is shutting down. You have been disconnected.");
            client.closeConnection();
        }
        clientHandlers.clear();
    }
}

class ClientHandler implements Runnable {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private String clientName;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            // Input and output streams for communication
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            // Ask for client's name
            out.println("Enter your name: ");
            clientName = in.readLine();
            if (clientName == null || clientName.trim().isEmpty()) {
                out.println("Invalid name. Connection closed.");
                closeConnection();
                return;
            }
            ChatServer.broadcast(clientName + " has joined the chat.", this);
            out.println("Welcome to the chat, " + clientName + "! Type 'exit' to leave.");

            String message;
            // Read messages from this client and broadcast them
            while ((message = in.readLine()) != null) {
                if (message.equalsIgnoreCase("exit")) {
                    break;
                }
                ChatServer.broadcast(clientName + ": " + message, this);
            }
        } catch (IOException e) {
            System.out.println("Error handling client: " + e.getMessage());
        } finally {
            closeConnection();
        }
    }

    // Send a message to this client
    public void sendMessage(String message) {
        out.println(message);
    }

    // Get the client's name
    public String getClientName() {
        return clientName;
    }

    // Close the connection and notify the server
    public void closeConnection() {
        try {
            socket.close();
            ChatServer.removeClient(this);
            ChatServer.broadcast(clientName + " has left the chat.", this);
        } catch (IOException e) {
            System.out.println("Error closing connection: " + e.getMessage());
        }
    }
}
