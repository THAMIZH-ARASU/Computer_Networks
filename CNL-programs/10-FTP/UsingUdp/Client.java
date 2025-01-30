import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) {
        DatagramSocket socket = null;
        byte[] buffer = new byte[1024];
        long startTime, endTime;
        long totalBytesReceived = 0;
        int timeout = 5000;
        int port = 9876;
        int clientPort = 9877;
        
        try {
            socket = new DatagramSocket(clientPort);
            socket.setSoTimeout(timeout);
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            FileOutputStream fileOutputStream = new FileOutputStream("received_file.txt");

            System.out.println("Client is waiting to receive the file...");
            startTime = System.nanoTime();

            while (true) {
                try {
                    socket.receive(packet); 
                    endTime = System.nanoTime();
                    
                    totalBytesReceived += packet.getLength();
                    fileOutputStream.write(packet.getData(), 0, packet.getLength());

                    long propagationDelay = (endTime - startTime) / 1000000; 
                    System.out.println("Received " + packet.getLength() + " bytes. Propagation delay: " + propagationDelay + " ms.");
                    startTime = endTime; 
                } catch (SocketTimeoutException e) {
                    System.out.println("Time to live period is exhausted. File transfer timed out.");
                    break; 
                }
            }

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
