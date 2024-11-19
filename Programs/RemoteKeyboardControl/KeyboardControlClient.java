import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class KeyboardControlClient {
    public static void main(String[] args) {
        String serverAddress = "    101.1.10.178"; // Server address (change as needed)
        int port = 1234; // Server port

        try (Socket socket = new Socket(serverAddress, port);
             PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("Connected to the Keyboard Control Server.");

            while (true) {
                System.out.println("\nEnter a command (e.g., TYPE text, KEY_PRESS keycode, KEY_RELEASE keycode):");
                String command = scanner.nextLine();

                // Send command to the server
                out.println(command);
            }
        } catch (Exception e) {
            System.out.println("Client error: " + e.getMessage());
        }
    }
}
