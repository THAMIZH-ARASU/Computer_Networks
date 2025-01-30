import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    private static final int PORT = 12345;
    private static final int MAX_RETRIES = 2;
    private static final int TIMEOUT = 100; // 100ms timeout
    private static final int MAX_DATAGRAM_SIZE = 512;
    private static long totalBytesSent = 0;
    private static long startTime;
    private static int successfulTransmissions = 0;
    private static int failedTransmissions = 0;

    public static void main(String[] args) {
        startTime = System.currentTimeMillis();
        try (Socket socket = new Socket("localhost", PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             Scanner scanner = new Scanner(System.in)) {

            socket.setSoTimeout(TIMEOUT);
            System.out.println("Connected to server. Ready to send messages...");

            while (true) {
                System.out.print("Enter message (or 'exit' to quit): ");
                String message = scanner.nextLine();
                
                if (message.equalsIgnoreCase("exit")) {
                    printStatistics();
                    break;
                }

                if (message.length() > MAX_DATAGRAM_SIZE) {
                    System.out.println("Message too long. Maximum size is " + MAX_DATAGRAM_SIZE + " bytes.");
                    continue;
                }

                sendMessage(message, out, in);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void sendMessage(String message, PrintWriter out, BufferedReader in) throws IOException {
        int retries = 0;
        boolean acknowledged = false;
        totalBytesSent += message.getBytes().length;

        while (!acknowledged && retries <= MAX_RETRIES) {
            try {
                System.out.println("Sending message: " + message);
                out.println(message);
                
                // Wait for acknowledgment
                String ack = in.readLine();
                if (ack != null && ack.equals("ACK")) {
                    System.out.println("Message acknowledged");
                    acknowledged = true;
                    successfulTransmissions++;
                }
            } catch (SocketTimeoutException e) {
                retries++;
                if (retries <= MAX_RETRIES) {
                    System.out.println("Timeout occurred. Retrying... (Attempt " + retries + " of " + MAX_RETRIES + ")");
                }
            }
        }

        if (!acknowledged) {
            System.out.println("ERROR: Message failed after " + MAX_RETRIES + " retries");
            failedTransmissions++;
        }
    }

    private static void printStatistics() {
        long duration = System.currentTimeMillis() - startTime;
        double throughput = (totalBytesSent * 1000.0) / duration; // bytes per second
        double reliability = (successfulTransmissions * 100.0) / (successfulTransmissions + failedTransmissions);
        double latency = (double)duration/successfulTransmissions;
        
        System.out.println("\nNetwork Statistics:");
        System.out.println("Total bytes sent: " + totalBytesSent);
        System.out.println("Successful transmissions: " + successfulTransmissions);
        System.out.println("Failed transmissions: " + failedTransmissions);
        System.out.println("Throughput: " + String.format("%.2f", throughput) + " bytes/second");
        System.out.println("Reliability: " + String.format("%.2f", reliability) + "%");
        System.out.println("Average latency: " + String.format("%.2f", latency) + "ms");
    }
}