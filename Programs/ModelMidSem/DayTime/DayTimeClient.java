import java.io.*;
import java.net.*;

public class DayTimeClient {
    public static void main(String[] args) {
        try {
            // Connect to the server on port 4078
            Socket socket = new Socket("localhost", 4078);

            // Create input/output streams for communication
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Send a request to the server
            out.println("Request for current date and time");

            // Read the server's response (current date and time)
            String serverResponse = in.readLine();
            System.out.println("Server Response: " + serverResponse);

            // Close the socket after receiving the response
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
