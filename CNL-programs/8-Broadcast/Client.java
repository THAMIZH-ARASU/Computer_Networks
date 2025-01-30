import java.net.*;

public class Client{
    public static void main(String[] args) throws Exception {
        try (DatagramSocket socket = new DatagramSocket(null)) {
            SocketAddress socketAddress = new InetSocketAddress(4445);
            
            socket.setReuseAddress(true);
            socket.bind(socketAddress);

            byte[] buffer = new byte[1024];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

            System.out.println("Listening for broadcast messages...");

            while (true) {
                socket.receive(packet);
                String receivedMessage = new String(packet.getData(), 0, packet.getLength());
                System.out.println("Received broadcast: " + receivedMessage);
            }
        }
    }
}
