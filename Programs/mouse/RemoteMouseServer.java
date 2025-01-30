import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class RemoteMouseServer extends JFrame {
    private JPanel mousePanel;
    private List<Point> points;
    private PrintWriter out;

    public RemoteMouseServer(PrintWriter out) {
        super("Remote Mouse Server");
        this.out = out;
        points = new ArrayList<>();

        mousePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.RED);

                if (points.size() > 1) {
                    for (int i = 0; i < points.size() - 1; i++) {
                        Point p1 = points.get(i);
                        Point p2 = points.get(i + 1);
                        if (p1 != null && p2 != null) {
                            g.drawLine(p1.x, p1.y, p2.x, p2.y);
                        }
                    }
                }
            }
        };

        mousePanel.setBackground(Color.BLACK);
        setLayout(new BorderLayout());
        add(mousePanel, BorderLayout.CENTER);

        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(e -> {
            points.clear();
            out.println("CLEAR");
            mousePanel.repaint();
        });
        add(clearButton, BorderLayout.SOUTH);

        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void addPoint(Point p) {
        points.add(p);
        mousePanel.repaint();
    }

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(8500);
            System.out.println("Server Listening...");
            Socket socket = serverSocket.accept();
            System.out.println("Connection Established...");

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            RemoteMouseServer server = new RemoteMouseServer(out);
            server.setVisible(true);

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                if (line.equals("CLEAR")) {
                    server.points.clear();
                    server.mousePanel.repaint();
                } else if (line.equals("STOP")) {
                    server.addPoint(null); // Add null to break the line
                } else {
                    String[] coords = line.split(",");
                    int x = Integer.parseInt(coords[0]);
                    int y = Integer.parseInt(coords[1]);
                    server.addPoint(new Point(x, y));
                }
            }
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        }
    }
}
