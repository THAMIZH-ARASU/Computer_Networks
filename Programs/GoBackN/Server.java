import java.io.*;
import java.net.*;

public class Server {
    private static final int PORT = 12345;
    private static final int WINDOW_SIZE = 4; // Window size for Go-Back-N
    private static final int TOTAL_FRAMES = 10; // Total frames to send

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started. Waiting for client...");

            try (Socket clientSocket = serverSocket.accept();
                 ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
                 ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream())) {
                
                System.out.println("Client connected.");

                int base = 0; // Base of the window
                while (base < TOTAL_FRAMES) {
                    // Send packets in the window
                    for (int i = base; i < Math.min(base + WINDOW_SIZE, TOTAL_FRAMES); i++) {
                        out.writeObject("Frame " + i);
                        System.out.println("Sent: Frame " + i);
                    }

                    // Wait for acknowledgments
                    for (int i = base; i < Math.min(base + WINDOW_SIZE, TOTAL_FRAMES); i++) {
                        try {
                            String ack = (String) in.readObject();
                            System.out.println("Received: " + ack);
                            // If ACK is for the base, move the window forward
                            if (ack.equals("ACK " + base)) {
                                base++;
                            }
                        } catch (EOFException e) {
                            System.out.println("Client disconnected.");
                            return;
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }

                // Indicate completion
                out.writeObject("Finished");
                System.out.println("All frames sent.");

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
