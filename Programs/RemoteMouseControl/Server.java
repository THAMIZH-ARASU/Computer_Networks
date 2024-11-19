import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class Server {
    private static final int PORT = 1234;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Server - Mouse Control");
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(600, 400));
        frame.add(panel);

        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is listening on port " + PORT);

            // Accept the client connection
            Socket socket = serverSocket.accept();
            System.out.println("Client connected!");

            try (DataInputStream input = new DataInputStream(socket.getInputStream())) {
                Robot robot = new Robot();

                while (true) {
                    try {
                        // Receive mouse x, y position and click information
                        int x = input.readInt();
                        int y = input.readInt();
                        boolean clicked = input.readBoolean();

                        // Move the mouse to the received position
                        robot.mouseMove(x, y);

                        // Simulate mouse click if required
                        if (clicked) {
                            robot.mousePress(MouseEvent.BUTTON1);  // Use MouseEvent.BUTTON1 instead of BUTTON1_MASK
                            robot.mouseRelease(MouseEvent.BUTTON1);  // Same here
                        }
                    } catch (EOFException e) {
                        System.out.println("Client disconnected.");
                        break; // Exit the loop if the client disconnects
                    } catch (IOException e) {
                        System.out.println("Error while reading data: " + e.getMessage());
                        break; // Exit the loop if an I/O error occurs
                    }
                }
            } catch (IOException | AWTException e) {
                System.out.println("Error during server operation: " + e.getMessage());
            }
        } catch (IOException e) {
            System.out.println("Server error: " + e.getMessage());
        }
    }
}
