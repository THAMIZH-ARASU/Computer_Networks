import java.io.*;
import java.net.*;

public class FileServer {
    private static final int PORT = 5000;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started. Waiting for connections...");

            while (true) {
                try (Socket clientSocket = serverSocket.accept();
                     DataInputStream dis = new DataInputStream(clientSocket.getInputStream());
                     DataOutputStream dos = new DataOutputStream(clientSocket.getOutputStream())) {

                    System.out.println("Client connected: " + clientSocket.getInetAddress());

                    String command = dis.readUTF();
                    String fileName = dis.readUTF();

                    if (command.equalsIgnoreCase("upload")) {
                        receiveFile(fileName, dis);
                    } else if (command.equalsIgnoreCase("download")) {
                        sendFile(fileName, dos);
                    } else {
                        System.out.println("Invalid command.");
                    }
                } catch (IOException e) {
                    System.err.println("Error handling client: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void receiveFile(String fileName, DataInputStream dis) throws IOException {
        File file = new File("server_files/" + fileName);
        file.getParentFile().mkdirs();
        try (FileOutputStream fos = new FileOutputStream(file)) {
            long fileSize = dis.readLong();
            byte[] buffer = new byte[4096];
            int read;
            while (fileSize > 0 && (read = dis.read(buffer, 0, (int) Math.min(buffer.length, fileSize))) != -1) {
                fos.write(buffer, 0, read);
                fileSize -= read;
            }
            System.out.println("File " + fileName + " uploaded successfully.");
        }
    }

    private static void sendFile(String fileName, DataOutputStream dos) throws IOException {
        File file = new File("server_files/" + fileName);
        if (file.exists()) {
            dos.writeUTF("OK");
            dos.writeLong(file.length());
            try (FileInputStream fis = new FileInputStream(file)) {
                byte[] buffer = new byte[4096];
                int read;
                while ((read = fis.read(buffer)) != -1) {
                    dos.write(buffer, 0, read);
                }
            }
            System.out.println("File " + fileName + " downloaded successfully.");
        } else {
            dos.writeUTF("File not found");
            System.out.println("Requested file " + fileName + " not found.");
        }
    }
}
