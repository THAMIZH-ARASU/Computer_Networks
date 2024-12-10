import java.io.*;
import java.net.*;

public class Server {
    public static void main(String[] args) {
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket(9876); // Server port
            File file = new File("sample.txt"); // The file to be sent
            byte[] buffer = new byte[1024]; // Buffer size for file chunks
            FileInputStream fileInputStream = new FileInputStream(file);
            int bytesRead;
            
            System.out.println("Server is waiting for client connection...");
            
            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                // Send file chunk to client
                DatagramPacket packet = new DatagramPacket(buffer, bytesRead, InetAddress.getByName("localhost"), 9877);
                socket.send(packet);
                System.out.println("Sent packet with " + bytesRead + " bytes.");
                Thread.sleep(100); // Optional: Wait before sending next chunk
            }
            fileInputStream.close();
            System.out.println("File sent successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        }
    }
}
