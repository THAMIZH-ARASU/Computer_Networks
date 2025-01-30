import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(1234);
        Socket socket = serverSocket.accept();
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        DataInputStream in = new DataInputStream(socket.getInputStream());

        int windowSize = 4; // The size of the sliding window
        int frame = 0;
        Map<Integer, Boolean> sentFrames = new HashMap<>();

        socket.setSoTimeout(10000); // Set timeout to 10 seconds

        while (frame < 10) { // For example, sending 10 frames
            // Send frames within the window size limit
            for (int i = 0; i < windowSize && frame < 10; i++) {
                System.out.println("Sending frame: " + frame);
                out.writeInt(frame); // Send frame to the client
                out.flush();
                sentFrames.put(frame, false); // Mark frame as sent but not acknowledged
                frame++;
            }

            try {
                // Wait for acknowledgment for individual frames
                int ack = in.readInt();
                sentFrames.put(ack, true); // Mark frame as acknowledged
                System.out.println("Acknowledgment received for frame: " + ack);

                // Slide the window by removing acknowledged frames
                sentFrames.entrySet().removeIf(entry -> entry.getValue());
            } catch (SocketTimeoutException e) {
                System.out.println("Timeout! Resending unacknowledged frames.");
                // Resend only the unacknowledged frames
                for (Map.Entry<Integer, Boolean> entry : sentFrames.entrySet()) {
                    if (!entry.getValue()) { // Check if acknowledgment was not received
                        System.out.println("Resending frame: " + entry.getKey());
                        out.writeInt(entry.getKey());
                        out.flush();
                    }
                }
            }
        }
        socket.close();
        serverSocket.close();
    }
}