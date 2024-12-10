import java.io.*;
import java.net.*;

public class Sender {
    public static String encrypt(String message, int key) {
        StringBuilder encryptedMessage = new StringBuilder();
        for (char c : message.toCharArray()) {
            if (Character.isLetter(c)) {
                char encryptedChar = (char) (c - 'A' + key);
                encryptedMessage.append(encryptedChar);
            } else {
                encryptedMessage.append(c); // Keep non-alphabetic characters as they are
            }
        }
        return encryptedMessage.toString();
    }

    public static void sendKeyToReceiver(int key, String fileName) throws IOException {
        FileWriter writer = new FileWriter(fileName);
        writer.write(key);
        writer.close();

        // Send the key file
        Socket socket = new Socket("localhost", 12345);
        File file = new File(fileName);
        byte[] fileBytes = new byte[(int) file.length()];
        FileInputStream fileInputStream = new FileInputStream(file);
        fileInputStream.read(fileBytes);
        fileInputStream.close();

        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        out.writeInt(fileBytes.length); // Send file size
        out.write(fileBytes); // Send file content
        out.flush();

        socket.close();
    }

    public static void main(String[] args) {
        try {
            String message = "INFORMATION TECHNOLOGY";
            int key = 4; // Key size is 4
            String encryptedMessage = encrypt(message, key);

            System.out.println("Encrypted Message: " + encryptedMessage);

            // Establish TCP connection with the receiver
            Socket socket = new Socket("localhost", 12345);
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());

            // Send the encrypted message
            out.writeUTF(encryptedMessage);
            out.flush();

            // Send the key file
            sendKeyToReceiver(key, "Key.txt");

            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
