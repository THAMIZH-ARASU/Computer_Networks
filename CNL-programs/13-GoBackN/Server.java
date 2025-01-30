import java.io.*;
import java.net.*;

public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(1234);
        Socket socket = serverSocket.accept();
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        DataInputStream in = new DataInputStream(socket.getInputStream());

        int windowSize = 4;
        int frame = 0;
        int lastAckReceived = -1;

        socket.setSoTimeout(10000); // Set timeout to 10 seconds

        while (frame < 10) {
            for (int i = 0; i < windowSize && frame < 10; i++) {
                System.out.println("Sending frame: " + frame);
                out.writeInt(frame); // Send each frame in the window
                out.flush();
                frame++;
            }

            try {
                int ack = in.readInt(); // Wait for cumulative acknowledgment
                lastAckReceived = ack;
                System.out.println("Acknowledgment received for frame: " + ack);

                if (ack >= frame - 1) {
                    continue; // Slide the window if acknowledgment received
                }
                frame = lastAckReceived + 1; // Reset the frame position
            } catch (SocketTimeoutException e) {
                System.out.println("Timeout! Resending from frame: " + (lastAckReceived + 1));
                frame = lastAckReceived + 1; // Restart from last acknowledged frame
            }
        }
        socket.close();
        serverSocket.close();
    }
}