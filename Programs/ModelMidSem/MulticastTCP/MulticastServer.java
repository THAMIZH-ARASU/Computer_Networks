import java.io.*;
import java.net.*;
import java.util.*;

public class MulticastServer {
    // Map to store clients connected on different ports
    private static Map<Integer, Socket> clientSockets = new HashMap<>();

    public static void main(String[] args) {
        try {
            // Server listens on multiple ports for client connections
            ServerSocket serverSocket;
            int[] ports = {5000, 5001, 5002, 5003, 5004};  // List of client ports

            for (int port : ports) {
                serverSocket = new ServerSocket(port);
                new ClientHandler(serverSocket).start();  // Handling client connections on separate threads
                System.out.println("Server listening on port " + port);
            }

            // Take user input to send messages to group
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                System.out.println("Enter group number to send message (1 or 2):");
                int group = Integer.parseInt(reader.readLine());

                if (group == 1 || group == 2) {
                    System.out.println("Enter message to send:");
                    String message = reader.readLine();
                    sendToGroup(group, message);
                } else {
                    System.out.println("Invalid group number. Please try again.");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to send message to a specific group
    private static void sendToGroup(int group, String message) {
        List<Integer> group1 = Arrays.asList(5000, 5001, 5002);
        List<Integer> group2 = Arrays.asList(5003, 5004);

        List<Integer> targetGroup = (group == 1) ? group1 : group2;

        for (Integer port : targetGroup) {
            Socket clientSocket = clientSockets.get(port);
            if (clientSocket != null) {
                try {
                    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                    out.println(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Client handler thread to handle client connections
    static class ClientHandler extends Thread {
        private ServerSocket serverSocket;

        public ClientHandler(ServerSocket serverSocket) {
            this.serverSocket = serverSocket;
        }

        @Override
        public void run() {
            try {
                Socket socket = serverSocket.accept();
                int port = socket.getPort();
                clientSockets.put(port, socket);
                System.out.println("Client connected on port: " + port);

                // Listen for messages from the server and print them
                BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String message;
                while ((message = input.readLine()) != null) {
                    System.out.println("Received from client (" + port + "): " + message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
