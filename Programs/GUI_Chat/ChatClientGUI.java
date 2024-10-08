import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;

public class ChatClientGUI extends JFrame {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private JTextArea chatArea;
    private JTextField messageField;
    private JTextField nameField;
    private JButton connectButton;
    private JButton sendButton;
    private String clientName;

    public ChatClientGUI() {
        setTitle("Chat Client");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Chat display area
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(chatArea);
        add(scrollPane, BorderLayout.CENTER);

        // Input panel with message field and buttons
        JPanel inputPanel = new JPanel(new BorderLayout());
        messageField = new JTextField();
        sendButton = new JButton("Send");
        sendButton.setEnabled(false);

        inputPanel.add(messageField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);
        add(inputPanel, BorderLayout.SOUTH);

        // Connection panel for entering name and connecting
        JPanel connectPanel = new JPanel(new BorderLayout());
        nameField = new JTextField();
        connectButton = new JButton("Connect");
        connectPanel.add(new JLabel("Enter your name: "), BorderLayout.WEST);
        connectPanel.add(nameField, BorderLayout.CENTER);
        connectPanel.add(connectButton, BorderLayout.EAST);
        add(connectPanel, BorderLayout.NORTH);

        // Action for connect button
        connectButton.addActionListener(e -> connectToServer());

        // Action for send button
        sendButton.addActionListener(e -> sendMessage());

        // Enter key sends message
        messageField.addActionListener(e -> sendMessage());
    }

    public void connectToServer() {
        clientName = nameField.getText().trim();
        if (clientName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter your name.");
            return;
        }

        try {
            socket = new Socket("localhost", 12345);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Send the client's name to the server
            out.println(clientName);

            // Start a new thread to listen for messages from the server
            new Thread(new ReadThread()).start();

            chatArea.append("Connected to the server as " + clientName + "\n");
            sendButton.setEnabled(true);
            connectButton.setEnabled(false);
            nameField.setEditable(false);
        } catch (IOException e) {
            chatArea.append("Error connecting to server: " + e.getMessage() + "\n");
        }
    }

    public void sendMessage() {
        String message = messageField.getText().trim();
        if (!message.isEmpty()) {
            out.println(message);
            messageField.setText("");
            if (message.equalsIgnoreCase("exit")) {
                disconnectFromServer();
            }
        }
    }

    public void disconnectFromServer() {
        try {
            socket.close();
            chatArea.append("Disconnected from server.\n");
            sendButton.setEnabled(false);
        } catch (IOException e) {
            chatArea.append("Error disconnecting: " + e.getMessage() + "\n");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ChatClientGUI client = new ChatClientGUI();
            client.setVisible(true);
        });
    }

    // Inner class to handle receiving messages from the server
    class ReadThread implements Runnable {
        @Override
        public void run() {
            String message;
            try {
                while ((message = in.readLine()) != null) {
                    chatArea.append(message + "\n");
                }
            } catch (IOException e) {
                chatArea.append("Connection lost: " + e.getMessage() + "\n");
            }
        }
    }
}
