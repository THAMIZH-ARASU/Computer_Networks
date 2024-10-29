import java.io.*;
import java.net.*;
import java.util.Random;
import java.util.Scanner;

public class Sender {
    private static final int PORT = 12345;
    private static Random random = new Random();
    private static final int TIME_OUT = 5000;
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT);
             Socket clientSocket = serverSocket.accept();
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             Scanner scanner = new Scanner(System.in)) {
            System.out.println("Client connected. Ready to send messages...");
            int frameNumber = 0;
            while (true) {
                System.out.print("Enter message to send (or 'exit' to quit): ");
                String message = scanner.nextLine();
                if (message.equalsIgnoreCase("exit")) break;
                sendFrame(out, in, frameNumber, message);
                frameNumber = 1 - frameNumber;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void sendFrame(PrintWriter out, BufferedReader in, int frameNumber, String message) throws IOException {
        System.out.println("Server: Sent frame " + frameNumber + ": " + message);

        String frame = frameNumber + ": " + message;
        
        if (random.nextInt(100) < 30) {
            System.out.println("Server: Frame " + frameNumber + " lost during transmission.");
            System.out.println("Resending the lost frame...");
            sendFrame(out, in, frameNumber, message);
            return;
        }
        out.println(frame);
        boolean ackReceived = false;
        long startTime = System.currentTimeMillis();
        while (!ackReceived) {
            try {
                if (in.ready()) {
                    String ack = in.readLine(); 
                    if (ack != null && ack.equals("ACK " + (1 - frameNumber))) {
                        System.out.println("Server: Received ACK for frame " + frameNumber);
                        ackReceived = true;
                    }
                } else if (System.currentTimeMillis() - startTime > TIME_OUT) { 
                    System.out.println("Server: ACK not received, resending frame " + frameNumber);
                    out.println(frame);
                    startTime = System.currentTimeMillis(); 
                }
            } catch (IOException e) {
                System.out.println("Server: Error receiving ACK.");
                break; 
            }
        }
    }
}
