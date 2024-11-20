import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import javax.swing.*;
import java.util.List;  // Import the List interface from java.util
import java.util.ArrayList;  // Import ArrayList from java.util

public class Client extends JFrame {
    private JPanel mousepanel;
    private Point lastPoint; // Track the last point for drawing lines
    private PrintWriter out; // To send data to the server
    private List<Point> points;  // Store points for drawing on the client side

    public Client(PrintWriter out) {
        super("Client");
        this.out = out;
        lastPoint = null;
        points = new ArrayList<>();  // Initialize the points list

        mousepanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.GREEN);
                
                // Draw points locally (on the client side)
                if (points.size() > 1) {
                    for (int i = 0; i < points.size() - 1; i++) {
                        Point p1 = points.get(i);
                        Point p2 = points.get(i + 1);
                        g.drawLine(p1.x, p1.y, p2.x, p2.y); // Draw a line between consecutive points
                    }
                }
            }
        };

        mousepanel.setBackground(Color.BLACK);
        setLayout(new BorderLayout());
        add(mousepanel, BorderLayout.CENTER);

        // Clear button
        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(e -> {
            points.clear();  // Clear the local points list
            out.println("CLEAR");  // Send the "CLEAR" command to the server
            mousepanel.repaint();  // Repaint the client canvas
        });
        add(clearButton, BorderLayout.SOUTH);

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
            // Add the point to the local list and send it to the server
            points.add(p);
            out.println(p.x + "," + p.y);
            mousepanel.repaint();
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
