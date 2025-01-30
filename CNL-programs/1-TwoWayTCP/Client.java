
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;


public class Client{
    private static final String SERVER_IP = "localhost";
    private static final int SERVER_PORT = 5000;
    public static void main(String[] args) {
        try {
            Socket socket = new Socket(SERVER_IP, SERVER_PORT);
            System.out.println("Connected to server");

            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader cin = new BufferedReader(new InputStreamReader(System.in));

            String clientMessage, serverMessage;
            while(true){
                System.out.print("You: ");
                clientMessage = cin.readLine();
                output.println(clientMessage);
                if (clientMessage.equalsIgnoreCase("exit")){
                    System.out.println("Client disconnected");
                    break;
                }

                serverMessage = input.readLine();
                System.out.println("Server: " + serverMessage);
                if (serverMessage.equalsIgnoreCase("exit")){
                    System.out.println("Server disconnected");
                    break;
                }
            }

            socket.close();
            input.close();
            output.close();
            cin.close();
            System.out.println("Client disconnected");
            
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}