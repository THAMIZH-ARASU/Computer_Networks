import java.awt.*;
import java.io.*;
import java.net.*;
import javax.swing.*;

public class Moveclient extends JFrame {
    private JPanel mousepanel;
    private Point point1, point2; // Store the two points for the line

    public Moveclient(BufferedReader in) {
        super("Client");
        point1 = null;
        point2 = null;

        mousepanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.GREEN);

                // Draw the line if both points are received
                if (point1 != null && point2 != null) {
                    g.drawLine(point1.x, point1.y, point2.x, point2.y);
                }
            }
        };

        mousepanel.setBackground(Color.BLACK);
        setLayout(new GridLayout(1, 1));
        add(mousepanel);

        // Thread to listen for server messages (point coordinates)
        new Thread(() -> {
            try {
                while (true) {
                    int x1 = Integer.parseInt(in.readLine());
                    int y1 = Integer.parseInt(in.readLine());
                    int x2 = Integer.parseInt(in.readLine());
                    int y2 = Integer.parseInt(in.readLine());

                    // Update points and repaint the panel
                    point1 = new Point(x1, y1);
                    point2 = new Point(x2, y2);
                    mousepanel.repaint();
                }
            } catch (Exception e) {
                System.out.println("Exception: " + e);
            }
        }).start();
    }

    public static void main(String[] args) {
        try {
            Socket s = new Socket("101.1.10.178", 8500); // Connect to the server
            System.out.println("Connection Established");

            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            Moveclient client = new Moveclient(in);

            client.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            client.setSize(300, 300);
            client.setVisible(true);
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        }
    }
}
