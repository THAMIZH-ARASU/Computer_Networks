import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.*;

public class BroadcastServerUDP {
    public static void main(String[] args) throws Exception {
        DatagramSocket socket = new DatagramSocket(); // Create a socket
        socket.setBroadcast(true); // Enable broadcasting

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter your broadcast message: ");
        String message = br.readLine();
        byte[] buffer = message.getBytes();

        InetAddress broadcastAddress = InetAddress.getByName("255.255.255.255"); // Broadcast address
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, broadcastAddress, 4445);

        socket.send(packet); // Send the broadcast packet
        System.out.println("Broadcast message sent!");
        socket.close(); // Close the socket
    }
}
