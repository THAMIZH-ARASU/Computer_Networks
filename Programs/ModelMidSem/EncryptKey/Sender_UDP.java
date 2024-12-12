import java.io.*;
import java.net.*;

public class Sender_UDP {
    public static String encrypt(String message, int key) {
        StringBuilder encryptedMessage = new StringBuilder();
        for (char c : message.toCharArray()) {
            if (Character.isLetter(c)) {
                char base = Character.isUpperCase(c) ? 'A' : 'a'; // Determine if it's uppercase or lowercase
                char encryptedChar = (char) ((c - base + key) % 26 + base);
                encryptedMessage.append(encryptedChar);
            } else {
                encryptedMessage.append(c); // Keep non-alphabetic characters as they are
            }
        }
        return encryptedMessage.toString();
    }

    public static void sendKeyFile(String fileName, DatagramSocket socket, InetAddress receiverAddress, int port) throws IOException {
        File file = new File(fileName);
        byte[] fileBytes = new byte[(int) file.length()];

        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            fileInputStream.read(fileBytes);
        }

        DatagramPacket packet = new DatagramPacket(fileBytes, fileBytes.length, receiverAddress, port);
        socket.send(packet);
        System.out.println("Key file sent successfully!");
    }

    public static void main(String[] args) {
        try {
            String message = "INFORMATION TECHNOLOGY";
            int key = 4; // Key size is 4
            String encryptedMessage = encrypt(message, key);

            System.out.println("Encrypted Message: " + encryptedMessage);

            // Write the key to a file
            String keyFileName = "Key";
            try (FileWriter writer = new FileWriter(keyFileName)) {
                writer.write(String.valueOf(key));
            }

            InetAddress receiverAddress = InetAddress.getByName("localhost");
            DatagramSocket socket = new DatagramSocket();

            // Send the encrypted message
            byte[] messageBytes = encryptedMessage.getBytes();
            DatagramPacket messagePacket = new DatagramPacket(messageBytes, messageBytes.length, receiverAddress, 12345);
            socket.send(messagePacket);
            System.out.println("Encrypted message sent!");

            // Send the key file
            sendKeyFile(keyFileName, socket, receiverAddress, 12346);

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
