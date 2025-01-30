import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class RemoteMouseClient extends JFrame {
    private JPanel mousePanel;
    private List<Point> points;
    private PrintWriter out;

    public RemoteMouseClient(PrintWriter out) {
        super("Remote Mouse Client");
        this.out = out;
        points = new ArrayList<>();

        mousePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.GREEN);

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

        mousePanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Point startPoint = e.getPoint();
                points.add(startPoint);
                out.println(startPoint.x + "," + startPoint.y);
                mousePanel.repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                points.add(null); // Add null to indicate line break
                out.println("STOP");
            }
        });

        mousePanel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                Point currentPoint = e.getPoint();
                points.add(currentPoint);
                out.println(currentPoint.x + "," + currentPoint.y);
                mousePanel.repaint();
            }
        });

        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 8500);
            System.out.println("Connection Established");

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            new RemoteMouseClient(out);
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        }
    }
}
