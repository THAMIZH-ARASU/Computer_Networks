import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class Client {
    private static final String SERVER_ADDRESS = "127.0.0.1";  // Change if connecting to a remote server
    private static final int PORT = 1234;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Client - Mouse Control");
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(600, 400));
        frame.add(panel);

        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        try (Socket socket = new Socket(SERVER_ADDRESS, PORT);
             DataOutputStream output = new DataOutputStream(socket.getOutputStream())) {

            System.out.println("Connected to server at " + SERVER_ADDRESS + ":" + PORT);

            // Listener for mouse movement
            panel.addMouseMotionListener(new MouseMotionListener() {
                @Override
                public void mouseMoved(MouseEvent e) {
                    try {
                        // Send mouse coordinates to the server
                        System.out.println("Sending mouse move event to server: " + e.getX() + ", " + e.getY());
                        output.writeInt(e.getX());
                        output.writeInt(e.getY());
                        output.writeBoolean(false); // Not clicking
                    } catch (IOException ex) {
                        System.out.println("Error sending data: " + ex.getMessage());
                        ex.printStackTrace();  // Print stack trace for debugging
                    }
                }

                @Override
                public void mouseDragged(MouseEvent e) {
                    mouseMoved(e); // Handle drag the same as move
                }
            });

            // Listener for mouse click
            panel.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    try {
                        // Send mouse coordinates and click information to the server
                        System.out.println("Sending mouse press event to server: " + e.getX() + ", " + e.getY());
                        output.writeInt(e.getX());
                        output.writeInt(e.getY());
                        output.writeBoolean(true); // Mouse clicked
                    } catch (IOException ex) {
                        System.out.println("Error sending data: " + ex.getMessage());
                        ex.printStackTrace();  // Print stack trace for debugging
                    }
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    try {
                        // Send mouse coordinates and release information to the server
                        System.out.println("Sending mouse release event to server: " + e.getX() + ", " + e.getY());
                        output.writeInt(e.getX());
                        output.writeInt(e.getY());
                        output.writeBoolean(false); // Mouse released
                    } catch (IOException ex) {
                        System.out.println("Error sending data: " + ex.getMessage());
                        ex.printStackTrace();  // Print stack trace for debugging
                    }
                }
            });

        } catch (IOException e) {
            System.out.println("Error connecting to the server: " + e.getMessage());
            e.printStackTrace();  // Print stack trace for debugging
        }
    }
}
