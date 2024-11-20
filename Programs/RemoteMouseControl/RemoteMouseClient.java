import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class RemoteMouseClient {

    public static void main(String[] args) {
        try {
            // Connect to the server (localhost:12345)
            Socket socket = new Socket("localhost", 12345);
            System.out.println("Connected to the server!");

            // Output stream to send mouse position and click events to the server
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

            // Robot to get local mouse position
            Robot robot = new Robot();

            // Variables to store the current mouse position
            int lastX = -1, lastY = -1;

            // Loop to constantly send the mouse position to the server
            while (true) {
                // Get the local mouse position
                Point mousePos = MouseInfo.getPointerInfo().getLocation();
                int x = (int) mousePos.getX();
                int y = (int) mousePos.getY();

                // Detect if the mouse moved
                if (x != lastX || y != lastY) {
                    // Send mouse position to the server
                    dos.writeInt(x);
                    dos.writeInt(y);
                    lastX = x;
                    lastY = y;
                }

                // Check for left or right mouse clicks
                int clickEvent = 0; // 0: no click, 1: left click, 2: right click
                if (MouseInfo.getPointerInfo().getDevice().getButtons() == 1) {
)                    if (MouseInfo.getPointerInfo().getDevice().getButtonCount() == 1) {
                        clickEvent = 1; // Left-click
                    }
                }
                if (MouseInfo.getPointerInfo().getDevice().getButtonCount() == 2) {
                    clickEvent = 2; // Right-click
                }

                // Send the click event to the server
                dos.writeInt(clickEvent);
                dos.flush();

                // Sleep for a short period to avoid flooding the server with data
                Thread.sleep(10);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
