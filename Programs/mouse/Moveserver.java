import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import javax.swing.*;

class Moveserver extends JFrame {
    private JPanel mousepanel;
    private Point point1, point2; // Store the two clicked points
    private PrintWriter out;

    public Moveserver(PrintWriter out) {
        super("Server");
        this.out = out;
        point1 = null;
        point2 = null;

        mousepanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.RED);

                // Draw a line if both points are set
                if (point1 != null && point2 != null) {
                    g.drawLine(point1.x, point1.y, point2.x, point2.y);
                }
            }
        };

        mousepanel.setBackground(Color.BLACK);
        mousepanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (point1 == null) {
                    point1 = e.getPoint(); // Set the first point
                } else if (point2 == null) {
                    point2 = e.getPoint(); // Set the second point

                    // Send the two points to the client
                    out.println(point1.x);
                    out.println(point1.y);
                    out.println(point2.x);
                    out.println(point2.y);

                    // Repaint to show the line on the server
                    mousepanel.repaint();
                } else {
                    // Reset points if both are already set
                    point1 = null;
                    point2 = null;
                }
            }
        });

        setLayout(new GridLayout(1, 1));
        setSize(300, 300);
        add(mousepanel);
    }

    public static void main(String[] args) {
        try {
            ServerSocket ss = new ServerSocket(8500);
            System.out.println("Server Listening.........");
            Socket s = ss.accept();
            System.out.println("Connection Established.....");

            PrintWriter out = new PrintWriter(s.getOutputStream(), true);
            Moveserver server = new Moveserver(out);

            server.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            server.setVisible(true);
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        }
    }
}
