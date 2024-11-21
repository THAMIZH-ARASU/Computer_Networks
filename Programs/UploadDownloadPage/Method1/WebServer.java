import java.io.*;
import java.net.*;

public class WebServer {
    private static final int PORT = 8080;
    private static final String UPLOAD_DIR = "uploads/";

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is running on port " + PORT);
            
            // Ensure the upload directory exists
            new File(UPLOAD_DIR).mkdirs();

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());
                
                new Thread(() -> handleClient(clientSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket clientSocket) {
        try (
            InputStream input = clientSocket.getInputStream();
            OutputStream output = clientSocket.getOutputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            PrintWriter writer = new PrintWriter(output, true)
        ) {
            writer.println("Connected to the Web Server. Type 'UPLOAD <filename>' or 'DOWNLOAD <filename>'.");

            String command = reader.readLine();
            if (command == null) return;

            if (command.startsWith("UPLOAD")) {
                String fileName = command.split(" ", 2)[1];
                receiveFile(fileName, input);
                writer.println("File uploaded successfully.");
            } else if (command.startsWith("DOWNLOAD")) {
                String fileName = command.split(" ", 2)[1];
                sendFile(fileName, output, writer);
            } else {
                writer.println("Invalid command.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void receiveFile(String fileName, InputStream input) throws IOException {
        File file = new File(UPLOAD_DIR + fileName);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = input.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }
        }
        System.out.println("File received: " + fileName);
    }

    private static void sendFile(String fileName, OutputStream output, PrintWriter writer) throws IOException {
        File file = new File(UPLOAD_DIR + fileName);
        if (!file.exists()) {
            writer.println("File not found.");
            return;
        }

        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }
        }
        System.out.println("File sent: " + fileName);
    }
}
