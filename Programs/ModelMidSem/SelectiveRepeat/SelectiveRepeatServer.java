import java.io.*;
import java.net.*;

public class SelectiveRepeatServer {
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
                    boolean[] ackReceived = new boolean[100];  // Assuming a max of 100 packets, adjust as needed
                    // Continuously receive data packets
                    while (true) {
                        int bytesRead = inStream.read(buffer);
                        if (bytesRead == -1) break;  // End of stream, break the loop
                        
                        // Write data to file
                        fileOutputStream.write(buffer, 0, bytesRead);
                        
                        // Acknowledge receipt of the packet
                        packetCount++;
                        ackReceived[packetCount - 1] = true;
                        outStream.writeInt(packetCount); // Acknowledge the current packet number
                        
                        System.out.println("Received packet " + packetCount);
                        
                        // Simulate processing delay
                        Thread.sleep(50);
                    }
                    // Close resources
                }
            }
            System.out.println("Data received and written to file.");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
