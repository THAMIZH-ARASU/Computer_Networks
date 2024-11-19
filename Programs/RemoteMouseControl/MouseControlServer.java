import java.awt.Robot;
import java.awt.event.InputEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class MouseControlServer {
    public static void main(String[] args) {
        int port = 1234; 

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Mouse Control Server started. Listening on port " + port);

            while (true) {
                try (Socket clientSocket = serverSocket.accept();
                     BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

                    System.out.println("Client connected.");
                    Robot robot = new Robot();  
                    String command;
                    while ((command = in.readLine()) != null) {
                        System.out.println("Received command: " + command);

                        
                        String[] parts = command.split(" ");
                        String action = parts[0];

                        switch (action) {
                            case "MOVE":
                                int x = Integer.parseInt(parts[1]);
                                int y = Integer.parseInt(parts[2]);
                                robot.mouseMove(x, y);
                                break;

                            case "CLICK":
                                robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                                robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                                break;

                            case "RIGHT_CLICK":
                                robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
                                robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
                                break;

                            case "SCROLL":
                                int scrollAmount = Integer.parseInt(parts[1]);
                                robot.mouseWheel(scrollAmount);
                                break;

                            default:
                                System.out.println("Unknown command: " + action);
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Error handling client: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.out.println("Server error: " + e.getMessage());
        }
    }
}
