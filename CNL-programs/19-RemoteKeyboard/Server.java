import java.io.*;
import java.net.*;

public class Server {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private BufferedReader in;

    public Server() {
        try {
            serverSocket = new ServerSocket(1234);
            System.out.println("Server is waiting for a connection...");
            clientSocket = serverSocket.accept();
            System.out.println("Connected to client");

            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                if (inputLine.startsWith("Length:")) {
                    System.out.println(inputLine);
                } else {
                    System.out.println("Received character: " + inputLine.toUpperCase());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Server();
    }
}
