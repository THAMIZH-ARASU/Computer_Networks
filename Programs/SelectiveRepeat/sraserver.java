import java.io.*;
import java.net.*;
import java.util.Random;
import java.util.Scanner;

public class sraserver{ 
    private static final int PORT = 12345; 
    private static final int TIMEOUT_MS = 2000; // Timeout duration in milliseconds
    private static Random random = new Random();

    // Instance variables for tracking state
    private static int nextSeqNum = 0; // Next sequence number to send
    private static int frameCount; // Total number of frames to send
    private static int windowSize; // Size of the sliding window
    private static String[] messages; // Array to hold user-defined messages
    private static boolean[] ackReceived; // Array to track received ACKs

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Get user input for the number of frames and window size
        System.out.print("Enter the total number of frames to send: ");
        frameCount = scanner.nextInt();
        System.out.print("Enter the window size: ");
        windowSize = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        messages = new String[frameCount];
        ackReceived = new boolean[frameCount]; // Initialize ACK tracking
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
                for (int i = 0; i < windowSize && nextSeqNum < frameCount; i++) {
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
            boolean allAcksReceived = true;

            // Wait for ACK or timeout
            while (System.currentTimeMillis() - startTime < TIMEOUT_MS) {
                if (in.ready()) {
                    String ack = in.readLine(); // Read ACK
                    if (ack != null) {
                        int ackNum = Integer.parseInt(ack.split(" ")[1]);
                        System.out.println("Server: Received " + ack);
                        ackReceived[ackNum] = true; // Mark ACK as received

                        // Check if all previous ACKs are received
                        for (int i = 0; i < ackReceived.length; i++) {
                            if (!ackReceived[i]) {
                                allAcksReceived = false;
                                break;
                            }
                        }

                        // Break out if all ACKs have been received
                        if (allAcksReceived) {
                            System.out.println("All ACKs received.");
                            return;
                        }
                    }
                }
            }

            // If no ACKs were received within the timeout period, resend unacknowledged frames
            System.out.println("Server: Timeout! Resending unacknowledged frames...");
            for (int i = 0; i < frameCount; i++) {
                if (!ackReceived[i]) {
                    sendFrame(out, i);
                }
            }
        }
    }
}
