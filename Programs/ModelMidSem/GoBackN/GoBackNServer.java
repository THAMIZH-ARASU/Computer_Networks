import java.io.*;
import java.net.*;
import java.util.Random;

public class GoBackNServer {
    @SuppressWarnings("CallToPrintStackTrace")
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(1234)) {
            System.out.println("Server is waiting for a connection...");
            try (Socket socket = serverSocket.accept()) {
                System.out.println("Client connected.");
                
                // Input and output streams
                BufferedInputStream inStream = new BufferedInputStream(socket.getInputStream());
                DataOutputStream outStream = new DataOutputStream(socket.getOutputStream());
                try (FileOutputStream fileOutputStream = new FileOutputStream("received_data.txt")) {
                    byte[] buffer = new byte[256];
                    int packetCount = 0;
                    // Add this inside the main method, before sending the acknowledgment
                    Random random = new Random();
                    double lossProbability = 0.5; // 20% acknowledgment loss probability
                    // Continuously receive data packets
                    while (true) {
                        int bytesRead = inStream.read(buffer);
                        if (bytesRead == -1) break;  // End of stream, break the loop
                        
                        // Write data to file
                        fileOutputStream.write(buffer, 0, bytesRead);

                        // Simulate acknowledgment loss
                        if (random.nextDouble() < lossProbability) {
                            System.out.println("Simulating loss of acknowledgment for packet " + packetCount);
                            continue; // Skip sending acknowledgment
                        }
                        
                        // Acknowledge receipt of the packet
                        packetCount++;
                        outStream.writeInt(packetCount);  // Send acknowledgment as packet number
                        outStream.flush();
                        System.out.println("Received and acknowledged packet " + packetCount);
                    }
                    // Close resources
                }
            }
            System.out.println("Data received and written to file.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
