import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) {
        DatagramSocket socket = null;
        byte[] buffer = new byte[1024];
        long startTime, endTime;
        long totalBytesReceived = 0;
        int timeout = 5000; // 5 seconds timeout
        int port = 9876;
        int clientPort = 9877;
        
        try {
            socket = new DatagramSocket(clientPort);
            socket.setSoTimeout(timeout); // Set timeout to 5 seconds
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            FileOutputStream fileOutputStream = new FileOutputStream("received_file.txt");

            System.out.println("Client is waiting to receive the file...");
            startTime = System.nanoTime();

            while (true) {
                try {
                    socket.receive(packet); // Receive file chunk
                    endTime = System.nanoTime();
                    
                    // Calculate file size and update total bytes received
                    totalBytesReceived += packet.getLength();
                    fileOutputStream.write(packet.getData(), 0, packet.getLength());

                    // Print received file chunk information and propagation delay
                    long propagationDelay = (endTime - startTime) / 1000000; // Convert to milliseconds
                    System.out.println("Received " + packet.getLength() + " bytes. Propagation delay: " + propagationDelay + " ms.");
                    startTime = endTime; // Update start time for next chunk
                } catch (SocketTimeoutException e) {
                    System.out.println("Time to live period is exhausted. File transfer timed out.");
                    break; // Exit the loop if timeout occurs
                }
            }

            // Final output
            System.out.println("File transfer completed. Total file size: " + totalBytesReceived + " bytes.");
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        }
    }
}
