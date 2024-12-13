import java.io.*;
import java.net.*;

public class GBNServer {
    public static void main(String[] args) throws Exception {
        final int PORT = 6262;
        final String OUTPUT_FILE = "received_data.txt";

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started. Waiting for client connection...");

            try (Socket clientSocket = serverSocket.accept();
                 ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
                 ObjectInputStream inputStream = new ObjectInputStream(clientSocket.getInputStream());
                 BufferedWriter writer = new BufferedWriter(new FileWriter(OUTPUT_FILE))) {

                System.out.println("Client connected.");

                int windowSize = (Integer) inputStream.readObject(); // Fixed at 5
                System.out.println("Window size: " + windowSize);
                
                int expectedFrame = 0;

                while (true) {
                    int frameNumber = (Integer) inputStream.readObject();
                    if (frameNumber == -1) break; // End signal

                    byte[] data = (byte[]) inputStream.readObject();

                    if (frameNumber == expectedFrame) {
                        System.out.println("Frame " + frameNumber + " received.");
                        writer.write(new String(data));
                        writer.flush();
                        expectedFrame++;
                    } else {
                        System.out.println("Out-of-order frame received. Expected: " + expectedFrame + ", Received: " + frameNumber);
                    }

                    outputStream.writeObject(expectedFrame - 1); // Acknowledge last correct frame
                }

                System.out.println("All frames received. Data written to " + OUTPUT_FILE);
            }
        }
    }
}