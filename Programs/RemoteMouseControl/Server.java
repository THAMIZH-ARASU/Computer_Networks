import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class Server {
    public static void main(String[] args) {
        try {
            // Server setup
            ServerSocket serverSocket = new ServerSocket(12345);
            System.out.println("Server started, waiting for client...");
            Socket socket = serverSocket.accept();
            System.out.println("Client connected.");

            // Create an input stream to receive data from the client
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            Robot robot = new Robot();

            while (true) {
                // Read the command (mouse action) from the client
                String command = dataInputStream.readUTF();

                if (command.equals("MOVE")) {
                    // Receive x and y coordinates for mouse movement
                    int x = dataInputStream.readInt();
                    int y = dataInputStream.readInt();
                    robot.mouseMove(x, y);
                } else if (command.equals("CLICK")) {
                    // Perform a mouse click
                    robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                    robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                } else if (command.equals("SCROLL")) {
                    // Perform a scroll action
                    int scrollAmount = dataInputStream.readInt();
                    robot.mouseWheel(scrollAmount);
                } else if (command.equals("EXIT")) {
                    // Exit the server
                    System.out.println("Exiting...");
                    break;
                }
            }

            dataInputStream.close();
            socket.close();
            serverSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
