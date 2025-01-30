import java.io.*;
import java.net.*;

public class Server {
    private static final int PORT = 12345;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started on port " + PORT);
            
            while (true) {
                try (Socket clientSocket = serverSocket.accept();
                     PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                     BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
                    
                    System.out.println("Client connected from " + clientSocket.getInetAddress());
                    
                    while (true) {
                        String message = in.readLine();
                        if (message == null) break;
                        
                        // Simulate network conditions (20% packet loss)
                        if (Math.random() < 0.2) {
                            System.out.println("Simulating packet loss - no acknowledgment sent");
                            continue;
                        }
                        
                        System.out.println("Received: " + message);
                        
                        // Send acknowledgment
                        out.println("ACK");
                        System.out.println("Sent acknowledgment");
                    }
                } catch (IOException e) {
                    System.out.println("Client disconnected");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}