import java.io.*;
import java.net.*;
import java.util.Random;
import java.util.Scanner;

public class Sender{ 
    private static final int PORT = 12345;
    private static final int TIMEOUT_MS = 2000; 
    private static Random random = new Random();
    private static int base = 0; 
    private static int nextSeqNum = 0; 
    private static int frameCount;
    private static int windowSize; 
    private static String[] messages; // Array to hold user-defined messages

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Get user input for the number of frames and window size
        System.out.print("Enter the total number of frames to send: ");
        frameCount = scanner.nextInt();
        System.out.print("Enter the window size: ");
        windowSize = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        messages = new String[frameCount];
        for (int i = 0; i < frameCount; i++) {
            System.out.print("Enter message for frame " + i + ": ");
            messages[i] = scanner.nextLine();
        }

        try (ServerSocket serverSocket = new ServerSocket(PORT);
             Socket clientSocket = serverSocket.accept();
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

            System.out.println("Client connected. Ready to send messages...");

            while (nextSeqNum < frameCount) {
                // Send frames within the window
                while (nextSeqNum < base + windowSize && nextSeqNum < frameCount) {
                    sendFrame(out, nextSeqNum);
                    nextSeqNum++;
                }

                // Wait for ACKs with timeout
                waitForAcks(in, out);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void sendFrame(PrintWriter out, int seqNum) {
        String frame = "Frame " + seqNum + ": " + messages[seqNum];
        System.out.println("Server: Sent " + frame);

        // Simulate frame loss with a 30% chance
        if (random.nextInt(100) < 30) {
            System.out.println("Server: " + frame + " lost during transmission.");
            return; // Simulate loss
        }

        out.println(frame); // Send frame
    }

    private static void waitForAcks(BufferedReader in, PrintWriter out) throws IOException {
        long startTime;

        while (true) {
            startTime = System.currentTimeMillis();
            boolean ackReceived = false;

            // Wait for ACK or timeout
            while (System.currentTimeMillis() - startTime < TIMEOUT_MS) {
                if (in.ready()) {
                    String ack = in.readLine(); // Read ACK
                    if (ack != null) {
                        int ackNum = Integer.parseInt(ack.split(" ")[1]);
                        System.out.println("Server: Received " + ack);
                        
                        // Move base forward if the received ACK is valid
                        if (ackNum >= base) {
                            base = ackNum + 1;
                            ackReceived = true;
                        }
                    }
                }
            }

            // If no ACK was received within the timeout period, resend all unacknowledged frames
            if (!ackReceived) {
                System.out.println("Server: Timeout! Resending frames from " + base + " to " + (nextSeqNum - 1));
                nextSeqNum = base; // Reset nextSeqNum to base for resending
                for (int i = base; i < nextSeqNum + windowSize && i < frameCount; i++) {
                    sendFrame(out, i);
                }
            }

            // Break out of the loop if all frames have been acknowledged
            if (base >= frameCount) {
                break;
            }
        }
    }
}
