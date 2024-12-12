import java.io.*;
import java.net.Socket;

public class Client {
    public static void main(String[] args) {
        String hostname = "localhost";
        int port = 5000;

        try (Socket socket = new Socket(hostname, port)) {

            // Create a new thread to read messages from the server
            new ReadThread(socket).start();

            // Get the output stream to send messages to the server
            OutputStream output = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(output, true);

            // Read user input and send to the server
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String userMessage;

            System.out.println("Enter your messages (type 'exit' to quit): ");
            while ((userMessage = reader.readLine()) != null) {
                if (userMessage.equalsIgnoreCase("exit")) {
                    break;
                }
                writer.println(userMessage);
            }

        } catch (IOException ex) {
            System.out.println("Client error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    // Thread to read messages from the server
    private static class ReadThread extends Thread {
        private Socket socket;

        public ReadThread(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                InputStream input = socket.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));

                String serverMessage;
                while ((serverMessage = reader.readLine()) != null) {
                    System.out.println("Received from server: " + serverMessage);
                }
            } catch (IOException ex) {
                System.out.println("Error reading from server: " + ex.getMessage());
            }
        }
    }
}
