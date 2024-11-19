import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class keyclient extends Frame {
    Label label;
    static Socket s;
    static PrintWriter out;
    TextField txtField;

    public static void main(String[] args) {
        try {
            s = new Socket("101.1.10.178", 3333); // Connect to server
            System.out.println("Connection Established");
            out = new PrintWriter(s.getOutputStream(), true); // Output stream to server
            keyclient k = new keyclient();
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        }
    }

    public keyclient() {
        super("Key Press Event Frame");
        Panel panel = new Panel();
        label = new Label();
        txtField = new TextField(20);

        // Add KeyListener to handle key events
        txtField.addKeyListener(new MyKeyListener());

        add(label, BorderLayout.NORTH);
        panel.add(txtField, BorderLayout.CENTER);
        add(panel, BorderLayout.CENTER);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                System.exit(0);
            }
        });

        setSize(200, 200);
        setVisible(true);
    }

    // KeyListener for handling key events
    class MyKeyListener extends KeyAdapter {
        public void keyPressed(KeyEvent e) {
            try {
                char keyChar = e.getKeyChar();
                int asciiValue = (int) keyChar; // Get ASCII value of the key
                String transformedChar;

                // Convert uppercase to lowercase and vice versa
                if (Character.isUpperCase(keyChar)) {
                    transformedChar = String.valueOf(Character.toLowerCase(keyChar));
                } else if (Character.isLowerCase(keyChar)) {
                    transformedChar = String.valueOf(Character.toUpperCase(keyChar));
                } else {
                    transformedChar = String.valueOf(keyChar); // Non-alphabetic characters remain the same
                }

                // Send the transformed character and ASCII value to the server
                out.println("Key: " + transformedChar + ", ASCII: " + asciiValue);

            } catch (Exception ex) {
                System.out.println("Exception: " + ex);
            }
        }
    }
}
