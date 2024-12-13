import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

public class SRServer {
    private static final int PORT = 12345;
    private static Random random = new Random();

    private static int frameCount; // Total number of frames to send
    private static int windowSize; // Size of the sliding window
    private static String[] messages; // Array to hold user-defined messages
    private static ConcurrentHashMap<Integer, Boolean> ackReceived = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Get user input for the number of frames, window size, and retries
        System.out.print("Enter the total number of frames to send: ");
        frameCount = scanner.nextInt();
        System.out.print("Enter the window size: ");
        windowSize = scanner.nextInt();
        System.out.print("Enter the timeout duration in ms: ");
        int timeoutDuration = scanner.nextInt();
        System.out.print("Enter the maximum retransmission count: ");
        int maxRetries = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        messages = new String[frameCount];
        for (int i = 0; i < frameCount; i++) {
            System.out.print("Enter message for frame " + i + ": ");
            messages[i] = scanner.nextLine();
            ackReceived.put(i, false);
        }

        try (ServerSocket serverSocket = new ServerSocket(PORT);
             Socket clientSocket = serverSocket.accept();
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             BufferedWriter fileWriter = new BufferedWriter(new FileWriter("received_data.txt"))) {

            System.out.println("Client connected. Ready to send messages...");

            int base = 0;

            while (base < frameCount) {
                // Send frames within the window
                for (int i = base; i < Math.min(base + windowSize, frameCount); i++) {
                    if (!ackReceived.get(i)) {
                        sendFrame(out, i);
                    }
                }

                // Wait for ACKs and handle retransmissions
                long startTime = System.currentTimeMillis();
                boolean timeoutOccurred = false;

                while (System.currentTimeMillis() - startTime < timeoutDuration) {
                    if (in.ready()) {
                        String ack = in.readLine();
                        if (ack != null) {
                            int ackNum = Integer.parseInt(ack.split(" ")[1]);
                            System.out.println("Server: Received " + ack);
                            ackReceived.put(ackNum, true);
                            if (ackNum == base) {
                                while (base < frameCount && ackReceived.get(base)) {
                                    fileWriter.write(messages[base] + "\n");
                                    base++;
                                }
                            }
                        }
                    }
                }

                // Handle timeout and retransmission
                for (int i = base; i < Math.min(base + windowSize, frameCount); i++) {
                    if (!ackReceived.get(i)) {
                        if (timeoutOccurred) {
                            sendFrame(out, i);
                        }
                    }
                }
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
}