import java.io.*;
import java.net.*;

public class Server{
    private static final int PORT = 5000;
    public static void main(String[] args) {
        try(ServerSocket serverSocket = new ServerSocket(PORT)){
            System.out.println("Server listening on port: " + PORT);
            Socket socket = serverSocket.accept();
            System.out.println("New client connected");

            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader cin = new BufferedReader(new InputStreamReader(System.in));

            String clientMessage, serverMessage;
            while(true){
                clientMessage = input.readLine();
                System.out.println("Client: " + clientMessage);
                if (clientMessage.equalsIgnoreCase("exit")){
                    System.out.println("Client disconnected");
                    break;
                }

                System.out.print("You: ");
                serverMessage = cin.readLine();
                output.println(serverMessage);
                if (serverMessage.equalsIgnoreCase("exit")){
                    System.out.println("Server disconnected");
                    break;
                }
            }

            socket.close();
            serverSocket.close();
            input.close();
            output.close();
            cin.close();
            System.out.println("Connection is Closed");
    }catch(Exception e){
        System.out.println("Server exception: " + e.getMessage());
    }
    }
}