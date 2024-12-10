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
            s = new Socket("127.0.0.1", 3333);
            System.out.println("Connection Established");
            out = new PrintWriter(s.getOutputStream(), true);

            keyclient k = new keyclient();
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        }
    }
    public keyclient() {
        super("Key Press Event Frame");
        Panel inputPanel = new Panel();
        txtField = new TextField(20);
        txtField.addKeyListener(new MyKeyListener());
        inputPanel.add(new Label("Enter Character: "));
        inputPanel.add(txtField);
        Panel displayPanel = new Panel();
        label = new Label("Output will be displayed here");
        displayPanel.add(label);
        add(inputPanel, BorderLayout.NORTH);
        add(displayPanel, BorderLayout.CENTER);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                System.exit(0);
            }
        });
        setSize(400, 200);
        setVisible(true);




    }
    public class MyKeyListener extends KeyAdapter {
        public void keyPressed(KeyEvent ke) {
            char i = ke.getKeyChar();
            int asciiValue = (int) i;
            char toggledChar;
            if (Character.isUpperCase(i)) {
                toggledChar = Character.toLowerCase(i);
            } else if (Character.isLowerCase(i)) {
                toggledChar = Character.toUpperCase(i);
            } else {
                toggledChar = i; 
            }
            String output = "Char: " + i + ", ASCII: " + asciiValue + ", Toggled: " + toggledChar;
            out.println(output);
            label.setText(output);
        }
    }
}
