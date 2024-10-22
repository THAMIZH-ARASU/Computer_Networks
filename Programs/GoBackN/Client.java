import java.io.*;
import java.net.*;

public class Client {
    private static final String SERVER_ADDRESS = "localhost"; // Change to the server's IP if needed
    private static final int PORT = 12345;
    private static final int TOTAL_FRAMES = 10; // Total frames to expect

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_ADDRESS, PORT);
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())) {
             
            System.out.println("Connected to server.");

            int expectedFrame = 0; // Expecting this frame
            while (true) {
                String frame = (String) in.readObject();
                
                if (frame.equals("Finished")) {
                    break; // Server indicates completion
                }

                System.out.println("Received: " + frame);

                // Simulate acknowledgment logic (could introduce packet loss for testing)
                if (expectedFrame == Integer.parseInt(frame.split(" ")[1])) {
                    out.writeObject("ACK " + expectedFrame);
                    expectedFrame++;
                } else {
                    // Simulating a lost frame scenario for testing (e.g., skip an ACK)
                    System.out.println("Lost ACK for Frame " + expectedFrame);
                }
            }

            System.out.println("Client finished receiving frames.");

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
