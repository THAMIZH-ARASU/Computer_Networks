import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class CommandClient {

    public static void main(String[] args) {
        String serverAddress = "localhost"; // Server address (change if needed)
        int port = 1234; // Server port

        try (Socket socket = new Socket(serverAddress, port);
             PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("Connected to server. Enter commands to execute:");

            String command;
            while (true) {
                System.out.print("Command: ");
                command = scanner.nextLine();

                // Send command to server
                out.println(command);

                // Read and print the server's response
                String response;
                while (!(response = in.readLine()).equals("END_OF_COMMAND")) {
                    System.out.println(response);
                }
            }
        } catch (Exception e) {
            System.out.println("Client error: " + e.getMessage());
        }
    }
}
