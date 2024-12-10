import java.io.*;
import java.net.*;

public class MulticastClient {
    public static void main(String[] args) {
        try {
            // Connect to the server on the specified port
            Socket socket = new Socket("localhost", Integer.parseInt(args[0]));
            System.out.println("Client connected to server on port " + args[0]);

            // Listen for incoming messages from the server
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String message;
            while ((message = input.readLine()) != null) {
                System.out.println("Received from server: " + message);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
