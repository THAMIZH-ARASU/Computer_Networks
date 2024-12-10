import java.io.*;
import java.net.*;
import java.util.*;
@SuppressWarnings("unused")
public class GoBackNClient {
    private static final int PACKET_SIZE = 256;
    private static final int TIMEOUT = 200; // Timeout in milliseconds
    private static final int MAX_RETRIES = 5; // Maximum retries for a packet
    
    private static final int WINDOW_SIZE = 5; // Window size for Go-Back-N ARQ

    @SuppressWarnings("CallToPrintStackTrace")
    public static void main(String[] args) {
        String filename = "data_to_send.txt"; // File to send
        try (Socket socket = new Socket("localhost", 1234);
             FileInputStream fileInputStream = new FileInputStream(filename);
             BufferedOutputStream outStream = new BufferedOutputStream(socket.getOutputStream());
             DataInputStream inStream = new DataInputStream(socket.getInputStream())) {

            byte[] packet = new byte[PACKET_SIZE];
            int bytesRead, packetCount = 0;
            long startTime = System.currentTimeMillis();
            List<Integer> lostPackets = new ArrayList<>();

            Queue<byte[]> sendWindow = new LinkedList<>(); // Send window for Go-Back-N

            while ((bytesRead = fileInputStream.read(packet)) != -1) {
                packetCount++;
                byte[] dataToSend = Arrays.copyOf(packet, bytesRead);

                // Add packet to the send window
                sendWindow.add(dataToSend);

                // Send the packets in the window
                while (!sendWindow.isEmpty()) {
                    byte[] currentPacket = sendWindow.poll();
                    boolean ackReceived = false;
                    int retryCount = 0;

                    // Send and retry packet if necessary
                    while (!ackReceived && retryCount < MAX_RETRIES) {
                        outStream.write(currentPacket);
                        outStream.flush();
                        System.out.println("Sent packet " + packetCount + " (Attempt " + (retryCount + 1) + ")");
                        socket.setSoTimeout(TIMEOUT); // Set the socket timeout to 200ms

                        try {
                            int ack = inStream.readInt(); // Read acknowledgment (packet number)
                            if (ack == packetCount) {
                                ackReceived = true;
                                System.out.println("Acknowledgment received for packet " + packetCount);
                            }
                        } catch (SocketTimeoutException e) {
                            retryCount++;
                            System.out.println("Timeout occurred for packet " + packetCount + ", retrying...");
                        }
                    }

                    if (!ackReceived) {
                        lostPackets.add(packetCount); // Track lost packets
                    }
                }
            }

            long endTime = System.currentTimeMillis();
            double elapsedTime = (endTime - startTime) / 1000.0; // seconds
            System.out.println("Transmission completed. Total time: " + elapsedTime + " seconds");

            // Measure throughput
            double throughput = (packetCount * PACKET_SIZE) / elapsedTime;
            System.out.println("Throughput: " + throughput + " bytes per second");

            // Log lost packets
            if (!lostPackets.isEmpty()) {
                System.out.println("Lost packets: " + lostPackets);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
