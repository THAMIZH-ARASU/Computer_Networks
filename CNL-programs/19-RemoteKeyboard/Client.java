import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class Client {
    private Socket socket;
    private PrintWriter out;
    private Frame frame;
    private TextField textField;

    public Client() {
        frame = new Frame("Remote Keyboard Control");
        textField = new TextField();
        textField.setBounds(50, 50, 200, 30);
        frame.add(textField);
        
        frame.setSize(300, 150);
        frame.setLayout(null);
        frame.setVisible(true);

        textField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                char keyChar = e.getKeyChar();
                sendCharacter(keyChar);
                if (keyChar == ' ') {
                    sendLength();
                }
            }
        });

        try {
            socket = new Socket("localhost", 1234);  // Connect to server on localhost
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendCharacter(char keyChar) {
        out.println(keyChar);
    }

    public void sendLength() {
        String text = textField.getText();
        out.println("Length: " + text.length());
    }

    public static void main(String[] args) {
        new Client();
    }
}
