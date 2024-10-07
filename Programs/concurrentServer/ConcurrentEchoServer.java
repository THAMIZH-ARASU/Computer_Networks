import java.io.*;
import java.net.*;

public class ConcurrentEchoServer {

    private static final int PORT = 12345;  

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Echo server is running on port " + PORT);

            // Continuously listen for client connections
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Connected to client: " + clientSocket.getInetAddress());

                // Create a new thread to handle the client connection
                new Thread(new EchoClientHandler(clientSocket)).start();
            }

        } catch (IOException e) {
            System.err.println("Server exception: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

class EchoClientHandler implements Runnable {
    private Socket clientSocket;
    private String clientName;

    public EchoClientHandler(Socket socket) {
        this.clientSocket = socket;
    }

    @Override
    public void run() {
        try (
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        ) {
            clientName = in.readLine();
            System.out.println("Client connected: " + clientName);

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                if (inputLine.equalsIgnoreCase("exit")) {
                    System.out.println(clientName + " has disconnected.");
                    out.println("Goodbye " + clientName + "!");
                    break; 
                }
                System.out.println(clientName + " says: " + inputLine);
                out.println("Echo [" + clientName + "]: " + inputLine);  // Echo the input back to the client with their name
            }

        } catch (IOException e) {
            System.err.println("Client handler exception: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
                System.out.println("Client socket closed: " + clientName);
            } catch (IOException e) {
                System.err.println("Failed to close client socket: " + e.getMessage());
            }
        }
    }
}
