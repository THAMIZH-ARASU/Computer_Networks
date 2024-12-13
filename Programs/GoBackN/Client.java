import java.io.*;
import java.net.Socket;

public class Client {
    private static final int PORT = 12345;
    private static final int PACKET_SIZE = 256; // Packet size in bytes
    private static final int TIMEOUT = 200;    // Timeout in milliseconds

    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            System.out.println("Client is ready to receive data...");
            int expectedSeqNum = 0;

            while (true) {
                String message = in.readLine(); // Receive frame
                if (message == null) break; // Exit if no more messages

                System.out.println("Client: Received " + message);
                int seqNum = Integer.parseInt(message.split(":")[0].split(" ")[1]); // Extract sequence number

                if (seqNum == expectedSeqNum) {
                    // Send ACK for in-order frame
                    String ack = "ACK " + seqNum;
                    out.println(ack);
                    System.out.println("Client: Sent " + ack);
                    expectedSeqNum++;
                } else {
                    System.out.println("Client: Ignored out-of-order frame " + seqNum);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
