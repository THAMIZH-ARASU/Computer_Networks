import java.io.*;
import java.net.*;
import java.util.Scanner;

public class WebClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 8080;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
            System.out.println("Connected to the server.");

            InputStream input = socket.getInputStream();
            OutputStream output = socket.getOutputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            PrintWriter writer = new PrintWriter(output, true);

            // Display server welcome message
            System.out.println(reader.readLine());

            try (Scanner scanner = new Scanner(System.in)) {
                while (true) {
                    System.out.print("Enter command (UPLOAD <filename> or DOWNLOAD <filename>): ");
                    String command = scanner.nextLine();
                    writer.println(command);

                    if (command.startsWith("UPLOAD")) {
                        String fileName = command.split(" ", 2)[1];
                        sendFile(fileName, output);
                        System.out.println(reader.readLine());
                    } else if (command.startsWith("DOWNLOAD")) {
                        String fileName = command.split(" ", 2)[1];
                        receiveFile(fileName, input);
                    } else {
                        System.out.println(reader.readLine());
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void sendFile(String fileName, OutputStream output) throws IOException {
        File file = new File(fileName);
        if (!file.exists()) {
            System.out.println("File not found: " + fileName);
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

    private static void receiveFile(String fileName, InputStream input) throws IOException {
        File file = new File(fileName);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = input.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }
        }
        System.out.println("File downloaded: " + fileName);
    }
}
