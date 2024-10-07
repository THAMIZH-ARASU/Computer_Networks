import java.net.*;

public class BroadcastServerUDP {
    public static void main(String[] args) throws Exception {
        DatagramSocket socket = new DatagramSocket(); // Create a socket
        socket.setBroadcast(true); // Enable broadcasting

        String message = "Broadcast message from server";
        byte[] buffer = message.getBytes();

        InetAddress broadcastAddress = InetAddress.getByName("255.255.255.255"); // Broadcast address
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, broadcastAddress, 4445);

        socket.send(packet); // Send the broadcast packet
        System.out.println("Broadcast message sent!");
        socket.close(); // Close the socket
    }
}
