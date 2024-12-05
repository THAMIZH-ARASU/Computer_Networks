import java.io.*;
import java.net.*;

public class FileClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 5000;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             DataInputStream dis = new DataInputStream(socket.getInputStream());
             DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
             BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {

            System.out.println("Connected to the server.");

            System.out.print("Enter command (upload/download): ");
            String command = br.readLine();
            dos.writeUTF(command);

            System.out.print("Enter file name: ");
            String fileName = br.readLine();
            dos.writeUTF(fileName);

            if (command.equalsIgnoreCase("upload")) {
                sendFile(fileName, dos);
            } else if (command.equalsIgnoreCase("download")) {
                receiveFile(fileName, dis);
            } else {
                System.out.println("Invalid command.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void sendFile(String fileName, DataOutputStream dos) throws IOException {
        File file = new File("client_files/" + fileName);
        if (file.exists()) {
            dos.writeLong(file.length());
            try (FileInputStream fis = new FileInputStream(file)) {
                byte[] buffer = new byte[4096];
                int read;
                while ((read = fis.read(buffer)) != -1) {
                    dos.write(buffer, 0, read);
                }
            }
            System.out.println("File " + fileName + " uploaded successfully.");
        } else {
            System.out.println("File " + fileName + " not found.");
        }
    }

    private static void receiveFile(String fileName, DataInputStream dis) throws IOException {
        String status = dis.readUTF();
        if (status.equals("OK")) {
            File file = new File("client_files/" + fileName);
            file.getParentFile().mkdirs();
            try (FileOutputStream fos = new FileOutputStream(file)) {
                long fileSize = dis.readLong();
                byte[] buffer = new byte[4096];
                int read;
                while (fileSize > 0 && (read = dis.read(buffer, 0, (int) Math.min(buffer.length, fileSize))) != -1) {
                    fos.write(buffer, 0, read);
                    fileSize -= read;
                }
                System.out.println("File " + fileName + " downloaded successfully.");
            }
        } else {
            System.out.println("Server: " + status);
        }
    }
}
