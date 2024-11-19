import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class exampleServer {
    public static void main(String[] args) {
        int port = 1234; // Server port for listening to client commands

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Keyboard Control Server started. Listening on port " + port);

            while (true) {
                try (Socket clientSocket = serverSocket.accept();
                     BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

                    System.out.println("Client connected.");
                    Robot robot = new Robot();  // Robot instance to control keyboard

                    String command;
                    while ((command = in.readLine()) != null) {
                        System.out.println("Received command: " + command);

                        // Parse the command received from the client
                        String[] parts = command.split(" ");
                        String action = parts[0];

                        switch (action) {
                            case "TYPE":
                                if (parts.length > 1) {
                                    String text = command.substring(5);  // Get the text to type after "TYPE "
                                    for (char ch : text.toCharArray()) {
                                        int keyCode = KeyEvent.getExtendedKeyCodeForChar(ch);
                                        if (KeyEvent.CHAR_UNDEFINED != keyCode) {
                                            robot.keyPress(keyCode);
                                            robot.keyRelease(keyCode);
                                        }
                                    }
                                }
                                break;

                            case "KEY_PRESS":
                                if (parts.length > 1) {
                                    int keyCode = Integer.parseInt(parts[1]);
                                    robot.keyPress(keyCode);
                                }
                                break;

                            case "KEY_RELEASE":
                                if (parts.length > 1) {
                                    int keyCode = Integer.parseInt(parts[1]);
                                    robot.keyRelease(keyCode);
                                }
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


