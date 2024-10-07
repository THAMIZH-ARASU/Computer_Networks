import java.io.*;
import java.net.*;

public class EchoClient {

    private static final String SERVER_ADDRESS = "localhost"; 
    private static final int SERVER_PORT = 12345;  

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))) {

            System.out.println("Enter your name: ");
            String clientName = stdIn.readLine();
            out.println(clientName); 

            System.out.println("Connected to the echo server as " + clientName);
            String userInput;

            while ((userInput = stdIn.readLine()) != null) {
                out.println(userInput); 

                if (userInput.equalsIgnoreCase("exit")) {
                    System.out.println(in.readLine()); 
                    break; 
                }

                System.out.println("Echo from server: " + in.readLine()); 
            }

        } catch (IOException e) {
            System.err.println("Client exception: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
