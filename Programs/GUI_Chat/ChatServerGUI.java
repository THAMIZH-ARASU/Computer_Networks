import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServerGUI extends JFrame {
    private static Set<ClientHandler> clientHandlers = new HashSet<>();
    private static final int PORT = 12345;
    private static boolean isRunning = false;
    private ServerSocket serverSocket;
    private JButton startButton, stopButton;
    private JTextArea logArea;

    public ChatServerGUI() {
        setTitle("Chat Server");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // UI Components
        logArea = new JTextArea();
        logArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(logArea);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        startButton = new JButton("Start Server");
        stopButton = new JButton("Stop Server");
        stopButton.setEnabled(false);

        buttonPanel.add(startButton);
        buttonPanel.add(stopButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Start Server Action
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startServer();
            }
        });

        // Stop Server Action
        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stopServer();
            }
        });
    }

    public void startServer() {
        logArea.append("Starting server...\n");
        isRunning = true;
        startButton.setEnabled(false);
        stopButton.setEnabled(true);
        new Thread(() -> {
            try {
                serverSocket = new ServerSocket(PORT);
                logArea.append("Server started on port " + PORT + "\n");
                while (isRunning) {
                    Socket clientSocket = serverSocket.accept();
                    logArea.append("New client connected...\n");

                    // Create a new client handler
                    ClientHandler clientHandler = new ClientHandler(clientSocket);
                    clientHandlers.add(clientHandler);
                    new Thread(clientHandler).start();
                }
            } catch (IOException e) {
                if (isRunning) {
                    logArea.append("Server error: " + e.getMessage() + "\n");
                }
            }
        }).start();
    }

    public void stopServer() {
        logArea.append("Stopping server...\n");
        isRunning = false;
        stopButton.setEnabled(false);
        startButton.setEnabled(true);
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
            // Disconnect all clients
            for (ClientHandler client : clientHandlers) {
                client.sendMessage("Server is shutting down. You have been disconnected.");
                client.closeConnection();
            }
            clientHandlers.clear();
            logArea.append("Server stopped.\n");
        } catch (IOException e) {
            logArea.append("Error stopping server: " + e.getMessage() + "\n");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ChatServerGUI server = new ChatServerGUI();
            server.setVisible(true);
        });
    }

    // Inner class for client handling
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
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                // Get client's name
                out.println("Enter your name: ");
                clientName = in.readLine();
                if (clientName == null || clientName.trim().isEmpty()) {
                    out.println("Invalid name. Connection closed.");
                    closeConnection();
                    return;
                }
                broadcast(clientName + " has joined the chat.", this);

                String message;
                while ((message = in.readLine()) != null) {
                    if (message.equalsIgnoreCase("exit")) {
                        break;
                    }
                    broadcast(clientName + ": " + message, this);
                }
            } catch (IOException e) {
                logArea.append("Error handling client: " + e.getMessage() + "\n");
            } finally {
                closeConnection();
            }
        }

        // Send message to the client
        public void sendMessage(String message) {
            out.println(message);
        }

        // Broadcast message to all clients except the sender
        public void broadcast(String message, ClientHandler excludeClient) {
            for (ClientHandler client : clientHandlers) {
                if (client != excludeClient) {
                    client.sendMessage(message);
                }
            }
        }

        // Close connection
        public void closeConnection() {
            try {
                socket.close();
                clientHandlers.remove(this);
                logArea.append(clientName + " has disconnected.\n");
                broadcast(clientName + " has left the chat.", this);
            } catch (IOException e) {
                logArea.append("Error closing connection: " + e.getMessage() + "\n");
            }
        }
    }
}
