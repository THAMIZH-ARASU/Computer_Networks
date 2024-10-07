import java.net.*;

public class BroadcastClientUDP {
    public static void main(String[] args) throws Exception {
        try (DatagramSocket socket = new DatagramSocket(null)) {
            SocketAddress socketAddress = new InetSocketAddress(4445);
            
            socket.setReuseAddress(true); // Allows multiple clients to bind to the same port
            socket.bind(socketAddress); // Bind to port 4445 (same as server)

            byte[] buffer = new byte[1024]; // Buffer to store received data
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

            System.out.println("Listening for broadcast messages...");

            while (true) {
                socket.receive(packet); // Receive broadcast packet
                String receivedMessage = new String(packet.getData(), 0, packet.getLength());
                System.out.println("Received broadcast: " + receivedMessage);
            }
        }
    }
}
