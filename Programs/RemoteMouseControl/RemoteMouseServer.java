import java.awt.*;
import java.awt.event.InputEvent;
import java.io.*;
import java.net.*;

public class RemoteMouseServer {

    public static void main(String[] args) {
        try {
            try (// Server socket on port 12345
            ServerSocket serverSocket = new ServerSocket(12345)) {
                System.out.println("Server is waiting for a connection...");

                // Accept the incoming connection
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected!");

                // Input stream to receive mouse coordinates and click events from the client
                DataInputStream dis = new DataInputStream(clientSocket.getInputStream());

                // Robot class to control the server's mouse
                Robot robot = new Robot();

                while (true) {
                    // Receive the mouse position (x, y) from the client
                    int x = dis.readInt();
                    int y = dis.readInt();

                    // Receive the mouse click event from the client (0 = no click, 1 = left click, 2 = right click)
                    int clickEvent = dis.readInt();

                    // Move the server's mouse to the received coordinates
                    System.out.println("Moving mouse to: (" + x + ", " + y + ")");
                    robot.mouseMove(x, y);

                    // Handle mouse click events
                    if (clickEvent == 1) {
                        // Left-click
                        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                    } else if (clickEvent == 2) {
                        // Right-click
                        robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
                        robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
