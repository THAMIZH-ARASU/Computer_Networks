import java.io.*;
import java.net.*;

public class keyserver {
    public static void main(String arg[]) {
        String receivedData;
        boolean done = false;
        try {
            ServerSocket serv = new ServerSocket(3333);
            System.out.println("Server started, waiting for client...");
            Socket sock = serv.accept();
            System.out.println("Client connected.");
            BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            while (!done) {
                receivedData = in.readLine();
                if (receivedData != null) {
                    System.out.println(receivedData);
                } else {
                    done = true;
                }
            }

		  serv.close();
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        }
    }
}
