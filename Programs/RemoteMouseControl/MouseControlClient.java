import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class MouseControlClient {
    public static void main(String[] args) {
        String serverAddress = "101.1.15.16"; 
        int port = 1234; 

        try (Socket socket = new Socket(serverAddress, port);
             PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("Connected to the Mouse Control Server.");

            while (true) {
                System.out.println("\nEnter a command (e.g., MOVE x y, CLICK, RIGHT_CLICK, SCROLL amount):");
                String command = scanner.nextLine();

               
                out.println(command);
            }
        } catch (Exception e) {
            System.out.println("Client error: " + e.getMessage());
        }
    }
}
