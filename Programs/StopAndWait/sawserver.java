import java.io.*;
import java.net.*;
import java.util.Random;
import java.util.Scanner;

public class sawserver {
    private static final int PORT = 12345;
    private static Random random = new Random();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT);
             Socket clientSocket = serverSocket.accept();
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("Client connected. Ready to send messages...");
            int frameNumber = 0;

            while (true) {
                System.out.print("Enter message to send (or 'exit' to quit): ");
                String message = scanner.nextLine();
                if (message.equalsIgnoreCase("exit")) break;

                sendFrame(out, in, frameNumber, message);
                frameNumber++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void sendFrame(PrintWriter out, BufferedReader in, int frameNumber, String message) throws IOException {
        String frame = frameNumber + ": " + message;
        System.out.println("Server: Sent frame " + frameNumber + ": " + message);

        // Simulate frame loss with a 30% chance
        if (random.nextInt(100) < 30) {
            System.out.println("Server: Frame " + frameNumber + " lost during transmission.");
            return; // Simulate loss
        }

        out.println(frame); // Send frame
        boolean ackReceived = false;
        long startTime = System.currentTimeMillis();

        // Wait for acknowledgment with timeout logic
        while (!ackReceived) {
            try {
                // Check for ACK within a timeout period
                if (in.ready()) { // Check if input stream has data
                    String ack = in.readLine(); // Read ACK
                    if (ack != null && ack.equals("ACK " + frameNumber)) {
                        System.out.println("Server: Received ACK for frame " + frameNumber);
                        ackReceived = true; // Exit loop on valid ACK
                    }
                } else if (System.currentTimeMillis() - startTime > 2000) { // 2 seconds timeout
                    System.out.println("Server: ACK not received, resending frame " + frameNumber);
                    out.println(frame); // Resend frame
                    startTime = System.currentTimeMillis(); // Reset the timer
                }
            } catch (IOException e) {
                System.out.println("Server: Error receiving ACK.");
                break; // Exit if there's an error
            }
        }
    }
}
