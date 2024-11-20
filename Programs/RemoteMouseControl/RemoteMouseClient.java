import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

import javax.swing.JFrame;

public class RemoteMouseClient {

    public static void main(String[] args) {
        try {
            try (// Connect to the server (localhost:12345)
            Socket socket = new Socket("localhost", 12345)) {
                System.out.println("Connected to server!");

                // Set up output stream to send data to the server
                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

                // Get the local mouse pointer location using MouseMotionListener
                Robot robot = new Robot();

                // Listen for the mouse move events and send to server
                MouseMotionListener mouseListener = new MouseMotionListener() {
                    @Override
                    public void mouseMoved(MouseEvent e) {
                        try {
                            // Get mouse position
                            int x = e.getX();
                            int y = e.getY();

                            // Send mouse position to the server
                            dos.writeInt(x);
                            dos.writeInt(y);
                            dos.flush();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }

                    @Override
                    public void mouseDragged(MouseEvent e) {
                        // We don't need to track mouse dragging, just moving
                    }
                };

                // Set up a JFrame to track mouse movements
                JFrame frame = new JFrame("Remote Mouse Client");
                frame.setSize(800, 600);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.addMouseMotionListener(mouseListener);
                frame.setVisible(true);
            }
            System.out.println("Tracking mouse movement...");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
