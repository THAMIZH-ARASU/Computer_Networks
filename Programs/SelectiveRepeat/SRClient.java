import java.io.*;
import java.net.Socket;
import java.util.Random;

public class SRClient {
    private static final int PORT = 12345;

    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            System.out.println("Client is ready to receive messages...");

            while (true) {
                String message = in.readLine(); // Receive frame
                if (message == null) break; // Exit if no more messages

                System.out.println("Client: Received " + message);
                int seqNum = Integer.parseInt(message.split(":")[0].split(" ")[1]); // Extract sequence number

                // Simulate ACK loss with a 30% chance
                if (new Random().nextInt(100) < 30) {
                    System.out.println("Client: ACK for " + seqNum + " lost.");
                    continue; // Simulate loss
                }

                // Send ACK for the received frame
                String ack = "ACK " + seqNum;
                out.println(ack); // Send ACK
                System.out.println("Client: Sent " + ack);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
