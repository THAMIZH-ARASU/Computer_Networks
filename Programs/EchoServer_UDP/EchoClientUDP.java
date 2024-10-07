import java.net.*;
import java.util.Scanner;

public class EchoClientUDP {
    public static void main(String[] args) {
        DatagramSocket socket = null;
        try {
            String hostname = "localhost"; // Server hostname
            int port = 4445; // Server port

            socket = new DatagramSocket();
            InetAddress serverAddress = InetAddress.getByName(hostname);

            try (Scanner scanner = new Scanner(System.in)) {
                System.out.println("Enter your messages (type 'exit' to quit):");

                while (true) {
                    // Read message from user
                    String message = scanner.nextLine();
                    if (message.equalsIgnoreCase("exit")) {
                        break;
                    }

                    // Send the message to the server
                    byte[] buffer = message.getBytes();
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length, serverAddress, port);
                    socket.send(packet);

                    // Receive the echo from the server
                    byte[] receiveBuffer = new byte[1024];
                    DatagramPacket responsePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                    socket.receive(responsePacket);

                    String echoedMessage = new String(responsePacket.getData(), 0, responsePacket.getLength());
                    System.out.println("Echoed from server: " + echoedMessage);
                }
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
