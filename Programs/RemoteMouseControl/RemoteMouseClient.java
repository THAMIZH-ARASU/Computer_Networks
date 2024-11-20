import java.awt.*;
import java.io.*;
import java.net.*;

public class RemoteMouseClient {

    public static void main(String[] args) {
        try {
            try (// Connect to the server (localhost:12345)
            Socket socket = new Socket("101.1.15.16", 12345)) {
                System.out.println("Connected to the server!");

                // Output stream to send mouse position and click events to the server
                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

                // Robot to get local mouse position and simulate clicks
                Robot robot = new Robot();

                // Variables to store the current mouse position and click event
                int lastX = -1, lastY = -1;
                int clickEvent = 0; // 0: no click, 1: left click, 2: right click

                while (true) {
                    // Get the local mouse position using MouseInfo
                    Point mousePos = MouseInfo.getPointerInfo().getLocation();
                    int x = (int) mousePos.getX();
                    int y = (int) mousePos.getY();

                    // Detect if the mouse moved
                    if (x != lastX || y != lastY) {
                        // Send mouse position and click event to the server
                        dos.writeInt(x);
                        dos.writeInt(y);
                        dos.writeInt(clickEvent);
                        dos.flush();

                        lastX = x;
                        lastY = y;
                    }

                    // Simulate detecting left mouse button press (BUTTON1 is left-click)
                    if (isLeftButtonPressed(robot)) {
                        clickEvent = 1; // Left-click
                    }
                    // Simulate detecting right mouse button press (BUTTON3 is right-click)
                    else if (isRightButtonPressed(robot)) {
                        clickEvent = 2; // Right-click
                    } else {
                        clickEvent = 0; // No click
                    }

                    // Sleep to avoid overloading the server with too many messages
                    Thread.sleep(10);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Simulate detecting the left mouse button press (button 1)
    private static boolean isLeftButtonPressed(Robot robot) {
        // We cannot directly detect button presses in a non-GUI context without a listener.
        // Placeholder method, simulating left button detection based on external criteria (keyboard or other sensors).
        return false; // Return actual condition if applicable
    }

    // Simulate detecting the right mouse button press (button 3)
    private static boolean isRightButtonPressed(Robot robot) {
        // Placeholder method for right-click detection logic.
        return false; // Return actual condition if applicable
    }
}
