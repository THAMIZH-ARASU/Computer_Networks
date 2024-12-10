import java.io.*;
import java.net.*;

public class Receiver {
    public static String decrypt(String encryptedMessage, int key) {
        StringBuilder decryptedMessage = new StringBuilder();
        for (char c : encryptedMessage.toCharArray()) {
            if (Character.isLetter(c)) {
                char decryptedChar = (char) (c - 'A' - key);
                decryptedMessage.append(decryptedChar);
            } else {
                decryptedMessage.append(c); // Keep non-alphabetic characters as they are
            }
        }
        return decryptedMessage.toString();
    }

    public static void receiveKeyFile(Socket socket) throws IOException {
        DataInputStream in = new DataInputStream(socket.getInputStream());

        // Read the file size
        int fileSize = in.readInt();
        byte[] fileBytes = new byte[fileSize];

        // Read the content of the key file
        in.readFully(fileBytes);

        // Write the received file content to a "Key" file
        FileOutputStream fileOutputStream = new FileOutputStream("Key");
        fileOutputStream.write(fileBytes);
        fileOutputStream.close();

        System.out.println("Received Key File: " + new String(fileBytes));
    }

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(12345);
            System.out.println("Receiver is waiting for the connection...");
            Socket socket = serverSocket.accept();

            // Receive the encrypted message
            DataInputStream in = new DataInputStream(socket.getInputStream());
            String encryptedMessage = in.readUTF();
            System.out.println("Received Encrypted Message: " + encryptedMessage);

            // Receive the key file
            receiveKeyFile(socket);

            // Read the key from the file
            BufferedReader keyReader = new BufferedReader(new FileReader("Key"));
            int key = Integer.parseInt(keyReader.readLine());
            keyReader.close();

            // Decrypt the message
            System.out.println("Decrypted Message: " + decrypt(encryptedMessage, key));

            socket.close();
            serverSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
