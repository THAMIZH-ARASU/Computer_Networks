import java.io.*;
import java.net.*;

public class Server {

    private static final int PORT = 12345; 

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Echo server is running on port " + PORT);

            while (true) {
                try (Socket clientSocket = serverSocket.accept()) {
                    System.out.println("Connected to client: " + clientSocket.getInetAddress());

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

        String clientName = in.readLine();
        System.out.println("Client connected: " + clientName);

        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            if (inputLine.equalsIgnoreCase("exit")) {
                System.out.println(clientName + " has disconnected.");
                out.println("Goodbye " + clientName + "!");
                break;
            }
            System.out.println(clientName + " says: " + inputLine);
            out.println("Echo [" + clientName + "]: " + inputLine);
        }

        System.out.println("Closing connection with " + clientName);
        clientSocket.close();
    }
}
