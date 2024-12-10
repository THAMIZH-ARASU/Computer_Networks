import java.io.*;
import java.net.*;
import java.util.Random;

public class Receiver {
    private static final int PORT = 12345;

    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            System.out.println("Client is ready to receive messages...");
            while (true) {
                String message = in.readLine(); 
                if (message == null) break; 
                int frameNumber = Integer.parseInt(message.split(":")[0].trim());
                System.out.println("Client: Received frame " + frameNumber + ": " + message);
                frameNumber = 1 - frameNumber;
                if (new Random().nextInt(100) < 30) {
                    System.out.println("Client: ACK " + frameNumber + " lost.");
                    continue; 
                }
                String ack = "ACK " + frameNumber;
                out.println(ack); 
                System.out.println("Client: Sent " + ack);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
