import java.io.*;
import java.net.*;

public class LineCountClient {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java LineCountClient <filename>");
            return;
        }

        String fileName = args[0];

        try (Socket socket = new Socket("localhost", 4080)) {
            // Create input/output streams for communication
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Send the file name to the server
            out.println(fileName);

            // Receive and print the server's response
            String response = in.readLine();
            System.out.println(response);

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
