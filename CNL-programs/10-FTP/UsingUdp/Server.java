import java.io.*;
import java.net.*;

public class Server {
    public static void main(String[] args) {
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket(9876); 
            File file = new File("sample.txt"); 
            byte[] buffer = new byte[1024];
            FileInputStream fileInputStream = new FileInputStream(file);
            int bytesRead;
            
            System.out.println("Server is waiting for client connection...");
            
            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                DatagramPacket packet = new DatagramPacket(buffer, bytesRead, InetAddress.getByName("localhost"), 9877);
                socket.send(packet);
                System.out.println("Sent packet with " + bytesRead + " bytes.");
                Thread.sleep(100);
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
