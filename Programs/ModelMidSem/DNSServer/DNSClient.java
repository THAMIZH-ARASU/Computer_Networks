import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

public class DNSClient {
    private static final String SERVER_ADDRESS = "localhost"; // Change to your server's address
    private static final int SERVER_PORT = 1053;

    public static void main(String[] args) throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter the domain name :");
        String domainName = br.readLine();

        try (DatagramSocket socket = new DatagramSocket()) {
            byte[] buffer = domainName.getBytes();
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, InetAddress.getByName(SERVER_ADDRESS), SERVER_PORT);
            socket.send(packet);
            System.out.println("Sent request for: " + domainName);

            byte[] responseBuffer = new byte[512];
            DatagramPacket responsePacket = new DatagramPacket(responseBuffer, responseBuffer.length);
            socket.receive(responsePacket);
            String ipAddress = new String(responsePacket.getData(), 0, responsePacket.getLength());
            System.out.println("Received response: " + ipAddress);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
