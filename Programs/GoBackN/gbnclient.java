import java.io.*;
import java.net.*;
import java.util.Scanner;

public class GBNClient {
    public static void main(String[] args) throws Exception {
        final int PORT = 6262;
        final String HOST = "localhost";
        final int PACKET_SIZE = 256; // Fixed packet size
        final int WINDOW_SIZE = 5; // Fixed window size
        final int TIMEOUT = 200; // Timeout in milliseconds
        final int RETRIES = 3; // Max retries

        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the number of frames to send: ");
        int frameCount = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        byte[][] frames = new byte[frameCount][PACKET_SIZE];
        for (int i = 0; i < frameCount; i++) {
            System.out.print("Enter data for frame " + i + ": ");
            String input = scanner.nextLine();
            byte[] data = input.getBytes();
            System.arraycopy(data, 0, frames[i], 0, Math.min(data.length, PACKET_SIZE));
        }

        try (Socket clientSocket = new Socket(HOST, PORT);
             ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
             ObjectInputStream inputStream = new ObjectInputStream(clientSocket.getInputStream())) {

            System.out.println("Connected to the server.");

            outputStream.writeObject(WINDOW_SIZE);

            int base = 0, nextSeqNum = 0;
            int retries = 0;
            long startTime = System.currentTimeMillis();

            while (base < frameCount) {
                while (nextSeqNum < base + WINDOW_SIZE && nextSeqNum < frameCount) {
                    System.out.println("Sending frame: " + nextSeqNum);
                    outputStream.writeObject(nextSeqNum);
                    outputStream.writeObject(frames[nextSeqNum]);
                    nextSeqNum++;
                }

                try {
                    clientSocket.setSoTimeout(TIMEOUT);
                    int ack = (Integer) inputStream.readObject();
                    System.out.println("Acknowledgment received for frame: " + ack);
                    base = ack + 1;
                    retries = 0;
                } catch (SocketTimeoutException e) {
                    retries++;
                    System.out.println("Timeout! Retries: " + retries);
                    if (retries > RETRIES) {
                        System.out.println("Maximum retries reached. Moving to next packet.");
                        base++;
                        retries = 0;
                    } else {
                        nextSeqNum = base; // Resend from base
                    }
                }
            }

            outputStream.writeObject(-1); // End signal
            long endTime = System.currentTimeMillis();

            double throughput = (double) frameCount / ((endTime - startTime) / 1000.0);
            System.out.println("All frames sent. Throughput: " + throughput + " frames/second");
        }
    }
}
