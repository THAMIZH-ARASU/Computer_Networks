import java.io.*;
import java.net.*;
import java.util.*;

public class MulticastServer {
    // Map to store the clients grouped by a group name (key = group name, value = list of clients)
    private static Map<String, List<ClientHandler>> groupClients = new HashMap<>();

    public static void main(String[] args) {
        int port = 5000;
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server is listening on port " + port);

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New client connected");

                // Handle client in a new thread
                new ClientHandler(socket).start();
            }
        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    // Method to send a message to all clients in the specified group
    public static void sendMessageToGroup(String message, String groupName, ClientHandler sender) throws IOException {
        List<ClientHandler> group = groupClients.get(groupName);
        if (group != null) {
            for (ClientHandler client : group) {
                if (client != sender) { // Avoid sending the message to the sender
                    client.sendMessage(message);
                }
            }
        }
    }

    // Inner class to handle each client connection
    private static class ClientHandler extends Thread {
        private Socket socket;
        private String groupName;
        private PrintWriter writer;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void sendMessage(String message) throws IOException {
            writer.println(message);
        }

        public void run() {
            try (InputStream input = socket.getInputStream();
                 BufferedReader reader = new BufferedReader(new InputStreamReader(input))) {

                OutputStream output = socket.getOutputStream();
                writer = new PrintWriter(output, true);

                // Ask the client for the group they want to join
                writer.println("Enter the group name you want to join:");
                groupName = reader.readLine();

                // Add client to the group
                synchronized (groupClients) {
                    groupClients.putIfAbsent(groupName, new ArrayList<>());
                    groupClients.get(groupName).add(this);
                }

                writer.println("You have joined group: " + groupName);

                String clientMessage;
                while ((clientMessage = reader.readLine()) != null) {
                    System.out.println("Message from group [" + groupName + "]: " + clientMessage);
                    // Send message to all clients in the same group
                    MulticastServer.sendMessageToGroup(clientMessage, groupName, this);
                }

            } catch (IOException ex) {
                System.out.println("Client disconnected: " + ex.getMessage());
            } finally {
                // Remove the client from the group when they disconnect
                if (groupName != null) {
                    synchronized (groupClients) {
                        groupClients.get(groupName).remove(this);
                    }
                }
                try {
                    socket.close();
                } catch (IOException e) {
                    System.out.println("Error closing socket: " + e.getMessage());
                }
            }
        }
    }
}
