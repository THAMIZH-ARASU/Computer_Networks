import java.io.*;
import java.net.*;

public class LineCountServer {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(4080)) {
            System.out.println("Server is running and waiting for clients...");

            while (true) {
                // Accept a client connection
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());

                // Create input/output streams for communication
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

                // Read the file name from the client
                String fileName = in.readLine();
                System.out.println("Requested file: " + fileName);

                // Construct the file path
                File file = new File("C:\\" + fileName);

                if (file.exists() && file.isFile()) {
                    // Count the number of lines in the file
                    try (BufferedReader fileReader = new BufferedReader(new FileReader(file))) {
                        int lineCount = 0;
                        while (fileReader.readLine() != null) {
                            lineCount++;
                        }
                        // Send the line count to the client
                        out.println("File: " + fileName + " has " + lineCount + " lines.");
                    } catch (IOException e) {
                        out.println("Error reading the file: " + e.getMessage());
                    }
                } else {
                    // File not found or invalid
                    out.println("Error: File does not exist or is not a valid file.");
                }

                // Close the client connection
                clientSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
