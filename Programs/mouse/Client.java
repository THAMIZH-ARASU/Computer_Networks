import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import javax.swing.*;

public class Client extends JFrame {
    private JPanel mousepanel;
    private Point lastPoint; // Track the last point for drawing lines
    private PrintWriter out; // To send data to the server

    public Client(PrintWriter out) {
        super("Client");
        this.out = out;
        lastPoint = null;

        mousepanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.GREEN);
                
                // Optionally, you can visualize the drawing on the client too
                if (lastPoint != null) {
                    g.fillOval(lastPoint.x - 2, lastPoint.y - 2, 4, 4);
                }
            }
        };

        mousepanel.setBackground(Color.BLACK);
        setLayout(new GridLayout(1, 1));
        add(mousepanel);

        // Handle mouse events to track drawing
        mousepanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                lastPoint = e.getPoint();
                sendPoint(lastPoint);
            }
        });

        mousepanel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                // Send the point continuously while dragging
                Point currentPoint = e.getPoint();
                sendPoint(currentPoint);
            }
        });

        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    // Send point to the server
    private void sendPoint(Point p) {
        if (p != null) {
            // Send the point as a comma-separated string (x,y)
            out.println(p.x + "," + p.y);
        }
    }

    public static void main(String[] args) {
        try {
            Socket s = new Socket("localhost", 8500); // Connect to the server
            System.out.println("Connection Established");

            PrintWriter out = new PrintWriter(s.getOutputStream(), true);
            Client client = new Client(out);
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        }
    }
}

