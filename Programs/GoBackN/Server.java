import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class Server {
    private static final int PORT = 12345;
    private static final int PACKET_SIZE = 256;
    private static final int TIMEOUT_DURATION = 200; // Timeout in ms
    private static final int MAX_RETRIES = 3; // Maximum retries for a frame
    private static final int WINDOW_SIZE = 5; // Window size for Go-Back-N
    private static Random random = new Random();

    private static int frameCount; // Total number of frames to send
    private static String[] messages; // Array to hold messages

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT);
             Socket clientSocket = serverSocket.accept();
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             BufferedWriter fileWriter = new BufferedWriter(new FileWriter("received_data.txt"))) {

            System.out.println("Client connected. Ready to send messages...");

            // Prepare messages
            frameCount = 10; // Example frame count
            messages = new String[frameCount];
            for (int i = 0; i < frameCount; i++) {
                messages[i] = "Data Packet " + i + ": " + repeat("X", PACKET_SIZE / 15);
            }

            boolean[] ackReceived = new boolean[frameCount];
            int[] retries = new int[frameCount]; // Track retries for each frame
            int base = 0;
            int nextSeqNum = 0;

            while (base < frameCount) {
                // Send frames within the window
                while (nextSeqNum < base + WINDOW_SIZE && nextSeqNum < frameCount) {
                    if (retries[nextSeqNum] < MAX_RETRIES) {
                        sendFrame(out, nextSeqNum);
                    }
                    nextSeqNum++;
                }

                // Wait for ACKs or timeout
                long startTime = System.currentTimeMillis();
                while (System.currentTimeMillis() - startTime < TIMEOUT_DURATION) {
                    if (in.ready()) {
                        String ack = in.readLine();
                        if (ack != null) {
                            int ackNum = Integer.parseInt(ack.split(" ")[1]);
                            System.out.println("Server: Received " + ack);

                            // Mark frame as acknowledged
                            if (ackNum >= base) {
                                ackReceived[ackNum] = true;

                                // Slide the window
                                while (base < frameCount && ackReceived[base]) {
                                    fileWriter.write(messages[base] + "\n");
                                    base++;
                                }
                            }
                        }
                    }
                }

                // Handle timeout: retransmit frames
                for (int i = base; i < nextSeqNum; i++) {
                    if (!ackReceived[i] && retries[i] < MAX_RETRIES) {
                        retries[i]++;
                        System.out.println("Server: Timeout occurred, retransmitting frame " + i + " (Retry " + retries[i] + ")");
                        sendFrame(out, i);
                    } else if (retries[i] >= MAX_RETRIES) {
                        System.out.println("Server: Maximum retries reached for frame " + i + ". Skipping.");
                    }
                }
            }

            System.out.println("All frames sent and acknowledged.");
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
            return;
        }

        out.println(frame); // Send frame
    }

    private static String repeat(String str, int count) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < count; i++) {
            result.append(str);
        }
        return result.toString();
    }
}
