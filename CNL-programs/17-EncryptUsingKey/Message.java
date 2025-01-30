import java.util.Scanner;

public class Message {
    public static String encryptMessage(String message, int key) {
        StringBuilder encryptedMessage = new StringBuilder();
        key = key % 26; // Normalize key
        for (char c : message.toCharArray()) {
            if (Character.isLetter(c)) {
                char base = Character.isUpperCase(c) ? 'A' : 'a';
                char encryptedChar = (char) (((c - base + key) % 26) + base);
                encryptedMessage.append(encryptedChar);
            } else {
                encryptedMessage.append(c);
            }
        }
        return encryptedMessage.toString();
    }

    public static String decryptMessage(String encryptedMessage, int key) {
        StringBuilder decryptedMessage = new StringBuilder();
        key = key % 26; // Normalize key
        for (char c : encryptedMessage.toCharArray()) {
            if (Character.isLetter(c)) {
                char base = Character.isUpperCase(c) ? 'A' : 'a';
                char decryptedChar = (char) (((c - base - key + 26) % 26) + base);
                decryptedMessage.append(decryptedChar);
            } else {
                decryptedMessage.append(c);
            }
        }
        return decryptedMessage.toString();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your message: ");
        String message = scanner.nextLine();
        System.out.println("Enter the key: ");
        int key = scanner.nextInt();

        String encrypted = encryptMessage(message, key);
        System.out.println("Encrypted message: " + encrypted);

        String decrypted = decryptMessage(encrypted, key);
        System.out.println("Decrypted message: " + decrypted);

        scanner.close();
    }
}
