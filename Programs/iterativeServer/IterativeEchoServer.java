import java.io.*;
import java.net.*;

public class IterativeEchoServer {

    private static final int PORT = 12345;  // Port number for the server

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Echo server is running on port " + PORT);

            while (true) {
                // Accept one client connection at a time
                try (Socket clientSocket = serverSocket.accept()) {
                    System.out.println("Connected to client: " + clientSocket.getInetAddress());

                    // Handle the client communication
                    handleClient(clientSocket);
                } catch (IOException e) {
                    System.err.println("Client handling error: " + e.getMessage());
                }
            }

        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        }
    }

    private static void handleClient(Socket clientSocket) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

        // Read client name
        String clientName = in.readLine();
        System.out.println("Client connected: " + clientName);

        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            if (inputLine.equalsIgnoreCase("exit")) {
                System.out.println(clientName + " has disconnected.");
                out.println("Goodbye " + clientName + "!");
                break;  // Exit the loop and terminate the connection
            }
            System.out.println(clientName + " says: " + inputLine);
            out.println("Echo [" + clientName + "]: " + inputLine);  // Echo the input back to the client with their name
        }

        System.out.println("Closing connection with " + clientName);
        clientSocket.close();
    }
}
