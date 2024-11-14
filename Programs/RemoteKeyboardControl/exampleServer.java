import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class exampleServer {
    public static void main(String[] args) {
        int port = 1234; // Port on which the server listens

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Keyboard Control Server started. Listening on port " + port);

            while (true) {
                try (Socket clientSocket = serverSocket.accept();
                     BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

                    System.out.println("Client connected.");
                    Robot robot = new Robot();  // Robot instance to perform keyboard actions

                    String command;
                    while ((command = in.readLine()) != null) {
                        System.out.println("Received command: " + command);

                        // Split the command into action and parameters
                        String[] parts = command.split(" ", 2);
                        String action = parts[0];
                        String parameters = parts.length > 1 ? parts[1] : "";

                        switch (action) {
                            case "TYPE":
                                typeText(robot, parameters);
                                break;

                            case "KEY_PRESS":
                                if (!parameters.isEmpty()) {
                                    int keyCode = Integer.parseInt(parameters);
                                    robot.keyPress(keyCode);
                                }
                                break;

                            case "KEY_RELEASE":
                                if (!parameters.isEmpty()) {
                                    int keyCode = Integer.parseInt(parameters);
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

    // Helper method to type a string character by character
    private static void typeText(Robot robot, String text) {
        for (char ch : text.toCharArray()) {
            int keyCode = KeyEvent.getExtendedKeyCodeForChar(ch);
            if (keyCode != KeyEvent.CHAR_UNDEFINED) {
                robot.keyPress(keyCode);
                robot.keyRelease(keyCode);
            }
        }
    }
}
