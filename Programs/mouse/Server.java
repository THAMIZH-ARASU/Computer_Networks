import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.List;  // Import only the List interface from java.util
import java.util.ArrayList;  // Import ArrayList specifically
import javax.swing.*;

public class Server extends JFrame {
    private JPanel mousepanel;
    private List<Point> points;  // List to store points for drawing
    private PrintWriter out;

    public Server(PrintWriter out) {
        super("Server");
        this.out = out;
        points = new ArrayList<>();  // Initialize the list

        mousepanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.RED);

                // Draw all the points stored by the client
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
            points.clear();  // Clear points list
            out.println("CLEAR");  // Send the "CLEAR" command to the client
            mousepanel.repaint();  // Repaint the server canvas
        });
        add(clearButton, BorderLayout.SOUTH);

        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    // Method to add points received from the client
    public void addPoint(int x, int y) {
        points.add(new Point(x, y));
        mousepanel.repaint();
    }

    public static void main(String[] args) {
        try {
            ServerSocket ss = new ServerSocket(8500);
            System.out.println("Server Listening...");
            Socket s = ss.accept();
            System.out.println("Connection Established...");

            PrintWriter out = new PrintWriter(s.getOutputStream(), true);
            Server server = new Server(out);
            server.setVisible(true);

            // Listening for data from the client
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                if (line.equals("CLEAR")) {
                    server.points.clear();  // Clear the server drawing
                    server.mousepanel.repaint();
                } else {
                    // Parsing received x and y points
                    String[] coords = line.split(",");
                    int x = Integer.parseInt(coords[0]);
                    int y = Integer.parseInt(coords[1]);

                    // Add point to the server's drawing
                    server.addPoint(x, y);
                }
            }
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        }
    }
}
