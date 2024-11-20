import java.awt.*;
import java.io.*;
import java.net.*;

public class RemoteMouseServer {

    public static void main(String[] args) {
        try {
            try (// Server socket on port 12345
            ServerSocket serverSocket = new ServerSocket(12345)) {
                System.out.println("Server is running and waiting for a connection...");

                // Accept the incoming connection
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected!");

                // Set up input stream from client
                DataInputStream dis = new DataInputStream(clientSocket.getInputStream());

                // Robot class to control the server's mouse
                Robot robot = new Robot();

                while (true) {
                    // Receive the mouse position from the client
                    int x = dis.readInt();
                    int y = dis.readInt();
                    System.out.println("Moving mouse to: (" + x + ", " + y + ")");
                    
                    // Move the mouse to the received coordinates
                    robot.mouseMove(x, y);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
