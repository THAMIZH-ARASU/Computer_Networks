import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) {
        try {
            // Client setup
            Socket socket = new Socket("101.1.10.178", 12345);  // Connect to the server
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());

            // Get the mouse position and send it to the server
            Robot robot = new Robot();
            PointerInfo pointerInfo = MouseInfo.getPointerInfo();
            Point mouseLocation = pointerInfo.getLocation();
            int currentX = (int) mouseLocation.getX();
            int currentY = (int) mouseLocation.getY();

            // Send mouse movement command
            dataOutputStream.writeUTF("MOVE");
            dataOutputStream.writeInt(currentX + 100);  // Move the mouse by 100 pixels
            dataOutputStream.writeInt(currentY);
            dataOutputStream.flush();

            // Send a click command
            dataOutputStream.writeUTF("CLICK");
            dataOutputStream.flush();

            // Send a scroll command
            dataOutputStream.writeUTF("SCROLL");
            dataOutputStream.writeInt(3); // Scroll down
            dataOutputStream.flush();

            // Close the connection
            dataOutputStream.writeUTF("EXIT");
            dataOutputStream.flush();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
