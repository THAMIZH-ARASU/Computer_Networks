import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    private static Map<String, List<ClientHandler>> groupClients = new HashMap<>();
    private static final Scanner scanner = new Scanner(System.in);
    private static final int port = 5000;
    public static void main(String[] args) {
        
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server is listening on port " + port);

            // Thread for server input
            new Thread(() -> handleServerInput()).start();

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New client connected");

                new ClientHandler(socket).start();
            }
        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private static void handleServerInput() {
        while (true) {
            System.out.print("Enter group name and message (format: groupName: message): ");
            String input = scanner.nextLine();
            String[] parts = input.split(": ", 2);
            if (parts.length == 2) {
                String groupName = parts[0].trim();
                String message = parts[1].trim();
                try {
                    sendMessageToGroup(message, groupName, null);
                } catch (IOException e) {
                    System.out.println("Error sending message to group: " + e.getMessage());
                }
            } else {
                System.out.println("Invalid format. Use 'groupName: message'");
            }
        }
    }

    public static void sendMessageToGroup(String message, String groupName, ClientHandler sender) throws IOException {
        List<ClientHandler> group = groupClients.get(groupName);
        if (group != null) {
            for (ClientHandler client : group) {
                if (client != sender) {
                    client.sendMessage(message);
                }
            }
        }else{
            System.out.println("No clients assigned in with the groupname" + groupName);
        }
    }

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

                writer.println("Enter the group name you want to join:");
                groupName = reader.readLine();

                synchronized (groupClients) {
                    groupClients.putIfAbsent(groupName, new ArrayList<>());
                    groupClients.get(groupName).add(this);
                }

                writer.println("You have joined group: " + groupName);

                String clientMessage;
                while ((clientMessage = reader.readLine()) != null) {
                    System.out.println("Message from group [" + groupName + "]: " + clientMessage);
                    Server.sendMessageToGroup(clientMessage, groupName, this);
                }

            } catch (IOException ex) {
                System.out.println("Client disconnected: " + ex.getMessage());
            } finally {
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
