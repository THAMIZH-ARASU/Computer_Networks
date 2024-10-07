import java.net.*;

public class EchoServerUDP {
    public static void main(String[] args) {
        DatagramSocket socket = null;
        try {
            int port = 4445; // Server port
            socket = new DatagramSocket(port);
            System.out.println("Server is listening on port " + port);

            byte[] buffer = new byte[1024];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

            while (true) {
                // Receive packet from client
                socket.receive(packet);
                String receivedMessage = new String(packet.getData(), 0, packet.getLength());
                System.out.println("Received from client: " + receivedMessage);

                // Echo the message back to the client
                InetAddress clientAddress = packet.getAddress();
                int clientPort = packet.getPort();
                DatagramPacket echoPacket = new DatagramPacket(packet.getData(), packet.getLength(), clientAddress, clientPort);
                socket.send(echoPacket);

                // Clear the buffer after each message
                packet.setLength(buffer.length);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        }
    }
}

