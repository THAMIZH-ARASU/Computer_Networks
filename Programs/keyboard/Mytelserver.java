import java.io.*;
import java.net.*;

public class Mytelserver {
    public static void main(String arg[]) {
        String is;
        boolean done = false;
        
        try {
            ServerSocket serv = new ServerSocket(3333);
            Socket sock = serv.accept();
            System.out.println("Server started");
            
            BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            PrintWriter out = new PrintWriter(sock.getOutputStream(), true);
            System.out.println("KEYBOARD INTERFACE");
            
            while (!done) {
                is = in.readLine(); // Read input from client
                if (is == null) {
                    done = true; // If client disconnects, end loop
                } else {
                    System.out.println(is); // Print the received message
                }
            }
            serv.close(); // Close the server socket
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        }
    }
}
