import java.io.*;
import java.net.*;

public class Receiver_UDP {
    public static String decrypt(String encryptedMessage, int key) {
        StringBuilder decryptedMessage = new StringBuilder();
        for (char c : encryptedMessage.toCharArray()) {
            if (Character.isLetter(c)) {
                char base = Character.isUpperCase(c) ? 'A' : 'a'; // Determine if it's uppercase or lowercase
                char decryptedChar = (char) ((c - base - key + 26) % 26 + base);
                decryptedMessage.append(decryptedChar);
            } else {
                decryptedMessage.append(c); // Keep non-alphabetic characters as they are
            }
        }
        return decryptedMessage.toString();
    }

    public static int receiveKeyFile(String fileName, DatagramSocket socket) throws IOException {
        byte[] buffer = new byte[1024];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

        // Receive the key file
        socket.receive(packet);

        try (FileOutputStream fileOutputStream = new FileOutputStream(fileName)) {
            fileOutputStream.write(packet.getData(), 0, packet.getLength());
        }

        System.out.println("Key file received successfully!");

        // Read and return the key from the file
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            return Integer.parseInt(reader.readLine());
        }
    }

    public static void main(String[] args) {
        try {
            DatagramSocket messageSocket = new DatagramSocket(12345);
            DatagramSocket keySocket = new DatagramSocket(12346);

            // Receive the encrypted message
            byte[] messageBuffer = new byte[1024];
            DatagramPacket messagePacket = new DatagramPacket(messageBuffer, messageBuffer.length);
            System.out.println("Receiver is waiting for the encrypted message...");
            messageSocket.receive(messagePacket);

            String encryptedMessage = new String(messagePacket.getData(), 0, messagePacket.getLength());
            System.out.println("Received Encrypted Message: " + encryptedMessage);

            // Receive the key file
            String keyFileName = "Key";
            int key = receiveKeyFile(keyFileName, keySocket);

            // Decrypt the message
            System.out.println("Decrypted Message: " + decrypt(encryptedMessage, key));

            messageSocket.close();
            keySocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
